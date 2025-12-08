<template>
    <view>
        <headerbar :title="title" background="#2195F3" title_color="#FFFFFF" :selfclick="true" @return="backpage"/>
        <view class="page"> 
            <Empty src="/static/formsuccess.png" :text="Emptytext"></Empty>
        <!--<view class="resulttips">成功创建车辆预约号 {{orderId || '直接通过'}}</view>-->
            <view class="butframe flex_center">
                <view class="but flex_center" @click="backpage">
                    <view class="buttext">返回</view>
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
            // 车辆预约号
            orderId: null,
            delta: 1,
            title: '审核成功',
            Emptytext: '恭喜您，审核通过'
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        // 如果是从历史来到详情到审核则返回上上个页面
        // 如果是直接从详情到审核 返回上个页面
        // 由 第三方审核过来
        this.orderId = e.orderId
        if (this.orderId === undefined || this.orderId === 'undefined') {
            this.orderId = null
        }
        this.delta = parseInt(e.delta || 0)
        this.Emptytext = '恭喜您，审核通过~'
        this.title = '审核成功'
    },
    methods: {
        // headbar 的返回操作
        backpage() {
            uni.navigateBack({  //uni.navigateTo跳转的返回，默认1为返回上一级
                delta: this.delta
            });
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

.resulttips {
    width: 100%;
    font-size: 32rpx;
    font-family: PingFangSC;
    color: #808080;
    text-align: center;
    margin-top: 24rpx;
}
</style>
