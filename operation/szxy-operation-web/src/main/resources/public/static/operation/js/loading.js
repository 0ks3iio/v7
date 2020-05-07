(function ($) {
    var messageTemplate = '<span class="color-grey special-span">\
                        <i class="fa fa-spinner fa-spin"></i>${message}\
                        </span>';
    var defaultMessage = messageTemplate.replace('${message}', '正在加载，请稍后...');
    var errorMessage = '<span class="color-red special-span"><i class="fa fa-exclamation-circle"></i> 加载出错了</span>';

    var Utils = {
        beforeAjax: function ($target, message) {
            var $clone = $target.clone()
            $target.addClass('hide');
            $clone.html('');
            $clone.append(message);
            $target.after($clone);
        },
        error: function ($target, status, errorMessage) {
            var $message = $target.next();
            if ($message.children().hasClass('special-span')) {
                $message.remove();
            }
            if (status === 4003) {
            } else {
                $target.html(errorMessage);
            }
            $target.removeClass('hide');
        },
        after: function ($target) {
            $target.removeClass('hide');
            var $message = $target.next();
            if ($message.children().hasClass('special-span')) {
                $message.remove();
            }
        }
    };
    $.fn.extend({
        loading: function (data, message, fn) {
            var realMessage = defaultMessage;
            if (typeof message === 'function') {
                fn = message;
            } else if (typeof message === 'string') {
                realMessage = messageTemplate.replace('${message}', message);
            }
            var $this = $(this);
            Utils.beforeAjax($this, realMessage);

            $this.load(data, function (reponseText, textStatus, jqXHR) {
                if (textStatus === 'error') {
                    Utils.error($this, jqXHR.status, errorMessage);
                    return ;
                }
                Utils.after($this);
                if (typeof fn === 'function') {
                    fn(reponseText, textStatus, jqXHR);
                }
            });
        },
        ajaxGetLoading: function (url, data, fn, type) {
            var $this = $(this);
            Utils.beforeAjax($this, defaultMessage);

            if (typeof data ==='function') {
                fn = data;
                data = undefined;
            }
            $.get(url, data, function (res, textStatus) {
                if (textStatus === 'error') {
                    Utils.error($this, res.status, errorMessage);
                    return ;
                }
                Utils.after($this)
                fn(res, textStatus);
            }, type);
        }
    });

    $.ajaxGetLoading = function (url, data, fn, type) {
        var index = layer.load(2);
        if (typeof data ==='function') {
            fn = data;
            data = undefined;
        }

        $.ajax($.extend( {
            url: url,
            type: 'GET',
            dataType: type,
            data: data,
            success: function (data, textStatus, jqXHR) {
                layer.close(index);
                fn(data, textStatus, jqXHR);
            },
            error: function () {
                layer.close(index);
            }
        }, $.isPlainObject( url ) && url ));
    }
}(window.jQuery));