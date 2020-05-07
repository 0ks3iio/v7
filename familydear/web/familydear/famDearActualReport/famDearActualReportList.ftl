<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if FamdearActualReportList?exists&& (FamdearActualReportList?size > 0)>
<table class="table table-bordered table-striped table-hover">


    <#--访亲轮次名称、结亲村、活动时间、活动图片数、状态、操作（修改、提交、删除）-->
    <thead>
    <tr>
        <th width="10%">访亲轮次名称</th>
        <th width="20%">结亲村</th>
        <th width="20%">活动时间</th>
        <th width="10%">活动图片数</th>
        <th width="15%">状态</th>
        <th width="25%">操作</th>
    </tr>
    </thead>
    <tbody>
	    <#list FamdearActualReportList as item>
        <tr>
            <td >${item.activityName!}</td>
            <td >${item.villageName!}</td>
            <td >${item.activityTimeStr!}</td>
            <td>${item.imageNum!}</td>
            <td>${item.stateStr!}</td>
            <#if hasPermission>

                 <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',1)"> 修改 </a><a id="deal" class="table-btn color-blue" style="cursor: pointer"  onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0">  提交  <#else > 撤销提交 </#if></a><a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')">  删除</a> </td>
             <#else >
                 <#if item.createUserId==createUserId>
                         <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',1)"> 修改 </a><a id="deal" class="table-btn color-blue" style="cursor: pointer"  onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0">  提交  <#else > 撤销提交 </#if></a><a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')">  删除</a> </td>
                 <#else >
                        <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',2)"> 查看 </a></td>
                 </#if>
             </#if>
        </tr>
        </#list>
    </tbody>
</table>
    <@htmlcom.pageToolBar container="#showList1" class="noprint"/>
<#else >
<div class="no-data-container">
    <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
        <div class="no-data-body">
            <p class="no-data-txt">暂无相关数据</p>
        </div>
    </div>
</div>
</#if>
<#--<@htmlcom.pageToolBar container="#tabDiv" class="noprint">-->
<#--</@htmlcom.pageToolBar>-->
<script type="text/javascript">
    <#--<#if viewType=="1">-->
    <#--$(function(){-->
    <#--var table = $('#showtable1').DataTable( {-->
    <#--scrollY: "148px",-->
    <#--info: false,-->
    <#--searching: false,-->
    <#--autoWidth: false,-->
    <#--sort: false,-->
    <#--scrollCollapse: true,-->
    <#--paging: false,-->
    <#--language:{-->
    <#--emptyTable: '没有数据',-->
    <#--loadingRecords: '加载中...',-->
    <#--}-->
    <#--} );-->
    <#--});-->
    <#--</#if>-->
    <#--function doSetExamItem(examId,type){-->
    <#--var url =  '${request.contextPath}/exammanage/examTeacher/examItemIndex/page?examId='+examId+"&type="+type;-->
    <#--$("#examTeacherDiv").load(url);-->
    <#--}-->
    function edit(id,type) {
        var year = $("#searchYear1").val();
        var activityId = $("#activityId").val();
        var village = $("#village").val();
        var url = "${request.contextPath}/familydear/famdearActualReport/editReport?id="+id+"&type="+type+"&year="+year+"&activityId="+activityId+"&village="+village;
        indexDiv = $("#myTabDiv").load(url);
        <#--var url = "${request.contextPath}/familydear/famdearActualReport/editReport?id="+id+"&type="+type;-->
        <#--if(type==1) {-->
            <#--indexDiv = layerDivUrl(url, {title: "修改结亲计划", width: 750, height: 500});-->
        <#--}else {-->
            <#--indexDiv = layerDivUrl(url, {title: "查看结亲计划", width: 750, height: 500});-->
        <#--}-->
    }
    function del(id) {
        showConfirmMsg('确认删除？','提示',function(){
            var ii = layer.load();
            $.ajax({
                url: '${request.contextPath}/familydear/famdearActualReport/delReport?',
                data: {'id':id},
                type:'post',
                success:function(data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        showList1();
                    }else{
                        layerTipMsg(jsonO.success,"失败",jsonO.msg);
                    }
                    layer.close(ii);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });

    }

    function deal(id,state) {
        if(state==0) {
            showConfirmMsg('确认提交？', '提示', function () {
                var ii = layer.load();
                $.ajax({
                    url : '${request.contextPath}/familydear/famdearActualReport/updateReportState?',
                    data: {'id': id,'state':state},
                    type: 'post',
                    success: function (data) {
                        layer.closeAll();
                        var jsonO = JSON.parse(data);
                        if (jsonO.success) {
                            showList1();
                        } else {
                            layerTipMsg(jsonO.success, "提交失败", jsonO.msg);
                        }
                        layer.close(ii);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            });
        }else {
            showConfirmMsg('确认取消提交？', '提示', function () {
                var ii = layer.load();
                $.ajax({
                    url : '${request.contextPath}/familydear/famdearActualReport/updateReportState?',
                    data: {'id': id,'state':state},
                    type: 'post',
                    success: function (data) {
                        layer.closeAll();
                        var jsonO = JSON.parse(data);
                        if (jsonO.success) {
                            showList1();
                        } else {
                            layerTipMsg(jsonO.success, "取消提交失败", jsonO.msg);
                        }
                        layer.close(ii);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            });
        }

    }
</script>