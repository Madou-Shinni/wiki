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
      <a-menu-item key="6" class="loginMenu" @click="showLoginModal">
        <span>登录</span>
      </a-menu-item>
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

export default defineComponent({
  name: "The-Header",
  setup() {

    const loginConfirmLoading = ref<boolean>(false);
    const loginVisible = ref<boolean>(false);
    const showLoginModal = () => {
      loginVisible.value = true;
    }

    /**
     * 登录变量定义
     */
    const loginUser = ref({
      loginName: "test",
      password: "123456"
    })

    const login = () => {
      loginConfirmLoading.value = true;
      console.log("开始登录");
    };

    return {
      showLoginModal,
      loginVisible,
      loginConfirmLoading,
      loginUser,
      login
    }
  }
})
</script>

<style scoped>

.loginMenu {
  float: right;
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
