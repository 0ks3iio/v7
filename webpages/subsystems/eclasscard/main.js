// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import store from './store'
import routers from './router'
import Router from 'vue-router'
import fastclick from 'fastclick'
import VueLazyload from 'vue-lazyload'
import 'mint-ui/lib/style1.css'
import '@mod/amfe-flexible/index.js'
import '@ass/css/common.css'
import './utils/styles.css'
import { Swipe, SwipeItem } from 'mint-ui';
// import './utils/daka.js'

Vue.component('Swipe', Swipe);
Vue.component('SwipeItem', SwipeItem);

Vue.use(Vuex)
Vue.use(Router)
Vue.use(VueLazyload, {
  // error: logoSrc,//这个是请求失败后显示的图片
  // loading: logoSrc, //这个是加载的loading过渡效果
  try: 2,// 这个是加载图片数量
  attempt: 1, //尝试次数
})
fastclick.attach(document.body)

const router = new Router(routers)
router.beforeEach((to, from, next) => {
  window.scrollTo(0, 0)
  next()
})

Vue.prototype.$rem2px = function (rem) {
  let fts = document.documentElement.style.fontSize
  let px = parseFloat(rem) * parseFloat(fts)
  return parseInt(px)
}

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})
