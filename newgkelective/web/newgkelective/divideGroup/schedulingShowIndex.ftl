<div class="box-body box-default" id="perArrangeDiv">
	<div class="table-switch-container">
		<div class="table-switch-filter">
			<div class="filter filter-sm">
				<div class="filter-item">
					<span class="filter-name">班级：</span>
					<div class="filter-content">
							
						<div class="input-group input-group-sm input-group-search">
							<select name="groupClassId" id="groupClassId" class="form-control" onChange="loadMyTable()">
							<#if divideClassList?exists && (divideClassList?size>0)>
								<#list divideClassList as group>
								<option value="${group.id!}" <#if divideClassId?default('')==group.id> selected="selected"</#if>>${group.className!}</option>
								</#list>
							<#else>
								<option value="">暂无班级</option>
							</#if>
							</select>
						   
					    </div><!-- /input-group -->
					</div>
				</div>
			</div>
		</div>
		<div style="width:100%;" class="tableDivClass" id="myTableId">
			
		</div>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-blue" href="javascript:void(0)" onclick="backToIndex()">上一步</a>
</div>
<script>
	function backToIndex(){
		var url ='';
		<#if type?default('')=='2'>
			url =  '${request.contextPath}/newgkelective/BathDivide/${divideId!}/arrangeAList/page';
		<#else>
			url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/groupIndex/page';
		</#if>
		$("#showList").load(url);
	}
	//加载右边
	function loadMyTable(){
		var divideClassId=$("#groupClassId").val();
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/schedulingOne/page?divideClassId='+divideClassId;
		$("#myTableId").load(url);
	}
	$(function(){
		//showBreadBack(backToIndex,false,"分班安排");
		loadMyTable();
	});
</script>
