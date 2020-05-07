

<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
    <form id="subForm">
        <div class="table-container-body" style="height:400px;overflow-y:auto;overflow-x: hidden;" >
            <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th width="20%" >结亲干部姓名</th>
                    <th width="20%" >部门</th>
                    <th width="10%" >手机号</th>
                    <th width="10%" >结亲开始时间</th>
                    <th width="10%" >结束时间</th>
                    <th width="30%" >原因</th>
                </tr>
                </thead>
                <tbody id="familyMemberTable">
                <#if  cadreList?exists && (cadreList?size gt 0)>
                    <#list cadreList as item>
                        <tr>
                            <td>${item.teacherName!}</td>
                            <td>${item.deptName!}</td>
                            <td>${item.mobilePhone!}</td>
                            <td>${(item.creationTime?string('yyyy-MM-dd'))?if_exists}</td>
                            <td>${(item.modifyTime?string('yyyy-MM-dd'))?if_exists}</td>
                            <td>${item.remark!}</td>
                        </tr>
                    </#list>

                </#if>

                </tbody>
            </table>


        </div>
    </form>
</div>
<#--<div class="layer-footer">-->
    <#--<a class="btn btn-lightblue" id="arrange-commit">确定</a>-->
    <#--<a class="btn btn-grey" id="arrange-close">取消</a>-->
<#--</div>-->
