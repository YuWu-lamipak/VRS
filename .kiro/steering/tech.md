# 技术栈

## 后端 (VRS/lemei)

### 框架和语言
- Java 1.8
- Spring Boot 2.5.6
- Maven（多模块项目）

### 核心依赖
- MyBatis 2.2.0（ORM 框架）
- Druid 1.2.8（数据库连接池）
- PageHelper 1.4.1（分页插件）
- Swagger 3.0.0（API 文档）
- JWT 0.9.1（身份认证）
- Fastjson 1.2.78（JSON 处理）
- Quartz（定时任务调度）
- Redis（缓存）
- Apache POI 4.1.2（Excel 操作）
- Velocity 1.7（代码生成模板）

### 构建命令
```bash
# 构建整个项目
mvn clean install

# 构建指定模块
mvn clean package -pl lemei-admin

# 运行后端（默认端口：8066）
java -jar lemei-admin/target/lemei-admin.jar
```

## 前端管理后台 (VRS/lemei-ui)

### 框架和语言
- Vue 2.6.12
- Element UI 2.15.6
- Vue Router 3.4.9
- Vuex 3.6.0

### 主要依赖库
- Axios 0.24.0（HTTP 客户端）
- ECharts 4.9.0（图表）
- Quill 1.3.7（富文本编辑器）
- vue-plugin-hiprint 0.0.56（打印）
- JSEncrypt 3.2.1（加密）

### 开发命令
```bash
# 安装依赖
npm install

# 启动开发服务器（端口：80）
npm run dev

# 生产环境构建
npm run build:prod

# 测试环境构建
npm run build:stage

# 代码检查
npm run lint
```

### 配置说明
- 开发代理目标：`http://localhost:8066`
- API 基础路径：`/dev-api`
- 使用 legacy OpenSSL provider 以兼容 Node.js

## 小程序 (VRS/LMXCX/ym_car_xcx/ym_car_xcx)

### 框架
- uni-app（基于 Vue 2.6.11）
- uView UI 2.0.37
- uni-simple-router 2.0.8-beta.4

### 开发命令
```bash
# 安装依赖
npm install

# 微信小程序开发
npm run dev:mp-weixin

# H5 开发
npm run dev:h5

# 微信小程序构建
npm run build:mp-weixin

# H5 构建
npm run build:h5
```

### 支持平台
- 微信小程序（主要）
- H5
- 支付宝、百度、QQ、头条小程序
- App（iOS/Android）

## 数据库
- MySQL（使用 MyBatis XML 映射）
- Redis（会话和缓存存储）

## 开发工具
- Maven 仓库：阿里云镜像
- Maven 版本：3.8.8
- Node.js >= 8.9
- npm >= 3.0.0

## 测试框架

### 后端测试
- JUnit 4（单元测试框架）
- Mockito（模拟框架）
- REST Assured（API 测试）
- Spring Boot Test（集成测试）

### 测试覆盖
- **lemei-system**: 6 个单元测试（业务逻辑测试）
- **lemei-api**: 7 个集成测试（API 接口测试）
- **lemei-admin**: 2 个控制器测试（控制器功能测试）
- **总计**: 15 个测试用例

### 测试工具
- **run-tests.bat** - 批处理测试脚本（推荐，双击即可运行）
- **run-full-tests.ps1** - PowerShell 测试脚本（包含智能服务管理）
- **generate-summary.ps1** - 测试报告生成脚本（UTF-8 编码支持）

### 测试报告
- 位置：`TestReport/`
- 格式：文本（.txt）和 XML（.xml）
- 总结：`TEST_SUMMARY.md`（UTF-8 编码，中文正常显示）

## 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| lemei-admin | 8066 | 后端管理服务 |
| lemei-api | 8601 | API 服务 |
| lemei-ui | 80 | 前端开发服务器 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存服务 |

## 环境要求

### 开发环境
- Windows 操作系统
- Java JDK 1.8
- Maven 3.8.8
- Node.js >= 8.9
- MySQL 数据库
- Redis 服务

### 运行环境
- 后端服务需要 Java 1.8 运行时
- 前端需要 Node.js 环境（开发模式）
- 数据库：MySQL + Redis
