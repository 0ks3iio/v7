<#if groupList?exists&&groupList?size gt 0>
	<#list groupList as group>
		<div id="div${group.id!}" class="filter-item overview-box" >
	    	<span onclick="loadFavoriteData('${group.id!}')">${group.groupName!}</span>
	    	<div class="pos-right-c">
	    		<img class="js-edit" src="${request.contextPath}/static/bigdata/images/editss.png" onclick="editGroup('${group.id!}')" />
	    		<img class="js-remove" id="${group.id!}-delete" src="${request.contextPath}/static/bigdata/images/delete-grey.png" alt="" onclick="deleteGroupId('${group.id!}','${group.groupName!}')" />
			</div>
		</div>
	 </#list>
 </#if>
<#if groupList?exists&&groupList?size lt 8>
<div class="filter-item js-add-overview" style="font-size: 30px;color: #ccc;" onclick="editGroup();"><span>+</span></div>
</#if>
<script type="text/javascript">

    	function editGroup(groupId){
        	$.ajax({
	            url: '${request.contextPath}/bigdata/event/dashboard/group/edit',
	            type: 'POST',
	            data: {groupId:groupId},
	            dataType: 'html',
	            beforeSend: function(){
			      	$('#eventGroupEditDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('#eventGroupEditDiv').html(response);
	            }
            });
        	var addGroupSubmit=false;
			layer.open({
        		type: 1,
                shade: .6,
                title: '设置组',
                btn: ['确定','取消'],
                yes:function(index, layero){
               		if(addGroupSubmit){
               			layer.msg("请不要重复保存", {offset: 't',time: 2000});
						reurn;
					}
			   		addGroupSubmit = true;
   		       		if($('#groupName').val()== '') {
                    	layer.tips("概览组名称不能为空!", "#groupName", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                   		addGroupSubmit = false;
                    	return;
                	}
                	if($('#orderId').val()== '') {
                    	layer.tips("排序号不能为空!", "#orderId", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                   		addGroupSubmit = false;
                    	return;
                	}
					var  options = {
						url : "${request.contextPath}/bigdata/event/dashboard/group/save",
						dataType : 'json',
						success : function(data){
						 	if(!data.success){
						 		showLayerTips('error',data.msg,'t');
						 		addGroupSubmit = false;
						 	}else{
						 		layer.close(index);
						 		showLayerTips('success',data.msg,'t');
						 		loadGroupList();
				    		}
						},
						type:"post",
						clearForm : false,
						resetForm : false,
						error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
					 };
						$("#eventGroupSubmitForm").ajaxSubmit(options);
            	},
            	 end:function(){
	               	$('#eventGroupEditDiv').empty();
	             },
                area: ['400px','300px'],
                content: $('#eventGroupEditDiv')
        	});
    	}
    	
		function loadFavoriteData(groupId){
			if(groupId==""){
				$('#cotentDiv').load("${request.contextPath}/bigdata/event/dashboard/group/list/nodata");
			}else{
				$('#div'+groupId).addClass('active').siblings('.overview-box').removeClass('active');
				$('#cotentDiv').load("${request.contextPath}/bigdata/event/dashboard/favorite/list?groupId="+groupId);
				$("#currentGroupId").val(groupId);
			}
		}
		
		function deleteGroupId(id,name){
			 showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
				 $.ajax({
			            url:"${request.contextPath}/bigdata/event/dashboard/group/delete",
			            data:{
			              'groupId':id
			            },
			            type:"post",
			            clearForm : false,
						resetForm : false,
			            dataType: "json",
			            success:function(data){
			          	    layer.closeAll();
					 		if(!data.success){
					 			showLayerTips('error',data.msg,'t');
					 		}else{
					 		     showLayerTips('success',data.msg,'t');
					 		     var currentGroupId=$("#currentGroupId").val();
					 		     if(currentGroupId ==id){
					 		     	$("#currentGroupId").val('');
					 		     }
					 		    loadGroupList();
			    			}
			          },
			          error:function(XMLHttpRequest, textStatus, errorThrown){}
			    });
			},function(){
		
			});
		}
    	
    	$(document).ready(function(){	
		   	var currentGroupId=$("#currentGroupId").val();
			<#if groupList?exists&&groupList?size gt 0>
					if(currentGroupId !=""){
	   					loadFavoriteData(currentGroupId);
	   				}else{
						<#list groupList as group>
							<#if group_index == 0>
								loadFavoriteData('${group.id!}');
							</#if>
				 		</#list>	
					}
		 	<#else>
					loadFavoriteData('');
			 </#if>
		});
</script>