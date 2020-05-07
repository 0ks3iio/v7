(function (root) {
    String.prototype.firstUpperCase = function () {
        return this.replace(/\b(\w)/g, function ($1) {
            return $1.toUpperCase();
        })
    };
    let dataVRender = {};

    dataVRender.doDelete = function (diagramId, screenId) {
        holder.remove(diagramId, screenId);
        holder.diagramContainer.delete(screenId + '_' + diagramId);
    };

    dataVRender.doRender = function (op) {
        let diagramId = op.diagramId;
        dataVNet.doGetDiagramRenderOption(op.screenId, diagramId, op.kvs, function (renderOption) {
            if (!renderOption.render) {
                return;
            }
            if (renderOption.diagramType >= 94 && renderOption.diagramType != 301) {
                dataVRender.doRenderOther(diagramId, renderOption, op.screenId);
            } else {
                if (renderOption.region && !echarts.getMap(renderOption.region)) {
                    dataVNet.doRegisterGeoJson(renderOption.region);
                }
                if (!op.hasOwnProperty('dispose')) {
                    op.dispose = false;
                }
                dataVRender.doRenderECharts(diagramId, renderOption, op.dispose, op.screenId);
            }
            //update width height
            let config = {};
            config.dom = $('div[data-index="' + diagramId + '"]')[0];
            //不需要resize 在create的时候就已经指定了宽度和高度
            if (renderOption.diagramType == 202) {
                dataVRender.resize($('div[data-index="' + diagramId + '"]').children('box-data-body')[0], renderOption.attribute.width, renderOption.attribute.height)
            }
            if (renderOption.diagramType != 204 || renderOption.diagramType != 205) {
                config.key = 'width';
                config.value = renderOption.attribute.width;
                dataVRender.updateConfig(config);
                config.value = renderOption.attribute.height;
                config.key = 'height';
                dataVRender.updateConfig(config);
            }
            config.value = renderOption.attribute.backgroundColor;
            config.key = 'backgroundColor';
            dataVRender.resize($('#box' + diagramId)[0], renderOption.attribute.width, renderOption.attribute.height);
            dataVRender.updateConfig(config);
            config.value = renderOption.attribute.border;
            config.key = 'border';
            dataVRender.updateConfig(config);
        }, undefined === op.async);
    };

    /**
     * 渲染Echarts图表
     * @param op
     */
    dataVRender.doRenderECharts = function (diagramId, renderOption, dispose, screenId) {
        try {
            if (renderOption.op == null) {
                return;
            }
            let targetRenderDom = document.getElementById('box' + diagramId);
            let targetECharts = echarts.getInstanceByDom(targetRenderDom);
            if (targetECharts && dispose) {
                targetECharts.dispose();
                targetECharts = echarts.init(targetRenderDom);
            } else {
                targetECharts = echarts.init(targetRenderDom);
            }
            targetECharts.setOption(renderOption.op);
            dataVRender.doCheckInteraction(diagramId, screenId, renderOption, targetECharts, targetRenderDom);
        } catch (e) {
            console.log(e);
        }
    };

    dataVRender.doCheckInteraction = function(diagramId, screenId, renderOption, targetECharts, targetRenderDom) {
        if (!holder.exist(diagramId, screenId)) {
            if (renderOption.interactionAttribute.beNotified) {
                holder.add(diagramId, targetECharts, screenId);
            }
        } else {
            if (!renderOption.interactionAttribute.beNotified) {
                holder.remove(diagramId, screenId);
            }
        }
        if (renderOption.interactionAttribute.active) {
            let key = screenId + '_' + diagramId;
            if (!holder.diagramContainer.has(key)) {
                holder.diagramContainer.set(key, diagramId);
            }
            return dataVRender.doActiveInteractionEvent(renderOption.interactionAttribute, renderOption.diagramType, targetECharts, screenId, targetRenderDom);
        }
    };

    /**
     * TODO key 应该从后台获取
     */
    dataVRender.doActiveInteractionEvent = function (interactionAttribute, diagramType, targetECharts, screenId, targetDom) {

        if (diagramType == 91) {
            targetECharts.on('click', function (event) {
                let notify = event.componentType === 'series' && event.seriesType === 'map';
                notify = notify || event.componentType === 'geo';
                if (notify) {
                    dataVNet.doGetRegionCodeByName(event.name, function (code) {
                        let kvs = [];
                        for (let i = 0; i < interactionAttribute.bindings.length; i++) {
                            kvs.push({key:interactionAttribute.bindings[i].bindKey, value: code})
                        }
                        holder.notifyAll(kvs, true, screenId);
                    })
                }
                //do nothing
            });

        }
        //时间轴
        else if (diagramType == 301) {
            targetECharts.on('timelinechanged', function (event) {
                let index = event.currentIndex;
                let val = targetECharts.getOption().timeline[0].data[index].value;
                let kvs = [];
                for (let i = 0; i < interactionAttribute.bindings.length; i++) {
                    kvs.push({key:interactionAttribute.bindings[i].bindKey, value: val})
                }
                holder.notifyAll(kvs, false, screenId);
            });
        }
        else if (diagramType == 302) {
            $(targetDom).on('click', '.js-tab-target', function () {
                let id = $(this).attr('id');
                let name = $.trim($(this).html());

                let kvs = [];
                for (let i = 0; i < interactionAttribute.bindings.length; i++) {
                    if (interactionAttribute.bindings[i].key=='id') {
                        kvs.push({key: interactionAttribute.bindings[i].bindKey, value: id})
                    } else {
                        kvs.push({key: interactionAttribute.bindings[i].bindKey, value: name})
                    }
                }

                holder.notifyAll(kvs, false, screenId);
            });
        }
    };

    /**
     * 渲染自定义图表 TODO 这里可以用设计模式优化代码
     * @param op
     */
    dataVRender.doRenderOther = function (diagramId, renderOption, screenId) {
        let target = document.getElementById('box' + diagramId);
        if (renderOption.diagramType == 99) {
            dataVOtherRender.renderTable(target, renderOption.op);
        }
        else if (renderOption.diagramType == 98) {
            dataVOtherRender.renderProportional(target, renderOption.op);
        }
        else if (renderOption.diagramType == 94) {
            dataVOtherRender.renderTitle(target, renderOption.op);
        }
        else if (renderOption.diagramType == 97) {
            dataVOtherRender.renderDynamicNumber(target, renderOption.op);
        }
        else if (renderOption.diagramType == 95) {
            dataVOtherRender.renderUpNumber(target, renderOption.op);
        }
        else if (renderOption.diagramType == 96) {
            dataVOtherRender.renderIndex(target, renderOption.op);
        }
        else if (renderOption.diagramType == 100) {
            dataVOtherRender.renderStateCard(target, renderOption.op);
        }
        else if (renderOption.diagramType == 110) {
            dataVOtherRender.renderSingleImage(target, renderOption.op);
        }
        else if (renderOption.diagramType == 111) {
            dataVOtherRender.renderShufflingImage(target, renderOption.op, renderOption.showPoint);
        }
        else if (renderOption.diagramType == 112) {
            dataVOtherRender.renderClock(target, renderOption.op);
        }
        else if (renderOption.diagramType == 202) {
            dataVOtherRender.renderCustomBg(target, renderOption.op);
        }
        else if (renderOption.diagramType == 101) {
            dataVOtherRender.renderDigitalCardTurner(target, renderOption.op);
        }
        else if (renderOption.diagramType == 302) {
            dataVRender.doCheckInteraction(diagramId, screenId, renderOption, null, target);
            dataVOtherRender.renderTab(target, renderOption.op);
        }
        else if (renderOption.diagramType == 203) {
            dataVOtherRender.renderIFrame(target, renderOption.op);
        }
        else if (renderOption.diagramType == 204) {
            dataVOtherRender.renderBorder(target, renderOption.op);
        }
        else if (renderOption.diagramType == 205) {
            dataVOtherRender.renderTitleDecoration(target, renderOption.op)
        }
    };

    dataVRender.slideShfflingImage = function (target) {
        let hide = $(target).find('.slick-dots').hasClass('hide');
        $(target).slick({
            arrows: false,
            dots: true,
            autoplay: true,
            autoplaySpeed: 3000
        });
        if (hide) {
            $(target).find('.slick-dots').addClass('hide');
        }
    };

    /**
     * 更新配置
     */
    dataVRender.updateConfig = function (config) {
        let basicUpdateFunctionName = 'doUpdate' + config.key.firstUpperCase();
        if (dataVRenderUpdateUtils.hasOwnProperty(basicUpdateFunctionName)) {
            eval('dataVRenderUpdateUtils.' + basicUpdateFunctionName + '(config)');
        }
    };

    /**
     * resize
     * @param echartsDom
     */
    dataVRender.resize = function (echartsDom, w, h) {
        if ($(echartsDom).attr('border-class')) {
            return;
        }
        let config = {};
        config.dom = echartsDom;
        if (typeof w !== 'undefined') {
            config.key = 'width';
            config.value = w;
            dataVRender.updateConfig(config);
        }
        if (typeof h !== 'undefined') {
            config.value = h;
            config.key = 'height';
            dataVRender.updateConfig(config);
        }

        if ($(echartsDom).attr('_echarts_instance_')) {
            let targetEcharts = echarts.getInstanceByDom(echartsDom);
            if (targetEcharts) {
                if (w && h) {
                    targetEcharts.resize({width:w, height: h});
                } else {
                    targetEcharts.resize();
                }
            }
        }
    };

    let dataVRenderUpdateUtils = {};
    /**
     * 更新高度
     */
    dataVRenderUpdateUtils.doUpdateHeight = function (config) {
        $(config.dom).height(config.value);
    };
    /**
     * 更新宽度
     * @param config
     */
    dataVRenderUpdateUtils.doUpdateWidth = function (config) {
        $(config.dom).width(config.value);
    };
    /**
     * 更新X
     * @param config
     */
    dataVRenderUpdateUtils.doUpdateX = function (config) {
        $(config.dom).css('left', config.value + 'px');
    };

    /**
     * 更新Y
     * @param config
     */
    dataVRenderUpdateUtils.doUpdateY = function (config) {
        $(config.dom).css('top', config.value + 'px');
    };

    /**
     * 更新背景颜色
     * @param config
     */
    dataVRenderUpdateUtils.doUpdateBackgroundColor = function (config) {
        if (config.value == 'transparent!important') {
            $(config.dom).addClass('no-bg')
        } else {
            $(config.dom).removeClass('no-bg').css("background-color", config.value);
        }
    };

    /**
     * 更新层级
     * @param config
     */
    dataVRenderUpdateUtils.doUpdateLevel = function (config) {
        $(config.dom).css('z-index', config.value);
    };

    dataVRenderUpdateUtils.doUpdateBorder = function (config) {
        $(config.dom).removeClass('selected').removeClass('none-border');
        if (config.value) {
            $(config.dom).find('.corner-left-bottom').addClass('hide');
            $(config.dom).find('.corner-right-bottom').addClass('hide');
            $(config.dom).addClass(config.value)
        } else {
            $(config.dom).removeClass('selected').removeClass('none-border');
            $(config.dom).find('.corner-left-bottom').removeClass('hide');
            $(config.dom).find('.corner-right-bottom').removeClass('hide');
        }
    };

    let holder = {};

    holder.screenContainer = new Map();
    holder.diagramContainer = new Map();

    holder.getOrCreateScreenContainer = function(screenId) {
        let container = holder.screenContainer.get(screenId);
        if (typeof container === 'undefined') {
            container = new Map();
            holder.screenContainer.set(screenId, container);
        }
        return container;
    };

    holder.add = function (diagramId, ec, screenId) {
        this.getOrCreateScreenContainer(screenId).set(diagramId, ec);
    };

    holder.remove = function (diagramId, screenId) {
        this.getOrCreateScreenContainer(screenId).delete(diagramId);
    };
    holder.exist = function (diagramId, screenId) {
        return this.getOrCreateScreenContainer(screenId).has(diagramId);
    };

    holder.notifyAll = function (kvs, dispose, screenId) {
        this.getOrCreateScreenContainer(screenId).forEach((value, key) => dataVRender.doRender({
            diagramId: key,
            dispose: dispose,
            kvs: kvs,
            screenId: _screenId
        }));
    };
    dataVRender.diagramContainer = holder.diagramContainer;
    root.dataVRender = dataVRender;
}(this));