<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>个人报告单</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/components.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/pages.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/page-desk.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />

    <script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
</head>

<body>
<div class="main-container" id="main-container">
    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <!--<div>
                    <button class="btn btn-blue mr10 font-14" onclick="showpdf()">
                        导出PDF
                    </button>
                </div>-->

                </div>
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>
<!-- /.main-container -->

<!-- basic scripts -->
<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>
<!-- <![endif]-->

<!--[if IE]>
<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>
<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>
<!-- page specific plugin scripts -->
<script src="${request.contextPath}/static/components/layer/layer.js"></script>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script src="${request.contextPath}/static/components/typeahead.js/dist/typeahead.jquery.min.js"></script>
<script src="${request.contextPath}/static/components/dragsort/jquery-list-dragsort.js"></script>
<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<!-- inline scripts related to this page -->
<script src="${request.contextPath}/static/js/desktop.js"></script>

<script type="text/javascript">

    //星星初始化
    function initstar() {
        $(".starul").each(function() {
            var isall = false;
            if($(this).hasClass("allstarul")){
                isall = true;
            }
            var score = $(this).attr("data-score") * 2;
            $(this).siblings(".star-tip").html($(this).attr("data-score") + "星");
            $(this).find("li").each(function(i, e) {
                if (2 * i + 2 <= score) {
                    $(e).attr("class", "fullStar");
                } else if (2 * i + 1 <= score) {
                    $(e).attr("class", "halfStar");
                } else {
                    if(isall) {
                        $(e).attr("class", "emptyStarnew");
                    }
                    else{
                        $(e).attr("class", "emptyStar");
                    }
                }
            });
        });
    }
    function setChart(){
        var mychartPdf = echarts.init(document.getElementById("mychartPdf"));
        var indicator4=[];
        <#if hasStatMoreList?exists && hasStatMoreList?size gt 0>
          <#list hasStatMoreList as item>
              indicator4[${item_index}]={
                  name: "${item.itemName!}", max: 5
              }
          </#list>
          </#if>
          var optionPdf = {
              color: ["#f10215"],
              radar: {
                  indicator: indicator4,
                  name: {
                      textStyle: {
                          color:'#333'
                      }
                  },
              },
              series: [
                  {
                      type: "radar",
                      areaStyle: { normal: {opacity:0.2} },
                      data: [
                          {
                              value: [
                                  <#if hasStatMoreList?exists && hasStatMoreList?size gt 0>
                                  <#list hasStatMoreList as item>
                                  <#if item_index!=0>,</#if>${(item.avgScore?default(0))?string("#.#")}
                                  </#list>
                                  </#if>
                              ],
                              name: "学生测评",
                              label: {
                                  normal: {
                                      show: true,
                                      formatter: function(params) {
                                          return params.value;
                                      }
                                  }
                              },
                          }
                      ]
                  }
              ]
          };

        mychartPdf.setOption(optionPdf);
      }
    $(function() {
        setChart();
        initstar();
        $(".stamp-bai-item").each(function(i, v) {
            $(v).css("width", i * 8 + 10);
        });
        $(".stamp-bai-min").removeClass("active");
        $(".stamp-bai-min").each(function(i, v) {
            if (i < parseInt($(".stamp-bai-box").attr("data-score"))) {
                $(v).addClass("active");
            } else {
                return;
            }
        });

    });
    // setChart();
</script>
</body>
</html>
