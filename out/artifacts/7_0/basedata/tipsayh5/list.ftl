<#if dtoList?exists && dtoList?size gt 0>
	<#list dtoList as dto>
		<div class="mui-card edu-list" data-value="${dto.tipsay.id!}">
        	<div class="mui-card-header tipsayItem">
        		<#if adminType?default('0')=='0'>
        		<span>第${dto.tipsay.weekOfWorktime}周/${dto.timeStr!}</span>
        		<#else>
        			<span>${dto.oldTeacherName!}<span class="f-12 ml-10">(第${dto.tipsay.weekOfWorktime}周/${dto.timeStr!})</span></span>
        		</#if>
        	</div>
        	<div class="mui-card-content tipsayItem">
        		<div class="mui-card-content-inner">
        			<table width="100%">
        				<tr>
        					<td width="50%"><span class="c-999">班级：</span>${dto.className!}</td>
        					<td><span class="c-999">申请类型：</span>${dto.tipsayTypeName!}</td>
        				</tr>
        				<tr>
        					<td><span class="c-999">原教师：</span>${dto.oldTeacherName!}</td>
        					<td><span class="c-999"><#if type?default('')=='1'>代课<#elseif type?default('')=='2'>管课</#if>教师：</span>${dto.newTeacherName!}</td>
        				</tr>
        				<tr>
        					<td colspan="2"><span class="c-999">备注：</span>${dto.remark!}</td>
        				</tr>
        			</table>
        		</div>
        	</div>
        	<div class="mui-card-footer">
        	<#if adminType?default('0')=='0'>
        		<#if dto.tipsay.state=='0'>
					<#if dto.tipsay.tipsayType?default('')=='03'>
					<span class="c-orange">待安排</span>
					<#elseif dto.tipsay.tipsayType?default('')=='02'>
					<span class="c-orange">待审核</span>
					<#else>
					<span class="c-orange">审核中</span>
					</#if>
					
				<#elseif dto.tipsay.state=='1'>
					<span class="c-orange"><i class="label"><#if type?default('')=='1'>代<#elseif type?default('')=='2'>管</#if></i>${dto.newTeacherName!}</span>
				<#elseif dto.tipsay.state=='2'>
					<span class="c-orange">未通过</span>
				</#if>
        		<#if teacherId?default('')==dto.tipsay.operator?default('') && dto.tipsay.tipsayType!='01'>
        		<span>
    				<a href="javascript:" class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16 deleteTipsay" data-value="${dto.tipsay.id!}">撤销</a>
        		</span>
        		</#if>
        	<#else>
				<#if dto.tipsay.state?default('0')=='1'>
					<span class="c-orange"><i class="label"><#if type?default('')=='1'>代<#elseif type?default('')=='2'>管</#if></i>${dto.newTeacherName!}</span>
				<#elseif dto.tipsay.state?default('0')=='2'>
				<span class="c-orange">未通过</span>
				<#elseif dto.tipsay.tipsayType?default('')=='03'>
				<span class="c-orange">待安排</span>
				<#elseif dto.tipsay.tipsayType?default('')=='02'>
				<span class="c-orange">待审核</span>
				</#if>
    			<span>
    				<#if dto.tipsay.state?default('0')=='1'>
    					<a href="javascript:" class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16 deleteTipsay" data-value="${dto.tipsay.id!}">撤销</a>
    				<#elseif dto.tipsay.state?default('0')=='2'>
    					<a href="javascript:" class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16 deleteTipsay" data-value="${dto.tipsay.id!}">撤销</a>
    				<#elseif dto.tipsay.tipsayType?default('')=='03'>
	        			<a class="mui-btn btn-radius-16 noAgreeTipsay" data-value="${dto.tipsay.id!}" >不通过</a>
	        			<a class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16 ArrangeTipsayTeacher" data-value="${dto.tipsay.id!}">去安排</a>
    				<#elseif dto.tipsay.tipsayType?default('')=='02'>
	        			<a class="mui-btn btn-radius-16 noAgreeTipsay" data-value="${dto.tipsay.id!}">不通过</a>
	        			<a class="mui-btn mui-btn-primary mui-btn-outlined btn-radius-16 agreeTipsay" data-value="${dto.tipsay.id!}">通过</a>
    				</#if>
    				
    			</span>
        	</#if>
        	</div>
        </div>
    </#list>
<#else>
	<div class="mui-page-noData"> 
    	 <i></i>
    	 <p class="f-16">没有找到相关数据</p>
    </div>
</#if>