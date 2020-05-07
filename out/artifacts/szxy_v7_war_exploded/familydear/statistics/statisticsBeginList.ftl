<div id="a1" class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
		<span class="filter-name">起止日期：</span>
		<div class="filter-content">
			<div class="input-group float-left"">
				<input id="startTime" autocomplete="off" name="startTime" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="开始时间" value="${startTime?string('yyyy-MM-dd')!}" />
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
			<span class="float-left mr10 ml10"> 至 </span>
			<div class="input-group float-left">
				<input id="endTime" autocomplete="off" name="endTime" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="结束时间" value="${endTime?string('yyyy-MM-dd')!}" />
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
		</div>
	</div>
		<div class="filter-item">
			<span class="filter-name">部门：</span>
			<div class="filter-content">
				<select name="" id="deptId" class="form-control" style="width:400px;" onchange="doSearch();">
				<option value="">--请选择--</option>
				<#if depts?exists && depts?size gt 0>
				    <#list depts as item>
					    <option value="${item.id!}" <#if '${deptId!}' == '${item.id!}'>selected="selected"</#if>>
					    ${item.deptName!}
					    </option>
					</#list>
			    <#else>
			        <option value="">--请选择--</option>
				</#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			 <button type="button" class="btn btn-default" onClick="doSearch();">
				 <i class="fa fa-search"></i>
			 </button>
	    </div>
		<div class="filter-item ">
			<div class="text-right">
				<a class="btn btn-blue" onclick="exportList()">导出</a>
			</div>
		</div>
	</div>
	<h3 class="text-center mb20">各单位“民族团结一家亲”活动开展情况统计表-走访见面情况</h3>
			<table class="table table-striped table-bordered table-hover no-margin">
				<thead>
					<tr>
						<th rowspan="2">统计时间</th>
						<th colspan="9" class="text-center">参与走访见面干部职工人/次</th>
						<th rowspan="2">走访见面各族群众户/次</th>
					</tr>
					<tr>
						<th>地厅级干部</th>
						<th>县处级干部</th>
						<th>乡科级干部</th>
						<th>一般干部</th>
						<th>专业技术人员</th>
						<th>工勤人员</th>
						<th>企业人员</th>
						<th>其他</th>
						<th>小计</th>
					</tr>
				</thead>
				<tbody>
					<tr>
					    <td>${familyStaDto.tempDate?string('yyyy-MM-dd')}</td>
					    <td>${familyStaDto.tjCount!}</td>
					    <td>${familyStaDto.xjCount!}</td>
					    <td>${familyStaDto.kjCount!}</td>
					    <td>${familyStaDto.normalCount!}</td>
					    <td>${familyStaDto.jisCount!}</td>
					    <td>${familyStaDto.gqCount!}</td>
					    <td>${familyStaDto.qyeCount!}</td>
					    <td>${familyStaDto.otherCount!}</td>
					    <td>${familyStaDto.totalCount!}</td>
					    <td>${familyStaDto.meetingCount!}</td>
					</tr>
				</tbody>
			</table>
		
</div>
<script>

$(function(){

	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	
	$("#startDate").on("change",function(){
		doSearch();
	})
	
	$("#endDate").on("change",function(){
		doSearch();
	})
});

function compareDate(d1,d2){
	if(d1&&d2){
		return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}
}

function doSearch(){
   var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	if(compareDate(startTime, endTime)){
		layer.tips('开始日期不能大于结束日期，请重新选择！', $("#endTime"), {
				tipsMore: true,
				tips:3				
			});
		return;
	}
   var deptId = $('#deptId').val();
   var url = "${request.contextPath}/familydear/statistics/beginRegister?startTime="+startTime+"&endTime="+endTime+"&deptId="+deptId+"&index="+${index!};
   $('#bmTblDiv').load(url);
}

function exportList() {
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if(compareDate(startTime, endTime)){
        layer.tips('开始日期不能大于结束日期，请重新选择！', $("#endTime"), {
            tipsMore: true,
            tips:3
        });
        return;
    }
    var deptId = $('#deptId').val();
    document.location.href='${request.contextPath}/familydear/statistics/export1?startTime='+startTime+'&endTime='+endTime+'&deptId='+deptId;
}

</script>