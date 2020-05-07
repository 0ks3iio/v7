<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
		<div class="box box-default">
		    <div class="box-body">
		    	<div class="filter box-graybg mb10 no-padding-bottom">
		    		<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
						<div class="filter-content">
							<select class="form-control" id="acadyear" onChange="changeExam()">
							<#if acadyearList?exists && acadyearList?size gt 0>
								<#list acadyearList as item>
								<option value="${item!}" <#if item==semester.acadyear?default("")>selected="selected"</#if>>${item!}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学期：</label>
						<div class="filter-content">
							<select class="form-control" id="semester" onChange="changeExam()">
								${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
							</select>
						</div>
					</div>		
			        <div class="filter-item">
						<label for="" class="filter-name">年级：</label>
						<div class="filter-content">
							<select class="form-control" id="gradeCode" onChange="changeExam()">
								<#if gradeList?exists && gradeList?size gt 0>
									<#list gradeList as item>
									<option value="${item.gradeCode!}" <#if item.gradeCode==gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
									</#list>
								</#if>
							</select>
						</div>
					</div>	
					<div class="filter-item chosenExamHeaderClass">
						<label for="" class="filter-name">本次考试：</label>
						<div class="filter-content">
							<select vtype="selectOne" class="form-control" id="examId" onChange="changeSubjectList()">
								<option value="">---请选择---</option>
							</select>
						</div>
					</div>		
					<div class="filter-item">
						<label for="" class="filter-name">科目：</label>
						<div class="filter-content">
							<select class="form-control" id="subjectId" onChange="changeReferExamList()">
								<option value="">---请选择---</option>
							</select>
						</div>
					</div>
					<div class="filter-item chosenReferExamHeaderClass">
						<label for="" class="filter-name">参照考试：</label>
						<div class="filter-content">
							<select vtype="selectMore" multiple data-placeholder="请选择" id="referExamId" onChange="searchList()">
								<option value="">暂无数据</option>
							</select>
						</div>
					</div>	
					<div class="filter-item">
						<div class="btn-group" role="group">
							<a type="button"  class="btn btn-blue" href="javascript:;" id="tableId" onclick="changeType('1')">报表</a>
							<a type="button" class="btn btn-white" href="javascript:;" id="echartsId" onclick="changeType('2')">图表</a>
						</div>
					</div>
					<div class="filter-item filter-item-right">
						<button class="btn btn-blue" id="searchId" onclick="searchList();">查询</button>
						<button class="btn btn-white" id="exportId" onclick="doExportPro();">导出</button>
					</div>
				</div>
				<div id="myTeachListDiv">
				</div>
		    </div>
		</div>
    </div>
</div>
<script>
	$(function(){
		//初始化单选控件
		var viewContent2={
			'width' : '220px',//输入框的宽度
			'multi_container_height' : '33px',//输入框的高度
			'results_height' : '150px',//下拉选择的高度
		}
		initChosenOne(".chosenExamHeaderClass","",viewContent2);
		var viewContent1={
			'width' : '280px',//输入框的宽度
			'multi_container_height' : '33px',//输入框的高度
			'results_height' : '150px',//下拉选择的高度
			'max_selected_options' : 1//限制1个
		}
		initChosenMore(".chosenReferExamHeaderClass","",viewContent1);
		changeExam();		
	});
	function changeType(type){
		if(type && type=='1'){
			if(!$("#tableId").hasClass("btn-blue")){
				$("#tableId").addClass("btn-blue").removeClass("btn-white");
				$("#echartsId").removeClass("btn-blue").addClass("btn-white");
				$("#scatterDiv").hide();
				$("#proTableDiv").show();
				$("#searchId").show();
				$("#exportId").show();
			}
		}else if(type && type=='2'){
			if(!$("#echartsId").hasClass("btn-blue")){
				$("#echartsId").addClass("btn-blue").removeClass("btn-white");
				$("#tableId").addClass("btn-white").removeClass("btn-blue");
				$("#scatterDiv").show();
				$("#proTableDiv").hide();
				$("#searchId").hide();
				$("#exportId").hide();
			}
		}
	}
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamList",
			data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				examClass.html("");
				examClass.chosen("destroy");
				if(infolist.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					var examHtml='';
					for(var i = 0; i < infolist.length; i ++){
						examHtml="<option value='"+infolist[i].id+"' ";
						examHtml+=" >"+infolist[i].name+"</option>";
						examClass.append(examHtml);
					}
				}
				$(examClass).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
				}); 
				changeSubjectList();
			}
		});
	}
	
	function changeSubjectList(){
		var examId=$("#examId").val();
		var subjectIdClass=$("#subjectId");
		var subType="0";
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamSubList",
			data:{examId:examId,unitId:'${unitId!}',subType:subType,fromNewGrade:"true"},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				subjectIdClass.html("");
				if(infolist==null || infolist.length==0){
					subjectIdClass.append("<option value='' >---请选择---</option>");
				}else{
					var subjectHtml='';
					for(var i = 0; i < infolist.length; i ++){
						subjectHtml="<option value='"+infolist[i].id+"' ";
						subjectHtml+=" >"+infolist[i].name+"</option>";
						subjectIdClass.append(subjectHtml);
					}
				}
				changeReferExamList();
			}
		});
	}
	
	function changeReferExamList(){
		var subjectId=$("#subjectId").val();
		var examId=$("#examId").val(); 
		var referExamClass=$("#referExamId");
		var sumId = "";
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryReferExamList",
			data:{examId:examId,unitId:'${unitId!}',subjectId:subjectId,sumId:sumId},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				referExamClass.html("");
				referExamClass.chosen("destroy");
				if(infolist==null || infolist.length==0){
					referExamClass.append("<option value='' >暂无数据</option>");
				}else{
					for(var i = 0; i < infolist.length; i ++){
						var htmlOption="<option ";
		    				htmlOption+=" value='"+infolist[i].id+"'>"+infolist[i].name;
						referExamClass.append(htmlOption);
					}
				}
					$("#referExamId").chosen({
						'width' : '280px',//输入框的宽度
						'multi_container_height' : '33px',//输入框的高度
						'results_height' : '150px',//下拉选择的高度
						'max_selected_options' : '1'//限制1个
					}); 
				searchList();
			}
		});
	}
	function searchList(){
		layer.msg("正在加载中,请稍后...");
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var subjectId=$("#subjectId").val();
		var gradeCode=$("#gradeCode").val();
		var referExamId=$("#referExamId").val();
		if(!referExamId){
			referExamId="";
		}
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='
		+gradeCode+"&referExamId="+referExamId;
		var url='${request.contextPath}/examanalysis/examNewGrade/proList/page'+c2;
		$("#myTeachListDiv").load(url);
	}
	function doExportPro(){
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var subjectId=$("#subjectId").val();
		var gradeCode=$("#gradeCode").val();
		var referExamId=$("#referExamId").val();
		if(!referExamId){
			referExamId="";
		}
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='
		+gradeCode+"&referExamId="+referExamId;
		location.href="${request.contextPath}/examanalysis/examNewGrade/exportPro"+c2;
	}
</script>