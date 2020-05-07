_keydown = 0;
_prekeydown = -1;
_touch = false;

var interval_timer = null;//setInterval全局变量，用于关闭
var date_timepicker = null;//date_timepicker全局变量，用于关闭

String.prototype.replaceAll = function(s1,s2){ 
	return this.replace(new RegExp(s1,"gm"),s2); 
}


$.ajaxSetup({
	cache : false
		// 关闭AJAX相应的缓存
});

$(document).keyup(function(event) {
	if (event.which == 17 && _keydown == 0) _keydown = 1;
	else if (event.which == 16 && _keydown == 1) _keydown = 2;
	else if (event.which == 20 && _keydown == 2) _keydown = 3;
	else _keydown = 0;
	if (event.which == 27) {
		if ($(".layui-layer").length > 0) {
			if (_prekeydown != 27) {
				layer.msg('再按一次ESC关闭', {
					time: 1000 //2秒关闭（如果不配置，默认是3秒）
				}, function() {
					_prekeydown = -1;
				});
			} else {
				layer.close($(".layui-layer").attr("times"));
				setTimeout(function() {
					_prekeydown = -1;
				}, 1000);

			}
		}
	}
	_prekeydown = event.which;
});

/**
 * @param type warn 警告 success成功 error失败 prompt 提示  
 * @param title 
 * @param msg 必须
 * @param yesFunction 
 * @param cancelFunction 
 */
function showConfirmTips(type,title,msg,yesFunction,cancelFunction){
	var icon=3;
	if(type =="success")
		icon=1;
	else if(type =="error")
		icon=2;
	else if(type =="warn")
		icon=0;
		
	if(!title)title='提示';
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	if(!(typeof cancelFunction === "function")){
		cancelFunction = function (index){layer.close(index);}
	}
	var content ='<p class="b">'+title+'</p><p>'+msg+'</p>'
	layer.msg(content, {
		icon: icon,
		shade: 0.2,
		time:false,
		area: ['410px', 'auto'],
		btn: ['确定', '取消'],
		yes: yesFunction,
		cancel:cancelFunction
	})
}

/**
 * @param type warn 警告 success成功 error失败 prompt 提示  
 * @param title 
 * @param msg
 * @param yesFunction 
 */
function showLayerTips4Confirm(type,msg,yesFunction){
	var icon=3;
	if(type =="success")
		icon=1;
	else if(type =="error")
		icon=2;
	else if(type =="warn")
		icon=0;
	
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	var content ='<p class="b">提示</p><p>'+msg+'</p>'
	layer.msg(content, {
		icon: icon,
		shade: 0.2,
		time:false,
		area: ['410px', 'auto'],
		btn: ['确定'],
		yes: yesFunction
	})
}

/**
 * @param type warn 警告 success成功 error失败 prompt 提示  
 * @param msg 必须
 * @param offset 't' 顶部 'b'底部 'r'右边 'l'左边 
 * @param callBackFunction 回调函数
 */
function showLayerTips(type,msg,offset,callBackFunction){
	var icon=3;
	if(type =="success")
		icon=1;
	else if(type =="error")
		icon=2;
	else if(type =="warn")
		icon=0;
	layer.msg(msg, {
		icon: icon,
		offset: offset,
		time: 2000 //2秒关闭（如果不配置，默认是3秒）
	}, function() {
		if (callBackFunction instanceof Function) {
			eval(callBackFunction)();
		} else {
			eval(callBackFunction);
		}
	});
}

function getLength(str) {
	if (str == null) return 0;
	if (typeof str != "string") {
		str += "";
	}
	return str.replace(/[^\x00-\xff]/g, "01").length;
}

function checkValue(containerName, prefix) {
	var tgs = ["INPUT:not(:file)", "SELECT", "TEXTAREA"];
	var len = tgs.length;
	var obj = new Object();
	var os;
	if (!prefix) {
		prefix = "form-group-";
	}
	for (var j = 0; j < len; j++) {
		if (containerName) {
			if (typeof(containerName) == "string") {
				os = jQuery(containerName + " " + tgs[j]);
			} else {
				len = 1; //针对单一确定元素
				os = jQuery(containerName);
			}
		} else {
			os = jQuery(tgs[j]);
		}
		if (os) {
			os.each(function() {
				$this = $(this);
				if (!$this.is(':hidden')) {
					var id = $this.attr("id");
					if (id != "") {
						$("#" + id + "-error").remove();
						$("#" + prefix + id).removeClass("has-error");
						var value = $this.val();
						var dl = $this.attr("decimalLength");
						if(!dl){
							dl= $this.attr("decimallength");
						}
						var vtype = $this.attr("vtype");
						if (vtype && vtype != "" && value && value != "") {
							if (vtype == "digits" || vtype == "int" || vtype == "long") {
								if (!/^\d+$/.test(value)) {
									obj[id] = (obj[id] ? obj[id] + "，" : "") + "请输入整数";
								}
							} else if (vtype == "number") {
								if (!/^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value)) {
									obj[id] = (obj[id] ? obj[id] + "，" : "") + "请输入正确的数字";
								}else if(dl && dl>0){
									var v;
									if (value.indexOf(".") >= 0){
										v = value.substring(value.indexOf(".")+1, getLength(value));
										if(getLength(v) > dl){
											obj[id] = (obj[id] ? obj[id] + "，" : "") + "请输入正确的数字，要求是不超过"+dl+"位小数的数字";
										}
									}
								}
							} else if (vtype == "email") {
								if (!/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(value)) {
									obj[id] = (obj[id] ? obj[id] + "，" : "") + "请输入正确的格式";
								}
							} else if (vtype == "url") {
								if (!/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test(value)) {
									obj[id] = (obj[id] ? obj[id] + "，" : "") + "请输入正确的格式";
								}
							}
						}
						var nullable = $this.attr("nullable");
						if (nullable == "false" && (!value || $.trim(value) == "")) {
							obj[id] = (obj[id] ? obj[id] + "，" : "") + "不能为空";
						}
						if (value && value != "") {
							var maxLength = $this.attr("maxLength");
							if (maxLength && maxLength > 0 && getLength(value) > maxLength) {
								obj[id] = (obj[id] ? obj[id] + "，" : "") + "内容不能超过" + maxLength + "个字节（一个汉字为两个字节）";
							}
							var minLength = $this.attr("minLength");
							if (minLength && minLength > 0 && getLength(value) < minLength) {
								obj[id] = (obj[id] ? obj[id] + "，" : "") + "最小长度不能少于" + minLength + "个字节（一个汉字为两个字节）";
							}
							var min = $this.attr("min");
							if (min && min != "" && parseFloat(min) > parseFloat(value)) {
								obj[id] = (obj[id] ? obj[id] + "，" : "") + "内容不能小于" + min;
							}
							var max = $this.attr("max");
							if (max && max != "" && parseFloat(max) < parseFloat(value)) {
								obj[id] = (obj[id] ? obj[id] + "，" : "") + "内容不能大于" + max;
							}
							var length = $this.attr("length");
							if (length && length > 0 && getLength(value) != length) {
								if (vtype == "int" || vtype == "long" || vtype == "digits") {
									obj[id] = (obj[id] ? obj[id] + "，" : "") + "长度必须为" + length + "个数字";
								} else {
									obj[id] = (obj[id] ? obj[id] + "，" : "") + "长度必须为" + length + "个字节（一个汉字为两个字节）";
								}
							}
							
							var regex = $this.attr("regex");
							var regexTip = $this.attr("regexTip");
							if(regex && !value.match(eval(regex))){
								if(regexTip){
									obj[id] = (obj[id] ? obj[id] + "，" : "") +regexTip ;
								}else{
									obj[id] = (obj[id] ? obj[id] + "，" : "") +"输入字符格式不对" ;
								}
							}
							
						}
						
					}
				}
			});
		}
	}
	var cyc = 0;
	var focusObj;
	for (key in obj) {
		cyc = 1;
		$(containerName + " #" + prefix + key).addClass("has-error");
		if (!_touch) {
			if (!focusObj)
				focusObj = $(containerName + " #" + key);
			layer.tips(obj[key], containerName + " #" + key, {
				tipsMore: true,
				tips:3		
			});
		}		
		else{
			layer.tips(obj[key], containerName + " #" + key, {
				tipsMore: true,
				tips:3				
			});
		}
	}
	if (cyc > 0) {
		if (focusObj) {
			focusObj.focus();
		}
		return false;
	}

	return true;
}

function checkParseFloat(v, dl){
	v = v.trim();
	if(v.length > 0){
		if(!dl){
			var p = /(^\+?\d+\.\d+$)|(^\+?\d+$)|(^\-\d+\.*\d+$)|(^\-\d+$)/;
			return p.test(v);
		}
		else{
			var p_ = "(^\\+?\\d+\\.\\d{1," + dl + "}$)|(^\\+?\\d+$)|(^\\-\\d+\\.*\\d{1," + dl + "}$)|(^\\-\\d+$)";
			var rg = new RegExp(p_);
			return rg.test(v);
		}
	}
	return false;
}