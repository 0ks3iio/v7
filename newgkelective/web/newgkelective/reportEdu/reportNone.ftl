<div class="box box-default">
	<div class="box-header">
		<h3 class="box-caption">${unitName!}</h3>
	</div>
	<div class="box-body">
		<div class="text-center margin-t-30 margin-b-30"><img src="${request.contextPath}/static/images/7choose3/icon-warning.png"></div>
		<#if isEdu>
			<p class="font-20 text-center margin-b-30">抱歉，该教育局下还未有上报的${typeName!}结果哦~</p>
			<#--
				<div class="bg-f2faff padding-30 clearfix class-result">
					<div class="text-center">
						<div class="choosed-class clearfix">
							<div class="float-left no-margin width-123">
								<p class="text-right">负责人：</p>
							</div>
							<div class="float-left margin-l-20">${unit.unitHeader!}</div>
						</div>
						<div class="choosed-class clearfix">
							<div class="float-left no-margin width-123">
								<p class="text-right">联系电话：</p>
							</div>
							<div class="float-left margin-l-20">${unit.mobilePhone!}</div>
						</div>
					</div>
				</div>
			-->
		<#else>
			<p class="font-20 text-center margin-b-30">抱歉，该学校还未上报${typeName!}结果哦~</p>
			<div class="bg-f2faff padding-30 clearfix class-result">
				<div class="text-center">
					<div class="choosed-class clearfix">
						<div class="float-left no-margin width-123">
							<p class="text-right">负责人：</p>
						</div>
						<div class="float-left margin-l-20">${unit.unitHeader!}</div>
					</div>
					<div class="choosed-class clearfix">
						<div class="float-left no-margin width-123">
							<p class="text-right">联系电话：</p>
						</div>
						<div class="float-left margin-l-20">${unit.mobilePhone!}</div>
					</div>
				</div>
			</div>
		</#if>
	</div>
</div>
