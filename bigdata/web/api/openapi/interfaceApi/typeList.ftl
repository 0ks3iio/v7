<div class="col-md-4">
          <#if isApply?default(true)>
               <button class="btn btn-block btn-primary mt-5" onclick="applyInterface()">申请数据</button>
          </#if>
          <div class="deve-card-box">
              <ul class="tabs tabs-card" tabs-group="g2">
                  <li class="tab active"><a href="#g2-tab1">全部</a></li>
                  <li class="tab"><a href="#g2-tab3">待审核</a></li>
                  <li class="tab"><a href="#g2-tab5">未通过</a></li>
                  <li class="tab"><a href="#g2-tab4">已通过</a></li>
              </ul>
              <div class="tabs-panel active" tabs-group="g2" id="g2-tab1">
                  <ul class="deve-card-ul">
                      <#if allType??&& allType?size gt 0>
                           <#list allType?keys as key>
                                <li value = "${key!}" onclick="chooseType('${key!}',0)">${allType[key]!}</li>     
                           </#list>
                      <#else>
                                                                      暂无接口    
                      </#if>
                </ul>
             </div>
	         <div class="tabs-panel" tabs-group="g2" id="g2-tab3">
	                <ul class="deve-card-ul">
	                <#if verify??&& verify?size gt 0>
		                  <#list verify?keys as key>
		                     <li value = "${key!}" onclick="chooseType('${key!}',3)" >${verify[key]!}</li>     
		                  </#list>
		             <#else>
		                                                    暂无待审核接口    
		            </#if>
	              </ul>
	          </div>
	          <div class="tabs-panel" tabs-group="g2" id="g2-tab5">
                <ul class="deve-card-ul">
	                <#if unpassType??&& unpassType?size gt 0>
		                  <#list unpassType?keys as key>
		                     <li value = "${key!}" onclick="chooseType('${key!}',2)" >${unpassType[key]!}</li>     
		                  </#list>
		             <#else>
		                                                  暂无未通过接口    
		            </#if>
               </ul>
             </div>
	          <div class="tabs-panel" tabs-group="g2" id="g2-tab4">
                <ul class="deve-card-ul">
	                <#if passType??&& passType?size gt 0>
		                  <#list passType?keys as key>
		                     <li value = "${key!}" onclick="chooseType('${key!}',1)" >${passType[key]!}</li>     
		                  </#list>
		             <#else>
		                                                  暂无通过接口    
		            </#if>
               </ul>
             </div>
      </div>
</div><!-- /.col -->
<div class="col-md-8 interfaceList" >
      请点击左侧接口！
</div>
<script type="text/javascript">
$(function(){
    //tabs
    $('.tabs-card').tabs({
        evt: 1,
        ant: 2
    });

    $(".deve-card-ul li").click(function(){
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
    })
})

function chooseType(type, status){
	var url = "${request.contextPath}/bigdata/api/interface/apiList?type="+type + "&status=" +status +"&dataType="+${dataType!};
    $(".interfaceList").load(url);
}
</script>
