import { getStuSchedule } from '../../utils/api'

const state = {
    curIndex: 0,
    coursesForm:[],
    objectId: '', //课程id
}

const getters = {

}

const actions = {
    getCoursesForm(context, fetch=true) {
        if(fetch) {
            getStuSchedule().then(data => {
                let curIndex = -1;
                data.data.forEach((element,index) => {
                    let time = element.time;
                    let temp = time.split("~");
                    let start = temp[0].split(":")[0]*3600 + temp[0].split(":")[1]*60;
                    let end = temp[1].split(":")[0]*3600 + temp[1].split(":")[1]*60;
                    let date = new Date();
                    let now = date.getHours()*3600 + date.getMinutes()*60;
                    if(now > end){
                        element.status = "pass";
                    }else if(now >= start && now <= end){
                        curIndex = index
                        element.status = "current";
                    }else{
                        curIndex = curIndex >= 0 ? curIndex : index;
                        element.status = "future";
                    }
                });
                context.commit('setCoursesForm', data.data);
                context.commit('setCurIndex', curIndex < 0 ? 0 : curIndex)
            }).catch(err => {
                
            })
        } else {
            let curIndex = -1;
            context.state.coursesForm.forEach((element,index) => {
                let time = element.time;
                let temp = time.split("~");
                let start = temp[0].split(":")[0]*3600 + temp[0].split(":")[1]*60;
                let end = temp[1].split(":")[0]*3600 + temp[1].split(":")[1]*60;
                let date = new Date();
                let now = date.getHours()*3600 + date.getMinutes()*60;
                if(now > end){
                    element.status = "pass";
                }else if(now >= start && now <= end){
                    curIndex = index
                    element.status = "current";
                }else{
                    curIndex = curIndex >= 0 ? curIndex : index;
                    element.status = "future";
                }
            });
            context.commit('setCurIndex', curIndex < 0 ? 0 : curIndex)
        }
    }
}

const mutations = {
   setCoursesForm(state, payload) {
      state.coursesForm = payload
   },
   setCurIndex(state, payload) {
        state.curIndex = payload
   },
   setObjectId(state, payload) {
       state.objectId = payload
   }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})