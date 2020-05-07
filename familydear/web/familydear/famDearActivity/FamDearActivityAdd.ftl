<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/layer/layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/moment/min/moment-with-locales.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/js/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/css/bootstrap-datetimepicker.min.css">

<!-- 新增 -->
		<div id="myDiv" class="layer-body" style="height:350px;overflow-y: scroll;overflow-x: hidden; ">
            <form id="subForm">
                <input type="hidden" name="createUserId" value="${createUserId!}">
                <input type="hidden" name="unitId" value="${unitId!}">
            <div class="layer-content">
                <p><b>基础信息</b></p>
                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>年度：</label>
                        <div class="col-sm-5">
                            <select class="form-control" id="year" name="year" onchange="yearChange1()">
                            <#if acadyearList?exists && (acadyearList?size>0)>
                            <#list acadyearList as item>
                            <option value="${item!}" <#if year?default('a')==item?default('b')>selected</#if>>${item!}</option>
                            </#list>
                            <#else>
                            <option value="">未设置</option>
                            </#if>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>结亲计划：</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="planList1" name="planId" style="width: 100%">
                                <#if planList?exists && (planList?size>0)>-->
                                <#list planList as item>
                                <option value="${item.id!}" <#if planId?default('a')==item.id>selected</#if>>${item.title!}</option>
                                </#list>
                                <#else>
                                <option value="">未设置</option>
                                </#if>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>活动标题：</label>
                        <div class="col-sm-10">
                            <textarea cols="30" rows="3" class="form-control" maxlength="100" id="activityTitle" name="title" ></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>文件内容：<br>
                        </label>
                        <div class="col-sm-9" style="z-index:900;">
                            <input type="hidden" id="activityContent" name="fileContent" value=""/>
                            <textarea id="activityContent1" ></textarea>
                        </div>
                    </div>
                    <#--<div class="form-group" style="height: 400px">-->
                        <#--<label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>文件内容：</label>-->
                        <#--<div class="sendBulletin-form">-->
                            <#--<textarea cols="30" rows="7" type="text/plain" class="form-control" maxlength="5000" id="activityContent" name="fileContent"></textarea>-->
                        <#--</div>-->
                    <#--</div>-->
                    <#--<div class="form-group">-->
                        <#--<label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>活动内容：</label>-->
                        <#--<div class="col-sm-10">-->
                            <#--<textarea cols="30" rows="3" class="form-control" maxlength="1000" id="activityContent" name="content"></textarea>-->
                        <#--</div>-->
                    <#--</div>-->
                </div>
                <p><b>活动安排</b></p>
                <table class="table table-striped table-bordered table-hover no-margin" id="tab">
                    <thead>
                    <tr>
                        <th width="100">批次</th>
                        <th width="150">活动开始时间</th>
                        <th width="150">活动结束时间</th>
                        <th width="100">结亲村</th>
                        <th width="100">干部人数</th>
                        <th width="150">报名开始时间</th>
                        <th width="150">报名结束时间</th>
                        <th width="100">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <#--<td><input type="text" class="form-control" value="第一批次"></td>-->
                        <#--<td><input type="text" class="form-control" value="2019.03.03-2019.03.09"></td>-->
                        <#--<td><input type="text" class="form-control" value="拜什克然木村"></td>-->
                        <#--<td><input type="text" class="form-control" value="36"></td>-->
                        <#--<td><input type="text" class="form-control" value="2019.03.03"></td>-->
                        <#--<td>-->
                            <#--<a class="table-btn color-blue" href="javascript:void(0)">删除</a>-->
                        <#--</td>-->
                    </tr>
                    <tr>
                        <td colspan="8" class="text-center"><a class="color-blue" href="#" onclick="tt()">+新增安排</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            </form>
        </div>
<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
</div>
 <style type="text/css">
     #edui_fixedlayer{z-index:9999 !important;}
     .sendBulletin-form .edui-default .edui-editor{width:824px !important;}
     body.widescreen .sendBulletin-form .edui-default .edui-editor{width:1024px !important;}
 </style>
<script>

    var ue = UE.getEditor('activityContent1',{
        //focus时自动清空初始化时的内容
        autoClearinitialContent:false,
        //关闭字数统计
        wordCount:false,
        //关闭elementPath
        elementPathEnabled:false,
        //默认的编辑区域高度
        toolbars:[[
        ]],
        initialFrameHeight:300,
        initialFrameWidth:695
        //更多其他参数，请参考ueditor.config.js中的配置项
    });

    var num=0;
    function tt() {
        var html = "<tr>" +
                "<td><input class='form-control' title='' onmouseover='this.title = this.value' name='famDearArrangeList["+num+"].batchType'/></td>" +
                "<td><input class='form-control date-picker' vtype='data' name='famDearArrangeList["+num+"].startTime'/></td>" +
                "<td><input class='form-control date-picker' vtype='data' name='famDearArrangeList["+num+"].endTime'/></td>" +
                "<td><select multiple='multiple'  class='form-control village' notnull='false' style='width:150px;' name='famDearArrangeList["+num+"].ruralValue' data-placeholder='结亲村'>${mcodeSetting.getMcodeSelect('DM-XJJQC', '', '1')} </select></td>"+
                // "<td><input class='form-control' name='famDearArrangeList["+num+"].rural'/></td>" +
                "<td><input class='form-control' name='famDearArrangeList["+num+"].peopleNumber' style='width:50px;'/></td>"+
                "<td><input  class='form-control date-picker' vtype='data' name='famDearArrangeList["+num+"].applyTime'/></td>"+
                "<td><input  class='form-control date-picker' vtype='data' name='famDearArrangeList["+num+"].applyEndTime'/></td>"+
                "<td onclick='delTr(this)'> <a class='table-btn color-blue' href='javascript:void(0)'>删除</a> </td>"+
                "</tr>";
        addTr("tab","last",html);
    }
    function addTr(tab, row, trHtml){
        //获取table最后一行 $("#tab tr:last")
        //获取table第一行 $("#tab tr").eq(0)
        //获取table倒数第二行 $("#tab tr").eq(-2)
        // var $tr=$("#"+tab+" tr").eq(row);
        var $tr = $("#tab tr").eq(-2);
        if($tr.size()==0){
            alert("指定的table id或行数不存在！");
            return;
        }
        $tr.after(trHtml);
        num=num+1;
        var viewContent = {
            'format': 'yyyy-mm-dd',
            'minView': '2'
        };
        initCalendarData("#myDiv", ".date-picker", viewContent);

        $('.form-control.village').chosen({
            width:'150px',
            results_height:'100px',
            multi_container_height:'100px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });

        var scrollHeight = $('#myDiv').prop("scrollHeight");
        $('#myDiv').scrollTop(scrollHeight,200);

    }

    $(function() {
        //初始化日期控件
        var viewContent = {
            'format': 'yyyy-mm-dd',
            'minView': '2'
        };
        initCalendarData("#myDiv", ".date-picker", viewContent);
    })

    function delTr(tt){
        //获取选中的复选框，然后循环遍历删除
        // var ckbs=$("input[name="+ckb+"]:checked");
        // if(ckbs.size()==0){
        //     alert("要删除指定行，需选中要删除的行！");
        //     return;
        // }
        showConfirmMsg('删除该批次活动？', '提示',function(ii){
            $(tt).parent().remove();
            //layer.closeAll();
            layer.close(ii);
        })
        // num=num-1;
    }
    function yearChange1(){
        var year = $("#year").val();
        var url = "${request.contextPath}/familydear/activity/edu/yearChange";
        $.ajax({
            url:url,
            data:{'year':year},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#planList1").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#planList1").html(htmlStr);
            }

        })
    }
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    var isSubmit=false;

    $("#arrange-commit").on("click",function () {
        isSubmit=true;
        var acadyear = $("#year").val();
        if(!acadyear){
            layerTipMsg(false,"提示!","年份不能为空!");
            isSubmit=false;
            return;
        }
        var plan = $("#planList1").val();
        if(!plan||plan=="未设置"){
            layerTipMsg(false,"提示!","计划不能为空!");
            isSubmit=false;
            return;
        }
        var title =$("#activityTitle").val();
        if(!title){
            layerTipMsg(false,"提示!","活动标题不能为空!");
            isSubmit=false;
            return;
        }
        // var guideThought = $("#activityRequire").val();
        // if(!guideThought){
        //     layerTipMsg(false,"提示!","访亲要求不能为空!");
        //     isSubmit=false;
        //     return;
        // }
        var workObjective = UE.getEditor('activityContent1').getContent();
        // var workObjective= $("#activityContent1").val();
        if(!workObjective){
            layerTipMsg(false,"提示!","文件内容不能为空!");
            isSubmit=false;
            return;
        }
        $("#activityContent").val(workObjective);
        // $("#tab").find('tr').each(function () {
        //     alert($(this).find('td').eq(0).find('input').value);
        // });
        var workObjective= $("#activityContent").val();
        if(!workObjective){
            layerTipMsg(false,"提示!","活动内容不能为空!");
            isSubmit=false;
            return;
        }
        var isBatch=true;
        var overTime;
        var isStartTime=true;
        var isEndTime=true;
        var isApplyStartTime=true;
        var isApplyEndTime=true;
        var overApplyTime;
        var isRural=true;
        var isPeopleNumber=true;
        var isNumber = true;
        var isCorrectTime = true;
        $("#tab").find('tr').each(function () {
            var startTime,endTime,applyStartTime,applyEndTime;
            if($(this).find('td').eq(0).find('input').length>0){
                var batch = $(this).find('td').eq(0).find('input').val();
                if(!batch){
                    isBatch = false;
                }
            }
            if($(this).find('td').eq(1).find('input').length>0) {
                startTime = $(this).find('td').eq(1).find('input').val();
                if(!startTime){
                    isStartTime = false;
                }
            }
            if($(this).find('td').eq(2).find('input').length>0){
                endTime = $(this).find('td').eq(2).find('input').val();
                if(!endTime){
                    isEndTime = false;
                }
            }
            if($(this).find('td').eq(5).find('input').length>0){
                applyStartTime = $(this).find('td').eq(5).find('input').val();
                if(!applyStartTime){
                    isApplyStartTime = false;
                }
            }
            if($(this).find('td').eq(6).find('input').length>0){
                applyEndTime = $(this).find('td').eq(6).find('input').val();
                if(!applyEndTime){
                    isApplyEndTime = false;
                }
            }
            if($(this).find('td').eq(3).find('input').length>0){
                var rural = $(this).find('td').eq(3).find('select').val();
                if(!rural){
                    isRural = false;
                }
            }
            if($(this).find('td').eq(4).find('input').length>0){
                var peopleNumber = $(this).find('td').eq(4).find('input').val();
                if(!peopleNumber){
                    isPeopleNumber = false;
                }else {
                    var re = /^[1-9]+[0-9]*]*$/;
                    if(!re.test(peopleNumber)){
                        isNumber = false;
                    }
                }

            }
            if(startTime>endTime){
                overTime=true
                return;
            }

            if(applyStartTime>applyEndTime){
                overApplyTime=true
                return;
            }

            if(applyEndTime>endTime){
                isCorrectTime=false
                return;
            }
        });
        if(!isBatch){
            layerTipMsg(false,"提示!","批次不能为空!");
            isSubmit=false;
            return;
        }
        if(!isStartTime){
            layerTipMsg(false,"提示!","活动开始时间不能为空!");
            isSubmit=false;
            return;
        }
        if(!isEndTime){
            layerTipMsg(false,"提示!","活动结束时间不能为空!");
            isSubmit=false;
            return;
        }if(!isApplyStartTime){
            layerTipMsg(false,"提示!","报名开始时间不能为空!");
            isSubmit=false;
            return;
        }
        if(!isApplyEndTime){
            layerTipMsg(false,"提示!","报名结束时间不能为空!");
            isSubmit=false;
            return;
        }
        debugger;
        if(!isRural){
            layerTipMsg(false,"提示!","结亲村不能为空!");
            isSubmit=false;
            return;
        }
        if(!isPeopleNumber){
            layerTipMsg(false,"提示!","干部数不能为空!");
            isSubmit=false;
            return;
        }

        if(!isNumber){
            layerTipMsg(false,"提示!","干部数只能输入正整数!");
            isSubmit=false;
            return;
        }

        if(overTime){
            layerTipMsg(false,"提示","活动开始时间不能大于活动结束时间！");
            isSubmit=false;
            return;
        }
        if(overApplyTime){
            layerTipMsg(false,"提示","报名开始时间不能大于报名结束时间！");
            isSubmit=false;
            return;
        }

        if(!isCorrectTime){
            layerTipMsg(false,"提示","报名结束时间不能大于活动结束时间！");
            isSubmit=false;
            return;
        }
        var options = {
            url : "${request.contextPath}/familydear/activity/edu/saveActivity",
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
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    showList1();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    })

</script>