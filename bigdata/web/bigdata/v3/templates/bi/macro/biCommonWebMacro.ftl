<#macro common>
<script>
    self.setInterval(function(){
        $.post( "${request.contextPath}/checkLive", function( data ) {
        });
    }, 1200000);
</script>
</#macro>
<#--
分页工具条
container 必须 例如：.class  #id
-->
<#macro pageToolBar container="" class="">
	<script>
        var reloadDataContainer ="${container}"
    </script>
	<div class="pagination ${class!}">
        ${htmlOfPaginationLoad}
    </div>
</#macro>

<#--
科技风格 confirm提示框
-->
<#macro biConfirmTip>
 <div class="layer-wrap layer-decide layer-confirm-4-bi">
     <div class="layer-made layer-bi-style">
         <div class="layer-made-head">
             <span class="layer-head-name"></span><img src="${request.contextPath}/bigdata/v3/static/images/bi/close.png" class="js-confirm-close" alt="">
         </div>
         <div class="layer-made-body">
             <div class="decide-text decide-text-success">
                 <img src="${request.contextPath}/bigdata/v3/static/images/bi/success-mark.png" >
                 <span class="layer-main-name"></span>
             </div>
             <div class="decide-text decide-text-error">
                 <img src="${request.contextPath}/bigdata/v3/static/images/bi/fail-mark.png" >
                 <span class="layer-main-name"></span>
             </div>
             <div class="decide-text decide-text-confirm">
                 <img src="${request.contextPath}/bigdata/v3/static/images/bi/exclaim-mark.png" >
                 <span class="layer-main-name"></span>
             </div>
         </div>
         <div class="layer-made-foot">
             <div class="btn-made unfilled-corner active js-confirm-ok">确定</div>
             <div class="btn-made unfilled-corner js-confirm-close">取消</div>
         </div>
     </div>
 </div>
 <!-- 提示框 -->
<div class="layer-made layer-tips layer-tips-4-bi">
    <span></span>
</div>
<script>
    // 自定义弹窗设置
    function showBIConfirmTips(title,msg,width,height,yesFunction,cancelFunction){
        var ele=$('.layer-confirm-4-bi');

        if(!title)title='提示';
        if(!(typeof yesFunction === "function")){
            yesFunction = function (){$(ele).hide();}
        }
        if(!(typeof cancelFunction === "function")){
            cancelFunction = function (){ $(ele).hide();;}
        }
        $(ele).find('.decide-text').hide();
        $(ele).show();
        $(ele).children().width(width).height(height);
        $(ele).find('.decide-text-confirm').show();
        $(ele).find('.layer-head-name').text(title);
        $(ele).find('.layer-main-name').text(msg);
        $(ele).on('click','.js-confirm-ok',yesFunction)
        $(ele).on('click','.js-confirm-close',cancelFunction)
    }

    // 自定义弹窗设置
    function showBISuccessTips(title,msg,width,height,yesFunction,cancelFunction){
        var ele=$('.layer-confirm-4-bi');

        if(!title)title='提示';
        if(!(typeof yesFunction === "function")){
            yesFunction = function (){$(ele).hide();}
        }
        if(!(typeof cancelFunction === "function")){
            cancelFunction = function (){ $(ele).hide();}
        }
        $(ele).find('.decide-text').hide();
        $(ele).show();
        $(ele).children().width(width).height(height);
        $(ele).find('.decide-text-success').show();
        $(ele).find('.layer-head-name').text(title);
        $(ele).find('.layer-main-name').text(msg);
        $(ele).on('click','.js-confirm-ok',yesFunction)
        $(ele).on('click','.js-confirm-close',cancelFunction)
    }

    // 自定义弹窗设置
    function showBIErrorTips(title,msg,width,height,yesFunction,cancelFunction){
        var ele=$('.layer-confirm-4-bi');

        if(!title)title='提示';
        if(!(typeof yesFunction === "function")){
            yesFunction = function (){$(ele).hide();}
        }
        if(!(typeof cancelFunction === "function")){
            cancelFunction = function (){ $(ele).hide();}
        }
        $(ele).find('.decide-text').hide();
        $(ele).show();
        $(ele).children().width(width).height(height);
        $(ele).find('.decide-text-error').show();
        $(ele).find('.layer-head-name').text(title);
        $(ele).find('.layer-main-name').text(msg);
        $(ele).on('click','.js-confirm-ok',yesFunction)
        $(ele).on('click','.js-confirm-close',cancelFunction)
    }

    // 自定义轻提示
    function showBiTips(txt,time){
        var ele=$('.layer-tips-4-bi');
        $(ele).show();
        $(ele).find('span').text(txt);
        setTimeout(function(){
            $(ele).hide()
        },time)
    }
</script>
</#macro>
