<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<a href="javascript:;" class="page-back-btn" onclick="backResultList()"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body" id="examResultDiv">
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<div class="box-header">
					<h4 class="box-title">考试名称：${examName!}</h4>
				</div>
				<#--&emsp;&emsp;<a class="color-orange" href="javascript:void(0);" onclick="arrangeResult('${examId!}')">自动编排</a>-->	
				<div class="picker-table">
					<table class="table">
						<tbody>
							<#if regionList?exists && regionList?size gt 0>
							<tr>
								<th width="150" style="vertical-align: top;">市：</th>
								<td>
									<div class="outter">
										<a <#if cityRegionCode?default('')==''>class="selected"</#if>  href="javascript:;" onclick="goSearch('','','')">全部</a>
											<#list regionList as item>
											<a <#if cityRegionCode?default('')==item.regionCode>class="selected"</#if> href="javascript:;" onclick="goSearch('','','${item.regionCode!}')">${item.regionName!}</a>
											</#list>
									</div>
								</td>
								<td></td>
							</tr>
							</#if>
							<tr>
								<th width="150" style="vertical-align: top;">考区名称：</th>
								<td>
									<div class="outter">
										<a <#if regionId?default('')==''>class="selected"</#if> href="javascript:;" onclick="goSearch('','','${cityRegionCode!}')">全部</a>
										<#if emRegionList?exists && emRegionList?size gt 0>
											<#list emRegionList as item>
											<a <#if regionId?default('')==item.id>class="selected"</#if> href="javascript:;" onclick="goSearch('${item.id!}','','${cityRegionCode!}')">${item.regionName!}</a>
											</#list>
										</#if>
									</div>
								</td>
								<td width="75" style="vertical-align: top;">
									<div class="outter">
										<a class="picker-more" href="javascript:void(0);" id="region"><span>展开</span><i class="fa fa-angle-down"></i></a>
										<input type="hidden" id="regionPick" value="${regionPick!}">
									</div>
								</td>
							</tr>
							<tr>
								<th width="150" style="vertical-align: top;">考点名称：</th>
								<td>
									<div class="outter">
										<a <#if optionId?default('')==''>class="selected"</#if> href="javascript:;" onclick="goSearch('${regionId!}','','${cityRegionCode!}')">全部</a>
										<#if emOptionList?exists && emOptionList?size gt 0>
											<#list emOptionList as item>
											<a <#if optionId?default('')==item.id>class="selected"</#if> href="javascript:;" onclick="goSearch('${regionId!}','${item.id!}','${cityRegionCode!}')">${item.optionName!}</a>
											</#list>
										</#if>
									</div>
								</td>
								<td width="75" style="vertical-align: top;">
									<div class="outter">
										<a class="picker-more" href="javascript:void(0);" id="option"><span>展开</span><i class="fa fa-angle-down"></i></a>
										<input type="hidden" id="optionPick" value="${optionPick!}">
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>		
				<input type="hidden" id="cityRegionCode" value="${cityRegionCode!}">
				<input type="hidden" id="regionId" value="${regionId!}">
				<input type="hidden" id="optionId" value="${optionId!}">
				<input type="hidden" id="noArrangeNum" value="${noArrangeNum!}">
				<input type="hidden" id="examId" value="${examId!}">
				<#-- <div class="filter">
					<div class="filter-item">
						<button class="btn btn-blue">导出</button>
					</div>
				</div>	-->			
				<div class="table-container">
					<div class="table-container-body">
						<table class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>序号</th>
									<th>考区名称</th>
									<th>考点名称</th>
									<th>考场编号</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<#if emPlaceList?exists&&emPlaceList?size gt 0>
								<#list emPlaceList as item>
									<tr>
										<td>${item_index+1}</td>
										<td>${item.regionName!}</td>
										<td>${item.optionName!}</td>
										<td>${item.examPlaceCode!}</td>
										<td>
											<a href="javascript:" onclick="toResultStudent('${item.id!}')" class="table-btn show-details-btn">查看结果</a>
										</td>
									</tr>
								</#list>
								<#else>
									<tr>
										<td colspan="5" align="center">
											暂无数据
										</td>
									<tr>
								</#if>
							</tbody>
						</table>
						<#if emPlaceList?exists&&emPlaceList?size gt 0>
							<@htmlcom.pageToolBar container="#examResultDiv" class="noprint"/>
						</#if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		if('${regionPick!}'=='true'){
			$("#region").children('span').text('折叠');
			$("#region").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#region").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
		}
		if('${optionPick!}'=='true'){
		    $("#option").children('span').text('折叠');
			$("#option").children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			$("#option").parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
		}
		$('.picker-more').click(function(){
			if($(this).children('span').text()=='展开'){
				$(this).children('span').text('折叠');
				$("#"+$(this).attr("id")+"Pick").val('true');
				$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			}else{
				$(this).children('span').text('展开');
				$("#"+$(this).attr("id")+"Pick").val('false');
				$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
			};
			$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
	    });
	})
	function goSearch(regionId,optionId,cityRegionCode){
		var regionPick=$("#regionPick").val();
		var optionPick=$("#optionPick").val();
		var examId=$("#examId").val();
		var noArrangeNum=$("#noArrangeNum").val();
		var url =  '${request.contextPath}/exammanage/edu/examResult/detailList/page?examId='+examId+"&noArrangeNum="+noArrangeNum+"&regionId="+regionId+"&optionId="+optionId+"&cityRegionCode="+cityRegionCode+"&regionPick="+regionPick+"&optionPick="+optionPick;
		$("#examResultDiv").load(url);
	}
	function toResultStudent(examPlaceId){
		var cityRegionCode=$("#cityRegionCode").val();
		var regionId=$("#regionId").val();
		var optionId=$("#optionId").val();
		var noArrangeNum=$("#noArrangeNum").val();
		var examId=$("#examId").val();
		var url =  '${request.contextPath}/exammanage/edu/examResult/studentList/page?examId='+examId+"&examPlaceId="+examPlaceId+"&noArrangeNum="+noArrangeNum+"&regionId="+regionId+"&optionId="+optionId+"&cityRegionCode="+cityRegionCode;
		$("#examResultDiv").load(url);
	}
	function backResultList(){
		var url ='${request.contextPath}/exammanage/edu/examResult/head/page';
		$("#examResultDiv").load(url);
	}
</script>