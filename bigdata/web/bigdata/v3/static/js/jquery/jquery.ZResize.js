/**div移动伸缩**/
(function($) {
 
    /**
     * 默认参数
     */
    var defaultOpts = {
        stage: document, //舞台
        item: 'resize-item', //可缩放的类名
    };
 
    /**
     * 定义类
     */
    var ZResize = function(options) {
        this.options = $.extend({}, defaultOpts, options);
        this.init();
    };
 	var z;
    ZResize.prototype = {
        init: function() {
            this.initResizeBox();
        },
        /**
         *  初始化拖拽item
         */
        initResizeBox: function() {
            var self = this;
            $(self.options.item).each(function () {
                //创建面板
                var width = $(this).width();
                var height = $(this).height();
                /**
                 * 创建控制点
                 */
                var n = $('<div class="box-btn n"></div>');//北
                var s = $('<div class="box-btn s"></div>');//南
                var w = $('<div class="box-btn w"></div>');//西
                var e = $('<div class="box-btn e"></div>');//东
                var ne = $('<div class="box-btn ne"></div>');//东北
                var nw = $('<div class="box-btn nw"></div>');//西北
                var se = $('<div class="box-btn se"></div>');//东南
                var sw = $('<div class="box-btn sw"></div>');//西南
 
                //添加公共样式
                self.addHandlerCss([n, s, w, e, ne, nw, se, sw]);
                //添加各自样式
                n.css({
                	'width':'15px',
                	'background': '#00cce3',
                    'top': '-4px',
                    'margin-left': '-7px',
                    'left': '50%',
                    'cursor': 'n-resize'
                });
                s.css({
                	'width':'15px',
                	'background': '#00cce3',
                    'bottom': '-4px',
                    'margin-left': '-7px',
                    'left': '50%',
                    'cursor': 's-resize'
                });
                e.css({
                	'height':'15px',
                	'background': '#00cce3',
                    'top': '50%',
                    'margin-top': '-8px',
                    'right': '-4px',
                    'cursor': 'e-resize'
                });
                w.css({
                	'height':'15px',
                	'background': '#00cce3',
                    'top': '50%',
                    'margin-top': '-8px',
                    'left': '-4px',
                    'cursor': 'w-resize'
                });
                ne.css({
                	'width':'15px',
                	'height':'15px',
                	'border-top':'6px solid #00cce3',
                	'border-right':'6px solid #00cce3',
                    'top': '-6px',
                    'right': '-6px',
                    'cursor': 'ne-resize'
                });
                nw.css({
                	'width':'15px',
                	'height':'15px',
                	'border-top':'6px solid #00cce3',
                	'border-left':'6px solid #00cce3',
                    'top': '-6px',
                    'left': '-6px',
                    'cursor': 'nw-resize'
                });
                se.css({
                	'width':'15px',
                	'height':'15px',
                	'border-bottom':'6px solid #00cce3',
                	'border-right':'6px solid #00cce3',
                    'bottom': '-6px',
                    'right': '-6px',
                    'cursor': 'se-resize'
                });
                sw.css({
                	'width':'15px',
                	'height':'15px',
                	'border-bottom':'6px solid #00cce3',
                	'border-left':'6px solid #00cce3',
                    'bottom': '-6px',
                    'left': '-6px',
                    'cursor': 'sw-resize'
                });
 
                // 添加项目
                self.appendHandler([n, s, w, e, ne, nw, se, sw], $(this));
                
                //绑定拖拽缩放事件
                self.bindResizeEvent($(this));
 
                //绑定触发事件
                self.bindTrigger($(this));
            });
            self.bindHidePanel();
        },
        //控制点公共样式
        addHandlerCss: function(els) {
            for(var i = 0; i < els.length; i++) {
                el = els[i];
                el.css({
                    position: 'absolute',
                    width: '7px',
                    height: '7px',
                    margin: '0'
                    //display: 'none'
                });
            }
        },
        /**
         *  插入容器
         */
        appendHandler: function(handlers, target) {
            for(var i = 0; i < handlers.length; i++) {
                el = handlers[i];
                target.append(el);
            }
        },
        /**
         *  显示拖拽面板
         */
        triggerResize: function(el) {
            var self = this;
            el.siblings(self.options.item).children('.box-btn').css({
                display: 'none'
            });
            el.children('.box-btn').css({
                display: 'block'
            });
        },
        /**
         * 拖拽事件控制 包含8个缩放点  和一个拖拽位置
         */
        
        bindResizeEvent: function(el) {
 
            var self = this;
            var ox = 0; //原始事件x位置
            var oy = 0; //原始事件y位置
            var ow = 0; //原始宽度
            var oh = 0; //原始高度
 
            var oleft = 0; //原始元素位置
            var otop = 0;
            var org = el;
 
            //东
            var emove = false;
            el.on('mousedown','.e', function(e) {
                ox = e.pageX;//原始x位置
                ow = el.width() + 40;
                emove = true;
            });
 
            //南
            var smove = false;
            el.on('mousedown','.s', function(e) {
                oy = e.pageY;//原始x位置
                oh = el.height() + 40;
                smove = true;
            });
 
            //西
            var wmove = false;
            el.on('mousedown','.w', function(e) {
                ox = e.pageX;//原始x位置
                ow = el.width() + 40;
                wmove = true;
                oleft = parseInt(org.css('left').replace('px', ''));
            });
 
            //北
            var nmove = false;
            el.on('mousedown','.n', function(e) {
                oy = e.pageY;//原始x位置
                oh = el.height() + 40;
                nmove = true;
                otop = parseInt(org.css('top').replace('px', ''));
            });
 
            //东北
            var nemove = false;
            el.on('mousedown','.ne', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width() + 40;
                oh = el.height() + 40;
                nemove = true;
                otop = parseInt(org.css('top').replace('px', ''));
            });
 
            //西北
            var nwmove = false;
            el.on('mousedown','.nw', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width() + 40;
                oh = el.height() + 40;
                otop = parseInt(org.css('top').replace('px', ''));
                oleft = parseInt(org.css('left').replace('px', ''));
                nwmove = true;
            });
 
            //东南
            var semove = false;
            el.on('mousedown','.se', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width() + 40;
                oh = el.height() + 40;
                semove = true;
            });
 
            //西南
            var swmove = false;
            el.on('mousedown','.sw', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width() + 40;
                oh = el.height() + 40;
                swmove = true;
                oleft = parseInt(org.css('left').replace('px', ''));
            });
 
            //拖拽
            var drag = false;
            el.on('mousedown', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                otop = parseInt(org.css('top').replace('px', ''));
                oleft = parseInt(org.css('left').replace('px', ''));
                drag = true;
               
            }).on('mouseup', function(e){
//          	var reIndex = el.attr('data-number');
//	        	if (el.children().hasClass('box-chart')) {
//	        		charts[parseInt(reIndex)].resize();
//	        	}
            });
            
            $(self.options.stage).on('mousemove', function(e) {
                var $num=$('input[type="range"]').val()/100;
                if(emove) {
                    var x = (e.pageX - ox)/$num;
                    var num=(x + ow);
                    el.css({
                        width: num
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').width(num-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num - 40);
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num);
                    }
                    
                } else if(smove) {
                    var y = (e.pageY - oy)/$num;
                    var num=(oh + y);
                    el.css({
                        height: num
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').height(num-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#h').val(num - 40);
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-height').find('input').val(num);
                    }
                } else if(wmove) {
                    var x = (e.pageX - ox)/$num;
                    var num=(ow - x);
                    var num1=(oleft + x)+'px'; 
                    el.css({
                        width: num,
                        left: num1
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').width(num-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num - 40);
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num);
                    	$('.js-box-x').find('input').val(num1.slice(0,-2));
                    }
                } else if(nmove) {
                    var y = (e.pageY - oy)/$num;
                    var num=(oh - y);
                    var num1=(otop + y)+'px';
                    el.css({
                        height: num,
                        top: num1
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').height(num-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#h').val(num - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-height').find('input').val(num);
                    	$('.js-box-y').find('input').val(num1.slice(0,-2));
                    }
                } else if(nemove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh - y);
                    var num2=(ow + x);
                    var num3=(otop + y)+'px';
                    el.css({
                        height: num1,
                        width: num2,
                        top: num3
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').width(num2-40).height(num1-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    	$('.js-box-y').find('input').val(num3.slice(0,-2));
                    }
                } else if(nwmove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh - y);
                    var num2=(ow - x);
                    var num3=(otop + y)+'px';
                    var num4=(oleft + x)+'px';
                    el.css({
                        height: num1,
                        width: num2,
                        top: num3,
                        left: num4
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').width(num2-40).height(num1-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    	$('.js-box-x').find('input').val(num4.slice(0,-2));
                    	$('.js-box-y').find('input').val(num3.slice(0,-2));
                    }
                } else if(semove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh + y);
                    var num2=(ow + x);
                    el.css({
                        width: num2,
                        height: num1
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').width(num2-40).height(num1-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    }
                } else if(swmove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh + y);
                    var num2=(ow - x);
                    var num3=(oleft + x)+'px';
                    el.css({
                    	height: num1,
                        width: num2,
                        left: num3
                    });
                    
                    var reIndex = el.attr('data-number');
		        	if (reIndex) {
		        		el.find('.box-chart').width(num2-40).height(num1-40);
		        		charts[parseInt(reIndex)].resize();
		        	}
		        	
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    	$('.js-box-x').find('input').val(num3.slice(0,-2));
                    }
                } else if(drag) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oleft + x).toFixed(0).slice(0,-1) + '0px';
                    var num2=(otop + y).toFixed(0).slice(0,-1) + '0px';
                	el.css({
                        left: num1,
                        top: num2
                    });
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-x').find('input').val(num1.slice(0,-2));
                    	$('.js-box-y').find('input').val(num2.slice(0,-2));
                    }
                }
            }).on('mouseup', function(e) {
            	var $num=$('input[type="range"]').val()/100;
                if(emove) {
                    var x = (e.pageX - ox)/$num;
                    var num=(x + ow).toFixed(0).slice(0,-1)+'0';
                    el.css({
                        width: num
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num - 40);
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num);
                    }
                } else if(smove) {
                    var y = (e.pageY - oy)/$num;
                    var num=(oh + y).toFixed(0).slice(0,-1)+'0';
                    el.css({
                        height: num
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#h').val(num - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-height').find('input').val(num);
                    }
                } else if(wmove) {
                    var x = (e.pageX - ox)/$num;
                    var num=(ow - x + 10).toFixed(0).slice(0,-1)+'0';
                    var num1=(oleft + x).toFixed(0).slice(0,-1)+'0px'; 
                    el.css({
                        width: num,
                        left: num1
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num - 40);
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num);
                    	$('.js-box-x').find('input').val(num1);
                    }
                } else if(nmove) {
                    var y = (e.pageY - oy)/$num;
                    var num=(oh - y + 10).toFixed(0).slice(0,-1)+'0';
                    var num1=(otop + y).toFixed(0).slice(0,-1)+'0px';
                    el.css({
                        height: num,
                        top: num1
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#h').val(num - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-height').find('input').val(num);
                    	$('.js-box-y').find('input').val(num1);
                    }
                } else if(nemove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh - y + 10).toFixed(0).slice(0,-1)+'0';
                    var num2=(ow + x + 10).toFixed(0).slice(0,-1)+'0';
                    var num3=(otop + y).toFixed(0).slice(0,-1)+'0px';
                    el.css({
                        height: num1,
                        width: num2,
                        top: num3
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    	$('.js-box-y').find('input').val(num3);
                    }
                } else if(nwmove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh - y + 10).toFixed(0).slice(0,-1)+'0';
                    var num2=(ow - x + 10).toFixed(0).slice(0,-1)+'0';
                    var num3=(otop + y).toFixed(0).slice(0,-1)+'0px';
                    var num4=(oleft + x).toFixed(0).slice(0,-1)+'0px';
                    el.css({
                        height: num1,
                        width: num2,
                        top: num3,
                        left: num4
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    	$('.js-box-x').find('input').val(num4);
                    	$('.js-box-y').find('input').val(num3);
                    }
                } else if(semove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh + y).toFixed(0).slice(0,-1)+'0';
                    var num2=(ow + x).toFixed(0).slice(0,-1)+'0';
                    el.css({
                        width: num2,
                        height: num1
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    }
                } else if(swmove) {
                    var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oh + y + 10).toFixed(0).slice(0,-1)+'0';
                    var num2=(ow - x + 10).toFixed(0).slice(0,-1)+'0';
                    var num3=(oleft + x).toFixed(0).slice(0,-1)+'0px';
                    el.css({
                    	height: num1,
                        width: num2,
                        left: num3,
                    });
                    if($(this).children().has('.bg-self')){
                    	$('#w').val(num2 - 40);
                    	$('#h').val(num1 - 40)
                    }
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-width').find('input').val(num2);
                    	$('.js-box-height').find('input').val(num1);
                    	$('.js-box-x').find('input').val(num3);
                    }
                } else if(drag) {
                	var x = (e.pageX - ox)/$num;
                    var y = (e.pageY - oy)/$num;
                    var num1=(oleft + x).toFixed(0).slice(0,-1) + '0';
                    var num2=(otop + y).toFixed(0).slice(0,-1) + '0';
                    if($(this).children().has('.box-selfs')){
                    	$('.js-box-x').find('input').val(num1);
                    	$('.js-box-y').find('input').val(num2);
                    }
                }
               	emove = false;
               	smove = false;
               	wmove = false;
               	nmove = false;
               	nemove = false;
               	nwmove = false;
               	swmove = false;
              	semove = false;
               	drag = false;
               	
            });
        },
        /**
         *  点击item显示拖拽面板
         */
        bindTrigger: function(el) {
            var self = this;
            el.on('mouseover', function(e) {
                e.stopPropagation();
                self.triggerResize(el);
                el.addClass('bordered');
            });
        },
        /**
         *  点击舞台空闲区域 隐藏缩放面板
         */
        bindHidePanel: function(el) {
            var stage = this.options.stage;
            var item = this.options.item;
            $(item).bind('mouseout', function() {
            	$(item).removeClass('bordered');
                $(item).find('.box-btn').css({
                    display: 'none'
                });
            })
        }
    };
 
    window.ZResize = ZResize;
 
})(jQuery);

