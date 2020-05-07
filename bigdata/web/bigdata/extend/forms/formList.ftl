<#import  "/bigdata/v3/templates/commonWebMacro.ftl" as cwm />
<div class="box box-structure">
	<div class="box-header clearfix">
	    <div class="form-group search">
	        <div class="input-group">
	            <input type="text" id="name_search" class="form-control" value="${formName!}"
	                   placeholder="输入表单名称查询">
	            <a href="javascript:void(0);" onclick="searchForm()" class="input-group-addon" hidefocus="true"><i class="wpfont icon-search"></i></a>
	        </div>
	    </div>
	    <div class="form-group clearfix">
	        <button class="btn btn-lightblue js-add-kanban" onclick="editFormSet('')">新增表单</button>
	    </div>
	</div>
	<div class="box-body">
	<#if forms?exists && forms?size gt 0>
	    <table class="tables">
	        <thead>
	        <tr>
	            <th>名称</th>
	            <th>元数据</th>
	            <th>备注</th>
	            <th>操作</th>
	        </tr>
	        </thead>
	        <tbody class="kanban-content">
	        <#list forms as form>
	            <tr>
	                <td title="${form.name!}">
	                    <div style="width: 150px;" class="ellipsis">
	                        ${form.name!}
	                    </div>
	                </td>
	                <td>${form.metaName!}</td>
	                <td title="${form.remark!}">
	                    <div style="width: 150px;" class="ellipsis">
	                        ${form.remark!}
	                    </div>
	                </td>
	                <td>
	                    <a href="javascript:void(0)" onclick="editFormSet('${form.id!}')"  class="look-over">编辑</a><span class="tables-line">|</span>
	                    <a href="javascript:void(0)" onclick="deleteForm('${form.id!}','${form.name!}');">删除</a>
	                </td>
	            </tr>
	        </#list>
	        </tbody>
	    </table>
	     <@cwm.pageToolBar container=".page-content" class="text-right"/>
	<#else>
	    <div class="no-data-word">
	        <img src="${request.contextPath}/bigdata/v3/static/images/kanban-design/img-focus.png"/>&nbsp;&nbsp;暂无记录，请<span
	            class="js-add-kanban color-00cce3 pointer" onclick="editFormSet('')">&nbsp;新增表单</span>
	    </div>
	</#if>
	</div>
</div>
<script>
function searchForm() {
    var url = '${request.contextPath}/bigdata/metadata/forms?formName=' + $('#name_search').val();
    $('.page-content').load(url);
}

function editFormSet(id) {
	router.go({
        path: '/bigdata/metadata/forms/edit?id='+id,
        name: '表单设置',
        level: 2
    }, function () {
		var url = '${request.contextPath}/bigdata/metadata/forms/edit?id='+id;
	    $('.page-content').load(url);
    });
}

function deleteForm(id,name){
    showConfirmTips('prompt',"提示","您确定要删除表单"+name+"吗？",function(){
        $.ajax({
            url:"${request.contextPath}/bigdata/metadata/forms/delete",
            data:{
                'id':id
            },
            type:"post",
            clearForm : false,
            resetForm : false,
            dataType: "json",
            success:function(result){
                if(!result.success){
                    showLayerTips4Confirm('error',result.message);
                }else{
                    showLayerTips('success',result.message,'t');
                    $('.page-content').load('${request.contextPath}/bigdata/metadata/forms');
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });
}
</script>