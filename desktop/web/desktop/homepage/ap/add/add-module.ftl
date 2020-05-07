<div class="layer-container">
	<div class="module-select-wrap clearfix">
		<div class="module-nav">
			<ul>
			<#if functionMap??&& functionMap?size gt 0>
             <#list functionMap?keys as key>
             	<li>                  
                  	<a href="javascript:void(0);" >${mcodeSetting.getMcode("DM-ZMGNQ-LX",key)}<i class="fa fa-angle-down"></i></a>                                               
                  	<#if functionMap[key]?exists && functionMap[key]?size gt 0>                                 
				     <ul class="module-style-list clearfix">
				        <#list functionMap[key] as function>
				           <li style="height:70px" data-action="select" class="${function.id} applied " value = "${function.isAdd}">				            
									<a href="#${function.id}" >							
						            <input type = "hidden" value = "${key}" name = "type"  />
						            <input type = "hidden" value = "${function.id}" name = "id"  />							
										<i class="fa fa-calendar"></i>
										<h6>${function.name!}</h6>
									</a>							
						   </li> 		
						</#list>					     				     
				     </ul>				   				    
				    </#if>
             </li>
             </#list>
            </#if>
			</ul>
		</div>
		<div class="module-preview">
			<h5>预览</h5>
			<div class="module-preview-content">						
			<#if functionMap??&& functionMap?size gt 0>                           
             <#list functionMap?keys as key>
                  <#if functionMap[key]?exists && functionMap[key]?size gt 0>                                 
				    <#list functionMap[key] as function>
				    <div id="${function.id!}" class="module-preview-item" >
					  <img src="${request.contextPath}/${function.templateImageUrl!}" alt="">					    
				    </div> 
				    </#list> 
				   </#if>
             </#list>
            </#if>
			</div>
		</div>
	</div>
</div>

<script>
$('.module-nav >ul>li>a').on('click',function(e){
	if($(this).closest('li').hasClass('open')){
		hideSubmenu( $(this).next() )
	}else{
		showSubmenu( $(this).next() );
		hideSubmenu( $(this).closest('li').siblings().find('.module-style-list') );
		$(this).closest('li').siblings().removeClass('open');
	}
})
function showSubmenu(submenu){        
		$(submenu).stop().slideDown(150).closest('li').addClass('open');
}
function hideSubmenu(submenu){
    //    $(submenu).find('li.selected').removeClass('selected');
		$(submenu).stop().slideUp(150).closest('li').removeClass('open');
}
//选择
$('[data-action=select]').on('click',function(){
	if($(this).hasClass('disabled')){
		return false;
	}else if($(this).parent().hasClass('multiselect') || $(this).hasClass('selected')){
		$(this).toggleClass('selected');
	}else{
		$(this).addClass('selected');
	}
});

	//预览
	function doPreview(functionAreaId){
		$("#preview"+functionAreaId).css("display","").load("${request.contextPath}/desktop/app/showFunctionArea?functionAreaId="+functionAreaId);
	}
	
	//判断模块是否已经添加
	$(document).ready(function(){
	  $('[data-action=select]').each(function(){
	     if($(this).val() == '0'){
	        $(this).removeClass('applied');
	     }
	  })
	});
	
	
	
	
</script>