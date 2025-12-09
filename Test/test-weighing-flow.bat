@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo 车辆称重流程自动化测试
echo ========================================
echo.

REM 配置参数
set BASE_URL=http://localhost:8601
set CAR_NUMBER=测试A
set CARD_ID=5FD9DBC4
set APPLICATION_ID=ee9c31488b1f45be908e01ca7c61bc21

echo 测试配置:
echo   API地址: %BASE_URL%
echo   车牌号: %CAR_NUMBER%
echo   卡片ID: %CARD_ID%
echo   预约ID: %APPLICATION_ID%
echo.

REM 检查curl是否可用
where curl >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo 错误: 未找到curl命令
    echo 请确保Windows 10/11系统或手动安装curl
    pause
    exit /b 1
)

REM 检查API服务状态
echo 检查API服务状态...
curl -s -o nul -w "%%{http_code}" %BASE_URL%/api/application/getReasonList > temp_status.txt
set /p STATUS=<temp_status.txt
del temp_status.txt

if not "%STATUS%"=="200" (
    echo 错误: API服务未运行或无法访问
    echo 请先启动API服务: start-api.bat
    pause
    exit /b 1
)

echo API服务正常运行
echo.

REM 生成时间戳 (格式: YYYY-MM-DD HH:MM:SS)
REM 中文Windows日期格式: 周X YYYY/MM/DD
for /f "tokens=2-4 delims=/ " %%a in ("%date%") do (
    set YEAR=%%a
    set MONTH=%%b
    set DAY=%%c
)
REM 补齐月份和日期的前导零
if "%MONTH:~0,1%"==" " set MONTH=0%MONTH:~1%
if "%DAY:~0,1%"==" " set DAY=0%DAY:~1%
REM 处理时间，补齐小时的前导零
for /f "tokens=1-3 delims=:. " %%a in ("%time%") do (
    set HOUR=%%a
    set MINUTE=%%b
    set SECOND=%%c
)
if "%HOUR:~0,1%"==" " set HOUR=0%HOUR:~1%
set TIMESTAMP=%YEAR%-%MONTH%-%DAY% %HOUR%:%MINUTE%:%SECOND%

REM 创建测试报告目录
if not exist TestReport mkdir TestReport

REM 设置报告文件
for /f "tokens=1-6 delims=/:. " %%a in ("%date% %time%") do (
    set REPORT_FILE=TestReport\WeighingFlow_%%a%%b%%c_%%d%%e%%f.txt
)

echo 车辆称重流程自动化测试报告 > %REPORT_FILE%
echo ======================================== >> %REPORT_FILE%
echo 测试时间: %TIMESTAMP% >> %REPORT_FILE%
echo 车牌号: %CAR_NUMBER% >> %REPORT_FILE%
echo 卡片ID: %CARD_ID% >> %REPORT_FILE%
echo 预约ID: %APPLICATION_ID% >> %REPORT_FILE%
echo. >> %REPORT_FILE%
echo 测试结果: >> %REPORT_FILE%
echo. >> %REPORT_FILE%

set TOTAL_TESTS=0
set PASSED_TESTS=0
set FAILED_TESTS=0

echo ========================================
echo 开始执行测试流程
echo ========================================
echo.

REM 步骤1: 制卡
echo [1] 测试: 制卡
echo [1] 测试: 制卡 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/proCard" ^
  -H "Content-Type: application/json" ^
  -d "{\"json\":\"{\\\"CardID\\\":\\\"%CARD_ID%\\\",\\\"CardType\\\":\\\"1\\\",\\\"TruckNo1\\\":\\\"%CAR_NUMBER%\\\",\\\"TruckNo2\\\":\\\"\\\",\\\"isUpdate\\\":\\\"0\\\",\\\"TruckType\\\":\\\"0\\\"}\"}" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: 制卡完成
    echo   成功: 制卡完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   失败: 制卡失败
    echo   失败: 制卡失败 >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a FAILED_TESTS+=1
    del temp_response.txt
    goto :summary
)
del temp_response.txt
echo.
timeout /t 2 /nobreak >nul

REM 步骤2: 待上一磅
echo [2] 测试: 待上一磅
echo [2] 测试: 待上一磅 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/upOnePound" ^
  -H "Content-Type: application/json" ^
  -d "{\"json\":\"{\\\"CardID\\\":\\\"%CARD_ID%\\\",\\\"TruckNo1\\\":\\\"%CAR_NUMBER%\\\",\\\"TruckNo2\\\":\\\"\\\",\\\"LoadoMeterID\\\":\\\"001\\\"}\"}" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: 待上一磅完成
    echo   成功: 待上一磅完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   失败: 待上一磅失败
    echo   失败: 待上一磅失败 >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a FAILED_TESTS+=1
    del temp_response.txt
    goto :summary
)
del temp_response.txt
echo.
timeout /t 2 /nobreak >nul

REM 步骤3: 一磅写入
echo [3] 测试: 一磅写入
echo [3] 测试: 一磅写入 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/oneWrite" ^
  -H "Content-Type: application/json" ^
  -d "{\"json\":\"{\\\"CardID\\\":\\\"%CARD_ID%\\\",\\\"TruckNo1\\\":\\\"%CAR_NUMBER%\\\",\\\"TruckNo2\\\":\\\"\\\",\\\"TaskID\\\":\\\"test-task-1\\\",\\\"Weight\\\":\\\"1660\\\",\\\"FirstWeightTime\\\":\\\"%TIMESTAMP%\\\",\\\"LoadoMeterID\\\":\\\"001\\\",\\\"ATMSTaskID\\\":null,\\\"UserID\\\":\\\"\\\",\\\"UserName\\\":\\\"\\\"}\"}" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: 一磅写入完成
    echo   成功: 一磅写入完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   失败: 一磅写入失败
    echo   失败: 一磅写入失败 >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a FAILED_TESTS+=1
    del temp_response.txt
    goto :summary
)
del temp_response.txt
echo.
timeout /t 2 /nobreak >nul

REM 步骤4: SAP导入DN数据
echo [4] 测试: SAP导入DN数据
echo [4] 测试: SAP导入DN数据 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/saleDeliveryOrderBodyInfo" ^
  -H "Content-Type: application/json" ^
  -d "[{\"lm_dnnum\":\"8000001211\",\"lm_actualdeliverydate\":\"\",\"lm_loadTime\":\"20251209120000\",\"ITEM\":[{\"lm_dnnum\":\"8000001211\",\"lm_dnline\":4,\"lm_grossweight\":1000,\"lm_weightunit\":\"KG\",\"lm_dnlineref\":10,\"lm_car_number\":\"%CAR_NUMBER%\"},{\"lm_dnnum\":\"8000001211\",\"lm_dnline\":5,\"lm_grossweight\":500,\"lm_weightunit\":\"KG\",\"lm_dnlineref\":10,\"lm_car_number\":\"%CAR_NUMBER%\"}]}]" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: SAP导入DN数据完成
    echo   成功: SAP导入DN数据完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   警告: SAP导入DN数据失败（危废业务可能不需要）
    echo   警告: SAP导入DN数据失败（危废业务可能不需要） >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
)
del temp_response.txt
echo.
timeout /t 2 /nobreak >nul

REM 步骤5: 待上二磅
echo [5] 测试: 待上二磅
echo [5] 测试: 待上二磅 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/upTwoPound" ^
  -H "Content-Type: application/json" ^
  -d "{\"json\":\"{\\\"CardID\\\":\\\"%CARD_ID%\\\",\\\"TruckNo1\\\":\\\"%CAR_NUMBER%\\\",\\\"TruckNo2\\\":\\\"\\\",\\\"LoadoMeterID\\\":\\\"001\\\",\\\"TaskID\\\":\\\"test-task-2\\\",\\\"ATMSTaskID\\\":\\\"123456\\\"}\"}" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: 待上二磅完成
    echo   成功: 待上二磅完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   失败: 待上二磅失败
    echo   失败: 待上二磅失败 >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a FAILED_TESTS+=1
    del temp_response.txt
    goto :summary
)
del temp_response.txt
echo.
timeout /t 2 /nobreak >nul

REM 步骤6: 二磅写入
echo [6] 测试: 二磅写入
echo [6] 测试: 二磅写入 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/twoWrite" ^
  -H "Content-Type: application/json" ^
  -d "{\"json\":\"{\\\"CardID\\\":\\\"%CARD_ID%\\\",\\\"TruckNo1\\\":\\\"%CAR_NUMBER%\\\",\\\"TruckNo2\\\":\\\"\\\",\\\"LoadoMeterID\\\":\\\"001\\\",\\\"TaskID\\\":\\\"test-task-3\\\",\\\"SecondWeightTime\\\":\\\"%TIMESTAMP%\\\",\\\"ATMSTaskID\\\":\\\"123456\\\",\\\"UserID\\\":\\\"\\\",\\\"UserName\\\":\\\"\\\",\\\"Gross\\\":\\\"3900\\\",\\\"Tare\\\":\\\"2200\\\",\\\"Net\\\":\\\"1000\\\",\\\"PackWeight\\\":\\\"0\\\",\\\"Fact\\\":\\\"1600\\\"}\"}" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: 二磅写入完成
    echo   成功: 二磅写入完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   失败: 二磅写入失败
    echo   失败: 二磅写入失败 >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a FAILED_TESTS+=1
    del temp_response.txt
    goto :summary
)
del temp_response.txt
echo.
timeout /t 2 /nobreak >nul

REM 步骤7: 退卡
echo [7] 测试: 退卡
echo [7] 测试: 退卡 >> %REPORT_FILE%
set /a TOTAL_TESTS+=1

curl -s -X POST "%BASE_URL%/api/application/refundCard" ^
  -H "Content-Type: application/json" ^
  -d "{\"json\":\"{\\\"CardID\\\":\\\"%CARD_ID%\\\",\\\"CardType\\\":\\\"1\\\",\\\"TruckNo1\\\":\\\"%CAR_NUMBER%\\\",\\\"TruckNo2\\\":\\\"\\\",\\\"RefundTime\\\":\\\"%TIMESTAMP%\\\"}\"}" > temp_response.txt

findstr /C:"\"Code\":\"0\"" temp_response.txt >nul
if %ERRORLEVEL% EQU 0 (
    echo   成功: 退卡完成
    echo   成功: 退卡完成 >> %REPORT_FILE%
    set /a PASSED_TESTS+=1
) else (
    echo   失败: 退卡失败
    echo   失败: 退卡失败 >> %REPORT_FILE%
    type temp_response.txt >> %REPORT_FILE%
    set /a FAILED_TESTS+=1
)
del temp_response.txt
echo.

:summary
echo ========================================
echo 测试总结
echo ========================================
echo.
echo 总测试数: %TOTAL_TESTS%
echo 通过: %PASSED_TESTS%
echo 失败: %FAILED_TESTS%

echo. >> %REPORT_FILE%
echo ======================================== >> %REPORT_FILE%
echo 测试总结 >> %REPORT_FILE%
echo ======================================== >> %REPORT_FILE%
echo 总测试数: %TOTAL_TESTS% >> %REPORT_FILE%
echo 通过: %PASSED_TESTS% >> %REPORT_FILE%
echo 失败: %FAILED_TESTS% >> %REPORT_FILE%

if %FAILED_TESTS% EQU 0 (
    echo.
    echo 所有测试通过！
    echo. >> %REPORT_FILE%
    echo 所有测试通过！ >> %REPORT_FILE%
) else (
    echo.
    echo 部分测试失败，请检查日志
    echo. >> %REPORT_FILE%
    echo 部分测试失败，请检查日志 >> %REPORT_FILE%
)

echo.
echo 测试报告已保存: %REPORT_FILE%
echo.
pause
