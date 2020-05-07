import { getClassHonor, getStuHonor, } from '../../utils/api'

const state = {
    classHonor: [],
    stuHonor: [],
}

const getters = {
    
}

const actions = {
    getClassHonor (context, state) {
        getClassHonor().then(data => {
            context.commit('setClassHonor', data.data)
        }).catch(err => {

        })
       
    },
    getStuHonor (context, state) {
        getStuHonor().then(data => {
            context.commit('setStuHonor', data.data)
        }).catch(err => {
            
        })
    }
}

const mutations = {
    setClassHonor (state, payload) {
        state.classHonor = payload
    },
    setStuHonor (state, payload) {
        state.stuHonor = payload
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})