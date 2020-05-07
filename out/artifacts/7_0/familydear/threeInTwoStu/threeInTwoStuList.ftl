<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if famDearThreeInTwoStus?exists&& (famDearThreeInTwoStus?size > 0)>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="10%">部门</th>
			<th width="17%">干部姓名</th>
			<th width="18%">学生姓名</th>
			<th width="5%">性别</th>
			<th width="10%">民族</th>
			<th width="10%">所在学校</th>
			<th width="10%">学生联系电话</th>
            <th width="15%">操作</th>
		</tr>
	</thead>
	<tbody>
	    <#list famDearThreeInTwoStus as item>
		<tr>
			<td >${item.deptName!}</td>
			<td >${item.teacherName!}</td>
			<td >${item.stuname!}</td>
			<td >${item.sex!}</td>
			<td >${item.nation!}</td>
			<td >${item.school!}</td>
			<td >${item.linkPhone!}</td>
			<td><a class="table-btn color-blue" style="cursor: pointer"  onclick="edit('${item.id}',1)">修改</a>
				<#if hasPermission>
					<a href="javascript:addCadre('${item.id!}');" class="table-btn color-red">设置干部</a>
				</#if>
				<a class="table-btn color-red" style="cursor: pointer"  onclick="del('${item.id}')"> 删除</a>
            </td>
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
function addCadre(id){
        var url = "${request.contextPath}/familydear/threeInTwoStu/cadreAddLink?objId="+id+"&currentPageIndex="+'${currentPageIndex!}'+"&currentPageSize="+'${currentPageSize!}';
        indexDiv = layerDivUrl(url,{title: "设置干部",width:750,height:630});
    }

function edit(id,type) {
    if(type==1) {
    	var stuName = $("#stuName").val();
        var ganbName = $("#ganbName").val();
        var stuPhone = $("#stuPhone").val();
        var url = "${request.contextPath}/familydear/threeInTwoStu/edu/edit/page?id="+id+"&currentPageIndex="+'${currentPageIndex!}'+"&currentPageSize="+'${currentPageSize!}'+"&stuName="+stuName+"&ganbName="+ganbName+"&stuPhone="+stuPhone;
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
            url: '${request.contextPath}/familydear/threeInTwoStu/edu/delete/page',
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