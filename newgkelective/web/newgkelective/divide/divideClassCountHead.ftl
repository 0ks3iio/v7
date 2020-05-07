<div class="filter">
	<div class="filter-item">
		<button class="btn btn-blue js-exportCount">导出</button>
	</div>
</div>
<div class="picker-table">
	<table class="table">
		<tbody>			
			<tr>
				<th width="150">科目：</th>
				<td>
					<div class="outter">
						<#if courseNameMap?exists>
	                    <#list courseNameMap?keys as key >
						<a href="javascript:;" onClick="doSearchList(this,'${key!}');" <#if key?default('') ==courseId?default('')>class="selected"</#if>>${courseNameMap[key]!}</a>
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
<div class="row" style="margin-left:5px;margin-right:5px;" id="jxbCountId">
</div>
<script>
$(function(){
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
    $('.picker-more').click();//默认展开
    $('.outter').children(":first").click();
    
    $('.js-exportCount').on("click",function(){
    	var url =  '${request.contextPath}/newgkelective/${divideId!}/exportCount?fromSolve=${fromSolve?default("0")}&arrayId=${arrayId?default("")}';
	  	document.location.href=url;
    })
})

function doSearchList(obj,subjectId){
	$(obj).addClass('selected').siblings().removeClass('selected');
	var url = '${request.contextPath}/newgkelective/${divideId!}/showDivideCountResult/list/page?fromSolve=${fromSolve?default("0")}&arrayId=${arrayId?default("")}&subjectId='+subjectId;
    $("#jxbCountId").load(url);
}
</script>