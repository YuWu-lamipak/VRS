# UTF-8 Test Summary Generator
$templatePath = "TestReport\SUMMARY_TEMPLATE.md"
$outputPath = "TestReport\TEST_SUMMARY.md"

if (-not (Test-Path $templatePath)) {
    Write-Host "Error: Template not found" -ForegroundColor Red
    exit 1
}

$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

function Get-TestResult($path) {
    $txtFile = Get-ChildItem -Path $path -Filter "*.txt" -ErrorAction SilentlyContinue | Select-Object -First 1
    if (-not $txtFile) {
        return @{Tests=0; Failures=0; Errors=0; Skipped=0; Time="0.000"}
    }
    
    $content = Get-Content $txtFile.FullName -Raw
    $pattern = "Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+), Time elapsed: (\d+\.\d+)"
    if ($content -match $pattern) {
        return @{
            Tests = [int]$matches[1]
            Failures = [int]$matches[2]
            Errors = [int]$matches[3]
            Skipped = [int]$matches[4]
            Time = $matches[5]
        }
    }
    return @{Tests=0; Failures=0; Errors=0; Skipped=0; Time="0.000"}
}

$system = Get-TestResult "TestReport\lemei-system"
$api = Get-TestResult "TestReport\lemei-api"
$admin = Get-TestResult "TestReport\lemei-admin"

$totalTests = $system.Tests + $api.Tests + $admin.Tests
$totalFailed = $system.Failures + $api.Failures + $admin.Failures + $system.Errors + $api.Errors + $admin.Errors
$totalPassed = $totalTests - $totalFailed
$totalSkipped = $system.Skipped + $api.Skipped + $admin.Skipped
$totalTime = [double]$system.Time + [double]$api.Time + [double]$admin.Time
$successRate = if ($totalTests -gt 0) { [math]::Round(($totalPassed / $totalTests) * 100, 2) } else { 0 }
$status = if ($totalFailed -eq 0) { "PASS" } else { "FAIL" }

$content = Get-Content $templatePath -Raw -Encoding UTF8
$content = $content -replace "{{TIMESTAMP}}", $timestamp
$content = $content -replace "{{STATUS}}", $status
$content = $content -replace "{{SUCCESS_RATE}}", "$successRate%"
$content = $content -replace "{{TOTAL_TIME}}", ("{0:F3}s" -f $totalTime)
$content = $content -replace "{{SYSTEM_TESTS}}", $system.Tests
$content = $content -replace "{{SYSTEM_PASSED}}", ($system.Tests - $system.Failures - $system.Errors)
$content = $content -replace "{{SYSTEM_FAILED}}", ($system.Failures + $system.Errors)
$content = $content -replace "{{SYSTEM_SKIPPED}}", $system.Skipped
$content = $content -replace "{{SYSTEM_TIME}}", "$($system.Time)s"
$content = $content -replace "{{API_TESTS}}", $api.Tests
$content = $content -replace "{{API_PASSED}}", ($api.Tests - $api.Failures - $api.Errors)
$content = $content -replace "{{API_FAILED}}", ($api.Failures + $api.Errors)
$content = $content -replace "{{API_SKIPPED}}", $api.Skipped
$content = $content -replace "{{API_TIME}}", "$($api.Time)s"
$content = $content -replace "{{ADMIN_TESTS}}", $admin.Tests
$content = $content -replace "{{ADMIN_PASSED}}", ($admin.Tests - $admin.Failures - $admin.Errors)
$content = $content -replace "{{ADMIN_FAILED}}", ($admin.Failures + $admin.Errors)
$content = $content -replace "{{ADMIN_SKIPPED}}", $admin.Skipped
$content = $content -replace "{{ADMIN_TIME}}", "$($admin.Time)s"
$content = $content -replace "{{TOTAL_TESTS}}", $totalTests
$content = $content -replace "{{TOTAL_PASSED}}", $totalPassed
$content = $content -replace "{{TOTAL_FAILED}}", $totalFailed
$content = $content -replace "{{TOTAL_SKIPPED}}", $totalSkipped

$Utf8NoBomEncoding = New-Object System.Text.UTF8Encoding $False
[System.IO.File]::WriteAllText($outputPath, $content, $Utf8NoBomEncoding)

Write-Host "Success: Test summary generated" -ForegroundColor Green
