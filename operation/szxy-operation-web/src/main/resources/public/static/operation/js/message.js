

if (typeof layer === 'undefined') {
    throw new Error("Operation-message require Layer");
}
if (typeof window.spop === 'undefined') {
    throw new Error("Operation-message require Spop");
}

(function () {

    var Message = function () {
        this.message = '';
        this.delay = 3000;
    };
    Message.prototype = {
        constructor: Message,

        _message: function(style, content, delay, call){
            window.spop({
                template: content,
                position  : 'top-center',
                autoclose: delay,
                style: style,
            });
            if (typeof call === 'function') {
                setTimeout(call, 500);
            }
        },
        successMessage: function (content, delay, call) {
            if (typeof content === 'object') {
                spop(content);
                return;
            }
            if (typeof content === 'undefined') {
                content = this.message;
            }
            if (typeof delay === "function") {
                call = delay;
                delay = this.delay;
            }
            if (typeof delay === 'undefined' || delay < 500) {
                delay = this.delay;
            }
            this._message('success', content, delay, call)
        },
        errorMessage: function (content, delay, call) {
            if (typeof content === 'object') {
                spop(content);
                return;
            }
            if (typeof content === 'undefined') {
                content = this.message;
            }
            if (typeof delay === "function") {
                call = delay;
                delay = this.delay;
            }
            if (typeof delay === 'undefined' || delay < 500) {
                delay = this.delay;
            }
            this._message('error', content, delay, call)
        },
        warnMessage: function (content, delay, call) {
            if (typeof content === 'object') {
                spop(content);
                return;
            }
            if (typeof content === 'undefined') {
                content = this.message;
            }
            if (typeof delay === "function") {
                call = delay;
                delay = this.delay;
            }
            if (typeof delay === 'undefined' || delay < 500) {
                delay = this.delay;
            }
            this._message('warning', content, delay, call)
        }
    };

    /**
     * 提示消息
     * @type {Message}
     */
    window.message = new Message();


    var Box = function () {

    };

    Box.prototype = {
        successBox: function () {
        
        },
        errorBox: function () {
            
        },
        warBox: function () {
            
        }
    };

    /**
     * 提示框
     * @type {Box}
     */
    window.box = new Box();
}());