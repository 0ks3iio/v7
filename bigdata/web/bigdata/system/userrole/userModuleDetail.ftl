<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="col-sm-3 col-lg-3">
	<div class="widget-box widget-color-blue2">
		<div class="widget-header">
		</div>
		<div class="widget-body">
			<div class="widget-main padding-8">
				<ul id="permissionTree" class="ztree"></ul>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	
	function reloadTree(){
		var setting = {
			check:{
				enable:false
			},
			data: {				
				simpleData: {
					enable: true
				}
			}
		};
		$.ajax({
			url:"${request.contextPath}/bigdata/authority/moduleZtree?userId=${userId!}&userType=${userType!}",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			$.fn.zTree.init($("#permissionTree"), setting, JSON.parse(jsonO.msg));
		 		}
			}
		});
	}
	
	reloadTree();
</script>