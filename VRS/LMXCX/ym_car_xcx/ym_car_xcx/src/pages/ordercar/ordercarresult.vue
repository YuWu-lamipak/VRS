<template>
    <view>
        <headerbar :title="title" background="#2195F3" title_color="#FFFFFF" :selfclick="true" @return="backpage"/>
        <view class="page"> 
            <Empty src="/static/formsuccess.png" :text="Emptytext"></Empty>
            <!-- 分 按钮的原因 -->
            <!-- 当 是修改的 情况下 返回前页理论上是 列表页 -> 详情页 -> 修改页(跳转时销毁 不存在了) -> 本页  -->
            <view class="butframe flex_center" v-if="edit">
                <view class="but flex_center" @click="backpage">
                    <view class="buttext">查看详情</view>
                </view>
            </view>
            <!-- 分 按钮的原因 -->
            <!-- 当 是新增的 情况下 返回新增页的时候不能留有上次的数据 所以关闭了所有的页面跳转 -->
            <!-- 返回前页理论上是 新增页(跳转时销毁 不存在了) -> 本页 (再次跳转销毁 不存在) -> 详情页(点击修改) -> 新增/编辑页(编辑后销毁) -> 本页  -->
            <view class="butframe flex_center" v-else>
                <view class="but flex_center" @click="todetail">
                    <view class="buttext">查看详情</view>
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
            // 预约记录id
            applicationId: null,
            // 新增/编辑
            edit: false,
            title: '提交成功',
            Emptytext: '恭喜您，车辆预约信息提交成功哦~'
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        this.edit = e.edit === 'true'
        this.applicationId = e.applicationId
        if (this.edit) {
            this.Emptytext = '恭喜您，修改成功哦~'
            this.title = '修改成功'
        } else {
            this.Emptytext = '恭喜您，车辆预约信息提交成功哦~'
            this.title = '提交成功'
        }
    },
    methods: {
        // 跳转预约成功页面
        todetail() {
            uni.reLaunch({
                url: '/pages/history/historydetail?applicationId=' + this.applicationId + '&isadd=true'
            })
        },
        // 跳转预约成功页面
        toeditdetail() {
            uni.redirectTo({
                url: '/pages/history/historydetail?applicationId=' + this.applicationId
            })
        },
        // headbar 的返回操作
        backpage() {
            if (this.edit) {
                // 如果是编辑跳转来的 默认返回 理论上会返回到订单详情页面
                uni.navigateBack({
                    delta: 1,
                })
            } else {
                // 如何是新增跳转来的 为保证新增页面没有数据 关闭其页面跳转回去
                uni.reLaunch({
                    url: '/pages/ordercar/ordercar',
                })
            }
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
