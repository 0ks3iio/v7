<div class="footer" id="footer">
    <div class="container" id="footer-body">
        <!--
         <div class="footer-body" id="footer-body">
             <p><a href="http://wk.wanpeng.com/" target="_blank">微课掌上通</a>|<a target="_blank" href="http://www.kehou.com">课后网</a>|<a target="_blank" href="http://www.edu88.com/index.html">三通两平台</a></p>
            <p>浙江万朋教育科技股份有限公司版权所有 备案号:浙ICP备05070430号</p>
            <p>客服热线：400-863-2003（教育云平台）400-617-1997（课后网）400-667-1997（家校互联）4000-150-150（微课掌上通）</p>
         </div>
        -->
        
    </div> 
</div>
        <!-- basic scripts -->


<!--[if !IE]> -->
<!--  <script src="${resourceUrl}/components/jquery/dist/jquery.js"></script> -->

<!-- <![endif]-->

<!--[if IE]>
<script src="${resourceUrl}/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script type="text/javascript">
   
    function html_decode(str){   
      var s = "";   
      if (str.length == 0) return ""; 
      s=str;
      s = s.replace(/&lt;/g, "<");   
      s = s.replace(/&gt;/g, ">");   
      s = s.replace(/&nbsp;/g, " ");   
      s = s.replace(/&#39;/g, "\'");   
      s = s.replace(/&quot;/g, "\"");   
      s = s.replace(/<br>/g, "\n");   
      return s;   
    }
    var foot='${foot!}';
    if(typeof(foot)=="string"){
     var foothtml= html_decode(foot);
     var id=document.getElementById('footer-body');
     var div=document.createElement("div");
     div.setAttribute("class", "footer-body");
     div.innerHTML = foothtml;
     id.innerHtml='';
     id.appendChild(div);
    }
    
</script>
<div id="script"></div>

<!-- inline scripts related to this page -->