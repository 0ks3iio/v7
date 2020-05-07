function connectWeikeJSBridge(callback, btn) {
    if (window.WeikeJSBridge) {
        callback(WeikeJSBridge, btn);
    } else {
        try {
            document.addEventListener('WeikeJSBridgeReady', function(){
               callback(WeikeJSBridge, btn);
            });
        } catch (e){}
    }
}

function AAAA(bridge, btn) {
	$(btn).click(
		function(){
			var d={"actionType":"windowClose"};
	    	bridge.send(d);//发送消息到微课		
		}
	); 
} 

var weikeJsBridge = {
		//关闭--返回到微课界面
		windowClose : function (btn){
			connectWeikeJSBridge(AAAA, btn);
		},
}
