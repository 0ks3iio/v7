<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="picker-table">
	<table class="table">
	<tbody>
		<tr>
			<th width="150" style="vertical-align: top;height:42px;">新行政班：</th>
			<td>
				<div class="outter">
					<a id="qb" href="javascript:toStudentClassResultList('')">全部</a>
					<#if classList?exists && classList?size gt 0>
					<#list classList as item>
						<a id="${item.id!}" href="javascript:toStudentClassResultList('${item.id!}')">${item.className!}</a>
					</#list>
					</#if>
				</div>
			</td>
			<td width="75" style="vertical-align: top;">
				<div class="outter">
					<a class="picker-more" href="javascript:;"><span>展开</span><i class="fa fa-angle-down"></i></a>
				</div>
			</td>
		</tr>	
	<#--
	ul class="picker-list">
		<li class="picker-item">
			<span class="picker-name">原行政班：</span>
			<div class="picker-content">
				<ul class="picker-value" style="height:auto">
					<li class="selected" id="qb"><a href="javascript:toStudentClassResultList('')">全部</a></li>
					<#if classList?exists && classList?size gt 0>
					    <#list classList as item>
					        <li id="${item.id!}"><a href="javascript:toStudentClassResultList('${item.id!}')">${item.classNameDynamic!}</a></li>
					    </#list>
					</#if>
				</ul>
			</div>
		</li>
	</ul-->
	</tbody>
	</table>
</div>
<div class="filter">
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<div class="input-group input-group-search">
				<div class="pos-rel pull-left">
					<input type="text" id="studentName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="输入学生姓名查询">
				 </div>
				<div class="input-group-btn">
					<a type="button" class="btn btn-default" onClick="toStudentClassResultList('','')">
						<i class="fa fa-search"></i>
					</a>
				 </div>
			</div>
		</div>
	</div>
	<div class="filter-item filter-item-left">
		<@htmlcomponent.printToolBar container=".print" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		<a href="javascript:doExportAll();" class="btn btn-white">导出全部课表</a>
	</div>
</div>
<div id="tableList2" class="print">
</div>
<script>
$(document).ready(function(){
    <#if classList?exists && classList?size gt 0>
		<#list classList as item>
			<#if item_index = 0>
				toStudentClassResultList('${item.id}');
			</#if>
		</#list>
	</#if>
	$("#studentName").bind('keypress',function(event){
	    if(event.keyCode == "13")    
	    {
	      toStudentClassResultList('','');
	    }
	});
	
	$('.picker-more').click(function(){
		if($(this).children('span').text()=='展开'){
			$(this).children('span').text('折叠');
			$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
		}else{
			$(this).children('span').text('展开');
			$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
		};
		$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
    });
});

function toStudentClassResultList(classId){
    var arrayId = '${arrayId!}';
    var studentName = $('#studentName').val();
    if(classId == ''){
        $('#qb').attr("class","selected");
        $('#qb').siblings().removeClass();
    }else{
        $('#'+classId).attr("class","selected");
        $('#'+classId).siblings().removeClass();
    }
    var url = encodeURI('${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/studentClassResultList?classId='+classId+'&studentName='+studentName);
	$("#tableList2").load(url);
}
function doExportAll(){
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/classStu/exportTimetableAll';
  	document.location.href=url;
}
</script>