/**
 * Created by shenke on 2017/3/20.
 */

(function () {

    "use strict"; //strict严格模式

    var root = this;

    //检查是否支持缓存
    if (!window['Promise']){
        //
    }

    //IE
    var bind = function (func, value) {
        return function () {
            return func.apply(value,arguments);
        }
    }

    function LocalCache(option) {
        if(option && option.storage){
            this.storage = option.storage;
        }
        this.async.get = bind(this.async.get, this);
        this.async.set = bind(this.async.set, this);
    }

    var supportLocalStorage = LocalCache.prototype.supportLocalStorage = (function () {
        try {
            var test_val = "__test_val__";
            root.localStorage.setItem(test_val, test_val);
            root.localStorage.getItem(test_val);
            root.localStorage.removeItem(test_val);
            return true;
        } catch (e) {
            return false;
        }
    })();

    var supportSessionStorage = LocalCache.prototype.supportSessionStorage = (function () {
        try {
            var test_session = "__test_session__";
            root.sessionStorage.setItem(test_session,test_session);
            root.sessionStorage.getItem(test_session);
            root.sessionStorage.removeItem(test_session);
            return true;
        } catch (e){
            return false;
        }
    })();

    var supportNativeJSON = LocalCache.prototype.supportNativeJSON = !!root.JSON;

    LocalCache.prototype.cachePrefix = '___localCache___';
    LocalCache.prototype.expirePrefix = '__localCacheExpire___';

    //wrapper
    LocalCache.prototype.eisCache = {
        localStorageCache:{
            set:function (key, val) {
                return root.localStorage.setItem(key,val);
            },
            get:function (key) {
                return root.localStorage.getItem(key);
            },
            remove:function (key) {
                return root.localStorage.removeItem(key);
            },
            key : function (index) {
                return root.localStorage.key(index);
            },
            //Clear all window cache
            clear:function () {
                return root.localStorage.clear();
            },
            length:function () {
                return root.localStorage.length;
            },
            enable:function () {
                return supportNativeJSON && supportLocalStorage;
            }
        },
        sessionCache:{
            set:function (key, val) {
                return root.sessionStorage.setItem(key,val);
            },
            get:function (key) {
                return root.sessionStorage.getItem(key);
            },
            remove:function (key) {
                return root.sessionStorage.removeItem(key);
            },
            key : function (index) {
                return root.sessionStorage.key(index);
            },
            //Clear all window cache
            clear:function () {
                return root.sessionStorage.clear();
            },
            length:function () {
                return root.sessionStorage.length;
            },
            enable:function () {
                return supportNativeJSON & supportSessionStorage;
            }
        }
    }
    //utils
    LocalCache.prototype.utils = {
        expireKey : function (key) {
            return LocalCache.prototype.expirePrefix + key;
        },
        key : function(key){
            return LocalCache.prototype.cachePrefix + key;
        },
        hasExpire : function (key) {
            var expireKey = this.expireKey(key);
            var expireValue = parseInt(LocalCache.prototype.storage.get(expireKey),10);
            return  expireValue && (expireValue < this.currentTime());
        },
        currentTime : function () {
            return new Date().getTime();
        }
    }

    LocalCache.prototype.storage = LocalCache.prototype.eisCache.localStorageCache;

    LocalCache.prototype.utils = LocalCache.prototype.utils;

    LocalCache.prototype.set = function (key, val, seconds) {
        if (!this.storage.enable() && !key){
            return null;
        }
        var cacheKey = this.utils.key(key);
        var expireKey = this.utils.expireKey(key);

        if (seconds){
            var ms = seconds * 1000;
            this.storage.set(expireKey,this.utils.currentTime() + ms);
        }
        else{
            this.storage.remove(expireKey);
        }

        val = JSON.stringify(val);
        return this.storage.set(cacheKey,val);
    }

    LocalCache.prototype.get = function (key) {
        if (!this.storage.enable() && !key){
            return null;
        }

        if (this.utils.hasExpire(key)){
            this.remove(key);
            return null;
        }

        var cacheKey = this.utils.key(key);
        var value = this.storage.get(cacheKey);

        if(value){
            try {
                return value = JSON.parse(value);
            } catch (e){
                return null;
            }
        }
        return value;
    }

    LocalCache.prototype.remove = function (key) {

        if(!this.storage.enable()){
            return null;
        }

        var cacheKey = this.utils.key(key);
        var expireKey = this.utils.expireKey(key);

        this.storage.remove(expireKey);
        this.storage.remove(cacheKey);
    }

    LocalCache.prototype.clear = function () {
        if (!this.storage.enable()){
            return null;
        }
        var length = this.storage.length();
        var prefix = this.cachePrefix;

        for (var i = length - 1; i >=0 ;i--){
            var key = this.storage.key(i);
            if (key && key.indexOf(prefix) === 0){
                var actKey = key.substring(prefix.length,key.length);
                this.remove(actKey);
            }
        }
    }
    LocalCache.prototype.length = function () {
        if(!this.storage.enable()){
            return 0;
        }

        var length = this.storage.length();

        var prefix = this.cachePrefix;
        var count = 0;
        for (var i = length - 1; i >=0 ;i--){
            var key = this.storage.key(i);
            if (key && key.indexOf(prefix) === 0){
                count ++;
            }
        }
        return count;
    }

    LocalCache.prototype.async = {

        set: function (key, value, seconds) {
            return Promise.resolve(this.set(key, value, seconds));
        },

        get: function (key) {
            return Promise.resolve(this.get(key));
        }

    };


    LocalCache.prototype.createCache = function (options) {
        return new LocalCache(options)
    }

    var localCache = new LocalCache();

    localCache.session = new LocalCache({
        storage : localCache.eisCache.sessionCache
    });

    root.eisCache = localCache;

}).call(this);
