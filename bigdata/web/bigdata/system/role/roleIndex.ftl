<div class="row no-margin height-1of1">
	<div class="col-md-3 height-1of1 no-padding-left">
		<div class="tree labels tree-item height-1of1">
			<p class="tree-name border-bottom-cfd2d4 no-margin">
                <b>用户组</b>
                <img src="${request.contextPath}/bigdata/v3/static/images/big-data/add.png" onclick="editRole('');"  class="pos-right-c js-add" alt=""/>
            </p>
            <div id="roleListDiv" class="type-choose tree-scroll-height js-click"></div>
		</div>
	</div>
	<div class="col-md-9 height-1of1 no-padding-right" id="roleDetailDiv"></div>
</div>
<div class="layer layer-form layer-role-edit" id="roleEditDiv"></div>
<input type="hidden" id="currentRoleId" value="" />
<script type="text/javascript">
	function loadRoleData(){
		var currentRoleId =$("#currentRoleId").val();
		var url =  "${request.contextPath}/bigdata/role/roleList?roleId="+currentRoleId;
		$("#roleListDiv").load(url);
	}
	
	function loadRoleDetailData(roleId){
		$("#currentRoleId").val(roleId);
		var url =  "${request.contextPath}/bigdata/role/roleDetail?roleId="+roleId;
		$("#roleDetailDiv").load(url);
	}

	function editRole(id) {
    	    $.ajax({
	            url: '${request.contextPath}/bigdata/role/edit',
	            type: 'POST',
	            data: {
					id:id
				},
	            dataType: 'html',
	            beforeSend: function(){
			      	$('#roleEditDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('#roleEditDiv').html(response);
	            }
            });
            var roleSubmit = false;
            layer.open({
            	type: 1,
            	shade: .6,
            	title: '用户组编辑',
            	btn: ['保存','取消'],
            	yes:function(index, layero){
               		if(roleSubmit){
               			showLayerTips('warn','数据保存中,请不要重复点击','t');
						return;
					}
			  		roleSubmit = true;
   		       		if($('#name').val()== '') {
                    	layer.tips("用户组名称不能为空!", "#name", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                    	roleSubmit = false;
                    	return;
                	}
                	
                	if($('#orderId').val()== '') {
                    	layer.tips("排序号不能为空!", "#orderId", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                    	roleSubmit = false;
                    	return;
                	}

					var  options = {
					url : "${request.contextPath}/bigdata/role/save",
					dataType : 'json',
					success : function(data){
					 	if(!data.success){
					 		showLayerTips4Confirm('error',data.msg);
					 		roleSubmit = false;
					 	}else{
					 		layer.close(index);
					 		showLayerTips('success','保存成功','t');
					 		$("#currentRoleId").val(data.businessValue);
					 		loadRoleData();
			    		}
					},
					clearForm : false,
					resetForm : false,
					type : 'post',
					error:function(XMLHttpRequest, textStatus, errorThrown){
					roleSubmit=false;
					}//请求出错 
				};
				$("#roleSubmitForm").ajaxSubmit(options);
            },
            end:function(){
               	$('#roleEditDiv').empty();
             },
            area: ['580px', '390px'],
            content: $('.layer-role-edit')
        });
	}
		
	function deleteRole(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			$.ajax({
		            url:'${request.contextPath}/bigdata/role/delete',
		            data:{
		              'id':id
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
						layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 			showLayerTips('success',data.msg,'t');
				 			$("#currentRoleId").val('');
						  	loadRoleData();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}

	$(document).ready(function(){
		  loadRoleData();
	});
</script>