<!--
 * @Author: yh
 * @Date: 2022 08 25
    属性
 * value  v-model String Input 绑定值
 * apikey String  根据接口请求数据
 * params Objcet  apikey存在时 根据接口请求数据 的参数
 * placeholder    string     Inputplaceholder
 * disabled        Boolean    是/否 可编辑
 * columns        Array      选择框的数组
 * 
-->

<template>
    <view>
      <view class="flex" @click="openpop()">
          <u-input v-model="inputele" placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #999999;"
            :placeholder="placeholder" border="none" inputAlign="right" readonly>
          </u-input>
          <img v-if="!disabled" class="icon margin_left12" src="/static/selectable.png"/>
      </view>
      <u-popup :show="pop" mode="bottom" @close="popclose()" :round="30">
        <scroll-view class="popupview" scroll-y="true">
            <view class="poptitleline flex">
                <view class="leftbut" @click="popclose()">取消</view>
                <view class="rightbut leftauto" @click="sureoop()">确认</view>
            </view>
            <view class="flex searchinputframe">
                <u-input :clearable="true" @clear="clearserch" v-model="linkvalue" @change="linksearch"
                fontSize="28rpx" placeholder="请输入" border="none" shape="circle"></u-input>
                <view class="searchIcon">
                    <img class="searchIcon" src="/static/search.png"/>
                </view>
            </view>
            <view class="empty" v-if="loading">
              <u-loadmore status="loading" />
            </view>
            <view v-if="linkvalue">
              <view v-for="(item, index) in linkcolumns" :class="'player_item flex_center ' + (pickerdata.userId === item.userId ? 'grey' : '')" @click="listsure(item)">
                <view v-html="item.showName">
                  <!-- <rich-text :nodes="item.showName"></rich-text> -->
                </view>
              </view>
            </view>
            <view v-else>
              <view v-for="(item, index) in pickercolumns" :class="'player_item flex_center ' + (pickerdata.userId === item.userId ? 'grey' : '')" @click="listsure(item)">
                {{item.nikName}}
              </view>
            </view>
            <view v-if="pickercolumns.length === 0 && !loading" class="empty">暂无数据哦~</view>
            <view v-if="linkvalue && linkcolumns.length === 0 && !loading" class="empty">暂无数据哦~</view>
            <!-- <view class="picker">
                <mypicker v-if="pickercolumns.length > 0 && !loading" :defaultIndex="defaultIndex" :columns="pickercolumns" keyName="nikName" @change="columnschange" :loading="loading"/>
                <view v-else class="empty">暂无数据哦~</view>
            </view> -->
        </scroll-view>
      </u-popup>
    </view>
</template>

<script>
import mypicker from './my-view/my-picker/my-picker.vue'
import { getUserList, getCheckerList, getReasonList, getSupplierList, getScrapList } from '@/api/ordercar.js'
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
      // 联想查询 展示数组
      linkvalue: null,
      linkcolumns: [],
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
  },
  mounted() {
	  this.qiUrl = this.qiNiuUrl;
    if (this.apikey) {
      this.getcolumnsbyapi(this.apikey)
    }
  },
  methods: {
      // 根据api获取数组
      getcolumnsbyapi() {
        let apikey = this.apikey
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
        } else if (apikey === 'supplier') {
          this.getSupplierListapi(apikey, this.params);
        } else if (apikey === 'scrap') {
          this.getScrapListapi();
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
            // pickercolumns.push(resdata)
            pickercolumns = resdata
            that.pickercolumns = pickercolumns
            // console.log(that.pickercolumns)
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
            // pickercolumns.push(resdata)
            pickercolumns = resdata
            that.pickercolumns = pickercolumns
            that.defaultval(that.value)
          }
          that.loading = false
        })
      },
      // 关联搜索清空
      clearserch() {
        this.linkvalue = null
      },
      // 联想查询
      linksearch() {
        let pickercolumns = this.pickercolumns || []
        let linkcolumns = []
        let linkvalue = this.linkvalue
        pickercolumns.map(item => {
          item.showName = item.nikName
          if (item.nikName.indexOf(linkvalue) !== -1) {
            item.showName = item.nikName.replace(linkvalue,('<span style="color:#2196F3">' + linkvalue + '</span>')) // 添加文本样式
            linkcolumns.push(item)
          }
        })
        this.linkcolumns = linkcolumns
        console.log(this.linkcolumns)
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
            // pickercolumns.push(resdata)
            pickercolumns = resdata
            that.pickercolumns = pickercolumns
            that.defaultval(that.value)
          }
          that.loading = false
        })
      },
    //供应商列表
      getSupplierListapi(apikey,params) {
        let that = this
        // let data = {
          // ...params,
        //}
        // 调用接口相当于整个组件重新刷新 关键数据清空 避免回传以往数据
        that.pickercolumns = []
        that.pickerdata = null
        that.inputele = ''
        getSupplierList().then(res => {
          let dataRes = res.data || {}
          if (dataRes.code === 200) {
            let resdata = dataRes.data || []
            resdata.map(item => {
            item.nikName = item.name;
            item.userId = item.ID;
        });
            let pickercolumns = []
            pickercolumns = resdata
            that.pickercolumns = pickercolumns
            that.defaultval(that.value)
          }
          that.loading = false
        })
      },      
      // 获取废料名称
      getScrapListapi(apikey, params) {
        let that = this
        // 调用接口相当于整个组件重新刷新 关键数据清空 避免回传以往数据
        that.pickercolumns = []
        that.pickerdata = null
        that.inputele = ''
        getScrapList().then(res => {
          let dataRes = res.data || {}
          if (dataRes.code === 200) {
            let resdata = dataRes.data || []
            resdata.map(item => {
              item.nikName = item.dictLabel
              item.userId = item.dictValue
            })
            let pickercolumns = []
            pickercolumns = resdata
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
      // 列表选中事件
      listsure(item) {
        // console.log(item)
        this.pickerdata = item
        // this.inputele = this.pickerdata.nikName
        // this.$emit('sure', this.pickerdata)
        // this.$emit('input', this.pickerdata && this.pickerdata.userId)
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
        if (pickercolumns.length > 0) {
          let getdefault = false
          pickercolumns.map((item, index) => {
            if (val !== null && val !== undefined) {
              if (item.userId !== null && item.userId !== undefined && item.userId.toString() === val.toString()) {
                that.pickerdata = item
                that.defaultIndex = [index]
                that.inputele = item.nikName
                that.$emit('sure', that.pickerdata)
                getdefault = true
                console.log(that.pickerdata)
              }
            }
          })
          // 没有找到对应id时 默认选择的为数组第一个
          if (!getdefault) {
            that.pickerdata = pickercolumns[0]
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
        this.getcolumnsbyapi()
      },
      deep: true ,// 深度监听父组件传过来对象变化 
    },
    value: {
      handler(newval, oldval) {
        this.defaultval(newval)
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

.popupview {
    z-index: 500;
    height: 80vh;
}

.poptitleline {
    width: 670rpx;
    height: 100rpx;
    display: flex;
    align-items: center;
    padding: 0 40rpx;
    position: sticky;
    top: 0;
    left: 0;
    background: white;
    z-index: 600;
    border-radius: 40rpx;

  .leftbut {
    font-size: 28rpx;
    font-family: FZHTJW--GB1-0;
    line-height: 40rpx;
    color: #212121;
  }

  .rightbut {
    font-size: 28rpx;
    font-family: FZHTJW--GB1-0;
    line-height: 40rpx;
    color: #2196F3;
  }
}

.searchinputframe {
    display: flex;  /* 使用 flex 布局 */
    align-items: center;  /* 垂直居中对齐 */
    width: 650rpx;
    padding: 14rpx 20rpx;
    border: 1rpx solid #D1D1D1;
    border-radius: 40rpx;
    margin: 0 30rpx;
    position: sticky;
    top: 100rpx;
    left: 0;
    z-index: 600;
    background: white;
 }

 .searchIcon {
      width: 40rpx;
      height: 40rpx;
      margin-left: 5rpx;
  }

u-input {
    flex: 1;  /* 让输入框占据剩余空间 */
}
.player_item {
  height: 118rpx;
  border-bottom: 2rpx solid #E7E7E7;
  padding: 0 40rpx;
  font-size: 32rpx;
  font-weight: 500;
}

.grey {
  background: #F5F5F5;
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