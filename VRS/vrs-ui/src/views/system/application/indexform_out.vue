<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="220px">
      <el-form-item :label="$t('y')" prop="businessType" :required="rules.businessType.required">
        <el-select ref="businessType" v-model="form.businessType" :placeholder="$t('hj')" @change="BusinessTypeChange" :disabled="isBusinessTypeDisabled"
          style="width: 250px;">
          <el-option v-for="dict in businessTypelist" :key="dict.value" :label="$t(`hjj${dict.value}`)"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('ssu')" prop="supplyCode"
        :rules="[{ required: true, message: $t('lmmm3'), trigger: 'blur' }]" v-if="form.businessType === '0'">
        <el-select ref="supplyCode" filterable v-model="form.supplyCode" :placeholder="$t('hjjj0')"
          @change="onSupplyCodeChange" style="width: 250px;">
          <el-option v-for="dict in suppliers" :key="dict.Id" :label="dict.name" :value="dict.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="供应商名称" prop="supplyName" required="rules.supplyCode.required" v-if="false">
        <el-input v-model="form.supplyName" :maxlength="20" placeholder="请输入供应商名称" readonly />
      </el-form-item>
      <el-form-item :label="$t('uu')" prop="custCode" :required="rules.custCode.required"
        v-if="form.businessType === '1'">
        <el-input v-model="form.custCode" :maxlength="20" :placeholder="$t('jk46')" />
      </el-form-item>
      <el-form-item label="工厂代码" prop="factoryCode" :required="rules.factoryCode.required" v-if="false">
        <el-input v-model="form.factoryCode" :maxlength="10" placeholder="请输入工厂代码" />
      </el-form-item>
      <el-form-item :label="$t('s')" prop="businessDescription" :required="rules.businessDescription.required">
        <el-input v-model="form.businessDescription" type="textarea" :maxlength="60" :placeholder="$t('hjjj1')" />
      </el-form-item>
      <el-form-item :label="$t('hjjj2')" prop="applicationDate"
        :rules="[{ required: true, message: $t('jk3'), trigger: 'blur' }]">
        <el-date-picker clearable size="small" v-model="form.applicationDate" type="date" value-format="yyyy-MM-dd"
          :placeholder="$t('hjjj3')" style="width: 280px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('ad')" prop="carNumber"
        :rules="[{ required: true, message: $t('jk2'), trigger: 'blur' }]" style="width: 500px;">
        <el-input v-model="form.carNumber" :maxlength="10" :placeholder="$t('ae')" />
      </el-form-item>
      <el-form-item label="承运商代码" prop="carrierCode" :required="rules.carrierCode.required" v-if="false">
        <el-input v-model="form.carrierCode" :maxlength="20" placeholder="请输入承运商代码" />
      </el-form-item>
      <el-form-item :label="$t('v')" prop="carrierName"
        :rules="[{ required: true, message: $t('jk1'), trigger: 'blur' }]">
        <el-input v-model="form.carrierName" :maxlength="60" :placeholder="$t('hjjj4')" />
      </el-form-item>
      <el-form-item :label="$t('w')" prop="driverName" :rules="[{ required: true, message: $t('jk'), trigger: 'blur' }]"
        style="width: 400px;">
        <el-input v-model="form.driverName" :maxlength="10" :placeholder="$t('hjjj5')" />
      </el-form-item>
      <el-form-item label="申请id" prop="applicationId" :required="rules.applicationId.required" v-if="false">
        <el-input v-model="form.applicationId" :maxlength="10" placeholder="请输入申请id" />
      </el-form-item>
      <el-form-item label="业务主键" prop="taskId" :required="rules.taskId.required" v-if="false">
        <el-input v-model="form.taskId" type="textarea" :maxlength="60" placeholder="请输入业务主键" />
      </el-form-item>
      <!-- <el-form-item :label="$t('jkk1')" prop="wMSPickingRouteIDs" :rules="[{ required: true, message: $t('jhddd'), trigger: 'blur' }]"
        v-if="form.businessType === '1'">
        <el-input v-model="form.wMSPickingRouteIDs" type="textarea" :maxlength="60" :placeholder="$t('jhd')" />
      </el-form-item>
      <el-form-item :label="$t('jkk2')" prop="wMSPickingRouteIDs2"
        v-if="form.businessType === '1'">
        <el-input v-model="form.wMSPickingRouteIDs2" type="textarea" :maxlength="60" :placeholder="$t('jhd2')" />
      </el-form-item> -->
      <el-form-item label="扣重" prop="float" :required="rules.float.required" v-if="false">
        <el-input v-model="form.float" type="number" placeholder="请输入扣重" :maxlength="8" />
      </el-form-item>
      <el-form-item :label="$t('nnz')" prop="appointmentWeight"
        :rules="[{ required: true, message: $t('jk4'), trigger: 'blur' }]" style="width: 500px;"
        v-if="form.businessType === '0' || form.businessType === '3'">
        <el-input v-model="form.appointmentWeight" type="number" :placeholder="$t('nnnn')" :maxlength="8" />
      </el-form-item>
      <el-form-item label="预约重量变更原因" prop="floatReason" :required="rules.floatReason.required" v-if="false">
        <el-input v-model="form.floatReason" type="textarea" :maxlength="60" placeholder="请输入预约重量变更原因" />
      </el-form-item>
      <el-form-item label="船名" prop="shipName" :required="rules.shipName.required" v-if="false">
        <el-input v-model="form.shipName" :maxlength="60" placeholder="请输入船名" />
      </el-form-item>
      <el-form-item label="船次" prop="shipNo" :required="rules.shipNo.required" v-if="false">
        <el-input v-model="form.shipNo" :maxlength="10" placeholder="请输入船次" />
      </el-form-item>

      <el-form-item :label="$t('xxxx')" prop="enableControl" :required="rules.enableControl.required"
        v-if="form.businessType === '0'">
        <el-select v-model="form.enableControl" placeholder="请选择是否启用管控">
          <el-option v-for="dict in enableControllist" :key="dict.value" :label="$t(`zpp${dict.value}`)"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('gg')" prop="toleranceAdd"
        :rules="[{ required: true, message: $t('jk44'), trigger: 'blur' }]"
        v-if="form.businessType === '0' || form.businessType === '1'">
        <el-input v-model="form.toleranceAdd" :placeholder="$t('zl')" :maxlength="2" :disabled="true" />
      </el-form-item>
      <el-form-item :label="$t('hh')" prop="toleranceDec"
        :rules="[{ required: true, message: $t('jk45'), trigger: 'blur' }]"
        v-if="form.businessType === '0' || form.businessType === '1'">
        <el-input v-model="form.toleranceDec" :placeholder="$t('zl1')" :maxlength="2" :disabled="true" />
      </el-form-item>
      <el-form-item label="单据编号" prop="documentCode" :required="rules.documentCode.required" v-if="false">
        <el-input v-model="form.documentCode" :maxlength="60" placeholder="请输入单据编号" />
      </el-form-item>
      <el-form-item label="产品收据" prop="packingSlipId" :required="rules.packingSlipId.required" v-if="false">
        <el-input v-model="form.packingSlipId" :maxlength="20" placeholder="请输入产品收据" />
      </el-form-item>
    </el-form>
    <div class="dialogfooter">
      <el-button type="primary" @click="submitForm" :loading="loading">{{ $t('nnnna') }}</el-button>
      <!-- 测试通过 -->
      <!-- <el-button type="primary" @click="textreturn" :loading="loading">审核通过</el-button> -->
      <el-button @click="cancel">{{ $t('hjjj') }}</el-button>
    </div>
  </div>
</template>

<script>
import { outform } from "@/api/outsystem/outsystem";
import { getSupplierList, getGlobalVariable, listApplication, updateApplication, authenApplication } from "@/api/system/application";

export default {
  name: "Application",
  props: {
    detail: {
      type: Object,
      default: () => { },
    },
  },
  data() {
    return {
      // 业务类型数组
      businessTypelist: [
        {
          label: '采购',
          value: '0'
        },
        {
          label: '销售',
          value: '1'
        },
        {
          label: '废料',
          value: '2'
        },
        {
          label: '固废危废处置',
          value: '4'
        },
        {
          label: '其他',
          value: '3'
        }
      ],
      // 启用管控数组
      enableControllist: [
        {
          label: '否',
          value: '0'
        },
        {
          label: '是',
          value: '1'
        }
      ],
      //供应商列表
      suppliers: [],
      //全局变量列表
      GlobalVariables: [],
      // 是否显示弹出层
      open: true,
      // 是否禁用业务类型下拉框
      isBusinessTypeDisabled: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        applicationDate: null,
        createdDate: null,
        enterStatus: null,
        status: null,
        queryCondition: null
      },
      // 表单参数
      form: {
        applicationId: null,
        businessType: null,
        supplyCode: null,
        supplyName: null,
        custCode: null,
        factoryCode: null,
        businessDescription: null,
        applicationDate: null,
        carNumber: null,
        driverName: null,
        taskId: null,
        wMSPickingRouteIDs: null,
        wMSPickingRouteIDs2: null,
        float: null,
        appointmentWeight: null,
        floatReason: null,
        ShipName: null,
        shipNo: null,
        carrierCode: null,
        carrierName: null,
        enableControl: '1',
        toleranceAdd: null,
        toleranceDec: null,
        documentCode: null,
        packingSlipId: null
      },
      // 表单校验
      rules: {
        businessType: {
          required: true,
          message: "请选择业务类型",
          trigger: ["change"],
        },
        supplyCode: {
          required: true,
          message: "请输入供应商编号",
          trigger: ["blur", "change"],
        },
        custCode: {
          required: false,
          message: "请输入客户编号",
          trigger: ["blur", "change"],
        },
        factoryCode: {
          required: false,
          message: "请输入工厂代码",
          trigger: ["blur", "change"],
        },
        businessDescription: {
          required: false,
          message: "请输入业务描述",
          trigger: ["blur", "change"],
        },
        applicationDate: {
          required: true,
          message: "请选择车辆进厂时间",
          trigger: ["blur", "change"],
        },
        carNumber: {
          required: true,
          message: "请输入车辆号",
          trigger: ["blur", "change"],
        },
        driverName: {
          required: true,
          message: "请输入司机名称",
          trigger: ["blur", "change"],
        },
        taskId: {
          required: false,
          message: "请输入业务主键",
          trigger: ["blur", "change"],
        },
        wMSPickingRouteIDs: {
          required: false,
          message: "请输入领料流程",
          trigger: ["blur", "change"],
        },
        wMSPickingRouteIDs2: {
          required: false,
          message: "请输入领料流程2",
          trigger: ["blur", "change"],
        },
        float: {
          required: false,
          message: "请输入扣重",
          trigger: ["blur", "change"],
        },
        appointmentWeight: {
          required: false,
          message: "请输入重量",
          trigger: ["blur", "change"],
        },
        floatReason: {
          required: false,
          message: "请输入预约重量变更原因",
          trigger: ["blur", "change"],
        },
        shipName: {
          required: false,
          message: "请输入船名",
          trigger: ["blur", "change"],
        },
        shipNo: {
          required: false,
          message: "请输入船次",
          trigger: ["blur", "change"],
        },
        carrierCode: {
          required: false,
          message: "请输入承运商代码",
          trigger: ["blur", "change"],
        },
        carrierName: {
          required: true,
          message: "请输入承运商名称",
          trigger: ["blur", "change"],
        },
        enableControl: {
          required: false,
          type: 'number',
          message: "请选择是否启用管控",
          trigger: ["blur", "change"],
        },
        toleranceAdd: {
          required: true,
          message: "请输入正容差",
          trigger: ["blur", "change"],
        },
        toleranceDec: {
          required: true,
          message: "请输入负容差",
          trigger: ["blur", "change"],
        },
        documentCode: {
          required: false,
          message: "请输入单据编号",
          trigger: ["blur", "change"],
        },
        packingSlipId: {
          required: false,
          message: "请输入产品收据",
          trigger: ["blur", "change"],
        },
      },
      // 按钮加载中
      loading: false,
    };
  },
  created() {
    this.form.applicationDate = this.detail.applicationDate;
    this.form.carNumber = this.detail.carNumber;
    this.form.carrierName = this.detail.carrierName;
    this.form.driverName = this.detail.driverName;
    this.form.appointmentWeight = this.detail.beforeWeight || null;
    this.form.businessDescription = this.detail.reason || null;
    this.form.applicationId = this.detail.applicationId;
    getSupplierList().then(response => {
      this.suppliers = response.lmSupplier
    });
  },
  watch: {
    'form.businessType': function (newVal) {
      if (newVal === '0' || newVal === '1') {
        getGlobalVariable().then(response => {
          this.form.toleranceAdd = (parseFloat(response.data.soToleranceAdd) * 100).toString();
          this.form.toleranceDec = (parseFloat(response.data.soToleranceDec) * 100).toString();
        });
      }
    }
  },
  methods: {
    // 提交表单
    submitForm() {
      let that = this
      this.$refs["form"].validate(valid => {
        if (that.form.float !== null && that.form.float !== undefined) {
          that.form.float = Number(that.form.float)
        }
        if (that.form.appointmentWeight !== null && that.form.appointmentWeight !== undefined) {
          that.form.appointmentWeight = Number(that.form.appointmentWeight)
        }
        if (that.form.toleranceAdd !== null && that.form.toleranceAdd !== undefined) {
          that.form.toleranceAdd = Number(that.form.toleranceAdd)
        }
        if (that.form.toleranceDec !== null && that.form.toleranceDec !== undefined) {
          that.form.toleranceDec = Number(that.form.toleranceDec)
        }
        // 类型为0  SupplyCode赋值
        // 类型为1  customcode赋值
        // 类型为2,3  两个参数传空
        if (that.form.businessType === '0') {
          that.form.custCode = null
          that.form.wMSPickingRouteIDs = null
          that.form.wMSPickingRouteIDs2 = null
        } else if (that.form.businessType === '1') {
          this.form.appointmentWeight = null
          that.form.supplyCode = null
          that.form.enableControl = null
        } else if (that.form.businessType === '2') {
          this.form.appointmentWeight = null
          that.form.supplyCode = null
          that.form.custCode = null
          that.form.enableControl = null
          that.form.wMSPickingRouteIDs = null
          that.form.wMSPickingRouteIDs2 = null
        } else if (that.form.businessType === '3') {
          that.form.supplyCode = null
          that.form.custCode = null
          that.form.enableControl = null
          that.form.wMSPickingRouteIDs = null
          that.form.wMSPickingRouteIDs2 = null
        } else if (that.form.businessType === '4') {
          this.form.appointmentWeight = null
          that.form.supplyCode = null
          that.form.custCode = null
          that.form.enableControl = null
          that.form.wMSPickingRouteIDs = null
          that.form.wMSPickingRouteIDs2 = null
        } 
        if (valid) {
          let retundata = {
            applicationId: that.form.applicationId,
            status: 2,
            orderId: that.detail.factoryType === 'KS' ? "LMKS" + "-" + that.getuuid() || null : "LMIN" + "-" + that.getuuid() || null,
            businessType: that.form.businessType || null,
            appointmentWeight: that.form.appointmentWeight,
            enableControl: that.form.enableControl,
            toleranceAdd: parseFloat((that.form.toleranceAdd / 100).toFixed(3)),
            toleranceDec: parseFloat((that.form.toleranceDec / 100).toFixed(3)),
            applicationDate: that.form.applicationDate,
            supplyCode: that.form.supplyCode,
            supplyName: that.form.supplyName,
            custCode: that.form.custCode,
            businessDescription: that.form.businessDescription,
            wMSPickingRouteIDs: that.form.wMSPickingRouteIDs,
            wMSPickingRouteIDs2: that.form.wMSPickingRouteIDs2,
            upFloatingWeight: that.form.appointmentWeight * (1 + parseFloat(((that.form.toleranceAdd / 100).toFixed(3)))),
            downFloatingWeight: that.form.appointmentWeight * (1 + parseFloat(((that.form.toleranceDec / 100).toFixed(3))))
          }
          that.$emit('sure', retundata)
        }
      });
    },
    onSupplyCodeChange() {
      const selectedSupplier = this.suppliers.find(s => s.code === this.form.supplyCode);
      if (selectedSupplier) {
        this.form.supplyName = selectedSupplier.name;
        // this.form.toleranceAdd = selectedSupplier.toleranceAdd * 100;
        // this.form.toleranceDec = selectedSupplier.toleranceDec * 100;
      } else {
        this.form.supplyName = '';
      }
    },
    getuuid() {
      return 'xxxx4xxxyxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    },
    // 直接通过测试按钮 事件
    textreturn() {
      this.$emit('textreturn')
    },
    // 取消按钮 事件
    cancel() {
      this.$emit('cancel')
    },
    // 业务类型 变化事件
    BusinessTypeChange(e) {
      this.isBusinessTypeDisabled = true;
      let that = this
      this.$refs.form.validateField('businessType')
      if (e === '0') {
        this.rules = {
          businessType: {
            required: true,
            message: "请选择业务类型",
            trigger: ["change"],
          },
          supplyCode: {
            required: false,
            message: "请输入供应商编号",
            trigger: ["blur", "change"],
          },
          custCode: {
            required: false,
            message: "请输入客户编号",
            trigger: ["blur", "change"],
          },
          factoryCode: {
            required: false,
            message: "请输入工厂代码",
            trigger: ["blur", "change"],
          },
          businessDescription: {
            required: false,
            message: "请输入业务描述",
            trigger: ["blur", "change"],
          },
          applicationDate: {
            required: true,
            message: "请选择车辆进厂时间",
            trigger: ["blur", "change"],
          },
          carNumber: {
            required: true,
            message: "请输入车辆号",
            trigger: ["blur", "change"],
          },
          driverName: {
            required: true,
            message: "请输入司机名称",
            trigger: ["blur", "change"],
          },
          taskId: {
            required: false,
            message: "请输入业务主键",
            trigger: ["blur", "change"],
          },
          wMSPickingRouteIDs: {
            required: false,
            message: "请输入领料流程",
            trigger: ["blur", "change"],
          },
          wMSPickingRouteIDs2: {
            required: false,
            message: "请输入领料流程2",
            trigger: ["blur", "change"],
          },
          float: {
            required: false,
            message: "请输入扣重",
            trigger: ["blur", "change"],
          },
          appointmentWeight: {
            required: true,
            message: "请输入重量",
            trigger: ["blur", "change"],
          },
          floatReason: {
            required: false,
            message: "请输入预约重量变更原因",
            trigger: ["blur", "change"],
          },
          shipName: {
            required: false,
            message: "请输入船名",
            trigger: ["blur", "change"],
          },
          shipNo: {
            required: false,
            message: "请输入船次",
            trigger: ["blur", "change"],
          },
          carrierCode: {
            required: false,
            message: "请输入承运商代码",
            trigger: ["blur", "change"],
          },
          carrierName: {
            required: true,
            message: "请输入承运商名称",
            trigger: ["blur", "change"],
          },
          enableControl: {
            required: true,
            // type: 'number',
            message: "请选择是否启用管控",
            trigger: ["blur", "change"],
          },
          toleranceAdd: {
            required: true,
            message: "请输入正容差",
            trigger: ["blur", "change"],
          },
          toleranceDec: {
            required: true,
            message: "请输入负容差",
            trigger: ["blur", "change"],
          },
          documentCode: {
            required: false,
            message: "请输入单据编号",
            trigger: ["blur", "change"],
          },
          packingSlipId: {
            required: false,
            message: "请输入产品收据",
            trigger: ["blur", "change"],
          },
        }
      } else if (e === '1') {
        this.rules = {
          businessType: {
            required: true,
            message: "请选择业务类型",
            trigger: ["change"],
          },
          supplyCode: {
            required: false,
            message: "请输入供应商编号",
            trigger: ["blur", "change"],
          },
          custCode: {
            required: false,
            message: "请输入客户编号",
            trigger: ["blur", "change"],
          },
          factoryCode: {
            required: false,
            message: "请输入工厂代码",
            trigger: ["blur", "change"],
          },
          businessDescription: {
            required: false,
            message: "请输入业务描述",
            trigger: ["blur", "change"],
          },
          applicationDate: {
            required: true,
            message: "请选择车辆进厂日期",
            trigger: ["blur", "change"],
          },
          carNumber: {
            required: true,
            message: "请输入车辆号",
            trigger: ["blur", "change"],
          },
          driverName: {
            required: true,
            message: "请输入司机名称",
            trigger: ["blur", "change"],
          },
          taskId: {
            required: false,
            message: "请输入业务主键",
            trigger: ["blur", "change"],
          },
          wMSPickingRouteIDs: {
            required: false,
            message: "请输入领料流程",
            trigger: ["blur", "change"],
          },
          wMSPickingRouteIDs2: {
            required: false,
            message: "请输入领料流程2",
            trigger: ["blur", "change"],
          },
          float: {
            required: false,
            message: "请输入扣重",
            trigger: ["blur", "change"],
          },
          appointmentWeight: {
            required: false,
            message: "请输入重量",
            trigger: ["blur", "change"],
          },
          floatReason: {
            required: false,
            message: "请输入预约重量变更原因",
            trigger: ["blur", "change"],
          },
          shipName: {
            required: false,
            message: "请输入船名",
            trigger: ["blur", "change"],
          },
          shipNo: {
            required: false,
            message: "请输入船次",
            trigger: ["blur", "change"],
          },
          carrierCode: {
            required: false,
            message: "请输入承运商代码",
            trigger: ["blur", "change"],
          },
          carrierName: {
            required: true,
            message: "请输入承运商名称",
            trigger: ["blur", "change"],
          },
          enableControl: {
            required: false,
            type: 'number',
            message: "请选择是否启用管控",
            trigger: ["blur", "change"],
          },
          toleranceAdd: {
            required: true,
            message: "请输入正容差",
            trigger: ["blur", "change"],
          },
          toleranceDec: {
            required: true,
            message: "请输入负容差",
            trigger: ["blur", "change"],
          },
          documentCode: {
            required: false,
            message: "请输入单据编号",
            trigger: ["blur", "change"],
          },
          packingSlipId: {
            required: false,
            message: "请输入产品收据",
            trigger: ["blur", "change"],
          },
        }
      } else if (e === '2' || e === '3') {
        this.rules = {
          businessType: {
            required: true,
            message: "请选择业务类型",
            trigger: ["change"],
          },
          supplyCode: {
            required: false,
            message: "请输入供应商编号",
            trigger: ["blur", "change"],
          },
          custCode: {
            required: false,
            message: "请输入客户编号",
            trigger: ["blur", "change"],
          },
          factoryCode: {
            required: false,
            message: "请输入工厂代码",
            trigger: ["blur", "change"],
          },
          businessDescription: {
            required: false,
            message: "请输入业务描述",
            trigger: ["blur", "change"],
          },
          applicationDate: {
            required: true,
            message: "请选择车辆进厂日期",
            trigger: ["blur", "change"],
          },
          carNumber: {
            required: true,
            message: "请输入车辆号",
            trigger: ["blur", "change"],
          },
          driverName: {
            required: true,
            message: "请输入司机名称",
            trigger: ["blur", "change"],
          },
          taskId: {
            required: false,
            message: "请输入业务主键",
            trigger: ["blur", "change"],
          },
          wMSPickingRouteIDs: {
            required: false,
            message: "请输入领料流程",
            trigger: ["blur", "change"],
          },
          wMSPickingRouteIDs2: {
            required: false,
            message: "请输入领料流程2",
            trigger: ["blur", "change"],
          },
          float: {
            required: false,
            message: "请输入扣重",
            trigger: ["blur", "change"],
          },
          appointmentWeight: {
            required: true,
            message: "请输入重量",
            trigger: ["blur", "change"],
          },
          floatReason: {
            required: false,
            message: "请输入预约重量变更原因",
            trigger: ["blur", "change"],
          },
          shipName: {
            required: false,
            message: "请输入船名",
            trigger: ["blur", "change"],
          },
          shipNo: {
            required: false,
            message: "请输入船次",
            trigger: ["blur", "change"],
          },
          carrierCode: {
            required: false,
            message: "请输入承运商代码",
            trigger: ["blur", "change"],
          },
          carrierName: {
            required: true,
            message: "请输入承运商名称",
            trigger: ["blur", "change"],
          },
          enableControl: {
            required: false,
            type: 'number',
            message: "请选择是否启用管控",
            trigger: ["blur", "change"],
          },
          toleranceAdd: {
            required: false,
            message: "请输入正容差",
            trigger: ["blur", "change"],
          },
          toleranceDec: {
            required: false,
            message: "请输入负容差",
            trigger: ["blur", "change"],
          },
          documentCode: {
            required: false,
            message: "请输入单据编号",
            trigger: ["blur", "change"],
          },
          packingSlipId: {
            required: false,
            message: "请输入产品收据",
            trigger: ["blur", "change"],
          },
        }
      }
      this.$nextTick(() => {
        that.$refs.form.clearValidate()
      })
    }
  }
};
</script>

<style scoped>
.dialogfooter {
  text-align: right;
}
</style>
