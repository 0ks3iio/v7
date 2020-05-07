<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if activityList?exists&& (activityList?size > 0)>
<table class="table table-bordered table-striped table-hover">
    <thead>
    <tr>
        <th width="10%" >结亲活动标题</th>
        <th width="10%">批次</th>
        <th width="12%">活动时间</th>
        <th width="10%">结亲村</th>
        <th width="5%">干部数</th>
        <th width="12%">报名时间</th>
        <th width="15%">带队人</th>
        <th width="25%">操作</th>
    </tr>
    </thead>
    <tbody>
	    <#list activityList as item>
            <#if item.famDearArrangeList?exists&& (item.famDearArrangeList?size > 0)>
            <tr>
            <#--<tr>-->
            <td rowspan=${item.famDearArrangeList?size}>${item.title}</td>
                <#list item.famDearArrangeList as item1 >

                    <td>${item1.batchType!}</td>
                    <td>${item1.activityTimeStr!}</td>
                    <td title="${item1.rural}">${item1.ruralSub!}</td>
                    <td>${item1.peopleNumber!}</td>
                    <td>${item1.applyTimeStr!}</td>
                    <td>${item1.leaderUserNames!}</td>
                    <#if hasPermission>
                        <#if item1_index ==0>
                            <td rowspan=${item.famDearArrangeList?size}><#if item.state?default("0")=="0"> <a class="table-btn color-blue"  style="cursor: pointer" onclick="edit('${item.id}',1)"> 修改 </a><#else ><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',2)"> 查看 </a></#if>
                                <a id="deal" class="table-btn color-blue" style="cursor: pointer"   onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0"> 发布 <#else >  取消发布  </#if></a>
                                <#if item.state?default("0")=="1"><a class="table-btn color-blue" style="cursor: pointer"  onclick="editArrange('${item.id}')"> 设置带队人 </a></#if><a class="table-btn color-red" style="cursor: pointer"   onclick="del('${item.id}')">  删除 </a> </td>
                        </#if>
                    <#else >

                        <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',2)"> 查看 </a></td>
                    </#if>
                </tr>
                </#list>
            <#else >
            <tr>
                <td>${item.title}</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <#if hasPermission>
                        <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',1)"> 修改 </a><a id="deal" class="table-btn color-blue" style="cursor: pointer"  onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0">  发布  <#else > 取消发布 </#if></a><a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')">  删除</a> </td>
                <#else >
                    <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',2)"> 查看 </a></td>
                </#if>
            </tr>
            </#if>

            <#--<td><a style="cursor:pointer" onclick="edit('${item.id}')">修改</a><a id="deal" style="cursor:pointer" onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0"> 发布 <#else > 取消发布 </#if></a><a style="cursor:pointer" onclick="del('${item.id}')"> 删除</a> </td>-->
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

    function deal(id,state) {
        if(state==0) {
            showConfirmMsg('确认发布？', '提示', function () {
                var ii = layer.load();
                $.ajax({
                    url : '${request.contextPath}/familydear/activity/edu/updateActivityState?',
                    data: {'id': id,'state':state},
                    type: 'post',
                    success: function (data) {
                        layer.closeAll();
                        var jsonO = JSON.parse(data);
                        if (jsonO.success) {
                            showList1();
                        } else {
                            layerTipMsg(jsonO.success, "发布失败", jsonO.msg);
                        }
                        layer.close(ii);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            });
        }else {
            showConfirmMsg('确认取消发布？', '提示', function () {
                var ii = layer.load();
                $.ajax({
                    url : '${request.contextPath}/familydear/activity/edu/updateActivityState?',
                    data: {'id': id,'state':state},
                    type: 'post',
                    success: function (data) {
                        layer.closeAll();
                        var jsonO = JSON.parse(data);
                        if (jsonO.success) {
                            showList1();
                        } else {
                            layerTipMsg(jsonO.success, "取消发布失败", jsonO.msg);
                        }
                        layer.close(ii);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            });
        }

    }
    function edit(id,type) {
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/activity/edu/activityEdit?id="+id+"&type="+type+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        if(type==1) {
            indexDiv = layerDivUrl(url, {title: "修改结亲活动", width: 900, height: 500});
        }else {
            indexDiv = layerDivUrl(url, {title: "查看结亲活动", width: 900, height: 500});
        }
    }
    function editArrange(id){
        var url = "${request.contextPath}/familydear/activity/edu/arrangeEdit?id="+id;
        indexDiv = layerDivUrl(url,{title: "设置带队人",width:850,height:400});
    }
    function del(id) {
        showConfirmMsg('确认删除？','提示',function(){
            var ii = layer.load();
            $.ajax({
                url: '${request.contextPath}/familydear/activity/edu/activityDel?',
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
</script>