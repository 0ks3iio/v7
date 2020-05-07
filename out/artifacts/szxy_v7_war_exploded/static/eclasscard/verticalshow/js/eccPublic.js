/* 第一部分 ： websocket连接，引入该文件，则会websocket握手，心跳检测 */
var websocket;//websocket实例
var lockReconnect = false;//避免重复连接
function createWebSocket() {
    try {
    	if ('WebSocket' in window) {
    	    websocket = new WebSocket(_webSocketUrl);
    	} else if ('MozWebSocket' in window) {
    	    websocket = new MozWebSocket(_webSocketUrl);
    	} else {
    	    websocket = new SockJS(_sockJSUrl);
    	}
        initEventHandle();
    } catch (e) {
        reconnect();
    }     
}

function initEventHandle() {
	heartCheck.reset().start();
	websocket.onclose = function () {
        reconnect();
    };
    websocket.onerror = function () {
        reconnect();
    };
    websocket.onopen = function () {
        //心跳检测重置
        heartCheck.reset().start();
    };
    websocket.onmessage = function (event) {
        //如果获取到消息，心跳检测重置
        //拿到任何消息都说明当前连接是正常的
    	var message = JSON.parse(event.data);
    	if (message.type == 'text') { 
             var id =  message.id;
             var text =  message.text;
             $("#"+id).html(text);
    	}else if(message.type =="div"){
    		var id =  message.id;
    		var url =  message.url;
    		url = _contextPath+url+"?name="+_deviceNumber+"&view="+_view;
    		setTimeout(function(){
    			$("#"+id).load(url);
    		},2000);
    	
    	}else if(message.type =="fresh"){
    		var url =  message.url;
    		url = _contextPath+url+"?deviceNumber="+_deviceNumber+"&view="+_view;
    		setTimeout(function(){
    			location.href = url;
    		},2000);
    	}else if(message.type =="function"){
    		var funname =  message.funName;
    		setTimeout(function(){
    			if (funname instanceof Function) {
    				eval(funname)();
    			} else {
    				eval(funname);
    			}
//    			eval(funname)();
    		},2000);
    	}
    	heartCheck.reset().start();
    }
}

function reconnect() {
    if(lockReconnect) return;
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function () {
        createWebSocket();
        lockReconnect = false;
    }, 10000);
}


//心跳检测
var heartCheck = {
    timeout: 20000,//60秒
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function(){
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function(){
        var self = this;
        this.timeoutObj = setTimeout(function(){
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
        	if(websocket.readyState == WebSocket.OPEN){
        		var heartBeat = {HeartBeat:"HeartBeat"};
        		websocket.send(JSON.stringify(heartBeat));
        	}
            self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
            	if(websocket.readyState == WebSocket.OPEN){
            		websocket.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            	}
            }, self.timeout)
        }, this.timeout)
    }
}

createWebSocket();

/* 第二部分：获取音频Audio,播放*/
var url = "http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&ctp=1&spd=6&vol=7&per=0";
var playAudio = new Audio();
function clockAudioPlay(cuid,token,str){
	url += "&cuid="+cuid;
	url += "&tok="+token;
	url += "&tex="+ encodeURI(str);
	playAudio.pause();
	playAudio.src = url;
	playAudio.play();
}
