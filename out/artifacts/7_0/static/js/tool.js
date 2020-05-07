_keydown = 0;
_prekeydown = -1;
_touch = false;
//$(document).ready(function(){
//	_touch = ace.vars['touch'];
//});
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
		if ($(".layui-layer:not(.layui-layer-dialog)").length > 0) {
			if (_prekeydown != 27) {
				layer.msg('再按一次ESC关闭', {
					time: 1000 //2秒关闭（如果不配置，默认是3秒）
				}, function() {
					_prekeydown = -1;
				});
			} else {
				layer.close($(".layui-layer:not(.layui-layer-dialog):last").attr("times"));
				setTimeout(function() {
					_prekeydown = -1;
				}, 1000);

			}
		}
	}
	_prekeydown = event.which;
});

function initSelect() {
}

Date.prototype.format = function(fmt) {
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function initCalendar(containerName, elements, dateType, showTime, showSeconds) {
	//如果没有指定类型，则范围时间与非范围时间都初始化一次
	if (!dateType) {
		initCalendar(containerName, elements, "rangeDate", showTime, showSeconds);
		dateType = 'date';
	}
	var ranges = {};
	if (dateType == "rangeDate") {
		ranges = {
			'今天': [moment(), moment()],
			'昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
			'明天': [moment().subtract(-1, 'days'), moment().subtract(-1, 'days')],
			'最近七天': [moment().subtract(6, 'days'), moment()],
			'最近30天': [moment().subtract(29, 'days'), moment()],
			'本月': [moment().startOf('month'), moment().endOf('month')],
			'下月': [moment().subtract(-1, 'month').startOf('month'), moment().subtract(-1, 'month').endOf('month')]
		};
		var singleDatePicker = false;
	}
	else{
		var singleDatePicker = true;
	}

	var timePicker = false;
	if (showTime) {
		timePicker = showTime;
	}
	var timePickerSeconds = false;
	if (showSeconds) {
		timePickerSeconds = showSeconds;
	}
	var s = 'input[stype=calendar][vtype=' + dateType + ']';
	if (containerName && containerName != "") {
		var cs = containerName.split(",");
		for(var c in cs){
			var os = $(cs[c] + " " + s).not('[readonly]');
			if (elements && elements != "") {
				os = os.filter(elements);
			}
			var format='';
			if(timePicker && showSeconds){
				format='YYYY-MM-DD HH:mm:ss';
			}else if(timePicker){
				format='YYYY-MM-DD HH:mm';
			}else{
				format='YYYY-MM-DD';
			}
			os.daterangepicker({
			'ranges': ranges,
			'autoApply': true,
			'opens': 'left',
			locale: {
				format: format,
				applyLabel: '确认',
				cancelLabel: '取消',
				customRangeLabel: '其他日期',
				separator: ' ~ ',
				weekLabel: '周次',
				daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
				monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
				firstDay: 1
			},
			"linkedCalendars": true,
			"autoUpdateInput": true,
			"alwaysShowCalendars": true,
			"singleDatePicker": singleDatePicker,
			"showDropdowns": true,
			"timePicker": timePicker,
			"timePicker24Hour": true,
			"timePickerSeconds": timePickerSeconds,
			"showWeekNumbers": true
			});
		}
	}
	
}

/**
 * 初始化时间控件--input标签中加入vtype="data"属性
 * @param containerName 必须 .class 或者 #id
 * @param elements 非必须，可为""，表示特定属性为XXX的
 * @param viewContent 非必须，默认是年月日形式	
 */
function initCalendarData(containerName, elements,viewContent) {
	if(!viewContent){
		/**
		 * startView、maxView、minView参数
			0	从小时视图开始，选分
			1	从天视图开始，选小时
			2	从月视图开始，选天
			3	从年视图开始，选月
			4	从十年视图开始，选年
		 */
		viewContent={
			'format' : 'yyyy-mm-dd',//格式yyyy-mm-dd hh:ii:ss
			'startView' : '2',//从选择天开始
			'maxView' : '4',//最大选择年
			'minView' : '2',//最小选择天
			'startDate':'',//可选范围-开始时间
			'endDate':'',//可选范围-结束时间
			'daysOfWeekDisabled' : []//一周的周几不能选，例如[0,6]这样表示周日和周六不能选
		}
	}else{
		if(!viewContent.format){
			viewContent['format']='yyyy-mm-dd';
		}
		if(!viewContent.startView){
			viewContent['startView']=2;
		}
		if(!viewContent.maxView){
			viewContent['maxView']=4;
		}
		if(!viewContent.minView){
			viewContent['minView']=2;
		}
		if(!viewContent.daysOfWeekDisabled){
			viewContent['daysOfWeekDisabled']=[];
		}
		if(!viewContent.startDate){
			viewContent['startDate']='';
		}
		if(!viewContent.endDate){
			viewContent['endDate']='';
		}
	}
	var s = 'input[vtype=data]';
	if (containerName && containerName != "") {
		var cs = containerName.split(",");
		for(var c in cs){
			if( typeof cs[c] == 'function'){
				continue;
			}
			var os = $(cs[c] + " " + s).not('[readonly]');
			if (elements && elements != "") {
				os = os.filter(elements);
			}
			os.attr('readonly','readonly');
			os.datetimepicker({
				language: 'zh-CN',
				format: viewContent.format,
				startView : viewContent.startView,
				maxView : viewContent.maxView,
				minView : viewContent.minView,
				startDate : viewContent.startDate,
				endDate : viewContent.endDate,
				daysOfWeekDisabled : viewContent.daysOfWeekDisabled,
				autoclose: true,
				todayHighlight: true,
				todayBtn:true
			});
		}
	}
	
}
/**
 * 初始化下拉多选控件--select标签中加入vtype="selectMore"属性
 * @param containerName 必须 .class 或者 #id
 * @param elements 非必须，可为""，表示特定属性为XXX的
 */
function initChosenMore(containerName, elements,viewContent){
	if(!viewContent){
		viewContent={
			'select_readonly':'false',//是否只读
			'width' : '100%',//输入框的宽度
			'multi_container_height' : '50px',//输入框的高度
			'results_height' : '100px',//下拉选择的高度
			'max_selected_options' : ''//限制选择个数
		}
		
	}else{
		if(!viewContent.select_readonly){
			viewContent['select_readonly']='false';
		}
		if(!viewContent.width){
			viewContent['width']='100%';
		}
		if(!viewContent.multi_container_height){
			viewContent['multi_container_height']='50px';
		}
		if(!viewContent.results_height){
			viewContent['results_height']='100px';
		}
		if(!viewContent.max_selected_options){
			viewContent['max_selected_options']='';
		}
	}
	var s = 'select[vtype=selectMore]';
	if (containerName && containerName != "") {
		var cs = containerName.split(",");
		for(var c in cs){
			if( typeof cs[c] == 'function'){
				continue;
			}
			var os = $(cs[c] + " " + s).not('[readonly]');
			if (elements && elements != "") {
				os = os.filter(elements);
			}
			os.chosen({
				select_readonly:viewContent.select_readonly,
				width:viewContent.width,
				multi_container_height:viewContent.multi_container_height,
				results_height:viewContent.results_height,
				max_selected_options:viewContent.max_selected_options,
				no_results_text:"未找到",
				disable_search:false,
				search_contains:true
			});
		}
	}
}
/**
 * 初始化下拉单选控件--select标签中加入vtype="selectOne"属性
 * @param containerName 必须 .class 或者 #id
 * @param elements 非必须，可为""，表示特定属性为XXX的
 */
function initChosenOne(containerName, elements,viewContent){
	if(!viewContent){
		viewContent={
			'allow_single_deselect':'true',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '100%',//输入框的宽度
			'results_height' : '100px'//下拉选择的高度
		}
	}else{
		if(!viewContent.allow_single_deselect){
			viewContent['allow_single_deselect']='true';
		}
		if(!viewContent.select_readonly){
			viewContent['select_readonly']='false';
		}
		if(!viewContent.width){
			viewContent['width']='100%';
		}
		if(!viewContent.results_height){
			viewContent['results_height']='100px';
		}
	}
	var s = 'select[vtype=selectOne]';
	if (containerName && containerName != "") {
		var cs = containerName.split(",");
		for(var c in cs){
			if( typeof cs[c] == 'function'){
				continue;
			}
			var os = $(cs[c] + " " + s);
			if (elements && elements != "") {
				os = os.filter(elements);
			}
			os.chosen({
				allow_single_deselect:(viewContent.allow_single_deselect == 'true'?true:false),
				select_readonly:viewContent.select_readonly,
				width:viewContent.width,
				results_height:viewContent.results_height,
				no_results_text:"未找到",
				disable_search:false,
				search_contains:true
			});
		}
	}
}

//function updatePagerIcons(table) {
//	var replacement = {
//		'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
//		'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
//		'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
//		'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
//	};
//	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
//		var icon = $(this);
//		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
//
//		if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
//	})
//}

function syncText(xmlHttpRequest) {
	var text = xmlHttpRequest.responseText;
	var begin = text.indexOf("java.lang.RuntimeException: ");
	var end = text.indexOf("org.springframework", begin);
	var text = text.substring(begin + "java.lang.RuntimeException: ".length, end);
	return text;
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
/**
 * 返回例如 searchUnitId=。。。&searchAcadyear=。。。&searchSemester=。。。
 * @param containerName
 * @returns
 */
function searchUrlValue(containerName){
	var tgs = ["INPUT:not(:file)", "SELECT", "TEXTAREA"];
	var len = tgs.length;
	var obj = '';
	for (var j = 0; j < len; j++) {
		if (containerName) {
			if (typeof(containerName) == "string") {
				var os = jQuery(containerName + " " + tgs[j]);
			} else {
				len = 1; //针对单一确定元素
				var os = jQuery(containerName);
			}
		} else {
			var os = jQuery(tgs[j]);
		}
		if (os) {
			var useMap = {};
			os.each(function() {
				var stype = $(this).attr("type");
				var value;
				if (stype == "radio") {
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					value = $("input[name='" + name + "']:checked").val();
					obj+='&'+name+"="+value
				} else if (stype == "checkbox") {
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					var vs = new Array();
					$("input[name='" + name + "']:checked").each(function() {
						vs.push($(this).val());
					});
					obj+='&'+name+"="+vs.join(",");
				} else if(stype == "text"){
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					value = $.trim($(this).val());
					obj+='&'+name+"="+encodeURIComponent(value);
				} else {
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					value = $(this).val();
					if(value == null){
						obj+='&'+name+"=";
					}else{
						obj+='&'+name+"="+value;
					}
				}
			});
		}
	}
	return obj.substring(1,obj.length);
}

function dealValue(containerName) {
	var tgs = ["INPUT:not(:file)", "SELECT", "TEXTAREA"];
	var len = tgs.length;
	var obj = new Object();
	for (var j = 0; j < len; j++) {
		if (containerName) {
			if (typeof(containerName) == "string") {
				var os = jQuery(containerName + " " + tgs[j]);
			} else {
				len = 1; //针对单一确定元素
				var os = jQuery(containerName);
			}
		} else {
			var os = jQuery(tgs[j]);
		}
		if (os) {
			var useMap = {};
			os.each(function() {
				var stype = $(this).attr("type");
				var value;
				if (stype == "radio") {
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					value = $("input[name='" + name + "']:checked").val();
					obj[name] = value;
				} else if (stype == "checkbox") {
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					var vs = new Array();
					$("input[name='" + name + "']:checked").each(function() {
						vs.push($(this).val());
					});
					obj[name] = vs.join(",");
				} else {
					var name = $(this).attr("name");
					if (!name) {
						name = $(this).attr("id");
					}
					if(!name || useMap[name]){
						return true;
					}
					useMap[name] = name;
					value = $(this).val();
					obj[name] = value;
				}
			});
		}
	}
	return JSON.stringify(obj);
}

var options_default = {
	width:1000,
	height:600,
	contentDom:false,
	title:"详情",
	max:false,
	parentHash:false,
	touch:false
	}

function dealWidth(width, maxWidth, minWidth){
	if(maxWidth && maxWidth < width){
		width = maxWidth;
	}
	if(minWidth && minWidth > width){
		width = minWidth;
	}
	var bw = $(window).width();
	if(bw < width)
		width = bw;
	return width;
}

function resizeLayer(indexDiv, width, height){
	var vheight = dealHeight($(window).height(), height, height) + "px";
	var vwidth = dealWidth($(window).width(), width, width) + "px";
	layer.style(indexDiv, {
		width:vwidth,
		height:vheight
	});
}

function dealHeight(height, maxHeight, minHeight){
	if(maxHeight && maxHeight < height){
		height = maxHeight;
	}
	if(minHeight && minHeight > height){
		height = minHeight;
	}
	var bh = $(window).height();
	if(bh < height)
		height = bh;
	return height;
}

function layerDivUrl(url, options) {
	options = options || {};

	var touch = options.touch || _touch || options_default.touch;
	var parentHash = options_default.parentHash || options.parentHash;
	var contentDom = options_default.contentDom || options.contentDom;
	var width = options.width ? options.width : options_default.width;
	width = dealWidth(width) + "px";
	var height = options.height ? options.height : options_default.height;
	height = dealHeight(height) + "px";
	var max = options_default.max || options.max;
	if(options.title == ""){
		options.title = " ";
	}
	var title = options.title || options_default.title;
	// if(touch){;
		// gotoHash(url);
		// return 0;
	// }
	// else{
		var index = layer.load(2);
		var dom = $(contentDom);
		if (dom.length <= 0) {
			dom = $("#layerContent");
		}
		if (dom.length <= 0) {
			$("body").append('<div class="row"><div id="layerContent" class="layer layui-layer-wrap"></div></div>');
			dom = $("#layerContent");
		}
		dom.load(url,
			function() {
				layer.close(index);
				indexDiv = layerDiv(width, height, dom, title, max);
				return indexDiv;
			}
		);
	// }	
}

function layerDiv(width, height, content, title, max) {
	var areaSize;
	if (height == '' && width == '') {
		areaSize = 'auto';
	} else {
		if (height == '') {
			areaSize = width;
		} else {
			areaSize = [width, height];
		}
	}
	if (content.is(':hidden')) {
		content.removeClass("hide");
	}

	var index = layer.open({
		type: 1,
		skin: 'layui-layer-demo',
		closeBtn: 1,
		shift: 2,
		shadeClose: false,
		maxmin: false,
		scrollbar: false,
		title: title,
		area: areaSize,
		content: content
	});
	if (max)
		layer.full(index);	
	$(".layui-layer").focus();
	return index;
}
/**
 * 
 * @param isSuccess boolean类型 必须
 * @param title 必须
 * @param msg 必须
 * @param width 不用写px
 * @param height 不用写px
 */
function layerTipMsg(isSuccess,title,msg,width, height){
	var areaSize;
	if (!height && !width) {
		areaSize = '355px';
	} else {
		if (!height ) {
			width = dealWidth(width) + "px";
			areaSize = width;
		} else {
			width = dealWidth(width) + "px";
			height = dealHeight(height) + "px";
			areaSize = [width, height];
		}
	}
	$(".layerTipClass").hide();
	if(isSuccess){
		$(".layerTipOpenSucc").show();
	}else{
		$(".layerTipOpenFail").show();
	}
	$(".openTisTitle").html(title);
	$(".openTisMsg").html(msg);
	var openIndex=layer.open({
		type: 1,
		shade: .5,
		title: ['提示'],
		area: areaSize,
		content: $('.layerTipOpen')
	});
	setTimeout(function(){
		layer.close(openIndex);
	},3000);
}
/**
 * 
 * @param title 必须
 * @param msg 必须
 * @param width 不用写px
 * @param height 不用写px
 */
function layerTipMsgWarn(title,msg,width, height){
	var areaSize;
	if (!height && !width) {
		areaSize = '355px';
	} else {
		if (!height ) {
			width = dealWidth(width) + "px";
			areaSize = width;
		} else {
			width = dealWidth(width) + "px";
			height = dealHeight(height) + "px";
			areaSize = [width, height];
		}
	}
	$(".layerTipClass").hide();
	$(".layerTipOpenWarn").show();
	$(".openTisTitle").html(title);
	$(".openTisMsg").html(msg);
	var openIndex=layer.open({
		type: 1,
		shade: .5,
		title: ['提示'],
		area: areaSize,
		content: $('.layerTipOpen')
	});
	setTimeout(function(){
		layer.close(openIndex);
	},3000);
}

function formatDateWithTime(cellvalue,  options,  rowObject) {
	if (!cellvalue || cellvalue == "0")
		return "";
	var datetime = new Date();
	datetime.setTime(cellvalue);
	return datetime.format("yyyy-MM-dd hh:mm:ss");
}

function formatDate(cellvalue,  options,  rowObject) {
	if (!cellvalue || cellvalue == "0")
		return "";
	var datetime = new Date();
	datetime.setTime(cellvalue);
	return datetime.format("yyyy-MM-dd");
}

function cookie(key, value, expire) {
	if (key && value && expire) {
		$.cookie(key, encodeURIComponent(value), {
			"expires": expire
		});
	} else if (key && value) {
		$.cookie(key, encodeURIComponent(value));
	} else if (key) {
		return decodeURIComponent($.cookie(key));
	}
}
/**
 * 用于修改页面控制个别关联属性
 * 用法如下：
 * $("#teacherName").addClass("OR");
	$("#teacherName").attr("orDisabled", "{'#teacherCode':'01'}");
	$("#teacherName").attr("orEnabled", "{'#teacherCode':'!01'}");//这里控制是否失效，当id=teacherCode的属性值为01时失效
	$("#form-group-teacherName").addClass("OR");
	$("#form-group-teacherName").attr("orHide", "{'[name=sex]':'2'}").attr("orHideCallBack","callBack");//隐藏时触发回调方法
	$("#form-group-teacherName").attr("orShow", "{'[name=sex]':'1'}");//这里控制是否显示，当name=sex的属性值为1时显示
	initOperationCheck('.teacherDetail');//写单个选择器
 */
function initOperationCheck(containerName) {
	if (containerName) {
		if (typeof(containerName) == "string") {
			os = jQuery(containerName + " .OR");
		} else {
			os = jQuery(containerName);
		}
	} else {
		os = $(".OR");
	}
	os.each(function(index, element) {
		var $this = $(this);
		var ordisabledstr = $this.attr("orDisabled");
		var orDisabledCallBack = $this.attr("orDisabledCallBack");
		if (ordisabledstr && ordisabledstr != "") {
			var ordisabled = eval("(" + ordisabledstr + ")");
			if (ordisabled instanceof Array) {
				for (var i = 0; i < ordisabled.length; i++) {
					orOperationVal(ordisabled[i], "change", function() {
						$this.attr("disabled", "disabled");
						if(typeof(orDisabledCallBack)!="undefined"){
							eval(orDisabledCallBack+"()");
						}
					});
				}
			} else {
				orOperationVal(ordisabled, "change", function() {
					$this.attr("disabled", "true");
					if(typeof(orDisabledCallBack)!="undefined"){
						eval(orDisabledCallBack+"()");
					}
				});
			}
		}

		var orenabledstr = $this.attr("orEnabled");
		var orEnabledCallBack = $this.attr("orEnabledCallBack");
		if (orenabledstr && orenabledstr != "") {
			var orenabled = eval("(" + orenabledstr + ")");
			if (orenabled instanceof Array) {
				for (var i = 0; i < orenabled.length; i++) {
					orOperationVal(orenabled[i], "change", function() {
						$this.removeAttr("disabled");
						if(typeof(orEnabledCallBack)!="undefined"){
							eval(orEnabledCallBack+"()");
						}
					});
				}
			} else {
				orOperationVal(orenabled, "change", function() {
					$this.removeAttr("disabled");
					if(typeof(orEnabledCallBack)!="undefined"){
						eval(orEnabledCallBack+"()");
					}
				});
			}
		}

		var orhidestr = $this.attr("orHide");
		var orHideCallBack = $this.attr("orHideCallBack");
		if (orhidestr && orhidestr != "") {
			var orhide = eval("(" + orhidestr + ")");
			if (orhide instanceof Array) {
				for (var i = 0; i < orhide.length; i++) {
					orOperationVal(orhide[i], "change", function() {
						$this.addClass("hide");
						if(typeof(orHideCallBack)!="undefined"){
							eval(orHideCallBack+"()");
						}
					});
				}
			} else {
				orOperationVal(orhide, "change", function() {
					$this.addClass("hide");
					if(typeof(orHideCallBack)!="undefined"){
						eval(orHideCallBack+"()");
					}
				});
			}
		}

		var showstr = $this.attr("orShow");
		var orShowCallBack = $this.attr("orShowCallBack");
		if (showstr && showstr != "") {
			var orshow = eval("(" + showstr + ")");
			if (orshow instanceof Array) {
				for (var i = 0; i < orshow.length; i++) {
					orOperationVal(orshow[i], "change", function() {
						$this.removeClass("hide");
						if(typeof(orShowCallBack)!="undefined"){
							eval(orShowCallBack+"()");
						}
					});
				}
			} else {
				orOperationVal(orshow, "change", function() {
					$this.removeClass("hide");
					if(typeof(orShowCallBack)!="undefined"){
						eval(orShowCallBack+"()");
					}
				});
			}
		}

		var showstr = $this.attr("orSet");
		var orSetCallBack = $this.attr("orSetCallBack");
		if (showstr && showstr != "") {
			var orset = eval("(" + showstr + ")");
			if (orset instanceof Array) {
				for (var i = 0; i < orset.length; i++) {
					orOperationVal(orset[i], "change", function() {
						$(this).val($this.val());
						if(typeof(orSetCallBack)!="undefined"){
							eval(orSetCallBack+"()");
						}
					});
				}
			} else {
				orOperationVal(orset, "change", function() {
					$(this).val($this.val());
					if(typeof(orSetCallBack)!="undefined"){
						eval(orSetCallBack+"()");
					}
				});
			}
		}
	});
}

function gotoHash(url){
	if (url.indexOf("#") == 0)
		window.location.hash = url;
	else
		window.location.hash = "#" + url;
	if(layer){
		layer.closeAll();
	}
	$(window).trigger('hashchange.ace_ajax', [false]);			
}

function confirmDiv(title, text, func){
	swal({title: title, html: true, 
		text: text,   
		type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
		cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
	}, 
	func());
}

var layerOkOptions = {
	redirect:false,
	window:false
}
// 通过layerDivUrl的操作结束后调用的函数
function doLayerOk(dom, options){
	options = options || {};
	var rf = options.redirect || layerOkOptions.redirect;
	var wf = options.window || layerOkOptions.window;
	var tt = $(dom).closest(".layui-layer");
	if(tt.length > 0){		
		wf();
	}
	else{
		rf();
	}	
}

function buttons(name, preIdName, id, color, icon, title){
	if(!title){
		title = "";
	}
	var b = "<a title=\"" + title + "\" href=\"javascript:;\" name=\"" + name + "\" value=\"" + id + "\" id=\"" + preIdName + id + "\" class=\"" + color + "\" href=\"#\"><i class=\"ace-icon fa " + icon + " bigger-130\"></i></a>"
	return b;
}

function showConfirmMsg(content,title,yesFunction,cancelFunction){
	if(!content)content='内容';
	if(!title)title='提示';
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	if(!(typeof cancelFunction === "function")){
		cancelFunction = function (index){layer.close(index);}
	}
	var yesBtn = '确定';
	var cancelBtn = '取消';
	options = {btn: [yesBtn,cancelBtn],title:title, icon: 1,closeBtn:0};
	showConfirm(content, options,yesFunction, cancelFunction);
}

function showMsgError(content,title,yesFunction){
	if(!content)content='';
	if(!title)title='错误';
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	options = {btn: ['确定'],title:title, icon: 2,closeBtn:0};
	showConfirm(content, options,yesFunction, function(){});
}

function showConfirmError(content,title,yesFunction,cancelFunction,yesBtn,cancelBtn){
	if(!content)content='';
	if(!title)title='错误';
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	if(!(typeof yesFunction === "cancelFunction")){
		cancelFunction = function (index){layer.close(index);}
	}
	if(!yesBtn){
		yesBtn = '确定';
	}
	if(!cancelBtn){
		cancelBtn = '取消';
	}
	options = {btn: [yesBtn,cancelBtn],title:title, icon: 2,closeBtn:0};
	showConfirm(content, options,yesFunction, function(){});
}

function showMsgSuccess(content,title,yesFunction){
	if(!content)content='';
	if(!title)title='成功';
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	options = {btn: ['确定'],title:title, icon: 1,closeBtn:0};
	showConfirm(content, options,yesFunction, function(){});
}

function showConfirmSuccess(content,title,yesFunction,cancelFunction,yesBtn,cancelBtn){
	if(!content)content='';
	if(!title)title='成功';
	if(!(typeof yesFunction === "function")){
		yesFunction = function (index){layer.close(index);}
	}
	if(!(typeof yesFunction === "cancelFunction")){
		cancelFunction = function (index){layer.close(index);}
	}
	if(!yesBtn){
		yesBtn = '确定';
	}
	if(!cancelBtn){
		cancelBtn = '取消';
	}
	options = {btn: [yesBtn,cancelBtn],title:title, icon: 1,closeBtn:0};
	showConfirm(content, options,yesFunction, function(){});
}

function showConfirm(content,options,yesFunction,cancelFunction){
	layer.confirm(content, options,yesFunction,cancelFunction);
}


function analyzeDcReport(){
	var ps = $("span");
	var store = document.createElement("SPAN");
	$(ps).each(function(){
		var text = $(this).text();
		var p = $(this);
		var v;
		if(text.indexOf("<$") >= 0 && text.indexOf("$>") >= 0){
			var text1 = text.substring(text.indexOf("<$") + 2, text.indexOf("$>"));
			var text2 = text1.substring(0, text1.indexOf("@"));
			//获取对象属性
			var text3 = text1.substring(text1.indexOf("@") + 1, text1.length);
			text2 = text2.replaceAll("\\.", "/");
			text2 = replaceParameter(text2);
			var url = "${request.contextPath}/" + text2;
			var md5 = b64_md5(url);
			v = $(store).attr(md5);
			if(!v || v == ""){
				$.getJSON({
					async:false,
					url:url,
					success:function(data){
						var dataf = data;
						if(text3.indexOf("[") == 0){
							if(dataf.length <= 0){
								dataf = "";
							}
							else{
								var index = text3.substring(1, text3.indexOf("]"));
								dataf = dataf[index];
							}
							text3 = text3.substring(text3.indexOf("]") + 1, text3.length);
						}
						$(store).attr(md5, JSON.stringify(dataf));
						if(dataf == ""){
							$(p).text("");
						}
						else{
							$(p).text($(dataf).attr(text3));
						}
					}
				});
			}
			else{
				if(v == ""){
					$(p).text("");
				}
				else{
					var st = $(JSON.parse(v));
					$(p).text(st.attr(text3));
				}
			}
		}
	});
	$(store).remove();
}

function orOperationVal(opj, trigger, fun) {
	for (regKey in opj) {
		if ($(regKey).length > 0) {
			var value = opj[regKey];
			if (value) {
				if (value.length > 1 && value.substring(0, 1) == "!") {
					if (String($(regKey).val()) != String(value.substring(1))) {
						fun();
					}
				} else {
					if (String($(regKey).val()) == String(value)) {
						fun();
					}
				}
			}
			if (opj[regKey] == "") {
				opj[regKey] = " ";
			}
			$(regKey).on(trigger, "", opj[regKey], function(event) {
				var value = event.data;
				var tvalue = $(this).val();
				if (tvalue == "") {
					tvalue = " ";
				}
				if (value) {
					if (value.length > 1 && value.substring(0, 1) == "!") {
						if (tvalue != value.substring(1)) {
							fun();
						}
					} else {
						if (tvalue == value) {
							fun();
						}
					}
				}
			});
		}
	}
}