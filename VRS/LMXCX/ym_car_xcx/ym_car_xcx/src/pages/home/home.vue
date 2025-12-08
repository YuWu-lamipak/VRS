<template>
    <view>
        <headerbar title="首页" background="#2195F3" title_color="#FFFFFF" :noback="true"/>
        <view class="page"> 
            <img class="home_ordercar margin_bottom24" src="/static/home_ordercar.png"/>
            <!-- 审核部分判断  有审核员角色 展示  没有角色 展示 -->
            <!-- v-if="checkauthority('admin') || (!checkauthority('admin') && !checkauthority('guard'))" -->
            <view class="flex margin_bottom16" v-if="checkauthority('admin') || (!checkauthority('admin') && !checkauthority('guard'))">
                <view class="home_ordercheck relative" @click="tohistory('admin', 0)">
                    <img class="home_ordercheck absolute" src="/static/home_ordercheck.png"/>
                    <view class="imgInfoframe">
                        <view class="margin_bottom8 text">今日待审核</view>
                        <view class="num">{{ checkerstatisticsInfo.unaudited || 0 }}</view>
                    </view>
                </view>
                <view class="leftauto home_ordercheck relative" @click="tohistory('admin', 1)">
                    <img class="home_ordercheck absolute" src="/static/home_ordercheck.png"/>
                    <view class="imgInfoframe">
                        <view class="margin_bottom8 text">今日已审核</view>
                        <view class="num">{{ checkerstatisticsInfo.reviewed || 0 }}</view>
                    </view>
                </view>
            </view>
            <!-- 门卫部分判断  有门卫角色 展示  没有角色 展示 -->
            <view class="flex margin_bottom16" v-if="checkauthority('guard')">
                <view class="home_nocome relative" @click="tohistory('guard', 0)">
                    <img class="home_nocome absolute" src="/static/home_nocome.png"/>
                    <view class="imgInfoframe">
                        <view class="margin_bottom8 nocometext">今日未进厂</view>
                        <view class="nocomenum">{{ guardstatisticsInfo.notIn || 0 }}</view>
                    </view>
                </view>
                <view class="leftauto home_nocome">
                    <view class="home_come relative" @click="tohistory('guard', 1)">
                        <img class="home_come absolute" src="/static/home_come.png"/>
                        <view class="imgInfoframe">
                            <view class="margin_bottom8 text">今日已进厂</view>
                            <view class="num">{{ guardstatisticsInfo.in || 0 }}</view>
                        </view>
                    </view>
                    <view class="home_out relative margin_top16" @click="tohistory('guard', 2)">
                        <img class="home_out absolute" src="/static/home_out.png"/>
                        <view class="imgInfoframe">
                            <view class="margin_bottom8 text">今日已出厂</view>
                            <view class="num">{{ guardstatisticsInfo.out || 0 }}</view>
                        </view>
                    </view>
                </view>
            </view>
            <!-- 车辆代预约 -->
            <img v-if="checkauthority('admin')" class="home_orderInfo margin_bottom16" src="/static/home_orderInfo_admin.png" @click="tohistory_adminmy()"/>
            <img class="home_orderInfo" src="/static/home_orderInfo.png" @click="tohistory()"/>
            <!-- <img class="home_orderInfo" src="/static/home_orderInfo.png" @click="toordercar()"/> -->
        </view>
        <bottombar active="home"/>
    </view>
</template>

<script>
import jwt from "@/utils/auth/jwt.js";
import { login } from "@/api/login.js";
import { guardstatistics, checkerstatistics, selfstatistics } from '@/api/ordercar.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 统计信息 门卫
            guardstatisticsInfo: {},
            // 统计信息 审核
            checkerstatisticsInfo: {},
        };
    },
    onLoad() {
        this.imgUrl = this.globalUrl;
    },
    onShow() {
        if (this.checkauthority('guard')) {
            this.getguardstatistics()
        }
        if (this.checkauthority('admin')) {
            this.getcheckerstatistics()
        } else if ((!this.checkauthority('admin') && !this.checkauthority('guard'))) {
            this.getselfstatistics()
        }
    },
    methods: {
        // 获取统计信息 门卫
        getguardstatistics() {
            let that = this
            guardstatistics().then(res => {
                let dataRes = res.data || {}
                let guardstatisticsInfo = dataRes.data || {}
                that.guardstatisticsInfo = guardstatisticsInfo
            })
        },
        // 获取统计信息 审核
        getselfstatistics() {
            let that = this
            selfstatistics().then(res => {
                let dataRes = res.data || {}
                // console.log(dataRes)
                let checkerstatisticsInfo = dataRes.data || {}
                that.checkerstatisticsInfo = checkerstatisticsInfo
            })
        },
        // 获取统计信息 本人
        getcheckerstatistics() {
            let that = this
            checkerstatistics().then(res => {
                let dataRes = res.data || {}
                // console.log(dataRes)
                let checkerstatisticsInfo = dataRes.data || {}
                that.checkerstatisticsInfo = checkerstatisticsInfo
            })
        },
        // 跳转预约页面
        toordercar() {
            if (this.checkuserType('driver')) {
                return
            }
            if ((!this.checkauthority('guard')) || (this.checkauthority('guard') && this.checkauthority('admin'))) {
                uni.redirectTo({
                    url: '/pages/ordercar/ordercar'
                })
            }
        },
        // 跳转预约信息页面无角色区分等等
        tohistoryno() {
            uni.reLaunch({
                url: '/pages/history/history'
            })
        },
        // 跳转预约信息
        tohistory(role, tabscurrent) {
            // role 是指定身份
            // role admin 审核 身份有两种情况 当 有审核员角色是 role是admin 当没有角色时 role是none
            if (role === 'admin') {
                if (!this.checkauthority('admin') && !this.checkauthority('guard')) {
                    role = 'none'
                }
            }
            uni.redirectTo({
                url: '/pages/history/history?role=' + role + '&tabscurrent=' + tabscurrent
            })
        },
        // 跳转预约信息 审核人查看自己的预约记录
        tohistory_adminmy() {
            // 需求 审核员点击时 查看的是自己的预约列表 而不是审核的预约列表
            uni.redirectTo({
                url: '/pages/history/history?role=' + 'adminnone'
            })
        },
        // 跳转今天待审核
        tohistorytoday(role, tabscurrent) {
            // role 是指定身份
            // role admin 审核 身份有两种情况 当 有审核员角色是 role是admin 当没有角色时 role是none
            if (role === 'admin') {
                if (!this.checkauthority('admin') && !this.checkauthority('guard')) {
                    role = 'none'
                }
            }
            uni.redirectTo({
                url: '/pages/history/historytoday?role=' + role + '&tabscurrent=' + tabscurrent
            })
        },
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';
.page {
    padding: 16rpx;
}

.home_ordercar {
    width: 100%;
    height: 310rpx;
}

.home_nocome {
    width: 352rpx;
    height: 396rpx;
}

.home_out{
    width: 352rpx;
    height: 190rpx;
}

.home_come {
    width: 352rpx;
    height: 190rpx;
}

.home_orderInfo {
    width: 100%;
    height: 260rpx;
}

.home_ordercheck {
    width: 352rpx;
    height: 190rpx;
}

.relative {
    position: relative;
}

.absolute {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
}

.imgInfoframe {
    padding: 42rpx 0 0 28rpx;
    .nocometext {
        font-size: 32rpx;
        font-family: PingFangSC;
        font-weight: 600;
        color: #0A82E3;
        height: 45rpx;
        line-height: 45rpx;
    }

    .nocomenum {
        font-size: 48rpx;
        font-family: PingFangSC;
        font-weight: 600;
        color: #333333;
        height: 68rpx;
        line-height: 68rpx;
    }

    .text {
        font-size: 28rpx;
        font-family: PingFangSC;
        font-weight: 500;
        color: #0A82E3;
        height: 40rpx;
        line-height: 40rpx;
    }

    .num {
        font-size: 44rpx;
        font-family: PingFangSC;
        font-weight: 600;
        color: #333333;
        height: 62rpx;
        line-height: 62rpx;
    }
}
</style>
