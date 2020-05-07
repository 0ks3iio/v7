<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>教务安排</title>
<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/default.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/array.css">
</style>
<#import "${request.contextPath}/basedata/tipsay/common/tipsayTeacherTable.ftl" as mytable />
</head>

<body class="fn-rel" data-sel="0" data-lock="0">
	<div id="header" class="fn-clear">
		<p class="tt">代课系统</p>
	    <div class="opt">
	        <#--<a href="#">代课</a>-->
	    </div>
	</div>

	<div id="container" class="daike-container fn-clear">
		<input type="hidden" id="acadyear" value="${tipsay.acadyear!}"/>
		<input type="hidden" id="semester" value="${tipsay.semester!}"/>
		<input type="hidden" id="chooseWeek" value="${tipsay.weekOfWorktime!}"/>
		<input type="hidden" id="chooseTeacherId" value="${tipsay.operator!}"/>
		<input type="hidden" id="tipsayId" value="${tipsay.id!}"/>
		
		<div class="sidebar-right">
	    	<div class="sidebar-right-wrap">
	        	<ul class="tab tab_ul">
	        		<li class="aa current"><span>同教研组老师</span></li>
	        		<li class="bb"><span>本班任课老师</span></li>
	        		<li class="cc"><span>全校老师</span></li>
	        	</ul>
	            <div class="pub-search">
	                <input type="text" id="inputTeacherName" value="" class="txt"  placeholder="请输入教师姓名">
	                <a href="javascript:" class="btn" onclick="searchByTeacherName()">查找</a>
	            </div>
	            <div class="tab-wrap">
	            	<div class="tab-item rightTeacherList">
	            		<div class="t-center mt-80">
							<img src="${request.contextPath}/static/images/public/nodata.png" alt="">
							<p class="c-999">暂无数据</p>
						</div>
	            	</div>
	            </div>
	        </div>
	        
	    	<p class="syllabus-tt"><span class="mr-5" id="moveTeacherName"></span>老师课程表</p>
	    	<@mytable.scheduleTable tableClass="syllabus-table" tableId="teacherTable1" weekDayList=weekDayList mm=mm am=am pm=pm nm=nm showBr=true>
			</@mytable.scheduleTable>
	    </div>
	    
	    
		<div class="content" id="tipsayContentTableDiv">
	    	<div class="syllabus-tt syllabus-tt2 fn-clear">
				<div class="tit-info" id="chooseTeacherName">
					<#if tipsay.operator?default('')==''>
						<p class="info">请先选择一个老师</p>
					<#else>
						<p class="info">申请老师：<span>${teacherName!}</span></p>
					</#if>
			    </div>
			</div>
			<@mytable.scheduleTable tableClass="syllabus-table js-time"  tableId="allTable" weekDayList=weekDayList mm=mm am=am pm=pm nm=nm showBr=true itemClass="item-lock">
			</@mytable.scheduleTable>
    	</div>
    </div>
</div>

<script type="text/javascript" src="${request.contextPath}/static/components/jquery-inputlimiter/jquery-inputlimiter/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/jquery.Layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/jquery.form.js"></script>

<script type="text/javascript" src="${request.contextPath}/basedata/tipsay/js/tipsayscript-array.js"></script>

<script type="text/javascript">
var chooseTeacherId="";
$(function(){
	function layout(){
		var sidebarrightwidth = Math.ceil(document.documentElement.clientWidth * 0.28);
		var marginright = sidebarrightwidth + 3
		$(".sidebar-right").css("width",sidebarrightwidth);
		$(".content").css("margin-right",marginright);
		$(".content").css("margin-left",3);
	};
	layout();
	window.onresize = function(){  
        layout();
    }
    reFreshTableWidth();
 	<#if courseschedule?exists>
 		$("#allTable").find(".item").html("");
 		var dataKey="${courseschedule.dayOfWeek!}_${courseschedule.periodInterval!}_${courseschedule.period}";
		var htmlTex='<span class="course">${courseschedule.subjectName!}</span>';
		htmlTex=htmlTex+'<span class="name" data-value="${courseschedule.classId!}">${courseschedule.className!}</span>';
		htmlTex=htmlTex+'<span class="room" data-value="${courseschedule.placeId!}">${courseschedule.placeName!}</span>';
 		$("#allTable").find("td .item[data-user='"+dataKey+"']").html(htmlTex);
 		//选中状态
 		$("#allTable").find("td .item[data-user='"+dataKey+"']").addClass("item-sel");
 		$("#teacherTable1").find("td .item[data-user='"+dataKey+"']").addClass("item-sel");
 		$("#allTable").find("td .item[data-user='"+dataKey+"']").each(function(){
			var con_item_h=$(this).height();
			var course=$(this).find('.course');
			var name=$(this).find('.name');
			var room=$(this).find('.room');
			var my_len=3;
			var my_h=parseInt(con_item_h/my_len);
			if(my_h>=14){
				course.css({height:my_h,'line-height':my_h+'px'});
				name.css({height:my_h,'line-height':my_h+'px'});
				room.css({height:my_h,'line-height':my_h+'px'});
			};
	    });
 		
 	</#if>
 	loadRightTeachers('${tipsay.operator!}');
 	
    $('#inputTeacherName').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			//回车执行查询
			searchByTeacherName();
		}
	}); 
	
	 $(".tab-item").on('mouseover','table tr',function(){
    	if($(this).hasClass("mytr")){
    		//移动到代课列表  
    	}else{
    		$(this).addClass('current').siblings('tr').removeClass('current');
			var teacherId=$(this).attr("data-teacher");
			var teacherName=$(this).find(".name").html();
			chooseTeacherId=teacherId;
			
			//延迟一秒
			setTimeout(function(){
				if(teacherId==chooseTeacherId){
					//获取教师课表
					$("#moveTeacherName").html(teacherName);
					loadTeacherByWeek(teacherId,false,"teacherTable1");
				}else{
					
				}
			},1000);
			
			
    	}
		
	},function(){
		if($(this).hasClass("mytr")){
    		//移动到代课列表
    	}else{
			$(this).removeClass('current');
		}
	});
	
	$(".rightTeacherList").on('click','.js-replace',function(){
		var tName=$(this).attr("data-tname");
		var tId=$(this).attr("data-tId");
		var tipType=$(this).attr("data-type");
		if(!addTipsay(tId,tName,tipType)){
			return;
		}
		//将之前的数据隐藏
		$(".mytr").hide();
		$("#tr_"+tId).show();
	});
});

//根据教师id查询某一周的数据 填入右下角表格
function loadTeacherByWeek(teacherId,tableId){
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	var chooseWeek=$("#chooseWeek").val();
	$.ajax({
		url:'${request.contextPath}/basedata/tipsay/scheduleByTeacherId',
		data:{'acadyear':acadyear,'semester':semester,'week':chooseWeek,'teacherId':teacherId},
		type:'post', 
		dataType:'json',
		success:function(data){
			$("#"+tableId).find(".item").html("");
			if(data!=""){
				var obj = eval(data);
				if(obj.length>0){
					for(var i=0;i<obj.length;i++){
						var dataKey=obj[i].dayOfWeek+"_"+obj[i].periodInterval+"_"+obj[i].period;
						var htmlTex='';
						if(obj[i].className){
							htmlTex=htmlTex+'<span class="course">'+obj[i].className+'</span>';
						}
						$("#"+tableId).find("td .item[data-user='"+dataKey+"']").html(htmlTex);
					}
				}
				$('#teacherTable1 td .item').each(function(){
					var con_item_h=$(this).height();
					var course=$(this).find('.course');
					if(con_item_h>=14){
						course.css({height:con_item_h,'line-height':con_item_h+'px'});
					};
		        });
			}
			
		}
	});	
	
}

function searchByTeacherName(){
	var teacherId=$("#chooseTeacherId").val();
	loadRightTeachers(teacherId);
}


function loadRightTeachers(teacherId){
	var keyTab="aa";
	$(".tab_ul").find("li").each(function(){
		if($(this).hasClass("current")){
			if($(this).hasClass("aa")){
				keyTab="1";
			}else if($(this).hasClass("bb")){
				keyTab="2";
			}else{
				keyTab="3";
			}
			return false;
		}
	})
	searchTab(teacherId,keyTab);
}
function searchTab(teacherId,tabkey){
	$(".rightTeacherList").html("");
	chooseTeacherId="";
	if(teacherId==""){
		return;
	}
	var inputTeacherName=$("#inputTeacherName").val().trim();
	//var url="${request.contextPath}/basedata/tipsay/loadTeacherList2/page?teacherId="+teacherId+"&type=${tipsay.type!}&tabType="+tabkey+"&teacherName="+inputTeacherName;
	//if(tabkey=="1"){
		//同教研组
		
	//}else if(tabkey=="2"){
	//	//同班 必须有选择的需要代课的
	//	var acadyear=$('#acadyear').val();
	//	var semester=$('#semester').val();
	//	url=url+"&acadyear="+acadyear+"&semester="+semester;
	//	var scheduleIdsClassId="";
		//周次选中
	//	$("#allTable").find(".item").each(function(){
	//		if($(this).find(".name").length>0){
	//			var clazzId=$(this).find(".name").attr("data-value");
	//			if($(this).hasClass("item-sel") &&  clazzId && clazzId!="" && clazzId.trim()!=""){
	//				scheduleIdsClassId=scheduleIdsClassId+","+clazzId;
	//			}
	//		}
			
	//	});
	//	if(scheduleIdsClassId==""){
	//		autoTips('请先选择需要代课课程的班级');
	//		isSave=false;
	//		$(".rightTeacherList").html('<div class="tab-item rightTeacherList">'
	//					+'<div class="t-center mt-80">'
	//						+'<img src="${request.contextPath}/static/images/public/nodata.png" alt="">'
	//						+'<p class="c-999">暂无数据</p></div></div>');
	//		return;
	//	}else{
	//		scheduleIdsClassId=scheduleIdsClassId.substring(1);
	//	}
	//	url=url+"&classIds="+scheduleIdsClassId;
	//}else{
	//	//全校
		
	//}
	//$(".rightTeacherList").load(url);
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	var scheduleIds='';
	var scheduleIdsClassId="";
	//周次选中
	$("#allTable").find(".item").each(function(){
		if($(this).find(".courseScheduleId").length>0){
			var clazzId=$(this).find(".classId").val();
			if($(this).hasClass("item-sel") &&  clazzId && clazzId!="" && clazzId.trim()!=""){
				scheduleIdsClassId=scheduleIdsClassId+","+clazzId;
			}
			if($(this).hasClass("item-sel")){
				var sId=$(this).find(".courseScheduleId").val();
				scheduleIds=scheduleIds+","+sId;
			}
		}
		
	});
	
	if(tabkey=="2"){
		//同班 必须有选择的需要代课的
		if(scheduleIdsClassId==""){
			autoTips('请先选择需要代课课程的班级');
			$(".rightTeacherList").html('<div class="tab-item rightTeacherList">'
						+'<div class="t-center mt-80">'
							+'<img src="${request.contextPath}/static/images/public/nodata.png" alt="">'
							+'<p class="c-999">暂无数据</p></div></div>');
			isSave=false;
			return;
		}
	}
	if(scheduleIds!=""){
		scheduleIds=scheduleIds.substring(1);
	}
	$.ajax({
		url:'${request.contextPath}/basedata/tipsay/loadTeacherList/all',
		data:{'teacherId':teacherId,'tabType':tabkey,'teacherName':inputTeacherName,'acadyear':acadyear,'semester':semester,'scheduleIds':scheduleIds},
		type:'post', 
		dataType:'json',
		success:function(data){
			var teacherList = data.teacherList;
	 		showRightTeacherList(teacherList);
		}
	});	
}

function showRightTeacherList(teacherList){
	$(".rightTeacherList").html("");
	if(teacherList.length==0){
		$(".rightTeacherList").html('<div class="tab-item rightTeacherList">'
						+'<div class="t-center mt-80">'
							+'<img src="${request.contextPath}/static/images/public/nodata.png" alt="">'
							+'<p class="c-999">暂无数据</p></div></div>');
		return;
	}else{
		var ss='';
		
		for(var ii=0;ii<teacherList.length;ii++){
			var tt=teacherList[ii];
			var tId=tt.teacherId;
			var tName=tt.teacherName;
			var tCode='';
			var error=tt.error;
			if(tt.teacherCode){
				tCode=tt.teacherCode;
			}
			var $tr=makeTr(tId,tName,tCode,error);
			ss=ss+$tr;
		}
		$(".rightTeacherList").append('<table>'+ss+'</table>');
		//隔行颜色变化初始化
		$('.tab-wrap .tab-item').each(function(){
	        $(this).find('table tr:not(".mytr"):even').addClass('even');
	    });
	    initmouse();
	}
}

function makeTr(tid,tname,tcode,error){
	var hh="";
	if(error=="1"){
		hh=`<tr class="conflict" data-teacher="`+tid+`">
	        <td class="name" style="width:100px;">`+tname+`</td>
			<td>`+tcode+`</td>
			<#if tipsay.type?default('1')=='1'>
	        <td class="opt">
	       		 <a  href="javascript:"  data-tname="`+tname+`" data-tId="`+tid+`" >代课</a>		
	        </td>
	        <#else>
	        <td class="opt"><a  href="javascript:" data-tname="`+tname+`" data-tId="`+tid+`" >管课</a></td>
			</#if>
		</tr>`;
	}else{
		hh=`<tr data-teacher="`+tid+`">
	        <td class="name" style="width:100px;">`+tname+`</td>
			<td>`+tcode+`</td>
			<#if tipsay.type?default('1')=='1'>
	        <td class="opt">
	       		 <a class="js-replace" href="javascript:"  data-tname="`+tname+`" data-tId="`+tid+`" onClick="saveTipsay('`+tid+`')">代课</a>		
	        </td>
	        <#else>
	        <td class="opt"><a class="js-replace" href="javascript:" data-tname="`+tname+`" data-tId="`+tid+`" onClick="saveTipsay('`+tid+`')">管课</a></td>
			</#if>
		</tr>`
	}
	return hh;
}

var isSave=false;
function saveTipsay(teacherId){
	//form提交
	if(isSave){
		return ;
	}
	isSave=true;
	var tipsayId=$("#tipsayId").val();
	if(!tipsayId){
		tipsayId="";
	}
	
	$.ajax({
		url : "${request.contextPath}/basedata/tipsay/saveTipsay2",
		data:{"tipsayId":tipsayId,"newTeacherId":teacherId},
		type:'post', 
		dataType:'json',
		success:function(data){
			var jsonO = data;
	 		if(!jsonO.success){
	 			isSave=false;
	 			autoTips(jsonO.msg)
	 		}else{
	 			isSave=false;
	 			autoTips('操作成功');
	 			//左边选中数据清空
	 			$("#allTable").find(".item").html("");
				//关闭
				$("#teacherTable1").find(".item").html("");
				$("#moveTeacherName").html("");
			}
			
		}
	});	
	
}
function initmouse(){
	//滑动
    $('.tab-item table tr').hover(function(){
    	if($(this).hasClass("mytr")){
    		//移动到代课列表  
    	}else{
    		$(this).addClass('current').siblings('tr').removeClass('current');
			var teacherId=$(this).attr("data-teacher");
			var teacherName=$(this).find(".name").html();
			chooseTeacherId=teacherId;
			
			//延迟一秒
			setTimeout(function(){
				if(teacherId==chooseTeacherId){
					//获取教师课表
					$("#moveTeacherName").html(teacherName);
					loadTeacherByWeek(teacherId,false,"teacherTable1");
				}else{
					
				}
			},1000);
			
			
    	}
		
	},function(){
		if($(this).hasClass("mytr")){
    		//移动到代课列表
    	}else{
			$(this).removeClass('current');
		}
	});
}
	
</script>
</body>
</html>