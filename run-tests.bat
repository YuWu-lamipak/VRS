@echo off
chcp 65001 >nul
echo ============================================
echo   乐美车辆预约系统 - 自动化测试
echo ============================================
echo.
echo 测试开始时间: %date% %time%
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-1.8
set PATH=%JAVA_HOME%\bin;%PATH%
set MAVEN_CMD=D:\maven\apache-maven-3.8.8\bin\mvn.cmd

echo [步骤 1/4] 检查服务状态...
echo.
netstat -ano | findstr ":8066" >nul
if %errorlevel% equ 0 (
    echo [OK] lemei-admin 服务运行中 ^(端口 8066^)
) else (
    echo [警告] lemei-admin 服务未运行，请先启动: start-backend-fixed.bat
)

netstat -ano | findstr ":8601" >nul
if %errorlevel% equ 0 (
    echo [OK] lemei-api 服务运行中 ^(端口 8601^)
) else (
    echo [警告] lemei-api 服务未运行，请先启动: start-api.bat
)

echo.
echo [步骤 2/4] 运行测试...
echo.
cd VRS\lemei
call %MAVEN_CMD% test

if %errorlevel% neq 0 (
    echo.
    echo [错误] 测试失败，请查看详细日志
    cd ..\..
    pause
    exit /b 1
)

cd ..\..

echo.
echo [步骤 3/4] 复制测试报告...
echo.

REM 备份模板文件
if exist TestReport\SUMMARY_TEMPLATE.md (
    copy /y TestReport\SUMMARY_TEMPLATE.md SUMMARY_TEMPLATE.md.bak >nul
)

if exist TestReport rmdir /s /q TestReport
mkdir TestReport
mkdir TestReport\lemei-system
mkdir TestReport\lemei-api
mkdir TestReport\lemei-admin

REM 恢复模板文件
if exist SUMMARY_TEMPLATE.md.bak (
    copy /y SUMMARY_TEMPLATE.md.bak TestReport\SUMMARY_TEMPLATE.md >nul
    del SUMMARY_TEMPLATE.md.bak
)

if exist VRS\lemei\lemei-system\target\surefire-reports (
    xcopy /y /q VRS\lemei\lemei-system\target\surefire-reports\* TestReport\lemei-system\ >nul
    echo [OK] lemei-system 测试报告已复制
)

if exist VRS\lemei\lemei-api\target\surefire-reports (
    xcopy /y /q VRS\lemei\lemei-api\target\surefire-reports\* TestReport\lemei-api\ >nul
    echo [OK] lemei-api 测试报告已复制
)

if exist VRS\lemei\lemei-admin\target\surefire-reports (
    xcopy /y /q VRS\lemei\lemei-admin\target\surefire-reports\* TestReport\lemei-admin\ >nul
    echo [OK] lemei-admin 测试报告已复制
)

echo.
echo [步骤 4/4] 生成测试摘要...
echo.

REM 使用 PowerShell 生成测试摘要
powershell -ExecutionPolicy Bypass -File generate-summary.ps1

REM 解析测试结果用于显示
set SYSTEM_RESULT=
set API_RESULT=
set ADMIN_RESULT=

for /f "tokens=*" %%a in ('findstr /c:"Tests run:" TestReport\lemei-system\*.txt 2^>nul') do set SYSTEM_RESULT=%%a
for /f "tokens=*" %%a in ('findstr /c:"Tests run:" TestReport\lemei-api\*.txt 2^>nul') do set API_RESULT=%%a
for /f "tokens=*" %%a in ('findstr /c:"Tests run:" TestReport\lemei-admin\*.txt 2^>nul') do set ADMIN_RESULT=%%a

echo.
echo ============================================
echo            测试结果摘要
echo ============================================
echo.

if defined SYSTEM_RESULT echo [PASS] lemei-system: %SYSTEM_RESULT%
if defined API_RESULT echo [PASS] lemei-api: %API_RESULT%
if defined ADMIN_RESULT echo [PASS] lemei-admin: %ADMIN_RESULT%

echo.
echo ============================================
echo 测试报告位置:
echo   - TestReport\TEST_SUMMARY.md
echo   - TestReport\lemei-system\
echo   - TestReport\lemei-api\
echo   - TestReport\lemei-admin\
echo.
echo 测试完成时间: %date% %time%
echo ============================================
echo.
echo [成功] 所有测试已完成！
echo.
pause
