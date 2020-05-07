<div class="tree-name border-bottom-cfd2d4 no-margin">
        <b>明细</b>
        <div class="pos-right">
        	<img src="${request.contextPath}/static/images/big-data/edit.png" class="pointer js-edit" onclick="editKylinJob('${kylin.id!}','${kylinCube.name!}')"/>&nbsp;
        	<#if kylin.id! !="">
        	<img src="${request.contextPath}/static/images/big-data/delete-grey.png" class="pointer js-delete" onclick="deleteKylinJob('${kylin.id!}','${kylinCube.name!}')" alt="" />
        	</#if>
        </div>
    </div>
    <div class="padding-20 js-scroll-height js-detail">
    <table class="table table-bordered table-striped table-hover">
		<tbody>
		    <tr class="js-name">
		        <td class="text-right" style="width:110px;">名称:</td>
		        <td class="js-replace">
		          <span class="word-break">${kylinCube.name!}</span>
		        </td>
		    </tr>
		     <tr class="js-time">
		        <td class="text-right">定时执行:</td>
		        <td class="js-replace">
		        	<#if kylin.isSchedule?default(0) ==1>是<#else>否</#if>
		        </td>
		    </tr>
		    <#if kylin.isSchedule?default(0) ==1>
		    <tr class="js-time">
		        <td class="text-right">定时执行参数:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kylin.scheduleParam!}</span>
		        </td>
		    </tr>
		    </#if>

		    <tr class="js-time">
		        <td class="text-right">备注:</td>
		        <td class="js-replace">
		        	<span class="word-break">${kylin.remark!}</span>
		        </td>
		    </tr>
		</tbody>
	</table>
	<#if kylin.id! !="">
	<div class="clearfix margin-bottom-20 text-center">
		<button class="btn btn-blue js-last js-save-btn"  onclick="execKylinJob('${kylin.id!}','${kylin.hasParam?default(0)}')" type="button">运行</button>
	</div>
	</#if>
</div>