<div class="col-sm-${data.col?default('12')}">
	<div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">${data.title}</h4>
		</div>
		<div class="box-body">
			<ul class="user-list user-list-hover user-list-bordered">
				<#if data.rankingMap??>
	             <#list 1..data.titleSize as key>
	             	<li>
	             	    <span class="user-sort">
	             	    <#if key==1>
	             	    <img src="../images/sort-first.png" alt="">
	             	    <#elseif key==2>
	             	    <img src="../images/sort-second.png" alt="">
	             	    <#elseif key==3>
	             	    <img src="../images/sort-third.png" alt="">
	             	    <#else>
	             	    ${key}	             	 
	                    </#if>
	             	    </span>
						<#if data.showImage?default(false)>
					    <img src="${data.rankingMap['${key}'].bgImg!}" alt="" class="user-avatar">
						</#if>
					    <h5 class="user-name">${data.rankingMap["${key}"].realName!}</h5>
					    <div class="user-content">今天放学别走${data.rankingMap["${key}"].content!}</div>	           	        
	             </li>
	             </#list>
	            </#if>
			</ul>
			<#if true>
			<div class="box-more"><a href="">更多&gt;</a></div>
			</#if>
		</div>
		<div class="box-tools dropdown">
			<a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>
			<ul class="box-tools-menu dropdown-menu">
				<span class="box-tools-menu-angle"></span>
				<li><a class="js-box-setting" href="javascript:void(0);">设置</a></li>
				<li><a class="js-box-remove" href="javascript:void(0);">删除</a></li>
			</ul>
		</div>
	</div>
</div>