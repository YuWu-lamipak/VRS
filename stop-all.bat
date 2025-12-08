@echo off
chcp 65001 >nul
echo ========================================
echo 乐美车辆预约系统 - 一键停止所有服务
echo ========================================
echo.

REM 设置路径
set "NGINX_DIR=D:\nginx\nginx-1.20.2"

echo [步骤 1/4] 停止 Nginx 服务...
echo.
netstat -ano | findstr "LISTENING" | findstr ":80" >nul 2>&1 && (
    echo [停止] 正在停止 Nginx...
    cd /d "%NGINX_DIR%"
    nginx.exe -s stop
    timeout /t 2 /nobreak >nul
    echo [成功] Nginx 已停止
) || (
    echo [跳过] Nginx 未运行
)
echo.

echo [步骤 2/4] 停止 Redis 服务...
echo.
for /f "tokens=5" %%a in ('netstat -ano ^| findstr "LISTENING" ^| findstr ":6379"') do (
    echo [停止] 正在停止 Redis (PID: %%a)...
    taskkill /F /PID %%a >nul 2>&1
    echo [成功] Redis 已停止
    goto :redis_done
)
echo [跳过] Redis 未运行
:redis_done
echo.

echo [步骤 3/4] 停止 lemei-admin 服务...
echo.
for /f "tokens=5" %%a in ('netstat -ano ^| findstr "LISTENING" ^| findstr ":8066"') do (
    echo [停止] 正在停止 lemei-admin (PID: %%a)...
    taskkill /F /PID %%a >nul 2>&1
    echo [成功] lemei-admin 已停止
    goto :admin_done
)
echo [跳过] lemei-admin 未运行
:admin_done
echo.

echo [步骤 4/4] 停止 lemei-api 服务...
echo.
for /f "tokens=5" %%a in ('netstat -ano ^| findstr "LISTENING" ^| findstr ":8601"') do (
    echo [停止] 正在停止 lemei-api (PID: %%a)...
    taskkill /F /PID %%a >nul 2>&1
    echo [成功] lemei-api 已停止
    goto :api_done
)
echo [跳过] lemei-api 未运行
:api_done
echo.

echo ========================================
echo 所有服务已停止！
echo ========================================
echo.
echo 提示:
echo - MySQL 服务未停止（通常保持运行）
echo - 如需停止 MySQL，请使用服务管理器
echo.
echo ========================================
pause
