/*
 * 配置图表参数
 */
(function (root) {
    var dataConfig = {};

    dataConfig.onChange = function(parameter) {
        dataVRender.updateConfig(parameter);
        let diagramId = $(parameter.dom).data('index');
        dataVNet.updateDiagramParameter(diagramId, parameter.key, parameter.arrayName, parameter.value);
        if (onChangeUtils.refresh(parameter.key)) {
            dataVRender.doRender({diagramId: diagramId, screenId: _screenId, dispose: true});
            onChangeUtils.refreshPointerAndLine(parameter.x)
        }
        if (onChangeUtils.resize(parameter.key)) {
            dataVRender.resize($("#box" + diagramId)[0], parameter.key=='width' ? parameter.value:undefined, parameter.key=='height' ? parameter.value:undefined);
        }
        //change pointer and line
    };

    dataConfig.onChangePointerAndLine = function (x, y, target) {
        onChangeUtils.refreshPointerAndLine(x, y, target);
    };

    var onChangeUtils = {};
    onChangeUtils.refresh = function(key) {
        return !(key == 'x' || key == 'y' || key == 'width' || key == 'height');
    };
    onChangeUtils.resize = function(key) {
        return (key == 'width' || key == 'height');
    };
    onChangeUtils.refreshPointerAndLine = function(x, y, target) {
        var $pointer =  $(target).find('span:first');
        $pointer.text([x, y].join(","));

        var $x = $pointer.next();
        $x.css('width', x + 'px');

        $x.next().css('height', y + 'px');
    };

    root.dataConfig = dataConfig;
}(this));