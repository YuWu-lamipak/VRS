@echo off
chcp 65001 >nul
echo ========================================
echo 创建开机自启动任务
echo ========================================
echo.

REM 获取当前脚本所在目录
set "SCRIPT_DIR=%~dp0"
set "START_SCRIPT=%SCRIPT_DIR%start-all.bat"

echo [信息] 脚本路径: %START_SCRIPT%
echo.
echo [提示] 此操作需要管理员权限
echo [提示] 将创建一个开机延迟 2 分钟启动的任务
echo.
pause

echo.
echo [创建] 正在创建任务计划...
schtasks /create /tn "乐美车辆预约系统-自启动" /tr "\"%START_SCRIPT%\"" /sc onstart /delay 0002:00 /rl highest /f

if %errorlevel% equ 0 (
    echo.
    echo [成功] 任务创建成功！
    echo.
    echo 任务详情:
    echo - 任务名称: 乐美车辆预约系统-自启动
    echo - 触发条件: 系统启动时
    echo - 延迟时间: 2 分钟
    echo - 运行权限: 最高权限（管理员）
    echo - 脚本路径: %START_SCRIPT%
    echo.
    echo 管理任务:
    echo - 查看任务: 打开"任务计划程序" 或运行 taskschd.msc
    echo - 删除任务: 运行 delete-startup-task.bat
    echo - 测试任务: 运行 schtasks /run /tn "乐美车辆预约系统-自启动"
) else (
    echo.
    echo [错误] 任务创建失败
    echo [提示] 请右键以管理员身份运行此脚本
)

echo.
echo ========================================
pause
