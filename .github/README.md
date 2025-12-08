# 文档结构说明

## 根目录文档

- **README.md** - 项目主文档，包含快速启动指南
- **TESTING.md** - 完整的测试指南，包含测试流程、故障排除和修复记录

## 测试脚本

- **run-tests.bat** - 批处理测试脚本（推荐，双击即可运行）
- **run-full-tests.ps1** - PowerShell 完整测试脚本（包含智能服务管理）
- **generate-summary.ps1** - 测试报告生成脚本（自动调用）

## 启动脚本

- **start-backend-fixed.bat** - 启动后端管理服务（端口 8066）
- **start-api.bat** - 启动 API 服务（端口 8601）

## 测试报告

- **TestReport/** - 测试报告目录
  - **TEST_SUMMARY.md** - 测试总结（UTF-8 编码，中文正常显示）
  - **SUMMARY_TEMPLATE.md** - 测试报告模板
  - **lemei-system/** - 单元测试报告
  - **lemei-api/** - API 集成测试报告
  - **lemei-admin/** - 控制器测试报告

## 项目配置

- **.kiro/steering/** - Kiro AI 助手的项目上下文配置
  - **product.md** - 产品概述
  - **structure.md** - 项目结构说明
  - **tech.md** - 技术栈详情
