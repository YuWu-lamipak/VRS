<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('k')" prop="queryCondition">
        <el-input v-model="queryParams.queryCondition" :placeholder="$t('aaa')" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>

      <!-- <el-form-item :label="$t('请输入交货单号')" prop="vrsDnnum">
        <el-input v-model="queryParams.vrsDnnum" :placeholder="$t('请输入交货单号')" clearable size="small" @keyup.enter.native="handleQuery"></el-input>
      </el-form-item>

      <el-form-item :label="$t('请输入包装ID')" prop="vrsPackagingid">
        <el-input v-model="queryParams.vrsPackagingid" :placeholder="$t('请输入包装ID')" clearable size="small" @keyup.enter.native="handleQuery"></el-input>
      </el-form-item> -->

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('o') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('q') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-edit" size="mini" @click="deliveryOrderHandleUpdate"
          v-hasPermi="['system:application:updateDeliveryOrder']">{{ $t('gxjhd')
          }}</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleSaleDelete"
          v-hasPermi="['system:application:saleDelete']">{{ $t('uaa')
          }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <div>
      <el-table ref="table" v-loading="loading" :data="saleList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="交货单Id" align="center" prop="vrsId" v-if="false" />
        <el-table-column :label="$t('x')" align="center" prop="vrsOrderid" />
        <el-table-column :label="$t('sjcc')" align="center" prop="vrsActualdeliverydate" />
        <el-table-column :label="$t('ad')" align="center" prop="vrsCarNumber" />
        <el-table-column :label="$t('jkk1')" align="center" prop="vrsDnnum" />
        <el-table-column :label="$t('zkk1')" align="center" prop="vrsDnLine" />
        <el-table-column :label="$t('bz')" align="center" prop="vrsPackagingid" />
        <el-table-column :label="$t('nnz')" align="center" prop="vrsGrossweight" />
        <!-- <el-table-column :label="$t('lll')" align="center" fixed="right" class-name="small-padding fixed-width"
          width="130px">
          <template slot-scope="scope">
            <div>
              <el-button plain size="small" type="primary" icon="el-icon-edit" @click="handleSaleUpdate(scope.row)" v-hasPermi="['system:application:saleEdit']" >{{ $t('f') }}</el-button>
            </div>
          </template>
</el-table-column> -->
      </el-table>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 修改交货单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('x')" prop="vrsOrderid" label-width="300px">
          <el-input style="width: 270px" v-model="form.vrsOrderid" :placeholder="$t('yyhmn')" />
        </el-form-item>

        <el-form-item :label="$t('ad')" prop="vrsCarNumber" label-width="300px">
          <el-input style="width: 270px" v-model="form.vrsCarNumber" :placeholder="$t('ae')" readonly />
        </el-form-item>

        <el-form-item :label="$t('jkk1')" prop="vrsDnnum" label-width="300px"
          :rules="[{ required: true, message: $t('lm'), trigger: ['blur', 'change'] }]">
          <el-input style="width: 270px" v-model="form.vrsDnnum" :placeholder="$t('abbb')" readonly />
        </el-form-item>

        <el-form-item :label="$t('bz')" prop="vrsPackagingid" label-width="300px"
          :rules="[{ required: true, message: $t('lm1'), trigger: ['blur', 'change'] }]">
          <el-input style="width: 270px" v-model="form.vrsPackagingid" :placeholder="$t('addd')" readonly />
        </el-form-item>

        <el-form-item :label="$t('nn')" prop="vrsGrossweight" label-width="300px"
          :rules="[{ required: true, message: $t('lm1'), trigger: ['blur', 'change'] }]">
          <el-input style="width: 270px" v-model="form.vrsGrossweight" :placeholder="$t('addd')" readonly />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSaleForm">{{ $t('accccc') }}</el-button>
        <el-button @click="cancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  saleListApplication, getSaleApplication, updateSaleApplication, deleteSaleApplication, getDeliveryOrderByThird, getDetailByUserId
} from "@/api/system/application";
import SelectUser from '@/views/system/role/selectUser'
import outformcomponents from '@/views/system/application/indexform_out'
import { formatDate } from '@/utils/index'
import { outform } from "@/api/outsystem/outsystem";

export default {
  components: { SelectUser, outformcomponents },
  name: "Application",
  data() {

    return {

      //是否修改司机或者是手输司机
      isChoose: false,
      //是否新增数据
      isAdd: false,
      //聚焦标识
      isFocus: false,
      //select改变标识
      isChange: false,
      //下拉框选中值或输入值
      chooseDate: {
        disabledDate(time) {
          return time.getTime() <= new Date().getTime() - 3600 * 24 * 1000;
        }
      },
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //存储打印内容
      printContent: '',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      //司机列表
      drivers: [],
      //审核人列表
      checkers: [],
      //承运商列表
      carriers: [],
      // 总条数
      total: 0,
      // 交货单表格数据
      saleList: [],
      // 弹出层标题
      title: "",
      // 是否显示新增和修改弹出层
      open: false,
      // 是否显示修改预约重量弹出层
      dialogVisible: false,
      outformopen: false,
      // 操作，默认为增加
      operation: 'add',
      // 输入框中的值
      inputValue: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // vrsDnnum: null,
        // vrsDnLine: null,
        // vrsPackagingid: null,
        queryCondition: null
      },
      // 表单参数
      form: {
        vrsId: null,
        vrsCarNumber: null,
        vrsDnnum: null,
        vrsDnLine: null,
        vrsPackagingid: null,
        vrsGrossweight: null
      },
      // 表单校验
      rules: {
        factoryType: {
          required: true,
          message: "请选择工厂类型",
          trigger: ["change"],
        },
        // carNumber: [
        //   {
        //     required: false,
        //     pattern: /^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([A-Z]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/,
        //     message: "请输入正确的车牌号",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        // carrierId: [
        //   { required: true, message: "司机所属承运商不能为空", trigger: ["blur", "change"] }
        // ],
        enter_type: [
          { required: true, message: "司机录入方式必选", trigger: ["blur", "change"] }
        ],
        driverId: [
          { required: true, message: "司机姓名不能为空", trigger: ["blur", "change"] }
        ],
        driverName: [
          { required: true, message: "司机姓名不能为空", trigger: ["blur", "change"] }
        ],
        phone: [
          {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: ["blur", "change"]
          }
        ],
        idcard: [
          {
            required: true,
            pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "请输入正确的身份证号",
            trigger: ["blur", "change"]
          }
        ],
        applicationDate: [
          { required: true, message: "入厂时间不能为空", trigger: "blur" }
        ],
        checkerId: [
          { required: true, message: "受访人不能为空", trigger: ["blur", "change"] }
        ],
        beforeWeight: [
          { required: true, message: "送/提货预计重量不能为空", trigger: ["blur", "change"] }
        ],
        reasonKey: [
          {
            required: true,
            message: "原因不能为空",
            trigger: "blur"
          }
        ],
        appointmentWeight: [
          {
            required: true,
            message: "预约重量不能为空",
            trigger: "blur,change"
          }
        ],
        floatReason: [
          {
            required: true,
            message: "预约重量变更原因不能为空",
            trigger: "blur,change"
          }
        ],
      },
      // 点击审核时 记录当前选中
      remember_row: null,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    onChangeRadio(e) {
      this.$refs['form'].clearValidate(['enter_type'])
    },

    /**输入清除校验 */
    handleInput(field) {
      this.$refs.form.clearValidate(field);
    },
    handleClear(field) {
      this.$refs.form.validateField(field);
    },

    /** 查询交货单列表 */
    getList() {
      this.loading = true;
      saleListApplication(this.queryParams).then(response => {
        this.saleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 更新交货单按钮操作 */
    deliveryOrderHandleUpdate() {
      this.$modal.confirm(this.$t('sfgxjhd')).then(function () {
        return getDeliveryOrderByThird();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('gxjhdcg'));
      }).catch(() => {
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
        vrsId: null,
        vrsCarNumber: null,
        vrsDnnum: null,
        vrsPackagingid: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },

    // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    //   if (selection.length === 1) {
    //     this.ids = selection.map(item => item.vrsId)
    //   }
    // },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.vrsId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },

    /** 修改按钮操作 */
    handleSaleUpdate(row) {
      this.reset();
      const vrsId = row.vrsId || this.ids
      getSaleApplication(vrsId).then(response => {
        this.form = {
          ...response.data,
        };
        this.open = true;
        this.title = this.$t('xgjhd');
      });

    },

    handleSaleDelete(row) {
      const vrsIds = row.vrsId || this.ids
      this.$modal.confirm(this.$t('joinj')).then(function () {
        return deleteSaleApplication(vrsIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('sccg'));
      }).catch(() => {
      });
    },

    /** 修改提交按钮 */
    submitSaleForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          //校验之后清除校验，否则下面赋值之后会再次校验
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          });
          if (this.form.vrsId != null) {
            updateSaleApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
