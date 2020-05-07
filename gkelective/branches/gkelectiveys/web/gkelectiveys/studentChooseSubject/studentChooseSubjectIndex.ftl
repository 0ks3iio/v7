<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<div class="box box-default" style="display: none;">
	<div class="box-body">
		<!-- 筛选开始 -->
		<div class="filter filter-f16">
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control" id="acadyearSearch"  onChange="itemShowList()">
						<#if (acadyearList?size>0)>
							<#list acadyearList as item>
							<option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select class="form-control" id="semesterSearch" onChange="itemShowList()" >
						 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
					</select>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="itemShowDivId">
</div>

<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
		var acadyearSearch = $("#acadyearSearch").val();
		var semesterSearch = $("#semesterSearch").val();
		var url =  '${request.contextPath}/gkelective/studentChooseSubject/list/page?acadyearSearch='+acadyearSearch+'&semesterSearch='+semesterSearch;
		$("#itemShowDivId").load(url);
	}
</script>
