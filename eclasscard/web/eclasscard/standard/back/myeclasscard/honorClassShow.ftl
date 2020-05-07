<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
			<div id="cc" class="tab-pane active" role="tabpanel">
				<div class="form-horizontal">
					<form id="honorForm">
					<div class="form-group">
						<label class="col-sm-2 control-title no-padding-right">查看荣誉&nbsp;</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">荣誉名称：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<label style="font-weight: normal;"><span class="lbl"></span>${eccHonor.title!}</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">班牌展示时间：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<label style="font-weight: normal;"><span class="lbl">${eccHonor.beginTime!}至${eccHonor.endTime!}</span></label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">获奖日期：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<label style="font-weight: normal;"><span class="lbl">${(eccHonor.awardTime?string("yyyy-MM-dd"))?if_exists}</span></label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">荣誉样式：</label>
						<div class="col-sm-8">
							<ul class="honor-list clearfix" id="selectStyle">
								<li>
								<#if eccHonor.style==1>
									<div >
										<img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" alt="">
									</div>
									<p>锦旗</p>
								<#elseif eccHonor.style==2>
									<div>
										<img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png" alt="">
									</div>
									<p>奖杯</p>
								<#else>
									<div>
										<img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png" alt="">
									</div>
									<p>勋章</p>
								</#if>
								</li>
							</ul>
						</div>
					</div>
					</form>
					<div class="form-group">
						<div class="col-sm-8 col-sm-offset-2">
							<button class="btn btn-long btn-blue" onclick="gobacktoList()">返回</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function gobacktoList(){
		backFolderIndex('2','2');
	}
	$(function(){
		<#--返回-->
		showBreadBack(gobacktoList,true,"班级荣誉列表");
	});
	
</script>