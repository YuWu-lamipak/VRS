import { RouterMount, createRouter } from "uni-simple-router";

const router = createRouter({
    platform: process.env.VUE_APP_PLATFORM,
    routes: [...ROUTES],
});

// router/index.js

// import modules from "./modules";
// import Vue from "vue";
// //这里仅示范npm安装方式的引入，其它方式引入请看最上面【安装】部分
// import Router from "uni-simple-router";

// Vue.use(Router);

// const whitelist = {
//     //声明了一个白名单
//     "/pages/index/index": "index", // 冒号后面的 index 要对应 自己设置的 name，否则无法跳转
//     "/pages/user/user": "user",
// };

// //初始化
// const router = new Router({
//     routes: [
//         ...modules,
//         {
//             aliasPath: "/404",
//             path: "/pages/test/404",
//             name: "404",
//         },
//         {
//             path: "*",
//             name: "moddle",
//             redirect: (to) => {
//                 const name = whitelist[to.path];
//                 if (name) {
//                     return { name };
//                 }
//                 return { name: "404" };
//             },
//         },
//     ], //路由表
// });

//全局路由前置守卫
// router.beforeEach((to, from, next) => {
//     console.log(to);
//     next();
// });
// // 全局路由后置守卫
// router.afterEach((to, from) => {});
// export default router;

//全局路由前置守卫
router.beforeEach((to, from, next) => {
    next();
});
// 全局路由后置守卫
router.afterEach((to, from) => {
    console.log("跳转结束");
});

export { router, RouterMount };
