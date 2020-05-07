<form id="subForm" method="post">
<div class="layer-content" id="myDiv">
	<div class="filter">
	   <div class="filter-item block">
			<span class="filter-name">复制学期：</span>
			<div class="filter-content">
				<select vtype="selectOne" class="form-control" id="semester2" name="semester2" onChange="">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
				</select>
			</div>
		</div>
		<div class="filter-item block" style="width:480px; height:179px; overflow:auto;">
			<span class="filter-name">复制学年：</span>
			<div class="filter-content">
				<table class="table table-bordered no-margin">
					<tbody>
					<#if acadyearList?? && (acadyearList?size>0)>
						<#list acadyearList as item>
							<tr>
							    <td><label><input type="checkbox" class="wp checked-input" value="${item!}"><span class="lbl">${item!}学年</span></label></td>
						    </tr>
						</#list>
					<#else>
						    <tr>
						        <td>暂无学年</td>
						    <tr>
					</#if>
					</tbody>	
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
    var acadyear = '';
    $(".checked-input").each(function(){
  		if($(this).is(':checked')){
  			   if(acadyear==''){
  				  acadyear = $(this).val();
  			   }else{
  				  acadyear+=','+$(this).val();
  			   }
  		    }
	});
    if(acadyear == ''){
        layerTipMsg(false,"","请选择要复制的学年！");
		return;
    }
    if(acadyear.length>9){
        layerTipMsg(false,"","只能选择一个学年！");
		return;
    }
    if(acadyear=='${acadyear!}' && semester == '${semester!}'){
        layerTipMsg(false,"","所选学年学期和本学期重复，请重新选择！");
		return;
    }
	var options = {
		url : "${request.contextPath}/studevelop/project/doCopy?acadyear="+acadyear+"&semester="+semester+"&gradeId=${gradeId!}&yAcadyear=${acadyear}&ySemester=${semester!}",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
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