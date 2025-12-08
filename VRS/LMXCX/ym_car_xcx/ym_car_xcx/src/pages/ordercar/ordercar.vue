<template>
    <view>
        <headerbar :title="title" background="#2195F3" title_color="#FFFFFF" :noback="!edit"/>
        <view class="page"> 
        <u-form-item labelWidth="0" prop="carNumber" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">车牌号</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.carNumber"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
            <u-form :model="form" :borderBottom="true" :rules="formRules" errorType="message" ref="uForm">
                <u-form-item labelWidth="0" prop="carrierId" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">承运商</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formselect_search v-model="form.carrierId" mode="select" @popchange="hidearea"
                                placeholder="请选择" apikey="carrier" @sure="carrierget" :disabled="checkuserType('driver') ||checkuserType('carrier')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
				<!-- <u-form-item labelWidth="0" prop="driverId" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">司机姓名</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formselect_search v-model="form.driverId" mode="select" @popchange="hidearea"
                                placeholder="请选择" apikey="driver" :params="driverparams" @sure="driverget" :disabled="checkuserType('driver')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item> -->
                <u-form-item labelWidth="0" prop="driverId" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">司机姓名</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400 flex">
                                <formdriverIdbut v-model="form.driverId" @popchange="hidearea" apikey="driver" :params="driverparams" @sure="driverget" :disabled="checkuserType('driver')"/>
                                <formelement v-model="form.driverName" @input="driverNameInput" :disabled="checkuserType('driver')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>               
                <u-form-item labelWidth="0" prop="phone" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">手机号</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.phone" type="number"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="idcard" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">身份证号</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.idcard"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
        <u-form-item labelWidth="0" prop="applicationDate" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">入厂时间</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formdate v-model="form.applicationDate" :minDate="minDate" mode="date" @sure="$refs.uForm.validateField('applicationDate')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
        <u-form-item labelWidth="0" prop="reasonKey" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">原因</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.reasonKey" mode="select" @popchange="hidearea"
                                placeholder="请选择" apikey="reason" @sure="reasonget"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
        <u-form-item labelWidth="0" prop="scrapKey" borderBottom
        v-if="form.reasonKey === '4'">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">废料名称</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formselect_search v-model="form.scrapKey" mode="select" @popchange="hidearea"
                                placeholder="请选择" apikey="scrap" @sure="scrapget"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>        
        <u-form-item labelWidth="0" prop="beforeWeight" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">送/提货预计重量(kg)</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400 flex_only">
                                <formelement v-model="form.beforeWeight" type="number" placeholder="请输入"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <!-- <u-form-item labelWidth="0" prop="application" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">受访人</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.application" :disabled="true"/>
                            </view>
                        </view>
                    </view>
				</u-form-item> -->
                <u-form-item labelWidth="0" prop="checkerId" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">受访人</view><text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formselect_search v-model="form.checkerId" mode="select" @popchange="hidearea"
                                placeholder="请选择" apikey="checker" :params="checkerparams"  @sure="checkerget"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="remark" :formRules="[{ required: true, message:'备注不能为空', trigger: 'change' }]" :validate-trigger="'change'" borderBottom v-if="form.reasonKey === '5'">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">备注</view>
                            <text class="isneed">*</text>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.remark" mode="textarea" @inputchange="$refs.uForm.validateField('remark')" v-show="textareahide"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
        <u-form-item labelWidth="0" prop="remark" borderBottom v-else>
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">备注</view>
                            <!--<text class="isneed">*</text>-->
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.remark" mode="textarea" @inputchange="$refs.uForm.validateField('remark')" v-show="textareahide"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
			</u-form>
            <view class="butframe flex_center">
                <view class="but flex_center" @click="submit">
                    <view class="buttext">提交</view>
                </view>
            </view>
        </view>
        <bottombar active="ordercar"/>
        <showmodal :showtitle="false" :show="blackmodal" content="您没有权限提交车辆预约信息哦,请联系公司的管理人员~" @sure="blackmodal = false" @cancel="blackmodal = false"></showmodal>
        <u-toast ref="uToast"></u-toast>
    </view>
</template>

<script>
import { insert, update, historydetail } from '@/api/ordercar.js'
import { userinfo } from '@/api/user.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 新增/编辑
            edit: false,
            // 编辑时 查找详情id
            applicationId: null,
            detailInfo: {},
            // 页面头文字
            title: '车辆预约',
            // 用户信息
            userinfo: {},
            // 黑名单提示
            blackmodal: false,
            //用户登录表单
            form: {
                carrierId: null,
                carrierName: '',
                driverId: null,
                driverName: '',
                carNumber: '',
                phone: '',
                idcard: '',
                application: '',
                applicationDate: '',
                beforeWeight: '',
                reason: null,
                reasonKey: null,
                scrap: null,
                scrapKey: null,
                remark: '',
                checkerId: null,
                checkerName: '',
            },
            formRules: {
                carrierId: {
                    required: true,
                    message: "请选择承运商",
                    type: 'number',
                    trigger: ["change"],
                },
                driverName: {
                    required: true,
                    message: "请输入司机姓名",
                    type: 'number',
                    trigger: ["blur", "change"],
                },
                carNumber: {
                    required: true,
                    message: "请输入车牌号",
                    trigger: ["blur", "change"],
                },
                phone: [
                    {
                        type: "string",
                        required: "true",
                        message: "请输入手机号",
                        trigger: ["blur", "change"],
                    },
                    {
                        // 自定义验证函数，见上说明
                        validator: (rule, value, callback) => {
                            // 上面有说，返回true表示校验通过，返回false表示不通过
                            // uni.$u.test.mobile()就是返回true或者false的
                            // console.log(rule)
                            return uni.$u.test.mobile(value);
                        },
                        message: "手机号码不正确",
                        // 触发器可以同时用blur和change
                        trigger: ["blur"],
                    },
                ],
                idcard: [
                    {
                        type: "string",
                        required: "true",
                        message: "请输入身份证号",
                        trigger: ["blur", "change"],
                    },
                    {
                        // 自定义验证函数，见上说明
                        validator: (rule, value, callback) => {
                            // 上面有说，返回true表示校验通过，返回false表示不通过
                            // uni.$u.test.mobile()就是返回true或者false的
                            // console.log(rule)
                            return uni.$u.test.idCard(value);
                        },
                        message: "身份证不正确",
                        // 触发器可以同时用blur和change
                        trigger: ["blur"],
                    },
                ],
                application: {
                    required: false,
                    message: "请输入受访人",
                    trigger: ["blur", "change"],
                },
                applicationDate: {
                    required: true,
                    message: "请选择入厂时间",
                    trigger: ["blur", "change"],
                },
                beforeWeight: {
                    required: true,
                    message: "请输入送/提货预计重量",
                    type: 'number',
                    trigger: ["blur", "change"],
                },
                reasonKey: {
                    required: true,
                    message: "请选择原因",
                    trigger: ["blur", "change"],
                },
                scrapKey: {
                    required: true,
                    message: "请选择废料名称",
                    trigger: ["blur", "change"],
                },
                checkerId: {
                    required: true,
                    message: "请选择受访人",
                    type: 'number',
                    trigger: ["blur", "change"],
                }
            },
            // 承运商键值对
            carrier: null,
            // 司机键值对
            driver: null,
            // 司机键值对 依赖于 承运商 所以组件需要请求参数
            driverparams: {},
            // 审核员键值对
            checker: null,
            // 司机键值对 依赖于 承运商 所以组件需要请求参数
            checkerparams: {},
            // 原因键值对
            reason: null,
            // 废料名称键值对
            scrap: null,            
            // 时间选择器最小时间
            minDate: null,
            // 
            textareahide: true,
        };
    },
    onLoad(e) {
        this.edit = e.edit === 'true'
        if (this.edit) {
            this.title = '修改'
            this.applicationId = e.applicationId
            this.gethistorydetail()
        } else {
            this.title = '车辆预约'

            // 测试时预留
            // this.applicationId = '6549a47dab084fb6808648119803cbf2'
            // this.gethistorydetail()
        }
        this.minDate = new Date().getTime()
        this.imgUrl = this.globalUrl;
		    this.getuserInfo()
        // #ifndef H5
        console.log('非H5执行代码')
        this.$refs.uForm.setRules(this.formRules);
        // #endif
    },
    methods: {
        // 提交表单
        submit() {
            // 黑名单0否1是
            let isBlacklist = this.userinfo.isBlacklist
            // console.log(this.userinfo)
            if (isBlacklist === 1) {
                this.blackmodal = true
                return
            }
            // console.log(this.form)
            let that = this
            this.$refs.uForm.validate().then((res) => {
                // console.log(res)
                if (res) {
                    // if (that.driver && that.driver.nikName) {
                    //     that.form.driverName = that.driver && that.driver.nikName || ''
                    // }
                    if (that.carrier && that.carrier.nikName) {
                        that.form.carrierName = that.carrier && that.carrier.nikName || ''
                    }
                    if (that.checker && that.checker.nikName) {
                        that.form.checkerName = that.checker && that.checker.nikName || ''
                    }
                    if (that.reason && that.reason.nikName) {
                        that.form.reason = that.reason && that.reason.nikName || ''
                    }
                    if (that.scrap && that.scrap.nikName) {
                        that.form.scrap = that.scrap && that.scrap.nikName || ''
                    }                    
                    // 1本人提交2代提交
                    // 目前业务是 admin（审核人） 提交都为 代提交 承运商是 本人提交 司机不可提交
                    that.form.postType = this.checkuserType('admin') ? 2 : 1
                    if (that.edit) {
                        that.updateapi()
                    } else {
                        that.insertapi()
                    }
                }
            }).catch((errors) => {

            });
        },
        // 调用新增接口
        insertapi() {
            let that = this
            uni.showLoading({
                title: "提交中...",
                mask: true
            });
            // console.log(this.form)
            insert(this.form).then((res) => {
                uni.hideLoading()
                let dataRes = res.data;
                if (dataRes.code == 200) {
                    this.$refs.uToast.show({
                        type: "success",
                        message: "预约成功",
                        complete() {
                            // that.toordercarresult()
                            uni.redirectTo({
                                url: '/pages/ordercar/ordercarresult?edit=' + that.edit + '&applicationId=' + dataRes.msg
                            })
                        },
                    });
                } else {
                    this.$refs.uToast.show({
                        type: "error",
                        message: dataRes.msg,
                    });
                }
            });
        },
        // 调用编辑接口
        updateapi() {
            let that = this
            uni.showLoading({
                title: "提交中...",
                mask: true
            });
            // console.log(this.form)
            update(this.form).then((res) => {
                uni.hideLoading()
                let dataRes = res.data;
                if (dataRes.code == 200) {
                    this.$refs.uToast.show({
                        type: "success",
                        message: "修改成功",
                        complete() {
                            // that.toordercarresult()
                            uni.redirectTo({
                                url: '/pages/ordercar/ordercarresult?edit=' + that.edit + '&applicationId=' + that.applicationId
                            })
                        },
                    });
                } else {
                    this.$refs.uToast.show({
                        type: "error",
                        message: dataRes.msg,
                    });
                }
            });
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
                    that.detailInfo = historydata
                    if (historydata.applicationDate) {
                        historydata.applicationDate = that.utils.timeFormat(new Date(historydata.applicationDate.replace(/-/g, '/')), 'YYYY-MM-DD', 'date')
                    }
                    that.form = {
                        applicationId: historydata.applicationId,
                        carrierId: historydata.carrierId,
                        carrierName: historydata.carrierName,
                        driverId: historydata.driverId,
                        driverName: historydata.driverName,
                        carNumber: historydata.carNumber,
                        phone: historydata.phone,
                        idcard: historydata.IDCard,
                        application: historydata.createdName,
                        applicationDate: historydata.applicationDate,
                        beforeWeight: historydata.beforeWeight,
                        reason: historydata.reason,
                        reasonKey: historydata.reasonKey,
                        scrap: historydata.scrap,
                        scrapKey: historydata.scrapKey,         
                        remark: historydata.remark,
                        checkerId: historydata.checkerId,
                        checkerName: historydata.checkerName
                    }
                    that.driverparams = {
                        parentId: historydata.carrierId
                    }
                }
            })
        },
        // 跳转预约成功页面
        toordercarresult() {
            this.blackmodal = false
            uni.redirectTo({
                url: '/pages/ordercar/ordercarresult?edit=' + this.edit
            })
        },
        // 获取用户信息 默认填写承运商
        getuserInfo() {
            let that = this
            userinfo().then(res => {
                let dataRes = res.data || {}
                let userinfo = dataRes.data || {}
                that.userinfo = userinfo
                // 新增时 主动带入承运商
                if (!that.edit) {
                    if (that.checkuserType('driver')) {
                        that.form.driverId = userinfo.userId
                        that.form.carrierId = userinfo.parentId
                        // 根据承运商id 去查询司机数组
                        that.driverparams = {
                            parentId: userinfo.parentId
                        }
                    }
                    if (that.checkuserType('carrier')) {
                        that.form.carrierId = userinfo.userId
                        // 根据承运商id 去查询司机数组
                        that.driverparams = {
                            parentId: userinfo.userId
                        }
                    }
                }
                if (!that.edit) {
                    // 预约人/受访人 永远都是自己 根据查看记录修改除外
                    that.form.application = userinfo.userName || ''
                }
            })
        },
        // 获取承运商数据
        carrierget(e) {
            // console.log(e)
            this.carrier = e
            this.driverparams = {
                parentId: this.carrier.userId
            }
            if (!this.edit) {
                this.form.phone = ''
                this.form.idcard = ''
            }
            this.$refs.uForm.validateField('carrierId')
        },
        // 获取司机数据
        driverget(e) {
            this.driver = e
            // console.log(e)
            if (this.driver && this.driver.nikName) {
                this.form.driverName = this.driver && this.driver.nikName || ''
                this.form.driverId = this.driver && this.driver.userId || ''
            }
            if (!this.edit) {
                if (e.phoneNumber) {
                    this.form.phone = e.phoneNumber || ''
                    this.$refs.uForm.validateField('phone')
                }
                if (e.idcard) {
                    this.form.idcard = e.idcard || ''
                    this.$refs.uForm.validateField('idcard')
                }
            }
            console.log('选择司机')
            console.log(this.form.driverName)
            console.log(this.form.driverId)
            this.$refs.uForm.validateField('driverId')
        },
        // 手动输入司机名称
        driverNameInput(e) {
            this.form.driverId = null
            console.log('手动输入名称')
            console.log(this.form.driverName)
            console.log(this.form.driverId)
        },
        // 获取审核员数据
        checkerget(e) {
            this.checker = e
            this.$refs.uForm.validateField('checkerId')
        },
        // 获取原因
        reasonget(e) {
            this.reason = e
            this.$refs.uForm.validateField('reasonKey')
        },
        // 获取废料名称
        scrapget(e) {
            this.scrap = e
            this.$refs.uForm.validateField('scrapKey')
        },        
        // 更新form的刷新规则
        applicationDateget(e) {
            // console.log(this.form)
            this.$refs.uForm.validateField('applicationDate')
        },
        // 隐藏textarea输入框
        hidearea(e) {
            this.textareahide = e
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
</style>
