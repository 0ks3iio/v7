<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
			<div class="box box-default">
				<div class="box-body">
					<div class="filter" id="searchType">
						<div class="filter-item" id="dateDiv">
							<span class="filter-name">时段：</span>
							<div class="filter-content">
								<div class="input-group" style="width:260px;">
									<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 145px" type="text"  name="startTime" id="startTime" value="${startTime?string('yyyy-MM-dd HH:mm')}"  onchange="showList();">									
									<span class="input-group-addon">
										<i class="fa fa-minus"></i>
									</span>
									<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 145px" type="text"  name="endTime" id="endTime" value="${(endTime?string('yyyy-MM-dd HH:mm'))!}" onchange="showList();">
								</div>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">行政班级：</span>
							<div class="filter-content">
								<select name="classId" id="classId" class="form-control" style="width:120px" onchange="showList();">
									<#if clazzList?exists && clazzList?size gt 0>
										<#list clazzList as item> 
											<option value="${item.id!}">${item.classNameDynamic!}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">刷卡班牌场地：</span>
							<div class="filter-content">
								<select name="placeId" id="placeId" class="form-control" style="width:120px" onchange="showList();">
									<option value="00000000000000000000000000000000">--请选择--</option>
									<#if placeNameMap?exists && placeNameMap?size gt 0>
										<#list placeNameMap?keys as key>
　　											<option value="${key}">${placeNameMap[key]}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">刷卡状态：</span>
							<div class="filter-content">
								<select name="state" id="state" class="form-control" style="width:120px" onchange="showList();">
									<option value="0">--请选择--</option>
									<option value="1">已刷卡</option>
									<option value="2">未刷卡</option>
									<option value="3">请假</option>
								</select>
							</div>
						</div>
					</div>
					<div class="table-container" id = "signInListDiv">
						
					</div>
				</div>
			</div>
<script>
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd hh:ii',
			'minView' : '0',
			'endDate' : '${endTime?string('yyyy-MM-dd HH:mm')}'
		};
		var viewContentEnd={
			'format' : 'yyyy-mm-dd hh:ii',
			'minView' : '0',
		};
		initCalendarData("#dateDiv","#startTime",viewContent);
		initCalendarData("#dateDiv","#endTime",viewContentEnd);
		showList();
	});

	function showList() {
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var classId = $("#classId").val();
		var placeId = $("#placeId").val();
		var state = $("#state").val();
		var str = "?classId="+classId+"&placeId="+placeId+"&state="+state+"&startTime="+encodeURI(startTime)+"&endTime="+encodeURI(endTime);
		var url =  '${request.contextPath}/eclasscard/signin/list'+str;
		$("#signInListDiv").load(url);
	}
</script>
