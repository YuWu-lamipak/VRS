# 🔒 项目敏感信息审计报告

**生成时间**: 2025-12-11  
**项目**: 乐美车辆预约系统 (KS-VRS)  
**目的**: 公开仓库前的安全检查

---

## ⚠️ 严重警告

**当前状态**: 项目包含大量敏感信息，**绝对不能直接公开**！

---

## 📋 敏感信息清单

### 🔴 1. 数据库凭证（高危）

#### 生产环境数据库
**位置**: 多个配置文件
- `VRS/lemei/lemei-admin/src/main/resources/application-druid.yml`
- `VRS/lemei/lemei-admin/src/main/resources/application-dev.yml`
- `VRS/lemei/lemei-api/src/main/resources/application-prod.yml`
- `VRS/lemei/lemei-api/src/main/resources/application-dev.yml`

**敏感内容**:
```yaml
# 生产环境数据库
url: jdbc:mysql://172.18.165.25:3306/carbooking?
username: carbook
password: LM!202pkS4cb!

# 测试环境数据库（注释中）
url: jdbc:mysql://172.18.165.44:6446/lemei?
username: carbook
password: LmKP#2024Ca!rBoo!k
```

**风险等级**: 🔴 极高
- 暴露生产数据库 IP 地址
- 暴露数据库用户名和密码
- 可能导致数据泄露或被篡改

---

### 🔴 2. JWT 密钥（高危）

**位置**:
- `VRS/lemei/lemei-api/src/main/resources/application.yml`
- `VRS/lemei/lemei-admin/src/main/resources/application.yml`

**敏感内容**:
```yaml
token:
  secret: wqw1300421223ss
```

**风险等级**: 🔴 极高
- 攻击者可伪造任意用户的 JWT token
- 可绕过身份认证系统
- 可获取任意用户权限

---

### 🔴 3. 微信企业号凭证（高危）

**位置**: `VRS/lemei/lemei-common/src/main/java/com/lemei/common/utils/WeChatParamesUtil.java`

**敏感内容**:
```java
// 测试版本
public final static String corpId = "ww284525d5744f4627";
public final static String corpsecret = "59DLU3CAIxY__mqhaBPUmS6dHCHOoIXff-2RtdYZFfw";
public final static int agentId = 1000002;

// 生产版本（注释中）
// public final static String corpId = "wwd46492b11746d9d8";
// public final static String corpsecret = "1N5AOw4iCuyEAhbZha1w-av0ljdADn_lBKyr8TOmSv0";
// public final static int agentId = 1000037;
```

**风险等级**: 🔴 极高
- 攻击者可冒充企业发送消息
- 可能泄露企业通讯录信息
- 可能造成企业声誉损失

---

### 🟠 4. 微信小程序 AppID（中危）

**位置**:
- `VRS/LMXCX/ym_car_xcx/ym_car_xcx/src/manifest.json`
- `VRS/LMXCX/ym_car_xcx/ym_car_xcx/project.config.json`
- `VRS/LMXCX/ym_car_xcx/project.config.json`
- `VRS/LMXCX/project.config.json`

**敏感内容**:
```json
"appid": "wx21fe2e8e5ff6358e"
"appid": "wx284f50b029132b10"
"appid": "wxa8689fcb4c1b59b2"
```

**风险等级**: 🟠 中等
- 暴露小程序身份信息
- 可能被用于钓鱼攻击
- 建议使用环境变量

---

### 🟠 5. 内网 IP 地址（中危）

**位置**: 多个配置文件和代码文件

**敏感内容**:
```yaml
# 数据库服务器
172.18.165.25:3306
172.18.165.44:6446

# ESB 服务器
172.18.165.151:8020
172.18.165.151:9020
```

**风险等级**: 🟠 中等
- 暴露内网拓扑结构
- 可能被用于内网渗透
- 建议使用环境变量或配置中心

---

### 🟠 6. Druid 监控控制台凭证（中危）

**位置**:
- `VRS/lemei/lemei-api/src/main/resources/application-prod.yml`
- `VRS/lemei/lemei-api/src/main/resources/application-dev.yml`
- `VRS/lemei/lemei-admin/src/main/resources/application-druid.yml`
- `VRS/lemei/lemei-admin/src/main/resources/application-dev.yml`

**敏感内容**:
```yaml
stat-view-servlet:
  login-username: ruoyi
  login-password: ww1300421296
```

**风险等级**: 🟠 中等
- 可访问数据库连接池监控信息
- 可能泄露 SQL 查询信息
- 可能导致性能问题

---

### 🟡 7. 外部 API 地址（低危）

**位置**: `VRS/lemei/lemei-admin/src/main/java/com/lemei/adminweb/system/LmCarApplicationController.java`

**敏感内容**:
```java
// ESB 接口
String url = "https://172.18.165.151:8020/esb/comm/service";

// SAP 接口
String url = "https://esbapi.lamipak.biz:9020/sap/SD024/third/noparams";
```

**风险等级**: 🟡 低
- 暴露第三方集成信息
- 可能被用于接口探测
- 建议使用配置文件

---

### 🟡 8. 数据库结构和测试数据（低危）

**位置**: `VRS/lemei/sql/carbooking.sql`

**敏感内容**:
- 完整的数据库表结构
- 可能包含测试数据（手机号、身份证号、邮箱等）

**风险等级**: 🟡 低
- 暴露业务逻辑和数据模型
- 可能包含真实用户数据
- 建议使用脱敏数据

---

## 🛠️ 修复建议

### 方案 1: 使用环境变量（推荐）

#### 1.1 修改配置文件

**application.yml**:
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/carbooking}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}

token:
  secret: ${JWT_SECRET:your-secret-key}

wechat:
  corpId: ${WECHAT_CORP_ID:your-corp-id}
  corpSecret: ${WECHAT_CORP_SECRET:your-corp-secret}
  agentId: ${WECHAT_AGENT_ID:1000002}
```

#### 1.2 创建 .env.example 文件

```bash
# 数据库配置
DB_URL=jdbc:mysql://localhost:3306/carbooking
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT 配置
JWT_SECRET=your-jwt-secret-key

# 微信企业号配置
WECHAT_CORP_ID=your-corp-id
WECHAT_CORP_SECRET=your-corp-secret
WECHAT_AGENT_ID=1000002

# ESB 配置
ESB_IP=your-esb-ip
```

#### 1.3 更新 .gitignore

```gitignore
# 环境变量文件
.env
.env.local
.env.production

# 配置文件
**/application-prod.yml
**/application-dev.yml
**/application-druid.yml

# 敏感配置
**/WeChatParamesUtil.java
```

---

### 方案 2: 使用配置中心（企业级）

推荐使用：
- Spring Cloud Config
- Nacos
- Apollo
- Consul

---

### 方案 3: 创建示例配置文件

保留配置文件结构，但使用占位符：

**application-example.yml**:
```yaml
spring:
  datasource:
    url: jdbc:mysql://YOUR_DB_HOST:3306/YOUR_DB_NAME
    username: YOUR_DB_USERNAME
    password: YOUR_DB_PASSWORD

token:
  secret: YOUR_JWT_SECRET_KEY
```

---

## ✅ 必须执行的步骤

### 步骤 1: 立即更改所有密码和密钥

- [ ] 更改数据库密码
- [ ] 重新生成 JWT 密钥
- [ ] 重置微信企业号密钥
- [ ] 更改 Druid 监控密码

### 步骤 2: 清理 Git 历史

⚠️ **警告**: 即使删除文件，Git 历史中仍然保留！

```bash
# 使用 BFG Repo-Cleaner 清理敏感信息
# 1. 安装 BFG
# 2. 创建敏感词列表
echo "LM!202pkS4cb!" > passwords.txt
echo "wqw1300421223ss" >> passwords.txt
echo "59DLU3CAIxY__mqhaBPUmS6dHCHOoIXff-2RtdYZFfw" >> passwords.txt

# 3. 清理历史
bfg --replace-text passwords.txt

# 4. 清理引用
git reflog expire --expire=now --all
git gc --prune=now --aggressive

# 5. 强制推送（危险操作！）
git push --force
```

### 步骤 3: 更新配置文件

- [ ] 将所有敏感信息替换为环境变量
- [ ] 创建 `.env.example` 示例文件
- [ ] 更新 `.gitignore` 忽略敏感文件
- [ ] 创建配置文档说明如何设置环境变量

### 步骤 4: 数据脱敏

- [ ] 检查 SQL 文件中的测试数据
- [ ] 替换真实手机号为 `138****0000`
- [ ] 替换真实身份证号为 `110101199001010000`
- [ ] 替换真实邮箱为 `example@example.com`

### 步骤 5: 代码审查

- [ ] 检查所有 `.java` 文件中的硬编码凭证
- [ ] 检查所有 `.yml` 和 `.properties` 文件
- [ ] 检查所有 `.js` 和 `.json` 文件
- [ ] 检查所有 `.bat` 和 `.ps1` 脚本

### 步骤 6: 文档更新

- [ ] 更新 README.md，添加环境变量配置说明
- [ ] 创建 SECURITY.md 安全策略文档
- [ ] 创建 CONTRIBUTING.md 贡献指南
- [ ] 添加 LICENSE 文件

---

## 📝 推荐的 .gitignore 配置

```gitignore
# 编译输出
target/
dist/
out/
*.class
*.jar
*.war

# 日志文件
logs/
*.log

# 环境变量
.env
.env.local
.env.production
.env.*.local

# 敏感配置文件
**/application-prod.yml
**/application-dev.yml
**/application-druid.yml
**/WeChatParamesUtil.java

# IDE 配置
.idea/
.vscode/
*.iml
*.ipr
*.iws

# 操作系统
.DS_Store
Thumbs.db

# 依赖
node_modules/
.mvn/

# 测试报告（可选）
TestReport/
*.txt
```

---

## 🎯 公开前检查清单

### 代码层面
- [ ] 所有密码已更改
- [ ] 所有密钥已重新生成
- [ ] 配置文件使用环境变量
- [ ] Git 历史已清理
- [ ] 测试数据已脱敏

### 文档层面
- [ ] README.md 已更新
- [ ] 添加了环境变量配置说明
- [ ] 添加了 LICENSE 文件
- [ ] 添加了 SECURITY.md
- [ ] 添加了 CONTRIBUTING.md

### 安全层面
- [ ] 生产环境凭证已轮换
- [ ] 微信企业号密钥已重置
- [ ] 数据库密码已更改
- [ ] JWT 密钥已重新生成
- [ ] 所有团队成员已知晓变更

### 测试层面
- [ ] 使用新凭证测试所有功能
- [ ] 确认旧凭证已失效
- [ ] 验证环境变量配置正确
- [ ] 运行完整测试套件

---

## 🚨 紧急情况处理

如果敏感信息已经泄露：

1. **立即更改所有密码和密钥**
2. **检查数据库访问日志**
3. **检查应用访问日志**
4. **通知安全团队**
5. **评估影响范围**
6. **制定应急响应计划**

---

## 📞 联系方式

如有安全问题，请联系：
- 安全团队邮箱: security@example.com
- 项目负责人: [联系方式]

---

**最后更新**: 2025-12-11  
**审计人员**: Kiro AI Assistant  
**下次审计**: 建议每季度进行一次安全审计
