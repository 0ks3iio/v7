<#import "/fw/macro/popupMacro.ftl" as popup />
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div class="layer layer-addTerm layer-change" style="display:block;" id="addDiv">
    <form id="subForm">
        <input type="hidden" name="year" id="year" value="${famdearMonth.year!}" >
        <input type="hidden" name="createUserId" id="createUserId" value="${famdearMonth.createUserId!}" >
        <input type="hidden" name="createTime" id="createTime" value="${famdearMonth.createTime!}" >
        <input type="hidden" name="unitId" id="unitId" value="${famdearMonth.unitId}" >
        <input type="hidden" name="id" id="id" value="${famdearMonth.id}" >
        <input type="hidden" name="state" id="state" value="${famdearMonth.state}" >
        <input type="hidden" id="picIds" value="">
        <div class="layer-body" style="height:350px;overflow:auto;">
            <div class="filter clearfix">
                <div class="filter clearfix">
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动分类：</label>
                        <div class="filter-content" style="width:180px;">
                            <select name="type" class="form-control" style="width:180px;" id="type" onchange="changeType(this.value)" <#if type?default("2")=="2">disabled="disabled"</#if>>
                                <option value="1" <#if famdearMonth.type?default("1")=="1"> selected </#if>>访亲轮次活动</option>
                                <option value="2" <#if famdearMonth.type?default("1")=="2"> selected </#if>>部门每月活动</option>
                            </select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动形式：</label>
                        <div class="filter-content" style="width:180px;">
                            <select name="activityForm" class="form-control" style="width:180px;" <#if type?default("2")=="2">disabled="disabled"</#if>>
                                <option value="1" <#if famdearMonth.activityForm?default("")=="1"> selected </#if>>座谈报告会（场）</option>
                                <option value="2" <#if famdearMonth.activityForm?default("")=="2"> selected </#if>>联欢会（场）</option>
                                <option value="3" <#if famdearMonth.activityForm?default("")=="3"> selected </#if>>文体活动（场）</option>
                                <option value="4" <#if famdearMonth.activityForm?default("")=="4"> selected </#if>>双语学习（次）</option>
                                <option value="5" <#if famdearMonth.activityForm?default("")=="5"> selected </#if>>参观学习（次）</option>
                                <option value="6" <#if famdearMonth.activityForm?default("")=="6"> selected </#if>>党组织生活（次）</option>
                                <option value="7" <#if famdearMonth.activityForm?default("1")=="7"> selected </#if>>主题班会（场）</option>
                                <option value="8" <#if famdearMonth.activityForm?default("")=="8"> selected </#if>>主题团</option>
                                <option value="9" <#if famdearMonth.activityForm?default("")=="9"> selected </#if>>队会（场）</option>
                                <option value="0" <#if famdearMonth.activityForm?default("")=="0"> selected </#if>>其他（场）</option>
                            </select>
                        </div>
                    </div>

                    <div class="filter-item" id="activityIdDiv">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动轮次：</label>
                        <div class="filter-content" style="width:500px;">
                            <#if type?default("2")=="1">
                                <select type="text" class="form-control" id="activityId" name="activityId" style="width:100%;" onchange="changeActivity(this.value)" >
                                    <#if activityList?exists && (activityList?size>0)>
                                        <#list activityList as item>
                                            <option value="${item.id!}" <#if famdearMonth.activityId?default("")=="${item.id!}"> selected </#if>>${item.title!}</option>
                                        </#list>
                                    <#else>
                                            <option value="">未设置</option>
                                    </#if>

                                </select>
                            <#else >
                                <input type="text" class="form-control" maxlength="50" id="place" name="place" value="${famdearMonth.activityName!}" disabled="disabled" style="width:100%;" >
                            </#if>
                        </div>
                    </div>
                    <div class="filter-item" id="arrangeIdDiv">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动批次：</label>
                        <div class="filter-content" style="width:500px;">
                            <input type="hidden" id="famArrangeId" value="${famdearMonth.activityId!}">
                            <#if type?default("2")=="1">
                                <select type="text" class="form-control"  id="arrangeId" name="arrangeId"   style="width:100%;" <#if type?default("2")=="2">disabled="disabled"</#if> ></select>
                            <#else >
                                <input type="text" class="form-control" maxlength="50" id="place" name="place" value="${famdearMonth.arrangeMame!}" disabled="disabled" style="width:100%;" >
                            </#if>
                        </div>
                    </div>
                    <div class="filter-item" id="deptIdDiv" style="display: none">
                        <label for="" class="filter-name"><span style="color:red">*</span>关联部门：</label>
                        <div class="filter-content" style="width:500px;">
                            <input type="hidden" name="deptId" id="deptId" value="${deptId!}">
                            <label class="form-control" style="width: 100%">${deptName!}</label>
                        <#--<select type="text" class="form-control" id="deptId" name="deptId"   style="width:100%;" ></select>-->
                        </div>
                    </div>

                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动开始时间：</label>
                        <div class="filter-content">
                            <div class="input-group" style="width: 180px">
                                <input class="form-control date-picker" vtype="data"  type="text" nullable="false" name="activityTime" <#if type?default("2")=="2">disabled="disabled"</#if> id="activityTime" value="${famdearMonth.activityTime?string('yyyy-MM-dd')}" placeholder="活动开始时间" >
                                <span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
                            </div>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动结束时间：</label>
                        <div class="filter-content">
                            <div class="input-group" style="width: 180px">
                                <input class="form-control date-picker" vtype="data"  type="text" nullable="false" <#if type?default("2")=="2">disabled="disabled"</#if> name="activityEndTime" id="activityEndTime" value="${famdearMonth.activityEndTime?string('yyyy-MM-dd')}" placeholder="活动结束时间" >
                                <span class="input-group-addon">
						<i class="fa fa-calendar bigger-110"></i>
					</span>
                            </div>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>地点：</label>
                        <div class="filter-content" style="width:500px;">
                            <input type="text" class="form-control" maxlength="50" id="place" name="place" value="${famdearMonth.place!}" <#if type?default("2")=="2">disabled="disabled"</#if>  style="width:100%;" >
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>参加人员：</label>
                        <div class="filter-content" style="width:500px;">

                            <input type="hidden" class="form-control" id="partUserIds" name="partUserIds" style="width:100%;" value="${famdearMonth.partUserIds!}" >
                            <textarea type="text" class='form-control ' id="partUseNames" <#if type?default("2")=="1">onclick="editLeader()"</#if> readonly style="width:100%;"  >${famdearMonth.partUserNames!}</textarea>
                        </div>
                        <div style="display: none;">
                        <@popup.selectMoreTeacherUser clickId="userName" id="userIds" name="userName" handler="savePartUseIds()">
                            <input type="hidden" id="userIds" name="userIds" value=${famdearMonth.partUserIds!}>
                            <input type="text" id="userName" class="form-control" value=>
                        </@popup.selectMoreTeacherUser>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>参加人数：</label>
                        <div class="filter-content" style="width:500px;">
                            <input type="text" class="form-control"  maxlength="1000" id="partnum" name="partnum"  readonly value="${famdearMonth.partnum!}" <#if type?default("2")=="2">disabled="disabled"</#if> style="width:100%;" >
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动情况说明：</label>
                        <div class="filter-content" style="width:500px;">
                            <textarea type="text" class="form-control"  maxlength="1000" id="activityContent" name="activityContent"  value="${famdearMonth.activityContent}" <#if type?default("2")=="2">disabled="disabled"</#if> style="width:100%;" >${famdearMonth.activityContent}</textarea>
                        </div>
                    </div>
                    <div class="filter-item" id="tab1">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动图片：</label>
                        <div class="filter-content" style="width:500px;">
                            <div id="images1" class="js-layer-photos">
                                <#if famdearMonth.attachmentList?exists&& (famdearMonth.attachmentList?size > 0)>
                                    <#list famdearMonth.attachmentList as item1>
                                        <span class="position-relative float-left mr10 mb10">
                                            <a class="pull-left">
                                                 <img id ="" style="width: 94px;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=1" src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=0" alt="">
                                             </a>
                                            <#if type?default("2")=="1">
                                                <a class="pos-abs" style="top: -10px;right: -6px;" onclick="delPic('${item1.id!}')">
                                                    <i class="fa fa-times-circle color-red"></i>
                                                </a>
                                            </#if>
                                        </span>
                                    </#list>
                                </#if>
                            </div>
                            <#if type?default("2")=="1">
                                <a class="form-file pull-left" onclick="addClick(this.id);" id="famdearMonth">
                                    <i  class="fa fa-plus"></i>
                                    <div ></div>
                                </a>
                            </#if>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>备注：</label>
                        <div class="filter-content" style="width:500px;">
                            <textarea type="text" class="form-control"  maxlength="100" id="mark" name="mark"  value="${famdearMonth.mark}" <#if type?default("2")=="2">disabled="disabled"</#if> style="width:100%;" >${famdearMonth.mark}</textarea>
                        </div>
                    </div>
                <#--
                <div class="filter-item">
                    <label for="" class="filter-name">非正常成绩是否统分：</label>
                    <div class="filter-content">
                        <label>
                            <input name="isTotalScore" type="radio" <#if '${examInfo.isTotalScore!}'=='0' || '${examInfo.isTotalScore!}'==''>checked</#if> class="ace" value="0"/>
                            <span class="lbl"> 否</span>
                        </label>&nbsp;&nbsp;
                        <label>
                            <input name="isTotalScore" type="radio" <#if '${examInfo.isTotalScore!}'=='1'>checked</#if> class="ace" value="1"/>
                            <span class="lbl"> 是</span>
                        </label>
                    </div>
                </div>
                -->
                </div>
            </div>
        </div>
    </form>
</div>
<#if type?default("2")=="1">
<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
</div>
</#if>
<script>
    function changeActivity(id){
        var id = $("#famArrangeId").val();
        var url = "${request.contextPath}/familydear/famdearMonth/famdearChangeActivity";
        $.ajax({
            url:url,
            data:{'activityId':id},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var htmlStr="";
                $("#arrangeId").empty();
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        if(id==data[i].id) {
                            htmlStr = htmlStr + "<option value='" + data[i].id + "' selected >" + data[i].name + "</option>";
                        }else {
                            htmlStr = htmlStr + "<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                        }
                    }
                }else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#arrangeId").html(htmlStr);
                //showList1();
            }

        })
    }
    var index;
    function addClick(id){
        // var attr = id.split(",");
        // index = attr[1];
        index = id;
    }
    $(function () {
        var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
        initCalendarData("#addDiv",".date-picker",viewContent);
        var type = $("#type").val();
        if(type){
            changeType(type)
        }
        var activityId = $("#activityId").val();
        if(activityId){
            changeActivity(activityId);
        }
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
                'objId':'${famdearMonth.id!}',
                'objType':'famdearMonth',
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#famdearMonth',
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
        $btn.on( 'click', function() {
            console.log("上传...");
            uploader.upload();
            console.log("上传成功");
        });
    })

    function removeFile( id) {
        alert(id);
        deletePic(id)
    }
    function refreshPic(param){
        var id = $("#id").val();
        var param = "famdearMonth";
        $("#images1").load("${request.contextPath}/familydear/famdearActualReport/showAllpic?id=" + id + "&objType=" + param);

    }
    function delPic(id,tt){
        var picIds;
        picIds = $("#picIds").val()+","+id;
        $("#picIds").val(picIds)
        $.ajax({
            url:'${request.contextPath}/familydear/famdearActualReport/delPic',
            data: {"id":id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    // layer.closeAll();
                    // layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                    refreshPic(tt);
                }
                else{
                    // layer.closeAll();
                    // layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
    function changeType(value){
        if(value==1){
            $("#deptIdDiv").hide();
            $("#deptId").val("");
            $("#activityIdDiv").show();
            $("#arrangeIdDiv").show();

        }else {
            $("#deptIdDiv").show();
            $("#activityIdDiv").hide();
            $("#arrangeIdDiv").hide();
            $("#activityId").val("");
            $("#arrangeId").val("");
        }
    }
    $("#arrange-close").on("click", function(){
        goBack();
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    var isSubmit=false;

    $("#arrange-commit").on("click",function () {
        var type = $("#type").val();
        var activityId=$("#activityId").val();
        var arrangeId=$("#arrangeId").val();
        var deptId = $("#deptId").val();
        if(type=="1"){
            if(!activityId){
                layerTipMsg(false,"提示!","活动轮次不能为空!");
                return;
            }
            if(!arrangeId){
                layerTipMsg(false,"提示!","活动批次不能为空!");
                return;
            }
        }else {
            if(!deptId){
                layerTipMsg(false,"提示!","关联部门不能为空!");
                return;
            }
        }
        var activityTime=$("#activityTime").val();
        if(!activityTime){
            layerTipMsg(false,"提示!","活动开始时间不能为空!");
            return;
        }
        var activityEndTime=$("#activityEndTime").val();
        if(!activityEndTime){
            layerTipMsg(false,"提示!","活动结束时间不能为空!");
            return;
        }
        if(activityTime&&activityEndTime){
            if(activityTime>activityEndTime){
                layerTipMsg(false,"提示!","活动开始时间不能大于结束时间!");
                return;
            }
        }
        var place=$("#place").val();
        if(!place){
            layerTipMsg(false,"提示!","活动地点不能为空!");
            return;
        }
        var partUserIds=$("#partUserIds").val();
        if(!partUserIds){
            layerTipMsg(false,"提示!","参加人员不能为空!");
            return;
        }
        var partnum=$("#partnum").val();
        if(!partnum){
            layerTipMsg(false,"提示!","参加人数不能为空!");
            return;
        }else {
            var num = isNum(partnum);
            if(!num){
                layerTipMsg(false,"提示!","活动人数必须为正整数!");
                return;
            }
        }
        var activityContent=$("#activityContent").val();
        if(!activityContent){
            layerTipMsg(false,"提示!","活动情况说明不能为空!");
            return;
        }
        var imageNum = $("#images1").find("a").length;
        if(imageNum==0){
            layerTipMsg(false,"提示!","活动图片不能为空!");
            return;
        }
        var mark=$("#mark").val();
        if(!mark){
            layerTipMsg(false,"提示!","备注不能为空!");
            return;
        }
        var picIds = $("#picIds").val();
        var url= "";
        if(picIds){
            url="${request.contextPath}/familydear/famdearMonth/famdearMonthSave?picIds="+picIds
        }else {
            url="${request.contextPath}/familydear/famdearMonth/famdearMonthSave?"
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
                    showMonthList();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    });

    function editLeader() {
        $('#userName').click();
    }

    function showMonthList(){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var type = $("#type").val();
        if(type==2){
            <#if hasPermission>
                $("#addDiv").show();
            <#else >
                $("#addDiv").hide();
            </#if>
        }else{
            <#if hasLeadPermission>
                $("#addDiv").show();
            <#else >
                $("#addDiv").hide();
            </#if>
        }
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        if(startTime&&endTime) {
            if (startTime > endTime) {
                layerTipMsg(false, "提示!", "活动开始时间不能大于结束时间!");
                return;
            }
            var url =  '${request.contextPath}/familydear/famdearMonth/famdearMonthList?&type='+type+"&endTime="+endTime+"&startTime="+startTime+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize;
            $("#showList2").load(url);
        }else {
            var url =  '${request.contextPath}/familydear/famdearMonth/famdearMonthList?&type='+type+"&endTime="+endTime+"&startTime="+startTime+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize;
            $("#showList2").load(url);
        }

    }

    function savePartUseIds(){
        if(isSubmit){
            return;
        }
        var userIds = $('#userIds').val();
        if(userIds != ''){
            var arr = userIds.split(",");
            if(arr.length > 100){
                layerTipMsgWarn("保存失败","人员最多100人！");
                return;
            }else {
                $("#partnum").val(arr.length);
            }
        }
        var userIds = $('#userIds').val();
        var userName = $('#userName').val();
        $('#partUserIds').val(userIds);
        $('#partUseNames').val(userName);

    }

    function isNum(num) {
        var isNumber = true;
        var re = /^[1-9]+[0-9]*]*$/;
        if(!re.test(num)){
            isNumber = false;
        }
        return isNumber;
    }

</script>