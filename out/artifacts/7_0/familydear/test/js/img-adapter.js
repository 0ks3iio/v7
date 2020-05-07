/*
 *	img-adapter.js
 *	copyright: liyuan
 *	license: MIT
 *
 *	用途：此插件实现的功能与background-size:cover;的功能一样，只不过不是使用背景图片的方式实现；
 		  当你需要在固定大小的容器里显示任意尺寸大小的图片时，你可能会用到它。
 		  不依赖任何库或者框架
 *	使用方法：对需要适配的图片调用imgAdapter方法
*/

var imgAdapter = function(images){
    // 获取元素的大小
    function getSize(el){
        var style = document.defaultView.getComputedStyle(el);
        return {
            width: parseFloat(style.width),
            height: parseFloat(style.height)
        };
    }

    // 适配图片
    function adapter(img){
        var axis = '';
        var scale;
        var wrapper = img.parentNode;
        // var img_w = getSize(img).width;
        // var img_h = getSize(img).height;
        // var wrapper_w = getSize(wrapper).width;
        // var wrapper_h = getSize(wrapper).height;
        var img_w = img.offsetWidth;
        var img_h = img.offsetHeight;
        var wrapper_w = wrapper.offsetWidth;
        var wrapper_h = wrapper.offsetHeight;
        var scaleX = img_w / wrapper_w;
        var scaleY = img_h / wrapper_h;

        wrapper.style.cssText = 'position:relative;overflow:hidden;';

        // 以比例小的那一轴为缩放基准
        if (scaleX <= scaleY) {
            axis = 'x';
            scale = scaleX;
        }else{
            axis = 'y';
            scale = scaleY;
        }
        reScale(axis, scale);

        // 从新计算图片的大小与显示
        function reScale(axis, scale){
            var diff;
            img_w = img_w / scale;
            img_h = img_h / scale;

            var img_style = 'position:absolute;width:' + img_w + 'px;height:' + img_h + 'px;';

            if(axis === 'x'){
                diff = (img_h - wrapper_h) / 2;
                img.style.cssText = img_style + 'top:-' + diff + 'px;left:0';
            }else{
                diff = (img_w - wrapper_w) / 2;
                img.style.cssText = img_style + 'left:-' + diff + 'px;top:0;';
            }
        }
    }

    // 初始化计算
    function init(){
        for(var i = 0; i < images.length; i++){
            adapter(images[i]);
        }
    }

    //计时器
    var requestAF = (function() {
        return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || function(callback) {
            window.setTimeout(callback, 1000 / 60);
        };
    })();

    // 第一次计算
    init();

    // 触发resize事件后从新计算
    window.onresize = function(){
        requestAF(init);
    };

};