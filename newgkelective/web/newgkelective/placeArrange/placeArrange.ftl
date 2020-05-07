<#--
	<div class="page-toolbar">
		<a href="javascript:goBack();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
		<div class="page-toolbar-btns">
			<span class="tip tip-grey">共有 ${countArrang!} 条选课结果</span>
			<a class="btn btn-blue" href="javascript:toPlaceArrangEdit();">新增教室安排方案</a>
		</div>
	</div>
-->
<div class="filter">	
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:toPlaceArrangEdit();">新增教室安排方案</a>
	</div>
	<div class="filter-item filter-item-right">
		<span class="color-999">共有 ${countArrang!} 条结果数据</span>
	</div>
</div>
<#if newGkArrayItemList?exists && newGkArrayItemList?size gt 0>
   <#list newGkArrayItemList as item>
<div class="box box-default">

       <div class="box-header">
		<h3 class="box-title">${item.itemName!}</h3>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						更多
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a href="javascript:deleteArrayItem('${item.id!}');" class="js-del">删除</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="box-body">
		<p class="explain">
		           创建时间：${item.creationTime?string('yyyy-MM-dd HH:mm')!}
		    <#if item.ts>
		        <font class="pull-right" style="color:red">当前教室方案还未给班级和科目安排具体教室，<a href="javascript:toPlaceArrayList('${item.id!}');">去安排</a></font>
		    </#if>
		</p>
		<div class="number-container">
			<a href="javascript:toPlaceArrayList('${item.id!}');">
				<ul class="number-list">
					<li><em>${item.countPlace!}</em><span>教学楼教室</span></li>
					<#if item.newGkItemDto?exists && item.newGkItemDto.num?exists>
					<#assign t = item.newGkItemDto.num?size>								
					<#list 0..t as c>
					    <li><em>${item.newGkItemDto.num[c]!}</em><span>${item.newGkItemDto.typeName[c]!}</span></li>
					</#list>	
					</#if>						
				</ul>
			</a>
		</div>
	</div>
</div>
   </#list>
</#if>
<script>
showBreadBack(goBack,false,"教室安排列表");
function toPlaceArrangEdit(){
   var divideId = '${divideId!}';
   var url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/eidt?arrayId=${arrayId!}';
   $("#showList").load(url);
}

function toPlaceArrayList(arrayItemId){
   var divideId = '${divideId!}';
   url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/list?arrayItemId='+arrayItemId+'&arrayId=${arrayId!}';
   $("#showList").load(url);
}

function deleteArrayItem(id){
  var divideId = '${divideId!}';
  var index = layer.confirm("确定删除吗？", {
			btn: ["确定", "取消"]
		}, function(){
		    $.ajax({
		        url:'${request.contextPath}/newgkelective/'+divideId+'/placeArrange/placeArrangeDelete',
		        data:{arrayItemId:id},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"删除失败",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
				      layer.msg(data.msg, {offset: 't',time: 2000});
                      url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/index/page?arrayId=${arrayId!}';
                      $("#showList").load(url);
    		       }
		        },
		        error:function(XMLHttpRequest, textStatus, errorThrown){alert(errorThrown);} 
	        });
	    })
}

function goBack(){
   
   <#if arrayId?default('')==''>
   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}';
   $("#showList").load(url);
   <#else>
   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
   $("#showList").load(url);
   </#if>
}
</script>