<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('k')" prop="queryCondition">
        <el-input v-model="queryParams.queryCondition" :placeholder="$t('aaa')" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item :label="$t('n')" prop="enterStatus">
        <!-- <el-select v-model="queryParams.enterStatus" placeholder="请选择进场状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.enter_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select> -->
        <el-radio-group v-model="queryParams.enterStatus" @change="enterStatuschange">
          <el-radio-button v-for="dict in dict.type.enter_status" :label="dict.value" :name="dict.value">{{
      $t(`lll${dict.value}`) }}</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('select') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('q') }}</el-button>
      </el-form-item>
    </el-form>
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="success"-->
    <!--          plain-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          :disabled="single"-->
    <!--          @click="handleUpdate"-->
    <!--          v-hasPermi="['system:application:edit']"-->
    <!--        >修改</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="danger"-->
    <!--          plain-->
    <!--          icon="el-icon-delete"-->
    <!--          size="mini"-->
    <!--          :disabled="multiple"-->
    <!--          @click="handleDelete"-->
    <!--          v-hasPermi="['system:application:remove']"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <el-col :span="1.5">
      <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
        v-hasPermi="['system:application:todayExport']">{{ $t('e') }}</el-button>
    </el-col>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>


    <el-table v-loading="loading" :data="applicationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="申请id" align="center" prop="applicationId" width="120px" v-if="false" />
      <el-table-column :label="$t('r')" align="center" prop="serialNumber" width="120px" fixed="left" />
      <el-table-column :label="$t('crcpz')" align="center" prop="mark" width="180px" />
      <el-table-column :label="$t('u')" align="center" prop="carNumber" width="110px" />
      <el-table-column :label="$t('dddd')" align="center" prop="applicationDate" width="180px">
        <template slot-scope="scope">
          <span>{{ scope.row.applicationDate }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('v')" align="center" prop="carrierName" width="220px" />
      <el-table-column :label="$t('w')" align="center" prop="driverName" width="120px" />
      <el-table-column :label="$t('s')" align="center" prop="businessDescription" width="110px" />
      <el-table-column :label="$t('ggg')" align="center" prop="remark" width="220px" />
      <el-table-column :label="$t('y')" align="center" prop="businessType" width="120px">
        <template slot-scope="scope">
          <span v-if="scope.row.businessType === 0">{{ $t('aaaa') }}</span>
          <span v-if="scope.row.businessType === 1">{{ $t('bbbb') }}</span>
          <span v-if="scope.row.businessType === 2">{{ $t('cccc') }}</span>
          <span v-if="scope.row.businessType === 3">{{ $t('ddddd') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('xxxx')" align="center" prop="enableControl" width="180px">
        <template slot-scope="scope">
          <div v-if="scope.row.enableControl === 0">{{ $t('yyyy') }}</div>
          <div v-if="scope.row.enableControl === 1">{{ $t('zz') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('eeee')" align="center" prop="reason" width="120px">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.application_reason" :value="scope.row.reasonKey" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('ff')" align="center" prop="enterDate" width="180px">
        <template slot-scope="scope">
          <span>{{ scope.row.enterDate }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('eee')" align="center" prop="outDate" width="180px">
        <template slot-scope="scope">
          <span>{{ scope.row.outDate }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('t')" align="center" prop="factoryType" width="120px">
        <template slot-scope="scope">
          <span v-if="scope.row.factoryType === 'KS'">KS</span>
          <span v-if="scope.row.factoryType === 'IN'">IN</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('z')" align="center" prop="phone" width="110px" />
      <el-table-column :label="$t('aa')" align="center" prop="createdName" width="220px" />
      <el-table-column :label="$t('bb')" align="center" prop="checkerName" width="130px" />
      <el-table-column :label="$t('cc')" align="center" prop="status" width="170px">
        <!--        <template slot-scope="scope">
          <dict-tag :options="dict.type.application_status" :value="scope.row.status" />
        </template>-->
        <!--        1=已申请,2=审核通过,3=审核不通过,4=取消申请-->

        <template slot-scope="scope">
          <div v-if="scope.row.status === 1" style="background-color:aqua;border-radius: 40%;font-weight: bold;">{{
      $t('zacbb') }}</div>
          <div v-if="scope.row.status === 2" style="background-color:palegreen;border-radius: 40%;font-weight: bold;">
            {{
      $t('nnnna') }}</div>
          <div v-if="scope.row.status === 3" style="background-color:red;border-radius: 40%;font-weight: bold;">{{
      $t('zacba') }}</div>
          <div v-if="scope.row.status === 4" style="background-color:red;border-radius: 40%;font-weight: bold;">{{
      $t('qxsq') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dd')" align="center" prop="enterStatus" width="200px">

        <template slot-scope="scope">
          <!--          <dict-tag :options="dict.type.enter_status" :value="scope.row.enterStatus" />-->
          <!--          0=未进厂,1=已进厂,2=已出厂,3=取消进厂-->
          <div v-if="scope.row.enterStatus === 0" style="background-color:red;border-radius: 40%;font-weight: bold;">
            {{
      $t('gx') }}</div>
          <div v-if="scope.row.enterStatus === 1" style="background-color:aqua;border-radius: 40%;font-weight: bold;">
            {{
      $t('hx') }}</div>
          <div v-if="scope.row.enterStatus === 2"
            style="background-color:palegreen;border-radius: 40%;font-weight: bold;">{{ $t('ix') }}</div>
          <div v-if="scope.row.enterStatus === 3" style="background-color:red;border-radius: 40%;font-weight: bold;">
            {{ $t('qxjc') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('ttt')" align="center" prop="appointmentStatus" width="180px">

        <template slot-scope="scope">
          <div v-if="scope.row.appointmentStatus === 0"
            style="background-color:yellow;border-radius: 40%;font-weight: bold;">{{ $t('gm') }}</div>
          <div v-if="scope.row.appointmentStatus === 1"
            style="background-color:aqua;border-radius: 40%;font-weight: bold;">{{ $t('hm') }}</div>
          <div v-if="scope.row.appointmentStatus === 2"
            style="background-color:aqua;border-radius: 40%;font-weight: bold;">{{ $t('im') }}</div>
          <div v-if="scope.row.appointmentStatus === 3"
            style="background-color:aqua;border-radius: 40%;font-weight: bold;">{{ $t('jm') }}</div>
          <div v-if="scope.row.appointmentStatus === 4"
            style="background-color:aqua;border-radius: 40%;font-weight: bold;">{{ $t('km') }}</div>
          <div v-if="scope.row.appointmentStatus === 5"
            style="background-color:aqua;border-radius: 40%;font-weight: bold;">{{ $t('kma') }}</div>
          <div v-if="scope.row.appointmentStatus === 6"
            style="background-color:palegreen;border-radius: 40%;font-weight: bold;">{{ $t('kmb') }}</div>
          <div v-if="scope.row.appointmentStatus === 7"
            style="background-color:red;border-radius: 40%;font-weight: bold;">{{ $t('yqxyy') }}</div>
          <!-- <dict-tag :options="dict.type.appointmentStatus" :value="scope.row.appointmentStatus" /> -->
        </template>
      </el-table-column>
      <el-table-column :label="$t('ee')" align="center" prop="idcard" width="170px" />
      <!--      <el-table-column label="申请时间" align="center" prop="createdDate" width="180">-->
      <!--        <template slot-scope="scope">-->
      <!--          <span>{{ scope.row.createdDate}}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column :label="$t('fff')" align="center" prop="beforeWeight" width="180px">
        <template slot-scope="scope">
          <div>{{ scope.row.beforeWeight }}</div>
        </template>
      </el-table-column>
      <!--      <el-table-column label="原因" align="center" prop="reason" :show-overflow-tooltip="true" />-->
      <el-table-column :label="$t('ffff')" align="center" prop="postType" width="160px">
        <template slot-scope="scope">
          <div v-if="scope.row.postType === 2">{{ $t('gggg') }}</div>
          <div v-if="scope.row.postType === 1">{{ $t('ggggg') }}</div>
        </template>
      </el-table-column>
      <!--新增字段-->
      <el-table-column :label="$t('ss')" align="center" prop="supplyCode" width="125px" />
      <el-table-column :label="$t('tt')" align="center" prop="supplyName" width="120px" />
      <el-table-column :label="$t('uu')" align="center" prop="custCode" width="135px" />
      <el-table-column :label="$t('vv')" align="center" prop="custName" width="120px" v-if="false" />
      <el-table-column :label="$t('ww')" align="center" prop="documentCode" width="120px" v-if="false" />
      <el-table-column :label="$t('xx')" align="center" prop="dataAreaId" width="120px" v-if="false" />
      <el-table-column :label="$t('yy')" align="center" prop="factoryCode" width="120px" v-if="false" />
      <el-table-column :label="$t('mm')" align="center" prop="taskId" width="145px" />
      <el-table-column :label="$t('x')" align="center" prop="orderId" width="160px">
        <template slot-scope="scope">
          <span>{{ scope.row.orderId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('nnz')" align="center" prop="appointmentWeight" width="180px" />
      <el-table-column :label="$t('nn')" align="center" prop="loseWeight" width="180px" />
      <el-table-column :label="$t('dzb')" align="center" prop="shippedWithPalletWeight" width="220px" />
      <el-table-column :label="$t('oo')" align="center" prop="floatReason" width="260px" />
      <el-table-column :label="$t('pp')" align="center" prop="cardId" width="185px" />
      <el-table-column :label="$t('qq')" align="center" prop="cardType" width="155px">
        <template slot-scope="scope">
          <div v-if="scope.row.cardType === 1">{{ $t('mmm') }}</div>
          <div v-if="scope.row.cardType === 2">{{ $t('nnn') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('rr')" align="center" prop="createCardTime" width="200px" />
      <el-table-column :label="$t('sss')" align="center" prop="returnCardTime" width="180px" />
      <el-table-column :label="$t('uuu')" align="center" prop="firstWeight" width="170px" />
      <el-table-column :label="$t('vvv')" align="center" prop="firstWeightTime" width="180px" />
      <el-table-column :label="$t('www')" align="center" prop="loadoMeterID1" width="180px" />
      <el-table-column :label="$t('xxx')" align="center" prop="userID1" width="180px" />
      <el-table-column :label="$t('yyy')" align="center" prop="userName1" width="180px" />
      <el-table-column :label="$t('ssss')" align="center" prop="secondWeight" width="220px" />
      <el-table-column :label="$t('tttt')" align="center" prop="truckWeight" width="220px" />
      <el-table-column :label="$t('uuuu')" align="center" prop="netWeight" width="220px" />
      <el-table-column :label="$t('vvvv')" align="center" prop="packWeight" width="220px" />
      <el-table-column :label="$t('wwww')" align="center" prop="goodsWeight" width="220px" />
      <el-table-column :label="$t('gg')" align="center" prop="toleranceAdd" width="160px" />
      <el-table-column :label="$t('hh')" align="center" prop="toleranceDec" width="160px" />
      <el-table-column :label="$t('ii')" align="center" prop="upFloatingWeight" width="160px" />
      <el-table-column :label="$t('jj')" align="center" prop="downFloatingWeight" width="160px" />
      <el-table-column :label="$t('kk')" align="center" prop="secondWeightTime" width="180px" />
      <el-table-column :label="$t('ll')" align="center" prop="loadoMeterID2" width="180px" />
      <el-table-column :label="$t('ga')" align="center" prop="userID2" width="180px" />
      <el-table-column :label="$t('hhh')" align="center" prop="userName2" width="180px" />
      <el-table-column :label="$t('iii')" align="center" prop="isUpdate" width="190px">
        <template slot-scope="scope">
          <div v-if="scope.row.isUpdate === '0'">{{ $t('jjj') }}</div>
          <div v-if="scope.row.isUpdate === '1'">{{ $t('kkk') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('lll')" align="center" fixed="right" class-name="small-padding fixed-width"
        width="120px">
        <template slot-scope="scope">
          <div v-if="scope.row.enterStatus === 0 && scope.row.status === 2">
            <el-button plain size="small" type="warning" icon="el-icon-caret-bottom" @click="handleEnter(scope.row)"
              v-hasPermi="['system:application:enter']">{{ $t('g') }}</el-button>
          </div>
          <div v-if="scope.row.enterStatus === 1 && scope.row.status === 2">
            <el-button plain size="small" type="success" icon="el-icon-caret-top" @click="handleOut(scope.row)"
              v-hasPermi="['system:application:out']">{{ $t('h') }}</el-button>
          </div>
          <!-- <div v-show="scope.row.enterStatus === 1 && scope.row.status === 2">
            <el-button plain size="small" type="success" icon="el-icon-caret-top" @click="handleoutform(scope.row)"
              v-hasPermi="['system:application:out']">{{ $t('i') }}</el-button>
          </div> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

  </div>
</template>

<script>
import { enterOrOut, getCarrierList, todayListApplication, getCheckerList, getDriverList, getApplication } from '@/api/system/application'
import SelectUser from '@/views/system/role/selectUser'
import { formatDate } from '@/utils'
import { outform } from "@/api/outsystem/outsystem";

export default {
  components: { SelectUser },
  dicts: ['enter_status', 'application_status', 'application_reason', 'appointmentStatus'],
  name: "Application",
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
      //司机列表
      drivers: [],
      //审核人列表
      checkers: [],
      //承运商列表
      carriers: [],
      // 总条数
      total: 0,
      // 车辆预约表格数据
      applicationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        applicationDate: null,
        createdDate: null,
        enterStatus: null,
        status: 2,
        queryCondition: null
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
    getCarrierList().then(response => {
      this.carriers = response.carrier
    });
    getCheckerList().then(response => {
      this.checkers = response.checkers
    })

  },
  methods: {
    /** 查询车辆预约列表 */
    getList() {
      this.loading = true;
      console.log("this.queryParam", this.queryParams)
      todayListApplication(this.queryParams).then(response => {
        this.applicationList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        applicationId: null,
        driverName: null,
        carNumber: null,
        driverId: null,
        phone: null,
        idcard: null,
        applicationDate: new Date(),
        outDate: null,
        enterDate: null,
        createdDate: null,
        createdBy: null,
        createdName: null,
        isdeleted: null,
        enterStatus: 0,
        status: 1,
        carrierId: null,
        carrierName: null,
        beforeWeight: null,
        remark: null,
        reason: null,
        postType: null,
        checkerId: null,
        checkerName: null
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.applicationId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },


    /** 进厂确认操作 */
    handleEnter(row) {
      const applicationId = row.applicationId || this.ids;
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        // this.form.appointmentStatus = response.data.appointmentStatus;
        this.$modal.confirm(this.$t('jjk2') + formatDate(new Date()) + this.$t('jjk3')).then(() => {
          return enterOrOut({ applicationId: applicationId, enterStatus: 1 });
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess(this.$t('jjk6'));
        }).catch(() => {
        });
      });
    },

    /** 空车出厂确认操作 */
    handleoutform(row) {
      const applicationId = row.applicationId || this.ids;
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentStatus = response.data.appointmentStatus;
        this.form.firstWeight = response.data.firstWeight;
        this.form.secondWeight = response.data.secondWeight;
        // const diff = Math.abs(this.form.secondWeight - this.form.firstWeight) / this.form.firstWeight;
        const m = (1 + 0.005) * this.form.firstWeight;
        const n = (1 - 0.005) * this.form.firstWeight;
        if (this.form.appointmentStatus === 6 && this.form.secondWeight >= n && this.form.secondWeight <= m) {
          this.$modal.confirm(this.$t('jjk2') + formatDate(new Date()) + this.$t('lmmm6')).then(() => {
            return enterOrOut({ applicationId: applicationId, enterStatus: 2 });
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess(this.$t('jkk8'));
          }).catch(() => {
          });
        } else {
          this.$modal.msgWarning(this.$t('lmmm7'));
        }
      });
    },

    /** 出厂确认操作 */
    handleOut(row) {
      const applicationId = row.applicationId || this.ids;
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentStatus = response.data.appointmentStatus;
        if (this.form.appointmentStatus === 6) {
          this.$modal.confirm(this.$t('jjk2') + formatDate(new Date()) + this.$t('jkk7')).then(() => {
            return enterOrOut({ applicationId: applicationId, enterStatus: 2 });
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess(this.$t('jkk8'));
          }).catch(() => {
          });
        } else {
          this.$modal.msgWarning(this.$t('lmmm5'));
        }
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/application/todayExport', {
        ...this.queryParams
      }, `todayApplication_${new Date().getTime()}.xlsx`)
    },

    // 未进厂 已进厂 已出场 点击事件
    enterStatuschange(e) {
      this.handleQuery()
    },
  }
};
</script>
