<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if famDearThreeInTwos?exists&& (famDearThreeInTwos?size > 0)>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="20%">标题</th>
			<th width="65%">内容</th>
            <th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
	    <#list famDearThreeInTwos as item>
		<tr>
			<td >${item.title}</td>
			<td >${item.content!}</td>
			<#if hasPermission><td><a class="table-btn color-blue" style="cursor: pointer"  onclick="edit('${item.id}',1)">修改</a><a id="deal" class="table-btn color-blue" style="cursor: pointer"  onclick="deal('${item.id}','${item.state}')"><#if item.state?default("0")=="0"> 发布 <#else > 取消发布 </#if></a><a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')"> 删除</a> </td>
            <#else >
                <td><a class="table-btn color-blue" style="cursor: pointer"   onclick="edit('${item.id}',2)"> 查看 </a></td>
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
<script type="text/javascript">

function deal(id,state) {
    if(state==0) {
        showConfirmMsg('确认发布？', '提示', function () {
            var ii = layer.load();
            $.ajax({
                url : '${request.contextPath}/familydear/threeInTwo/updateThreeInTwoState',
                data: {'id': id,'state':state},
                type: 'post',
                success: function (data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if (jsonO.success) {
                        showList();
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
                url : '${request.contextPath}/familydear/threeInTwo/updateThreeInTwoState',
                data: {'id': id,'state':state},
                type: 'post',
                success: function (data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if (jsonO.success) {
                        showList();
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
    if(type==1) {
        var url = "${request.contextPath}/familydear/threeInTwo/edu/edit/page?id="+id;
        $(".model-div").load(url);
    }else {
    	var url = "${request.contextPath}/familydear/threeInTwo/viewThreeInTwo?id="+id;
        $(".model-div").load(url);
    }
}
function del(id) {
    showConfirmMsg('确认删除？','提示',function(){
        var ii = layer.load();
        $.ajax({
            url: '${request.contextPath}/familydear/threeInTwo/delThreeInTwo',
            data: {'id':id},
            type:'post',
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    showList();
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