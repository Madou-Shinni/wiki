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
              @select="onSelect"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2> {{ doc.name }}</h2>
            <div>
              <span>阅读数：{{ doc.viewCount }}</span> &nbsp; &nbsp;
              <span>点赞数：{{ doc.voteCount }}</span>
            </div>
          </div>
          <a-divider style="height: 2px; background-color: #9999cc" />
          <div class="wangEditor" :innerHTML="html"></div>
          <div class="voteDiv" v-if="doc.id">
            <a-button type="primary" shape="round" size="large" @click="vote">
              <template #icon><LikeOutlined /> &nbsp;点赞：{{ doc.voteCount }} </template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<style>
/* table 样式 */
.wangEditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangEditor table td,
.wangEditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangEditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangEditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangEditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #282c34;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangEditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangEditor ul, ol {
  margin: 10px 0 10px 20px;
}

.wangEditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}

.voteDiv {
  padding: 15px;
  text-align: center;
}
</style>

<script lang="ts">
import { defineComponent, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {message} from "ant-design-vue";
import axios from "axios";
import {Tool} from "@/util/tool";
import 'highlight.js/styles/monokai-sublime.css';

export default defineComponent({
  name: "Doc",
  // vue3新函数，组件初始会执行
  setup() {
    // 路由参数
    const route = useRoute();
    // ref: 响应式数据(获取和赋值都需要.value)
    const docs = ref();
    docs.value = [];

    const doc = ref();
    doc.value = {};

    // 文档内容
    const html = ref();

    // 默认节点
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

    /**
     * 数据查询
     */
    const handleQuery = () => {
      // 参数二必须用{params:{}} 或者 {params}简化写法
      axios.get(`/doc/all/${route.query.ebookId}`).then((response) => {
        const data = response.data
        if (data.success) {
          docs.value = data.data;

          if(Tool.isNotEmpty(docs)) {
            defaultSelectedKeys.value = [docs.value[0].id];
            handleQueryContent(docs.value[0].id);
            // 将第一个节点的信息赋值给响应式变量
            doc.value = docs.value[0];
          }
        } else {
          message.error(data.message);
        }
      })
    };

    /**
     * 内容查询
     */
    const handleQueryContent = (id: number) => {
      // 参数二必须用{params:{}} 或者 {params}简化写法
      axios.get(`/doc/findContent/${id}`).then((response) => {
        const data = response.data
        if (data.success) {
          html.value = data.data
        } else {
          message.error(data.message);
        }
      })
    };

    /**
     * 点击树型组件节点
     */
    const onSelect = (selectKeys: any, info: any) => {
      if(Tool.isNotEmpty(selectKeys)) {
        // 选中某一个节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
        // 加载内容
        handleQueryContent(selectKeys[0]);
      }
    }

    /**
     * 点赞
     */
    const vote = () => {
      axios.get(`/doc/vote/${doc.value.id}`).then((response)=>{
        const data =  response.data;
        if(data.success) {
          doc.value.voteCount++;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 初始的时候也查询一次
     */
    onMounted(() => {
      handleQuery();
    });

    // 返回数据让页面能够使用
    return {
      defaultSelectedKeys,
      docs,
      html,
      onSelect,
      doc,
      vote
    }
  }
});

</script>
