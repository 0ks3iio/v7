<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-header header_filter">
        <div class="filter filter-f16">
        	<input type='hidden' id='noLimit' value='${noLimit!}'/>
        	<input type='hidden' id='type' value='${type!}'/>
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
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>		
	        <div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" onChange="changeClassType()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>	
		</div>
		<div class="filter filter-f16">
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
			<div class="filter-item" id='findDiv'>	
				<a href="javascript:" class="btn btn-blue" onclick="searchList()">查找</a>
				<a href="javascript:" class="btn btn-blue" onclick="searchImport()">导入</a>
			</div>
     	</div>
    </div>
	<div class="box-body tabDiv">	
	</div>
</div>
<script>
	$(function(){
		showBreadBack(gobackLock,true,"录入");
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	
	function gobackLock(){
		var c2='?examId=${examId!}&acadyear=${acadyearSearch!}&semester=${semesterSearch!}&gradeCode=${gradeCode!}'
				+'&subjectInfoId=${subjectInfoId!}&noLimit=${noLimit!}';
		var url='${request.contextPath}/scoremanage/scoreInfo/lock/index/page'+c2;
		$('.model-div').load(url);
	}
	
	function changeExam(){
		var acadyear=$("#acadyearSearch").val();
		var semester=$("#semesterSearch").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}',noLimit:'${noLimit!}',fromType:'1'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				examClass.chosen("destroy");
				if(data.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						var sels = '';
						if(data[i].id=='${examId!}'){
							sels=" selected='selected'";
						}
						examClass.append("<option value='"+data[i].id+"' "+sels+">"+data[i].examNameOther+"</option>");
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
			data:{examId:examId,unitId:'${unitId!}',noLimit:'${noLimit!}',fromType:'1'},
			dataType: "json",
			success: function(data){
				gradeCodeClass.html("");
				if(data==null){
					gradeCodeClass.append("<option value='' >---请选择---</option>");
				}else{
					for(key in data){
						var sels = '';
						if(key=='${gradeCode!}'){
							sels=" selected='selected'";
						}
						i++;
						gradeCodeClass.append("<option value='"+key+"' "+sels+">"+data[key]+"</option>");
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
		var type = $("#type").val();
		var selClass=$("#classIdSearch");
		$.ajax({
			url:"${request.contextPath}/scoremanage/scoreInfo/classList",
			data:{examId:examId,classType:classType,gradeCode:gradeCode,unitId:'${unitId!}',noLimit:'${noLimit!}'},
			dataType: "json",
			success: function(data){
				selClass.html("");
				selClass.chosen("destroy");
				if(data==null){
					selClass.append("<option value='' >---请选择---</option>");
				}else{
					var m=0;
					for(key in data){
						m=m+1;
						var sels = '';
						if(key=='${classIdSearch!}'){
							sels=" selected='selected'";
						}
						selClass.append("<option value='"+key+"' "+sels+">"+data[key]+"</option>");
					}
					if(m==0){
						selClass.append("<option value='' >---请选择---</option>");
					}
				}
				$(selClass).chosen({
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				if (type == 1) {
					searchList('${subjectId!}');
				} else {
					searchImport();
				}
			}
		});
	}
	
	function searchList(data){
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var classIdSearch=$("#classIdSearch").val();
		var c2='?examId='+examId+'&classType='+classType+'&classIdSearch='+classIdSearch+'&subjectId='+data+'&noLimit=${noLimit!}';
		var url='${request.contextPath}/scoremanage/scoreInfo/tablist/page'+c2;
		$(".tabDiv").load(url);
	}
	
	function searchImport() {
		//alert(1);
		var classIdSearch = $('#classIdSearch').val();
		if(classIdSearch==''){
			layerTipMsg(false,"失败","请选择班级");
			return;
		}
		
		//alert(2);
		if($("#classIdSearch").attr("onChange") != 'searchImport();'){
			//alert(3);
			var islock = $("#mysubmit").length;
			var printResultLen = $('#printResult').length;
			if(islock<1 && printResultLen <1){
				//alert();
				layerTipMsg(false,"失败","您没有这门课的导入权限");
				return;
			}
			if(islock<1){
				layerTipMsg(false,"失败","成绩已提交，不能再导入");
				return;
			}
		}
		
	
		$('#findDiv').attr("style","display:none;");
		$("#type").val(2);
		$("#classIdSearch").attr("onChange","searchImport();");
		var subType = $("#subType li[class='active']").children().text();
		var examId=$("#examId").val();
		var classIdSearch=$("#classIdSearch").val();
		var str = '?examId='+examId+'&classIdSearch='+classIdSearch+'&subType='+subType;
		var url='${request.contextPath}/scoremanage/scoreImport/head'+str;
		url=encodeURI(encodeURI(url));
		$(".tabDiv").load(url);
	}
	
</script>