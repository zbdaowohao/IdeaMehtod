import Vue from 'vue'
import Router from 'vue-router'
import PrintPlugin from './views/PrintPlugin.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: PrintPlugin
    }
  ]
})
