<template>
  <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
          <el-form-item :label="$t('zcc')" prop="userName">
            <el-input v-model="queryParams.userName" :placeholder="$t('aacc')" clearable size="small" style="width: 245px"
              @keyup.enter.native="handleQuery" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('select') }}</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('q') }}</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
              v-hasPermi="['system:driver:add']">{{ $t('a') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['system:driver:edit']">{{ $t('f') }}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['system:driver:export']">{{ $t('e') }}</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55px" align="center" />
          <el-table-column :label="$t('ggga')" align="center" key="userId" prop="userId" />
          <el-table-column :label="$t('zcc')" align="center" key="userName" prop="userName"
            :show-overflow-tooltip="true" />
          <el-table-column :label="$t('zcb')" align="center" key="nickName" prop="nickName"
            :show-overflow-tooltip="true" />
          <el-table-column :label="$t('gxa')" align="center" key="carrierName" prop="carrierName"
            :show-overflow-tooltip="true" />
          <!--         <el-table-column label="手机号码" align="center" key="phoneNumber" prop="phoneNumber"  width="120" />-->
          <el-table-column :label="$t('taa')" align="center" key="isBlacklist">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.isBlacklist" :active-value=1 :inactive-value=0
                @change="handleBlackChange(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column :label="$t('va')" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('lll')" align="center" width="160" class-name="small-padding fixed-width">
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['system:driver:edit']">{{ $t('f') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['system:driver:remove']">{{ $t('uaa') }}</el-button>
              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                v-hasPermi="['system:driver:resetPwd', 'system:driver:edit']">
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>{{ $t('taaa') }}
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                    v-hasPermi="['system:driver:resetPwd']">{{ $t('uaaa') }}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="850px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('zcc')" prop="userName">
              <el-input v-model="form.userName" :placeholder="$t('aacc')" maxlength="30" />
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" :label="$t('uaaaa')" prop="password">
              <el-input v-model="form.password" :placeholder="$t('yaa')" type="password" maxlength="20" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>

          <el-col :span="12">
            <el-form-item :label="$t('zcb')" prop="nickName">
              <el-input v-model="form.nickName" :placeholder="$t('zcbb')" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('gxa')" prop="parentId">
              <el-select v-model="form.parentId" :placeholder="$t('aff')">
                <el-option v-for="item in carrier" :key="item.userId" :label="item.nickName"
                  :value="item.userId"></el-option>
              </el-select>

            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('accc')" prop="idcard">
              <el-input v-model="form.idcard" :placeholder="$t('addd')" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('agg')" prop="phoneNumber">
              <el-input v-model="form.phoneNumber" :placeholder="$t('abbb')" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('ixx')" prop="userType" style="display: none">
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('ggg')">
              <el-input v-model="form.remark" type="textarea" :placeholder="$t('hxa')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="cancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserBlack } from "@/api/system/user";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  dicts: ['sys_normal_disable', 'sys_user_sex', 'user_type'],
  name: "User",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      //承运商-新增司机时用
      carrier: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phoneNumber: undefined,
        status: undefined,
        deptId: undefined,
        userType: '02'
      },
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "账号不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        idcard: [
          {
            required: true,
            pattern: /^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "请输入正确的身份证号码",
            trigger: "blur"
          }
        ],
        parentId: [
          { required: true, type: 'number', message: "所属承运商不能为空", trigger: "blur" } ||
          { required: true, type: 'String', message: "所属承运商不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },

    // 设置黑名单
    handleBlackChange(row) {
      let text = row.isBlacklist === 0 ? "取消黑名单" : "设置黑名单";
      this.$modal.confirm('确认要将该用户"' + text + '"吗？').then(function () {
        return changeUserBlack(row.userId, row.isBlacklist);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.isBlacklist = row.isBlacklist === 0 ? 1 : 0;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phoneNumber: undefined,
        userType: "02",
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        carrie: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getUser().then(response => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.carrier = response.carriers;
        this.open = true;
        this.title = this.$t('tjsj');
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.carrier = response.carriers;
        this.open = true;
        this.title = this.$t('xgsj');
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
      }).then(({ value }) => {
        resetUserPwd(row.userId, value).then(response => {
          this.$modal.msgSuccess("修改成功，新密码是：" + value);
        });
      }).catch(() => { });
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lmm5'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId;
      const userName = row.userName;
      this.$modal.confirm(this.$t('sfsc2') + '"' + userName + '"?').then(function () {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('sccg'));
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `司机列表.xlsx`)
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },

  }
};
</script>

