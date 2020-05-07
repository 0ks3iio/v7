<#import "/fw/macro/webmacro.ftl" as w>

<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/js/jquery.form.js"></script>
<#if errorMsg?default('')!=''>
<div class="widget-box" style="margin-top:80px;margin-bottom:80px;">
    <div class="widget-body">
        <div class="widget-main">
            <p class="alert alert-warning center">${errorMsg!}</p>
        </div>
    </div>
</div>
<div class="clearfix form-actions center">
    <@w.btn btnId="studentFlowIn-errorYes" btnClass="fa-check" btnValue="确定" />
</div>
<script>
    $('.page-content-area').ace_ajax('loadScripts', [], function() {
        $("#studentFlowIn-close").on("click",function(){
            layer.closeAll();
        });
        $("#studentFlowIn-errorYes").on("click",function(){
            $(".js-inSchool").unbind().addClass("disabled");
            layer.closeAll();
        });
    });
</script>
<#else>
<form id="article21" enctype="multipart/form-data">
<div class="row"  style="width:600px;">
    <input type="hidden" id="type" name="type" value="${type!}" />
    <input type="hidden" id="id" name="id" value="${webArticle.id!}" />
    <input type="hidden" id="commitUserId" name="commitUserId" value="${webArticle.commitUserId!}" />
    <input type="hidden" id="commitUnitId" name="commitUnitId" value="${webArticle.commitUnitId!}" />
    <input type="hidden" id="commitState" name="commitState" value="2" />
    <input type="hidden" id="titleImageUrl" name="titleImageUrl" value="${webArticle.titleImageUrl!}" />
    <input type="hidden" id="isDeleted" name="isDeleted" value="${webArticle.isDeleted?default('0')}">
    <div class="clearfix">
        <div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">

            <div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10" style="padding-top: 10px;" id="gradeDiv">
                <label for="" class="filter-name">地区名称：</label>
                <div class="filter-content">
                    <#--<input class="form-control" nullable="false" type="text" id="title" value="${webArticle.title!}" style="width: 200px;" />-->
                    <select class="form-control" name="regionCode" id="title" style="width: 200px;">
                        <option value="">请选择地区</option>
                        <#if regions?exists && regions?size gt 0>
                            <#list regions as region>
                                <option value="${region.fullCode!}" <#if regionCode?default('')==region.fullCode>selected="selected"</#if>>${region.regionName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>

            <div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
                <label for="" class="filter-name">图片：</label>
                <div class="filter-content">
                    <#--<span id="span1" class="upload-span" style="width:80px;"><a href="javascript:void(0)" class="">选择文件</a></span>-->
                    <input id="span1" type="file" name="titleImage"  />
                    <input id="uploadFilePath1" name="titleImageName"  type="text" class="input-txt input-readonly" readonly="readonly" style="width:200px;height:30px;" value="${webArticle.titleImageName!}" maxLength="125"/>
                </div>
            </div>
        </div>
    </div>

    <div class="clearfix form-actions center">
        <a href="javascript:;" id="btn-commit1" class="btn btn-primary ">确定</a>
        <a href="javascript:;" id="btn-close2" class="btn btn-primary ">取消</a>
    </div>
</div>
</form>
</#if>
<script>

    $('.page-content-area').ace_ajax('loadScripts', [], function() {
        $("#btn-close2").on("click",function(){
            layer.closeAll();
        });
        $("#btn-commit1").on("click",function(){
            $("#btn-commit1").addClass("disabled");
            var check = checkVal();
            if(!check){
                $("#btn-commit1").removeClass("disabled");
                return ;
            }
            if($("#commitState")==''){
                $("#commitState").val('2');
            }
            var regionCode = $("#title").val();
            var options={
                url: '${request.contextPath}/sitedata/${type!}/saveorupdate',
                type: 'post',
                datatype:'json',
                contentType:'application/json',
                cache: false,
                success:showReply
            };
            // 提交数据
            $("#article21").ajaxSubmit(options);
        });
    });

    function showReply(data) {
        var jsonO = JSON.parse(data);
        if (!jsonO.success) {
            showMsgError(jsonO.msg, "操作失败!", function (index) {
                layer.close(index);
                $("#btn-commit1").removeClass("disabled");
            });
        }
        else {
            // 需要区分移动端和非移动端返回处理不一样
            showMsgSuccess('保存成功', "操作成功!", function (index) {
                location.href="#${request.contextPath}/sitedata/${type!}/index/page";
                location.reload(true);                                        ///sitedata/10/index/page
                layer.closeAll();
            });
        }
    }

    function checkVal(){
        var title = $("#title").val();
        var titleLink = $("#uploadFilePath1").val();
        var isOk = true;

        if(isNull(title)){
            layer.tips("地区 不能为空", "#title" , {
                tipsMore: true,
            });
            isOk = false;
        }
        if(isNull(titleLink)){
            layer.tips("图片 不能为空", "#uploadFilePath1" , {
                tipsMore: true,
            });
            isOk = false;
        }
        var fileName = titleLink;
        var type = fileName.substring(fileName.lastIndexOf(".")+1);

        submit = false;
        if($.trim(type)!='jpeg' && $.trim(type) != 'jpg' && $.trim(type) != 'png'){
            layer.tips('文件类型错误,请选择正确的文件类型','#uploadFilePath1',{tipsMore:true});
            isOk = false;
        }
        return isOk;
    }
    function isNull(str){
        return !str && str.replace(/(^s*)|(s*$)/g, "").length==0;
    }

    //处理上传文件样式
    $(document).ready(function () {
        initFileInput();
    });
    function resetFilePos(){
        $("#span1").css({"position":"absolute","-moz-opacity":"0","opacity":"0","filter":"alpha(opacity=0)","cursor":"pointer","width":$(".upload-span a").width(),"height":$(".upload-span").height()});
        $("#span1").offset({"left":$("#span1").offset().left});
        $("#span1").css({"display":""});
    }
    function initFileInput(){
        $("#span1").mouseover(function(){
            $("#span1").offset({"top":$("#span1").offset().top });
        });

        $("#span1").on("change",function(){
            var p1 = $("#span1").val().lastIndexOf("\\");
            var fileName = $("#span1").val().substring(p1+1);
            var type = fileName.substring(fileName.lastIndexOf(".")+1);
            if($.trim(type)==''){
                return ;
            }
            submit = false;
            if($.trim(type)!='jpeg' && $.trim(type) != 'jpg' && $.trim(type) != 'png'){
                layer.tips('文件类型错误,请选择正确的文件类型','#uploadFilePath1',{tipsMore:true});
            }
            $('#uploadFilePath1').val(fileName);
        });

        resetFilePos();
    }
</script>