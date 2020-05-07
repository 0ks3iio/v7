<#import "/fw/macro/popupMacro.ftl" as popup />
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div class="layer layer-addTerm layer-change" style="display:block;" id="addDiv">
    <form id="subForm">
        <input type="hidden" id="id" name="id" value="${id}">
        <div class="layer-body" style="height:350px;overflow:auto;">
            <div class="filter clearfix">
                <div class="filter clearfix">
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动分类：</label>
                        <div class="filter-content" style="width:180px;">
                            <select name="type" class="form-control" style="width:180px;" id="monthType" onchange="changeType(this.value)">
                                <#if hasLeadPermission><option value="1" <#if type=="1">selected</#if>>访亲轮次活动</option></#if>
                                <#if hasPermission><option value="2" <#if type=="2">selected</#if>>部门每月活动</option></#if>
                            </select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动形式：</label>
                        <div class="filter-content" style="width:180px;">
                            <select name="activityForm" class="form-control" style="width:180px;">
                                <option value="1">座谈报告会（场）</option>
                                <option value="2">联欢会（场）</option>
                                <option value="3">文体活动（场）</option>
                                <option value="4">双语学习（次）</option>
                                <option value="5">参观学习（次）</option>
                                <option value="6">党组织生活（次）</option>
                                <option value="7">主题班会（场）</option>
                                <option value="8">主题团</option>
                                <option value="9">队会（场）</option>
                                <option value="0">其他（场）</option>
                            </select>
                        </div>
                    </div>

                    <div class="filter-item" id="activityIdDiv">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动轮次：</label>
                        <div class="filter-content" style="width:500px;">
                            <select type="text" class="form-control" id="activityId" name="activityId"   style="width:100%;" onchange="changeActivity(this.value)" >
                                <#if activityList?exists && (activityList?size>0)>
                                    <#list activityList as item>
                                        <option value="${item.id!}">${item.title!}</option>
                                    </#list>
                                <#else>
                                        <option value="">未设置</option>
                                </#if>

                            </select>
                        </div>
                    </div>
                    <div class="filter-item" id="arrangeIdDiv">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动批次：</label>
                        <div class="filter-content" style="width:500px;">
                            <select type="text" class="form-control"  id="arrangeId" name="arrangeId"   style="width:100%;" ></select>
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
                                <input class="form-control date-picker" vtype="data"  type="text" nullable="false" name="activityTime" id="activityTime" placeholder="活动开始时间" >
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
                                <input class="form-control date-picker" vtype="data"  type="text" nullable="false" name="activityEndTime" id="activityEndTime" placeholder="活动结束时间" >
                                <span class="input-group-addon">
						<i class="fa fa-calendar bigger-110"></i>
					</span>
                            </div>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>地点：</label>
                        <div class="filter-content" style="width:500px;">
                            <input type="text" class="form-control" maxlength="50" id="place" name="place"    style="width:100%;">
                        </div>
                    </div>

                    <#--<input type="hidden" class='form-control ' id="leaderUserId${item1_index}" name='famDearArrangeList[${item1_index}].leaderUserId' onclick="editLeader(${item1_index})" value="${item1.leaderUserId!}"/>-->
                    <#--<input type="text" class='form-control ' id="leaderUserName${item1_index}" onclick="editLeader(${item1_index})" value="${item1.leaderUserNames!}"/>-->
                    <#--<div style="display: none;">-->
                        <#--<@popup.selectMoreTeacherUser clickId="user${item1_index}Name" id="user${item1_index}Ids" name="user${item1_index}Name" handler="savePermission(${item1_index})">-->
                            <#--<input type="hidden" id="user${item1_index}Ids" name="user${item1_index}Ids" value=${item1.leaderUserId!}>-->
                            <#--<input type="text" id="user${item1_index}Name" class="form-control" value=${item1.leaderUserNames!}>-->
                        <#--</@popup.selectMoreTeacherUser>-->
                    <#--</div>-->

                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>参加人员：</label>
                        <div class="filter-content" style="width:500px;">

                            <input type="hidden" class="form-control" id="partUserIds" name="partUserIds" style="width:100%;" >
                            <textarea type="text" class='form-control ' id="partUseNames" onclick="editLeader()" readonly style="width:100%;" ></textarea>
                        </div>
                        <div style="display: none;">
                        <@popup.selectMoreTeacherUser clickId="userName" id="userIds" name="userName" handler="savePartUseIds()">
                            <input type="hidden" id="userIds" name="userIds" value=>
                            <input type="text" id="userName" class="form-control" value=>
                        </@popup.selectMoreTeacherUser>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>参加人数：</label>
                        <div class="filter-content" style="width:500px;">
                            <input type="text" class="form-control"  maxlength="1000" id="partnum" name="partnum"   style="width:100%;" readonly>
                        </div>
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动情况说明：</label>
                        <div class="filter-content" style="width:500px;">
                            <textarea type="text" class="form-control"  maxlength="1000" id="activityContent" name="activityContent"   style="width:100%;" ></textarea>
                        </div>
                    </div>
                    <div class="filter-item" id="tab1">
                        <label for="" class="filter-name"><span style="color:red">*</span>活动图片：</label>
                        <div class="filter-content" style="width:500px;">
                            <div id="images1" class="js-layer-photos">
                            </div>
                            <a class="form-file pull-left" onclick="addClick(this.id);" id="famdearMonth">
                                <i  class="fa fa-plus"></i>
                                <div ></div>
                            </a>
                        </div>
                        <#--<tr>-->
                            <#--<td class="text-right"><span style="color:red">*</span>活动图片:</td>-->
                            <#--<td>-->
                                <#--<div id="images1" class="js-layer-photos">-->
                                <#--</div>-->
                                <#--<a class="form-file pull-left" onclick="addClick(this.id);" id="activity_1">-->
                                    <#--<i  class="fa fa-plus"></i>-->
                                    <#--<div ></div>-->
                                <#--</a>-->
                            <#--</td>-->
                        <#--</tr>-->
                    </div>
                    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>备注：</label>
                        <div class="filter-content" style="width:500px;">
                            <textarea type="text" class="form-control"  maxlength="100" id="mark" name="mark"   style="width:100%;" ></textarea>
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
<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
</div>
<script>
    function changeActivity(id){
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
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }else {
                    htmlStr = htmlStr+"<option value=''>未设置</option>";
                }
                $("#arrangeId").html(htmlStr);
                //showList1();
            }

        })
    }



    function editLeader() {
        $('#userName').click();
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
        var type = $("#monthType").val();
        // var type = $("#type");
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
                'objId':'${id!}',
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
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });

    var isSubmit=false;

    $("#arrange-commit").on("click",function () {

        var type = $("#monthType").val();
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
        var options = {
            url : "${request.contextPath}/familydear/famdearMonth/famdearMonthSave?",
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
                    showList2();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    })

    function deletePic(){
        $.ajax({
            url:'${request.contextPath}/studevelop/common/attachment/delete',
            data: {"ids":toDelIds},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                    refreshList();
                }
                else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
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