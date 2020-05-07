(function ($) {
    $.fn.bootstrapValidator.validators.chineseStringLength = {
        html5Attributes: {
            message: 'message',
            min: 'min',
            max: 'max'
        },

        enableByHtml5: function ($field) {
            var options = {},
                maxLength = $field.attr('maxChineseLength'),
                minLength = $field.attr('minChineseLength');
            if (maxLength) {
                options.max = parseInt(maxLength, 10);
            }
            if (minLength) {
                options.min = parseInt(minLength, 10);
            }

            return $.isEmptyObject(options) ? false : options;
        },
        validate: function (validator, $field, options) {
            var value = "";
            if ($field.attr('type') === 'undefined') {
                value = $field.html();
            } else {
                value = $field.val();
            }

            if (value === '') {
                return true;
            }
            var length = value.length;

            for (var i = 0; i < length; i++) {
                if (value.charCodeAt(i) > 127) {
                    length = length + 1;
                }
            }
            var min = $.isNumeric(options.min) ? options.min : validator.getDynamicOption($field, options.min),
                max = $.isNumeric(options.max) ? options.max : validator.getDynamicOption($field, options.max),
                isValid = true,
                message = options.message || $.fn.bootstrapValidator.i18n.stringLength['default'];

            if ((min && length < parseInt(min, 10)) || (max && length > parseInt(max, 10))) {
                isValid = false;
            }

            switch (true) {
                case (!!min && !!max):
                    message = $.fn.bootstrapValidator.helpers.format(options.message || $.fn.bootstrapValidator.i18n.stringLength.between, [parseInt(min, 10), parseInt(max, 10)]);
                    break;

                case (!!min):
                    message = $.fn.bootstrapValidator.helpers.format(options.message || $.fn.bootstrapValidator.i18n.stringLength.more, parseInt(min, 10));
                    break;

                case (!!max):
                    message = $.fn.bootstrapValidator.helpers.format(options.message || $.fn.bootstrapValidator.i18n.stringLength.less, parseInt(max, 10));
                    break;

                default:
                    break;
            }

            return {valid: isValid, message: message};
        }
    };
}(window.jQuery));