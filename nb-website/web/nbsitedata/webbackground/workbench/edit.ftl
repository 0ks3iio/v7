<title>学生转入</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<#import "/fw/macro/htmlcomponent.ftl" as htmlmacro>

<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>

<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/js/jquery.form.js"></script>

<style type="text/css">
    #edui_fixedlayer{z-index:9999 !important;}
    .sendBulletin-form .edui-default .edui-editor{width:824px !important;}
    body.widescreen .sendBulletin-form .edui-default .edui-editor{width:1024px !important;}
</style>

<script>
    /////////ue start//////////
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    //var oInput = document.getElementById("title");
    //oInput.focus();
    var ue = UE.getEditor('cont',{
        //focus时自动清空初始化时的内容
        autoClearinitialContent:false,
        //关闭字数统计
        wordCount:false,
        //关闭elementPath
        elementPathEnabled:false,
        //默认的编辑区域高度
        initialFrameHeight:300
        //更多其他参数，请参考ueditor.config.js中的配置项
    });
    $("#edui1").css("z-index","90");

    //调整全屏功能
    $("#edui3_state").click(function(){
        var fontSize = $(this).attr("class").indexOf("edui-state-checked");
        if(fontSize != (-1)){
            $("#edui1").css("z-index","9999");
        }else{
            $("#edui1").css("z-index","90");
        }
    });
    ///////////////////////UE editor end////////////////////

</script>
<form id="article" enctype="multipart/form-data">
    <!-- 相關屬性 -->
    <input type="hidden" id="id" name="id" value="${webArticle.id!}">
    <input type="hidden" id="commitUserId" name="commitUserId" value="${webArticle.commitUserId!}">
    <input type="hidden" id="commitState" name="commitState" value="${webArticle.commitState!}">
    <input type="hidden" id="clickNumber" name="clickNumber" value="${webArticle.clickNumber!}">
    <input type="hidden" id="commitUnitId" name="commitUnitId" value="${webArticle.commitUnitId!}">
    <input type="hidden" id="createUserId" name="createUserId" value="${webArticle.createUserId!}">
    <input type="hidden" id="auditUserId" name="auditUserId" value="${webArticle.auditUserId!}">
    <input type="hidden" id="auditUnitId" name="auditUnitId" value="${webArticle.auditUnitId!}">
    <input type="hidden" id="type" name="type" value="${webArticle.type!}">
    <input type="hidden" id="isTop" name="isTop" value="${webArticle.isTop!}">
    <input type="hidden" id="titleImageUrl" name="titleImageUrl" value="${webArticle.titleImageUrl!}">
    <input type="hidden" id="isDeleted" name="isDeleted" value="${webArticle.isDeleted?default('0')}">
    <input type="hidden" id="noOpinion" name="noOpinion" value="${webArticle.noOpinion!}">
    <input type="hidden" id="pushType" name="pushType" value="${webArticle.pushType!}">

    <div class="row">
        <div class="col-xs-12">
            <div class="row teachClassDetail" style="margin-top:10px;">
                <div class="clearfix">
                    <div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">

                        <div class="form-group col-lg-12 col-sm-12 col-xs-12 col-md-12" id="form-group-${id!}">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" for="${id!}">文章标题</label>
                            <div class="col-xs-3 col-sm-3 col-md-3">
							<span class="block input-icon input-icon-right">
							<input name="title" min="" max="" minlength="0" length="" vtype="" maxlength="100" nullable="false" regex="" regextip="" type="text" id="title" oid="title" placeholder="标题" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${webArticle.title!}">
							</span>
                            </div>
                            
                            <label class="pos-rel" style="padding-top:5px;">
                                <input type="checkbox"  class="ace" name="nnnn" <#if webArticle.titleLink?default('')!=''>checked="checked"</#if>/>
                                <span class="lbl use-titlelink" chk="<#if webArticle.titleLink?default('')==''>false<#else>true</#if>"></span>
                            </label>
                            <span class="" style="padding-top:5px;">使用链接标题</span>
                            <input type="text" name="titleLink" id="titleLink" maxlength="500" style="<#if webArticle.titleLink?default('')==''>display:none;</#if>" value="${webArticle.titleLink?default('')}"/>
                        </div>
                        <#if addWeb!false>
                        <div class="form-group col-lg-12 col-sm-12 col-xs-12 col-md-12" id="form-group-${id!}">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" for="${id!}">所属项目</label>
                            <div class="col-xs-3 col-sm-3 col-md-3">
							<select id='selectType'  nullable="false" >
                            <#if models?exists && models?size gt 0>
                            <#list models as model> 
                          
                            <option name='articleType'  value ="${model.thisId!}">${model.mcodeContent!}</option>
                           
                            </#list>
                            </#if>
                            </select>
                            </div>                          
                        </div> 
                        </#if>      
                        <#if (manager || webArticle.auditOpinion?default('') != '') && (type?default('')!='11')&&(webArticle.id?default('')!=''&&webArticle.commitState?default('')=='1')>                     
                        <div class="form-group col-lg-12 col-sm-12 col-xs-12 col-md-12" id="form-group-${id!}">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" for="${id!}">所属项目</label>
                            <div class="col-xs-3 col-sm-3 col-md-3">
							<select id='selectType'  nullable="false" >
                            <#if models?exists && models?size gt 0>
                            <#list models as model> 
                            <#if webArticle.type== model.thisId>
                     <option name='articleType'  value ="${model.thisId!}" selected="selected">${model.mcodeContent!}</option>
                            <#else>
                     <option name='articleType'  value ="${model.thisId!}">${model.mcodeContent!}</option>
                            </#if>
                            </#list>
                            </#if>
                            </select>
                            </div>                          
                        </div>                        
                          <div class="form-group col-lg-12 col-sm-12 col-xs-12 col-md-12" id="form-group-${id!}">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" for="${id!}">推送项目</label>
                            <div class="col-xs-3 col-sm-3 col-md-3">
							<select id='selectPushType'   >
                            <#if models?exists && models?size gt 0>                 
                            <option name='pushType'  value ="" selected="selected">请选择</option>
                            <#list models as model>                          
                            <option name='pushType'  value ="${model.thisId!}">${model.mcodeContent!}</option>                         
                            </#list>
                            </#if>
                            </select>
                            </div>                          
                        </div>
                        </#if>                               
                    <#if type?default('')=='03' || type?default('')=='11'>
                        <div class="form-group col-lg-12 col-sm-12 col-xs-12 col-md-12" id="form-group-${id!}">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" for="${id!}">标题图片</label>
                            <div class="col-xs-3 col-sm-3 col-md-3">
							<span class="block input-icon input-icon-right">
								<span id="span1" class="upload-span" style="width:80px;"><a href="javascript:void(0)" class="">选择文件</a></span>
								<input id="imgPhoto1" type="file" name="titleImage"  />
								<input id="uploadFilePath1" name="titleImageName"  type="text" class="input-txt input-readonly" readonly="readonly" style="width:200px;height:30px;" value="${(webArticle.titleImageName?default(''))?trim}" maxLength="125"/>
							</span>
                            </div>
                        </div>
                    <#elseif type?default('')=='08'>
                        <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" for="${id!}">文章介绍</label>
                        <div class="col-xs-3 col-sm-3 col-md-3">
                            <textarea id="introduction" name="introduction" style="width: 1020px;height:55px;">${webArticle.introduction?default('')?trim}</textarea>
                        </div>
                    </#if>
                        <div class="form-group col-xs-12 col-sm-12 col-md-12">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" style="padding-top:200px;" for="">正文</label>
                            <div class="col-xs-6 col-sm-6 col-md-9" style="height:50px;">
                                <div style="border:solid 1px #D4D4D4 ;border-radius:5px;width:1024px;height:50px;padding-top:10px;background:#F5F5F5;">
                                    <span style="padding-left:10px;">提交时间:</span>
                                    <input type="text" id=""  disabled="true"; style="border:none;" value="${(webArticle.commitTime?string("yyyy-MM-dd HH:mm:ss"))?if_exists}"/>
                                    <span style="padding-left:10px;" >作者:</span>
                                    <input type="text" name="author" id="author" style="" value="${webArticle.author!}"/>
                                    <span style="padding-left:10px;">文章来源:</span>
                                    <input type="text" name="articleSource" id="articleSource"  style="" value="${webArticle.articleSource!}"/>

                                </div>
                            </div>
                            <div class=" col-xs-6 col-sm-6 col-md-9">
                                <textarea id="cont" name="content" type="text/plain" style="width:1024px;height:500px;">${webArticle.content!}</textarea>
                            </div>
                        </div>
                    <#if (manager || webArticle.auditOpinion?default('') != '') && (type?default('')!='11' && webArticle.id?default('')!='' && webArticle.commitState?default('')!='0' && webArticle.commitState?default('')!='2' )>
					<div class="form-group col-xs-12 col-sm-12 col-md-12">
						<label class="ace-icon  col-md-1 control-label no-padding-right pull-left" style="padding-top:90px;" for="">未通过意见</label>
						<div class="col-xs-3 col-sm-3 col-md-3">
							<ul class="list-group" id='list_agreement' >							  
                               <li class="list-group-item" >
                               <input class="g" id="check0"  name="g" type="checkbox"   value="27:13" /> 
                               <label for="check0">文章内容不全</label>
                               </li>
                               <li class="list-group-item"  >
                               <input class="g" id="check1"  name="g" type="checkbox"  value="27:13" /> 
                               <label for="check1">字数不对</label>                               
                               </li>                        
                               <li class="list-group-item"  >
                               <input class="g" id="check2"  name="g" type="checkbox"  value="27:13" /> 
                               <label for="check2">内容不健康</label>                              
                               </li>                                                             
                               <li class="list-group-item"  >
                               <input class="g" id="check3"  name="g" type="checkbox"  value="27:13" /> 
                               <label for="check3">图片与内容不匹配</label>                            
                               </li>
                            </ul>
						</div>
					</div>
					</#if>
                        <!-- 審核員編輯時顯示 -->
                    <#if (manager || webArticle.auditOpinion?default('') != '') && (type?default('')!='11')&&(webArticle.id?default('')!=''&&webArticle.commitState?default('')!='0')>
                        <div class="form-group col-xs-12 col-sm-12 col-md-12">
                            <label class="ace-icon  col-md-1 control-label no-padding-right pull-left" style="padding-top:45px;" for="">修改意见</label>
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <textarea id="auditOpinion" name="auditOpinion"  type="text/plain" <#if !manager>disabled="true"</#if> style="width:1024px;height:100px;">${webArticle.auditOpinion!}</textarea>
                            </div>
                        </div>
                    </#if>

                    </div>
                </div>
                <div class="clearfix form-actions center">

                <#if type?default('')=='11'>
                    <a href="javascript:;" id="btn-edit-pass" class="btn btn-primary ">保存    </a>
                <#else >
                    <!-- 審核員編輯時顯示  -->
                    <#if manager && webArticle.commitState?default('')=='1'>
                        <a href="javascript:;" id="btn-edit-pass" class="btn btn-primary ">通过    </a>
                        <a href="javascript:;" id="btn-edit-unpass" class="btn btn-primary ">不通过</a>
              <#--    <#elseif (webArticle.commitState?default('0')=='0' || webArticle.commitState?default('0')=='5') && !manager>
                        <a href="javascript:;" id="btnArticle-commit" class="btn btn-primary ">提交</a>
                        <a href="javascript:;" id="btn-edit-save" class="btn btn-primary ">保存为草稿</a> -->  
                    <#elseif container?default('')=='c'>
                        <#if !manager>
                        <a href="javascript:;" id="btnArticle-commit" class="btn btn-primary ">提交</a>
                        <#else>
                            <a href="javascript:;" id="btn-edit-pass" class="btn btn-primary ">发布</a>
                        </#if>
                        <a href="javascript:;" id="btn-edit-save" class="btn btn-primary ">保存为草稿</a>
                    </#if>
                </#if>
                <#if addWeb!false>
                     <a href="javascript:;" id="btn-edit-pass" class="btn btn-primary ">发布</a>
                
                </#if>
                    <a href="javascript:;" id="btn-edit-cancel" class="btn btn-primary ">返回</a>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    var replayState = "";
    $('.page-content-area').ace_ajax('loadScripts', [], function() {
        $("#btn-edit-cancel").on("click",function(){
            location.href="#${request.contextPath}/sitedata/workbench/index/page?container=${container!}";
        });
        $(".use-titlelink").unbind("click").bind("click",function(){
            if($(this).attr("chk")=="true"){
                $("#titleLink").val("").css("display","none");
                $(this).attr("chk","false");
            }else{
                $(this).attr("chk","true");
                $("#titleLink").val("http://").show();
            }
        });
        
        
        
        
    <#if manager && webArticle.commitState?default('')=='1'>
        $("#btn-edit-pass").unbind("click").bind("click",function(){
            var state = "2";
            var modify=document.getElementById("auditOpinion").value     
            var a=document.getElementById("selectType");　　
            var type=a.options[a.selectedIndex].value;　     
            var a=document.getElementById("selectPushType");　　
            var pushType=a.options[a.selectedIndex].value;　   
       //   saveOrUpdate($(this),state,type,pushType);
            saveOrUpdate($(this),state,'',type,pushType,'1');
            
        });
        $("#btn-edit-return").unbind("click").bind("click",function(){
            var state = "5";
            saveOrUpdate($(this),state);
        });
        $("#btn-edit-unpass").unbind("click").bind("click",function(){
            var state = "4";
				var lists='';
				var ul=  $(":checkbox:checked").parents("li")
                var modify=document.getElementById("auditOpinion").value     
               for(var i=0;i<ul.length;i++){
                var noOpinion=ul[i].getElementsByTagName("label")[0].innerText;
         //       alert("cityid=="+noOpinion);
                if(i==ul.length-1){
                  lists=lists+noOpinion
                }else{
                  lists=lists+noOpinion+","
                }               
       //         alert(lists);          
               }
				saveOrUpdate($(this),state,lists,'','','2');
        //    var state = "4"; 
              
         //   saveOrUpdate($(this),state);                   
           
        });
    <#elseif type?default('')=='11'>
        $("#btn-edit-pass").unbind("click").bind("click",function(){
            var state = "2";
            saveOrUpdate($(this),state);
        });
    <#else>
        $("#btnArticle-commit").unbind("click").bind("click",function(){
            var state = "1";
            saveOrUpdate($(this),state);
        });
        $("#btn-edit-pass").unbind("click").bind("click",function(){
            var state = "2";
            saveOrUpdate($(this),state);
        });
        $("#btn-edit-save").unbind("click").bind("click",function(){
            var state = "0";
            saveOrUpdate($(this),state);
        });
    </#if>
          <#--    20170228  --->
    <#if addWeb!false>
     $("#btn-edit-pass").unbind("click").bind("click",function(){
     
            var state ='';
            <#if manager>
            state = '2';
            <#else>
            state = '1';
            </#if>            
            var a=document.getElementById("selectType");　　
            var type=a.options[a.selectedIndex].value;　     
            saveOrUpdate($(this),state,'',type,'','1');
            
        });
    
    </#if> 
       
    });

    function saveOrUpdate(obj,state,noOpinion,type,pushType,operation){
     
        //校驗數據
        if(state=="1"){
            $(obj).addClass("disabled");
        }
     
        var check = checkValue('#article');
        if(!check){
            $(obj).removeClass("disabled");
        }
        //console.log($("textarea[name='content']").val());
        if($("textarea[name='content']").val()=='' && state!='0'){
            layer.confirm('正文内容不能为空！', {icon: 3, title:'提示'}, function(index){
                layer.close(index);
            });
            $(obj).removeClass("disabled");
            check = false;
        }
        //判断是否勾选未通过意见
        if(operation=='2'){
         if(noOpinion==''){
           layer.confirm('请选择未通过意见！', {icon: 3, title:'提示'}, function(index){
  				layer.close(index);
			});
           check = false;
         }
        }
        

        //文章来源，作者
        var articleSources = $("#articleSource").val();
        var author = $("#author").val();
        if (articleSources && articleSources !='' && getLength(articleSources) >30){
    
            layer.tips("文章来源长度过长", "#articleSource" , {
                tipsMore: true,
                tips:3
            });
            check = false;
        }
        if (author && author !='' && getLength(author) >15){
   
            layer.tips("作者 长度过长", "#author" , {
                tipsMore: true,
                tips:3
            });
            check = false;
        }
    <#if type?default('')=='03' || type?default('')=='11'>
        if(!$("#uploadFilePath1").val() || $("#uploadFilePath1").val()==''){
  
            layer.tips("标题图片 不能为空", "#uploadFilePath1" , {
                tipsMore: true,
                tips:3
            });
            check = false;
        }
    <#elseif type?default('')=='08'>
        var introduction = $("#introduction").val();
        if(introduction && getLength(introduction) > 65){
 
            layer.tips("文章介绍 长度超出限制", "#introduction" , {
                tipsMore: true,
                tips:3
            });
            check = false;
        }
    </#if>
    <#if (manager || webArticle.auditOpinion?default('') != '') && (type?default('')!='11')>
        var op = $("textarea[name='auditOpinion']").val();
        if(op!='' && getLength(op)>200){
  
            layer.tips('修改意见 长度超过限制！', "#auditOpinion" , {
                tipsMore: true,
                tips:3
            });
            $(obj).removeClass("disabled");
            check = false;
        }
    </#if>
        if(!check){
 
            $("#btnArticle-commit").removeClass("disabled");
            return ;
        }

        var obj = new Object();
        // 获取此控件下所有的可输入内容，并组织成json格式
        obj = JSON.parse(dealValue('#article'));
        //填充正文
        obj.content = $("textarea[name='webArticle.content']").val();

        var opinion = $("textarea[name='auditOpinion']").val();
        if(opinion != '' && typeof opinion!='undefined'){
            obj.auditOpinion = opinion;
        }
        obj.commitState = state;
       
        
        //var form = new FormData(document.getElementById("article"));

        replayState = state;
        $("#commitState").val(state);
        //return ;
        // 提交数据
        var options={
            url:'${request.contextPath}/sitedata/${type!}/saveorupdate?lnoOpinion='+noOpinion+'&ltype='+type+'&lpushType='+pushType,
            //data: JSON.stringify(obj),
            type:'post',
            datatype:'json',
            contentType:'application/json',
            cache:false,
            success:showReply
        }
        $("#article").ajaxSubmit(options);
    }

    function showReply(data) {
        var jsonO = JSON.parse(data);
//		var jsonO = $.parseJSON(data);
        if(!jsonO.success){
            showMsgError(jsonO.msg,"操作失败!",function(index){
                layer.close(index);
                $("#btnArticle-commit").removeClass("disabled");
            });
        }
        else{
            showMsgSuccess("操作成功!","操作成功!",function(index){
            	
                layer.closeAll();
                if(replayState!="0"){
                    $("#btn-edit-save").css("display","none");
                    $("#btnArticle-commit").css("display","none");
                    $("#btnArticle-commit").removeClass("disabled");
                    $("#edui1").html("");
                    location.href="#${request.contextPath}/sitedata/workbench/index/page?container=${container!}";
                }
                else if(replayState=="0"){
                    try {
                        //location.href="#${request.contextPath}/sitedata/${type!}/addoredit/page?articleId="+jsonO.businessValue;
                        location.href="#${request.contextPath}/sitedata/workbench/index/page?container=${container!}";
                    }catch(e) {
                        //who care
                    }
                }
            });
        }
    }

    <#if type?default('')=='03' || type?default('')=='11'>
    //处理上传文件样式
    $(document).ready(function () {
        initFileInput();
    });
    function resetFilePos(){
        $("#imgPhoto1").css({"position":"absolute","-moz-opacity":"0","opacity":"0","filter":"alpha(opacity=0)","cursor":"pointer","width":$(".upload-span a").width(),"height":$(".upload-span").height()});
        $("#imgPhoto1").offset({"left":$("#span1").offset().left});
        $("#imgPhoto1").css({"display":""});
    }
    function initFileInput(){
        $("#span1").mouseover(function(){
            $("#imgPhoto1").offset({"top":$("#span1").offset().top });
        });

        $("#imgPhoto1").on("change",function(){
            var p1 = $("#imgPhoto1").val().lastIndexOf("\\");
            var fileName = $("#imgPhoto1").val().substring(p1+1);
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
    </#if>
    <#if webArticle.noOpinion?default('')!=''>
    $(document).ready(function() {     
     var arry = new Array();
     var str = "${webArticle.noOpinion!}";
     arry=str.split(',') 
     var check0 = document.getElementById("check0");
     var check1 = document.getElementById("check1");
     var check2 = document.getElementById("check2");
     var check3 = document.getElementById("check3");   
     for (var i = 0; i < arry.length; i++) {
                           if (arry[i] == "文章内容不全")
                               check0.checked = true;
                           if (arry[i] == "字数不对")
                               check1.checked = true;
                           if (arry[i] == "内容不健康")
                               check2.checked = true;
                           if (arry[i] == "图片与文章内容不匹配")
                               check3.checked = true;
                          
             } 
    }); 
    </#if>
</script>
