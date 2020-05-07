(function ($) {

    $.fn.bootstrapValidator.validators.chineseMobile = {
        html5Attributes: {
            message: 'message'
        },
        enableByHtml5: function ($field) {

        },
        validate: function (validator, $field, options) {
            var value = "";
            if ($field.attr('type') === 'undefined') {
                value = $field.html()
            } else {
                value = $field.val();
            }

            value = $.trim(value);

            if (value === '') {
                return true
            }

            var regexp = /^1((3[0-9]|4[579]|5[0-35-9]|6[6]|7[135-8]|8[0-9]|9[198])\d{8}|(70[0-9])\d{7})$/;
            var valid = regexp.test(value);
            var message = options.message || $.fn.bootstrapValidator.i18n.chineseMobile['default'];

            return {valid: valid, message: message};
        }
    };

    $.fn.bootstrapValidator.i18n = $.extend(true, $.fn.bootstrapValidator.i18n, {
        chineseMobile: {
            'default': '手机号 格式不正确'
        }
    });
}(window.jQuery));