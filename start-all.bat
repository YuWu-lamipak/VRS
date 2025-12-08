@echo off
chcp 65001 >nul
echo ========================================
echo 乐美车辆预约系统 - 一键启动所有服务
echo ========================================
echo.

REM 设置路径
set "NGINX_DIR=D:\nginx\nginx-1.20.2"
set "REDIS_DIR=D:\Program Files\Redis"
set "LM_DIR=%USERPROFILE%\Desktop\lm"

echo [步骤 1/4] 检查文件是否存在...
echo.

REM 检查 Nginx
if not exist "%NGINX_DIR%\nginx.exe" (
    echo [错误] 找不到 nginx.exe: %NGINX_DIR%\nginx.exe
    pause
    exit /b 1
)

REM 检查 Redis
if not exist "%REDIS_DIR%\redis-server.exe" (
    echo [错误] 找不到 redis-server.exe: %REDIS_DIR%\redis-server.exe
    pause
    exit /b 1
)

REM 检查 Java 服务
if not exist "%LM_DIR%\lemei-admin\lemei-admin.jar" (
    echo [错误] 找不到 lemei-admin.jar: %LM_DIR%\lemei-admin\lemei-admin.jar
    pause
    exit /b 1
)

if not exist "%LM_DIR%\lemei-api\lemei-api.jar" (
    echo [错误] 找不到 lemei-api.jar: %LM_DIR%\lemei-api\lemei-api.jar
    pause
    exit /b 1
)

echo [成功] 所有文件检查通过
echo.

echo [步骤 2/4] 检查依赖服务...
echo.
echo [提示] 请确保以下服务已启动:
echo   - MySQL (端口 3306)
echo   - 如需检查服务状态，可运行 check-services.bat
echo.

echo [步骤 3/4] 启动基础服务 (Redis + Nginx)...
echo.

REM 检查并启动 Redis
netstat -ano | findstr "LISTENING" | findstr ":6379" >nul 2>&1 && (
    echo [跳过] Redis 已在运行 (端口 6379)
) || (
    echo [启动] 正在启动 Redis 服务...
    cd /d "%REDIS_DIR%"
    start "Redis服务" cmd /k "redis-server.exe"
    timeout /t 3 /nobreak >nul
    echo [成功] Redis 服务已启动
)
echo.

REM 检查并启动 Nginx
netstat -ano | findstr "LISTENING" | findstr ":80" >nul 2>&1 && (
    echo [跳过] Nginx 已在运行 (端口 80)
) || (
    echo [启动] 正在启动 Nginx 服务...
    cd /d "%NGINX_DIR%"
    start /b nginx.exe
    timeout /t 2 /nobreak >nul
    echo [成功] Nginx 服务已启动 (后台运行)
)
echo.

echo [步骤 4/4] 启动 Java 后端服务...
echo.

REM 检查并启动 lemei-admin
netstat -ano | findstr "LISTENING" | findstr ":8066" >nul 2>&1 && (
    echo [跳过] lemei-admin 已在运行 (端口 8066)
) || (
    echo [启动] 正在启动后端管理服务 (端口 8066)...
    cd /d "%LM_DIR%\lemei-admin"
    start "乐美后端管理服务" cmd /k "java -jar lemei-admin.jar"
    timeout /t 3 /nobreak >nul
    echo [成功] 后端管理服务已启动
)
echo.

REM 检查并启动 lemei-api
netstat -ano | findstr "LISTENING" | findstr ":8601" >nul 2>&1 && (
    echo [跳过] lemei-api 已在运行 (端口 8601)
) || (
    echo [启动] 正在启动 API 服务 (端口 8601)...
    cd /d "%LM_DIR%\lemei-api"
    start "乐美API服务" cmd /k "java -jar lemei-api.jar"
    timeout /t 3 /nobreak >nul
    echo [成功] API 服务已启动
)
echo.

echo ========================================
echo 所有服务启动完成！
echo ========================================
echo.
echo 服务地址:
echo - 前端管理: http://localhost:80 (Nginx)
echo - 后端管理: http://localhost:8066
echo - API 服务: http://localhost:8601
echo.
echo 依赖服务:
echo - MySQL: 端口 3306 ✓
echo - Redis: 端口 6379 ✓
echo.
echo 提示:
echo - 等待 10-20 秒让 Java 服务完全启动
echo - 每个服务在独立窗口中运行
echo - 关闭窗口即可停止对应服务
echo - Nginx 在后台运行，需执行 nginx.exe -s stop 停止
echo.
echo ========================================
pause
