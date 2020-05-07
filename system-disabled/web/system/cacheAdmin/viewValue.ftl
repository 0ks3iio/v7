<style>
    .table .form-control{
        height: 36px;
    }
</style>
<div class="modal-body">

    <div class="input-group">
        <span class="input-group-addon" >serverName</span>
        <input id="updateModal_serverName" value="${redis_host!}:${redis_port!}" name="serverName" class="form-control" placeholder="serverName" readonly>
    </div>

    <div class="row">
        <br>
    </div>

    <div class="input-group">
        <span class="input-group-addon" >dbIndex</span>
        <input id="updateModal_dbIndex" value="${dbIndex}" name="dbIndex" class="form-control" placeholder="dbIndex" readonly>
    </div>

    <div class="row">
        <br>
    </div>

    <div class="input-group">
        <span class="input-group-addon" >key</span>
        <input id="updateModal_key" value="${key!}" name="key" class="form-control" placeholder="key" readonly>
    </div>

    <div class="row">
        <br>
    </div>

    <div class="input-group">
        <span class="input-group-addon" >dataType</span>
        <input id="updateModal_dataType" value="${dataType!}" name="dataType" class="form-control" placeholder="dataType" readonly>
    </div>

    <div class="row">
        <br>
    </div>

    <!-- values form  -->
    <form id="STRINGForm" style="<#if dataType?default("") != "STRING" >display: none;</#if>" value1="STRINGForm">
        <table id="STRINGFormTable" class="table table-striped table-bordered" >
            <thead>
            <tr>
                <th>value</th>
                <th class="col-sm-1 col-lg-1">operation</th>
            </tr>
            </thead>
            <tbody>
            <#if data?exists && data?size gt 0>
            <#list data as cValue>
            <tr style="<#if dataType?default("") != "STRING" >display: none;</#if>">
                <td title="value">
                    <div class="input-group">
                        <span class="input-group-addon">value</span>
                        <input name="value" value="${cValue.viewValue!}" type="text" class="requireds form-control" placeholder="value"  >
                        <span class="input-group-btn">
                            <button class="update_minus_btn btn btn-default" type="button" value1="STRING" >
                                <span class="glyphicon glyphicon-minus" />
							</button>
						</span>
                    </div>
                    <!-- <input name="value" class="form-control" > -->
                </td>
                <td><button type="button" class="update_redis_btn btn btn-info">update</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
        </table>
    </form>

    <form id="LISTForm" style="<#if dataType?default("") != "LIST" >display: none;</#if>">
        <label for="recipient-name" class="control-label">value(REMARK:leftpop , leftpush)</label>
        <table id="LISTFormTable" class="table table-striped table-bordered" >
            <thead>
            <tr>
                <th>value</th>
                <th class="col-sm-1 col-lg-1">operation</th>
            </tr>
            </thead>
            <tbody>
            <#if data?exists && data?size gt 0 >
            <#list data as cValue>
            <tr style="<#if dataType?default("") != "LIST" >display: none;</#if>">
                <td title="value">
                    <div class="input-group">
                        <span class="input-group-addon">value</span>
                        <input name="value" value="${cValue.viewValue!}" type="text" class="requireds form-control" placeholder="value"  >
                        <input type="hidden" name="index" value="${cValue_index}" type="text" class="requireds form-control" placeholder="index"  >
                        <span class="input-group-btn">
                            <button class="update_plus_btn btn btn-default" type="button" value1="LIST">
                                <span class="glyphicon glyphicon-plus" />
                            </button>
                            <button class="update_minus_btn btn btn-default" type="button" value1="LIST">
                                <span class="glyphicon glyphicon-minus" />
                            </button>
						</span>
                    </div>
                </td>
                <td><button type="button" class="update_redis_btn btn btn-info">lpush</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
        </table>
    </form>

    <form id="SETForm" style="<#if dataType?default("") != "SET">display: none;</#if>">
        <label for="recipient-name" class="control-label">value(REMARK:minus is random)</label>
        <table id="SETFormTable" class="table table-striped table-bordered" >
            <thead>
            <tr>
                <th>value</th>
                <th class="col-sm-1 col-lg-1">operation</th>
            </tr>
            </thead>
            <tbody>
            <#if data?exists && data?size gt 0 >
            <#list data as cValue>
            <tr style="<#if dataType?default("") != "SET">display: none;</#if>">
                <td title="value">
                    <div class="input-group">
                        <span class="input-group-addon">value</span>
                        <input name="value" value="${cValue.viewValue}" type="text" class="requireds form-control" placeholder="value"  >
                        <input type="hidden" name="oldValue" value="${cValue.viewValue}" type="text" class="requireds form-control" placeholder="value"  >
                        <span class="input-group-btn">
                            <button class="update_plus_btn btn btn-default" type="button" value1="SET">
                                <span class="glyphicon glyphicon-plus" />
                            </button>
                            <button class="update_minus_btn btn btn-default" type="button" value1="SET">
                                <span class="glyphicon glyphicon-minus" />
                            </button>
                        </span>
                    </div>
                </td>
                <td><button type="button" class="update_redis_btn btn btn-info">update</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
        </table>
    </form>

    <form id="ZSETForm" style="<#if dataType?default("") != "ZSET">display: none;</#if>">
        <table id="ZSETFormTable" class="table table-striped table-bordered" >
            <thead>
            <tr>
                <th class="col-sm-5 col-lg-5">score</th>
                <th>member</th>
                <th class="col-sm-1 col-lg-1">operation</th>
            </tr>
            </thead>
            <tbody>
            <#if data?exists && data?size gt 0 >
            <#list data as cValue>
            <tr style="<#if dataType?default("") != "ZSET">display: none;</#if>">
                <td title="score"><input value="${cValue.score!}" name="score" class="form-control" ></td>
                <td title="value">
                    <div class="input-group">
                        <span class="input-group-addon">value</span>
                        <input name="value" value="${cValue.viewValue}" type="text" class="requireds form-control" placeholder="value" readonly>
                        <span class="input-group-btn"><button class="update_plus_btn btn btn-default" type="button" value1="ZSET">
                            <span class="glyphicon glyphicon-plus" />
                        </button><button class="update_minus_btn btn btn-default" type="button" value1="ZSET">
                            <span class="glyphicon glyphicon-minus" />
                        </button></span>
                    </div>
                    <!-- <input name="member" class="form-control" > -->
                </td>
                <td><button type="button" class="update_redis_btn btn btn-info">update</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
        </table>
    </form>

    <form id="HASHForm" style="<#if dataType?default("") != "HASH">display: none;</#if>">
        <table id="HASHFormTable" class="table table-striped table-bordered" >
            <thead>
            <tr>
                <th class="col-sm-5 col-lg-5">field</th>
                <th>value</th>
                <th class="col-sm-1 col-lg-1">operation</th>
            </tr>
            </thead>
            <tbody>
            <#if data?exists && data?size gt 0 >
            <#list data as cValue>
            <tr style="<#if dataType?default("") != "HASH">display: none;</#if>">
                <td title="field"><input name="field" value="${cValue.field!}" class="form-control" placeholder="field" readonly></td>
                <td title="value">
                    <div class="input-group">
                        <span class="input-group-addon">value</span>
                        <input name="value" value="${cValue.viewValue!}" type="text" class="requireds form-control" placeholder="value"  >
                        <span class="input-group-btn">
                            <button class="update_plus_btn btn btn-default" type="button" value1="HASH">
                                <span class="glyphicon glyphicon-plus" />
                            </button>
                            <button class="update_minus_btn btn btn-default" type="button" value1="HASH">
                                <span class="glyphicon glyphicon-minus" />
                            </button>
                        </span>
                    </div>
                    <!-- <input name="value" class="form-control"> -->
                </td>
                <td><button type="button" class="update_redis_btn btn btn-info">update</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
        </table>
    </form>
</div>
<script>
    $(document).ready(function () {
        $(".update_redis_btn").bind("click",function () {
           var redis_host = '${redis_host!}';
           var redis_port = '${redis_port!}';
           var dataType = '${dataType!}';
           var key = '${key!}';
           var dbIndex = '${dbIndex!}';

           switch (dataType){
               case "STRING":
                   var ctrLine = $(this).parent().parent();
                   var value = $(ctrLine).find("input[name='value']").val();
                   $.ajax({
                       type: "post",
                       url: '${request.contextPath}/redis/string/update',
                       dataType: "json",
                       data: {
                           redis_host: redis_host,
                           redis_port: redis_port,
                           dbIndex: dbIndex,
                           key: key,
                           dataType: dataType,
                           value: value,
                       },
                       success : function(data) {

                           showSuccessMsg(data.msg);
                       }
                   });
                   break;
               case "HASH":
                   var ctrLine = $(this).parent().parent();
                   var value = $(ctrLine).find("input[name='value']").val();
                   var field = $(ctrLine).find("input[name='field']").val();
                   $.ajax({
                       type: "post",
                       url: '${request.contextPath}/redis/hash/update',
                       dataType: "json",
                       data: {
                           redis_host: redis_host,
                           redis_port: redis_port,
                           dbIndex: dbIndex,
                           key: key,
                           dataType: dataType,
                           value: value,
                           field: field,
                       },
                       success : function(data) {

                           showSuccessMsg(data.msg);
                       }
                   });
                   break;
               case "SET":
                   var ctrLine = $(this).parent().parent();
                   var value = $(ctrLine).find("input[name='value']").val();
                   var oldValue = $(ctrLine).find("input[name='oldValue']").val();
                   $.ajax({
                       type: "post",
                       url: '${request.contextPath}/redis/set/update',
                       dataType: "json",
                       data: {
                           redis_host: redis_host,
                           redis_port: redis_port,
                           dbIndex: dbIndex,
                           key: key,
                           dataType: dataType,
                           value: value,
                           oldValue: oldValue,
                       },
                       success : function(data) {
                           showSuccessMsg(data.msg);
                       }
                   });
                   break;
               case "ZSET":
                   var ctrLine = $(this).parent().parent();
                   var value = $(ctrLine).find("input[name='value']").val();
                   var score = $(ctrLine).find("input[name='score']").val();
                   $.ajax({
                       type: "post",
                       url: '${request.contextPath}/redis/zset/update',
                       dataType: "json",
                       data: {
                           redis_host: redis_host,
                           redis_port: redis_port,
                           dbIndex: dbIndex,
                           key: key,
                           dataType: dataType,
                           value: value,
                           score: score,
                       },
                       success : function(data) {
                           showSuccessMsg(data.msg);
                       }
                   });
                   break;
               case "LIST":
                   var ctrLine = $(this).parent().parent();
                   var value = $(ctrLine).find("input[name='value']").val();
                   var index = $(ctrLine).find("input[name='index']").val();
                   $.ajax({
                       type: "post",
                       url: '${request.contextPath}/redis/list/update',
                       dataType: "json",
                       data: {
                           redis_host: redis_host,
                           redis_port: redis_port,
                           dbIndex: dbIndex,
                           key: key,
                           dataType: dataType,
                           value: value,
                           index: index,
                       },
                       success : function(data) {
                           showSuccessMsg(data.msg);
                       }
                   });
                   break;
           }
        });

        $(".update_minus_btn").bind("click",function () {
            var redis_host = '${redis_host!}';
            var redis_port = '${redis_port!}';
            var dataType = '${dataType!}';
            var key = '${key!}';
            var dbIndex = '${dbIndex!}';

            switch (dataType) {
                case "STRING":
                    var ctrLine = $(this).parent().parent();
                    var value = $(ctrLine).find("input[name='value']").val();
                    $.ajax({
                        type: "post",
                        url: '${request.contextPath}/redis/delete',
                        dataType: "json",
                        data: {
                            redis_host: redis_host,
                            redis_port: redis_port,
                            dbIndex: dbIndex,
                            key: key,
                            dataType: dataType,
                        },
                        success : function(data) {
                            refreshKeyList(getDynamicParameters() + "&dataType="+$("#dataType").val()+"&queryType="+$("#queryType").val()+"&queryKey="+$("#query_input").val());
                            $(this).parent().parent().parent().remove();
                            showSuccessMsg(data.msg);
                        }
                    });
                    break;
                case "HASH":
                    var ctrLine = $(this).parent().parent().parent().parent();
                    var value = $(ctrLine).find("input[name='value']").val();
                    var field = $(ctrLine).find("input[name='field']").val();
                    $.ajax({
                        type: "post",
                        url: '${request.contextPath}/redis/hash/delete',
                        dataType: "json",
                        data: {
                            redis_host: redis_host,
                            redis_port: redis_port,
                            dbIndex: dbIndex,
                            key: key,
                            dataType: dataType,
                            field: field,
                        },
                        success : function(data) {
                            $(this).parent().parent().parent().remove();
                            showSuccessMsg(data.msg);
                        }
                    });
                    break;
                case "SET":
                    var ctrLine = $(this).parent().parent().parent().parent();
                    var value = $(ctrLine).find("input[name='value']").val();
                    var oldValue = $(ctrLine).find("input[name='oldValue']").val();
                    $.ajax({
                        type: "post",
                        url: '${request.contextPath}/redis/set/delete',
                        dataType: "json",
                        data: {
                            redis_host: redis_host,
                            redis_port: redis_port,
                            dbIndex: dbIndex,
                            key: key,
                            dataType: dataType,
                            value: oldValue,
                        },
                        success : function(data) {
                            $(this).parent().parent().parent().remove();
                            showSuccessMsg(data.msg);
                        }
                    });
                    break;
                case "ZSET":
                    var ctrLine = $(this).parent().parent().parent().parent();
                    var value = $(ctrLine).find("input[name='value']").val();
                    var score = $(ctrLine).find("input[name='score']").val();
                    $.ajax({
                        type: "post",
                        url: '${request.contextPath}/redis/zset/delete',
                        dataType: "json",
                        data: {
                            redis_host: redis_host,
                            redis_port: redis_port,
                            dbIndex: dbIndex,
                            key: key,
                            dataType: dataType,
                            value: value,
                            score: score,
                        },
                        success : function(data) {
                            $(this).parent().parent().parent().remove();
                            showSuccessMsg(data.msg);
                        }
                    });
                    break;
                case "LIST":
                    var ctrLine = $(this).parent().parent().parent().parent();
                    var value = $(ctrLine).find("input[name='value']").val();
                    var index = $(ctrLine).find("input[name='index']").val();
                    $.ajax({
                        type: "post",
                        url: '${request.contextPath}/redis/list/delete',
                        dataType: "json",
                        data: {
                            redis_host: redis_host,
                            redis_port: redis_port,
                            dbIndex: dbIndex,
                            key: key,
                            dataType: dataType,
                            value: value,
                            index: index,
                        },
                        success : function(data) {
                            $(this).parent().parent().parent().remove();
                            showSuccessMsg(data.msg);
                        }
                    });
                    break;
            }
        });
    });

    function getDynamicParameters() {
        var parameter = "";
        parameter += "?redis_host=${redis_host!}";
        parameter += "&redis_port=${redis_port!}";
        parameter += "&dbIndex=${dbIndex!}";
        return parameter;
    }
</script>

