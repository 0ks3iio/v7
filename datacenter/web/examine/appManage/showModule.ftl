<div class="layer-container">
	<div class="module-select-wrap clearfix">
		<div >
			<table class="table table-striped table-outline table-hover no-margin" id = "modelManage">
			    <tr>
		            <th>id</th>
		            <th>模块id</th>
		            <th>模块名</th>
		            <th>挂载目录</th>
		            <th>排序号</th>
		            <th>模块类型</th>
		            <th>单位类型</th>
		            <th>操作</th>
          		</tr>
			<#if showModel?exists&& showModel?size gt 0>
             <#list showModel as model>
             	<tr  class="${model.id!}" >                  
                  	<td name="id">${model.id!}</td>
                  	<td name="mid">${model.mid!}</td>
                  	<td name="modelName"> 
                  	<input type="text" id="modelName"  class="form-control"  value="${model.name!}">
                  	</td>
                  	<td name="parentId">
                  	  <#if model.parentId == -1>
	                  	${model.parentId!}
	                  <#else>
								<label for="" class="filter-name">子系统&nbsp&nbsp&nbsp：</label>
								<div class="filter-content" style="width: 160px; height:33px" >
									<select   id='subId' style="width: 160px;" onchange="changeServer(this.value,${model.id!},${model.unitClass!})">
								      <#if apps?exists && apps?size gt 0>
										<#list apps as app>
										   <#if model.subSystem == app.subId >
				                              <option name='subId'  value ="${app.subId!}" selected="selected">${app.name!}</option>
				                            <#else>
				                             <option name='subId' value="${app.subId!}">${app.name!}</option>
				                            </#if>
										</#list>
								      </#if>
									</select>
								</div>
								<label for="" class="filter-name">上级目录：</label>
								<div class="pub-search fn-left " id="modelParent">
								        <#if parentIdMap??&& parentIdMap?size gt 0>
						                  	<#if parentIdMap["${model.unitClass!}"]?exists && parentIdMap["${model.unitClass!}"]?size gt 0>                                 
						                  		<select id="parentId" class = "show${model.id!}" style="width: 160px; height:23px">
												    <#list parentIdMap["${model.unitClass!}"] as mo>
												        		<!---
									                            <#if mo.id== model.parentId >
									                              <input type="hidden"  id="dparentId" value="${model.parentId!}" />
									                              <input type="text" value="${mo.name!}" class="txt" id="search${model.id!}" >
									                            </#if>
									                            -->
									                     <option value="${mo.id!}" <#if mo.id== model.parentId>selected</#if> >${mo.name!}</option>
													</#list>	
												</select>				     				     
										    </#if>
						                </#if>
					                    <div id="${model.id!}" class="pub-search-list" style="display:none"  >                            
					                    </div>
					                    <a id="testOne" ></a>
					            </div>
						</#if>
                  	</td>
                  	<td name="displayOrder">${model.displayOrder!}</td>
                  	<td name="type">${model.type!}</td>
                  	<td name="unitName">
                  	<#if model.unitClass == 1>
                  	 教育局
                  	<#else>
                  	 学校
                  	</#if>
                  	</td>
                  	<td width="10%" id = "showb" name="mark">
                  	<button type="hidden" class="btn btn-blue "  value = "${model.mark!}" />
                    <input type="hidden"  id="mark" />
                  	</td>
               </tr>
             </#list>
            </#if>
			</table>
		</div>
	</div>
</div>
<script>
  
  $('#showb .btn').on('click',function(){
	if($(this).hasClass('btn-blue')){
	    $(this).removeClass('btn-blue');
		$(this).addClass('btn-white');
		$(this).text("停用");
		$(this).parents("tr").find("#mark").val("1");
	}else{
	    $(this).removeClass('btn-white');
		$(this).addClass('btn-blue');
		$(this).text("启用");
		$(this).parents("tr").find("#mark").val("-1");
	}
});

  //判断应用是否已经添加
	$(document).ready(function(){
	  $('#showb .btn').each(function(){
	     if($(this).val() == '1'){
	         $(this).removeClass('btn-blue');
		     $(this).addClass('btn-white');
		     $(this).text("停用");
		     $(this).parents("tr").find("#mark").val("1");
	     }else{
	         $(this).text("启用");
	         $(this).parents("tr").find("#mark").val("-1");
	         
	     }
	  })
	});
  
  //查找子系统下的全部目录
  function changeServer(subId,modelId,unitClass){
  	$.ajax({
  		url : "${request.contextPath}/system/server/findParentId?subId="+subId,
  		type: "post",
  		dataType:"json",
  		success: function(data) {
  			var optionHtml = "";
  			for(var m in data) {
  			    if(unitClass == data[m].unitClass){
  				optionHtml += '<option value="'+data[m].id+'">'+data[m].name+'</option>'
  			    }
  			}
  			$(".show"+modelId).html(optionHtml);
  		}
  	});
  	<#---
    $("#"+modelId).load("${request.contextPath}/system/server/findParentId?subId="+subId,function(data,status){
	        if (status=="success"){ 
			         $("#"+modelId).html("<select id='parentId'  class='" + subId+ "'></select>");
			   if(data=='没有目录'){
			        $('<option></option>').html(data).appendTo('select');       
	           }
	         document.getElementById("search"+modelId).style.display="none";
	         document.getElementById(modelId).style.display="";
	         var  d = JSON.parse(data);
		     $.each(d,function(index,item){
		         if(index==0){ 
		            return true; 
		         }
		         var unitCl = item.unitClass;
		         var modelName = item.name;
		         var modelId=item.id;
		         if(unitClass == unitCl){
		          var option = $("<option name='parentId'></option>").val(modelId).html(modelName)
		          $('.'+subId).append(option);
		         }
	         })
       }
       });
       -->
  }
  
  
  
  
  

</script>

