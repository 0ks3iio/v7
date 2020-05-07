<style type="text/css">
	.baidu-map {width: 100%;height: 100%;overflow: hidden;font-family:"微软雅黑";}
	.anchorBL{
    	display:none;
	}
</style>
<div class="row">
    <div class="col-xs-12">
        <div class="box box-default position-relative no-border overflow-hidden js-height">
			<div class="map-search">
				<div class="input-group">
			      	<input type="text" class="form-control" placeholder="请输入校名">
			      	<span class="input-group-btn">
			        	<button class="btn btn-blue" id="keyword" type="button"><i class="wpfont icon-search"></i></button>
			      	</span>
			    </div>
			</div>
			<div class="map-switch clearfix">
				<div class="map-switch-local" onclick="return2MainMap('${regionCode!}','${parentRegionCode!}')">地区模式</div>
			</div>
			<div class="wrap-1of1 bmap-made baidu-map" id="allmap"></div>
			<div class="map-right-part-wrap slimScrollBar-made clearfix" id="schoolDetailId">
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var map;
	var school_markers = new Array();
	
	function loadScript() {
        var script = document.createElement("script");
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=lm7EnvxLlwCTsBszhaU2NQkVLjwGDlZy&callback=initialize";
        document.body.appendChild(script);
    }

    function initialize() {  
		// 百度地图API功能
		map = new BMap.Map("allmap",{
			minZoom : 6,
			maxZoom : 15，
			enableMapClick: false
		});    // 创建Map实例
		map.centerAndZoom(new BMap.Point('${currentRegion.latitude!}','${currentRegion.longitude!}'), 12);  // 初始化地图,设置中心点坐标和地图级别
		map.setCurrentCity("${currentRegion.regionName!}");   
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		map.addControl(new BMap.NavigationControl({ type: BMAP_NAVIGATION_CONTROL_LARGE ,anchor: BMAP_ANCHOR_TOP_LEFT, offset: new BMap.Size(40, 250)}));
			map.setMapStyle({
			  	styleJson:[
			          {
			                "featureType": "poi",
			                "elementType": "all",
			                "stylers": {
								"visibility": "off"
			                }
			          }
				]
			});
		 var schools = new Array();
		  <#if schoolList?exists&&schoolList?size gt 0>
				<#list schoolList as school>	
					var schoolJSON={};
					schoolJSON.name="${school.schoolName!}";
					schoolJSON.color="#9F79EE",
					schoolJSON.latitude="${school.latitude!}";
					schoolJSON.longitude="${school.longitude!}";
					schools.push(schoolJSON);
		 	</#list>
		</#if>     
		addSchoollabel(schools);
		
	}
	
	function addSchoollabel(schools) {
		var pointArray = [];
		for(var i=0;i<schools.length;i++){
		    var point =new BMap.Point(schools[i].latitude,schools[i].longitude);
			pointArray.push(point);
		}
	    var optsArray = [];
	    for(var i=0;i<schools.length;i++){
			optsArray.push({});
		}
	    var labelArray = [];
	    var contentArray=[];
	    for(var i=0;i<schools.length;i++){
			contentArray.push(schools[i].name);
		}
		
	   for(var i=0;i<schools.length;i++){
	      var marker = new BMap.Marker(pointArray[i]);//按照地图点坐标生成标记
	      //marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
          labelArray[i] = new window.BMap.Label(contentArray[i], { offset: new window.BMap.Size(20, -10) });
          school_markers.push(marker);
          (function(){
    			var _marker = marker;
    			_marker.addEventListener("mouseover",function(){
    					$("#schoolDetailId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/schoolDetail?schoolId=",function() {
	
						});
    			   });
    			_marker.addEventListener("mouseout",function () {
						$("#schoolDetailId").empty();
				 });
				 _marker.addEventListener("click",function (e) {

				 });
    		})() 
	      map.addOverlay(marker);
	    }	  
	}

	function return2MainMap(regionCode,parentRegionCode){
		$("#transferId").load("${request.contextPath}/bigdata/frame/common/demo/baidumap/xz?regionCode="+regionCode+"&parentRegionCode="+parentRegionCode);
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
