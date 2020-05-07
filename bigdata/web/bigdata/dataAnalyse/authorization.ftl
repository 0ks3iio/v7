<form id="myForm">
    <div class="tab-content">
        <div class="tab-pane active">
            <input type="hidden" value="${favorite.id!}" name="id">
            <input type="hidden" value="${favorite.unitId!}" name="unitId">
            <input type="hidden" value="${favorite.userId!}" name="userId">
            <input type="hidden" value="${favorite.orderType!}" name="orderType">
            <div class="order-type pb-20 ml-15">
                <#if orderTypes?? && orderTypes?size gt 0>
                    <#list orderTypes as orderType>
                        <label class="choice">
                            <input type="radio" <#if orderType.orderType==favorite.orderType>checked</#if> value="${orderType.orderType!}" class="wp" name="class-radio">
                            <span class="choice-name"> ${orderType.orderName!}</span>
                        </label>
                    </#list>
                </#if>
            </div>
            <div class="order-tree">
                <div class="order-main-div">

                </div>
            </div>
        </div>
    </div>
</form>
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.basicEdit.js"/>
<script>
    var _chart_json = ${favoriteJson!};
</script>