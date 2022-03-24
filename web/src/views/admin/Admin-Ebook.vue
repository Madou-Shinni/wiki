<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";


export default defineComponent({
  name: 'AdminEbook',
  // vue3新函数，组件初始会执行
  setup() {
    // ref: 响应式数据(获取和赋值都需要.value)
    const ebooks = ref();
    // 分页
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);
    // 列
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        // 渲染
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '分类一',
        dataIndex: 'category1Id',
      },
      {
        title: '分类二',
        dataIndex: 'category2Id',
      },
      {
        title: '文档数',
        dataIndex: 'docCount',
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
      },
      {
        title: 'Action',
        dataIndex: 'action',
        slots: {customRender: 'action'}
      },
    ];

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      // 参数二必须用{params:{}} 或者 {params}简化写法
      axios.get("/ebook/list", {params}).then((response) => {
        console.log(params)
        loading.value = false
        const data = response.data
        // 我们后端封装的返回数据的数据集是对象.data
        ebooks.value = data.data.list;
        // 重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.data.total;
      })
    };

    /**
     *  表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    };

    /**
     * 初始的时候也查询一次
     */
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    // 返回数据让页面能够使用
    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
}
</style>
