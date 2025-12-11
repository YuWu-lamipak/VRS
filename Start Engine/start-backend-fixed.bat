@echo off
chcp 65001 >nul
echo ========================================
echo 启动后端管理服务 (lemei-admin)
echo ========================================

set "JAVA_HOME=C:\Program Files\Java\jdk-1.8"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo 正在切换到项目目录...
cd /d "%~dp0..\VRS\lemei"

if not exist "pom.xml" (
    echo [错误] 找不到项目根目录的 pom.xml 文件
    echo 当前目录: %CD%
    pause
    exit /b 1
)

echo 项目目录: %CD%
echo 正在启动后端管理服务 (端口 8066)...
echo.

D:\maven\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run -pl lemei-admin
