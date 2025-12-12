# 🛡️ VRS项目分支保护设置指南

## 📋 设置步骤（请按顺序执行）

### 第1步：进入GitHub仓库设置
1. 打开你的GitHub仓库：https://github.com/wagalicn/VRS
2. 点击页面顶部的 **Settings** 选项卡
3. 在左侧菜单中找到 **Branches**

### 第2步：添加分支保护规则
1. 点击 **Add rule** 按钮
2. 在 **Branch name pattern** 中输入：`public-code`

### 第3步：配置保护选项（重要！）

#### ✅ 必须勾选的选项：

**基本保护**：
- ☑️ **Require a pull request before merging**
  - ☑️ **Require approvals**: 设置为 `1`
  - ☑️ **Dismiss stale PR approvals when new commits are pushed**
  - ☑️ **Require review from code owners**

**状态检查**：
- ☑️ **Require status checks to pass before merging**
  - ☑️ **Require branches to be up to date before merging**

**其他重要选项**：
- ☑️ **Require conversation resolution before merging**
- ☑️ **Require linear history**
- ☑️ **Restrict pushes that create files larger than 100 MB**

#### ⚠️ 可选设置：
- ☑️ **Include administrators** (如果你想对自己也应用这些规则)

### 第4步：保存设置
1. 滚动到页面底部
2. 点击 **Create** 按钮保存规则

## 🎯 设置完成后的效果

### ✅ 你将获得：
- **完全控制权**：只有你能批准和合并PR
- **代码安全**：无人能直接推送到主分支
- **质量保证**：所有更改都经过审核
- **公开展示**：项目对所有人可见和学习

### 🔄 其他人的贡献流程：
1. **Fork你的项目** → 创建自己的副本
2. **在Fork中修改** → 进行开发工作
3. **创建Pull Request** → 提交更改请求
4. **等待你审核** → 你决定是否接受
5. **合并到主分支** → 你控制最终代码

## 🔍 验证设置是否成功

### 测试方法：
1. 尝试直接推送到 `public-code` 分支（应该被拒绝）
2. 创建一个测试分支进行修改
3. 创建PR到 `public-code` 分支（应该需要审核）

### 预期结果：
```bash
# 这个命令应该失败
git push origin public-code
# 错误信息：remote: error: GH006: Protected branch update failed
```

## 🚨 常见问题解决

### Q: 设置后我自己也不能推送了？
**A**: 这是正常的！现在你需要：
- 创建新分支进行开发
- 通过PR合并到主分支
- 或者取消勾选 "Include administrators"

### Q: 如何添加可信任的协作者？
**A**: 
1. Settings → Manage access → Invite a collaborator
2. 给予 "Write" 或 "Maintain" 权限
3. 他们仍需通过PR流程，但可以创建分支

### Q: 如何临时关闭保护？
**A**: 
1. Settings → Branches → 找到规则
2. 点击 "Edit" 或 "Delete"
3. 完成紧急修改后重新启用

## 📞 需要帮助？
- 📧 在项目中创建Issue
- 📚 查看 [GitHub官方文档](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/about-protected-branches)
- 💬 联系项目维护者

---

**⚡ 快速链接**：
- 🔗 [直接进入分支设置](https://github.com/wagalicn/VRS/settings/branches)
- 📋 [查看当前保护规则](https://github.com/wagalicn/VRS/settings/branches)
- 🎯 [测试PR流程](https://github.com/wagalicn/VRS/compare)