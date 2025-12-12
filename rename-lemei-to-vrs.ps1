# ============================================================================
# 乐美车辆预约系统重命名脚本：lemei -> VRS
# ============================================================================

$ErrorActionPreference = "Stop"

Write-Host "============================================================================" -ForegroundColor Cyan
Write-Host "  乐美车辆预约系统 -> VRS 重命名脚本" -ForegroundColor Cyan
Write-Host "============================================================================`n" -ForegroundColor Cyan

# 函数：替换文件内容
function Replace-FileContent {
    param(
        [string]$FilePath,
        [hashtable]$Replacements
    )
    
    if (Test-Path $FilePath) {
        $content = Get-Content -Path $FilePath -Raw -Encoding UTF8
        $modified = $false
        
        foreach ($key in $Replacements.Keys) {
            if ($content -match [regex]::Escape($key)) {
                $content = $content -replace [regex]::Escape($key), $Replacements[$key]
                $modified = $true
            }
        }
        
        if ($modified) {
            Set-Content -Path $FilePath -Value $content -Encoding UTF8 -NoNewline
            return $true
        }
    }
    return $false
}

# 函数：批量替换目录下的文件
function Replace-InDirectory {
    param(
        [string]$Path,
        [string[]]$Extensions,
        [hashtable]$Replacements,
        [string]$Description
    )
    
    Write-Host "`n处理: $Description" -ForegroundColor Yellow
    $count = 0
    
    foreach ($ext in $Extensions) {
        $files = Get-ChildItem -Path $Path -Filter "*.$ext" -Recurse -File -ErrorAction SilentlyContinue
        foreach ($file in $files) {
            if (Replace-FileContent -FilePath $file.FullName -Replacements $Replacements) {
                $count++
            }
        }
    }
    
    Write-Host "  修改了 $count 个文件" -ForegroundColor Green
}

# ============================================================================
# 阶段 1: 重命名目录结构
# ============================================================================
Write-Host "[阶段 1/5] 重命名目录结构...`n" -ForegroundColor Cyan

# 1.1 重命名 Maven 模块目录
Write-Host "重命名 Maven 模块..." -ForegroundColor Yellow
$modules = @(
    @{Old="lemei-admin"; New="vrs-admin"},
    @{Old="lemei-api"; New="vrs-api"},
    @{Old="lemei-common"; New="vrs-common"},
    @{Old="lemei-framework"; New="vrs-framework"},
    @{Old="lemei-generator"; New="vrs-generator"},
    @{Old="lemei-quartz"; New="vrs-quartz"},
    @{Old="lemei-system"; New="vrs-system"}
)

foreach ($module in $modules) {
    $oldPath = "VRS/lemei/$($module.Old)"
    if (Test-Path $oldPath) {
        Write-Host "  $($module.Old) -> $($module.New)"
        Rename-Item -Path $oldPath -NewName $module.New -Force
    }
}

# 1.2 重命名主目录
Write-Host "`n重命名主目录..." -ForegroundColor Yellow
if (Test-Path "VRS/lemei") {
    Write-Host "  lemei -> vrs"
    Rename-Item -Path "VRS/lemei" -NewName "vrs" -Force
}

# 1.3 重命名 UI 目录
Write-Host "`n重命名 UI 目录..." -ForegroundColor Yellow
if (Test-Path "VRS/lemei-ui") {
    Write-Host "  lemei-ui -> vrs-ui"
    Rename-Item -Path "VRS/lemei-ui" -NewName "vrs-ui" -Force
}

Write-Host "`n✓ 目录重命名完成`n" -ForegroundColor Green

# ============================================================================
# 阶段 2: 重命名 Java 包结构
# ============================================================================
Write-Host "[阶段 2/5] 重命名 Java 包结构...`n" -ForegroundColor Cyan

Write-Host "重命名包目录: com/lemei -> com/vrs" -ForegroundColor Yellow
$javaDirs = Get-ChildItem -Path "VRS/vrs" -Directory -Recurse -Filter "lemei" -ErrorAction SilentlyContinue | Where-Object { $_.Parent.Name -eq "com" }
foreach ($dir in $javaDirs) {
    Write-Host "  $($dir.FullName)"
    Rename-Item -Path $dir.FullName -NewName "vrs" -Force
}

Write-Host "`n✓ Java 包目录重命名完成`n" -ForegroundColor Green

# ============================================================================
# 阶段 3: 修改文件内容 - 包名和模块名
# ============================================================================
Write-Host "[阶段 3/5] 修改文件内容 - 包名和模块名...`n" -ForegroundColor Cyan

$packageReplacements = @{
    "com.lemei" = "com.vrs"
    "lemei-admin" = "vrs-admin"
    "lemei-api" = "vrs-api"
    "lemei-common" = "vrs-common"
    "lemei-framework" = "vrs-framework"
    "lemei-generator" = "vrs-generator"
    "lemei-quartz" = "vrs-quartz"
    "lemei-system" = "vrs-system"
    "<artifactId>lemei</artifactId>" = "<artifactId>vrs</artifactId>"
    "<groupId>com.lemei</groupId>" = "<groupId>com.vrs</groupId>"
    "<name>lemei</name>" = "<name>vrs</name>"
    '"name": "lemei"' = '"name": "vrs"'
    "lemei.version" = "vrs.version"
}

Replace-InDirectory -Path "VRS/vrs" -Extensions @("java") -Replacements $packageReplacements -Description "Java 源文件"
Replace-InDirectory -Path "VRS/vrs" -Extensions @("xml") -Replacements $packageReplacements -Description "XML 配置文件"
Replace-InDirectory -Path "VRS/vrs" -Extensions @("yml", "yaml") -Replacements $packageReplacements -Description "YAML 配置文件"
Replace-InDirectory -Path "VRS/vrs" -Extensions @("properties") -Replacements $packageReplacements -Description "Properties 配置文件"
Replace-InDirectory -Path "VRS/vrs-ui" -Extensions @("json") -Replacements $packageReplacements -Description "前端配置文件"

Write-Host "`n✓ 包名和模块名修改完成`n" -ForegroundColor Green

# ============================================================================
# 阶段 4: 修改类名前缀 Lm -> VRS
# ============================================================================
Write-Host "[阶段 4/5] 修改类名前缀 Lm -> VRS...`n" -ForegroundColor Cyan

# 4.1 重命名 Java 类文件
Write-Host "重命名 Java 类文件..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Path "VRS/vrs" -Filter "Lm*.java" -Recurse -File -ErrorAction SilentlyContinue
foreach ($file in $javaFiles) {
    $newName = $file.Name -replace "^Lm", "VRS"
    Write-Host "  $($file.Name) -> $newName"
    Rename-Item -Path $file.FullName -NewName $newName -Force
}

# 4.2 修改类名引用
Write-Host "`n修改类名引用..." -ForegroundColor Yellow
$classReplacements = @{
    "LmCarApplication" = "VRSCarApplication"
    "LmGlobalVariable" = "VRSGlobalVariable"
    "LmSupplier" = "VRSSupplier"
    "LmSupplierApp" = "VRSSupplierApp"
    "LmDeliveryItemReal" = "VRSDeliveryItemReal"
    "LmDeliveryItemField" = "VRSDeliveryItemField"
    "ILmCarApplicationService" = "IVRSCarApplicationService"
    "LmCarApplicationServiceImpl" = "VRSCarApplicationServiceImpl"
    "LmCarApplicationMapper" = "VRSCarApplicationMapper"
    "LmCarApplicationController" = "VRSCarApplicationController"
}

Replace-InDirectory -Path "VRS/vrs" -Extensions @("java", "xml") -Replacements $classReplacements -Description "类名引用"

Write-Host "`n✓ 类名前缀修改完成`n" -ForegroundColor Green

# ============================================================================
# 阶段 5: 修改字段名前缀 lm -> vrs
# ============================================================================
Write-Host "[阶段 5/5] 修改字段名前缀 lm -> vrs...`n" -ForegroundColor Cyan

$fieldReplacements = @{
    "lmId" = "vrsId"
    "lmDnnum" = "vrsDnnum"
    "lmDnLine" = "vrsDnLine"
    "lmActualdeliverydate" = "vrsActualdeliverydate"
    "lmOrderid" = "vrsOrderid"
    "lmCarNumber" = "vrsCarNumber"
    "lmPackagingid" = "vrsPackagingid"
    "lmGrossweight" = "vrsGrossweight"
    "lmInsertTime" = "vrsInsertTime"
    "lmUpdateTime" = "vrsUpdateTime"
}

Replace-InDirectory -Path "VRS/vrs" -Extensions @("java") -Replacements $fieldReplacements -Description "Java 字段名"
Replace-InDirectory -Path "VRS/vrs" -Extensions @("xml") -Replacements $fieldReplacements -Description "MyBatis 映射文件"
Replace-InDirectory -Path "VRS/vrs-ui" -Extensions @("vue", "js") -Replacements $fieldReplacements -Description "前端代码"

if (Test-Path "VRS/LMXCX") {
    Replace-InDirectory -Path "VRS/LMXCX" -Extensions @("vue", "js") -Replacements $fieldReplacements -Description "小程序代码"
}

Write-Host "`n✓ 字段名前缀修改完成`n" -ForegroundColor Green

# ============================================================================
# 完成
# ============================================================================
Write-Host "============================================================================" -ForegroundColor Cyan
Write-Host "  重命名任务完成！" -ForegroundColor Green
Write-Host "============================================================================`n" -ForegroundColor Cyan

Write-Host "已完成的修改：" -ForegroundColor Yellow
Write-Host "  ✓ 目录结构: lemei -> vrs"
Write-Host "  ✓ 包名: com.lemei -> com.vrs"
Write-Host "  ✓ 模块名: lemei-* -> vrs-*"
Write-Host "  ✓ 类名前缀: Lm -> VRS"
Write-Host "  ✓ 字段前缀: lm -> vrs`n"

Write-Host "下一步操作：" -ForegroundColor Yellow
Write-Host "  1. 检查修改结果"
Write-Host "  2. 运行测试确保功能正常"
Write-Host "  3. 提交更改`n"
