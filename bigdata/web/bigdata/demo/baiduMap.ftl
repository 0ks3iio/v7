<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
	body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
	.anchorBL{
	    display:none;
	}
	#up-map-div{
		width:300px;
		height:100px;
		top:30px;
		left:30px;
		position:absolute;
		z-index:9999;
		border:1px solid blue;
		background-color:#FFFFFF;
	}
</style>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=lm7EnvxLlwCTsBszhaU2NQkVLjwGDlZy"></script>
<title></title>
</head>
<body>
	<div id="allmap"></div>
	<!--
	<div id="up-map-div">
		悬浮div，让它悬浮在地图上面
	</div>-->
</body>
</html>
<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap",{
		minZoom : 6,
		maxZoom : 6，
		enableMapClick: false
	});    // 创建Map实例
	//map.centerAndZoom(new BMap.Point(87.62,43.82), 6);  // 初始化地图,设置中心点坐标和地图级别
	map.centerAndZoom(new BMap.Point(99.62,43.82), 6);  // 初始化地图,设置中心点坐标和地图级别
	map.setCurrentCity("乌鲁木齐");          // 设置地图显示的城市 此项是必须设置的
	//map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	//map.addControl(new BMap.NavigationControl({ type: BMAP_NAVIGATION_CONTROL_LARGE ,anchor: BMAP_ANCHOR_TOP_LEFT, offset: new BMap.Size(40, 250)}));
	map.disableDragging();  
	map.disableDoubleClickZoom(true);
    map.setMapStyle({
	  	styleJson:[
	          {
                    "featureType": "background",
                    "elementType": "all",
                    "stylers": {
                    	"color": "#212121"
                    }
	          },
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
                    	"color": "#212121"
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
    var cityJSONZ=[{"name":"乌鲁木齐市","color": "#9F79EE","latitude":"87.62 ","longitude":"43.82"},
             {"name":"克拉玛依市","color": "#9F79EE","latitude":"84.87","longitude":"45.60"},
			 {"name":"吐鲁番市","color": "#9F79EE","latitude":"89.17","longitude":"42.95"},
			 {"name":"哈密市","color": "#9F79EE","latitude":"93.52","longitude":"42.83"},
			 {"name":"阿克苏地区","color": "#9F79EE","latitude":"80.27","longitude":"41.17"},
			 {"name":"喀什地区","color": "#9F79EE","latitude":"77.08","longitude":"38.47"},
			 {"name":"和田地区","color": "#9F79EE","latitude":"79.92","longitude":"37.12"},
			 {"name":"塔城地区","color": "#9F79EE","latitude":"82.98","longitude":"46.75"},
			 {"name":"阿勒泰地区","color": "#9F79EE","latitude":"88.13","longitude":"47.85"},
			 {"name":"昌吉回族自治州","color": "#9F79EE","latitude":"88.30","longitude":"44.52"},
             {"name":"博尔塔拉蒙古自治州","color": "#9F79EE","latitude":"82.07","longitude":"44.90"},
             {"name":"巴音郭楞蒙古自治州","color": "#9F79EE","latitude":"86.15","longitude":"41.77"},
             {"name":"伊犁哈萨克自治州","color": "#9F79EE","latitude":"81.32","longitude":"43.92"},
             {"name":"石河子市","color": "#9F79EE","latitude":"86.03","longitude":"44.30"},
             {"name":"克孜勒苏柯尔克孜自治州","color": "#9F79EE","latitude":"76.45","longitude":"39.93"}
             ];
	var city = new Array();
	for(var i=0;i<cityJSONZ.length;i++){
		var cityJSON={};
		cityJSON.name=cityJSONZ[i].name;
		cityJSON.color=cityJSONZ[i].color;
		cityJSON.latitude=cityJSONZ[i].latitude;
		cityJSON.longitude=cityJSONZ[i].longitude;
		city.push(cityJSON);
	}
	for(var i=0;i<city.length;i++){
	    getBoundary(city[i]);
	}
	addlabel();
	var local = new BMap.LocalSearch(map, {
		renderOptions:{map: map}
	});
	local.search("学校");
	
	function getBoundary(city){
	    var bdary = new BMap.Boundary();
	    bdary.get(city.name, function (rs) {       //获取行政区域       
	       var count = rs.boundaries.length; //行政区域的点有多少个
			if (count === 0) {
				alert('未能获取当前输入行政区域');
				return ;
			}
          	var pointArray = [];
			for (var i = 0; i < count; i++) {
				var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor:city.color}); //建立多边形覆盖物
				map.addOverlay(ply);  //添加覆盖物
			} 
	  	});
	}  
	
	function addlabel() {
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
		
		var markers = new Array();
		

	   for(var i=0;i<city.length;i++){
	      var marker = new BMap.Marker(pointArray[i]);//按照地图点坐标生成标记
	     // marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		  var iw = createInfoWindow(i);//创建信息窗口对象
          labelArray[i] = new window.BMap.Label(contentArray[i], { offset: new window.BMap.Size(20, -10) });
          marker.setLabel(labelArray[i]);//显示marker的title
          labelArray[i].addEventListener("click",attribute);//创建label监听事件
          markers.push(marker);
          (function(){
    			var _iw = createInfoWindow(city[i]);
    			var _marker = marker;
    			_marker.addEventListener("mouseover",function(){
    				this.openInfoWindow(_iw);
    			   });
    			_marker.addEventListener("mouseout",function () {
					this.closeInfoWindow();
				 });
    		})() 
	      map.addOverlay(marker);
	    }	  
	}
	
	
	//触发监听事件
	//根据x,y坐标显示对应窗口
	function attribute(e){
		var p = e.target;
		var name = (p.getPosition().lng).toString()+(p.getPosition().lat).toString();
		window.parent.loadIframe("http://localhost:8099/bigdata/frame/common/demo/baidumap/xz?regionCode="+regionCode);
	}
	
	 function createInfoWindow(city){
    	var opts1 = {title : '<span style="font-size:20px;color:#0A8021">'+city.name+'</span>'};
        var iw = new BMap.InfoWindow("<div style='line-height:1.8em;font-size:12px;'><b>学生数: </b>"+ 100+ "</br><b>教师数: </b>"+200+"</br></a></div>", opts1);
        return iw;
    }
</script>
