<template>
    <view>
        <view class="img">
            <u-image :src="src" width="280rpx" height="280rpx"></u-image>
        </view>
        <view class="text">{{ text }}</view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            //图片路径
            // src: "",
            //七牛前缀
            qiUrl: "",
            //文案话语
            // text: "暂无消息内容哦~",
        };
    },
    mounted() {},
    methods: {},
    props: {
        src: {
            type: String,
            default: "http://rgdxjhghl.hd-bkt.clouddn.com/pic/empty.png",
        },
        text: {
            type: String,
            default: "暂无消息内容哦~",
        },
    },
};
</script>

<style lang="scss" scoped>
.img {
    display: flex;
    justify-content: center;
    margin-top: 16vh;
}
.text {
    display: flex;
    justify-content: center;
    margin-top: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
    color: #666666;
}
</style>
