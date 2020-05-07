<!-- /section:basics/sidebar -->
<div class="main-content-inner">
  <div class="page-content">
    <div class="row">
      <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
          <div class="box-body">
            <div class="tab-content">
              <div role="tabpanel" class="tab-pane active" id="aa">
                <div class="row">
                  <div class="col-xs-9">
                    <div class="base-item">
                      <h2
                        class="base-menu-from"
                        data-tier="1"
                        id="tabpanel1-item1"
                      >
                        开放平台概述
                      </h2>
                      <p class="lead">
                        <span class="lead-body"
                          >本开放平台为广大教育信息化应用提供商提供一站式、规范、标准化的开放能力服务。</span
                        >
                        <span class="lead-body"
                          >通过本文档，您可以了解到接入应用的对接流程，包括通过调取标准API接口获取基础数据、创建应用、单点登录、使用API接口、提交审核等。</span
                        >
                      </p>
                    </div>
                    <div class="base-item">
                      <h2
                        class="base-menu-from"
                        data-tier="1"
                        id="tabpanel1-item2"
                      >
                        用户使用场景
                      </h2>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic1.png"
                          alt=""
                        />
                      </p>
                      <p class="base-warn">
                        图示说明：标黄为第三方应用的界面，灰色为万朋公司产品界面。
                      </p>
                    </div>
                    <div class="base-item">
                      <h2
                        class="base-menu-from"
                        data-tier="1"
                        id="tabpanel1-item3"
                      >
                        典型对接流程
                      </h2>
                      <h3
                        class="base-menu-from"
                        data-tier="2"
                        id="tabpanel1-item4"
                      >
                        网站单点登录
                      </h3>
                      <p class="lead">
                        <span class="lead-body"
                          >此对接流程适用于”用户使用场景一、二”</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic2.png"
                          alt=""
                        />
                      </p>
                      <p class="base-warn">
                        图示说明：序号和下面的步骤对应，标黄为第三方应用开发人员要做的工作，灰色为万朋公司配合做的工作。
                      </p>
                      <p class="base-item-title">步骤1：注册开发者</p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic3.png"
                          alt=""
                        />
                      </p>
                      <p class="base-item-title">
                        步骤2：申请“用户信息”接口，提交审核。
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >点击“开发文档”，在左边导航选择“开放数据”-“获取基础数据”页面，点击“申请接口”按钮，选择“用户信息”，点击确定
                          （如下图）。然后此接口就是待审核状态，请联系我司对接方人员审核。</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic4.png"
                          alt=""
                        />
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic5.png"
                          alt=""
                        />
                      </p>
                      <p class="base-item-title">
                        步骤3：调试“用户信息”接口
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >“用户信息”接口审核通过后，在“开发文档”-“基础数据”页面上，将审核接口开关切换到“已通过审核接口”状态上，可看到“用户信息”已列出，如下图。</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic6.png"
                          alt=""
                        />
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >点击“用户信息”这条栏目，可以将此接口信息展开，看到各类“用户信息”接口，如下图：</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic7.png"
                          alt=""
                        />
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >点击某条接口，可以看到接口详细信息说明及调用说明，并且可以直接输入调入参数并点击“获取”进行真实数据的调试。如下图：
                        </span>
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic8.png"
                          alt=""
                        />
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic9.png"
                          alt=""
                        />
                      </p>
                      <p class="base-item-title">
                        步骤4： 开发对应数据接口并将用户数据存入第三方库
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >调试完成后，确保将用户信息数据存入第三方库。单点登陆的前提是：
                          需要保证双方基础数据的一致性，账号需要通过接口的形式来进行拉取同步；
                          单点对接的时候，可以获取到平台当前登陆的账号信息，用这个账号来完成自己系统的登陆，保证单点的正常流程
                          。</span
                        >
                      </p>
                      <p class="base-item-title">
                        步骤5：新增应用，提交审核
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic10.png"
                          alt=""
                        />
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >1）首页URL、登录URL、退出URL
                          的设置都是跟单点登录相关，请事先查看“单点登录”的说明，在如下图页面：</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic11.png"
                          alt=""
                        />
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >2）适用机构、适用对象、适用学段：
                          是指接入的网站需要将图标及应用链接挂到基础平台统一桌面上时，会根据登录用户所属的机构、对象、学段来判断TA的桌面上有没有此应用出现，不在所属范围内则不显示该应用。</span
                        >
                      </p>
                      <p class="base-item-title">
                        步骤6：调试并开发单点登录及退出对应接口
                      </p>
                      <p class="lead">
                        <span class="lead-body"
                          >根据“开放文档”-“单点登录”里的说明，进行调试，包括单点登录及单点退出。</span
                        >
                        <span class="lead-body"
                          >调试完成后，通知我方对接人员将此应用上线。</span
                        >
                      </p>
                      <p class="base-item-title">步骤7：应用上线</p>
                      <p class="lead">
                        <span class="lead-body"
                          >上线后就在“基础平台-统一桌面-我的应用”中出现该应用了，有权限的用户会看到此应用图标（没权限的看不到），点击后，正常就是跳转到该应用的首页，并且是已登录状态，不需要再二次登录。</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic12.png"
                          alt=""
                        />
                      </p>
                      <h3
                        class="base-menu-from"
                        data-tier="2"
                        id="tabpanel1-item5"
                      >
                        网站单点登录并读取各类基础数据
                      </h3>
                      <p class="lead">
                        <span class="lead-body"
                          >此对接流程适用于”用户使用场景一、二”</span
                        ><span class="lead-body"
                          >流程同场景”网站单点登录（包括同步用户）“，区别只在步骤2中需要申请其他需要的基础数据（如下图），步骤3、4调试开发对应的基础数据接口。</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic13.png"
                          alt=""
                        />
                      </p>
                      <h3
                        class="base-menu-from"
                        data-tier="2"
                        id="tabpanel1-item6"
                      >
                        客户端应用-基础平台账号登录
                      </h3>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic14.png"
                          alt=""
                        />
                      </p>
                      <p class="lead">
                        <span class="lead-body">说明：</span
                        ><span class="lead-body"
                          >对接流程和网站对接基本一样，区别在单点登录不同于网站，需要在如下图位置查看移动应用的接入说明来开发。</span
                        >
                      </p>
                      <p>
                        <img
                          src="${resourceUrl}/images/base/temp/pic15.png"
                          alt=""
                        />
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
      </div>
      <!-- /.col -->
    </div>
    <!-- /.row -->
  </div>
 <!-- /.page-content -->
</div>
    <script>
    $(function(){
    	 //初始化
        $(".page-content .tab-pane").each(function() {
          var paneIndex = $(this).index() + 1;
          var $menu = "";
          var $list = "";
          var itemIndex = 0;
          $(this)
            .find(".base-menu-from")
            .each(function() {
              itemIndex++;
              var itemId = "tabpanel" + paneIndex + "-item" + itemIndex;
              $(this).attr("id", itemId);
              var off_top = parseInt($(this).offset().top);
              $list =
                $list +
                '<li class="base-fixed-menu-tier' +
                $(this).attr("data-tier") +
                '" data-scroll="' +
                off_top +
                '"><a href="#' +
                itemId +
                '"><i class="fa fa-circle"></i>' +
                $(this).text() +
                "</a></li>";
            });
          $menu =
            '<div class="col-xs-3"><ul class="base-fixed-menu">' +
            $list +
            "</ul></div>";
          $(this)
            .children(".row")
            .append($menu);
          $(this)
            .find(".base-fixed-menu li:eq(0)")
            .addClass("active");
        });
        //切换的时候重新算高度，隐藏层offset().top不准确
        $(".nav-tabs li").click(function() {
          setTimeout(function() {
            var $pane = $(
              ".page-content .tab-pane:eq(" + $(this).index() + ")"
            );
            var itemIndex = 0;
            $pane.find(".base-menu-from").each(function() {
              var off_top = parseInt($(this).offset().top);
              $pane
                .find(".base-fixed-menu li:eq(" + itemIndex + ")")
                .attr("data-scroll", off_top);
              itemIndex++;
            });
          }, 200);
        });
        //点击左侧定位
        $(".page-content .tab-pane").on(
          "click",
          ".base-fixed-menu li",
          function(e) {
            e.preventDefault();
            var sTop = $(this).attr("data-scroll") - 136;
            $(this)
              .addClass("active")
              .siblings("li")
              .removeClass("active");
            $(window).scrollTop(sTop);
            //alert(sTop);
            //alert($(window).scrollTop());
            //$('html,body').animate({scrollTop:$(this).attr('data-scroll')},500);
          }
        );
    })
    </script>
