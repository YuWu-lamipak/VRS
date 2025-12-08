<template>
    <view>
        <headerbar title="个人中心" background="#2195F3" title_color="#FFFFFF" :noback="true"/>
        <view class="page"> 
            <img class="bgimg" src="/static/my_bg.png"/>
            <view class="mineInfoframe flex_center margin_bottom12">
                <view> 
                    <view class="flex_center margin_bottom6"> 
                        <img class="defaultIcon" :src="userinfo.avatar ? imgUrl + userinfo.avatar : '/static/dafaultuser.png'"/>
                    </view>
                    <view class="username flex_center">{{ userinfo.nickName || '暂无昵称'}}</view>
                </view>
            </view>
            <view class="mylistoutframe">
                <view class="mylistframe">
                    <view class="listitemframe flex" @click="tomineInfo()">
                        <view class="listitem flex">
                            <view class="listicon margin_right40">
                                <img class="listicon" src="/static/mine_userInfo.png"/>
                            </view>
                            <view class="leftkey">个人信息</view>
                            <view class="leftauto righticon">
                                <img class="righticon" src="/static/minearraw.png"/>
                            </view>
                        </view>
                    </view>
                    <view class="listitemframe flex" @click="topassword()">
                        <view class="listitem flex">
                            <view class="listicon margin_right40">
                                <img class="listicon" src="/static/mine_password.png"/>
                            </view>
                            <view class="leftkey">修改密码</view>
                            <view class="leftauto righticon">
                                <img class="righticon" src="/static/minearraw.png"/>
                            </view>
                        </view>
                    </view>
                    <view class="listitemframe flex" @click="reloginmodal = true">
                        <view class="listitem flex">
                            <view class="listicon margin_right40">
                                <img class="listicon" src="/static/mine_relogin.png"/>
                            </view>
                            <view class="leftkey">注销登录</view>
                            <view class="leftauto righticon">
                                <img class="righticon" src="/static/minearraw.png"/>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <showmodal :showtitle="false" :show="reloginmodal" content="确定注销登录吗？" @sure="tologin" @cancel="reloginmodal = false"></showmodal>
        <bottombar active="mine"/>
  </view>
</template>

<script>
import { userinfo } from '@/api/user.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 显示/隐藏 注销 dialog
            reloginmodal: false,
            // 用户信息
            userinfo: {},
        };
    },
    onLoad() {
        this.imgUrl = this.globalUrl;
        this.getuserInfo()
    },
    onShow() {
        if (uni.getStorageSync("needToRefreshmine")) {
            uni.setStorageSync("needToRefreshmine", false)
            this.getuserInfo()
        }
    },
    methods: {
        // 获取用户信息 默认填写承运商
        getuserInfo() {
            let that = this
            userinfo().then(res => {
                let dataRes = res.data || {}
                let userinfo = dataRes.data || {}
                that.userinfo = userinfo
            })
        },
        // 跳转预约成功页面
        toordercarresult() {
            uni.navigateTo({
                url: '/pages/ordercar/ordercarresult?edit=' + this.edit
            })
        },
        // 跳转到个人信息
        tomineInfo() {
            uni.navigateTo({
                url: '/pages/mine/mineInfo',
            })
        },
        // 跳转到修改密码
        topassword() {
            uni.navigateTo({
                url: '/pages/mine/minepassword',
            })
        },
        // 注销登陆 跳转到登陆页面
        tologin() {
            uni.reLaunch({
                url: '/pages/login/login',
            })
        }
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
.page {
    width: 100%;
    position: relative;
}
.bgimg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 328rpx;
    z-index: -1;
}

.mineInfoframe {
    width: 100%;
    padding: 36rpx 0 12rpx;

    .defaultIcon {
        width: 124rpx;
        height: 124rpx;
        border-radius: 50%;
    }

    .username {
        font-size: 30rpx;
        font-family: PingFangSC;
        font-weight: 400;
        color: #FFFFFF;
    }
}

.mylistoutframe {
    width: calc(100% - 64rpx);
    padding: 0 32rpx;
}

.mylistframe {
    width: 100%;
    border-radius: 12rpx;
    background: #FFFFFF;
    padding: 12rpx 0 40rpx;

    .listitemframe {
        width: calc(100% - 96rpx);
        padding: 0 48rpx;
        
        .listitem {
            width: 100%;
            height: 112rpx;
            border-bottom: 1rpx solid #EAEAEA;
        }

        .listicon {
            width: 48rpx;
            height: 48rpx;
        }

        .leftkey {
            font-size: 32rpx;
            font-family: PingFangSC;
            font-weight: 400;
            color: #393939;
        }

        .righticon {
            width: 24rpx;
            height: 24rpx;
        }
    }
}
</style>
