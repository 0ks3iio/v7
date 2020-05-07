
$(function(){
	
	var $nav_wrap = $('.js-nav-wrap'),
		$subNav_modal = $('.subNav-modal'),
		$subNav_modal_container = $('.subNav-modal-container'),
		$sidebar = $('.sidebar'),
		$sidebar_toggle = $('.js-siderbar-collapse'),
		$win_height = $(window).height(),
		timerShow,
		timerHide;

	function wpScroll(container, height){
		container.slimscroll({
			height: height
		})
	}

	function onceResize(callback, duration){
		var timer = null;

		$(window).on('resize', function(){
			clearTimeout(timer);
			timer = setTimeout(callback, duration);
		}).trigger('resize');
	}

	// 左侧导航滚动
	onceResize(function(){
		$win_height = $(window).height();
		$nav_height = $win_height - $nav_wrap.offset().top + $(window).scrollTop() - 30;  /*30px为底部预留距离*/

		$nav_wrap.slimscroll({destroy:true})
		.css({
			'height':'auto'
		});

		wpScroll($nav_wrap, $nav_height);
	}, 150);

	function subNavScroll(){
		var s_h = $win_height - $sidebar.offset().top + $(window).scrollTop();
		if($subNav_modal_container.height() >= s_h){
			var $subNav_height = s_h;
			wpScroll($subNav_modal_container, $subNav_height)
		}
	}

	function showSubmenu(submenu){
		$(submenu).stop().slideDown(150).closest('li').addClass('open');
		$(submenu).prev().find('.arrow').removeClass('fa-angle-right').addClass('fa-angle-down');
	}

	function hideSubmenu(submenu){
		$(submenu).stop().slideUp(150).closest('li').removeClass('open');
		$(submenu).prev().find('.arrow').removeClass('fa-angle-down').addClass('fa-angle-right');
	}

	// 下拉展开导航
	$('.nav-list').on('click',function(e){
		e.stopPropagation();
		function getNode(node){
			return node.nodeName === 'A' ? node : getNode(node.parentNode);
		}
		var $self = $(getNode(e.target));
		// 注释掉，因为当前左侧只有模块
		// if($sidebar.hasClass('collapsed') || !$self.is('A')) return;

		if($self.hasClass('dropdown-toggle')){

			if($self.closest('li').hasClass('open')){
				hideSubmenu( $self.next() );
			}else{

                if($self.text().trim()=="民族一家亲"){
                    $self.closest('li').siblings().removeClass('active')
                        .find('li').removeClass('open active');
                    $self.find("ul").find("li").each(function () {
                        $("this").addClass("open");
                    })
                }else {
                    $self.closest('li').siblings().removeClass('open active')
                        .find('li').removeClass('active');
                }

				// $self.closest('li').siblings().removeClass('open active')
				// .find('li').removeClass('active');

				showSubmenu( $self.next() );
                if($self.text().trim()=="民族一家亲") {
                    $self.next().find("ul").css('display', 'block');
                }
				hideSubmenu( $self.closest('li').siblings().find('.submenu') );

				// 保证在有展开的导航隐藏之后再做计算
				setTimeout(function(){
					var t = $nav_wrap[0].scrollTop,  							/*当前滚动高度*/
						tot = $self.offset().top - $nav_wrap.offset().top, 		/*当前点击元素距滚动区域上边沿的距离*/
						s_h = $self.parent().height(),							/*当前下拉导航的高度*/
						n_h = $nav_wrap.height();   							/*滚动区域的高度*/

					// 当当前导航展开后，显示区域显示不下，当前滚动距离加上两者的差值
					if( tot + s_h > n_h){
						var diff = tot + s_h - n_h;
						
						$nav_wrap.slimscroll({
							scrollTo: t + diff + 'px'
						})
					}
				},150)
			}
		}else{
			$self.closest('li').addClass('active')
			.siblings().removeClass('open active')
			.find('li').removeClass('active');

			hideSubmenu( $self.closest('li').siblings().find('.submenu') );
		}
	})

	// 二级菜单导航弹框的显示位置
	function posModal(t, b, h){
		$subNav_modal.css({
			'top': t,
			'bottom': b,
			'height': h
		});
	}

	// 关闭二级菜单导航弹框
	function closeModal(element){
		// 移除滚动条
		$subNav_modal_container
		.slimscroll({destroy:true})
		.css({
			'height':'auto'
		});

		posModal('auto','auto','auto');

		element.closest('li').removeClass('hover');
		$('[data-id=' + element.data("show") + ']').hide().closest($subNav_modal).hide();
	}

	$('.nav-list a[data-show]').on('mouseenter',function(){

		var $this = $(this),
			$top = $this.offset().top - $sidebar.offset().top,
			$t_h = $win_height - $sidebar.offset().top + $(window).scrollTop(),
			$m_h = 0;

		clearTimeout(timerShow);
 		timerShow = setTimeout(function(){

 			// 切换hover状态、显示对应的二级菜单导航
 			$this.closest('li').addClass('hover').siblings().removeClass('hover');

			if(!$sidebar.hasClass('collapsed')) return;

			$('[data-id=' + $this.data("show") + ']')
			.show().siblings().hide()
			.closest($subNav_modal).show();

			$m_h = $subNav_modal.height();	//获取弹出框的高度

			if( $t_h <= $m_h ){

				//当二级弹出框的高度大于左侧的高度时
				posModal(0, 'auto', $t_h);
				subNavScroll();

			}else if($top < $t_h-$m_h){

				//当前导航项距顶部条的距离小于左侧高度减去二级弹出框的高度时
				posModal($top, 'auto', 'auto');

			}else if($top >= $t_h-$m_h && $t_h > $m_h){

				//当前导航项距顶部条的距离大于等于左侧高度减去二级弹出框的高度，
				//并且左侧高度大于二级弹出框的高度
				posModal('auto', 0, 'auto');
			}
		},150);
	}).on('mouseleave',function(){
		var $in=false,
			$this=$(this);

		clearTimeout(timerShow);
		clearTimeout(timerHide);

		$subNav_modal.on('mouseenter',function(){
			$in=true;
		}).on('mouseleave',function(){
			closeModal($this);
		})
		
		// 避免鼠标未移到二级菜单就关闭
		timerHide=setTimeout(function(){
			if($in===false){
				closeModal($this);
			}
		},150)
	})

		
	// 侧边栏展开折叠
	;(function(){

		var DURATION = 150;
		var MIN_W = 60;
		var MAX_W = 210;
		var timer = null;

		function openSidebar(){
			$sidebar_toggle.find('i').removeClass('icon-indent').addClass('icon-dedent');
			$sidebar.stop().animate({
					width: MAX_W
				}, DURATION, function(){
					$(this).removeClass('collapsed');
					$(document).trigger('resize.custom');
				})
				.next().stop().animate({
					'margin-left': MAX_W
				}, DURATION) 
		}

		function collapseSidebar(){
			$('.nav-list > li').removeClass('open')
				.find('.submenu').stop().slideUp( DURATION )
				.prev().find('.arrow').removeClass('fa-angle-down').addClass('fa-angle-right');

			$sidebar_toggle.find('i').removeClass('icon-dedent').addClass('icon-indent');
			$sidebar.addClass('collapsed')
				.stop().animate({
					width: MIN_W
				}, DURATION, function(){
					$(document).trigger('resize.custom');
				})
				.next().stop().animate({
					'margin-left': MIN_W
				}, DURATION) 
		}

		$sidebar_toggle.on('click', function(){
			if($sidebar.hasClass('collapsed')){
				openSidebar();
			}else{
				collapseSidebar();
			}
		});

		onceResize(function(){
			if($(window).innerWidth() <= 768){
				collapseSidebar();
			}else{
				openSidebar();
			}
		}, DURATION);
		
	})()

	// 下拉弹出框
	$('.js-dropbox-toggle').on('click', function(){
		$(this).next().toggle()
		.closest('li').toggleClass('open')
		.siblings().removeClass('open').find('.dropbox').hide();
		scrollMailList();
	})

	// 通讯录内容部分滚动
	function scrollMailList(){
		var $userContainer=$('.mail-list-wrap .tab-content');
		if($userContainer.length<=0) return;
		var $maxHeight=$(window).height()-$userContainer.offset().top;
		var $resultContainer=$('.mail-search-result .user-list');
		if($userContainer.length<=0) return;
		var $resultHeight=$(window).height()-$resultContainer.offset().top;
		
		wpScroll($userContainer,$maxHeight);
		wpScroll($resultContainer,$resultHeight);
	}
		
	onceResize(scrollMailList, 150);

	// 登录用户切换列表滚动
	$('.dropdown-menu-user').parent().on('shown.bs.dropdown', function() {
		var container = $('.dropdown-menu-user .user-list');
		var max = $(window).height() - container.offset().top + $(window).scrollTop() - 55;
		
		if(container.outerHeight() > max){
			container.slimscroll({
				height: max
			})
		}
	})



	// 通讯录分组下拉展开
	$('.panel-collapse').on('shown.bs.collapse',function(){
		$(this).parent().find('.panel-title i')
		.removeClass('fa-caret-right').addClass('fa-caret-down');
	}).on('hidden.bs.collapse',function(){
		$(this).parent().find('.panel-title i')
		.removeClass('fa-caret-down').addClass('fa-caret-right');
	})

	// 通讯录搜索
	$('.mail-search input').on('focus',function(){
		$(this).closest('.input-icon').addClass('focus');
		$('.mail-search-result').removeClass('hidden').addClass('show');
		$('.mail-list-wrap').removeClass('show').addClass('hidden');
		scrollMailList();
	})
	$('.mail-search-close').on('click',function(){
		$(this).closest('.input-icon').removeClass('focus').find('input').val('');
		$('.mail-search-result').removeClass('show').addClass('hidden');
		$('.mail-list-wrap').removeClass('hidden').addClass('show');
		scrollMailList();
	})
	$('.input-icon>i').on('click', function(){
		$(this).next().find('input').focus();
	})

	// 查看通讯录好友信息
	$('[data-toggle=show-user-detail]').on('click',function(){
		if($(this).parent().hasClass('open')){
			$(this).parent().removeClass('open');
		}else{
			$(this).parent().addClass('open').siblings().removeClass('open');
		}
	});

	// 选择
	$('[data-action=select]').on('click',function(){
		if($(this).hasClass('disabled')){
			return false;
		}else if($(this).parent().hasClass('multiselect') || $(this).hasClass('selected')){
			$(this).toggleClass('selected');
		}else{
			$(this).addClass('selected').siblings().removeClass('selected');
		}
	});

	$('.module-nav >ul>li>a').on('click',function(e){
		
		if($(this).closest('li').hasClass('open')){
			hideSubmenu( $(this).next() )
		}else{
			showSubmenu( $(this).next() )
			hideSubmenu( $(this).closest('li').siblings().find('.module-style-list') );
			$(this).closest('li').siblings().removeClass('open');
		}
	})

	// iframe插入页面高度
	onceResize(function(){
		var $iframe=$('.js-iframe-page');
		if($iframe.length>0){
			$iframe.each(function(index){
				$iframe.eq(index).css({
					'padding-bottom': $(window).height() - $iframe.eq(index).offset().top
				})
			})
		}
	}, 150);
})

$(function(){

	//下拉选择框
	function selectChosen(){
		if($('.chosen-select').length>0){
			$('.chosen-select').chosen({
				allow_single_deselect:true,
				disable_search_threshold: 10
			}); 
			//resize the chosen on window resize

			$(window)
			.off('resize.chosen')
			.on('resize.chosen', function() {
				$('.chosen-select').each(function() {
					var $this = $(this);
					$this.next().css({'width': $this.width()});
				})
			}).trigger('resize.chosen');

			// 设置高度
			// $('.chosen-choices').each(function(){
			// 	$(this)[0].style.cssText = "height:60px !important;overflow:auto";
			// })
		}
		
	}
	selectChosen();


	// #############提示工具#############
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});
})

$(function(){
	
	var $left=$('.js-pageTabLeft'),
		$right=$('.js-pageTabRight'),
		$tabs=$('.js-pageTabs'),
		$tab_c=$('.js-pageTabsContainer'),
		$tab=$('.js-pageTab'),
		$sup_w=$sub_w=0;

	function calcWidth(){
		var $tab=$('.js-pageTab');
		$sup_w=$sub_w=0;		//重新计算的时候宽度清零
		$sup_w=$tabs.width();	//

		$tab.each(function(index){
			$sub_w=$sub_w+$(this).outerWidth()+1;	//计算结果向上取整
		})

		if($sub_w >= $sup_w){
			$left.addClass('show');
			$right.addClass('show');
		}else{
			$left.removeClass('show');
			$right.removeClass('show');
		}
		
		$tab_c.css('width', $sub_w);
	}

	// 初始化滚动
	function initMargin(){
		if($sub_w <= $sup_w){
			changeMargin(0);
		}
		if($sub_w > $sup_w){
			changeMargin($sup_w-$sub_w);
		}
	}

	// 改变滚动位子
	function changeMargin(m){
		$tab_c.stop().animate({
			'margin-left': m
		},300)
	}

	function pageTabInit(){
		calcWidth();
		initMargin();
	}

	$(document).on('resize.custom', function(){
		pageTabInit();
	}).trigger('resize.custom');

	// 向左滚动
	$left.on('click', function(e){
		var $m=parseInt($tab_c.css('margin-left'));

		if( $m==0 || $sub_w <= $sup_w){
			return false;
		}else if(Math.abs($m) <= $sup_w){
			changeMargin(0);
		}else{
			changeMargin($m+$sup_w);
		}
	})

	// 向右滚动
	$right.on('click', function(e){
		var $m=parseInt($tab_c.css('margin-left'));

		if( $m==$sup_w-$sub_w || $sub_w <= $sup_w){
			return false;
		}else if(Math.abs($m) >= $sub_w-2*$sup_w){
			changeMargin($sup_w-$sub_w);
		}else{
			changeMargin($m-$sup_w);
		}
	})

	// 切换标签和关闭标签
	$tab.on('click',function(e){
		e.preventDefault();
		$(this).addClass('active').siblings().removeClass('active');

	}).find('i').on('click',function(e){
		var $m=parseInt($tab_c.css('margin-left')),
			$width=$(this).parent().outerWidth()+1;

		e.preventDefault();
		$(this).parent().remove();
		calcWidth();
		
		if( Math.abs($m) > $width){
			changeMargin($m+$width);
		}else{
			changeMargin(0);
		}
	})

	// 班级标签筛选
	$('.label-select .label-select-item').on('click',function(){
		if($(this).hasClass('disabled')){
			return false;
		}else if($(this).parent().hasClass('multiselect')){
			$(this).toggleClass('active');
		}else{
			$(this).addClass('active').siblings().removeClass('active');
		}
	});
	
	
	//数字输入框
	$('.form-number .form-control').keyup(function(){
		var num = /^\d{1,4}$/;
		if(!num.test($(this).val())){
			layer.msg('请输入数字！', {
				icon: 2,
				time: 1500,
				shade: 0.2
			});
			$(this).val('');
		};
	});
	$('.form-number > button').click(function(e){
		e.preventDefault();
		var $num = $(this).siblings('.form-control');
		var val = $num.val();
		if (!val ) val = 0;
		var num = parseInt(val);
		var step = $num.parent('.form-number').attr('data-step');
		if (step === undefined) {
			step = 1;
		} else{
			step = parseInt(step);
		}
		if ($(this).hasClass('form-number-add')) {
			num += step;
		} else{
			num -= step;
			if (num <= 0) num = 0;
		}
		$num.val(num);
	});
})