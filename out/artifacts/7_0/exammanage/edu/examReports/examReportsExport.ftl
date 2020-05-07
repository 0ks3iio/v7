<div id="exportDiv">
<div class="layer layer-gk-copy" style="display:block" >
	<div class="layer-content">
		<div class="gk-copy">
			<div class="gk-copy-side" style="width:30%;">
				<h3 class="gk-copy-header">
					<label><span class="lbl"> 考区</span></label>
				</h3>
				<ul class="nav gk-copy-nav" role="tablist">
					<#if emRegionList?exists && emRegionList?size gt 0>
					<#list emRegionList as item>
						<li <#if item.id=regionId?default("")>class="active regionclass"<#else>class="regionclass"</#if> role="presentation" id="${item.id}">
							<a href="javascript:;" role="tab" data-toggle="tab">${item.regionName!}</a>
						</li>
					</#list>
					</#if>
				</ul>
			</div>
			<div class="gk-copy-main" id="optionDivId">
				<#--<h3 class="gk-copy-header">
					<label> 考点</span></label><input class="wp" type="checkbox"><span class="lbl">
				</h3>
				<div class="tab-content">
					<div id="FamDearPlanService" class="tab-pane active" role="tabpanel">
						<ul class="gk-copy-list">
						<#if emOptionList?exists && emOptionList?size gt 0>
						<#list emOptionList as item>
							<li><label><input type="radio" name="myOption" class="wp" value="${item.id!}"><span class="lbl">${item.optionName!}</span></label></li>
						</#list>
						</#if>
						</ul>
					</div>
					<div id="bbb" class="tab-pane" role="tabpanel"></div>
				</div>-->
			</div>
		</div>
	</div>
</div>
<form id="templateform" name="templateform" method="post">
	<input type="hidden" name="examId" value="${examId!}">
	<input type="hidden" name="type" value="${type!}">
	<input type="hidden" name="regionId" id="regionId" value="${regionId!}">
	<input type="hidden" id="optionId" name="optionId" value="${optionId!}">
	<input type="hidden" id="flag" name="flag">
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" onclick="exportHtmlToPdf();">确定</a>
	<a href="javascript:" class="btn btn-grey" id="export-close">取消</a>
</div>
<script>
	$(function(){
		// 取消按钮操作功能
		$("#export-close").on("click", function(){
		     layer.closeAll();  
		});
		$(".regionclass").click(function(){
			getOption($(this).attr("id"));
		});
		getOption('${regionId!}');
	})
	function getOption(regionId){
		$("#regionId").val(regionId);
	    var url = "${request.contextPath}/exammanage/edu/examReports/exportOptionList/page?examId=${examId!}&regionId="+regionId;
	    $(".regionclass.active").removeClass("active");
	    $("#"+regionId).addClass("active");
	    $("#optionDivId").load(url);
	}
	var isSubmit=false;
	function exportHtmlToPdf(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var optionId=$("input:radio[name='myOption']:checked").val();
		if(optionId==undefined || optionId==''){
			layerTipMsg(false,"提示","请先选择考点！");
			isSubmit=false;
			return;
		}
		$("#optionId").val(optionId);
		var flag=false;
		if(confirm('是否覆盖已生成的pdf')){
			var flag=true;
		}
		$("#flag").val(flag);
		var templateform=document.getElementById('templateform');
		if(templateform){
			templateform.action="${request.contextPath}/exammanage/edu/examReports/exportHtmlToPdf/page";
			templateform.target="hiddenIframe";
			templateform.submit();
			isSubmit=false;
		}
	}
	function changeRegion(regionId){
		 var url = "${request.contextPath}/exammanage/edu/examReports/exportList/page?examId=${examId!}&type=${type!}&regionId="+regionId;
		 $("#exportDiv").load(url);
	}
</script>
</div>