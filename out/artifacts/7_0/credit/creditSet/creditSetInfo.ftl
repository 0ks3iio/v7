<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="subForm">
    <div class="main-container" id="main-container">
        <div class="main-content">
            <div class="main-content-inner">
                <div class="page-content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-default">
                                <div class="box-body">
                                    <input type="hidden" name="id" value=${creditSet.id!}>
                                    <div class="form-horizontal">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-title no-padding-right">
                                                <span class="form-title">规则设置</span>
                                            </label>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">平时成绩：</label>
                                            <div class="col-sm-2">
                                                <div class="input-group form-num">
                                                    <input class="form-control creditScore" name="usualScore"  id="usualScore" type="text" placeholder="请输入平时成绩" value="${creditSet.usualScore!}" onchange="calSumScore()"/>
                                                    <span class="input-group-btn">
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-up" onclick="add(this)"></i>
                                                      </a>
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-down"  onclick="sub(this)"></i>
                                                      </a>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">模块成绩：</label>
                                            <div class="col-sm-2">
                                                <div class="input-group form-num">
                                                    <input class="form-control creditScore" type="text" name="moduleScore" id="moduleScore" placeholder="请输入模块成绩" value="${creditSet.moduleScore!}" onchange="calSumScore()"/>
                                                    <span class="input-group-btn">
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-up" onclick="add(this)"></i>
                                                      </a>
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-down"  onclick="sub(this)"></i>
                                                      </a>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">日常表现分：</label>
                                            <div class="col-sm-2">
                                                <div class="input-group form-num">
                                                    <input class="form-control creditScore" type="text" name="dailyScore" id="dailyScore" placeholder="请输入日常表现分" value="${creditSet.dailyScore!}" onchange="calSumScore()"/>
                                                    <span class="input-group-btn">
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-up" onclick="add(this)"></i>
                                                      </a>
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-down" onclick="sub(this)"></i>
                                                      </a>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">及格线：</label>
                                            <div class="col-sm-2">
                                                <div class="input-group form-num">
                                                    <input class="form-control creditScore" name="passLine" id="passLine" type="text" placeholder="请输入及格线" value="${creditSet.passLine!}"/>
                                                    <span class="input-group-btn">
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-up" onclick="add(this)"></i>
                                                      </a>
                                                      <a class="btn btn-default" type="button">
                                                        <i class="fa fa-angle-down" onclick="sub(this)"></i>
                                                      </a>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group credit-group">
                                            <div class="col-sm-2"></div>
                                            <div class="col-sm-2">
                                                <i class="credit-tip"></i>
                                                <input type="hidden" id="sum" name="sumScore" value="${creditSet.sumScore!}">
                                                <span class="credit-tip-text" id="sumSpan">总分：${creditSet.sumScore!}</span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">日常表现评分项：
                                                <div>
                                                    <a class="btn btn-second btn-info btn-sm btn-credit btn-white" id="edit-btn">编辑</a>
                                                </div>
                                            </label>
                                            <div class="col-sm-6">
                                                <table id="tab" class="table table-bordered table-striped table-hover no-margin">
                                                    <thead>
                                                    <tr>
                                                        <th>子项目</th>
                                                        <th>分项</th>
                                                        <th width="150">满分值</th>
                                                        <th>给分形式</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                <#assign index = 0>
                                                <#if creditSet.dailySetList?exists&& (creditSet.dailySetList?size > 0)>
                                                    <#list creditSet.dailySetList as item>
                                                        <#if item.parentId?default("")?trim?length lt 1>
                                                            <#if item.subSetList?exists&& (item.subSetList?size > 0)>
                                                                <#list item.subSetList as item1>
                                                                <tr>
                                                                    <#if item1_index ==0>
                                                                            <td rowspan=${item.subSetList?size}>${item.name!}</td>
                                                                        </#if>
                                                                    <input type="hidden" name="dailySetList[${index}].id" value=${item1.id!}>
                                                                    <input type="hidden" name="dailySetList[${index}].setId" value=${creditSet.id!}>
                                                                    <input type="hidden" name="dailySetList[${index}].parentId" value=${item1.parentId!}>
                                                                    <input type="hidden" name="dailySetList[${index}].name" value=${item1.name!}>
                                                                    <td>${item1.name!}</td>
                                                                    <td>
                                                                        <div class="input-group form-num">
                                                                            <input class="form-control creditScore" type="text" name="dailySetList[${index}].score" value="${item1.score!}" placeholder=${item1.name!}>
                                                                            <span class="input-group-btn">
                                                                                <a class="btn btn-default" type="button">
                                                                                    <i class="fa fa-angle-up" onclick="add(this)"></i>
                                                                                </a>
                                                                                <a class="btn btn-default" type="button">
                                                                                    <i class="fa fa-angle-down" onclick="sub(this)"></i>
                                                                                </a>
                                                                            </span>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <label class="no-margin-top">
                                                                            <input type="radio" name="dailySetList[${index}].scoreType" class="wp" value="1" <#if item1.scoreType?default("1")=="1">checked="checked"</#if > >
                                                                            <span class="lbl"> 打分</span>
                                                                        </label>
                                                                        <label class="no-margin-top">
                                                                            <input type="radio" name="dailySetList[${index}].scoreType" class="wp" value="2" <#if item1.scoreType?default("1")=="2">checked="checked"</#if >>
                                                                            <span class="lbl"> 扣分</span>
                                                                        </label>
                                                                    </td>
                                                                    <#assign index = index + 1>
                                                                </tr>
                                                                </#list>
                                                            </#if>
                                                        </#if>
                                                    </#list>
                                                </#if>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right"></label>
                                            <div class="col-sm-8">
                                                <a class="btn btn-blue" onclick="save()">保存</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</form>
<div class="layer layer-new" style="height: 600px">
</div>
<script>
    function calSumScore(){
        var usualScore = Number($("#usualScore").val());
        var moduleScore = Number($("#moduleScore").val());
        var dailyScore = Number($("#dailyScore").val());
        if(usualScore&&moduleScore&&dailyScore){
            var sumScore = usualScore+moduleScore+dailyScore;
            $("#sum").val(sumScore);
            $("#sumSpan").html("总分："+sumScore);
        }else {
            if(isNaN(usualScore)){
                layerTipMsg(false,"提示!","平时成绩必须为数字!");
                return;
            }
            if(isNaN(moduleScore)){
                layerTipMsg(false,"提示!","模块成绩必须为数字!");
                return;
            }
            if(isNaN(dailyScore)){
                layerTipMsg(false,"提示!","日常表现分必须为数字!");
                return;
            }
            $("#sum").val(0);
            $("#sumSpan").html("总分："+0);
        }
    }
    function add(_this){
        var tt = $(_this).parent().parent().parent().find("input[class='form-control creditScore']").val();
        // if(tt<$("#sum").val()) {
            $(_this).parent().parent().parent().find("input[class='form-control creditScore']").val(Number(tt) + 1)
        // }
    }
    function sub(_this){
        var tt = $(_this).parent().parent().parent().find("input[class='form-control creditScore']").val()
        if(tt>0) {
            $(_this).parent().parent().parent().find("input[class='form-control creditScore']").val(Number(tt) - 1)
        }
    }
    $(function() {
        //添加分类 data-id 是大类别的标识，删除啥的都靠他
        $("#edit-btn").on("click", function() {
            var url =  '${request.contextPath}/exammanage/credit/creditDailySetInfo?';
            $(".layer-new").load(url);
            layer.open({
                type: 1,
                shadow: 0.5,
                area: ["800px","600px"],
                title: "编辑",
                btn: ["确定", "取消"],
                content: $(".layer-new"),
                yes:function (index,layero) {
                    var url="${request.contextPath}/exammanage/credit/creditDailySet/save?"
                    var options = {
                        url : url,
                        dataType : 'json',
                        success : function(data){
                            var jsonO = data;
                            if(!jsonO.success){
                                layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                                isSubmit=false;
                                return;
                            }else{
                                layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                                layer.close(index);
                                search();
                            }
                        },
                        clearForm : false,
                        resetForm : false,
                        type : 'post',
                        error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
                    };
                    $("#subForm1").ajaxSubmit(options);
                },btn2: function(index, layero){
                    layer.close(index);
                }
            });
        });

    })
</script>