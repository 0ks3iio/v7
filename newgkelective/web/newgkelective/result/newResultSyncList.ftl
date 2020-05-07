<#--<a href="javascript:void(0);" class="page-back-btn" onclick="goback('${choiceId!}')"><i class="fa fa-arrow-left"></i> 返回</a>-->
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
<div class="box-body">
<form id="myform">
<table class="table table-striped table-bordered table-hover no-margin mainTable">
    <thead>
        <tr>
        	<th width="10%">序号</th>
        	<th width="10%">班级</th>
        	<th width="20%">姓名</th>
            <th width="10%">学号</th>
            <th width="8%">性别</th>
        </tr>
    </thead>
    <tbody>
	<#if stus?? && (stus?size>0)>
		<#list stus as item>
		<tr>
			<td>${item_index+1}</td>
			<td>${item.className!}</td>
			<td>${item.studentName!}</td>
			<td>${item.studentCode!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
		</tr>
	</#list>
	</#if> 
	</tbody>
</table>
</form>
</div>
</div>
<script type="text/javascript">
function goMybackChoice(){
	goback('${choiceId!}');
}
$(function(){
	showBreadBack(goMybackChoice,false,"同化详细");
})
function goback(choiceId){
	var url = '${request.contextPath}/newgkelective/'+choiceId+'/choiceAnalysis/list/page';
	$("#showList").load(url);
}
</script>