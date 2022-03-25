<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-button type="primary" @click="add()" size="large">
        新增
      </a-button>
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
        <!--record代表一整行的数据,传递给edit方法-->
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <!--对话框-->
  <a-modal
      title="知识库表单"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      :isAdd="isAdd"
  >
    <a-form :model="ebook" :label-col="{span: 6}">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="text" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'AdminEbook',
  // vue3新函数，组件初始会执行
  setup() {
    // ref: 响应式数据(获取和赋值都需要.value)
    const ebooks = ref();
    // 分页
    const pagination = ref({
      current: 1,
      pageSize: 1001,
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

    /*--------- 对话框 ----------*/
    const ebook = ref({})
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const isAdd = ref<boolean>(false);
    /**
     * 编辑
     */
    const edit = (record: any) => {
      visible.value = true;
      ebook.value = record;
    };
    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      ebook.value = {};
      isAdd.value = true;
    };
    /**
     * 删除
     */
    const  handleDelete = (id:number)=>{
      axios.delete(`/ebook/${id}`).then((response) => {
        const data = response.data;
        console.log(`${id}`)
        if (data.success) {// 保存成功对话框消失，loading效果消失
          // 重新加载列表数据
          handleQuery({// 加载当前页
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        }
      })
    }

    const handleOk = () => {
      confirmLoading.value = true;
      // 判断是否新增
      if(isAdd.value) {
        // 新增
        axios.post("/ebook/save", ebook.value).then((response) => {
          const data = response.data;
          confirmLoading.value = false;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            visible.value = false;
            isAdd.value = false;
            // 重新加载列表数据
            handleQuery({// 加载当前页
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          }
        })
      }else {
        // 修改
        axios.put("/ebook/update", ebook.value).then((response) => {
          const data = response.data;
          confirmLoading.value = false;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            visible.value = false;
            // 重新加载列表数据
            handleQuery({// 加载当前页
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          }
        })
      }
    };
    /*-------------------------*/

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
        if(data.success) {
          /**
           * 后端返回统一格式
           * {
           *   success: ,
           *   message: ,
           *   data:{
           *
           *   }
           * }
           *
           * 业务上的成功或失败
           * private boolean success = true;
           *
           * 返回信息
           * private String message;
           *
           * 返回泛型数据，自定义类型
           * private T data;
           */
          ebooks.value = data.data.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.data.total;
        }else {
          message.error(data.message);
        }
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
      handleTableChange,
      /*--------- 对话框 ----------*/
      ebook,
      visible,
      confirmLoading,
      isAdd,

      edit,
      add,
      handleDelete,

      handleOk,
      /*-------------------------*/
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
