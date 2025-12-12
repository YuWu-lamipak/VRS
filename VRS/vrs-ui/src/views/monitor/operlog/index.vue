<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('xd')" prop="title">
        <el-input v-model="queryParams.title" :placeholder="$t('yd')" clearable size="small" style="width: 235px;"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('mz')" prop="operName">
        <el-input v-model="queryParams.operName" :placeholder="$t('nz')" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('oz')" prop="businessType">
        <el-select v-model="queryParams.businessType" :placeholder="$t('pz')" clearable size="small">
          <el-option v-for="dict in dict.type.sys_oper_type" :key="dict.value" :label="$t(`hh${dict.value}`)"
            :value="dict.value">{{ $t(`hh${dict.value}`) }}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('kq')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('pz')" clearable size="small">
          <el-option v-for="dict in dict.type.sys_common_status" :key="dict.value" :label="$t(`ww${dict.value}`)"
            :value="dict.value">{{ $t(`ww${dict.value}`) }}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('qz')">
        <el-date-picker v-model="dateRange" size="small" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" :start-placeholder="$t('hnn')" :end-placeholder="$t('inn')"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('select') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('q') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['monitor:operlog:remove']">{{ $t('uaa') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean"
          v-hasPermi="['monitor:operlog:remove']">{{ $t('x4') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['monitor:operlog:export']">{{ $t('e') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange"
      :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55px" align="center" />
      <el-table-column :label="$t('s5')" align="center" prop="operId" width="150px" />
      <el-table-column :label="$t('xd')" align="center" prop="title" width="150px" />
      <el-table-column :label="$t('sys_oper_type')" align="center" prop="businessType" width="150px">
        <template slot-scope="scope">
          <div v-if="scope.row.businessType === 1">{{ $t('hh1') }}</div>
          <div v-if="scope.row.businessType === 2">{{ $t('hh2') }}</div>
          <div v-if="scope.row.businessType === 3">{{ $t('hh3') }}</div>
          <div v-if="scope.row.businessType === 4">{{ $t('hh4') }}</div>
          <div v-if="scope.row.businessType === 5">{{ $t('hh5') }}</div>
          <div v-if="scope.row.businessType === 6">{{ $t('hh6') }}</div>
          <div v-if="scope.row.businessType === 7">{{ $t('hh7') }}</div>
          <div v-if="scope.row.businessType === 8">{{ $t('hh8') }}</div>
          <div v-if="scope.row.businessType === 9">{{ $t('hh9') }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('sy')" align="center" prop="requestMethod" width="150px" />
      <el-table-column :label="$t('ty')" align="center" prop="operName" width="100px" :show-overflow-tooltip="true"
        sortable="custom" :sort-orders="['descending', 'ascending']" />
      <el-table-column :label="$t('uy')" align="center" prop="operIp" width="150px" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('vy')" align="center" prop="operLocation" width="140px" />
      <el-table-column :label="$t('wy')" align="center" prop="status" width="150px">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">{{ $t('ww0') }}</span>
          <span v-if="scope.row.status === 1">{{ $t('ww1') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('xy')" align="center" prop="operTime" sortable="custom"
        :sort-orders="['descending', 'ascending']" width="180px">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('lll')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row, scope.index)"
            v-hasPermi="['monitor:operlog:query']">{{ $t('yyq') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 操作日志详细 -->
    <el-dialog :title="$t('g8')" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('h8') + ':'">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
            <el-form-item :label="$t('i8') + ':'">{{ form.operName }} / {{ form.operIp }} / {{ form.operLocation
            }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('j8') + ':'">{{ form.operUrl }}</el-form-item>
            <el-form-item :label="$t('sy') + ':'">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('k8') + ':'">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('l8') + ':'">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('g81') + ':'">{{ form.jsonResult }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('wy') + ':'">
              <div v-if="form.status === 0">{{ $t('a0') }}</div>
              <div v-else-if="form.status === 1">{{ $t('ww1') }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('h81') + ':'">{{ parseTime(form.operTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('i81') + ':'" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">{{ $t('j81') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, delOperlog, cleanOperlog } from "@/api/monitor/operlog";

export default {
  name: "Operlog",
  dicts: ['sys_oper_type', 'sys_common_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: { prop: 'operTime', order: 'descending' },
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询登录日志 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_oper_type, row.businessType);
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
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.operId)
      this.multiple = !selection.length
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids;
      this.$modal.confirm(this.$t('sfsc12') + '"' + operIds + '"?').then(function () {
        return delOperlog(operIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('sccg'));
      }).catch(() => { });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm(this.$t('sfsc13')).then(function () {
        return cleanOperlog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('qkcg'));
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('monitor/operlog/export', {
        ...this.queryParams
      }, `operlog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

