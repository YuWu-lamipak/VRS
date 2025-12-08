# 乐美车辆预约系统

## 快速启动

### 启动后端服务
```bash
# 启动管理后台服务（端口 8066）
.\start-backend-fixed.bat

# 启动 API 服务（端口 8601）
.\start-api.bat
```

### 启动前端
```bash
cd VRS\lemei-ui
npm run dev
```

## 运行测试

### 快速测试（推荐）

```cmd
# 双击运行或命令行执行
.\run-tests.bat
```

**自动完成：** 检查服务 → 运行测试 → 生成报告 → 显示结果

### 其他方式

```powershell
# PowerShell 脚本（包含智能服务管理）
powershell -ExecutionPolicy Bypass -File .\run-full-tests.ps1

# 手动测试
cd VRS\lemei
D:\maven\apache-maven-3.8.8\bin\mvn.cmd test
```

## 测试报告

- 测试总结: [TestReport/TEST_SUMMARY.md](TestReport/TEST_SUMMARY.md)
- 详细报告: `TestReport/lemei-system/`, `TestReport/lemei-api/`, `TestReport/lemei-admin/`

## 文档

- [测试指南](TESTING.md) - 完整的测试流程、故障排除和修复记录

## 项目结构

```
VRS/
├── lemei/              # 后端 Java 应用
│   ├── lemei-admin/    # 管理后台服务（端口 8066）
│   ├── lemei-api/      # API 服务（端口 8601）
│   ├── lemei-system/   # 系统模块
│   └── ...
├── lemei-ui/           # 前端管理后台（端口 80）
└── LMXCX/             # 微信小程序
```

## 技术栈

- **后端**: Java 1.8, Spring Boot 2.5.6, MyBatis
- **前端**: Vue 2.6.12, Element UI
- **数据库**: MySQL, Redis
- **测试**: JUnit 4, Mockito, REST Assured

## 服务地址

- 前端: http://localhost
- 后端管理: http://localhost:8066/dev
- API 服务: http://localhost:8601
- Swagger: http://localhost:8601/swagger-ui/index.html
