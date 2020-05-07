
if (typeof jQuery === 'undefined') {
    throw new Error("szxyTree require jQuery");
}
(function ($) {
    $.fn.extend({
       szxyTree: function () {
           var $treeContainer = $(this);
           $(this).on('click', '.chosen-tree-item .arrow', function (e) {
               e.stopPropagation();
               var $scroll = $($treeContainer.find('.page-sidebar-body')[0]),
                   $tree = $scroll.children('.chosen-tree'),
                   $li = $(this).parent('.chosen-tree-item').parent('li');
               if ($li.hasClass('sub-tree')) {
                   $li.toggleClass('open');
               }
               $scroll.scrollLeft(500);
               var sLeft = $scroll.scrollLeft(),
                   sWidth = $scroll.width(),
                   tWidth = sLeft + sWidth;
               $tree.width(tWidth);
           });
           $(this).on('click', '.chosen-tree-item', function (e) {
               var $li = $(this).parent('li');
               $('.chosen-tree li').removeClass('active');
               $li.addClass('active');
           });
       }
    });

    //*****=S 对比功能
    // if ($('.page-sidebar-compare').length) {
    //     var $body = $('.page-sidebar-body'),
    //         $footer = $('.page-sidebar-footer'),
    //         $btn = $('.chosen-compare-btn'),
    //         $list = $('.chosen-compare-list');
    //     //滑过显示对比按钮
    //     $('.page-sidebar-compare .chosen-tree-item').hover(function(){
    //         var $li = $(this).parent('li');
    //         if ($li.hasClass('report') || $li.hasClass('sub-tree')) {
    //             $(this).append('<span class="compare">+对比</span>');
    //         }
    //         if ($li.hasClass('is-lock')) {
    //             $(this).append('<span class="compare">已加入</span>');
    //         }
    //         var sLeft = $('.page-sidebar-body').scrollLeft(),
    //             sRight = $('.page-sidebar-body').children('.chosen-tree').width() - $('.page-sidebar-body').width() + 10 - sLeft;
    //         $(this).children('.compare').css('right', sRight);
    //     },function(){
    //         $(this).children('.compare').remove();
    //     });
    //     //加入对比项
    //     $('.page-sidebar-compare').on('click', '.chosen-tree-item .compare', function(e){
    //         e.stopPropagation();
    //         var len = $list.children('li').length,
    //             txt = $(this).siblings('.name').text(),
    //             index = $(this).parent('.chosen-tree-item').data('index');
    //         if (len >= 2) {
    //             layer.msg('最多只能选择两个机构，请删除一个后再选择');
    //         } else{
    //             $(this).text('已加入').parent('.chosen-tree-item').parent('li').addClass('is-lock');
    //             $list.show().append('<li data-index="' + index + '"><a href="#" class="remove"></a>'+ txt + '</li>');
    //             var sLen = $list.children('li').length;
    //             $btn.children('span').show().text('（' + sLen + '）');
    //             if (sLen === 2) {
    //                 $btn.removeAttr('disabled');
    //             }
    //             //获取高度，处理滚动条
    //             var footerH = $footer.height();
    //             $body.css('bottom', footerH);
    //         }
    //     });
    //     //删除对比项
    //     $('.chosen-compare-list').on('click', 'li .remove', function(e){
    //         var $li = $(this).parent('li'),
    //             index = $li.data('index');
    //         $li.remove();
    //         var len = $list.children('li').length;
    //         $btn.children('span').text('（' + len + '）');
    //         $('.chosen-tree-item[data-index="' + index + '"]').parent('li').removeClass('is-lock');
    //         $btn.attr('disabled','disabled');
    //         if (len === 0) {
    //             $list.hide();
    //             $btn.children('span').hide();
    //         }
    //         //获取高度，处理滚动条
    //         var footerH = $footer.height();
    //         $body.css('bottom', footerH);
    //     });
    // }
}(window.jQuery));