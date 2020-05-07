<script type="text/javascript">
	//新增课表
	function addLessonTable(){
		var divide_id = $("input[name='divide_id']").val();
		var url =  '${request.contextPath}/newgkelective/'+divide_id+'/gradeArrange/edit?arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	//重新加载当前页面
	function refreshPage(){
		var divide_id = $("input[name='divide_id']").val();
		var url='${request.contextPath}/newgkelective/'+divide_id+'/gradeArrange/index?arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	function backToArrayAdd(){
		var gradeId = $('[name="gradeId"]').val();
		<#if arrayId?default('')==''>
		   var url =  '${request.contextPath}/newgkelective/'+gradeId+'/goArrange/addArray/page?divideId=${divideId!}';
			$("#showList").load(url);
	   <#else>
		   var url =  '${request.contextPath}/newgkelective/'+gradeId+'/goArrange/editArray/page?arrayId=${arrayId!}';
			$("#showList").load(url);
	   </#if>
	}
$(function(){
	var divide_id = $("input[name='divide_id']").val();

	//编辑上课时间方案
	$(".edit").click(function(){
		var array_item_id = $(this).find(":hidden").val();
		var url = "${request.contextPath}/newgkelective/"+divide_id+"/gradeArrange/edit?arrayId=${arrayId!}&arrayItemId="+array_item_id;
		$("#showList").load(url);
	});
});  

var isDel=false;
function deleteItem(arrayItemId){
	if(isDel){
		return;
	}
	isDel=true;
	var url = "${request.contextPath}/newgkelective/"+arrayItemId+"/gradeArrange/delete";
	layer.confirm('确定删除吗？', function(index){
		$.ajax({
			url: url,
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
					refreshPage();
					layer.close(index);
				}else{
					layerTipMsg(data.success,"失败",data.msg);
					isDel=false;
				}
			},
		});
	},function(){
		isDel=false;
	})
} 

	function modifyName(newName,oldName,arrayItemId){
		var obj = this.event.target;
		var nn = $.trim(newName);
		if(nn==''){
			layer.tips('名称不能为空！',$(obj), {
					tipsMore: true,
					tips: 3
				});
			$(obj).focus();
			return;	
		}
		if(nn==oldName){
			return;
		}
		if(getLength(nn)>80){
			layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj), {
					tipsMore: true,
					tips: 3
				});
			$(obj).focus();
			return;	
		}
		$.ajax({
			url:'${request.contextPath}/newgkelective/'+arrayItemId+'/gradeArrange/changName',
			data: {'itemName':newName},
			type:'post',
			dataType:'JSON',
			success:function(data) {
		 		if(data.success){
		 			layer.closeAll();
				  	layer.msg(data.msg, {
							offset: 't',
							time: 2000
						});
					//refreshList();
		 		}else{
		 			this.value=oldName;
		 			layer.tips(data.msg,$(obj), {
						tipsMore: true,
						tips: 3
					});
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function copyThis(itemId){
		$.ajax({
			url:'${request.contextPath}/newgkelective/'+itemId+'/gradeArrange/copyFeature',
			type:'post',
			dataType:'JSON',
			success:function(data) {
		 		if(data.success){
		 			layer.closeAll();
				  	layer.msg(data.msg, {
							offset: 't',
							time: 2000
						});
					refreshPage();
		 		}else{
		 			alert('失败');
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function copyFromOther(){
		$.ajax({
			url : '${request.contextPath}/newgkelective/${divideId!}/showDivedes/page',
			type: "POST",
			success:function(data){
				openLayer(data);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function openLayer(content1){
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '复制排课特征',
			area: ['60%','80%'],
			btn: ['确定', '取消'],
			content: content1,
			yes:function(index,layero){
				var param = $('form').serialize();
				if(!param){
					alert(' 请选择排课特征方案');				
					return;
				}
				$.ajax({
					url : '${request.contextPath}/newgkelective/${divideId!}/copyFeatureFromOthers',
					data: param,
					type: "POST",
					dataType: "JSON",
					success:function(data){
						if(data.success){
							layer.close(index);
							layer.msg(data.msg, {
								offset: 't',
								time: 2000
							});
							refreshPage();
						}else{
							//layerTipMsg(false,'失败',data.msg);
							alert(data.msg);
						}
					},
					error:function(XMLHttpRequest, textStatus, errorThrown){}
				});
				
			}
			
		});
	}
	
</script>

<input type="hidden" name="divide_id" value="${divideId!}"/>
<input type="hidden" name="gradeId" value="${gradeId!}"/>	
<div class="page-toolbar">
	<a href="javascript:" onclick="backToArrayAdd()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
	<div class="page-toolbar-btns">
		<span class="tip tip-grey">共有 ${items?size!}  条结果</span>
		<!-- <a class="btn btn-blue" href="javascript:" onclick="refreshPage()">刷新列表</a>  -->
		<a class="btn btn-blue" href="javascript:" onclick="copyFromOther()">复制其他分班的排课特征</a>
		<a class="btn btn-blue" href="javascript:" onclick="addLessonTable()">新增排课特征</a>
	</div>
</div>
<#if items?exists && items?size gt 0>
<#list items as item>

<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title"><input style="width: 350%;" class="table-input" type="text" data-choiceId="" 
			value="${item.itemName!}" onblur="modifyName(this.value,'${item.itemName!}','${item.id}')">
		</h3>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
				<div class="btn-group" role="group">
					<a type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						更多
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a href="javascript:copyThis('${item.id!}')" class="js-del">复制此特征</a></li>
						<li><a href="javascript:deleteItem('${item.id!}')" class="js-del">删除</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="box-body edit">
		<p class="explain">创建时间：${item.creationTime!}</p>
		<div class="number-container">
			<a href="javascript:">
				<input type="hidden" name="array_item_id" value="${item.id!}"/>
				<#--
					<ul class="number-list">
						<#if item.freeLessonMap?exists>
						<#list item.freeLessonMap?keys as key>
							<li><em>${item.freeLessonMap[key]!}</em><span>${key!}</span></li>
						</#list>
						</#if>
					</ul>
				-->
				
			</a>
			<#if item.newGkItemDto?exists && item.newGkItemDto.typeName?size gt 0 >
				<#assign typeNameArr=item.newGkItemDto.typeName>
				<#assign numArr=item.newGkItemDto.num>
				<table class="table table-bordered table-striped table-hover">
                    <thead>
						<tr>
						    <th class="text-center">周课时/科目教师数</th>
							<#list typeNameArr as typeName>
							<th class="text-center">${typeName!}</th>
							</#list>
						</tr>
					</thead>
					<tbody>	
					    <tr>
							<th class="text-center">数量</th>
							<#list numArr as num>
								<td class="text-center">${num!}</td>
							</#list>
						</tr>
					</tbody>
				</table>
				</#if>
		</div>
	</div>
</div>
</#list>
</#if>