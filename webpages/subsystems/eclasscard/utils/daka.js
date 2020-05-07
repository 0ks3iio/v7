import request from '../utils/api'
import store from '../store'

window.eccClockIn = (cardNumber) => {
	if(!cardNumber||cardNumber==''||cardNumber=='0'){
		// showMsgTip("无效卡");
		return;
    }
    let url = '/eccShow/eclasscard/clockIn'
    let body = {"cardNumber":cardNumber,"objectId":"${classAttence.id!}"}
    let method = 'post'
    request(url, body, method).then( data => {
        var result = JSON.parse(data);
        if(result.haveStu){
            store.commit('setCardNumber', cardNumber)
            // showStuClockMsg(result);
            if(result.status!=2){
                if(result.ownerType=='2'){
                    // showTeaSuccess(result);
                }else{
                    // showStuSuccess(result);
                }
            }
        }else{
            showMsgTip(result.msg);
        }
    }).catch( err => {

    })
    
    
    // $.ajax({
    //     url:'${request.contextPath}/eccShow/eclasscard/clockIn',
    //     data:{"cardNumber":cardNumber,"deviceNumber":_deviceNumber,"objectId":"${classAttence.id!}"},
    //     type:'post',
    //     success:function(data){
    //         var result = JSON.parse(data);
    //         if(result.haveStu){
    //         	showStuClockMsg(result);
    //         	if(result.status!=2){
    //         		if(result.ownerType=='2'){
	//             		showTeaSuccess(result);
    //         		}else{
	//             		showStuSuccess(result);
    //         		}
    //         	}
    //         }else{
    //         	showMsgTip(result.msg);
    //         }
    //     },
    //     error : function(XMLHttpRequest, textStatus, errorThrown) {
    //     }
    // });
}
