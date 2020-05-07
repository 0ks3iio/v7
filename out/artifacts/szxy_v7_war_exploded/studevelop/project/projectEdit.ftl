<form id="subForm" method="post">
<#if stuDevelopProject?exists && stuDevelopProject.state?default('') != ''>
    <input type="hidden" name="acadyear" value="${stuDevelopProject.acadyear!}">
    <input type="hidden" name="semester" value="${stuDevelopProject.semester!}">
    <input type="hidden" name="gradeId" value="${stuDevelopProject.gradeId!}">
    <input type="hidden" name="creationTime" value="${stuDevelopProject.creationTime!}">
    <input type="hidden" name="id" value="${stuDevelopProject.id!}">
<#else>
     <input type="hidden" name="acadyear" value="${acadyear!}">
     <input type="hidden" name="semester" value="${semester!}">
     <input type="hidden" name="gradeId" value="${gradeId!}">
</#if>
    <input type="hidden" name="unitId" value="${unitId!}">
	<div class="layer-content" id="myDiv">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">项目</label>
				<div class="col-sm-9">
					<input type="text" class="form-control" id="projectName" name="projectName" value="${stuDevelopProject.projectName!}" maxLength="225" nullable="false">
				</div>
			</div>
			<div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">显示对象</label>
            <div class="col-sm-9">
                <select name="state" id="state" class="form-control" nullable="false" onChange="">
                    <option value="">--请选择--</option>
                    <option value="1" <#if stuDevelopProject?exists && stuDevelopProject.state?default('') == '1'>selected</#if>>仅对学科类别显示</option>
                    <option value="2" <#if stuDevelopProject?exists && stuDevelopProject.state?default('') == '2'>selected</#if>>仅对学科显示</option>
                </select>
            </div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">维护方式</label>
				<div class="col-sm-9 col-sm-offset-1">

                    <label><input type="radio" name="useVersion" class="wp" value=1><span class="lbl" >单选</span></label>
                    <label><input type="radio" name="useVersion" class="wp"  value=2><span class="lbl" >输入</span></label>

				</div>
			</div>
                <div class="form-group">
                    <span class="col-sm-2 control-label no-padding-right">单选内容：</span>
                    <div class="col-sm-9">
                        <table class="table table-bordered no-margin">
                            <tbody id="addHtml">
                            <tr>
                                <td><input type="text" class="form-control" style="width:160px;" name="stuDevelopCateGoryList[0].categoryName" id="categoryName0"></td>

                            </tr>
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
                        <a href="javascript:void(0);" id="js-add">新增单选内容</a>
                    </div>

                </div>

        </div>
		</div>				
	</div>
</form>
<script>
var isSubmit=false;
function saveProject(){
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/studevelop/project/save",
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
}
</script>
