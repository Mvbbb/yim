import { createRouter, createWebHistory } from 'vue-router'
// import Home from '../views/Home.vue'

const routes = [
  {
    path:'/',
    redirect:'/Interface',
  },
  {
    path: '/Interface',
    name: 'Interface',
    component:()=>import('../views/Interface')
  },
  // {
  //   path: '/PersonalInf',
  //   name: 'PersonalInf',
  //   component:()=>import('../views/detailPage/left/personalInf')
  // },
  // {
  //   path: '/ChatList',
  //   name: 'PersonalInf',
  //   component:()=>import('../views/detailPage/left/personalInf')
  // },
  {
    path: '/test',
    name: 'test',
    component: () => import(/* webpackChunkName: "about" */ '../views/test')
  },
  {
    path: '/test2',
    name: 'test2',
    component: () => import(/* webpackChunkName: "about" */ '../views/test2')
  },
  // {
  //   path: '/test',
  //   name: 'test',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/test')
  // }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
