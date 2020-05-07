<div class="tree-name border-bottom-cfd2d4 no-margin">
        <b>明细</b>
        <div class="pos-right">
        	<img src="${request.contextPath}/static/images/big-data/edit.png" class="pointer js-edit" onclick="editShellJob('${shell.id!}')"/>&nbsp;
        	<img src="${request.contextPath}/static/images/big-data/delete-grey.png" class="pointer js-delete" onclick="deleteShellJob('${shell.id!}','${shell.name!}')" alt="" />
        </div>
    </div>
    <div class="padding-20 js-scroll-height js-detail">
    <table class="table table-bordered table-striped table-hover">
		<tbody>
		    <tr class="js-name">
		        <td class="text-right" style="width:110px;">名称:</td>
		        <td class="js-replace">
		           <span class="word-break">${shell.name!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">调度文件:</td>
		        <td class="js-replace">
		        	 <span class="word-break">${shell.fileName!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">定时执行参数:</td>
		        <td class="js-replace">
		        	 <span class="word-break">${shell.scheduleParam!}</span>
		        </td>
		    </tr>
		    <tr class="js-time">
		        <td class="text-right">备注:</td>
		        <td class="js-replace">
		        	 <span class="word-break">${shell.remark!}</span>
		        </td>
		    </tr>
		</tbody>
	</table>
	<!--
	<div class="clearfix margin-bottom-20 text-center">
		<button class="btn btn-blue js-last js-save-btn"  onclick="execShellJob('${shell.id!}','${shell.hasParam?default(0)}')" type="button">运行</button>
	</div>-->
</div>
