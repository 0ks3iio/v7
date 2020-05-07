<a href="javascript:void(0);" onclick="showRegionIndex();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<#import "/fw/macro/webmacro.ftl" as w>
<style type="text/css">
    .form-control {
        height: 34px;
    }

    .white_content {
        display: none;
        position: absolute;
        top: 2%;
        left: 5%;
        width: 1050px;
        height: 650px;
        padding: 1px;
        border: 1px solid black;
        background-color: white;
        z-index: 1002;
        overflow: auto;
    }
    .mask{position:absolute;width:100%;height:100%;z-index:1;filter:alpha(opacity=0);opacity:0;background:#ffffff}
</style>
<div class="box box-default">
    <div class="box-body">
        <a href="javascript:void(0)" onclick="closeDialog()" style="color: red ;display: none"  id="closebutton"><b>关闭</b></a>
           <#if !isEdit>
	           <div class="form-horizontal">
	               <div class="form-group">
	                   <label class="col-sm-3 control-label" for="form-field-2">部署地区选择</label>
	                   <div class="col-sm-3">
	                      <@w.region regionCode="" regionName="" />
	                   </div>
	                   <div class="">
	                       <button class="filter-name btn btn-blue" onClick= "doSearchUnit()">选择单位</button>
	                       <span class="filter-name">&nbsp;&nbsp;&nbsp;</span>
			                <div  class="filter-content" id="unitSetList">
			        
			                </div>
	                   </div>
	               </div>
	           </div>
	       <#else>
	           <input type="hidden"  name="region" id= "hiddenRegion" value="${regionCode!}"/>
               <input type="hidden"  name="unitId" id= "hiddenUnitId" value="${unitId!}"/>
           </#if>
        <form id="form-field-1" enctype="multipart/form-data" method="post" role="form">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">单位域名设置</label>
                    <div class="col-sm-6">
                        <input id="page-Title" type="text" value="${loginOption.domainName!}" name="domainName" placeholder="请输入单位域名"
                               class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">其他页title设置</label>
                    <div class="col-sm-6">
                        <input id="page-Title" type="text" value="${loginOption.commonPageTitle!}" name="commonPageTitle" placeholder="请输入title"
                               class="form-control" />
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">登录页title设置</label>
                    <div class="col-sm-6">
                        <input id="page-Title" type="text" value="${loginOption.pageTitle!}" name="pageTitle" placeholder="请输入title"
                               class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="form-field-1">登陆页logo图片</label>
                    <div class="col-sm-6">
                        <input id="logo-Ima" type="file" name="pageLogoImage" />
                    </div>
                </div>
            </div>

            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="form-field-1">登陆页logo名称</label>
                    <div class="col-sm-6">
                        <input id="logo-Name" type="text" id="" value="${loginOption.pageLogoName!}" name="pageLogoName" placeholder="请输入logo"
                               class="form-control"/>
                    </div>
                </div>
            </div>

            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="form-field-1">登录页面背景图片</label>
                    <div class="col-sm-6">
                        <input id="bg-Ima" type="file" name="loginBgImage" class="pic"/>
                    </div>
                </div>
            </div>

            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="form-field-1">版权信息</label>
                    <div class="col-sm-6">
                        <input id="copy-Right" type="text" value="${loginOption.copyRight!}" placeholder="请输入版权信息" class="form-control" name="copyRight"/>
                    </div>
                </div>
            </div>


            <#--<div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">手机号是否作为标准账号登录</label>
                    <div class="col-sm-9">
                        <label>
                            <input id="phone" class="wp wp-switch js-usePhone" type="checkbox">
                            <span class="lbl"></span>
                        </label>
                    </div>

                </div>
            </div>-->
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否启用动画（标准页建议启用）</label>
                    <div class="col-sm-9">
                        <label>
                            <input id="js-userpalyer" class="wp wp-switch js-userpalyer" type="checkbox" checked="checked"  />
                            <span class="lbl"></span>
                        </label>
                    </div>
                    <input type="hidden" id="player" name="player" value="false"/>
                </div>
            </div>
            
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否启用登陆页logo图片（标准页建议启用）</label>
                    <div class="col-sm-9">
                        <label>
                            <input id="js-userenablePageLogoImage" class="wp wp-switch js-userenablePageLogoImage" type="checkbox" <#if loginOption.enablePageLogoImage!true> checked="checked" </#if> />
                            <span class="lbl"></span>
                        </label>
                    </div>
                    <input type="hidden" id="enablePageLogoImage" name="enablePageLogoImage" value="${loginOption.enablePageLogoImage?string('true', 'false')}"/>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否启用登陆页logo名称（标准页建议启用）</label>
                    <div class="col-sm-9">
                        <label>
                            <input id="js-userenablePageLogoName" class="wp wp-switch js-userenablePageLogoName" type="checkbox" <#if loginOption.enablePageLogoName!true> checked="checked" </#if> />
                            <span class="lbl"></span>
                        </label>
                    </div>
                    <input type="hidden" id="enablePageLogoName" name="enablePageLogoName" value="${loginOption.enablePageLogoName?string('true', 'false')}"/>
                </div>
            </div>


            <!--登录页面相关的参数设置-->
            <#--<div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">基于标准登录页</label>
                    <div class="col-sm-9">
                        <label>
                            <input id="standardPage" class="wp wp-switch js-useStandard"
                                   type="checkbox" >
                            <span class="lbl"></span>
                        </label>
                    </div>

                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="form-field-1">登陆页模版(开发提供)</label>
                    <div class="col-sm-6">
                        <input type="file" id="template" class="form-control" name="loginPageTemplate"/>
                        <span class="control-disabled"></span>
                    </div>
                </div>
            </div>-->
            <input type="hidden" name="standard" id="standard" value="true"/>
            <input type="hidden" id="phoneAsUserName" name="phoneAsUserName" value="false"/>
        </form>
        <div style="padding-left: 25%" class="col-sm-offset-2">
           <#-- <a href="JavaScript:void(0)" class="btn btn-blue btn-long" id="preview">预览</a>-->
            <input type="button" id="savebutton" class="btn btn-blue btn-long" value="保存">
        </div>
    </div>
</div>


<script>
var isSubmit=false;
function doSearchUnit(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	$("#unitSetList").empty();
	$.ajax({
        url:"${request.contextPath}/system/ops/serverRegion/unitList?regionCode="+$('#regionCode').val()+"&isDomain=true",
        dataType:'json',
        success:function(msg){
        	 if(typeof(msg)=='object'){
        		 var unitList=msg.unitList;
        		 if(typeof(unitList)=='object'){
        			 var mhtml='<select class="form-control" id="regionUnitId" style="width:180px"><option value="">--- 请选择 ---</option>';
                     for(i in unitList){
                         var obj=unitList[i];
                         mhtml=mhtml+'<option value="'+obj.id+'">'+obj.unitName+'</option>'
                     }
                     mhtml=mhtml+'</select>'
                     $("#unitSetList").append(mhtml);
                 }
        	 }
        	 isSubmit=false;
        }
    })
}


    function closeDialog() {
        document.getElementById('light').style.display = 'none';
        document.getElementById('closebutton').style.display = 'none';
    }

    /*$(".js-useStandard").on('click', function () {
        if ($(this).prop('checked') !== true) {
            $(this).closest('.form-group').nextAll().removeClass('hidden');
            $('#standard').val(false)
        } else {
            $(this).closest('.form-group').nextAll().addClass('hidden');
            $('#standard').val(true)
        }
    })*/
    /*$(".js-usePhone").on('click',function () {
        if ($(this).prop('checked') !== true) {
            $(this).closest('.form-group').nextAll().removeClass('hidden');
            $('#phoneAsUserName').val(false)
        } else {
            $(this).closest('.form-group').nextAll().addClass('hidden');
            $('#phoneAsUserName').val(true)
        }
    });*/
    $("#js-userpalyer").change(function(){
    	if ($(this).prop('checked') !== true) {
            $(this).closest('.form-group').nextAll().removeClass('hidden');
            $('#player').val(false)
        } else {
            $(this).closest('.form-group').nextAll().addClass('hidden');
            $('#player').val(true)
        }
    })
    
    $("#js-userenablePageLogoImage").change(function(){
    	if ($(this).prop('checked') !== true) {
            $(this).closest('.form-group').nextAll().removeClass('hidden');
            $('#enablePageLogoImage').val(false)
        } else {
            $(this).closest('.form-group').nextAll().addClass('hidden');
            $('#enablePageLogoImage').val(true)
        }
    })
    
    $("#js-userenablePageLogoName").change(function(){
        if ($(this).prop('checked') !== true) {
            $(this).closest('.form-group').nextAll().removeClass('hidden');
            $('#enablePageLogoName').val(false)
        } else {
            $(this).closest('.form-group').nextAll().addClass('hidden');
            $('#enablePageLogoName').val(true)
        }
    })
    
    $('#preview').on("click",function () {
        var index = layer.open({
            title : '预览',
            type : 2,
            maxmin: true,
            fixed: true,
            resize: false,
            content: ['${request.contextPath}/fpf/login/loginForPassport.action?preview=true', 'yes']
        });
        layer.full(index);
    });

    function changeRegion(obj) {
        openModel('super2','登录页设置','1','${request.contextPath}/ops/loginSet/domain/page?fromDesktop=true&regionCode='+$(obj).val(),'','','superId','');
    }

    $(".btn-long").click(function () {
        var logoImaType = $("#logo-Ima").val().substring($("#logo-Ima").val().lastIndexOf("."));
        var bgImaType = $("#bg-Ima").val().substring($("#bg-Ima").val().lastIndexOf("."));
        //var templateType = $("#template").val().substring($("#template").val().lastIndexOf("."));

        $(".save-tip").empty();
//        if ($("#page-Title").val() == 0) {
//            layerError('#page-Title', '请输入标题');
//            $("#page-Title").focus();
//            return false;
//        }
//        if ($("#logo-Ima").val() == 0) {
//            layerError('#logo-Ima', '请选择logo图片');
//            $("#logo-Ima").focus();
//            return false;
//        }
//        if (!/.(gif|jpg|jpeg|png|gif|jpg|png|bmp)$/.test(logoImaType)) {
//            layerError('#logo-Ima', '请选择图片格式文件');
//            $("#logo-Ima").focus();
//            return false;
//        }
//        if ($("#logo-Name").val() == 0) {
//            layerError('#logo-Name', '请输入logo名称');
//            $("#logo-Name").focus();
//            return false;
//        }
//        if ($("#bg-Ima").val() == 0) {
//            layerError('#bg-Ima', '请选择背景图片');
//            $("#bg-Ima").focus();
//            return false;
//        }
//        if (!/.(gif|jpg|jpeg|png|gif|jpg|png|bmp)$/.test(bgImaType)) {
//            layerError('#bg-Ima', '请选择图片格式文件');
//            $("#bg-Ima").focus();
//            return false;
//        }
//        if ($("#copy-Right").val() == 0) {
//            layerError('#copy-Right', '请填写版权信息');
//            $("#copy-Right").focus();
//            return false;
//        }
//        if (!$(".js-useStandard").is(":checked")) {
//            if ($("#template").val() == 0) {
//                layerError('#template', '请选择模板');
//                $("#template").focus();
//                return false;
//            }
//            if (templateType != ".ftl") {
//                layerError('#template', '请选择正确的模板文件(.ftl)');
//                $("#template").focus();
//                return false;
//            }
//        }


        if (this.id == "savebutton") {
        	var unitId = $('#regionUnitId').val();
        	var region = $('#regionCode').val();
        	<#if isEdit>
        	   unitId = $('#hiddenUnitId').val();
        	   region = $('#hiddenRegion').val();
        	</#if>
            var formData = new FormData($("#form-field-1")[0]);
            $.post({
                url: "${request.contextPath}/ops/loginSet/saveLoginPageInfo?unitId="+unitId+ "&region="+region,
                data: formData,
                secureuri: false,
                processData: false,
                contentType: false,
                success: function (data) {
                    var callBack = JSON.parse(data);
                    if (callBack.success) {
                        showSuccessMsg(callBack.msg)
                    } else {
                        showErrorMsg(callBack.msg);
                    }
                }
            })
        } else {
            document.getElementById('light').style.display = 'block';
            document.getElementById('closebutton').style.display = 'block';
        }
    });


</script>
