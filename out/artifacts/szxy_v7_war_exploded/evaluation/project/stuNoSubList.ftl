<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id = "stuNoList">
<a href="javascript:;" onclick="black();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation"><a href="javascript:;" role="tab" data-toggle="tab" onclick="showSubStu()">已提交（${subNum}）</a></li>
			<li class="active" role="presentation"><a href="javascript:;" role="tab" data-toggle="tab" onclick="showNoSubStu()">未提交（${noSubNum}）</a></li>
		</ul>
		<div class="tab-content">
			<div id="bb" class="tab-pane active" role="tabpanel">
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">年级：</span>
						<div class="filter-content">
							<select name="gradeId" id="gradeId" class="form-control" onchange="findByCondition1()" style="width:120px">
		                        <#if gradelist?? && (gradelist?size>0)>
		                        	<option value="">--请选择--</option>
		                            <#list gradelist as grade>
		                            <option value="${grade.id!}" <#if gradeId! == grade.id>selected</#if>>${grade.gradeName!}</option>
		                            </#list>
		                        <#else>
		                            <option value="">暂无数据</option>
		                        </#if> 
		                        </select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<select name="classId" id="classId" class="form-control" onchange="findByCondition1()" style="width:120px">
		                        <#if clslist?? && (clslist?size>0)>
		                        	<option value="">--全部--</option>
		                            <#list clslist as cls>
		                            <option value="${cls.id!}" <#if classId! == cls.id>selected</#if>>${cls.classNameDynamic!}</option>
		                            </#list>
		                        <#else>
		                            <option value="">--全部--</option>
		                        </#if> 
		                        </select>
						</div>
					</div>
					<div class="filter-item">
						<div class="filter-content">
							<div class="input-group input-group-search">
						        <select name="selectType" id="selectType" class="form-control">
						        	<option value="1" <#if selectType! == '1'>selected</#if>>学号</option>
						        	<option value="2" <#if selectType! == '2'>selected</#if>>姓名</option>
						        </select>
						        <div class="pos-rel pull-left">
	                            	<input type="text" style="width:100px" class="form-control" name="selectObj" id="selectObj" value="${selectObj!}" onkeydown="dispRes()">
	                            </div>
	                            <div class="input-group-btn">
	                                <a href="javascript:" class="btn btn-default"  onclick="findByCondition1()">
	                                    <i class="fa fa-search"></i>
	                                </a>
	                            </div>
						    </div>
						</div>
					</div>
					<div class="filter-item filter-item-right">
						<button class="btn btn-blue" onclick="doExport();">导出</button>
					</div>
				</div>
				<div class="table-container">
					<div class="table-container-body">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>姓名</th>
									<th>学号</th>
									<th>行政班</th>
								</tr>
							</thead>
							<tbody>
							<#if stulist?? && stulist?size gt 0>
								<#list stulist as stu>
									<tr>
										<td>${stu_index + 1}</td>
										<td>${stu.studentName!}</td>
										<td>${stu.studentCode!}</td>
										<td>${stu.className!}</td>
									</tr>
								</#list>
							</#if>
							</tbody>
						</table>
					</div>
					<#if stulist?? && stulist?size gt 0>
					<@htmlcom.pageToolBar container="#stuNoList" class="noprint">
					</@htmlcom.pageToolBar>
					</#if>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<script>
function black(){
	var url =  '${request.contextPath}/evaluate/project/index/page?acadyear=${acadyear!}&semester=${semester!}';
	$(".model-div").load(url);
}
function doExport(){
	var gradeId = $('#gradeId').val();
	var classId = $('#classId').val();
	var selectObj = $('#selectObj').val();
	var selectType = $('#selectType').val();
	location.href='${request.contextPath}/evaluate/project/doExport/page?projectId=${projectId!}&acadyear=${acadyear!}&semester=${semester!}&gradeId='+gradeId+'&classId='+classId+'&selectType='+selectType+'&selectObj='+selectObj;
}
function dispRes(){
		var x;
        if(window.event) // IE8 以及更早版本
        {	x=event.keyCode;
        }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
        {
            x=event.which;
        }
        if(13==x){
            findByCondition1();
        }
    }
function findByCondition1(){
	var gradeId = $('#gradeId').val();
	var classId = $('#classId').val();
	var selectObj = $('#selectObj').val();
	var selectType = $('#selectType').val();
	var url =  '${request.contextPath}/evaluate/project/showStuNotSubList/page?projectId=${projectId!}&acadyear=${acadyear!}&semester=${semester!}&gradeId='+gradeId+'&classId='+classId+'&selectType='+selectType+'&selectObj='+selectObj;
	$(".model-div").load(url);
}

function showSubStu(){
	var url =  '${request.contextPath}/evaluate/project/showStuSubList/page?projectId=${projectId!}&acadyear=${acadyear!}&semester=${semester!}';
	$(".model-div").load(url);
}
function showNoSubStu(){
	var url =  '${request.contextPath}/evaluate/project/showStuNotSubList/page?projectId=${projectId!}&acadyear=${acadyear!}&semester=${semester!}';
	$(".model-div").load(url);
}
</script>