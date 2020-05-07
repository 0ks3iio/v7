<!-- 单位联系人管理首页 -->
<div class="page-sidebar">
	<div class="page-sidebar-header ml10 mr10">
		<div class="input-group mt20">
			<input type="text" class="form-control">
			<a href="#" class="input-group-addon">
				<i class="fa fa-search"></i>
			</a>
		</div>
	</div>
	<#import "../macro/unitTree.ftl" as u />
	<@u.unitTree dataType='unitPrincipal' callback='changeUnit'/>
</div>
		<div class="page-content-inner">
			<div id="unitPrincipalListContainer" class="box box-default">
				<div class="box-header">
					<h3 class="box-caption">单位负责人管理</h3>
				</div>
				<div class="box-body">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">重要层级：</span>
							<div class="filter-content">
								<select class="form-control js-select-level" name="unitStarLevelOption" id="unitStarLevelOption" onchange="doGetUnitPrincipalList('')">
									<#if unitStarLevelList?? && unitStarLevelList?size gt 0>
                                        <option value="-1">全部</option>
										<#list unitStarLevelList as unitStarLevelVo>
											<option value="${unitStarLevelVo.unitStarLevel}">${unitStarLevelVo.represent}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">联系人：</span>
							<div class="filter-content">
								<input id = "unitHeaderParameterValue" class="form-control" type="text">
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">手机号：</span>
							<div class="filter-content">
								<input id="phoneParameterValue" class="form-control" type="text">
							</div>
						</div>
						<div class="filter-item">
							<div class="filter-content">
								<button class="btn btn-blue" onclick="doGetUnitPrincipalList('')">查找</button>
							</div>
						</div>
					</div>
					<div id="unitPrincipalAccounts">
					</div>
				</div>
			</div>
		</div>
<script>
	var trUnit = {};
	var recordId;
	var ztreeId = '';
	var unitNameVaild = 0; //验证单位名称是否重复 0为不重复 1为重复
	var parentRegionCode = '000000';
	var pId = '';

	// 点击页面调用
	$(function(){
            doGetUnitPrincipalList('');
	})
	// 获取单位负责人列表
	function doGetUnitPrincipalList(pageURL) {
		let pURL = doBuildDynamicParameter();
		if ($.trim(pageURL) !== '') {
			pURL = pURL + '&' + pageURL;
		}
		$('#unitPrincipalAccounts').load(_contextPath + '/operation/unitPrincipal/unitPrincipalList?parentId=' + ztreeId + '&' + pURL);
	}

	function doBuildDynamicParameter() {

		let pURL = '';
		let starLevel = $('#unitStarLevelOption').val();
		if (starLevel == '-1') {
			starLevel = '';
		}
		pURL = pURL+ 'starLevel=' + starLevel;
		let unitHeaderVal = $.trim($('#unitHeaderParameterValue').val());
		if (unitHeaderVal != ''){
            pURL += '&' + 'unitHeader'+ '=' + unitHeaderVal;
        }
		let phoneVal = $.trim($('#phoneParameterValue').val());
		if (phoneVal != '') {
            pURL += '&' + 'unitHeaderPhone' + '=' + phoneVal;
        }
        return pURL;
	}

	function changeUnit(pId) {
		// 初始化条件查询
		$("#unitStarLevelOption").val("-1");
		$("#unitHeaderParameterValue").val('').text('');
		$("#phoneParameterValue").val('').text('');
		ztreeId = pId;
		$('#unitPrincipalAccounts').load(_contextPath + '/operation/unitPrincipal/unitPrincipalList?parentId=' + pId);
	}
</script>