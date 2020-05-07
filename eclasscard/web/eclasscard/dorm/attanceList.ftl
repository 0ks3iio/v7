<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="main-content">
	<div class="main-content-inner">
		<div class="page-content">
			<div class="box box-default">
				<div class="box-body">
					<div class="filter" id="searchType">
						<div class="filter-item" id="dateDiv">
							<span class="filter-name">日期：</span>
							<div class="filter-content">
								<div class="input-group">
									<input class="form-control date-picker startTime-date date-picker-time" style="width:110px" vtype="data" style="width: 120px" type="text"  name="searchDate" id="searchDate" value="${(attDto.searchDate?string('yyyy-MM-dd'))!}" onchange="doSearch('1');">
									<span class="input-group-addon">
										<i class="fa fa-calendar bigger-110"></i>
									</span>
								</div>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">寝室楼：</span>
							<div class="filter-content">
								<select name="buildingId" id="buildingId" class="form-control" style="width:100px" onchange="doSearch('1');">
									<#if buildingList?exists && buildingList?size gt 0>
										<#list buildingList as item> 
											<option value="${item.buildingId!}" <#if attDto.buildingId?default("")==item.buildingId>selected="selected"</#if>>${item.buildingName!}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">年级：</span>
							<div class="filter-content">
								<select name="gradeCode" id="gradeCode" class="form-control" style="width:110px" onchange="doSearch();">
									<option value="">--请选择--</option>
									<#if gradeList?exists && gradeList?size gt 0>
										<#list gradeList as item>
											<option value="${item.gradeCode!}" <#if attDto.gradeCode?default("")==item.gradeCode>selected="selected"</#if>>${item.gradeName!}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">考勤时段：</span>
							<div class="filter-content">
								<select name="periodId" id="periodId" class="form-control" style="width:110px" onchange="doSearch();">
									<#--<option value="">--请选择--</option>-->
									<#if dormPdList?exists && dormPdList?size gt 0>
										<#list dormPdList as item>
											<option value="${item.id!}" <#if attDto.periodId?default("")==item.id>selected="selected"</#if>>${item.beginTime!}-${item.endTime!}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">状态：</span>
							<div class="filter-content">
								<select name="attStatus" id="attStatus" class="form-control" style="width:110px" onchange="doSearch();">
									<option value=0>--请选择--</option>
									<option value=1 <#if attDto.attStatus?default(0)==1>selected="selected"</#if>>未签到</option>
									<option value=2 <#if attDto.attStatus?default(0)==2>selected="selected"</#if>>请假</option>
									<option value=3 <#if attDto.attStatus?default(0)==3>selected="selected"</#if>>已签到</option>
								</select>
							</div>
						</div>
						<div class="filter">
							<div class="filter-item filter-item-right" >
								<button class="btn btn-blue" onclick="doExport()">导出</button>
							</div>
						</div>
					</div>
					<div class="table-container">
						<div class="table-container-body">
							<table class="table table-striped">
								<thead>
									<tr>
										<th style='width:4%'>序号</th>
										<th style='width:15%'>刷卡时间</th>
										<th style='width:9%'>学生姓名</th>
										<th style='width:35%'>请假信息</th>
										<th style='width:9%'>寝室</th>
										<th style='width:9%'>行政班</th>
										<th style='width:9%'>班主任</th>
										<th style='width:10%'>状态</th>
									</tr>
								</thead>
								<tbody>
									<#if attenceList?exists && attenceList?size gt 0>
										<#list attenceList as item>
										<tr>
											<td>${item_index+1}</td>
											<td>${(item.clockDate?string("yyyy-MM-dd HH:mm:ss"))!}</td>
											<td>${item.studentName!}<#if item.isFirst == 1><span class="redPoint"></span></#if></td>
											<td>${item.content!}</td>
											<td>${item.roomName!}</td>
											<td>${item.gradeName!}${item.className!}</td>
											<td>${item.teacherName!}</td>
											<td>
											<select name="status" id="status${item_index}" class="form-control" onchange="saveStatus('${item.id!}','${item_index}')">
												<option value=3 <#if item.status==3>selected="selected"</#if>>已签到</option>
												<option value=1 <#if item.status==1>selected="selected"</#if>>未签到</option>
												<option value=2 <#if item.status==2>selected="selected"</#if>>请假</option>
											</select>
											</td>
										</tr>
										</#list>
									<#else>
									<tr>
										<td colspan="8" align="center">暂无数据</td>
									</tr>
									</#if>
									
								</tbody>
							</table>
							<#if attenceList?exists && attenceList?size gt 0>
							<@htmlcom.pageToolBar container="#itemShowDiv">
							</@htmlcom.pageToolBar>
							</#if>
						</div>
					</div>
				</div>
			</div>
		</div><!-- /.page-content -->
	</div>
</div><!-- /.main-content -->
<script>
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2',
			'endDate' : '${nowDate?string('yyyy-MM-dd')}',
			'startDate' : '${beginDate?string('yyyy-MM-dd')}'
		};
		initCalendarData("#dateDiv",".date-picker",viewContent);
	});
	
	function doSearch(type){
		if(type=='1'){
			$("#gradeCode").val("");
			$("#periodId").val("");
		}
		$("#itemShowDiv").load("${request.contextPath}/eclasscard/dorm/attance/list/page?"+searchUrlValue("#searchType"));
	}
	function toContent(content){
		layer.confirm(content);
	}
	function doExport(){
		document.location.href = "${request.contextPath}/eclasscard/dorm/attance/export?"+searchUrlValue("#searchType");
	}
	var isSubmit = false;
	function saveStatus(id,number){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var status=$("#status"+number).val();
		$.ajax({
			url:'${request.contextPath}/eclasscard/dorm/attance/saveStatus',
			data:{'id':id,'status':status},
			type:"post",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
		 			doSearch();
		 		}else{
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
    			}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
</script>
