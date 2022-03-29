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
              @search="onSearch({page:1,size:pagination.pageSize})"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">
            新增
          </a-button>
        </a-form-item>
      </a-form>
      <br/>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:category="{text, record}">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <!--record代表一整行的数据,传递给edit方法-->
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary" @click="resetPassword(record)">
              重置密码
            </a-button>
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
      title="用户表单"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
  >
    <a-form :model="user" :label-col="{span: 6}" :wrapper-col="{ span: 18}">
      <a-form-item label="用户名">
        <!--!!：绕过类型检查-->
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name"/>
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password"/>
      </a-form-item>
    </a-form>
  </a-modal>
  <a-modal
      title="重置密码"
      v-model:visible="resetVisible"
      :confirm-loading="resetConfirmLoading"
      @ok="handleResetOk"
  >
    <a-form :model="user" :label-col="{span: 6}" :wrapper-col="{ span: 18}">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

declare let  hexMd5: any;
declare let  KEY: any;


export default defineComponent({
  name: 'AdminUser',
  // vue3新函数，组件初始会执行
  setup() {
    // ref: 响应式数据(获取和赋值都需要.value)
    const users = ref();
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
        title: '用户名',
        dataIndex: 'loginName',
      },
      {
        title: '昵称',
        dataIndex: 'name',
      },
      {
        title: '密码',
        dataIndex: 'password',
      },
      {
        title: 'Action',
        dataIndex: 'action',
        slots: {customRender: 'action'}
      },
    ];

    /*--------- 对话框 ----------*/
    /**
     * 数组 [100,101] 对应：前端开发/vue
     */
    const user = ref()
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const isAdd = ref<boolean>(false);
    /**
     * 编辑
     */
    const edit = (record: any) => {
      isAdd.value = false;
      visible.value = true;
      // 通过Tool来复制一个新对象不让他影响列表数据
      user.value = Tool.copy(record);
    };
    /**
     * 新增
     */
    const add = () => {
      user.value = {};
      visible.value = true;
      isAdd.value = true;
    };
    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete(`/user/${id}`).then((response) => {
        const data = response.data;
        if (data.success) {// 保存成功对话框消失，loading效果消失
          // 重新加载列表数据
          handleQuery({// 加载当前页
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        }
      })
    }
    /**
     * 搜索
     */
    const searchText = ref();
    searchText.value = {};
    const onSearch = (searchValue: any) => {
      axios.get("/user/list", {
        params: {
          page: searchValue.page,
          size: searchValue.size,
          loginName: searchText.value.text
        }
      }).then((response) => {
        const data = response.data
        if (data.success) {
          users.value = data.data.list;
          // 重置分页按钮
          pagination.value.current = searchValue.page;
          pagination.value.total = data.data.total;
        } else {
          message.error(data.message);
        }
      })
    };

    const handleOk = () => {
      confirmLoading.value = true;
      // 判断是否新增
      if (isAdd.value) {
        user.value.password = hexMd5(user.value.password + KEY)
        // 新增
        axios.post("/user/save", user.value).then((response) => {
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
          }else {
            message.error(data.message);
          }
        })
      } else {
        // 修改
        axios.put("/user/update", user.value).then((response) => {
          const data = response.data;
          confirmLoading.value = false;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            visible.value = false;
            // 重新加载列表数据
            handleQuery({// 加载当前页
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          }else {
            message.error(data.message);
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
      axios.get("/user/list", {params}).then((response) => {
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
          users.value = data.data.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.data.total;
        } else {
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
     * 重置-编辑
     */
    const resetPassword = (record: any) => {
      resetVisible.value = true;
      // 通过Tool来复制一个新对象不让他影响列表数据
      user.value = Tool.copy(record);
      user.value.password = null;
    };

    /**
     * 重置密码
     */
    const resetVisible = ref<boolean>(false);
    const resetConfirmLoading = ref<boolean>(false);
    const handleResetOk = () => {
      resetConfirmLoading.value = true;
      user.value.password = hexMd5(user.value.password + KEY)
      axios.put("/user/resetPassword", user.value).then((response) => {
        const data = response.data;
        resetConfirmLoading.value = false;
        if (data.success) {// 保存成功对话框消失，loading效果消失
          resetVisible.value = false;
          // 重新加载列表数据
          handleQuery({// 加载当前页
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        }else {
          message.error(data.message);
        }
      })
    }


    /**
     * 初始的时候也查询一次
     */
    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    });

    // 返回数据让页面能够使用
    return {
      users,
      pagination,
      columns,
      loading,
      handleTableChange,
      /*--------- 对话框 ----------*/
      user,
      visible,
      confirmLoading,

      edit,
      add,
      handleDelete,

      handleOk,
      /*-------------------------*/
      /*----------- 搜索 --------*/
      searchText,
      onSearch,
      /*-------------------------*/
      resetPassword,
      resetVisible,
      resetConfirmLoading,
      handleResetOk
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
