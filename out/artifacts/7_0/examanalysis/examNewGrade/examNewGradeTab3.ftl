<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row">
	<div class="col-md-12">
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
					<div class="filter-item">
						<label for="" class="filter-name">考试名称：</label>
						<div class="filter-content">
							<select vtype="selectOne" class="form-control" id="examId" onChange="<#if type?default("")=="1">changeSubjectList()<#else>searchList()</#if>">
								<option value="">---请选择---</option>
							</select>
						</div>
					</div>
					<div class="filter-item filter-item-right">
						<button class="btn btn-blue" onclick="searchList();">查询</button>
						<button class="btn btn-white" onclick="doExportTotal();">导出</button>
					</div>
				</div>
				<div id="tableDiv"></div>
		    </div>
		</div>
	</div>
</div>
<script>
	$(function(){
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
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
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				searchList();
			}
		});
	}
	function searchList(){
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&gradeCode='+gradeCode;
		var url='${request.contextPath}/examanalysis/examNewGrade/subTotalList/page'+c2;
		$("#tableDiv").load(url);
	}
</script>