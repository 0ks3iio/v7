<div class="row">
    <div class="col-xs-12">
        <div id="addScaleDiv">
        <div class="box box-default">
            <div class="box-body clearfix" style="padding-top: 0;">
                <div class="row">
                    <div class="evaluate-body col-xs-12"   style="min-height: 0px">
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
                                                        <select name="acadyear" id="acadyear" class="form-control"  onchange="acadyearSearch()">
                                                        <#if acadeyearList?exists && (acadeyearList?size gt 0)>
                                                            <#list acadeyearList as item>
                                                                <option value="${item!}" <#if acadyear?default('') == item> selected </#if> >${item!}</option>
                                                            </#list>
                                                        </#if>
                                                        </select>
                                                    </div>
                                                </div>
                                                <span class="filter-name color999">学期：</span>
                                                <div class="filter-content minh">
                                                    <select vtype="selectOne" class="form-control" id="semester" name="semester" onchange="semesterSearch()">
                                                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                                    </select>
                                                </div>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <th width="150" style="vertical-align: top;">
                                            年级：
                                        </th>
                                        <td>
                                            <div class="outter">
                                                <#if acadeyearList?exists && (acadeyearList?size gt 0)>
                                                    <#list gradeList  as grade>
                                                        <a <#if  grade.id == gradeId?default("")>class="selected"</#if> href="javascript:void(0);"  data-id="${grade.id!}" data-name="${grade.gradeName!}" data-code="${grade.gradeCode}">${grade.gradeName!}</a>
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
                        <#--   box开始   -->
                        <div id="ScaleBox">

                        </div>
                        <#--   box结束   -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script type="text/javascript">
    $(function() {
        semesterSearch();
        $(".evaluate-body").css("min-height", $("#sidebar").height() - 125);
        $(".point-probox-right").find(".point-pro-item").each(function(i, e) {if (i > 1) {$(e).css("border-top", "0");}});
        $(".picker-table").find(".outter a").click(function() {
                $(this).siblings().removeClass("selected");
                $(this).addClass("selected");
                   semesterSearch();
            });
        $(window).resize(function() {
            $(".evaluate-body").css("min-height", $("#sidebar").height() - 125);
        });
    });


    //新建规则
    function newScale() {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        var url = '${request.contextPath}/stutotality/rule/add/index?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId;
        $('#addScaleDiv'). load(url);
    }

    //同步规则
    var isSubmit=false;
    function syncRule() {
        if(isSubmit){
            return;
        }
        isSubmit=true;
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        var gradeCode =$(".outter").find("a.selected").attr("data-code");
        $.ajax({
            url:"${request.contextPath}/stutotality/rule/sync",
            data:{"acadyear":acadyear,"semester":semester,"gradeId":gradeId,'gradeCode':gradeCode},
            type:'post',
            dataType:'json',
            success:function(data) {
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layer.msg("同步成功", {
                        offset: 't',
                        time: 2000
                    });
                    isSubmit=false;
                    semesterSearch();
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg(XMLHttpRequest.status);
            }

        });

    }
    //学年查询
    function acadyearSearch() {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var url = '${request.contextPath}/stutotality/rule/index/page?acadyear='+acadyear+"&semester="+semester;
        $('#addScaleDiv').load(url);
    }
    //学期 年级 查询
    function semesterSearch() {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        var gradeCode =$(".outter").find("a.selected").attr("data-code");
        var url = '${request.contextPath}/stutotality/rule/show?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&gradeCode="+gradeCode;
        $('#ScaleBox').load(url);
    }
    //保存后查询当前年级
    function gradeNow(id) {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId =$(".outter").find("a.selected").attr("data-id");
        var url = '${request.contextPath}/stutotality/rule/show?acadyear='+acadyear+"&semester="+semester+"&gradeId="+id;
        $('#ScaleBox').load(url);
    }
</script>