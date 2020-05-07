<div class="filter filter-f16">
	<#if code?default("")=="1">
    <div class="filter-item">
        <span class="filter-name">导入学科：</span>
        <div class="filter-content" id="subjectIdDiv">
        	<select name="subjectId" id="subjectId" class="form-control" onChange="findByCondition()">
				<#if (subjectList?exists && subjectList?size>0)>
					<#list subjectList as item>
				     	<option  value = "${item.id!}">${item.name!}</option>
			     	</#list>
			     <#else>
			     	<option  value = "" >--请选择--</option>
			     </#if>
			</select>
        </div>
    </div>
    </#if>
     <div class="filter-item filter-item-right">
	   <a href="javascript:" class="btn btn-blue"  onclick="toResultBack();">返回</a>
	</div>
</div>
<div id="importScoreDiv">
</div>
<script>
$(function(){
	findByCondition();
})
function toResultBack(){
	$("#isExportId").val("0");//进入列表页
	$(".attBtn").show();
	var code = $("#code").find("li[class = 'active']").find("a").attr("val");
	doSearch(code);
}
function findByCondition(){
	<#if code?default("")=="1">
	var subjectId = $("#subjectId").val();
    if(!subjectId){
		layer.tips('请选择学科', $("#subjectIdDiv"), {
			tipsMore: true,
			tips: 3
		});
		return;
    }
    toImport(subjectId);
    <#else>
    toImport('');
    </#if>
}
function toImport(subjectId){
    var str = '?acadyear=${acadyear!}&semester=${semester!}&classId=${classId!}&code=${code!}&isAdmin=${isAdmin!}&subjectId='+subjectId;
    var url='${request.contextPath}/studevelop/resultImport/main'+str;
	$("#importScoreDiv").load(url);
}
</script>