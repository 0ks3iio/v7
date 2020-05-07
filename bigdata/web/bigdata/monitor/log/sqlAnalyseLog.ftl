<div class="table-show" id="sqlLogDiv">
    <div class="filter-made mb-10">
        <div class="filter-item">
            <div class="form-group">
                <select class="form-control" id="dbType" onChange="searchLog();">
                    <option value="all" >请选择数据库类型</option>
                    <option value="MYSQL" <#if dbType! == 'MYSQL'>selected="selected"</#if>>MYSQL</option>
                    <option value="ORACLE" <#if dbType! == 'ORACLE'>selected="selected"</#if>>ORACLE</option>
                    <option value="HBASE" <#if dbType! == 'HBASE'>selected="selected"</#if>>HBASE</option>
                    <option value="KYLIN" <#if dbType! == 'KYLIN'>selected="selected"</#if>>KYLIN</option>
                    <option value="IMPALA" <#if dbType! == 'IMPALA'>selected="selected"</#if>>IMPALA</option>
                    <option value="HIVE" <#if dbType! == 'HIVE'>selected="selected"</#if>>HIVE</option>
                    <option value="ES" <#if dbType! == 'ES'>selected="selected"</#if>>ES</option>
                </select>
            </div>
        </div>
       <#-- <div class="filter-item">
            <input type="text"  id="" class="form-control" placeholder="">
        </div>-->
        <div class="filter-item">
            <div class="form-group">
                <div class="input-group">
                    <input type="text"  id="duration" class="form-control" placeholder="耗时>=     毫秒" value="${duration!}">
                    <a href="javascript:void(0);" class="input-group-addon" onclick="searchLog()"  hidefocus="true"><i class="wpfont icon-search"></i></a>
                </div>
            </div>
        </div>
    </div>
<table class="tables tables-border no-margin">
    <thead>
    <tr>
        <th >数据库类型</th>
        <th >操作时间</th>
        <th >耗时(毫秒)</th>
        <th >SQL</th>
    </tr>
    </thead>
    <tbody>
    <#if list??>
    <#list list as l>
        <tr>
            <td>${l.dbType!}</td>
            <td>${l.operationTime!}</td>
            <td>${l.duration!}</td>
            <td><a href="javascript:;" class="look-over" onclick="viewDetailLog('${l_index!}')">查看sql语句</a></td>
        </tr>
    </#list>
    </#if>
    </tbody>
</table>

<#if list?exists&&list?size gt 0>
    <#list list as l>
        <div class="layer layer-param-${l_index!}">
            <div class="layer-content">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">sql：</label>
                        <div class="col-sm-6">
                            <textarea name="remark" id="remark" type="text/plain" readonly style="width:480px;height:166px;resize: none;">${l.sql!}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</#if>
</div>
<script>
    function searchLog(){
        var dbType = $("#dbType option:selected").val();
        var duration =$("#duration").val();
        var reg = /^[1-9]\d*$/;
        if(duration!=""&&!reg.test(duration)){
            alert('耗时 请输入正整数')
            return;
        }
        var url =  "${request.contextPath}/bigdata/monitor/sql/index?dbType="+dbType+"&duration="+duration;
        $("#sqlLogDiv").load(url);
    }

    function viewDetailLog(logIndexNum){
        layer.open({
            type: 1,
            shade: 0.5,
            closeBtn:1,
            title: '详细信息',
            area: '666px',
            btn: ['关闭'],
            yes:function(index,layero){
                parent.layer.close(index);
            },
            content: $('.layer-param-'+logIndexNum)
        })
    }
</script>