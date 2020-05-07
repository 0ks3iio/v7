window.onload = function () {
    var wSelf = document.getElementById('w-self');
    var hSelf = document.getElementById('h-self');
    var bg = document.getElementsByClassName('table-bg')[0];
    var outside = document.getElementsByClassName("box-outside");
    var cut = document.getElementsByClassName("flex-wrap")[0];
    var cutWidth = 0;
    var cutHeight = 0;
    var startX = 0;
    var startY = 0;
    var top = 0;
    var left = 0;
    var dir = "";
    for (var i = 0; i < outside.length; i++) {
        outside[i].onmousedown = function (e) {
            startX = e.clientX;
            startY = e.clientY;
            cutWidth = cut.offsetWidth;
            cutHeight = cut.offsetHeight;
            top = cut.offsetTop;
            left = cut.offsetLeft;
            var className = this.className;
            if (className.indexOf("box-right") > -1) {
                dir = "E";
            }
            else if (className.indexOf("box-left") > -1) {
                dir = "W";
            }
            else if (className.indexOf("box-bottom") > -1) {
                dir = "S";
            }
            else if (className.indexOf("box-top") > -1) {
                dir = "N";
            }
            else if (className.indexOf("box-top-left") > -1) {
                dir = "NW";
            }
            else if (className.indexOf("box-top-right") > -1) {
                dir = "NE";
            }
            else if (className.indexOf("box-bottom-left") > -1) {
                dir = "SW";
            }
            else if (className.indexOf("box-bottom-right") > -1) {
                dir = "SE";
            }
            document.addEventListener('mousemove', test);
            e.preventDefault();
        }
    }

    document.onmouseup = function (e) {
        dir = "";
        document.removeEventListener('mousemove', test);
        e.preventDefault();
    };

    function test(e) {
        var width = e.clientX - startX;
        var height = e.clientY - startY;
        if (dir == "E") {
            cut.style.width = cutWidth + width + "px";
            wSelf.value = cutWidth + width - 60 + "px";
        }
        else if (dir == "S") {
            cut.style.height = cutHeight + height + "px";
            hSelf.value = cutHeight + height - 60 + "px";
        }
    }

};

(function ($) {

    /**
     * 默认参数
     */
    var defaultOpts = {
        stage: document, //舞台
        item: 'resize-item', //可缩放的类名
        mouseupCall: function () {

        },
        mousemoveCall: function () {

        },
        mousedownCall: function () {

        },
        oLeft: 0,
        oTop: 0,
        lock: false,
    };

    /**
     * 定义类
     */
    var ZResize = function (options) {
        this.options = $.extend({}, defaultOpts, options);
        this.init();
    };
    var z;
    ZResize.prototype = {
        init: function () {
            this.initResizeBox();
        },
        /**
         *  初始化拖拽item
         */
        initResizeBox: function () {
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
                    'width': '15px',
                    'background': '#00cce3',
                    'top': '-4px',
                    'margin-left': '-3px',
                    'left': '50%',
                    'cursor': 'n-resize'
                });
                s.css({
                    'width': '15px',
                    'background': '#00cce3',
                    'bottom': '-4px',
                    'margin-left': '-3px',
                    'left': '50%',
                    'cursor': 's-resize'
                });
                e.css({
                    'height': '15px',
                    'background': '#00cce3',
                    'top': '50%',
                    'margin-top': '-8px',
                    'right': '-4px',
                    'cursor': 'e-resize'
                });
                w.css({
                    'height': '15px',
                    'background': '#00cce3',
                    'top': '50%',
                    'margin-top': '-8px',
                    'left': '-4px',
                    'cursor': 'w-resize'
                });
                ne.css({
                    'width': '15px',
                    'height': '15px',
                    'border-top': '6px solid #00cce3',
                    'border-right': '6px solid #00cce3',
                    'top': '-6px',
                    'right': '-6px',
                    'cursor': 'ne-resize'
                });
                nw.css({
                    'width': '15px',
                    'height': '15px',
                    'border-top': '6px solid #00cce3',
                    'border-left': '6px solid #00cce3',
                    'top': '-6px',
                    'left': '-6px',
                    'cursor': 'nw-resize'
                });
                se.css({
                    'width': '15px',
                    'height': '15px',
                    'border-bottom': '6px solid #00cce3',
                    'border-right': '6px solid #00cce3',
                    'bottom': '-6px',
                    'right': '-6px',
                    'cursor': 'se-resize'
                });
                sw.css({
                    'width': '15px',
                    'height': '15px',
                    'border-bottom': '6px solid #00cce3',
                    'border-left': '6px solid #00cce3',
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
                $(this).data('resize-options', self.options);
            });
            self.bindHidePanel();
        },
        //控制点公共样式
        addHandlerCss: function (els) {
            for (var i = 0; i < els.length; i++) {
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
        appendHandler: function (handlers, target) {
            for (var i = 0; i < handlers.length; i++) {
                el = handlers[i];
                target.append(el);
            }
        },
        /**
         *  显示拖拽面板
         */
        triggerResize: function (el) {
            var self = this;
            el.siblings(self.options.item).children('.box-btn').css({
                display: 'none'
            });
            el.children('div').css({
                display: 'block'
            });
        },
        /**
         * 拖拽事件控制 包含8个缩放点  和一个拖拽位置
         */

        bindResizeEvent: function (el) {

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
            el.on('mousedown', '.e', function (e) {
                ox = e.pageX;//原始x位置
                ow = el.width();
                emove = true;
            });

            //南
            var smove = false;
            el.on('mousedown', '.s', function (e) {
                oy = e.pageY;//原始x位置
                oh = el.height();
                smove = true;
            });

            //西
            var wmove = false;
            el.on('mousedown', '.w', function (e) {
                ox = e.pageX;//原始x位置
                ow = el.width();
                wmove = true;
                oleft = parseInt(org.css('left').replace('px', ''));
            });

            //北
            var nmove = false;
            el.on('mousedown', '.n', function (e) {
                oy = e.pageY;//原始x位置
                oh = el.height();
                nmove = true;
                otop = parseInt(org.css('top').replace('px', ''));
            });

            //东北
            var nemove = false;
            el.on('mousedown', '.ne', function (e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
                nemove = true;
                otop = parseInt(org.css('top').replace('px', ''));
            });

            //西北
            var nwmove = false;
            el.on('mousedown', '.nw', function (e) {
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
            el.on('mousedown', '.se', function (e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
                semove = true;
            });

            //西南
            var swmove = false;
            el.on('mousedown', '.sw', function (e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                ow = el.width();
                oh = el.height();
                swmove = true;
                oleft = parseInt(org.css('left').replace('px', ''));

            });

            //拖拽
            var drag = false;
            el.on('mousedown', function (e) {
                ox = e.pageX;//原始x位置
                oy = e.pageY;
                otop = parseInt(org.css('top').replace('px', ''));
                oleft = parseInt(org.css('left').replace('px', ''));
                self.options.oLeft = oleft;
                self.options.oTop = otop;
                if (e.which != 3) {
                    drag = true;
                }
                if (e.target.className !== "key-box") {
                    $('.key-box').hide()
                }
                self.options.mousedownCall({el: el});
            }).on('mouseup', function (e) {
                var reIndex = el.attr('data-number');
                if (reIndex) {
                    //charts[parseInt(reIndex)].resize();
                }
            });
            $(self.options.stage).on('mousemove', function (e) {

                if (e.button === 1 || self.options.lock) {
                    return;
                }
                var $num = $('input[type="range"]').val() / 100;
                if (emove) {
                    var x = (e.pageX - ox) / $num;
                    var num = (x + ow).toFixed(0).slice(0, -1) + '0';
                    el.css({
                        width: num
                    });
                } else if (smove) {
                    var y = (e.pageY - oy) / $num;
                    var num = (oh + y).toFixed(0).slice(0, -1) + '0';
                    el.css({
                        height: num
                    });
                } else if (wmove) {
                    var x = (e.pageX - ox) / $num;
                    var num = (ow - x).toFixed(0).slice(0, -1) + '0';
                    var num1 = (oleft + x).toFixed(0).slice(0, -1) + '0px';
                    el.css({
                        width: num,
                        left: num1
                    });
                } else if (nmove) {
                    var y = (e.pageY - oy) / $num;
                    var num = (oh - y).toFixed(0).slice(0, -1) + '0';
                    var num1 = (otop + y).toFixed(0).slice(0, -1) + '0px';
                    el.css({
                        height: num,
                        top: num1
                    });
                } else if (nemove) {
                    var x = (e.pageX - ox) / $num;
                    var y = (e.pageY - oy) / $num;
                    var num1 = (oh - y).toFixed(0).slice(0, -1) + '0';
                    var num2 = (ow + x).toFixed(0).slice(0, -1) + '0';
                    var num3 = (otop + y).toFixed(0).slice(0, -1) + '0px';
                    el.css({
                        height: num1,
                        top: num3,
                        width: num2
                    });
                } else if (nwmove) {
                    var x = (e.pageX - ox) / $num;
                    var y = (e.pageY - oy) / $num;
                    var num1 = (oh - y).toFixed(0).slice(0, -1) + '0';
                    var num2 = (ow - x).toFixed(0).slice(0, -1) + '0';
                    var num3 = (otop + y).toFixed(0).slice(0, -1) + '0px';
                    var num4 = (oleft + x).toFixed(0).slice(0, -1) + '0px';
                    el.css({
                        height: num1,
                        top: num3,
                        width: num2,
                        left: num4
                    });
                } else if (semove) {
                    var x = (e.pageX - ox) / $num;
                    var y = (e.pageY - oy) / $num;
                    var num1 = (oh + y).toFixed(0).slice(0, -1) + '0';
                    var num2 = (ow + x).toFixed(0).slice(0, -1) + '0';
                    el.css({
                        width: num2,
                        height: num1
                    });
                } else if (swmove) {
                    var x = (e.pageX - ox) / $num;
                    var y = (e.pageY - oy) / $num;
                    var num1 = (oh + y).toFixed(0).slice(0, -1) + '0';
                    var num2 = (ow - x).toFixed(0).slice(0, -1) + '0';
                    var num3 = (oleft + x).toFixed(0).slice(0, -1) + '0px';
                    el.css({
                        width: num2,
                        left: num3,
                        height: num1
                    });
                } else if (drag) {
                    var x = (e.pageX - ox) / $num;
                    var y = (e.pageY - oy) / $num;
                    var num1 = (oleft + x).toFixed(0).slice(0, -1) + '0px';
                    var num2 = (otop + y).toFixed(0).slice(0, -1) + '0px';
                    el.css({
                        left: num1,
                        top: num2
                    });
                    el.attr('offsetX', x);
                    el.attr('offsetY', y);
                }


                if ((semove || smove || swmove || nemove || nmove || nwmove || emove || wmove || drag)) {
                    self.options.move = true;
                    self.options.mousemoveCall({
                        left: el.css('left').replace('px', ''),
                        top: el.css('top').replace('px', ''),
                        width: el.width(),
                        height: el.height(),
                        el: el
                    });

                    //判定是否选中了其他的图表,通知其他图表偏移量
                    var offsetX = el.attr('offsetX');
                    var offsetY = el.attr('offsetY');
                    $('.box-data.choose').each(function (index, ele) {
                        if ($(ele).data('index') != el.data('index')) {
                            var options = $(ele).data('resize-options');
                            if (!options.lock) {
                                var left = parseInt($(ele).css('left').replace('px', ''));
                                if (options.oLeft === 0) {
                                    options.oLeft = left;
                                    console.log("oLeft new:" + options.oLeft);
                                }
                                left = (options.oLeft + parseInt(offsetX)).toFixed(0).slice(0, -1) + '0';

                                var top = parseInt($(ele).css('top').replace('px', ''));
                                if (options.oTop === 0) {
                                    options.oTop = top;
                                }
                                top = (options.oTop + parseInt(offsetY)).toFixed(0).slice(0, -1) + '0';
                                $(ele).css({left: left + 'px', top: top + 'px'})

                                $(ele).data('resize-options').mousemoveCall({
                                    left: left,
                                    top: top,
                                    width: $(ele).width(),
                                    height: $(ele).height(),
                                    el: $(ele)
                                })
                            }
                        }
                    })
                }

            }).on('mouseup', function (e) {
                if (self.options.lock) {
                    el.removeClass('zIndex');
                    emove = false;
                    smove = false;
                    wmove = false;
                    nmove = false;
                    nemove = false;
                    nwmove = false;
                    swmove = false;
                    semove = false;
                    drag = false;
                    return;
                }
                //TODO 点击仍然会触发鼠标移动事件
                if ((semove || smove || swmove || nemove || nmove || nwmove || emove || wmove || drag) && self.options.move) {
                    self.options.move = false;
                    try {
                        self.options.mouseupCall({
                            left: el.css('left').replace('px', ''),
                            top: el.css('top').replace('px', ''),
                            width: el.width(),
                            height: el.height(),
                            el: el
                        });
                        self.options.oLeft = 0;
                        self.options.oTop = 0;
                    } catch (e) {

                    }

                    //通知其他选中的图表后台更新
                    $('.box-data.choose').each(function (index, ele) {
                        if ($(ele).data('index') != el.data('index')) {
                            var options = $(ele).data('resize-options');
                            if (!options.lock) {
                                options.oLeft = 0;
                                options.oTop = 0;
                                var left = $(ele).css('left').replace('px', '');
                                var top = $(ele).css('top').replace('px', '');

                                $(ele).data('resize-options').mouseupCall({
                                    left: left,
                                    top: top,
                                    width: $(ele).width(),
                                    height: $(ele).height(),
                                    el: $(ele)
                                })
                            }
                        }
                    })
                }

                el.removeClass('zIndex');
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
        bindTrigger: function (el) {
            var self = this;
            el.on('mouseover', function (e) {
                e.stopPropagation();
                if (!self.options.lock) {
                    self.triggerResize(el);
                    el.addClass('bordered');
                }
            });
        },
        /**
         *  点击舞台空闲区域 隐藏缩放面板
         */
        bindHidePanel: function (el) {
            var stage = this.options.stage;
            var item = this.options.item;
            $(item).bind('mouseout', function () {
                $(item).removeClass('bordered');
                $(item).find('.box-btn').css({
                    display: 'none'
                });
            })
        }
    };

    window.ZResize = ZResize;

})(jQuery);