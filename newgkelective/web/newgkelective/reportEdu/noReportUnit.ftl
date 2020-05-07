<div class="box box-default">
	<div class="box-header">
		<h3 class="box-caption">${unitName!}</h3>
	</div>
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">所属教育局：</span>
				<div class="filter-content">
					<select name="chooseUnitId" id="chooseUnitId" class="form-control" style="width:200px;" onChange="loadNoByEduUnitId();">
					     <#if unitList?exists && unitList?size gt 0>
					     	<#list unitList as item>
					     		<option value="${item.id!}">${item.unitName!}</option>
					     	</#list>
					     </#if>
					</select>
				</div>
			</div>
		</div>
		<div id="unitTableList">
		</div>
	</div>
</div>
<script>
	$(function(){
		
		$('#chooseUnitId').chosen({
			disable_search:false, //是否隐藏搜索框
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			search_contains:true//模糊匹配，false是默认从第一个匹配
		}); 
		loadNoByEduUnitId();
	})
	function loadNoByEduUnitId(){
		var chooseUnitId=$("#chooseUnitId").val();
		$("#unitTableList").load("${request.contextPath}/newgkelective/edu/noreportList/page?gradeYear=${gradeYear!}&type=${type!}&unitId="+chooseUnitId);
	}
</script>
