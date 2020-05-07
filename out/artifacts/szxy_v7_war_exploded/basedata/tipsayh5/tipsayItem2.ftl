<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title><#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>详情</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/font-awesome.css" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.picker.min.css">
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body>
	<#--
    <header class="mui-bar mui-bar-nav" style="display:none;">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"></a>
        <h1 class="mui-title">代课详情</h1>
    </header>
    -->
     <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" style="display:none;"></a>
    <#if error?default('')==''>
    <#--申请信息---applyContent-->		
    <div class="mui-content add-form" style="padding-bottom: 51px;">
    	<ul class="mui-table-view">
    		<li class="mui-table-view-cell">
            	<span class="f-16 f-left">审核状态</span>
            	<span class="f-16 f-right c-blue">
					<#if tipsayDto.tipsay.state=='0'>
						<#if tipsayDto.tipsay.tipsayType?default('')=='03'>
						待安排
						<#elseif tipsayDto.tipsay.tipsayType?default('')=='02'>
						待审核
						<#else>
						审核中
						</#if>
					<#elseif tipsayDto.tipsay.state=='1'>
						已通过
					<#elseif tipsayDto.tipsay.state=='2'>
						未通过
					</#if>
            	</span>
            </li>
            <li class="mui-table-view-cell">
            	<span class="f-16 f-left">申请类型</span>
            	<span class="f-16 f-right">${tipsayDto.tipsayTypeName!}</span>
            </li>
            <li class="mui-table-view-cell">
            	<span class="f-16 f-left">原任课教师</span>
            	<span class="f-16 f-right">${tipsayDto.oldTeacherName!}</span>
            </li>
            <li class="mui-table-view-cell">
            	<span class="f-16 f-left">周次</span>
            	<span class="f-16 f-right">第${tipsayDto.tipsay.weekOfWorktime}周</span>
            </li>
            <li class="mui-table-view-cell">
            	<div class="f-16">节次</div>
            	<div class="mt-5">${tipsayDto.timeStr!}<span class="c-999"> - ${tipsayDto.className!}</span></div>
            </li> 
    	</ul>
    	<#if tipsayDto.tipsay.state=='0' && tipsayDto.tipsay.tipsayType?default('')=='03'>
    	<#else>
    	<ul class="mui-table-view">
    		<li class="mui-table-view-cell">
            	<span class="f-16 f-left">代课教师</span>
            	<span class="f-16 f-right">${tipsayDto.newTeacherName!}</span>
            </li>
        </ul>
        </#if>
    	<ul class="mui-table-view">
            <li class="mui-table-view-cell">
            	<span class="f-16">备注</span>
            </li>
            <li class="mui-table-view-cell">${tipsayDto.remark!}</li>
        </ul>
    </div>
    
    <nav class="mui-bar mui-bar-tab footButton">
		<#if teacherId?default('')==tipsayDto.tipsay.operator?default('') && tipsayDto.tipsay.tipsayType!='01'>
			<a href="javascript:" class="mui-tab-item f-16 deleteTipsay" data-value="${tipsayDto.tipsay.id!}" style="background: #2f7bff;color: #fff;">撤销</a>
		</#if>
    </nav>
    <#else>
    	<div class="mui-content " style="padding-bottom: 51px;">
	    	<div class="mui-page-noData"> 
		    	 <i></i>
		    	 <p class="f-16">${error!}</p>
		    </div>
	    </div>
    </#if>


    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/jquery.form.js"></script>
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${request.contextPath}/static/mui/js/mui.picker.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
    <script type="text/javascript">
     $('#cancelId').off('click').on('click',function(){
     	loadBackList();
	});
	
	function loadBackList(){
		var parm='&acadyear=${acadyear!}&semester=${semester!}&week=${week!}&statType=${statType!}';
		var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}'+parm;
		loadByHref(url);
	}
    (function($) {
		$.init();
	})(mui);
	
	var deleFlag=false;
	$(".footButton").on('tap','.deleteTipsay',function(e){
		var tipsayId=$(this).attr("data-value");
		mui.confirm('确定要撤销吗？', '',['取消','确认'], function(e) {
            if (e.index == 1) {
            	//确认
                deleteTipsay(tipsayId);
            } else {
            	//取消
            	deleFlag=false;
            }
        })
	})
	
	
	function deleteTipsay(tipsayId){
		if(deleFlag){
			return;
		}
		deleFlag=true;
		$.ajax({
			url:'${request.contextPath}/mobile/open/adjusttipsay/deleteTipsay',
			data:{'tipsayId':tipsayId,'type':'${type!}'},
			type:'post', 
			dataType:'json',
			success:function(jsonO){
				deleFlag=false;
				if(jsonO.success){
					loadBackList();
				}else{
					toastMsg(jsonO.msg);
				}
			}
		});	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	var oldChooseTeacherId="";
	<#if applyType?default('1')!='1'>
		<#--教师列表选中某个老师-->
		$(".teacherDiv").on("click",".teacher-cell",function(){
			var tId=$(this).attr("data-value");
			var tName=$(this).attr("data-name");
			var opopType=$("#opopId").val();
			if(opopType=="old"){
				
				if(oldChooseTeacherId!=tId){
					$("#oldTeacherId").val(tId);
					$("#oldTeacherName").html(tName);
					oldChooseTeacherId=tId;
					//清空原来选中的节次
					$(".chooseSchedule").remove();
				}
				
			}else{
				$("#newTeacherId").val(tId);
				$("#newTeacherName").html(tName);
			}
			
			$(".teacherDiv").hide();
			$(".applyContent").show();
			
		})
		<#--加载代课老师-->
		$(".addNewTeacherId").click(function(){
			$(".applyContent").hide();
			$("#opopId").val("new");
			$(".teacherDiv").show();
			var teacherId=$("#oldTeacherId").val();
			searchTeacherList(teacherId);
		})
		//教师列表
		function searchTeacherList(teacherId){
			var length=$(".chooseSchedule").length;
			var scheduleIds="";
			if(length>0){
				$(".chooseSchedule").each(function(){
					var id=$(this).find("input[name='courseScheduleId']").val();
					scheduleIds=scheduleIds+","+id;
				})
				scheduleIds=scheduleIds.substring(1);
			}
			var searchTeacherName=$("#searchTeacherName").val();
			$.ajax({
				url:'${request.contextPath}/mobile/open/adjusttipsay/findTeacherList',
				data:{'unitId':'${unitId!}','acadyear':'${acadyear!}','semester':'${semester!}','teacherId':teacherId,'scheduleIds':scheduleIds,'searchTeacherName':searchTeacherName},
				type:'post', 
				dataType:'json',
				success:function(data){
					$(".teacherContent").html("");
					if(data.success){
						var groupDtoList=data.groupDtoList;
						addTeacherGroup(groupDtoList);
					}else{
						$(".teacherContent").html('<div class="mui-page-noData"><i></i><p class="f-16">未搜到结果</p></div>');
					}
					
				}
			});	
		}
		
		
		function addTeacherGroup(groupDtoList){
			var text='<ul class="mui-table-view choose-teacher">';
			for(var mm in groupDtoList){
				var groupDto=groupDtoList[mm];
				text=text+' <li class="mui-table-view-cell mui-collapse">'
					+'<a class="mui-navigate-right" href="#">'+groupDto.teachGroupName+'</a><ul class="mui-table-view">';
				for(var kk in groupDto.showList){
					var dto=groupDto.showList[kk];
					if(dto[2]=='1'){
						text=text+'<li class="mui-table-view-cell teacher-cell c-red" data-value="'+dto[0]+'" data-name="'+dto[1]+'">'+dto[1]+'<span class="f-right">存在冲突</span>';
					}else{
						text=text+'<li class="mui-table-view-cell teacher-cell " data-value="'+dto[0]+'" data-name="'+dto[1]+'">'+dto[1];
					}
					text=text+'</li>';
				}
				text=text+'</ul></li>';
			}
			text=text+'</ul>';
			$(".teacherContent").html(text);
		}
		
		$('#searchTeacherName').bind('search',function(){
			searchTeacherList();
		})
	</#if>
	
	<#if applyType?default('1')=='3'>
		$(".addTeacherId").click(function(){
			 $(".applyContent").hide();
			 $("#opopId").val("old");
			 $(".teacherDiv").show();
			 searchTeacherList("");
		})
	</#if>
    </script>
</body>
</html>
