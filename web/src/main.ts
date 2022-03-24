import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
// 导入所有图标库
import * as Icons from '@ant-design/icons-vue'
import axios from 'axios'

axios.defaults.url = process.env.VUE_APP_SERVER

//createApp(App).use(store).use(router).use(Antd).mount('#app')

// 分离
const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

// 全局使用图标
const icons: any = Icons;
for (const i in icons) {
    app.component(i,icons[i])
}

console.log('环境：',process.env.NODE_ENV)
console.log('server地址：',process.env.VUE_APP_SERVER)
