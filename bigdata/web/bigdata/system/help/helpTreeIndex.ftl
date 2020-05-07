<div class="index bg-fff">
	<div class="height-1of1">
		<div class="box box-default height-1of1 clearfix">
			<div class="left-side-dropdown scrollBar4">
				<dl>
					<dt class="mb-10" style="cursor:pointer;" onclick="loadIntroduction();"><b>帮助与支持</b></dt>
					<#list topModuleList as module>
						<dd>
							<a href="#helptree${module.id!}" data-toggle="collapse" <#if module.helpId! !=""> onclick="loadHelpDetailPage('${module.helpId!}')"</#if>>
								<span class="multilevel">
								<#if module.type =="dir" && secondModuleMap[module.id]?exists && secondModuleMap[module.id]?size gt 0>
									<i class="iconfont icon-caret-down"></i>
								<#elseif module.type !="dir" && helpMap[module.id]?exists && helpMap[module.id]?size gt 0>
									<i class="iconfont icon-caret-down"></i>
								</#if>
								</span>
								<span>${module.name!}</span>
							</a>
							<#if module.type =="dir"> 
								<#if secondModuleMap[module.id]?exists&&secondModuleMap[module.id]?size gt 0>
								<ul class="collapse" id="helptree${module.id!}">
									<#list secondModuleMap[module.id] as secondModule>
									<li class="active">
										<a href="#helptree${secondModule.id!}" data-toggle="collapse" <#if secondModule.helpId! !=""> onclick="loadHelpDetailPage('${secondModule.helpId!}')"</#if>>
											<span class="multilevel"><#if helpMap[secondModule.id]?exists&&helpMap[secondModule.id]?size gt 0><i class="iconfont icon-caret-down"></i></#if></span>
											<span>${secondModule.name!}</span>
										</a>
										<#if helpMap[secondModule.id]?exists&&helpMap[secondModule.id]?size gt 0>
										<ul class="collapse" id="helptree${secondModule.id!}">
											<#list helpMap[secondModule.id] as help>	
											<li>
												<a id="${help.id!}" href="javascript:void(0);" onclick="loadHelpDetailPage('${help.id!}')">
													<span>${help.name!}</span>
												</a>
											</li>
											</#list>
										</ul>
										</#if>
									</li>
									</#list>
								</ul>
								</#if>
							<#else>
									<#if helpMap[module.id]?exists&&helpMap[module.id]?size gt 0>
									<ul class="collapse" id="helptree${module.id!}">
										<#list helpMap[module.id] as help>
										<li class="active">
											<a id="${help.id!}" href="javascript:void(0)" onclick="loadHelpDetailPage('${help.id!}')" data-toggle="collapse">
												<span>${help.name!}</span>
											</a>
										</li>
										</#list>
									</ul>
									</#if> 
							</#if>
						</dd>
                	</#list> 
				</dl>
			</div>
			<div class="left-side-dropdown-rest height-1of1 scrollBar4" style="height:100%;"  >	
				<div id="helpContentDiv"></div>
			</div>
		</div>
	</div>
</div>
<script>
	function loadHelpDetailPage(id) {
		$('#helpContentDiv').load('${request.contextPath}/bigdata/help/preview?id='+id,function(){
			$(".left-side-dropdown-rest").scrollTop(0);
		}); 
    }
    
    function loadIntroduction(){
    	$('#helpContentDiv').load('${request.contextPath}/bigdata/common/introduction'); 
    }
    
    $(function(){
		$('body').on('click','.left-side-dropdown a',function(){
			$('.left-side-dropdown a').removeClass('active');
			$(this).addClass('active');
		})
		<#if helpId! =="">
			loadIntroduction();
		<#else>
			loadHelpDetailPage('${helpId!}');
		</#if>
	})
</script>