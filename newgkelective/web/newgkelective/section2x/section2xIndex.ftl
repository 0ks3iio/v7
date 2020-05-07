<#assign nowNum=0>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">2+x开班</h3>
	</div>
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<a class="btn btn-blue" onclick="addSection()">新建一轮</a>
			</div>
		</div>
		<table class="table table-bordered table-striped table-hover ">
			<thead>
				<tr>
					<th>2+x</th>
					<th>备注</th>
					<th style="">操作</th>
				</tr>
			</thead>
			<tbody>
				<#if sectionList?exists && (sectionList?size > 0)>
					<#list sectionList as dto>
					<tr>
					<td>${dto.sectionName!}</td>
					<td>${dto.remark!}</td>
					<td>
					<#if dto.stat?default('0')=='0'>
						<a href="javascript:" onclick="editSection('${dto.id!}')">编辑</a>
						<a href="javascript:" onclick="deleteSection('${dto.id!}','0')">删除</a>
					<#elseif dto.stat?default('0')=='2'>
						<a href="javascript:" onclick="resultSection('${dto.id!}')">查看</a>
						<a href="javascript:" onclick="deleteSection('${dto.id!}','1')">重新设置</a>
						<a href="javascript:" onclick="deleteSection('${dto.id!}','0')">删除</a>
						<a href="javascript:" onclick="chooseSection('${dto.id!}')">使用</a>
					<#elseif dto.stat?default('0')=='1'>
						<#assign nowNum=nowNum+1>
						<a href="javascript:" onclick="editSection('${dto.id!}')">执行中</a>
					<#else>
						<a href="javascript:" onclick="editSection('${dto.id!}')">编辑</a>
						<a href="javascript:" onclick="deleteSection('${dto.id!}','0')">删除</a>
					</#if>
					
					</td>
					</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<script>
$(function(){
	showBreadBack(toBack,false,"2+x");
	<#if nowNum gt 0>
		setTimeClick=setTimeout("freshSection()",30000);
	</#if>
})

function toBack(){
	$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/divideClass/resultClassList");
}

function freshSection(){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/section2x/index/page?type=1';
	$("#showList").load(url);
}

function editSection(sectionId){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/section2x/edit/page?sectionId='+sectionId;
	$("#showList").load(url);
}
function addSection(){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/section2x/add/page';
	$("#showList").load(url);
}
function resultSection(sectionId){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/section2x/result/page?sectionId='+sectionId;
	$("#showList").load(url);
}
var isDeletd=false;
function deleteSection(sectionId,type){
    var mess="";
	if(type=='0'){
		mess="确定删除数据？";
	}else{
		mess="确定重新设置，将会删除之前的分班结果数据？";
	}
	if(isDeletd){
		return;
	}
	isDeletd=true;
	layer.confirm(mess, function(index){
		var ii = layer.load();
		deleteOrStartSection(sectionId,type);
	},function(){
		isDeletd=false;
	})
	
}

function deleteOrStartSection(sectionId,type){
	$.ajax({
		url:"${request.contextPath}/newgkelective/${divideId!}/section2x/deleteOrStartSection",
		data:{'sectionId':sectionId,'type':type},
		dataType: "json",
		success: function(jsonO){
			layer.closeAll();
			if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
	 			isDeletd=false;
	 		}else{
				isDeletd=false;
				layer.msg("操作成功", {
					offset: 't',
					time: 2000
				});
	 			freshSection();
	 		}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}
var isChoose=false;
function chooseSection(sectionId){
	if(isChoose){
		return;
	}
	isChoose=true;
	var url =  '${request.contextPath}/newgkelective/${divideId!}/section2x/useSectionResult';
	var ii = layer.load();
	$.ajax({
		url:url,
		data:{'sectionId':sectionId},
		dataType: "json",
		success: function(jsonO){
			layer.closeAll();
			if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isDeletd=false;
	 			freshSection();
	 		}else{
	 			layer.msg("操作成功", {
					offset: 't',
					time: 2000
				});
				toBack();
	 		}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}
</script>