<div class="filter-made mb-10">
	<div class="filter-item">
		 <span class="filter-name">所属模块：</span>
		<div class="form-group">
            <select id="moduleId" class="form-control" name="moduleId" onChange="moduleChange(this.value);">
            	<option value="">全部</option>
      			<#list moduleList as module>
					<#if module.type! !="dir">
	                	<option value="${module.id!}" <#if moduleId! == module.id!>selected="selected"</#if>>${module.name!}</option>
					</#if>
                </#list>           
        	</select>
        </div>
	</div>
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="addHelp();">新增帮助</button>
	</div>
</div>
<#if helpList?exists && helpList?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th>帮助名称</th>
		            <th>是否核心模块</th>
		            <th>所属模块</th>
		            <th>描述</th>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
		    	<#list helpList as help>
		    	<tr>
			         <td>${help.name!}</td>
			         <td><#if help.core! ==1>是<#else>否</#if></td>
			         <td>${help.moduleName!}</td>
			         <td title="${help.description!}"><#if help.description! !="" && help.description?length gt 30>${help.description?substring(0, 30)}......<#else>${help.description!}</#if></td>
			         <td>
			               <a href="javascript:void(0)" onclick="editHelp('${help.id!}')"  class="look-over">编辑</a><span class="tables-line">|</span>
			               <a href="javascript:void(0)" onclick="previewHelp('${help.id!}')"  class="look-over">预览</a><span class="tables-line">|</span>
			                <a href="javascript:void(0)" class="remove" onclick="deleteHelp('${help.id!}','${help.name!}');">删除</a>
			        </td>
			  	</tr>
			  	</#list>
		    </tbody>
		</table>
<#else>
<div class="no-data-common">
	<div class="text-center">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
		<p class="color-999">
			暂无帮助信息
		</p>
	</div>
</div>
</#if>
<script>
	function moduleChange(moduleId){
		$('.page-content').load('${request.contextPath}/bigdata/help/index?moduleId='+moduleId); 
	}

    function addHelp() {
		var moduleId = $("#moduleId").find("option:selected").val();
    	var url = '/bigdata/help/add?moduleId='+moduleId;
    	router.go({
	        path: url,
	        name: '新建帮助',
	        level: 2
	    }, function () {
	         $('.page-content').load('${request.contextPath}'+url);
	    });
    }

 	function editHelp(id) {
    	router.go({
	        path: '/bigdata/help/edit?id='+id,
	        name: '修改帮助',
	        level: 2
	    }, function () {
	    	var url = '${request.contextPath}/bigdata/help/edit?id='+id;
	         $('.page-content').load(url);
	    });
    }

	function previewHelp(id) {
		var url = '${request.contextPath}/bigdata/help/preview?id='+id;
	 	window.open(url,id)
    }

	function deleteHelp(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
		 	$.ajax({
		            url:"${request.contextPath}/bigdata/help/delete",
		            data:{
		              'id':id
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(data){
		            	layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 		    showLayerTips('success',data.message,'t');
         					$('.page-content').load('${request.contextPath}/bigdata/help/index'); 
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}
</script>