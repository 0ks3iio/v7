<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

<script>
    //实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    var ue = UE.getEditor('notice',{
        //focus时自动清空初始化时的内容
        autoClearinitialContent:false,
        //关闭字数统计
        wordCount:false,
        //关闭elementPath
        elementPathEnabled:false,
        //默认的编辑区域高度
        toolbars: [
            ['fullscreen', 'source', 'undo', 'redo'],
            ['bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
        ],
        initialFrameHeight:300
        //更多其他参数，请参考ueditor.config.js中的配置项
    });
</script>
<form id="schoolNoticeForm">

    <div class="form-horizontal">
        <input type="hidden" name="id" value="${schoolNotice.id!}" >
        <input type="hidden" name="unitId" value="${schoolNotice.unitId!}" >
        <input type="hidden" name="acadyear" value="${schoolNotice.acadyear!}" >
        <input type="hidden" name="semester" value="${schoolNotice.semester!}" >
        <input type="hidden" name="schoolSection" value="${schoolNotice.schoolSection!}" >
        <input type="hidden" name="creationTime" value="${((schoolNotice.creationTime)?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >

        <div class="form-group">
        	<div class="col-sm-8">
                <div class="filter-content">
                	本学期实际授课&nbsp;<input name="studyDate" <#if (schoolNotice.id)?exists>value="${(schoolNotice.studyDate?default(0.0))?string("0.#")!}"<#else>value=""</#if> id="studyDate" type="text"  style="width:40px;" size="4" maxLength="5"  min="0" max="200" nullable="false" vtype="number"  />&nbsp;天，
              		 下学期定于&nbsp;<input type="text" style="width:80px" id="registerBegin" name="registerBegin"  class=" datepicker" value="${schoolNotice.registerBegin!}" readOnly>日注册报到，
					<input type="text" style="width:80px" id="studyBegin" name="studyBegin"  class=" datepicker" value="${schoolNotice.studyBegin!}" readOnly>&nbsp;日正式上课。
                </div>
			</div>
                
            <div class="col-sm-8">
                <textarea id="notice" name="notice" maxLength="700"  nullable="false" type="text/plain" style="width:100%;height:360px;">
                ${schoolNotice.notice!}
                </textarea>
            </div>
            <div class="col-sm-8 col-sm-offset-3" >
                <a id="clickBtn" href="javascript:;"style="margin-top: 5px;" class="btn btn-long btn-blue" onclick="saveNotice()">保存</a>
            </div>
        </div>
    </div>
</form>
<script>
    $(function(){
			$('.datepicker').datepicker({
				language: 'zh-CN',
				format: 'mm-dd',
				autoclose: true
			});
    })

    isSubmit = false;
    function saveNotice(){
        if(isSubmit){
            return;
        }
        var notice = UE.getEditor('notice').getContent();
        if(!notice||notice==''){
            layerTipMsgWarn("开学通知内容","不可为空");
            return false;
        }

        if(notice.length > 600){
            layerTipMsgWarn("开学通知内容","长度不能超过600字符");
            return false;
        }
        isSubmit = true;
        var options = {
            url : "${request.contextPath}/studevelop/templateSet/schoolNotice/save",
            dataType : 'json',
            success : function(data){
                if(!data.success){
                    layerTipMsg(data.success,"保存失败",data.msg);
                }else{
                    layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
                	changeSchoolNotice();
                }
                isSubmit = false;
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#schoolNoticeForm").ajaxSubmit(options);
    }


</script>