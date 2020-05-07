<#if tipsayDtoList?exists && tipsayDtoList?size gt 0>
	<#list tipsayDtoList as tipsayDto>
		<div class="mui-card replace-list">
        	<div class="mui-card-header">
        		<span>${tipsayDto.className!}<span class="f-12 c-999 ml-5">(${tipsayDto.dateStr!}${tipsayDto.timeStr!})</span></span>
        		<#if adminType?default('')=='1'>
        		<#if tipsayDto.canDeleted>
        		<a class="mui-icon mui-icon-trash c-blue confirmBtn" data-value="${tipsayDto.tipsayId!}"></a>
        		</#if>
        		</#if>
        	</div>
        	<div class="mui-card-content">
        		<div class="mui-card-content-inner">
        			<table width="100%">
        				<tr>
        					<td width="33%">
        						<#if tipsayDto.newImg?default('') !=''>
        						<img class="teach" src="${tipsayDto.newImg!}">
        						<#elseif tipsayDto.newTeacherSex?default(1)==1>
        						<img class="teach" src="${request.contextPath}/static/mui/images/edu-admin/boy.png">
        						<#else>
        						<img class="teach" src="${request.contextPath}/static/mui/images/edu-admin/girl.png">
        						</#if>
        						
		        				<div>${tipsayDto.newTeacherName!}</div>
		        				<div class="c-999 f-12">代课老师</div>
        					</td>
        					<td width="33%">
        						<img class="tip" src="${request.contextPath}/static/mui/images/edu-admin/dai.png">
        					</td>
        					<td width="33%">
        						
								<#if tipsayDto.oldImg?default('') !=''>
        						<img class="teach" src="${tipsayDto.oldImg!}">
        						<#elseif tipsayDto.oldTeacherSex?default(1)==1>
        						<img class="teach" src="${request.contextPath}/static/mui/images/edu-admin/boy.png">
        						<#else>
        						<img class="teach" src="${request.contextPath}/static/mui/images/edu-admin/girl.png">
        						</#if>
		        				<div>${tipsayDto.oldTeacherName!}</div>
		        				<div class="c-999 f-12">原任课老师</div>
        					</td>
        				</tr>
        			</table>
        		</div>
        	</div>
        	<div class="mui-card-footer"><span class="c-999 f-12">备注：${tipsayDto.remark!}</span></div>
        </div>
	</#list>
</#if>
        
 <script type="text/javascript" charset="utf-8"> 
 	<#if adminType?default('')=='1'>      
 	$(".mui-card-header").on('tap','.confirmBtn',function(){
 		var tipsayId=$(this).attr("data-value");
 		
    	 mui.confirm('确定要删除吗？', '',['取消','确认'], function(e) {
            if (e.index == 1) {
            	//确认
                deleteTipsay(tipsayId);
            } else {
            	//取消
            	
            }
        })
 	})
 	
 	var isDelete=false;
 	function deleteTipsay(tipsayId){
 		if(isDelete){
 			return;
 		}
 		isDelete=true;
 		$.ajax({
			url:'${request.contextPath}/mobile/open/tipsay/deleteTipsay',
			data:{'tipsayId':tipsayId},
			type:'post', 
			dataType:'json',
			success:function(data){
				isDelete=false;
				if(data.success){
					changeWeek();
				}else{
					isDelete=false;
					toastMsg(data.msg);
				}
			}
		});		
 	}
 	</#if>
    </script>