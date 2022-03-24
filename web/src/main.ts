import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
// 导入所有图标库
import * as Icons from '@ant-design/icons-vue'
import axios from 'axios'

axios.defaults.baseURL = process.env.VUE_APP_SERVER
/**
 * axios拦截器
 */
// http request 拦截器
axios.interceptors.request.use(
    config => {
        console.log('请求参数：',config)
        return config
    },error => {
        return Promise.reject(error)
    })
// http response 拦截器
axios.interceptors.response.use(
    response => {
        console.log('返回结果：',response)
        return response
    },error => {
        console.log('返回错误：',error)
        return Promise.reject(error)
    })


//createApp(App).use(store).use(router).use(Antd).mount('#app')
// 分离
const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

// 全局使用图标
const icons: any = Icons;
for (const i in icons) {
    app.component(i,icons[i])
}
