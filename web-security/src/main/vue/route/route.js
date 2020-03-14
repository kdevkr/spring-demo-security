import Vue from 'vue'
import VueRouter from 'vue-router'

// const Blank = () => import(/* webpackChunkName: "common-view" */ '~/vue/common/blank')
// const Index = () => import(/* webpackChunkName: "index-view" */ '~/vue/page/index')
// const Main = () => import(/* webpackChunkName: "main-view" */ '~/vue/page/main')

import Blank from '~/vue/common/blank'
import Index from '~/vue/page/index'
import Main from '~/vue/page/main'

Vue.use(VueRouter)

const routes = [
    {
        path: '',
        component: Index,
        meta: {
            title: 'index'
        }
    },
    {
        path: '/:service(s1)',
        component: Blank,
        meta: {
            title: ''
        },
        children: [
            {
                path: '',
                component: Main
            }
        ]
    }
]

export default new VueRouter({
    mode: 'history',
    routes
})