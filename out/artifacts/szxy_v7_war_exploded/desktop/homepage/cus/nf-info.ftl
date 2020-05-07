<!--- 用户设置 -->
<a href="javascript:;" class="" data-toggle="dropdown">
	<img class="nav-user-photo" src="${avatarUrl!}"  style="width: 39px;height: 39px;"/>
    <span class="user-info">${realName!}</span>

    <i class="wpfont icon-caret-down"></i>
</a>
 <#if !(isWenXuan?default(false))>
	<div class="dropdown-menu dropdown-menu-user">
	    <ul class="user-list user-list-hover">
			<#if multiIdentity?exists && multiIdentity?size gt 0>
				<#list multiIdentity as u>
	                <li id="li-${u.userName!}">
	                    <img src="${u.avatarUrl!}" alt="" class="user-avatar" style="width: 39px;height: 39px;">
	                    <h5 class="user-name">${u.realName!}</h5>
	                    <span class="label <#if u.role?default(-1) == 3>label-blue<#elseif u.role?default(-1)==2>label-green<#elseif u.role?default(-1)==9>label-orange<#else>label-yellow</#if>">${u.roleName!}</span>
	                    <div class="user-content">${u.unitName!}</div>
	                    <a href="javascript:;" class="user-switch">切换</a>
	                </li>
				</#list>
			</#if>
	    </ul>
	     <a href="${request.contextPath}/homepage/logout/page?psId=${passportSessionId!}" class="quit">退出账号</a>
	</div>
</#if>
<script>

	$(document).ready(function(){
        $('.nav-bar-user').on('click',function(){
            $(this).next().toggle().end()
                    .closest('li').addClass('open')
                    .siblings().removeClass('open').find('.dropbox').hide();
        });
        $(".nav-user-photo").each(function () {
			var iconUrl = $(this).attr("src");
			$(this).attr("src",iconUrl + "?" + new Date().getTime());
        });
        <#if multiIdentity?exists && multiIdentity?size gt 0>
            <#list multiIdentity as u>
                $("#li-${u.userName!}").click(function () {
                    $.ajax({
                        type: "get",
                        url: "${request.contextPath}/homepage/logout/page?userName=${u.userName!}&psId=${passportSessionId!}",
                        dataType: "json",
                        data: {},
                        success:function (data) {
                            if ( data.success ) {
                                location.href = data.businessValue;
                            } else {
                                showWarnMsg(data.msg);
                                $("#li-${u.userName!}").remove();
                            }
                        }
                    });
                });
            </#list>
        </#if>

        $('.dropdown-menu-user').parent().on('shown.bs.dropdown', function () {
            var container = $('.dropdown-menu-user .user-list');
            var max = $(window).height() - container.offset().top - 55;

            if(container.outerHeight() > max){
                container.slimscroll({
                    height: max
                })
            }
        })
	});

	//passport 切换失败时会调用
    function processError(e) {
        showWarnMsg(e)
    }
</script>