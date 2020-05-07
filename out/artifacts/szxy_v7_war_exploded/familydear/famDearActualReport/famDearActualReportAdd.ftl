<#--<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/webuploader/webuploader.css"/>-->
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div id="myDiv">
<form id="subForm" >
    <input type="hidden" name="year" id="year" value="${year}">
    <#--<input type="hidden" name="activityId" id="activityId" value="${activityId}">-->
    <input type="hidden" name="createUserId" value="${createUserId}">
    <input type="hidden" id="village" value="${village!}">
    <input type="hidden" id="id" name="id" value="${id}">
    <input type="hidden" id="picIds" value="">
    <input type="hidden" id="activityId" value="${activityId!}">
    <div class="main-content">

        <div class="main-content-inner">
            <div class="page-content">
                <div class="box box-default">

                    <div class="box-body">

                        <table class="table table-bordered table-striped no-margin" id="tab">
                            <thead>
                            <tr>
                                <th colspan="4" class="text-center">基本信息</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="text-right" width="150"><span style="color:red">*</span>访亲轮次:</td>
                                <td>
                                    <select name="activityId" class="form-control" id="activityId1" onchange="activityChange(this.value)">
                                         <#if activityList?exists && (activityList?size>0)>-->
                                             <#list activityList as item>
                                                 <option value="${item.id!}" <#if activityId?default('a')==item.id>selected</#if>>${item.title!}</option>
                                             </#list>
                                         <#else>
                                                 <option value="">暂无需要填报的轮次</option>
                                         </#if>
                                    <#--arrangeList-->
                                    </select>
                                </td>
                                <td class="text-right" width="150"><span style="color:red">*</span>访亲批次:</td>
                                <td>
                                    <select name="arrangeId" class="form-control" id="arrangeId1">
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right"><span style="color:red">*</span>访亲到达时间:</td>
                                <td><input type="text" class='form-control date-picker' vtype='data' name="arriveTime" id="arriveTime" maxlength="30"></td>
                                <td class="text-right"><span style="color:red">*</span>返回时间:</td>
                                <td><input type="text" class='form-control date-picker' vtype='data' name="backTime" id="backTime"  maxlength="30"></td>
                            </tr>
                        </table>
                        <table class="table table-bordered table-striped no-margin" id="tab2">
                            <thead>
                            <tr>
                                <th colspan="2" class="text-center">车票信息</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="text-right"><span style="color:red">*</span>车票图片:</td>
                                <td>
                                    <div id="ticketImages1" class="js-layer-photos">
                                    </div>
                                    <a class="form-file pull-left picker1" id="ticket_1" onclick="addClick(this.id);">
                                        <i  class="fa fa-plus"></i>
                                    <#--<div id="picker">选择文件</div>-->
                                        <input>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right" width="150"><span style="color:red">*</span>车票说明:</td>
                                <td><input type="text" name="ticketTitle" id="ticketTitle1" maxlength="1500"  value="" class="form-control"></td>
                            </tr>
                            <tr>
                                <td class="text-center" colspan="2">
                                    <a class="table-btn color-blue" onclick="add2()">+新增车票信息</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table table-bordered table-striped no-margin" id="tab1">
                            <thead>
                            <tr>
                                <th colspan="2" class="text-center">活动信息</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="text-right" width="150">活动地点:</td>
                                <td><input type="text" class="form-control" name="place" id="place" maxlength="50" ></td>
                            </tr>
                            <tr>
                                <td class="text-right" width="150"><span style="color:red">*</span>活动内容说明:</td>
                                <td><input type="text" maxlength="1000" class="form-control" name="activityContent" id="activityContent"></td>
                            </tr>
                            <#--<tr>-->
                            <#--<td class="text-right" width="150" >活动地点:</td>-->
                            <#--<td>-->
                            <#--<input type="text" class="form-control" name="place" id="place" maxlength="50" >-->
                            <#--</td>-->
                            <#--<td class="text-right">活动内容说明:</td>-->
                            <#--<td>-->
                            <#--<input type="text" maxlength="1000" class="form-control" name="activityContent" id="activityContent">-->
                            <#--</td>-->
                            <#--</tr>-->
                            <tr>
                                <td class="text-right" width="150"><span style="color:red">*</span>活动标题:</td>
                                <td><input type="text" name="activityTitle" id="activityTitle1" maxlength="1500"  value="" class="form-control"></td>
                            </tr>
                            <tr>
                                <td class="text-right"><span style="color:red">*</span>活动图片:</td>
                                <td>
                                    <div id="images1" class="js-layer-photos">
                                    </div>
                                    <a class="form-file pull-left" onclick="addClick(this.id);" id="activity_1">
                                        <i  class="fa fa-plus"></i>
                                        <div ></div>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-center" colspan="2">
                                    <a class="table-btn color-blue" onclick="add1()">+新增活动信息</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table table-bordered table-striped no-margin">
                            <tr>
                                <td class="text-right" width="150">活动类型:</td>
                                <td colspan="3">办实事办好事</td>
                            </tr>
                            <tr>
                                <td class="text-right">访亲形式:</td>
                                <td colspan="3">
                                    <label><input type="checkbox" class="wp" value="1" name="activityFrom"><span class="lbl"></span>捐款</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="2" name="activityFrom"><span class="lbl"></span>捐物</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="3" name="activityFrom"><span class="lbl"></span>就医</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="4" name="activityFrom"><span class="lbl"></span>就学</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="5" name="activityFrom"><span class="lbl"></span>就业</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="6" name="activityFrom"><span class="lbl"></span>生产发展</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="7" name="activityFrom"><span class="lbl"></span>其他</label>&nbsp;&nbsp;
                                    <label><input type="checkbox" class="wp" value="8" name="activityFrom"><span class="lbl"></span>惠及各族群众数</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right">捐款金额:</td>
                                <td><input type="text" name="donateMoney" id="donateMoney" nullable="true" class="form-control"></td>
                                <td class="text-right">捐款说明:</td>
                                <td><input type="text" class="form-control" name="donateMark" id="donateMark" placeholder="" maxlength="500"></td>
                            </tr>
                            <thead>
                            <tr>
                                <th colspan="4" class="text-center">捐物信息</th>
                            </tr>
                            </thead>
                            <tr>
                                <td class="text-right">捐物件数:</td>
                                <td><input type="text"  class="form-control" name="donateObjectnum" id="donateObjectnum" placeholder="" ></td>
                                <td class="text-right">折合金额:</td>
                                <td><input type="text" class="form-control"name="conversionAmount" id="conversionAmount" ></td>
                            </tr>
                            <tr>
                                <td class="text-right">捐物说明:</td>
                                <td colspan="3">
                                    <textarea rows="3" name="donateobjMark" id="donateobjMark" maxlength="500"  value="" class="form-control" ></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right">就医(件):</td>
                                <td><input type="text" class="form-control" name="seekMedical" id="seekMedical" ></td>
                                <td class="text-right">就学(件):</td>
                                <td><input type="text" class="form-control" name="seekStudy" id="seekStudy" ></td>
                            </tr>
                            <tr>
                                <td class="text-right">就业(件):</td>
                                <td><input type="text" class="form-control" name="seekEmploy" id="seekEmploy"></td>
                                <td class="text-right">发展生产(件):</td>
                                <td><input type="text" class="form-control" name="developProduct" id="developProduct"></td>
                            </tr>
                            <tr>
                                <td class="text-right">其他(件):</td>
                                <td colspan="3"> <input type="text" class="form-control" name="otherThingsnum" id="otherThingsnum"></td>
                            </tr>
                            <tr>
                                <td class="text-right">惠及各族群众户数:</td>
                                <td><input type="text" class="form-control" name="benefitPeople" id="benefitPeople" ></td>
                                <td class="text-right">走访各族群众户数:</td>
                                <td><input type="text" class="form-control" name="walkPeople" id="walkPeople" ></td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th colspan="2" class="text-center">其他</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="text-right" width="150">备注:</td>
                                <td><input type="text" name="mark" id="mark" maxlength="100"  value="" class="form-control"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div><!-- /.page-content -->
        </div>
    </div>
    <div class="base-bg-gray text-center">
        <a class="btn btn-blue" onclick="save();" href="javascript:;">保存</a>
        <a class="btn btn-white" onclick="goBack();" href="javascript:;">返回</a>
    </div>
</form>
</div>
<script>
    $("#headIndex").hide();
    $("#backA").show();

    var titleNum = 1;
    var ticketNum=1;
    var index;
    function addClick(id){
        // var attr = id.split(",");
        // index = attr[1];
        index = id;
    }

    $(function(){
        var viewContent = {
            'format': 'yyyy-mm-dd',
            'minView': '2'
        };
        initCalendarData("#myDiv", ".date-picker", viewContent);
        var activityId=$("#activityId1").val();
        activityChange(activityId);
        var activityId=$("#activityId1").val();
        var $wrap = $('#uploader');
        // 图片容器
        // var $queue = $( '<ul class="filelist"></ul>' )
        var $queue = $("#filelist");
        /*init webuploader*/
        var $list=$("#thelist");  //这几个初始化全局的百度文档上没说明，好蛋疼。
        var $btn =$("#ctlBtn");   //开始上传
        var thumbnailWidth = 100;   //缩略图高度和宽度 （单位是像素），当宽高度是0~1的时候，是按照百分比计算，具体可以看api文档
        var thumbnailHeight = 100;


        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/famdearActualReport/saveAttachment',

            formData: {
                'objId':'${id!}',
                'objType':'activity_1',
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#activity_1',
            duplicate:true,
            // dnd: '#uploader .uploader-list',
            // paste: '#uploader',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        var uploader1 = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/famdearActualReport/saveAttachment',

            formData: {
                'objId':'${id!}',
                'objType':'ticket_1',
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#ticket_1',
            duplicate:true,
            // dnd: '#uploader .uploader-list',
            // paste: '#uploader',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        $("#filelist").find("li").each(function () {
            var $this = $(this);
            $this.on( 'mouseenter', function() {
                $this.find(".file-panel").stop().animate({height: 30});
            });
            $this.on( 'mouseleave', function() {
                $this.find(".file-panel").stop().animate({height: 0});
            });
        });
        $("#filelist").find("li").find(".file-panel").on( 'click', function() {
            var index = $(this).index(),
                    deg;
            var id = $(this).find("#picId").val();

            switch ( index ) {
                case 0:
                    removeFile( id);
                    return;

                case 1:
                    removeFile( id);
                    break;

                case 2:
                    removeFile( id);
                    break;
            }

            if ( supportTransition ) {
                deg = 'rotate(' + file.rotation + 'deg)';
                $wrap.css({
                    '-webkit-transform': deg,
                    '-mos-transform': deg,
                    '-o-transform': deg,
                    'transform': deg
                });
            } else {
                $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');

            }


        });
        // 当有文件添加进来的时候
        uploader.on( 'fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="imgWrap">' +
                    '<img>' +
                    '</p>'+
                    '</li>' ),
                    $img = $li.find('img'),

                    $btns = $('<div class="file-panel">' +
                            '<span class="cancel">删除</span></div>').appendTo( $li );


            // $list为容器jQuery实例
            // $queue.append( $li );
            // refreshPic();
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            uploader.makeThumb( file, function( error, src ) {   //webuploader方法
                if ( error ) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }

                $img.attr( 'src', src );
            }, thumbnailWidth, thumbnailHeight );

            // uploader.upload();
        });
        // 文件上传过程中创建进度条实时显示。
        uploader.on( 'uploadProgress', function( file, percentage ) {
            var $li = $( '#'+file.id ),
                    $percent = $li.find('.progress span');

            // 避免重复创建
            if ( !$percent.length ) {
                $percent = $('<p class="progress"><span></span></p>')
                        .appendTo( $li )
                        .find('span');
            }

            $percent.css( 'width', percentage * 100 + '%' );
        });

        // 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on( 'uploadSuccess', function( file ) {
            refreshPic(index);
            $( '#'+file.id ).addClass('upload-state-done');
        });

        // 文件上传失败，显示上传出错。
        uploader.on( 'uploadError', function( file ) {
            var $li = $( '#'+file.id ),
                    $error = $li.find('div.error');

            // 避免重复创建
            if ( !$error.length ) {
                $error = $('<div class="error"></div>').appendTo( $li );
            }

            console.log("上传成功");
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').remove();
        });
        uploader1.on( 'uploadSuccess', function( file ) {
            refreshPic(index);
            $( '#'+file.id ).addClass('upload-state-done');
        });
        $btn.on( 'click', function() {
            console.log("上传...");
            uploader.upload();
            console.log("上传成功");
        });
    });

    // 负责view的销毁
    function removeFile( id) {
        deletePic(id)
    }

    function refreshPic(param){
        var attr = param.split("_");
        var id = $("#id").val();
        if(attr[0]=="ticket"){
            $("#ticketImages" + attr[1]).load("${request.contextPath}/familydear/famdearActualReport/showAllpic?id=" + id + "&objType=" + param);
        }else {
            $("#images" + attr[1]).load("${request.contextPath}/familydear/famdearActualReport/showAllpic?id=" + id + "&objType=" + param);
        }

    }
    function add1() {
        titleNum=titleNum+1;
        if(titleNum>10){
            layerTipMsg(false,"提示!","最多添加10个活动!");
            return;
        }
        var html = "<tr>" +
                "<td class='text-right'>活动标题:</td>" +
                "<td colspan='4'><input type='text' name='activityTitle' id='activityTitle"+titleNum+"' maxlength='1500'   class='form-control'></td>" +
                "</tr>"+
                "<tr>"+
                "<td class='text-right'>活动图片:</td>"+
                "<td ><div id='images"+titleNum+"'></div><a class='form-file pull-left' onclick='addClick(this.id);' id='activity_"+titleNum+"'><i class='fa fa-plus'></i><input type='file' name='file' id='upFile'></a></td>"+
                "</tr>";
        var $tr = $("#tab1 tr").eq(-2);
        if($tr.size()==0){
            alert("指定的table id或行数不存在！");
            return;
        }
        $tr.after(html);
        var objType='activity_'+titleNum;
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/famdearActualReport/saveAttachment',

            formData: {
                'objId':'${id!}',
                'objType':objType,
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#activity_'+titleNum,
            duplicate:true,
            // dnd: '#uploader .uploader-list',
            // paste: '#uploader',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        uploader.on( 'uploadSuccess', function( file ) {
            refreshPic(index);
            $( '#'+file.id ).addClass('upload-state-done');
        });
    }
    function add2() {
        ticketNum=ticketNum+1;
        var html = "<tr>" +
                "<td class='text-right'>车票说明:</td>" +
                "<td><input type='text' name='ticketTitle' id='ticketTitle"+ticketNum+"' maxlength='1500'   class='form-control'></td>" +
                "</tr>"+
                "<tr>"+
                "<td class='text-right'>车票图片:</td>"+
                "<td><div id='ticketImages"+ticketNum+"'></div><a class='form-file pull-left' onclick='addClick(this.id);' id='ticket_"+ticketNum+"'><i class='fa fa-plus'></i><input type='file' name='file' id='upFile'></a></td>"+
                "</tr>";
        var $tr = $("#tab2 tr").eq(-2);
        if($tr.size()==0){
            alert("指定的table id或行数不存在！");
            return;
        }
        $tr.after(html);
        var objType='ticket_'+ticketNum;
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/famdearActualReport/saveAttachment',

            formData: {
                'objId':'${id!}',
                'objType':objType,
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#ticket_'+ticketNum,
            // dnd: '#uploader .uploader-list',
            // paste: '#uploader',
            duplicate:true,
            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        uploader.on( 'uploadSuccess', function( file ) {
            refreshPic(index);
            $( '#'+file.id ).addClass('upload-state-done');
        });
        // $('#myDiv').scrollTop(scrollHeight,100);
    }
    
    function save() {
        //7.	访亲时间、活动说明、标题、图片、车票说明、图片，必填
        var activityId = $("#activityId1").val();
        if(!activityId){
            layerTipMsg(false,"提示!","访亲轮次不能为空!");
            return;
        }
        var arrangeId = $("#arrangeId1").val();
        if(!arrangeId){
            layerTipMsg(false,"提示!","访亲批次不能为空!");
            return;
        }
        var arriveTime = $("#arriveTime").val();
        var backTime = $("#backTime").val();
        var donateMoney = $("#donateMoney").val();
        if(donateMoney){
            var float=isFloat(donateMoney);
            if(!float){
                layerTipMsg(false,"提示!","捐款金额不符合格式!");
                return;
            }
        }
        var donateMark = $("#donateMark").val();
        var donateObjectnum = $("#donateObjectnum").val();
        if(donateObjectnum){
            var num=isNum(donateObjectnum);
            if(!num){
                layerTipMsg(false,"提示!","捐款件数不符合格式!");
                return;
            }
        }
        var conversionAmount = $("#conversionAmount").val();
        if(conversionAmount){
            var float=isFloat(conversionAmount);
            if(!float){
                layerTipMsg(false,"提示!","折合金额不符合格式!");
                return;
            }
        }
        var donateobjMark = $("#donateobjMark").val();
        var seekMedical = $("#seekMedical").val();
        if(seekMedical){
            var num=isNum(seekMedical);
            if(!num){
                layerTipMsg(false,"提示!","就医件数不符合格式!");
                return;
            }
        }
        var seekStudy = $("#seekStudy").val();
        if(seekStudy){
            var num=isNum(seekStudy);
            if(!num){
                layerTipMsg(false,"提示!","就学件数不符合格式!");
                return;
            }
        }
        var seekEmploy = $("#seekEmploy").val();
        if(seekEmploy){
            var num=isNum(seekEmploy);
            if(!num){
                layerTipMsg(false,"提示!","就业件数不符合格式!");
                return;
            }
        }
        var developProduct = $("#developProduct").val();
        if(developProduct){
            var num=isNum(developProduct);
            if(!num){
                layerTipMsg(false,"提示!","发展生产件数不符合格式!");
                return;
            }
        }
        var otherThingsnum = $("#otherThingsnum").val();
        if(otherThingsnum){
            var num=isNum(otherThingsnum);
            if(!num){
                layerTipMsg(false,"提示!","其他件数不符合格式!");
                return;
            }
        }
        var benefitPeople = $("#benefitPeople").val();
        if(benefitPeople){
            var num=isNum(benefitPeople);
            if(!num){
                layerTipMsg(false,"提示!","惠及各族群众户数不符合格式!");
                return;
            }
        }
        var walkPeople = $("#walkPeople").val();
        if(walkPeople){
            var num=isNum(walkPeople);
            if(!num){
                layerTipMsg(false,"提示!","走访各族群众户数不符合格式!");
                return;
            }
        }
        var place = $("#place").val();
        var activityContent = $("#activityContent").val();
        if(!activityContent){
            layerTipMsg(false,"提示!","活动内容说明不能为空!");
            return;
        }
        var activityTitle = $("input[name='activityTitle']").val();
        var ticketTitle = $("input[name='ticketTitle']").val();
        var mark = $("#mark").val();
        if(!arriveTime){
            layerTipMsg(false,"提示!","访亲到达时间不能为空!");
            return;
        }
        if(!backTime){
            layerTipMsg(false,"提示!","访亲返回时间不能为空!");
            return;
        }
        if(arriveTime&&backTime){
            if(arriveTime>backTime){
                layerTipMsg(false,"提示!","访亲到达时间不能大于访亲返回时间!");
                return;
            }
        }
        for(var i=1;i<titleNum+1;i++){
            var title = $("#activityTitle"+i).val();
            if(i==1){
                if(!title){
                    layerTipMsg(false,"提示!","活动标题不能为空!");
                    return
                }
            }
            // if(!title){
            //     layerTipMsg(false,"提示!","访亲标题不能为空!");
            // }
            if(title){
                var imageNum = $("#images"+i).find("a").length;
                if(imageNum==0){
                    layerTipMsg(false,"提示!","活动图片不能为空!");
                    return;
                }
            }
        }
        for(var i=1;i<ticketNum+1;i++){
            var ticket = $("#ticketTitle"+i).val();
            if(i==1){
                if(!ticket){
                    layerTipMsg(false,"提示!","车票说明不能为空!");
                    return
                }
            }
            // if(!title){
            //     layerTipMsg(false,"提示!","访亲标题不能为空!");
            // }
            if(ticket){
                var imageNum = $("#ticketImages"+i).find("a").length;
                if(imageNum==0){
                    layerTipMsg(false,"提示!","车票图片不能为空!");
                    return;
                }
            }
        }
        var picIds = $("#picIds").val();
        var url= "";
        if(picIds){
            url="${request.contextPath}/familydear/famdearActualReport/famdearActualReportSave?picIds="+picIds
        }else {
            url="${request.contextPath}/familydear/famdearActualReport/famdearActualReportSave?"
        }
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
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    $("#activityId").val($("#activityId1").val());
                    $("#village").val("");
                    goBack();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
    
    function isFloat(num) {
        var isFloat = true;
        var re = /^(0|[1-9]\d*|[1-9]\d*\.\d+|0\.\d*[1-9]\d*)$/;
        if(!re.test(num)){
            isFloat = false;
        }
        return isFloat;
    }
    function isNum(num) {
        var isNumber = true;
        var re = /^[1-9]+[0-9]*]*$/;
        if(!re.test(num)){
            isNumber = false;
        }
        return isNumber;
    }
    function activityChange(activityId){
        var activityId = $("#activityId1").val();
        var url = "${request.contextPath}/familydear/famdearActualReport/arrangeList";
        $.ajax({
            url:url,
            data:{'activityId':activityId},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#arrangeId1").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }
                else {
                    htmlStr = htmlStr+"<option value=''>暂无需要填报的批次</option>";
                }
                $("#arrangeId1").html(htmlStr);
                queryArrangeTime();
                // showList1();
            }

        })
    }
    function queryArrangeTime() {
        var arrangeId=$("#arrangeId1").val();
        var url = "${request.contextPath}/familydear/famdearActualReport/queryArrangeTime";
        if(arrangeId) {
            $.ajax({
                url: url,
                data: {'arrangeId': arrangeId},
                dataType: 'json',
                type: 'post',
                success: function (data) {
                    var startTime = data.startTime;
                    var endTime = data.endTime;
                    $("#arriveTime").val(startTime);
                    $("#backTime").val(endTime);
                }

            })

        }
    }
</script>