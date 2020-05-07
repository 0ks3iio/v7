<div class="main-content-inner">
    <div class="page-content">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <div class="box box-default">
                    <div class="box-body">
                        <div role="tabpanel" class="tab-pane active">
                            <div class="row">
                                <div class="col-xs-9">
                                    <div class="base-item">
                                        <!--<h2 class="base-menu-from" data-tier="1">应用接入规范</h2>-->
                                        <div style="text-indent:2em;">
                                            <p><span style="font-size:20px;">应用名称：</span><span>应用名称需要具有一定概括性，使用户通过应用名称可大致了解该应用的性质、面向对象等要素。应用名称长度在256个字符以内（一个汉字两个字符）。</span></p>
                                            <p><span style="font-size:20px;">应用简介：</span><span>应用简介是对应用的简单介绍，简短并清晰阐述应用的主体内容、优势等要素，编辑内容保存在文件中。</span></p>
                                            <p><span style="font-size:20px;">应用图标：</span><span>应用图标。</span></p>
                                            <p><span style="font-size:20px;">域名：</span><span>应用的域名。</span></p>
                                            <p><span style="font-size:20px;">上下文：</span><span></span></p>
                                            <p><span style="font-size:20px;">首页URL：</span><span>应用首页地址，不包含域名。</span></p>
                                            <p><span style="font-size:20px;">登录URL：</span><span>用户登录应用的地址，不包含域名。</span></p>
                                            <p><span style="font-size:20px;">退出URL：</span><span>用户退出应用的地址，不包含域名。</span></p>
                                            <p><span style="font-size:20px;">适用机构：</span><span>选择适用的机构，包括学校、教育局，可多选。</span></p>
                                            <p><span style="font-size:20px;">适用对象：</span><span>选择使用的用户身份，包括学生、老师、家长，可多选。</span></p>
                                            <p><span style="font-size:20px;">适用学段：</span><span>选择使用的学段，包括幼儿园、小学、初中、高中，可多选。</span></p>
                                            <!--<p style="font-size:20px;"></p>
                                            <div style="text-indent:2em;"></div>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div>

<script>
    $(function(){
        //初始化
        $('.page-content .tab-pane').each(function(){
            var paneIndex=$(this).index()+1;
            var $menu='';
            var $list='';
            var itemIndex=0;
            $(this).find('.base-menu-from').each(function(){
                itemIndex++;
                var itemId='tabpanel'+paneIndex+'-item'+itemIndex;
                $(this).attr('id',itemId)
                var off_top=parseInt($(this).offset().top);
                $list=$list+'<li class="base-fixed-menu-tier'+$(this).attr('data-tier')+'" data-scroll="'+off_top+'"><a href="#'+itemId+'"><i class="fa fa-circle"></i>'+$(this).text()+'</a></li>';
            });
            $menu='<ul class="base-fixed-menu">'+$list+'</ul>';
            $(this).append($menu);
            $(this).find('.base-fixed-menu li:eq(0)').addClass('active');
        });
        //切换的时候重新算高度，隐藏层offset().top不准确
        $('.nav-tabs li').click(function(){
            setTimeout(function(){
                var $pane=$('.page-content .tab-pane:eq('+$(this).index()+')');
                var itemIndex=0;
                $pane.find('.base-menu-from').each(function(){
                    var off_top=parseInt($(this).offset().top);
                    $pane.find('.base-fixed-menu li:eq('+itemIndex+')').attr('data-scroll',off_top);
                    itemIndex++;
                });
            },200);
        });
        //点击左侧定位
        $('.page-content .tab-pane').on('click','.base-fixed-menu li',function(e){
            e.preventDefault();
            var sTop=$(this).attr('data-scroll')-136;
            $(this).addClass('active').siblings('li').removeClass('active');
            $(window).scrollTop(sTop);
            //alert(sTop);
            //alert($(window).scrollTop());
            //$('html,body').animate({scrollTop:$(this).attr('data-scroll')},500);
        });
        //滚动右侧导航切换
        scrollFixed();
        $(window).scroll(function(){
            scrollFixed();
        });
        function scrollFixed(){
            var $menu_li=$('.page-content .tab-pane:visible .base-fixed-menu li');
            var sTop=$(this).scrollTop()+136;
            var menu_len=$('.page-content .tab-pane:visible').find('.base-fixed-menu li').length;
            var i=0;
            $menu_li.each(function(){
                var data_scroll=$(this).attr('data-scroll');
                if (sTop >= data_scroll) {
                    i++;
                };
            });
            if (i == 0) {
                i==0;
            } else{
                i--;
            };
            $menu_li.eq(i).addClass('active').siblings('li').removeClass('active');
            //判断是否滚动到底部
            var canTop=$(document).height()-$(window).height();
            if (sTop == canTop) {
                $menu_li.last().addClass('active').siblings('li').removeClass('active');
            }
        };
    })
</script>