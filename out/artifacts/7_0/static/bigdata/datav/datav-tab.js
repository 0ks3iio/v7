/*
 * 可交互tab
 */
(function (root) {

    $.fn.dataVSimpleTab = function (options) {
        let tab = buildTabs(options.tabs, buildTabStyle(options.tabStyle), buildTabClass(options.tabStyle));
        this.options = $.extend(options, this.options);
        let $tabDom = $(tab);
        this.target = $tabDom;
        $(this).html('').append($tabDom);
        let op = this.options;
        this.target.find('.js-tab-target').on('click', function () {
            //选中的样式
            $(this).css({
                "font-size": op.tabStyle.selectedFontSize,
                "font-weight": op.tabStyle.selectedFontBold,
                "color": op.tabStyle.selectedFontColor
            }).addClass('active').siblings('div').removeClass('active').css({
                "font-size": op.tabStyle.fontSize,
                "font-weight": op.tabStyle.fontBold,
                "color": op.tabStyle.fontColor,
                // "padding": [op.tabStyle.tabWidth, op.tabStyle.tabWidth],
            });

            if (op.tabStyle.category != 'box2') {
                if (op.tabStyle.category != 'box3') {
                    $(this).css({
                        "background-color": op.tabStyle.selectedBoxBackgroundColor
                    }).siblings('div').css({
                        "background-color": op.tabStyle.boxBackgroundColor
                    });
                } else {
                    $(this).css({
                        "background-color": 'transparent'
                    }).siblings('div').css({
                        "background-color": 'transparent'
                    });
                }
            }
            else {
                $(this).css({
                    "border-bottom-color": op.tabStyle.selectedBoxBackgroundColor
                }).siblings('div').css({
                    "border-bottom-color": 'transparent'
                });
                if (op.tabStyle.selectedFontColor == '#FFFFFF' || op.tabStyle.selectedFontColor == '#ffffff') {
                    $(this).css('color', op.tabStyle.selectedBoxBackgroundColor);
                }
            }
        });

        // this.target.find('.js-tab-target').eq(0).trigger('click')
        return this;
    };

    $.fn.dataVSimpleTab.options = {

    };

    function buildTabStyle(style) {
        return ' style="font-size:' + style.fontSize + 'px;font-weight:'
            + style.fontBold + ';color:' + style.fontColor + ';' +
            (style.category == 'box2' ? 'border-bottom: 2px solid transparent;' : 'background-color:' + style.boxBackgroundColor + ';') +
            'padding:' + style.tabHeight + 'px ' + style.tabWidth +
            'px;' +
            (style.orient == 'horizontal' ? 'margin-right:' : 'margin-bottom:') + style.space +
            'px;" '
    }

    function buildTabClass(style) {
        return 'class="js-tab-target tab-box tab-' + style.category + (style.orient == 'horizontal' ? '' : ' line') + '" ';
    }

    function buildTabs(tabs, styleStr, classStr) {
        let html = '';
        for (let t in tabs) {
            html = html + '<div ' + classStr + styleStr + ' id="' + tabs[t].id + '">' + tabs[t].name + '</div>'
        }
        if (classStr.indexOf('box2') > -1) {
            return '<div class="tab-wrap" style="border-bottom: 1px solid rgb(67, 90, 118);">' + html + '</div>';
        }
        return '<div class="tab-wrap" style="border-bottom: none;">' + html + '</div>';
    }
}(this));