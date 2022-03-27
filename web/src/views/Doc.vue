<template>
  <a-layout>
    <a-layout-content :style="{ margin: '24px 16px 0', overflow: 'initial' }">
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="docs.length"
              :tree-data="docs"
              :defaultExpandAll="true"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">col-12</a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {message} from "ant-design-vue";
import axios from "axios";

export default defineComponent({
  name: "Doc",
  // vue3新函数，组件初始会执行
  setup() {
    // 路由参数
    const route = useRoute();
    // ref: 响应式数据(获取和赋值都需要.value)
    const docs = ref();
    docs.value = [];

    const loading = ref(false);



    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;
      // 参数二必须用{params:{}} 或者 {params}简化写法
      axios.get(`/doc/all/${route.query.ebookId}`).then((response) => {
        loading.value = false
        const data = response.data
        if (data.success) {
          docs.value = data.data;
        } else {
          message.error(data.message);
        }
      })
    };


    /**
     * 初始的时候也查询一次
     */
    onMounted(() => {
      handleQuery();
    });

    // 返回数据让页面能够使用
    return {
      docs,
      loading,
    }
  }
});

</script>

<style scoped>

</style>
