import { getTime } from '../../utils/api'

const state = {
    time: 946656000,
}

const getters = {
    year: (state, getters) => {
        let time = new Date(state.time)
        return time.getFullYear()
    },
    mounth: (state) => {
        let time = new Date(state.time)
        return time.getMonth() + 1
    },
    date: (state) => {
        let time = new Date(state.time)
        return time.getDate()
    },
    hour: (state) => {
        let time = new Date(state.time)
        return time.getHours() < 10 ? '0' + time.getHours() : time.getHours()
    },
    minutes: (state) => {
        let time = new Date(state.time)
        return time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes()
    },
    seconds: (state) => {
        let time = new Date(state.time)
        return time.getSeconds()
    },
    day: (state) => {
        let time = new Date(state.time)
        const l = ["日","一","二","三","四","五","六"]
        return `星期${l[time.getDay()]}`
    }
}

const actions = {
    getTime (context, state) {
        getTime().then(data => {
            let time = data.data
            context.commit('setTime', time)
            let addTime = () => {
                time += 1000
                let times = parseInt(time / 1000)
                if(!(times%60)) {                   //每过一分钟去判断课程表
                    context.dispatch('getCoursesForm', false)
                }
                context.commit('setTime', time)
                setTimeout(addTime, 1000);
            };
            setTimeout(addTime , 1000);
        }).catch(err => {
            let time = new Date().getTime()
            context.commit('setTime', time)
            let addTime = () => {
                time += 1000
                context.commit('setTime', time)
                setTimeout(addTime, 1000);
            };
            setTimeout(addTime , 1000);
        })
    },
}

const mutations = {
    setTime (state, time) {
        state.time = time
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})