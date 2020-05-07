import { getStuLoginUser } from '../../utils/api'
const state = {
    cardNumber: '',
    pswCheck: false,
    userId: '',
}

const getters = {
}

const actions = {
    getStuLoginUser(context, pasword) {
        getStuLoginUser({
            cardNumber: context.state.cardNumber,
            type: pasword?"2":"1",      //如果有密码为空间登陆， 否则为课表登陆
            pasword: pasword
        }).then(data => {
            context.userId = data.userId
        })
    }
}

const mutations = {
    setCardNumber(state, payload) {
        state.cardNumber = payload
    },
    resetClockIn() {
        state.cardNumber = '',
        state.pswCheck = false
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})