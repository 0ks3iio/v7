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
<a class="btn btn-sm btn-blue mb10" href="javascript:void(0);" onclick="goback()">返回</a>
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
					<span class="filter-name">开班数:</span>
					<div class="filter-content">
						<input id="classNum" type="text" class="form-control inline-block input-sm no-margin" style="width:60px;">
						<a class="btn btn-sm btn-blue" href="javascript:void(0);" onclick="divideMutil()">确定</a>
					</div>
				</div>													
			</div>
		</div>
		<#-- leftTable content -->
		<script language="javascript" type="text/javascript">
		$(document).ready(function(){
			$("#${right!'0'}_sort").tablesorter({
				headers:{
					0:{sorter:false},
					1:{sorter:false},
					2:{sorter:false},
				},
				sortInitialOrder: 'desc',
				widgets: ['fixFirstNumber']  
			});
		});
		</script>
		<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass">
		<div class="table-switch-data default labelInf">
			<span>总数：<em>${dtoList?size!}</em></span>
			<span>男：<em>${manCount!}</em></span>
			<span>女：<em>${woManCount!}</em></span>
			<span>${courseName!}：<em>${courseAvg?string("#.##")!'0'}</em></span>
		</div>
		<table class="table table-bordered table-striped table-hover no-margin mainTable tablesorter" style="font-size:1em;" id="${right!'0'}_sort">
			<thead>
				<tr>
					<th>
						<label class="pos-rel">
							<input onclick="selAll(event)" name="course-checkbox" type="checkbox" class="wp">
							<span class="lbl"></span>
						</label>
					</th>
					<th>序号</th>
					<th>姓名</th>
					<th>性别</th>
					<th>班级</th>
					<th>${courseName!}</th>
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && (dtoList?size>0)>
					<#list dtoList as dto>
						<tr>
							<td>
								<label class="pos-rel">
									<input name="course-checkbox" type="checkbox" class="wp" value="${dto.studentId!}">
									<span class="lbl"></span>
								</label>
							</td>
							<td>${dto_index +1}</td>
							<td>${dto.studentName!}</td>
							<td>${dto.sex!}</td>
							<td>${dto.className!}</td>
							<td>${dto.subjectScore[subjectId]!}</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
		</div>
	</div>
	
	<div class="table-switch-control">
		<a id='toRight' onclick='moveStudents("R")'  class="btn btn-sm"><i class="wpfont icon-arrow-right"></i></a>
		<a id='toLeft'  onclick='moveStudents("L")' class="btn btn-sm"><i class="wpfont icon-arrow-left"></i></a>
	</div>
	<div class="table-switch-box" id="rightTable">
		
		
	</div>
</div>
		
<script>
$(function(){
	showBatchClass('${classId!}');
});

function goback(){
	var url = '${request.contextPath}/newgkelective/${divideId!}/manualDivide/index';
	$("#batchDiv").load(url);
}

var submitting = false;
var	leftContent = $("#leftTable").html();
var	rightContent = $("#rightTable").html();
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
		
		saveStudents();
	}
	
	//updateNum();
}
function showBatchClass(classId){
	if(!classId){
		classId = '';
	}
	var params = 'batch=${batch!}&course.id=${subjectId!}&classId='+classId;
	var url = '${request.contextPath}/newgkelective/${divideId}/showDivideStudents?'+params;
	$('#rightTable').load(url);
}
function divideMutil(){
	var classNum = $("#classNum").val();
	if(isNaN(classNum) || classNum.indexOf('.') != -1 || classNum < 1){
		alert('必须是正整数！');
		return;
	}
	if($("#leftTable").find('tbody input:checkbox[name="course-checkbox"]:checked').length < 1){
		$('#leftTable thead th input:checkbox[name="course-checkbox"]').click();
		if($("#leftTable").find('tbody input:checkbox[name="course-checkbox"]:checked').length < 1){
			alert('请选择学生！');
			return;
		}
	}
	layer.confirm('确定按照系统规则开班吗？', function(index2){
		var stuId = [];
		var i = 0;
		$("#leftTable").find('tbody input:checkbox[name="course-checkbox"]:checked').each(function(i){
			stuId[i]=$(this).val();
			i=i+1;
			$(this).prop('checked',false);
			$(this).parents("tr").addClass("linChangeClass");
		});
		$("#leftTable").find("table").find(".linChangeClass").remove();
		
		saveStudents(classNum,stuId);
	});
}
function saveStudents(classNum,stuIds){
	var classId = $('#classId').val();
	if(!classId){
		classId = '';
	}
	var batch = '${batch!}';
	var subjectId = '${subjectId!}';
	
	if(!classNum){
		stuIds = [];
		var i = 0;
		$("#rightTable").find('tbody input:checkbox[name="course-checkbox"]:enabled').each(function(i){
			stuIds[i]=$(this).val();
			i=i+1;
		});
	}
	
	//return;
	var params = {"studentIds":stuIds,'classId':classId,'batch':batch,'course.id':subjectId,'classNum':classNum};
	$.ajax({
		url:"${request.contextPath}/newgkelective/${divideId}/modifyStudents",
		data:params,
		dataType: "JSON",
		success: function(data){
			if(data.success){
				layer.msg("保存成功！", {
					offset: 't',
					time: 2000
				});
				
				updateNum();
				var hcb = $("#hcb").val();
				if(classNum || !classId || hcb == 1){
					showBatchClass();
				}
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
function deleteClass(){
	var classId = $('#classId').val();
	layer.confirm('确定删除吗？', function(index){
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId}/deleteManualClass",
			data: "classId="+classId,
			dataType: "JSON",
			success: function(data){
				if(data.success){
					layer.msg("删除成功！", {
						offset: 't',
						time: 2000
					});
					
					//showBatchClass();
					refreshPage();
				}else{
					layerTipMsg(data.success,"失败",data.msg);
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	})
}
function refreshPage(){
	var params = 'batch=${batch!}&course.id=${subjectId!}';
	var url = "${request.contextPath}/newgkelective/${divideId!}/showUndivideStudents?"+params;
	$("#batchDiv").load(url);
}
function updateNum(){
	updateTableInf("leftTable"); 
	updateTableInf("rightTable"); 
	
	updateSort();
}
function updateSort(){
	if($("#0_sort")){
		$("#0_sort").trigger("update");
	}
	if($("#1_sort")){
		$("#1_sort").trigger("update");
	}
	
}
function updateTableInf(obj){
	var all = $("#"+obj).find('tbody tr');
	if(all.length <1){
		$("#"+obj).find(".labelInf span em").text(0);
		return;
	}
	
	var avg1=0.0;
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
		avg1 = avg1 + parseFloat($(this).find('td:eq(5)').text());
	});
	
	var labelInfs = $("#"+obj).find(".labelInf span em");
	labelInfs.eq(0).text(all.length);
	labelInfs.eq(1).text(manCount);
	labelInfs.eq(2).text(woManCount);
	labelInfs.eq(3).text((avg1/all.length).toFixed(2));
}
function selMutil(){
	var rowNum = $("#rowNum").val();
	
	if(isNaN(rowNum)){
		layer.msg("只能输入整数！", {
			offset: 't',
			time: 2000
		});
		return;
	}else if(rowNum < 0){
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
	var checked = $(event.target).prop("checked");
	var all = $(event.target).parents("table").find("tbody input:checkbox[name='course-checkbox']:enabled");
	if(checked){
		all.prop('checked',true);
	}else{
		all.prop('checked',false);
	}
}
//showBatchClass();
</script>