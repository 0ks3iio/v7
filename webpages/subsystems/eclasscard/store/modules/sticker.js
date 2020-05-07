import { getSticker } from '../../utils/api'

const state = {
    sticker: '',
    type: 3,
}

const getters = {
}

const actions = {
    getSticker(context, id) {
        getSticker({id: id}).then(data => {
            context.commit('setSticker', data.data)
        })
    },
}

const mutations = {
    setSticker(state, payload) {
        state.sticker = payload
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})