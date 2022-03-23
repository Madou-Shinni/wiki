<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <template #title>
              <span>
                <user-outlined />
                subnav 1
              </span>
          </template>
          <a-menu-item key="1">option1</a-menu-item>
          <a-menu-item key="2">option2</a-menu-item>
          <a-menu-item key="3">option3</a-menu-item>
          <a-menu-item key="4">option4</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
              <span>
                <laptop-outlined />
                subnav 2
              </span>
          </template>
          <a-menu-item key="5">option5</a-menu-item>
          <a-menu-item key="6">option6</a-menu-item>
          <a-menu-item key="7">option7</a-menu-item>
          <a-menu-item key="8">option8</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <template #title>
              <span>
                <notification-outlined />
                subnav 3
              </span>
          </template>
          <a-menu-item key="9">option9</a-menu-item>
          <a-menu-item key="10">option10</a-menu-item>
          <a-menu-item key="11">option11</a-menu-item>
          <a-menu-item key="12">option12</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <pre>{{ebooks}}</pre>
      <pre>{{ebooks2}}</pre>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref,toRef} from 'vue';
import axios from "axios";

export default defineComponent({
  name: 'Home',
  // vue3新函数，组件初始会执行
  setup() {
    // ref: 响应式数据(获取和赋值都需要.value)
    const ebooks = ref();
    // reactive: 里面一般是放一个对象
    const ebooks1 = reactive({books:[]});
    onMounted(()=>{
      axios.get("http://localhost:8081/ebook/list?name=spring").then(
          (response)=>{
            const data = response.data
            // 我们后端封装的返回数据的数据集是对象.data
            ebooks.value = data.data;
            ebooks1.books = data.data;
          })
    });

    // 返回数据让页面能够使用
    return {
      ebooks,
      // toRef：需要有一个变量(变量名可以随意取)赋值！可以把一个对象返回成响应式数据
      // 参数1：对象， 参数2：对象的属性
      ebooks2:toRef(ebooks1,"books")
    }
  }
});
</script>
