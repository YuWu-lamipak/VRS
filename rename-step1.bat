@echo off
chcp 65001 >nul
echo ============================================================================
echo   第一步：重命名目录结构
echo ============================================================================

echo.
echo 重命名 Maven 模块目录...
if exist "VRS\lemei\lemei-admin" ren "VRS\lemei\lemei-admin" "vrs-admin"
if exist "VRS\lemei\lemei-api" ren "VRS\lemei\lemei-api" "vrs-api"
if exist "VRS\lemei\lemei-common" ren "VRS\lemei\lemei-common" "vrs-common"
if exist "VRS\lemei\lemei-framework" ren "VRS\lemei\lemei-framework" "vrs-framework"
if exist "VRS\lemei\lemei-generator" ren "VRS\lemei\lemei-generator" "vrs-generator"
if exist "VRS\lemei\lemei-quartz" ren "VRS\lemei\lemei-quartz" "vrs-quartz"
if exist "VRS\lemei\lemei-system" ren "VRS\lemei\lemei-system" "vrs-system"

echo.
echo 重命名主目录...
if exist "VRS\lemei" ren "VRS\lemei" "vrs"

echo.
echo 重命名 UI 目录...
if exist "VRS\lemei-ui" ren "VRS\lemei-ui" "vrs-ui"

echo.
echo ✓ 目录重命名完成
pause