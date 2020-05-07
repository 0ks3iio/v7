(function () {
    var myChart = {};
    var root = this;
    /**
     * @see HTMLElement
     * @param dom
     */
    myChart.init = function (dom, option, chartType, title, edit) {
        if (chartType != 97)
        $(dom).css('font-size','').removeClass('vetically-center');
        var type = parseInt(chartType);
        if (type === 99) {
            var tableHtml = buildTableHtml($(dom).attr("id"),option, edit);
            var real = buildBoxHtml(title, edit, tableHtml);
            $(dom).html(real);
            setTimeout(function () {
                slid(dom)
            }, 2000);
            return;
        }
        //总数对比图
        else if (type === 98) {
            var h = buildBoxHtml(option.title, edit, buildNumberHtml(option, edit));
            $(dom).html(h);
        }
        else if (type === 97) {
            //先判断dom节点是否存在，不存在就更新
            if ($(dom).find(".timer").length>0) {
                var $timer = $(dom).find(".timer");
                $timer.text(option.value);
            } else {
                $(dom).html(buildBoxHtml(option.title, edit, buildDynamicNumber(option)));
            }
            fontEffects(dom, $(dom).find(".timer").text().replace(",", ""), option.value);
        } else if (type == 95) {
            //设置字体大小
            if (option.value<0) {
                $(dom).html(buildBoxHtml(option.title, edit, buildDownNumber(option)))
            } else{
                $(dom).html(buildBoxHtml(option.title, edit, buildUpNumber(option)));
            }
        } else if (type == 96) {
            $(dom).html(buildBoxHtml(option.title, edit, buildDownNumber(option)))
        }
    };

    function buildUpNumber(option) {
        return '<div class="data-change data-change-up width-1of1 vetically text-center">\n' +
            '<span>'+Math.abs(option.value)+'<i class="wpfont icon-arrow-up"></i></span>\n' +
            '</div>';
    }

    function buildDownNumber(option) {
        return '<div class="data-change data-change-down width-1of1 vetically text-center">\n' +
            '<span>'+Math.abs(option.value)+'<i class="wpfont icon-arrow-down"></i></span>\n' +
            '</div>';
    }
    
    function buildDynamicNumber(option) {
        return '<div class="number-self timer count-title" data-to="' + option.value + '" data-speed="100">' + option.value + '</div>';
    }

    function buildBoxHtml(title, edit, innerHtml) {
        var boxEdit = '<div class="box-self box-sm">';
        if (edit) {
            return boxEdit +
                '<div class="box-body-self box-body" id="box-body">'
                + innerHtml + '</div></div>';
        }
        else {
            return innerHtml;
        }
    }

    function slid(dom) {
        var $height=$(dom).height()-$(dom).find('.'+$(dom).attr("id")).height();
        if($height<0 && $('#slid_' + $(dom).attr('id') + '_' + $height).length<1){
            var overHeight=parseFloat(($height+"").slice(1));
            var sec;
            if(overHeight<200){
                sec=2;
            }else if(overHeight>=200&&overHeight<1000){
                sec=parseInt((overHeight+"").slice(0,1))*2;
            }else if(overHeight>1000||overHeight==1000){
                sec=parseInt((overHeight+"").slice(0,2))*2;
            }
            // var mymove='mover'+overHeight;
            var mymove = $(dom).attr('id');
            $('#slid_' + $(dom).attr('id') + '_' + $height).remove();
            var str='<style id="slid_'+$(dom).attr('id') + '_' + $height +'">@keyframes '+mymove+'{from {top:0px;}to {top:'+ $height+'px;}} .'+mymove+'{position:relative;animation:'+mymove+' '+ sec +'s infinite;}</style>';
            $('head').append(str);
        } else {
            $('#slid_' + $(dom).attr('id') + '_' +$height).remove();
            $(dom).find('.'+$(dom).attr("id")).removeClass($(dom).attr('id'))
        }

    }

    function buildNumberHtml(option, edit) {
        var domHtml = "";
        var ration = option.ration;
        var titleClass = "box-title-self";

        domHtml += '<div class="teacher-sex width-1of1 vetically-center">' +
            '<div class="teacher-sex-male" style="width:' + ration.leftValue + ';">' + ration.leftName + '：' + ration.leftValue + '</div>' +
            '<div class="teacher-sex-female" style="width:' + ration.rightValue + ';">' + ration.rightName + '：' + ration.rightValue + '</div>' +
            '</div>';
        return domHtml;
    }

    //chartType=99 table
    /**
     * 图表Html构建
     * @param option
     * @returns {string}
     */
    function buildTableHtml(domId, option, edit) {
        var ths = option.thead;
        var trs = option.trs;
        var table = '<table class="table '+ domId +'">';
        if (edit) {
            table = '<table class="table-self '+ domId+'">'
        }
        table += '<thead><tr>';
        for (var i = 0; i < ths.length; i++) {
            table += '<td>' + ths[i] + '</td>';
        }
        table += '</tr></thead>'

        table += '<tbody>';

        for (var tl = 0; tl < trs.length; tl++) {
            var tds = trs[tl].tds;
            table += '<tr>';
            for (var s = 0; s < tds.length; s++) {
                table += '<td>' + tds[s] + '</td>';
            }
            table += '</tr>';
        }

        table += '</tbody>';
        table += '</table>';
        return table;
    };

    function fontEffects(dom, fromValue, toValue) {
        //字体特效
        $.fn.countTo = function (options) {
            options = options || {};
            $(this).attr("data-to", fromValue);
            return $(this).each(function () {
                // set options for current element
                var settings = $.extend({}, $.fn.countTo.defaults, {
                    from: $(this).data('from'),
                    to: $(this).data('to'),
                    speed: $(this).data('speed'),
                    refreshInterval: $(this).data('refresh-interval'),
                    decimals: $(this).data('decimals')
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
                    value = toValue;
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
        $(dom).find('.timer').data('countToOptions', {
            formatter: function (value, options) {
                return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, ',');
            }
        });

        // start all the timers
        $(dom).find('.timer').each(count);

        function count(options) {
            var $this = $(this);
            options = $.extend({}, options || {}, $this.data('countToOptions') || {});
            $this.countTo(options);
        }

        $(dom).find('.count-title').addClass('vetically-center');
    }

    root.myChart = myChart;
}).call(this);