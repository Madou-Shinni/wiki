<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to="/admin/ebook">知识库管理</router-link>
      </a-menu-item>
      <a-menu-item key="4">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="5">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a class="loginMenu" v-if="user.id"  @click="showLoginModal">
        <span v-show="user.id">您好：{{ user.name }}</span>
      </a>
      <a class="loginMenu" v-if="!user.id"  @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginVisible"
        :confirm-loading="loginConfirmLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{span: 6}" :wrapper-col="{ span: 18}">
        <a-form-item label="用户名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input type="password" v-model:value="loginUser.password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";

declare let  hexMd5: any;
declare let  KEY: any;

export default defineComponent({
  name: "The-Header",
  setup: function () {


    const loginConfirmLoading = ref<boolean>(false);
    const loginVisible = ref<boolean>(false);
    const showLoginModal = () => {
      loginVisible.value = true;
    }

    /**
     * 登录后保存的
     */
    const user = ref();
    user.value = {}; // 初始化空对象避免空指针异常

    /**
     * 登录变量定义
     */
    const loginUser = ref({
      loginName: "test",
      password: "123456"
    })

    const login = () => {
      loginConfirmLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY)
      axios.post(`/user/login`, loginUser.value).then((response) => {
        loginConfirmLoading.value = false;
        const data = response.data;
        if (data.success) {// 保存成功对话框消失，loading效果消失
          loginVisible.value = false;
          message.success("登录成功!");
          user.value = data.data;
          store.commit("setUser", user.value);
        } else {
          loginUser.value.password = "";
          message.error(data.message);
        }
      })
    };

    return {
      showLoginModal,
      loginVisible,
      loginConfirmLoading,
      loginUser,
      login,
      user
    }
  }
})
</script>

<style scoped>

.header {
  position: relative;
}
.loginMenu {
  position: absolute;
  right: 50px;
  color: white;
}

.logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

</style>
