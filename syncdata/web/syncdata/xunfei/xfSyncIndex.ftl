<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="同步状态">
<script>
 
function showProcess(divId, type, intervalId){
	$.ajax({url:'${request.contextPath}/syncdata/monitor/xunfei/syncState?appName=xunfei&type=' + type,
		    data: null,  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	$(divId).html(data);
		    	if(data == "同步结束" || data == "同步错误"){
		    		clearInterval(intervalId);
		    	}}});
}
var idw = setInterval(function(){showProcess("#dw_proccess", "unit", idw)}, 1000);
var ixx = setInterval(function(){showProcess("#xx_proccess", "school", ixx)}, 1000);
var ibm = setInterval(function(){showProcess("#bm_proccess", "dept", ibm)}, 1000);
var ijs = setInterval(function(){showProcess("#js_proccess", "teacher", ijs)}, 1000);
var iyh = setInterval(function(){showProcess("#yh_proccess", "user", iyh)}, 1000);
var ijspt = setInterval(function(){showProcess("#jspt_proccess", "teacherpt", ijspt)}, 1000);
var iyhpt = setInterval(function(){showProcess("#yhpt_proccess", "userpt", iyhpt)}, 1000);
var ixs = setInterval(function(){showProcess("#xs_proccess", "student", ixs)}, 1000);
var ibj = setInterval(function(){showProcess("#bj_proccess", "class", ibj)}, 1000);
var inj = setInterval(function(){showProcess("#nj_proccess", "grade", inj)}, 1000);

</script>
</head>
<body>
<input type="button" value="clear" id="clearState" /><p>
单位：<font color="red" id="dw_proccess"></font><p>
学校：<font color="red" id="xx_proccess"></font><p>
教师（管理员）：<font color="red" id="js_proccess"></font><p>
用户（管理员）：<font color="red" id="yh_proccess"></font><p>
教师（普通）：<font color="red" id="jspt_proccess"></font><p>
用户（普通）：<font color="red" id="yhpt_proccess"></font><p>
部门：<font color="red" id="bm_proccess"></font><p>
班级：<font color="red" id="bj_proccess"></font><p>
年级：<font color="red" id="nj_proccess"></font><p>
学生：<font color="red" id="xs_proccess"></font><p>

<script>
$("#clearState").on("click", function(){
	$.ajax({url:'${request.contextPath}/syncdata/monitor/xunfei/clearState',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});
</script>
</@webmacro.commonWeb>