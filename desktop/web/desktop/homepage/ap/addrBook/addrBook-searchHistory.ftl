 <h5>搜索${showString!}</h5>
    <#if sessionUsers ?? && sessionUsers ?size gt 0>
    <#list sessionUsers as user>
	<li>
		<div id ="d" data-toggle="show-user-detail">
		 <img src="${user.avatarUrl!}" alt="" class="user-avatar user-avatar-search">  
			<h5 class="user-name user-name-big">${user.userName!}</h5>
		<#--	<div class="user-content">${user.content!}</div> -->
        	<div class="user-action"><a  class="i fa fa-envelope"></a></div> 
	<#--	<div class="user-action"><a href = "${request.contextPath}/desktop/mailList/sendMsg?userName= ${user.realName!}" class="i fa fa-envelope"></a></div> -->
		</div>
		<div class="user-detail">
			<ul class="user-metadata">
			    <li>
					<i class="fa fa-envelope"></i>
					<span class="user-metadata-name">单位</span>
					<span class="user-metadata-content">${user.unitName!}</span>
				</li>
				<li>
					<i class="fa fa-envelope"></i>
					<span class="user-metadata-name">部门</span>
					<span class="user-metadata-content">${user.deptName!}</span>
				</li>
				<li>
					<i class="fa fa-briefcase"></i>
					<span class="user-metadata-name">职务</span>
				<#--<span class="user-metadata-content">${StringUtils.substring("${user.duty!}", 0, StringUtils.indexOf("${user.duty!}", ",", StringUtils.indexOf("${user.duty!}",",")+1))}</span>-->
					<span class="user-metadata-content">${user.duty!}</span>
				</li>
				<li>
					<i class="fa fa-phone"></i>
					<span class="user-metadata-name">电话</span>
					<span class="user-metadata-content">${user.linkPhone!}</span>
				</li>
				<li>
					<i class="fa fa-mobile fa-lg"></i>
					<span class="user-metadata-name">手机</span>
					<span class="user-metadata-content">${user.personTel!}</span>
				</li>
				<li>
					<i class="fa fa-envelope"></i>
					<span class="user-metadata-name">邮箱</span>
					<span class="user-metadata-content">${user.email!}</span>
				</li>
			</ul>
		</div>
	</li>
	</#list>
	<#else>
	<div id ="d" data-toggle="show-user-detail">
	<li class="text-center tips tip-grey">${msg!}</li>
	</div>	
	</#if>
<script>

// 查看通讯录好友信息
	$('#d[data-toggle=show-user-detail]').on('click',function(){
		if($(this).parent().hasClass('open')){
			$(this).parent().removeClass('open');
		}else{
			$(this).parent().addClass('open').siblings().removeClass('open');
		}
	});
	
	$(document).ready(function(){
		$(".user-avatar-search").each(function(){
			var iconUrl = $(this).attr("src");
			$(this).attr("src",iconUrl+"?"+new Date().getTime());
		});
	
	});

</script>


