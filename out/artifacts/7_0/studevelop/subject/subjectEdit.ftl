<form id="subForm" method="post">
<div class="layer-content" id="myDiv">
<#if stuDevelopSubject?exists && stuDevelopSubject.name?default('') != ''>
    <input type="hidden" name="stuDevelopSubject.acadyear" value="${stuDevelopSubject.acadyear!}">
    <input type="hidden" name="stuDevelopSubject.semester" value="${stuDevelopSubject.semester!}">
    <input type="hidden" name="stuDevelopSubject.gradeId" value="${stuDevelopSubject.gradeId!}">
    <input type="hidden" name="stuDevelopSubject.creationTime" value="${stuDevelopSubject.creationTime!}">
    <input type="hidden" name="stuDevelopSubject.id" value="${stuDevelopSubject.id!}">
<#else>
     <input type="hidden" name="stuDevelopSubject.acadyear" value="${acadyear!}">
     <input type="hidden" name="stuDevelopSubject.semester" value="${semester!}">
     <input type="hidden" name="stuDevelopSubject.gradeId" value="${gradeId!}">
</#if>
    <input type="hidden" name="stuDevelopSubject.unitId" value="${unitId!}">
	<div class="filter">
		<div class="filter-item block">
			<span class="filter-name">学科名称：</span>
			<div class="filter-content">
				<input type="text" class="form-control" style="width:160px;" name="stuDevelopSubject.name" id="name" value="${stuDevelopSubject.name!}" maxLength="225" nullable="false">
			</div>
		</div>
		<div class="filter-item block" style="width:480px; height:179px; overflow:auto;">
			<span class="filter-name">学科类别：</span>
			<div class="filter-content">
				<table class="table table-bordered no-margin">
					<tbody id="addHtml">
						<#-- tr>
							<td><input type="text" class="form-control" style="width:160px;" name="stuDevelopCateGoryList[0].categoryName" id="categoryName0"></td>
							<td><label><input type="checkbox" class="wp" onClick="changel(this);" name="stuDevelopCateGoryList[0].state" id="state0"><span class="lbl"> 是否只取一个成绩</span></label></td>
							<td></td>
						</tr-->
						<#if stuDevelopCateGoryList?exists && stuDevelopCateGoryList?size gt 0>
						    <#list stuDevelopCateGoryList as item>
						        <tr>
						            <input type="hidden" name="stuDevelopCateGoryList[${item_index!}].id" value="${item.id!}">
						            <input type="hidden" name="stuDevelopCateGoryList[${item_index!}].creationTime" value="${item.creationTime!}">
                                    <td><input type="text" class="form-control" style="width:160px;" name="stuDevelopCateGoryList[${item_index!}].categoryName" id="categoryName${item_index!}" value="${item.categoryName!}" maxLength="225" nullable="false"></td>
                                    <#--td><label><input type="checkbox" class="wp" <#if item.state == '1'>checked</#if> onClick="changel(this);" name="stuDevelopCateGoryList[${item_index!}].state" id="state${item_index!}" value="${item.state!}"><span class="lbl"> 是否只取一个成绩</span></label></td-->
                                    <td><a href="javascript:void(0)" class="color-red" onClick="trDelete(this)">删除</a></td>
                                </tr>
						    </#list>
						</#if>
					</tbody>
				</table>
				<a href="javascript:void(0);" id="js-add">新增学科类别</a>
			</div>
		</div>
	</div>
</div>
</form>
<script>
var index = 0;
<#if stuDevelopCateGoryList?exists && stuDevelopCateGoryList?size gt 0>
    index = ${stuDevelopCateGoryList?size!};
</#if>
$(function(){
     $('#js-add').on('click',function(e){    
          e.preventDefault();
          addCategory();
          index++;
     });
});

function trDelete(obj){
    $(obj).parents("tr").remove();
}

function changel(obj){
   if($(obj).val() == '' || $(obj).val() == 'on' || $(obj).val() == '0'){
      $(obj).attr("value",'1');
   }else{
      $(obj).attr("value",'0');
   }
}

function addCategory(){
    var addhtml = '<tr>\
                        <td><input type="text" class="form-control" style="width:160px;" name="stuDevelopCateGoryList['+index+'].categoryName" id="categoryName'+index+'" maxLength="225" nullable="false"></td>\
                        <#--td><label><input type="checkbox" class="wp" onClick="changel(this);" name="stuDevelopCateGoryList['+index+'].state" id="state'+index+'"><span class="lbl"> 是否只取一个成绩</span></label></td>\-->
                        <td><a href="javascript:void(0)" class="color-red" onClick="trDelete(this)">删除</a></td>\
                   </tr>';
    $('#addHtml').append(addhtml);           
}

var isSubmit=false;
function saveSubject(){
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/studevelop/subject/save",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
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
}
</script>