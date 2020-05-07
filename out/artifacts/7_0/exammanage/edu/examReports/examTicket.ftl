<div id="dd" class="tab-pane active" role="tabpanel">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">考试名称：${examName!}</span>
		</div>
		<div class="filter-item filter-item-right">
			<button class="btn btn-blue js-export2" onclick="exportTicketPDF('${examId!}','${type!}')">导出PDF</button>
		</div>
	</div>	
	<div class="picker-table">
		<table class="table">
			<tbody>
				<tr>
					<th>学校名称：</th>
					<td>
						<div class="outter">
							<#if schlist?exists && schlist?size gt 0>
								<#list schlist as sch>
									<a <#if schoolId?default('')==sch.id>class="selected"</#if> href="javascript:;" onclick="searchTicket('${sch.id!}','');">${sch.schoolName!}</a>
								</#list>
							</#if>
						</div>
					</td>
					<td width="75" style="vertical-align: top;">
						<div class="outter">
							<a class="picker-more" href="#" id="optionPickId1"><span>展开</span><i class="fa fa-angle-down"></i></a>
							<input type="hidden" id="optionPick1" value="${optionPick1!}"><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
				<tr>
					<th width="150" style="vertical-align: top;">班级名称：</th>
					<td>
						<div class="outter">
							<#if clslist?exists && clslist?size gt 0>
								<#list clslist as cls>
									<a <#if classId?default('')==cls.id>class="selected"</#if> href="javascript:;" onclick="searchTicket('${schoolId!}','${cls.id!}');">${cls.classNameDynamic!}</a>
								</#list>
							</#if>
						</div>
					</td>
					<td width="75" style="vertical-align: top;">
						<div class="outter">
							<a class="picker-more" href="#" id="optionPickId"><span>展开</span><i class="fa fa-angle-down"></i></a>
							<input type="hidden" id="optionPick" value="${optionPick!}"><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="box box-primary">
		<div class="box-header">
			<h3 class="box-title"><span class="mr70">学校：${schoolName!}</span><span class="mr70">班级：${className!}</span></h3>
		</div>
		<div class="box-body">
			<#if dtos?exists && dtos?size gt 0>
				<#list dtos as dto>
			<div class="row">
					<div class="col-xs-6">
						<table class="table table-bordered exam-table">
							<tbody>
								<tr>
									<td align="center" style="font-size: 26px;">准考证</td>
									<td width="120" rowspan="5"><img width="120" height="174" src="${request.contextPath}${dto.studentFilePath!}"></td>
								</tr>
								<tr>
									<td>考号:${dto.examNumber!}</td>
								</tr>
								<tr>
									<td>姓名:${dto.name!}</td>
								</tr>
								<tr>
									<td>学号:${dto.unitiveCode!}</td>
								</tr>
								<tr>
									<td>学校:${dto.schoolName!}</td>
								</tr>
								<tr>
									<td colspan="2"><span class="mr30">考区:${dto.examRegionName!}</span>&nbsp;&nbsp;<span>考点:${dto.examOptionName!}</span></td>
								</tr>
								<tr>
									<td colspan="2"><span class="mr30">考场:${dto.examPlace!}</span>&nbsp;&nbsp;<span>座位号:${dto.seatNum!}</span>&nbsp;&nbsp;<span>考点地址:${dto.examPlaceAdd!}</span></td>
								</tr>
							</tbody>
						 </table>
					</div>
					<div class="col-xs-6">
					<#if subDtos?exists && subDtos?size gt 0>
						<table class="table table-bordered text-center exam-table">
						<tbody>
							<tr>
								<td colspan="${subDtos?size}" style="height:50px;font-size: 16px;">${examName!}【日程表】</td>
							</tr>
							<tr>
								<#list subDtos as subDateDto>
								<td>${subDateDto.date!}</td>
								</#list>
							</tr>
							<#list 1..maxTr as i>
								<tr>
									<#list 1..subDtos?size as j>
									<#if subDtos[j-1].subDtos?size gt (i-1)>
										<td><b>${subDtos[j-1].subDtos[i-1].subName!}</b><br>${subDtos[j-1].subDtos[i-1].startDate!}-${subDtos[j-1].subDtos[i-1].endDate!}</td>
									<#else>
										<td><b></b><br></td>
									</#if>
									</#list>
								</tr>
							</#list>
						</tbody>
					</table>
					</#if>
				</div>
			</div>
				</#list>
			</#if>	
		</div>
	</div>
</div>
<script>
function searchTicket(schoolId,classId){
	var optionPick=$("#optionPick").val();
	var optionPick1=$("#optionPick1").val();
	var url =  '${request.contextPath}/exammanage/edu/examReports/examTicket/page?examId=${examId!}&schoolId='+schoolId+'&classId='+classId+"&optionPick="+optionPick+"&optionPick1="+optionPick1;
	$("#showTabDiv").load(url);
}
function exportTicketPDF(examId,type){
   var url = "${request.contextPath}/exammanage/edu/examReports/exportTicketList/page?examId="+examId+"&type="+type;
   indexDiv = layerDivUrl(url,{title: "导出对象",width:650,height:500});
}

$(function(){
	if('${optionPick!}'=='true'){
		    $("#optionPickId").children('span').text('折叠');
			$("#optionPickId").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#optionPickId").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
	}
	if('${optionPick1!}'=='true'){
		    $("#optionPickId1").children('span').text('折叠');
			$("#optionPickId1").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#optionPickId1").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
	}
	$('.picker-more').click(function(){
		if($(this).children('span').text()=='展开'){
			$(this).children('span').text('折叠');
			$(this).siblings('input').val('true');
			$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
		}else{
			$(this).children('span').text('展开');
			$(this).siblings('input').val('false');
			$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
		};
		$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
    });
})
</script>

