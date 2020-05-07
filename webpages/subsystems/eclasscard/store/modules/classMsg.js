import { getClassCardInfo, getClassInfo, getStudents, getDescription, getClassAlbum, getClassSpaceHonor, getClassStuHonor, getClassClockIn } from '../../utils/api'
// getClassHeader
const state = {
    className: '',
    classType: '',
    classInfo: {teacherName:'班主任',leaveStus:0,classStuNum:0,leaveStusName:''},
    classDescription: {},
    classAlbum: [],
    classSpaceHonor: [],
    classStuHonor: [],
    stuList: [],
}

const getters = {
    getLeaveList: state => {
        return state.leaveList.join('、') + ( state.leaveList.length > 2 ? '...' : '' )
    },
}

const actions = {
    init (context) {
        getClassCardInfo().then(data => {
            let type = data.data.type
            context.commit('setClassType', type)
            if(type == '10') {
                getClassInfo().then(data => {
                    context.commit('setClassInfo', data.data)
                }).catch(err => {
        
                })
            }
        }).catch(err => {

        })

        // getClassHeader().then(data => {
        //     context.commit('setClass', data.data)
        // }).catch(err => {

        // })
       
    },
    // 初始学生列表
    getStudents (context) {
        getStudents().then(data => {
            context.commit('setStuList', data.data)
        })
    },
    // 每节上课前获取学生列表
    getClassClockIn(context, id) {
        getChechClock().then(data => {      //需要签到
            if(data.data.id) {
                context.commit('setObjectId', id)
                getClassClockIn({id: id}).then(data => {
                    context.commit('setStuList', data.data)
                })
            }
        })
   
    },
    getDescription (context) {
        getDescription().then(data => {
            context.commit('setClassDes', data.data)
        })
    },
    getClassAlbum (context) {
        getClassAlbum().then(data => {
            context.commit('setClassAlbum', data.data)
        })
    },
    getClassSpaceHonor (context) {
        getClassSpaceHonor().then(data => {
            context.commit('setClassSpaceHonor', data.data)
        })
    },
    getClassStuHonor (context) {
        getClassStuHonor().then(data => {
            context.commit('setClassStuHonor', data.data)
        })
    },
}

const mutations = {
    setStuList (state, payload) {
        state.sutList = payload
    },
    setClass (state, className) {
        state.className = className
    },
    setClassDes(state, payload) {
        state.classDescription = payload
    },
    setClassAlbum(state, payload) {
        state.classAlbum = payload
    },
    setClassSpaceHonor(state, payload) {
        state.classSpaceHonor = payload
    },
    setClassStuHonor(state, payload) {
        state.classStuHonor = payload
    },
    setClassType (state, classType) {
        state.classType = classType
    },
    setStuList (state, stuList) {
        state.stuList = stuList
    },
    setClassInfo (state, classInfo) {
        state.classInfo = classInfo
        state.className = classInfo.className
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})