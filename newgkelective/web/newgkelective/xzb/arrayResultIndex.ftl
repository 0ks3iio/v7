<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li <#if type?default('1')=='1'>class="active" </#if> role="presentation"><a href="javascript:void(0)" onclick="resultIndex('1')"role="tab" data-toggle="tab">行政班结果</a></li>
			<li <#if type?default('1')=='2'>class="active" </#if> role="presentation"><a href="javascript:void(0)" onClick="resultIndex('2')" role="tab" data-toggle="tab">教学班结果</a></li>
			<li <#if type?default('1')=='3'>class="active" </#if> role="presentation"><a href="javascript:void(0)" onclick="resultIndex('3')"role="tab" data-toggle="tab">学生上课查询</a></li>
			<li <#if type?default('1')=='4'>class="active" </#if> role="presentation"><a href="javascript:void(0)" onclick="resultIndex('4')" role="tab" data-toggle="tab">教师上课查询</a></li>
			
			<li <#if type?default('1')=='6'>class="active" </#if> role="presentation"><a href="javascript:void(0)" onclick="resultIndex('6')" role="tab" data-toggle="tab">班级总课表</a></li>
			<li <#if type?default('1')=='7'>class="active" </#if> role="presentation"><a href="javascript:void(0)" onclick="resultIndex('7')" role="tab" data-toggle="tab">教师总课表</a></li>
			
		</ul>
		<div class="tab-content" id="tableList">
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	resultIndex("${type?default('1')}");
});

function resultIndex(type){
	if("1"==type){
		toNewClassResult(); 
	}else if("2"==type){
		toSubjectClassResult();
	}else if("3"==type){
		toStudentClassResult();
	}else if("4"==type){
		toTeacher();
	}else if("5"==type){
		toClassroomUseage();
	}else if("6"==type){
		toResultAll("1");
	}else if("7"==type){
		toResultAll("2");
	}else if("8"==type){
		toResultAll("3");
	}else if("9"==type){
		toChangeClass();
	}else if("10"==type){
		//修改学生选课数据 但对选课数据不进行保存
		toChangeStuChooseIndex();
	}
}

function toChangeStuChooseIndex(){
	var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/toChangeStuChooseIndex';
	$("#tableList").load(url);
}

function goBack2(){
	var url =  '${request.contextPath}/newgkelective/xzb/index/page';
	$("#showList").load(url);
}
showBreadBack(goBack2, false, '排课结果');

function toStudentClassResult(){
    var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/studentClassResultHead';
	$("#tableList").load(url);
}

function toNewClassResult(){
    var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/newClassResult';
	$("#tableList").load(url);
}

function toResultAll(type){
    var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/page?type='+type;
	$("#tableList").load(url);
}

function toSubjectClassResult(){
    var arrayId = '${arrayId!}';
    var url = '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/subjectClassResult/tabHead/page';
	$("#tableList").load(url);
}


function toTeacher(){
	var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/teacher/page';
	$("#tableList").load(url);
}
function toClassroomUseage(){
	var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/classroomUseage';
	$("#tableList").load(url);
}


function toChangeClass(){
	var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/changeClass/page?type=2';
	$("#tableList").load(url);
	
}
</script>