(function (root) {
    $(document).mousemove(function (e) {
        var $w = $('.temp-wrap-big.block').width() - $(window).scrollLeft();
        var $h = $(window).height() + $(window).scrollTop();
        x = e.pageX;
        y = e.pageY;
        if (y > ($h - 40)) {
            $('.flex-bar').addClass('fix-bottom').height(40)
        } else {
            $('.flex-bar').height(0)
        }
        if (y < 55 && x > ($w - 36)) {
            $('.js-full').addClass('active')
        } else {
            $('.js-full').removeClass('active')
        }
    });
    let dataVCommon = {};
    $('body').on('click', '.js-full', function () {
        if($(this).find('span').text() == '全屏'){
            $('.js-full span').text('退出');
            fullScreen();
        }else{
            $('.js-full span').text('全屏');
            exitFullScreen();
        }
    });

    $(window).keydown(function (event) {
        if (event.keyCode == 122) {
            $('.js-full span').text('退出');
        }
    });
    $(window).resize(function(){
        if(!checkFull()){
            $('.js-full span').text('全屏');
        }
    });
//是否是全屏
    function checkFull(){
        var isFull =  document.fullscreenEnabled || window.fullScreen || document.webkitIsFullScreen || document.msFullscreenEnabled;
        if(isFull === undefined) {
            isFull = false;
        }
        return isFull;
    }

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

    function exitFullScreen(el) {
        var el = document,
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
}(this));