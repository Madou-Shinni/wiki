<template>
  <a-layout>
    <TheHeader />
    <RouterView />
    <a-layout-footer style="text-align: center">
      Just Create ©2022 Created by Freedom lovers
    </a-layout-footer>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import TheHeader from '@/components/The-Header.vue';
export default defineComponent({
  name: 'App',
  components: {
    TheHeader,
  },
});
</script>

<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
