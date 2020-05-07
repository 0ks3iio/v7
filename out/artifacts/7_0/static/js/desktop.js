/**
 * Created by shenke on 2016/12/28.
 */
var currentModuleName="";

function goHome(){
    if (!$("#index-home")[0]){
        var html = '<div class="row"> ' +
            '<div class="col-xs-12" id="deskTopContainer" > ' +
            '<div id="index-home" class="row"></div></div></div>';
        $(".page-content").html(html);
    }
    $("#index-home").load(_contextPath+"/desktop/index/index-home",function () {
        breadClose();
        modelHide();
        if(_preModelId != ''){
            closeModel(_preModelId);
            _preModelId = '';
        }
        $("#index-home").css("display","");
        $(".page-content").css("display","");
        $(".page-tabs").find("a.js-pageTab").removeClass("active");
        $(".js-iframe-page").css({
            'padding-bottom': 0
        })
    });
}

/** 顶部初始化**/
function headerInit() {

    $('.panel-collapse').on('shown.bs.collapse',function(){
        $(this).parent().find('.panel-title i')
            .removeClass('fa-caret-right').addClass('fa-caret-down');
    }).on('hidden.bs.collapse',function(){
        $(this).parent().find('.panel-title i')
            .removeClass('fa-caret-down').addClass('fa-caret-right');
    })
}

/** 侧边栏初始化**/
function sideBarInit(){
    var $sidebar=$('.sidebar');
    var $sidebar_toggle=$('.js-siderbar-collapse');
    // 侧边栏展开折叠
    $sidebar_toggle.on('click',function(){

        $('.nav-list > li').removeClass('open').find('.submenu').stop().slideUp(150)

        if($sidebar.hasClass('collapsed')){
            $(this).find('i').removeClass('icon-indent').addClass('icon-dedent');
            $sidebar.stop().animate({
                width: 210
            },150,function(){
                $(this).removeClass('collapsed');
                $(window).trigger('resize');
            })
        }else{
            $(this).find('i').removeClass('icon-dedent').addClass('icon-indent');
            $sidebar.addClass('collapsed')
                .stop().animate({
                width: 60
            },150,function(){
                $(window).trigger('resize');
            })
        }

    })
}

/** 子模块显示初始化**/
function newNavListInit() {
    var $navbar_height=$('.navbar').height(),
        $nav_wrap=$('.js-nav-wrap'),
        $subNav_modal=$('.subNav-modal'),
        $subNav_modal_container=$('.subNav-modal-container'),
        $subNav=$('.subNav'),
        $sidebar=$('.sidebar'),
        $sidebar_toggle=$('.js-siderbar-collapse'),
        $win_height=$(window).height(),
        timerShow,
        timerHide;


    function wpScroll(container, height){
        container.slimscroll({
            height: height
        })
    }

    // 左侧导航滚动
    $(window).resize(function(){

        $win_height = $(window).height();
        $nav_height = $win_height - $nav_wrap.offset().top - 30;  /*30px为底部预留距离*/

        $nav_wrap.slimscroll({destroy:true})
            .css({
                'height':'auto'
            });

        wpScroll($nav_wrap, $nav_height);

    }).trigger('resize');

    function subNavScroll(){
        if($subNav_modal_container.height() >= $win_height - $navbar_height){
            var $subNav_height = $win_height - $navbar_height;
            wpScroll($subNav_modal_container, $subNav_height)
        }
    }

    function showSubmenu(submenu){
        $(submenu).stop().slideDown(150).closest('li').addClass('open');


        //还原点击效果
        // var subId = $(submenu).prev("a").attr("data-show");
        // var dirId = eisCache.get(subId);
        // if (subId && dirId){
        //     var hasOpen = $("#"+dirId).parent("li").hasClass("open")
        //     if(!hasOpen){
        //         $("#"+dirId).click();
        //     }
        // }
    }

    function hideSubmenu(submenu){
        $(submenu).stop().slideUp(150).closest('li').removeClass('open');
    }

    // 下拉展开导航
    $('.nav-list').on('click',function(e){
        //e.stopPropagation();
        
        function getNode(node){
            if ( node.nodeName === 'HTML' ) {
                return null;
            }
            return node.nodeName==='A' ? node : getNode(node.parentNode);
        }
        var $self=$(getNode(e.target));

        if($sidebar.hasClass('collapsed') || !$self.is('A')) return;

        if($self.hasClass('dropdown-toggle')){

            if($self.closest('li').hasClass('open')){
                hideSubmenu( $self.next() );
            }else{
                if($self.text().trim()=="民族一家亲"){
                    $self.closest('li').siblings().removeClass('active')
                        .find('li').removeClass('open active');
                    $self.find("ul").find("li").each(function () {
                        $("this").addClass("open");
                    })
                }else {
                    $self.closest('li').siblings().removeClass('open active')
                        .find('li').removeClass('active');
                }

                showSubmenu( $self.next() );
                if($self.text().trim()=="民族一家亲") {
                    $self.next().find("ul").css('display', 'block');
                    showSubmenu($self.next().find("ul"));
                }
                hideSubmenu( $self.closest('li').siblings().find('.submenu') );
                // 保证在有展开的导航隐藏之后再做计算
                setTimeout(function(){
                    var t = $nav_wrap[0].scrollTop,  							/*当前滚动高度*/
                        tot = $self.offset().top - $nav_wrap.offset().top, 		/*当前点击元素距滚动区域上边沿的距离*/
                        s_h = $self.parent().height(),							/*当前下拉导航的高度*/
                        n_h = $nav_wrap.height();   							/*滚动区域的高度*/

                    // 当当前导航暂开后，显示区域显示不下，当前滚动距离加上两者的差值
                    if( tot + s_h > n_h){
                        var diff = tot + s_h - n_h;

                        $nav_wrap.slimscroll({
                            scrollTo: t + diff + 'px'
                        })
                    }
                },150)
            }
        }else{
            $self.closest('li').addClass('active')
                .siblings().removeClass('open active')
                .find('li').removeClass('active');

            hideSubmenu( $self.closest('li').siblings().find('.submenu') );
        }
    })

    // 二级菜单导航弹框的显示位置
    function posModal(t, b, h){
        $subNav_modal.css({
            'top': t,
            'bottom': b,
            'height': h
        });
    }

    // 关闭二级菜单导航弹框
    function closeModal(element){
        // 移除滚动条
        $subNav_modal_container
            .slimscroll({destroy:true})
            .css({
                'height':'auto'
            });

        posModal('auto','auto','auto');

        element.closest('li').removeClass('hover');
        $('[data-id='+element.data("show")+']').hide().closest($subNav_modal).hide();
    }

    $('.nav-list a[data-show]').on('mouseenter',function(){

        var $this=$(this),
            $top=$this.offset().top-$navbar_height-$(window).scrollTop(),
            $t_h=$win_height-$navbar_height,
            $m_h=0;

        clearTimeout(timerShow);
        timerShow=setTimeout(function(){

            // 切换hover状态、显示对应的二级菜单导航
            $this.closest('li').addClass('hover').siblings().removeClass('hover');

            if(!$sidebar.hasClass('collapsed')) return;

            $('[data-id='+$this.data("show")+']')
                .show().siblings().hide()
                .closest($subNav_modal).show();

            $m_h=$subNav_modal.height();	//获取弹出框的高度

            if( $t_h <= $m_h ){

                //当二级弹出框的高度大于左侧的高度时
                posModal(0, 'auto', $t_h);
                subNavScroll();

            }else if($top < $t_h-$m_h){

                //当前导航项距顶部条的距离小于左侧高度减去二级弹出框的高度时
                posModal($top, 'auto', 'auto');

            }else if($top >= $t_h-$m_h && $t_h > $m_h){

                //当前导航项距顶部条的距离大于等于左侧高度减去二级弹出框的高度，
                //并且左侧高度大于二级弹出框的高度
                posModal('auto', 0, 'auto');
            }
        },150);
    }).on('mouseleave',function(){
        var $in=false,
            $this=$(this);

        clearTimeout(timerShow);
        clearTimeout(timerHide);

        $subNav_modal.on('mouseenter',function(){
            $in=true;
        }).on('mouseleave',function(){
            closeModal($this);
        })

        // 避免鼠标未移到二级菜单就关闭
        timerHide=setTimeout(function(){
            if($in===false){
                closeModal($this);
            }
        },150)
    })


}

/** 日期选择初始化**/
function datePickerInit() {
    // 日期选择
    $('.date-picker').datepicker({
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        format: 'yyyy-mm-dd'
    })
    //show datepicker when clicking on the icon
        .next().on('click', function(){
        $(this).prev().focus();
    });
}

function deskTopCheckVal(container){
    var isOk = true;
    if(!container) return false;
    var tags = ["input:not(:file)","select","textarea"];
    var os ;
    for(var i=0; i<tags.length;i++){
        if(typeof(container) == "string"){
            os = jQuery(container + " " +tags[i]);
        }
        else{
            return ;
        }
        os.each(function () {
            $this = $(this);
            if(!$this.is(":hidden")){
                var value = $this.val();
                var nullable = $this.attr("nullable");
                var type = $this.attr("type");
                if(nullable == "false" && value.trim() == ""){
                    layerError(container+" #"+$this.attr("id"),$this.attr("msgName")+" 不能为空");
                    isOk = false;
                }
                else if ((type == 'number' || type=='int') && value!='' && value!=null) {
                    if (!/^[0-9]*$/.test(value)) {
                        layerError(container+" #"+$this.attr("id"),$this.attr("msgName")+" 请输入合法的数字");
                        isOk = false;
                    } else {
                        var min = parseInt($this.attr("min"));
                        var max = parseInt($this.attr("max"));
                        value = parseInt(value);
                        if (value>max) {
                            layerError(container+" #"+$this.attr("id"),$this.attr("msgName")+" 不能大于"+max);
                            isOk=false;
                        } else if (value<min) {
                            layerError(container+" #"+$this.attr("id"),$this.attr("msgName")+" 不能小于"+min);
                            isOk=false;
                        }
                    }
                }

            }
        });
    }
    return isOk;
}
function layerError(key,msg){
    layer.tips(msg, key, {
        tipsMore: true,
        tips:2,
        time:3000
    });
}
function saveUserSetting(contextPath){
    var $active ;
    $(".user-setting").find("div").each(function () {
        $this = $(this);
        if($this.hasClass("active")){
            $active = $this;
            return ;
        }
    });
    var id = $active.attr("id");
    var options;
    //user-info
    if(id && id=="aa"){
        if(!deskTopCheckVal("#"+id)){
            return ;
        }
        options = {
            url:contextPath+"/desktop/user/info/reset",
            data:dealDValue("#"+id),
            clearForm : false,
            resetForm : false,
            dataType:'json',
            type:'post',
            contentType: "application/json",
            success:function (data) {
                if(data.success){
                    showSuccessMsg(data.msg);
                }else{
                    showErrorMsg("更新个人信息失败");
                }
            }
        }
    }
    //pwd
    else if(id == "bb"){
        if(!deskTopCheckVal("#"+id)){
            return ;
        }
        if(!verifyPassword(contextPath)){
            return ;
        }
        options = {
            url:contextPath+"/desktop/user/pwd/reset",
            clearForm : false,
            resetForm : false,
            dataType:'json',
            contentType: "application/json",
            type:'post',
            success:function (data) {
                if(data.success){
                    showSuccessMsg("密码修改成功");
                }else{
                    showErrorMsg("密码修改失败");
                }
            }
        }
    }
    //image
    else if(id == "cc"){
        JsCropUtils.JsCropSave(function (data) {
            if(data.success){
                showSuccessMsg(data.msg);
            }else{
                showSuccessMsg(data.msg);
            }
        });
    }
    $.ajax(options);

}
function dealDValue(container){
    var tags = ["input","select","textarea"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var value = $this.val();
            var type = $this.attr("type");
            if ((type == 'number' || type=='int') && value!='' && value!=null) {
                value = parseInt(value);
            }
            var name = $this.attr("name");
            name = name || $this.attr("id");
            var exclude = $this.attr("exclude");
            if (!exclude) {
                if (obj[name] && !(obj[name] instanceof Array)) {
                    var array = new Array();
                    array.push(obj[name]);
                    array.push(value);
                    obj[name] = array;
                } else if (obj[name] && obj[name] instanceof Array){
                    obj[name].push(value);
                }else{
                    obj[name] = value;
                }
            }
        });
    }
    return JSON.stringify(obj);
}
function verifyPassword(contextPath){
    var options = {
        url:contextPath+"/desktop/user/pwd/verify?c1="+$("#password").val(),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            if(!data.success){
                //$(".old_err").html("密码错误");
                layerError("#bb #password","原密码错误");
            }else{
                $(".old_err").html("");
            }
            return data.success;
        }
    };
    var isOk = $.ajax(options);
    return isOk;
}
function verify2Pwd(){
    var newPwd = $("#newPassword").val();
    if ( newPwd.length < 6) {
        layerError("#bb #newPassword","密码长度不低于6位");
        return false;
    }
    var password = $("#password").val();
    if($(".old_err").html()!=''){
        return ;
    }
    if(newPwd!='' && password!='' && newPwd == password){
        layerError("#bb #newPassword","不能和原密码相同");
        return false;
    }else{
        $(".new_err").html("");
    }
    var cofNewPwd = $("#cfNewPassword").val();
    if(newPwd==cofNewPwd){
        $(".confirm_err").html("");
        return true;
    }else {
        if(newPwd!='' && cofNewPwd!=''){
        	layerError("#bb #cfNewPassword","密码不一致");
        }
    }
    return true;
}
function showSuccessMsgWithCall(msg, call){
    if (!(call == null || !(call instanceof Function))) {
        showMsg("成功", msg, true, call);
    } else {
        showSuccessMsg(msg)
    }
}
function showSuccessMsg(msg) {
    showMsg("成功",msg,true);
}
function showErrorMsg(msg) {
    showMsg("失败",msg,false);
}
function showWarnMsg(msg) {
    layer.alert(msg,{
        icon:3,
        title:"警告",
        btn:['确定']
    });
}

function showErrorMsgWithCall(msg, call) {
    if (call == null || !(call instanceof Function) ) {
        showErrorMsg(msg)
    } else {
        showMsg("成功",msg,true,call);
    }
}

function showMsg(title,msg,success,call){
    layer.alert(msg,{
        icon:success?1:2,
        title:title,
        btn:['确定'],
        end:function () {
            if (call!=null && call instanceof Function) {
                call();
            }
        },
        yes:function (index) {
            if (success){
                layer.closeAll();
            } else {
                layer.close(index);
            }
        }
    });
}

function cookies(key, value, expire) {
    if (key && value && expire) {
        $.cookie(key, encodeURIComponent(value), {
            "expires": expire
        });
    } else if (key && value) {
        $.cookie(key, encodeURIComponent(value));
    } else if (key) {
        return decodeURIComponent($.cookie(key));
    }
}

function getRoot() {
    var webroot=document.location.href;
    webroot=webroot.substring(webroot.indexOf('//')+2,webroot.length);
    webroot=webroot.substring(webroot.indexOf('/')+1,webroot.length);
    webroot=webroot.substring(0,webroot.indexOf('/'));
    if(webroot==''){
        return "/";
    }
    return "/" + webroot;
}

function pageTabInit(){
    //$(".js-pageTabsContainer").css("width","200px");
    // initMargin();
    bindClickFunction();
}
function openUserSetting(url,contextPath){
    $(".user-setting").load(url,function(){
        layer.open({
            type: 1,
            shade: .5,
            title: '个人设置',
            area: '600px',
            move: '.layer-header',
            btn:['确定','取消'],
            yes:function(index,layero){
                saveUserSetting(contextPath);
            },
            btnAlign: "c",
            content: $('.user-setting')
        })
    });

}

var _preModelId = "";

function openVue(name, url) {

}

/*
 * 模块三种打开方式
 * 2、iframe
 *
 * 1、div（load）
 *    当且仅当桌面和要加载的系统部署于同一个系统且css和js样式没有冲突的时候才可以;
 *    Load div时，当preUrl为空时，使用当前系统域名
 * 3、label（新标签页）
 *    打开一个全新的标签页
 * */
function openModel(id,name,mode,fullUrl,serverName,parentName,subId,dirId,thirdAp){
	  currentModuleName=name;
    //exists active
    //隐藏显示div或者iframe或者index-home
    //	$(".page-tab-wrap").addClass("show");
    if(mode == '2' || mode == '1'){
        modelHide();
        if(_preModelId != ""){
            closeModel(_preModelId);
        }
        breadUpdate(mode,serverName,name,parentName,id,subId,dirId);
    }
    showDefaultBread();
    if($("#pageTab"+id).length > 0){
        if(mode == '2'){
            $(".page-content").css("display","none");
        }
        //activeModel(id);
        refreshModel(id,fullUrl);
        return ;
    }
    if(mode == '2'){
        $(".page-content").css("display","none");
        if (fullUrl.indexOf("?")>0){
            fullUrl = fullUrl + "&showFramework=true&desktopIndex=false&openType=2";
        }else{
            fullUrl = fullUrl + "?showFramework=true&desktopIndex=false&openType=2";
        }
        // fullUrl = fullUrl + "&callBack=;"
        fullUrl = fullUrl.replace("{","%7B");
        fullUrl = fullUrl.replace("}","%7D");
        var iframeHtml = "<iframe class='embed-responsive-item model-iframe model-iframe-show' " + "src='"+fullUrl+"'" + "width='100%' height='100%' id='model-iFrame-"+id+"'></iframe>";
        addPageTabs(name,id,initMargin);
        $(".iframe-page").append(iframeHtml);
        loadIframeCall();
    }else if(mode == '1'){
        $(".page-content").css("display","");
        var divHtml = '<div class="model-div model-div-show" id="model-div-'+id+'" ></div>';
        addPageTabs(name,id,initMargin);
        $("#deskTopContainer").append(divHtml);
        $("#model-div-"+id).load(fullUrl);
    }else if(mode == '3'){
        if(!thirdAp){
            if (fullUrl.indexOf("?")>0){
                fullUrl = fullUrl + "&showFramework=true&desktopIndex=false&openType=3";
            }else{
                fullUrl = fullUrl + "?showFramework=true&desktopIndex=false&openType=3";
            }
        }
        window.open(fullUrl,name);
    }

    _preModelId = id;

    //记录上一次点击
    if (subId && dirId){
        //eisCache.set(subId,dirId);
    }
}

function locationModel(subId,dirId) {

    $("a[data-show='"+subId+"']")[0].scrollIntoView();
    $("#"+dirId).click();
}

function refreshModel(id,fullUrl){
    var $descIFrame = $("#model-iFrame-"+id);
    var $descDiv = $("#model-div-"+id);
    if($descIFrame.length > 0){
        var url = $descIFrame.attr("src");
        $descIFrame.attr("src",url+"&time="+new Date().getTime());
    }
    if($descDiv.length > 0){
        $descDiv.load(fullUrl);
    }
}

function showBreadBack(backFunc, hiden, name) {
    $('#breadDiv div._back_div').show();
    $('#breadDiv div._back_div').find(".back").show().unbind('click').bind('click', function () {
        if (typeof backFunc == "function") {
            backFunc();
        }
        else {
            //goHome();
        }
        //back 之后是否隐藏返回按钮
        if (hiden) {
            //$('#breadDiv div._back_div').hide();
        	hidenBreadBack();
        }
    });
    
    if(name && name!=""){
    	$('#breadDiv div._back_div').find('span.title-name').show();
    	$('#breadDiv div._back_div').find('span.title-name').text(name);
    }else{
    	$('#breadDiv div._back_div').find('span.title-name').hide();
    }
    
}

function showDefaultBread() {
    $('#breadDiv div._back_div').show();
	$('#breadDiv div._back_div').find(".back").hide();
    if(currentModuleName && currentModuleName!=""){
    	$('#breadDiv div._back_div').find('span.title-name').show();
    	$('#breadDiv div._back_div').find('span.title-name').text(currentModuleName);
    }else{
    	$('#breadDiv div._back_div').find('span.title-name').hide();
    }
}

function hidenBreadBack() {
    //$('#breadDiv div._back_div').hide();
    //$('#breadIFrame div._back_iframe').hide();
    showDefaultBread();
}

function breadUpdate(mode,systemName,childName,parentName,id,subId,dirId) {
    if(mode == '2'){
        $("#breadIFrame").addClass("show");
        $("#breadDiv").removeClass("show")
        var $breadSystem = $("#breadIFrame #bread-system");
        var $breadParent = $("#breadIFrame #bread-parent");
        var $breadChild = $("#breadIFrame #bread-child");
    }else{
        $("#breadDiv").addClass("show");
        $("#breadIFrame").removeClass("show")
        var $breadSystem = $("#breadDiv #bread-system");
        var $breadParent = $("#breadDiv #bread-parent");
        var $breadChild = $("#breadDiv #bread-child");
    }

    $breadSystem.html(systemName).unbind("click").bind("click",function () {
        $("a[data-show='"+subId+"']")[0].scrollIntoView()
        if($("a[data-show='"+subId+"']").parent("li").hasClass("open")){
            return ;
        }
        $("a[data-show='"+subId+"']").click();
    });
    if(systemName==null || isBlank(systemName)){
        $breadSystem.parent('li').css("display","none")
    }else{
        $breadSystem.parent('li').css("display","")
    }

    $breadParent.find("a").html(parentName).unbind("click").bind("click",function () {
        $("a[data-show='"+subId+"']")[0].scrollIntoView();
        if($("a[data-show='"+subId+"']").parent("li").hasClass("open")){

        }else{
            $("a[data-show='"+subId+"']").click();
        }
        if($("#"+dirId).parent("li").hasClass("open")){
            return ;
        }
        $("#"+dirId).click();
    });
    if(parentName==null || isBlank(parentName)){
        $breadParent.css("display","none")
    }else{
        $breadParent.css("display","")
    }
    $breadChild.find("a").html(childName).unbind("click").bind("click",function () {
        if(systemName==null || isBlank(systemName)){
            $("#commonModel-"+id).click();
        }else{
            $("#"+id).click();
        }
    });
}

function breadClose(){
    $(".breadcrumb").each(function () {
        $(this).removeClass("show");
    })
}

function addBreadChild(childName,id) {
    var $next = $("#bread-child").next('li');
    if($next && $next.length>0){
        replaceBreadChild(childName);
    }else{
        var firstChildName = $("#bread-child").html();
        $("#bread-child").html("")
        $("#bread-child").append("<li class='active'>'+childName+'</li>")
    }

}

function replaceBreadChild(childName) {

}

function modelHide(){
    $(".iframe-page").css("padding-bottom","");
    $(".iframe-page").find("iframe.model-iframe-show").css("display","none").removeClass("model-iframe-show").addClass("model-iframe-hidden"); //iframe
    $("#deskTopContainer").find(".model-div-show").css("display","none").removeClass("model-div-show").addClass("model-div-hidden");
    $("#deskTopContainer").find("#index-home").css("display","none");
}

function activeModel(id){

    if(document.all){ //判断IE浏览器
        window.event.returnValue = false;
    }
    else{
        event.preventDefault();
    };
    var $objA = $("#pageTab"+id);
    //本不应判断，i标签事件冒泡无法阻止
    if($objA.length){
        modelHide();
        $objA.addClass('active').siblings().removeClass('active');
        var $descIFrame = $("#model-iFrame-"+id);
        if($descIFrame.length){
            $descIFrame.css("display","").removeClass("model-iframe-hidden").addClass("model-iframe-show");
        }else{
            var $descDiv = $("#model-div-"+id);
            if($descDiv.length > 0){
                $descDiv.css("display","").removeClass("model-div-hidden").addClass("model-div-show");
            }
        }
    }else{
        return ;
    }
}
function closeModel(id){
    var aL = $(".js-pageTabsContainer").find("a").length;
    if(aL && aL > 1) {
        var $nowtab = $("#pageTab"+id);
        var $next = $("#pageTab" + id).next();
        var $before = $("#pageTab" + id).prev();
        if($nowtab.hasClass("active")) {
            var descId ;
            if ($before && $before.length > 0) {
                $before.addClass("active").siblings().removeClass('active');
                descId = $before.attr("id").replace("pageTab", "");
            } else if ($next && $next.length > 0) {
                $next.addClass("active").siblings().removeClass('active');
                descId = $next.attr("id").replace("pageTab", "");
            }
            //close
            //activeModel(descId);
        }
    }
    else if(aL == 1){
        //$(".page-tab-wrap").removeClass("show");
        //goHome();
    }

    $("#pageTab"+id).remove();
    var $iframeModel = $("#model-iFrame-"+id);
    var $divModel = $("#model-div-"+id);
    if($iframeModel.length > 0){
        $iframeModel.remove();
    }
    if($divModel.length > 0){
        $divModel.remove();
    }
    return false;
}

function loadIframeCall(){
    var $iframe=$('.js-iframe-page');
    if($iframe.length>0){
        $iframe.each(function(index){
            $iframe.eq(index).css({
                'padding-bottom': $(window).height()-$iframe.eq(index).offset().top
            })
        })
    }
}

//pageTabs add and load iframe
function addPageTabs(name,id,callback){
    var $pageTabContainer = $(".js-pageTabsContainer");
    var a = '<a class="js-pageTab active" href="javascript:;" onclick="activeModel(\''+id+'\');" id="pageTab'+id+'">';
    a +=name;
    a +='<i class="fa fa-times-circle" javascript:; onclick="closeModel(\''+id+'\')"></i>';
    a +='</a>';
    $pageTabContainer.find("a.active").removeClass("active");
    $pageTabContainer.prepend(a);
    var maxWidth = $(".js-pageTab").width();
    var nowWidth = 0;
    $pageTabContainer.find("a").each(function () {
        nowWidth += $(this).width();
    });
    if(nowWidth > maxWidth){
        $(".js-pageTabRight").addClass("show");
    }
    callback();
}

/**pageTabs相关*/
function calcWidth(){
    var $tab = $('.js-pageTab');
    var $sub_w = 0;		//重新计算的时候宽度清零
    var $sup_w = $('.js-pageTabs').width();
    var $left=$('.js-pageTabLeft');
    var $right=$('.js-pageTabRight');

    $tab.each(function(index){
        $sub_w=$sub_w+$(this).outerWidth()+1;	//计算结果向上取整
    })
    $(".js-pageTabsContainer").css("width",$sub_w);
    if($sub_w >= $sup_w){
        $left.addClass('show');
        $right.addClass('show');
    }else{
        $left.removeClass('show');
        $right.removeClass('show');
    }
    return $sub_w;
}

function changeMargin(m){
    $(".js-pageTabsContainer").stop().animate({
        'margin-left': m
    },300)
}

function initMargin(){
    var $sub_w = calcWidth();
    var $sup_w = $(".js-pageTabs").width();
    if($sub_w <= $sup_w){
        changeMargin(0);
    }
}



//绑定点击事件(左右切换)
function bindClickFunction(){
    $(".js-pageTabLeft").bind("click",floatLeft);
    $(".js-pageTabRight").bind("click",floatRight);
}
function floatLeft(){
    var $tab_c = $(".js-pageTabsContainer");
    var $sub_w = calcWidth();
    var $sup_w = $(".js-pageTabs").width();
    var $m=parseInt($tab_c.css('margin-left'));

    if( $m==0 || $sub_w <= $sup_w){
        return false;
    }else if(Math.abs($m) <= $sup_w){
        changeMargin(0);
    }else{
        changeMargin($m+$sup_w);
    }
}
function floatRight(){
    var $tab_c = $(".js-pageTabsContainer");
    var $sub_w = calcWidth();
    var $sup_w = $(".js-pageTabs").width();
    var $m=parseInt($tab_c.css('margin-left'));

    if( $m==$sup_w-$sub_w || $sub_w <= $sup_w){
        return false;
    }else if(Math.abs($m) >= $sub_w-2*$sup_w){
        changeMargin($sup_w-$sub_w);
    }else{
        changeMargin($m-$sup_w);
    }
}
function isBlank(s) {
    var re = /^\s*$/g;
    return re.test(s);
}

//5.0 子系统首页调用
function clickModule(id) {
    if (id && $("#"+id).length > 0)
    $("#"+id).click();
}

if($(".navbar-fixed-bottom")) {
	$(".page-content").css("padding-bottom","57px");
};

/*//包含小数点数字输入框
	$('.form-shuzi input.form-control').keyup(function(){
		var num = /^\d+(\.\d{0,2})?$/;
		var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
		if (max === undefined) {
			max = 999;
		} else{
			max = parseFloat(max);
		}
		if($(this).attr("numtype") == "int") {
			num = /^\d*$/;
		}else{
			num = /^\d+(\.\d{0,2})?$/;
		}
		if(!num.test($(this).val())){
			$(this).val('');
			$(this).change();
		};
		if($(this).val() > max) {
			$(this).val(max);
			$(this).change();
		}
	});
	$('.form-shuzi > .btn').click(function(e){
		e.preventDefault();
		var $num = $(this).siblings('input.form-control');
		var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
		var val = $num.val();
		if (!val ) val = 0;
		var num = parseFloat(val);
		var step = $num.parent('.form-shuzi').attr('data-step');
		if (step === undefined) {
			step = 1;
		} else{
			step = parseFloat(step);
		}
		if ($(this).hasClass('form-shuzi-add')) {
			num = parseFloat(numAdd(num,step));
		} else{
			num = parseFloat(numSub(num,step));
			if (num <= 0) num = 0;
		}
		if(num > max) {
			$num.val(max);
			$num.change();
		}else{
			$num.val(num);
			$num.change();
		}
	});

	$(".form-shuzi").find("input.form-control").bind("input propertychange", function(event) {
		if($(this).val() == ""){
			$(this).siblings(".evaluate-shuzi-close").removeClass("active");
		}else{
			$(this).siblings(".evaluate-shuzi-close").addClass("active");
		}
	});
	$(".form-shuzi").find("input.form-control").on("change", function(event) {
		if($(this).val() == ""){
			$(this).siblings(".evaluate-shuzi-close").removeClass("active");
		}else{
			$(this).siblings(".evaluate-shuzi-close").addClass("active");
		}
	});

	$(".form-shuzi").find("input.form-control").focus(function() {
		if($(this).val() == ""){
			$(this).siblings(".evaluate-shuzi-close").removeClass("active");
		}else{
			$(this).siblings(".evaluate-shuzi-close").addClass("active");
		}
	})*/

// $(".form-shuzi").find("input.form-control").blur(function() {
// 	var that = this;
// 	setTimeout(function() {
// 		$(that).siblings(".evaluate-shuzi-close").removeClass("active");
// 	}, 200)
// })


function deletesure(title,event,id) {
	if(title == ''){
		title = '确认是否删除'
	}
    if($("#layer-delete-main").length == 0) {
        $("body").append('<div class="layer" id="layer-delete-main">'+
            '<div class="layer-content"><divclass="form-horizontal layer-edit-body" style="margin-bottom: 10px;">' +
            '<i class="sz-icon iconjingshi delete-icon"></i>' + title + '?</div>' +
            '<div class="layer-evaluate-right"><button class="btn btn-white mr10 font-14" onclick="hidelayer()">取消</button>' +
            '<button class="btn btn-blue font-14" onclick="' + event + '(' + "'" +id + "'" +')' + '">	确定</button>' +
            '</div></div></div>')
    }else{
        $("#layer-delete-main").html('<div class="layer-content"><divclass="form-horizontal layer-edit-body" style="margin-bottom: 10px;">' +
            '<i class="sz-icon iconjingshi delete-icon"></i>' + title + '?</div>' +
            '<div class="layer-evaluate-right"><button class="btn btn-white mr10 font-14" onclick="hidelayer()">取消</button>' +
            '<button class="btn btn-blue font-14" onclick="' + event + '(' + "'" +id + "'" +')' + '">	确定</button>' +
            '</div></div>')
    }
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '',
		area: "500px",
		closeBtn: 0,
		content: $("#layer-delete-main")
	});
}
function hidelayer() {
	layer.closeAll();
}
//清空内容
function closenuminput(that) {
	$(that).siblings("input.form-control").val("");
	$(that).siblings("input.form-control").change();
}

/**
 * 加法运算，避免数据相加小数点后产生多位数和计算精度损失。
 * 
 * @param num1加数1 | num2加数2
 */
function numAdd(num1, num2) {
	var baseNum, baseNum1, baseNum2;
	try {
			baseNum1 = num1.toString().split(".")[1].length;
	} catch (e) {
			baseNum1 = 0;
	}
	try {
			baseNum2 = num2.toString().split(".")[1].length;
	} catch (e) {
			baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	return (num1 * baseNum + num2 * baseNum) / baseNum;
};

/**
* 减法运算，避免数据相减小数点后产生多位数和计算精度损失。
* 
* @param num1被减数  |  num2减数
*/
function numSub(num1, num2) {
	var baseNum, baseNum1, baseNum2;
	var precision;// 精度
	try {
			baseNum1 = num1.toString().split(".")[1].length;
	} catch (e) {
			baseNum1 = 0;
	}
	try {
			baseNum2 = num2.toString().split(".")[1].length;
	} catch (e) {
			baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
	return ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision);
}


