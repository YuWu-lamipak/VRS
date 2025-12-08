<!--
 * @Author: yh
 * @Date: 2022 08 25
    属性
 * value  v-model String Input 绑定值
 * mode   String  类型模式    input 输入 select 选择
 * apikey String  根据接口请求数据
 * params Objcet  apikey存在时 根据接口请求数据 的参数
 * type   String  Input类型时生效  同input标签的type
 * placeholder    string     Inputplaceholder
 * disabled        Boolean    是/否 可编辑
 * columns        Array      选择框的数组
 * 
-->

<template>
    <view>
      <view class="flex" v-if="mode === 'input'">
        <!-- customStyle="box-shadow: 2px 2px #EAEAEA;" -->
          <u-input v-model="inputele" placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #999999;" 
          @change="inputchange" :maxlength="maxlength" :placeholder="placeholder" border="none" inputAlign="right" :readonly="disabled" :type="type">
          </u-input>
      </view>
      <view class="flex" v-if="mode === 'textarea'">
          <textarea
              :placeholder="placeholder"
              v-model="inputele"
              class="textareainput"
              :maxlength="maxlength"
              :auto-height="true"
              placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #999999;"
              @input="textareachange"
              :disabled="disabled"
            />
      </view>
      <view class="flex" @click="openpop()" v-else-if="mode === 'select'">
          <u-input v-model="inputele" placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #999999;"
            :placeholder="placeholder" border="none" inputAlign="right" readonly>
          </u-input>
          <img v-if="!disabled" class="icon margin_left12" src="/static/selectable.png"/>
      </view>
      <u-popup :show="pop" mode="bottom" @close="popclose()">
          <view class="poptitleline flex">
            <view class="leftbut" @click="popclose()">取消</view>
            <view class="rightbut leftauto" @click="sureoop()">确认</view>
          </view>
          <view class="picker">
            <mypicker v-if="pickercolumns.length > 0 && !loading" :defaultIndex="defaultIndex" :columns="pickercolumns" keyName="nikName" @change="columnschange" :loading="loading"/>
            <view v-else class="empty">暂无数据哦~</view>
          </view>
      </u-popup>
    </view>
</template>

<script>
import mypicker from './my-view/my-picker/my-picker.vue'
import { getUserList, getCheckerList, getReasonList } from '@/api/ordercar.js'
export default {
  components: {
      mypicker
  },
  data() {
    return {
      qiUrl: '',
      // 是否展示pop选框
      pop: false,
      // input 显示的值
      inputele: '',
      // picker 展示数组
      pickercolumns: [],
      // picker 临时选择的值
      pickerdata: null,
      // 加载
      loading: false,
      // 各列的默认索引
      defaultIndex: []
    };
  },
  props: {
    value: {
      type: String,
      default: () => ''
    },
    apikey: {
      type: String,
      default: () => null
    },
    params: {
      type: Object,
      default: () => null
    },
    mode: {
      type: String,
      default: () => 'input'
    },
    type: {
      type: String,
      default: () => 'text'
    },
    placeholder: {
      type: String,
      default: () => '请输入'
    },
    disabled: {
      type: Boolean,
      default: () => false
    },
    columns: {
      type: Array,
      default: () => []
    },
    maxlength: {
      type: Number,
      default: () => 200
    },
  },
  mounted() {
	  this.qiUrl = this.qiNiuUrl;
    if (this.apikey) {
      this.getcolumnsbyapi(this.apikey)
    }
  },
  methods: {
      // 根据api获取数组
      getcolumnsbyapi(apikey) {
        this.loading = true
        if (apikey === 'carrier') {
          apikey = '01'
          this.getUserListapi(apikey, this.params)
        } else if (apikey === 'driver') {
          apikey = '02'
          this.getUserListapi(apikey, this.params)
        } else if (apikey === 'checker') {
          this.getCheckerListapi(apikey, this.params)
        } else if (apikey === 'reason') {
          this.getReasonListapi()
        }
      },
      // 司机、承运商列表
      getUserListapi(apikey,params) {
        let that = this
        let data = {
          ...params,
          userType: apikey
        }
        // 调用接口相当于整个组件重新刷新 关键数据清空 避免回传以往数据
        that.pickercolumns = []
        that.pickerdata = null
        that.inputele = ''
        getUserList(data).then(res => {
          let dataRes = res.data || {}
          if (dataRes.code === 200) {
            let resdata = dataRes.data || []
            let pickercolumns = []
            pickercolumns.push(resdata)
            that.pickercolumns = pickercolumns
            that.defaultval(that.value)
          }
          that.loading = false
        })
      },
      // 审核人列表
      getCheckerListapi(apikey,params) {
        let that = this
        let data = {
          // ...params,
          roleId: 144
        }
        // 调用接口相当于整个组件重新刷新 关键数据清空 避免回传以往数据
        that.pickercolumns = []
        that.pickerdata = null
        that.inputele = ''
        getCheckerList(data).then(res => {
          let dataRes = res.data || {}
          if (dataRes.code === 200) {
            let resdata = dataRes.data || []
            let pickercolumns = []
            pickercolumns.push(resdata)
            that.pickercolumns = pickercolumns
            that.defaultval(that.value)
          }
          that.loading = false
        })
      },
      // 获取原因列表
      getReasonListapi(apikey,params) {
        let that = this
        // 调用接口相当于整个组件重新刷新 关键数据清空 避免回传以往数据
        that.pickercolumns = []
        that.pickerdata = null
        that.inputele = ''
        getReasonList().then(res => {
          let dataRes = res.data || {}
          if (dataRes.code === 200) {
            let resdata = dataRes.data || []
            resdata.map(item => {
              item.nikName = item.dictLabel
              item.userId = item.dictValue
            })
            let pickercolumns = []
            pickercolumns.push(resdata)
            that.pickercolumns = pickercolumns
            that.defaultval(that.value)
          }
          that.loading = false
        })
      },
      // input 双向绑定值
      inputchange(e) {
        this.$emit('input', e)
      },
      // textarea 双向绑定值
      textareachange(e) {
        this.$emit('inputchange', e.target.value)
        this.$emit('input', e.target.value)
      },
      // 打开pop
      openpop() {
        if (this.disabled) {
          return
        }
        this.pop = true
        this.$emit('popchange', false)
      },
      // 关闭pop
      popclose() {
        this.pop = false
        this.$emit('popchange', true)
      },
      // 确认pop
      sureoop() {
        this.popclose()
        // console.log(this.pickerdata)
        if (this.pickerdata) {
          this.inputele = this.pickerdata.nikName
        }
        this.$emit('sure', this.pickerdata)
        this.$emit('input', this.pickerdata && this.pickerdata.userId)
      },
      // picker变动时
      columnschange(e) {
        // console.log(e)
        // console.log(e.value)
        if (e.value) {
          this.pickerdata = e.value[0]
        }
      },
      // 根据参数匹对 为picker 与 input展示确定 默认值
      defaultval(val) {
        let that = this
        let pickercolumns = this.pickercolumns
        if (pickercolumns.length > 0 && pickercolumns[0].length > 0) {
          let getdefault = false
          pickercolumns[0].map((item, index) => {
            if (val !== null && val !== undefined) {
              if (item.userId !== null && item.userId !== undefined && item.userId.toString() === val.toString()) {
                that.pickerdata = item
                that.defaultIndex = [index]
                that.inputele = item.nikName
                that.$emit('sure', that.pickerdata)
                getdefault = true
                // console.log(that.pickerdata)
              }
            }
          })
          // 没有找到对应id时 默认选择的为数组第一个
          if (!getdefault) {
            that.pickerdata = pickercolumns[0][0]
            that.defaultIndex = [0]
          }
        }
      }
  },

  watch: {
    columns: {
      handler(newval, oldval) {
        if (newval) {
          this.pickercolumns = newval
        }
      },
      immediate: true
    },
    params: {
      handler(newval, oldval) {
        // console.log(newval)
        this.getcolumnsbyapi(this.apikey)
      },
      deep: true ,// 深度监听父组件传过来对象变化 
    },
    value: {
      handler(newval, oldval) {
        if (this.mode === 'select') {
            this.defaultval(newval)
        } else {
          this.inputele = newval
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

.empty {
  height: 250rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 28rpx;
  font-size: 32rpx;
  font-weight: 500;
  color: #666666;
}
</style>