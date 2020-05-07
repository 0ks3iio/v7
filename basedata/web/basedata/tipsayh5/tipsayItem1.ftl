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
    <div class="mui-content add-form applyContent" style="padding-bottom: 51px;">
    	<ul class="mui-table-view">
    		<li class="mui-table-view-cell">
            	<span class="f-16 f-left">审核状态</span>
            	<span class="f-16 f-right c-blue">
					<#if tipsayDto.tipsay.state?default('0')=='1'>
					已通过
					<#elseif tipsayDto.tipsay.state?default('0')=='2'>
					未通过
					<#elseif tipsayDto.tipsay.tipsayType?default('')=='03'>
					待安排
					<#elseif tipsayDto.tipsay.tipsayType?default('')=='02'>
					待审核
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
    	<#if tipsayDto.tipsay.state?default('0')=='0' && tipsayDto.tipsay.tipsayType?default('')=='03'>
    	
    	<#else>
	    	<ul class="mui-table-view">
	    		<li class="mui-table-view-cell">
	            	<span class="f-16 f-left"><#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>教师</span>
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
    <nav class="mui-bar mui-bar-tab footButton applyContent">
    	<#if tipsayDto.tipsay.state?default('0')=='1'>
			<a href="javascript:" class="mui-tab-item f-16 opopTipsay" data-value="${tipsayDto.tipsay.id!}" data-type="0" style="background: #2f7bff;color: #fff;">撤销</a>
		<#elseif tipsayDto.tipsay.state?default('0')=='2'>
			<a href="javascript:" class="mui-tab-item f-16 opopTipsay" data-value="${tipsayDto.tipsay.id!}" data-type="0" style="background: #2f7bff;color: #fff;">撤销</a>
		<#elseif tipsayDto.tipsay.tipsayType?default('')=='03'>
			<a class="mui-tab-item f-16 opopTipsay" data-value="${tipsayDto.tipsay.id!}" data-type="2" style="background: #2f7bff;color: #fff;">不通过</a>
			<a class="mui-tab-item f-16 ArrangeTipsayTeacher" data-value="${tipsayDto.tipsay.id!}" style="background: #2f7bff;color: #fff;">安排老师</a>
		<#elseif tipsayDto.tipsay.tipsayType?default('')=='02'>
			<a class="mui-tab-item f-16 opopTipsay" data-value="${tipsayDto.tipsay.id!}" data-type="2" style="background: #2f7bff;color: #fff;">不通过</a>
			<a class="mui-tab-item f-16 opopTipsay" data-value="${tipsayDto.tipsay.id!}" data-type="1" style="background: #2f7bff;color: #fff;">通过</a>
		</#if>
    </nav>
    <#---老师列表-teacherDiv-->
    <#if tipsayDto.tipsay.state?default('0')=='0' && tipsayDto.tipsay.tipsayType?default('')=='03'>
    <div class="search-container teacherDiv" style="display:none;">
    	<input type="hidden" id="opopId" value="">
    	<div class="mui-content-padded">
	    	<div class="mui-input-row mui-search">
	    	    <input type="search" id="searchTeacherName" class="mui-input-clear" placeholder="请输入教师名称">
	    	</div>
	    </div>
    </div>
    <div class="mui-content teacherContent teacherDiv" style="padding-top: 40px;display:none;">
    	<#if groupDtoList?exists && groupDtoList?size gt 0>
    	<#list groupDtoList as groupDto>
    		<ul class="mui-table-view choose-teacher">
    	    <li class="mui-table-view-cell mui-collapse">
    	        <a class="mui-navigate-right" href="#">${groupDto.teachGroupName!}</a>
    	        <ul class="mui-table-view">
    	        	<#list groupDto.showList as item>
    	        	<#if item[2]=='1'>
	                <li class="mui-table-view-cell c-red teacher-cell" data-value="${item[0]!}" data-name="${item[1]!}">${item[1]!}<span class="f-right">存在冲突</span></li>
	                <#else>
	                 <li class="mui-table-view-cell teacher-cell" data-value="${item[0]!}" data-name="${item[1]!}">${item[1]!}</li>
	                </#if>
	                </#list>
	            </ul>
    	    </li>
    	</ul>
    	</#list>
    	<#else>
    	<div class="mui-page-noData"> 
	    	 <i></i>
	    	 <p class="f-16">未搜到结果</p>
	    </div>
	    </#if>
    </div>
    </#if>
    <#else>
    	<div class="mui-content applyContent" style="padding-bottom: 51px;">
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
    	if((!$(".applyContent").is(":hidden") )){
    		loadBackList();
    	}else{
    		$(".applyContent").show();
    		$(".teacherDiv").hide();
    	}
     	
	});
    (function($) {
		$.init();
	})(mui);
	
	function loadBackList(){
		var parm='&acadyear=${acadyear!}&semester=${semester!}&week=${week!}&statType=${statType!}';
		var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type=${type!}'+parm;
		loadByHref(url);
	}
	<#if error?default('')==''>
	var deleFlag=false;
	$(".footButton").on('tap','.opopTipsay',function(e){
		var tipsayId=$(this).attr("data-value");
		var opopType=$(this).attr("data-type");
		var opopname="";
		if(opopType=="0"){
			opopname="撤销";
		}else if(opopType=="1"){
			opopname="通过";
		}else if(opopType=="2"){
			opopname="不通过";
		}else{
			return;
		}
		mui.confirm('确定要'+opopname+'吗？', '',['取消','确认'], function(e) {
            if (e.index == 1) {
            	//确认
                deleteTipsay(tipsayId);
            } else {
            	//取消
            	deleFlag=false;
            }
        })
	})
	
	function opopFunction(tipsayId,opopType){
		if(deleFlag){
			return;
		}
		deleFlag=true;
		
		if(opopType=="0"){
			deleteTipsay(tipsayId);
		}else if(opopType=="1"){
			agreeOrNot(tipsayId,"1");
		}else if(opopType=="2"){
			agreeOrNot(tipsayId,"0");
		}else{
			return;
		}
	}
	function deleteTipsay(tipsayId){
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
	
	function agreeOrNot(tipsayId,state){
		$.ajax({
			url:'${request.contextPath}/mobile/open/adjusttipsay/agreeOrNot',
			data:{'tipsayId':tipsayId,'state':state,'teacherId':'${teacherId!}'},
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
	
	//安排老师
	 <#if tipsayDto.tipsay.state?default('0')=='0' && tipsayDto.tipsay.tipsayType?default('')=='03'>
	 	$(".footButton").on('tap','.ArrangeTipsayTeacher',function(e){
	 		$("#searchTeacherName").val("");
			$(".applyContent").hide();
    		$(".teacherDiv").show();
		})
	 	
	 	var arrangeFlag=false;
	 	$(".teacherDiv").on("tap",".teacher-cell",function(){
			var tId=$(this).attr("data-value");
			var tName=$(this).attr("data-name");
			
			mui.confirm('确定选择'+tName+'<#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>吗？', '',['取消','确认'], function(e) {
	            if (e.index == 1) {
	            	//确认
	                arrangeTipsayTeacher(tId);
	            } else {
	            	//取消
	            	arrangeFlag=false;
	            }
       		 })
			
		})
		
		function arrangeTipsayTeacher(tId){
			if(arrangeFlag){
				return;
			}
			arrangeFlag=true;
			$.ajax({
				url:'${request.contextPath}/mobile/open/adjusttipsay/arrangeTeacher',
				data:{'tipsayId':'${tipsayDto.tipsay.id!}','newTeacherId':tId,'teacherId':'${teacherId!}'},
				type:'post', 
				dataType:'json',
				success:function(data){
					arrangeFlag=false;
					if(data.success){
						loadBackList()
					}else{
						toastMsg(data.msg);
					}
					
				}
			});	
		}
		//教师搜索
		$('#searchTeacherName').bind('search',function(){
			var tipsayId='${tipsayDto.tipsay.id!}';
			console.log(tipsayId);
			searchTeacherList2(tipsayId);
		})
		//教师列表
		function searchTeacherList2(tipsayId){
			var searchTeacherName=$("#searchTeacherName").val();
			$.ajax({
				url:'${request.contextPath}/mobile/open/adjusttipsay/findTeacherList2',
				data:{'unitId':'${unitId!}','tipsayId':tipsayId,'searchTeacherName':searchTeacherName},
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
	 </#if>
	

	</#if>
    </script>
</body>
</html>
