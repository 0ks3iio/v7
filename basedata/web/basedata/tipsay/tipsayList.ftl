<#if tipsayDtoList?exists && tipsayDtoList?size gt 0>
<table class="table table-striped table-bordered table-hover no-margin mainTable">
    <thead>
        <tr>
        	<th style="width:60px;">时间</th>
        	<th width="">上课节次</th>
        	<th width="">班级</th>
        	<th width="">课程</th>
        	<th width="">原任课教师</th>
        	<th width="">代课教师</th>
        	<th width="">类型</th>
        	<th width="">申请类别</th>
        	<th style="width:200px;">备注</th>
        	<th width="">状态</th>
        	<#if isAdmin>
            <th style="12%">操作</th>
            </#if>
        </tr>
    </thead>
    <tbody>
    	<#list tipsayDtoList as dto>
    		<tr>
    			<td>${dto.dateStr!}</td>
    			<td>${dto.timeStr!}</td>
    			<td>${dto.className!}</td>
    			<td>${dto.subjectName!}</td>
    			<td>${dto.oldTeacherName!}</td>
    			<td>${dto.newTeacherName!}</td>
    			<td>${dto.typeName!}</td>
    			<td>${dto.tipsayTypeName!}</td>
    			<td style="word-wrap: break-word;word-break:break-all;">${dto.remark!}</td>
    		
    			<td>
    				<#if dto.tipsay.state?default('0')=='1'>
    				通过
    				<#elseif dto.tipsay.state?default('0')=='2'>
    				未通过
    				<#elseif dto.tipsay.tipsayType?default('')=='03'>
    				待安排
    				<#elseif dto.tipsay.tipsayType?default('')=='02'>
    				待审核
    				</#if>
    			</td>
    		
    			<#if isAdmin>
    			<td>
    				<#if dto.tipsay.state?default('0')=='1'>
    					<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="deleteTipsay('${dto.tipsayId!}')" >撤销</a>
    				<#elseif dto.tipsay.state?default('0')=='2'>
    					<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="deleteTipsay('${dto.tipsayId!}')" >撤销</a>
    				<#elseif dto.tipsay.tipsayType?default('')=='03'>
    					<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="arrange('${dto.tipsayId!}')" >去安排</a>
    				<#elseif dto.tipsay.tipsayType?default('')=='02'>
    					<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="arrangeYesOrNo('${dto.tipsayId!}','1')" >通过</a>
    					<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="arrangeYesOrNo('${dto.tipsayId!}','0')" >不通过</a>
    				</#if>
    				
    			</td>
    			</#if>
    		</tr>
    	</#list>
    </tbody>
</table>
<#else>
<div class="no-data-container ">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">暂无相关数据</p>
		</div>
	</div>
</div>
</#if>
<script >
	$(function(){
		<#if isAdmin>
			$(".js_addTipsay").show();
		<#else>
			$(".js_addTipsay").hide();
		</#if>
	})
	
	var isdeleted=false;
	function deleteTipsay(tipsayId){
		if(isdeleted){
			return;
		}
		isdeleted=true;
		
		layer.confirm('确定撤销吗？', function(index){
			$.ajax({
				url:'${request.contextPath}/basedata/tipsay/deleteTipsay',
				data:{'tipsayId':tipsayId},
				type:'post', 
				dataType:'json',
				success:function(jsonO){
					layer.closeAll();
					if(jsonO.success){
						layer.msg("操作成功", {
								offset: 't',
								time: 2000
							});
						searchTipsay();
					}else{
						isdeleted=false;
						layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
					}
				}
			});	
		},function(){
			isdeleted=false;
		})
			
	}
	var isarrange=false;
	function arrange(tipsayId){
		if(isarrange){
			return;
		}
		isarrange=true;
		$.ajax({
			url:'${request.contextPath}/basedata/tipsay/checkTipsay',
			data:{'tipsayId':tipsayId},
			type:'post', 
			dataType:'json',
			async:false,
			success:function(jsonO){
				layer.closeAll();
				isarrange=false;
				if(jsonO.success){
					arrangeFull(tipsayId);
					//防止拦截
					//$("#arrangeFull_"+tipsayId).click();
				}else{
					layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
					searchTipsay();
				}
			}
		});	
	}
	
	function arrangeFull(tipsayId){
		//全屏展现
		var url =  '${request.contextPath}/basedata/tipsay/arrangeSelfTipsay/page?tipsayId='+tipsayId;
		
		var availHeight = window.screen.availHeight;
 		var availWidth = window.screen.availWidth;
 		
		window.open(url,
			'tipsayArrangAdmin',
			'height='+availHeight+',width='+availWidth+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	var isYesOrNo=false;
	function arrangeYesOrNo(tipsayId,state){
		if(isYesOrNo){
			return;
		}
		isYesOrNo=true;
		$.ajax({
			url:'${request.contextPath}/basedata/tipsay/arrangeYesOrNo',
			data:{'tipsayId':tipsayId,'state':state},
			type:'post', 
			dataType:'json',
			success:function(jsonO){
				if(jsonO.success){
					layer.msg("操作成功", {
							offset: 't',
							time: 2000
						});
					searchTipsay();
				}else{
					isYesOrNo=false;
					layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
				}
			}
		});	
	}
</script>