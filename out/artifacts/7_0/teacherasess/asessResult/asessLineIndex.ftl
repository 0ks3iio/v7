<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div id="importDiv" class="filter-item filter-item-right">
				<div class="filter">
	                <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doExport()">导出单双上线</a>
	            </div>
			</div>
		</div>
		<div class="picker-table">
			<table class="table">
				<tbody>			
					<tr>
						<th width="150" style="vertical-align: top;">科目：</th>
						<input type="hidden" id="subjectId" value="${subjectId!}">
						<td>
							<div class="outter">
							<#if courses?exists && courses?size gt 0>
								<#list courses as item>
								<a id="A${item.id}" <#if item.id == subjectId>class="selected"</#if> href="javascript:;" onclick="showList('${item.id}')">${item.subjectName!}</a>
								</#list>
							</#if>
							</div>
						</td>
						<td width="75" style="vertical-align: top;">
							<div class="outter">
								<a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="dataTables_scroll" id="scoreList">
			
		</div>
	</div>
</div>
<script>
$(function(){
	showList('${subjectId!}');
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

function showList(subjectId){
	$("#subjectId").val(subjectId);
	$("#A"+subjectId).siblings().removeClass("selected");
	$("#A"+subjectId).addClass("selected");
	var parmUrl="?assessId=${teacherAsessId!}&subjectId="+subjectId;
	var url="${request.contextPath}/teacherasess/asessLine/list"+parmUrl;
	$("#scoreList").load(url);
}

function doExport(){
	var subjectId=$("#subjectId").val();
    var teacherAsessId='${teacherAsessId!}';
	var str="?subjectId="+subjectId+"&assessId="+teacherAsessId;
    var url='${request.contextPath}/teacherasess/asessLine/export'+str;
    location.href=url;
}
</script>