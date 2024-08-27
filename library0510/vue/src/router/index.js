import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/',
    name: 'layout',
    component: () => import('../views/layout/index.vue'),
    children:[
      {
        path: 'home',
        name: 'home',
        component: () => import('../views/layout/components/home/HomeView.vue')
      },
      {
        path: 'books',
        name: 'books',
        component: () => import('../views/layout/components/manage/BooksView.vue')
      },
      {
        path: 'users',
        name: 'users',
        component: () => import('../views/layout/components/manage/UsersView.vue')
      },
      {
        path: 'about',
        name: 'about',
        component: () => import('../views/layout/components/public/AboutView.vue')
      },
      {
        path: 'settings',
        name: 'settings',
        component: () => import('../views/layout/components/public/SettingsView.vue')
      },
      {
        path: 'library',
        name: 'library',
        component: () => import('../views/layout/components/house/LibraryView.vue')
      },
      {
        path: 'elegant',
        name: 'elegant',
        component: () => import('../views/layout/components/house/ElegantView.vue')
      },
      {
        path: 'bar',
        name: 'bar',
        component: () => import('../views/layout/components/house/BarView.vue')
      },
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
