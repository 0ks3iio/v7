<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta charset="UTF-8">
  <title>综合大屏</title>
  <meta name="description" content="" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css" />
  <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css" />
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css" />
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css" />
</head>

<body class="subsume-box">
  <!--主体 E-->
  <div class="subsume-body">
    <div class="subsume-map" id="container">

    </div>

    <div class="subsume-search-box">
      <div class="subsume-search-input">
        <div class="subsume-search-sou">
          <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/search-icon.png">
          <input type="text" placeholder="搜索" id="subsumesearch">
        </div>
        <ul class="subsume-search-menu">
          <#--<li><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"> <span>乌鲁木齐</span>第一中学</li>-->
          <#--<li><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"> <span>乌鲁木齐</span>第一中学</li>-->
          <#--<li><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"> <span>乌鲁木齐</span>第一中学</li>-->
        </ul>
      </div>
      <div class="subsume-shou active">
        <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/left-icon.png">
        <span>search</span>
      </div>
      <div>

      </div>
    </div>

    <div class="subsume-right">
      <div class="subsume-title">库尔勒综合大屏</div>
      <div class="subsume-right-body">
        <div class="subsume-right-item subsume-right-one">
          <div class="subsume-ritem-title">
            <div class="subsume-ritem-back">总体指标</div>
            <div class="subsume-ritem-line"></div>
            <div class="subsume-ritem-bian">
              <div></div>
              <div></div>
            </div>
          </div>
          <div class="subsume-ritem-body">
            <div class="subsume-ritem-norm">
              <div class="subsume-norm-item">
                <div class="subsume-norm-title">学生总数</div>
                <div class="subsume-norm-num"><b id="student_num" class="norm-num" data-from="0" data-to="0"
                    data-speed="2000">0</b></div>
                <div class="subsume-norm-title">班级总数</div>
                <div class="subsume-norm-num"><b id="class_num" class="norm-num" data-from="0" data-to="0"
                    data-speed="2000">0</b></div>
              </div>
              <div class="subsume-norm-line-item">
                <div class="subsume-norm-line"></div>
                <div class="subsume-norm-line"></div>
              </div>
              <div class="subsume-norm-item">
                <div class="subsume-norm-title">教师总数</div>
                <div class="subsume-norm-num"><b id="teacher_num" class="norm-num" data-from="0" data-to="0"
                    data-speed="2000">0</b></div>
                <div class="subsume-norm-title">平均班级人数</div>
                <div class="subsume-norm-num"><b id="aveclass_peop_num" class="norm-num" data-from="0" data-to="0"
                    data-speed="2000">0</b></div>
              </div>
              <div class="subsume-norm-line-item">
                <div class="subsume-norm-line"></div>
                <div class="subsume-norm-line"></div>
              </div>
              <div class="subsume-norm-item">
                <div class="subsume-norm-title">设备总数</div>
                <div class="subsume-norm-num"><b id="deviceCount" class="norm-num" data-from="0" data-to="0"
                    data-speed="2000">0</b></div>
                <div class="subsume-norm-title">教室人均面积</div>
                <div class="subsume-norm-num"><b id="aveclass_instrument_num" class="norm-num" data-from="0" data-to="0"
                    data-speed="2000">0</b></div>
              </div>
            </div>
          </div>
        </div>

        <div class="subsume-right-item subsume-right-two">
          <div class="subsume-ritem-title">
            <div unit-id="" id="bindUnitId" class="subsume-ritem-back">各科目教师人数</div>
            <div class="subsume-ritem-line"></div>
            <div class="subsume-ritem-bian">
              <div></div>
              <div></div>
            </div>
            <div class="subsume-btn-group smh" style="width:220px;">
              <div id="small" class="subsume-btn-item active">小学</div>
              <div id="mid" class="subsume-btn-item">初中</div>
              <div id="high" class="subsume-btn-item">高中</div>
            </div>
          </div>
          <div class="subsume-ritem-body subsume-person-box">
            <div class="subsume-person-chart">
              <div class="subsume-person-title">各科教师数</div>
              <div class="subsume-person-tu" id="person-chart"></div>
            </div>
            <div class="subsume-person-right">
              <div class="subsume-person-title">各科师生比</div>
              <div class="subsume-person-tubiao" id="subsume-person-tubiao">
                <div class="subsume-person-li">
                  <span>语文</span>
                  <span>1:20</span>
                </div>
              </div>
            </div>
          </div>
        </div>


        <div class="subsume-right-item subsume-right-three">
          <div class="subsume-ritem-title">
            <div class="subsume-ritem-back">教师工龄结构</div>
            <div class="subsume-ritem-line"></div>
            <div class="subsume-ritem-bian">
              <div></div>
              <div></div>
            </div>
            <div class="subsume-btn-group teacherAge">
              <div class="subsume-btn-item active">工龄</div>
              <div class="subsume-btn-item">年龄</div>
            </div>
          </div>
          <div class="subsume-ritem-body" id="seniority-chart">
          </div>
        </div>
      </div>
    </div>


    <div class="subsume-bottom">
      <div class="subsume-bottom-body">
        <div class="subsume-right-item subsume-bottom-one">
          <div class="subsume-ritem-title">
            <div class="subsume-ritem-back">师生性别/民汉比</div>
            <div class="subsume-ritem-line"></div>
            <div class="subsume-ritem-bian">
              <div></div>
              <div></div>
            </div>
            <div class="subsume-btn-group teacherAndStudent">
              <div class="subsume-btn-item active">教师</div>
              <div class="subsume-btn-item">学生</div>
            </div>
          </div>

          <div class="subsume-boitem-left">
            <div class="subsume-teacher-chart">
              <div class="subsume-person-title">性别分布</div>
              <div class="subsume-teacher-tu">
                <div class="subsume-teacher-tuli subsume-tetu-one">
                  <div class="subsume-man">
                  </div>
                  <div class="subsume-woman">
                  </div>
                </div>
                <div class="subsume-teacher-tuli subsume-tetu-two">
                  <span id="manBFB"></span>
                  <span id="womanBFB"></span>
                </div>
              </div>
            </div>

            <div class="subsume-teacher-chart">
              <div class="subsume-person-title">民汉比例</div>
              <div class="subsume-teacher-tu" id="ratio-chart"></div>
            </div>
          </div>
        </div>

        <div class="subsume-right-item subsume-bottom-two">
          <div class="subsume-ritem-title">
            <div class="subsume-ritem-back">设备总览</div>
            <div class="subsume-ritem-line"></div>
            <div class="subsume-ritem-bian">
              <div></div>
              <div></div>
            </div>
          </div>
          <div class="subsume-equ">
            <div class="subsume-equ-body">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
  <script src="https://webapi.amap.com/maps?v=1.4.15&key=da1bc644fff023714ecda46aca2448a4"></script>
  <script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>
  <script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
  <script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
  <script>
    var teacherAge = JSON.parse('${teacherAge!}')[0];
    var studentData = JSON.parse('${studentData!}')[0];
    var teacherData = JSON.parse('${teacherData!}')[0];
    var deviceData = JSON.parse('${deviceData!}');
    var courseData = JSON.parse('${courseData!}');
    var deviceCount = 0;
    if (!jQuery.isEmptyObject(studentData)){
        $("#student_num").attr('data-to',studentData.student_num);
        $("#class_num").attr('data-to',studentData.class_num);
        $("#aveclass_peop_num").attr('data-to',studentData.aveclass_peop_num);
        $("#aveclass_instrument_num").attr('data-to',studentData.aveclass_instrument_num);
    }
    if (!jQuery.isEmptyObject(teacherData)){
        $("#teacher_num").attr('data-to',teacherData.teacher_number);
    }
    if (!jQuery.isEmptyObject(deviceData)){
        $('.subsume-equ-body').empty();
        for (let i = 0; i < deviceData.length; i++) {
          if (typeof (deviceData[i].instrument_count)=="undefined"){
            deviceData[i].instrument_count = 0;
          }
            $('.subsume-equ-body').append('<div class="subsume-equ-li">\n' +
                '                <div class="subsume-equ-li1">'+deviceData[i].devicetype_content+'</div>\n' +
                '                <div class="subsume-equ-li2">'+deviceData[i].instrument_count+'</div>\n' +
                '              </div>');
            deviceCount +=deviceData[i].instrument_count;
        }
    }
    $("#deviceCount").attr('data-to',deviceCount);
    if (!jQuery.isEmptyObject(courseData)) {
      $("#bindUnitId").attr('unit-id',courseData[0].unit_id);
      $('#subsume-person-tubiao').empty();
      for (let i = 0; i < courseData.length; i++) {
        $('#subsume-person-tubiao').append('<div class="subsume-person-li">\n' +
                '                  <span>'+courseData[i].subject_name+'</span>\n' +
                '                  <span>1:'+Math.round(courseData[i].student_num/courseData[i].teacher_num) +'</span>\n' +
                '                </div>');
      }
    }else {
      $('#subsume-person-tubiao').empty();
    }
    $(function () {
      //进场动画
      setTimeout(function () {
        $(".subsume-right").css("right", "0");
        $(".subsume-bottom").css("left", "0");
      }, 500)

      //搜索框
      $(document).click(function (event) {
        var eo = $(event.target);
        if (eo.attr('class') != 'subsume-search-menu' && !eo.parents('.subsume-search-input').length)
          $('.subsume-search-menu').hide();
      });

      $("#subsumesearch").bind("input propertychange", function () {
        $.ajax({
          url: '${request.contextPath}/bigdata/datav/fixed/screen/searchSchoolName?name='+$('#subsumesearch').val(),
          type: 'GET',
          success: function (result) {
            if (!result.success) {
              $('.subsume-search-menu').empty();
            } else {
              $('.subsume-search-menu').empty();
              for (var i=0;i<result.data.length;i++){
                $('.subsume-search-menu').append('<li id='+result.data[i].unit_id+'><img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/subsume/positioning.png"><span>'+result.data[i].unit_name+'</span></li>');
              }
            }
          }
        });
        $('.subsume-search-menu').show();
      });



      //收起，打开
      $(".subsume-shou").click(function () {
        $(".subsume-shou").toggleClass("active");
        $(".subsume-search-box").toggleClass("active");
      })

      //按钮组
      $(".subsume-btn-item").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
      })

      gunfawen();
    })
    var map;
    var markers = [];
    var tiaodong = null;
    function mapInit() {
      var latitude;
      var longitude;
      var unitName;
      $("body").on('click', '.subsume-search-menu li', function (e) {
        var unitId = $(this).attr('id');
        $('#subsumesearch').val($(this).find('span').html());
        var section = 0;
        var params = {
            id:unitId,
            section:section
        }
          $.ajax({
              url: '${request.contextPath}/bigdata/datav/fixed/screen/kelData',
              type: 'GET',
              data:params,
              success: function (result) {
                  if (!result.success) {
                      alert("获取数据失败");
                  } else {
                      teacherAge = result.data.teacherAge[0];
                      studentData = result.data.studentData[0];
                      teacherData = result.data.teacherData[0];
                      deviceData = result.data.deviceData;
                      courseData = result.data.courseData;
                      var latiAndlongit = result.data.latiAndlongit[0];
                      if (!jQuery.isEmptyObject(teacherAge)) {
                        option2.series[0].data=[teacherAge.workyear_5,teacherAge.workyear_6_10,teacherAge.workyear_11_15,
                          teacherAge.workyear_16_20,teacherAge.workyear_21_25,teacherAge.workyear_26_30,
                          teacherAge.workyear_31_35,teacherAge.workyear_36_40,teacherAge.workyear_41_45,
                          teacherAge.workyear_46];
                      }else {
                        option2.series[0].data=[0,0,0, 0,0,0,0,0,0,0];
                      }
                      myChart2.setOption(option2);
                      var deviceCountEve = 0;
                      if (!jQuery.isEmptyObject(studentData)){
                          $("#student_num").attr('data-to',studentData.student_num);
                          $("#class_num").attr('data-to',studentData.class_num);
                          $("#aveclass_peop_num").attr('data-to',studentData.aveclass_peop_num);
                          $("#aveclass_instrument_num").attr('data-to',studentData.aveclass_instrument_num);
                      }else {
                          $("#student_num").attr('data-to',0);
                          $("#class_num").attr('data-to',0);
                          $("#aveclass_peop_num").attr('data-to',0);
                          $("#aveclass_instrument_num").attr('data-to',0);
                      }
                      if (!jQuery.isEmptyObject(teacherData)){
                          $("#teacher_num").attr('data-to',teacherData.teacher_number);
                      }else {
                          $("#teacher_num").attr('data-to',0);
                      }
                      if (!jQuery.isEmptyObject(deviceData)){
                          $('.subsume-equ-body').empty();
                          for (let i = 0; i < deviceData.length; i++) {
                            if (typeof (deviceData[i].instrument_count)=="undefined"){
                              deviceData[i].instrument_count = 0;
                            }
                              $('.subsume-equ-body').append('<div class="subsume-equ-li">\n' +
                                  '                <div class="subsume-equ-li1">'+deviceData[i].devicetype_content+'</div>\n' +
                                  '                <div class="subsume-equ-li2">'+deviceData[i].instrument_count+'</div>\n' +
                                  '              </div>');
                              deviceCountEve +=deviceData[i].instrument_count;
                          }
                      }else {
                          $('.subsume-equ-body').empty();
                      }
                      $("#deviceCount").attr('data-to',deviceCountEve);
                      $('.teacherAndStudent .subsume-btn-item').eq(0).click();

                    $('#subsume-person-tubiao').empty();
                    if (!jQuery.isEmptyObject(courseData)) {
                      $("#bindUnitId").attr('unit-id',courseData[0].unit_id);
                      for (let i = 0; i < courseData.length; i++) {
                        $('#subsume-person-tubiao').append('<div class="subsume-person-li">\n' +
                                '                  <span>'+courseData[i].subject_name+'</span>\n' +
                                '                  <span>1:'+Math.round(courseData[i].student_num/courseData[i].teacher_num) +'</span>\n' +
                                '                </div>');
                      }
                      option1.xAxis.data = [courseData[0].subject_name,courseData[1].subject_name,courseData[2].subject_name,
                        courseData[3].subject_name,courseData[4].subject_name,courseData[5].subject_name,courseData[6].subject_name
                        ,courseData[7].subject_name,courseData[8].subject_name];
                      option1.series[1].data = [courseData[0].teacher_num,courseData[1].teacher_num,courseData[2].teacher_num,
                        courseData[3].teacher_num,courseData[4].teacher_num,courseData[5].teacher_num,courseData[6].teacher_num
                        ,courseData[7].teacher_num,courseData[8].teacher_num];
                        var yMax1 = courseData[0].teacher_num;

                      var dataShadow = [];

                      for (var i = 0; i < data1.length; i++) {
                        dataShadow.push(yMax1);
                      }
                      option1.series[0].data=dataShadow;
                    }else {
                      $('#subsume-person-tubiao').empty();
                      $("#bindUnitId").attr('unit-id','');
                      option1.xAxis.data = [];
                      option1.series[1].data = [];
                    }
                    myChart1.setOption(option1);
                  }
                  if (typeof (latiAndlongit.longitude)!="undefined") {
                    latitude = latiAndlongit.latitude;
                    longitude = latiAndlongit.longitude;
                    unitName = latiAndlongit.unit_name;
                  }else {
                    latitude = 86.181494;
                    longitude = 41.732373;
                    unitName = latiAndlongit.unit_name;
                  }
                $("#small").removeClass('active');
                $("#mid").removeClass('active');
                $("#high").removeClass('active');
                $("#small").addClass('active');
                qiehuan(latitude,longitude,unitName);
              }
          });
        $(".subsume-search-menu").hide();

      });

      map = new AMap.Map('container', {
        resizeEnable: true,
        rotateEnable: true,
        pitchEnable: false,
        zoom: 16,
        pitch: 30,
        rotation: -30,
        viewMode: '3D',//开启3D视图,默认为关闭
        buildingAnimation: true,//楼块出现是否带动画

        expandZoomRange: true,//	是否支持可以扩展最大缩放级别,和zooms属性配合使用
        zooms: [3, 20],
        //skyColor: '#333333',
        //setMapStyle: 'amap://styles/78cc394aba7d584a9c40c35d08ed63bd'
        center: [86.181494, 41.732373]
      });


      markers = [];
      //获取当前中心点


      function biaodian(unitName) {
        clearInterval(tiaodong);
        tiaodong = null;
        var center = map.getCenter();
        var lat = center.lat;
        var lng = center.lng;
        if (typeof (unitName)!="undefined"){
          var marker = new AMap.Marker({
            map: map,
            position: new AMap.LngLat(lng, lat),
            anchor: 'bottom-left',
            icon: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png',
            offset: new AMap.Pixel(-13, -30),
            content: `<div class="subsume-dizhi-tip">
              <div class="subsume-dizhi-title">`+unitName+`</div>
            <div class="subsume-dizhi-body">
              <div class="subsume-zhu"></div>
              <div class="subsume-quan1">
                <div class="subsume-quan2">
                  <div class="subsume-quan3">

                  </div>
                </div>
              </div>
            </div>
          </div>`
          });
        }else {
          var marker = new AMap.Marker({
            map: map,
            position: new AMap.LngLat(lng, lat),
            anchor: 'bottom-left',
            icon: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png',
            offset: new AMap.Pixel(-13, -30),
            content: `<div class="subsume-dizhi-tip">
              <div class="subsume-dizhi-title">新疆库尔勒市教育局</div>
            <div class="subsume-dizhi-body">
              <div class="subsume-zhu"></div>
              <div class="subsume-quan1">
                <div class="subsume-quan2">
                  <div class="subsume-quan3">

                  </div>
                </div>
              </div>
            </div>
          </div>`
          });
        }

        markers.push(marker);
        tiaodong = setInterval(function () {
          $(".subsume-dizhi-tip").css("transition", "bottom 0.5s")
          $(".subsume-dizhi-tip").css("bottom", "0");
          setTimeout(function () {
            $(".subsume-dizhi-tip").css("bottom", "50px");
          }, 500)
        }, 1000)
      }


      //定位
      // AMap.plugin('AMap.Geolocation', function () {
      //   var geolocation = new AMap.Geolocation({
      //     enableHighAccuracy: true,//是否使用高精度定位，默认:true
      //     timeout: 10000,          //超过10秒后停止定位，默认：5s
      //     showButton: false
      //     //zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点

      //   });
      //   map.addControl(geolocation);
      //   geolocation.getCurrentPosition(function (status, result) {
      //     if (status == 'complete') {
      //       onComplete(result)
      //       setTimeout(function () {
      //         map.setZoom(18);
      //       }, 2000)
      //     } else {
      //       onError(result)
      //     }
      //   });

      // });
      map.setMapStyle('amap://styles/97c27b303dfc26554b76050bc191d3d8');
      setTimeout(function () {
        map.setZoom(18);
        biaodian();
          $('.teacherAndStudent .subsume-btn-item').eq(0).click();
      }, 1500)

      //解析定位结果
      function onComplete(data) {
        console.log(data)
      }
      //解析定位错误信息
      function onError(data) {
        console.log(data)
      }

      //切换位置
      function qiehuan(latitude,longitude,unitName) {
        var lng = latitude; //经度范围[121.138398, 121.728226]
        var lat = longitude; //纬度范围[30.972688, 31.487611]
        map.setZoom(14);
        map.clearMap();

        $(".subsume-right").css("right", "-33.33333%");
        $(".subsume-bottom").css("left", "-66.66666%");

        setTimeout(function () {
          map.setCenter([lng, lat]); //设置地图中心点
          map.setZoom(18);
          setTimeout(function () {
            biaodian(unitName);
          }, 1000)
          $(".subsume-right").css("right", "0");
          $(".subsume-bottom").css("left", "0");
          $("#subsume-person-tubiao").scrollTop(0);
          $("#subsume-equ-body").css("top", "0");
          //修改数据，图表
          fontEffects();

        }, 1500)
      }
    }
    mapInit();

    fontEffects();
    function fontEffects() {
      $.fn.countTo = function (options) {
        options = options || {};
        return $(this).each(function () {
          // set options for current element
          var settings = $.extend({}, $.fn.countTo.defaults, {
            from: $(this).data('from'),
            to: $(this).attr('data-to'),
            speed: $(this).data('speed'),
            refreshInterval: $(this).data('refresh-interval'),
            decimals: $(this).data('decimals')
          }, options);

          // how many times to update the value, and how much to increment the value on each update
          var loops = Math.ceil(settings.speed / settings.refreshInterval),
            increment = (settings.to - settings.from) / loops;

          // references & variables that will change with each update
          var self = this,
            $self = $(this),
            loopCount = 0,
            value = settings.from,
            data = $self.data('countTo') || {};

          $self.data('countTo', data);

          // if an existing interval can be found, clear it first
          if (data.interval) {
            clearInterval(data.interval);
          }
          data.interval = setInterval(updateTimer, settings.refreshInterval);

          // initialize the element with the starting value
          render(value);

          function updateTimer() {
            value += increment;
            loopCount++;

            render(value);

            if (typeof (settings.onUpdate) == 'function') {
              settings.onUpdate.call(self, value);
            }

            if (loopCount >= loops) {
              // remove the interval
              $self.removeData('countTo');
              clearInterval(data.interval);
              value = settings.to;

              if (typeof (settings.onComplete) == 'function') {
                settings.onComplete.call(self, value);
              }
            }
          }

          function render(value) {
            //value=toValue;
            var formattedValue = settings.formatter.call(self, value, settings);
            $self.html(formattedValue);
          }

        });
      };

      $.fn.countTo.defaults = {
        from: 0,               // the number the element should start at
        to: 0,                 // the number the element should end at
        speed: 1000,           // how long it should take to count between the target numbers
        refreshInterval: 100,  // how often the element should be updated
        decimals: 0,           // the number of decimal places to show
        formatter: formatter,  // handler for formatting the value before rendering
        onUpdate: null,        // callback method for every time the element is updated
        onComplete: null       // callback method for when the element finishes updating
      };

      function formatter(value, settings) {
        return value.toFixed(settings.decimals);
      }

      // custom formatting example
      $('.subsume-norm-num .norm-num').data('countToOptions', {
        formatter: function (value, options) {
          return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, '');
        }
      });

      // start all the timers
      $('.subsume-norm-num .norm-num').each(count);

      function count(options) {
        var $this = $(this);
        options = $.extend({}, options || {}, $this.data('countToOptions') || {});
        $this.countTo(options);
      }
    }

    //图表
    var arr = [];
    function resizeChart() {
      for (var i = 0; i < arr.length; i++) {
        arr[i].resize()
      }
    }

    //各科目教师人数
    var myChart1 = echarts.init(document.getElementById('person-chart'));
    arr.push(myChart1);

    if (!jQuery.isEmptyObject(courseData)) {
      var dataAxis1 = [courseData[0].subject_name,courseData[1].subject_name,courseData[2].subject_name,
        courseData[3].subject_name,courseData[4].subject_name,courseData[5].subject_name,courseData[6].subject_name
        ,courseData[7].subject_name,courseData[8].subject_name];
      //柱子要按照收发文数量进行排次
      var data1 = [courseData[0].teacher_num,courseData[1].teacher_num,courseData[2].teacher_num,
        courseData[3].teacher_num,courseData[4].teacher_num,courseData[5].teacher_num,courseData[6].teacher_num
        ,courseData[7].teacher_num,courseData[8].teacher_num];
    }else {
      var dataAxis1 = [];
      var data1 = [];
    }

    if (!jQuery.isEmptyObject(courseData)) {
      var yMax1 = courseData[0].teacher_num;
    }else {
      var yMax1 = 500;
    }

    var dataShadow = [];

    for (var i = 0; i < data1.length; i++) {
      dataShadow.push(yMax1);
    }

    var option1 = {
      xAxis: {
        data: dataAxis1,
        axisLabel: {
          textStyle: {
            color: '#8C9DBF',
            fontSize: 14
          },
          interval: 0,//标签设置为全部显示
          rotate:45,//倾斜度 -90 至 90 默认为0
          margin:2,
          formatter: function (value, index) {
            var v = value.substring(0, 3) + '...';
            return value.length > 3 ? v : value;
          }
        },
        axisTick: {
          show: false
        },
        axisLine: {
          show: false
        },
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          textStyle: {
            color: '#8C9DBF',
            fontSize: 14
          },
          interval: 0,//标签设置为全部显示
        },
        axisTick: {
          show: false
        },
        axisLine: {
          show: false
        },
        splitLine: {
          show: false
        }
      },
      grid: {
        top: 20,
        left: 50,
        right: 10,
        bottom: 40,
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: '{b}<br/>{c1}'
      },
      series: [
        { // For shadow
          type: 'bar',
          itemStyle: {
            normal: { color: '#303c53' },
          },
          barGap: '-100%',
          barCategoryGap: '40%',
          data: dataShadow,
          animation: false
        },
        {
          type: 'bar',
          itemStyle: {
            normal: {
              color: function (params) {
                return (params.dataIndex % 2 == 0) ?
                  '#DA2A3F' :
                  '#0C94F2';
              }
            },
          },
          data: data1,
          animationDelay: 1500
        }
      ]
    };

    myChart1.setOption(option1);

    //各科目教师人数
    var myChart2 = echarts.init(document.getElementById('seniority-chart'));
    arr.push(myChart2);

    var dataAxis2 = ['0-5', '6-10', '11-15', '16-20', '21-25', '26-30','31-35','36-40','41-45','46+'];
    //柱子要按照收发文数量进行排次
    var data2 = [teacherAge.workyear_5,teacherAge.workyear_6_10,teacherAge.workyear_11_15,
        teacherAge.workyear_16_20,teacherAge.workyear_21_25,teacherAge.workyear_26_30,
        teacherAge.workyear_31_35,teacherAge.workyear_36_40,teacherAge.workyear_41_45,
        teacherAge.workyear_46];

    var option2 = {
      color: '#0C94F2',
      xAxis: {
        data: dataAxis2,
        axisLabel: {
          textStyle: {
            color: '#8C9DBF',
            fontSize: 14
          },
          interval: 0,//标签设置为全部显示
        },
        axisTick: {
          show: false
        },
        axisLine: {
          show: false
        },
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          textStyle: {
            color: '#8C9DBF',
            fontSize: 14
          },
          interval: 0,//标签设置为全部显示
        },
        axisTick: {
          show: false
        },
        axisLine: {
          show: false
        },
        splitLine: {
          show: false
        }
      },
      grid: {
        top: 20,
        left: 40,
        right: 10,
        bottom: 30,
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'line'
        },
      },
      series: [
        {
          type: 'line',
          areaStyle: {
            normal: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: '#16637e' // 0% 处的颜色
                }, {
                  offset: 1, color: '#112f4e' // 100% 处的颜色
                }],
              }
            }
          },
          data: data2,
          smooth: true,
          showSymbol: false,
          animationDelay: 1500
        }
      ]
    };

    myChart2.setOption(option2);

    //民汉比例
    var myChart3 = echarts.init(document.getElementById('ratio-chart'));
    arr.push(myChart3);

    var option3 = {
      color: ['#0C94F2', '#DA2A3F'],
      legend: {
        textStyle: {
          color: '#8C9DBF'
        },
        bottom: 10,
        data: ['汉族', '少数民族'],
        itemWidth: 13,
        itemHeight: 13,
      },
      tooltip: {
        trigger: 'item'
      },
      grid: {
        top: '3%',
        left: '3%',
        right: '4%',
        bottom: '10'
      },
      series: [
        {
          name: '人数',
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '40%'],
          label: {
            normal: {
              show: false
            }
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          data: [{ name: '汉族', value: 0 },
          { name: '少数民族', value: 0 }],
          animationDelay: 1500
        }
      ]
    };

    myChart3.setOption(option3);

    $('.teacherAndStudent .subsume-btn-item').eq(0).click(function () {
      if (!jQuery.isEmptyObject(teacherData)) {
        option3.series[0].data = [{ name: '汉族', value: teacherData.teacher_mz_01_num },
          { name: '少数民族', value: teacherData.teacher_mz_other_num }];
        myChart3.setOption(option3);
        $("#manBFB").html(Math.round(teacherData.man*100)+'%');
        $("#womanBFB").html(Math.round((1-teacherData.man)*100)+'%');
        $(".subsume-man").empty();
        $(".subsume-woman").empty();
        for (let i = 0; i <Math.round(24*teacherData.man) ; i++) {
          $(".subsume-man").append('<div class="subsume-man-li"></div>');
        }
        for (let i = 0; i <Math.round(24*(1-teacherData.man)) ; i++) {
          $(".subsume-woman").append('<div class="subsume-man-li"></div>');
        }
      }else {
        option3.series[0].data = [{ name: '汉族', value: 0 },
          { name: '少数民族', value: 0 }];
        myChart3.setOption(option3);
        $("#manBFB").html('');
        $("#womanBFB").html('');
        $(".subsume-man").empty();
        $(".subsume-woman").empty();
      }

    });

    $('.teacherAndStudent .subsume-btn-item').eq(1).click(function () {
      if (!jQuery.isEmptyObject(studentData)) {
        option3.series[0].data = [{ name: '汉族', value: studentData.student_mz_01_num },
          { name: '少数民族', value: studentData.student_mz_other_num }];
        myChart3.setOption(option3);
        $("#manBFB").html(Math.round(studentData.man*100)+'%');
        $("#womanBFB").html(Math.round((1-studentData.man)*100)+'%');
        $(".subsume-man").empty();
        $(".subsume-woman").empty();
        for (let i = 0; i <Math.round(24*studentData.man) ; i++) {
          $(".subsume-man").append('<div class="subsume-man-li"></div>');
        }
        for (let i = 0; i <Math.round(24*(1-studentData.man)) ; i++) {
          $(".subsume-woman").append('<div class="subsume-man-li"></div>');
        }
      }else {
        option3.series[0].data = [{ name: '汉族', value: 0 },
          { name: '少数民族', value: 0 }];
        myChart3.setOption(option3);
        $("#manBFB").html('');
        $("#womanBFB").html('');
        $(".subsume-man").empty();
        $(".subsume-woman").empty();
      }

    });

    $('.teacherAge .subsume-btn-item').eq(0).click(function () {
        option2.xAxis.data = ['0-5', '6-10', '11-15', '16-20', '21-25', '26-30','31-35','36-40','41-45','46+'];
      if (!jQuery.isEmptyObject(teacherAge)) {
        option2.series[0].data=[teacherAge.workyear_5,teacherAge.workyear_6_10,teacherAge.workyear_11_15,
          teacherAge.workyear_16_20,teacherAge.workyear_21_25,teacherAge.workyear_26_30,
          teacherAge.workyear_31_35,teacherAge.workyear_36_40,teacherAge.workyear_41_45,
          teacherAge.workyear_46];
      }else {
        option2.series[0].data=[0,0,0, 0,0,0,0,0,0,0];
      }
      myChart2.setOption(option2);
    });

    $('.teacherAge .subsume-btn-item').eq(1).click(function () {
        option2.xAxis.data = ['0-24', '25-29', '30-34', '35-39', '40-44', '45-49','50-54','55-59','60-64','65+'];
      if (!jQuery.isEmptyObject(teacherAge)) {
        option2.series[0].data=[teacherAge.age_24,teacherAge.age_25_29,teacherAge.age_30_34,
          teacherAge.age_35_39,teacherAge.age_40_44,teacherAge.age_45_49,
          teacherAge.age_50_54,teacherAge.age_55_59,teacherAge.age_60_64,
          teacherAge.age_65];
      }else {
        option2.series[0].data=[0,0,0, 0,0,0,0,0,0,0];
      }
        myChart2.setOption(option2);
    });

    $('.smh .subsume-btn-item').eq(0).click(function () {
      var params = {
        unitId:$("#bindUnitId").attr('unit-id'),
        section:1
      };
      $.ajax({
        url: '${request.contextPath}/bigdata/datav/fixed/screen/teacherNumber',
        type: 'POST',
        data:params,
        success: function (result) {
          if (!result.success) {
            $('#subsume-person-tubiao').empty();
            option1.xAxis.data = [];
            option1.series[1].data = [];
            myChart1.setOption(option1);
          } else {
            $('#subsume-person-tubiao').empty();
            if (!jQuery.isEmptyObject(result.data)) {
              for (let i = 0; i < result.data.length; i++) {
                $('#subsume-person-tubiao').append('<div class="subsume-person-li">\n' +
                        '                  <span>'+result.data[i].subject_name+'</span>\n' +
                        '                  <span>1:'+Math.round(result.data[i].student_num/result.data[i].teacher_num) +'</span>\n' +
                        '                </div>');
              }
              option1.xAxis.data = [result.data[0].subject_name,result.data[1].subject_name,result.data[2].subject_name,
                result.data[3].subject_name,result.data[4].subject_name,result.data[5].subject_name,result.data[6].subject_name
                ,result.data[7].subject_name,result.data[8].subject_name];
              option1.series[1].data = [result.data[0].teacher_num,result.data[1].teacher_num,result.data[2].teacher_num,
                result.data[3].teacher_num,result.data[4].teacher_num,result.data[5].teacher_num,result.data[6].teacher_num
                ,result.data[7].teacher_num,result.data[8].teacher_num];
            }else {
              $('#subsume-person-tubiao').empty();
              option1.xAxis.data = [];
              option1.series[1].data = [];
            }
            myChart1.setOption(option1);
          }
        }
      });
    });

    $('.smh .subsume-btn-item').eq(1).click(function () {
      var params = {
        unitId:$("#bindUnitId").attr('unit-id'),
        section:2
      };
      $.ajax({
        url: '${request.contextPath}/bigdata/datav/fixed/screen/teacherNumber',
        type: 'POST',
        data:params,
        success: function (result) {
          if (!result.success) {
            $('#subsume-person-tubiao').empty();
            option1.xAxis.data = [];
            option1.series[1].data = [];
            myChart1.setOption(option1);
          } else {
            $('#subsume-person-tubiao').empty();
            if (!jQuery.isEmptyObject(result.data)) {
              for (let i = 0; i < result.data.length; i++) {
                $('#subsume-person-tubiao').append('<div class="subsume-person-li">\n' +
                        '                  <span>'+result.data[i].subject_name+'</span>\n' +
                        '                  <span>1:'+Math.round(result.data[i].student_num/result.data[i].teacher_num) +'</span>\n' +
                        '                </div>');
              }
              option1.xAxis.data = [result.data[0].subject_name,result.data[1].subject_name,result.data[2].subject_name,
                result.data[3].subject_name,result.data[4].subject_name,result.data[5].subject_name,result.data[6].subject_name
                ,result.data[7].subject_name,result.data[8].subject_name];
              option1.series[1].data = [result.data[0].teacher_num,result.data[1].teacher_num,result.data[2].teacher_num,
                result.data[3].teacher_num,result.data[4].teacher_num,result.data[5].teacher_num,result.data[6].teacher_num
                ,result.data[7].teacher_num,result.data[8].teacher_num];
            }else {
              $('#subsume-person-tubiao').empty();
              option1.xAxis.data = [];
              option1.series[1].data = [];
            }
            myChart1.setOption(option1);
          }
        }
      });
    });

    $('.smh .subsume-btn-item').eq(2).click(function () {
      var params = {
        unitId:$("#bindUnitId").attr('unit-id'),
        section:3
      };
      $.ajax({
        url: '${request.contextPath}/bigdata/datav/fixed/screen/teacherNumber',
        type: 'POST',
        data:params,
        success: function (result) {
          if (!result.success) {
            $('#subsume-person-tubiao').empty();
            option1.xAxis.data = [];
            option1.series[1].data = [];
            myChart1.setOption(option1);
          } else {
            $('#subsume-person-tubiao').empty();
            if (!jQuery.isEmptyObject(result.data)) {
              for (let i = 0; i < result.data.length; i++) {
                $('#subsume-person-tubiao').append('<div class="subsume-person-li">\n' +
                        '                  <span>'+result.data[i].subject_name+'</span>\n' +
                        '                  <span>1:'+Math.round(result.data[i].student_num/result.data[i].teacher_num) +'</span>\n' +
                        '                </div>');
              }
              option1.xAxis.data = [result.data[0].subject_name,result.data[1].subject_name,result.data[2].subject_name,
                result.data[3].subject_name,result.data[4].subject_name,result.data[5].subject_name,result.data[6].subject_name
                ,result.data[7].subject_name,result.data[8].subject_name];
              option1.series[1].data = [result.data[0].teacher_num,result.data[1].teacher_num,result.data[2].teacher_num,
                result.data[3].teacher_num,result.data[4].teacher_num,result.data[5].teacher_num,result.data[6].teacher_num
                ,result.data[7].teacher_num,result.data[8].teacher_num];
            }else {
              $('#subsume-person-tubiao').empty();
              option1.xAxis.data = [];
              option1.series[1].data = [];
            }
            myChart1.setOption(option1);
          }
        }
      });
    });


    //滚动设备总览
    function gunfawen() {
      var contentnum = parseInt($(".subsume-equ").height() / 42);
      var linum = $(".subsume-equ-body").find(".subsume-equ-li").length;
      if (linum > contentnum) {
        gundongtime = setInterval(function () {
          if (($(".subsume-equ-body").position().top - 10) / -42 < (linum - parseInt($(".subsume-equ").height() / 42))) {
            if ($(".subsume-equ-body").position().top == 0) {
              $(".subsume-equ-body").css("top", $(".subsume-equ-body").position().top - 52);
            } else {
              $(".subsume-equ-body").css("top", $(".subsume-equ-body").position().top - 42);
            }
          } else {
            $(".subsume-equ-body").css("top", 0);
          }
        }, 2000)
      }
    }

    //滚动
    function startmarquee(lh, speed, delay, id) {
      var t;
      var p = false;
      var o = document.getElementById(id);
      var oHeight = o.offsetHeight; /** div的高度 **/
      var preTop = 0;
      o.scrollTop = 0;
      function start() {
        t = setInterval(scrolling);
        o.scrollTop += 1;
      }
      function scrolling() {
        if (o.scrollTop % lh != 0 && o.scrollTop % (o.scrollHeight - oHeight - 1) != 0) {
          preTop = o.scrollTop;
          o.scrollTop += 1;
          if (preTop >= o.scrollHeight - o.offsetHeight - 3 || preTop == o.scrollTop) {
            o.scrollTop = 0;
          }
        } else {
          clearInterval(t);
          setTimeout(start, delay);
        }
      }
      setTimeout(start, delay);
    }


    startmarquee(2, 84, 300, "subsume-person-tubiao");
    //窗口变化，图表resize
    $(window).resize(function () {
      resizeChart();
    })
    function circle() { }

    function btnOverstep() { }

  </script>
</body>

</html>