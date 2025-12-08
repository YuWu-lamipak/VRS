<!--
 * @Author: yh
 * @Date: 2022 08 10
   属性
 * @show 展示/隐藏                   true/false   默认false
 * @overlay 显示遮罩层               true/false   默认true
 * @closeicon 显示右上角x图标        true/false   默认true
 * @round pop圆角                    Number   默认32
 * @title 标题                       String
 * @showtitle 是否展示标题           true/false   默认true
 * @content 展示内容                 String
 * @suretext 确认文字                String  默认确定
 * @suretextcolor 确认文字颜色       String  默认 #F7901F
 * @canceltext 取消文字              String  默认取消 
 * @canceltextcolor 取消文字颜色     String  默认 #5A5A5A
   事件
 * @sure 点击确认事件
 * @cancel 点击 取消/icon图标x 事件
-->
<template>
  <u-popup :show="show" :safeAreaInsetBottom="false" :round="round" mode="center" :overlay="overlay">
    <view class="dialog">
      <view class="iconline" v-if="closeicon">
        <view class="icon" @click="cancel">
          <img src="/static/close.png" class="iconimg"/>
        </view>
      </view>
      <view class="titleline" v-if="showtitle">
        <view class="title">{{title}}</view>
      </view>
      <view class="contentline">
        <view class="content">{{content}}</view>
      </view>
      <view class="butline flex">
        <view class="butboxleft flex_center" :style="'color:' + canceltextcolor" @click="cancel">{{canceltext}}</view>
        <view class="butboxright flex_center" :style="'color:' + suretextcolor" @click="sure">{{suretext}}</view>
      </view>
    </view>
	</u-popup>
</template>

<script>
export default {
  data() {
    return {
      qiUrl: ''
    };
  },
  props: {
    show: {
      type: Boolean,
      default: () => false
    },
    round: {
      type: Number,
      default: () => 16
    },
    title: {
      type: String,
      default: () => "温馨提示",
    },
    showtitle: {
      type: Boolean,
      default: () => true
    },
    content: {
      type: String,
      default: () => "请继续操作",
    },
    canceltext: {
      type: String,
      default: () => "取消",
    },
    canceltextcolor: {
      type: String,
      default: () => "#5A5A5A",
    },
    suretext: {
      type: String,
      default: () => "确定",
    },
    suretextcolor: {
      type: String,
      default: () => "#2196F3",
    },
    overlay: {
      type: Boolean,
      default: () => true
    },
    closeicon: {
      type: Boolean,
      default: () => true
    },
  },
  mounted() {
			this.qiUrl = this.qiNiuUrl;
	},
  
  methods: {
    sure() {
      this.$emit('sure');
    },
    cancel() {
      this.$emit('cancel');
    },
  }
};
</script>
<style lang='scss' scoped>
@import "@/styles/common.scss";
.dialog {
  width: 638rpx;
  padding-top: 56rpx;
}
.iconline {
  width: 100%;
}
.icon {
  width: 28rpx;
  height: 28rpx;
  margin: 0 56rpx 0 auto;
}
.iconimg {
  width: 28rpx;
  height: 28rpx;
}
.titleline {
  width: 100%;
}
.title {
  padding-top: 26rpx;
  font-size: 36rpx;
  font-family: PingFangSC;
  font-weight: bold;
  line-height: 46rpx;
  color: #333333;
  text-align: center;
}
.contentline {
  width: calc(100% - 112rpx);
  padding: 52rpx 56rpx 86rpx 56rpx;
}
.content {
  font-size: 30rpx;
  font-family: PingFangSC;
  font-weight: 400;
  line-height: 46rpx;
  color: #666666;
  text-align: center;
}

.butline {
  width: 100%;
  border-top: 1rpx solid #D5D5D5;
  .butboxleft {
    width: calc(50% - 1rpx);
    border-right: 1rpx solid #D5D5D5;
    height: 100rpx;
  }
  .butboxright {
    width: 50%;
    height: 100rpx;
  }
  .buttext {
    font-size: 32rpx;
    font-family: PingFangSC;
    font-weight: 400;
  }
}
</style>