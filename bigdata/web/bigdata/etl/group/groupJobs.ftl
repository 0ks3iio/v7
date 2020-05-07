<div class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-3 control-label">批处理名称：</label>
        <div class="col-sm-9">
            <select id="jobId" class="form-control" nullable="true" name="jobId">
                <option value="">---请选择---</option>
                <#if jobs?exists&&jobs?size gt 0>
                    <#list jobs as job>
                        <option value="${job.id!}" <#if job.id == jobId!>selected</#if>>${job.name!}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
</div>