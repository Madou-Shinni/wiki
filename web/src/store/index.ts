import { createStore } from 'vuex'

const store = createStore({
  state: {
    user: {}
  },
  mutations: {
    // state -> 全局变量 ; user -> 外部传进来的值
    setUser(state,user) {
      state.user = user
    }
  },
  actions: {
  },
  modules: {
  }
})
export default store;
