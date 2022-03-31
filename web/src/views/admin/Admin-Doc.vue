<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form layout="inline" :model="searchText">
            <a-form-item>
              <a-button type="primary" @click="add()">
                新增
              </a-button>
            </a-form-item>
          </a-form>
          <br/>
          <a-table
              :key="tableKey"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="docs"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{ record.sort }} {{ text }}
            </template>
            <!--record代表一整行的数据,传递给edit方法-->
            <template v-slot:action="{text,record}">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="doc">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>

            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent">
                内容预览
                <EyeOutlined />
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-drawer
          width="900"
          placement="right"
          :closable="false"
          v-model:visible="drawerVisible"
      >
        <div class="wangEditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
</template>




<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import E from 'wangeditor'
import hljs from "highlight.js";
import 'highlight.js/styles/monokai-sublime.css'

export default defineComponent({
  name: 'AdminDoc',
  // vue3新函数，组件初始会执行
  setup() {
    // 富文本编辑工具
    let editor: any;
    //tableKey
    const tableKey = ref();
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
        slots: {customRender: 'name'}
      },
      {
        title: 'Action',
        dataIndex: 'action',
        slots: {customRender: 'action'}
      },
    ];

    const treeSelectData = ref();// 1.因为树型选择节点的的组件会随当前编辑的节点数据变化
    treeSelectData.value = []; // 2.所以定义了一个新变量来增加根节点选项
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const isAdd = ref<boolean>(true);

    /**
     * 富文本预览
     */
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    }

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
     * 赋值递归的方法
     *  存文档id用来递归删除用
     */
    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any)=>{
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if(node.id === id) {
          ids.push(id);
          const children = node.children;
          if(Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children,children[j].id)
            }
          }
        }else {
          // 如果当前节点不是目标节点，则到其子节点再找找看
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    }

    /**
     * 编辑
     */
    const edit = (record: any) => {
      isAdd.value = false;
      // 清空富文本框的内容
      editor.txt.html("");
      // 通过Tool来复制一个新对象不让他影响列表数据
      doc.value = Tool.copy(record);
      // 等待doc.value有值，查询文档内容
      handleQueryContent();
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
      // 清空富文本框的内容
      editor.txt.html("");
      isAdd.value = true;

      treeSelectData.value = Tool.copy(docs.value);
      // 为选择树添加一个无
      //treeSelectData.value.unshift({id: 0,  name: '无'});
    };
    /**
     * 删除
     */
    // 二次确认框
    const showConfirm = (id: number) => {
      Modal.confirm({
        title: () => '重要提示：您确定要删除吗?',
        icon: () => createVNode(ExclamationCircleOutlined),
        content: () => createVNode('div', { style: 'color:red;' }),
        onOk() {
          getDeleteIds(docs.value,id);// 递归拿到ids
          axios.delete(`/doc/${ids.join(",")}`).then((response) => {
            const data = response.data;
            if (data.success) {// 保存成功对话框消失，loading效果消失
              message.success("删除成功！");
              // 重新加载列表数据
              ids.length = 0;// 清空数组
              handleQuery()
            }
          })
        },
        class: 'test',
      });
    };
    // 确认框
    const handleDelete = (id: number) => {
      showConfirm(id);
    }

    const handleSave = () => {
      doc.value.content = editor.txt.html();
      // 判断是否新增
      if (isAdd.value) {
        // 新增
        axios.post("/doc/save", doc.value).then((response) => {
          const data = response.data;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            message.success("保存成功！")
            //isAdd.value = false;
            //doc.value = {};
            // 重新加载列表数据
            handleQuery()
          }else {
            message.error(data.message);
          }
        })
      } else {
        // 修改
        axios.put("/doc/update", doc.value).then((response) => {
          const data = response.data;
          if (data.success) {// 保存成功对话框消失，loading效果消失
            message.success("修改成功！");
            // 重新加载列表数据
            handleQuery()
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
    const handleQuery = () => {
      loading.value = true;
      // 参数二必须用{params:{}} 或者 {params}简化写法
      axios.get(`/doc/all/${route.query.ebookId}`).then((response) => {
        loading.value = false
        const data = response.data
        if (data.success) {
          if(data.data.length != 0) {
            docs.value = data.data;
            // 父文档下拉框初始化，相当于新增
            treeSelectData.value = Tool.copy(docs.value);
          }
          // 修改tableKey使之可以展开
          tableKey.value = Math.random();
          // 添加一个根节点 '无'
          treeSelectData.value.unshift({id: 0, name: '无'})
        } else {
          message.error(data.message);
        }
      })
    };

    /**
     * 内容查询
     */
    const handleQueryContent = () => {
      axios.get(`/doc/findContent/${doc.value.id}`).then((response) => {
        loading.value = false
        const data = response.data
        if (data.success) {
          editor.txt.html(data.data);
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
      setTimeout(()=>{
        editor = new E('#content');
        editor.config.zIndex = 0;
        // 挂载highlight插件
        editor.highlight = hljs
        editor.create();
      },100)
    });

    // 返回数据让页面能够使用
    return {
      tableKey,
      treeSelectData,
      docs,
      columns,
      loading,

      doc,
      isAdd,
      showConfirm,

      edit,
      add,
      handleDelete,

      handleSave,

      /** 富文本预览 */
      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    }
  }
});
</script>
