@echo off
chcp 65001 >nul
echo ========================================
echo 删除开机自启动任务
echo ========================================
echo.

echo [提示] 此操作需要管理员权限
echo [提示] 将删除"乐美车辆预约系统-自启动"任务
echo.
pause

echo.
echo [删除] 正在删除任务计划...
schtasks /delete /tn "乐美车辆预约系统-自启动" /f

if %errorlevel% equ 0 (
    echo.
    echo [成功] 任务已删除
) else (
    echo.
    echo [错误] 任务删除失败或任务不存在
    echo [提示] 请右键以管理员身份运行此脚本
)

echo.
echo ========================================
pause
