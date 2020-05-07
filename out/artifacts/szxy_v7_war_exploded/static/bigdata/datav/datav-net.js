(function (root) {
    let dataV = {};

    dataV.createDiagram = function (diagram) {
        var diagramId = "";
        var error;
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/' + diagram.screenId + '/' + diagram.diagramType,
            type: 'POST',
            async: false,
            success: function (res) {
                if (res.success) {
                    diagramId = res.data;
                } else {
                    error = res.message;
                }
            },
            error: netUtils.netError
        });
        if (error) {
            netUtils.error(error);
        }
        return diagramId;
    };

    dataV.updateScreenName = function (title) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/screen/update/' + _screenId,
            type: 'POST',
            data: {
                name: title
            },
            success: function (res) {
                if (!res.success) {
                    if (res.message) {
                        netUtils.error(res.message);
                    } else {
                        netUtils.error(res.msg);
                    }
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 更新指定diagram的坐标
     * @param diagramId
     * @param boxXY
     */
    dataV.updateXY = function (diagramId, boxXY) {
        this.updateDiagramParameter(diagramId, "x", null, boxXY.x);
        this.updateDiagramParameter(diagramId, "y", null, boxXY.y);
        this.updateDiagramParameter(diagramId, "level", null, boxXY.level);
    };

    /**
     * 更新图表的参数信息
     * @param diagramId 图表ID
     * @param key
     * @param arrayName
     * @param value
     */
    dataV.updateDiagramParameter = function (diagramId, key, arrayName, value) {
        let elementId = $('.js-back').attr('data-elementid');
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/update/' + diagramId,
            type: 'POST',
            data: {
                key: key,
                arrayName: arrayName ? arrayName : null,
                value: value,
                elementId: elementId
            },
            async: false,
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 增加数据系列
     */
    dataV.addConfigArray = function (diagramId, groupKey, arrayName, items, call) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/' + diagramId,
            type: 'POST',
            sync: false,
            data: JSON.stringify({
                groupKey: groupKey,
                arrayName: arrayName,
                configArrayItems: items
            }),
            contentType: 'application/json',
            success: function (res) {
                if (res.success) {
                    call();
                } else {
                    netUtils.error(res.message | res.msg)
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 更新screen Style
     */
    dataV.updateScreenStyle = function (screenStyle) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/screen_style/update/' + _screenId,
            type: 'POST',
            data: screenStyle,
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 删除数据系列
     */
    dataV.deleteConfigArray = function (diagramId, arrayName, call) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/delete/' + diagramId + '/' + encodeURIComponent(arrayName) + '/array',
            type: 'GET',
            sync: false,
            success: function (res) {
                if (res.success) {
                    call();
                } else {
                    netUtils.error(res.message | res.msg)
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 删除
     * @param screenId
     * @param diagramId
     */
    dataV.deleteDiagram = function (screenId, diagramId) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/delete/' + screenId + '/' + diagramId,
            type: 'GET',
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.netError,
        });
    };

    dataV.updateDiagram = function (diagramId, diagram) {
        let elementId = diagram.elementId;
        delete diagram.elementId;
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/update/' + diagramId + '?elementId=' + elementId,
            type: 'POST',
            async: false,
            data: diagram,
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.netError
        });
    };

    dataV.doGetDiagramRenderOption = function (screenId, diagramId, kvs, callBack, async) {
        let parameters = "?";
        if (kvs) {
            parameters = parameters + 'interaction=true&';
            for (let i = 0; i < kvs.length; i++) {
                parameters += kvs[i].key + '=' + kvs[i].value + '&';
            }
        }
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/render/' + screenId + '/' + diagramId + parameters,
            type: 'GET',
            async: async,
            success: function (res) {
                if (res.success) {
                    let renderOp = JSON.parse(res.data);
                    callBack(renderOp)
                } else {
                    if (res.message) {
                        netUtils.error(res.message)
                    } else {
                        netUtils.error(res.msg)
                    }
                }
            },
            error: function () {
                dataVTimer.removeTask(diagramId);
                netUtils.netError();
            }
        });
    };

    dataV.doRegisterGeoJson = function (region) {
        $.ajax({
            url: _contextPath + '/static/bigdata/js/echarts/map/json/province/' + region + ".json",
            type: 'GET',
            async: false,
            success: function (geoJson) {
                echarts.registerMap(region, geoJson);
            }
        });
    };

    /**
     * 添加组件
     */
    dataV.doAddElement = function (element) {
        let id = '';
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/' + element.diagramId + '/' + element.diagramType,
            type: 'POST',
            async: false,
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.msg);
                } else {
                    id = res.data;
                }
            },
            error: netUtils.netError
        });
        return id;
    };

    /**
     * 删除组件
     * @param elementId
     */
    dataV.doDeleteElement = function (element, call) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/delete/' + element.diagramId + '/' + element.elementId,
            type: 'get',
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.msg);
                } else {
                    dataV.reloadScreenDefaultBindKey();
                    call();
                }
            },
            error: netUtils.netError
        });
    };

    dataV.doUploadShot = function (s) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/screen/shot/' + _screenId,
            data: {
                data: s
            },
            type: 'POST',
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message | res.msg);
                } else {
                    netUtils.success(res.message);
                }
            },
            error: netUtils.netError
        });
    };
    dataV.doGetMaxLevel = function () {
        let level = 3;
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/' + _screenId + '/max-level',
            type: 'GET',
            async: false,
            success: function (res) {
                if (res.success) {
                    level = res.data;
                } else {
                    netUtils.error(res.msg | res.message);
                }
            },
            error: netUtils.netError
        });
        return level;
    };

    dataV.doActiveInteraction = function (elementId, diagramType, callForSuccess) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/interaction/' + elementId + '/active',
            data: {
                diagramType: diagramType
            },
            type: 'POST',
            success: function (res) {
                if (res.success) {
                    if (callForSuccess) {
                        callForSuccess();
                    }
                } else {
                    netUtils.error(res.msg);
                }
            },
            error: netUtils.netError
        });
    };

    dataV.doCancelActiveInteraction = function (elementId, callForSuccess) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram/config/interaction/delete/' + elementId + '/active?screenId=' + _screenId,
            type: 'GET',
            success: function (res) {
                if (res.success) {
                    if (callForSuccess) {
                        callForSuccess();
                    }
                } else {
                    netUtils.error(res.msg);
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 更新整个大屏交互的默认值
     */
    dataV.doUpdateScreenInteractionElement = function (key, value) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/interaction/screen-element/update/' + _screenId,
            type: 'POST',
            data: {
                key: key,
                value: value
            },
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                } else {
                    //
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 删除大屏的交互默认值
     */
    dataV.doDeleteScreenInteractionElement = function (key) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/interaction/screen-element/delete/' + _screenId + '?key=' + key,
            type: 'GET',
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                } else {
                    //
                }
            },
            error: netUtils.netError
        });
    };

    dataV.doGetRegionCodeByName = function (name, callBack) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/region/convert',
            data: {
                name: name
            },
            type: 'post',
            success: function (res) {
                if (res.success) {
                    callBack(res.data);
                } else {
                    netUtils.error(res.msg);
                }
            },
            error: netUtils.netError
        });
    };

    dataV.doUploadImage = function (image, index, diagramId, suffix, callBack) {
        //form
        // let form = document.createElement("form");
        let form = new FormData();
        form.append("index", index);
        form.append("suffix", suffix);
        form.append("image", image);
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram-image/uploader/' + diagramId,
            type: 'POST',
            data: form,
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                } else {
                    callBack(res.data);
                }
            },
            contentType: false,
            processData: false,
            error: netUtils.netError
        });
    };

    dataV.doDeleteImage = function (index, diagramId, callBack) {
        $.ajax({
            url: _contextPath + '/bigdata/datav/diagram-image/uploader/delete/' + diagramId + '?index=' + index,
            type: 'GET',
            success: function (res) {
                if (!res.success) {
                    netUtils.error(res.message);
                } else {
                    callBack();
                }
            },
            error: netUtils.netError
        });
    };

    /**
     * 收藏
     */
    dataV.doCollect = function (diagramId, name) {
        $.ajax({
            url: _contextPath + '/bigdata/diagramLibrary/collect',
            type: 'post',
            data: {
                diagramId: diagramId,
                libraryName: name
            },
            success: function (res) {
                if (res.success) {
                    //
                    netUtils.success(res.message);
                } else {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.error
        })
    };

    dataV.doAddLibraryToScreen = function (libraryId, screenId) {
        let id = null;
        $.ajax({
            url: _contextPath + '/bigdata/diagramLibrary/collect/screen',
            type: 'post',
            data: {
                libraryId: libraryId,
                screenId: screenId
            },
            async: false,
            success: function (res) {
                if (res.success) {
                    //
                    id = res.data;
                } else {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.error
        });
        return id;
    };

    //update bindKey

    dataV.updateBindKey = function (elementId, key, bindKey) {
        $.ajax({
            url: _contextPath + '/bigdata/diagram/bind-key/' + _screenId + '/' + elementId + '/' + key + '/' + bindKey,
            type: 'GET',
            async: true,
            success: function (res) {
                if (res.success) {
                    //
                    dataVAce.refreshTips();
                    dataV.reloadScreenDefaultBindKey();
                } else {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.error
        });
    };

    dataV.reloadScreenDefaultBindKey = function() {
        $.ajax({
            url: _contextPath + '/bigdata/datav/interaction/screen-element/' + _screenId ,
            type: 'GET',
            async: true,
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    //
                    let els = res.data;
                    let html = '';
                    for (var e in els) {
                        let dv = els[e].defaultValue==null?"":els[e].defaultValue;
                        html = html + '<div class="filter-item">\n' +
                            '                                    <span class="front-title">'+els[e].bindKey+'</span>\n' +
                            '                                    <input id="'+els[e].bindKey+'"\n' +
                            '                                           value="'+dv+'" type="text"\n' +
                            '                                           class="form-control inside-select" placeholder="请输入默认值"/>\n' +
                            '                                </div>'
                    }
                    $('.screen-interaction-element').html(html);
                } else {
                    netUtils.error(res.message);
                }
            },
            error: netUtils.error
        });
    };

    let netUtils = {};
    netUtils.error = function (message) {
        layer.msg(message, {icon: 2});
    };
    netUtils.netError = function () {
        layer.msg("网络异常", {icon: 2});
    };
    netUtils.success = function (message) {
        layer.msg(message, {icon: 1});
    };

    root.dataVNet = dataV;
}(this));