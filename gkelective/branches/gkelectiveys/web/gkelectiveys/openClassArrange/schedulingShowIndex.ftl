<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div class="box-body" id="perArrangeDiv">
	<p class="text-right"><a class="btn btn-sm btn-blue" href="javascript:" onclick="backToPerArrange()">返回</a></p>
	<div class="table-switch-container">
		<div class="table-switch-box">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
								
							<div class="input-group input-group-sm input-group-search">
								<select name="groupClassId" id="groupClassId" class="form-control" onChange="loadMyTable()">
								<#if groupClassList?exists && (groupClassList?size>0)>
									<#list groupClassList as group>
									<option value="${group.id!}" <#if groupClassId?default('')==group.id> selected="selected"</#if>>${group.groupName!}</option>
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
</div>
<script>
	contextPath = '${request.contextPath}';
	roundsId = '${roundsId!}';
	function backToPerArrange(){
		toPerArrange();
	}
	//加载右边
	function loadMyTable(){
		var groupClassId=$("#groupClassId").val();
		var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/schedulingOne/page?groupClassId='+groupClassId;
		$("#myTableId").load(url);
	}
	$(function(){
		loadMyTable();
	});
</script>
