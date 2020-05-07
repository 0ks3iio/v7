<style type="text/css">
	.baidu-map {width: 100%;height: 100%;overflow: hidden;font-family:"微软雅黑";}
	.anchorBL{
    	display:none;
	}
</style>
<div class="row">
    <div class="col-xs-12">
        <div class="box box-default position-relative no-border overflow-hidden js-height">
			<div class="map-switch clearfix">
				<#if parentRegionCode! !="" &&  parentRegionCode !=regionCode>
					<div class="map-switch-arrow js-switch"  onclick="return2Upper();"><i class="wpfont icon-arrow-left"></i></div>
				</#if>
				<div class="map-switch-local" onclick="showSchool('${regionCode!}','${parentRegionCode!}');">学校模式</div>
			</div>
			<div class="wrap-1of1 bmap-made baidu-map" id="allmap"></div>
			<div class="map-right-part-wrap slimScrollBar-made clearfix" id="regionDetailId">
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var map;
	var region_markers = new Array();
	var parentRegionCode="${parentRegionCode!}";
	
	var fillColors = new Array();
	fillColors.push("#dd6b66");
	fillColors.push("#759aa0");
	fillColors.push("#e69d87");
	fillColors.push("#8dc1a9");
	fillColors.push("#ea7e53");
	fillColors.push("#eedd78");
	fillColors.push("#73a373");
	fillColors.push("#73b9bc");
	fillColors.push("#7289ab");
	fillColors.push("#91ca8c");
	fillColors.push("#f49f42");

	function loadScript() {
        var script = document.createElement("script");
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=lm7EnvxLlwCTsBszhaU2NQkVLjwGDlZy&callback=initialize";
        document.body.appendChild(script);
    }

    function initialize() {  
		// 百度地图API功能
		 map = new BMap.Map("allmap",{
			minZoom : 2,
			maxZoom : 12
		});    // 创建Map实例
		map.centerAndZoom(new BMap.Point('${currentRegion.locationLatitude!}','${currentRegion.locationLongitude!}'), ${currentRegion.zoomLevel!});  // 初始化地图,设置中心点坐标和地图级别
		map.setCurrentCity("${currentRegion.regionName!}");   
		//map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		//map.addControl(new BMap.NavigationControl({ type: BMAP_NAVIGATION_CONTROL_LARGE ,anchor: BMAP_ANCHOR_TOP_LEFT, offset: new BMap.Size(40, 250)}));
		map.disableDragging();  
		map.disableDoubleClickZoom(true);
	
	    var city = new Array();
	     <#if regionList?exists&&regionList?size gt 0>
				<#list regionList as region>
		     		var cityJSON={};
					cityJSON.name="${region.regionName!}";
					cityJSON.color="#9F79EE",
					cityJSON.latitude="${region.latitude!}";
					cityJSON.longitude="${region.longitude!}";
					cityJSON.fullCode="${region.fullCode!}";
					city.push(cityJSON);
	     		</#list>
		</#if>     
	
		for(var i=0;i<city.length;i++){
		    getBoundary(city[i],i);
		}
		
		addlabel(city);
	
		$("#regionDetailId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/regionDetail?regionCode=${regionCode!}",function() {

		});
	}

	function getBoundary(city,indexNum){
	    var bdary = new BMap.Boundary();
	    bdary.get(city.name, function (rs) {       //获取行政区域       
	       var count = rs.boundaries.length; //行政区域的点有多少个
			if (count === 0) {
				console.log(city.name);
				console.log('未能获取当前输入行政区域');
				return ;
			}
          	var pointArray = [];
			for (var i = 0; i < count; i++) {
				indexNum=indexNum%10;
				var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor:"#ffffff",fillColor: fillColors[indexNum]}); //建立多边形覆盖物
				map.addOverlay(ply);  //添加覆盖物
			} 
	  	});
	}  
	
	function addlabel(city) {
		var pointArray = [];
		for(var i=0;i<city.length;i++){
		    var point =new BMap.Point(city[i].latitude,city[i].longitude);
			pointArray.push(point);
		}
	    var optsArray = [];
	    for(var i=0;i<city.length;i++){
			optsArray.push({});
		}
	    var labelArray = [];
	    var contentArray=[];
	    for(var i=0;i<city.length;i++){
			contentArray.push(city[i].name);
		}
		
		var fullCodes = new Array();
	    for(var i=0;i<city.length;i++){
	      var marker = new BMap.Marker(pointArray[i]);//按照地图点坐标生成标记
	     // marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
	      fullCodes.push(city[i].fullCode);
          labelArray[i] = new window.BMap.Label(contentArray[i], { offset: new window.BMap.Size(20, -10) });
          marker.setLabel(labelArray[i]);//显示marker的title
          marker.fullCode =fullCodes[i];
          region_markers.push(marker);
          (function(){
    			var _marker = marker;
    			_marker.addEventListener("mouseover",function(){
	    				$("#regionDetailId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/regionDetail?regionCode="+_marker.fullCode,function() {
	
						});
    			   });
    			_marker.addEventListener("mouseout",function () {
					$("#regionDetailId").empty();
				 });
				 _marker.addEventListener("click",function (e) {
					if(_marker.fullCode.endWith("00")){
						$("#transferId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/xz?regionCode="+_marker.fullCode+"&parentRegionCode="+parentRegionCode);
					}else{
						console.log("已经是区县级别，不能再扩展了")
					}
				 });
    		})() 
	      map.addOverlay(marker);
	    }	  
	}

	function return2Upper(){
		$("#transferId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/xz?regionCode="+parentRegionCode);
	}
	
	function showSchool(regionCode,parentRegionCode){
		$("#transferId").empty();
		$("#transferId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/school?regionCode="+regionCode+"&parentRegionCode="+parentRegionCode);
	}
    
    String.prototype.endWith=function(str){
		if(str==null||str==""||this.length==0||str.length>this.length)
		  return false;
		if(this.substring(this.length-str.length)==str)
		  return true;
		else
		  return false;
		return true;
	}

	$(function(){
	    loadScript();
		$('.js-height').each(function(){
			$(this).css({
	            height: $(window).height() - $(this).offset().top - 20,
	        });
		});
	})
</script>