<div id="a1" class="tab-pane active">
    <div class="filter">
        <div class="filter-item">
            <a class="btn btn-blue" href="#" onClick="bacthAudting('2');">审核通过</a>
            <a class="btn btn-white js-nopass" href="#" onClick="bacthNoAudting('-1');">审核不通过</a>
        </div>
        <div class="filter-item filter-item-right">
            <button type="button" class="btn btn-default" onClick="searchList1();">
                <i class="fa fa-search"></i>
            </button>
        </div>
        <div class="filter-item filter-item-right">
            <span class="filter-name">访亲轮次名称：</span>
            <div class="filter-content">
                <select name="" id="activityId" class="form-control" style="width:400px;" onchange="searchList1();">
				<#if famDearActivities?exists && famDearActivities?size gt 0>
				    <#list famDearActivities as item>
					    <option value="${item.id!}" <#if '${activityId!}' == '${item.id!}'>selected="selected"</#if>>
                            ${item.title!}
                        </option>
                    </#list>
                <#else>
			        <option value="">--请选择--</option>
                </#if>
                </select>
            </div>
        </div>
        <div class="filter-item filter-item-right">
            <span class="filter-name">结亲对象村：</span>
            <div class="filter-content">
                <select name="contryName" id="contryName" class="form-control" notnull="false" onChange="searchList1();" style="width:168px;">
                ${mcodeSetting.getMcodeSelect("DM-XJJQC", contryNameValue, "1")}
                </select>
            <#--<input type="text" class="typeahead scrollable form-control"  autocomplete="off" data-provide="typeahead" id="contryName" value="${contryName!}">-->
            </div>
        </div>
    </div>
    <div id="auditingDiv"></div>
</div>
<script>
    $(function(){
        searchList1();
    })
    function searchList1(){
        var activityId = $('#activityId').val();
        var options=$("#contryName option:selected");
        var contryName = "";
        if(options.val()) {
            contryName = options.text();
        }
        var url = "${request.contextPath}/familydear/registerAudit/auditingList?activityId="+activityId+"&contryName="+encodeURIComponent(contryName);
        $('#auditingDiv').load(url);
    }
    </script>