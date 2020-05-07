<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">

<a class="btn btn-sm btn-blue mb10" href="javascript:void(0);" onclick="goback()">返回</a>
<div class="table-switch-container" id="allTable">
	<div class="table-switch-box"  id="leftTable">
		<div class="table-switch-filter">
			<span>待合班学生</span>
			<div class="filter filter-sm">
				<div class="filter-item">
					<span class="filter-name">时间点：</span>
					<div class="filter-content">
						<select id="batch" onchange="refresh(this.value)">
							<option value="1" <#if batch?default('0') == '1'>selected</#if>>时间点1</option>
							<option value="2" <#if batch?default('0') == '2'>selected</#if>>时间点2</option>
							<option value="3" <#if batch?default('0') == '3'>selected</#if>>时间点3</option>
						</select>
					</div>
				</div>													
				<div class="filter-item">
					<span class="filter-name">科目：</span>
					<div class="filter-content">
						<select id="subjectId" onchange="refresh('',this.value)">
						<#if courseList?exists && courseList?size gt 0>
						<#list courseList as course>
							<option value="${course.id!}" <#if course.id! == subjectId!>selected</#if>>${course.subjectName!}</option>
						</#list>
						
						<#else>
							<option>暂无科目</option>
						</#if>
						</select>
					</div>
				</div>													
			<#-- 	<div class="filter-item">
					<span class="filter-name">班级：</span>
					<div class="filter-content">
						<select id="classId" onchange="refresh('','',this.value)">
						<#if divideClassList?exists && divideClassList?size gt 0>
						<#list divideClassList as clz>
							<option value="${clz.id!}" <#if clz.id! == classId!>selected</#if>>${clz.className!}</option>
						</#list>
						</#if>
						</select>
					</div>
				</div>	 -->												
			</div>
		</div>
		<#-- leftTable content -->
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
		<script language="javascript" type="text/javascript">
		$(document).ready(function(){
			$("#${right!'0'}_sort").tablesorter({
				headers:{
					0:{sorter:false},
					1:{sorter:false}
				},
				sortInitialOrder: 'desc',
				widgets: ['fixFirstNumber']  
			});
		});
		</script>
		<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass">
		<div class="table-switch-data default labelInf">
			<span>总数：<em>${(dtoList?size)!0}</em></span>
			<span>男：<em>${manCount!0}</em></span>
			<span>女：<em>${woManCount!0}</em></span>
			<span>已选学生：<em>0</em></span>
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
					<th>班级</th>
					<th>姓名</th>
					<th>性别</th>
					<th>${courseName!}</th>
					<th>总成绩</th>
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
							<td>${dto.className!}</td>
							<td>${dto.studentName!}</td>
							<td>${dto.sex!}</td>
							<td>${dto.subjectScore[subjectId]!}</td>
							<td>${dto.subjectScore['TOTAL']!}</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
		</div>
	</div>
	<style>
	#copyItemDiv span{
		width:auto;
		height:36px;
		padding-left:20px;
		padding-right:20px;
	}
	#rightTable{
		margin-left:10px;
	}
	</style>
	<div class="table-switch-box" id="rightTable">
		<a class="btn btn-sm btn-blue" onclick="combine()" style="margin-bottom:10px;">合班</a><span>(请选择一个班级进行合班)</span>
		<#if showPureList?exists && showPureList?size gt 0>
		<div class="publish-course choose-course" id="copyItemDiv">
			<#list showPureList as clz>
				<span  data-value="" id="${clz.id!}">
					${clz.className!}(${clz.studentCount!0})
				</span>
			</#list>	
		</div>
		<#else>
			<br>没有可以合班的班级
		</#if>
	</div>
</div>
<script>
var useMaster = '';
function refresh(batch,subjectId,classId){
	if(!batch){
		batch = $('#batch').val();
	}
	if(!subjectId){
		subjectId = $('#subjectId').val();
	}
	//if(!classId){
	//	classId = $('#classId').val();
	//}
	var params = 'batch='+batch+'&course.id='+subjectId+'&classId='+classId+"&useMaster="+useMaster;
	var url = '${request.contextPath}/newgkelective/${divideId!}/showBatchJxbClass/index?'+params;
	$("#batchDiv").load(url);
}
function combine(){
	var stuIds = [];
	var i = 0;
	$("#leftTable").find('tbody input:checkbox[name="course-checkbox"]:checked').each(function(i){
		stuIds[i]=$(this).val();
		i=i+1;
	});
	
	var classId = $('#copyItemDiv span.active').attr('id');
	var oldClassId = $('#classId').val();
	if(stuIds.length == 0 || !classId ){
		alert('选中学生以及要合班的班级才能合班');
		return;
	}
	var params = {"batch":"${batch!}","stuIds":stuIds,"classId":classId,"course.id":"${subjectId!}","oldClassId":oldClassId};
	$.ajax({
		url:"${request.contextPath}/newgkelective/${divideId}/doCombine",
		data:params,
		dataType: "JSON",
		success: function(data){
			if(data.success){
				layer.msg("合班成功！", {
					offset: 't',
					time: 2000
				});
				updateNum();
				useMaster = "1";
				refresh();
			}else{
				layerTipMsg(data.success,"失败",data.msg);
				
			}
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
	
}
function updateNum(){
	updateTableInf("leftTable"); 
	
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
	$("#"+obj).find('tbody input:checkbox[name="course-checkbox"]:checked').parents('tr').remove();
	
	var all = $("#"+obj).find('tbody tr');
	var i = 0;
	all.each(function(){
		i = i+1;
		$(this).find('td:eq(1)').text(i);
	});
	$("#"+obj).find(".labelInf span em:eq(0)").text(all.length);
}
$('.choose-course span').off('click').on('click', function(e){
	e.preventDefault();
	if($(this).hasClass('disabled')) return;

	if($(this).hasClass('active')){
		$(this).removeClass('active');
	}else{
		$('.choose-course span').removeClass('active');
		$(this).addClass('active');
	}
});
function goback(){
	var url = '/newgkelective/${divideId!}/showStudentDistribution/page';
	$("#batchDiv").load(url);
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
$('input[type="checkbox"]').click(function(event){
	var all = $(event.target).parents("table").find("tbody input:checkbox[name='course-checkbox']:checked");
	$(".labelInf span em").eq(3).text(all.length);
});
</script>