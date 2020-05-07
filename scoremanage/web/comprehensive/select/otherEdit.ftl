<p class="color-999"><i class="fa fa-exclamation-circle color-yellow"></i> 请选择需依据的考试</p>
<#if examList?exists && examList?size gt 0>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th width="100">选择</th>
			<th>考试名称</th>
		</tr>
	</thead>
	<tbody>
		<#list examList as exam>
		<tr>
			<td>
				<label><input type="radio" name="examId" class="wp" <#if exam.isStat?default('0')=='1'>checked</#if> value="${exam.id!}"><span class="lbl"></span></label>
			</td>
			<td>${exam.examName!}</td>
		</tr>
		</#list>
	</tbody>
</table>
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
<script type="text/javascript">
function doSave(){
	<#if !(examList?exists && examList?size gt 0)>
		layer.closeAll();
		return;
	</#if>
	var arr = []
	var examId = "";
	if($("input[type=radio][name=examId]:checked")){
		examId=$("input[type=radio][name=examId]:checked").val();
	}
	var url = "${request.contextPath}/comprehensive/select/otherEdit/save";
	layer.load();
	$.post(url,{"infoId":"${infoId!}","type":"${type!}","examId":examId},function(msg){
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