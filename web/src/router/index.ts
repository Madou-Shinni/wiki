import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import {Tool} from "@/util/tool";
import store from "@/store";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/admin/user',
    name: '/Admin-User.vue',
    component: () => import('../views/admin/Admin-User.vue'),
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/ebook',
    name: '/Admin-Ebook.vue',
    component: () => import('../views/admin/Admin-Ebook.vue'),
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: '/Admin-Category.vue',
    component: () => import('../views/admin/Admin-Category.vue'),
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: '/Admin-Doc.vue',
    component: () => import('../views/admin/Admin-Doc.vue'),
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/doc',
    name: '/Doc.vue',
    component: () => import('../views/Doc.vue')
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    //console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      //console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
