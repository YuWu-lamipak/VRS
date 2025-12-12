# VRS车辆预约系统

**版本**: 3.7.0  
**最后更新**: 2025-12-12  
**项目状态**: ✅ 生产运行中

一个综合性的车辆预约和管理系统，专注于Web应用架构，包含后端API服务和前端管理后台。

---

## 🚀 快速启动

### 环境要求

- **Java JDK 1.8+** - 后端运行环境
- **Maven 3.8.8+** - 项目构建工具
- **MySQL 8.0+** - 数据库（推荐8.0以上版本）
- **Redis 6.0+** - 缓存服务
- **Node.js 8.9+** - 前端开发环境

### 数据库初始化

1. **创建数据库**
   ```sql
   CREATE DATABASE vrs_system CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
   ```

2. **导入数据库结构**
   ```bash
   mysql -u root -p vrs_system < VRS/vrs/sql/VRS.sql
   ```

### 启动步骤

1. **启动后端服务**
   ```bash
   cd VRS/vrs
   mvn clean package
   java -jar vrs-admin/target/vrs-admin.jar  # 端口: 8066
   java -jar vrs-api/target/vrs-api.jar      # 端口: 8601
   ```

2. **启动前端服务**
   ```bash
   cd VRS/vrs-ui
   npm install
   npm run dev  # 端口: 80
   ```

### 服务地址

| 服务 | 地址 | 端口 | 说明 |
|------|------|------|------|
| 前端管理后台 | http://localhost | 80 | Vue.js Web管理界面 |
| 后端管理服务 | http://localhost:8066/dev | 8066 | Spring Boot管理API |
| API 服务 | http://localhost:8601 | 8601 | 外部接口服务 |
| Swagger 文档 | http://localhost:8601/swagger-ui/index.html | 8601 | API接口文档 |

---

## 📁 项目结构

```
VRS/
├── vrs/                    # 后端Java项目
│   ├── vrs-admin/         # 管理后台主应用（端口 8066）
│   │   ├── src/main/java/com/vrs/
│   │   └── target/vrs-admin.jar
│   ├── vrs-api/           # API接口服务（端口 8601）
│   │   ├── src/main/java/com/vrs/
│   │   └── target/vrs-api.jar
│   ├── vrs-system/        # 系统管理模块
│   ├── vrs-framework/     # 框架核心
│   ├── vrs-common/        # 通用工具
│   ├── vrs-generator/     # 代码生成器
│   ├── vrs-quartz/        # 定时任务
│   ├── sql/
│   │   └── VRS.sql       # 数据库结构文件
│   └── pom.xml           # 父POM配置
└── vrs-ui/               # 前端Vue.js项目（端口 80）
    ├── src/
    │   ├── api/          # API接口调用
    │   ├── components/   # 可复用组件
    │   ├── views/        # 页面组件
    │   ├── router/       # 路由配置
    │   ├── store/        # Vuex状态管理
    │   └── utils/        # 工具函数
    ├── public/           # 静态资源
    └── package.json      # 前端依赖配置
```

---

## 🛠️ 技术栈

### 后端技术
- **Java 1.8** - 开发语言
- **Spring Boot 2.5.6** - 应用框架
- **MyBatis 2.2.0** - ORM框架
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Maven 3.8.8** - 项目构建
- **Druid 1.2.8** - 数据库连接池
- **JWT 0.9.1** - 身份认证
- **Swagger 3.0.0** - API文档

### 前端技术
- **Vue 2.6.12** - 前端框架
- **Element UI 2.15.6** - UI组件库
- **Axios 0.24.0** - HTTP客户端
- **Vuex 3.6.0** - 状态管理
- **Vue Router 3.4.9** - 路由管理
- **ECharts 4.9.0** - 图表组件

---

## 📋 核心功能

### 🚗 车辆预约管理
- 车辆预约申请和审核流程
- 预约状态实时跟踪
- 预约时间智能调度
- 预约单号自动生成

### ⚖️ 称重管理
- 一次称重（空车重量记录）
- 二次称重（满载重量记录）
- 净重自动计算和记录
- 称重数据历史查询

### 👥 用户权限管理
- 基于角色的访问控制（RBAC）
- 用户、角色、权限三级管理
- 部门组织架构管理
- 菜单权限动态配置

### 📊 基础数据管理
- 司机信息档案管理
- 承运商资质管理
- 仓库信息维护
- 门卫值班管理
- 供应商信息管理

### 📈 系统监控
- 实时数据统计看板
- 操作日志详细记录
- 系统配置参数管理
- 登录日志安全监控

### 🔧 业务流程
- 制卡/退卡流程管理
- 进出厂状态控制
- 业务类型分类（采购/销售/废料/其他）
- 审计追踪和数据备份

---

## 🔧 开发指南

### Maven 构建

```bash
# 快速打包（跳过测试，推荐用于开发）
mvn clean package

# 运行测试
mvn clean test -P with-tests

# 带测试打包
mvn clean package -P with-tests

# 构建指定模块
mvn clean package -pl vrs-admin
mvn clean package -pl vrs-api
```

### 前端开发

```bash
# 安装依赖
cd VRS/vrs-ui
npm install

# 开发模式启动
npm run dev

# 生产环境构建
npm run build:prod

# 代码检查
npm run lint
```

### 开发规范

1. **代码规范**
   - 后端遵循阿里巴巴Java开发手册
   - 前端遵循Vue官方风格指南
   - 统一使用UTF-8编码
   - 包名统一使用`com.vrs`

2. **数据库规范**
   - 表名使用`VRS_`前缀
   - 字段名使用下划线命名
   - 必须有主键和创建时间
   - 中文注释使用UTF-8编码

3. **API规范**
   - RESTful API设计
   - 统一返回格式（AjaxResult）
   - 完整的错误处理
   - Swagger文档注解

### 配置说明

- **后端配置**: `application.yml`、`application-dev.yml`
- **前端配置**: `vue.config.js`、`.env.development`
- **数据库**: 使用Druid连接池，支持多数据源

---

## 📚 文档

- **[项目说明.md](项目说明.md)** - 详细的项目说明和架构文档
- **[需求说明书.md](需求说明书.md)** - 完整的需求规格说明
- **[.github/README.md](.github/README.md)** - 文档结构说明
- **[VRS/vrs/sql/VRS.sql](VRS/vrs/sql/VRS.sql)** - 完整的数据库结构文件

### API文档
- **Swagger UI**: http://localhost:8601/swagger-ui/index.html
- **API接口**: 详细的RESTful API文档和测试界面

### 项目状态
- **测试状态**: ✅ 全部通过（15/15测试用例）
- **代码质量**: 100%命名一致性，完整的VRS重命名
- **架构状态**: 专注Web应用，移除小程序模块

---

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

---

## 🎯 项目特色

- **🏗️ 模块化架构**: 清晰的多模块Maven项目结构
- **🔒 安全可靠**: JWT认证 + RBAC权限控制
- **📱 响应式设计**: Element UI组件，适配多种设备
- **⚡ 高性能**: Redis缓存 + Druid连接池优化
- **📊 实时监控**: 完整的操作日志和系统监控
- **🔧 易于扩展**: 标准化的代码生成器和开发规范

## 📞 联系方式

- **项目维护者**: VRS开发团队
- **技术支持**: wagalicn@gmail.com
- **项目仓库**: [VRS车辆预约系统](https://github.com/vrs-system/vrs)

## 🏆 版本历史

- **v3.7.0** (2025-12-12) - 完成项目重命名，架构简化，专注Web应用
- **v3.6.x** - 多用户会话隔离修复，API接口优化
- **v3.5.x** - 测试框架完善，中文编码问题修复

---

**开发愉快！** 🎉