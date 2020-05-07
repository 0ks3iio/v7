<#if stutotalityHealthOptions?exists&&(stutotalityHealthOptions?size>0)>
<div class="filter">
    <div class="filter-item">
        <button class="btn btn-blue mr10 font-14" onclick="saveResult()">
            保存
        </button>
    </div>
</div>
<form id="subForm" >
    <input type="hidden" name="studentId" value="${studentId!}"/>
    <input type="hidden" name="gradeId" value="${gradeId!}"/>
    <div>
      <table class="table table-bordered table-striped table-hover typein-table">
        <thead>
        <th class="evaluate-center">项目</th>
        <th class="evaluate-center">达标标准</th>
        <th class="evaluate-center">评测结果</th>
        </thead>
        <tbody>
                <#list stutotalityHealthOptions as item>
                <tr>
                    <td class="evaluate-center">${item.healthName!}</td>
                    <td class="td-no-con evaluate-center">${item.healthStandard!}</td>
                    <td class="evaluate-center">
                        <#if item.healthName?default("")=="视力">
                            <div class="eye-input-box">
                                左：
                                <input type="text" name="healthOptions[${item_index}].result"  class="form-control evaluate-control eye-input result" value="${item.result!}" placeholder="请输入" />
                            </div>
                            <div class="eye-input-box">
                                右：
                                <input type="text"  name="healthOptions[${item_index}].result2"  class="form-control evaluate-control eye-input result" value="${item.result2!}" placeholder="请输入" />
                            </div>
                        <#else >
                             <div><#--id="result${item_index}"-->
                                 <input type="text" name="healthOptions[${item_index}].result"   class="form-control evaluate-control result" value="${item.result!}" <#--onblur="checkNum(this);"--> placeholder="请输入"/>
                             </div>
                             <div class="col-sm-4 control-tips tip-false"></div>
                        </#if>
                        <input type="hidden" name="healthOptions[${item_index}].resultId" value="${item.resultId!}"/>
                        <input type="hidden" name="healthOptions[${item_index}].healthId" value="${item.healthId!}"/>
                        <input type="hidden" name="healthOptions[${item_index}].id" value="${item.id!}"/>
                        <input type="hidden" name="healthOptions[${item_index}].healthName" value="${item.healthName!}"/>
                    </td>

                </tr>
                </#list>

        </tbody>
    </table>
    </div>
</form>
<div class="evaluate-item"></div>
<#else >
    <div class="no-data-container">
        <div class="no-data">
    <span class="no-data-img">
        <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
    </span>
            <div class="no-data-body">
                <p class="no-data-txt">暂无相关数据</p>
            </div>
        </div>
    </div>
</#if>
<script>
    container = $("#showTabDiv").html();
    function checkNum(that){
        var num = $(that).val();
        var reg = /^\d+(\.\d{1})?$/;
        var id = that.attr("id");
        if(!num || (reg.test(parseFloat(num))&&!(parseFloat(num)>=10000))){
            // addSuccess(id,"");
            return true;
        }else{
            // addError(id,"数据格式不符合规则!(必须为不超过1000正整数，保留一位小数)");
            // return false;
            layer.tips('格式不正确(最多4位整数，1位小数)!', $(that), {
                tipsMore: true,
                tips: 3
            });
            return false;
        }
    }

    function addError(id,errormsg){
        if(!errormsg){
            errormsg='错误';
        }
        $("#"+id).parent().siblings(".control-tips").addClass("tip-false");
        $("#"+id).parent().siblings(".control-tips").html('<span class="has-error"><i class="fa fa-times-circle"></i>'+errormsg+'</span>');
    }

    function addSuccess(id,msg){
        if(!msg){
            msg='正确';
        }
        $("#"+id).parent().siblings(".control-tips").removeClass("tip-false");
        $("#"+id).parent().siblings(".control-tips").html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;'+msg+'</span>');
    }
    function saveResult() {
        var flag = true;
        $(".form-control.evaluate-control.result").each(function () {
            var isNum = checkNum($(this));
            if(!isNum){
                flag = false;
            }
        })
        if(!flag){
            return;
        }
        var url =  '${request.contextPath}/stutotality/result/saveResultByHealth?';
        var options = {
            url : url,
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layer.msg("保存成功", {
                        offset: 't',
                        time: 2000
                    });
                    var gradeId = '${gradeId!}';
                    var studentId = '${studentId!}';
                    var url =  '${request.contextPath}/stutotality/result/getStuPhysicalQualityList?studentId='+studentId+"&gradeId="+gradeId;
                    $("#divList1").load(url);
                    isSubmit=false;
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
</script>