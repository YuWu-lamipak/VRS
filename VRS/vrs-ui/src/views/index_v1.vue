<template>
  <div class="dashboard-editor-container">
<!-- <div style="margin-bottom: 20px;">欢迎使用乐美车辆预约系统！</div> -->
<!--    <panel-group @handleSetLineChartData="handleSetLineChartData" />-->

<!--    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">-->
<!--      <line-chart :chart-data="lineChartData" />-->
<!--    </el-row>-->

<!--    <el-row :gutter="32">-->
<!--      <el-col :xs="24" :sm="24" :lg="8">-->
<!--        <div class="chart-wrapper">-->
<!--          <raddar-chart />-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :xs="24" :sm="24" :lg="8">-->
<!--        <div class="chart-wrapper">-->
<!--          <pie-chart />-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :xs="24" :sm="24" :lg="8">-->
<!--        <div class="chart-wrapper">-->
<!--          <bar-chart />-->
<!--        </div>-->
<!--      </el-col>-->
<!--    </el-row>-->
    <el-row :gutter="32">
      <!-- <el-col :xs="24" :sm="24" :lg="6">
        <div class="StatisticsCard relative">
            <img class="StatisticsCard absolute" src="@/assets/home/home_ordercheck.png"/>
            <div class="imgInfoframe">
                <div class="margin_bottom8 text">待审核</div>
                <div class="num">{{ 0 }}</div>
            </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="StatisticsCard relative">
            <img class="StatisticsCard absolute" src="@/assets/home/home_ordercheck.png"/>
            <div class="imgInfoframe">
                <div class="margin_bottom8 text">已审核</div>
                <div class="num">{{ 0 }}</div>
            </div>
        </div>
      </el-col> -->
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="StatisticsCard relative">
            <img class="StatisticsCard absolute" src="@/assets/home/home_ordercheck.png"/>
            <div class="imgInfoframe">
                <div class="margin_bottom8 text">{{ $t('lbb') }}</div>
                <div class="num">{{ statis.total }}</div>
            </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="6">
        <div class="StatisticsCard relative">
            <img class="StatisticsCard absolute" src="@/assets/home/home_ordercheck.png"/>
            <div class="imgInfoframe">
                <div class="margin_bottom8 text">{{ $t('lbbb') }}</div>
                <div class="num">{{ statis.today }}</div>
            </div>
        </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import { statis } from '@/api/system/application'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      lineChartData: lineChartData.newVisitis,
      statis: {
        today: 0,
        total: 0
      }
    }
  },
  mounted() {
    this.getstatis()
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    getstatis() {
      let that = this
      statis().then(res => {
        if (res.code === 200) {
          let backdata = res.data
          that.statis = {
            today: backdata.today,
            total: backdata.total
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  // background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

// 2022 10 24 首页样式
.StatisticsCard {
  width: 100%;
  // height: 240px;
  z-index: 10;
}
.relative {
    position: relative;
}

.absolute {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
}

.imgInfoframe {
    // padding: 42px 0 0 28px;
    padding: 30px 30px 30px 30px;
    z-index: 10;
    .nocometext {
        font-size: 28px;
        font-family: PingFangSC;
        font-weight: 600;
        color: #0A82E3;
        height: 40px;
        line-height: 40px;
    }

    .nocomenum {
        font-size: 44px;
        font-family: PingFangSC;
        font-weight: 600;
        color: #333333;
        height: 64px;
        line-height: 64px;
    }

    .text {
        font-size: 24px;
        font-family: PingFangSC;
        font-weight: 500;
        color: #0A82E3;
        height: 40px;
        line-height: 40px;
    }

    .num {
        font-size: 40px;
        font-family: PingFangSC;
        font-weight: 600;
        color: #333333;
        height: 58px;
        line-height: 58px;
    }
}
</style>
