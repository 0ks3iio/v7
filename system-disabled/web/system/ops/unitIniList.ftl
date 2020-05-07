<#import "/fw/macro/webmacro.ftl" as w>
<div role="tabpanel" >
    <div class="filter filter-f16">
      <div class="filter-item">
            <span class="filter-name">行政区划：</span>
            <div class="filter-content">
            <@w.region regionCode=regionCode regionName=regionName/>
            </div>
        </div>
        
        <div class="filter-item">
            <span class="filter-name">单位名称：</span>
            <div class="filter-content">
                <input type="text" class="form-control" id="unitName" value="${unitName }"/>
            </div>
        </div>
        <div class="filter-item">
            <button class="btn btn-blue" id="searchBtn">查找</button>
        </div>
    </div>
    <table class="table table-striped table-hover no-margin">
      <thead>
          <tr>
              <th>单位名称</th>
              <th>单位编号</th>
              <th>行政区划</th>
              <th>操作</th>
          </tr>
      </thead>
      <tbody>
            <#if unitList?exists && unitList?size gt 0>
                <#list unitList as unit>
                  <tr>
                      <td>${unit.unitName!}</td>
                      <td>${unit.unionCode!}</td>
                      <td>${unit.regionName!}</td>
                      <td>
                          <a href="javascript:;" class="table-btn color-blue js-modify" data-id="${unit.unitId}" >设置参数</a>
                      </td>
                  </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    <#if unitList?exists && unitList?size gt 0>
        <@w.pagination2  container="#TabContentDiv" pagination=pagination page_index=2/>
    </#if>
</div>
<!-- 单位配置修改弹层 -->
<div id="unitIniEditLayer" class="layer layer-edit">
</div>

              
<script>
  $("#unitName").bind('keydown',function(event){  
    if(event.keyCode == "13")      
    {  
      search();
    }  
  });  

  //搜索
  $('#searchBtn').on('click',search);
  
  function search(){
    var unitName = $("#unitName").val();
    var region = $("#regionCode").val();
    var regionName = $("#regionName").val();
    if(regionName == ""){
      region = "";
      $("#regionCode").val("");
    }
    var url = "${request.contextPath}/system/ops/sysOption/unitIni?unitName="+unitName+"&region="+region+"&regionName="+regionName;
    tabContentLoad(encodeURI(encodeURI(url)));
  }


  $('.js-modify').click(function(){
      var $this = $(this);
      var unitId = $this.data("id");
      var url =  '${request.contextPath}/system/ops/sysOption/unitIni/edit?unitId='+unitId;
	  $("#unitIniEditLayer").load(url,function() {
			  layerShowEdit();
			});
      
  });
 function layerShowEdit(){
     layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '设置单位参数',
	    	area: ['600px', '300px'],
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    saveUnitIni(index);
			  },
	    	content: $("#unitIniEditLayer")
	    })
    }
</script>              

