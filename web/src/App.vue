<template>
  <a-layout>
    <TheHeader />
    <RouterView />
    <TheFooter />
  </a-layout>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import TheHeader from '@/components/The-Header.vue';
import TheFooter from "@/components/The-Footer.vue";
export default defineComponent({
  name: 'App',
  components: {
    TheFooter,
    TheHeader,
  },
});
</script>

<style>

.site-layout-background {
  background: #fff;
}
</style>
