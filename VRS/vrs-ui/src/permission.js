import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/auth-redirect', '/bind', '/register']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/

    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})
// router.beforeEach((to, from, next) => {
//   NProgress.start()

//   const hasToken = getToken()

//   // // 输出调试信息
//   // console.log('Token:', hasToken)
//   // console.log('Roles:', store.getters.roles)

//   // 如果已登录
//   if (hasToken) {
//     // console.log('User is logged in')

//     // 如果目标是登录页且已经登录，避免死循环
//     if (to.path === '/login') {
//       const redirectPath = to.query.redirect || '/car/application'
//       if (to.path !== redirectPath) {
//         next({ path: redirectPath })  // 如果目标路径不是重定向路径，则跳转到指定的页面
//       } else {
//         next()  // 如果目标路径已经是重定向路径，直接跳过
//       }
//       NProgress.done()
//     } else {
//       // 登录后还未获取用户信息，执行获取用户信息的逻辑
//       if (store.getters.roles.length === 0) {
//         store.dispatch('GetInfo').then(() => {
//           store.dispatch('GenerateRoutes').then(accessRoutes => {
//             router.addRoutes(accessRoutes) // 动态添加路由
//             next({ ...to, replace: true }) // 确保 addRoutes 完成后才导航
//           })
//         }).catch(err => {
//           store.dispatch('LogOut').then(() => {
//             Message.error(err || '验证失败，请重新登录')
//             next(`/login?redirect=${to.fullPath}`) // 验证失败，重新跳转到登录页
//           })
//         })
//       } else {
//         next() // 已获取用户信息，继续跳转
//       }
//     }
//   } else {
//     // console.log('User is not logged in')

//     // 如果没有 token，判断是否在白名单中
//     if (whiteList.indexOf(to.path) !== -1) {
//       next() // 在免登录白名单，直接进入
//     } else {
//       // 跳转到登录页并附带原始路径作为重定向参数
//       next(`/login?redirect=${to.fullPath}`)
//       NProgress.done() // 结束进度条
//     }
//   }
// })

router.afterEach(() => {
  NProgress.done()
})
