<template>
    <view>
        <headerbar :title="title" background="#2195F3" title_color="#FFFFFF"/>
        <view class="page"> 
            <u-form :model="form" :borderBottom="true" :rules="formRules" errorType="message" ref="uForm">
                <u-form-item labelWidth="0" prop="BusinessType" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">业务类型</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.BusinessType" mode="select" :columns="BusinessTypelist"
                                placeholder="请选择"  @sure="BusinessTypeget"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="SupplyCode" borderBottom v-if="form.BusinessType === '0'">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">供应商</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <!--<formelement :maxlength="20" v-model="form.SupplyCode"/>-->
                                <formselect_search v-model="form.SupplyCode" mode="select" @popchange="hidearea"
                                placeholder="请选择" apikey="supplier" @sure="getSupplier" />
                            </view>
                        </view>
                    </view>
				</u-form-item>   
                <u-form-item labelWidth="0" prop="CustCode" borderBottom v-if="form.BusinessType === '1'">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">客户编号</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="20" v-model="form.CustCode"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="FactoryCode" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">工厂代码</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="10" v-model="form.FactoryCode"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="BusinessDescription" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">业务描述</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.BusinessDescription" :maxlength="60" mode="textarea" @inputchange="$refs.uForm.validateField('BusinessDescription')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="CarStarDate" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">车辆进厂时间</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formdate v-model="form.CarStarDate" :minDate="minDate" mode="date" @sure="$refs.uForm.validateField('CarStarDate')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="TruckNo" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">车牌号</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="10" v-model="form.TruckNo"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="CarrierCode" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">承运商代码</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="20" v-model="form.CarrierCode"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="CarrierName" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">承运商</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="60" v-model="form.CarrierName"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="Driver" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">司机姓名</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="10" v-model="form.Driver"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="TackID" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">业务主键</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.TackID" :maxlength="60" mode="textarea" @inputchange="$refs.uForm.validateField('TackID')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
              <!--<u-form-item labelWidth="0" prop="WMSPickingRouteIDs" borderBottom v-if="form.BusinessType === '1'">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">领料流程</view><text v-if="form.BusinessType === '1' && false" class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.WMSPickingRouteIDs" :maxlength="60" mode="textarea" @inputchange="$refs.uForm.validateField('WMSPickingRouteIDs')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="WMSPickingRouteIDs2" borderBottom v-if="form.BusinessType === '1'">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">领料流程2</view><text v-if="form.BusinessType === '1' && false" class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.WMSPickingRouteIDs2" :maxlength="60" mode="textarea" @inputchange="$refs.uForm.validateField('WMSPickingRouteIDs2')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>-->
                <u-form-item labelWidth="0" prop="Float" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">扣重</view><text v-if="form.BusinessType === '1'" class="isneed">*</text>
                            <view class="leftauto maxwidth400 flex_only">
                                <formelement v-model="form.Float" type="number" :maxlength="8"/>
                                <view style="line-height: 26px; margin-left: 6rpx;">kg</view>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="AppointmentWeight" borderBottom v-if="form.BusinessType === '0'||form.BusinessType === '3'">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">预约重量(kg)</view><text v-if="form.BusinessType === '0'
                            ||form.BusinessType === '2'||form.BusinessType === '3'" class="isneed">*</text>
                            <view class="leftauto maxwidth400 flex_only">
                                <formelement v-model="form.AppointmentWeight" type="number" :maxlength="8"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="FloatReason" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">预约重量变更原因</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.FloatReason" :maxlength="60" mode="textarea" @inputchange="$refs.uForm.validateField('FloatReason')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="ShipName" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">船名</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="60" v-model="form.ShipName"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="ShipNo" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">船次</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="20" v-model="form.ShipNo"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="EnableControl" borderBottom v-if="form.BusinessType === '0'">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">启用管控</view><text class="isneed" v-if="form.BusinessType === '0'">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.EnableControl" mode="select" :columns="EnableControlList"
                                placeholder="请选择"  @sure="$refs.uForm.validateField('EnableControl')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="TolerancePercentageAdd" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">正容错比例</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.TolerancePercentageAdd" type="number"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="TolerancePercentageDec" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">负容错比例</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.TolerancePercentageDec" type="number"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="DocumentCode" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">单据编号</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="60" v-model="form.DocumentCode"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="PackingSlipId" borderBottom v-if="false">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">产品收据</view>
                            <view class="leftauto maxwidth400">
                                <formelement :maxlength="20" v-model="form.PackingSlipId"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
			</u-form>
            <!--<view class="butframe flex_center">
                <view class="but flex_center" @click="submit">
                    <view class="buttext">预约审核</view>
                </view>
            </view>-->
            <view class="butframe flex_center">
                <view class="but flex_center" @click="useauthenapi">
                    <view class="buttext">提交</view>
                </view>
            </view>
        </view>
        <u-toast ref="uToast"></u-toast>
    </view>
</template>

<script>
import { CreateTruckAppointmentList } from '@/api/outapi.js'
import { historydetail } from '@/api/ordercar.js'
import { authen } from '@/api/ordercar.js'
import { getSupplierList } from '@/api/ordercar.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 页面头文字
            title: '预约审核',
            // 用户信息
            userinfo: {},
            // 详情信息
            detailInfo: {},
            //用户登录表单
            form: {
                BusinessType: null,
                SupplyCode: null,
                CustCode: null,
                FactoryCode: null,
                BusinessDescription: null,
                CarStarDate: null,
                TruckNo: null,
                Driver: null,
                TackID: null,
                WMSPickingRouteIDs: null,
                WMSPickingRouteIDs2: null,
                Float: null,
                AppointmentWeight: null,
                FloatReason: null,
                ShipName: null,
                ShipNo: null,
                CarrierCode: null,
                CarrierName: null,
                EnableControl: null,
                TolerancePercentageAdd: null,
                TolerancePercentageDec: null,
                DocumentCode: null,
                PackingSlipId: null,
                //供应商键值对
                supplier: null,
            },
            formRules: {
                BusinessType: {
                    required: true,
                    message: "请选择业务类型",
                    type: 'number',
                    trigger: ["change"],
                },
                SupplyCode: {
                    required: false,
                    message: "请选择供应商",
                    trigger: ["blur","change"],
                },  
                CustCode: {
                    required: false,
                    message: "请输入客户编号",
                    trigger: ["blur", "change"],
                }, 
                FactoryCode: {
                    required: false,
                    message: "请输入工厂代码",
                    trigger: ["blur", "change"],
                },  
                BusinessDescription: {
                    required: false,
                    message: "请输入业务描述",
                    trigger: ["blur", "change"],
                }, 
                CarStarDate: {
                    required: true,
                    message: "请选择车辆进厂日期",
                    trigger: ["blur", "change"],
                },  
                TruckNo: {
                    required: true,
                    message: "请输入车辆号",
                    trigger: ["blur", "change"],
                }, 
                Driver: {
                    required: true,
                    message: "请输入司机名称",
                    trigger: ["blur", "change"],
                },  
                WMSPickingRouteIDs: {
                    required: false,
                    message: "请输入领料流程",
                    trigger: ["blur", "change"],
                }, 
                WMSPickingRouteIDs2: {
                    required: false,
                    message: "请输入领料流程2",
                    trigger: ["blur", "change"],
                }, 
                ShipName: {
                    required: false,
                    message: "请输入船名",
                    trigger: ["blur", "change"],
                },  
                ShipNo: {
                    required: false,
                    message: "请输入船次",
                    trigger: ["blur", "change"],
                }, 
                CarrierCode: {
                    required: false,
                    message: "请输入承运商代码",
                    trigger: ["blur", "change"],
                },  
                CarrierName: {
                    required: true,
                    message: "请输入承运商名称",
                    trigger: ["blur", "change"],
                }, 
                EnableControl: {
                    required: false,
                    type: 'number',
                    message: "请选择是否启用管控",
                    trigger: ["blur", "change"],
                },  
                TolerancePercentageAdd: {
                    required: false,
                    message: "请输入正容错比例",
                    trigger: ["blur", "change"],
                },  
                TolerancePercentageDec: {
                    required: false,
                    message: "请输入负容错比例",
                    trigger: ["blur", "change"],
                }, 
                DocumentCode: {
                    required: false,
                    message: "请输入单据编号",
                    trigger: ["blur", "change"],
                }, 
                PackingSlipId: {
                    required: false,
                    message: "请输入产品收据",
                    trigger: ["blur", "change"],
                }, 
            },
            // 业务类型数组
            BusinessTypelist: [[
                {
                    nikName: '采购',
                    userId: '0'
                },
                {
                    nikName: '销售',
                    userId: '1'
                }, {
                  nikName: "废料",
                  userId: "2"
                }, {
                  nikName: "其他",
                  userId: "3"
                }
            ]],
            // 启用管控数组
            EnableControlList: [[
                {
                    nikName: '否',
                    userId: 0
                },
                {
                    nikName: '是',
                    userId: 1
                }
            ]],  
			// 时间选择器最小时间
			minDate: null,
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        this.applicationId = e.applicationId
        this.gethistorydetail()
        this.minDate = new Date().getTime()
        // #ifndef H5
        console.log('非H5执行代码')
        this.$refs.uForm.setRules(this.formRules);
        // #endif
    },
    methods: {
        // 提交表单
        submit() {
            // console.log(this.form)
            let that = this
            this.$refs.uForm.validate().then((res) => {
                // console.log(res)
                if (that.form.Float !== null && that.form.Float !== undefined) {
                    that.form.Float = Number(that.form.Float)
                }
                if (that.form.AppointmentWeight !== null && that.form.AppointmentWeight !== undefined) {
                    that.form.AppointmentWeight = Number(that.form.AppointmentWeight)
                }
                if (that.form.TolerancePercentageAdd !== null && that.form.TolerancePercentageAdd !== undefined) {
                    that.form.TolerancePercentageAdd = Number(that.form.TolerancePercentageAdd)
                }
                if (that.form.TolerancePercentageDec !== null && that.form.TolerancePercentageDec !== undefined) {
                    that.form.TolerancePercentageDec = Number(that.form.TolerancePercentageDec)
                }
                // 类型为0  SupplyCode赋值
                // 类型为1  customcode赋值
                // 类型为2,3  两个参数传空
                if (that.form.BusinessType === '0') {
                    console.log('BusinessType===0')
                    that.form.CustCode = null
                    that.form.WMSPickingRouteIDs = null
                    that.form.WMSPickingRouteIDs2 = null
                } else if (that.form.BusinessType === '1') {
                    console.log('BusinessType===1')
                    that.form.SupplyCode = null
                    that.form.EnableControl = null
                } else if (that.form.BusinessType === '2' || that.form.BusinessType === '3') {
                    // console.log('BusinessType===2')
                    that.form.SupplyCode = null
                    that.form.CustCode = null
                    that.form.EnableControl = null
                    that.form.WMSPickingRouteIDs = null
                    that.form.WMSPickingRouteIDs2 = null
                }
                // console.log(that.form)
                // console.log(that.form)
                if (res) {
                    that.CreateTruckAppointmentListapi()
                }
            }).catch((errors) => {

            });
            // this.blackmodal = true
        },
        // 测试使用 直接通过
        useauthenapi() {
            uni.showLoading({
                title: "提交审核中...",
                mask: true
            });
            this.authenapi()
        },
        // 调用外部新增订单接口
        CreateTruckAppointmentListapi() {
            let that = this
            // this.authenapi()
            // return
            // console.log(this.form)
            this.form.BusinessType = this.form.BusinessType.toString()
            // if (this.form.BusinessType !== '0') {
            //  this.form.AppointmentWeight = null
            //}
            let params = {
                name: "CreateTruckAppointmentList",
                json: JSON.stringify(this.form),
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
            uni.showLoading({
                title: "提交审核中...",
                mask: true
            });
            wx.cloud.init()
            wx.cloud.callFunction({
                name: 'epr',
                data: params,
            }).then(cloudres => {
                console.log(cloudres)
                let res = cloudres && cloudres.result
                if (typeof(res) !== 'object') {
                    res = JSON.parse(res)
                }
                console.log(res)
                if (res) {
                    let returnres = res || {}
                    if (returnres.Code === '0') {
                        // return
                        that.authenapi(returnres.Result && returnres.Result.IWS_AppointmentID || '')
                    } else {
                        uni.hideLoading()
                        this.$refs.uToast.show({
                            type: "error",
                            message: res.Message || '审核失败',
                        });
                    }
                } else {
                    uni.hideLoading()
                }
            }).catch((errors) => {
                uni.hideLoading()
                console.log(errors)
                uni.showToast({
                    title: errors,
                    icon: 'none'
                })
            });
            // CreateTruckAppointmentList(params).then((res) => {
            //     console.log(res)
            //     if (res) {
            //         let returnres = res.data || {}
            //         if (returnres.Code === '0') {
            //             // return
            //             that.authenapi(returnres.Result && returnres.Result.IWS_AppointmentID || '')
            //         } else {
            //             uni.hideLoading()
            //             this.$refs.uToast.show({
            //                 type: "error",
            //                 message: returnres.Message || '审核失败',
            //             });
            //         }
            //     } else {
            //         uni.hideLoading()
            //     }
            // }).catch((errors) => {
            //     uni.hideLoading()
            //     console.log(errors)
            //     uni.showToast({
            //         title: errors,
            //         icon: 'none'
            //     })
            // });
        },
        // 审核 1已申请2审核通过3审核不通过
        authenapi(orderId) {
            let that = this
            let status = 2
            let params = {
                applicationId: this.applicationId,
                status,
                orderId: "LMKS" + "-" + that.getuuid2() || null,
                businessType: parseInt(this.form.BusinessType),
                supplyCode: this.form.supplyCode,
                supplyName: this.form.supplyName,
                businessDescription: this.form.BusinessDescription,
                enableControl: this.form.EnableControl,
                appointmentWeight: this.form.AppointmentWeight,
                custCode: this.form.CustCode,
            }
            // that.detailInfo.status = status
            // return
            // uni.showLoading({
            //     title: "提交审核中...",
            //     mask: true
            // });
            authen(params).then(res => {
                uni.hideLoading()
                let dataRes = res.data || {}
                if (dataRes.code === 200) {
                    uni.setStorageSync("needToRefreshhistorylist", true)
                    // 对数组操作 修改操作内容
                    let pages = getCurrentPages() // 获取栈实例
                    if (pages && pages.length > 2) {
                        // 如果是从列表页过来 修改列表页数组
                        let page = pages[pages.length - 3]
                        if (page.$vm.list && page.$vm.list.length > 1) {
                            let deleteindex = -1;
                            for (let index = 0; index < page.$vm.list.length; index++ ) {
                                if (page.$vm.list[index].applicationId === that.applicationId){
                                    page.$vm.list[index].status = status
                                    break
                                }
                            }
                        }
                        this.$refs.uToast.show({
                            type: "success",
                            message: "提交成功",
                            complete() {
                                // 如果是从历史来到详情到审核则返回上上个页面
                                // uni.navigateBack({  //uni.navigateTo跳转的返回，默认1为返回上一级
                                //     delta: 2
                                // });
                                // 直接返回上一页
                                uni.redirectTo({
                                    url: '/pages/ordercar/orderauthenresult?delta=2&orderId=' + ( orderId || '' )
                                })
                            },
                        });
                    } else {
                        this.$refs.uToast.show({
                            type: "success",
                            message: "提交成功",
                            complete() {
                                // 如果是直接从详情到审核 返回上个页面
                                // uni.navigateBack({  //uni.navigateTo跳转的返回，默认1为返回上一级
                                //     delta: 1
                                // });
                                uni.redirectTo({
                                    url: '/pages/ordercar/orderauthenresult?delta=1&orderId=' + ( orderId || '' )
                                })
                            },
                        });
                    }
                } else {
                    this.$refs.uToast.show({
                        type: "error",
                        message: dataRes.msg || '提交审核失败',
                    });
                }
            })
        },
        //供应商列表
        getSupplier: function getSupplier(e) {
          this.supplier = e;
          this.form.supplyCode = this.supplier.code;
          this.form.supplyName = this.supplier.nikName;
          this.$refs.uForm.validateField('SupplyCode');
        },
        // 预约详情
        gethistorydetail() {
            let that = this
            let params = {
                applicationId: this.applicationId
            }
            historydetail(params).then(res => {
                let dataRes = res.data || {}
                let historydata = dataRes.data || {}
                if (dataRes.code === 200) {
                    that.detailInfo = historydata || {}
                    if (that.detailInfo.applicationDate) {
                        that.detailInfo.applicationDate = that.utils.timeFormat(new Date(that.detailInfo.applicationDate.replace(/-/g, '/')), 'YYYY-MM-DD', 'date')
                    }
                    if (that.detailInfo) {
                        that.form.Driver = that.detailInfo.driverName
                        that.form.TruckNo = that.detailInfo.carNumber
                        that.form.CarrierName = that.detailInfo.carrierName
                        that.form.AppointmentWeight = that.detailInfo.beforeWeight || null
                        that.form.BusinessDescription = that.detailInfo.reason || null
                        if (that.detailInfo.applicationDate) {
                            that.detailInfo.applicationDate = that.utils.timeFormat(new Date(that.detailInfo.applicationDate.replace(/-/g, '/')), 'YYYY-MM-DD', 'date')
                            that.form.CarStarDate = that.detailInfo.applicationDate
                        }
                    }
                }
            })
        },
        getuuid2() {
      return 'xxxx4xxxyxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    },           
        // 业务类型改变
        BusinessTypeget(e) {
            console.log(e)
            let that = this
            // console.log(this.form)
            this.$refs.uForm.validateField('BusinessType')
            if (e.userId === '0') {
                this.formRules = {
                    BusinessType: {
                        required: true,
                        message: "请选择业务类型",
                        type: 'number',
                        trigger: ["change"],
                    },
                    SupplyCode: {
                        required: false,
                        message: "请选择供应商",
                        trigger: ["blur", "change"],
                    },  
                    // CustCode: {
                    //     required: true,
                    //     message: "请输入客户编号",
                    //     trigger: ["blur", "change"],
                    // }, 
                    FactoryCode: {
                        required: false,
                        message: "请输入工厂代码",
                        trigger: ["blur", "change"],
                    },  
                    BusinessDescription: {
                        required: false,
                        message: "请输入业务描述",
                        trigger: ["blur", "change"],
                    }, 
                    CarStarDate: {
                        required: true,
                        message: "请选择车辆进厂日期",
                        trigger: ["blur", "change"],
                    },  
                    TruckNo: {
                        required: true,
                        message: "请输入车辆号",
                        trigger: ["blur", "change"],
                    }, 
                    Driver: {
                        required: true,
                        message: "请输入司机名称",
                        trigger: ["blur", "change"],
                    },  
                    TackID: {
                        required: false,
                        message: "请输入业务主键",
                        trigger: ["blur", "change"],
                    },  
                    WMSPickingRouteIDs: {
                        required: false,
                        message: "请输入领料流程",
                        trigger: ["blur", "change"],
                    }, 
                    WMSPickingRouteIDs2: {
                        required: false,
                        message: "请输入领料流程2",
                        trigger: ["blur", "change"],
                    }, 
                    Float: {
                        required: false,
                        message: "请输入扣重",
                        trigger: ["blur", "change"],
                    },  
                    AppointmentWeight: {
                        required: true,
                        message: "请输入重量",
                        type: 'number',
                        trigger: ["blur", "change"],
                    },
                    FloatReason: {
                        required: false,
                        message: "请输入预约重量变更原因",
                        trigger: ["blur", "change"],
                    }, 
                    ShipName: {
                        required: false,
                        message: "请输入船名",
                        trigger: ["blur", "change"],
                    },  
                    ShipNo: {
                        required: false,
                        message: "请输入船次",
                        trigger: ["blur", "change"],
                    }, 
                    CarrierCode: {
                        required: false,
                        message: "请输入承运商代码",
                        trigger: ["blur", "change"],
                    },  
                    CarrierName: {
                        required: true,
                        message: "请输入承运商名称",
                        trigger: ["blur", "change"],
                    }, 
                    EnableControl: {
                        required: true,
                        type: 'number',
                        message: "请选择是否启用管控",
                        trigger: ["blur", "change"],
                    },  
                    TolerancePercentageAdd: {
                        required: false,
                        message: "请输入正容错比例",
                        trigger: ["blur", "change"],
                    },  
                    TolerancePercentageDec: {
                        required: false,
                        message: "请输入负容错比例",
                        trigger: ["blur", "change"],
                    }, 
                    DocumentCode: {
                        required: false,
                        message: "请输入单据编号",
                        trigger: ["blur", "change"],
                    }, 
                    PackingSlipId: {
                        required: false,
                        message: "请输入产品收据",
                        trigger: ["blur", "change"],
                    }, 
                }
            } else if (e.userId === '1') {
                this.formRules = {
                    BusinessType: {
                        required: true,
                        message: "请选择业务类型",
                        type: 'number',
                        trigger: ["change"],
                    },
                    // SupplyCode: {
                    //     required: false,
                    //     message: "请输入供应商编号",
                    //     trigger: ["blur", "change"],
                    // },  
                    CustCode: {
                        required: false,
                        message: "请输入客户编号",
                        trigger: ["blur", "change"],
                    }, 
                    FactoryCode: {
                        required: false,
                        message: "请输入工厂代码",
                        trigger: ["blur", "change"],
                    },  
                    BusinessDescription: {
                        required: false,
                        message: "请输入业务描述",
                        trigger: ["blur", "change"],
                    }, 
                    CarStarDate: {
                        required: true,
                        message: "请选择车辆进厂日期",
                        trigger: ["blur", "change"],
                    },  
                    TruckNo: {
                        required: true,
                        message: "请输入车辆号",
                        trigger: ["blur", "change"],
                    }, 
                    Driver: {
                        required: true,
                        message: "请输入司机名称",
                        trigger: ["blur", "change"],
                    },  
                    TackID: {
                        required: false,
                        message: "请输入业务主键",
                        trigger: ["blur", "change"],
                    },  
                    WMSPickingRouteIDs: {
                        // required: true,
                        required: false,
                        message: "请输入领料流程",
                        trigger: ["blur", "change"],
                    }, 
                    WMSPickingRouteIDs2: {
                        // required: true,
                        required: false,
                        message: "请输入领料流程2",
                        trigger: ["blur", "change"],
                    }, 
                    Float: {
                        required: false,
                        message: "请输入扣重",
                        trigger: ["blur", "change"],
                    }, 
                    AppointmentWeight: {
                        required: false,
                        message: "请输入重量",
                        type: 'number',
                        trigger: ["blur", "change"],
                    }, 
                    FloatReason: {
                        required: false,
                        message: "请输入预约重量变更原因",
                        trigger: ["blur", "change"],
                    }, 
                    ShipName: {
                        required: false,
                        message: "请输入船名",
                        trigger: ["blur", "change"],
                    },  
                    ShipNo: {
                        required: false,
                        message: "请输入船次",
                        trigger: ["blur", "change"],
                    }, 
                    CarrierCode: {
                        required: false,
                        message: "请输入承运商代码",
                        trigger: ["blur", "change"],
                    },  
                    CarrierName: {
                        required: false,
                        message: "请输入承运商名称",
                        trigger: ["blur", "change"],
                    }, 
                    // EnableControl: {
                    //     required: false,
                    //     type: 'number',
                    //     message: "请选择是否启用管控",
                    //     trigger: ["blur", "change"],
                    // },  
                    // TolerancePercentageAdd: {
                    //     required: false,
                    //     message: "请输入正容错比例",
                    //     trigger: ["blur", "change"],
                    // },  
                    // TolerancePercentageDec: {
                    //     required: false,
                    //     message: "请输入负容错比例",
                    //     trigger: ["blur", "change"],
                    // }, 
                    // DocumentCode: {
                    //     required: false,
                    //     message: "请输入单据编号",
                    //     trigger: ["blur", "change"],
                    // }, 
                    // PackingSlipId: {
                    //     required: false,
                    //     message: "请输入产品收据",
                    //     trigger: ["blur", "change"],
                    // }, 
                }
            } else if (e.userId === '2') {
                this.formRules = {
                    BusinessType: {
                        required: true,
                        message: "请选择业务类型",
                        type: 'number',
                        trigger: ["change"],
                    },
                    // SupplyCode: {
                    //     required: false,
                    //     message: "请输入供应商编号",
                    //     trigger: ["blur", "change"],
                    // },  
                    // CustCode: {
                    //     required: true,
                    //     message: "请输入客户编号",
                    //     trigger: ["blur", "change"],
                    // }, 
                    FactoryCode: {
                        required: false,
                        message: "请输入工厂代码",
                        trigger: ["blur", "change"],
                    },  
                    BusinessDescription: {
                        required: false,
                        message: "请输入业务描述",
                        trigger: ["blur", "change"],
                    }, 
                    CarStarDate: {
                        required: true,
                        message: "请选择车辆进厂日期",
                        trigger: ["blur", "change"],
                    },  
                    TruckNo: {
                        required: true,
                        message: "请输入车辆号",
                        trigger: ["blur", "change"],
                    }, 
                    Driver: {
                        required: true,
                        message: "请输入司机名称",
                        trigger: ["blur", "change"],
                    },  
                    TackID: {
                        required: false,
                        message: "请输入业务主键",
                        trigger: ["blur", "change"],
                    },  
                    WMSPickingRouteIDs: {
                        // required: true,
                        required: false,
                        message: "请输入领料流程",
                        trigger: ["blur", "change"],
                    }, 
                    WMSPickingRouteIDs2: {
                        // required: true,
                        required: false,
                        message: "请输入领料流程2",
                        trigger: ["blur", "change"],
                    }, 
                    Float: {
                        required: false,
                        message: "请输入扣重",
                        trigger: ["blur", "change"],
                    },  
                    FloatReason: {
                        required: false,
                        message: "请输入预约重量变更原因",
                        trigger: ["blur", "change"],
                    }, 
                    // ShipName: {
                    //     required: false,
                    //     message: "请输入船名",
                    //     trigger: ["blur", "change"],
                    // },  
                    // ShipNo: {
                    //     required: false,
                    //     message: "请输入船次",
                    //     trigger: ["blur", "change"],
                    // }, 
                    // CarrierCode: {
                    //     required: false,
                    //     message: "请输入承运商代码",
                    //     trigger: ["blur", "change"],
                    // },  
                    CarrierName: {
                        required: true,
                        message: "请输入承运商名称",
                        trigger: ["blur", "change"],
                    }, 
                    // EnableControl: {
                    //     required: false,
                    //     type: 'number',
                    //     message: "请选择是否启用管控",
                    //     trigger: ["blur", "change"],
                    // },  
                    // TolerancePercentageAdd: {
                    //     required: false,
                    //     message: "请输入正容错比例",
                    //     trigger: ["blur", "change"],
                    // },  
                    // TolerancePercentageDec: {
                    //     required: false,
                    //     message: "请输入负容错比例",
                    //     trigger: ["blur", "change"],
                    // }, 
                    // DocumentCode: {
                    //     required: false,
                    //     message: "请输入单据编号",
                    //     trigger: ["blur", "change"],
                    // }, 
                    // PackingSlipId: {
                    //     required: false,
                    //     message: "请输入产品收据",
                    //     trigger: ["blur", "change"],
                    // }, 
                }
            }
            this.$nextTick(() => {
                // #ifndef H5
                this.$refs.uForm.setRules(this.formRules);
                console.log('非H5执行代码')
                console.log(this.formRules)
                // #endif
                that.$refs.uForm.clearValidate()
            })
        },
        // 获取uuid 自己生成随机数
        getuuid() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
                return v.toString(16);
            });
        }
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';
@import '@/styles/formlist.scss';
.page {
    padding: 12rpx 24rpx 40rpx;
}

.butframe {
    width: 100%;
    margin-top: 48rpx;

    .but {
        width: 638rpx;
        height: 98rpx;
        background: #2196F3;
        box-shadow: 0rpx 8rpx 16rpx rgba(0,0,0,0.16);
        opacity: 1;
        border-radius: 8rpx;
    }

    .buttext {
        font-size: 36rpx;
        font-family: PingFangSC;
        font-weight: 400;
        color: #FFFFFF;
    }
}

.buttips {
    width: 100%;
    font-size: 24rpx;
    font-family: PingFangSC;
    color: #808080;
    text-align: center;
    margin-top: 24rpx;
}
</style>
