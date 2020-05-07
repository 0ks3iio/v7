$(function(){
	$('.multi-select-text').each(function(){
		$(this).children('.form-control').attr('readonly','readonly');
	});
	$('.multi-select-text').click(function(){
		var $multi=$(this).parent('.multi-select');
		//初始化
		$multi.append('<div class="multi-select-layer"><ul class="multi-select-tab clearfix"><li class="current">选择省份</li><li>选择城市</li><li>选择区县</li></ul><div id="province" class="content-list"><dl class="clearfix"><dt>A-G</dt><dd class="clearfix"><span pro-sort="0">北京</span><span pro-sort="11">安徽</span><span pro-sort="12">福建</span><span pro-sort="18">广东</span><span pro-sort="19">广西</span><span pro-sort="21">重庆</span><span pro-sort="23">贵州</span><span pro-sort="27">甘肃</span><span pro-sort="32">澳门</span><span pro-sort="34">钓鱼岛</span></dd></dl><dl class="clearfix"><dt>H-K</dt><dd class="clearfix"><span pro-sort="9">江苏</span><span pro-sort="13">江西</span><span pro-sort="15">河南</span><span pro-sort="2">河北</span><span pro-sort="16">湖北</span><span pro-sort="17">湖南</span><span pro-sort="20">海南</span><span pro-sort="6">吉林</span><span pro-sort="7">黑龙江</span></dd></dl><dl class="clearfix"><dt>L-S</dt><dd class="clearfix"><span pro-sort="8">上海</span><span pro-sort="1">天津</span><span pro-sort="14">山东</span><span pro-sort="3">山西</span><span pro-sort="22">四川</span><span pro-sort="26">陕西</span><span pro-sort="4">内蒙古</span><span pro-sort="5">辽宁</span><span pro-sort="28">青海</span><span pro-sort="29">宁夏</span></dd></dl><dl class="clearfix"><dt>T-Z</dt><dd class="clearfix"><span pro-sort="10">浙江</span><span pro-sort="24">云南</span><span pro-sort="25">西藏</span><span pro-sort="30">新疆</span><span pro-sort="31">台湾</span><span pro-sort="33">香港</span></dd></dl></div><ul id="city" class="content-list clearfix"></ul><ul id="district" class="content-list clearfix"></ul></div>');
		$multi.children('.multi-select-layer').height($multi.children('.multi-select-layer').height());
		
		//省选择获取数据
		$('#province').on('click','dl dd span',function(){
			var province_txt=$(this).text();
			$('#province dd span').removeClass('current');
			$(this).addClass('current');
			$multi.find('.form-control').val(province_txt).attr('data-province',province_txt);
		});
		
		//获取市列表
		$('#province').on('click','dl dd span',function(){
			var proSort=$(this).attr('pro-sort');
			$('.multi-select-tab li:eq(1)').addClass('current').siblings('li').removeClass('current');
			$('.multi-select-tab li:eq(0)').addClass('can');
			$('#province').hide().next('#city').show();
			$.getJSON('../js/data.json', function(data2) {
		        $('#city,#district').html('');//清空市、区列表
		        $.each(data2[proSort].city, function(y, item2) {
		        	//插入市列表
		            $('#city').append(
	                    '<li pro-sort="'+ proSort +'" city-sort="'+ y +'">' + item2.name + '</li>' 
	                );
		        });
	        });
		});
		//市选择获取数据
		$('#city').on('click','li',function(){
			var province_txt=$multi.find('.form-control').attr('data-province');
			var city_txt=$(this).text();
			$(this).addClass('current').siblings('li').removeClass('current');
			$multi.find('.form-control').val(province_txt+'/'+city_txt).attr('data-city',city_txt);
		});
		
		
		//获取区列表
		$('#city').on('click','li',function(){
			var proSort=$(this).attr('pro-sort');
			var citySort=$(this).attr('city-sort');
			$('.multi-select-tab li:eq(2)').addClass('current').siblings('li').removeClass('current');
			$('.multi-select-tab li:eq(0),.multi-select-tab li:eq(1)').addClass('can');
			$('#province').hide().next('#city').hide().next('#district').show();
			$.getJSON('../js/data.json', function(data3) {
		        $('#district').html('');//清空区列表
		        $.each(data3[proSort].city[citySort].area, function(z, item3) {
		        	//插入区列表
		            $('#district').append(
	                    '<li>' + item3 + '</li>' 
	                );
		        });
	        });
		});
		//区选择获取数据
		$('#district').on('click','li',function(){
			var province_txt=$multi.find('.form-control').attr('data-province');
			var city_txt=$multi.find('.form-control').attr('data-city');
			var district_txt=$(this).text();
			$(this).addClass('current').siblings('li').removeClass('current');
			$multi.find('.form-control').val(province_txt+'/'+city_txt+'/'+district_txt).attr('data-district',district_txt);
			$multi.children('.multi-select-layer').remove();
		});
		
		//tab切换-回退
		$('.multi-select-tab').on('click','li.can',function(){
			$(this).addClass('current').removeClass('can').siblings('li').removeClass('current');
			$('.multi-select-tab li:gt('+$(this).index()+')').removeClass('can');
			$multi.find('.multi-select-layer .content-list:eq('+$(this).index()+')').show().siblings('.content-list').hide();
		});
		
		//点击其他区域隐藏层
		$(document).click(function(event){
			var eo=$(event.target);
			if($('.multi-select-layer').is(':visible') && !eo.parents('.multi-select').length)
			$('.multi-select-layer').remove();
		});
		
	});
});