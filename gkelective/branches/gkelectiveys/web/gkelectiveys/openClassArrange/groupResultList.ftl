<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="box-body">
<@htmlcomponent.printToolBar container=".print"/>
<div class="tab-content print" id="resultListDiv">
	<#if (rounds.step < 4)>
		<#assign nowStep=true>
	<#else>
		<#assign nowStep=false>
	</#if>
	<table class="table table-striped table-hover no-margin">
		<thead>
			<tr>
				<th>组合班级</th>
				<th>总人数</th>
				<th>男生</th>
				<th>女生</th>
				<th class="noprint">查看学生</th>
				<#if nowStep>
				<th class="noprint">操作</th>
				</#if>
			</tr>
		</thead>
		<tbody>
			<#if groupClassList?? && (groupClassList?size>0)>
				<#list groupClassList as item>
				<tr>
					<td>${item.groupName!}</td>
					<td>${item.number!}</td>
					<td>${item.manNumber!}</td>
					<td>${item.womanNumber!}</td>
					<td class="noprint">
					<#if (item.number?default(0)>0)>
					<a href="#" onclick="showGroupStu('${item.id!}')">查看</a>
					</#if>
					</td>
					<#if nowStep>
					<td class="noprint"><a href="javascript:" class="color-red" onclick="doDelGroup('${item.id!}','${item.groupName!}')">解散</a></td>
					</#if>
				</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	<div class="page-footer-btns text-right noprint">
		<a class="btn btn-white preStep-btn" href="#">上一步</a>
		<a class="btn btn-blue nextStep-btn" href="#">下一步</a>
	</div>
</div>
</div>
<script>
$(function(){
	$(".preStep-btn").on("click",function(){
		toPerArrange();
	});
	$(".nextStep-btn").on("click",function(){
		toUnArrange();
	});
});
function showGroupStu(groupId){
	var url = "${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/detail/page?groupId="+groupId;
	$("#resultListDiv").load(url);
}
function doDelGroup(groupId,groupName){
	var msg = "确定要解散<span style='color:red'>"+groupName+"</span>班级吗？";
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm(msg,options,function(){
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/delete',
			data:{'groupClassId':groupId},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
				  	reloadListData();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});	
	},function(){});
}
function reloadListData(){
	toGroupResult();
}
</script>
