<div class="box-half border <#if type! ==7>no-margin width-1of1</#if>">
	<ul class="nav nav-tabs">
	    <li class="active" id="tab-4-chart" >
	        <a href="javascript:void(0);" onclick="loadComponentLibray('chart')" data-toggle="tab">数据图表</a>
	    </li>
	    <li class="" id="tab-4-report">
	        <a href="javascript:void(0);" onclick="loadComponentLibray('report')" data-toggle="tab">数据报表</a>
	    </li>
	    <li class="" id="tab-4-multimodel">
	        <a href="javascript:void(0);" onclick="loadComponentLibray('multimodel')" data-toggle="tab">多维报表</a>
	    </li>
	</ul>
	<div class="tab-content slimScrollBar-made">
	    <div class="tab-pane active" id="library-list-div"></div>
	</div>
</div>
<#if type! ==6>
<div class="box-half">
	<h4>窗口大小</h4>
	<div class="window-size across clearfix">
		<div class="box-window-wrap box-window-wrap-width <#if component.width! =="col-md-3">active</#if>" data-width = "col-md-3">
			<div class="box-window">
				<div class="clearfix">
					<div class="box-one fill"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
				</div>
				<div class="text-center">小</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-width <#if component.width! =="col-md-6">active</#if>" data-width = "col-md-6">
			<div class="box-window">
				<div class="clearfix">
					<div class="box-two fill"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
				</div>
				<div class="text-center">中</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-width <#if component.width! =="col-md-9">active</#if>" data-width = "col-md-9">
			<div class="box-window">
				<div class="clearfix">
					<div class="box-three fill"></div>
					<div class="box-one"></div>
				</div>
				<div class="text-center">中大</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-width <#if component.width! =="col-md-12">active</#if>" data-width = "col-md-12">
			<div class="box-window">
				<div class="clearfix">
					<div class="box-four fill"></div>
				</div>
				<div class="text-center">大</div>
			</div>
		</div>
	</div>
	<h4 id="componentHeightTextDiv">窗口高度</h4>
	<div class="window-size upright clearfix" id="componentHeightDiv">
		<div class="box-window-wrap box-window-wrap-height <#if component.height! =="di">active</#if>" data-height = "di">
			<div class="box-window clearfix">
				<div class="clearfix">
					<div class="box-one fill"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
				</div>
				<div class="centered">低</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-height <#if component.height! =="zhong">active</#if>" data-height = "zhong">
			<div class="box-window clearfix">
				<div class="clearfix">
					<div class="box-two fill"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
				</div>
				<div class="centered">中</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-height <#if component.height! =="gao">active</#if>" data-height = "gao">
			<div class="box-window clearfix">
				<div class="clearfix">
					<div class="box-three fill"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
				</div>
				<div class="centered">高</div>
			</div>
		</div>
	</div>
</div>
</#if>
<input type="hidden" id="type" value="${type!}">
<form id="component-param-form">
<input type="hidden" id="width" name="width" value="${component.width!}">
<input type="hidden" id="id" name="id" value="${component.id!}">
<input type="hidden" id="height" name="height" value="${component.height!}">
<input type="hidden" id="businessType" name="businessType" value="${component.businessType?default('chart')}">
<input type="hidden" id="businessId" name="businessId" value="${component.businessId!}">
<input type="hidden" id="businessName" name="businessName" value="${component.businessName!}">
<input type="hidden" id="reportId" name="reportId" value="${reportId!}">
<input type="hidden" id="componentId" name="componentId" value="${componentId!}">
<input type="hidden" id="orderId" name="orderId" value="${component.orderId!}">
</form>
<script type="text/javascript">

	function loadComponentLibray(businessType,businessId){
		$('#businessType').val(businessType);
		if(businessType=='chart'){
			$("#componentHeightDiv").show();
			$("#componentHeightTextDiv").show();
		}else{	
			$("#componentHeightDiv").hide();
			$("#componentHeightTextDiv").hide();
		}
		if(!businessId)
				businessId="";
		$("#tab-4-"+businessType).addClass('active').siblings().removeClass('active');
	    $("#library-list-div").load("${request.contextPath}/bigdata/data/analyse/component/library?reportId=${reportId!}&businessType="+businessType+"&businessId="+businessId);
	};
	
	$(document).ready(function(){
		$('.box-window-wrap-width').on('click',function(){
			$(this).addClass('active').siblings().removeClass('active');
			$('#width').val($(this).attr('data-width'));
		});
		$('.box-window-wrap-height').on('click',function(){
			$(this).addClass('active').siblings().removeClass('active');
			$('#height').val($(this).attr('data-height'));
		});
		loadComponentLibray('${component.businessType?default('chart')}','${component.businessId!}');
	});
</script>