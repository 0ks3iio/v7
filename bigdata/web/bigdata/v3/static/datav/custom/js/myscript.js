$(function(){
    //tabs
    $('.tabs-card').tabs({
    	evt: 1,
    	ant: 2
    });
    
    //下拉菜单
    $('.dropdown-toggle').not(':disabled').click(function(e){
        e.preventDefault();
        $('.dropdown-toggle').each(function(){
            var $g = $(this).parent('.btn-group'); 
            if ($g.hasClass('open')) {
                $g.removeClass('open');
            }
        });
        $(this).parent('.btn-group').toggleClass('open');
    });
    $('.dropdown-menu li:not(.disabled)').click(function(e){
        //e.preventDefault();
        var $btnGroup = $(this).parents('.btn-group');
        $(this).addClass('active').siblings('li').removeClass('active');
        $btnGroup.removeClass('open');
        if ($btnGroup.hasClass('filter')) {
            e.preventDefault();
            var txt = $(this).text() + '<i class="wpfont icon-angle-single-down"></i>';
            $btnGroup.children('.dropdown-toggle').html(txt);
        } else if($btnGroup.hasClass('filter2')){
            e.preventDefault();
            var txt = $(this).text() + '<i class="wpfont icon-caret-down"></i>';
            $btnGroup.children('.dropdown-toggle').html(txt);
        }
    });
    $(document).click(function(event){
        var eo=$(event.target);
        if($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-toggle' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
        $('.btn-group').removeClass('open');
    });
    
    //全屏
    var fs = true;
    $('body').on('click','.full-screen-wrap',function(){
        if (fs){
            fullScreen();
        } else{
            exitFullScreen();
        }
        fs = !fs;
    });
    //F11
    function fullScreen() {
        var el = document.documentElement;
        var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen;
        if (typeof rfs != "undefined" && rfs) {
            rfs.call(el);
        } else if (typeof window.ActiveXObject != "undefined") {
            var wscript = new ActiveXObject("WScript.Shell");
            if (wscript != null) {
                wscript.SendKeys("{F11}");
            }
        }
    }
    //取消F11
    function exitFullScreen(el) {
        var el= document,
            cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen,
            wscript;
        if (typeof cfs != "undefined" && cfs) {
          cfs.call(el);
          return;
        }
        if (typeof window.ActiveXObject != "undefined") {
            wscript = new ActiveXObject("WScript.Shell");
            if (wscript != null) {
                wscript.SendKeys("{F11}");
            }
        }
    }
    
    //头部切换按钮
    $('.box-data-scope>.btn').on('click',function(){
        if ($(this).hasClass('active') == false){
            $(this).addClass('active').siblings('.btn').removeClass('active')
        }
    });
    //左边切换按钮
    $('.left-btn-wrap li').on('click',function(){
        if ($(this).hasClass('active') == false){
            $(this).addClass('active').siblings().removeClass('active')
        }
    });
    
    //字体翻滚
    function fontEffects(){
        $.fn.countTo = function (options) {
            options = options || {};
            return $(this).each(function () {
                // set options for current element
                var settings = $.extend({}, $.fn.countTo.defaults, {
                    from:            $(this).data('from'),
                    to:              $(this).data('to'),
                    speed:           $(this).data('speed'),
                    refreshInterval: $(this).data('refresh-interval'),
                    decimals:        $(this).data('decimals')
                }, options);
    
                // how many times to update the value, and how much to increment the value on each update
                var loops = Math.ceil(settings.speed / settings.refreshInterval),
                    increment = (settings.to - settings.from) / loops;
    
                // references & variables that will change with each update
                var self = this,
                    $self = $(this),
                    loopCount = 0,
                    value = settings.from,
                    data = $self.data('countTo') || {};
    
                $self.data('countTo', data);
    
                // if an existing interval can be found, clear it first
                if (data.interval) {
                    clearInterval(data.interval);
                }
                data.interval = setInterval(updateTimer, settings.refreshInterval);
    
                // initialize the element with the starting value
                render(value);
    
                function updateTimer() {
                    value += increment;
                    loopCount++;
    
                    render(value);
    
                    if (typeof(settings.onUpdate) == 'function') {
                        settings.onUpdate.call(self, value);
                    }
    
                    if (loopCount >= loops) {
                        // remove the interval
                        $self.removeData('countTo');
                        clearInterval(data.interval);
                        value = settings.to;
    
                        if (typeof(settings.onComplete) == 'function') {
                            settings.onComplete.call(self, value);
                        }
                    }
                }
    
                function render(value) {
                    //value=toValue;
                    var formattedValue = settings.formatter.call(self, value, settings);
                    $self.html(formattedValue);
                }
    
            });
        };
    
        $.fn.countTo.defaults = {
            from: 0,               // the number the element should start at
            to: 0,                 // the number the element should end at
            speed: 1000,           // how long it should take to count between the target numbers
            refreshInterval: 100,  // how often the element should be updated
            decimals: 0,           // the number of decimal places to show
            formatter: formatter,  // handler for formatting the value before rendering
            onUpdate: null,        // callback method for every time the element is updated
            onComplete: null       // callback method for when the element finishes updating
        };
    
        function formatter(value, settings) {
            return value.toFixed(settings.decimals);
        }
    
        // custom formatting example
        $('.js-two .font-quartz').data('countToOptions', {
            formatter: function (value, options) {
                return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, '');
            }
        });
    
        // start all the timers
        $('.js-two .font-quartz').each(count);
    
        function count(options) {
            var $this = $(this);
            options = $.extend({}, options || {}, $this.data('countToOptions') || {});
            $this.countTo(options);
        }
    }
    
})
 