<#import "/fw/macro/treemacro.ftl" as treemacro>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />

<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">用途：</span>
				<div class="filter-content">
					<select name="" id="bpyt" class="form-control" onchange="showList()">
						<option value="">---请选择---</option>
						<#if usedForDtos?exists&&usedForDtos?size gt 0>
		                  	<#list usedForDtos as item>
							<option value="${item.thisId!}">${item.content!}</option>
		              	    </#list>
                    	</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="" id="grade" class="form-control" onchange="showList()">
					<option value="">---请选择---</option>
					<#if grades?exists&&grades?size gt 0>
	                  	<#list grades as item>
						<option value="${item.id!}">${item.gradeName!}</option>
	              	    </#list>
                    </#if>
					</select>
				</div>
			</div>
			<!--
			<div class="filter-item">
				<span class="filter-name">状态：</span>
				<div class="filter-content">
					<select name="" id="status" class="form-control" onchange="showList()">
                          <option value="0">---请选择---</option>                
                          <option value="1">正常</option>                
                          <option value="2">异常</option>                
					</select>
				</div>
			</div>
			-->
			<div class="filter-item">
				本单位编号：${unitCode!}（该编号用于输入到班牌APP端的参数设置中）
			</div>
			<div id="showList">
			</div>
		</div>
	</div>
</div>
<!-- page specific plugin scripts -->

<script type="text/javascript">
$(function(){
		showList();
	});
function showList(){
	var type = $("#bpyt").val();
	var gradeId = $("#grade").val();
	var url =  '${request.contextPath}/eclasscard/manage/list?type='+type+'&gradeId='+gradeId;
	$("#showList").load(url);
}
</script>

