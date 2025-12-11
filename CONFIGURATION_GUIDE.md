！**
常工作否正行测试验证系统是完成后，请运---

**配置- 备份信息

md) NFO.CKUP_I(BA.md]ACKUP_INFO审计报告
- [B) - 安全EPORT.mdDIT_RCURITY_AURT.md](SET_REPO_AUDI
- [SECURITYmd) - 测试指南手册..md](Test/测试 [Test/测试手册细说明
-d) - 项目详说明.m[项目说明.md](项目目快速启动
- - 项ADME.md) ](RE- [README.md

📚 相关文档## 


---
持
4. 联系技术支EPORT.md`_AUDIT_R`SECURITY. 查看安全审计报告: /测试手册.md`
3 `Test试手册:看测
2. 查log`/lemei-api.logs `n.log` 或-admis/lemei`log文件: 看日志：

1. 查如果遇到配置问题

取帮助
## 📞 获设置

---
. 检查 IP 白名单应用是否启用
32. 检查企业微信是否正确
cret rpSecod 和 corpI*:
1. 检查 方案*败`

**解决en失ccess_tok`获取a
**错误**: 失败
 Q4: 微信推送##重启服务

#T 密钥并
3. 重新生成 JW期 是否过
2. 检查 token JWT 密钥相同的使用1. 确保所有服务
**解决方案**:
败`
 `Token验证失
**错误**:效
oken 无# Q3: JWT t

##，配置中留空is 没有密码Red否正确
3. 如果 密码是is Red379`
2. 检查  findstr 6at -ano |`netsts 是否启动: 1. 检查 Redi方案**:
**解决

edis`o Rto connect te *: `Unabl**错误*s 连接失败

: Redi
### Q2查防火墙设置
L 是否正确
4. 检
3. 检查数据库 UR户名密码是否正确6`
2. 检查用indstr 330| fetstat -ano : `n 检查数据库是否启动解决方案**:
1.e`

**nk failurations li*: `Communic败

**错误*1: 数据库连接失

### Q题

## ❓ 常见问

---
```有测试通过# 应该看到所.bat

un-tests元测试
Test\r`bash
# 运行单
``行测试
 运# 4.##``

'
`23"}d":"admin1","passwor:"admin"username"-d '{on" \
  plication/jsape: Typnt-H "Conte  -n \
logit:8066/dev/alhos http://locl -X POST```bash
cur正常：
 是否返回的 token，查看登录系统置

JWT 配## 3. 检查 

#```
> keys *
redis-cliash
数据：
```b 是否有edis的接口，查看 R

访问任意需要缓存 连接is## 2. 检查 Red
```

#"mpleted- Start coiPool-1 "Hikar到类似:  应该看接成功的日志
#

# 查找数据库连min.logi-ademeogs/l -f ltail查看日志
sh
# 启动应用后

```ba查数据库连接 检 1.###

## 🧪 验证配置


```

---sql-data:mes:
  myvoluSWORD}

REDIS_PASepass ${requir-server --: redis   command:7-alpine
 disimage: re:
      redisql

ar/lib/mysa:/vmysql-dat - :
     volumesoking
    E=carboTABASMYSQL_DA      - 
DB_PASSWORD}ORD=${OT_PASSWQL_RO - MYSment:
     environ
    l:8.0mysqge: 
    imal:sq  my

 - redisysql
           - mon:
depends_
       - .envle:
   env_fi  
  PORT=6379   - REDIS_   edis
S_HOST=rEDI  - RECRET}
    ${JWT_SET=SECR   - JWT_SWORD}
   D=${DB_PASB_PASSWOR}
      - DB_USERNAMEERNAME=${D DB_US      -rbooking
B_NAME=ca    - D306
  _PORT=3   - DB  T=mysql
 B_HOS  - Dt:
    ronmen
    envi6:8066"- "806   s:
     port
  in:latestei-admem l image:admin:
   emei-ices:
  l.8'

servn: '3yaml
versio
```.yml
er-compose dock

### Docker 部署： 配置

如果使用# 🐳 Docker-

#`

--rc
`` ~/.bashrc
source ~/.bashrd' >>ur_passwoWORD=yort DB_PASS
echo 'expoc
```bash/.zshrshrc 或 ~3: 添加到 ~/.ba
#### 方法 ``

`jaremei-admin.jar l)
java -gst .env | xarport $(caenv 文件
exsh
# 加载 .```ba文件
v en用 . 方法 2: 使`

####r
``admin.jar lemei-java -jasword
your_pas_PASSWORD=t DB
exporbash变量
``` 方法 1: 导出环境
####
# Linux/Mac

##in.jar
```admmei-java -jar le%%a
set v) do  (.en=*" %%a in "tokensff
for /fo o```cmd
@ech加载：
然后使用启动脚本nv` 文件，推荐）
创建 `.ev 文件（ 使用 .en
#### 方法 3:ar
```
i-admin.j lemeva -jarjar_password
RD=youWOset DB_PASS
```cmd
: 命令行临时设置 2 方法量

####变量或系统变. 添加新的用户"
3"环境变量统设置" → 
2. "高级系→ "属性"此电脑" 
1. 右键": 系统环境变量## 方法 1
## Windows
配置方式

###
## 🌍 环境变量-
``

--PKCS12
`: pere-tyey-stoord
    kswre_pasour_keystoord: yore-passw  key-st.p12
  keystore: classpath:  key-storee
  truled:     enab ssl:

server:
 ```yamlTTPS：
境必须使用 H
生产环TTPS
# 5. 使用 H 文档

##gerSwag用  生产环境禁 白名单
-控设置 IP限
- Druid 监据库用户只授予必要的权 数问权限

-制访## 4. 限企业号密钥

#密码
- 微信WT 密钥
- 数据库
- J次： 3-6 个月更换一期轮换密钥

建议每# 3. 定
##密码：至少 8 位
ruid 位随机字符串
- D密钥：至少 32 符
- JWT 母、数字和特殊字小写字包含大库密码：至少 12 位，- 数据密码

 2. 使用强
```

###esUtil.javaatParamCh/Weod.yml
**on-pr*/applicati.yml
*on-devtica
**/applinv.*e
.env
.egitignor含：
```` 包.gitignoret

确保 `要提交敏感信息到 Gi### 1. 不全最佳实践

--

## 🔒 安``

-=prod
`.activeilesg.profjar --sprinmin.ei-ad lemjarva -
```bash
ja通过命令行：
```

或prod, test, evv  # 可选: dve: deti
    aces:g:
  profilml
sprintion.yl
# applica``yam
`境：
ive` 指定环ctofiles.aprring.换

通过 `sp

### 环境切. 命令行参数3. 环境变量
4- 环境特定配置
ofile}.yml` ication-{pr2. `appl` - 基础配置
ation.yml `applic

1.后面的会覆盖前面的）：配置（序加载g Boot 按以下顺

Sprin文件优先级明

### 配置📝 配置文件说
## 

---
-ip
```r-esb youB:
  ip:yaml
ES
```B 系统：
要对接 ES

如果需B 配置/

### ES8601/druidlocalhost:或 http://id/ t:8066/drucalhos://lo地址**: http
**访问ord
```
: your_passwword-passin
    logadmin-username:   logint:
  leServew:
  statViuid``yaml
dr
`
Druid 监控配置

### 中查看企业 ID业"我的企t
4. 在"ntId 和 Secre详情获取 Age看应用用管理"
3. 查进入"应业微信管理后台
2. 1. 登录企方式**:

```

**获取D=1000002AGENT_I
WECHAT_-secretET=your-corpCORP_SECRCHAT_corp-id
WE_ID=your-T_CORPsh
WECHA
```ba
**环境变量**:```
2"));
0000", "10AT_AGENT_IDult("WECHOrDefaetenv().getstem.grseInt(Sy Integer.pa agentId =l static intnaublic fi;
pCRET")_CORP_SEv("WECHATeten = System.gpsecret String coral static
public fin");CORP_IDWECHAT_em.getenv("rpId = Systg coinic Stratstlic final pubava
ava`

```jUtil.jatParames/WeChutilsommon/om/lemei/cmain/java/cc/common/sremei-`VRS/lemei/l

**文件**: ：能，需要配置业号推送功需要使用微信企企业号配置

如果项

### 微信
## 🔧 可选配置

```

---留空  # 如果没有密码，swordasedis_pur_rpassword: yo: 6379
    t
    portosalhoct: l:
    hos
  redisring:ml
sp置

```yas 配## Redi))
```

#imum 256 }om -Maxet-Randt { GEach-Objec.32 | For4String((1.:ToBase6nvert]:hell
[CorS Powe32

# 或使用e64 sl rand -bas随机密钥
opens 生成SSL# 使用 Openash
``b**:
`
**生成方式ey
```
-secret-k-jwtcret: yourtoken:
  se
```yaml
钥
T 密## JWL 数据库

#ySQ本地 M
- 开发环境可以使用库连接信息管理员获取数据*:
- 联系数据库`

**获取方式*``ssword
par_sword: you
        passername your_u username:
       T%2B8e=GMimezontrue&serverTNull&useSSL=rtTo=conveehaviorDateTimeBf8&zerorEncoding=utctee=true&charaicodng?useUnkirboo3306/calocalhost:ql://dbc:mysurl: j   
        master:uid:
       drrce:
asoung:
  dat``yaml
spri

`配置### 数据库项

必需的配置🔐 
## 
实际值。

---OUR_*` 占位符为替换所有 `Yyml` 文件，n-dev.`applicatio配置信息

编辑  3. 填写###`

.yml
``devion-es/applicatmain/resourcmin/src/mei-adS/lemei/le VRle.ymlon-examppplicatiesources/a/main/radmin/srcmei-mei/lele VRS/min 模块
cp# Adev.yml

plication-durces/apesoi/src/main/rmei-apVRS/lemei/lexample.yml cation-eurces/appli/main/resoemei-api/srcmei/lS/le VR
cpAPI 模块
# 

```bash配置文件示例
### 2. 复制
```
x/Macnu     # Li
vim .env  或 Windows
#.env  #
notepad 置值写实际的配，填辑 .env 文件 编le .env

#examp件
cp .env.复制环境变量示例文
# ash件

```b环境变量示例文 1. 复制

##### 🚀 快速开始---

中。

硬编码在代码变量或配置中心管理，不要密钥等）都应该通过环境全，所有敏感信息（密码、**重要**: 为了安。

⚠️ 量和敏感信息辆预约系统的环境变如何配置乐美车档说明📋 概述

本文--

## 5-12-11

-新**: 202.0  
**最后更: 3.7版本**指南

** 配置# 🔧