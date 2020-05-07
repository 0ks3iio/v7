<div class="box box-default">
<div class="row">
	<div class="col-xs-12">
	      <div class="box-body">
	         <div class="filter clearfix head-div">
	        	<div class="filter-item">
					<label for="" class="filter-name">学年：</label>
					<div class="filter-content">
						<select class="form-control" id="acadyear" onChange="searchExam();" style="width:120px;">
						<#if (acadyearList?size>0)>
							<#list acadyearList as item>
							<option value="${item!}" <#if item==acadyearSearch>selected</#if>>${item!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">学期：</label>
					<div class="filter-content">
						<select class="form-control" id="semester" onChange="searchExam();" style="width:120px;">
						 ${mcodeSetting.getMcodeSelect('DM-XQ',(semesterSearch?default(0))?string,'0')}
						</select>
					</div>
				</div>
				
				<div class="filter-item">
					<label for="" class="filter-name">考试名称：</label>
					<div class="filter-content">
						<select class="form-control" id="examId" onChange="searchGrade();" style="width:150px;">
						<option value="">--请选择--</option>
						<#if (examInfoList?size>0)>
							<#list examInfoList as item>
							<option value="${item.id!}">${item.examName!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				
				<div class="filter-item">
					<label for="" class="filter-name">年级：</label>
					<div class="filter-content">
						<select class="form-control" id="gradeId" onChange="resitInfoList();" style="width:120px;">
						<option value="">--请选择--</option>
						<#if (gradeList?size>0)>
							<#list gradeList as item>
							<option value="${item.id!}">${item.gradeName!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
			 
			    <div class="filter-item">
			        <a href="javascript:" class="btn btn-blue" style="margin-left: 10px" onClick="doImport();">导入</a>
                     <a href="javascript:" class="btn btn-blue" onClick="doExport();">导出</a>
			        <a href="javascript:" class="btn btn-blue" onclick="saveResitScoreInfo();">获取补考名单</a>
				</div>
			</div>
			<div class="tab-content" id="resitInfoList">
			       <div class="no-data-container">
				        <div class="no-data no-data-hor">
					        <span class="no-data-img">
						        <img src="${request.contextPath}/scoremanage/images/no-tutor-project.png" alt="">
					        </span>
					        <div class="no-data-body">
					            <h3>暂无数据</h3>
						        <p class="no-data-txt">请选择一个考试和年级！</p>
					        </div>
			          </div>
		         </div>
		   </div>
	    </div>
	</div>
</div>
</div>
<script>
function resitInfoList(){
    var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var examId = $('#examId').val();
	var gradeId = $('#gradeId').val();
	var url="${request.contextPath}/scoremanage/resitScore/resitInfoList?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&gradeId="+gradeId;
	$('#resitInfoList').load(url);
}

function doExport(){
    var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var examId = $('#examId').val();
	var gradeId = $('#gradeId').val();
	if(gradeId == ''){
	     layerTipMsgWarn("提示","请选择一个考试和年级!");
	     var isSubmit=false;
		 return;
	}
	var url="${request.contextPath}/scoremanage/resitScore/doExport?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&gradeId="+gradeId;
    document.location.href=url;
}

function doImport(){
    var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var examId = $('#examId').val();
	var gradeId = $('#gradeId').val();
	if(gradeId == ''){
	     layerTipMsgWarn("提示","请选择一个考试和年级!");
	     var isSubmit=false;
		 return;
	}
	var url="${request.contextPath}/scoremanage/resitScoreImport/main?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&gradeId="+gradeId;
    $('#resitInfoList').load(url);
}

function searchExam(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var examSel=$("#examId");
    $.ajax({
			url:"${request.contextPath}/scoremanage/resitScore/searchExam",
			data:{acadyear:acadyear,semester:semester},
			dataType: "json",
			success: function(data){
			    examSel.html("");
			    examSel.chosen("destroy");
			    if(data==null){
				    examSel.append("<option value='' >---请选择---</option>");
			    }else{
			        examSel.append("<option value='' >---请选择---</option>");
				    for(var m=0;m<data.length;m++){
				        examSel.append("<option value='"+data[m].id+"' >"+data[m].examName+"</option>");
				    }
			   }
			}
		});
}

function searchGrade(){
    var examId = $('#examId').val();
    var gradeSel=$("#gradeId");
    $.ajax({
			url:"${request.contextPath}/scoremanage/resitScore/searchGrade",
			data:{examId:examId},
			dataType: "json",
			success: function(data){
			    gradeSel.html("");
			    gradeSel.chosen("destroy");
			    if(data==null){
				    gradeSel.append("<option value='' >---请选择---</option>");
			    }else{
			        gradeSel.append("<option value='' >---请选择---</option>");
				    for(var m=0;m<data.length;m++){
				        gradeSel.append("<option value='"+data[m].id+"' >"+data[m].gradeName+"</option>");
				    }
			   }
			}
		});
}

var isSubmit=false;
function saveResitScoreInfo(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var examId = $('#examId').val();
	var gradeId = $('#gradeId').val();
	if(gradeId == ''){
	     layerTipMsgWarn("提示","请选择一个考试和年级!");
	     var isSubmit=false;
		 return;
	}
   showConfirmMsg('确定要重新获取补考名单吗？','确定',function(){
			var ii = layer.load();
			$.ajax({
				url:"${request.contextPath}/scoremanage/resitScore/saveResitInfo",
				data:{"acadyear":acadyear,"semester":semester,"examId":examId,"gradeId":gradeId},
				type:'post',
				success:function(data) {
				    layer.closeAll();
				    var jsonO = JSON.parse(data);
				    if(jsonO.success){
					    //layerTipMsg(jsonO.success,"成功",jsonO.msg);
					    layer.msg(jsonO.msg, {
						  offset: 't',
						  time: 2000
					    });
					    resitInfoList();
				    }else{
					    layerTipMsg(jsonO.success,"失败",jsonO.msg);
				    }
				    layer.close(ii);
				    isSubmit =false;
			   },
			   error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	});
}
</script>