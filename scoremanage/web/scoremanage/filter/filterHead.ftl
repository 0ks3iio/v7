<div id="bb" class="tab-pane active" role="tabpanel">
	<div class="box box-default">
	    <div class="box-header header_filter">
	        <div class="filter filter-f16">
	        	<div class="filter-item">
					<label for="" class="filter-name">学年：</label>
					<div class="filter-content">
						<select class="form-control" id="acadyearSearch" onChange="changeExam()">
						<#if (acadyearList?size>0)>
							<#list acadyearList as item>
							<option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}学年</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">学期：</label>
					<div class="filter-content">
						<select class="form-control" id="semesterSearch" onChange="changeExam()">
						 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
						</select>
					</div>
				</div>	
				<div class="filter-item">
					<label for="" class="filter-name">考试名称：</label>
					<div class="filter-content">
						<select vtype="selectOne" class="form-control" id="examId" onChange="changeGradeCode()">
							<option value="">-----请选择-----</option>
						</select>
					</div>
				</div>	
				<!--<a href="javascript:" class="btn btn-blue pull-left btn-export"><#if tabType=='1'>不排考<#else>不统分</#if>导入</a>-->
			</div>
	        <div class="filter filter-f16">
		        <div class="filter-item">
					<label for="" class="filter-name">年级：</label>
					<div class="filter-content">
						<select class="form-control" id="gradeCode" onChange="changeClassType()">
							<option value='' >---请选择---</option>
						</select>
					</div>
				</div>
	        	<div class="filter-item">
					<label for="" class="filter-name">班级类型：</label>
					<div class="filter-content">
						<select class="form-control" id="classType" onChange="changeClassType()">
							<option value="1" <#if classType?default('1')=='1'>selected="selected"</#if>>行政班</option>
							<option value="2" <#if classType?default('1')=='2'>selected="selected"</#if>>教学班</option>
						</select>
					</div>
				</div>	
				<div class="filter-item">
					<label for="" class="filter-name">班级：</label>
					<div class="filter-content">
						<select vtype="selectOne" class="form-control" id="classIdSearch" onChange="searchList()">
							<option value="">---请选择---</option>
						</select>
					</div>
				</div>	
				<div class="filter-item">
					<label for="" class="filter-name">类型：</label>
					<div class="filter-content">
						<select class="form-control" id="arrType" onChange="searchList()" >
							<#if tabType=='1'>
								<option value="1" <#if arrType?default('1')=='1'>selected="selected"</#if>>排考名单</option>
								<option value="2" <#if arrType?default('1')=='2'>selected="selected"</#if>>不排考名单</option>
							<#elseif tabType=='2'>
								<option value="1" <#if arrType?default('1')=='1'>selected="selected"</#if>>统分名单</option>
								<option value="2" <#if arrType?default('1')=='2'>selected="selected"</#if>>不统分名单</option>
							</#if>
						</select>
					</div>
				</div>			
				<a href="javascript:" class="btn btn-blue pull-left btn-seach" onclick="searchList()">查找</a>
				
	     	</div>
	    </div>
	    <div class="box-body filterDiv1">
	    
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
		var acadyear=$("#acadyearSearch").val();
		var semester=$("#semesterSearch").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				examClass.chosen("destroy");
				if(data.length==0){
					examClass.append("<option value=''>-----请选择-----</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examNameOther+"</option>");
					}
				}
				$(examClass).chosen({
					width:'100%',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				changeGradeCode();
			}
		});
	}
	function changeGradeCode(){
		var examId=$("#examId").val();
		var gradeCodeClass=$("#gradeCode");
		var i=0;
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/gradeCodeList",
			data:{examId:examId,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				gradeCodeClass.html("");
				if(data==null){
					gradeCodeClass.append("<option value='' >---请选择---</option>");
				}else{
					for(key in data){
						i++;
						gradeCodeClass.append("<option value='"+key+"' >"+data[key]+"</option>");
					}
					if(i==0){
						gradeCodeClass.append("<option value='' >---请选择---</option>");
					}
				}
				changeClassType();
			}
		});
	}
	
	function changeClassType(){
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var gradeCode=$("#gradeCode").val();
		var selClass=$("#classIdSearch");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/classList",
			data:{examId:examId,classType:classType,gradeCode:gradeCode,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				selClass.html("");
				selClass.chosen("destroy");
				if(data==null){
					selClass.append("<option value='' >---请选择---</option>");
				}else{
					selClass.append("<option value='' >---请选择---</option>");
					for(key in data){
						selClass.append("<option value='"+key+"' >"+data[key]+"</option>");
					}
				}
				$(selClass).chosen({
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
		var classType=$("#classType").val();
		var classIdSearch=$("#classIdSearch").val();
		var gradeCode=$("#gradeCode").val();
		var arrType=$("#arrType").val();
		var c2='?examId='+examId+'&gradeCode='+gradeCode+'&classType='+classType+'&classIdSearch='+classIdSearch+'&arrType='+arrType+'&tabType=${tabType!}';
		var url='${request.contextPath}/scoremanage/filter/list/page'+c2;
		$(".filterDiv1").load(url);
	}
</script>
       