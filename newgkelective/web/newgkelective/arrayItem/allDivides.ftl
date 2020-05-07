<style>
#divideTree {
	width:23%;
	height:auto;
	margin:10px 10px 0px 0px;
	position: absolute;
	top: 0px;
    bottom: 0px;
}
#bb {
	margin:0px 10px 0px 10px;
	position: absolute;
	top: 0px;
	left:25%;
	right:0px;
    bottom: 0px;
    overflow:auto;
}
</style>
<div class="box-body">
<#-- 	<div class="filter">
		<div class="filter-item filter-item-right">
			<div class="filter-content">
				<button class="btn btn-blue">打印</button>
				<button class="btn btn-blue">导出Excel</button>
			</div>
		</div>
	</div>  -->
	<div class="clearfix" style="">
		<div class="tree-wrap" id="divideTree">
			<h4>选择分班方案</h4>
			<div class="tree-list" style="margin: 0px">
			<#if divideList?exists && divideList?size gt 0>
			<#list divideList as divide>
				<a <#if divide_index == 0>class="active"</#if> onclick="showFeatures('${divide.id}',event)" href="javascript:void(0);">${divide.divideName}</a>
			</#list>
			</#if>
			</div>
		</div>
		<div id="bb" style="">
			
		</div>
		<div>
		</div>
	</div>  
</div>

<script>
	function showFeatures(divideId,event){
		$('#divideTree a.active').removeClass("active");
		$(event.target).addClass("active");
		$.ajax({
			url : '${request.contextPath}/newgkelective/'+divideId+'/showItherFeatures/page',
			type: "POST",
			success:function(data){
				$('#bb').html(data);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	$(function(){
		$('#divideTree a.active').click();
	});
</script>