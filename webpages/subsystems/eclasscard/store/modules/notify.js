import { getBulletin, getTopbulletin } from '../../utils/api'

const state = {
    notify: [],
    notice: []
}

const getters = {
    getNotifyList: (state) => {
        return state.notify
    },
}

const actions = {
    getNotify(context) {
        getBulletin().then(data => {
            context.commit('setNotify', data.data)
        }).catch(err => {
            
        })
    },
    getTopNotice(context) {
        getTopbulletin().then(data => {
            context.commit('setNotice', data.data)
        })
    }
}

const mutations = {
    setNotify(state,payload) {
        state.notify = payload
    },  
    setNotice(state, payload) {
        state.notice = payload
    },
    changeOrder(state) {
        let firstOrder = state.notify.shift()
        state.notify.push(firstOrder)
    },
    changeNotice(state) {
        let firstNotice = state.notice.shift()
        state.notice.push(firstNotice)
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})