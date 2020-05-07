<div id="exammanageDiv">
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">考试信息</h4>
	</div>
	<div class="box-body">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showList()">
						<#if acadyearList?exists && (acadyearList?size>0)>
		                    <#list acadyearList as item>
			                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		                    </#list>
	                    <#else>
		                    <option value="">未设置</option>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select class="form-control" id="searchSemester" name="searchSemester" onChange="showList()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select class="form-control" id="searchGradeCode" name="searchGradeCode"  onChange="showList()">
						<option value="">全部</option>
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.gradeCode!}">${item.gradeName!}</option>
		                    </#list>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span  class="filter-name">类型：</span>
				<div class="filter-content">
					<select class="form-control" style="width:180px;" id="searchType" name="searchType" onChange="showList()">
						<option value="1">本单位设定的考试</option>
						<#if unitClass?default(-1) == 2>
	                        <option value="3">参与的校校联考</option>
	                    </#if>
					</select>
				</div>
			</div>
			<div class="filter-item text-right">
					<a href="javascript:" class="btn btn-white js-addExam" onclick="addExam();">新增考试</a>
			</div>
		</div>
	</div>
</div>
<div class="table-container">
	<div class="table-container-body" id="showList">	
	</div>
</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(function(){
		showList();
	});
	function showList(){
		 var searchAcadyear = $('#searchAcadyear').val();
	     var searchSemester = $('#searchSemester').val();
	     var searchGradeCode = $('#searchGradeCode').val();
		 var searchType = $('#searchType').val();
		 var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&searchGradeCode="+searchGradeCode+"&searchType="+searchType;
	     var url =  '${request.contextPath}/exammanage/edu/examInfo/list/page'+str;
		 $("#showList").load(url);
	}
	function addExam(){
	   	var url = "${request.contextPath}/exammanage/edu/examInfo/edit/page";
	    indexDiv = layerDivUrl(url,{title: "信息",width:750,height:550});
	}
	function doDelete(id){
	     var ii = layer.load();
	     $.ajax({
				url:'${request.contextPath}/exammanage/examInfo/delete',
				data: {'id':id},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
	                    layer.closeAll();
						layer.msg(jsonO.msg, {
								offset: 't',
								time: 2000
							});
					  	showList();
			 		}
			 		else{
			 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
			 			isSubmit=false;
					}
					layer.close(ii);
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
	}
	
	function doEdit(id){
	   var url = "${request.contextPath}/exammanage/edu/examInfo/edit/page?id="+id;
	   indexDiv = layerDivUrl(url,{title: "信息",width:750,height:550});
	}
	
	function doSetSubject(examId,isView){
		var url =  '${request.contextPath}/exammanage/edu/subjectClassIndex/index/page?examId='+examId+'&isView='+isView;
		$("#exammanageDiv").load(url);
	}
</script>

