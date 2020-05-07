(function (root) {
    let UI = {};
    /**
     * 新增一个图表
     */
    UI.addDiagram = function (diagram, call) {
        let dg = {};
        dg.screenId = diagram.screenId;
        dg.diagramType = diagram.diagramType;
        var newDiagramId;
        try {
            if (diagram.libraryId) {
                newDiagramId = dataVNet.doAddLibraryToScreen(diagram.libraryId, diagram.screenId);
                if (newDiagramId == null) {
                    return;
                }
            } else {
                newDiagramId = dataVNet.createDiagram(dg);
            }
        } catch (e) {
            console.log("net error" + e);
            return;
        }

        let level = dataVNet.doGetMaxLevel();
        let style = {level: level + 1};
        diagramUtils.createBox($('.chart-part'), newDiagramId, diagram.diagramType, style);
        let boxXY = diagramUtils.boxFix();
        let $target = $('div[data-index="' + newDiagramId + '"]');
        boxXY.width = $target.width();
        boxXY.height = $target.height();
        boxXY.level = level+1;

        diagramUtils.boxDragAble(newDiagramId, boxXY);
        dataVRender.doRender({diagramId: newDiagramId, screenId: dg.screenId, async: false});
        try {
            dataVNet.updateXY(newDiagramId, boxXY);
        } catch (e) {
            console.log("net error" + e);
        }
        if (typeof call === 'function') {
            call();
        }
    };

    /**
     * 在页面上下增一个之前已经create的diagram
     */
    UI.addExistsDiagram = function (diagramVo) {
        if (diagramVo.hasOwnProperty("screenId")) {
            diagramUtils.createBox($('#temp-wrap-' + diagramVo.screenId).find('.chart-part')[0], diagramVo.diagramId, diagramVo.diagramType, diagramVo);
        } else {
            diagramUtils.createBox($('.chart-part')[0], diagramVo.diagramId, diagramVo.diagramType, diagramVo);
        }
        if (!_preview) {
            diagramUtils.boxDragAble(diagramVo.diagramId, diagramVo);
        }
        dataVRender.doRender({diagramId: diagramVo.diagramId, screenId: diagramVo.screenId});
    };

    /**
     * 删除一个图表
     * @param diagramId
     */
    UI.deleteDiagram = function (screenId, diagramId) {
        try {
            dataVNet.deleteDiagram(screenId, diagramId);
            dataVTimer.removeTask(diagramId);
            dataVRender.doDelete(diagramId, screenId);
        } catch (e) {

        }
        this.hideConfigPanel();
        $('div[data-index="' + diagramId + '"]').remove();
    };

    UI.doUpdateScreenStyle = function (width, height) {
        let style = {};
        if (width) {
            style.width = width;
            style.height = height;
        } else {
            style.width = parseInt($('#w-self').val());
            style.height = parseInt($('#h-self').val());
        }
        style.dateTimeStyle = $('#dateTimeStyle').val();
        style.backgroundColor = $('.set-detail').find('.bg.active').data('bg');
        dataVNet.updateScreenStyle(style);
    };

    UI.hideConfigPanel = function () {
        $('.temp-pick').find('.attr-set').eq(0).removeClass('hidden').siblings('.attr-set').addClass('hidden');
        $('.box-data').removeClass('choose');
        $('.box-data').find('.box-btn').css('display', 'none');
    };

    UI.onLoadDiagramConfigManagerPanel = function (diagramId) {
        let targetId = $('.diagram-config-manager').attr('data-diagramid');
        if (targetId == diagramId) {
            $('.diagram-config-manager').removeClass('hidden').siblings('.attr-set').addClass('hidden');
            return;
        }
        $('.diagram-config-manager').removeClass('hidden').siblings('.attr-set').addClass('hidden');
        $('.diagram-config-manager').load(_contextPath + '/bigdata/datav/diagram/config/' + diagramId);
        $('.diagram-config-manager').attr('data-diagramid', diagramId);
    };

    let diagramUtils = {};
    diagramUtils.createBox = function (dom, diagramId, type, style) {
        let box;
        style.type = type;
        if (dataVDiagramTypes.TEXT_TITLE == type || dataVDiagramTypes.STATE_CARD == type) {
            box = diagramUtils.createBoxOfHeader(diagramId, style, type);
        } else {
            box = diagramUtils.createBoxOfBody(diagramId, style);
        }

        $(dom).append(box);
    };

    /**
     * 创建body型的
     */
    diagramUtils.createBoxOfBody = function (diagramId, style) {
        let box = '<div class="box-data js-unbind box-chart  resize-item' + diagramId +
            '" data-number="' + diagramId + '" data-index="' + diagramId + '"' + this.createDiagramStyleStr(style) + '>';
        // if (!_preview) {
        //     box += '<img class="js-remove" src="' + _contextPath + '/static/bigdata/images/delate.png"/>';
        // }
        box += '<div class="corner-left-bottom hide"></div>';
        box += '<div class="corner-right-bottom hide"></div>';
        box += '<div class="box-data-body height-1of1" id="box' + diagramId + '"'+ this.createBoxBodyStyle(style) +'>\
                        </div>\
                    </div>';
        return box;
        // $('.chart-part').append(box);
    };

    /**
     * 创建header型的
     */
    diagramUtils.createBoxOfHeader = function (diagramId, style, type) {
        let box = '<div class="box-data js-unbind  ' + (type == 100 ? '' : 'box-chart') + '  resize-item' + diagramId +
            '" data-number="' + diagramId + '" data-index="' + diagramId + '"' + this.createDiagramStyleStr(style) + '>';
        // if (!_preview) {
        //     box += '<img class="js-remove" src="' + _contextPath + '/static/bigdata/images/delate.png"/>';
        // }
        box += '<div class="corner-left-bottom hide"></div>';
        box += '<div class="corner-right-bottom hide"></div>';
        box += '<div class="box-data-header height-1of1" id="box' + diagramId + '">\
                        </div>\
                    </div>';
        return box;
        // $('.chart-part').append(box);
    };

    diagramUtils.createDiagramStyleStr = function (style) {
        if (style) {
            let styleStr = 'style="top:' + style.y + 'px;left:' + style.x + 'px;width:' + style.width + 'px;height:' +
                +style.height + 'px;background-color:' + style.backgroundColor + ';z-index:' + style.level + ';"';
            return styleStr;
        }
        return '';
    };

    diagramUtils.createBoxBodyStyle = function(style) {
        if (style && (style.type != 204 && style.type != 205)) {
            let styleStr = 'style="width:' + style.width + 'px;height:'+style.height + 'px;"';
            return styleStr;
        }
        return '';
    };

    /**
     * 调整新增的box的位置
     */
    diagramUtils.boxFix = function () {
        let count = $('.chart-part').find('.box-data').length;
        let boxXY = {};
        boxXY.x = parseInt($('.limit-wrap').scrollLeft().toFixed(0).slice(0, -1) + '0') + 10 * count;
        boxXY.y = parseInt($('.limit-wrap').scrollTop().toFixed(0).slice(0, -1) + '0') + 10 * count;
        $('.chart-part').find('.box-data').last().css({
            top: boxXY.y,
            left: boxXY.x
        });
        return boxXY;
    };

    /**
     * 设置新增的盒子可移动，可缩放
     */
    diagramUtils.boxDragAble = function (diagramId, style) {
        new ZResize({
            stage: ".chart-part",
            item: '.resize-item' + diagramId,
            //鼠标松开，Ajax更新
            mouseupCall: function (data) {
                //更新宽、高、X、Y
                if (changeXY(this.lastData, data)) {
                    dataConfig.onChange(createOnChangeConfig("x", data.left, data.el));
                    dataConfig.onChange(createOnChangeConfig("y", data.top, data.el));
                }
                if (changeWH(this.lastData, data)) {
                    dataConfig.onChange(createOnChangeConfig("width", data.width, data.el));
                    dataConfig.onChange(createOnChangeConfig("height", data.height, data.el));
                    notifyResize(data.el, data.width, data.height);
                }
                //防止移动的过程中resize不彻底，因此在鼠标松开的时候进行一次resize
                this.lastData = data;
            },
            //鼠标移动过程中，更新坐标、宽、高等参数
            mousemoveCall: function (data) {
                let now;
                $('input[name="width"]').val(data.width);
                $('input[name="height"]').val(data.height);
                $('input[name="x"]').val(data.left);
                $('input[name="y"]').val(data.top);
                //每100ms 检查resize
                if ((now = new Date().getTime()) - this.lastRefresh > 100 && changeWH(this.lastData, data)) {
                    notifyResize(data.el, data.width, data.height);
                    this.lastRefresh = now;
                }
            },
            mousedownCall: function (data) {
                let diagramId = data.el.attr('data-index');
                UI.onLoadDiagramConfigManagerPanel(diagramId);
            },
            lastRefresh: 0,
            lastData: {
                left: style.x,
                top: style.y,
                width: style.width,
                height: style.height
            }
        });
    };

    function changeXY(last, now) {
        return last.left != now.left || last.top != now.top;
    }

    function changeWH(last, now) {
        return last.width != now.width || last.height != now.height;
    }

    function notifyResize(el, w, h) {
        dataVRender.resize(el.children('.box-data-body')[0], w, h);
    }

    function createOnChangeConfig(key, value, el) {
        let config = {};
        if (el) {
            config.dom = el[0];
        }
        config.key = key;
        config.value = value;
        config.arrayName = null;
        return config;
    }

    root.dataVUI = UI;
}(this));