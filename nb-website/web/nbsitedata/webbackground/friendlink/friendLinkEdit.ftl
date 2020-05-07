<#import "/fw/macro/webmacro.ftl" as w>
<#import "/nbsitedata/webbackground/detailmacro.ftl" as d>
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
<form id="friendForm" enctype="multipart/form-data">
<div class="row" id="article" style="width:600px;">
    <input type="hidden" id="type" name="type" value="${type!}" />
    <input type="hidden" id="id" name="id" value="${webArticle.id!}" />
    <input type="hidden" id="commitUserId" name="commitUserId" value="${webArticle.commitUserId!}" />
    <input type="hidden" id="commitUnitId" name="commitUnitId" value="${webArticle.commitUnitId!}" />
    <input type="hidden" id="isDeleted" name="isDeleted" value="${webArticle.isDeleted?default('0')}">
    <input type="hidden" id="commitState" name="commitState" value="${webArticle.commitState?default('2')}">
    <div class="clearfix">
        <div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">

            <div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10" style="padding-top: 10px;" id="gradeDiv">
                <label for="" class="filter-name">链接名称：</label>
                <div class="filter-content">
                    <input class="form-control" maxlength="100" nullable="false" type="text" id="title" name="title" value="${webArticle.title!}" style="width: 300px;" />
                </div>
            </div>

            <div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
                <label for="" class="filter-name">链接地址：</label>
                <div class="filter-content">
                    <input class="form-control" maxlength="500" nullable="false" type="text" id="titleLink" name="titleLink" value="${webArticle.titleLink!}" style="width: 300px;" />
                </div>
            </div>
        </div>
    </div>

    <div class="clearfix form-actions center">
        <a href="javascript:;" id="btn-commit" class="btn btn-primary ">确定</a>
        <a href="javascript:;" id="btn-close" class="btn btn-primary ">取消</a>
    </div>
</div>
</form>
</#if>
<script>

    $('.page-content-area').ace_ajax('loadScripts', [], function() {
        $("#btn-close").on("click",function(){
            layer.closeAll();
        });
        $("#btn-commit").on("click",function(){
            $("#btn-commit").addClass("disabled");
            var check = checkVal();
            if(!check){
                $("#btn-commit").removeClass("disabled");
                return ;
            }
            var obj = new Object();
            obj = JSON.parse(dealValue('#article'));

            obj.commitState = '2';
            // 提交数据
            var options= {
                url: '${request.contextPath}/sitedata/${type!}/saveorupdate',
                type: 'post',
                datatype: 'json',
                contentType: 'application/json',
                cache: false,
                success:showReply
            };
            $("#friendForm").ajaxSubmit(options);
        });
    });

    function showReply(data) {
        var jsonO = JSON.parse(data);
        if (!jsonO.success) {
            showMsgError(jsonO.msg, "操作失败!", function (index) {
                layer.close(index);
                $("#btn-commit").removeClass("disabled");
            });
        }
        else {
            // 需要区分移动端和非移动端返回处理不一样
            showMsgSuccess('保存成功', "操作成功!", function (index) {
                location.href="#${request.contextPath}/sitedata/${type!}/index/page";
                location.reload(true);
                ///sitedata/10/index/page
                layer.closeAll();
            });
        }
    }

    function checkVal(){
        var title = $("#title").val();
        var titleLink = $("#titleLink").val();
        var isOk = true;

        if(isNull(title)){
            layer.tips("链接名称 不能为空", "#title" , {
                tipsMore: true,
            });
            isOk = false;
        }
        if(isNull(titleLink)){
            layer.tips("链接地址 不能为空", "#titleLink" , {
                tipsMore: true,
            });
            isOk = false;
        }
        return isOk;
    }
    function isNull(str){
        return !str && str.replace(/(^s*)|(s*$)/g, "").length==0;
    }
</script>