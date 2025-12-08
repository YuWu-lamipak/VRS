<template>
    <view>
        <headerbar title="修改成功" background="#2195F3" title_color="#FFFFFF"/>
        <view class="page"> 
            <Empty src="/static/passwordsuccess.png" text="恭喜您，密码修改成功啦~"></Empty>
            <view class="butframe flex_center">
                <view class="but flex_center" @click="tomine()">
                    <view class="buttext">返回个人中心</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            imgUrl: "",
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
    },
    methods: {
        // 跳转预约成功页面
        tomine() {
            uni.reLaunch({
                url: '/pages/mine/mine'
            })
        }
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';
.page {
    padding-top: 70rpx;
}

.butframe {
    width: 100%;
    position: fixed;
    left: 0;
    bottom: 100rpx;

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
