import Vue from 'vue'
import Vuex from 'vuex'
import time from './modules/time'
import classMsg from './modules/classMsg'
import honor from './modules/honor'
import courses from './modules/courses'
import photos from './modules/photos'
import notify from './modules/notify'
import stuMsg from './modules/stuMsg'
import sticker from './modules/sticker'
Vue.use(Vuex)

const debug = process.env.NODE_ENV !== 'production'

const state = {
    deviceNumber: '0001',       //开发版行政班0001
    view: '1',        //1.横   2竖
    domain: 'http://192.168.0.36:8020',        //域名+端口
    contextPath: '',        //上下文
}

const actions = {

}

const mutations = {
   
}

export default new Vuex.Store({
    state,
    actions,
    mutations,
    modules: {
        time,
        honor,
        notify,
        photos,
        courses,
        sticker,
        stuMsg,
        classMsg,
    },
    strict: debug,
})

let host = state.domain + state.contextPath
let query = {'deviceNumber': state.deviceNumber}
export { host, query }
