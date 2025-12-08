import Vue from "vue";
import App from "./App";
import uView from "uview-ui";
import store from "./store";
import { checkauthority, checkuserType } from '@/utils/authority.js'

import { router, RouterMount } from "./router/index.js"; //路径换成自己的

Vue.use(router);
// 公共方法
import utils from "utils/util.js";
import filters from "utils/filters.js";

// 全局定义组件
import dialog from "components/dialog.vue";
Vue.component("showmodal", dialog);
import headerbar from "components/headerbar.vue";
Vue.component("headerbar", headerbar);
import bottombar from "components/bottombar.vue";
Vue.component("bottombar", bottombar);
import formelement from "components/formelement.vue";
Vue.component("formelement", formelement);
import formselect_search from "components/formselect_search.vue";
Vue.component("formselect_search", formselect_search);
import formdriverIdbut from "components/formdriverIdbut.vue";
Vue.component("formdriverIdbut", formdriverIdbut);
import formdate from "components/formdate.vue";
Vue.component("formdate", formdate);
import empty from "components/empty.vue";
Vue.component("Empty", empty);

// 首先在main.js中进行注册，将my-link注册为全局组件，注册后使用方法同<router-link>
import Mylink from "../node_modules/uni-simple-router/dist/link.vue";
Vue.component("my-link", Mylink);

Vue.prototype.$utils = utils;
Vue.config.productionTip = false;
Vue.prototype.checkauthority = checkauthority;
Vue.prototype.checkuserType = checkuserType;

  
// Vue.prototype.globalUrl = "http://rgdxjhghl.hd-bkt.clouddn.com";
// Vue.prototype.globalUrl = "http://192.168.2.107:8600";
Vue.prototype.globalUrl = "http://101.132.238.169:8600";
 

Vue.prototype.utils = utils;

Object.keys(filters).map((v) => {
    Vue.filter(v, filters[v]);
});

App.mpType = "app";
Vue.use(uView);
const app = new Vue({
    store,
    ...App,
});

// #ifndef H5
//为了兼容小程序及app端必须这样写才有效果
app.$mount();
// #endif

// App.mpType = "app";

//v1.3.5起 H5端 你应该去除原有的app.$mount();使用路由自带的渲染方式
// #ifdef H5
RouterMount(app, router, "#app");
// #endif
