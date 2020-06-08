import Vue from 'vue'
import VueRouter from 'vue-router'
import uploader from '../components/vue-simple-uploader.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/upload' },
  { path: '/upload', component: uploader }
]

const router = new VueRouter({
  routes
})

export default router
