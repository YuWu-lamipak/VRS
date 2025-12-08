<!--
 * @Author: yh
 * @Date: 2022 08 10
   该组件为 2022 08 10 为实现 navbar上 title为 时间icon图片 加 时间倒计时。mode是为了预留其他情况
 * 本组件实际高度 offsettop = 45px + statusBarHeight  其中45px为app 小程序 navbar固定高度
 * 特殊样式的头请使用mode 新增一个模式复制来改
    属性
 * mode           模式 'time' (显示带有时间图标的模式 ) 模式 'img' 无背景色 无标题
 * title          string     标题
 * showtitle      true/false 是否展示标题
 * noback         true/false 是否展示返回图标
 * background     string(background="#FFFFFF")背景颜色   #FFFFFF默认
 * title_color    string(title_color="#333333")标题文字颜色 #333333默认
 * opacity        Number 透明度 
 * nobottomborder true/false true则没有底部边框线 默认为true
 * 
 * selfclick      true/false true时点击返回按钮将触发回传函数(return)
 *                使用方式 :selfclick="true" @return="你的函数名"
   事件
 * @return 返回点击事件
-->

<template>
    <view :style="'margin-bottom: ' + offsettop + 'px;'">
        <view>
            <view :class="nobottomborder ? 'bgdnoborder' : 'bgd'" :style="'opacity:' + opacity + '; height:' + offsettop + 'px; background: ' + background  + ';'">
                <view  class="navtitle flex_center" :style="'margin-top: ' + statusBarHeight + 'px; ' + 'height:' + (offsettop - statusBarHeight) + 'px;'">
                    <view v-if="!noback" @click="goback" class="arrawframe flex" :style="'height:' + (offsettop - statusBarHeight) + 'px'">
                      <img class="arrawicon" src="/static/return.png"/>
                    </view>
                    <view v-if="showtitle" class="navBars" :style="'color: ' + title_color">{{title}}</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
  data() {
    return {
      qiUrl: '',
      // 顶部状态栏高度
      statusBarHeight: 0,
      offsettop: 89
    };
  },
  props: {
    title: {
      type: String,
      default: () => '标题'
    },
    showtitle: {
      type: Boolean,
      default: () => true
    },
    mode: {
      type: String,
      default: () => ''
    },
    noback: {
      type: Boolean,
      default: () => false
    },
    background: {
      type: String,
      default: () => '#FFFFFF'
    },
    title_color: {
      type: String,
      default: () => '#333333'
    },
    opacity: {
      type: Number,
      default: () => 1
    },
    nobottomborder: {
      type: Boolean,
      default: () => true
    },
    selfclick: {
      type: Boolean,
      default: () => false
    },
    iconsrc: {
      type: String,
      default: () => '/icon/return.png'
    },
  },
  created() {
	  this.qiUrl = this.qiNiuUrl;
    // 标题头 获取系统顶部高度自动算出
    let systemInfo = uni.getSystemInfoSync()
    this.statusBarHeight = systemInfo.statusBarHeight || 0
    this.offsettop = 45 + this.statusBarHeight
  },
  
  methods: {
    goback: function () {
      if (this.selfclick) {
        this.$emit('return', null)
      } else {
        let pages = getCurrentPages() // 获取栈实例
        if (pages && pages.length > 1) {
            uni.navigateBack({
                delta: 1,
            })
        } else {
          // let page = pages[pages.length - 1] // 获取当前页面的数据，包含页面路由
          // let prevPage = pages[pages.length - 2] // 获取上个页面的数据，包含页面路由
          uni.reLaunch({
            url: '/pages/ordercar/ordercar',
          })
        }
      }
    }
  }
};
</script>
<style lang='scss' scoped>
@import '@/styles/common.scss';
.arrawframe {
  width: 44rpx;
  position: absolute;
  left: 24rpx;
  top: 0;
}

.arrawicon {
  width: 44rpx;
  height: 44rpx;
}

.navBars {
  font-size: 36rpx;
  font-family: PingFang SC;
  font-weight: bold;
  line-height: 40rpx;
  margin-left: 16rpx;
}

.navtitle{
  position: relative;
}

.bgd {
  position: fixed;
  width: 100%;
  left: 0rpx;
  top: 0rpx;
  z-index: 99 !important;
  border-bottom: 2rpx solid #E7E7E7;
}

.bgdnoborder {
  position: fixed;
  width: 100%;
  left: 0rpx;
  top: 0rpx;
  z-index: 99 !important;
}

// 时间
.timeicon {
  width: 36rpx;
  height: 36rpx;
}

// mode img 是的背景类
.modeimgbg {
  position: absolute;
  width: 100%;
  left: 0rpx;
  top: 0rpx;
  z-index: 99 !important; 
}
</style>