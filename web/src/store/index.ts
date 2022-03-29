import { createStore } from 'vuex'

declare let  SessionStorage: any;
const USER = "USER";

const store = createStore({
  state: {
    // 加载的时候先去缓存里面能获取一下，获取不到才是空对象
    user: SessionStorage.get(USER) || {}
  },
  mutations: {
    // state -> 全局变量 ; user -> 外部传进来的值
    setUser(state,user) {
      state.user = user;
      SessionStorage.set(USER, user);
    }
  },
  actions: {
  },
  modules: {
  }
})
export default store;
