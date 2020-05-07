
<#--<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/webuploader/webuploader.css"/>-->
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div id="myDiv">
    <form id="subForm" >
        <input type="hidden" name="year" id="year" value="${famdearActualReport.year}">
        <#--<input type="hidden" name="activityId" value="${famdearActualReport.activityId}">-->
        <input type="hidden" id="activityId" value="${activityId!}">
        <input type="hidden" id="village" value="${village!}">
        <input type="hidden" name="createUserId" value="${famdearActualReport.createUserId}">
        <input type="hidden" name="id" id="reportId" value="${famdearActualReport.id}">
        <input type="hidden" name="unitId" value="${famdearActualReport.unitId}">
        <input type="hidden" name="state" value="${famdearActualReport.state}">
        <input type="hidden" name="createTime" value="${famdearActualReport.createTime}">
        <input type="hidden" id="activityId" value="${activityId!}">
        <input type="hidden" id="picIds" value="">
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
                                        <#if type?default("2")=="1">
                                        <select name="activityId" class="form-control" id="activityId1" onchange="activityChange(this.value)">
                                         <#if activityList?exists && (activityList?size>0)>-->
                                             <#list activityList as item>
                                                 <option value="${item.id!}" <#if famdearActualReport.activityId?default('a')==item.id>selected</#if>>${item.title!}</option>
                                             </#list>
                                         <#else>
                                                 <option value="">暂无需要填报的轮次</option>
                                         </#if>
                                        <#--arrangeList-->
                                        </select>
                                        <#else >
                                             <input type="text" class="form-control" maxlength="50" id="place" name="place" value="${famdearActualReport.activityName!}" disabled="disabled" style="width:100%;" >
                                        </#if>
                                    </td>
                                    <td class="text-right" width="150"><span style="color:red">*</span>访亲批次:</td>
                                    <td>
                                        <#if type?default("2")=="1">
                                            <select name="arrangeId" class="form-control" id="arrangeId">
                                                <#if arrangeList?exists && (arrangeList?size>0)>-->
                                                     <#list arrangeList as item>
                                                         <option value="${item.id!}" <#if famdearActualReport.arrangeId?default('a')==item.id>selected</#if>>${item.batchType!}</option>
                                                     </#list>
                                                <#else>
                                                         <option value="">暂无需要填报的轮次</option>
                                                </#if>
                                            </select>
                                        <#else >
                                             <input type="text" class="form-control" maxlength="50" id="place" name="place" value="${famdearActualReport.arrangeName!}" disabled="disabled" style="width:100%;" >
                                        </#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><span style="color:red">*</span>访亲到达时间:</td>
                                    <td><input type="text" class='form-control date-picker' vtype='data' name="arriveTime" id="arriveTime" value="${famdearActualReport.arriveTime?string('yyyy-MM-dd')!}" maxlength="30" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    <td class="text-right"><span style="color:red">*</span>返回时间:</td>
                                    <td><input type="text" class='form-control date-picker' vtype='data' name="backTime" id="backTime" value="${famdearActualReport.backTime?string('yyyy-MM-dd')!}"  maxlength="30" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                </table>
                            <table class="table table-bordered table-striped no-margin" id="tab2">
                                <thead>
                                <tr>
                                    <th colspan="2" class="text-center">车票信息</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#if famdearActualReport.ticketTitles?exists&& (famdearActualReport.ticketTitles?size > 0)>
                                    <#list famdearActualReport.ticketTitles as item>
                                    <tr>
                                        <td class="text-right" width="150"><span style="color:red">*</span>车票说明:</td>
                                        <td><input type="text" name="ticketTitle" id="ticketTitle${item_index+1}" maxlength="1500"  value="${item.title!}" class="form-control" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    </tr>
                                    <tr>
                                        <td class="text-right"><span style="color:red">*</span>车票图片:</td>
                                        <td>
                                            <div class="js-layer-photos" id="ticketImages${item_index+1}">
                                                <#if item.attachmentList?exists&& (item.attachmentList?size > 0)>
                                                    <#list item.attachmentList as item1>

                                                        <span class="position-relative float-left mr10 mb10">
                                                            <a class="pull-left">
                                                                 <img id ="" style="width: 94px;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=1" src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=0" alt="">
                                                             </a>
                                                        <#if type?default("2")=="1">
                                                            <a class="pos-abs" style="top: -10px;right: -6px;" onclick="delPic('${item1.id!}','ticket_${item_index+1}')">
                                                                <i class="fa fa-times-circle color-red"></i>
                                                            </a>
                                                        </#if>
                                                        </span>

                                                    <#--<a class="pull-left" style="margin-left: 10px;margin-right: 10px">-->
                                                    <#--<img id ="" style="width: 100px;height: 100px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=1" src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=0" alt="">-->
                                                    <#--</a>-->
                                                    </#list>
                                                </#if>
                                            </div>
                                            <#if type?default("2")=="1">
                                                <a class="form-file pull-left picker1" id="ticket_${item_index+1}" onclick="addClick(this.id);">
                                                    <i  class="fa fa-plus"></i>
                                                <#--<div id="picker">选择文件</div>-->
                                                    <input>
                                                </a>
                                            </#if>
                                        </td>
                                    </tr>
                                    </#list>
                                </#if>
                                <#if type?default("2")=="1">
                                    <tr>
                                        <td class="text-center" colspan="2">
                                            <a class="table-btn color-blue" onclick="add2()">+新增车票信息</a>
                                        </td>
                                    </tr>
                                </#if>
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
                                    <td><input type="text" class="form-control" name="place" id="place" maxlength="50" value="${famdearActualReport.place!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                <tr>
                                    <td class="text-right" width="150"><span style="color:red">*</span>活动内容说明:</td>
                                    <td><input type="text" maxlength="1000" class="form-control" name="activityContent" id="activityContent" value="${famdearActualReport.activityContent!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
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
                                <#if famdearActualReport.activityTitles?exists&& (famdearActualReport.activityTitles?size > 0)>
                                    <#list famdearActualReport.activityTitles as item>
                                        <tr>
                                            <td class="text-right" width="150"><span style="color:red">*</span>活动标题:</td>
                                            <td><input type="text" name="activityTitle" id="activityTitle${item_index+1}" maxlength="1500"  value="${item.title!}"  class="form-control" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                        </tr>
                                        <tr>
                                            <td class="text-right"><span style="color:red">*</span>活动图片:</td>
                                            <td>
                                                <div class="js-layer-photos" id="images${item_index+1}">
                                                <#if item.attachmentList?exists&& (item.attachmentList?size > 0)>
                                                    <#list item.attachmentList as item1>
                                                        <span class="position-relative float-left mr10 mb10">
                                                            <a class="pull-left">
                                                                 <img id ="" style="width: 94px;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=1" src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=0" alt="">
                                                             </a>
                                                        <#if type?default("2")=="1">
                                                            <a class="pos-abs" style="top: -10px;right: -6px;" onclick="delPic('${item1.id!}','activity_${item_index+1}')">
                                                                <i class="fa fa-times-circle color-red"></i>
                                                            </a>
                                                        </#if>
                                                        </span>
                                                    <#--<a class="pull-left" style="margin-left: 10px;margin-right: 10px">-->
                                                    <#--<img id ="" style="width: 100px;height: 100px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=1" src="${request.contextPath}/familydear/famdearActualReport/showPic?id=${item1.id!}&showOrigin=0" alt="">-->
                                                    <#--</a>-->
                                                    </#list>
                                                </#if>
                                                </div>
                                                <#if type?default("2")=="1">
                                                        <a class="form-file pull-left" onclick="addClick(this.id);" id="activity_${item_index+1}">
                                                            <i  class="fa fa-plus"></i>
                                                            <div ></div>
                                                        </a>
                                                </#if>
                                            </td>
                                        </tr>
                                    </#list>
                                </#if>
                                <#if type?default("2")=="1">
                                    <tr>
                                        <td class="text-center" colspan="2">
                                            <a class="table-btn color-blue" onclick="add1()">+新增活动信息</a>
                                        </td>
                                    </tr>
                                </#if>
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
                                        <#if famdearActualReport.activityFroms?exists>
                                            <label><input type="checkbox" class="wp" value="1" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("1")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>捐款</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="2" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("2")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>捐物</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="3" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("3")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>就医</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="4" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("4")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>就学</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="5" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("5")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>就业</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="6" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("6")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>生产发展</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="7" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("7")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>其他</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="8" name="activityFrom" <#if famdearActualReport.activityFroms?seq_contains("8")>checked</#if> <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>惠及各族群众数</label>
                                        <#else >
                                            <label><input type="checkbox" class="wp" value="1" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>捐款</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="2" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>捐物</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="3" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>就医</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="4" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>就学</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="5" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>就业</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="6" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>生产发展</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="7" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>其他</label>&nbsp;&nbsp;
                                            <label><input type="checkbox" class="wp" value="8" name="activityFrom" <#if type?default("2")=="2">disabled="disabled"</#if>><span class="lbl"></span>惠及各族群众数</label>
                                        </#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right">捐款金额:</td>
                                    <td><input type="text" name="donateMoney" id="donateMoney" nullable="true" class="form-control" value="${famdearActualReport.donateMoney!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    <td class="text-right">捐款说明:</td>
                                    <td><input type="text" class="form-control" name="donateMark" id="donateMark" placeholder="" value="${famdearActualReport.donateMark!}" maxlength="500" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                <thead>
                                <tr>
                                    <th colspan="4" class="text-center">捐物信息</th>
                                </tr>
                                </thead>
                                <#--<tr>-->
                                    <#--<td class="text-right">捐物:</td>-->
                                    <#--<td colspan="3"><input type="text" value="" class="form-control" <#if type?default("2")=="2">disabled="disabled"</#if> value="${famdearActualReport.donateObjectnum!}"></td>-->
                                <#--</tr>-->
                                <tr>
                                    <td class="text-right">捐物件数:</td>
                                    <td><input type="text"  class="form-control" name="donateObjectnum" id="donateObjectnum" placeholder="" value="${famdearActualReport.donateObjectnum!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    <td class="text-right">折合金额:</td>
                                    <td><input type="text" class="form-control"name="conversionAmount" id="conversionAmount" value="${famdearActualReport.conversionAmount!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                <tr>
                                    <td class="text-right">捐物说明:</td>
                                    <td colspan="3">
                                        <textarea rows="3" name="donateobjMark" id="donateobjMark" maxlength="500"  value="${famdearActualReport.donateobjMark!}" class="form-control" <#if type?default("2")=="2">disabled="disabled"</#if>>${famdearActualReport.donateobjMark!}</textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right">就医(件):</td>
                                    <td><input type="text" class="form-control" name="seekMedical" id="seekMedical" value="${famdearActualReport.seekMedical!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    <td class="text-right">就学(件):</td>
                                    <td><input type="text" class="form-control" name="seekStudy" id="seekStudy" value="${famdearActualReport.seekStudy!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                <tr>
                                    <td class="text-right">就业(件):</td>
                                    <td><input type="text" class="form-control" name="seekEmploy" id="seekEmploy" value="${famdearActualReport.seekEmploy!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    <td class="text-right">发展生产(件):</td>
                                    <td><input type="text" class="form-control" name="developProduct" id="developProduct" value="${famdearActualReport.developProduct!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                <tr>
                                    <td class="text-right">其他(件):</td>
                                    <td colspan="3"> <input type="text" class="form-control" name="otherThingsnum" id="otherThingsnum" value="${famdearActualReport.otherThingsnum!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                <tr>
                                    <td class="text-right">惠及各族群众户数:</td>
                                    <td><input type="text" class="form-control" name="benefitPeople" id="benefitPeople" value="${famdearActualReport.benefitPeople!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                    <td class="text-right">走访各族群众户数:</td>
                                    <td><input type="text" class="form-control" name="walkPeople" id="walkPeople" value="${famdearActualReport.walkPeople!}" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
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
                                    <td><input type="text" name="mark" id="mark" maxlength="100"  value="${famdearActualReport.mark!}" class="form-control" <#if type?default("2")=="2">disabled="disabled"</#if>></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div><!-- /.page-content -->
            </div>
        </div>
        <div class="base-bg-gray text-center">
            <#if type?default("2")=="1">
                        <a class="btn btn-blue" onclick="save();" href="javascript:;">保存</a>
                </#if>
            <a class="btn btn-white" onclick="goBack();" href="javascript:;">返回</a>
        </div>
    </form>
</div>
<script>
    $("#headIndex").hide();
    $("#backA").show();

    var titleNum = ${activityTitlesSize!};
    var ticketNum=${ticketTitlesSize!};
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
        layer.photos({
            shade: .6,
            photos:'.js-layer-photos',
            shift: 5
        });
        var $wrap = $('#uploader');
        // 图片容器
        // var $queue = $( '<ul class="filelist"></ul>' )
        var $queue = $("#filelist");
        /*init webuploader*/
        var $list=$("#thelist");  //这几个初始化全局的百度文档上没说明，好蛋疼。
        var $btn =$("#ctlBtn");   //开始上传
        var thumbnailWidth = 100;   //缩略图高度和宽度 （单位是像素），当宽高度是0~1的时候，是按照百分比计算，具体可以看api文档
        var thumbnailHeight = 100;
        var id = $("#reportId").val();

         <#if famdearActualReport.activityTitles?exists&& (famdearActualReport.activityTitles?size > 0)>
             <#list famdearActualReport.activityTitles as item>
                var uploader${item_index+1} = WebUploader.create({
                    // 选完文件后，是否自动上传。
                    auto: true,

                    // swf文件路径
                    swf: '${request.contextPath}/static/webuploader/Uploader.swf',
                    // 文件接收服务端。
                    server: '${request.contextPath}/familydear/famdearActualReport/saveAttachment',

                    formData: {
                        'objId':id,
                        'objType':'activity_${item_index+1}',
                    },

                    // 选择文件的按钮。可选。
                    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                    pick: '#activity_${item_index+1}',
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
                 // 文件上传成功，给item添加成功class, 用样式标记上传成功。
                uploader${item_index+1}.on( 'uploadSuccess', function( file ) {
                    refreshPic(index);
                    $( '#'+file.id ).addClass('upload-state-done');
                });
             </#list>
         </#if>
        <#if famdearActualReport.ticketTitles?exists&& (famdearActualReport.ticketTitles?size > 0)>
            <#list famdearActualReport.ticketTitles as item>
                var uploaderTicket${item_index+1} = WebUploader.create({
                    // 选完文件后，是否自动上传。
                    auto: true,

                    // swf文件路径
                    swf: '${request.contextPath}/static/webuploader/Uploader.swf',
                    // 文件接收服务端。
                    server: '${request.contextPath}/familydear/famdearActualReport/saveAttachment',

                    formData: {
                        'objId':id,
                        'objType':'ticket_${item_index+1}',
                    },

                    // 选择文件的按钮。可选。
                    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                    pick: '#ticket_${item_index+1}',
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
                 // 文件上传成功，给item添加成功class, 用样式标记上传成功。
                uploaderTicket${item_index+1}.on( 'uploadSuccess', function( file ) {
                    refreshPic(index);
                    $( '#'+file.id ).addClass('upload-state-done');
                });
            </#list>
        </#if>
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
    });

    // 负责view的销毁

    function refreshPic(param){
        var attr = param.split("_");
        var id = $("#reportId").val();
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
        var id = $("#reportId").val();
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
                'objId':id,
                'objType':objType,
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#activity_'+titleNum,
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
        var id = $("#reportId").val();
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
                'objId':id,
                'objType':objType,
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#ticket_'+ticketNum,
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
        // $('#myDiv').scrollTop(scrollHeight,100);
    }

    function save() {
        var activityId = $("#activityId1").val();
        if(!activityId){
            layerTipMsg(false,"提示!","访亲轮次不能为空!");
            return;
        }
        var arrangeId = $("#arrangeId").val();
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
        if(!activityTitle){
            layerTipMsg(false,"提示!","访亲标题不能为空!");
            return;
        }
        if(!ticketTitle){
            layerTipMsg(false,"提示!","车票说明不能为空!");
            return;
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
                $("#arrangeId").empty();
                if(data.length>0){
                    // htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    <#--if("${famdearActualReport.arrangeId!}"==data[i].id){-->
                        <#--htmlStr = htmlStr+"<option value='"+data[i].id+"' selected>"+data[i].name+"</option>";-->
                    <#--}else{-->
                        <#--htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";-->
                    <#--}-->

                    for(var i=0;i<data.length;i++){
                        htmlStr = htmlStr+"<option value='"+data[i].id+"'>"+data[i].name+"</option>";
                    }
                }
                else {
                    htmlStr = htmlStr+"<option value=''>暂无需要填报的批次</option>";
                }
                $("#arrangeId").html(htmlStr);
                // showList1();
            }

        })
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
</script>