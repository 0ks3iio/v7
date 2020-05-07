<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body clearfix">
		<div class="filter">
			<div class="filter-item">
				<a class="btn btn-blue js-add" href="javascript:void(0);" onClick="siteEdit(this);">新增考点</a>
			</div>
		</div>
        <div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>序号</th>
							<th>考点名称</th>
							<th>考场数量</th>
							<th>考场可容纳人数</th>
							<th>共可容纳数</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<#if siteList?exists && siteList?size gt 0>
					    <#list siteList as item>
						<tr>
						    <input type="hidden" class="tid" value="${item.id!}">
							<td>${item_index+1!}</td>
							<td>${item.siteName!}</td>
							<td>${item.siteNum!}</td>
							<td>${item.capacity!}</td>
							<td>${item.siteNum*item.capacity!}</td>
							<td><a class="color-blue mr10 js-add" href="#" onClick="siteEdit(this)">编辑</a><a class="color-red" href="javascript:void(0);" onClick="deleteSite('${item.id!}');">删除</a></td>
						</tr>
						</#list>
					<#else>
			            <tr>
				           <td colspan="6" align="center">暂无数据</td>
			            </tr>
					</#if>
					</tbody>
				</table>
				<@htmlcom.pageToolBar container=".model-div-show" class="noprint">
	            </@htmlcom.pageToolBar>
			</div>
		</div>
		<div class="layer layer-add">			
		</div>
	</div>
</div>
<script>
function siteEdit(obj){
    var that = $(obj);
	var id = that.closest('tr').find('.tid').val();
	if(id != undefined){
	    var url =  "${request.contextPath}/teaexam/site/edit?id="+id;
	}else{
	    var url =  "${request.contextPath}/teaexam/site/edit";
	}
	$(".layer-add").load(url,function() {
		layerShow(id);
	});
}

function layerShow(id){
     var title = "";
     if(id != undefined){
	    title = "编辑";
	 }else{
	    title = "新增";
	 }
     layer.open({
	    type: 1,
	    shade: 0.5,
	    title: title,
	    area: '510px',
	    btn: ['确定','取消'],
	    yes: function(index){
			saveSite();
		},
	    content: $('.layer-add')
	 });
}

var isSubmit=false;
function saveSite(){
   if(isSubmit){
		return;
	}
	var check = checkValue('#subForm');
	if(!check){
		isSubmit = false;
		return;
	}
	var options = {
		url : '${request.contextPath}/teaexam/site/save',
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				searchList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}

function searchList(){
    var url = "${request.contextPath}/teaexam/site/index/page";
    $('.model-div-show').load(url);
}

function deleteSite(id){
    var index = layer.confirm("确定删除吗？", {
			btn: ["确定", "取消"]
		}, function(){
		    $.ajax({
		        url:"${request.contextPath}/teaexam/site/delete",
		        data:{id:id},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"删除失败",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
				      layerTipMsg(data.success,"删除成功","");
                       searchList();
    		       }
		       },
	        });
	  })
	
}
</script>