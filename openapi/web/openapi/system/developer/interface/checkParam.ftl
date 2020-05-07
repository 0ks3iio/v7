<div class="layer-content">
	<div class="form-horizontal">
	  <table class="table table-middle table-outline">
	     <tbody id="topasstr">
	         <tr class="table-first">
	             <th class="table-title" width="200">接口名称</th>
	             <th class="table-title" width="200">字段选择</th>
	             <th class="table-title" width="240">敏感字段选择</th>
	             <th class="table-title" width="240">每天最大调用次数</th>
	             <th class="table-title" width="240">每次最大获取数量</th>
	         </tr>
	          <#if interfaceDtos?exists && interfaceDtos?size gt 0>
	             <#list interfaceDtos as dto>
	                  <tr class="trmg" data-type="${dto.type!}">
		                   <th class="table-title">${dto.typeName!}</th>
			               <td><div class="filter">
			                   <div class="filter-item block">
			                        <div class="filter-content">
			                             <#assign entities = dto.entitys?if_exists>
			                             <#if entities?exists && entities?size gt 0>
					                          <#list entities as entity>
					                              <#if entity.isSensitive == 0>
					                                <label class="inline"><input checked="checked"  class="wp mgxz" type="checkbox" value="${entity.columnName!}">
						                            <span class="lbl">${entity.displayName!}</span>
						                            </label>
					                              </#if>
		                                        </#list>
		                                    </#if>
			                        </div>
			                    </div>
			                </td>
			               <td><div class="filter">
			                   <div class="filter-item block">
			                        <div class="filter-content">
			                             <#assign entities = dto.entitys?if_exists>
			                             <#if entities?exists && entities?size gt 0>
					                          <#list dto.entitys as entity>
					                              <#if entity.isSensitive == 1>
					                                <label class="inline"><input checked="checked"  class="wp mgxz" type="checkbox" value="${entity.columnName!}">
						                            <span class="lbl">${entity.displayName!}</span>
						                            </label>
					                              </#if>
		                                        </#list>
		                                    </#if>
			                        </div>
			                    </div>
			                </td>
				            <td> 
		                      <div class="form-group">
		                          <input type="text" vtype="digits" min="1" max="100000" value="${dto.maxNumDay!}"  placeholder="每天接口的调用最大次数" class="form-control numEveryDay" id="numEveryDay${dto_index}">  
		                      </div> 
		                   </td>
		                   <td> 
		                      <div class="form-group">
		                          <input type="text" vtype="digits" min="1" max="1000" value="${dto.limitEveryTime!}"  placeholder="每次接口的最大数量" class="form-control limitEveryTime" id="limitEveryTime${dto_index}">  
		                      </div> 
		                   </td>
	                   </tr>
	             </#list>
	           </#if>
	    </tbody>
	    <script>
	  //保存接口审核参数
	    var isSubmit=false;
	    function savePassInter(){
	    	if(isSubmit){
	    		return;
	    	}
	    	isSubmit = true;
	        var check = checkValue('.layer-sensitive');
	    	if(!check){
	    	 	$(this).removeClass("disabled");
	    	 	isSubmit=false;
	    	 	return;
	    	}
	      var developerDto=new Object();
	      developerDto.id=$('#developerId').val();
	      developerDto.ticketKey=$('#ticketKey').val();
	      var passInterfaceDtos=new Array();
	      $('.trmg').each(function(){
	        var interfaceDto=new Object();
	        var $this=$(this);
	        var entitys=new Array();
	        interfaceDto.type=$this.data('type');
	        $this.find('input').each(function(){
	          if($(this).prop('checked')){
	            var entityDto=new Object();                        
	            entityDto.columnName=$(this).val();
	            entitys.push(entityDto);
	          }
	        });
	        if(entitys.length>0){
	          interfaceDto.entitys=entitys;
	        }
	        interfaceDto.maxNumDay = $this.find('.numEveryDay').val();
	        interfaceDto.limitEveryTime = $this.find('.limitEveryTime').val();
	        passInterfaceDtos.push(interfaceDto)
	      });
	      developerDto.passInterfaceDtos=passInterfaceDtos;
	      if(isSubmit){
	    	  var url = '${request.contextPath}/system/developer/passApply';
	    	  var params = {'developerDto':JSON.stringify(developerDto)};
	          $.ajax({
	    		   type: "POST",
	    		   url: url,
	    		   data: params,
	    		   success: function(data){
	    			 if(data.success){
	    				 showSuccessMsgWithCall(data.msg,lookInterface('aa','3'));
	    			 }else{
	    				 showErrorMsg(data.msg);
	    			 }
	    		   },
	    		   dataType: "JSON"
	    		});
	      }
	    }
	    </script>
	  </table>
	</div>
</div>