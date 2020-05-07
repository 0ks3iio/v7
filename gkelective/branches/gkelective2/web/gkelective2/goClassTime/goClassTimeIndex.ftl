<a href="javascript:" class="page-back-btn gotoLcIndex"><i class="fa fa-arrow-left"></i> 返回</a>
<div id="itemShowDivId">
	<div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">${gaent.arrangeName!}</h4>
		</div>
		<div class="box-body">
		<#if plan.step == 0 >
		<div class="schedule-labels js-schedule-labels">
		<#if (bathNumA>0)>
			<#list 1..bathNumA as index>
				<label class="schedule-label schedule-label-${index}" data-show="${index}" data-filter="${index}"><i class="fa fa-plus"></i>选考${index}</label>
			</#list>
		</#if>
		<#if (bathNumB>0)>
			<#list 1..bathNumB as index>
				<label class="schedule-label schedule-label-${index+bathNumA}" data-show="${index+bathNumA}" data-filter="${index+bathNumA}"><i class="fa fa-plus"></i>学考${index}</label>
			</#list>
		</#if>
			<label class="schedule-label schedule-label-0" data-filter="0"><i class="fa fa-trash"></i>删除</label>
			<#-- div class="filter-item-right">
				<button class="btn btn-blue export-btn">导出</button>
			</div-->
		</div>
		</#if>
		<#assign edudays=7>
			<table class="table table-bordered table-schedule">
			    <thead>
				    <tr>
						<th class="text-center"></th>
						<th class="text-center" width="40"></th>
						<#if (edudays>0)>
						<th class="text-center">周一</th>
						</#if>
						<#if (edudays>1)>
						<th class="text-center">周二</th>
						</#if>
						<#if (edudays>2)>
						<th class="text-center">周三</th>
						</#if>
						<#if (edudays>3)>
						<th class="text-center">周四</th>
						</#if>
						<#if (edudays>4)>
						<th class="text-center">周五</th>
						</#if>
						<#if (edudays>5)>
						<th class="text-center">周六</th>
						</#if>
						<#if (edudays>6)>
						<th class="text-center">周日</th>
						</#if>
					</tr>
			   </thead>
			   <tbody>		   
			        <#list timeIntervalMap?keys as key>
			        <#if (timeIntervalMap[key]>0)>
			        <#list 1..timeIntervalMap[key] as t>
					<tr>
					<#if t==1>
					<td class="text-center" rowspan="${timeIntervalMap[key]}">
					    <#if key=='1'>早自习
					    <#elseif key=='2'>上午
					    <#elseif key=='3'>下午
					    <#else>晚自习
					    </#if>
					</td>
					</#if>
					<td class="text-center order"></td>
					    <#list 0..(edudays-1) as tt>
						<td class="text-center edited" data-value="${tt}_${t}_${key!}">
						    <#if gkTimetableLimitArrangList?exists && (gkTimetableLimitArrangList?size>0)>
						         <#list gkTimetableLimitArrangList as item>
						         <#if '${item.weekday}_${item.period}_${item.periodInterval}'=='${tt}_${t}_${key!}'>
							         <#if 'item.arrangId'!=''>						         
								         <label class="schedule-label schedule-label-${item.arrangId!} thin selected" data-label="${item.arrangId!}">${item.remark!}</label>
								     </#if>
						         </#if>
						         </#list>
						    </#if>
						</td>
						</#list>
					</tr>
					</#list>
					</#if>
					</#list>			       
				</tbody>
			</table>
			<em>温馨提示：请先选择选考或者学考或删除按钮进行操作，开班进行中不能修改（历史轮次也不能修改哦！）。</em>
		</div>
	</div>
</div>
<iframe style="display:none;" id="hiddenFrame" name="hiddenFrame" />
<script type="text/javascript">

$(function(){

    var i = 1;
    $(".order").each(function(){
        $(this).text(i);
        i++;
    });
<#if plan.step == 0>
	
	var $table=$('.table-schedule');
	$('.js-schedule-labels label').on('click',function(){
		if($(this).hasClass('selected')){
			$(this).removeClass('selected');
			$table.attr('data-filter',100);
		}else{
			$(this).addClass('selected').siblings().removeClass('selected');
			$table.attr('data-filter',$(this).attr('data-filter'));
		}
	});
	$('.table-schedule .edited').click(function(){
	    var currentLabel=$(this).find('label').attr("data-label")				    
		var dataFilter = $table.attr('data-filter');
		
		if (dataFilter == 100 || dataFilter == undefined) {
			//alert('请先选择需要进行的操作！');
		}else if (dataFilter == 0 || currentLabel==dataFilter || dataFilter==undefined){
		    doDelete($(this).attr("data-value"));
			$(this).empty();
		} else {
		    save($(this).attr("data-value"),dataFilter,$(this));
		};
	});
</#if>
	$('.gotoLcIndex').on('click',function(){
		var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/index/page';
		$("#showList").load(url);
	});
})
<#if plan.step == 0>	
function save(selectxyz,dataFilter,thisId){
	var id = '${arrangeId!}';
	var planId ='${planId!}';
	var showval="";
	if(dataFilter <= ${bathNumA}){
		showval = "选考"+dataFilter;
	}else {
		showval = "学考"+(dataFilter-${bathNumA});
	}
	var label = '<label class="schedule-label schedule-label-'+dataFilter+' thin selected" data-label="'+dataFilter+'">'+showval+'</label>';
	$.ajax({
		url:'${request.contextPath}/gkelective/'+id+'/goClassTime/save',
		data: {'selectxyz':selectxyz,'dataFilter':dataFilter+"#"+showval,'planId':planId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
				//layerTipMsg(jsonO.success,"成功",jsonO.msg);
			  	layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				thisId.empty().append(label);
	 		}else{
	 			//layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 			layer.msg(jsonO.msg, {
						offset: 't',
						time: 6000
					});
					
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});	   
}
	
function doDelete(selectxyz){
	var id = '${arrangeId}';
	var planId ='${planId!}';
	$.ajax({
		url:'${request.contextPath}/gkelective/'+id+'/goClassTime/delete',
			data: {'selectxyz':selectxyz,'planId':planId},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){

		 		}
		 		else{
		 				
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});	
}
  
//$(".export-btn").on("click",function(){
//	var url = "${request.contextPath}/gkelective/${roundsId!}/goClassTime/export";
//	hiddenFrame.location.href=url;
//}); 
</#if> 

</script>
