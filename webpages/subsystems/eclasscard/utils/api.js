import "whatwg-fetch"
import { host, query } from '../store/index.js'
// 开发环境使用proxy代理  生产环境使用后端定义的地址
const Host = (process.env.NODE_ENV === 'development') ? "/api" : host

export default Request = (url, body, method = 'get') => {
    return new Promise((resolve, reject) => {
        let isSuccess = true;
        let path = (Host + url).trim()
         
        // 后端定义    必带参数DeviceNumber
        body = (body && body.constructor === Object) ? body : {}
        Object.assign(body, query);

        // 将参数遍历至请求路径中
        if(method === 'get' && body) {
            path += '?'
            for (let i in body) {
                path += `${i}=${body[i]}&`
            }
        }

        fetch(path, {
            method,
            headers: { },
            body: (body && (method === 'post')) ? JSON.stringify(body) : body
        }).then((response) => {
            isSuccess = response.ok;
            return response.json();
        }).then((response) => {
            console.log('请求','---url:'+url,'---body:'+JSON.stringify(body),'---method:'+method)
            console.log('返回',response)                
            if (isSuccess) {
                if(response.success){
                    resolve(response);
                } else {
                    reject(response.msg)
                }
            } else {
                reject(response);
            }
        }).catch((error) => {
            reject(error);
        });
    })
}

// 获取时间
export let getTime = (body) => {
    return  Request(getTimeUrl,body);
}
// 检查设备
export let checkDevice = (body) => {
    return  Request(checkDeviceUrl,body);
}
// 获取websocket请求链接
export let getWebSocket = (body) => {
    return  Request(getWebSocketUrl,body);
}
// 班牌信息
export let getClassCardInfo = (body) => {
    return  Request(getClassCardInfoUrl,body);
}
// 头部名称
// export let getClassHeader = (body) => {
//     return  Request(getClassHeaderUrl,body, 'post');
// }
// 行政班信息
export let getClassInfo = (body) => {
    return  Request(getClassInfoUrl,body);
}
// 班级荣誉
export let getClassHonor = (body) => {
    return  Request(getClassHonorUrl,body);
}
// 学生荣誉
export let getStuHonor = (body) => {
    return  Request(getStuHonorUrl,body);
}
// 相册
export let getAlbum = (body) => {
    return  Request(getAlbumUrl,body);
}
// 普通公告
export let getBulletin = (body) => {
    return  Request(getBulletinUrl,body);
}
// 首页课表
export let getStuSchedule = (body) => {
    return  Request(getStuScheduleUrl,body);
}
//顶栏公告
export let getTopbulletin = (body) => {
    return  Request(topbulletinUrl,body);
}
//全屏公告   参数：id                               //????????????????????????
export let fullbulletin = (body) => {
    return  Request(fullbulletinUrl,body);
}
//班级空间-首页         同首页  暂时不用
export let getClassSpace = (body) => {
    return  Request(getClassSpaceUrl,body);
}
//班级空间-班内学生
export let getStudents = (body) => {
    return  Request(getStudentsUrl,body);
}
//班级空间-班级简介
export let getDescription = (body) => {
    return  Request(getDescriptionUrl,body);
}
//班牌班级空间-相册
export let getClassAlbum = (body) => {
    return  Request(getClassAlbumUrl,body);
}
//班级空间-班级荣誉
export let getClassSpaceHonor = (body) => {
    return  Request(getClassSpaceHonorUrl,body);
}
//班级空间-个人荣誉
export let getClassStuHonor = (body) => {
    return  Request(getClassStuHonorUrl,body);
}
//班牌打卡记录展示
export let getClockin = (body) => {                 //??????????????
    return  Request(getClockinUrl,body);
}
//获取考场门贴              id
export let getSticker = (body) => {         
    return  Request(getStickerUrl,body, 'post');
}
//检测是否上课考勤  //返回id
export let getChechClock = (body) => {
    return  Request(getChechClockUrl,body);
}
//进入上课考勤页面             参数：id  
export let getClassClockIn = (body) => {
    return  Request(getClassClockInUrl,body, 'post');
}
// 打卡签到                    参数：objectId（上课考勤，带上考勤id）   cardNumber
export let clockIn = (body) => {
    return  Request(clockInUrl,body);
}
// 打卡触发
export let clock = (body) => {
    return  Request(clockUrl,body, 'post');
}

/************************************* 学生空间 *************************************/
// 学生空间-每天的课程数
export let getLessonCount = (body) => {
    return  Request(lessonCountUrl,body);
}
// 学生空间-主页
export let getStudentIndex = (body) => {
    return  Request(studentIndexUrl,body);
}
// 学生空间-请假申请页面
export let getStuLeaveApply = (body) => {
    return  Request(stuLeaveApplyUrl,body);
}
// 学生空间-我的荣誉
export let getStudentHonor = (body) => {
    return  Request(studentHonorUrl,body);
}
// 学生空间登录验证
export let getStuLoginUser = (body) => {
    return  Request(stuLoginUserUrl,body, 'post');
}
// 学生空间-我的课表
export let getStudentSchedule = (body) => {
    return  Request(studentScheduleUrl,body);
}

// 获取时间
const getTimeUrl = '/eccShow/eclasscard/backend/get/system/nowtime'
// 检查设备
const checkDeviceUrl = '/eccShow/eclasscard/backend/check/devicenumber'
// 获取websocket请求链接
const getWebSocketUrl = '/eccShow/eclasscard/backend/websocketurl'
// 班牌信息
const getClassCardInfoUrl = '/eccShow/eclasscard/backend/show/info'
// 头部名称         由行政班信息获得
// const getClassHeaderUrl = '/eccShow/eclasscard/backend/show/header'
// 行政班信息
const getClassInfoUrl = '/eccShow/eclasscard/backend/showindex/class/info'
// 班级荣誉
const getClassHonorUrl = '/eccShow/eclasscard/backend/class/honorlist'
// 学生荣誉
const getStuHonorUrl = '/eccShow/eclasscard/backend/student/honorlist'
// 相册
const getAlbumUrl = '/eccShow/eclasscard/backend/showindex/album'
// 普通公告
const getBulletinUrl = '/eccShow/eclasscard/backend/showindex/bulletin'
// 首页课表
const getStuScheduleUrl = '/eccShow/eclasscard/backend/showindex/stuschedule'
//顶栏公告
const topbulletinUrl = '/eccShow/eclasscard/backend/showindex/topbulletin'
//全屏公告   参数：id  
const fullbulletinUrl = '/eccShow/eclasscard/backend/showindex/fullbulletin'
//班级空间-首页
const  getClassSpaceUrl = '/eccShow/eclasscard/backend/classspace/index'
//班级空间-班内学生
const  getStudentsUrl = '/eccShow/eclasscard/backend/classspace/students'
//班级空间-班级简介
const  getDescriptionUrl = '/eccShow/eclasscard/backend/classspace/description'
//班牌班级空间-相册
const  getClassAlbumUrl = '/eccShow/eclasscard/backend/classspace/album'
//班级空间-班级荣誉
const  getClassSpaceHonorUrl = '/eccShow/eclasscard/backend/classspace/classhonor'
//班级空间-个人荣誉
const  getClassStuHonorUrl = '/eccShow/eclasscard/backend/classspace/stuhonor'
//班牌打卡记录展示
const getClockinUrl = '/eccShow/eclasscard/backend/showindex/clockin'
//获取考场门贴
const getStickerUrl = '/eccShow/eclasscard/backend/exam/door/sticker'
//检测是否上课考勤
const getChechClockUrl = '/eccShow/eclasscard/backend/classClockIn/chechClock'
//进入上课考勤页面             参数：id  
const getClassClockInUrl = '/eccShow/eclasscard/backend/classClockIn/index'
// 打卡签到                     参数：objectId（上课考勤，带上考勤id）   cardNumber
const clockInUrl = '/eccShow/eclasscard/backend/clockIn'
// 打卡触发
const clockUrl = '/eccShow/eclasscard/clockIn'


/************************************* 学生空间 *************************************/
// 学生空间-每天的课程数
const lessonCountUrl = '/eccShow/eclasscard/backend/lessonCount'
// 学生空间-主页
const studentIndexUrl = '/eccShow/eclasscard/backend/studentSpace/index'
// 学生空间-请假申请页面
const stuLeaveApplyUrl = '/eccShow/eclasscard/backend/studentSpace/leaveapply'
// 学生空间-我的荣誉
const studentHonorUrl = '/eccShow/eclasscard/backend/studentSpace/stuhonor'
// 学生空间登录验证
const stuLoginUserUrl = '/eccShow/eclasscard/backend/stuLoginUser/page'
// 学生空间-我的课表
const studentScheduleUrl = '/eccShow/eclasscard/backend/stuSchedule'
