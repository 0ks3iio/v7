/*
 * 配置图表参数
 */
(function (root) {
    let dataConfig = {};

    dataConfig.onChange = function(parameter) {
        dataVRender.updateConfig(parameter);
        let diagramId = $(parameter.dom).data('index');
        dataVNet.updateDiagramParameter(diagramId, parameter.key, parameter.arrayName, parameter.value);
        if (onChangeUtils.refresh(parameter.key)) {
            dataVRender.doRender({diagramId: diagramId, screenId: _screenId, dispose: true});
        }
        if (onChangeUtils.resize(parameter.key)) {
            dataVRender.resize($("#box" + diagramId)[0], parameter.key=='width' ? parameter.value:undefined, parameter.key=='height' ? parameter.value:undefined);
        }
    };

    let onChangeUtils = {};
    onChangeUtils.refresh = function(key) {
        return !(key == 'x' || key == 'y' || key == 'width' || key == 'height');
    };
    onChangeUtils.resize = function(key) {
        return (key == 'width' || key == 'height');
    }

    root.dataConfig = dataConfig;
}(this));