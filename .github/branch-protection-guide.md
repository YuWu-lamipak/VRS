# 分支保护设置指南

## 🛡️ GitHub 分支保护设置

### 1. 进入仓库设置
```
你的仓库 → Settings → Branches
```

### 2. 添加分支保护规则
点击 "Add rule" 按钮

### 3. 配置保护规则

#### 基本设置
- **Branch name pattern**: `main` 或 `public-code`
- **Restrict pushes that create files larger than**: `100 MB`

#### 必需检查 ✅
- ☑️ **Require a pull request before merging**
  - ☑️ Require approvals: `1`
  - ☑️ Dismiss stale PR approvals when new commits are pushed
  - ☑️ Require review from code owners

- ☑️ **Require status checks to pass before merging**
  - ☑️ Require branches to be up to date before merging
  - 添加必需的状态检查（如果有CI/CD）

- ☑️ **Require conversation resolution before merging**

#### 高级保护 🔒
- ☑️ **Restrict pushes that create files larger than 100 MB**
- ☑️ **Require linear history**
- ☑️ **Include administrators** (可选，更严格)

### 4. 保存设置
点击 "Create" 保存规则

## 🎯 效果说明

### ✅ 设置后的效果：
- 项目公开可见，任何人可以查看和Fork
- 主分支受保护，无法直接推送
- 所有修改必须通过Pull Request
- 你作为管理员审核所有更改
- 保持项目活跃状态，可以持续开发

### 🔄 贡献流程：
1. 其他人Fork你的项目
2. 在自己的Fork中进行修改
3. 创建Pull Request到你的主分支
4. 你审核并决定是否合并
5. 合并后自动更新主分支

## 📞 如果需要帮助
- GitHub文档: https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/about-protected-branches
- 或者在项目中创建Issue询问