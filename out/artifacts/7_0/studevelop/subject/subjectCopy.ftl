<form id="subForm" method="post">
<div class="layer-content" id="myDiv">
	<div class="filter">
	   <div class="filter-item block">
			<span class="filter-name">学年：</span>
			<div class="filter-content">
				<select vtype="selectOne" class="form-control" name="acadyear2" id="acadyear2" onChange="searchList();">
					<#if acadyearList?? && (acadyearList?size>0)>
						<#list acadyearList as item>
							<option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}</option>
						</#list>
					<#else>
						<option value="">暂无数据</option>
					</#if>
				</select>
			</div>
		</div>
	   <div class="filter-item block">
			<span class="filter-name">学期：</span>
			<div class="filter-content">
				<select vtype="selectOne" class="form-control" id="semester2" name="semester2" onChange="">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
				</select>
			</div>
		</div>
		<div class="filter-item block">
			<span class="filter-name">年级：</span>
			<div class="filter-content">
				<select name="" id="gradeId2" class="form-control" onChange="searchList();">
					<#if gradeList?exists && gradeList?size gt 0>
					    <#list gradeList as item>
					        <option value="${item.id!}">${item.gradeName!}</option>
					    </#list>
					</#if>
			    </select>
			</div>
		</div>
	</div>
</div>
</form>
<script>
var isSubmit=false;
function doCopy(){
showConfirmMsg('复制后将会覆盖本学年学期数据，确认复制？','提示',function(){
   var check = checkValue('#myDiv');
   var semester = $('#semester2').val();
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var acadyear = $('#acadyear2').val();
    var gradeId = $('#gradeId2').val();
    if(acadyear=='${acadyear!}' && semester == '${semester!}' && gradeId == '${gradeId!}'){
        layerTipMsg(false,"","所选学年学期和本学期重复，请重新选择！");
		return;
    }
	var options = {
		url : "${request.contextPath}/studevelop/subject/doCopy?acadyear="+acadyear+"&semester="+semester+"&yGradeId=${gradeId!}&yAcadyear=${acadyear}&ySemester=${semester!}",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		//$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
				searchList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
	});
}
</script>