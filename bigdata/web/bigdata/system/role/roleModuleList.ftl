<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="col-sm-3 col-lg-3">
	<div class="widget-box widget-color-blue2">
		<div class="widget-header">
			<h4 class="widget-title lighter smaller">请选择</h4>
		</div>
		<div class="widget-body">
			<div class="widget-main padding-8">
				<ul id="permissionTree" class="ztree"></ul>
			</div>
		</div>
	</div>
	<div class="form-group">
	    <div class="col-sm-6 col-sm-offset-2" >
	        <button type="button" class="btn btn-long btn-blue js-added" onclick="saveRolePerm();">&nbsp;保存&nbsp;</button>
	    </div>
	    </div>
	</div>
</div>
<script type="text/javascript">
	function saveRolePerm(){
	    var treeObj=$.fn.zTree.getZTreeObj("permissionTree"),
	    nodes=treeObj.getCheckedNodes(true),
        moduleIds="";
        for(var i=0;i<nodes.length;i++){
        	moduleIds+=nodes[i].id + ",";
		}
		$.ajax({
	            url:'${request.contextPath}/bigdata/role/saveRolePerm',
	            data:{
	              'roleId':'${roleId!}',
	              'moduleIds':moduleIds
	            },
	            type:"post",
	            dataType: "json",
	            success:function(data){
	            	layer.closeAll();
			 		if(!data.success){
			 			showLayerTips4Confirm('error',data.msg);
			 		}else{
			 			showLayerTips('success',data.msg,'t');
	    			}
	          },
	          error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	}
	
	function reloadTree(){
		var setting = {
			check:{
				enable:true
			},
			data: {				
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onTreeClick
			}
		};
		$.ajax({
			url:"${request.contextPath}/bigdata/role/moduleZtree?roleId=${roleId!}",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			$.fn.zTree.init($("#permissionTree"), setting, JSON.parse(jsonO.msg));
		 		}
			}
		});
	}
	
	var currentTreeId;
	var currentSubsystemCode;
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "dir"){

			currentTreeId = "";
			currentSubsystemCode = treeNode.id;
		}
		else if(treeNode.type == "item"){
			currentTreeId = "";
		}
		else{
			currentTreeId = treeNode.id;
		}		
	}
	reloadTree();
</script>