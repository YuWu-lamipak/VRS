<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="查询条件" prop="driverName">
        <el-input
          v-model="queryParams.queryCondition"
          placeholder="请输入查询条件"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="入厂时间" prop="applicationDate">
        <el-date-picker clearable size="small"
                        v-model="queryParams.applicationDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择入厂时间">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="申请时间" prop="createdDate">
        <el-date-picker clearable size="small"
                        v-model="queryParams.createdDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择申请时间">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="进场状态" prop="enterStatus">
        <!-- <el-select v-model="queryParams.enterStatus" placeholder="请选择进场状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.enter_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select> -->
        <el-radio-group v-model="queryParams.enterStatus" @change="enterStatuschange">
          <el-radio-button v-for="dict in dict.type.enter_status" :label="dict.value" :name="dict.value">{{ dict.label }}</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="预约状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择预约单状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.application_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:application:add']"
        >新增</el-button>
      </el-col>
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
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:application:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="applicationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="预约单号" align="center" prop="applicationNo" width="120px"/>-->
      <el-table-column label="车牌号" align="center" prop="carNumber" width="110px" />
      <el-table-column label="承运商" align="center" prop="carrierName" />
      <el-table-column label="司机" align="center" prop="driverName" />
      <el-table-column label="预约号码" align="center" prop="orderId" width="120">
        <template slot-scope="scope">
          <span>{{scope.row.orderId}}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="phone" width="110px" />
      <el-table-column label="创建人" align="center" prop="createdName" />
      <el-table-column label="受访人" align="center" prop="checkerName" />
      <el-table-column label="审核状态" align="center" prop="status" width="120px" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.application_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="进场状态" align="center" prop="enterStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.enter_status" :value="scope.row.enterStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="入厂时间" align="center" prop="applicationDate" width="180">
        <template slot-scope="scope">
          <span>{{scope.row.applicationDate}}</span>
        </template>
      </el-table-column>
      <el-table-column label="身份证号" align="center" prop="idcard"  width="170px"/>

      <el-table-column label="实际入厂时间" align="center" prop="enterDate" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.enterDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际出厂时间" align="center" prop="outDate" width="180">
        <template slot-scope="scope">
          <span>{{scope.row.outDate}}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="申请时间" align="center" prop="createdDate" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.createdDate}}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->


      <el-table-column label="送/提货预计重量" align="center" prop="beforeWeight" width="140px" >
        <template slot-scope="scope">
          <div>{{ scope.row.beforeWeight }}kg</div>
        </template>
      </el-table-column>

      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="原因" align="center" prop="reason" width="120px" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.application_reason" :value="scope.row.reasonKey"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="原因" align="center" prop="reason" :show-overflow-tooltip="true" />-->
      <el-table-column label="提交方式" align="center" prop="postType" >
        <template slot-scope="scope">
          <div v-if="scope.row.postType===2">代提交</div>
          <div v-if="scope.row.postType===1">本人提交</div>
        </template>

      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right"  class-name="small-padding fixed-width" width="120px">
        <template slot-scope="scope">
          <div v-show="scope.row.self && scope.row.status ===1">
            <el-button plain
              size="small"
              type="primary"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:application:edit']"
            >修改</el-button>
          </div>
          <div v-show="scope.row.enterStatus===0 && scope.row.status===2">
          <el-button plain
            size="small"
            type="warning"
            icon="el-icon-caret-bottom"
            @click="handleEnter(scope.row)"
            v-hasPermi="['system:application:enter']"
          >确认进厂</el-button>
          </div>
          <div  v-show="scope.row.enterStatus===1 && scope.row.status===2">
            <el-button plain
              size="small"
              type="success"
              icon="el-icon-caret-top"
              @click="handleOut(scope.row)"
              v-hasPermi="['system:application:out']"
            >确认出厂</el-button>
          </div>
          <div  v-show="scope.row.enterStatus===1 && scope.row.status===2">
            <el-button plain
              size="small"
              type="success"
              icon="el-icon-caret-top"
              @click="handleoutform(scope.row)"
              v-hasPermi="['system:application:out']"
            >空车出厂</el-button>
          </div>
          <div v-show="scope.row.status===1&&scope.row.check">
            <el-button plain
              size="small"
              type="danger"
              icon="el-icon-caret-top"
              @click="authAction(scope.row)"
              v-hasPermi="['system:application:authen']"
            >审核</el-button>
          </div>
          <el-button plain
            size="small"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:application:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改车辆预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="车牌号" prop="carNumber" required>
          <el-input style="width: 220px" v-model="form.carNumber" placeholder="请输入车牌号"  />
        </el-form-item>
        <el-form-item label="承运商" prop="carrierId" required>
          <el-select ref="carrierId" filterable v-model="form.carrierId" placeholder="请选择承运商"  @change="handleChange" >
            <el-option
              v-for="dict in carriers"
              :key="dict.userId"
              :label="dict.nickName"
              :value="dict.userId"
            ></el-option>
          </el-select>
        </el-form-item>



        <el-form-item label="司机" prop="driverId" required>

          <el-select ref="driverId"
            v-model="form.driverId"
            placeholder="请选择"
                     name="userId"
            clearable
            filterable
            @focus="selectFocus"
            @blur="selectBlur"
            @clear="selectClear"
            @change="chooseDriver"
          >
            <el-option
              v-for="item in drivers"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId" />
          </el-select>


<!--          <el-select ref="driverId"  filterable @change="chooseDriver" v-model="form.driverId" placeholder="请选择司机" >-->
<!--            <el-option-->
<!--              v-for="d in drivers"-->
<!--              :key="d.userId"-->
<!--              :label="d.nickName"-->
<!--              :value="d.userId"-->
<!--              ></el-option>-->
<!--          </el-select>-->
        </el-form-item>
        <el-form-item label="手机号"  prop="phone" required>
          <el-input style="width: 220px" v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item  label="身份证号" prop="idcard" required>
          <el-input style="width: 220px" v-model="form.idcard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="入厂时间" prop="applicationDate" required>
          <el-date-picker clearable size="small"
                          :picker-options="chooseDate"
                          v-model="form.applicationDate"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择入厂时间">
          </el-date-picker>
        </el-form-item>
<!--        <el-form-item label="出场时间" prop="outDate">-->
<!--          <el-date-picker clearable size="small"-->
<!--                          v-model="form.outDate"-->
<!--                          type="datetime"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="选择出场时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="入场时间" prop="enterDate">-->
<!--          <el-date-picker clearable size="small"-->
<!--                          v-model="form.enterDate"-->
<!--                          type="datetime"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="选择入场时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

        <el-form-item label="送/提货预计重量(kg)" prop="beforeWeight" required>
          <el-input style="width: 220px" v-model="form.beforeWeight" placeholder="请输入送/提货预计重量" />
        </el-form-item>
        <el-form-item  label="受访人" prop="checkerId" required>
          <el-select ref="checkerId" filterable v-model="form.checkerId" placeholder="请选择受访人" >
            <el-option
              v-for="c in checkers"
              :key="c.userId"
              :label="c.nickName"
              :value="c.userId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" rows="2" style="width: 400px" v-model="form.remark" placeholder="如果有随行人员，请在备注中写明" />
        </el-form-item>
        <el-form-item label="原因" prop="reasonKey">
          <el-select ref="reasonKey" v-model="form.reasonKey" placeholder="请选择原因" clearable size="small">
            <el-option
              v-for="dict in dict.type.application_reason"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
<!--          <el-input v-model="form.reason" type="textarea" placeholder="请输入内容" />-->
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核通过" :visible.sync="outformopen" width="600px" append-to-body :close-on-click-modal="false">
      <!-- 使用v-if 销毁组件 免除重置表单工作 -->
      <!-- cancel 取消按钮事件 textreturn 直接通过按钮事件 sure 审核通过 点击事件 -->
      <!-- sure 触发时代表 调用第三方接口成功且值返回正确 所以要再次调用直接通过接口 -->
      <outformcomponents v-if="outformopen" :detail="remember_row" @cancel="outformopen = false" @textreturn="authenApplicationapi" @sure="authenApplicationapi"/>
    </el-dialog>
  </div>
</template>

<script>
import { listApplication,authenApplication, getApplication,getCarrierList,getDriverList ,enterOrOut,
  getCheckerList, deleteApplication, addApplication, updateApplication,getDetailByUserId } from "@/api/system/application";
import SelectUser from '@/views/system/role/selectUser'
import outformcomponents from '@/views/system/application/indexform_out'
import {formatDate} from '@/utils/index'
import { outform } from "@/api/outsystem/outsystem";

export default {
  components: { SelectUser, outformcomponents },
  dicts: ['enter_status', 'application_status','application_reason'],
  name: "Application",
  data() {
    return {
      //是否修改司机或者是手输司机
      isChoose:false,
      //是否新增数据
      isAdd:false,
      //聚焦标识
      isFocus:false,
      //select改变标识
      isChange:false,
      //下拉框选中值或输入值
      chooseDate:{
        disabledDate(time) {
          return time.getTime() <= new Date().getTime() - 3600*24*1000;
        }
      },
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
      drivers:[],
      //审核人列表
      checkers:[],
      //承运商列表
      carriers:[],
      // 总条数
      total: 0,
      // 车辆预约表格数据
      applicationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      outformopen: false,
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
        driverId:null,
        driverName:null
      },
      // 表单校验
      rules: {
        carNumber: [
          { required:true,
            pattern: /^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/,
            message: "请输入正确的车牌号",
            trigger: ["blur","change"]
          }
        ],
        carrierId  :[
          {required:true,message:"司机所属承运商不能为空",trigger:["blur","change"]}
        ],

        driverId :[
          {required:true,message:"司机不能为空",trigger:["blur","change"]}
        ],
        phone: [
          {
            required:true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: ["blur","change"]
          }
        ],
        idcard: [
          { required:true,
            pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "请输入正确的身份证号",
            trigger: ["blur","change"] }
        ],
        applicationDate: [
          { required:true, message: "入厂时间不能为空", trigger: "blur" }
        ],
        checkerId:[
          {required:true,message:"受访人不能为空",trigger:["blur","change"]}
        ],
        beforeWeight:[
          {required:true,message:"送/提货预计重量不能为空",trigger:"blur"}
        ]
      },
      // 点击审核时 记录当前选中
      remember_row: null,
    };
  },
  created() {
    this.getList();
    getCarrierList().then(response=>{
      this.carriers = response.carrier
    });
    getCheckerList().then(response=>{
      this.checkers = response.checkers
    })
  },
  methods: {

    selectFocus(e){
      this.isFocus=true
      console.log("每次都进来focus？")
      this.form.driverName = e.target.value
    },
    selectBlur(e) {
      if (this.isChange/*||(!this.isChange && this.isFocus)*/){
        return
      }
      console.log("失焦事件")
      console.log(e.target.value,"e.target.value")
      console.log(e.target.value===null,"e.target.value")
      console.log(e.target.value==="","e.target.value")
      //修改司机标识
      this.isChoose = true;
      if (e.target.value !== '') {
        console.log("进输入")
        //输入时候有值。选择之后失焦事件e.target.value为空
        this.form.driverId = e.target.value;
        this.form.driverName = e.target.value;
        this.$forceUpdate()   // 强制更新
      }else{
        //下拉框选择的时候
        if( this.form.driverName === ''){
          //用于编辑时候，本身有值，但是下拉框下拉之后没有选中值
          this.form.driverName =null
          console.log(this.form.driverName,"driverName")
        }
     }
      // if (e.target.value !== '') {
      //   //输入时候有值。选择之后失焦事件e.target.value为空
      //   this.form.driverId = e.target.value;
      //   this.form.driverName = e.target.value;
      //   this.$forceUpdate()   // 强制更新
      // }else{
      //   //下拉框选择的时候
      //   this.form.driverName =null;
      // }
    },
    selectClear() {
      this.form.driverId = null
      this.form.driverName = null
      this.$forceUpdate()
    },

    /** 查询车辆预约列表 */
    getList() {
      this.loading = true;
      listApplication(this.queryParams).then(response => {
        this.applicationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    handleChange(value) {
      //根据选择的承运商查询其下司机
      getDriverList(value).then(response => {
        this.drivers = response.drivers
      })
    },

    chooseDriver(data){
      console.log(data,"选择change事件 ")
      //修改司机标识
      this.isChoose = true;
      this.isChange=true
      //下拉框change事件，将name清空，以此来判定是下拉框还是输入形式
      this.form.driverName=null
      this.form.driverId = data
      console.log(this.form.driverId,"选择之后的ID")
      this.$forceUpdate()
      console.log(this.form.driverId,"强制更新之后的id")
      if( this.form.driverId !==""){
        getDetailByUserId(data).then(response=>{
          this.form.idcard = response.user.idcard;
          this.form.phone = response.user.phoneNumber;
        })
      }


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
        applicationDate: null,
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
        reasonKey: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.isAdd=true;
      this.reset();
      //是否修改司机或者是手输司机
      this.isChoose=false;
      //聚焦标识
      this.isFocus=false;
      //select改变标识
      this.isChange=false;
      this.open = true;
      this.title = "添加车辆预约";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      //是否修改司机或者是手输司机
      this.isChoose=false
      //是否新增数据
      this.isAdd=false
      //聚焦标识
      this.isFocus=false
      //select改变标识
      this.isChange=false
      const applicationId = row.applicationId || this.ids
      getApplication(applicationId).then(response => {
      console.log(response.data,"data前")
        this.form = response.data;
        if (response.data.driverId===null||response.data.driverId===''){
          this.form.driverId = response.data.driverName
          //手输标识
          this.isChoose = true;
          console.log("空id赋值")
        }
        console.log(this.isChoose,"choose")
        //查询预约司机
        getDriverList(this.form.carrierId).then(response => {
          this.drivers = response.drivers
        })

        this.open = true;
        this.title = "修改车辆预约";
      });

    },

    /** 进厂确认操作 */
    handleEnter(row) {
      const applicationIds = row.applicationId || this.ids;
      this.$modal.confirm('当前时间为' + formatDate(new Date()) + ",是否确认入厂?").then(function() {
        return enterOrOut({ applicationId: applicationIds, enterStatus: 1 });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("入厂成功");
      }).catch(() => {
      });
    },

    /** 空车出厂操作 */
    handleoutform(row) {
      let that = this
      const applicationIds = row.applicationId || this.ids;
      this.$modal.confirm('当前时间为' + formatDate(new Date()) + ",是否空车出厂?").then(function() {
        let data = {
          AppointmentID: row.orderId,
          UpdateType: 'EmptyTruck'
        }
        let params = {
            name: "EmptyTruck",
            json: JSON.stringify(data),
            company: "LMKS",
            // 对接外部接口 需要header 特殊处理 届时将header传入
            headers: {
                // requestId: that.applicationId,
                requestId: that.getuuid(),
                trackId: null,
                sourceSystem: 'ESB',
                serviceName: 'S_ESB_D365_CreateTruckAppointmentList_S'
            }
        }
        return outform(params)
      }).then((res) => {
        if (res) {

              let returnres = JSON.parse(res) || {}
              console.log(returnres)
              if (returnres.Code === '0') {
                that.getList();
                that.$modal.msgSuccess("空车出厂成功");
              } else {
                  that.$message({
                      type: 'error',
                      message: returnres.Message || '审核提交失败'
                  })
              }
          }
      }).catch((errors) => {
        // console.log(errors)
        // that.$message({
        //     type: 'error',
        //     message: errors
        // })
      });
    },

    /** 出厂确认操作 */
    handleOut(row) {
      const applicationIds = row.applicationId || this.ids;
      this.$modal.confirm('当前时间为' + formatDate(new Date()) + ",是否确认出厂?").then(function() {
        return enterOrOut({ applicationId: applicationIds, enterStatus: 2 });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("出厂成功");
      }).catch(() => {
      });
    },

    authAction(row) {
      let that = this
      this.remember_row = row
      this.$confirm('请选择审核状态', {
        distinguishCancelAndClose: true,
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning',
        center: true
      })
        .then(() => {
          // 打开第三方提交审核表单dialog
          that.outformopen = true
          // authenApplication({ applicationId: row.applicationId, status: 2 })
          //   .then((res) => {
          //     this.getList();
          //     this.$message({
          //       type: 'success',
          //       message: '审核通过!'
          //     })
          //   })
          //   .catch((error) => {
          //     this.getList();
          //   })
        })
        .catch((action) => {
          if (action === 'cancel') {
            authenApplication({ applicationId: row.applicationId, status: 3 })
              .then((res) => {
                this.getList();
                this.$message({
                  type: 'info',
                  message: '审核不通过'
                })
              })
              .catch((error) => {
                this.getList();
              })
          }
        })
    },

    // 审核通过
    authenApplicationapi(retundata) {
      authenApplication({
        applicationId: this.remember_row.applicationId,
        status: 2,
        orderId: retundata.orderId || null,
        businessType: retundata.businessType || null
      })
        .then((res) => {
          this.getList();
          this.$message({
            type: 'success',
            message: '审核通过!'
          })
          this.outformopen = false
        })
        .catch((error) => {
          this.getList();
        })
    },

    /** 提交按钮 */
    submitForm() {

      this.$refs["form"].validate(valid => {
        if (valid) {
          //校验之后清除校验，否则下面赋值之后会再次校验
          this.$nextTick(()=>{
            this.$refs['form'].clearValidate();
          })
          console.log("变化之前driverId",this.form.driverId)
          if (this.isChoose||this.isAdd){
            console.log("进来了？")
            //司机选择有变动的情况下,或者是新增的情况下
            //可输入不为空的情况下，司机id为空
            if (this.form.driverName!=null){
              this.form.driverId =null;
            }else{
              //下拉框选中情况下
              this.form.driverName = this.$refs.driverId.selectedLabel;
            }
          }
          console.log("变化之后driverId",this.form.driverId)

          //获取选中的司机、承运商、审核人姓名
          this.form.carrierName = this.$refs.carrierId.selectedLabel;
          this.form.checkerName = this.$refs.checkerId.selectedLabel;

          //原因
          this.form.reason = this.$refs.reasonKey.selectedLabel;

          if (this.form.applicationId != null) {
            updateApplication(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApplication(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {

      const applicationIds = row.applicationId || this.ids;
      this.$modal.confirm('该操作请谨慎操作，是否确认删除该车辆预约单数据？').then(function() {
        return deleteApplication({applicationId:applicationIds});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/application/export', {
        ...this.queryParams
      }, `application_${new Date().getTime()}.xlsx`)
    },

    // 未进场 已进厂 已出场 点击事件
    enterStatuschange(e) {
      // console.log(e)
      this.handleQuery()
    },

    // 获取uuid 自己生成随机数
    getuuid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
            return v.toString(16);
        });
    },
  }
};
</script>
