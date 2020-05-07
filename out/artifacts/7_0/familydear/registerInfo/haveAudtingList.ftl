<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if familyDearAuditDtos?exists && familyDearAuditDtos?size gt 0>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>

            <#--干部部门，结亲村，干部姓名，干部联系电话，结亲对象，结亲对象联系电话，结亲对象地址，报名批次，报名时间-->
				<tr>
                    <th width="9%">干部部门</th>
                    <th width="9%">结亲村</th>
				    <th width="9%">干部姓名</th>
					<th width="9%">干部联系电话</th>
                    <th width="9%">结亲对象</th>
                    <th width="9%">结亲对象联系电话</th>
					<#--<th>性别</th>-->
					<th width="9%">结亲对象村</th>
					<th width="9%">报名批次</th>
                    <th width="9%">报名时间</th>
					<th width="9%">审核状态</th>
					<#--<th>审核人</th>-->
					<#--<th>审核时间</th>-->
					<#if hasPermission>
						<th width="10%">操作</th>
					</#if>
				</tr>
			</thead>
			<tbody>
			<#if familyDearAuditDtos?exists && familyDearAuditDtos?size gt 0>
				    <#list familyDearAuditDtos as item>
						<#if item.familyDearObjectList?exists&& (item.familyDearObjectList?size > 0)>
							<tr>
								<td rowspan=${item.familyDearObjectList?size}>${item.deptName!}</td>
								<td title=${item.contrys!} rowspan=${item.familyDearObjectList?size}>${item.contrysSub!}</td>
								<td rowspan=${item.familyDearObjectList?size}>${item.teacherName!}</td>
								<td rowspan=${item.familyDearObjectList?size}>${item.teacherPhone!}</td>
								<#list item.familyDearObjectList as item1 >
									<td >${item1.name!}</td>
									<td >${item1.mobilePhone!}</td>
									<#--<td>${item.sex!}</td>-->
									<td >${item1.village!}</td>
									<#if item1_index ==0>
										<td rowspan=${item.familyDearObjectList?size}>${item.batchType!}</td>
										<td rowspan=${item.familyDearObjectList?size}>${item.applyTime?string('yyyy-MM-dd')!}</td>
									<td rowspan=${item.familyDearObjectList?size}><#if item.state == 2>
                                        <span><i class="fa fa-circle color-green font-12"></i> 通过</span>
									<#elseif item.state == -1>
				        				<span><i class="fa fa-circle color-red font-12"></i> 未通过</span>
										<a class="color-blue" data-toggle="tooltip" data-placement="right" title="${item.remark!}" href="javascript:;"><i class="fa fa-commenting-o"></i></a>
									</#if>
                                    </td>
										<#if hasPermission>
										<td rowspan=${item.familyDearObjectList?size}><a class="table-btn color-blue" style="cursor: pointer"   onclick="cancelAudit('${item.id}')">  取消审核 </a></td>
										</#if>
									</#if>

								</tr>
								</#list>
						<#else >
								<tr>
									<td >${item.deptName!}</td>
									<td title=${item.contrys!}>${item.contrysSub!}</td>
									<td >${item.teacherName!}</td>
									<td >${item.teacherPhone!}</td>
									<td ></td>
									<td ></td>
							<#--<td>${item.sex!}</td>-->
									<td ></td>
									<td >${item.batchType!}</td>
									<td >${item.applyTime?string('yyyy-MM-dd')!}</td>
                                    <td><#if item.state == 2>
                                        <span><i class="fa fa-circle color-green font-12"></i> 通过</span>
									<#elseif item.state == -1>
				        				<span><i class="fa fa-circle color-red font-12"></i> 未通过</span>
										<a class="color-blue" data-toggle="tooltip" data-placement="right" title="${item.remark!}" href="javascript:;"><i class="fa fa-commenting-o"></i></a>
									</#if>
									</td>
									<#if hasPermission>
										<td ><a class="table-btn color-blue" style="cursor: pointer"   onclick="cancelAudit('${item.id}')">  取消审核 </a></td>
									</#if>
								</tr>
						</#if>
				</#list>
			</#if>
			</tbody>
		</table>
		<@htmlcom.pageToolBar container="#haveAudtionDiv" class="noprint">
	    </@htmlcom.pageToolBar>
	</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<!-- 审核不通过 -->
<div class="layer layer-nopass">
	<div class="layer-content">
		<textarea rows="5" class="form-control" id="remark" placeholder="请输入不通过原因" maxLength="100"></textarea>
	</div>
</div>
<script>
function searchAudit(){
   var examId = $('#examId').val();
   var schId = $('#schId').val();
   var subId = $('#subId').val();
   var status = $('#status').val();
   var searchCon = $('#searchCon').val();
   var type = $('#type').val();
   if(type == '1'){
       var url = "${request.contextPath}/teaexam/registerAudit/haveAudtingList?infoType=${infoType?default(0)}&schId="+schId+"&subId="+subId+"&status="+status+"&examId="+examId+"&teacherName="+searchCon+"&type="+type;
       $('#haveAudtionDiv').load(url);
   }else{
       var url = "${request.contextPath}/teaexam/registerAudit/haveAudtingList?infoType=${infoType?default(0)}&schId="+schId+"&subId="+subId+"&status="+status+"&examId="+examId+"&identityCard="+searchCon+"&type="+type;
       $('#haveAudtionDiv').load(url);
   }
}

function cancelAudit(id) {
    showConfirmMsg('已维护信息填报或每月活动填报的报名不能取消审核,确认取消审核？','提示',function(){
        var ii = layer.load();
        $.ajax({
            url: '${request.contextPath}/familydear/registerAudit/auditingCancel?',
            data: {'id':id},
            type:'post',
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    searchList();
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