<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">方案名称：</span>
				<div class="filter-content">${dto.convertName!}</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">${dto.acadyear!}</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">${dto.gradeName!}</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">所选考试：</span>
				<div class="filter-content">
					<#if dto.examDtos?exists && dto.examDtos?size gt 0>
					<#list dto.examDtos as item>
						<#if item_index gt 0>
						、
						</#if>${item.examName}(${item.scale}%)
					</#list>
					</#if>
				</div>
			</div>
			<div id="importDiv" class="filter-item filter-item-right" <#if subjectId?default("")!="00000000000000000000000000000000">style="display:none"</#if>>
				<div class="filter">
	                <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doExport()">导出折算总分</a>
	                <a href="javascript:" class="btn btn-white pull-right" style="margin-bottom:5px;margin-right: 10px" onclick="doImport()">导入折算总分</a>
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
							<#if courseList?exists && courseList?size gt 0>
								<#list courseList as item>
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
		<div class="table-container" id="scoreList">
			
		</div>
	</div>
</div>
<script>
function gobackIndex(){
    var url =  '${request.contextPath}/teacherasess/convert/index/page?acadyear=${dto.acadyear}';
    $(".model-div").load(url);
}
$(function(){
	showList('${subjectId!}');
	showBreadBack(gobackIndex,true,"查看结果");
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
	if(subjectId=="00000000000000000000000000000000"){
		$("#importDiv").show();
	}else{
		$("#importDiv").hide();
	}
	$("#A"+subjectId).siblings().removeClass("selected");
	$("#A"+subjectId).addClass("selected");
	var parmUrl="?convertId=${dto.convertId}&subjectId="+subjectId;
	var url="${request.contextPath}/teacherasess/convert/showResult/list/page"+parmUrl;
	$("#scoreList").load(url);
}
 function doImport(){
 	var subjectId=$("#subjectId").val();
    var convertId='${dto.convertId!}';
    var gradeId='${dto.gradeId!}';
    var str="?subjectId="+subjectId+"&convertId="+convertId+"&gradeId="+gradeId;
    var url='${request.contextPath}/teacherasess/convert/resultImport/main'+str;
    $('.model-div').load(url);
}
function doExport(){
	var subjectId=$("#subjectId").val();
    var convertId='${dto.convertId!}';
	var str="?subjectId="+subjectId+"&convertId="+convertId;
    var url='${request.contextPath}/teacherasess/convert/showResult/export'+str;
    location.href=url;
}
</script>