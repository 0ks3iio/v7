<#--<a href="javascript:showList();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default" style="box-shadow: 0px 1px 3px rgba(255,255,255,0.6)">
	<div class="box-header">
		<h3 class="box-title">${refName!}</h3>
        <button class="btn btn-sm btn-blue pull-right" onClick="scoreTable()">返回成绩列表</button>
	</div>
	<div class="box-body">
		<div class="picker-table">
			<table class="table">
				<tbody>			
					<tr id="courseSearch">
						<th width="150" style="vertical-align: top;">行政班：</th>
						<td>
							<div class="outter">
								<a class="selected" id="cheackAll" href="javascript:searchList('');">全部</a>
								<#if clsList?exists && clsList?size gt 0>
								    <#list clsList as item>
								         <a id="${item.id!}" href="javascript:searchList('${item.id!}');">${item.classNameDynamic!}</a>
								    </#list>
								</#if>
							</div>
						</td>
						<td width="75" style="vertical-align: top;">
							<div class="outter">
								<a class="picker-more" href="javascript:"><span>展开</span><i class="fa fa-angle-down"></i></a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>		
		
		<div id="tableList">
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
   //showBreadBack(showList,false,"成绩详细");
   
   $('#cheackAll').next().addClass('selected');
   var classId = $('#cheackAll').next().attr('id');
   searchList(classId);
   
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

function searchList(classId){
   var unitId = '${unitId!}';
   var refId = '${refId!}';
   if(classId == ''){
       $('#cheackAll').attr("class","selected");
       $('#cheackAll').siblings().attr("class","");
   }else{
       $('#'+classId).attr("class","selected");
       $('#'+classId).siblings().attr("class","");
   }
   var url = "${request.contextPath}/newgkelective/newGkScoreResult/showScoreList?refId="+refId+"&classId="+classId+"&unitId="+unitId+"&gradeId=${gradeId}";
   $("#tableList").load(url);
}

function scoreTable(){
    var url = '${request.contextPath}/newgkelective/newGkScoreResult/index?gradeId=${gradeId}';
    $("#gradeTableList").load(url);
}
</script>
