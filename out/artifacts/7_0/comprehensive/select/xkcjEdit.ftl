<div style="max-height: 300px;overflow: auto;">
	<#if examNameMap?exists && examSubjectMap?exists && examSubjectMap?size gt 0>
	<#list examSubjectMap?keys as key>
	<p><b>${examNameMap[key]!}</b></p>
	<div>
		<#if examSubjectMap[key]?size gt 0>
		<#list examSubjectMap[key] as subject>
		<label class="mb10 mr10 w100 ellipsis">
			<input type="checkbox" class="wp ${subject.subjectId!}" name="subject" <#if subject.isUsed?default('0')='1'>checked</#if> data-subject="${subject.subjectId!}" value="${key!}_${subject.subjectId!}">
			<span class="lbl"> ${subject.courseName!}</span>
		</label>
		</#list>
		</#if>
	</div>
	</#list>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无相关数据</p>
			</div>
		</div>
	</div>
	</#if>
</div>
<script type="text/javascript">

$(function(){
	$("input[type=checkbox][name=subject].wp").on("click",function(){
		if($(this).is(":checked")){
			var subjectId = $(this).data("subject");
			$(this).parent().parent().siblings().find(".wp."+subjectId).prop("checked",false);
		}
	})
})

function doSave(){
	<#if !(examSubjectMap?exists && examSubjectMap?size gt 0)>
		layer.closeAll();
		return;
	</#if>
	var arr = []
	$("input[type=checkbox][name=subject]:checked").each(function(){
		arr.push($(this).val());
	})
	var examSubjectStr = "";
	if(arr.length>0){
		examSubjectStr = arr.join(",");
	}
	var url = "${request.contextPath}/comprehensive/select/xkcjEdit/save";
	layer.load();
	$.post(url,{"infoId":"${infoId!}","examSubjectStr":examSubjectStr},function(msg){
		 if(msg.success){
			layer.closeAll();
			layer.msg(msg.msg, {offset: 't',time: 2000});
			showDetailList("${infoId!}");
		 }else{
			layer.closeAll();
			layer.msg(msg.msg, {offset: 't',time: 2000});
		 }
	 }, "JSON");
}
</script>