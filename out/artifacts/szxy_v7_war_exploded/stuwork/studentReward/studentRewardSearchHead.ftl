		<div class="box box-default">
			<div class="box-body">
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">学年：</span>
						<div class="filter-content">
							<select name="" id="acadyear" class="form-control" onChange="">
								<#if acadyearList?exists && acadyearList?size gt 0>
						            <#list acadyearList as item>
									    <option value="${item!}" <#if '${acadyear!}' == '${item!}'>selected</#if>>${item!}</option>
									</#list>
							    </#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">学期：</span>
						<div class="filter-content">
							<select name="" id="semester" class="form-control" onChange="">
								<option value="1" <#if '${semester!}' == '1'>selected</#if>>第一学期</option>
					            <option value="2" <#if '${semester!}' == '2'>selected</#if>>第二学期</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">年级：</span>
						<div class="filter-content">
							<select name="gradeId" id="gradeId" class="form-control" onChange="getClsList(this.value);">
								<option value="">全部</option>
								<#if gradeList?exists && gradeList?size gt 0>
									<#list gradeList as item>
										<option value="${item.id!}">${item.gradeName!}</option>
									</#list>
								</#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">行政班级：</span>
						<div class="filter-content">
							<select name="classId" id="classId" class="form-control" onChange="">
								<option value="">全部</option>
							<#if clazzList?exists && clazzList?size gt 0>
								<#list clazzList as item> 
									<option value="${item.id!}">${item.classNameDynamic!}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
				</div>
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">奖励类别：</span>
						<div class="filter-content">
							<select name="clsType" id="clsType" class="form-control" onChange="getProList(this.value);">
								<option value="">全部</option>
								<option value="1">学科竞赛</option>
								<option value="2">校内奖励</option>
								<option value="3">节日活动奖励</option>
								<option value="4">其他奖励</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">奖励类型：</span>
						<div class="filter-content">
							<select name="projectId" id="projectId" class="form-control" onChange="">
								<option value="">全部</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<div class="filter-content">
							<div class="input-group input-group-search">
								<select name="" id="stuSearch" class="form-control">
									<option value="1">学号</option>
									<option value="2">姓名</option>
								</select>
								<div class="pos-rel pull-left">
									<input type="text" id="searchContent" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead">
								</div>											    
							</div>
							<a class="btn btn-blue ml5" onClick="searchListAll();">查询</a>
						</div>
					</div>
					<div class="filter-item filter-item-right">
						<a class="btn btn-blue" onClick="doExport();">导出Excel</a>
					</div>
				</div>
				<div class="table-container" id="showList">
				</div>
			</div>
		</div>
<script>
$(function(){
});
searchListAll();

function searchList(){
    var classId = $('#classId').val();
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    $('#searchContent').val("");
    var url = "${request.contextPath}/stuwork/studentReward/studentRewardSearchList?acadyear="+acadyear+"&semester="+semester+"&classId="+classId;
    $("#showList").load(url);
}

function searchList1(){
	var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
	var stuSearch = $('#stuSearch').val();
    var searchContent = $('#searchContent').val();
    var studentName = '';
    var studentCode = '';
    if(stuSearch == '1'){
        studentCode = $.trim(encodeURI(searchContent));
    }else{
        studentName = $.trim(encodeURI(searchContent));
    }
    var url = "${request.contextPath}/stuwork/studentReward/studentRewardSearchList?acadyear="+acadyear+"&semester="+semester+"&studentCode="+studentCode+"&studentName="+studentName;
    $("#showList").load(url);
}

function searchListAll(){
	var classId = $('#classId').val();
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var stuSearch = $('#stuSearch').val();
	var searchContent = $('#searchContent').val();
	var clsType = $('#clsType').val();
	var projectId = $('#projectId').val();
	var gradeId = $('#gradeId').val();
	var studentName = '';
	var studentCode = '';
	if(stuSearch == '1'){
		studentCode = searchContent;
	}else{
		studentName = searchContent;
	}
	var url = "${request.contextPath}/stuwork/studentReward/studentRewardSearchList?acadyear="+acadyear+"&semester="+semester
			+"&studentCode="+studentCode+"&studentName="+studentName+"&gradeId="+gradeId+"&classId="+classId+"&projectId="+projectId+"&clsType="+clsType;
	$("#showList").load(url);
}

function getClsList(gradeId){
	$.ajax({
		url:"${request.contextPath}/stuwork/studentReward/studentRewardClass",
		data: { 'gradeId': gradeId},
		type:'post',
		success:function(data) {
			$('#classId').empty();
			var proOptions = '<option value="">全部</option>';
			for(var i=0;i<data.length;i++){
				var op = data[i];
				proOptions+=('<option value="'+op.id+'">'+op.classNameDynamic+'</option>');
			}
			$('#classId').html(proOptions);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function getProList(clsType){
	if(clsType == '' || clsType == '4'){
		$('#projectId').empty();
		$('#projectId').html('<option value="">全部</option>');
		return;
	}
	$.ajax({
		url:"${request.contextPath}/stuwork/studentReward/studentRewardProject",
		data: { 'classType': clsType},
		type:'post',
		success:function(data) {
			$('#projectId').empty();
			var proOptions = '<option value="">全部</option>';
			for(var i=0;i<data.length;i++){
				var op = data[i];
				proOptions+=('<option value="'+op.rewardClasses+'">'+op.rewardClasses+'</option>');
			}
			$('#projectId').html(proOptions);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function doExport(){
	var classId = $('#classId').val();
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var stuSearch = $('#stuSearch').val();
    var searchContent = $('#searchContent').val();
	var clsType = $('#clsType').val();
	var projectId = $('#projectId').val();
	var gradeId = $('#gradeId').val();
    var studentName = '';
    var studentCode = '';
    if(stuSearch == '1'){
        studentCode = searchContent;
    }else{
        studentName = searchContent;
    }
    var url = "${request.contextPath}/stuwork/studentReward/studentRewardSearchExport?acadyear="+acadyear+"&semester="+semester
			+"&studentCode="+studentCode+"&studentName="+studentName+"&gradeId="+gradeId+"&classId="+classId+"&projectId="+projectId+"&clsType="+clsType;
    document.location.href = url;
}
</script>