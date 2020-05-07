<script type="text/javascript" src="${request.contextPath}/bigdata/v3/static/plugs/wangEditor/wangEditor.min.js"></script>
<div class="index">
	<div class="box box-structure box-structure-inner">
		<div class="box-header clearfix">
			<div class="edit-name edit-name-32">
				<span>${multiReport.name!}</span>
			</div>
		</div>
		<div class="box-body body-scroll-32 scrollBar4">
			<div class="box-default">
				<div class="box-body">
					<div class="row no-padding kanban-wrap border report-set-wrap">
							<#if reportDetailList?exists&&reportDetailList?size gt 0>
							<#list reportDetailList as component>
								<div class="part-box col-md-12"  id="report-detail-${component.id!}"></div>
							</#list>
							</#if>
					</div>
					<div class="report-part-default">
						<div class="">
							<p class="color-999">请添加报告组件</p>
							<ul class="clearfix">
								<li class="js-add-text" onclick="addRichText2Report();return false;"><i class="iconfont icon-text-fill"></i></li>
								<li class="js-add-data" onclick="reportComponentSet('chart','${reportId!}');return false;" ><i class="iconfont icon-sjtb-fill"></i></li>
								<li class="js-add-data" onclick="reportComponentSet('report','${reportId!}');return false;"><i class="iconfont icon-sjbb-fill"></i></li>
								<li class="js-add-data" onclick="reportComponentSet('multimodel','${reportId!}');return false;"><i class="iconfont icon-dwbb-fill"></i></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="layer layer-default-height add-data clearfix" id="reportComponentEditDiv"></div>
	</div>
</div>
<script type="text/javascript">
    var E = window.wangEditor;
    var editorMap=new Map();
    var wangEditorZindex=10000;
	
	function addRichText2Report(){
		 $.ajax({
		            url:"${request.contextPath}/bigdata/data/analyse/component/richText/new",
		            data:{
		              'reportId':'${reportId!}'		           
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
	         				$('.page-content').load('${request.contextPath}/bigdata/data/analyse/design?type=7&reportId=${reportId!}');
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
	}
	
	function reportComponentSet(businessType,reportId,componentId){
		var obj=$('#reportComponentEditDiv');
		var area=['450px', '600px'];
		if(!componentId)
			componentId="";
		layer.open({
			type: 1,
	        title: '添加(编辑)组件',
	        area: area,
	        btn: ['确定', '取消'],
	        yes: function (index, layero) {
	        	addComponent2Report();
	        },
	        content: obj
		});
		obj.load("${request.contextPath}/bigdata/data/analyse/component/set?type=7&reportId="+reportId+"&componentId="+componentId+"&businessType="+businessType);
		return false;
	}

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
			$("#boardComponentEditDiv").empty().load("${request.contextPath}/bigdata/data/analyse/component/set?type=7&reportId="+reportId+"&componentId="+componentId);
			return false;
		}
	
	function deleteComponent(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			 $.ajax({
		            url:"${request.contextPath}/bigdata/data/analyse/component/delete",
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
				 		    $('.page-content').load('${request.contextPath}/bigdata/data/analyse/design?type=7&reportId=${reportId!}');
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}
	
	$(document).ready(function(){
		<#if reportDetailList?exists&&reportDetailList?size gt 0>
			<#list reportDetailList as component>
				  wangEditorZindex--;
				  $("#report-detail-${component.id!}").load("${request.contextPath}/bigdata/data/analyse/component/detail?type=7&componentId=${component.id!}&zIndex="+wangEditorZindex);
			</#list>
		</#if>
	});
</script>