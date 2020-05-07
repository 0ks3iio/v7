<div class="filter-made mb-10">
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="editQualityRuleRelation('');">新增规则</button>
	</div>
</div>
<div class="table-container-body">
    <table class="tables">
        <thead>
        <tr>
            <th>规则名称</th>
            <th>规则类型</th>
            <th>所属维度</th>
            <th>计算规则</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
            <#if  ruleRelations?exists &&ruleRelations?size gt 0>
            <#list ruleRelations as item>
        <tr>
            <td>${item.name!}</td>
            <td><#if item.ruleType! ==1>表规则<#elseif item.ruleType! ==2>字段规则<#elseif item.ruleType! ==3>任务规则</#if></td>
            <td>${item.dimName!}</td>
            <td>${mcodeSetting.getMcode("DM-BG-JSGZ","${item.computerType!}")}</td>
            <td>
                <a href="javascript:void(0);" onclick="editQualityRuleRelation('${item.id!}')">编辑</a><span class="tables-line">|</span>
                <a href="javascript:void(0);" onclick="deleteQualityRuleRelation('${item.id!}')">删除</a>
            </td>
        </tr>
            </#list>
        <#else>
            <tr >
                <td  colspan="5" align="center">
                    暂无数据规则
                </td>
            <tr>
        </#if>
        </tbody>
    </table>
</div>