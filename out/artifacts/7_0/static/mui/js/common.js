
/**
 * 整个页面刷新
 * @param url
 */
function load(url){
	var _form = document.getElementById("_macro_form_id");
    _form.action = url;
    _form.submit();
}

function loadByHref(url){
	location.href=url;
}

/**
 * ajax form表单提交
 * @param url 
 * @param formObj form元素 如： "#form"
 * @param callback 成功回调函数
 */
function ajaxSubmit(url, formObj, callback){
	var options = {
       url:url,
       success : callback,
       dataType : 'json',
       clearForm : false,
       resetForm : false,
       type : 'post',
       timeout : 30000 
    };
			
    $(formObj).ajaxSubmit(options);
}
	    
//==============mui.js============================================================================
var _default_btn_title = '提示';
var _default_btn_submit = '确认';
var _default_btn_cancel = '取消';
/**
 * @param message Type: String 提示对话框上显示的内容
 * @param title Type: String 提示对话框上显示的标题
 * @param btnValue Type: String 提示对话框上按钮显示的内容
 * @param callback Type: Function 提示对话框上关闭后的回调函数
 */
function alertMsg(message, title, btnText, callback){
	if(!isNotBlank(title))
		title = _default_btn_title;
	if(!isNotBlank(btnText))
		btnText = _default_btn_submit;
	
	mui.alert(message, title, btnText, callback);
}

/**
 * @param message Type: String 提示对话框上显示的内容
 * @param title Type: String 提示对话框上显示的标题
 * @param submitText Type: String 提示对话框上确认按钮显示的内容
 * @param cancelText Type: String 提示对话框上取消按钮显示的内容
 * @param callback Type: Function 提示对话框上关闭后的回调函数
 */
function confirmMsg(message, title, submitText, cancelText, callback, cancelCallback){
	if(!isNotBlank(title))
		title = _default_btn_title;
	if(!isNotBlank(submitText))
		submitText = _default_btn_submit;
	if(!isNotBlank(cancelText))
		cancelText = _default_btn_cancel;
	mui.confirm(message, title, new Array(cancelText,submitText), function(e){
		if(e.index == 1){//确认
			if (isNotBlank(callback)) {
				if (callback instanceof Function) {
					eval(callback)();
				} else {
					eval(callback);
				}
			}
		}else{//取消
			if (isNotBlank(cancelCallback)) {
				if (cancelCallback instanceof Function) {
					eval(cancelCallback)();
				} else {
					eval(cancelCallback);
				}
			}
		}
	});
}

/**
 * @param message Type: String 消息框显示的文字内容
 * @param millisec 持续显示时间(毫秒),默认值2000ms
 */
function toastMsg(message, millisec){
	if(!isNotBlank(millisec)){
		millisec = 2000;
	}
	mui.toast(message,{ duration:millisec, type:'div' })
}

//==============validate.js============================================================================

/**
 * 判断按钮是否可用
 * true 可用
 */
function isActionable(obj) {
	if($(obj).hasClass("mui-btn-grey"))
		return false;
	else
		return true;
}

/**
 * 设置按钮不可用
 */
function setDisabled(obj){
	if($(obj).hasClass("mui-btn-green")){
		$(obj).removeClass("mui-btn-green");
	}
	$(obj).addClass("mui-btn-grey");
}

/**
 * 设置按钮恢复默认状态
 */
function setDefault(obj){
	if($(obj).hasClass("mui-btn-grey")){
		$(obj).removeClass("mui-btn-grey");
	}
	$(obj).addClass("mui-btn-green");
}

/**
 * 检验是否为空
 */
function isNotBlank(str){
	if(typeof(str)!='undefined' && str!='undefined' && str != null && str != 'null' && str!=''){
		if("" != $.trim(str)){
			return true;
		}else{
			return false;
		}
	}else{
		return false;
	}
}

/**
 * 计算字符数(一个汉子占两个字符)
 * @param Str
 * @returns
 */
function  getLength(Str){
	var i, len, code;
	if (Str == null || Str == "")
		return 0;
	len = Str.length;
	for (i = 0; i < Str.length; i++) {
		code = Str.charCodeAt(i);
		if (code > 255) {
			len++;
		}
	}
	return len;   
}



//================file=====================================================================

/**
* 文件上传前预览 file(file对象)， fileObj预览的img 如:"#img"
* callback 回调函数
*/
function previewFile(file, fileObj, callback){
	if(window.FileReader){  //支持filereader
		var reader = new FileReader();
	    //读取文件过程方法
	    reader.onloadstart = function (e) {
	        //console.log("开始读取....");
	    }
	    reader.onprogress = function (e) {//进度条
	    	//console.log(e.loaded+","+e.total);
	    	if (e.lengthComputable) {
			      //var percentLoaded = Math.round((e.loaded/e.total) * 100);
			      //console.log(percentLoaded);
    	    }
	    }
	    reader.onabort = function (e) {
	        //console.log("中断读取....");
	    }
	    reader.onerror = function (e) {
	        //console.log("读取异常....");
	    }
	    reader.onload = function (e) {
	        //console.log("成功读取....");
	        //img.src = e.target.result;
	        $(fileObj).get(0).src = e.target.result;
	        //调用回调函数
	        if (callback instanceof Function) {
				eval(callback)();
			} else {
				eval(callback);
			}
	    }
	    reader.readAsDataURL(file);
  }else{  
  	alertMsg("您的手机不支持文件上传");
  }

}