<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var params="业务参数";
	//layerTipMsgWarn("提示",params);
	dataimport(params);

}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo!}">
<div class="import-step clearfix">
	<span class="import-step-num">✔</span>
	<div class="import-content">
		<p>选择任课信息相关属性</p>
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control">
						<option value=""></option>
						<option value="2016">2016</option>
						<option value="2015">2015</option>
						<option value="2014">2014</option>
						<option value="2013">2013</option>
						<option value="2012">2012</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select class="form-control">
						<option value=""></option>
						<option value="2016">2016</option>
						<option value="2015">2015</option>
						<option value="2014">2014</option>
						<option value="2013">2013</option>
						<option value="2012">2012</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试方式：</label>
				<div class="filter-content">
					<select class="form-control">
						<option value=""></option>
						<option value="2016">2016</option>
						<option value="2015">2015</option>
						<option value="2014">2014</option>
						<option value="2013">2013</option>
						<option value="2012">2012</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">成绩录入方式：</label>
				<div class="filter-content">
					<select class="form-control">
						<option value=""></option>
						<option value="2016">2016</option>
						<option value="2015">2015</option>
						<option value="2014">2014</option>
						<option value="2013">2013</option>
						<option value="2012">2012</option>
					</select>
				</div>
			</div>
		</div>
	</div>
</div>
</@import.import>