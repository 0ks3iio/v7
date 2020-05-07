<meta charset="utf-8" />

<script type="text/javascript">
	showBreadBack(backToArrayAdd,false,"课表设置列表");
	//新增课表
	function addLessonTable(){
		var divide_id = $("input[name='divide_id']").val();
		var url =  '${request.contextPath}/newgkelective/'+divide_id+'/lessonTimeSetting/list/page?arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	//重新加载当前页面
	function refreshPage(){
		var divide_id = $("input[name='divide_id']").val();
		var url='${request.contextPath}/newgkelective/'+divide_id+'/lessonTimeArrange/index/page?arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	function backToArrayAdd(){
		var gradeId = $('[name="gradeId"]').val();
		<#if arrayId?default('')==''>
		   var url =  '${request.contextPath}/newgkelective/'+gradeId+'/goArrange/addArray/page?divideId=${divide_id!}';
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
		var array_item_id = $(this).children(":hidden").val();
		var url = "${request.contextPath}/newgkelective/"+array_item_id+"/lessonTimeEdit/list/page?divide_id="+divide_id+"&arrayId=${arrayId!}";
		$("#showList").load(url);
	});
	//根据divide_id删除某上课时间方案
	$(".js-del").click(function(){
		var array_item_id = $(this).parents(".box-default:first").find("input[name='array_item_id']").val();
		layer.confirm('确定删除吗？', function(index){
			var url = "${request.contextPath}/newgkelective/"+array_item_id+"/deleteLessonTimeItem";
			$.post(url, 
					function(msg){
						if(msg=="SUCCESS"){
							layer.msg("删除方案成功", {
								offset: 't',
								time: 2000
							});
							refreshPage();
						}
					});
		})
	});
});   
</script>

<input type="hidden" name="divide_id" value="${divide_id!}"/>
<input type="hidden" name="gradeId" value="${grade.id!}"/>	
<#--
<div class="page-toolbar">
	<a href="javascript:" onclick="backToArrayAdd()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
	<div class="page-toolbar-btns">
		<span class="tip tip-grey">共有 ${simpleInfs?size!}  条选课结果</span>
		<a class="btn btn-blue" href="javascript:" onclick="addLessonTable()">新增课表</a>
	</div>
</div>
-->
<div class="filter">	
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:" onclick="addLessonTable()">新增课表</a>
	</div>
	<div class="filter-item filter-item-right">
		<span class="color-999">共有 ${simpleInfs?size!}  条数据</span>
	</div>
</div>
<#if simpleInfs?exists && simpleInfs?size gt 0>
<#list simpleInfs as item>

<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${item.item_name!}</h3>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						更多
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a href="javascript:" class="js-del">删除</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="box-body">
		<p class="explain">创建时间：${item.creation_time!}</p>
		<div class="number-container">
			<a href="javascript:" class="edit">
				<input type="hidden" name="array_item_id" value="${item.array_item_id!}"/>
				<ul class="number-list">
					<#if item.freeLessonMap?exists>
					<#list item.freeLessonMap?keys as key>
						<li><em>${item.freeLessonMap[key]!}</em><span>${key!}</span></li>
					</#list>
					</#if>
				<!--	<li><em>${item.freeLessonMap['2']!}</em><span>星期二</span></li>
					<li><em>${item.freeLessonMap['3']!} </em><span>星期三</span></li>
					<li><em>${item.freeLessonMap['4']!} </em><span>星期四</span></li>
					<li><em>${item.freeLessonMap['5']!} </em><span>星期五</span></li>	 ||&& simpleInfs.freeLessonMap?size gt 0-->
				</ul>
			</a>
		</div>
	</div>
</div>
</#list>
</#if>