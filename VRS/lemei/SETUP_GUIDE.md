# 🚀 后端服务配置指南

**重要**: 首次运行前必须完成配置！

---

## 📋 快速开始

### 1. 复制配置文件

```bash
# 进入项目目录
cd VRS/lemei

# 复制 API 模块配置
cp lemei-api/src/main/resources/application-example.yml lemei-api/src/main/resources/application-dev.yml

# 复制 Admin 模块配置
cp lemei-admin/src/main/resources/application-example.yml lemei-admin/src/main/resources/application-dev.yml
```

### 2. 编辑配置文件

打开 `application-dev.yml` 文件，替换以下占位符：

```yaml
# 数据库配置
url: jdbc:mysql://YOUR_DB_HOST:3306/YOUR_DB_NAME?...
username: YOUR_DB_USERNAME
password: YOUR_DB_PASSWORD

# Redis 配置
host: YOUR_REDIS_HOST
password: YOUR_REDIS_PASSWORD

# Druid 监控
login-username: YOUR_DRUID_USERNAME
login-password: YOUR_DRUID_PASSWORD

# ESB 配置
ip: YOUR_ESB_IP
```

### 3. 配置微信企业号（可选）

如果需要使用微信推送功能，设置环境变量：

**Windows**:
```cmd
set WECHAT_CORP_ID=your-corp-id
set WECHAT_CORP_SECRET=your-corp-secret
set WECHAT_AGENT_ID=1000002
```

**Linux/Mac**:
```bash
export WECHAT_CORP_ID=your-corp-id
export WECHAT_CORP_SECRET=your-corp-secret
export WECHAT_AGENT_ID=1000002
```

### 4. 配置外部服务 URL（可选）

在 `application.yml` 中添加：

```yaml
# ESB 服务配置
esb:
  service:
    url: https://your-esb-server:8020/esb/comm/service

# SAP 服务配置
sap:
  service:
    url: https://your-sap-server:9020/sap/SD024/third/noparams
```

---

## 🔐 必需的配置项

### 数据库

- **DB_HOST**: 数据库服务器地址（如：localhost 或 172.18.165.25）
- **DB_NAME**: 数据库名称（carbooking）
- **DB_USERNAME**: 数据库用户名
- **DB_PASSWORD**: 数据库密码

### Redis

- **REDIS_HOST**: Redis 服务器地址（如：localhost）
- **REDIS_PASSWORD**: Redis 密码（如果没有密码，留空）

### JWT

- **JWT_SECRET**: JWT 密钥（在 application.yml 中配置）

---

## 🧪 验证配置

### 1. 编译项目

```bash
cd VRS/lemei
mvn clean package
```

### 2. 启动服务

```bash
# 启动 Admin 服务
cd ../../
"Start Engine\start-backend-fixed.bat"

# 启动 API 服务
"Start Engine\start-api.bat"
```

### 3. 检查日志

查看日志文件确认服务启动成功：
- Admin: `VRS/lemei/lemei-admin/logs/`
- API: `VRS/lemei/lemei-api/logs/`

### 4. 运行测试

```bash
cd ../../
Test\run-tests.bat
```

---

## ❓ 常见问题

### Q: 找不到配置文件

**A**: 确保已经从 `application-example.yml` 复制配置文件到 `application-dev.yml`

### Q: 数据库连接失败

**A**: 检查：
1. 数据库是否启动
2. 用户名密码是否正确
3. 数据库 URL 是否正确
4. 防火墙设置

### Q: 微信推送不工作

**A**: 检查：
1. 环境变量是否设置正确
2. corpId 和 corpSecret 是否有效
3. 企业微信应用是否启用

---

## 📚 相关文档

- [配置指南](../../CONFIG_GUIDE.md) - 详细的配置说明
- [安全审计报告](../../SECURITY_AUDIT_REPORT.md) - 安全最佳实践
- [项目说明](../../项目说明.md) - 项目架构和技术栈

---

**配置完成后，请运行测试验证系统是否正常工作！**
