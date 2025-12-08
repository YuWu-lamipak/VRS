<template>
    <view>
        <headerbar :title="title" background="#2195F3" title_color="#FFFFFF" :noback="true"/>
        <view class="sticky" :style="'top: ' + offsettop + 'px'">
            <!-- 有 审核员权限  -->
            <u-tabs v-if="(checkauthority('admin') && role === null) || (role === 'admin' && checkauthority('admin')) || (role === 'adminnone' && checkauthority('admin'))"
             :list="typelist" lineWidth="375rpx" @click="tabschange" :current="tabscurrent" activeStyle="color: #FFFFFF;"
              inactiveStyle="color: #A7D3F6;">
            </u-tabs>
            <u-tabs v-else-if="(checkauthority('guard') && role === null) || (role === 'guard' && checkauthority('guard'))"
             :list="guardtypelist" lineWidth="250rpx" @click="tabschange" :current="tabscurrent" activeStyle="color: #FFFFFF;"
              inactiveStyle="color: #A7D3F6;">
            </u-tabs>
            <view class="serchframe">
                <view class="searchinputframe margin_bottom24 flex">
                    <u-input fontSize="28rpx" v-model="searchparams.keyword" 
                    placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #C1C1C1;" 
                    placeholder="请输入车牌号/受访人/承运商搜索" border="none" inputAlign="left"></u-input>
                    <view class="searchIcon margin_left40">
                        <img class="searchIcon" src="/static/search.png"/>
                    </view>
                </view>
                <view class="timesearchframe flex">
                    <view class="timesearchbox flex" @click="starttimeshow = true">
                        <u-input fontSize="28rpx" v-model="searchparams.starttime" readonly
                        placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #C1C1C1;" 
                        placeholder="请选择开始时间" border="none" inputAlign="left"></u-input>
                        <img v-if="!searchparams.starttime" src="/static/timeselecticon.png" class="timeselecticon"/>
                        <view v-else class="flex_center clear" @click.stop="cleartime(0)">
                            <u-icon  class="close" name="close-circle-fill" size="36rpx" color="#c6c7cb"></u-icon>
                        </view>
                    </view>
                    <view class="timesearchbox leftauto flex" @click="endtimeshow = true">
                        <u-input fontSize="28rpx" v-model="searchparams.endtime" readonly
                        placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #C1C1C1;" 
                        placeholder="请选择结束时间" border="none" inputAlign="left"></u-input>
                        <img v-if="!searchparams.endtime" src="/static/timeselecticon.png" class="timeselecticon"/>
                        <view v-else class="flex_center clear" @click.stop="cleartime(1)">
                            <u-icon  class="close" name="close-circle-fill" size="36rpx" color="#c6c7cb"></u-icon>
                        </view>
                    </view>
                    <view class="searchbut leftauto flex_center" @click="getSearch">
                        <view class="buttext">查 询</view>
                    </view>
                </view>
            </view>
        </view>
        <view class="listframe">
            <view class="listitem margin_bottom16" v-for="item in list">
                <view class="listtopline flex margin_bottom32">
                    <view class="toplinetitle textmore">{{ item.createdName || '' }}</view>
                    <view v-if="item.enterStatus === 1 && isguardrole" class="leftauto flex">
                        <view class="statusbut statuscolor0 margin_right24">已入厂</view>
                        <view class="statusbut statuscolor3">未出厂</view>
                    </view>
                    <view v-else-if="item.enterStatus === 2 && isguardrole" class="leftauto flex">
                        <view class="statusbut statuscolor0 margin_right24">已入厂</view>
                        <view class="statusbut statuscolor3">已出厂</view>
                    </view>
                    <view v-else-if="item.status === 1" class="leftauto statusbut statuscolor0">已申请</view>
                    <view v-else-if="item.status === 2" class="leftauto statusbut statuscolor1">审核通过</view>
                    <view v-else-if="item.status === 3" class="leftauto statusbut statuscolor2">审核不通过</view>
                </view>
                <view class="listcontentline flex margin_bottom24">
                    <view class="leftkey width150">入厂日期</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ item.createdDate || '' }}</view>
                </view>
                <view class="listcontentline flex margin_bottom24">
                    <view class="leftkey width150">手机号</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ item.phone || '' }}</view>
                </view>
                <view class="listcontentline flex margin_bottom24">
                    <view class="leftkey width150">承运商</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ item.carrierName || '' }}</view>
                </view>
                <view class="listcontentline flex margin_bottom24">
                    <view class="leftkey width150">车牌号</view>
                    <view class="rightvalue maxwidth350 textmore leftauto">{{ item.carNumber || '' }}</view>
                </view>
                <view class="listbottomline" v-if="isguardrole && item.enterStatus === 0">
                    <view class="leftauto flex listbottomlinebox">
                        <view class="listbottombut flex_center" @click="tohistorydetail(item)">
                            <view class="listbottom_text">确认进厂</view>
                        </view>
                    </view>
                </view>
                <view class="listbottomline" v-else-if="isguardrole && item.enterStatus === 1">
                    <view class="leftauto flex listbottomlinebox">
                        <view class="listbottombut flex_center" @click="tohistorydetail(item)">
                            <view class="listbottom_text">确认出厂</view>
                        </view>
                    </view>
                </view>
                <view class="listbottomline" v-else-if="isguardrole && item.enterStatus === 2">
                    <view class="leftauto flex listbottomlinebox">
                        <view class="listbottombut flex_center" @click="tohistorydetail(item)">
                            <view class="listbottom_text">查看详情</view>
                        </view>
                    </view>
                </view>
                <view class="listbottomline" v-else>
                    <view class="leftauto flex listbottomlinebox">
                        <view class="listbottombut margin_right20 flex_center" @click="deleteclick(item)" v-if="item.status === 1">
                            <view class="listbottom_text">删除</view>
                        </view>
                        <view class="listbottombut flex_center" @click="tohistorydetail(item)">
                            <view class="listbottom_text">查看详情</view>
                        </view>
                    </view>
                </view>
            </view>
            <view class="flex_center margin_top24" v-if="pageloading">
                <u-loading-icon mode="circle"></u-loading-icon>
            </view>
        </view>
        <timeselect :show="starttimeshow" v-model="searchparams.starttime" @sure="starttimeshow = false" @close="starttimeshow = false" mode="date"/>
        <timeselect :show="endtimeshow" v-model="searchparams.endtime" @sure="endtimeshow = false" @close="endtimeshow = false" mode="date"/>
        <showmodal :showtitle="false" :show="deletemodal" content="确定删除车辆预约信息吗？" @sure="deletelist()" @cancel="deletemodal = false"></showmodal>
        <Empty v-if="list.length === 0 && !pageloading" src="/static/nolist.png" text="暂无数据哦~"></Empty>
        <bottombar active="history"/>
    </view>
</template>

<script>
import { guardlist, checklist, historylist, mylist, historydelete } from '@/api/ordercar.js'
import timeselect from '@/components/timeselect.vue'
export default {
    components: {
        timeselect
    },
    data() {
        return {
            imgUrl: "",
            // 页面标题
            title: '',
            // 分页查询
            pageSize: 10,
            pageIndex: 1,
            pageloading: false,
            pagetotle: 1,
            list: [],
            // 吸顶距离 单位px
            offsettop: 65,
            // 查询列表所用的角色身份
            role: null,
            // 由于门卫 列表展示页面不同 又因目前需求可以多角色 用来判断页面展示到地是那个类型列表
            isguardrole: false,
            // 搜索参数
            searchparams: {
                keyword: '',
                starttime: null,
                endtime: null,
                status: 0,
                postType: 1,
            },
            // tabs 默认展示下标
            tabscurrent: 0,
            // tabs 数组 审核员
            typelist: [{
                name: "未审核",
                id: 1
            },
            {
                name: "已审核",
                id: 2
            }],
            // tabs 数组 门卫
            guardtypelist: [{
                name: "未进厂",
                id: 0
            },
            {
                name: "已进厂",
                id: 1
            },
            {
                name: "已出厂",
                id: 2
            }],
            // 时间选择器 显隐
            starttimeshow: false,
            endtimeshow: false,
            // dialog提示
            deletemodal: false,
            // 删除弹出框记录 打算删除item信息
            deleteitem: null,
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        // 标题头 获取系统顶部高度自动算出
        let systemInfo = uni.getSystemInfoSync()
        this.statusBarHeight = systemInfo.statusBarHeight || 0
        this.offsettop = 45 + this.statusBarHeight
        this.title = '历史预约'
        
        let time = this.utils.timeFormat(new Date(), 'YYYY-MM-DD', 'date')
        this.searchparams.starttime = time
        this.searchparams.endtime = time
        // console.log(e)
        // role 角色 该参数时 home页面 跳转用于区分展示的参数
        // role 不存在的时候 相当于直接打开本页面
        if (e.role === 'undefined' || e.role === 'null' || !e.role) {
            this.role = null
        } else {
            this.role = e.role
        }
        // 默认 tabs 展示的下标
        if (e.tabscurrent === 'undefined' || e.tabscurrent === 'null' || !e.tabscurrent) {
            this.tabscurrent = 0
        } else {
            this.tabscurrent = parseInt(e.tabscurrent) || 0
        }
        // 如果有 role 角色再（是从 home 页面跳转过来时判断） 则按照角色搜索
        if (this.role) {
            if (this.role === 'admin') {
                // tabs 搜索条件默认值
                this.searchparams.status = this.typelist[this.tabscurrent || 0].id
            } else if (this.role === 'guard') {
                // tabs 搜索条件默认值 isguardrole 列表展示 门卫列表
                this.isguardrole = true
                this.searchparams.status = this.guardtypelist[this.tabscurrent || 0].id
            } else if (this.role === 'none') {
                this.searchparams.status = null
            } else if (this.role === 'adminnone') {
                // adminnone 的情况一般为 审核员 查看自己代提交时
                this.searchparams.status = this.typelist[this.tabscurrent || 0].id
                // 目前业务只用 审核员 有postType（代提交）的筛选条件
                this.searchparams.postType = 2
            }
        } else {
            // 直接打开本页面时
            // 没有role 按照 默认身份的优先级搜索 admin 审核 > guard 门卫 > 没角色
            if (this.checkauthority('admin')) {
                // tabs 搜索条件默认值
                this.searchparams.status = 1
            } else if (this.checkauthority('guard')) {
                // tabs 搜索条件默认值
                this.isguardrole = true
                this.searchparams.status = 0
            } else {
                this.searchparams.status = null
            }
        }
        this.getSearch()
    },
    onShow() {
        if (uni.getStorageSync("needToRefreshhistorylist")) {
            uni.setStorageSync("needToRefreshhistorylist", false)
            this.getSearch()
        }
    },
    // 下拉刷新  数据清空 页数归1 用search
	onPullDownRefresh() {
		//监听下拉刷新动作的执行方法，每次手动下拉刷新都会执行一次
		this.getSearch()
	},
	// 上拉加载  数据情况 页数+1 
	onReachBottom() {
		this.pageIndex++;
		if (this.pageIndex > this.pagetotle) {
			uni.hideNavigationBarLoading();
			uni.stopPullDownRefresh();  //停止下拉刷新动画
			uni.showToast({
				title: '暂无更多数据',
				icon: 'none'
			})
		} else {
		    this.getlist()
		}
	},  
    methods: {
        // 查询
        getSearch() {
            this.pageIndex = 1
            this.list = []
            this.getlist()
        },
        // 查询列表
        getlist() {
            // 如果有 role 角色再 则按照角色搜索
            if (this.role) {
                if (this.role === 'admin') {
                    this.getchecklist()
                } else if (this.role === 'guard') {
                    this.getguardlist()
                } else if (this.role === 'none') {
                    this.getmylist()
                } else if (this.role === 'adminnone') {
                    // adminnone 的时候 searchparams.postType = 2 查 审核员 的 代提交
                    this.getchecklist()
                }
            } else {
                if (this.checkauthority('admin')) {
                    // 审核员查审核
                    this.getchecklist()
                } else if (this.checkauthority('guard')) {
                    // 门卫查审核
                    this.getguardlist()
                } else {
                    // 非 审核人 非 门卫（司机）查
                    this.getmylist()
                }
            }
        },
        // 预约列表（历史）--门卫
        getguardlist() {
            let that = this
            this.pageloading = true
            let params = {
                pageSize: this.pageSize,
                pageIndex: this.pageIndex,
                queryCondition: this.searchparams.keyword,
                enterStatus: this.searchparams.status,
                startDate: this.searchparams.starttime,
                endDate: this.searchparams.endtime,
            }
            guardlist(params).then(res => {
                uni.hideNavigationBarLoading();
			    uni.stopPullDownRefresh();  //停止下拉刷新动画
                let dataRes = res.data || {}
                let datalist = dataRes.data || {}
                if (dataRes.code === 200) {
                    let list = datalist.list || []
                    that.list = that.list.concat(list)
                    that.pagetotle = datalist.totalPage || 1
                } else {
                    if (that.pageSize > 1) {
                        that.pageSize = that.pageSize - 1
                    }
                }
                that.pageloading = false
            })
        },
        // 预约列表（历史）--审核员
        getchecklist() {
            let that = this
            this.pageloading = true
            let params = {
                pageSize: this.pageSize,
                pageIndex: this.pageIndex,
                queryCondition: this.searchparams.keyword,
                anthStatus: this.searchparams.status,
                startDate: this.searchparams.starttime,
                endDate: this.searchparams.endtime,
                postType: this.searchparams.postType,
            }
            checklist(params).then(res => {
                uni.hideNavigationBarLoading();
			    uni.stopPullDownRefresh();  //停止下拉刷新动画
                let dataRes = res.data || {}
                let datalist = dataRes.data || {}
                if (dataRes.code === 200) {
                    let list = datalist.list || []
                    that.list = that.list.concat(list)
                    that.pagetotle = datalist.totalPage || 1
                } else {
                    if (that.pageSize > 1) {
                        that.pageSize = that.pageSize - 1
                    }
                }
                that.pageloading = false
            })
        },
        // 预约列表（历史）-- 非 门卫 / 审核员
        gethistorylist() {
            let that = this
            this.pageloading = true
            let params = {
                pageSize: this.pageSize,
                pageIndex: this.pageIndex,
                queryCondition: this.searchparams.keyword,
                startDate: this.searchparams.starttime,
                endDate: this.searchparams.endtime,
            }
            historylist(params).then(res => {
                uni.hideNavigationBarLoading();
			    uni.stopPullDownRefresh();  //停止下拉刷新动画
                let dataRes = res.data || {}
                let datalist = dataRes.data || {}
                if (dataRes.code === 200) {
                    let list = datalist.list || []
                    that.list = that.list.concat(list)
                    that.pagetotle = datalist.totalPage || 1
                } else {
                    if (that.pageSize > 1) {
                        that.pageSize = that.pageSize - 1
                    }
                }
                that.pageloading = false
            })
        },
        // 预约列表（历史）-- 非 门卫 / 审核员
        getmylist() {
            let that = this
            this.pageloading = true
            let params = {
                pageSize: this.pageSize,
                pageIndex: this.pageIndex,
                queryCondition: this.searchparams.keyword,
                startDate: this.searchparams.starttime,
                endDate: this.searchparams.endtime,
            }
            mylist(params).then(res => {
                uni.hideNavigationBarLoading();
			    uni.stopPullDownRefresh();  //停止下拉刷新动画
                let dataRes = res.data || {}
                let datalist = dataRes.data || {}
                if (dataRes.code === 200) {
                    let list = datalist.list || []
                    that.list = that.list.concat(list)
                    that.pagetotle = datalist.totalPage || 1
                } else {
                    if (that.pageSize > 1) {
                        that.pageSize = that.pageSize - 1
                    }
                }
                that.pageloading = false
            })
        },
        // 删除订单
        historydeleteapi() {
            let that = this
            let params = {
                applicationId: this.deleteitem && this.deleteitem.applicationId || null,
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
                    let deleteindex = -1;
                    for (let index = 0; index < that.list.length; index++ ) {
                        if (that.list[index].applicationId === that.deleteitem.applicationId){
                            deleteindex = index
                            break
                        }
                    }
                    if(deleteindex > -1){
                        that.list.splice(deleteindex,1);
                    }
                    uni.showToast({
                        title: '删除成功',
                        icon: 'success'
                    })
                } else {
                    uni.showToast({
                        title: dataRes.msg || '删除失败',
                        icon: 'error'
                    })
                }
                this.deleteitem = {}
                this.deletemodal = false
            })
        },
        // 点击删除按钮 记录按钮所属的list item
        deleteclick(item) {
            this.deleteitem = {}
            this.deleteitem = item
            this.deletemodal = true
        },
        // 删除按钮点击确认
        deletelist() {
            let deleteitem = this.deleteitem
            this.historydeleteapi()
            // this.deletemodal = false
        },
        // 强制刷新数据 不要删除该函数 再其他页面有调用本页面的该函数
        forceflash() {
            console.log(this.list)
            // this.list = this.list
            this.$forceUpdate()
        },
        // tabs点击事件
        tabschange(e) {
            this.searchparams.status = e.id
            // console.log(this.tabscurrent)
            this.getSearch()
        },
        // 跳转详情
        tohistorydetail(item) {
            if (item.applicationId) {
                uni.navigateTo({
                    url: '/pages/history/historydetail?applicationId=' + item.applicationId || null
                })
            } else {
                uni.showToast({
                    title: '未知错误无id',
                    icon: 'none'
                })
            }
        },
        // 情况时间选择数据
        cleartime(type) {
            if (type === 0) {
                this.searchparams.starttime = null
            } else {
                this.searchparams.endtime = null
            }
        },
    },
};
</script>

<style>
page{ 
    background-color:#F7F8FA;
}
</style>
<style lang="scss" scoped>
@import '@/styles/common.scss';
@import './historylist.scss';
.sticky {
    /* 兼容chorme */
    width: 100%;
    position: -webkit-sticky;
    position: sticky;
}

.serchframe {
    width: calc(100% - 48rpx);
    background: #FFFFFF;
    padding: 20rpx 24rpx 32rpx;
    .searchinputframe {
        width: calc(100% - 42rpx);
        padding: 14rpx 20rpx;
        border: 1rpx solid #D1D1D1;
        border-radius: 8rpx;
    }

    .searchIcon {
        width: 40rpx;
        height: 40rpx;
    }

    .timeselecticon {
        width: 24rpx;
        height: 24rpx;
    }
}

.timesearchframe {
    .timesearchbox {
        width: 244rpx;
        height: 64rpx;
        padding: 0 20rpx;
        background: #FFFFFF;
        border: 1rpx solid #D1D1D1;
        opacity: 1;
        border-radius: 8rpx;
    }

    .searchbut {
        width: 98rpx;
        height: 64rpx;
        background: #2196F3;
        opacity: 1;
        border-radius: 8rpx;

        .buttext {
            font-size: 24rpx;
            font-family: PingFangSC;
            font-weight: 400;
            color: #FFFFFF;
        }
    }

    .clear {
        height: 64rpx;
    }
}

::v-deep .u-tabs__wrapper__nav__line {
    background: rgb(171 210 242) !important;
    height: 4rpx !important;
    bottom: 2rpx !important;
}

::v-deep .u-tabs__wrapper__nav__item {
    flex: 1 !important;
    padding: 0 !important;
    background: #2195F3 !important;

    .u-tabs__wrapper__nav__item__text {
        font-size: 30rpx !important;
        font-family: FZHei-B01S;
        font-weight: 400;
    }
}
</style>
