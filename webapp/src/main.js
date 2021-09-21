import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'



// io('ws://172.20.10.2:8080')
// import VueSocketIO  from "vue-socket.io";
// new VueSocketIO({
//     debug: true,
//     connection: io('ws://172.20.10.2:8000'),  // 连接后端地址
//     vuex: {
//     }
// })

import axios from 'axios'
import VueAxios from 'vue-axios'

axios.defaults.timeout = 5000 // 请求超时

createApp(App).use(VueAxios, axios)
    .use(store).use(router)
    .use(ElementPlus).mount('#app')
