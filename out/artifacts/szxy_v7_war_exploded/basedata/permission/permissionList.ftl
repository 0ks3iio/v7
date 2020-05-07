<#import "/fw/macro/webmacro.ftl" as w>

<table id="simple-table" class="table table-bordered table-hover">
	<thead>
		<tr>
			<th class="center">
				<@w.cbx />
			</th>
			<th>名称</th>
			<th>类型</th>
			<th>扩展内容</th>
			<th>地址</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
	<#if permissions?size gt 0>
		<#list permissions as permission>
		<tr>
			<td class="center">
				<@w.cbx />
			</td>
			<td>${permission.name!}</td>
			<td>${permission.type!}</td>
			<td>${permission.extendId!}</td>
			<td>${permission.url!}</td>
			<th>
				<@w.btnEdit id=permission.id value=permission.id otherText=permission.name />
				<@w.btnDelete id=permission.id value=permission.id  otherText=permission.name permission="/basedata/permission/{id}/delete" />
			</th>
		</tr>
		</#list>
		<#else>
		<tr><td colspan="6">无内容</td></tr>
		</#if>
	</tbody>
	</table>
	<#-- <div class="row">
	<div class="col-xs-6"><div>Showing 1 to 10 of 23 entries</div></div>
	<div class="col-xs-6">
		<div class="dataTables_paginate paging_simple_numbers">
			<ul class="pagination">
			<li class="paginate_button previous disabled"><a href="javascript:;">上一页</a></li>
			<li class="paginate_button active"><a href="javascript:;">1</a></li>
			<li class="paginate_button"><a href="javascript:;">2</a></li>
			<li class="paginate_button"><a href="javascript:;">3</a></li>
			<li class="paginate_button next"><a href="javascript:;">下一页</a></li>
			</ul>
		</div>
	</div>
	</div> -->

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(".btn-edit").each(function(){
		$(this).on("click", function(){
			var value = $(this).attr("value");
			var url =  '${request.contextPath}/basedata/permission/' + value + '/detail/page';
			indexDiv = layerDivUrl(url,{height:350});
		});

	});

	$(".btn-delete").each(function(){
		$(this).on("click", function(){
			var value = $(this).attr("value");
			var name = $(this).attr("otherText");

			swal({title: "删除", html: true, 
			text: "是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
			type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
			cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
			}, 
			function(){   
				$.ajax({
		    		url:'${request.contextPath}/basedata/permission/' + value + '/delete',
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:500},
		    					function(){
		    						sweetAlert.close();
		    						gotoHash("${request.contextPath}/basedata/permission/index/page");
		    					}
		    				);
				 		}
				 		else{
		    				swal({title: "操作失败!",
		    					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
		    				);
		    			}
		    		}
				});
			});
		});
	});
</script>
