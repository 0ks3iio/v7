<#if dtoList?exists && dtoList?size gt 0>
	<#if showOne?default('0')=='1'>
		<#---获奖--->
		<div class="encourage">
		<#list dtoList as dto>
			<#if dto.partList1?exists && dto.partList1?size gt 0>
					<#list dto.partList1 as partItem>
                    <div class="encourage_wapper">
                        <div>
                        	<#if "市级"==partItem.mainTitle?default('')>
                        		 <img src="${request.contextPath}/static/eclasscard/standard/show/images/municipal.jpg">
                        	<#elseif "校级"==partItem.mainTitle?default('')>
                        		<img src="${request.contextPath}/static/eclasscard/standard/show/images/school.jpg">
                        	<#else>
                        		 <img src="${request.contextPath}/static/eclasscard/standard/show/images/classes.jpg">
                        	</#if>
                        </div>
                        <div class="encourage_info">
                            <div class="encourage_info_title">
                                <h2>${partItem.mainTitle!}<span>${partItem.timeStr!}</span></h2>
                                <p>${partItem.description!}</p>
                            </div>
                            <div class="encourage_info_star">
                                <div class="star_list" number="${partItem.starScore}">
                                    <i class="number"></i>
                                </div>
                                <div class="star_number">${partItem.starScore}星</div>
                            </div>
                        </div>
                  </div>
				</#list>
			</#if>
		</#list>
		</div>
	<#else>
	<div class="Com_content">
	     <div class="Com_content_dl">
	      <#list dtoList as dto>
		  	<div class="Com_content_dd" id="com-dd-${dto.itemkey!}">
	        	<h2 class="Com_content_title">${dto.mainTitle!}</h2>
	       		 <div class="Com_content_border">
	                <ul class="content_fristList">
	                	<#if dto.partList1?exists && dto.partList1?size gt 0>
	                	<#list dto.partList1 as partItem>
	                    <li class="Com_content_ratings">
	                    	<#if partItem.starScore?default('')!=''>
		                    	<#if showOne?default('0')=='1' || partItem.fiveBetter?default('0')=='1'>
		                        	<ul class="rating" number="${partItem.starScore!}">
			                            <li><span>${partItem.mainTitle!}</span></li>
			                            <li class="rating-item newStars"><i class="number"></i></li>
			                        </ul>
		                        <#else>
			                        <ul class="rating" count="${partItem.starScore!}">
			                            <li><span>${partItem.mainTitle!}</span></li>
			                            <li class="rating-item start"><i class="count"></i></li>
			                        </ul>
		                        </#if>
	                        <#else>
	                        <ul class="rating" count="5">
	                            <li><span>${partItem.mainTitle!}</span></li>
	                            <li class="rating-item">暂未评星</li>
	                        </ul>
	                        </#if>
	                    </li>
	                    </#list>
	                    </#if>
	                </ul>
	                <#if dto.partList2?exists && dto.partList2?size gt 0>
	                <ul class="content_fristList">
	                	
	                	<#list dto.partList2 as partItem>
	                    <li class="Com_content_ratings">
	                    	<#if partItem.starScore?default('')!=''>
	                        <ul class="rating" count="${partItem.starScore!}">
	                            <li><span>${partItem.mainTitle!}</span></li>
	                            <li class="rating-item start"><i class="count"></i></li>
	                        </ul>
	                        <#else>
	                        <ul class="rating" count="5">
	                            <li><span>${partItem.mainTitle!}</span></li>
	                            <li class="rating-item">暂未评星</li>
	                        </ul>
	                        </#if>
	                    </li>
	                    </#list>
	                </ul>
	               </#if>
	            </div>
	        </div>
	        </#list>
	    </div>
	   <#if dtoList?size gt 1>
	    <ul class="Com_content_list">
	   		<#list dtoList as dto>
	        <li class="com-item <#if (dto.itemkey==showTitleKey?default('')) || (showTitleKey?default('')=='' && dto_index==0)>active</#if>" >
	       		 <a data-action="tab" id="com-xx-${dto.itemkey!}"  href="#com-dd-${dto.itemkey!}">${dto.mainTitle!}</a></li>
	        </#list>
	    </ul>
	    </#if>
	</div>
	<script>
		$(function(){
			
			//滚动定位在上方
			$(".quality_space").scrollTop(0);
			<#if dtoList?size gt 1>
				var length = $('.Com_content_list li').length;
	            $('.scroll-content').scroll(function() {
	                let dd = $('.Com_content_dd')
	                let that = $('.Com_content_list li')
	                for (let i = 0; i < length; i++) {
	                    let top = dd.eq(i).offset().top - dd.eq(i).height()-40
	                    let scrop = $('.scroll-content').scrollTop();
	                    if (scrop > top) {
	                        that.eq(i).addClass('active');
	                        that.eq(i).siblings().removeClass('active');
	                    }
	                }
	            })
				$(".Com_content_list").find("li").on("click",function(){
				if($(this).hasClass('com-item')){
						$(this).addClass('active').siblings().removeClass('active');
					}
				})
				<#if  showTitleKey?default('')!="">
					var element = document.getElementById('com-dd-${showTitleKey!}'); 
					if(element){
						element.scrollIntoView();
					}
				</#if>
			</#if>
		})
		
	</script>
</#if>
<#else>
<div class="no-content">
    <div>
        <img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png">
        <p>当前暂无内容，请至后台添加</p>
    </div>
</div>
</#if>
