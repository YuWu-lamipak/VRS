# 项目结构

## 根目录组织

```
项目根目录/
├── .github/
│   └── README.md              # 文档结构说明
├── .kiro/
│   └── steering/              # AI 助手上下文配置
│       ├── product.md         # 产品概述和项目状态
│       ├── structure.md       # 项目结构说明
│       └── tech.md            # 技术栈详情
├── TestReport/                # 测试报告目录
│   ├── TEST_SUMMARY.md        # 测试总结（UTF-8 编码）
│   ├── SUMMARY_TEMPLATE.md    # 报告模板
│   ├── lemei-system/          # 单元测试报告
│   ├── lemei-api/             # 集成测试报告
│   └── lemei-admin/           # 控制器测试报告
├── VRS/                       # 项目源代码
│   ├── lemei/                 # 后端 Java 应用
│   ├── lemei-ui/              # 管理后台前端

├── README.md                  # 项目主文档
├── TESTING.md                 # 测试指南
├── run-tests.bat              # 批处理测试脚本（推荐）
├── run-full-tests.ps1         # PowerShell 测试脚本
├── generate-summary.ps1       # 报告生成脚本
├── start-backend-fixed.bat    # 启动后端服务
└── start-api.bat              # 启动 API 服务
```

## 核心文档说明

### 用户文档
- **README.md** - 项目主文档，包含快速启动指南
- **TESTING.md** - 完整的测试指南，包含测试流程、故障排除和修复记录

### 测试脚本
- **run-tests.bat** - 批处理测试脚本，双击即可运行（推荐）
- **run-full-tests.ps1** - PowerShell 测试脚本，包含智能服务管理
- **generate-summary.ps1** - 测试报告生成脚本（自动调用）

### 启动脚本
- **start-backend-fixed.bat** - 启动后端管理服务（端口 8066）
- **start-api.bat** - 启动 API 服务（端口 8601）

## 后端结构 (VRS/lemei)

遵循标准 Spring Boot 架构的多模块 Maven 项目：

```
lemei/
├── lemei-admin/        # 主应用入口和 Web 控制器
│   ├── src/main/java/com/lemei/
│   │   ├── LeMeiApplication.java          # Spring Boot 主类
│   │   └── adminweb/
│   │       ├── common/                     # 通用工具
│   │       ├── core/                       # 核心配置
│   │       ├── domain/                     # 领域模型
│   │       ├── monitor/                    # 监控控制器
│   │       ├── system/                     # 系统管理控制器
│   │       └── tool/                       # 工具控制器
│   └── src/main/resources/
│       ├── application.yml                 # 主配置文件
│       ├── application-dev.yml             # 开发环境配置
│       ├── application-druid.yml           # 数据库连接池配置
│       ├── mybatis/                        # MyBatis 配置
│       └── mapper/                         # MyBatis XML 映射文件
├── lemei-api/          # API 模块（外部接口）
├── lemei-common/       # 通用工具和基础类
├── lemei-framework/    # 框架核心（安全、配置、拦截器）
├── lemei-system/       # 系统管理模块（用户、角色、菜单）
├── lemei-quartz/       # 定时任务管理
├── lemei-generator/    # 代码生成器模块
├── sql/               # 数据库脚本
└── pom.xml            # 父 POM
```

### 模块依赖关系
- `lemei-admin` 依赖所有其他模块
- `lemei-framework` 提供核心功能
- `lemei-common` 被所有模块共享
- `lemei-system` 处理用户/角色/权限管理

## 前端结构 (VRS/lemei-ui)

标准的 Vue CLI 项目，使用 Element UI：

```
lemei-ui/
├── public/            # 静态资源
├── src/
│   ├── api/          # API 服务层（按功能组织）
│   │   ├── login.js
│   │   ├── menu.js
│   │   ├── monitor/  # 监控 API
│   │   ├── system/   # 系统管理 API
│   │   └── tool/     # 工具 API
│   ├── assets/       # 图片、样式、图标
│   │   ├── icons/    # SVG 图标
│   │   ├── images/
│   │   └── styles/   # SCSS 样式表
│   ├── components/   # 可复用组件
│   │   ├── Breadcrumb/
│   │   ├── Pagination/
│   │   ├── DictTag/
│   │   ├── FileUpload/
│   │   └── ...
│   ├── directive/    # 自定义 Vue 指令
│   │   ├── permission/  # 权限指令（hasPermi、hasRole）
│   │   └── dialog/      # 对话框拖拽指令
│   ├── layout/       # 布局组件
│   ├── router/       # Vue Router 配置
│   ├── store/        # Vuex 状态管理
│   │   └── modules/  # Store 模块（app、user、permission 等）
│   ├── utils/        # 工具函数
│   │   ├── request.js    # Axios 封装
│   │   ├── auth.js       # Token 管理
│   │   ├── dict/         # 字典工具
│   │   └── ...
│   ├── views/        # 页面组件
│   │   ├── dashboard/    # 仪表盘页面
│   │   ├── system/       # 系统管理页面
│   │   │   ├── user/
│   │   │   ├── role/
│   │   │   ├── menu/
│   │   │   ├── dept/
│   │   │   ├── application/  # 车辆申请管理
│   │   │   ├── driver/
│   │   │   ├── carrier/
│   │   │   └── warehouse/
│   │   ├── monitor/      # 监控页面
│   │   └── tool/         # 工具页面
│   ├── plugins/      # Vue 插件（auth、cache、modal、tab）
│   ├── App.vue
│   ├── main.js       # 应用入口
│   ├── permission.js # 路由权限控制
│   └── settings.js   # UI 设置
├── vue.config.js     # Vue CLI 配置
└── package.json
```



## 关键约定

### 后端
- 控制器位于 `adminweb/{feature}/controller/`
- 服务层位于模块的 `service/` 目录
- Mapper（DAO）位于模块的 `mapper/` 目录
- MyBatis XML 位于 `resources/mapper/`
- 领域模型位于模块的 `domain/` 目录
- 基础路径：`/dev`（在 application.yml 中配置）

### 前端
- API 调用按功能组织在 `src/api/`
- `src/views/` 中的页面组件与后端结构对应
- 可复用组件放在 `src/components/`
- 使用 Vuex 模块进行状态管理
- 通过指令控制权限（`v-hasPermi`、`v-hasRole`）
- 通过 `DictData` 组件管理字典数据


