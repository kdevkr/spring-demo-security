import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import Vuelidate from 'vuelidate'
import axios from 'axios'
import '~/vue/common/css/common.scss'

Vue.use(BootstrapVue)
Vue.use(Vuelidate)

axios.defaults.withCredentials = true
Vue.prototype.$http = axios
window.$http = axios