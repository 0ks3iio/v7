<div class="box-body">
   <ul class="nav nav-tabs" role="tablist" id="gradeUl">
      <li role="presentation" class="active">
          <a href="javascript:void(0)" role="tab" val="" onclick="installView('1');" data-toggle="tab">学科设置</a>
      </li>
       <#if deployRegion?default('') != 'ucan' >
            <li role="presentation">
                <a href="javascript:void(0)" role="tab" val="" onclick="installView('2');" data-toggle="tab">项目设置</a>
            </li>
       </#if>

   </ul>
   <div id="showHead"></div>
</div>
<script type="text/javascript">
    $(function(){
        installView('1');
    });
    function installView(tabIndex){
        if(!tabIndex || tabIndex == '1'){
            var url =  '${request.contextPath}/studevelop/subject/head';
        }else if(tabIndex == '2'){
            var url =  '${request.contextPath}/studevelop/project/head';
        }
        $("#showHead").load(url);
    }
</script>