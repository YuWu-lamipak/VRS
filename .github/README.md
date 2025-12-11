# 乐美车辆预约系统 - 文档结构说明

**版本**: 3.7.0  
**最后更新**: 2025-12-11  
**文档状态**: ✅ 完整且最新

本文档说明项目的文档组织结构和各文档的用途。

---

## 📚 文档目录

### 核心文档（根目录）

#### 1. README.md
**用途**: 快速启动指南  
**内容**: 
- ⚠️ 首次运行必读（配置说明）
- 快速启动命令
- 运行测试
- 常用脚本
- 服务地址
- 技术栈概览
- 安全说明

**适用人群**: 新手开发者、快速上手

**重要提示**: 2025-12-11 完成敏感信息清理，首次运行前必须配置环境

#### 2. 项目说明.md
**用途**: 完整的项目说明文档  
**内容**:
- 项目概述和核心价值
- 系统架构详解
- 核心功能说明
- 技术栈详情
- 测试覆盖情况
- 部署架构
- 业务流程图
- 项目进度和更新记录

**适用人群**: 项目经理、架构师、新加入团队成员

#### 3. 需求说明书.md
**用途**: 正式的需求文档  
**内容**:
- 项目概述和背景
- 业务需求详解
- 用户角色和权限
- 功能需求规格
- 非功能需求
- 系统架构需求
- 接口需求
- 数据需求
- 安全需求
- 性能需求
- 部署需求

**适用人群**: 产品经理、需求分析师、开发团队、测试团队

---

### 配置文档（根目录）

#### 4. CONFIG_GUIDE.md
**用途**: 配置指南（必读）  
**内容**:
- 快速开始步骤
- 必需的配置项
- 安全最佳实践
- 相关文档链接

**适用人群**: 所有开发者（首次运行必读）

#### 5. .env.example
**用途**: 环境变量配置示例  
**内容**:
- 数据库配置
- Redis 配置
- JWT 配置
- 微信企业号配置
- Druid 监控配置
- ESB 配置
- 应用配置

**适用人群**: 所有开发者

#### 6. VRS/lemei/SETUP_GUIDE.md
**用途**: 后端服务详细配置指南  
**内容**:
- 配置文件复制步骤
- 配置项详细说明
- 验证配置方法
- 常见问题解答

**适用人群**: 后端开发者

#### 7. VRS/lemei/*/src/main/resources/application-example.yml
**用途**: 应用配置模板  
**内容**:
- 数据库连接配置模板
- Redis 配置模板
- Druid 监控配置模板
- ESB 配置模板

**适用人群**: 后端开发者

**使用方式**: 复制为 `application-dev.yml` 并填写实际值

---

### 安全文档（根目录）

#### 8. CLEANUP_COMPLETED.md
**用途**: 敏感信息清理完成报告  
**内容**:
- 已完成的工作
- 安全改进总结
- 重要提醒
- 文件变更统计
- 下一步操作

**适用人群**: 所有团队成员



---

### 技术文档（根目录）

#### 12. 多用户会话隔离修复说明.md
**用途**: 2025-12-11修复的技术文档  
**内容**:
- 问题描述和影响
- 根本原因分析
- 解决方案设计
- 技术实现细节
- 测试验证
- 部署说明

**适用人群**: 开发人员、技术支持

#### 13. 快速部署-多用户修复版本.md
**用途**: 快速部署指南  
**内容**:
- 快速部署步骤
- 验证修复效果
- 新增文件说明
- 修复前后对比
- 故障排除

**适用人群**: 运维人员、部署人员

---

### 测试文档（Test/）

#### 14. Test/测试手册.md
**用途**: 完整的测试指南  
**内容**:
- 测试环境准备
- 单元测试说明
- 称重流程测试
- 测试报告说明
- 故障排除
- 测试覆盖详情

**适用人群**: 测试人员、QA团队

#### 15. Test/TestReport/TEST_SUMMARY.md
**用途**: 最新测试报告  
**内容**:
- 测试执行时间
- 测试结果统计
- 各模块测试详情
- 成功率和耗时

**适用人群**: 项目经理、测试人员

---

### AI助手上下文文档（.kiro/steering/）

#### 16. .kiro/steering/product.md
**用途**: 产品概述和项目状态  
**内容**:
- 产品概述
- 核心功能
- 版本信息
- 开发环境状态
- 测试状态
- 最近修复记录
- 文档优化记录

**适用人群**: AI助手、项目团队

#### 17. .kiro/steering/structure.md
**用途**: 项目结构说明  
**内容**:
- 根目录组织
- 核心文档说明
- 后端结构
- 前端结构
- 小程序结构
- 关键约定

**适用人群**: AI助手、新开发者

#### 18. .kiro/steering/tech.md
**用途**: 技术栈详情  
**内容**:
- 后端技术栈
- 前端技术栈
- 小程序技术栈
- 测试框架
- 构建命令
- 开发命令
- 服务端口
- 环境要求

**适用人群**: AI助手、技术团队

#### 19. .kiro/steering/status.md
**用途**: 项目状态和进度  
**内容**:
- 当前状态
- 系统运行状态
- 测试状态
- 最近完成的工作
- 当前文档结构
- 测试覆盖详情
- 已知问题
- 下一步计划

**适用人群**: AI助手、项目经理

---

### 启动脚本（Start Engine/）

#### 服务启动脚本
- `start-backend-fixed.bat` - 启动后端管理服务（8066）
- `start-api.bat` - 启动API服务（8601）
- `start-all.bat` - 启动所有服务
- `stop-all.bat` - 停止所有服务
- `check-services.bat` - 检查服务状态
- `create-startup-task.bat` - 创建开机启动任务
- `delete-startup-task.bat` - 删除开机启动任务

**适用人群**: 开发人员、运维人员

---

### 测试脚本（Test/）

#### 测试执行脚本
- `run-tests.bat` - 运行单元测试（15个测试用例）
- `test-weighing-flow-auto.bat` - 称重流程测试（智能版，推荐）
- `test-weighing-flow.bat` - 称重流程测试（标准版）
- `generate-summary.ps1` - 生成测试报告（自动调用）

**适用人群**: 测试人员、开发人员

---

## 📁 完整项目结构

```
项目根目录/
├── .github/                       # GitHub配置
│   └── README.md                  # 本文档（文档结构说明）
├── .kiro/                         # AI助手配置
│   └── steering/                  # 上下文文档
│       ├── product.md             # 产品概述和项目状态
│       ├── structure.md           # 项目结构说明
│       ├── tech.md                # 技术栈详情
│       └── status.md              # 项目状态和进度
├── .env.example                   # 环境变量示例（必读）
├── CONFIG_GUIDE.md                # 配置指南（必读，含安全最佳实践）
├── CLEANUP_COMPLETED.md           # 敏感信息清理报告
├── VRS/                           # 源代码目录
│   ├── lemei/                     # 后端Java应用
│   │   ├── SETUP_GUIDE.md         # 后端配置指南
│   │   ├── lemei-admin/           # 管理后台服务（8066）
│   │   │   └── src/main/resources/
│   │   │       ├── application.yml           # 基础配置
│   │   │       └── application-example.yml   # 配置模板（必读）
│   │   ├── lemei-api/             # API服务（8601）
│   │   │   └── src/main/resources/
│   │   │       ├── application.yml           # 基础配置
│   │   │       └── application-example.yml   # 配置模板（必读）
│   ├── lemei/                     # 后端Java应用
│   │   ├── lemei-admin/           # 管理后台服务（8066）
│   │   ├── lemei-api/             # API服务（8601）
│   │   ├── lemei-system/          # 系统模块
│   │   ├── lemei-common/          # 通用模块
│   │   ├── lemei-framework/       # 框架核心
│   │   ├── lemei-quartz/          # 定时任务
│   │   ├── lemei-generator/       # 代码生成器
│   │   ├── sql/                   # 数据库脚本
│   │   └── pom.xml                # Maven配置
│   ├── lemei-ui/                  # 前端管理后台（Vue.js）
│   └── LMXCX/                     # 微信小程序（uni-app）
├── Start Engine/                  # 服务启动脚本
│   ├── start-backend-fixed.bat   # 启动后端管理服务
│   ├── start-api.bat              # 启动API服务
│   ├── start-all.bat              # 启动所有服务
│   ├── stop-all.bat               # 停止所有服务
│   ├── check-services.bat         # 检查服务状态
│   ├── create-startup-task.bat   # 创建开机启动任务
│   └── delete-startup-task.bat   # 删除开机启动任务
├── Test/                          # 测试相关文件
│   ├── TestReport/                # 测试报告目录
│   │   ├── TEST_SUMMARY.md        # 测试总结
│   │   ├── lemei-admin/           # Admin模块测试报告
│   │   ├── lemei-api/             # API模块测试报告
│   │   └── lemei-system/          # System模块测试报告
│   ├── 测试手册.md                # 测试指南
│   ├── run-tests.bat              # 单元测试脚本
│   ├── test-weighing-flow-auto.bat # 称重流程测试（智能版）
│   ├── test-weighing-flow.bat     # 称重流程测试（标准版）
│   ├── generate-summary.ps1       # 报告生成脚本
│   └── .postman.json              # Postman配置
├── README.md                      # 快速启动指南（含配置说明）
├── 项目说明.md                    # 完整项目说明
├── 需求说明书.md                  # 需求文档
├── 多用户会话隔离修复说明.md      # 技术修复文档
├── 快速部署-多用户修复版本.md     # 部署指南
└── .gitignore                     # Git忽略配置（已更新）

注意：
- application-dev.yml 和 application-prod.yml 已从仓库中删除（包含敏感信息）
- 使用 application-example.yml 作为模板创建自己的配置文件
- .gitignore 已更新，防止敏感配置文件被提交
```

---

## 📖 文档阅读顺序建议

### 新手入门（必读）
1. **README.md** - 快速启动和配置说明
2. **CONFIG_GUIDE.md** - 配置指南（首次运行必读）
3. **VRS/lemei/SETUP_GUIDE.md** - 后端详细配置
4. **项目说明.md** - 了解项目全貌
5. **Test/测试手册.md** - 学习如何测试

### 开发人员
1. **CONFIG_GUIDE.md** - 配置指南（必读）
2. **项目说明.md** - 系统架构和技术栈
3. **.kiro/steering/structure.md** - 代码结构
4. **.kiro/steering/tech.md** - 技术细节
5. **需求说明书.md** - 功能需求

### 产品/需求人员
1. **需求说明书.md** - 完整需求文档
2. **项目说明.md** - 业务流程和功能
3. **.kiro/steering/status.md** - 项目进度

### 测试人员
1. **Test/测试手册.md** - 测试指南
2. **Test/TestReport/TEST_SUMMARY.md** - 测试报告
3. **项目说明.md** - 测试覆盖情况

### 运维人员
1. **CONFIG_GUIDE.md** - 配置指南（必读，含安全最佳实践）
2. **README.md** - 快速启动
3. **快速部署-多用户修复版本.md** - 部署指南
4. **Start Engine/** - 启动脚本

### 安全/项目负责人
1. **CLEANUP_COMPLETED.md** - 敏感信息清理报告（含备份和回滚信息）
2. **CONFIG_GUIDE.md** - 配置指南（含安全最佳实践）

---

## 🔄 文档更新记录

### 2025-12-11（敏感信息清理）
- ✅ **安全改进**: 完成敏感信息清理
- ✅ **配置文档**: 创建配置指南和模板文件
  - CONFIG_GUIDE.md
  - .env.example
  - application-example.yml
  - VRS/lemei/SETUP_GUIDE.md
- ✅ **安全文档**: 创建安全相关文档
  - SECURITY_AUDIT_REPORT.md
  - CLEANUP_COMPLETED.md
  - BACKUP_INFO.md
  - NEXT_STEPS.md
- ✅ **代码重构**: 使用环境变量替代硬编码
  - WeChatParamesUtil.java
  - ExternalServiceConfig.java
- ✅ **配置清理**: 删除包含敏感信息的配置文件
  - application-dev.yml
  - application-prod.yml
  - application-druid.yml
- ✅ **Git配置**: 更新 .gitignore 防止敏感信息泄露
- ✅ **备份**: 创建备份分支 `backup-before-security-cleanup-20251211`
- ✅ **文档更新**: 更新 README.md 和本文档

### 2025-12-11（功能修复）
- ✅ 添加多用户会话隔离修复说明
- ✅ 更新需求说明书安全需求章节
- ✅ 创建技术修复文档和部署指南

### 2025-12-09
- ✅ 重组项目文档结构
- ✅ 创建Test和Start Engine文件夹
- ✅ 创建项目说明文档
- ✅ 创建测试手册
- ✅ 优化文档组织

### 2025-12-05
- ✅ 测试报告中文编码修复
- ✅ 文档整合和优化

---

## 📞 文档维护

### 文档维护原则
1. **及时更新** - 每次重要修改后更新相关文档
2. **保持一致** - 确保各文档信息一致
3. **清晰简洁** - 文档内容清晰易懂
4. **版本控制** - 记录文档修订历史

### 文档维护责任
- **核心文档** - 项目负责人
- **技术文档** - 技术负责人
- **测试文档** - 测试负责人
- **AI助手文档** - 自动更新

### 联系方式
- 文档问题：查看对应文档的维护者信息
- 技术支持：查看项目文档

---

**文档版本**: 2.0  
**最后更新**: 2025-12-11  
**维护者**: 开发团队
