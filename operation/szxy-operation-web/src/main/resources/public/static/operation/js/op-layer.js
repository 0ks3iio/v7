/*
 * 通用弹出提示
 */
(function (root) {
    let opLayer = {};

    const SUCCESS = "SUCCESS";
    const ERROR = "ERROR";
    const WARN = "WARN";

    /**
     * 弹出提示
     */
    opLayer.layerMsg = function(state, message, title, call) {
        if (state === SUCCESS) {
            this.success(message, title, call);
        }
        else if (state === ERROR) {
            this.error(message, title, call);
        }
        else if (state === WARN) {
            this.warn(message, title, call);
        }
    };

    opLayer.success = function (message, title, call) {
        open('success', title, message, call);
    };

    // opLayer.callback = function(message, title){
    //     callbackUnitList('success', title, message);
    // }

    opLayer.error = function (message, title, call) {
        open('error', title, message, call);
    };

    opLayer.warn = function (message, title, call) {
        open('warn', title, message, call);
    };

    function open(state, title, message, call) {
        $('.' + state + '-content').html(message);
        if (typeof title === "undefined") {
            title = "提示";
        }
        if (typeof title === 'function') {
            call = title;
            title = "提示";
        }
        layer.open({
            type: 1,
            shade: .5,
            title: [title],
            area: '355px',
            content: $('#op_' + state),
            cancel: call
        })
    };

    root.opLayer = opLayer;
    var state = {

    };

    state.SUCCESS = SUCCESS;
    state.ERROR = ERROR;
    state.WARN = WARN;
    state.from = function(success) {
        if (success) {
            return SUCCESS;
        }
        return ERROR;
    };
    root.opLayerState = state;
}(this));