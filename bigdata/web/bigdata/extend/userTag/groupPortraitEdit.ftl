<div class="box-half border">
	<ul class="nav nav-tabs">
	    <li class="active" id="tab-4-chart" >
	        <a href="javascript:void(0);" onclick="loadComponentLibray('chart')" data-toggle="tab">${userProfileName!}标签</a>
	    </li>
	</ul>
	<div class="tab-content slimScrollBar-made">
	    <div class="tab-pane active" id="library-list-div">
		<#if tags?exists&&tags?size gt 0>
			<#list tags as tag>
		    <label id="library-${tag.id!}" class="choice" onclick="chooseComponent('${tag.id!}');">
			    <input type="checkbox" name="${tag.id!}" id="${tag.id!}" <#if isEdit>checked="checked" disabled</#if>/>
			    <span class="choice-name">${tag.tagName!}</span>
			</label>
			</#list>
		<#else>
		<li><a href="javacript:void(0);" ><label class="pos-rel js-kind" ><span class="lbl">无可用的标签</span></label></a></li>
		</#if>
	    </div>
	</div>
</div>
<div class="box-half">
	<h4>窗口大小</h4>
	<div class="window-size across clearfix">
		<div class="box-window-wrap box-window-wrap-width <#if template.width! =="col-md-3">active</#if>" data-width = "col-md-3">
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
		<div class="box-window-wrap box-window-wrap-width <#if template.width! =="col-md-6">active</#if>" data-width = "col-md-6">
			<div class="box-window">
				<div class="clearfix">
					<div class="box-two fill"></div>
					<div class="box-one"></div>
					<div class="box-one"></div>
				</div>
				<div class="text-center">中</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-width <#if template.width! =="col-md-9">active</#if>" data-width = "col-md-9">
			<div class="box-window">
				<div class="clearfix">
					<div class="box-three fill"></div>
					<div class="box-one"></div>
				</div>
				<div class="text-center">中大</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-width <#if template.width! =="col-md-12">active</#if>" data-width = "col-md-12">
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
		<div class="box-window-wrap box-window-wrap-height <#if template.height! =="di">active</#if>" data-height = "di">
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
		<div class="box-window-wrap box-window-wrap-height <#if template.height! =="zhong">active</#if>" data-height = "zhong">
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
		<div class="box-window-wrap box-window-wrap-height <#if template.height! =="gao">active</#if>" data-height = "gao">
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
	<h4 id="chartTypeTextDiv">图表类型</h4>
	<div class="window-size clearfix new-charts-box" id="chartTypeDiv">
		<div class="box-window-wrap box-window-wrap-chart <#if template.chartType! =="line">active</#if>" data-chart-type = "line">
			<div class="box-window">
				<div class="text-center">
					<i class="iconfont icon-linechart"></i>
				</div>
				<div class="text-center">线图</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-chart <#if template.chartType! =="bar">active</#if>" data-chart-type = "bar">
			<div class="box-window">
				<div class="text-center">
					<i class="iconfont icon-histogram"></i>
				</div>
				<div class="text-center">柱图</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-chart <#if template.chartType! =="pie">active</#if>" data-chart-type = "pie">
			<div class="box-window">
				<div class="text-center">
					<i class="iconfont icon-pie"></i>
				</div>
				<div class="text-center">饼图</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-chart <#if template.chartType! =="map">active</#if>" data-chart-type = "map">
			<div class="box-window">
				<div class="text-center">
					<i class="iconfont icon-map"></i>
				</div>
				<div class="text-center">地图</div>
			</div>
		</div>
		<div class="box-window-wrap box-window-wrap-chart <#if template.chartType! =="scale">active</#if>" data-chart-type = "scale">
			<div class="box-window">
				<div class="text-center">
					<i class="iconfont icon-proportion"></i>
				</div>
				<div class="text-center">比例</div>
			</div>
		</div>
	</div>
	<div class="map-block">
		<div id="mapRegionCodeDiv" class="map-block-li" <#if template.chartType! !="map">style="display: none;"</#if>>
			<label>地区码</label>
			<input id="regionCodeVal" class="form-control" value="${template.regionCode?default('00')}"  maxlength="10">
		</div>
		<div class="map-block-li">
			<label>排序号</label>
			<input id="orderIdVal" class="form-control" value="${template.orderId!}" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="3">
		</div>
	</div>
</div>
<form id="tag-group-form">
<input type="hidden" id="width" name="width" value="${template.width!}">
<input type="hidden" id="id" name="id" value="${template.id!}">
<input type="hidden" id="businessId" name="tagId" value="${template.tagId!}">
<input type="hidden" id="profileCode" name="profileCode" value="${template.profileCode!}">
<input type="hidden" id="height" name="height" value="${template.height!}">
<input type="hidden" id="chartType" name="chartType" value="${template.chartType!}">
<input type="hidden" id="orderId" name="orderId" value="${template.orderId!}">
<input type="hidden" id="regionCode" name="regionCode" value="">
</form>
<script type="text/javascript">
$(document).ready(function(){
	$(".map-block").show();
	$('.box-window-wrap-width').on('click',function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('#width').val($(this).attr('data-width'));
	});
	$('.box-window-wrap-height').on('click',function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('#height').val($(this).attr('data-height'));
	});
	$('.box-window-wrap-chart').on('click',function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('#chartType').val($(this).attr('data-chart-type'));
		if($(this).attr('data-chart-type') == 'map'){
			$('#mapRegionCodeDiv').show();
		}else{
			$('#mapRegionCodeDiv').hide();
		}
	});
});

function chooseComponent(id){
	if($("#library-"+id).children('input').prop('checked')){
		$("#businessId").val(id);
		$("#library-"+id).siblings().find('input').prop('checked',false)
	}else{
		$("#businessId").val('');
	}
}
var isSubmit = false;
function saveGroupTag(){
		if(isSubmit){
			return;
		}
		var businessId=$("#businessId").val();
		var width=$("#width").val();
		var height=$("#height").val();
 		var chartType=$("#chartType").val();
 		var orderId=$("#orderIdVal").val();
 		var regionCode=$("#regionCodeVal").val();
		if(businessId ==""){
			showLayerTips4Confirm('warn',"请先选择标签");
			return;
		}
		 if(width ==""){
			showLayerTips4Confirm('warn',"请先选择窗口宽度");
			return;
		 }
		 if(height ==""){
			showLayerTips4Confirm('warn',"请先选择窗口高度");
			return;
		 }
		 if(chartType ==""){
			showLayerTips4Confirm('warn',"请先选择图表类型");
			return;
		 }
		 if(chartType =="map"){
		 	 if(regionCode==""){
				showLayerTips4Confirm('warn',"请填写地区码");
				return;
		 	 }
			 $("#regionCode").val(regionCode);
		 }
		 $("#orderId").val(orderId);
		 isSubmit = true;
		 var options = {
			url:"${request.contextPath}/bigdata/userTag/group/save",
			dataType : 'json',
			success : function(data){
				layer.closeAll();
		 		if(!data.success){
		 			showLayerTips4Confirm('error',data.message);
		 			isSubmit = false;
		 		}else{
		 			showLayerTips('success',data.message,'t');
		 			$('.page-content').load('${request.contextPath}/bigdata/userTag/group/portrait?type=0&profileCode=${template.profileCode!}');
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#tag-group-form").ajaxSubmit(options);
	}
</script>