<div id="aa" class="tab-pane active" role="tabpanel">
		<div class="filter">
		    <div class="filter-item">
		       <span class="color-red">同步数据操作只同步成绩状态为正常的学生数据</span>
		    </div>
			<div class="filter-item filter-item-right">
			<#if !hasSetZf?default(false)>
				<span class="color-red">当前科目未折分，计算前请先设置折分。</span>
			</#if>
			    <a class="btn btn-blue js-setting" onclick="save();">保存</a>
				<button class="btn btn-blue js-setting" onclick="synDate();">同步数据</button>
				<button class="btn btn-blue js-setting" onclick="compute();">计算</button>
				<button class="btn btn-blue js-setting" onclick="doImport();">导入</button>
				<button class="btn btn-blue js-setting" id="zfButton">折分设置</button>
			</div>
		</div>
		<form id="subForm" method="post">
		<div class="table-container" id="listDiv">
		    <div class="table-container-header">共有${scoreSize!}个数据</div>
			<div class="table-container-body">
			    <input type="hidden" name="examId" value="${examId!}">
			    <input type="hidden" name="subjectId" value="${subjectId!}">
			    <input type="hidden" name="gradeId" value="${gradeId!}">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>学号</th>
							<th>班级</th>
							<th>卷面分</th>
							<th>实分</th>
						</tr>
					</thead>
					<tbody>
					  <#if scoreDtoList?exists&&scoreDtoList?size gt 0>
			              <#list scoreDtoList as scoreDto>
						<tr>
							<td>${scoreDto_index+1!}</td>
							<td>${scoreDto.studnetName!}</td>
							<td>${scoreDto.studentCode!}</td>
							<td>${scoreDto.className!}</td>
							<td>${scoreDto.score!}</td>
							<td>
							    <input type="text" class="form-control" name="compreScoreList[${scoreDto_index!}].toScore" id="dyCourseRecordList${scoreDto_index!}toScore" value="${scoreDto.toScore!}" vtype="number" min="0" max="999" maxLength="5" decimalLength="1">
							    <input type="hidden" name="compreScoreList[${scoreDto_index!}].id" value="${scoreDto.id!}">
							</td>
						</tr>
						 </#list>
					  <#else>
					    <tr>
							<td  colspan="88" align="center">
							暂无成绩
							</td>
						<tr>
					  </#if>
					</tbody>
				</table>
			</div>
        </div>
        </form>
</div>
<div id="zfEdit"></div>
<script>
$(function(){	    
	$('#zfButton').on('click',function(e){
    	e.preventDefault();
    	var that = $(this);
    	var gradeId = '${gradeId!}';
    	var examId = '${examId!}';
    	var subjectId = '${subjectId!}';
    	var url =  "${request.contextPath}/comprehensive/subjects/score/toZfEdit?examId="+examId+"&subjectId="+subjectId+"&gradeId="+gradeId;
		$("#zfEdit").load(url,function() {
			layerShow();
		});
    })
});

function layerShow(){
   layer.open({
	   type: 1,
	   shade: 0.5,
	   title: '折分设置',
	   area: '360px',
	   btn: ['确定','取消'],
	   yes: function(index){
	       saveZfs();
	   },
	   content: $('.layer-setting')
	});
}

var isSubmit=false;
function save(){
   var check = checkValue('#listDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/comprehensive/subjects/score/compreScoreSave",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		return;
		 	}else{
		 		layer.closeAll();
				//layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				 layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
				reLoad();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}

function reLoad(){
    var gradeId = '${gradeId!}';
	var examId = '${examId!}';
	var subjectId = '${subjectId!}';
	var url =  '${request.contextPath}/comprehensive/subjects/score/showpage?examId='+examId+'&subjectId='+subjectId+'&gradeId='+gradeId;
    $('#showCompreScore').load(url);
}

function doImport(){
    var gradeId = '${gradeId!}';
	var examId = '${examId!}';
	var subjectId = '${subjectId!}';
    $("#exammanageDiv").load("${request.contextPath}/comprehensive/subjects/compreScore/import/main?gradeId="+gradeId+"&examId="+examId+"&subjectId="+subjectId);
}

 //同步数据
  function synDate(){
    var gradeId = '${gradeId!}';
    var examId = '${examId!}';
    var subjectId = '${subjectId!}';
    $.ajax({
            url:'${request.contextPath}/comprehensive/subjects/score/page?examId='+examId+'&subjectId='+subjectId+'&gradeId='+gradeId,
            data:JSON.stringify({}),
            clearForm : false,
            resetForm : false,
            dataType:'json',
            contentType: "application/json",
            type:'post',
            success:function (data) {
                if(data.success){
	               layer.closeAll();
				   layer.msg("同步成功", {
						offset: 't',
						time: 2000
					});
				   showSynScore(examId,subjectId,gradeId);
                }else{
                   layerTipMsg(data.success,"同步失败",data.msg);
			 	   return;
                }
            }
    })
  }
  

  //计算折分
  function compute(){
    var gradeId = '${gradeId!}';
    var examId = '${examId!}';
    var subjectId = '${subjectId!}';
     $.ajax({
            url:'${request.contextPath}/comprehensive/subjects/score/isSetUp?examId='+examId+'&subjectId='+subjectId+'&gradeId='+gradeId,
            data:JSON.stringify({}),
            clearForm : false,
            resetForm : false,
            dataType:'json',
            contentType: "application/json",
            type:'post',
            success:function (data) {
                if(data.success){
                   layer.closeAll();
                  // layerTipMsg(data.success,"计算成功",data.msg);
                    layer.msg("计算成功", {
						offset: 't',
						time: 2000
					});
				   showSynScore(examId,subjectId,gradeId);
                }else{
                   layerTipMsg(data.success,"计算失败",data.msg);
			 	   return;
                }
            }
      })
  }


</script>
