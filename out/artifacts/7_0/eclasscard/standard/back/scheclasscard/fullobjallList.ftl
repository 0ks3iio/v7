<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="col-md-3 no-padding-left">
		<div class="tree labels">
			<p class="tree-name no-margin">
				<b>信息列表</b>
				<a href="javascript:void(0);" class="btn btn-blue btn-30" onclick="fullObjAllEdit('')">发布</a>
			</p>
			<#if eccFullObjAlls?exists&&eccFullObjAlls?size gt 0>
			<ul class="brand-message">
		        <#list eccFullObjAlls as item>
				<li onclick="fullObjAllShow('${item.id!}')" <#if showId == item.id>class="bg-choose"</#if>>
					<span>${item.objectName!}<#if item.lockScreen><i class="fa fa-lock" aria-hidden="true"></i></#if></span>
					<span class="brand-time">${item.beginTime!}&nbsp;-&nbsp;${item.endTime!}</span>
					<#if item.status==1 && item_index == 0>
					<span class="show-now">展示中</span>
					</#if>
					<#if item.type=='03'><img src="${request.contextPath}/static/images/growth-manual/jpg-icon.png" alt="" />
					<#elseif item.type=='04'><img src="${request.contextPath}/static/images/growth-manual/mov-icon.png" alt="" />
					<#elseif item.type=='05'><img src="${request.contextPath}/static/images/growth-manual/ppt-icon.png" alt="" />
					<#else><img src="${request.contextPath}/static/images/growth-manual/doc-icon.png" alt="" /></#if>
					
				</li>
				</#list>
			</ul>
			<#else>
				<li  align="center">
					暂无数据
				</li>
			</#if>
		</div> 
	</div>
	<div class="col-md-9 no-padding-right">
		<div id="showMediaRight" class="tree labels">
			<p class="tree-name no-margin">
				<b>展示内容</b>
			</p>
			<div class="no-data-container" style="padding-top:150px">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>暂无内容</h3>
						<p class="no-data-txt">没有要展示的全屏信息</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$(function(){
	$('.tree').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 80
		})
	});
	$('.brand-message li').click(function(){
		$(this).addClass('bg-choose').siblings().removeClass('bg-choose');
	});
	fullObjAllShow("${showId!}");
});
function fullObjAllEdit(id){
	var url =  '${request.contextPath}/eclasscard/standard/fullscreensch/edit?id='+id;
	$("#schEclasscardDiv").load(url);
}
function fullObjAllShow(id){
	if(id &&id!=''){
		var url =  '${request.contextPath}/eclasscard/standard/fullscreensch/show?id='+id;
		$("#showMediaRight").load(url);
	}
}

function fullObjAllDelete(id){
	layer.confirm("是否确认删除全屏展示该媒体内容", function(index){
		$.ajax({
			url:'${request.contextPath}/eclasscard/standard/fullscreensch/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			showContent('4');
		 			layer.msg("删除成功");
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		layer.close(index);
	});
}

</script>