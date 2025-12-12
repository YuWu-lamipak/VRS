import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' //directive
import plugins from './plugins' // plugins
import { download } from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

const i18n = new VueI18n({
  locale: "zh",
  messages: {
    en: {
      language: '中文',
      loginText: 'Login', rempassword: 'Remember the password', theme: 'LamiPak truck reservation system',
      select: 'Query', a: 'Add', b: 'Modify lose weight', c: 'Cancel weighing', d: 'Obsolete weighing', e: 'Export', f: 'Edit',
      g: 'Confirm Entry', h: 'Confirm Exit', i: 'Empty', j: 'Check', k: 'Filter Criteria', l: 'Entry time',
      m: 'Application time', n: 'Truck status', o: 'Search', p: 'Approval status', q: 'Reset', r: 'SN#', mn: 'Applicant',
      s: 'Description', t: 'Factory', u: 'PlateNbr', v: 'CarrierCompany', w: 'Driver', x: 'ReservationNbr', y: 'Business type',
      z: 'Mobile', aa: 'Creator', bb: 'LamiPak Contact', cc: 'Approval Status', dd: 'Truck Status',
      aaa: 'Enter search criteria', bbb: 'Select entry time', ccc: 'Select request time', ddd: 'Please select approval status',
      dddd: 'Planned Entry time', ee: 'Id number', ff: 'Actual Entry time',
      eee: 'Actual exit time', fff: 'Expected net weight (kg)', ggg: 'Remark', ggga: 'SN#',
      eeee: 'Reason', ffff: 'Submission method', gggg: 'Sumit for', ggggg: 'Submit ',
      ss: 'Supplier number', tt: 'Supplier name', ssu: 'Supplier', uu: 'Customer number', vv: 'Customer name', ww: 'Document number', xx: 'Book number', yy: 'Factory code',
      mm: 'Service primary key', nn: 'Lose weight (kg)', nnz: 'Reservation weight (kg)', oo: 'Reason for reservation weight change', pp: 'Weighbridge card number', qq: 'Card type', rr: 'Business card printing time',
      sss: 'Return time', ttt: 'Appointment status', uuu: 'One pound weight (kg)', vvv: 'One pound weighing time', www: 'One weighbridge number', xxx: 'One pound user number', yyy: 'One pound user name',
      ssss: 'Two pound weight (gross kg)', tttt: 'Two pound weight (skin kg)', uuuu: 'Two pound weight (net kg)', vvvv: 'Two pound weight (buckle kg)', wwww: 'Two pound weight (real kg)', xxxx: 'Enable control', yyyy: 'No',
      zz: 'Yes', aaaa: 'Purchase', bbbb: 'Sales', cccc: 'Scrap', ddddd: 'Other', mmm: 'IC card number', nnn: 'Electronic tag',
      gg: 'Positive tolerance (%)', hh: 'Negative tolerance (%)', ii: 'Lifting weight (kg)', jj: 'Floating weight (kg)', kk: 'Two pound weighing time', ll: 'Two weighbridge number',
      ga: 'Two pound user number', hhh: 'Two pound user name', iii: 'Whether to update', jjj: 'New add', kkk: 'Correction,Card number unchanged,Car number changed', lll: 'Controls',
      ab: 'Type of factory', ac: 'Please select a factory type', ad: 'License plate number', ae: 'Please enter the license plate number', af: 'Please choose a general carrier', ag: 'Driver entry mode',
      abb: 'Choose', acc: 'Lose by hand', add: 'Driver’s Name', aee: 'Please enter the Driver’s Name ', aff: 'Please choose', agg: 'Phone number',
      abbb: 'Please enter your phone number', accc: 'ID card', addd: 'Please enter ID card', aeee: 'Entry time', afff: 'Select the factory entry time', aggg: 'Reason',
      abbbb: 'Please choose a reason', acccc: 'Expected delivery/Delivery weight (kg)', adddd: 'Please enter the estimated delivery/pick up weight', aeeee: 'Interviewee', affff: 'Please choose a interviewee', agggg: 'Remark',
      abbbbb: 'If there is an entourage,please indicate it in the remarks', accccc: 'Determinate', addddd: 'cancel',
      nnnn: 'Please enter the lose weight', nnc: 'Reason for lose weight change', nna: 'Please enter the reason for reservation weight change', nnnna: 'Pass the examination',
      sa: 'Account name', ta: 'Please enter your account name', ua: 'User nickname', va: 'Create time', wa: 'Remove', xa: 'Username', ya: 'Please enter your username',
      saa: 'Short name', taa: 'Blacklist', uaa: 'Delete', taaa: 'More', uaaa: 'Update password', uaaaa: 'User password', yaa: 'Please enter the user password',
      sab: 'Unit name', tab: 'Please enter the name of the unit', uab: 'Character', sabb: 'Unit address', saabb: 'Please enter the address of the unit', xaa: 'Contact person', xaaa: 'Please enter contact person',
      zc: 'Email', aac: 'Please enter the email address', bbc: 'Chinese name', gn: 'Please enter your Chinese name', hn: 'Intro', in: 'Please enter a personal profile',
      zcc: 'Account number', aacc: 'Please enter your account number', zccc: 'Enabled state', aaacc: 'User status', hnn: 'Start time', inn: 'End time',
      zca: 'Subscriber number', zcb: 'Username', zcba: 'Assign roles', zcbb: 'Please enter your name', zacbb: 'Already requested', zacba: 'Fail the audit',
      gx: 'Not in the factory', hx: 'Already in the factory', ix: 'Have left factory', ixa: 'User gender', hxa: 'Please enter content', gxa: 'Owned carrier', ixx: 'User type', lll0: 'Not in the factory', lll1: 'Already in the factory',
      lll2: 'Have left factory', lll3: 'Cancellation of entry', gm: 'To make a card', hm: 'Stay a pound', im: 'One pound to be confirmed', jm: 'Stay two pounds', km: 'Two pounds to be confirmed', kma: 'Pending return card', kmb: 'completed',
      ll1: 'Already requested', ll2: 'Pass the examination', ll3: 'Fail the audit', ll4: 'Cancel application',
      l0: 'normal', l1: 'ban', gq: 'Role name', hq: 'Please enter a role name', iq: 'Permission character', jq: 'Please enter permission character', kq: 'Status', lq: 'Role Status', lqa: 'Role number',
      zn: 'Display sequence', aan: 'Assigned user', bbn: 'Role order', s1: 'Menu permissions', t1: 'Unfold/fold', u1: 'Select all/Select none', v1: 'Father-son interaction', w1: 'Task name', x1: 'Please enter a task name', y1: 'Task group name',
      m1: 'Select a task group name', n1: 'Task status', o1: 'Please select task status', p1: 'Logs', q1: 'Task number', a1: 'Stop', a0: 'Normal', DEFAULT: 'Default', SYSTEM: 'System',
      s2: 'Call target string', t2: 'Cron executes expressions', u2: 'Execute once', v2: 'Task detail', w2: 'Scheduling log', x2: 'Task grouping', y2: 'Calling method',
      s3: 'Please enter the call target string', t3: 'Enter cron to execute the expression', u3: 'Generated expression', v3: 'Wrong strategy', w3: 'Immediate execution', x3: 'Waiver of execution', y3: 'Permit', z3: 'Ban', z31: 'Concurrent or not',
      s4: 'Execution state', t4: 'Please select execution status', ww0: 'Successful', ww1: 'Fail', w4: 'Execution time', x4: 'Clear', y4: 'Close',
      s5: 'Log number', t5: 'Log information', u5: 'Details', v5: 'Scheduling log details', w5: 'Log sequence number', x5: 'Exception message', y5: 'Menu name',
      g2: 'Please enter a menu name', h2: 'Menu status', i2: 'Icon', j2: 'Sort', k2: 'Permission identification', l2: 'Component path',
      sq: 'Parent menu', tq: 'Select upper menu', uq: 'Menu type', vq: 'Menu', wq: 'Button', xq: 'Menu icon', yq: 'Click Select icon',
      sw: 'Display sort', tw: 'Outside chain or not', uw: 'Routing address', vw: 'Please enter the routing address', wwq: 'Please enter the component path', xw: 'Please enter a permission identifier', yw: 'Please enter route parameters',
      sn: 'Route parameter', tn: 'Cache or not', un: 'Cache', vn: 'Uncached', wn: 'Display status', xn: 'Catalogue', yn: 'Dictionary name',
      sc: 'Please enter a dictionary name', tc: 'Dictionary type', uc: 'Please enter the dictionary type', vc: 'Dictionary state', wc: 'Refresh cache', xc: 'Dictionary number', yc: 'Dictionary tag',
      sys_user_sex: 'User gender', sys_show_hide: 'Menu status', sys_normal_disable: 'System switch', sys_job_status: 'Task status', sys_job_group: 'Task grouping',
      sys_yes_no: 'Whether the system', sys_notice_type: 'Notification type', sys_notice_status: 'Notification status', sys_oper_type: 'Type of operation',
      sys_common_status: 'System state', enter_status: 'Approach status', application_status: 'Application status', user_type: 'User type', application_reason: 'Reason for appointment',
      sx: 'Please enter the dictionary label', tx: 'Data state', ux: 'Dictionary key value', vx: 'Lexicographic sorting', wx: 'Data tag', xx1: 'Data key value', yx: 'Style attribute', am: 'Echo style',
      default: 'default', primary: 'primary', success: 'success', info: 'info', warning: 'warning', danger: 'danger',
      sb: 'Please enter the data label', tb: 'Please enter a data key value', ub: 'Please enter a style attribute', vb: 'Parameter name', wb: 'Please enter a parameter name', xb: 'Parameter key name', yb: 'Please enter the parameter key name',
      sd: 'System built-in', Y: 'Yes', N: 'No', vd: 'Parameter primary key', wd: 'Parameter key value', xd: 'Log of operation', yd: 'Please enter the log of operation',
      mz: 'Operator', nz: 'Please enter operator', oz: 'Type', pz: 'Operating state', qz: 'Operating time',
      hh1: 'Add', hh2: 'Update', hh3: 'Delete', hh4: 'Accredit', hh5: 'Export', hh6: 'Import', hh7: 'Forced retreat', hh8: 'Generated code', hh9: 'Clear data',
      sy: 'Request mode', ty: 'Operator', uy: 'Operational address', vy: 'Operating location', wy: 'Operating state', xy: 'Date of operation', yyq: 'Details',
      g8: 'Operation log details', h8: 'Operation module', i8: 'Login information', j8: 'Request address', k8: 'Operation method', l8: 'Request parameter',
      g81: 'Return parameter', h81: 'Operating time', i81: 'Exception message', j81: 'Close',
      sv: 'Login address', tv: 'Please enter your login address', uv: 'User name', vvb: 'Please enter the user name', wv: 'Login status', xv: 'Login time', yv: 'Access number', mnv: 'Login location',
      mz1: 'Browser', nz1: 'Operating system', oz1: 'Operational information', pz1: 'Date of entry', jb: 'password', jbb: 'Account number', jbbb: 'Please enter your account number', jbbbb: 'Please enter your password', hb: 'Display search', hbb: 'Hidden search',
      hbbb: 'Refresh', hbbbb: 'Display or hide columns', lb: 'System built-in', lbb: 'History', lbbb: 'Today', mb: 'Personal center', mbb: 'Layout setting', mbbb: 'Log out', mbbbb: 'Layout size', mbbbbb: 'Search', aj: 'Theme style setting', ajj: 'Theme colors', ajjj: 'System layout configuration',
      ajjjj: 'Open TopNav', ajjjjj: 'Open Tags-Views', ajjjjjj: 'Fixed Header', ajjjjjjj: 'Show Logo', ajjjjjjjj: 'Dynamic heading', bj: 'Save', bjj: 'Reset', cj: 'Personal details', cjj: 'UserName', cjjj: 'Phone number', dj: 'Basic information', djj: 'Modify password',
      ej: 'Old password', ejj: 'New password', ejjj: 'Confirm password', fj: 'Please enter the old password', fjj: 'Please enter the new password', fjjj: 'Please Confirm the password', gj: 'Modify Lose Weight', gjj: 'Add Vehicle Reservation', hj: 'Please choose a business type', 
      hjj0: 'Purchase', hjj1: 'Sales', hjj2: 'Scrap', hjj3: 'Other', hjjj: 'Cancel', hjjj0: 'Please choose a supplier name', hjjj1: 'Please entry a description', hjjj2: 'Vehicle entry time', hjjj3: 'Please choose a vehicle entry time', hjjj4: 'Please entry a carrierCompany', hjjj5: 'Please entry a driver',
      jk: 'The driver cannot be empty', jk1: 'The carrierCompany cannot be empty', jk2: 'The license plate number cannot be empty', jk3: 'The vehicle entry time cannot be empty', jk4: 'The reservation weight cannot be empty', zpp0: 'No', zpp1: 'Yes', zl: 'Please entry a positive tolerance', zl1: 'Please entry a Negative tolerance',
      jk44: 'The positive tolerance cannot be empty', jk45: 'The negative tolerance cannot be empty', jk46: 'Please enter a customer number', jkk1: 'Delivery order number', zkk1: 'Delivery order line number', jkk2: 'Delivery order number 2', jkk3: 'Please entry a material requisition process', jkk4: 'Please entry a material requisition process 2',
      jjk: 'Pass', jjk1: 'No pass', jjk2: 'The current time is:', jjk3: ',confirm whether to enter the factory?', jjk4: 'Confirm', jjk5: 'System prompt', jjk6: 'Successful entry', jkk7: ',confirm whether to leave the factory?', jkk8: 'Successful empty', jKk9: 'Successful exit', VRSm: 'The phone number cannot be empty', VRSm1: 'The Id number cannot be empty',
      VRSm2: 'The entry time cannot be empty', VRSm3: 'The expected delivery/delivery weight cannot be empty', VRSm4: 'The reason cannot be empty', VRSm5: 'The VRS Contact cannot be empty', VRSm6: 'The driver entry mode is required', VRSm7: 'The general carrier cannot be empty', VRSm8: 'The reason for lose weight change cannot be empty',
      VRSm9: 'Modify successfully', VRSm10: 'Modify Vehicle Reservation', VRSmm: 'Pass the audit', VRSmm1: 'Whether to cancel weighing?', VRSmm2: 'Cancel one pound successfully', VRSmm22: 'Cancel two pound successfully', VRSmm222: 'Please check the reservation status', VRSmm3: 'Whether to obsolete weighing?', VRSmm4: 'Obsolete weighing successfully', VRSmm5: 'Add successfully', VRSmm6: 'Be logging in...', VRSmmm: 'Confirm', VRSmmm1: 'Are you sure to log out and exit the system?',
      VRSmmm2: 'Hint', VRSmmm3: 'The supplier cannot be empty', VRSmmm4: 'Can not enter the factory,reservation status: To make a card', VRSmmm5: 'Can not leave the factory,reservation status is not: completed', VRSmmm6: ',confirm whether the vehicle is empty or not?', VRSmmm7: 'Can not empty car, please check whether the status is approved and entered the factory',
      kmm: 'Print', kmm1: 'Print Time', kmm2: 'Lamipak (Indonesia) limited company', zw: 'Weight bill number : ', zw1: 'Weighing sheet', zw2: 'ReservationNbr : ', zw3: 'PlateNbr : ', zw4: 'Driver : ', zw5: 'Reservation weight : ', zw6: 'Destination : ', zw7: 'Annotation: All weight units in the system are: kg(kilogram)', zw8: 'Company : ', zw9: 'Cause of overweight : ',
      zww1: 'Pound Platform 1 : ', zww2: 'Pound Platform 2 : ', zww3: 'One pound weight : ', zww4: 'Two pound weight : ', zww5: 'Pound time 1 : ', zww5: 'Pound time 2 : ', zww6: 'Loadage : ', zww7: 'Suttle : ', zww8: 'Deduction and supplementation : ', zww9: 'Deduct weight : ', zww10: 'Type of factory : ', zww11: 'Kunshan Factory', zww12: 'Indonesia Factory', zww13: 'Declaration: All handwritten data is invalid',
      zww14: 'Please check whether the business type is purchasing and the reservation status is the status of two pounds!', llll1: 'Loading-finished goods', llll2: 'Loading-not finished goods', llll3: 'Deliver goods', llll4: 'Scrap transport', llll5: 'Borrow pound', llll6: 'Others', drb: 'Increased value', drbb: 'Reduced value', jhd: 'Please enter the delivery order number', jhd2: 'Please enter the delivery order number 2',
      bz: 'PackagingId', xgjhd: 'Modify sales delivery information', jhddd: 'The delivery order number cannot be blank', xgp: 'Modify the weight of the factory tape pallet', xsxg: 'Modify the sale reservation weight', xsfl: 'Modify the scrap reservation weight', dzb: 'Shipped with pallet weight (kg)', dzbb: 'The weight of the delivery pallet cannot be empty', dzbbb: 'Please enter the shipped with pallet weight', sjcc: 'Actual delivery date', yyhmn: 'The reservation number cannot be empty', gxjhd: 'Update delivery order', sfgxjhd: 'Whether to update the delivery order?', gxjhdcg: 'Update delivery order successful', yqxyy: 'Cancel the reservation', qxyy: 'Cancel the reservation', lmmyy: 'Whether to cancel the reservation?',
      lmmyyy: 'Cancel the reservation successfully', lmyy4: 'Can not cancel the reservation,the reservation status is not: To make a card', zwww14: 'Please check if the reservation status is for two pounds!', qxjc: 'Cancellation of entry', qxsq: 'Cancel application', opp1: 'Original Weight(kg)', opp2: 'Revised sales reservation weight(kg)', op2p2: 'Revised scrap reservation weight(kg)', opp22: 'The modified sales reservation weight cannot be empty', opp23: 'The modified scrap reservation weight cannot be empty', opp222: 'Please enter the sales reservation weight', opp223: 'Please enter the scrap reservation weight', opp3: 'Modifier', opp33: 'The modifier cannot be empty', opp333: 'Please enter the modifier', opp4: 'Reason for modification', opp44: 'The change reason cannot be empty', opp444: 'Please enter the reason for the change',
      zwh: 'Modify the sales reservation weight', zwhh: 'Modify the scrap reservation weight', yuo: 'The service type is not sales or the reservation status is not two pounds, and the reserved weight cannot be changed', yuoo: 'The service type is not scrap or the reservation status is not two pounds, and the reserved weight cannot be changed', es: 'Enter status', as: 'Appointment Status', es0: 'Not in the factory', es1: 'Already in the factory', es2: 'Have left factory', es3: 'Cancellation of entry', ess: 'Modify Status', esss: 'Mark the entry and exit certificate', as0: 'To make a card', as1: 'Stay a pound', as2: 'One pound to be confirmed', as3: 'Stay two pounds', as4: 'Two pounds to be confirmed', as5: 'Pending return card', as6: 'completed', as7: 'Cancel the reservation', auo: 'Fail the audit', tjjs: 'Add a role', xgjs: 'Modify a role', sccg: 'Delete successfully', sfsc1: 'Do you want to delete the role whose ID is',
      tjyh: 'Add a user', xgyh: 'Modify a user', sfsc2: 'Confirm whether to delete the user', sfsc3: 'Confirm whether to delete the driver', tjsj: 'Add a driver', xgsj: 'Modify a driver', tjshr: 'Add a checker', xgshr: 'Modify a checker', sfsc4: 'Confirm whether to delete the checker', tjmw: 'Add a guard', xgmw: 'Modify a guard', sfsc5: 'Confirm whether to delete the guard', tjck: 'Add a warehouse', xgck: 'Modify a warehouse', sfsc6: 'Confirm whether to delete the warehouse', tjcys: 'Add a carrier', xgcys: 'Modify a carrier', sfsc7: 'Confirm whether to delete the carrier', tjrw: 'Add a job', xgrw: 'Modify a job', sfsc8: 'Confirm whether to delete the job', tjcd: 'Add a menu', xgcd: 'Modify a menu', sfsc9: 'Confirm whether to delete the menu',
      tjzd: 'Add a dict', xgzd: 'Modify a dict', sfsc10: 'Confirm whether to delete the dict', tjcs: 'Add a config', xgcs: 'Modify a config', sfsc11: 'Confirm whether to delete the config', qkcg: 'Clear successfully', sfsc12: 'Confirm whether to delete the log', sfsc13: 'Confirm whether to clear the log', joinj: 'Perform this operation with caution and confirm whether to delete data?', bjcrcpz: 'Whether to mark the entry and exit certificate?', bjcg: 'Mark success', crcpz: 'Entry and exit certificate', fl: 'Scrap Name', fll: 'The scrap name cannot be empty', flll: 'Please choose a scrap name', qhcc: 'Switch Size Success', bbbbb: 'StartDate', bbbbbb: 'EndDate'
    },
    zh: {
      language: 'English',
      loginText: '登录', rempassword: '记住密码', theme: '乐美车辆预约系统',
      select: '查询', a: '新增', b: '修改扣重', c: '取消称重', d: '作废称重', e: '导出', f: '编辑',
      g: '确认进厂', h: '确认出厂', i: '空车', j: '审核', k: '查询条件', l: '入厂时间',
      m: '申请时间', n: '进厂状态', o: '搜索', p: '审核状态', q: '重置', r: '流水号', mn: '申请人',
      s: '业务描述', t: '工厂类型', u: '车牌号', v: '承运商', w: '司机姓名', x: '预约号码', y: '业务类型',
      z: '手机号', aa: '创建人', bb: '受访人', cc: '审核状态', dd: '进厂状态',
      aaa: '请输入查询条件', bbb: '选择入厂时间', ccc: '选择申请时间', ddd: '请选择审核状态',
      dddd: '计划入厂时间', ee: '身份证号', ff: '实际入厂时间',
      eee: '实际出厂时间', fff: '送/提货预计重量(kg)', ggg: '备注', ggga: '编号',
      eeee: '原因', ffff: '提交方式', gggg: '代提交', ggggg: '本人提交',
      ss: '供应商编号', tt: '供应商名称', ssu: '供应商', uu: '客户编号', vv: '客户名称', ww: '单据编号', xx: '账套编号', yy: '工厂代码',
      mm: '业务主键', nn: '扣重(kg)', nnz: '预约重量 (kg)', oo: '预约重量变更原因', pp: '地磅卡号', qq: '卡类型', rr: '制卡时间',
      sss: '退卡时间', ttt: '预约状态', uuu: '一磅重量(kg)', vvv: '一磅过磅时间', www: '一地磅编号', xxx: '一磅用户编号', yyy: '一磅用户名',
      ssss: '二磅重量(毛kg)', tttt: '二磅重量(皮kg)', uuuu: '二磅重量(净kg)', vvvv: '二磅重量(扣kg)', wwww: '二磅重量(实kg)', xxxx: '启用管控', yyyy: '否',
      zz: '是', aaaa: '采购', bbbb: '销售', cccc: '废料', ddddd: '其他',ccc0: '采购', ccc1: '销售', ccc2: '废料',ccc4: '固废危废处置', ccc3: '其他', mmm: 'IC 卡号', nnn: '电子标签',
      gg: '正容差(%)', hh: '负容差(%)', ii: '上浮重量(kg)', jj: '下浮重量(kg)', kk: '二磅过磅时间', ll: '二地磅编号',
      ga: '二磅用户编号', hhh: '二磅用户名', iii: '是否更新', jjj: '新增', kkk: '更正，卡号不变，更改车号', lll: '操作',
      ab: '工厂类型', ac: '请选择工厂类型', ad: '车牌号', ae: '请输入车牌号', af: '请选择承运商', ag: '司机录入方式',
      abb: '选择', acc: '手输', add: '司机姓名', aee: '请输入司机姓名', aff: '请选择', agg: '手机号',
      abbb: '请输入手机号', accc: '身份证号', addd: '请输入身份证号', aeee: '入厂时间', afff: '选择入厂时间', aggg: '原因',
      abbbb: '请选择原因', acccc: '送/提货预计重量(kg)', adddd: '请输入送/提货预计重量', aeeee: '受访人', affff: '请选择受访人', agggg: '备注',
      abbbbb: '如果有随行人员，请在备注中写明', accccc: '确 定', addddd: '取 消',
      nnnn: '请输入扣重', nnc: '扣重变更原因', nna: '请输入预约重量变更原因', nnnna: '审核通过',
      sa: '账户名称', ta: '请输入账户名称', ua: '用户昵称', va: '创建时间', wa: '移除', xa: '用户名', ya: '请输入用户名',
      saa: '名称简称', taa: '黑名单', uaa: '删除', taaa: '更多', uaaa: '重置密码', uaaaa: '用户密码', yaa: '请输入用户密码',
      sab: '单位名称', tab: '请输入单位名称', uab: '角色', sabb: '单位地址', saabb: '请输入单位地址', xaa: '联系人', xaaa: '请输入联系人',
      zc: '邮箱', aac: '请输入邮箱', bbc: '中文名', gn: '请输入中文名', hn: '简介', in: '请输入个人简介',
      zcc: '账号', aacc: '请输入账号', zccc: '启用状态', aaacc: '用户状态', hnn: '开始时间', inn: '结束时间',
      zca: '用户编号', zcb: '姓名', zcba: '分配角色', zcbb: '请输入姓名', zacbb: '已申请', zacba: '审核不通过',
      gx: '未进厂', hx: '已进厂', ix: '已出厂', ixa: '用户性别', hxa: '请输入内容', gxa: '所属承运商', ixx: '用户类型', lll0: '未进厂', lll1: '已进厂',
      lll2: '已出厂', lll3:'取消进厂', gm: '待制卡', hm: '待上一磅', im: '待确认一磅', jm: '待上二磅', km: '待确认二磅', kma: '待退卡', kmb: '已完成', ll1: '已申请', ll2: '审核通过', ll3: '审核不通过', ll4: '取消申请',
      l0: '正常', l1: '停用', gq: '角色名称', hq: '请输入角色名称', iq: '权限字符', jq: '请输入权限字符', kq: '状态', lq: '角色状态', lqa: '角色编号',
      zn: '显示顺序', aan: '分配用户', bbn: '角色顺序', s1: '菜单权限', t1: '展开/折叠', u1: '全选/全不选', v1: '父子联动', w1: '任务名称', x1: '请输入任务名称', y1: '任务组名',
      m1: '请选择任务组名', n1: '任务状态', o1: '请选择任务状态', p1: '日志', q1: '任务编号', a1: '暂停', a0: '正常', DEFAULT: '默认', SYSTEM: '系统',
      s2: '调用目标字符串', t2: 'cron执行表达式', u2: '执行一次', v2: '任务详细', w2: '调度日志', x2: '任务分组', y2: '调用方法',
      s3: '请输入调用目标字符串', t3: '请输入cron执行表达式', u3: '生成表达式', v3: '错误策略', w3: '立即执行', x3: '放弃执行', y3: '允许', z3: '禁止', z31: '是否并发',
      s4: '执行状态', t4: '请选择执行状态', ww0: '成功', ww1: '失败', w4: '执行时间', x4: '清空', y4: '关闭',
      s5: '日志编号', t5: '日志信息', u5: '详细', v5: '调度日志详细', w5: '日志序号', x5: '异常信息', y5: '菜单名称',
      g2: '请输入菜单名称', h2: '菜单状态', i2: '图标', j2: '排序', k2: '权限标识', l2: '组件路径',
      sq: '上级菜单', tq: '选择上级菜单', uq: '菜单类型', vq: '菜单', wq: '按钮', xq: '菜单图标', yq: '点击选择图标',
      sw: '显示排序', tw: '是否外链', uw: '路由地址', vw: '请输入路由地址', wwq: '请输入组件路径', xw: '请输入权限标识', yw: '请输入路由参数',
      sn: '路由参数', tn: '是否缓存', un: '缓存', vn: '不缓存', wn: '显示状态', xn: '目录', yn: '字典名称',
      sc: '请输入字典名称', tc: '字典类型', uc: '请输入字典类型', vc: '字典状态', wc: '刷新缓存', xc: '字典编号', yc: '字典标签',
      sys_user_sex: '用户性别', sys_show_hide: '菜单状态', sys_normal_disable: '系统开关', sys_job_status: '任务状态', sys_job_group: '任务分组',
      sys_yes_no: '系统是否', sys_notice_type: '通知类型', sys_notice_status: '通知状态', sys_oper_type: '操作类型',
      sys_common_status: '系统状态', enter_status: '进场状态', application_status: '申请状态', user_type: '用户类型', application_reason: '预约原因',
      sx: '请输入字典标签', tx: '数据状态', ux: '字典键值', vx: '字典排序', wx: '数据标签', xx1: '数据键值', yx: '样式属性', am: '回显样式',
      default: '默认', primary: '主要', success: '成功', info: '信息', warning: '警告', danger: '危险',
      sb: '请输入数据标签', tb: '请输入数据键值', ub: '请输入样式属性', vb: '参数名称', wb: '请输入参数名称', xb: '参数键名', yb: '请输入参数键名',
      sd: '系统内置', Y: '是', N: '否', vd: '参数主键', wd: '参数键值', xd: '日志操作', yd: '请输入日志操作',
      mz: '操作人员', nz: '请输入操作人员', oz: '类型', pz: '操作状态', qz: '操作时间',
      hh1: '新增', hh2: '修改', hh3: '删除', hh4: '授权', hh5: '导出', hh6: '导入', hh7: '强退', hh8: '生成代码', hh9: '清空数据',
      sy: '请求方式', ty: '操作人员', uy: '操作地址', vy: '操作地点', wy: '操作状态', xy: '操作日期', yyq: '详细',
      g8: '操作日志详细', h8: '操作模块', i8: '登录信息', j8: '请求地址', k8: '操作方法', l8: '请求参数',
      g81: '返回参数', h81: '操作时间', i81: '异常信息', j81: '关 闭',
      sv: '登录地址', tv: '请输入登录地址', uv: '用户名称', vvb: '请输入用户名称', wv: '登录状态', xv: '登录时间', yv: '访问编号', mnv: '登录地点',
      mz1: '浏览器', nz1: '操作系统', oz1: '操作信息', pz1: '登录日期', jb: '密码', jbb: '账号', jbbb: '请输入您的账号', jbbbb: '请输入您的密码', hb: '隐藏搜索', hbb: '显示搜索',
      hbbb: '刷新', hbbbb: '显隐列', lb: '系统内置', lbb: '历史', lbbb: '今日', mb: '个人中心', mbb: '布局设置', mbbb: '退出登录', mbbbb: '布局大小', mbbbbb: '搜索', aj: '主题风格设置', ajj: '主题颜色', ajjj: '系统布局配置',
      ajjjj: '开启 TopNav', ajjjjj: '开启 Tags-Views', ajjjjjj: '固定 Header', ajjjjjjj: '显示 Logo', ajjjjjjjj: '动态标题', bj: '保存', bjj: '重置', cj: '个人信息', cjj: '用户名称', cjjj: '手机号码', dj: '基本资料', djj: '修改密码', ej: '旧密码', ejj: '新密码', ejjj: '确认密码',
      fj: '请输入旧密码', fjj: '请输入新密码', fjjj: '请确认密码', gj: '修改扣重', gjj: '添加车辆预约', hj: '请选择业务类型', hjj0: '采购', hjj1: '销售', hjj2: '废料',hjj4: '固废危废处置', hjj3: '其他', hjjj: '取消', hjjj0: '请选择供应商', hjjj1: '请输入业务描述', hjjj2: '车辆进厂时间', hjjj3: '请选择车辆进厂时间',
      hjjj4: '请输入承运商', hjjj5: '请输入司机姓名', jk: '司机姓名不能为空', jk1: '承运商不能为空', jk2: '车牌号不能为空', jk3: '车辆进厂时间不能为空', jk4: '预约重量不能为空', zpp0: '否', zpp1: '是', zl: '请输入正容差', zl1: '请输入负容差', jk44: '正容差不能为空', jk45: '负容差不能为空', jk46: '请输入客户编号',
      jkk1: '交货单号', zkk1: '交货单行号', jkk2: '交货单号2', jkk3: '请输入领料流程', jkk4: '请输入领料流程2', jjk: '通过', jjk1: '不通过', jjk2: '当前时间为:', jjk3: ',是否确认进厂?', jjk4: '确认', jjk5: '系统提示', jjk6: '进厂成功', jkk7: ',是否确认出厂?', jkk8: '空车成功', jkk9: '出厂成功', lm: '手机号不能为空', lm1: '身份证号不能为空', lm2: '入厂时间不能为空', 
      lm3: '送/提货预计重量不能为空', lm4: '原因不能为空', lm5: '受访人不能为空', lm6: '司机录入方式为必选', lm7: '承运商不能为空', lm8: '扣重变更原因不能为空', lm9: '修改成功', lm10: '修改车辆预约', lmm: '审核通过', lmm1: '是否取消称重?', lmm2: '取消一次磅成功', lmm22: '取消二次磅成功', lmm222: '请检查预约状态', lmm3: '是否作废称重?', lmm4: '作废称重成功', lmm5: '新增成功', lmm6: '登录中...',
      lmmm: '确定', lmmm1: '确定注销并退出系统吗?', lmmm2: '提示', lmmm3: '供应商不能为空', lmmm4: '无法进厂,预约状态为:待制卡', lmmm5: '无法出厂,预约状态不为:已完成', lmmm6: ',是否空车?', lmmm7: '无法空车,请检查状态是否为审核通过和已进厂', kmm: '打印', kmm1: '打印时间', kmm2: '乐美包装 (昆山) 有限公司',
      zw: '磅单号 : ', zw1: '称量磅单', zw2: '预约号 : ', zw3: '车号 : ', zw4: '司机 : ', zw5: '预约重量 : ', zw6: '去向 : ', zw7: '注: 系统中所有重量单位为: kg(千克)', zw8: '公司 : ', zw9: '超重原因 : ', zww1: '磅台1 : ', zww2: '磅台2 : ', zww3: '一次磅重 : ', zww4: '二次磅重 : ', zww5: '司磅时间1 : ', zww6: '司磅时间2 : ', zww7: '货重 : ', zww8: '净重 : ', zww9: '扣重 : ',
      zww10: '工厂类型 : ', zww11: '昆山工厂', zww12: '印尼工厂', zww13: '声明: 所有手写数据无效', zww14: '请检查业务类型是否为采购或者预约状态是否为待上二磅！', llll1: '装货-成品', llll2: '装货-非成品', llll3: '送货', llll4: '运废料', llll5: '借磅', llll6: '其他', drb: '增加的值', drbb: '减少的值', jhd: '请输入交货单号', jhd2: '请输入交货单号2',
      bz: '包装ID', xgjhd: '修改销售交货信息', jhddd: '交货单号不能为空', xgp: '修改出厂带栈板重量', xsxg: '修改销售预约重量', xsfl: '修改废料预约重量', dzb: '出厂带栈板重量(kg)', dzbb: '出厂带栈板重量不能为空', dzbbb: '请输入出厂带栈板重量', sjcc: '实际发货日期', yyhmn: '预约号码不能为空', gxjhd: '更新交货单', sfgxjhd: '是否更新交货单?', gxjhdcg: '更新交货单成功', yqxyy: '取消预约', qxyy: '取消预约', lmmyy: '是否取消预约?',reWeight1:'重新上1磅',reWeight2:'重新上2磅',
      lmmyyy: '取消预约成功', lmyy4: '无法取消预约,预约状态不为:待制卡', reweight1W: '预约状态不为:待确认一磅！或其他状态不对！',reweight2W: '预约状态不为:待确认二磅！或其他状态不对！',zwww14: '请检查预约状态是否为待上二磅！', qxjc: '取消进厂', qxsq: '取消申请', opp1: '原重(kg)', opp2: '修改后的销售预约重量(kg)', op2p2: '修改后的废料预约重量(kg)', opp22: '修改后的销售预约重量不能为空', opp23: '修改后的废料预约重量不能为空', opp222: '请输入销售预约重量', opp223: '请输入废料预约重量', opp3: '修改人', opp33: '修改人不能为空', opp333: '请输入修改人', opp4: '修改原因', opp44: '修改原因不能为空', opp444: '请输入修改原因',reweight1C:'确认重新上一磅？',reweight2C:'确认重新上二磅？',
      zwh: '修改销售预约重量', zwhh: '修改废料预约重量', yuo: '业务类型不为销售或者预约状态不为待上二磅,不允许修改预约重量', yuoo: '业务类型不为废料或者预约状态不为待上二磅,不允许修改预约重量', es: '进厂状态', as: '预约状态', es0: '未进厂', es1: '已进厂', es2: '已出厂', es3: '取消进厂', ess: '修改状态', esss: '标记出入厂凭证', as0: '待制卡', as1: '待上一磅', as2: '待确认一磅', as3: '待上二磅', as4: '待确认二磅', as5: '待退卡', as6: '已完成', as7: '取消预约', auo: '审核不通过', tjjs: '添加角色', xgjs: '修改角色', sccg: '删除成功', sfsc1: '是否确认删除角色编号为',
      tjyh: '添加用户', xgyh: '修改用户', sfsc2: '是否确认删除用户', sfsc3: '是否确认删除司机', tjsj: '添加司机', xgsj: '修改司机', tjshr: '添加审核人', xgshr: '修改审核人', sfsc4: '是否确认删除审核人', tjmw: '添加门卫', xgmw: '修改门卫', sfsc5: '是否确认删除门卫', tjck: '添加仓库', xgck: '修改仓库', sfsc6: '是否确认删除仓库', tjcys: '添加承运商', xgcys: '修改承运商', sfsc7: '是否确认删除承运商', tjrw: '添加定时任务', xgrw: '修改定时任务', sfsc8: '是否确认删除定时任务', tjcd: '添加菜单', xgcd: '修改菜单', sfsc9: '是否确认删除菜单',
      tjzd: '添加字典类型', xgzd: '修改字典类型', sfsc10: '是否确认删除字典', tjcs: '添加参数', xgcs: '修改参数', sfsc11: '是否确认删除参数', qkcg: '清空成功', sfsc12: '是否确认删除日志', sfsc13: '是否确认清空日志', joinj: '该操作请谨慎操作，是否确认删除数据?', bjcrcpz: '是否标记出入厂凭证?', bjcg: '标记成功', crcpz: '出入厂凭证', fl: '废料名称', fll: '废料名称不能为空', flll: '请选择废料名称', qhcc: '切换尺寸成功', bbbbb: '开始时间', bbbbbb: '结束时间'
    }
  }
});
export {i18n}



// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)

Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
DictData.install()


/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */



Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

const vue = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')

export default vue
