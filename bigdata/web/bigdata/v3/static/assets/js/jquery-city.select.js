//$('#citySelect1').citySelect({
//	direction: 'down',
//	format: '/'
//});
//行政区划选择
;
(function($, window, document, undefined){
	//初始化用字符串拼接
	var initStrTab = '<ul class="city-select-tab">' +
		'<li class="active">选择省份</li>' +
		'<li>选择城市</li>' +
		'<li>选择区县</li>' +
	'</ul>';
	var initStrList = '<div id="province" class="city-select-list">' +
		'<dl>' +
			'<dt>A-G</dt>' +
			'<dd><span pro-sort="0">北京</span><span pro-sort="11">安徽</span><span pro-sort="12">福建</span><span pro-sort="18">广东</span><span pro-sort="19">广西</span><span pro-sort="21">重庆</span><span pro-sort="23">贵州</span><span pro-sort="27">甘肃</span><span pro-sort="32">澳门</span></dd>' +
		'</dl>' +
		'<dl>' +
			'<dt>H-K</dt>' +
			'<dd><span pro-sort="9">江苏</span><span pro-sort="13">江西</span><span pro-sort="15">河南</span><span pro-sort="2">河北</span><span pro-sort="16">湖北</span><span pro-sort="17">湖南</span><span pro-sort="20">海南</span><span pro-sort="6">吉林</span><span pro-sort="7">黑龙江</span></dd>' +
		'</dl>' +
		'<dl>' +
			'<dt>L-S</dt>' +
			'<dd><span pro-sort="8">上海</span><span pro-sort="1">天津</span><span pro-sort="14">山东</span><span pro-sort="3">山西</span><span pro-sort="22">四川</span><span pro-sort="26">陕西</span><span pro-sort="4">内蒙古</span><span pro-sort="5">辽宁</span><span pro-sort="28">青海</span><span pro-sort="29">宁夏</span></dd>' +
		'</dl>' +
		'<dl>' +
			'<dt>T-Z</dt>' +
			'<dd><span pro-sort="10">浙江</span><span pro-sort="24">云南</span><span pro-sort="25">西藏</span><span pro-sort="30">新疆</span><span pro-sort="31">台湾</span><span pro-sort="33">香港</span></dd>' +
		'</dl>' +
	'</div>' +
	'<ul id="city" class="city-select-list"></ul>' +
	'<ul id="district" class="city-select-list"></ul>';
	
	//默认参数
	var defaults = {
		direction: 'down',
		format: '/'
	};
	
	//定义构造函数
	function CitySelect(element, setting) {
		this.element = element;
		this.options = $.extend({}, initStrTab, initStrList, defaults, setting);
		this.init();
	};

	//定义方法
	CitySelect.prototype = {
		//初始化
		init: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element);
			_el.addClass('form-control-toggle').prop('readonly', true);
			_el.on('click.citySelect', function(e) {
				e.preventDefault();
				_txt = $(this);
				_.show();
				_.selectData();
			});
			$(document).click(function(event){
				eo=$(event.target);
				_.hide();
			});
		},
		//显示功能组件：加载省数据
		show: function() {
			var _ = this,
				_el = $(this.element),
				direction = this.options.direction,
				initStr;
			if (direction == 'up') {
				initStr = '<div class="city-select">' +
					initStrList +
					initStrTab +
				'</div>';
			} else{
				initStr = '<div class="city-select">' +
					initStrTab +
					initStrList +
				'</div>';
			}
			$('.city-select-txt').removeClass('city-select-txt');
			_txt.addClass('city-select-txt');
			if ($('.city-select').length === 1) {
				$('.city-select').remove();
			}
			$('body').append(initStr);
			var _city = $('.city-select'), o_top, o_bottom, o_left;
			o_left = _txt.offset().left;
			if (direction == 'up') {
				o_top = _txt.offset().top - _txt.height() - _city.height();
			} else{
				o_top = _txt.offset().top + _txt.height();
			}
			_city.css({top: o_top, left: o_left});
		},
		//隐藏功能组件
		hide: function(){
			if(!eo.hasClass('city-select-txt') && !eo.parents('.city-select').length){
				$('.city-select').remove();
				$('.city-select-txt').removeClass('city-select-txt').removeAttr('data-province data-city data-district');
			}
	    },
	    //选取数据
		selectData: function(){
			var _city = $('.city-select'),
				direction = this.options.direction,
				format = this.options.format;
			//省相关操作
			$('body').off('click').on('click','#province dd > span',function(){
				//省选择获取数据
				var province_txt = $(this).text();
				$('#province dd > span').removeClass('active');
				$(this).addClass('active');
				$('.city-select-txt').val(province_txt).attr('data-province',province_txt);
				//获取市列表
				var proSort=$(this).attr('pro-sort');
				$('.city-select-tab li:eq(1)').addClass('active').siblings('li').removeClass('active');
				$('.city-select-tab li:eq(0)').addClass('can');
				$('#province').hide().next('#city').show();
				$.getJSON('./assets/js/city.data-3.json', function(data2) {
			        $('#city,#district').html('');//清空市、区列表
			        $.each(data2[proSort].city, function(y, item2) {
			        	//插入市列表
			            $('#city').append(
		                    '<li pro-sort="'+ proSort +'" city-sort="'+ y +'">' + item2.name + '</li>' 
		                );
			        });
		        });
		        offsetTop();
			});
			
			//市相关操作
			$('body').on('click','#city li',function(){
				//市选择获取数据
				var province_txt = $('.city-select-txt').attr('data-province');
				var city_txt = $(this).text();
				$(this).addClass('active').siblings('li').removeClass('active');
				$('.city-select-txt').val(province_txt + format + city_txt).attr('data-city', city_txt);
				//获取区列表
				var proSort = $(this).attr('pro-sort');
				var citySort = $(this).attr('city-sort');
				$('.city-select-tab li:eq(2)').addClass('active').siblings('li').removeClass('active');
				$('.city-select-tab li:eq(0),.city-select-tab li:eq(1)').addClass('can');
				$('#province').hide().next('#city').hide().next('#district').show();
				$.getJSON('./assets/js/city.data-3.json', function(data3) {
			        $('#district').html('');//清空区列表
			        $.each(data3[proSort].city[citySort].area, function(z, item3) {
			        	//插入区列表
			            $('#district').append(
		                    '<li>' + item3 + '</li>' 
		                );
			        });
		        });
		        offsetTop();
			});
			
			//区相关操作
			$('body').on('click','#district li',function(){
				//区选择获取数据
				var province_txt = $('.city-select-txt').attr('data-province');
				var city_txt = $('.city-select-txt').attr('data-city');
				var district_txt = $(this).text();
				$(this).addClass('active').siblings('li').removeClass('active');
				$('.city-select-txt').val(province_txt + format + city_txt + format + district_txt).attr('data-district', district_txt);
				$('.city-select').remove();
				$('.city-select-txt').removeClass('city-select-txt').removeAttr('data-province data-city data-district');
				offsetTop();
			});
			
			//tab切换-回退
			$('body').on('click','.city-select-tab li.can',function(){
				$(this).addClass('active').removeClass('can').siblings('li').removeClass('active');
				$('.city-select-tab li:gt(' + $(this).index() + ')').removeClass('can');
				$('.city-select-list:eq(' + $(this).index() + ')').show().siblings('.city-select-list').hide();
				offsetTop();
			});
			
			//重新定位层位置
			function offsetTop(){
				if (direction == 'up') {
					setTimeout(function(){
						o_top = _txt.offset().top - _txt.height() - _city.height();
						_city.css('top', o_top);
					},50);
		        }
	      };
			
			
		}
	};
	
	//定义插件
	$.fn.citySelect = function(options) {
        return this.each(function(index, element) {
            element.citySelect = new CitySelect(element, options);
        });
	};
	
})(jQuery, window, document);