<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined/>{{ item.name }}</span>
          </template>
          <a-menu-item :key="child.id" v-for="child in item.children">
            <MailOutlined/>
            <span>{{ child.name }}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <TheWelcome/>
      </div>
      <!--列表数据-->
      <!--:grid="{gutter:20,column: 3}" 一行三列每一列间距20px-->
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{gutter:20,column: 3}"
              :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <!--标题-->
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <!--封面-->
              <template #avatar>
                <a-avatar style="width: auto" :src="item.cover"/>
              </template>
            </a-list-item-meta>
            {{ item.content }}
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import TheWelcome from "@/components/The-Welcome.vue";

export default defineComponent({
  name: 'Home',
  components: {
    TheWelcome
  },
  // vue3新函数，组件初始会执行
  setup() {
    // ref: 响应式数据(获取和赋值都需要.value)
    const ebooks = ref();
    const isShowWelcome = ref(true);

    // 列表数据分页
    const pagination = {
      // onChange: (page: number) => {
      //   console.log(page);
      // },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];

    /**
     * 查询知识库
     */
    const handleQueryEbook = () => {
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2
        }
      }).then(
          (response) => {
            const data = response.data
            // 我们后端封装的返回数据的数据集是对象.data
            ebooks.value = data.data.list;
          })
    }

    // 次级分类id
    let categoryId2 = 0;
    const handleClick = (value: any) => {
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
      // isShowWelcome.value = value.key === 'welcome';
    }

    /**
     * 查询所有分类
     */
    const level1 = ref();
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data
        if (data.success) {
          level1.value = [];
          level1.value = data.data;
        } else {
          message.error(data.message);
        }
      })
    };

    onMounted(() => {
      handleQueryCategory();
    })

    // 返回数据让页面能够使用
    return {
      handleClick,
      isShowWelcome,

      ebooks,
      pagination,
      actions,
      /* 分类菜单 */
      level1,
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
