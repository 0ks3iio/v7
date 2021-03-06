<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if famdearMonthList?exists&& (famdearMonthList?size > 0)>
<table class="table table-bordered table-striped table-hover">


<#--访亲轮次名称、结亲村、活动时间、活动图片数、状态、操作（修改、提交、删除）-->
    <thead>
    <tr>
        <th width="15%">活动类型</th>
        <th width="20%">时间</th>
        <th width="20%">地点</th>
        <th width="10%">参加人数</th>
        <th width="15%">活动说明</th>
        <th width="20%">操作</th>
    </tr>
    </thead>
    <tbody>
	    <#list famdearMonthList as item>
        <tr>
            <td >${item.typeStr!}</td>
            <td >${item.activityTimeStr!}</td>
            <td >${item.place!}</td>
            <td>${item.partnum!}</td>
            <td>${item.activityContent!}</td>
            <#if hasPermission>
             <#--<#if item.createUserId==createUserId>-->
                 <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',1)"> 修改 </a><a id="deal" class="table-btn color-blue" style="cursor: pointer"  onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0">  提交  <#else > 撤销提交 </#if></a><a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')">  删除</a> </td>
             <#elseif item.createUserId==createUserId&&item.type=="1">
                <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',1)"> 修改 </a><a id="deal" class="table-btn color-blue" style="cursor: pointer"  onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0">  提交  <#else > 撤销提交 </#if></a><a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')">  删除</a> </td>
             <#else >
                    <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',2)"> 查看 </a></td>
             </#if>
        </tr>
        </#list>
    </tbody>
</table>
    <@htmlcom.pageToolBar container="#showList2" class="noprint"/>
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
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/famdearMonth/famdearMonthEdit?id="+id+"&type="+type+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        if(type==1){
            $("#layerContent").load(url,
                    function() {
                        var index = layer.open({
                            type: 1,
                            skin: 'layui-layer-demo',
                            closeBtn: 1,
                            shift: 2,
                            shadeClose: false,
                            maxmin: false,
                            scrollbar: false,
                            title: "每月活动编辑",
                            area: ['750px','500px'],
                            content: $("#layerContent"),
                            cancel:function () {
                                goBack();
                            }
                        });
                    }
            );
        }else {
            indexDiv = layerDivUrl(url,{title: "查看月活动填报",width:750,height:500});
        }
        // indexDiv = $("#myTabDiv").load(url);
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
                url: '${request.contextPath}/familydear/famdearMonth/delMonth?',
                data: {'id':id},
                type:'post',
                success:function(data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        showList2();
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
                    url : '${request.contextPath}/familydear/famdearMonth/updateMonthState?',
                    data: {'id': id,'state':state},
                    type: 'post',
                    success: function (data) {
                        layer.closeAll();
                        var jsonO = JSON.parse(data);
                        if (jsonO.success) {
                            showList2();
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
                    url : '${request.contextPath}/familydear/famdearMonth/updateMonthState?',
                    data: {'id': id,'state':state},
                    type: 'post',
                    success: function (data) {
                        layer.closeAll();
                        var jsonO = JSON.parse(data);
                        if (jsonO.success) {
                            showList2();
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