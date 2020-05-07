<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<div id="myTestDiv">
<div class="box box-default">
    <div class="box-header header_filter">
        <div class="filter filter-f16">
        	<input type='hidden' id='noLimit' value='${noLimit!}'/>
        	<#--<input type='hidden' id='type' value='${type!}'/>-->
        	<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control" id="acadyearSearch">
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
				<div class="filter-content" style="width:130px;">
					<select class="form-control" id="semesterSearch" >
					 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
					</select>
				</div>
			</div>		
			
			<div class="filter-item">
				<label for="" class="filter-name">考试课程：</label>
				<div class="filter-content" style="width:150px;">
					<select vtype="selectOne" class="form-control" id="courseId" style="width:20%;">
					<#if courseList?exists && courseList?size gt 0>
						<#list courseList as item>
							<option value="${item.id!}">${item.subjectName!}</option>
						</#list>
					<#else>
						<option value="">没有选修课</option>
					</#if>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">教学班级：</label>
				<div class="filter-content">
					<select class="form-control" id="classIdSearch" onChange="searchList()">
						
					</select>
				</div>
			</div>	
			<div class="filter-item" id='findDiv'>	
				<a href="javascript:" class="btn btn-blue" onclick="searchList()">查找</a>
				<a href="javascript:" class="btn btn-blue" onclick="searchImport()" id="importBTN">导入</a>
			</div>
     	</div>
    </div>
	<div class="box-body tabDiv">	
	</div>
</div>
</div>
<script>
$(function(){
	//初始化单选控件
	initChosenOne(".header_filter");
	updateClass();
	
	$('#acadyearSearch,#semesterSearch').on('change',function(){
		changeAS();
	});
	
	$('#courseId').on('change',function(){
		updateClass();
	});
	
});

function changeAS(){
	var acadyearSearch = $("#acadyearSearch").val();
	var semesterSearch = $("#semesterSearch").val();
	var url = '${request.contextPath}/scoremanage/optionalScore/index/page?acadyearSearch='+acadyearSearch+'&semesterSearch='+semesterSearch+'&noLimit=${noLimit!}';
	$('#myTestDiv').load(url);
}
function updateClass(){
	var acadyearSearch = $("#acadyearSearch").val();
	var semesterSearch = $("#semesterSearch").val();
	var courseId = $("#courseId").val();
	
	var url = '${request.contextPath}/scoremanage/optionalScore/updateClass';
	var params = {"unitId":"${unitId!}","acadyear":acadyearSearch,"semester":semesterSearch,"courseId":courseId,"noLimit":"${noLimit!}"};
	
	//alert('updateClass='+params);
	$.ajax({
		url:url,
		data:params,
		type:"POST",
		dataType:"JSON",
		success:function(data){
			//alert('data = '+data);
			//alert('data.success = '+data.success);
			
			if(data.success){
				//alert(1);
				var teachClassArray = data.businessValue;
				$("#classIdSearch").html("");
				for(var i=0;i<teachClassArray.length;i++){
					$("#classIdSearch").append('<option value="'+teachClassArray[i].id+'">'+teachClassArray[i].name+'</option>');
				}
			}else{
				$("#classIdSearch").html('<option value="">'+data.message+'</option>');
			}
			//searchList();
			$('#classIdSearch').change();
		}
	}); 
}
function searchList(){
	$('#findDiv').attr("style","display:block;");
	var noLimit = $('#noLimit').val();
	var acadyearSearch = $("#acadyearSearch").val();
	var semesterSearch = $("#semesterSearch").val();
	var courseId = $("#courseId").val();
	var classIdSearch = $("#classIdSearch").val();
	
	//alert('classIdSearch = '+classIdSearch);
	if(classIdSearch == ''){
		$(".tabDiv").html("暂无数据");
		return;
	}
	var url = '${request.contextPath}/scoremanage/optionalScore/tablist';
	var params = {acadyear:acadyearSearch,semester:semesterSearch,subjectId:courseId,
		teachClassId:classIdSearch,unitId:"${unitId!}",noLimit:noLimit};
	$.ajax({
		url:url,
		data:params,
		type:"POST",
		dataType:"HTML",
		success:function(data){
			$(".tabDiv").html(data);
		}
	}); 
}
function importToEdit(){
	$('#findDiv').attr("style","display:block;");
	//将原来的  导入功能 变成录入
	//$("#classIdSearch").attr("onChange","searchList();");
	$("#classIdSearch").change();
}
function searchImport(){
	var classIdSearch=$("#classIdSearch").val();
	var islock = $("#mysubmit").length;
	
	//检验是否有教学班
	if(classIdSearch == ''){
		importToEdit();
		layer.tips("班级不能为空", $("#classIdSearch"), {
				tipsMore: true,
				tips:3		
			});
		return;
	}
	
	var printResultLen = $('#printResult').length;
	if(islock<1 && printResultLen <1){
		layerTipMsg(false,"失败","您没有这门课的导入权限");
		return;
	}
	if(islock<1){
		layerTipMsg(false,"失败","成绩已提交，不能再导入");
		return;
	}
	
	$('#findDiv').attr("style","display:none;");
	//将原来的  录入功能 变成导入
	//$("#classIdSearch").attr("onChange","searchImport();");
	
	var subjectId = $("#courseId").val();
	var str = '?unitId=${unitId!}&subjectId='+subjectId+'&teachClassId='+classIdSearch;
	var url='${request.contextPath}/scoremanage/optionalScore/import/index'+str;
	url=encodeURI(encodeURI(url));
	$(".tabDiv").load(url);
}
</script>