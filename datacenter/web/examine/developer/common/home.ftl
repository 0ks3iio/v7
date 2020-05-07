<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-daterangepicker/daterangepicker-bs3.css">
<!-- PAGE CONTENT BEGINS -->
<div id="moduleContentDiv">
</div>
<!-- PAGE CONTENT ENDS -->
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.core.js"></script>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.excheck.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/jquery.upload2.js"></script>
<script src="${request.contextPath}/static/js/desktop.js"></script>
<script type="text/javascript">
    $(function(){
        window.moduleContentLoad = function(url,data){
          if(data){
            $("#moduleContentDiv").load(url,data);
            return;
          }
           $("#moduleContentDiv").load(url);
        };

        //默认加载tab内容页
        moduleContentLoad("${request.contextPath}${url}");
    });
    //获取jbk编码字节数
      function jbkLength(str){
        return str.replace(/[^x00-xFF]/g,'**').length;
      }
      function errorText(_this,oldVal){
        _this.val(oldVal);
        _this.next('.txt-val').text(oldVal);
        //_this.siblings('.fa').click();
      }
</script>

