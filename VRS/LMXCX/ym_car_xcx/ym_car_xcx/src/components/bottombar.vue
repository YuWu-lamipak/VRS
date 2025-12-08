<!--
 * @Author: yh
 * @Date: 2022 08 25
   该组件为 2022 08 25 为实现 底部tabbar上 展示内容数量不同
 * 本组件实际高度  76rpx +  24rpx(padding) + iossave(苹果底部安全距离) 
 * 由于组件内部写了安全距离 该组件的使用一定要放在页面最下面 否则会有影响
    属性
 * active   String  Bar数组里面参数的key值 相同则 为选中的bar
-->

<template>
    <view>
        <!-- 安全距离 savebox 是 bottombar的高度 iossavebox是苹果安全距离的高度 保证页面展示不被挡住-->
        <view class="savebox"></view>
        <view class="iossavebox"></view>
        <view class="bottombarframe"> 
            <view class="bottombar iosbottom flex" v-if="bars.length > 0">
                <view class="box" v-for="item in bars">
                    <view @click="itemclick(item)"> 
                        <view class="flex_center iconframe margin_bottom4">
                            <img class="icon" :src="item.active ? item.activeicon : item.icon"/>
                        </view>
                        <view :class="'flex_center bartext ' + (item.active ? 'active' : '')">
                            {{item.bartext}}
                        </view>
                    </view>
                </view>
            </view>
            <!-- 当存在iossavebox苹果安全距离的高度时 填充底部颜色与高度 -->
            <view class="iossavebox"></view>
        </view>
    </view>
</template>

<script>
import jwt from "@/utils/auth/jwt.js";
export default {
  data() {
    return {
      qiUrl: '',
      bars: []
    };
  },
  props: {
    active: {
      type: String,
      default: () => 'ordercar'
    },
  },
  mounted() {
    // 业务要求 司机不可以预约
    // 先判断是否是司机
    if (this.checkuserType('driver')) {
        this.bars = [{
            key: 'home',
            path: '/pages/home/home',
            active: false,
            icon: '/static/tabbar/home.png',
            activeicon: '/static/tabbar/home_active.png',
            bartext: '首页'
        },
        // {
        //     key: 'ordercar',
        //     path: '/pages/ordercar/ordercar',
        //     active: false,
        //     icon: '/static/tabbar/ordercar.png',
        //     activeicon: '/static/tabbar/ordercar_active.png',
        //     bartext: '车辆预约'
        // },
        {
            key: 'history',
            path: '/pages/history/history',
            active: false,
            icon: '/static/tabbar/history.png',
            activeicon: '/static/tabbar/history_active.png',
            bartext: '预约信息'
        },{
            key: 'mine',
            path: '/pages/mine/mine',
            active: false,
            icon: '/static/tabbar/mine.png',
            activeicon: '/static/tabbar/mine_active.png',
            bartext: '个人中心'
        }]
    }
    // 判断 权限（是否有审核员权限 是否有门卫权限） （审核员权限 优先级 大于 门卫）
    else if (this.checkauthority('admin')) {
        this.bars = [{
            key: 'home',
            path: '/pages/home/home',
            active: false,
            icon: '/static/tabbar/home.png',
            activeicon: '/static/tabbar/home_active.png',
            bartext: '首页'
        },{
            key: 'ordercar',
            path: '/pages/ordercar/ordercar',
            active: false,
            icon: '/static/tabbar/ordercar.png',
            activeicon: '/static/tabbar/ordercar_active.png',
            bartext: '车辆预约'
        },{
            key: 'history',
            path: '/pages/history/history',
            active: false,
            icon: '/static/tabbar/history.png',
            activeicon: '/static/tabbar/history_active.png',
            bartext: '预约信息'
        },{
            key: 'mine',
            path: '/pages/mine/mine',
            active: false,
            icon: '/static/tabbar/mine.png',
            activeicon: '/static/tabbar/mine_active.png',
            bartext: '个人中心'
        }]
    } 
    // 门卫权限 不要求 预约功能
    else if (this.checkauthority('guard')) {
        this.bars = [{
            key: 'home',
            path: '/pages/home/home',
            active: false,
            icon: '/static/tabbar/home.png',
            activeicon: '/static/tabbar/home_active.png',
            bartext: '首页'
        },{
            key: 'history',
            path: '/pages/history/history',
            active: false,
            icon: '/static/tabbar/history.png',
            activeicon: '/static/tabbar/history_active.png',
            bartext: '预约信息'
        },{
            key: 'mine',
            path: '/pages/mine/mine',
            active: false,
            icon: '/static/tabbar/mine.png',
            activeicon: '/static/tabbar/mine_active.png',
            bartext: '个人中心'
        }]
    } else {
        this.bars = [{
            key: 'home',
            path: '/pages/home/home',
            active: false,
            icon: '/static/tabbar/home.png',
            activeicon: '/static/tabbar/home_active.png',
            bartext: '首页'
        },
        {
            key: 'ordercar',
            path: '/pages/ordercar/ordercar',
            active: false,
            icon: '/static/tabbar/ordercar.png',
            activeicon: '/static/tabbar/ordercar_active.png',
            bartext: '车辆预约'
        },
        {
            key: 'history',
            path: '/pages/history/history',
            active: false,
            icon: '/static/tabbar/history.png',
            activeicon: '/static/tabbar/history_active.png',
            bartext: '预约信息'
        },{
            key: 'mine',
            path: '/pages/mine/mine',
            active: false,
            icon: '/static/tabbar/mine.png',
            activeicon: '/static/tabbar/mine_active.png',
            bartext: '个人中心'
        }]
    }
    
    let bar = this.bars.find(item=>item.key===this.active)
    if (bar) {
        bar.active = true
    }
  },
  
  methods: {
    itemclick(item) {
        uni.reLaunch({
            url: item.path
        })
    }
  }
};
</script>
<style lang='scss' scoped>
@import '@/styles/common.scss';
.bottombarframe {
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    background: #FFFFFF;
    z-index: 99;
}

.bottombar {
    width: 100%;
    padding: 12rpx 0;
    height: 76rpx;
    box-shadow: 3rpx 0px 8rpx rgba(0,0,0,0.08);

    .box {
        flex: 1;
    }

    .active {
        color: #2196F3;
    }

    .iconframe {
        width: 100%;

        .icon {
            width: 44rpx;
            height: 44rpx;
        }
    }

    .bartext {
        font-size: 20rpx;
        font-family: PingFangSC;
        font-weight: 400;
    }
}

.savebox {
    width: 100%;
    height: 100rpx;
}
</style>