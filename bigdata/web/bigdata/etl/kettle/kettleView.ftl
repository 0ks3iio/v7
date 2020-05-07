

    <div class="tree-name border-bottom-cfd2d4 no-margin">
        <b>明细</b>
        <div class="pos-right">
        	<img src="${request.contextPath}/static/images/big-data/edit.png" class="pointer js-edit" onclick="editKettleJob('${kettle.id!}')"/>&nbsp;
        	<img src="${request.contextPath}/static/images/big-data/delete-grey.png" class="pointer js-delete" onclick="deleteKettleJob('${kettle.id!}','${kettle.name!}')" alt="" />
        </div>
    </div>
    <div class="padding-20 js-scroll-height js-detail">
    <table class="table table-bordered table-striped table-hover">
		<tbody>
		    <tr class="js-name">
		        <td class="text-right" style="width:110px;">名称:</td>
		        <td class="js-replace">
		        <span class="word-break">${kettle.name!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">类型:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kettle.jobType!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">调度文件:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kettle.allFileNames!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">主文件名称:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kettle.fileName!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">定时执行:</td>
		        <td class="js-replace">
		        	<#if kettle.isSchedule?default(0) ==1>是<#else>否</#if>
		        </td>
		    </tr>
		    <#if kettle.isSchedule?default(0) ==1>
		    <tr class="js-time">
		        <td class="text-right">定时执行参数:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kettle.scheduleParam!}"</span>
		        </td>
		    </tr>
		    </#if>
		    <#if kettle.isSchedule?default(0) ==0>
		    <tr class="js-time">
		        <td class="text-right">是否包含参数:</td>
		        <td class="js-replace">
		        	<#if kettle.hasParam?default(0) ==1>是<#else>否</#if>
		        </td>
		    </tr>
		    </#if>
		    <tr class="js-time">
		        <td class="text-right">备注:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kettle.remark!}</span>
		        </td>
		    </tr>
		</tbody>
	</table>
	<div class="clearfix margin-bottom-20 text-center">
		<button class="btn btn-blue js-last js-save-btn"  onclick="execKettleJob('${kettle.id!}','${kettle.hasParam?default(0)}')" type="button">运行</button>
	</div>
</div>