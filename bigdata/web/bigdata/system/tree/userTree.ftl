<link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/js/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js"></script>
<div class="form-horizontal">
	<div class="form-group no-margin">
		<div class="col-sm-4">
			<div class="bs-callout bs-callout-danger">
                <ul class="nav nav-tabs">
					<li class="active">
					    <a href="javascript:void(0);" data-toggle="tab">下级单位</a>
					</li>
				</ul>
				<div class="tab-content tree-wrap tree-tab width-1of1 scrollBar4">
					<div class="row no-margin">
						<div class="filter-item col-sm-8 no-margin-right">
					        <div class="input-group">
			                    <input type="text" id="unit_tree_search_keywords"  class="form-control" />
			                    <a href="javascript:void(0);" onClick="unitTreeSearch();return false;" class="input-group-addon"><img src="${request.contextPath}/bigdata/v3/static/images/icon/search_gray_16x16.png"/></a>
			                </div>
					    </div>
					</div>
					<ul id="unitTree" class="ztree"></ul>
				</div>
            </div>							
		</div>
		
		<div class="col-sm-4">
			<div class="bs-callout bs-callout-danger">
                <ul class="nav nav-tabs">
					<li class="active">
					    <a href="javascript:void(0);" data-toggle="tab">部门用户</a>
					</li>
				</ul>
				<div class="tab-content tree-wrap tree-tab width-1of1 scrollBar4">
					<div class="row no-margin">
						<div class="filter-item col-sm-8 no-margin-right">
					        <div class="input-group">
			                    <input type="text" id="user_tree_search_keywords"  class="form-control" />
			                    <a href="javascript:void(0);" onClick="userTreeSearch();return false;" class="input-group-addon"><img src="${request.contextPath}/bigdata/v3/static/images/icon/search_gray_16x16.png"/></a>
			                </div>
					    </div>
					</div>
					<ul id="userTree" class="ztree"></ul>
				</div>
            </div>							
		</div>
		
		<div class="col-sm-4">
			<div class="bs-callout bs-callout-danger">
				<p class="choose-num">已选（<span>0</span>）</p>
				<div class="choose-item scrollBar4">
					<div class="no-data">
						<img src="${request.contextPath}/bigdata/v3/static/images/big-data/no-choice.png"/>
					</div>
				</div>
			</div>	
		</div>
	</div>
</div>
<script>
	var num=0;
	var zTreeSelectedUserIdMap = new Map();
	var unitTree,userTree ;
	$('.tree-wrap').height($('.layer-power').height());
	$('.choose-item').height($('.tree-wrap').height() + 10);
	$('.no-data').css('line-height',($('.choose-item').height()-2)+'px');
	
	//已选
	$('.choose-item').on('click','.fa-times-circle',function(){
		var selectedNodeId=$(this).parent().attr("id");
		if(userTree){
			var selectedNode=userTree.getNodeByParam('id',selectedNodeId);
			if(selectedNode)
				userTree.checkNode(selectedNode,false,false,false);
		}
		zTreeSelectedUserIdMap.delete(selectedNodeId);
		$(this).parent().remove();
		num--;
		$('.choose-num span').text(num);
		if(num==0){
			$('.no-data').show()
		}
	});
	  	
	function unitTreeSearch(){
        var keywords = $('#unit_tree_search_keywords').val();
        var matchKeywordsTreeNodeArray = unitTree.getNodesByParamFuzzy('name', keywords);
        //匹配
        if (matchKeywordsTreeNodeArray.length > 0) {
            unitTree.selectNode(matchKeywordsTreeNodeArray[0]);
        }
    }
    
    function userTreeSearch(){
        var keywords = $('#user_tree_search_keywords').val();
        var matchKeywordsTreeNodeArray = userTree.getNodesByParamFuzzy('name', keywords);
        //匹配
        if (matchKeywordsTreeNodeArray.length > 0) {
            userTree.selectNode(matchKeywordsTreeNodeArray[0]);
        }
    }
	    
    function loadUnitTree(){
		var setting = {
			 view: {
	            selectedMulti: false
	        },
			check:{
				enable:false
			},
			data: {				
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onUnitTreeClick
			}
		};
		$.ajax({
			url:"${request.contextPath}/bigdata/common/tree/unitTree",
			beforeSend: function(){
		      	$('#unitTree').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
		    },
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			unitTree=$.fn.zTree.init($("#unitTree"), setting, JSON.parse(jsonO.msg));
		 		}
			}
		});
	}
		
	function loadUserTree(unitId){
		var setting = {
			 view: {
	            selectedMulti: false
	        },
			check:{
				enable:true,
				autoCheckTrigger: true
			},
			data: {				
				simpleData: {
					enable: true
				}
			},
			callback: {
				onCheck:onUserTreeCheck
			}
		};
		$.ajax({
			url:"${request.contextPath}/bigdata/common/tree/userTreeByUnit?unitId="+unitId,
			beforeSend: function(){
		      	$('#userTree').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
		    },
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			userTree=$.fn.zTree.init($("#userTree"), setting, JSON.parse(jsonO.msg));
		 			var parentNodes = userTree.getNodes(); 
		 			var allNodes = userTree.transformToArray(parentNodes);
		            //遍历所有节点
					allNodes.forEach(function(e){  
					   if(zTreeSelectedUserIdMap.has(e.id)){
	                        userTree.checkNode(e, true, true);
	                	}
					});
		 		}
			}
		});
	}
		
	function onUnitTreeClick(event, treeId, treeNode, clickFlag){
		loadUserTree(treeNode.id);
	}

	function onUserTreeCheck(event, treeId, treeNode){
		if(treeNode.type == "dept"){
			return;
		}else{
			if(treeNode.checked){
				if($('#' + treeNode.id).length > 0) {
					return;
				}
				zTreeSelectedUserIdMap.set(treeNode.id,treeNode.name);
				var str='<div id="'+treeNode.id+'">'+treeNode.name+'<i class="fa fa-times-circle"></i></div>';
	   			$('.choose-item').append(str);
	   			num++;
			}else{
				if($('#' + treeNode.id).length == 0) {
					return;
				}
			     $('#' + treeNode.id).remove();
			     zTreeSelectedUserIdMap.delete(treeNode.id);
	              num--;
			}
			$('.choose-num span').text(num);
			if(num==0){
				$('.no-data').show()
			}else{
				$('.no-data').hide();
			}
		}		
	} 
	
	function loadOldUsers(){
		 var jsondata =JSON.parse('${users!}');
		 for (var i = 0; i < jsondata.length; i++){
		 	var userId=jsondata[i].userId;
		 	var userName=jsondata[i].userName;
		 	zTreeSelectedUserIdMap.set(userId,userName);
		 	var str='<div id="'+userId+'">'+userName+'<i class="fa fa-times-circle"></i></div>';
   			$('.choose-item').append(str);
   			num++;
		 }
		 $('.choose-num span').text(num);
		 if(num>0)
		 	$('.no-data').hide();
	}
	    
     $(document).ready(function(){
			loadOldUsers();
			loadUnitTree();
    });
	    
</script>