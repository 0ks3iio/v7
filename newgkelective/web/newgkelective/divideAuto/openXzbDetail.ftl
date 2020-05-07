<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<div class="filter-content">
					<button class="btn btn-blue" onclick="backToPenXzb()">返回</button>
				</div>
			</div>
		</div>
		<div class="table-switch-container no-margin">
			<div class="">
				<div class="table-switch-filter">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">班级：</span>
							<div class="filter-content">
								<select class="form-control" name="groupClassId" id="groupClassId" onChange="loadRight2()">
									
									<#if divideClassList?exists && divideClassList?size gt 0>
										<#list divideClassList as zz>
											<option value="${zz.id!}" <#if groupClassId?default('')==zz.id>selected</#if>>${zz.className!}</option>
										</#list>
									<#else>
										<option value="">暂无数据</option>
									</#if>
									
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="rightTable">
					
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		loadRight2();
	})
	function loadRight2(){
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==''){
			$(".rightTable").html('<div class="table-switch-data default" style="heigth:560px;">暂无班级</div>');
			return;
		}
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/loadRightList?type=1&groupClassId='+groupClassId;
		$(".rightTable").load(url);
	}
	function backToPenXzb(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
		$("#showList").load(url);
	}
	
</script>