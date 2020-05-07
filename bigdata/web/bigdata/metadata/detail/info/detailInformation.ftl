<#if mdType=='table'>
    <div class="form-horizontal" style="width: 500px;">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">名称：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.name!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">数据库类型：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.dbType!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">表名：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.tableName!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">是否计入资产：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">
                        <#if metadata.isProperty?default(1) == 1>是<#else>否</#if>
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">是否支持api：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">
                        <#if metadata.isSupportApi?default(1) == 1>是<#else>否</#if>
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">数据主题：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${propertyTopic!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">数据层次：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${dwRank!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">标签：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${tags!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">创建时间：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin"
                           style="text-align:left">${metadata.creationTime!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">修改时间：</label>
            <div class="col-sm-8 position-relative">
                <div>
                    <label class="control-label no-margin"
                           style="text-align:left">${metadata.modifyTime!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-8">
                <label class="control-label no-margin" style="text-align:left">${metadata.remark!}</label>
            </div>
        </div>
    </div>
<#elseif mdType=='job'>
    <div class="form-horizontal" style="width: 500px;">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">名称：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.name!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">类型：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.jobType!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">文件名：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.fileName!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">创建时间：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin"
                           style="text-align:left">${metadata.creationTime?string('yyyy-MM-dd hh:mm:ss')}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">修改时间：</label>
            <div class="col-sm-8 position-relative">
                <div>
                    <label class="control-label no-margin"
                           style="text-align:left">${metadata.modifyTime?string('yyyy-MM-dd hh:mm:ss')}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">是否启用：</label>
            <div class="col-sm-8 position-relative">
                <div>
                    <label class="control-label no-margin"
                           style="text-align:left"><#if metadata.status?default('0') == '0'>未启用<#else>启用</#if></label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-8">
                <label class="control-label no-margin" style="text-align:left">${metadata.remark!}</label>
            </div>
        </div>
    </div>
<#elseif mdType=='model'>
    <div class="form-horizontal" style="width: 500px;">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">名称：</label>
            <div class="col-sm-9">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.name!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">code：</label>
            <div class="col-sm-9">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.code!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">类型：</label>
            <div class="col-sm-9">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.type!}</label>
                </div>
            </div>
        </div>
        <#if metadata.type == 'kylin'>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">项目名：</label>
                <div class="col-sm-9">
                    <div>
                        <label class="control-label no-margin" style="text-align:left">${metadata.project!}</label>
                    </div>
                </div>
            </div>
        <#else>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">数据库名称：</label>
                <div class="col-sm-9">
                    <div>
                        <label class="control-label no-margin" style="text-align:left">${metadata.dbName!}</label>
                    </div>
                </div>
            </div>
        </#if>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">创建时间：</label>
            <div class="col-sm-9">
                <div>
                    <label class="control-label no-margin"
                           style="text-align:left">${metadata.creationTime?string('yyyy-MM-dd hh:mm:ss')}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-9">
                <label class="control-label no-margin" style="text-align:left">${metadata.remark!}</label>
            </div>
        </div>
    </div>
<#elseif mdType=='event'>
    <div class="form-horizontal" style="width: 500px;">
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">名称：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.eventName!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">code：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin" style="text-align:left">${metadata.eventCode!}</label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">是否包含时间属性：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin"><#if metadata.timeProperty?default(0) == 1>是<#else>否</#if></label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">是否包含用户属性：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin"><#if metadata.userProperty?default(0) == 1>是<#else>否</#if></label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">是否包含系统属性：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin"><#if metadata.envProperty?default(0) == 1>是<#else>否</#if></label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">统计颗粒度：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin">
                        <#if metadata.granularity?default('') == 'second'>按秒</#if>
                        <#if metadata.granularity?default('') == 'minute'>按分钟</#if>
                        <#if metadata.granularity?default('') == 'fifteen_minute'>按15分钟</#if>
                        <#if metadata.granularity?default('') == 'thirty_minute'>按30分钟</#if>
                        <#if metadata.granularity?default('') == 'hour'>按小时</#if>
                        <#if metadata.granularity?default('') == 'day'>按天</#if>
                        <#if metadata.granularity?default('') == 'week'>按周</#if>
                        <#if metadata.granularity?default('') == 'month'>按月</#if>
                        <#if metadata.granularity?default('') == 'quarter'>按季度</#if>
                        <#if metadata.granularity?default('') == 'year'>按年</#if>
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">创建时间：</label>
            <div class="col-sm-8">
                <div>
                    <label class="control-label no-margin"><#if metadata.creationTime?exists>${metadata.creationTime?string('yyyy-MM-dd hh:mm:ss')}</#if></label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label no-padding-right">备注：</label>
            <div class="col-sm-8">
                <label class="control-label no-margin" style="text-align:left">${metadata.remark!}</label>
            </div>
        </div>
    </div>
</#if>