<template>
  <a-layout-footer style="text-align: center">
    <a href="https://beian.miit.gov.cn">浙ICP备2021028812号-2&nbsp;</a>小天才知识库 | ©2022-present | Created by 小天才
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted} from "vue";
import store from "@/store";
import {Tool} from "@/util/tool";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "The-Footer",
  setup() {
    const user = computed(() => store.state.user)
    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log("WebSocket连接成功,状态码：",websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log("WebSocket收到消息：",event.data)
        notification['info']({
          message: '收到消息',
          description: event.data
        });
    };
    const onError = () => {
      console.log("WebSocket连接错误,状态码：",websocket.readyState)
    };
    const onClose = () => {
      console.log("WebSocket连接关闭，状态码：",websocket.readyState)
    };
    const initWebSocket = () => {
      // 连接成功
      websocket.onopen = onOpen;
      // 收到消息的回调
      websocket.onmessage = onMessage;
      // 连接错误
      websocket.onerror = onError;
      // 连接关闭的回调
      websocket.onclose = onClose;
    };

    onMounted(()=>{
      // WebSocket
      if('WebSocket' in window) {
        token = Tool.uuid(10);
        // 连接地址：ws://127.0.0.1:8081/ws/xxx
        // /ws/xxx 后端暴露的url
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket();
        // 关闭
        // websocket.close();
      }else {
        alert("当前浏览器 不支持")
      }
    });

    return {
      user,
    }
  }
})
</script>

<style scoped>

</style>
