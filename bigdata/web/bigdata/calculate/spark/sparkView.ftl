<div class="tree-name border-bottom-cfd2d4 no-margin">
    <b>明细</b>
    <div class="pos-right">
        <img src="${request.contextPath}/static/images/big-data/edit.png" class="pointer js-edit"
             onclick="editSparkJob('${spark.id!}')"/>&nbsp;
        <img src="${request.contextPath}/static/images/big-data/delete-grey.png" class="pointer js-delete"
             onclick="deleteSparkJob('${spark.id!}','${spark.name!}')" alt=""/>
    </div>
</div>
    <div class="padding-20 js-scroll-height js-detail">
        <table class="table table-bordered table-striped table-hover">
            <tbody>
            <input type="hidden" id="jobId" value="${spark.id!}">
            <tr class="js-name">
                <td class="text-right" style="width:110px;">名称:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.name!}</span>
                </td>
            </tr>
            <tr class="js-time">
                <td class="text-right">类型:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.jobType!}</span>
                </td>
            </tr>
            <tr class="js-time">
                <td class="text-right">jar包:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.allFileNames!}</span>
                </td>
            </tr>
            <tr class="js-time">
                <td class="text-right">类名称:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.fileName!}</span>
                </td>
            </tr>
            <tr class="js-time">
                <td class="text-right">文件路径:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.businessFile!}</span>
                </td>
            </tr>
            <tr class="js-time">
                <td class="text-right">流处理:</td>
                <td class="js-replace">
		        	<#if spark.runType?default(0) ==1>是<#else>否</#if>
                    <input type="hidden" id="runType" value="${spark.runType?default(0)}">
                </td>
            </tr>
            <tr class="js-time">
                <td class="text-right">定时执行:</td>
                <td class="js-replace">
		        	<#if spark.isSchedule?default(0) ==1>是<#else>否</#if>
                </td>
            </tr>
		    <#if spark.isSchedule?default(0) ==1>
		    <tr class="js-time">
                <td class="text-right">定时执行参数:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.scheduleParam!}"</span>
                </td>
            </tr>
            </#if>
		    <#if spark.isSchedule?default(0) ==0>
		    <tr class="js-time">
                <td class="text-right">是否包含参数:</td>
                <td class="js-replace">
		        	<#if spark.hasParam?default(0) ==1>是<#else>否</#if>
                </td>
            </tr>
            </#if>
            <tr class="js-time">
                <td class="text-right">备注:</td>
                <td class="js-replace">
                    <span class="word-break">${spark.remark!}</span>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="clearfix margin-bottom-20 text-center">
            <button class="btn btn-blue js-last js-save-btn" id="running_btn"
                    onclick="execSparkJob('${spark.id!}','${spark.hasParam?default(0)}')" type="button">运行
            </button>

            <button class="btn btn-blue js-last js-stop-btn" id="stop_btn"
                    onclick="stopSparkJob('${spark.id!}','${spark.hasParam?default(0)}')" type="button">停止
            </button>
        </div>
    </div>