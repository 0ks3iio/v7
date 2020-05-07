<div class="index">
	<div class="box box-structure">
		<div class="box-header clearfix">
			<div class="edit-name edit-name-32">
				<span>${multiReport.name!}</span>
			</div>
			<div class="float-right">
				<button class="btn btn-lightblue btn-fang js-add-data" onclick="componentSet('${reportId!}');"><i class="wpfont icon-plus"></i></button>
			</div>
		</div>
		<div class="box-body body-scroll-32">
			<div class="row no-padding kanban-wrap height-1of1 scrollBar4">
				<#if reportDetailList?exists&&reportDetailList?size gt 0>
				<#list reportDetailList as component>
					<div class="part-box ${component.width!}"  id="board-detail-${component.id!}"></div>
				</#list>
				</#if>
			</div>
		</div>
		<div class="layer layer-default-height add-data clearfix" id="boardComponentEditDiv"></div>
	</div>
</div>
<#if reportDetailList?exists&&reportDetailList?size gt 0>
<#else>
<div class="no-data">
	<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data.png"/>
	<div>还未添加组件，点击右上角+添加组件</div>
</div>
</#if>
<script type="text/javascript">
		function componentSet(reportId,componentId){
			if(!componentId)
				componentId="";
			 libraryIndex =layer.open({
        		type: 1,
                title: '添加(编辑)组件',
                area: ['750px', '600px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
	        		addComponent2Report();
	        	},
                content: $('#boardComponentEditDiv')
        	});
			$("#boardComponentEditDiv").empty().load("${request.contextPath}/bigdata/multireport/component/set?type=6&reportId="+reportId+"&componentId="+componentId);
			return false;
		}
		
		function deleteComponent(id,name){
			showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			 	$.ajax({
			            url:"${request.contextPath}/bigdata/multireport/component/delete",
			            data:{
			              'componentId':id
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
	         					$('.page-content').load('${request.contextPath}/bigdata/multireport/design?type=6&reportId=${reportId!}'); 
			    			}
			          },
			          error:function(XMLHttpRequest, textStatus, errorThrown){}
			    });
			});
		}
    	
    	$(document).ready(function(){
			<#if reportDetailList?exists&&reportDetailList?size gt 0>
				<#list reportDetailList as component>
					  $("#board-detail-${component.id!}").load("${request.contextPath}/bigdata/multireport/component/detail?type=6&componentId=${component.id!}");		
				</#list>
			</#if>
		});
</script>