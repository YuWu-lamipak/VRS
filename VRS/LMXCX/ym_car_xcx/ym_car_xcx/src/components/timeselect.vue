<!--
  // formdate 是基于 timeselect （u-time-pick 该组件） 写的 timeselect要新加入属性 formdate 也要加
 * @Author: yh
 * @Date: 2022 08 25
    属性
 * value  v-model String     时间值
 * mode           String     类型模式    input 输入 select 选择
 * placeholder    string     Inputplaceholder
 * minDate        Number     可选的最小时间
 * mode	          String     展示格式 mode=date为日期选择，mode=time为时间选择，mode=year-month为年月选择，
                             mode=datetime为日期时间选择  ( 默认 'datetime' )
-->

<template>
    <view>
      <u-popup :show="show" mode="bottom" @close="popclose()">
          <view class="poptitleline flex">
            <view class="leftbut" @click="popclose()">取消</view>
            <view class="rightbut leftauto" @click="surepop()">确认</view>
          </view>
          <view class="picker">
            <mydatetimepicker v-model="timedata" :minDate="minDate"  @change="timechange" :formatter="formatter" :mode="mode"/>
          </view>
      </u-popup>
    </view>
</template>

<script>
import mydatetimepicker from './my-view/my-datetime-picker/my-datetime-picker.vue'
export default {
  components: {
      mydatetimepicker
  },
  data() {
    return {
      qiUrl: '',
      // input 显示的值
      inputele: '',
      // picker 展示数组
      pickercolumns: [],
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
    show: {
      type: Boolean,
      default: () => false
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
        // this.show = false
        this.$emit('close')
      },
      // 确认pop
      surepop() {
        // this.show = false
        if (this.timedata) {
          if (this.mode === 'datetime') {
            this.inputele = this.utils.timeFormat(new Date(this.timedata), 'YYYY-MM-DD hh:mm:ss', 'date')
          } else if (this.mode === 'date') {
			  this.inputele = this.utils.timeFormat(new Date(this.timedata), 'YYYY-MM-DD', 'date')
		  }
        }
        this.$emit('sure', this.inputele)
        this.$emit('input', this.inputele)
      },
      // picker变动时
      timechange(e) {
        // console.log(e)
        // console.log(e.value)
        if (e.value) {
          this.timedata = e.value
        }
      },
      // 时间格式
      formatter(type, value) {
        if (type === "year") {
            return `${value}年`;
        }
        if (type === "month") {
            return `${value}月`;
        }
        if (type === "day") {
            return `${value}日`;
        }
        if (type === "hour") {
            return `${value}时`;
        }
        if (type === "minute") {
            return `${value}分`;
        }
        return value;
    },
  },

  watch: {
    value: {
      handler(newval, oldval) {
        if (newval) {
            this.inputele = newval
            // 苹果不支持 - 的日期时间
            this.timedata = new Date(newval.replace(/-/g, '/')).toString()
        } else {
            this.timedata = new Date().toString()
        }
      },
      immediate: true
    },
  }
};
</script>
<style lang='scss' scoped>
@import '@/styles/common.scss';

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