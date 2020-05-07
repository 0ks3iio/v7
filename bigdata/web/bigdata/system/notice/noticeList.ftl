<div class="filter-made mb-10">
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="addNotice();">新增公告</button>
	</div>
</div>
<#if noticeList?exists && noticeList?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th>公告名称</th>
		            <th>公告类型</th>
		            <th>状态</th>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
		    	<#list noticeList as notice>
		    	<tr>
			        <td>${notice.title!}</td>
			         <td>${notice.noticeTypeName!}</td>
			         <td><#if notice.status! ==1>正常<#else>下架</#if></td>
			         <td>
			               <a href="javascript:void(0)" onclick="editNotice('${notice.id!}')"  class="look-over">编辑</a><span class="tables-line">|</span>
			               <a href="javascript:void(0)" onclick="previewNotice('${notice.id!}')"  class="look-over">预览</a><span class="tables-line">|</span>
			               <a href="javascript:void(0)" onclick="changeStatus('${notice.id!}',${notice.status!})"  class="look-over"><#if notice.status ==1>下架<#else>上架</#if></a><span class="tables-line">|</span>
			                <a href="javascript:void(0)" class="remove" onclick="deleteNotice('${notice.id!}','${notice.title!}');">删除</a>
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
			暂无公告记录
		</p>
	</div>
</div>
</#if>
<script>
    function addNotice() {
    	router.go({
	        path: '/bigdata/notice/add',
	        name: '新建公告',
	        level: 2
	    }, function () {
	    	var url = '${request.contextPath}/bigdata/notice/add';
	         $('.page-content').load(url);
	    });
    }

 	function editNotice(id) {
    	router.go({
	        path: '/bigdata/notice/edit?id='+id,
	        name: '修改公告',
	        level: 2
	    }, function () {
	    	var url = '${request.contextPath}/bigdata/notice/edit?id='+id;
	         $('.page-content').load(url);
	    });
    }

	function previewNotice(id) {
		var url = '${request.contextPath}/bigdata/notice/preview?id='+id;
	 	window.open(url,id)
    }

	function changeStatus(id,status){
		if(status==1)
			status=0;
		else
			status=1;
		 $.ajax({
		            url:"${request.contextPath}/bigdata/notice/changeStatus",
		            data:{
		              'id':id,
		              'status':status	           
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
         					$('.page-content').load('${request.contextPath}/bigdata/notice/index'); 
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
	}

	function deleteNotice(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
		 	$.ajax({
		            url:"${request.contextPath}/bigdata/notice/delete",
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
         					$('.page-content').load('${request.contextPath}/bigdata/notice/index'); 
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}
</script>