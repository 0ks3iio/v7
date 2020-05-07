(function ($) {
    $.fn.bootstrapValidator.validators.decimal = {
        html5Attributes: {
            message: 'message',
            integerLength: 'integerLength',
            decimalLength: 'integerLength'
        },

        enableByHtml5: function ($field) {

            var options = {};

            var integerLength = $field.attr('integerLength');
            var decimalLength = $field.attr('decimalLength');
            if (integerLength) {
                options.integerLength = parseInt(integerLength);
            }
            if (decimalLength) {
                options.decimalLength = parseInt(integerLength);
            } else {
                options.decimalLength = 0;
            }

            if (!options.integerLength || options.integerLength <= 0 || options.decimalLength<0) {
                return false;
            }
            return options;
        },
        validate: function (validator, $field, options) {
            var value = $field.val();

            if (value === '') {
                return true;
            }
            var vs = value.split('.');
            var message = options.message || $.fn.bootstrapValidator.i18n.stringLength['default'];;

            if (vs.length > 2) {
                return {valid: false, message: message};
            } else if (vs.length === 1) {
                vs[1] = 0;
            } else if (vs.length === 2) {
                if (vs[0] === "") {
                    vs[0] = 0;
                    $field.val('0' + value);
                }
                if (vs[1] === "") {
                    vs[1] = 0;
                }
            }

            if (isNaN(parseInt(vs[0])) || isNaN(parseInt(vs[1]))) {
                return {valid: false, message: message};
            }

            if (vs[0].length > options.integerLength) {
                return {valid: false, message: message};
            }

            if (vs[1].length > options.decimalLength) {
                return {valid: false, message: message};
            }

            return {valid: true, message: message};
        }
    };
}(window.jQuery));