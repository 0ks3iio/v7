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
    }
 
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
                var resizePanel = $('<div class="resize-panel"></div>');
                resizePanel.css({
                    width: width,
                    height: height,
                    top: 0,
                    left: 0,
                    position: 'absolute',
                    //'background-color': 'rgba(0,0,0,0.5)',
                    cursor: 'move',
                    display: 'none'
                });
                self.appendHandler(resizePanel, $(this));
                /**
                 * 创建控制点
                 */
                var n = $('<div class="n"></div>');//北
                var s = $('<div class="s"></div>');//南
                var w = $('<div class="w"></div>');//西
                var e = $('<div class="e"></div>');//东
                var ne = $('<div class="ne"></div>');//东北
                var nw = $('<div class="nw"></div>');//西北
                var se = $('<div class="se"></div>');//东南
                var sw = $('<div class="sw"></div>');//西南
 
                //添加公共样式
                self.addHandlerCss([n, s, w, e, ne, nw, se, sw]);
                //添加各自样式
                n.css({
                	'width':'15px',
                	'background': '#00cce3',
                    'top': '-4px',
                    'margin-left': '-2px',
                    'left': '50%',
                    'cursor': 'n-resize'
                });
                s.css({
                	'width':'15px',
                	'background': '#00cce3',
                    'bottom': '-4px',
                    'margin-left': '-2px',
                    'left': '50%',
                    'cursor': 's-resize'
                });
                e.css({
                	'height':'15px',
                	'background': '#00cce3',
                    'top': '50%',
                    'margin-top': '-2px',
                    'right': '-4px',
                    'cursor': 'e-resize'
                });
                w.css({
                	'height':'15px',
                	'background': '#00cce3',
                    'top': '50%',
                    'margin-top': '-2px',
                    'left': '-4px',
                    'cursor': 'w-resize'
                });
                ne.css({
                	'width':'15px',
                	'height':'15px',
                	'border-top':'3px solid #00cce3',
                	'border-right':'3px solid #00cce3',
                    'top': '-2px',
                    'right': '-2px',
                    'cursor': 'ne-resize'
                });
                nw.css({
                	'width':'15px',
                	'height':'15px',
                	'border-top':'3px solid #00cce3',
                	'border-left':'3px solid #00cce3',
                    'top': '-2px',
                    'left': '-2px',
                    'cursor': 'nw-resize'
                });
                se.css({
                	'width':'15px',
                	'height':'15px',
                	'border-bottom':'3px solid #00cce3',
                	'border-right':'3px solid #00cce3',
                    'bottom': '-2px',
                    'right': '-2px',
                    'cursor': 'se-resize'
                });
                sw.css({
                	'width':'15px',
                	'height':'15px',
                	'border-bottom':'3px solid #00cce3',
                	'border-left':'3px solid #00cce3',
                    'bottom': '-2px',
                    'left': '-2px',
                    'cursor': 'sw-resize'
                });
 
                // 添加项目
                self.appendHandler([n, s, w, e, ne, nw, se, sw], resizePanel);
                
                //绑定拖拽缩放事件
                self.bindResizeEvent(resizePanel, $(this));
 
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
                    //background: '#00cce3',
                    margin: '0',
                    //'border-radius': '2px',
                    //border: '1px solid #00cce3',
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
        	var reIndex = el.parents('.box-to').attr('data-index');
        	if (reIndex) {
        		charts[parseInt(reIndex)].resize();
        	}
            var self = this;
            el.siblings(self.options.item).children('.resize-panel').css({
                display: 'none'
            });
            el.children('div').css({
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
            var org = el.parent('div');
 
            //东
            var emove = false;
            el.on('mousedown','.e', function(e) {
                ox = e.pageX;//原始x位置
                ow = el.width();
                emove = true;
            });
 
            //南
            var smove = false;
            el.on('mousedown','.s', function(e) {
                oy = e.pageY;//原始x位置
                oh = el.height();
                smove = true;
            });
 
            //西
            var wmove = false;
            el.on('mousedown','.w', function(e) {
                ox = e.pageX;//原始x位置
                ow = el.width();
                wmove = true;
                oleft = parseInt(org.css('left').replace('px', ''));
            });
 
            //北
            var nmove = false;
            el.on('mousedown','.n', function(e) {
                oy = e.pageY;//原始x位置
                oh = el.height();
                nmove = true;
                otop = parseInt(org.css('top').replace('px', ''));
            });
 
            //东北
            var nemove = false;
            el.on('mousedown','.ne', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
                nemove = true;
                otop = parseInt(org.css('top').replace('px', ''));
            });
 
            //西北
            var nwmove = false;
            el.on('mousedown','.nw', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
                otop = parseInt(org.css('top').replace('px', ''));
                oleft = parseInt(org.css('left').replace('px', ''));
                nwmove = true;
            });
 
            //东南
            var semove = false;
            el.on('mousedown','.se', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
                semove = true;
            });
 
            //西南
            var swmove = false;
            el.on('mousedown','.sw', function(e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
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
            	var reIndex = el.parents('.box-to').attr('data-index');
	        	if (reIndex) {
	        		charts[parseInt(reIndex)].resize();
	        	}
            });
 
            $(self.options.stage).on('mousemove', function(e) {
                if(emove) {
                    var x = (e.pageX - ox);
                    el.css({
                        width: ow + x
                    });
                    org.css({
                        width: ow + x
                    });
                } else if(smove) {
                    var y = (e.pageY - oy);
                    el.css({
                        height: oh + y
                    });
                    org.css({
                        height: oh + y
                    });
                } else if(wmove) {
                    var x = (e.pageX - ox);
                    el.css({
                        width: ow - x,
                        // left: oleft + x
                    });
                    org.css({
                        width: ow - x,
                        left: oleft + x
                    });
                } else if(nmove) {
                    var y = (e.pageY - oy);
                    el.css({
                        height: oh - y,
                        // top: otop + y
                    });
                    org.css({
                        height: oh - y,
                        top: otop + y
                    });
                } else if(nemove) {
                    var x = e.pageX - ox;
                    var y = e.pageY - oy;
                    el.css({
                        height: oh - y,
                        // top: otop + y,
                        width: ow + x
                    });
                    org.css({
                        height: oh - y,
                        top: otop + y,
                        width: ow + x
                    });
                } else if(nwmove) {
                    var x = e.pageX - ox;
                    var y = e.pageY - oy;
                    el.css({
                        height: oh - y,
                        // top: otop + y,
                        width: ow - x,
                        // left: oleft + x
                    });
                    org.css({
                        height: oh - y,
                        top: otop + y,
                        width: ow - x,
                        left: oleft + x
                    });
                } else if(semove) {
                    var x = e.pageX - ox;
                    var y = e.pageY - oy;
                    el.css({
                        width: ow + x,
                        height: oh + y
                    });
                    org.css({
                        width: ow + x,
                        height: oh + y
                    });
                } else if(swmove) {
                    var x = e.pageX - ox;
                    var y = e.pageY - oy;
                    el.css({
                        width: ow - x,
                        // left: oleft + x,
                        height: oh + y
                    });
                    org.css({
                        width: ow - x,
                        left: oleft + x,
                        height: oh + y
                    });
                } else if(drag) {
                    var x = e.pageX - ox;
                    var y = e.pageY - oy;
                    org.css({
                        left: oleft + x,
                        top: otop + y
                    });
                }
            }).on('mouseup', function(e) {
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
            el.on('click', function(e) {
            	var sIndex = 399;
            	var zIndex = parseInt(el.css('z-index'));
            	if (zIndex !== sIndex) {
            		el.addClass('box-active').attr('data-zIndex', zIndex).css('z-index', sIndex);
            	}
            	el.siblings('.box-active').each(function(){
            		var oIndex = $(this).attr('data-zIndex');
        			$(this).removeClass('box-active').css('z-index', oIndex).removeAttr('data-zIndex');
            	})
                e.stopPropagation();
                self.triggerResize(el);
            });
        },
        /**
         *  点击舞台空闲区域 隐藏缩放面板
         */
        bindHidePanel: function(el) {
            var stage = this.options.stage;
            var item = this.options.item;
            $(item).bind('blur', function() {
                $(item).find('.resize-panel').css({
                    display: 'none'
                });
            })
        }
    }
 
    window.ZResize = ZResize;
 
})(jQuery);