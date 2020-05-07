<div class="col-sm-${data.col}">
	<div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">${data.title}</h4>
		</div>
		<div class="box-body">
			<ul class="user-list user-list-bordered">
				<#if data.rankingMap??>
	             <#list 1..data.showSize as key>
	             <li>	             	               	    
					<img src="${data.rankingMap['${key}'].bgImg!}" alt="" class="user-avatar">
					<h5 class="user-name">${data.rankingMap["${key}"].realName!}</h5>
					<span class="user-tips">${data.rankingMap["${key}"].accountId!}</span>
					<div class="user-content"><a href="">发生日贺卡</a><a href="">发名姓片</a></div>	           	        
	             </li>
	             </#list>
	            </#if>			
			</ul>
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