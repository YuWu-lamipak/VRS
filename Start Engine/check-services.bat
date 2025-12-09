@echo off
chcp 65001 >nul
echo ========================================
echo 服务状态检查工具
echo ========================================
echo.

echo [MySQL 检查]
echo 检查端口 3306...
netstat -ano | findstr ":3306"
if errorlevel 1 (
    echo 未找到 3306 端口监听
    echo.
    echo 检查 MySQL 服务状态...
    sc query MySQL
    sc query MySQL80
    sc query MySQL57
) else (
    echo MySQL 正在运行
)
echo.

echo [Redis 检查]
echo 检查端口 6379...
netstat -ano | findstr ":6379"
if errorlevel 1 (
    echo 未找到 6379 端口监听
) else (
    echo Redis 正在运行
)
echo.

echo [Nginx 检查]
echo 检查端口 80...
netstat -ano | findstr ":80"
if errorlevel 1 (
    echo 未找到 80 端口监听
) else (
    echo Nginx 正在运行
)
echo.

echo [Java 服务检查]
echo 检查端口 8066 (lemei-admin)...
netstat -ano | findstr ":8066"
if errorlevel 1 (
    echo 未找到 8066 端口监听
) else (
    echo lemei-admin 正在运行
)
echo.

echo 检查端口 8601 (lemei-api)...
netstat -ano | findstr ":8601"
if errorlevel 1 (
    echo 未找到 8601 端口监听
) else (
    echo lemei-api 正在运行
)
echo.

echo ========================================
echo 检查完成
echo ========================================
pause
