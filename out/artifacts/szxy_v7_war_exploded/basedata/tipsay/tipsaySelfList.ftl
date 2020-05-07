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
            <th width="12%">操作</th>
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
    			<td style="word-wrap: break-word;word-break:break-all; ">${dto.remark!}</td>
    			<td>
					<#if dto.tipsay.state=='0'>
						<#if dto.tipsay.tipsayType?default('')=='03'>
    					待安排
    					<#elseif dto.tipsay.tipsayType?default('')=='02'>
    					待审核
    					<#else>
    					审核中
    					</#if>
						
					<#elseif dto.tipsay.state=='1'>
						通过
					<#elseif dto.tipsay.state=='2'>
						未通过
					</#if>
    			</td>
    			<td>
    				<#if teacherId?default('')==dto.tipsay.operator && dto.tipsay.tipsayType!='01'>
    					<#if dto.tipsay.state=='0'>
    						<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="deleteTipsay('${dto.tipsayId!}','0')" >撤销</a>
    					<#elseif dto.tipsay.state=='1'>
    						<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="deleteTipsay('${dto.tipsayId!}','1')" >撤销</a>
    					<#elseif dto.tipsay.state=='2'>
    						<a href="javascript:;" class="table-btn color-green js-toggleLock " onclick="deleteTipsay('${dto.tipsayId!}','0')" >撤销</a>
    					</#if>
    					
    				</#if>
    			</td>
    			
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
		
	})
	
	var isdeleted=false;
	function deleteTipsay(tipsayId,type){
		if(isdeleted){
			return;
		}
		isdeleted=true;
		var text="撤销";
		
		layer.confirm('确定'+text+'吗？', function(index){
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
</script>