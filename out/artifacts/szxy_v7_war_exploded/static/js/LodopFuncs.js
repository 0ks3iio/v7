var CreatedOKLodop7766 = null, CLodopIsLocal;
function docType() {
    var standard = " Trasitional";
    var dtd = "loose";
    return '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01' + standard + '//EN" "http://www.w3.org/TR/html4/' + dtd + '.dtd">';
}

function getHead() {
    var head = "<head><title></title>";
    jQuery(document).find("link")
        .filter(function () {
            return jQuery(this).attr("rel").toLowerCase() == "stylesheet";
        })
        .filter(function () { 
            var media = jQuery(this).attr("media");
            if (media == undefined) {
                return true;
            }
            else {
                return (media.toLowerCase() == "" || media.toLowerCase() == "all" || media.toLowerCase() == "print");
            }
        })
        .each(function () {
            head += '<link type="text/css" rel="stylesheet" href="' + jQuery(this).attr("href") + '" >';
        });
    head += "<style>.noprint{display:none;}</style></head>";
    return head;
}

function getBody(printElement, dClass) {
	//一些对打印有影响的样式，需要去除掉
	var ss = "";
	if(dClass){
		for(c = 0; c < dClass.length; c ++){
			var objs = jQuery("." + dClass[c]);
			objs.removeClass(dClass[c]);
			ss = printElement.html();
			objs.addClass(dClass[c]);
		}
	}
	if(ss == ""){
		ss = printElement.html();
	}
    ss = '<body>' + ss + '</body>';
    return ss;
}

function getPrintContent(printElement, disableclasses){
return docType() + "<html>" + getHead() + getBody(printElement, disableclasses) + "</html>";
}

//====判断是否需要 Web打印服务CLodop:===
//===(不支持插件的浏览器版本需要用它)===
function needCLodop() {
    try{
        return true;
    } catch(err) {return true;};
}

//====页面引用CLodop云打印必须的JS文件,用双端口(8000和18000）避免其中某个被占用：====
if (needCLodop()) {
    var src1 = "http://localhost:8000/CLodopfuncs.js?priority=1";
    var src2 = "http://localhost:18000/CLodopfuncs.js?priority=0";

    var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
    var oscript = document.createElement("script");
    oscript.src = src1;
    head.insertBefore(oscript, head.firstChild);
    oscript = document.createElement("script");
    oscript.src = src2;
    head.insertBefore(oscript, head.firstChild);
    CLodopIsLocal = !!((src1 + src2).match(/\/\/localho|\/\/127.0.0./i));
}


//====获取LODOP对象的主过程：====
function getLodop(contextPath, oOBJECT, oEMBED) {
	var contextPath = contextPath === undefined ? _contextPath : contextPath;//需要重构contextPath，由方法参数传递
    var strHtmInstall = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='" + contextPath + "/static/downfolder/install_lodop32.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    var strHtmUpdate = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='" + contextPath + "/static/downfolder/install_lodop32.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    var strHtm64_Install = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='" + contextPath + "/static/downfolder/install_lodop64.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    var strHtm64_Update = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='" + contextPath + "/static/downfolder/install_lodop64.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    var strHtmFireFox = "<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
    var strHtmChrome = "<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";
    var strCLodopInstall_1 = "<br><font color='#FF00FF'>Web打印服务CLodop未安装启动，点击这里<a href='" + contextPath + "/static/downfolder/CLodop_Setup_for_Win32NT.exe' target='_self'>下载执行安装</a>";
    var strCLodopInstall_2 = "<br>（若此前已安装过，可<a href='CLodop.protocol:setup' target='_self'>点这里直接再次启动</a>）";
    var strCLodopInstall_3 = "，成功后请刷新本页面。</font>";
    var strCLodopUpdate = "<br><font color='#FF00FF'>Web打印服务CLodop需升级!点击这里<a href='" + contextPath + "/static/downfolder/CLodop_Setup_for_Win32NT.exe' target='_self'>执行升级</a>,升级后请刷新页面。</font>";
    var LODOP;
    try {
        var ua = navigator.userAgent;
        var isIE = !!(ua.match(/MSIE/i)) || !!(ua.match(/Trident/i));
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch (err) {}
            if (!LODOP && document.readyState !== "complete") {
                alert("网页还没下载完毕，请稍等一下再操作.");
                return;
            }
            if (!LODOP) {
                layerTipMsgWarn('提示',strCLodopInstall_1 + (CLodopIsLocal ? strCLodopInstall_2 : "") + strCLodopInstall_3);
                return;
            } else {
                if (CLODOP.CVERSION < "3.0.9.2") {
                    layerTipMsgWarn('提示',strCLodopUpdate);
                    return;
                }
                if (oEMBED && oEMBED.parentNode)
                    oEMBED.parentNode.removeChild(oEMBED);
                if (oOBJECT && oOBJECT.parentNode)
                    oOBJECT.parentNode.removeChild(oOBJECT);
            }
        } else {
            var is64IE = isIE && !!(ua.match(/x64/i));
            //=====如果页面有Lodop就直接使用，没有则新建:==========
            if (oOBJECT || oEMBED) {
                if (isIE)
                    LODOP = oOBJECT;
                else
                    LODOP = oEMBED;
            } else if (!CreatedOKLodop7766) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 0);
                LODOP.setAttribute("height", 0);
                LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE)
                    LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                else
                    LODOP.setAttribute("type", "application/x-print-lodop");
                document.documentElement.appendChild(LODOP);
                CreatedOKLodop7766 = LODOP;
            } else
                LODOP = CreatedOKLodop7766;
            //=====Lodop插件未安装时提示下载地址:==========
            if ((!LODOP) || (!LODOP.VERSION)) {
                if (ua.indexOf('Chrome') >= 0)
                	layerTipMsgWarn('提示',strHtmChrome);
                if (ua.indexOf('Firefox') >= 0)
                	layerTipMsgWarn('提示',strHtmFireFox);
                layerTipMsgWarn('提示',(is64IE ? strHtm64_Install : strHtmInstall));
                return LODOP;
            }
        }
        if (LODOP.VERSION < "6.2.2.6") {
            if (!needCLodop())
                layerTipMsgWarn('提示',is64IE ? strHtm64_Update : strHtmUpdate);
            return LODOP;
        }

        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):===
        LODOP.SET_LICENSES("浙江万朋教育科技股份有限公司","2849DAC7C273EE56139DBE4F3C37E863","","");
        //===========================================================
        return LODOP;
    } catch(err) {alert("getLodop出错:"+err);};
};

