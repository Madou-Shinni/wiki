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
          <a-button type="primary" @click="add()">
            新增
          </a-button>
        </a-form-item>
      </a-form>
      <br/>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="docs"
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
      title="文档表单"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      :isAdd="isAdd"
  >
    <a-form :model="doc" :label-col="{span: 6}" :wrapper-col="{ span: 18}">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeSelectData"
            placeholder="请选择父文档"
            tree-default-expand-all
            :replaceFields="{title: 'name', key: 'id', value: 'id'}"
        >
<!--          <a-tree-select-node key="0" value="0" title="根节点">-->
<!--          </a-tree-select-node>-->
        </a-tree-select>

      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";


export default defineComponent({
  name: 'AdminDoc',
  // vue3新函数，组件初始会执行
  setup() {
    // 路由参数
    const route = useRoute();
    // ref: 响应式数据(获取和赋值都需要.value)
    const docs = ref();

    const loading = ref(false);
    // 列
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '父文档',
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
    const treeSelectData = ref();// 1.因为树型选择节点的的组件会随当前编辑的节点数据变化
    treeSelectData.value = []; // 2.所以定义了一个新变量来增加根节点选项
    const doc = ref({})
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const isAdd = ref<boolean>(false);

    /**
     * 递归
     * 将某节点及其子孙节点全部置为disable
     */
    const setDisable = (treeSelectData: any, id: any)=>{
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if(node.id === id) {
          // 如果当前节点就是目标节点
          // 将目标节点置为disable
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disable
          const children = node.children;
          if(Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children,children[j].id)
            }
          }
        }else {
          // 如果当前节点不是目标节点，则到其子节点再找找看
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    }


    /**
     * 编辑
     */
    const edit = (record: any) => {
      visible.value = true;
      // 通过Tool来复制一个新对象不让他影响列表数据
      doc.value = Tool.copy(record);
      // 不能选择当前节点及其所有子孙节点，作为父节点会使树断开
      treeSelectData.value = Tool.copy(docs.value);
      setDisable(treeSelectData.value,record.id);
      // 为选择树添加一个无
      treeSelectData.value.unshift({id: 0,  name: '无'});
    };
    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };
      isAdd.value = true;

      treeSelectData.value = Tool.copy(docs.value);
      // 为选择树添加一个无
      treeSelectData.value.unshift({id: 0,  name: '无'});
    };
    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete(`/doc/${id}`).then((response) => {
        const data = response.data;
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
      axios.get("/doc/list", {
        params:{
          page: searchValue.page,
          size: searchValue.size,
          name: searchText.value.text
        }
      }).then((response) => {
        const data = response.data
        if (data.success) {
          docs.value = data.data.list;
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
        axios.post("/doc/save", doc.value).then((response) => {
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
        axios.put("/doc/update", doc.value).then((response) => {
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
      axios.get("/doc/all").then((response) => {
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
      treeSelectData,
      docs,
      columns,
      loading,
      /*--------- 对话框 ----------*/
      doc,
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
