/*
 * jquery ajax 安全问题统一处理
 */
(function () {

    var Handler = function(){}

    Handler.prototype = {
        error: function (jqXHR) {
            switch (jqXHR.status) {
                case 4003:
                    message.errorMessage(jqXHR.responseText);
                    break;
                case 4005:
                    message.errorMessage(jqXHR.responseText);
                    break;
                case 408:
                    message.errorMessage("请求超时");
                    break;
                case 4001:
                    window.location.href = jqXHR.responseText;
                    break;
                case 404:
                    message.errorMessage("页面找不到了");
                    break;
                case 200:
                case 302:
                    break;
                default:
                    message.errorMessage(jqXHR.responseText);
            }
        }
    };

    var handler = new Handler();

    $.ajaxSetup({
        complete: function(jqXHR, textStatus){
            if (textStatus === 'error') {
                handler.error(jqXHR);
            }
        }
    });

}(this));