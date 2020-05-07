<div id="innerShowList">
<div class="filter">	
	<div class="filter-item">
		<a href="javascript:"  class="btn btn-blue" onClick="doImport2();">导入成绩</a>
	</div>
	<div class="filter-item filter-item-right">
		<span class="color-999">共有 ${count!}条结果数据</span>
	</div>
</div>
<#if newGkReferScoreList?exists && newGkReferScoreList?size gt 0>

    <#list newGkReferScoreList as item>
	<div class="box box-default box-border" >
		<#if item.isDefault?default(0)==1>
				<span class="default-icon" id="score_default_${item.id}"></span>
		<#else>
				<span id="score_default_${item.id}"></span>
		</#if>
	<div class="box-header">
		<h3 class="box-caption">${item.name!}</h3>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
			    <#if item.isDefault?default(0)==1>
	               <button type="button" class="btn btn-sm btn-white js-setReference" onClick="saveRef('${item.id!}','0');">取消</button>
		        <#else>
		           <button type="button" class="btn btn-sm btn-white js-setReference" onClick="saveRef('${item.id!}','1');">设为参考分</button>
		        </#if>
                <button type="button" class="btn btn-sm btn-white js-setReference" onClick="doDelete('${item.id!}');">删除</button>
				<#--<div class="btn-group" role="group">
					<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						更多
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a href="javascript:doDelete('${item.id!}');" class="js-del">删除</a></li>
					</ul>
				</div>-->
			</div>
		</div>
	</div>
	<div class="box-body" onClick="showScore('${item.id!}')">
		<table class="table table-bordered text-center diy-table">
			<thead>
				<tr>
					<#list item.dataList as item2>
		               <th class="text-center">${item2[0]!}</th>
					</#list>
				</tr>
			</thead>
			<tbody>	
			    <tr>
					<#list item.dataList as item2>
						<td>${item2[1]!}</td>
					</#list>
				</tr>
			</tbody>
		</table>
		<div class="clearfix color-999">
			<div class="pull-left">科目分数为平均分</div>
			<div class="pull-right">创建时间：${item.creationTime?string('yyyy-MM-dd HH:mm')!}</div>
		</div>
	</div>
	</div>
	</#list>

	<#else>
	<div class="no-data-container">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
				</span>
				<div class="no-data-body">
					<h3>暂无成绩</h3>
					<p class="no-data-txt">请点击左上角的“导入成绩”按钮</p>
				</div>
			</div>
	</div>
</#if>
</div>

<script>
function doImport2(){
	var url = "${request.contextPath}/newgkelective/newGkScoreResult/import/main?unitId=${unitId!}&gradeId=${gradeId!}&showReturn=true";
	$("#innerShowList").load(url);
}

function doDelete(refId){
    var index = layer.confirm("确定删除吗？", {
			btn: ["确定", "取消"]
		}, function(){
		    $.ajax({
		        url:"${request.contextPath}/newgkelective/newGkScoreResult/delete",
		        data:{refId:refId},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"删除失败",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
				      layer.msg(data.msg, {offset: 't',time: 2000});
				      scoreTable();
				     // showList("1");
    		       }
		        },
		        error:function(XMLHttpRequest, textStatus, errorThrown){alert(errorThrown);} 
	        });
	    })
}

function saveRef(refId,state){
    var unitId = '${unitId!}';
    $.ajax({
		url:"${request.contextPath}/newgkelective/newGkScoreResult/saveRef",
		data:{unitId:unitId,refId:refId,state:state},
		dataType : 'json',
		success : function(data){
		   if(!data.success){
		 	  //layerTipMsg(data.success,"删除失败",data.msg);
		 	  //return;
		   }else{
		 	  //layer.closeAll();
			  //layerTipMsg(data.success,"删除成功",data.msg);
			  scoreTable();
			  //showList("1");
    	   }
	    },
		error:function(XMLHttpRequest, textStatus, errorThrown){alert(errorThrown);} 
	});
}

function showScore(refId){
   var unitId = '${unitId!}';
   var url = "${request.contextPath}/newgkelective/newGkScoreResult/showScoreHead?refId="+refId+"&unitId="+unitId+"&gradeId=${gradeId!}";
   $("#innerShowList").load(url);
}

</script>