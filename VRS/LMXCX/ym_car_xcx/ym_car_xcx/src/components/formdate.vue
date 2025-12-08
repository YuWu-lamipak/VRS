<!--
  // 理论上 要支持 timeselect （u-time-pick） 的所有  所以timeselect要新加入属性 这里也加入
 * @Author: yh
 * @Date: 2022 08 25
    属性
 * value  v-model String     Input 绑定值
 * mode           String     类型模式    input 输入 select 选择
 * placeholder    string     Inputplaceholder
 * minDate        Number     可选的最小时间
 * mode	          String     展示格式 mode=date为日期选择，mode=time为时间选择，mode=year-month为年月选择，
                             mode=datetime为日期时间选择  ( 默认 'datetime' )
 * 
-->

<template>
    <view>
      <view class="flex" @click="pop = true">
          <u-input v-model="inputele" placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #999999;"
            :placeholder="placeholder" border="none" inputAlign="right" readonly>
          </u-input>
          <img class="icon margin_left12" src="/static/selectable.png"/>
      </view>
      <timeselect :show="pop" :minDate="minDate" v-model="timedata" @sure="surepop" @close="popclose()" :mode="mode"/>
    </view>
</template>

<script>
import timeselect from './timeselect.vue'
export default {
  components: {
      timeselect
  },
  data() {
    return {
      qiUrl: '',
      // 是否展示pop选框
      pop: false,
      // input 显示的值
      inputele: '',
      // picker 临时选择的值
      timedata: null
    };
  },
  props: {
    value: {
      type: String,
      default: () => ''
    },
    mode: {
      type: String,
      default: () => 'datetime'
    },
    placeholder: {
      type: String,
      default: () => '请选择'
    },
    // 可选的最小时间
    minDate: {
        type: Number,
        // 最小默认值为前10年
        default: uni.$u.props.datetimePicker.minDate
    },
  },
  mounted() {
	  this.qiUrl = this.qiNiuUrl;
  },
  
  methods: {
      // 关闭pop
      popclose() {
        this.pop = false
      },
      // 确认pop
      surepop(inputele) {
        this.pop = false
        // if (this.timedata) {
        //   this.inputele = this.utils.timeFormat(new Date(this.timedata), 'YYYY-MM-DD hh:mm:ss', 'date')
        // }
		    this.inputele = inputele
        this.$emit('sure', inputele)
        this.$emit('input', inputele)
      },
      // picker变动时
      timechange(e) {
        // console.log(e)
        // console.log(e.value)
        if (e.value) {
          this.timedata = e.value
        }
      },
  },

  watch: {
    value: {
      handler(newval, oldval) {
        if (newval) {
            this.inputele = newval
            // 苹果不支持 - 的日期时间
            this.timedata = new Date(newval.replace(/-/g, '/')).toString()
        }
      },
      immediate: true
    },
  }
};
</script>
<style lang='scss' scoped>
@import '@/styles/common.scss';

.icon {
  width: 24rpx;
  height: 24rpx;
}

.poptitleline {
  width: 690rpx;
  padding: 0 30rpx;
  height: 98rpx;
  background: #F1F1F1;

  .leftbut {
    font-size: 28rpx;
    font-family: FZHTJW--GB1-0;
    color: #212121;
  }

  .rightbut {
    font-size: 28rpx;
    font-family: FZHTJW--GB1-0;
    color: #2196F3;
  }
}
</style>