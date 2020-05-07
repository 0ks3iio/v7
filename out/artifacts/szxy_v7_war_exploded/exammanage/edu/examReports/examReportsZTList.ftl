<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="cc" class="tab-pane active" role="tabpanel">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">考试名称：${examName!}</span>
		</div>
		<div class="filter-item filter-item-right">
			<button class="btn btn-blue" onclick="exportPDF('${examId!}','${type!}')">导出PDF</button>
		</div>
	</div>
	<div class="picker-table">
		<table class="table">
			<tbody>
				<tr>
					<th>考区名称：</th>
					<td>
						<div class="outter">
							<#if emRegionList?exists && emRegionList?size gt 0>
								<#list emRegionList as item>
									<a <#if regionId?default('')==item.id>class="selected"</#if> href="javascript:;" onclick="goSearch('${item.id!}','','')">${item.regionName!}</a>
								</#list>
							</#if>
						</div>
					</td>
					<td style="vertical-align: top;" width="75">
						<div class="outter">
							<a class="picker-more" href="javascript:void(0);" id="regionPickId"><span>展开</span><i class="fa fa-angle-down"></i></a>
							<input type="hidden" id="regionPick" value="${regionPick!}"><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
				<tr>
					<th style="vertical-align: top;" width="150">考点名称：</th>
					<td>
						<div class="outter">
							<#if emOptionList?exists && emOptionList?size gt 0>
								<#list emOptionList as item>
									<a <#if optionId?default('')==item.id>class="selected"</#if> href="javascript:;" onclick="goSearch('${regionId!}','${item.id!}','')">${item.optionName!}</a>
								</#list>
							</#if>
						</div>
					</td>
					<td style="vertical-align: top;" width="75">
						<div class="outter">
							<a class="picker-more" href="javascript:void(0);" id="optionPickId"><span>展开</span><i class="fa fa-angle-down"></i></a>
							<input type="hidden" id="optionPick" value="${optionPick!}"><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
				<tr>
					<th>考场编号：</th>
					<td>
						<div class="outter">
							<#if emPlaceList?exists && emPlaceList?size gt 0>
								<#list emPlaceList as item>
									<a <#if placeId?default('')==item.id!>class="selected"</#if> href="javascript:;" onclick="goSearch('${regionId!}','${optionId!}','${item.id}')">${item.examPlaceCode!}</a>
								</#list>
							</#if>
						</div>
					</td>
					<td style="vertical-align: top;" width="75">
						<div class="outter">
							<a class="picker-more" href="javascript:void(0);" id="numPickId"><span>展开</span><i class="fa fa-angle-down"></i></a>
							<input type="hidden" id="numPick" value="${numPick!}"><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<input type="hidden" id="examId" value="${examId!}">
	
	<div class="box box-primary">
	<div class="box-header">
		<h3 class="box-title"><span class="mr70">考区：${regionName!}</span><span class="mr70">考点：${emOptionName!}</span><span>考场：${placeName!}</span></h3>
	</div>
	<div class="box-body">
		<div class="row">
		<#if emPlaceStudents?exists && emPlaceStudents?size gt 0>
		<#list emPlaceStudents as item>
			<div class="col-xs-6">
				<table class="table table-bordered">
					<tbody>
						<tr>
							<td colspan="2">考号：${item.examNumber!}</td>
							<td width="123" rowspan="4"><img width="123" height="180" src="${request.contextPath}${item.studentFilePath!}"></td>
						</tr>
						<tr>
							<td colspan="2">姓名：${item.studentName!}</td>
						</tr>
						<tr>
							<td colspan="2">考点：${emOptionName!}</td>
						</tr>
						<tr>
							<td>考场：${placeName!}</td>
							<td>座位号：${item.seatNum!}</td>
						</tr>
					</tbody>
				 </table>
			</div>	
			</#list>
		</#if>
		</div>
	</div>
</div>
	
</div>
<script>
	$(function(){
		if('${regionPick!}'=='true'){
			$("#regionPickId").children('span').text('折叠');
			$("#regionPickId").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#regionPickId").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
		}
		if('${optionPick!}'=='true'){
		    $("#optionPickId").children('span').text('折叠');
			$("#optionPickId").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#optionPickId").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
		}
		if('${numPick!}'=='true'){
		    $("#numPickId").children('span').text('折叠');
			$("#numPickId").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#numPickId").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
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
	function goSearch(regionId,optionId,placeId){
		var regionPick=$("#regionPick").val();
		var optionPick=$("#optionPick").val();
		var numPick=$("#numPick").val();
		var examId=$("#examId").val();
		var url ='${request.contextPath}/exammanage/edu/examReports/detailList/page?examId='+examId+"&regionId="+regionId+"&optionId="+optionId+"&placeId="+placeId+"&regionPick="+regionPick+"&optionPick="+optionPick+"&type=3"+"&numPick="+numPick;
		$("#showTabDiv").load(url);
	}
	function exportPDF(examId,type){
	   var url = "${request.contextPath}/exammanage/edu/examReports/exportList/page?examId="+examId+"&type="+type;
	   indexDiv = layerDivUrl(url,{title: "导出对象",width:650,height:500});
	}
</script>
