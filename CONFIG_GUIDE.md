# 🔧 配置指南

**版本**: 3.7.0  
**最后更新**: 2025-12-11

---

## 📋 概述

本文档说明如何配置乐美车辆预约系统的环境变量和敏感信息。

⚠️ **重要**: 为了安全，所有敏感信息（密码、密钥等）都应该通过环境变量或配置中心管理，不要硬编码在代码中。

---

## 🚀 快速开始

### 1. 复制环境变量示例文件

```bash
# 复制环境变量示例文件
cp .env.example .env

# 编辑 .env 文件，填写实际的配置值
notepad .env
```

### 2. 复制配置文件示例

```bash
# API 模块
cp VRS/lemei/lemei-api/src/main/resources/application-example.yml VRS/lemei/lemei-api/src/main/resources/application-dev.yml

# Admin 模块
cp VRS/lemei/lemei-admin/src/main/resources/application-example.yml VRS/lemei/lemei-admin/src/main/resources/application-dev.yml
```

### 3. 填写配置信息

编辑 `application-dev.yml` 文件，替换所有 `YOUR_*` 占位符为实际值。

---

## 🔐 必需的配置项

### 数据库配置

```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/carbooking
        username: your_username
        password: your_password
```

### JWT 密钥

```yaml
token:
  secret: your-jwt-secret-key
```

**生成方式**:
```bash
# 使用 OpenSSL
openssl rand -base64 32

# 或使用 PowerShell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

### Redis 配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
```

---

## 🔒 安全最佳实践

1. **不要提交敏感信息到 Git**
2. **使用强密码**（至少 12 位）
3. **定期轮换密钥**（每 3-6 个月）
4. **限制访问权限**
5. **生产环境使用 HTTPS**

---

## 📚 相关文档

- [README.md](README.md) - 项目快速启动
- [SECURITY_AUDIT_REPORT.md](SECURITY_AUDIT_REPORT.md) - 安全审计报告
- [BACKUP_INFO.md](BACKUP_INFO.md) - 备份信息

---

**配置完成后，请运行测试验证系统是否正常工作！**
