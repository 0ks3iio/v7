
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else if (typeof module === 'object' && module.exports) {
        module.exports = factory();
    } else {
        root.router = factory();
  }
}(typeof self !== 'undefined' ? self : this, function () {

    "use strict";

	var EventUtil = {
		on: function(element, type, callback){
			if(element.addEventListener){
				element.addEventListener(type, callback, false);
			}else if(element.attachEvent){
				element.attachEvent('on' + type, callback);
			}else{
				element['on' + type] = callback;
			}
		},
		off: function(element, type, callback){
			if(element.removeEventListener){
				element.removeEventListener(type, callback, false);
			}else if(element.detachEvent){
				element.detachEvent('on' + type, callback);
			}else{
				element['on' + type] = null;
			}
		}
	};

	var Router = function(routes){
		this.routes = routes || {};
		this.curHash = window.location.hash;
	};

	// 注册路由
	Router.prototype.add = function(path, callback){
		this.routes[path] = callback;
	};

	// 跳转
	Router.prototype.go = function(path, callback){
		if(typeof this.routes[path] !== 'function'){
			this.add(path, callback);
			location.hash =  path;
			this.curHash = window.location.hash;
		}else{
            location.hash =  path;
            this.curHash = window.location.hash;
			// this.routes[path]();
		}
	};

	Router.prototype.checkHash = function(){
		var h = window.location.hash;
		if(h != this.curHash){
			this.curHash = h;
			this.reload();
		}
	};

	// 刷新页面
	Router.prototype.reload = function(path){
		var h = window.location.hash;
		if(h !== ''){
			this.routes[h.split('#')[1]]();
		}else{
			this.routes['/']();
		}
	};

	Router.prototype.init = function(){
		if('onpopstate' in window){
			EventUtil.on(window, 'popstate', this.reload.bind(this));
		}else{
			EventUtil.on(window, 'hashchange', this.reload.bind(this));
		}

		EventUtil.on(window, 'load', this.reload.bind(this));
	};

    return Router;
}));

	