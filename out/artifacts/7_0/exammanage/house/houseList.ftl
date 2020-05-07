<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th style="width:10%">学生姓名</th>
			<th style="width:15%">身份证号</th>
			<th style="width:15%">原学校</th>
			<th style="width:15%">原学校上级教育局单位</th>
			<th style="width:15%">转入户口所在教育局单位</th>
			<th class="text-center">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if exammanageHouseRegisters?exists && (exammanageHouseRegisters?size > 0)>
			<#list exammanageHouseRegisters as dto>
				<tr>
					<td class="text-center">${dto.stuName!}</td>
					<td class="text-center">${dto.card!}</td>
					<td class="text-center">${dto.oldSchoolName!}</td>
					<td class="text-center">${dto.oldUnitName!}</td>
					<td class="text-center">${dto.newUnitName!}</td>
					<td class="text-center">
						<a class="color-red delete pos-rel" href="javascript:;" onclick="onDel('${dto.id}')">删除
					</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="6" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
<#if exammanageHouseRegisters?exists&&exammanageHouseRegisters?size gt 0>
		<@htmlcom.pageToolBar container="#stuList" class="noprint"/>
</#if>
<script>
	function onDel(id) {
        showConfirmMsg('确认删除？','提示',function(){
            var ii = layer.load();
            $.ajax({
                url: '${request.contextPath}/exammanage/edu/house/delete?',
                data: {'id':id},
                type:'post',
                success:function(data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        showStuList();
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