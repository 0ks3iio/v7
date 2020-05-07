//需要先引用weike.js storage.js
//用了判断是否是从微课接入
var WeikeConstants = {
	WEIKE_FLAG_KEY : "weike_flag_key",
	WEIKE_FLAG_VALUE_TYPE_1 : "1",//旧版对接、微办公
	WEIKE_FLAG_VALUE_TYPE_2 : "2",//新版对接，微课工作界面
}

$(function(){
	if (weikeJsBridge) {
		weikeJsBridge.windowClose(".mui-btn-close");//绑定关闭事件
	}else{
		$(".mui-btn-close").hide();
	}
});

//微课返回方法
function wkGoBack(){
	if($(".mui-action-back").length > 0){//有返回事件  则触发返回事件
		$(".mui-action-back").click();
	}else{//否则触发关闭事件
		if($(".mui-btn-close").length > 0){
			$(".mui-btn-close").click();
		}
	}
}

//微课新版工作返回方法--旧版微办公返回到微办公界面而新版工作上的办公模块  则需要返回到微课界面
function initWieikeGoBack(){
	var isNewWeikeFlag = storage.get(WeikeConstants.WEIKE_FLAG_KEY);
	if(WeikeConstants.WEIKE_FLAG_VALUE_TYPE_2 == isNewWeikeFlag){//如果是跟新版微课对接 返回则退回到微课界面
		$("#cancelId").removeClass("mui-action-back");
	}else{
		if(!$("#cancelId").hasClass("mui-action-back")){
			$("#cancelId").addClass("mui-action-back");
		}
	}
}

//移动OA返回方法
function back(){
	if($(".mui-action-back").length > 0){//有返回事件  则触发返回事件
		$(".mui-action-back").click();
	}
}
