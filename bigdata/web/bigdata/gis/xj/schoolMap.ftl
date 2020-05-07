<style type="text/css">
    .anchorBL a{
        display: none;
    }
    .anchorBL img{
        display: none;
    }
    .anchorBL span{
        display: none!important;
    }

</style>
 <div class="box box-default box-face js-height">
	<div class="map-search">
		<div class="input-group">
	      	<input type="text" class="form-control" id="schoolNameId" placeholder="请输入校名">
	      	<span class="input-group-btn">
	        	<button class="btn btn-blue"  id="searchBtn" type="button" onclick="schoolNameSearch();return ;"><i class="wpfont icon-search"></i></button>
	      	</span>
	    </div>
	</div>
     <div class="map-name">${regionName!}</div>
	<div class="wrap-full bmap-made baidu-map" id="allmap"></div>
	
	<div class="map-right-part-wrap clearfix">
		<div class="map-switch clearfix">
    		<div class="map-switch-local" onclick="return2MainMap('${regionCode!}','${parentRegionCode!}')">地区模式</div>
            <#if schoolName! !="">
    		<div id="originId" class="map-switch-arrow js-switch"  onclick="return2Origin();"><i class="wpfont icon-arrow-left"></i></div>
            </#if>
    	</div>
    	<div class="wrap-1of1 slimScrollBar-made"  id="schoolDetailId"></div>
	</div>
</div>
<script type="text/javascript">
	var map;
	var school_markers = new Array();
	
	 var iconMap=new Map();
	 iconMap.set("21","101");
	 iconMap.set("22","102");
	 iconMap.set("31","103");
	 iconMap.set("32","104");
	 iconMap.set("33","105");
	 iconMap.set("34","106");
	 iconMap.set("35","107");
	 iconMap.set("36","108");
	 iconMap.set("37","109");
	 iconMap.set("51","110");
	
	function loadScript() {
        var script = document.createElement("script");
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=${ak!}&callback=initialize";
        document.body.appendChild(script);
    }

    function initialize() {  
		// 百度地图API功能
		map = new BMap.Map("allmap",{
			minZoom : 6,
			maxZoom : 19,
			enableMapClick: false
		});    // 创建Map实例
        <#if schoolName! !="">
            <#if schoolList?exists&&schoolList?size gt 0>
                <#list schoolList as school>
                    <#if school_index ==0>
                        <#if schoolList?size == 1>
                            map.centerAndZoom(new BMap.Point('${school.latitude!}','${school.longitude!}'), 16);  // 初始化地图,设置中心点坐标和地图级别
                        <#else>
                            map.centerAndZoom(new BMap.Point('${school.latitude!}','${school.longitude!}'), 16);
                        </#if>
                    </#if>
                </#list>
            <#else>
                map.centerAndZoom(new BMap.Point('${currentRegion.latitude!}','${currentRegion.longitude!}'), 16);  // 初始化地图,设置中心点坐标和地图级别
            </#if>
        <#else>
            map.centerAndZoom(new BMap.Point('${currentRegion.latitude!}','${currentRegion.longitude!}'), 16);  // 初始化地图,设置中心点坐标和地图级别
        </#if>
        map.setCurrentCity("${currentRegion.regionName!}");
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		map.addControl(new BMap.NavigationControl({ type:BMAP_NAVIGATION_CONTROL_LARGE,anchor:BMAP_ANCHOR_TOP_LEFT, offset:new BMap.Size(40, 250)}));
        //map.setMapType(BMAP_HYBRID_MAP);
        map.addControl(new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP],anchor:BMAP_ANCHOR_BOTTOM_LEFT}));
		map.setMapStyle({
			  	styleJson:[
			          {
		                    "featureType": "road",
		                    "elementType": "all",
		                    "stylers": {
								"visibility": "off"
		                    }
			          },
			          {
		                    "featureType": "label",
		                    "elementType": "all",
		                    "stylers": {
		                    	"visibility": "off"
		                    }
			          },
			          {
		                    "featureType": "boundary",
		                    "elementType": "all",
		                    "stylers": {
		                    	"color": "#ffffff"
		                    }
			          },
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
					schoolJSON.name="${school.school_name!}";
					schoolJSON.color="#9F79EE",
					schoolJSON.latitude="${school.latitude!}";
					schoolJSON.longitude="${school.longitude!}";
					schoolJSON.type="${school.school_type!}";
					schoolJSON.address="${school.address!}";
					schoolJSON.linkman="${school.linkman!}";
					schoolJSON.telephone="${school.telephone!}";
					schools.push(schoolJSON);
		 	</#list>
		</#if>     
		addSchoollabel(schools);
	}
	
	function clearSchoolMarkers(){
		for(var i=0;i<school_markers.length;i++){
			map.removeOverlay(school_markers[i]);
		}
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
	   	  var icon=iconMap.get(schools[i].type.substring(0,2));
	   	  var marker;
	   	  if(!icon){
	   	  	//归为其他
       		var myIcon = new BMap.Icon("${request.contextPath}/bigdata/v3/static/images/map/110.png", new BMap.Size(24,32));
        	marker = new BMap.Marker(pointArray[i],{icon:myIcon});  // 创建标注
	   	  }else{
	     	var myIcon = new BMap.Icon("${request.contextPath}/bigdata/v3/static/images/map/"+icon+".png", new BMap.Size(24,32));
        	marker = new BMap.Marker(pointArray[i],{icon:myIcon});  // 创建标注
	      }
          labelArray[i] = new window.BMap.Label(contentArray[i], { offset: new window.BMap.Size(25, 0) });
          // labelArray[i].setStyle({
			// display: "none"
			// });
          marker.setLabel(labelArray[i]);//显示marker的title
          school_markers.push(marker);
          marker.schooName =schools[i].name;
          marker.latitude =schools[i].latitude;
          marker.longitude =schools[i].longitude;
          (function(){
    			var _marker = marker;
    			//var _label = labelArray[i];
				 _marker.addEventListener("click",function (e) {
                     this.closeInfoWindow();
                     $("#schoolDetailId").load("${request.contextPath}/bigdata/customization/xinjiang/common/gis/schoolDetail?schoolName="+_marker.schooName,{},function() {
                         //$("#allmap").addClass("active");
                         map.centerAndZoom(new BMap.Point(_marker.latitude,_marker.longitude), 19);
                     });
				 });
    		})() 
	      map.addOverlay(marker);
	    }	  
	}

	function return2MainMap(regionCode,parentRegionCode){
		$(".page-content").load("${request.contextPath}/bigdata/customization/xinjiang/common/gis/drill?regionCode="+regionCode+"&parentRegionCode="+parentRegionCode);
	}

	function return2Origin(){
		$(".page-content").load("${request.contextPath}/bigdata/customization/xinjiang/common/gis/school?regionCode=${regionCode!}&parentRegionCode=${parentRegionCode!}");
	}
	
	 //搜索学校
	function schoolNameSearch(){
        var keyword = $("#schoolNameId").val();
        $(".page-content").empty();
        $(".page-content").load("${request.contextPath}/bigdata/customization/xinjiang/common/gis/school?regionCode=${regionCode!}&parentRegionCode=${parentRegionCode!}&schoolName="+keyword);
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
