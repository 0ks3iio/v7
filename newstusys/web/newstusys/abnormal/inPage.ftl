<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-caption"><#if deploy?default('')=='hangwai'>学籍增加<#else>转入学生</#if></h3>
	</div>
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<a href="javascript:;" class="btn btn-blue" onclick="backToSch();">返回</a>
			</div>
		</div>
		<div class="box-body padding-15 explain-blue text-center">
			<h3><#if deploy?default('')=='hangwai'>学籍增加<#else>转入学生</#if></h3>
			<div class="mt20 inline-block">
				<table width="100%">
					<tr>
						<td>学生姓名</td>
						<td></td>
						<td>身份证号</td>
						<td></td>
					</tr>
					<tr>
						<td><p class="mt10"><input type="text" class="form-control" id="stuName" placeholder="请输入"></p></td>
						<td><p class="mt10"><span class="ml10 mr10">-</span></p></td>
						<td><p class="mt10"><input type="text" class="form-control" id="stuIdentityCard" style="width: 200px;" placeholder="请输入"></p></td>
						<td><p class="mt10"><a  href="javascript:;" class="btn btn-blue ml10" onclick="searchApplyStu();">查询</a></p></td>
					</tr>
					<tr>
						<td colspan="4"><div class="text-left color-999"><i class="fa fa-exclamation-circle color-yellow"></i> <span class="font-12">请输入并认真核查姓名和身份证号</span></div></td>
					</tr>
				</table>
			</div>
		</div>
		<div id="applyInDiv"></div>
	</div>
</div>
<script>
function backToSch(){
	var url = '${request.contextPath}/newstusys/abnormal/sch/index/page?schoolId=${schoolId!}&gradeId=${gradeId!}&clsId=${clsId!}';
	<#if loginInfo.unitClass==1>
	$('#stuDataDiv').load(url);
	<#else>
	$('.model-div').load(url);
	</#if>
}

function searchApplyStu(){
	var sn = $('#stuName').val();
	var idc = $('#stuIdentityCard').val();
	if(sn=='' || idc==''){
        showMsgError('学生姓名和身份证号都必填，不能为空！');
        return ;
    }
    var url = '${request.contextPath}/newstusys/abnormal/in/edit?schoolId=${schoolId!}&stuName='+encodeURIComponent(sn)+'&stuIdentityCard='+encodeURIComponent(idc);
    $('#applyInDiv').load(url);
}	
</script>