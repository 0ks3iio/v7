<div class="evaluate-body col-xs-12">
<#--    <div class="filter evaluate-filter">-->
<#--        <div class="filter-item">-->
<#--            <button class="btn btn-blue font-14" onclick="itemTemplateShow()">-->
<#--                评价项目模板-->
<#--            </button>-->
<#--        </div>-->
<#--        <div class="filter-item filter-item-right">-->
<#--            <span class="filter-name">学期：</span>-->
<#--            <div class="filter-content">-->
<#--                <select vtype="selectOne" class="form-control" id="semester" name="semester" onchange="itemTableShow()" >-->
<#--                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}-->
<#--                </select>-->
<#--            </div>-->
<#--        </div>-->
<#--        <div class="filter-item filter-item-right">-->
<#--            <span class="filter-name">学年：</span>-->
<#--            <div class="filter-content">-->
<#--                <select name="acadyear" id="acadyear" onChange="changeClsAndSearchList()"  class="form-control" >-->
<#--                <#if acadeyearList?exists && (acadeyearList?size gt 0)>-->
<#--                    <#list acadeyearList as item>-->
<#--                        <option value="${item!}" <#if acadyear == item?default('')> selected </#if> >${item!}</option>-->
<#--                    </#list>-->
<#--                <#else>-->
<#--                    <option value="">暂无数据</option>-->
<#--                </#if>-->
<#--                </select>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->

<#--    <div class="evaluate-item">-->
<#--        <div class="picker-table evaluate-picker-table">-->
<#--            <table class="table">-->
<#--                <tbody>-->
<#--                <tr>-->
<#--                    <th width="150" style="vertical-align: top;">年级：</th>-->
<#--                    <td><div class="outter">-->
<#--                            <#if gradeList?exists && (gradeList?size gt 0)>-->
<#--                                <#list gradeList  as grade>-->
<#--                                     <a <#if  grade.id==gradeId?default("")>class="selected"</#if> href="javascript:void(0);"  data-id="${grade.id!}" data-code="${grade.gradeCode!}"  data-name="${grade.gradeName!}">${grade.gradeName!} </a>-->
<#--                                </#list>-->
<#--                            </#if>-->
<#--                        </div>-->
<#--                    </td>-->
<#--                    <td width="100" style="vertical-align: top;">-->
<#--                        <div class="outter">-->
<#--                        </div>-->
<#--                    </td>-->

<#--                </tr>-->
<#--                </tbody>-->
<#--            </table>-->
<#--        </div>-->
<#--    </div>-->

    <div class="evaluate-item">
        <div class="picker-table">
            <table class="table">
                <tbody>
                <tr>
                    <th width="150" style="vertical-align: top;">学年学期：</th>
                    <td>
                        <div class="filter-item mb0">
                            <div class="filter-item mb0">
                                <span class="filter-name color999">学年：</span>
                                <div class="filter-content minh">
                                    <select name="acadyear" id="acadyear" onChange="changeClsAndSearchList()"  class="form-control" >
                                    <#if acadeyearList?exists && (acadeyearList?size gt 0)>
                                        <#list acadeyearList as item>
                                            <option value="${item!}" <#if acadyear == item?default('')> selected </#if> >${item!}</option>
                                        </#list>
                                    <#else>
                                        <option value="">暂无数据</option>
                                    </#if>
                                    </select>
                                </div>
                            </div>
                            <span class="filter-name color999">学期：</span>
                            <div class="filter-content minh">
                                <select vtype="selectOne" class="form-control" id="semester" name="semester" onchange="itemTableShow()" >
                                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                </select>
                            </div>
                        </div>
                    </td>
                    <td>
                                    <button class="btn btn-blue font-14" onclick="itemTemplateShow()">
                                        评价项目模板
                                    </button>
                    </td>
                </tr>
                <tr>
                    <th width="150" style="vertical-align: top;">
                        年级：
                    </th>
                    <td>
                        <div class="outter">
                                <#if gradeList?exists && (gradeList?size gt 0)>
                                    <#list gradeList  as grade>
                                         <a <#if  grade.id==gradeId?default("")>class="selected"</#if> href="javascript:void(0);"  data-id="${grade.id!}" data-code="${grade.gradeCode!}"  data-name="${grade.gradeName!}">${grade.gradeName!} </a>
                                    </#list>
                                </#if>
                        </div>
                    </td>
                    <td width="100" style="vertical-align: top;">
                        <div class="outter">
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
<#-- 表格开始-->
    <div class="evaluate-item" id="itemTableShowDiv">

    </div>
<#--    表格结束-->
</div>

<script type="text/javascript">
    $(function() {
        $(".evaluate-body").css("min-height", $("#sidebar").height() - 125);
        itemTableShow();
        $(".picker-table").find(".outter a").click(function() {
            //年级查询
             $(this).siblings().removeClass("selected");
             $(this).addClass("selected");
             itemTableShow();
            });

    });

    //模板
    function itemTemplateShow() {
        var url = '${request.contextPath}/stutotality/typetemplate/show';
        $('#templateTabShow').load(url);
    }

    //学期年级查询
    function itemTableShow() {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        var gradeName =$(".outter").find("a.selected").attr("data-name");
        var gradeCode =$(".outter").find("a.selected").attr("data-code");
        if(!gradeId){
            gradeId="";
        }
        var url = '${request.contextPath}/stutotality/item/tableShow?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&gradeName="+gradeName+"&gradeCode="+gradeCode;
        $('#itemTableShowDiv').load(url);
    }
    //学年查询
    function changeClsAndSearchList() {
        var acadyear = $("#acadyear").val();
        var semester = $('#semester').val();
        var url = '${request.contextPath}/stutotality/item/show/index?acadyear='+acadyear+"&semester="+semester;
        $('#showTabList').load(url);
    }

</script>
