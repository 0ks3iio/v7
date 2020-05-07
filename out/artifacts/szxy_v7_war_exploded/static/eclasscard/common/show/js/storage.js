/**
 * storage.js by gex 2012.02.17
 * 
 * HTML5，本地存储
 * 
 */


//sessionStorage不是一种持久化的本地存储，仅仅是会话级别的存储
var storage = {

  supports_localStorage : function() {
    return 'sessionStorage' in window && window['sessionStorage'] !== null;
  },

  set : function(key, value) {
    if (storage.supports_localStorage()) {
    	sessionStorage.setItem(key, value);
    } else {
      // brower不支持本地存储
    }
  },

  get : function(key, isThorwException) {
	    if (storage.supports_localStorage()) {
	    	var _isThorwException = false;
	    	if(undefined != isThorwException){
	    		_isThorwException = isThorwException;
	    	}
	    	var val = sessionStorage.getItem(key);
	    	if(_isThorwException){
	    		if(null == val){
	    			alert("取不到对应的"+key+"值");
	    			throw new Exception();
	    		}
	    	}
	      return val;
	    } else {
	    	alert(999);
	      // brower不支持本地存储
	    }
	  },

  remove : function(key) {
    if (storage.supports_localStorage()) {
    	sessionStorage.removeItem(key);
    } else {
      // brower不支持本地存储
    }
  }
};


//用于持久化的本地存储，除非主动删除数据，否则数据是永远不会过期的
var storageLocal = {

	  supports_localStorage : function() {
	    return 'localStorage' in window && window['localStorage'] !== null;
	  },

	  set : function(key, value) {
	    if (storageLocal.supports_localStorage()) {
	    	localStorage.setItem(key, value);
	    } else {
	      // brower不支持本地存储
	    }
	  },

	  get : function(key, isThorwException) {
		    if (storageLocal.supports_localStorage()) {
		    	var _isThorwException = false;
		    	if(undefined != isThorwException){
		    		_isThorwException = isThorwException;
		    	}
		    	var val = localStorage.getItem(key);
		    	if(_isThorwException){
		    		if(null == val){
		    			alert("取不到对应的"+key+"值");
		    			throw new Exception();
		    		}
		    	}
		      return val;
		    } else {
		    	alert(999);
		      // brower不支持本地存储
		    }
		  },

	  remove : function(key) {
	    if (storageLocal.supports_localStorage()) {
	    	localStorage.removeItem(key);
	    } else {
	      // brower不支持本地存储
	    }
	  },  
 };