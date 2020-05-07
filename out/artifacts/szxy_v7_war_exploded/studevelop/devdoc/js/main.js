$(function(){
	var option = {
	    tooltip: {},
	    radar: {
	        name: {
	            textStyle: {
	                color: '#9b9b9b',
	                padding: [3, 5],
	                fontSize: 26
	           }
	        },
	        indicator: [
	           { name: '注意', max: 6500},
	           { name: '意志', max: 16000},
	           { name: '情绪', max: 30000},
	           { name: '思维', max: 38000},
	           { name: '记忆', max: 52000},
	           { name: '观察', max: 25000}
	        ]
	    },
	    series: [{
	        name: '预算 vs 开销',
	        type: 'radar',
	        // areaStyle: {normal: {}},
	        data : [
	            {
	                value : [4300, 10000, 28000, 35000, 50000, 19000],
	                name : '预算分配（Allocated Budget）'
	            },
	             {
	                value : [5000, 14000, 28000, 31000, 42000, 21000],
	                name : '实际开销（Actual Spending）'
	            }
	        ]
	    }]
	};
	var echartsIns = echarts.init(document.getElementById('chart01'));
	echartsIns.setOption(option);
	var isc = echartsIns.getDataURL({
	    pixelRatio: 2,
	});
	console.log(isc);
	$('#chartImg').attr("src",isc);
	$('#chart01').hide();
	$('#chartImg').show();

	// tab切换
	$('.tab a').on('click',function(e){
		e.preventDefault();
		var id = $(this).attr('href').split('#')[1];
		$(this).parent().addClass('active').siblings().removeClass('active');
		$('#'+id).addClass('active').siblings().removeClass('active');
	})

	// 评分
	$('[data-score]').each(function(){
		var stars = $(this).find('span');
		for(var i=0; i< $(this).data('score'); i++){
			stars.eq(i).addClass('active');
		}
	})

	// 幻灯片
	if($('.slider').length > 0){
		$('.slider').each(function(){
			var that = $(this),
				counter = that.closest('.slider-box').find('.slider-counter');
			var sl = that.slick({
				fade: true,
				prevArrow: that.parent().find('.slider-prev'),
				nextArrow: that.parent().find('.slider-next'),
				autoplay:true
			});
			counter.text(1 + '/' + that.find('.slick-slide').length);
			$(sl).on('afterChange', function(slick, currentSlide){
				counter.text(currentSlide.currentSlide + 1 + '/' + currentSlide.slideCount);
			})
		})
	}
})