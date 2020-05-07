<#if shareList?exists && shareList?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th>名称</th>
		            <th>类型</th>
		            <#if type! ==1>
		            <th>被分享人</th>
		            <#else>
		            <th>分享人</th>
		            </#if>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
		    	<#list shareList as share>
		    	<tr>
			        <td>${share.businessName!}</td>
			         <td><#if share.businessType! =="1">数据图表<#elseif share.businessType! =="3">数据报表<#elseif share.businessType! =="5">多维报表<#elseif share.businessType! =="6">数据看板<#elseif share.businessType! =="7">数据报告<#else>未知</#if></td>
			         <td title="${share.userNames!}">
							<#if share.userNames! !="" && share.userNames?length gt 50>${share.userNames?substring(0, 50)}......<#else>${share.userNames!}</#if>
					</td>
			         <td>
			               <a href="javascript:void(0)"  onclick="showReportFromShare('${share.businessId!}','${share.businessType!}','${share.businessName!}')"  class="look-over">查看</a>
			               <#if type! ==1>
			               <span class="tables-line">|</span><a href="javascript:void(0)" class="remove" onclick="deleteShare('${share.businessId!}','${share.businessType!}','${share.businessName!}');">分享</a>
							</#if>
			        </td>
			  	</tr>
			  	</#list>
		    </tbody>
		</table>
<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999"><#if type! ==1>暂无分享记录<#else>暂无被分享记录</#if></p>
		</div>
	</div>
</#if>
<script>
   function showReportFromShare(id,type,name) {
		var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
	 	window.open(url,id)
    }
    
    function deleteShare(id, type, name){
		shareLayer(id, type, name);
	}

   function shareLayer(businessId, businessType, businessName) {
	   $.ajax({
		   url: '${request.contextPath}/bigdata/share/getAllUser',
		   type: 'GET',
		   data: {businessId: businessId},
		   success: function (response) {
			   $.ajax({
				   url: '${request.contextPath}/bigdata/common/tree/shareUserTreeIndex',
				   type: 'POST',
				   data: {users:response.data},
				   dataType: 'html',
				   beforeSend: function(){
					   $('.share-div').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
				   },
				   success: function (response) {
					   $('.share-div').html(response);
				   }
			   });
		   }
	   });

	   var index = layer.open({
		   type: 1,
		   title: '用户选择',
		   shade: .5,
		   shadeClose: true,
		   closeBtn: 1,
		   btn :['确定','取消'],
		   area: ['70%','550px'],
		   yes:function(index, layero){
			   var teacherArray = [];
			   zTreeSelectedUserIdMap.forEach(function (value, key, map) {
				   teacherArray.push(key);
			   });
			   doShare(businessId, businessType, businessName, teacherArray);
			   layer.close(index);
		   },
		   content: $('.share-div')
	   })
   }

   function doShare(businessId, businessType, businessName, userArray) {
	   $.ajax({
		   url: _contextPath + '/bigdata/share/addShare',
		   type: 'POST',
		   dataType: 'json',
		   data: {
			   businessId: businessId,
			   businessType: businessType,
			   businessName: businessName,
			   userArray: userArray
		   },
		   success: function (response) {
			   if (response.success) {
				   showLayerTips('success', response.message, 't');
                   $('.page-content').load('${request.contextPath}/bigdata/share/index');
               } else {
				   showLayerTips4Confirm('error', response.message);
			   }
		   }
	   });
   };
</script>