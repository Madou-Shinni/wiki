<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form layout="inline" :model="searchText">
        <a-form-item>
          <a-input-search
              v-model:value="searchText.text"
              placeholder="搜索"
              style="width: 200px"
              @search="onSearch()"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()" size="large">
            新增
          </a-button>
        </a-form-item>
      </a-form>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categorys"
          :loading="loading"
          :pagination="false"
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
      title="分类表单"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      :isAdd="isAdd"
  >
    <a-form :model="category" :label-col="{span: 6}" :wrapper-col="{ span: 18}">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
        <!--下拉框-->
        <a-select
            ref="select"
            v-model:value="category.parent"
        >
          <a-select-option value="0">无</a-select-option>
          <a-select-option :value="c.id" v-for="c in categorys" :key="c.id"
          :disabled="category.id === c.id"> <!--不能选择自己为上级分类-->
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";


export default defineComponent({
  name: 'AdminCategory',
  // vue3新函数，组件初始会执行
  setup() {
    // ref: 响应式数据(获取和赋值都需要.value)
    const categorys = ref();

    const loading = ref(false);
    // 列
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '父分类',
        dataIndex: 'parent',
      },
      {
        title: '顺序',
        dataIndex: 'sort',
      },
      {
        title: 'Action',
        dataIndex: 'action',
        slots: {customRender: 'action'}
      },
    ];

    /*--------- 对话框 ----------*/
    const category = ref({})
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const isAdd = ref<boolean>(false);
    /**
     * 编辑
     */
    const edit = (record: any) => {
      visible.value = true;
      // 通过Tool来复制一个新对象不让他影响列表数据
      category.value = Tool.copy(record);
    };
    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      category.value = {};
      isAdd.value = true;
    };
    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete(`/category/${id}`).then((response) => {
        const data = response.data;
        console.log(`${id}`)
        if (data.success) {// 保存成功对话框消失，loading效果消失
          // 重新加载列表数据
          handleQuery()
        }
      })
    }
    /**
     * 搜索
     */
    const searchText = ref();
    searchText.value = {};
    const onSearch = (searchValue: any) => {
      axios.get("/category/list", {
        params:{
          page: searchValue.page,
          size: searchValue.size,
          name: searchText.value.text
        }
      }).then((response) => {
        const data = response.data
        if (data.success) {
          categorys.value = data.data.list;
        } else {
          message.error(data.message);
        }
      })
    };

    const handleOk = () => {
      confirmLoading.value = true;
      // 判断是否新增
      if (isAdd.value) {
        // 新增
        axios.post("/category/save", category.value).then((response) => {
          const data = response.data;
          confirmLoading.value = false;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            visible.value = false;
            isAdd.value = false;
            // 重新加载列表数据
            handleQuery()
          }
        })
      } else {
        // 修改
        axios.put("/category/update", category.value).then((response) => {
          const data = response.data;
          confirmLoading.value = false;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            visible.value = false;
            // 重新加载列表数据
            handleQuery()
          }
        })
      }
    };
    /*-------------------------*/

    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;
      // 参数二必须用{params:{}} 或者 {params}简化写法
      axios.get("/category/all").then((response) => {
        loading.value = false
        const data = response.data
        if (data.success) {
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
          categorys.value = data.data;
        } else {
          message.error(data.message);
        }
      })
    };


    /**
     * 初始的时候也查询一次
     */
    onMounted(() => {
      console.log("发送请求")
      handleQuery();
    });

    // 返回数据让页面能够使用
    return {
      categorys,
      columns,
      loading,
      /*--------- 对话框 ----------*/
      category,
      visible,
      confirmLoading,
      isAdd,

      edit,
      add,
      handleDelete,

      handleOk,
      /*-------------------------*/
      /*----------- 搜索 --------*/
      searchText,
      onSearch
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
