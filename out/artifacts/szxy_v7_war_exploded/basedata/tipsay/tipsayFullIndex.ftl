<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>教务安排</title>
<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-daterangepicker/daterangepicker-bs3.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/public.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/default.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/array.css">
<style>
	.daterangepicker{display: none;}
	.daterangepicker{display: none;}
	.daterangepicker .btn {
	    display: inline-block;
	    margin-bottom: 0;
	    font-weight: normal;
	    text-align: center;
	    vertical-align: middle;
	    -ms-touch-action: manipulation;
	    touch-action: manipulation;
	    cursor: pointer;
	    background-image: none;
	    border: 1px solid transparent;
	    white-space: nowrap;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    border-radius: 4px;
	    -webkit-user-select: none;
	    -moz-user-select: none;
	    -ms-user-select: none;
	    user-select: none;
	}
	.daterangepicker .btn-default {
	    color: #333333;
	    background-color: #ffffff;
	    border-color: #cccccc;
	}
	.daterangepicker .btn-success {
	    color: #ffffff;
	    background-color: #5cb85c;
	    border-color: #4cae4c;
	}
</style>
<#import "/basedata/tipsay/common/tipsayTeacherTable.ftl" as mytable />
</head>

<body class="fn-rel" data-sel="0" data-lock="0">
	<div id="header" class="fn-clear">
		<p class="tt">代课系统</p>
	    <div class="opt">
	         <#--<a href="#">代课</a>-->
	    </div>
	</div>

	<div id="container" class="daike-container fn-clear">
		<input type="hidden" id="acadyear" value="${semesterObj.acadyear!}"/>
		<input type="hidden" id="semester" value="${semesterObj.semester!}"/>
		<input type="hidden" id="applyTypeValue" <#if showLeft>value="1"<#else> value="2"</#if> />
		<#if showLeft>
			<div class="sidebar-left">
		    	<div class="sidebar-filter">
		    		<p class="tt"><input type="text" class="txt" placeholder="请输入教师姓名"></p>
		            <div class="inner">
		                <p class="list">
		                	<#if teacherInGroupMap?exists && teacherInGroupMap?size gt 0 >
		                	<#list teacherInGroupMap?keys as key>
		                    <a href="#" data-class="c_${key!}">${teacherInGroupMap[key]!}</a>
		                    </#list>
		                    </#if>
		                </p>
		            </div>
		    	</div>
		    	<div class="group-list">
					<#if groupDtoList?exists && groupDtoList?size gt 0>
						<#list groupDtoList as dto>
						<div class="group">
							<p class="title" data-value="${dto.teachGroupId!}">${dto.teachGroupName!}<i class="fa fa-angle-down"></i></p>
			                <ol>
			                	<#if dto.mainTeacherList?exists && dto.mainTeacherList?size gt 0>
			                    	<#list dto.mainTeacherList as tt>
						    		<li data-class="c_${dto.teachGroupId!}_${tt.teacherId!}" data-value="${tt.teacherId!}" data-groupId="${dto.teachGroupId!}"><a href="javascript:" >${tt.teacherName!}</a></li>
					    			</#list>
			                    </#if>
					    	</ol>
				    	</div>
		                </#list>
		                
		              </#if>
		              
		    	</div>
		    </div>
		</#if>
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
    	<input type="hidden" name="chooseTeacherId" id="chooseTeacherId" value="${teacherId!}">
    	<input type="hidden" name="nowWeek" id="nowWeek" value="${nowWeek!}">
		<div class="content" id="tipsayContentTableDiv">
	    	<div class="syllabus-tt syllabus-tt2 fn-clear">
				<div class="tit-info" id="chooseTeacherName">
					<#if teacherId?default('')==''>
						<p class="info">请先选择一个老师</p>
					<#else>
						<p class="info"><span>${teacherName!}</span>老师</p>
					</#if>
			    	
			    	<div class="fn-clear">
			    		<span class="fn-left mt-3 f14">代课时间：</span>
			        	<span class="ui-radio fn-left mt-3 mr-10 f14 ui-radio-current"><input type="radio" class="radio" value="1">按周次</span>
			            <select id="chooseWeek" name="chooseWeek" class="fn-left mt-3 mr-10 f14 func1" style="width:85px" onChange="changeWeek()">
							<#if weekList?exists && weekList?size gt 0>
		                    	<#list weekList as weekDto>
		                    		<option value="${weekDto!}" <#if nowWeek==weekDto>selected="selected"</#if>>第${weekDto!}周</option>
		                        </#list>
		                    </#if>
						</select>
			            <span class="ui-radio fn-left mt-3 mr-10 f14"><input type="radio" class="radio" value="2">自定义</span>
			           
						<select id="clazzId" name="clazzId"  class="fn-left mt-3 mr-10 f14 func2" style="width:100px;" disabled onChange="changeClassId()">
							<option value="">全部</option>
						</select>
						<input type="text" class="fn-left input-txt  mt-3 mr-10  input-date func2" id="reservation" style="width: 170px;" disabled value="${(startTime?string('yyyy-MM-dd'))!}-${(afterWeekTime?string('yyyy-MM-dd'))!}">
			        </div>
			    </div>
			</div>
			<@mytable.scheduleTable tableClass="syllabus-table js-time" tableId="allTable" weekDayList=weekDayList mm=mm am=am pm=pm nm=nm showBr=true>
			</@mytable.scheduleTable>
			
		    <div class="tableContainer js-time" style="display:none;overflow-y:scroll;">
	        	<table class="table" id="scheduleListTable">
					<thead>
						<tr>
							<th width="5%"><span class="ui-checkbox ui-checkbox-all" data-all="no"><input type="checkbox" class="chk"></span></th>
							<th width="15%">时间</th>
							<th width="12%">节次</th>
							<th width="11%">周次</th>
							<th width="19%">班级</th>
							<th width="19%">教室</th>
							<th width="19%">课程</th>
						</tr>
					</thead>
					<tbody>
						<tr>
						</tr>
					</tbody>
				</table>
			</div>
    	</div>
    </div>
</div>

<script type="text/javascript" src="${request.contextPath}/static/components/jquery-inputlimiter/jquery-inputlimiter/jquery-1.7.2.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/jquery.Layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/jquery.form.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/myscript-chkRadio.js"></script>

<script type="text/javascript" src="${request.contextPath}/basedata/tipsay/js/tipsayscript-array.js"></script>

<script type="text/javascript">
var chooseTeacherId="";
$(function(){
	function layout(){
		
		var sidebarrightwidth = Math.ceil(document.documentElement.clientWidth * 0.28);
		var marginright = sidebarrightwidth + 3
		$(".sidebar-right").css("width",sidebarrightwidth);
		$(".content").css("margin-right",marginright);
		<#if !showLeft>
			$(".content").css("margin-left",3);
		</#if>
	};
	layout();
	window.onresize = function(){  
        layout();
    }
    reFreshTableWidth();

	
	//时间--默认一周时间
    $('#reservation').daterangepicker(
	{
	    format: 'YYYY-MM-DD',
	    startDate: '${(startTime?string('yyyy-MM-dd'))!}',
	    endDate: '${(afterWeekTime?string('yyyy-MM-dd'))!}',
	    minDate:'${(startTime?string('yyyy-MM-dd'))!}',
	    maxDate:'${(endTime?string('yyyy-MM-dd'))!}'
	}).on('apply.daterangepicker', function(ev, picker) {	
		var startT=picker.startDate.format('YYYY-MM-DD');
		var endT=picker.endDate.format('YYYY-MM-DD');
		if(startT=="" || endT==""){
			 $("#reservation").val("");
		}else{
			$("#reservation").val(startT+"-"+endT);
			loadTeacherByTime();
		}
	});
  
   
    // 切换代课时间
    $(".content .syllabus-tt .ui-radio").click(function(){
    	$(".content .syllabus-tt .ui-radio").removeClass("ui-radio-current");
    	$(this).addClass("ui-radio-current");
    	var index = $(".content .syllabus-tt .ui-radio").index(this);
    	$(".content .js-time").hide()
    	$(".content .js-time").eq(index).show();
    	if($(this).find(".radio").val()=='2'){
    		$(".func1").prop("disabled",true);
    		$(".func2").prop("disabled",false);
    		loadTeacherByTime();
    	}else{
    		$(".func1").prop("disabled",false);
    		$(".func2").prop("disabled",true);
    		changeWeek();
    	}
    	
    	//切换 右边
		if($('.sidebar-right-wrap').find("table").length>0 && $('.sidebar-right-wrap').find("table").find(".inner").length>0){
			$('.sidebar-right-wrap').find("table").find(".inner").hide();
		}
		//选中取消
		$("#allTable").find(".item-sel").removeClass("item-sel");
		$("#teacherTable1").find(".item-sel").removeClass("item-sel");
    });
   	<#if teacherId?default('')!=''>
   		loadTeacherByWeek('${teacherId!}',true,"allTable");
   		loadRightTeachers('${teacherId!}');
   		findClazzListByTeacherId('${teacherId!}');
   	</#if>
    $('#inputTeacherName').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			//回车执行查询
			searchByTeacherName();
		}
	}); 
	
	//右边教师这边事件
	
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

//切换教师
function changeTeacherId(teacherId){
	loadTeacherByWeek(teacherId,true,"allTable");
}

//拿到范围
function takeRadioValue(){
	var radioValue="1";
	if($(".content .syllabus-tt .ui-radio-current")){
		radioValue=$(".content .syllabus-tt .ui-radio-current").find(".radio").val();
	}
	return radioValue;
}

function changeWeek(){
	if(takeRadioValue()=="1"){
		var tId=$("#chooseTeacherId").val();
		if(tId!=""){
			loadTeacherByWeek(tId,true,"allTable");
		}
	}
}
function changeClassId(){
	if(takeRadioValue()=="2"){
		loadTeacherByTime();
	}
}

//根据教师id查询某一周的数据 填入表格
function loadTeacherByWeek(teacherId,showId,tableId){
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	
	var radioType=takeRadioValue();
	
	var chooseWeek=$("#chooseWeek").val();
	if(radioType=="2"){
		chooseWeek=$("#minWeek").val();
	}
	if(chooseWeek && chooseWeek!="" && chooseWeek!="0"){
		
	}else{
		chooseWeek=$("#newWeek").val();
	}
	if(showId){
		$("#allTable").find(".item-sel").removeClass("item-sel");
		$("#teacherTable1").find(".item-sel").removeClass("item-sel");
	}

	$.ajax({
		url:'${request.contextPath}/basedata/tipsay/scheduleByTeacherId',
		data:{'acadyear':acadyear,'semester':semester,'week':chooseWeek,'teacherId':teacherId},
		type:'post', 
		dataType:'json',
		success:function(data){
			$("#"+tableId).find(".item").html("");
			if(data!=""){
				var obj = eval(data);
				if(obj.length==0){
					
				}else{
					for(var i=0;i<obj.length;i++){
						var dataKey=obj[i].dayOfWeek+"_"+obj[i].periodInterval+"_"+obj[i].period;
						var htmlTex='';
						if(showId){
							htmlTex='<input type="hidden" class="courseScheduleId" value="'+obj[i].id+'">';
							htmlTex=htmlTex+'<input type="hidden" class="classId" value="'+obj[i].classId+'">';
							if(obj[i].subjectName){
								htmlTex=htmlTex+'<span class="course">'+obj[i].subjectName+'</span>';
							}else{
								htmlTex=htmlTex+'<span class="course"></span>';
							}
							if(obj[i].className){
								htmlTex=htmlTex+'<span class="name" data-value="'+obj[i].classId+'">'+obj[i].className+'</span>';
							}else{
								htmlTex=htmlTex+'<span class="name" data-value=""></span>';
							}
							
							if(obj[i].placeName){
								htmlTex=htmlTex+'<span class="room" data-value="'+obj[i].placeId+'">'+obj[i].placeName+'</span>';
							}else{
								htmlTex=htmlTex+'<span class="room" data-value=""></span>';
							}
						}else{
							if(obj[i].className){
								htmlTex=htmlTex+'<span class="course">'+obj[i].className+'</span>';
							}
						}
						$("#"+tableId).find("td .item[data-user='"+dataKey+"']").html(htmlTex);
					}
				} 
				
				//路径
				if(showId){
					$('#allTable td .item').each(function(){
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
				}else{
					$('#teacherTable1 td .item').each(function(){
						var con_item_h=$(this).height();
						var course=$(this).find('.course');
						if(con_item_h>=14){
							course.css({height:con_item_h,'line-height':con_item_h+'px'});
						};
			        });
				}
				
				
				
			}
			
			
		}
	});	
	
}



function loadTeacherByTime(){
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	var teacherId=$("#chooseTeacherId").val();
	var time=$('#reservation').val();
	var classId=$("#clazzId").val();
	var url="${request.contextPath}/basedata/tipsay/loadTeacherByTime/page?acadyear="+acadyear+"&semester="+semester+"&teacherId="+teacherId+"&timeArea="+time+"&classId="+classId;
	$(".tableContainer").load(url);

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
	chooseTeacherId="";
	$(".rightTeacherList").html("");
	if(teacherId==""){
		$(".rightTeacherList").html('<div class="tab-item rightTeacherList">'
						+'<div class="t-center mt-80">'
							+'<img src="${request.contextPath}/static/images/public/nodata.png" alt="">'
							+'<p class="c-999">暂无数据</p></div></div>');
		return;
	}
	var inputTeacherName=$("#inputTeacherName").val().trim();
	//	var url="${request.contextPath}/basedata/tipsay/loadTeacherList/page?teacherId="+teacherId+"&tabType="+tabkey+"&teacherName="+inputTeacherName;
	
//	if(tabkey=="1"){
//		//同教研组
//		
//	}else if(tabkey=="2"){
//		//同班 必须有选择的需要代课的
//		var acadyear=$('#acadyear').val();
//		var semester=$('#semester').val();
//		url=url+"&acadyear="+acadyear+"&semester="+semester;
//		var scheduleIdsClassId="";
//		if(takeRadioValue()=="2"){
//			//自定义选中
//			var lll=$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").length;
//			if(lll>0){
//				$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").each(function(){
//					var clazzId=$(this).attr("data-classId");
//					if(clazzId && clazzId!="" && clazzId.trim()!=""){
//						scheduleIdsClassId=scheduleIdsClassId+","+clazzId;
//					}
//				})
//			}
//		}else{
			//周次选中
//			$("#allTable").find(".item").each(function(){
//				if($(this).find(".courseScheduleId").length>0){
//					var clazzId=$(this).find(".classId").val();
//					if($(this).hasClass("item-sel") &&  clazzId && clazzId!="" && clazzId.trim()!=""){
//						scheduleIdsClassId=scheduleIdsClassId+","+clazzId;
//					}
//				}
//				
//			});
//		}
//		if(scheduleIdsClassId==""){
//			autoTips('请先选择需要代课课程的班级');
//			$(".rightTeacherList").html('<div class="tab-item rightTeacherList">'
//						+'<div class="t-center mt-80">'
//							+'<img src="${request.contextPath}/static/images/public/nodata.png" alt="">'
//							+'<p class="c-999">暂无数据</p></div></div>');
//			isSave=false;
//			return;
//		}else{
//			scheduleIdsClassId=scheduleIdsClassId.substring(1);
//		}
//		url=url+"&classIds="+scheduleIdsClassId;
//	}else{
		//全校
		
//	}
	//$(".rightTeacherList").load(url);
	
	
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	var scheduleIds='';
	var scheduleIdsClassId="";
	if(takeRadioValue()=="2"){
		//自定义选中
		var lll=$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").length;
		if(lll>0){
			$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").each(function(){
				var clazzId=$(this).attr("data-classId");
				if(clazzId && clazzId!="" && clazzId.trim()!=""){
					scheduleIdsClassId=scheduleIdsClassId+","+clazzId;
				}
				var sId=$(this).find("input").val();
				scheduleIds=scheduleIds+","+sId;
			})
		}
	}else{
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
		
	}
	
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
	
function loadTeacherId(acadyear,semester,type,teacherId,groupId,clazzId,teacherName){
	$("#teacherTab").find(".no-data-container").hide();
	$("#teacherTab").find(".replace-list").show();
	var url='${request.contextPath}/basedata/tipsay/loadTeacherList';
	var parm="acadyear="+acadyear+"&semester="+semester+"&type="+type+"&teacherId="+teacherId+"&groupId="+groupId+"&classId="+clazzId
				+"&teacherName="+teacherName;
	url=encodeURI(url+"?"+parm);
	$("#teacherTab").find(".replace-list").load(url);
}
	
function findClazzListByTeacherId(teacherId){
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	$.ajax({
		url:'${request.contextPath}/basedata/tipsay/classByTeacherId',
		data:{'acadyear':acadyear,'semester':semester,'teacherId':teacherId},
		type:'post', 
		dataType:'json',
		success:function(data){
			var classMap=data.classMap;
			$("#clazzId").html("");
			$("#clazzId").append('<option value="">全部</option>');
			if(classMap){
				for(var key in classMap){
				  $("#clazzId").append('<option value="'+key+'">'+classMap[key]+'</option>');
				}
			}
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
	        <td class="opt">
	       		 <a  href="javascript:" data-tname="`+tname+`" data-tId="`+tid+`"  data-type="1">代课</a>		
	        </td>
	        <td class="opt"><a  href="javascript:" data-tname="`+tname+`" data-tId="`+tid+`"  data-type="2" >管课</a></td>
		</tr>
		`;
	}else{
		 hh=`<tr data-teacher="`+tid+`">
	        <td class="name" style="width:100px;">`+tname+`</td>
			<td>`+tcode+`</td>
	        <td class="opt">
	       		 <a class="js-replace" href="javascript:" data-tname="`+tname+`" data-tId="`+tid+`"  data-type="1">代课</a>		
	        </td>
	        <td class="opt"><a class="js-replace" href="javascript:" data-tname="`+tname+`" data-tId="`+tid+`"  data-type="2" >管课</a></td>
		</tr>
		<tr class="inner mytr" id="tr_`+tid+`" style="display:none">
			<td colspan="4" class="py-5">
				<form id="form_`+tid+`">
					<input type="hidden" name="acadyear"id="acadyear_`+tid+`"  value=""/>
					<input type="hidden" name="semester"id="semester_`+tid+`"  value=""/>
					<input type="hidden" name="newTeacherId" value="`+tid+`"/>
					<input type="hidden" name="courseScheduleIds" id="scheduleId_`+tid+`" value=""/>
					<input type="hidden" name="oldTeacherId" id="oldTeacherId_`+tid+`"  value=""/>
					<input type="hidden" name="type" id="type_`+tid+`" value="">
					<input type="hidden" name="applyType" id="applyType_`+tid+`" value=""/>
					<p id="typeName_`+tid+`">类型：</p>
					<p>备注：<input type="text"  maxlength="250" class="form-control" name="remark" id="remark_`+tid+`"></p>
					<p class="abtn">
						<a class="abtn-blue" onClick="saveTipsay('`+tid+`')">确定</a>
						<a class="abtn-white" onClick="cancelForm('`+tid+`')">取消</a>
					</p>
				</form>
			</td>
		</tr>`;
	}
	return hh;
}

//右边教师保存js
function addTipsay(teacherId,tName,tipType){
	$(".mytr").find("input[name='remark']").val("");
	$(".mytr").find("input[name='courseScheduleIds']").val("");
	//选中的值
	var opotName="代课";
	if("2"==tipType){
		opotName="管课";
	}
	var scheduleIds=getChooseSchedule();
	
	if(!scheduleIds || scheduleIds==""){
		autoTips('请先选择需要'+opotName+'课程');
		return false;
	}else{
		scheduleIds=scheduleIds.substring(1);
		$("#scheduleId_"+teacherId).val(scheduleIds);
		$("#typeName_"+teacherId).html("类型："+opotName);
		$("#type_"+teacherId).val(tipType);
		var oldTeacherId=$("#chooseTeacherId").val();
		$("#oldTeacherId_"+teacherId).val(oldTeacherId);
	}
	return true;
}

function getChooseSchedule(){
	var radioValue=takeRadioValue();
	var scheduleIds="";
	if(radioValue=="1"){
		$("#allTable").find(".item").each(function(){
			if($(this).find(".courseScheduleId").length>0){
				var scheduleId=$(this).find(".courseScheduleId").val();
				if($(this).hasClass("item-sel")){
					scheduleIds=scheduleIds+","+scheduleId;
				}
			}
			
		});
	}else{
		var lll=$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").length;
		if(lll>0){
			$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").each(function(){
				scheduleIds=scheduleIds+","+$(this).find(".chk").val();
			})
		}
	}
	return scheduleIds;
}

function cancelForm(teacherId){
	$("#tr_"+teacherId).hide();
}
var isSave=false;
function saveTipsay(teacherId){
	//form提交
	if(isSave){
		return ;
	}
	var scheduleIds=getChooseSchedule();
	
	if(!scheduleIds || scheduleIds==""){
		autoTips('请先选择需要代课课程');
		isSave=false;
		return;
	}else{
		scheduleIds=scheduleIds.substring(1);
		$("#scheduleId_"+teacherId).val(scheduleIds);
	}
	$("#acadyear_"+teacherId).val($("#acadyear").val());
	$("#semester_"+teacherId).val($("#semester").val());
	$("#applyType_"+teacherId).val($("#applyTypeValue").val());
	var oldTeacherId=$("#oldTeacherId_"+teacherId).val();
	isSave=true;
	var options = {
		url : "${request.contextPath}/basedata/tipsay/saveTipsay1",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			isSave=false;
	 			autoTips(jsonO.msg)
	 		}else{
	 			isSave=false;
	 			autoTips('操作成功');
				//重新左边数据
				var radioValue=takeRadioValue();
				if(radioValue=="1"){
					loadTeacherByWeek(oldTeacherId,true,"allTable");
				}else{
					loadTeacherByTime();
				}
				//关闭
				cancelForm(teacherId);
				$("#teacherTable1").find(".item").html("");
				$("#moveTeacherName").html("");
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#form_"+teacherId).ajaxSubmit(options);
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