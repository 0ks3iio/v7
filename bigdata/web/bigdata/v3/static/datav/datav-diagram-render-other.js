/*
 * 渲染自定义的图表
 */
(function (root) {
    let dataVOtherRender = {};

    root.dataVOtherRender = dataVOtherRender;

    dataVOtherRender.renderTable = function (target, option) {
        $(target).html(otherCreator.createTable(option));
    };
    dataVOtherRender.renderProportional = function (target, option) {
        $(target).html(otherCreator.createProportional(option));
    };
    dataVOtherRender.renderTitle = function (target, option) {
        $(target).html(otherCreator.createTitle(option));
    };
    dataVOtherRender.renderDynamicNumber = function (target, option) {
        let last = 0;
        if ($(target).find('div.timer').length > 0) {
            last = $(target).find('div.timer').attr('data-from');
        }
        $(target).html(otherCreator.createDynamicNumber(option, last));
        dataVDynamic.init($(target).find('div.timer')[0]);
    };
    dataVOtherRender.renderUpNumber = function (target, option) {
        $(target).html(otherCreator.createUpNumber(option));
    };
    dataVOtherRender.renderIndex = function (target, option) {
        $(target).html(otherCreator.createIndex(option));
    };

    dataVOtherRender.renderStateCard = function (target, option) {
        $(target).addClass('box-status').addClass('clearfix box-selfs').html(otherCreator.createStateCard(option));

    };
    dataVOtherRender.renderSingleImage = function (target, option) {
        let domStr = '';
        if (option.length == 0) {
            domStr = '<img class="vetically-center default-img" src="' + _contextPath + '/static/bigdata/datav/images/single-noname-pic-grey.png" alt="">'
        } else {
            domStr = '<div><img src="' + _fileUrl + '/' + option[0].imagePath + '" alt=""></div>';
        }
        $(target).addClass('box-img').html(domStr);
    };

    dataVOtherRender.renderShufflingImage = function (target, option, show) {
        let domStr;
        if (option.length == 0) {
            domStr = '<img class="vetically-center default-img" src="' + _contextPath + '/static/bigdata/datav/images/slide-pic-grey.png" />'
        } else {
            domStr = '<div class="slide-class height-1of1">'
            for (let i = 0; i < option.length; i++) {
                domStr = domStr + '<div class="height-1of1"><img src="' + _fileUrl + '/' + option[i].imagePath + '" alt=""></div>'
            }
            domStr = domStr + '</div>';
        }
        $(target).parent().addClass('box-slide');
        $(target).addClass('box-img').html(domStr);
        if (option.length > 1) {
            dataVRender.slideShfflingImage($(target).find('.slide-class')[0]);
            if (!show) {
                $(target).find('.slick-dots').addClass('hide');
            }
        }
    };

    /**
     * 自定义背景
     */
    dataVOtherRender.renderCustomBg = function (target, option) {
        $(target).html(otherCreator.createCustomBg(option));
    };

    /**
     * 数字翻牌器
     */
    dataVOtherRender.renderDigitalCardTurner = function (target, option) {

        //try get old number
        let oldNumber = 0;
        let numHdoler = $(target).find('.numRun');
        if (numHdoler.length > 0) {
            oldNumber = numHdoler.html();
        } else {
            oldNumber = option.number;
        }
        $(target).html(otherCreator.createDigitalCardTurner(option, oldNumber));

        let oi = parseFloat(oldNumber.replace(/,/g, ''));
        let ni = parseFloat(option.number.replace(/,/g, ''))
        if (numHdoler.length>0 && oi !==ni) {
            $(target).find('.numRun').lemCounter({
                value_from: oi,
                value_to: ni
            });
        }

    };

    /**
     * 可交互Tab
     */
    dataVOtherRender.renderTab = function (target, option) {
        $(target).dataVSimpleTab(option);
    };

    dataVOtherRender.renderIFrame = function(target, option) {
        $(target).html(otherCreator.createIFrame(option)).addClass('iframe-self');
    };

    dataVOtherRender.renderBorder = function(target, option) {
        let old = $(target).attr('border-class');
        let newClass = 'datav-border' + option.borderType;
        let $target = $(target).removeClass(old).addClass(newClass).attr('border-class', newClass);
        $target.css('transform', 'rotate(' + option.rotateAngle + 'deg)').addClass('border-self');;

    };

    dataVOtherRender.renderTitleDecoration = function(target, option) {
        let old = $(target).attr('border-class');
        let newClass = 'title-effect' + option.titleType;
        let titleHtml = '<div class="title-content" style="font-size:'+option.titleFontSize+'px; color:'+option.titleFontColor+'; font-weight:'+option.titleFontBold+';" >'+ $.trim(option.title) +'</div>'
        $(target).removeClass(old).addClass(newClass).addClass('title-self').attr('border-class', newClass).html(titleHtml);
    };

    dataVOtherRender.renderClock = function(target, option) {
        var date = new Date();
        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

        let html = '<div class="date"><span class="time">'+hours + ":" + minutes + ":" + seconds+'</span></div>'
        $(target).addClass('time-self').html(html);
    };

    let otherCreator = {};
    /**
     * 表格
     * @param option
     * @returns {string}
     */
    otherCreator.createTable = function (option) {
        let table = '<div class="box-self box-sm">\
                        <div class="box-body-self">\
                            <table class="table-self">\
                                <tbody>';
        let heads = option.heads;
        if (heads.length > 0) {
            table += '<thead><tr '+buildHeaderStyle(option)+'>';
            for (let i = 0; i < heads.length; i++) {
                table += '<th style="font-weight:'+option.tableHeaderStyle.headerFontBold+';color: '+option.tableHeaderStyle.headerFontColor+'">' + heads[i] + '</th>';
            }
            table += '</tr></thead>';
        }
        table += '<tbody>';
        let items = option.items;
        for (let i = 0; i < items.length; i++) {
            table += '<tr '+buildBodyStyle(option)+'>';
            let subItems = items[i];
            for (let j = 0; j < subItems.length; j++) {
                table += '<td style="color: '+option.tableBodyStyle.bodyFontColor+'">' + subItems[j] + '</td>';
            }
            table += '</tr>';
        }
        table += '</tbody>';
        table += '</table></div></div>';
        return table;
    };

    function buildHeaderStyle(op) {
        let h = op.tableHeaderStyle;
        return 'style="height:'+h.headerHeight+'px;font-size:'+h.headerFontSize+'px;background-color:'+h.headerBackgroundColor+';"'
    }

    function buildBodyStyle(op) {
        let h = op.tableBodyStyle;
        return 'style="height:'+h.bodyHeight+'px;font-size:'+h.bodyFontSize+'px;font-weight:'+h.bodyFontBold+';color:'+h.bodyFontColor+';"'
    }

    /**
     * 比例条
     * @param option
     */
    otherCreator.createProportional = function (option) {
        let html = '<div class="box-self box-sm">\
                        <div class="box-body-self box-body">\
                            <div class="teacher-sex width-1of1 vetically-center">\
                                <div class="teacher-sex-male" style="width:' + option.leftValue + ';">' + option.leftName + ':' + option.leftValue + '</div>\
                                <div class="teacher-sex-female" style="width:' + option.rightValue + ';">' + option.rightName + ':' + option.rightValue + '</div>\
                            </div>\
                        </div>\
                    </div>';
        return html;
    };

    /**
     * 标题
     * @param option
     */
    otherCreator.createTitle = function (option) {
        let html = '<h3 class="box-made-title font-change">\
                        <span style="font-size: ' + option.textFontSize + 'px;font-weight: ' + option.textFontWeight + '; color: ' + option.textFontColor + ';">';
        if (option.url) {
            html += '<a href="' + option.url + '" target="_blank" style="text-decoration: none; color: ' + option.textFontColor + ';">';
        }
        html += option.value + '</span>';
        if (option.url) {
            html += '</a>';
        }
        html += '</h3>';
        return html;
    };
    /**
     * 动态的数字
     * @param option
     * @param last
     * @returns {string}
     */
    otherCreator.createDynamicNumber = function (option, last) {
        let html = '<div class="box-self box-sm">\
                        <div class="box-body box-body-self">\
                            <div class="data-change data-change-up vetically-center">\
                                <div style="font-size: ' + option.fontSize + 'px;" class="number-self timer vetically-center count-title font-change" id="count-number" data-from="' + last + '" data-to="' + option.value + '" data-speed="500"></div>\
                            </div>\
                        </div>\
                    </div>';
        return html;
    };

    /**
     * 带有上升或者下降箭头的数字
     *
     * @param option
     * @returns {string}
     */
    otherCreator.createUpNumber = function (option) {
        let html = '<div class="box-self box-sm">\
                            <div class="box-body box-body-self">\
                                <div class="data-change data-change-' + (option.value > 0 ? 'up' : 'down') + ' font-change width-1of1 vetically text-center">\
                                    <span style="font-size: ' + option.fontSize + 'px;">' + Math.abs(option.value) + '<i class="wpfont icon-arrow-' + (option.value > 0 ? 'up' : 'down') + '"></i></span>\
                                </div>\
                            </div>\
                    </div>';
        return html;

    };
    /**
     * 指标卡
     * @param option
     */
    otherCreator.createIndex = function (option) {
        let html = '<div class="box-self box-sm">\
                              <div class="box-body-self box-body">\
                                   <div class="padding-tb-20">';
        if (option.series && option.series.length > 0) {
            for (let i = 0; i < option.series.length; i++) {
                html = html + '<div class="app-percent margin-b-10 blue-bar">\
                                <p class="relative margin-b-10">\
                                    <span class="" style="' + buildFontStyle(option) + '">' + option.series[i].key + '</span>\
                                    <span class="pos-right" style="color:' + option.series[i].backgroundColor + ';"><b>' + option.series[i].value + '</b></span>\
                                </p>\
                                <div class="progress no-margin" style="border-color: ' + option.series[i].backgroundColor + ';">\
                                    <div class="progress-bar progress-bar-primary" role="progressbar" aria-valuenow="60" \
                                    aria-valuemin="0" aria-valuemax="100" style="width: ' + option.series[i].value + ';background-color: ' + option.series[i].backgroundColor + ';">\
                                    </div>\
                                </div>\
                           </div>';
            }
        }

        html += '</div>\
                           </div>\
                      </div>';
        return html;
    };

    function buildFontStyle(option) {
        return "font-size:" + option.textFontSize + "px" +
            ";font-weight:" + option.textFontWeight + ";color:" + option.textFontColor + ";";
    }

    /**
     * 状态卡片
     */
    otherCreator.createStateCard = function (option) {
        let html = '';
        if (option.series && option.series.length > 0) {
            for (let i = 0; i < option.series.length; i++) {
                html = html + '<div class="" style="' + buildFontStyle(option) + '">\
                                    <span class="circle circle-success pos-left" style="background-color: ' + option.series[i].backgroundColor + ';"></span>' + option.series[i].name + '\
                               </div>';
            }
        }
        return html;
    };

    otherCreator.createCustomBg = function (option) {
        let style;
        style = 'style="border-style:solid; border-color:' + option.customBgBorderColor + ';border-width:' + option.customBgBorderWidth + 'px;';
        style = style + 'border-radius:' + option.customBgUpLeft + '% ' + option.customBgUpRight + '% ' + option.customBgLowerRight + '% ' + option.customBgLowerLeft + '%;"'
        return '<div class="width-1of1 height-1of1 js-bg"' + style + ' ></div>';
    };

    /**
     * 数字翻牌器
     */
    otherCreator.createDigitalCardTurner = function (op, oldNumber) {
        let html = '';
        let titleStyle = '';
        if (op.titleStyle) {
            let style = op.titleStyle;
            let align = op.titleStyle.titlePosition.replace(/up|down/, '');
            titleStyle = 'style="text-align:' + align + ';color:' + style.titleFontColor + ';font-size:'
                + style.titleFontSize + 'px;' + 'font-weight:'+ style.titleFontBold +';';
        }
        let titleContent = op.titleStyle.titleContent ? op.titleStyle.titleContent : '';
        if (op.titleStyle.titlePosition.indexOf('down') == -1) {
            titleStyle = titleStyle + 'margin-bottom:' + op.titleStyle.titleBottom + 'px;"';
            html = '<div class="js-text-target" data-place="1"' + titleStyle + ' >' + titleContent + '</div>';
        }
        let prefixStyle = 'style="font-size:' + op.turner.prefixFontSize + 'px;color:' + op.turner.prefixFontColor + ';font-weight:' + op.turner.prefixFontBold + ';"';
        let numberStyle = 'style="font-size:' + op.turner.numberFontSize + 'px;color:' + op.turner.numberFontColor + ';font-weight:' + op.turner.numberFontBold + ';"';
        let suffixStyle = 'style="font-size:' + op.turner.suffixFontSize + 'px;color:' + op.turner.suffixFontColor + ';font-weight:' + op.turner.suffixFontBold + ';"';
        let prefixContent = op.turner.prefixContent ? op.turner.prefixContent : '';

        let suffixContent = op.turner.suffixContent ? op.turner.suffixContent : '';
        html = html + '<div style="text-align: '+ op.turner.contentPosition +';"><span class="js-before-target"' + prefixStyle + ' >' + prefixContent + '</span>' +
            '<span  class="dealer-default js-prefix-target numRun" ' + numberStyle + '>' + oldNumber + '</span><span class="js-after-target" ' + suffixStyle + '>' + suffixContent + '</span></div>';
        if (op.titleStyle.titlePosition.indexOf('down') != -1) {
            titleStyle = titleStyle + 'margin-top:' + op.titleStyle.titleBottom + 'px;"';
            html = html + '<div class="js-text-target" data-place="1"' + titleStyle + ' >' + titleContent + '</div>';
        }
        return html;
    };

    otherCreator.createIFrame = function (op) {
        if (op.url) {
            return '<iframe src="'+op.url+'" style="width: 100%;height: 100%;">'
        } else {
            return '<div class="width-1of1 text-center vetically"> < iframe ></div>'
        }
    }
}(this));