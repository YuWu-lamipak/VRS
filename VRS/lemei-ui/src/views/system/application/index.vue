<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('k')" prop="queryCondition">
        <el-input v-model="queryParams.queryCondition" :placeholder="$t('aaa')" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>

      <!-- <el-form-item :label="$t('l')" prop="applicationDate">
        <el-date-picker clearable size="small" v-model="queryParams.applicationDate" type="date"
          value-format="yyyy-MM-dd" :placeholder="$t('bbb')">
        </el-date-picker>
      </el-form-item> -->

      <el-form-item :label="$t('l')" prop="dateRange">
        <el-date-picker clearable size="small" v-model="queryParams.dateRange" type="daterange" value-format="yyyy-MM-dd"
          range-separator="-" :start-placeholder="$t('hnn')" :end-placeholder="$t('inn')">
        </el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('m')" prop="createdDate">
        <el-date-picker clearable size="small" v-model="queryParams.createdDate" type="date" value-format="yyyy-MM-dd"
          :placeholder="$t('ccc')">
        </el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('n')" prop="enterStatus">
        <el-radio-group v-model="queryParams.enterStatus" @change="enterStatuschange" clearable size="small">
          <el-radio-button v-for="dict in dict.type.enter_status" :label="dict.value" :name="dict.value">{{
            $t(`lll${dict.value}`) }}</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <!-- <el-form-item :label="$t('y')" prop="businessType">
        <el-radio-group v-model="queryParams.businessType" @change="businessTypechange" clearable size="small">
          <el-radio v-for="dict in businessTypelist" :label="dict.value" :name="dict.value">{{
            $t(`ccc${dict.value}`) }}</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item :label="$t('y')" prop="businessType">
        <el-select v-model="queryParams.businessType"size="small" style="width: 120px" placeholder="请选择">
        <el-option
        v-for="dict in businessTypelist"
        :key="dict.value"
        :label="$t(`ccc${dict.value}`)"
        :value="dict.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('p')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('ddd')" clearable size="small" style="width: 225px;">
          <el-option v-for="dict in dict.type.application_status" :value="dict.value" :label="$t(`ll${dict.value}`)"
            :name="dict.value">{{ $t(`ll${dict.value}`) }}</el-option>
          <!--        v-for="dict in dict.type.application_status" :key="dict.value" :label="dict.label"
          :value="dict.value"
         v-for="dict in dict.type.application_status" :label="dict.value":value="dict.value" :key="dict.value"-->
        </el-select>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('o') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('q') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:application:add']">{{ $t('a') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single" @click="statusHandleUpdate"
          v-hasPermi="['system:application:statusEdit']">{{ $t('ess')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="certificateHandleConfirm" v-hasPermi="['system:application:certificateConfirm']">{{ $t('esss')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="loseWeightHandleUpdate" v-hasPermi="['system:application:loseWeightEdit']">{{ $t('b')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="saleAppointmentWeightHandleUpdate" v-hasPermi="['system:application:saleAppointmentWeightEdit']">{{
            $t('xsxg')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="scrapAppointmentWeightHandleUpdate" v-hasPermi="['system:application:scrapAppointmentWeightEdit']">{{
            $t('xsfl')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="shippedWithPalletWeightHandleUpdate" v-hasPermi="['system:application:palletWeightEdit']">{{ $t('xgp')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleoutform"
          v-hasPermi="['system:application:empty']">{{ $t('i')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="cancelReservationHandleUpdate" v-hasPermi="['system:application:cancelReservation']">{{ $t('qxyy')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="reWeight1" v-hasPermi="['system:application:reWeight1']">{{ $t('reWeight1')
          }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="reWeight2" v-hasPermi="['system:application:reWeight2']">{{ $t('reWeight2')
          }}</el-button>
      </el-col>
      
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single" @click="cancelWeight"
          v-hasPermi="['system:application:weightCancelInfo']">{{ $t('c') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-edit" size="mini" :disabled="single" @click="invalidWeight"
          v-hasPermi="['system:application:weightCancelInfo']">{{ $t('d') }}</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
              <el-button>
                type="danger"
               plain
                icon="el-icon-delete"
                size="mini"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['system:application:remove']"
              >删除</el-button>
           </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:application:export']">{{ $t('e') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :disabled="single" @click="handlePrint"
          v-hasPermi="['system:application:print']">{{
            $t('kmm') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <div>
      <el-table ref="table" v-loading="loading" :data="applicationList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="申请id" align="center" prop="applicationId" width="120px" v-if="false" />
        <el-table-column :label="$t('r')" align="center" prop="serialNumber" width="100px" fixed="left" />
        <el-table-column :label="$t('crcpz')" align="center" prop="mark" width="180px" />
        <el-table-column :label="$t('u')" align="center" prop="carNumber" width="110px" />
        <el-table-column :label="$t('dddd')" align="center" prop="applicationDate" width="140px">
          <template slot-scope="scope">
            <span>{{ scope.row.applicationDate }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column :label="$t('jkk1')" align="center" prop="wMSPickingRouteIDs" width="170px" />
        <el-table-column :label="$t('jkk2')" align="center" prop="wMSPickingRouteIDs2" width="170px" /> -->
        <el-table-column :label="$t('v')" align="center" prop="carrierName" width="125px" />
        <el-table-column :label="$t('w')" align="center" prop="driverName" width="120px" />
        <el-table-column :label="$t('fl')" align="center" prop="scrap" width="120px" />
        <el-table-column :label="$t('uuuu')" align="center" prop="netWeight" width="190px" />
        <el-table-column :label="$t('s')" align="center" prop="businessDescription" width="110px" />
        <el-table-column :label="$t('ggg')" align="center" prop="remark" width="220px" />
        <el-table-column :label="$t('y')" align="center" prop="businessType" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.businessType === 0">{{ $t('aaaa') }}</span>
            <span v-if="scope.row.businessType === 1">{{ $t('bbbb') }}</span>
            <span v-if="scope.row.businessType === 2">{{ $t('cccc') }}</span>
            <span v-if="scope.row.businessType === 3">{{ $t('ddddd') }}</span>
            <span v-if="scope.row.businessType === 4">{{ $t('ccc4') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('xxxx')" align="center" prop="enableControl" width="120px">
          <template slot-scope="scope">
            <div v-if="scope.row.enableControl !=1">{{ $t('yyyy') }}</div>
            <div v-if="scope.row.enableControl === 1">{{ $t('zz') }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('eeee')" align="center" prop="reason" width="200px">
          <!-- <template slot-scope="scope">
            <dict-tag :options="dict.type.application_reason" :value="scope.row.reasonKey" />
          </template> -->

          <template slot-scope="scope">
            <span v-if="scope.row.reason === '装货-成品'">{{ $t('llll1') }}</span>
            <span v-if="scope.row.reason === '装货-非成品'">{{ $t('llll2') }}</span>
            <span v-if="scope.row.reason === '送货'">{{ $t('llll3') }}</span>
            <span v-if="scope.row.reason === '运废料'">{{ $t('llll4') }}</span>
            <span v-if="scope.row.reason === '借磅'">{{ $t('llll5') }}</span>
            <span v-if="scope.row.reason === '其他'">{{ $t('llll6') }}</span>
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
        <el-table-column :label="$t('t')" align="center" prop="factoryType" width="100px">
          <template slot-scope="scope">
            <span v-if="scope.row.factoryType === 'KS'">KS</span>
            <span v-if="scope.row.factoryType === 'IN'">IN</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('z')" align="center" prop="phone" width="110px" />
        <el-table-column :label="$t('aa')" align="center" prop="createdName" width="220px" />
        <el-table-column :label="$t('bb')" align="center" prop="checkerName" width="130px" />
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
        <el-table-column :label="$t('tt')" align="center" prop="supplyName" width="120px"
          :show-overflow-tooltip="true" />
        <el-table-column :label="$t('uu')" align="center" prop="custCode" width="135px" />
        <el-table-column :label="$t('vv')" align="center" prop="custName" width="120px" v-if="false" />
        <el-table-column :label="$t('ww')" align="center" prop="documentCode" width="120px" v-if="false" />
        <el-table-column :label="$t('xx')" align="center" prop="dataAreaId" width="120px" v-if="false" />
        <el-table-column :label="$t('yy')" align="center" prop="factoryCode" width="120px" v-if="false" />
        <el-table-column :label="$t('mm')" align="center" prop="taskId" width="145px" :show-overflow-tooltip="true" />
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
        <!-- <el-table-column :label="$t('ttt')" align="center" prop="appointmentStatus" width="180px">
          <template slot-scope="scope">
            <div v-if="scope.row.appointmentStatus === 0"
              style="background-color:red;border-radius: 40%;font-weight: bold;">{{ $t('gm') }}</div>
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
            <div v-if="scope.row.appointmentStatus === 6" style="background-color:palegreen;border-radius: 40%;">{{
              $t('kmb') }}</div>
          </template>
        </el-table-column> -->
        <el-table-column :label="$t('uuu')" align="center" prop="firstWeight" width="170px" />
        <el-table-column :label="$t('vvv')" align="center" prop="firstWeightTime" width="180px" />
        <el-table-column :label="$t('www')" align="center" prop="loadoMeterID1" width="180px" />
        <el-table-column :label="$t('xxx')" align="center" prop="userID1" width="180px" />
        <el-table-column :label="$t('yyy')" align="center" prop="userName1" width="180px" />
        <el-table-column :label="$t('ssss')" align="center" prop="secondWeight" width="220px" />
        <el-table-column :label="$t('tttt')" align="center" prop="truckWeight" width="220px" />
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
          width="130px">

          <template slot-scope="scope">
            <div v-show="scope.row.self && scope.row.status === 1 && scope.row.appointmentStatus != -1">
              <el-button plain size="small" type="primary" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['system:application:edit']">{{ $t('f') }}</el-button>
            </div>
            <div v-show="scope.row.enterStatus === 0 && scope.row.status === 2">
              <el-button plain size="small" type="warning" icon="el-icon-caret-bottom" @click="handleEnter(scope.row)"
                v-hasPermi="['system:application:enter']">{{ $t('g') }}</el-button>
            </div>
            <div v-show="scope.row.enterStatus === 1 && scope.row.status === 2">
              <el-button plain size="small" type="success" icon="el-icon-caret-top" @click="handleOut(scope.row)"
                v-hasPermi="['system:application:out']">{{ $t('h') }}</el-button>
            </div>
            <!-- <div v-show="scope.row.enterStatus === 1 && scope.row.status === 2">
              <el-button plain size="small" type="success" icon="el-icon-caret-top" @click="handleoutform(scope.row)"
                v-hasPermi="['system:application:empty']">{{ $t('i') }}</el-button>
            </div> -->
            <div v-show="scope.row.status === 1 && scope.row.check && scope.row.appointmentStatus != -1">
              <el-button plain size="small" type="danger" icon="el-icon-caret-top" @click="authAction(scope.row)"
                v-hasPermi="['system:application:authen']">{{ $t('j') }}</el-button>
            </div>
            <!-- <el-button plain
            size="small"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:application:remove']"
          >删除</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </div>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改车辆预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!-- <el-form-item :label="$t('ab')" prop="factoryType" label-width="300px" :required="rules.factoryType.required">
          <el-select ref="factoryType" v-model="form.factoryType" :placeholder="$t('ac')"  @change="factoryTypeChange"
            style="width: 220px;">
            <el-option v-for="dict in factoryTypelist" :key="dict.label" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item :label="$t('ad')" prop="carNumber" label-width="300px"
          :rules="[{ required: true, message: $t('jk2'), trigger: ['blur', 'change'] }]">
          <el-input style="width: 270px" v-model="form.carNumber" :placeholder="$t('ae')" />
        </el-form-item>
        <el-form-item :label="$t('v')" prop="carrierId" label-width="300px"
          :rules="[{ required: true, message: $t('lm7'), trigger: ['blur', 'change'] }]">
          <el-select ref="carrierId" filterable v-model="form.carrierId" :placeholder="$t('af')" @change="handleChange"
            style="width: 250px;">
            <el-option v-for="dict in carriers" :key="dict.userId" :label="dict.nickName"
              :value="dict.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('ag')" prop="enter_type" label-width="300px"
          :rules="[{ required: true, message: $t('lm6'), trigger: 'blur' }]">
          <el-radio-group v-model="form.enter_type" @change="onChangeRadio">
            <el-radio label="0">{{ $t('abb') }}</el-radio>
            <el-radio label="1">{{ $t('acc') }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="form.enter_type === '1'" :label="$t('add')" prop="driverName" label-width="300px"
          :rules="[{ required: true, message: $t('jk'), trigger: 'blur' }]">
          <el-input style="width: 270px" v-model="form.driverName" :placeholder="$t('aee')" />
        </el-form-item>

        <el-form-item v-if="form.enter_type === '0'" :label="$t('add')" prop="driverId" label-width="300px" required>

          <el-select ref="driverId" v-model="form.driverId" :placeholder="$t('aff')" name="userId" clearable filterable
            @change="chooseDriver" style="width: 270px">
            <el-option v-for="item in drivers" :key="item.userId" :label="item.nickName" :value="item.userId" />
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
        <el-form-item :label="$t('agg')" prop="phone" label-width="300px"
          :rules="[{ required: true, message: $t('lm'), trigger: ['blur', 'change'] }]">
          <el-input style="width: 270px" v-model="form.phone" :placeholder="$t('abbb')" />
        </el-form-item>
        <el-form-item :label="$t('accc')" prop="idcard" label-width="300px"
          :rules="[{ required: true, message: $t('lm1'), trigger: ['blur', 'change'] }]">
          <el-input style="width: 270px" v-model="form.idcard" :placeholder="$t('addd')" />
        </el-form-item>
        <el-form-item :label="$t('aeee')" prop="applicationDate" label-width="300px"
          :rules="[{ required: true, message: $t('lm2'), trigger: 'blur' }]">
          <el-date-picker :picker-options="chooseDate" v-model="form.applicationDate" type="date"
            value-format="yyyy-MM-dd" :placeholder="$t('afff')" style="width: 240px">
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
        <el-form-item :label="$t('aggg')" prop="reasonKey" label-width="300px"
          :rules="[{ required: true, message: $t('lm4'), trigger: ['blur', 'change'] }]">
          <el-select style="width: 220px;" ref="reasonKey" v-model="form.reasonKey" :placeholder="$t('abbbb')">
            <!-- <el-option v-for="dict in dict.type.application_reason" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option> -->
            <el-option v-for="dict in dict.type.application_reason" :key="dict.value" :label="$t(`llll${dict.value}`)"
              :value="dict.value"></el-option>
          </el-select>
          <!--          <el-input v-model="form.reason" type="textarea" placeholder="请输入内容" />-->
        </el-form-item>
        <!-- 当 reasonKey 的值为 4 时，新增废料名称的下拉输入框 -->
        <el-form-item v-if="form.reasonKey === '4'" :label="$t('fl')" prop="scrapKey" label-width="300px"
          :rules="[{ required: true, message: $t('fll'), trigger: 'blur' }]">
          <el-select style="width: 240px;" ref="scrapKey" filterable v-model="form.scrapKey" :placeholder="$t('flll')">
            <el-option v-for="dict in dict.type.application_scrap" :key="dict.value" :label="dict.label"
              :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('acccc')" prop="beforeWeight"
          :rules="[{ required: true, message: $t('lm3'), trigger: 'blur' }]" label-width="300px">
          <el-input style="width: 350px;" v-model="form.beforeWeight" :placeholder="$t('adddd')" />
        </el-form-item>
        <el-form-item :label="$t('aeeee')" prop="checkerId" label-width="300px"
          :rules="[{ required: true, message: $t('lm5'), trigger: ['blur', 'change'] }]">
          <el-select ref="checkerId" filterable v-model="form.checkerId" :placeholder="$t('affff')">
            <el-option v-for="c in checkers" :key="c.userId" :label="c.nickName" :value="c.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('agggg')" prop="remark"
          :rules="[{ required: true, message: '备注不能为空', trigger: 'change' }]" label-width="300px"
          v-if="form.reasonKey === '5'">
          <el-input type="textarea" rows="2" style="width: 350px" v-model="form.remark" @input="handleInput('remark')"
            @clear="handleClear('remark')" :placeholder="$t('abbbbb')" />
        </el-form-item>
        <el-form-item :label="$t('ggg')" prop="remark" label-width="300px" v-else>
          <el-input type="textarea" rows="2" style="width: 350px" v-model="form.remark" :placeholder="$t('abbbbb')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="isSubmitDisabled" @click="submitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="cancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改状态 -->
    <el-dialog :title="title" :visible.sync="statusVisible" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item :label="$t('es')" prop="enterStatus">
          <el-select ref="enterStatus" v-model="form.enterStatus" style="width: 200px;">
            <el-option v-for="dict in enterStatuslist" :key="dict.value" :label="$t(`es${dict.value}`)"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('as')" prop="appointmentStatus">
          <el-select ref="appointmentStatus" v-model="form.appointmentStatus" style="width: 200px;">
            <el-option v-for="dict in appointmentStatuslist" :key="dict.value" :label="$t(`as${dict.value}`)"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="statusSubmitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="statusCancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改扣重 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">

        <el-form-item :label="$t('nn')" prop="loseWeight" label-width="300px"
          :rules="[{ required: true, message: $t('dzbb'), trigger: 'blur' }]">
          <el-input style="width: 300px" v-model="form.loseWeight" :placeholder="$t('nnnn')" />
        </el-form-item>

        <el-form-item :label="$t('nnc')" prop="floatReason" label-width="300px"
          :rules="[{ required: true, message: $t('lm8'), trigger: 'blur' }]">
          <el-input style="width: 250px" type="textarea" v-model="form.floatReason" :placeholder="$t('nna')" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="loseWeightSubmitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="loseWeightCancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改销售预约重量 -->
    <el-dialog :title="title" :visible.sync="saleVisible" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('opp1')" prop="appointmentWeight" label-width="300px">
          <el-input style="width: 250px" v-model="form.appointmentWeight" disabled />
        </el-form-item>
        <el-form-item :label="$t('opp2')" prop="editWeight" label-width="300px"
          :rules="[{ required: true, message: $t('opp22'), trigger: 'blur' }]">
          <el-input style="width: 300px" v-model="form.editWeight" :placeholder="$t('opp222')" />
        </el-form-item>
        <el-form-item :label="$t('opp3')" prop="editPerson" label-width="300px"
          :rules="[{ required: true, message: $t('opp33'), trigger: 'blur' }]">
          <el-input style="width: 250px" v-model="form.editPerson" :placeholder="$t('opp333')" />
        </el-form-item>
        <el-form-item :label="$t('opp4')" prop="editReason" label-width="300px"
          :rules="[{ required: true, message: $t('opp44'), trigger: 'blur' }]">
          <el-input style="width: 250px" type="textarea" v-model="form.editReason" :placeholder="$t('opp444')" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saleAppointmentWeightSubmitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="saleAppointmentWeightCancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改废料预约重量 -->
    <el-dialog :title="title" :visible.sync="scrapVisible" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('opp1')" prop="appointmentWeight" label-width="300px">
          <el-input style="width: 250px" v-model="form.appointmentWeight" disabled />
        </el-form-item>
        <el-form-item :label="$t('op2p2')" prop="editWeight" label-width="300px"
          :rules="[{ required: true, message: $t('opp23'), trigger: 'blur' }]">
          <el-input style="width: 300px" v-model="form.editWeight" :placeholder="$t('opp223')" />
        </el-form-item>
        <el-form-item :label="$t('opp3')" prop="editPerson" label-width="300px"
          :rules="[{ required: true, message: $t('opp33'), trigger: 'blur' }]">
          <el-input style="width: 250px" v-model="form.editPerson" :placeholder="$t('opp333')" />
        </el-form-item>
        <el-form-item :label="$t('opp4')" prop="editReason" label-width="300px"
          :rules="[{ required: true, message: $t('opp44'), trigger: 'blur' }]">
          <el-input style="width: 250px" type="textarea" v-model="form.editReason" :placeholder="$t('opp444')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="scrapAppointmentWeightSubmitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="scrapAppointmentWeightCancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改出厂带栈板重量 -->
    <el-dialog :title="title" :visible.sync="palletVisible" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('dzb')" prop="shippedWithPalletWeight" label-width="300px"
          :rules="[{ required: true, message: $t('dzbb'), trigger: 'blur' }]">
          <el-input style="width: 300px" v-model="form.shippedWithPalletWeight" :placeholder="$t('dzbbb')" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="shippedWithPalletWeightSubmitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="palletWeightCancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>

    <!-- 车辆重新称重 -->
    <el-dialog :title="title" :visible.sync="reWeightVisible" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
       
        <el-form-item :label="$t('opp4')" prop="editReason" label-width="300px"
          :rules="[{ required: true, message: $t('opp44'), trigger: 'blur' }]">
          <el-input style="width: 250px" type="textarea" v-model="form.editReason" :placeholder="$t('opp444')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="reWeightSubmitForm">{{ $t('accccc') }}</el-button>
        <el-button @click="reWeightCancel">{{ $t('addddd') }}</el-button>
      </div>
    </el-dialog>
    

    <el-dialog :title="$t('nnnna')" :visible.sync="outformopen" width="600px" append-to-body
      :close-on-click-modal="false">
      <!-- 使用v-if 销毁组件 免除重置表单工作 -->
      <!-- cancel 取消按钮事件 textreturn 直接通过按钮事件 sure 审核通过 点击事件 -->
      <!-- sure 触发时代表 调用第三方接口成功且值返回正确 所以要再次调用直接通过接口 -->
      <!-- <outformcomponents v-if="outformopen" :detail="remember_row" @cancel="outformopen = false" @textreturn="authenApplicationApi" @sure="authenApplicationApi"/> -->
      <outformcomponents v-if="outformopen" :detail="remember_row" @cancel="outformopen = false"
        @sure="authenApplicationApi" />
    </el-dialog>
  </div>
</template>

<script>
import {
  listApplication, authenApplication, getApplication, getCarrierList, getDriverList, enterOrOut, cancelReservationApplication,reWeight1,reWeight2,
  getCheckerList, deleteApplication, addApplication, updateApplication, statusApplication, enterOrOutMark, loseWeightApplication, saleAppointmentWeightApplication, scrapAppointmentWeightApplication, updateShippedWithPalletWeightApplication, weightCancelInfo, getDetailByUserId
} from "@/api/system/application";
import SelectUser from '@/views/system/role/selectUser'
import outformcomponents from '@/views/system/application/indexform_out'
import { formatDate } from '@/utils/index'
import { outform } from "@/api/outsystem/outsystem";
import { start } from "nprogress";

export default {
  components: { SelectUser, outformcomponents },
  dicts: ['enter_status', 'application_status', 'application_reason', 'application_scrap'],
  name: "Application",
  data() {

    return {

      // 工厂类型数组
      factoryTypelist: [
        {
          label: 'KS',
          value: 'KS'
        },
        {
          label: 'IN',
          value: 'IN'
        }
      ],
      // 进厂状态数组
      enterStatuslist: [
        {
          label: '未进厂',
          value: 0
        },
        {
          label: '已进厂',
          value: 1
        },
        {
          label: '已出厂',
          value: 2
        },
        {
          label: '取消进厂',
          value: 3
        }
      ],
      // 预约状态数组
      appointmentStatuslist: [
        {
          label: '待制卡',
          value: 0
        },
        {
          label: '待上一磅',
          value: 1
        },
        {
          label: '待确认一磅',
          value: 2
        },
        {
          label: '待上二磅',
          value: 3
        },
        {
          label: '待确认二磅',
          value: 4
        },
        {
          label: '待退卡',
          value: 5
        },
        {
          label: '已完成',
          value: 6
        },
        {
          label: '取消预约',
          value: 7
        }
      ],
    // 业务类型数组
     businessTypelist: [
        {
          label: '采购',
          value: 0
        },
        {
          label: '销售',
          value: 1
        },
        {
          label: '废料',
          value: 2
        },
        {
          label: '固废危废处置',
          value: 4
        },
        {
          label: '其他',
          value: 3
        }
      ],
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
      // 车辆预约表格数据
      applicationList: [],
      // 弹出层标题
      title: "",
      // 是否显示新增和修改弹出层
      open: false,
      // 是否显示状态修改弹出层
      statusVisible: false,
      // 是否显示修改扣重弹出层
      dialogVisible: false,
      // 是否显示修改销售预约重量弹出层
      saleVisible: false,
      // 是否显示修改废料预约重量弹出层
      scrapVisible: false,
      // 是否显示修改出厂带栈板重量弹出层
      palletVisible: false,
      // reWeight1:false,
      // reWeight2:false,
      reWeightVisible: false,
      isSubmitDisabled: false,
      outformopen: false,
      // 操作，默认为增加
      operation: 'add',
      // 输入框中的值
      inputValue: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateRange: [],
        startTime: null,
        endTime: null,
        createdDate: null,
        enterStatus: null,
        status: null,
        businessType:null
      },
      // 表单参数
      form: {
        enter_type: '0',
        driverId: null,
        driverName: null,
        beforeWeight: null,
        remark: null,
        applicationId: null,
        enableControl: null,
        appointmentWeight: null,
        loseWeight: null,
        shippedWithPalletWeight: null,
        floatReason: null,
        factoryType: null,
        enterStatus: null,
        appointmentStatus: null,
        firstWeight: null,
        secondWeight: null,
        status: null,
        businessType: null
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
    getCarrierList().then(response => {
      this.carriers = response.carrier
    });
    getCheckerList().then(response => {
      this.checkers = response.checkers
    })
  },
  watch: {
    open(val) {
      if (!val) {
        this.submitButtonDisabled = false;
      }
    }
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
    /** 查询车辆预约列表 */
    getList() {
      this.loading = true;
      // 将 dateRange 的值赋值给 queryParams 的 startTime 和 endTime
      if (this.queryParams.dateRange && this.queryParams.dateRange.length === 2) {
        this.queryParams.startTime = this.queryParams.dateRange[0];
        this.queryParams.endTime = this.queryParams.dateRange[1];
      } else {
        this.queryParams.startTime = null;
        this.queryParams.endTime = null;
      }
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

    chooseDriver(data) {
      this.form.driverId = data
      if (this.form.driverId !== "") {
        getDetailByUserId(data).then(response => {
          this.form.idcard = response.user.idcard;
          this.form.phone = response.user.phoneNumber;
        })
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.isSubmitDisabled = false;
      this.reset();
    },
    // 取消修改扣重按钮
    statusCancel() {
      this.statusVisible = false;
      // this.reset();
    },
    // 取消修改扣重按钮
    loseWeightCancel() {
      this.dialogVisible = false;
      // this.reset();
    },
    // 取消修改销售预约重量按钮
    saleAppointmentWeightCancel() {
      this.saleVisible = false;
      this.reset();
    },
    // 取消修改废料预约重量按钮
    scrapAppointmentWeightCancel() {
      this.scrapVisible = false;
      this.reset();
    },
    // 取消修改带栈板重量按钮
    palletWeightCancel() {
      this.palletVisible = false;
      // this.reset();
    },
    // 取消重新称重上磅按钮
    reWeightCancel() {
      this.reWeightVisible = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        enter_type: null,
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
        scrap: null,
        scrapKey: null,
        postType: null,
        checkerId: null,
        checkerName: null,
        appointmentWeight: null,
        factoryType: null,
        shippedWithPalletWeight: null,
        saleEdit: null,
        salePerson: null,
        saleReason: null
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
      if (selection.length === 1) {
        this.ids = selection.map(item => item.applicationId)
        this.printContent = ''; // 清空上次选中行的打印内容
      } else {
        this.selectedRow = null;
        this.printContent = '';
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('gjj');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const applicationId = row.applicationId || this.ids
      getApplication(applicationId).then(response => {
        this.form = {
          ...response.data,
          enter_type: null,
        };
        if (response.data.driverId === null || response.data.driverId === '') {
          //  this.form.driverId = response.data.driverName
          //手输标识
          this.form.enter_type = '1';
          this.isChoose = true;
        } else {
          this.form.enter_type = '0';
        }

        //查询预约司机
        getDriverList(this.form.carrierId).then(response => {
          this.drivers = response.drivers
        })
        this.open = true;
        this.title = this.$t('lm10');
      });

    },

    /** 打印按钮操作 */
    handlePrint() {
      var currentTime = new Date().toLocaleString();
      // 创建自定义打印模板
      this.printContent = `<h1>${this.$t('kmm2')}</h1>`;
      this.printContent += `<h2>${this.$t('zw1')}</h2>`;
      this.printContent += '<tr><td><br></td></tr>';
      this.printContent += '<tr><td><br></td></tr>';
      this.printContent += `<h3>${this.$t('zw7')}</h3>`;
      this.printContent += '<tr><td><br></td></tr>';
      this.printContent += '<table class="print-table">';
      const requests = this.ids.map(item => getApplication(item));
      Promise.all(requests).then(responses => {
        responses.forEach(response => {
          this.form.serialNumber = response.data.serialNumber;
          this.form.orderId = response.data.orderId;
          this.form.carNumber = response.data.carNumber;
          this.form.driverName = response.data.driverName;
          this.form.appointmentWeight = response.data.appointmentWeight;
          this.form.businessType = response.data.businessType;
          this.form.carrierName = response.data.carrierName;
          this.form.floatReason = response.data.floatReason;
          this.form.loadoMeterID1 = response.data.loadoMeterID1;
          this.form.loadoMeterID2 = response.data.loadoMeterID2;
          this.form.firstWeight = response.data.firstWeight;
          this.form.secondWeight = response.data.secondWeight;
          if (this.form.businessType === 0) {
            this.form.secondWeight = response.data.truckWeight;
          }
          this.form.firstWeightTime = response.data.firstWeightTime;
          this.form.secondWeightTime = response.data.secondWeightTime;
          this.form.goodsWeight = response.data.goodsWeight;
          this.form.netWeight = response.data.netWeight;
          this.form.packWeight = response.data.packWeight;
          this.form.factoryType = response.data.factoryType;
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.serialNumber !== null ? this.form.serialNumber : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw2')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.orderId !== null ? this.form.orderId : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw3')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.carNumber !== null ? this.form.carNumber : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw4')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.driverName !== null ? this.form.driverName : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw5')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.appointmentWeight !== null ? this.form.appointmentWeight : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw9')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.floatReason !== null ? this.form.floatReason : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw8')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.carrierName !== null ? this.form.carrierName : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zw6')}</b></td>`;
          // this.printContent += `<td>${this.form.businessType !== null ? this.form.businessType : ''}</td>`;
          this.printContent += '<td style="white-space: nowrap;">';
          if (this.form.businessType !== null) {
            if (this.form.businessType === 0) {
              this.printContent += '采购';
            } else if (this.form.businessType === 1) {
              this.printContent += '销售';
            } else if (this.form.businessType === 2) {
              this.printContent += '废料';
            } else if (this.form.businessType === 3) {
              this.printContent += '其他';
            } else if (this.form.businessType === 4) {
              this.printContent += '固废危废处置';
            }
          }
          this.printContent += '</td>';
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww1')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.loadoMeterID1 !== null ? this.form.loadoMeterID1 : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww2')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.loadoMeterID2 !== null ? this.form.loadoMeterID2 : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww3')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.firstWeight !== null ? this.form.firstWeight : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww4')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.secondWeight !== null ? this.form.secondWeight : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww5')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.firstWeightTime !== null ? this.form.firstWeightTime : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww6')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.secondWeightTime !== null ? this.form.secondWeightTime : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww7')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.goodsWeight !== null ? this.form.goodsWeight : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww8')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.netWeight !== null ? this.form.netWeight : ''}</td>`;
          this.printContent += '</tr>';
          this.printContent += '<tr><td><br></td></tr>';
          this.printContent += '<tr>';
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww9')}</b></td>`;
          this.printContent += `<td style="white-space: nowrap;">${this.form.packWeight !== null ? this.form.packWeight : ''}</td>`;
          this.printContent += `<td style="text-align: right; white-space: nowrap;"><b>${this.$t('zww10')}</b></td>`;
          this.printContent += '<td style="white-space: nowrap;">';
          if (this.form.factoryType !== null) {
            if (this.form.factoryType === 'KS') {
              this.printContent += this.$t('zww11');
            } else if (this.form.factoryType === 'IN') {
              this.printContent += this.$t('zww12');
            }
          }
          this.printContent += '</td>';
          this.printContent += '</tr>';
        });
        this.printContent += '<tr><td><br></td></tr>';
        this.printContent += '</table>';
        this.printContent += `<h4>${this.$t('zww13')}</h4>`;

        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        document.body.appendChild(iframe);
        const iframeDocument = iframe.contentWindow.document;
        iframeDocument.open();
        iframeDocument.write(`
        <html>
        <head>
          <title>${this.form.checkerName}</title>
          <style>
            h1 {
              color: black;
              font-size: 28px;
              text-align: center;
            }
            h2 {
              color: black;
              font-size: 20px;
              text-align: center;
            }
            h3 {
              color: black;
              font-size: 15px;
              text-align: center;
            }
            h4 {
              color: black;
              font-size: 12px;
              text-align: center;
            }
            table {
              border-collapse: collapse;
              width: 100%;
            }
            th {
              border: 1px solid black;
              padding: 8px;
              text-align: left;
            }
            @media print {
            /* 隐藏页脚 */
            @page {
              size: auto;
              margin: 10;
            }

            body {
              margin: 0;
              padding-bottom: 2cm; /* 调整页脚的高度 */
            }

            footer {
              position: fixed;
              bottom: 0;
              left: 0;
              right: 0;
              height: 2cm; /* 调整页脚的高度 */
              text-align: center;

            }
          }
          </style>
        </head>
        <body>
          ${this.printContent} <!-- 使用组件数据属性 -->
          <footer>${this.$t('kmm1')} : ${currentTime} </footer>
        </body>
        </html>
      `);

        iframeDocument.close();

        // 等待iframe加载完成后进行打印操作
        iframe.onload = () => {
          iframe.contentWindow.print();
          document.body.removeChild(iframe);
        };
      });
    },

    /** 修改状态操作 */
    statusHandleUpdate(row) {
      // this.reset();
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      })
      const applicationId = row.applicationId || this.ids
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.enterStatus = response.data.enterStatus,
          this.form.appointmentStatus = response.data.appointmentStatus,
          this.statusVisible = true,
          this.title = this.$t('ess')
      });
    },

    /** 标记出入厂凭证操作 */
    certificateHandleConfirm(row) {
      const applicationId = row.applicationId || this.ids;
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        // this.form.appointmentStatus = response.data.appointmentStatus;
        this.$modal.confirm(this.$t('bjcrcpz')).then(() => {
          return enterOrOutMark({ applicationId: String(applicationId), mark: 'Yes' });
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess(this.$t('bjcg'));
        }).catch(() => {
        });
      });
    },

    /** 修改扣重按钮操作 */
    loseWeightHandleUpdate(row) {
      // this.reset();
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      })
      const applicationId = row.applicationId || this.ids
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentWeight = response.data.appointmentWeight,
          this.form.loseWeight = response.data.loseWeight,
          this.form.shippedWithPalletWeight = response.data.shippedWithPalletWeight,
          this.form.floatReason = response.data.floatReason,
          this.form.enterStatus = response.data.enterStatus,
          this.form.appointmentStatus = response.data.appointmentStatus,
          this.dialogVisible = true,
          this.title = this.$t('gj')
      });
    },

    /** 修改销售预约重量按钮操作 */
    saleAppointmentWeightHandleUpdate(row) {
      // this.reset();
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      })
      const applicationId = row.applicationId || this.ids
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentWeight = response.data.appointmentWeight !== null ? response.data.appointmentWeight : 0,
          this.form.loseWeight = response.data.loseWeight,
          this.form.businessType = response.data.businessType,
          this.form.shippedWithPalletWeight = response.data.shippedWithPalletWeight,
          this.form.enterStatus = response.data.enterStatus,
          this.form.appointmentStatus = response.data.appointmentStatus,
          this.saleVisible = true,
          this.title = this.$t('zwh')
      });
    },

    /** 修改废料预约重量按钮操作 */
    scrapAppointmentWeightHandleUpdate(row) {
      // this.reset();
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      })
      const applicationId = row.applicationId || this.ids
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentWeight = response.data.appointmentWeight !== null ? response.data.appointmentWeight : 0,
          this.form.loseWeight = response.data.loseWeight,
          this.form.businessType = response.data.businessType,
          this.form.shippedWithPalletWeight = response.data.shippedWithPalletWeight,
          this.form.enterStatus = response.data.enterStatus,
          this.form.appointmentStatus = response.data.appointmentStatus,
          this.scrapVisible = true,
          this.title = this.$t('zwhh')
      });
    },

    /** 修改出厂带栈板重量按钮操作 */
    shippedWithPalletWeightHandleUpdate(row) {
      // this.reset();
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      })
      const applicationId = row.applicationId || this.ids
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentWeight = response.data.appointmentWeight,
          this.form.loseWeight = response.data.loseWeight,
          this.form.shippedWithPalletWeight = response.data.shippedWithPalletWeight,
          this.form.enterStatus = response.data.enterStatus,
          this.form.appointmentStatus = response.data.appointmentStatus,
          this.form.businessType = response.data.businessType,
          this.palletVisible = true,
          this.title = this.$t('xgp')
      });
    },

    /** 取消预约按钮操作 */
    cancelReservationHandleUpdate(row) {
      const applicationId = row.applicationId || this.ids;
      getApplication(applicationId).then(response => {
        this.form.appointmentStatus = response.data.appointmentStatus;
        this.form.enterStatus = response.data.enterStatus;
        if (this.form.appointmentStatus === 0) {
          this.$modal.confirm(this.$t('lmmyy')).then(function () {
            return cancelReservationApplication({ applicationId: String(applicationId), status: 4, enterStatus: 3, appointmentStatus: 7 });
          }).then((res) => {
            this.getList();
            this.$modal.msgSuccess(this.$t('lmmyyy'));
          })
            .catch(() => {
            })
        } else {
          this.$modal.msgWarning(this.$t('lmyy4'));
        }
      });
    },

    /** 重新称重1按钮 */
    reWeight1(row) {
      this.handleReWeight(row, 'reWeight1');
    },
    reWeight2(row) {
      this.handleReWeight(row, 'reWeight2');
    },
      /** 统一的处理方法 */
    handleReWeight(row, type) {
    this.$nextTick(() => {
    this.$refs['form'] && this.$refs['form'].clearValidate();
  });
  
    const applicationId = row.applicationId || this.ids;
    getApplication(applicationId).then(response => {
    this.form.applicationId = applicationId;
    this.form.appointmentStatus = response.data.appointmentStatus;
    this.form.enterStatus = response.data.enterStatus;
    this.form.status = response.data.status;
    this.currentWeightType = type;
    
    // 根据类型设置不同的标题和条件
    if (type === 'reWeight1') {
      this.title = this.$t('reweight1C');
      if (this.form.appointmentStatus === 2 && this.form.enterStatus === 1 && this.form.status === 2) {
        this.reWeightVisible = true;
      } else {
        this.$modal.msgWarning(this.$t('reweight1W'));
      }
    } else if (type === 'reWeight2') {
      this.title = this.$t('reweight2C');
      // reWeight2 可能有不同的条件
      if (this.form.appointmentStatus === 4 && this.form.enterStatus === 1 && this.form.status === 2) {
        this.reWeightVisible = true;
      } else {
        this.$modal.msgWarning(this.$t('reweight2W'));
      }
    }
  });
},
    // /** 重新称重2按钮 */
    // reWeight2(row) {
    //   this.$nextTick(() => {
    //      this.$refs['form'] && this.$refs['form'].clearValidate();
    //   })
    //   const applicationId = row.applicationId || this.ids;
    //   getApplication(applicationId).then(response => {
    //     this.form.appointmentStatus = response.data.appointmentStatus;
    //     this.form.enterStatus = response.data.enterStatus;
    //     this.form.status = response.data.status;
    //     this.form.applicationId = applicationId; // 保存applicationId
    //     this.title = this.$t('reweight2C')
    //     if (this.form.appointmentStatus === 4 && this.form.enterStatus === 1 && this.form.status === 2) {
    //       this.reWeight2 = true; 
    //       // return reWeight1({ 
    //       // applicationId: String(applicationId), 
    //       // appointmentStatus: 3 
    //       // }).then((res) => {
    //       //   this.getList();
    //       //   this.$modal.msgSuccess(this.$t('ww0'));
    //       //   return res; // 保持返回值
    //       // });
    //     }else{
    //       this.$modal.msgWarning(this.$t('reweight2W'));
    //     }
    //   });
    // },
    /** 取消称重按钮操作 */
    cancelWeight(row) {
      const applicationId = row.applicationId || this.ids;
      getApplication(applicationId).then(response => {
        this.form.appointmentStatus = response.data.appointmentStatus;
        if (this.form.appointmentStatus === 3) {// 取消一次磅
          this.$modal.confirm(this.$t('lmm1')).then(function () {
            return weightCancelInfo({ applicationId: String(applicationId), appointmentStatus: 3, requestId: 1 });
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess(this.$t('lmm2'));
          }).catch(() => {
          });
        } else if (this.form.appointmentStatus === 5) {// 取消二次磅
          this.$modal.confirm(this.$t('lmm1')).then(function () {
            return weightCancelInfo({ applicationId: String(applicationId), appointmentStatus: 5, requestId: 2 });
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess(this.$t('lmm22'));
          }).catch(() => {
          });
        } else {
          this.$modal.msgWarning(this.$t('lmm222'));
        }
      });
    },

    /** 作废称重按钮操作 */
    invalidWeight(row) {
      const applicationId = row.applicationId || this.ids;
      getApplication(applicationId).then(response => {// 作废称重
        this.form.appointmentStatus = response.data.appointmentStatus;
        if (this.form.appointmentStatus === 5) {
          this.$modal.confirm(this.$t('lmm3')).then(function () {
            return weightCancelInfo({ applicationId: String(applicationId), appointmentStatus: 5, requestId: 3 });
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess(this.$t('lmm4'));
          }).catch(() => {
          });
        }
        else {
          this.$modal.msgWarning(this.$t('lmm222'));
        }
      });

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

    /** 空车按钮操作 */
    handleoutform(row) {
      const applicationId = row.applicationId || this.ids;
      this.form.applicationId = String(applicationId);
      getApplication(applicationId).then(response => {
        this.form.appointmentStatus = response.data.appointmentStatus;
        this.form.enterStatus = response.data.enterStatus;
        this.form.status = response.data.status;
        this.form.beforeWeight = response.data.beforeWeight;
        // this.form.firstWeight = response.data.firstWeight;
        // this.form.secondWeight = response.data.secondWeight;
        // const diff = Math.abs(this.form.secondWeight - this.form.firstWeight) / this.form.firstWeight;
        // const m = (1 + 0.005) * this.form.firstWeight;
        // const n = (1 - 0.005) * this.form.firstWeight;
        if (this.form.enterStatus === 1 && this.form.status === 2) {
          this.$modal.confirm(this.$t('jjk2') + formatDate(new Date()) + this.$t('lmmm6')).then(() => {
            return enterOrOut({ applicationId: String(applicationId), appointmentWeight: this.form.beforeWeight, downFloatingWeight: 0, upFloatingWeight: 50, editFlag: 'Yes' });
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
            this.$modal.msgSuccess(this.$t('jkk9'));
          }).catch(() => {
          });
        } else {
          this.$modal.msgWarning(this.$t('lmmm5'));
        }
      });
    },
    /** 审核操作 */
    authAction(row) {
      let that = this
      this.remember_row = row
      this.$confirm(this.$t('ddd'), {
        distinguishCancelAndClose: true,
        confirmButtonText: this.$t('jjk'),
        cancelButtonText: this.$t('jjk1'),
        type: 'warning',
        center: true
      })
        .then(() => {
          // 打开第三方提交审核表单dialog
          that.outformopen = true
        })
        .catch((action) => {
          if (action === 'cancel') {
            authenApplication({ applicationId: row.applicationId, status: 3, enterStatus: 3, appointmentStatus: 7 })
              .then((res) => {
                this.getList();
                this.$message({
                  type: 'info',
                  message: this.$t('auo')
                })
              })
              .catch((error) => {
                this.getList();
              })
          }
        })
    },

    /** 审核通过 */
    authenApplicationApi(retundata) {
      authenApplication({
        applicationId: retundata.applicationId,
        status: 2,
        orderId: retundata.orderId || null,
        businessType: retundata.businessType || null,
        appointmentWeight: retundata.appointmentWeight,
        enableControl: retundata.enableControl,
        toleranceAdd: retundata.toleranceAdd,
        toleranceDec: retundata.toleranceDec,
        applicationDate: retundata.applicationDate,
        supplyCode: retundata.supplyCode,
        supplyName: retundata.supplyName,
        custCode: retundata.custCode,
        businessDescription: retundata.businessDescription,
        wMSPickingRouteIDs: retundata.wMSPickingRouteIDs,
        wMSPickingRouteIDs2: retundata.wMSPickingRouteIDs2,
        upFloatingWeight: retundata.upFloatingWeight,
        downFloatingWeight: retundata.downFloatingWeight
      })
        .then((res) => {
          this.getList();
          this.$message({
            type: 'success',
            message: this.$t('lmm')
          })
          this.outformopen = false
        })
        .catch((error) => {
          this.getList();
          this.outformopen = false
        })
    },

    /** 新增和修改提交按钮 */
    submitForm() {
      this.isSubmitDisabled = true;
      this.$refs["form"].validate(valid => {
        if (valid) {
          //校验之后清除校验，否则下面赋值之后会再次校验
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          })
          // console.log("变化之前driverId",this.form.driverId)
          if (this.form.enter_type === '1') {
            //手输
            this.form.driverId = null;
          } else {
            this.form.driverName = this.$refs.driverId.selectedLabel;
          }
          //获取选中的司机、承运商、审核人姓名
          this.form.carrierName = this.$refs.carrierId.selectedLabel;
          this.form.checkerName = this.$refs.checkerId.selectedLabel;
          //原因
          this.form.reason = this.$refs.reasonKey.selectedLabel;
          //废料名称
          if (this.form.reasonKey === '4') {
            this.form.scrap = this.$refs.scrapKey.selectedLabel;
          }

          if (this.form.applicationId != null) {
            updateApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              // this.open = false;
              this.cancel();
              this.getList();
            });
          } else {
            addApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lmm5'));
              // this.open = false;
              this.cancel();
              this.getList();
            });
          }
        }
      });
      // this.isSubmitDisabled = false;
    },
    /** 修改状态按钮 */
    statusSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          });
          if (this.form.applicationId != null) {
            statusApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.statusVisible = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 修改扣重提交按钮 */
    loseWeightSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          });
          if (this.form.applicationId != null && this.form.appointmentStatus === 3) {
            loseWeightApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.dialogVisible = false;
              this.inputValue = '';
              this.getList();
            });
          } else {
            this.$modal.msgWarning(this.$t('zwww14'));
            this.dialogVisible = false; // 关闭对话框窗口
            this.inputValue = '';
            this.getList();
          }
        }
      });
    },
    /** 修改销售预约重量提交按钮 */
    saleAppointmentWeightSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          });
          if (this.form.applicationId != null && this.form.businessType === 1 && this.form.appointmentStatus === 3) {
            saleAppointmentWeightApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.saleVisible = false;
              this.form.editWeight = '';
              this.form.editPerson = '';
              this.form.editReason = '';
              this.getList();
            });
          } else {
            this.$modal.msgWarning(this.$t('yuo'));
            this.saleVisible = false; // 关闭对话框窗口
            this.form.editWeight = '';
            this.form.editPerson = '';
            this.form.editReason = '';
            this.getList();
          }
        }
      });
    },
    /** 修改废料预约重量提交按钮 */
    scrapAppointmentWeightSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          });
          if (this.form.applicationId != null && this.form.businessType === 2 || this.form.businessType === 4 && this.form.appointmentStatus === 3) {
            scrapAppointmentWeightApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.scrapVisible = false;
              this.form.editWeight = '';
              this.form.editPerson = '';
              this.form.editReason = '';
              this.getList();
            });
          } else {
            this.$modal.msgWarning(this.$t('yuoo'));
            this.scrapVisible = false; // 关闭对话框窗口
            this.form.editWeight = '';
            this.form.editPerson = '';
            this.form.editReason = '';
            this.getList();
          }
        }
      });
    },
    /** 修改出厂带栈板重量提交按钮 */
    shippedWithPalletWeightSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$nextTick(() => {
            this.$refs['form'].clearValidate();
          });
          if (this.form.applicationId != null && this.form.businessType === 0 && this.form.appointmentStatus === 3) {
            updateShippedWithPalletWeightApplication(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('lm9'));
              this.palletVisible = false;
              this.form.shippedWithPalletWeight = '';
              this.getList();
            });
          } else {
            this.$modal.msgWarning(this.$t('zww14'));
            this.palletVisible = false; // 关闭对话框窗口
            this.form.shippedWithPalletWeight = '';
            this.getList();
          }
        }
      });
    },

    /** 重新称重提交按钮 */
    reWeightSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const params = {
        applicationId: String(this.form.applicationId),
        editPerson: this.form.editPerson,
        editReason: this.form.editReason
      };
      
      let apiCall;
      let successMessage;
        
        // 根据类型调用不同的API
      if (this.currentWeightType === 'reWeight1') {
        params.appointmentStatus = 1;  // reWeight1 设置为1
        apiCall = reWeight1(params);
        successMessage = this.$t('ww0');
      } else if (this.currentWeightType === 'reWeight2') {
        params.appointmentStatus = 3;  // reWeight2 设置为3
        apiCall = reWeight2(params);
        successMessage = this.$t('ww0');
      }
      
      apiCall.then((res) => {
        this.$modal.msgSuccess(successMessage);
        this.getList();
        this.reWeightVisible = false;
        this.reset();
      }).catch(error => {
        this.$modal.msgError(this.$t('operationFailed'));
        });
      }
    });
  },  
  //     /** 重新称重2提交按钮 */
  //   reWeightSubmitForm2() {
  //     this.$refs["form"].validate(valid => {
  //       if (valid) {
  //         // 表单验证通过，执行重新称重操作
  //         const applicationId = this.form.applicationId;
  //         // 先清除验证
  //         this.$nextTick(() => {
  //         this.$refs['form'].clearValidate();
  //         });
  //         // 执行重新称重操作
  //         reWeight1({ 
  //         applicationId: String(applicationId), 
  //         appointmentStatus: 3,
  //         editPerson: this.form.editPerson,      // 添加操作人
  //         editReason: this.form.editReason       // 添加操作原因
  //       }).then((res) => {
  //       this.$modal.msgSuccess(this.$t('ww0'));
  //       this.getList();
        
  //       // 关闭对话框并重置表单
  //       this.reWeight = false;
  //       this.resetForm();
  //       }).catch(error => {
  //         this.$modal.msgError(this.$t('reweight1W'));
  //       });
  //     }
  //   });
  // },



    /** 删除按钮操作 */
    handleDelete(row) {
      const applicationIds = row.applicationId || this.ids;
      this.$modal.confirm('该操作请谨慎操作，是否确认删除该车辆预约单数据？').then(function () {
        return deleteApplication({ applicationId: applicationIds });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      if (this.ids.length > 0) {
        this.download('system/application/export', {
          'ids': this.ids.join(','), ...this.queryParams
        }, `application_${new Date().getTime()}.xlsx`)
      } else {
        this.download('system/application/export', {
          'ids': this.ids.join(','), ...this.queryParams
        }, `application_${new Date().getTime()}.xlsx`)
      }
    },

    // // 未进厂 已进厂 已出厂 点击事件
    // enterStatuschange(e) {
    //   // console.log(e)
    //   this.handleQuery()
    // },

    // // 业务类型点击事件
    // businessTypechange(e) {
    //   // console.log(e)
    //   this.handleQuery()
    // },

    // 业务类型 变化事件
    factoryTypeChange(e) {
      let that = this
      this.$refs.form.validateField('factoryType')
      if (e === 'KS') {
        this.rules = {
          factoryType: {
            required: true,
            message: "请选择工厂类型",
            trigger: ["change"],
          },
          // carNumber: {
          //   required: true,
          //   pattern: /^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([A-Z]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/,
          //   message: "请输入正确的车牌号",
          //   trigger: ["blur", "change"],
          // },
          // carrierId: {
          //   required: true, message: "司机所属承运商不能为空", trigger: ["blur", "change"]
          // },
          enter_type: {
            required: true, message: "司机录入方式必选", trigger: ["blur", "change"]
          },
          driverId: {
            required: true, message: "司机姓名不能为空", trigger: ["blur", "change"]
          },
          driverName: {
            required: true, message: "司机姓名不能为空", trigger: ["blur", "change"]
          },
          phone: {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: ["blur", "change"]
          },
          idcard: {
            required: true,
            pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "请输入正确的身份证号",
            trigger: ["blur", "change"]
          },
          applicationDate: {
            required: true, message: "入厂时间不能为空", trigger: "blur"
          },
          checkerId: {
            required: true, message: "受访人不能为空", trigger: ["blur", "change"]
          },
          beforeWeight: {
            required: true, message: "送/提货预计重量不能为空", trigger: ["blur", "change"]
          },
          reasonKey: {
            required: true,
            message: "原因不能为空",
            trigger: "blur"
          },
        }
      } else if (e === 'IN') {
        this.rules = {
          factoryType: {
            required: true,
            message: "请选择工厂类型",
            trigger: ["change"],
          },
          // carNumber: {
          //   required: true,
          //   pattern: /^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([A-Z]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/,
          //   message: "请输入正确的车牌号",
          //   trigger: ["blur", "change"],
          // },
          carrierId: {
            required: true, message: "司机所属承运商不能为空", trigger: ["blur", "change"]
          },
          enter_type: {
            required: true, message: "司机录入方式必选", trigger: ["blur", "change"]
          },
          driverId: {
            required: true, message: "司机姓名不能为空", trigger: ["blur", "change"]
          },
          driverName: {
            required: true, message: "司机姓名不能为空", trigger: ["blur", "change"]
          },
          phone: {
            required: true,
            // pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: ["blur", "change"]
          },
          idcard: {
            required: true,
            // pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "请输入正确的身份证号",
            trigger: ["blur", "change"]
          },
          applicationDate: {
            required: true, message: "入厂时间不能为空", trigger: "blur"
          },
          checkerId: {
            required: true, message: "受访人不能为空", trigger: ["blur", "change"]
          },
          beforeWeight: {
            required: true, message: "送/提货预计重量不能为空", trigger: ["blur", "change"]
          },
          reasonKey: {
            required: true,
            message: "原因不能为空",
            trigger: "blur"
          },
        }
      }
    },
  }
};
</script>
