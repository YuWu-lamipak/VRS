<template>
    <view class="page">
        <headerbar title="预约详情" background="#2195F3" title_color="#FFFFFF" :noback="false"/>
        <view class="cardframe">
            <view class="carditem margin_bottom16">
                <view class="cardtopline flex margin_bottom32">
                    <view class="cardtoplinetitle textmore">车辆预约状态</view>
                </view>
                <view class="cardcontentline flex">
                    <view class="leftkey width150">状态</view>
                    <view v-if="detailInfo.enterStatus === 1 && isguardrole" class="leftauto statusbut statuscolor0">已进厂</view>
                    <view v-else-if="detailInfo.enterStatus === 2 && isguardrole" class="leftauto statusbut statuscolor3">已出厂</view>
                    <view v-else-if="detailInfo.status === 1" class="leftauto statusbut statuscolor0">已申请</view>
                    <view v-else-if="detailInfo.status === 2" class="leftauto statusbut statuscolor1">审核通过</view>
                    <view v-else-if="detailInfo.status === 3" class="leftauto statusbut statuscolor2">审核不通过</view>
                </view>
            </view>
            <view class="carditem margin_bottom16">
                <view class="cardtopline flex margin_bottom32">
                    <view class="cardtoplinetitle textmore">车辆预约信息</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">申请时间</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.createdDate || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">车牌号</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.carNumber || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">承运商</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.carrierName || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">司机姓名</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.driverName || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">手机号</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.phone || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">身份证号</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.IDCard || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">入厂时间</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.applicationDate || '' }}</view>
                </view>
                <view class="cardcontentline flex_only margin_bottom24">
                    <view class="leftkey width150">原因</view>
                    <view class="rightvalue maxwidth450 leftauto">{{ detailInfo.reason || '无' }} </view>
                </view>
                <view class="cardcontentline flex_only margin_bottom24"
                v-if="detailInfo.reasonKey === '4'">
                    <view class="leftkey width150">废料名称</view>
                    <view class="rightvalue maxwidth450 leftauto">{{ detailInfo.scrap || '无' }} </view>
                </view>                
                <!-- <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150">受访人</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.createdName || '' }}</view>
                </view> -->
                <view class="cardcontentline flex margin_bottom24">
                    <view class="leftkey width150" style="width: 200px;">送/提货预计重量(kg)</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.beforeWeight ? detailInfo.beforeWeight: '无' }}</view>
                </view>
                <view class="cardcontentline flex_only margin_bottom24">
                    <view class="leftkey width150">备注</view>
                    <view class="rightvalue maxwidth450 leftauto">{{ detailInfo.remark || '无' }} </view>
                </view>
            </view>
            <view class="carditem">
                <view class="cardtopline flex margin_bottom32">
                    <view class="cardtoplinetitle textmore">审核信息</view>
                </view>
                <view class="cardcontentline flex">
                    <view class="leftkey width150">审核人</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.checkerName || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_top24" v-if="checkauthority('admin') && detailInfo.orderId">
                    <view class="leftkey width150">预约号码</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.orderId || '直接审核通过' }}</view>
                </view>
                <view class="cardcontentline flex margin_top24" v-if="detailInfo.enterDate">
                    <view class="leftkey width200">实际进厂时间</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.enterDate || '' }}</view>
                </view>
                <view class="cardcontentline flex margin_top24" v-if="detailInfo.outDate">
                    <view class="leftkey width200">实际出厂时间</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ detailInfo.outDate || '' }}</view>
                </view>
            </view>
        </view>
        <view class="savebox"></view>
        <!-- 修改 删除 展示条件  审核通过不可修改 只能本人修改  审核通过 与 修改 与 入厂/出场 同时只能存在一个-->
        <!-- 从 新增成功 页面跳转过来的 展示 回到首页 -->
        <view class="butframe flex_center" v-if="isadd">
            <view class="centerbut flex_center" @click="tohome">
                <view class="buttext">回到首页</view>
            </view>
        </view>
        <!-- v-if="(detailInfo.status === 1 || detailInfo.status === 3) && userinfo.userId === detailInfo.createdBy && (!checkauthority('admin') || (checkauthority('admin') && detailInfo.status === 3))" -->
        <view class="butframe flex_center" v-else-if="detailInfo.status === 1 && userinfo.userId === detailInfo.createdBy && !checkauthority('admin')">
            <view class="leftbut flex_center marginright84" @click="toordercar()">
                <view class="buttext">修改</view>
            </view>
            <!--<view class="rightbut flex_center" @click="deleteclick">
                <view class="buttext">删除</view>
            </view>-->
        </view>
        <view class="butframe flex_center" v-else-if="(detailInfo.status === 1 && userinfo.userId === detailInfo.createdBy && role === 'adminnone')">
            <view class="leftbut flex_center marginright84" @click="toordercar()">
                <view class="buttext">修改</view>
            </view>
            <view class="rightbut flex_center" @click="deleteclick">
                <view class="buttext">删除</view>
            </view>
        </view>
        <view class="butframe flex_center" v-else-if="detailInfo.status === 1 && checkauthority('admin')">
            <view class="leftbut flex_center marginright84" @click="toorderauthen()">
                <view class="buttext">审核通过</view>
            </view>
            <view class="rightbut flex_center" @click="authenapi(3)">
                <view class="buttext">审核不通过</view>
            </view>
        </view>
    <!--<view class="butframe flex_center" v-else-if="detailInfo.status === 2 && checkauthority('admin') && detailInfo.enterStatus === 1 && userinfo.userId === detailInfo.checkerId">
            <view class="centerbut flex_center" @click="emptycarapi">
                <view class="buttext">空车出厂</view>
            </view>
        </view>-->
    <!--<view class="butframe flex_center" v-else-if="detailInfo.status === 2 && checkauthority('guard') && detailInfo.enterStatus === 0">
            <view class="centerbut flex_center" @click="enterapi">
                <view class="buttext">确认进厂</view>
            </view>
        </view>-->
        <view class="butframe flex_center" v-else-if="detailInfo.status === 2 && checkauthority('guard') && detailInfo.enterStatus === 1">
            <view class="centerbut flex_center" @click="outapi">
                <view class="buttext">确认出厂</view>
            </view>
        </view>
        <u-toast ref="uToast"></u-toast>
        <showmodal :show="tipsmodal.show" :title="tipsmodal.title" :content="tipsmodal.content" @sure="backpage()" @cancel="backpage()"></showmodal>
        <showmodal :showtitle="false" :show="deletemodal" content="确定删除车辆预约信息吗？" @sure="deletelist()" @cancel="deletemodal = false"></showmodal>
    </view>
</template>

<script>
import { historydetail, historydelete, authen, enter, out } from '@/api/ordercar.js'
import { userinfo } from '@/api/user.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 预约记录id
            applicationId: null,
            // 由于门卫 列表展示页面不同 又因目前需求可以多角色 用来判断页面展示到地是那个类型列表
            isguardrole: false,
            // 判断是否从 新增成功页面 跳转过来的（新增后查看详情 只展示 回到首页 按钮）
            isadd: false,
            // 详情信息
            detailInfo: {
                // 1已申请2审核通过3审核不通过
                status: null,
                // 0未入厂1已入厂2已出厂
                enterStatus: null,
            },
            // 用户信息
            userinfo: {},
            // dialog提示
            deletemodal: false,
            tipsmodal: {
                show: false,
                title: '',
                content: ''
            },
            // 判断是否来自车辆代预约
            role: null,
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        this.applicationId = e.applicationId
        this.isadd = e.isadd === 'true'
        // 当role为 adminnone时 代表从车辆代预约进来 不可以审核
        // console.log(e)
        this.role = e.role || null
        console.log(this.role)
        this.getuserInfo()
        if (this.checkauthority('admin')) {
        } else if (this.checkauthority('guard')) {
            this.isguardrole = true
        } else {
        }
    },
    onShow() {
        this.reflash()
    },
    methods: {
        // 审核 1已申请2审核通过3审核不通过
        authenapi(status) {
            let that = this
            let params = {
                applicationId: this.applicationId,
                status,
            }
            // that.detailInfo.status = status
            // return
            uni.showLoading({
                title: "提交审核中...",
                mask: true
            });
            authen(params).then(res => {
                uni.hideLoading()
                let dataRes = res.data || {}
                if (dataRes.code === 200) {
                    uni.setStorageSync("needToRefreshhistorylist", true)
                    // 对数组操作 修改操作内容
                    let pages = getCurrentPages() // 获取栈实例
                    if (pages && pages.length > 1) {
                        // 如果是从列表页过来 修改列表页数组
                        let page = pages[pages.length - 2]
                        if (page.$vm.list && page.$vm.list.length > 1) {
                            let deleteindex = -1;
                            for (let index = 0; index < page.$vm.list.length; index++ ) {
                                if (page.$vm.list[index].applicationId === that.applicationId){
                                    page.$vm.list[index].status = status
                                    break
                                }
                            }
                        }
                    }
                    that.detailInfo.status = status
                    if (status === 2) {
						uni.showToast({
						    title: '审核通过',
						    icon: 'none'
						})
					} else if (status === 3) {
						uni.showToast({
						    title: '审核不通过',
						    icon: 'none'
						})
					}
                } else {
                    uni.showToast({
                        title: dataRes.msg || '提交审核失败',
                        icon: 'error'
                    })
                }
            })
        },
        // 进厂
        enterapi() {
            let that = this
            let params = {
                applicationId: this.applicationId,
            }
            uni.showLoading({
                title: "进厂中...",
                mask: true
            });
            enter(params).then(res => {
                uni.hideLoading()
                let dataRes = res.data || {}
                if (dataRes.code === 200) {
                    that.reflash()
                    let time = that.utils.timeFormat(new Date(), 'YYYY-MM-DD hh:mm:ss', 'date')
                    uni.setStorageSync("needToRefreshhistorylist", true)
                    that.tipsmodal = {
                        show: true,
                        title: '确认入厂成功~',
                        content: '入厂时间：' + time
                    }
                } else {
                    uni.showToast({
                        title: dataRes.msg || '入厂失败',
                        icon: 'error'
                    })
                }
            })
        },
        // 出厂
        outapi() {
            let that = this
            let params = {
                applicationId: this.applicationId,
            }
            uni.showLoading({
                title: "出厂...",
                mask: true
            });
            out(params).then(res => {
                uni.hideLoading()
                let dataRes = res.data || {}
                if (dataRes.code === 200) {
                    that.reflash()
                    let time = that.utils.timeFormat(new Date(), 'YYYY-MM-DD hh:mm:ss', 'date')
                    uni.setStorageSync("needToRefreshhistorylist", true)
                    that.tipsmodal = {
                        show: true,
                        title: '确认出厂成功~',
                        content: '出厂时间：' + time
                    }
                } else {
                    uni.showToast({
                        title: dataRes.msg || '出厂失败',
                        icon: 'error'
                    })
                }
            })
        },
        // 调用外部空车出厂
        emptycarapi() {
            let that = this
            if (!this.detailInfo.orderId) {
                this.$refs.uToast.show({
                    type: "error",
                    message: '未知错误，预约单号不存在',
                });
                return
            }
            let data = {
                AppointmentID: this.detailInfo.orderId,
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
            uni.showLoading({
                title: "请稍等...",
                mask: true
            });
            wx.cloud.init()
            wx.cloud.callFunction({
                name: 'emptycar',
                data: params,
            }).then(cloudres => {
                console.log(cloudres)
                let res = cloudres && cloudres.result || {}
                if (typeof(res) !== 'object') {
                    res = JSON.parse(res)
                }
                console.log(res)
                if (res) {
                    let returnres = res || {}
                    if (returnres.Code === '0') {
                        uni.hideLoading()
                        this.$refs.uToast.show({
                            type: "success",
                            message: res.Message || '空车出厂成功',
                        });
                    } else {
                        uni.hideLoading()
                        this.$refs.uToast.show({
                            type: "error",
                            message: res.Message || '空车出厂失败',
                        });
                    }
                } else {
                    uni.hideLoading()
                    this.$refs.uToast.show({
                        type: "error",
                        message: res.Message || '请求超时请稍后再试',
                    });
                }
            }).catch((errors) => {
                uni.hideLoading()
                console.log(errors)
                uni.showToast({
                    title: errors,
                    icon: 'none'
                })
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
                    that.detailInfo = historydata || {}
                    if (that.detailInfo.applicationDate) {
                        that.detailInfo.applicationDate = that.utils.timeFormat(new Date(that.detailInfo.applicationDate.replace(/-/g, '/')), 'YYYY-MM-DD', 'date')
                    }
                }
            })
        },
        // 获取用户信息 默认填写承运商
        getuserInfo() {
            let that = this
            userinfo().then(res => {
                let dataRes = res.data || {}
                let userinfo = dataRes.data || {}
                that.userinfo = userinfo
            })
        },
        // 删除订单
        historydeleteapi() {
            let that = this
            let params = {
                applicationId: this.applicationId
            }
            uni.showLoading({
                title: "删除中...",
                mask: true
            });
            historydelete(params).then(res => {
                uni.hideLoading()
                let dataRes = res.data || {}
                if (dataRes.code === 200) {
                    // 对数组操作 去掉删除内容
                    let pages = getCurrentPages() // 获取栈实例
                    if (pages && pages.length > 1) {
                        // 如果是从列表页过来 修改列表页数组 navigateBack 返回上一页必定是 列表页
                        let page = pages[pages.length - 2]
                        if (page.$vm.list && page.$vm.list.length > 0) {
                            let deleteindex = -1;
                            for (let index = 0; index < page.$vm.list.length; index++ ) {
                                if (page.$vm.list[index].applicationId === that.applicationId){
                                    deleteindex = index
                                    break
                                }
                            }
                            if(deleteindex > -1){
                                page.$vm.list.splice(deleteindex,1);
                            }
                        }
                        // debugger
                        uni.showToast({
                            title: '删除成功',
                            icon: 'success'
                        })
                        uni.navigateBack({
                            delta: 1,
                        })
                    } else {
                        // 当时新增成功页面 点击查看详情按钮路径过来 是没有上一页的 所以直接去 预约页面
                        uni.showToast({
                            title: '删除成功',
                            icon: 'success'
                        })
                        uni.reLaunch({
                           url: '/pages/ordercar/ordercar' 
                        })
                    }
                } else {
                    uni.showToast({
                        title: dataRes.msg || '删除失败',
                        icon: 'error'
                    })
                }
                this.deletemodal = false
            })
        },
        // 点击删除按钮 记录按钮所属的list item
        deleteclick() {
            this.deletemodal = true
        },
        // 删除按钮点击确认
        deletelist() {
            this.historydeleteapi()
            // this.deletemodal = false
        },
        // 刷新该页面 
        reflash() {
            if (this.applicationId) {
                this.detailInfo.status = null
                this.detailInfo.enterStatus = null
                this.gethistorydetail()
            }
        },
        // 返回上一页
        backpage() {
            this.tipsmodal.show = false
            uni.navigateBack({
                delta: 1,
            })
        },
        // 跳转修改
        toordercar() {
            uni.navigateTo({
                url: '/pages/ordercar/ordercar?edit=true&applicationId=' + this.applicationId
            })
        },
        // 跳转审核通过页面
        toorderauthen() {
            uni.navigateTo({
                url: '/pages/ordercar/orderauthen?applicationId=' + this.applicationId
            })
        },
        // 返回首页
        tohome() {
            uni.reLaunch({
                url: '/pages/home/home'
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

<style>
page{ 
    /* background-color:#F7F8FA; */
	background-color:#75B8FC;
}
</style>
<style lang="scss" scoped>
@import '@/styles/common.scss';
@import './historylist.scss';

.page {
    padding: 16rpx;
}

.cardframe {
    width: 100%;

    .carditem {
        width: calc(100% - 60rpx);
        padding: 24rpx 30rpx 48rpx;
        background: #FFFFFF;
        opacity: 1;
        border-radius: 16rpx;
    }

    .cardtopline {
        width: 100%;
        height: 94rpx;
        border-bottom: 1rpx solid #EAEAEA;

        .cardtoplinetitle {
            max-width: 450rpx;
            font-size: 32rpx;
            font-family: PingFangSC;
            font-weight: 600;
            color: #333333;
        }
    }

    .cardcontentline {

        .leftkey {
            font-size: 28rpx;
            font-family: PingFangSC;
            font-weight: 400;
            color: #717171;
        }

        .rightvalue {
            font-size: 28rpx;
            font-family: PingFangSC;
            font-weight: 400;
            color: #333333;
        }

        .width150 {
            width: 150rpx;
        }

        .width200 {
            width: 200rpx;
        }

        .maxwidth350 {
            max-width: 350rpx;
        }

        .maxwidth450 {
            max-width: 450rpx;
        }
    }
}

.savebox {
    width: 100%;
    height: 210rpx;
}

.butframe {
    position: fixed;
    bottom: 76rpx;
    left: 0;
    width: 100%;
    height: 88rpx;

    .centerbut {
        width: 638rpx;
        height: 98rpx;
        background: #2196F3;
        box-shadow: 0rpx 8rpx 16rpx rgba(0,0,0,0.16);
        opacity: 1;
        border-radius: 8rpx;
    }

    .leftbut {
        width: 257rpx;
        height: 88rpx;
        background: #2196F3;
        box-shadow: 0px 8rpx 16rpx rgba(0,0,0,0.16);
        opacity: 1;
        border-radius: 8rpx;
    }

    .rightbut {
        width: 257rpx;
        height: 88rpx;
        background: #C9C9C9;
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

    .marginright84 {
        margin-right: 84rpx;
    }
}
</style>
