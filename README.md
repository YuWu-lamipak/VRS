# 乐美车辆预约系统

**版本**: 3.7.0  
**最后更新**: 2025-12-11  
**最新修复**: 多用户会话隔离问题（2025-12-11）✅

一个综合性的车辆预约和管理系统，包含后端API、管理后台和微信小程序。

---

## 🚀 快速启动

### 启动服务

```cmd
# 启动后端管理服务（端口 8066）
"Start Engine\start-backend-fixed.bat"

# 启动 API 服务（端口 8601）
"Start Engine\start-api.bat"

# 启动前端管理后台（端口 80）
cd VRS\lemei-ui
npm run dev
```

### 检查服务状态

```cmd
# 检查服务端口
netstat -ano | findstr "8066 8601"

# 或使用检查脚本
"Start Engine\check-services.bat"
```

---

## 🧪 运行测试

### 单元测试（15个测试用例）

```cmd
# 双击运行或命令行执行
Test\run-tests.bat
```

### 称重流程测试（推荐智能版）

```cmd
# 智能版：自动创建和清理数据，可重复运行
Test\test-weighing-flow-auto.bat

# 标准版：使用固定测试数据
Test\test-weighing-flow.bat
```

---

## 📚 文档

- **[Test/测试手册.md](Test/测试手册.md)** - 完整的测试指南、流程说明和故障排除
- **[Test/TestReport/TEST_SUMMARY.md](Test/TestReport/TEST_SUMMARY.md)** - 最新测试报告

---

## 📁 项目结构

```
项目根目录/
├── VRS/
│   ├── lemei/              # 后端 Java 应用（Spring Boot）
│   │   ├── lemei-admin/    # 管理后台服务（端口 8066）
│   │   ├── lemei-api/      # API 服务（端口 8601）
│   │   ├── lemei-system/   # 系统模块
│   │   ├── lemei-common/   # 通用模块
│   │   └── ...
│   ├── lemei-ui/           # 前端管理后台（Vue.js，端口 80）
│   └── LMXCX/             # 微信小程序（uni-app）
├── Start Engine/          # 服务启动脚本
│   ├── start-backend-fixed.bat
│   ├── start-api.bat
│   └── ...
├── Test/                  # 测试相关文件
│   ├── TestReport/        # 测试报告目录
│   ├── 测试手册.md        # 测试指南
│   ├── run-tests.bat      # 单元测试脚本
│   └── ...
├── 项目说明.md            # 项目说明文档
└── README.md             # 本文档（快速启动）
```

---

## 🛠️ 技术栈

### 后端
- Java 1.8
- Spring Boot 2.5.6
- MyBatis 2.2.0
- MySQL + Redis

### 前端
- Vue 2.6.12
- Element UI 2.15.6
- Axios 0.24.0

### 小程序
- uni-app
- uView UI 2.0.37

### 测试
- JUnit 4
- Mockito
- REST Assured

---

## 🌐 服务地址

| 服务 | 地址 | 端口 |
|------|------|------|
| 前端管理后台 | http://localhost | 80 |
| 后端管理服务 | http://localhost:8066/dev | 8066 |
| API 服务 | http://localhost:8601 | 8601 |
| Swagger 文档 | http://localhost:8601/swagger-ui/index.html | 8601 |
| MySQL | localhost | 3306 |
| Redis | localhost | 6379 |

---

## 📝 常用脚本

### 启动脚本（Start Engine/）

| 脚本 | 说明 |
|------|------|
| `start-backend-fixed.bat` | 启动后端管理服务（端口8066） |
| `start-api.bat` | 启动API服务（端口8601） |
| `start-all.bat` | 启动所有服务 |
| `stop-all.bat` | 停止所有服务 |
| `check-services.bat` | 检查服务状态 |

### 测试脚本（Test/）

| 脚本 | 说明 |
|------|------|
| `run-tests.bat` | 运行单元测试（15个测试用例） |
| `test-weighing-flow-auto.bat` | 运行称重流程测试（智能版，推荐） |
| `test-weighing-flow.bat` | 运行称重流程测试（标准版） |

---

## 📞 获取帮助

- 项目详细说明：查看 [项目说明.md](项目说明.md)
- 测试相关问题：查看 [Test/测试手册.md](Test/测试手册.md)
- 项目结构说明：查看 `.kiro/steering/structure.md`
- 技术栈详情：查看 `.kiro/steering/tech.md`

---

**开发愉快！** 🎉
