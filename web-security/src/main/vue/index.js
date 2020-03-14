import Vue from 'vue'
import App from './App'
import '~/vue/common/common'

import store from '~/vue/store/store'
import router from '~/vue/route/route'

new Vue({
    router,
    store,
    el: '#app',
    render: h => h(App)
})