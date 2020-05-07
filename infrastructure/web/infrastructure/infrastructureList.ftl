<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mannReForm">

	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
        			<#if isAdmin?default(false) >

					<th ><label class="pos-rel">
                        <input type="checkbox" class="wp" id="selectAll">
                        <span class="lbl"></span>
                    </label>选择</th>
        			</#if>

					<th width="15%">项目学校</th>
					<th width="10%" >项目名称</th>
					<th width="10%">项目性质</th>
					<th width="15%">施工企业</th>
                    <th width="15%" >监理单位</th>
                    <th width="5%" >控制价</th>
                    <th width="5%" >合同价</th>
                    <th width="10%">建设时间</th>
                    <th width="8%" >操作</th>
				</tr>
		</thead>
		<tbody id="list" >
			<#if infrastructureProjectList?exists && (infrastructureProjectList?size > 0)>
				<#list infrastructureProjectList as item>
					<tr>
						<#if isAdmin?default(false) >
							<td>
                                <label class="pos-rel js-select">
                                    <input name="stu-checkbox" type="checkbox" class="wp" value="${item.id!}"/>
                                    <span class="lbl"></span>
                                </label>
                            </td>

						</#if>

						<td>${item.projectSchool!}</td>
						<td>${item.projectName!}</td>
						<td>${mcodeSetting.getMcode("DM-XMXZ", item.projectNature?default("01"))}</td>
                        <td>${item.constructionCompany!}</td>
                        <td>${item.supervisoryUnit!}</td>
                        <td>
							${item.controlPrice!}
						</td>
                        <td>${item.contractPrice!}
						</td>
						<td>
							${(item.constructionTime?string('yyyy-MM-dd'))?default('')}</td>
						<td>
						<#if isAdmin?default(false) >
							<a href="javascript:infrastructureEdit('${item.id!}',false);" class="table-btn color-red">编辑</a>
							<a href="javascript:infrastructureDelete('${item.id!}');" class="table-btn color-red">删除</a>
							<#else>
							<a href="javascript:infrastructureEdit('${item.id!}',true);" class="table-btn color-red">查看</a>
						</#if>
			            </td>
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	    <#if infrastructureProjectList?exists && (infrastructureProjectList?size > 0)>
           <@htmlcom.pageToolBar container="#showList"/>
        </#if>

</form>		
<script>
function infrastructureEdit(id ,isDetail){
	var str = "?id="+id +"&isDetail="+isDetail;
	var url = "${request.contextPath}/infrastructure/project/edit"+str;

    $(".model-div").load(url);
}



var isSubmit=false;
function infrastructureDelete(id){
     showConfirmMsg('确认删除？','提示',function(){
     var ii = layer.load();
     var ids = new Array();
     ids.push(id);
     $.ajax({
			url:'${request.contextPath}/infrastructure/project/delete',
			data: {'ids':ids},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
				  	searchList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
		 			//$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		});
}
//全选
$('#selectAll').on('click',function(){
    var total = $('#list :checkbox').length;
    var length = $('#list :checkbox:checked').length;
    if(length != total){
        $('#list :checkbox').prop("checked", "true");
        $(this).prop("checked", "true");
    }else{
        $('#list :checkbox').removeAttr("checked");
        $(this).removeAttr("checked");
    }
});
</script>