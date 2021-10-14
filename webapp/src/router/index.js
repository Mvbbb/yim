import { createRouter, createWebHistory } from 'vue-router'

// import Home from '../views/Home.vue'

const routes = [
  {
    path:'/',
    redirect:'/Login',
  },
  {
    path: '/Login',
    name: 'Login',
    component:()=>import('../views/Login')
  },
  {
    path: '/register',
    name: 'register',
    component:()=>import('../views/register')
  },
  {
    path: '/Interface',
    name: 'Interface',
    component:()=>import('../views/Interface'),
    meta:{title:'界面',
      keepAlive: true
    }
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
