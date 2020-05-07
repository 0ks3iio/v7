<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">

<script>
$.tablesorter.addWidget({  
  id: 'fixFirstNumber',  
  format: function(table, config, widgetOptions, initFlag) {  
    for(var i=1;i<table.rows.length;i++){  
        //console.log(table.rows[i].cells[1].innerHTML);  
        table.rows[i].cells[1].innerHTML = i;  
    }  
  }  
});  
</script>

<#--<a href="javascript:void(0)" onclick="gobackResult()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<#if courseMap?exists && (courseMap?size>0)>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist" id="subjectTabs">
			<#if courseMap?exists && (courseMap?size>0)>
				<#assign index=1>
				<#list courseMap?keys as subjectName>
					<li <#if index == 1>class="active"</#if> data-subjectId="${courseMap[subjectName].id}" role="presentation">
						<a onclick="changeSubjectTab('${courseMap[subjectName].id}')" href="javascript:void(0)" role="tab" data-toggle="tab">${subjectName}</a>
					</li>
					<#assign index=index+1>
				</#list>
			</#if>
		</ul>
		
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
			<#-- 	<div class="filter">
					<div class="filter-item">
						<a class="btn btn-blue" onclick="saveRangeInfo('${divideId}')">保存</a>
					</div>
				</div>  -->
				<div class="table-switch-container" id="allTable">
					<div class="table-switch-box"  id="leftTable">
						<div class="table-switch-filter">
							<div class="filter filter-sm">
								<div class="filter-item">
									<span class="filter-name">前&nbsp;</span>
									<div class="filter-content">
										<input id="rowNum" type="text" class="form-control inline-block input-sm no-margin" style="width:60px;">
										行
										&nbsp;
										<a class="btn btn-sm btn-blue" onclick="selMutil()">确定勾选</a>
									</div>
								</div>													
								<div class="filter-item">
									<div class="filter-content">
										<#if isCanEdit><a class="btn btn-sm btn-blue" onclick="resetRange()">重置</a></#if>
									<!-- <a class="btn btn-sm btn-blue" onclick="changeRangeTab('A')">加载右侧</a>  -->
									</div>
								</div>													
							</div>
						</div>
						<#-- leftTable content -->
					</div>
					
					<div class="table-switch-control">
						<a id='toRight' <#if isCanEdit> onclick='moveStudents("R")' </#if> class="btn btn-sm"><i class="wpfont icon-arrow-right"></i></a>
						<a id='toLeft' <#if isCanEdit> onclick='moveStudents("L")'</#if> class="btn btn-sm"><i class="wpfont icon-arrow-left"></i></a>
					</div>
					<div class="table-switch-box" id="rightTable">
						<#-- rightTable content -->
						<div class="tab-content" style="padding: 0;">
							
							
							<div id="dd" class="tab-pane" role="tabpanel"></div>
						</div>
					</div>
				</div>
			</div>
			<div id="bb" class="tab-pane" role="tabpanel"></div>
		</div>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-long btn-blue" onclick="toNextStep()">下一步</a>
</div>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/7choose3/noSelectSystem.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">没有需要单科分层的数据</p>
			</div>
		</div>
	</div>
	<div class="text-center">
		<a class="btn btn-long btn-blue" onclick="toNextStep()">下一步</a>
	</div>
</#if>
<script>
	showBreadBack(gobackResult, false, "分班安排");
	function gobackResult(){
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		<#if fromArray?default('') == '1'>
			<#if arrayId?default('')==''>
			   url = '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${plArrayId!}';
		   <#else>
			   url = '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
		   </#if>
		</#if>
		$("#showList").load(url);
	}
<#if courseMap?exists && (courseMap?size>0)>
	changeSubjectTab(getSubjectId());
	
	function getSubjectId(){
		var subjectId = $('#subjectTabs .active').attr('data-subjectId');
		return subjectId;
	}
	
	var leftTableStr = "";
	var needSubmit = false;
	
	var isChangeSubjectTab=false;
	function changeSubjectTab(subjectId){
		if(isChangeSubjectTab){
			return;
		}
		isChangeSubjectTab=true;
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId}/subjectChoiceInfo",
			data:{"subjectId":subjectId},
			dataType: "HTML",
			success: function(data){
				var div1 = $("#leftTable .table-switch-filter")[0];
				$("#leftTable").html(div1);
				$("#leftTable").append(data);
				changeRangeTab('A');
				leftTableStr = $("#leftTable").html()+"";
				isChangeSubjectTab=false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
	}
	
	function changeRangeTab(range){
		if(needSubmit){
			//$("#leftTable").html(leftTableStr);
			//updateSort();
		}
	
		var divideId = '${divideId}';
		var subjectId = getSubjectId();
		
		//alert($('#subjectTabs .active').length);
		//alert($('#subjectTabs .active').attr('data-subjectId'));
		//alert($('#subjectTabs li .active :first'));
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId}/showRangeStus",
			data:{"subjectId":subjectId,"range":range},
			dataType: "HTML",
			success: function(data){
				$("#rightTable").html(data);
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
		needSubmit=false;
	}
	
	var leftContent = $("#leftTable").html();
	var rightContent = $("#rightTable").html();
	
	var submitting = false;
	function moveStudents(flag){
		leftContent = $("#leftTable").html();
		rightContent = $("#rightTable").html();
	
		var src = '';
		var dest = '';
		if(flag == 'R'){
			src = "#leftTable";
			dest = "#rightTable";
		}else{
			dest = "#leftTable";
			src = "#rightTable";
		}
		var i=0;
		var stuId=[];
		//增加到右边的html
		var htmlText="";
		if($(src).find('tbody input:checkbox[name="course-checkbox"]:checked').length>0){
			if(submitting){
				//alert('正在提交');
				return;
			}
		
			submitting = true;
		
			$(src).find('tbody input:checkbox[name="course-checkbox"]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				htmlText=htmlText+'<tr class="new">'+$(this).parents("tr").html()+'</tr>';
				$(this).parents("tr").addClass("linChangeClass");
			});
			$(dest).find("table").find("tbody").append(htmlText);
			$(src).find("table").find(".linChangeClass").remove();
			
			needSubmit = true;
			
			saveRangeInfo();
		}
		
		
		//updateNum();
	}
	
	function updateSort(){
		if($("#0_sort")){
			$("#0_sort").trigger("update");
		}
		if($("#1_sort")){
			$("#1_sort").trigger("update");
		}
		
	}
	
	function updateNum(){
		var num = $("#rightTable").find('tbody input:checkbox[name="course-checkbox"]').length;
		$('#rangeTab .active span').text(num);
		
		updateTableInf("leftTable"); 
		updateTableInf("rightTable"); 
		
		updateSort();
	}
	
	function updateTableInf(obj){
		var all = $("#"+obj).find('tbody tr');
		if(all.length <1){
			$("#"+obj).find(".labelInf span em").text(0);
			return;
		}
		
		var avg1=0.0;
		var ysyAvg=0.0;
		var allAvg=0.0;
		var manCount =0;
		var woManCount=0;
		var i = 0;
		all.each(function(){
			i = i+1;
			$(this).find('td:eq(1)').text(i);
			if($(this).find('td:eq(3)').text() == '男'){
				manCount = manCount +1;
			}
			if($(this).find('td:eq(3)').text() == '女'){
				woManCount =woManCount+1;
			}
			avg1 = avg1 + parseFloat($(this).find('td:eq(4)').text());
			ysyAvg = ysyAvg + parseFloat($(this).find('td:eq(5)').text());
			allAvg = allAvg + parseFloat($(this).find('td:eq(6)').text());
		});
		
		var labelInfs = $("#"+obj).find(".labelInf span em");
		labelInfs.eq(0).text(all.length);
		labelInfs.eq(1).text(manCount);
		labelInfs.eq(2).text(woManCount);
		labelInfs.eq(3).text((avg1/all.length).toFixed(2));
		labelInfs.eq(4).text((ysyAvg/all.length).toFixed(2));
		labelInfs.eq(5).text((allAvg/all.length).toFixed(2));
	}
	
	function saveRangeInfo(divideId){
		
		var subjectId = getSubjectId();
		var range = $('#rangeTab .active').attr('data-range');
		
		var stuIds = [];
		var i = 0;
		$("#rightTable").find('tbody input:checkbox[name="course-checkbox"]').each(function(i){
			stuIds[i]=$(this).val();
			i=i+1;
		});
		
		//return;
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId}/saveRangeInfo",
			data:{"subjectId":subjectId,"range":range,"stuIds":stuIds},
			dataType: "JSON",
			success: function(data){
				if(data.success){
					layer.msg("保存成功！", {
						offset: 't',
						time: 2000
					});
					
					updateNum();
					//needSubmit = false;
				//	leftTableStr = $("#leftTable").html()+"";
					
				}else{
					layerTipMsg(data.success,"失败",data.msg);
					
					$("#leftTable").html(leftContent);
					$("#rightTable").html(rightContent);
					
				}
				submitting = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
	}
	
	function resetRange(){
		var subjectId = getSubjectId();
		$.ajax({url:"${request.contextPath}/newgkelective/${divideId}/resetRange",
				data:{subjectId:subjectId},
				dataType:"JSON",
				success: function(data){
					if(data.success){
						//layerTipMsg(data.success,"成功",data.msg);
						layer.msg("重置成功！", {
							offset: 't',
							time: 2000
						});
						changeSubjectTab(subjectId);
						
					}else{
						layerTipMsg(data.success,"失败",data.msg);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	function toNextStep(){
		<#if isCanEdit>
			var url =  '${request.contextPath}/newgkelective/${divideId!}/checkRangeFinish';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					if(data.success){
						//layerTipMsg(data.success,"已安排完毕","");
						$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/singleList/page");
					}else{
						layerTipMsg(data.success,"失败",data.msg);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});
		<#else>
			$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/singleList/page");
		</#if>
		
	}
	
	
	function selMutil(){
		var rowNum = $("#rowNum").val();
		
		if(isNaN(rowNum)){
			layer.msg("只能输入整数！", {
				offset: 't',
				time: 2000
			});
			return;
		}else if(rowNum.indexOf(".") != -1){
			layer.msg("只能输入整数！", {
				offset: 't',
				time: 2000
			});
			return;
		}
		
		$("#leftTable").find('tbody input:checkbox[name="course-checkbox"]').prop('checked',false);
		var checkboxs = $("#leftTable").find('tbody input:checkbox[name="course-checkbox"]:lt('+rowNum+')');
		checkboxs.each(function(){
			$(this).prop('checked',true);
		});
	}
	
	function selAll(event){
		var checked =$(event.target).is(':checked');
		<#--var checked = $(event.target).prop("checked");-->
		var all = $(event.target).parents("table").find("tbody input:checkbox[name='course-checkbox']");
		if(checked){
			all.prop('checked',true);
		}else{
			all.prop('checked',false);
		}
	}
<#else>
	function toNextStep(){
		$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/singleList/page");
	}
</#if>

</script>