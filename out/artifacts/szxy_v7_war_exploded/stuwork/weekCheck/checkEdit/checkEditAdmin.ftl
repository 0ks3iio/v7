<div class="box box-default box-roleEntry" xmlns="http://www.w3.org/1999/html">
							<div class="box-body">
								<h3><span>值周录入身份选择</span></h3>
								<#list roleTypes as roleType>
								<a class="btn btn-block btn-blue text-left" onclick="toCheckEdit('${roleType!}')" href="javascript:void(0)">
								<#if roleType == '01'>总管理员
								<#elseif roleType == '02'>值周干部
								<#elseif roleType == '03'>值周班
								<#elseif roleType == '04'>学生处
								<#elseif roleType == '05'>保卫处
								<#elseif roleType == '06'>年级组
								<#elseif roleType == '07'>体育老师
								<#elseif roleType == '08'>卫生检查
								</#if>
								<i class="fa fa-long-arrow-right"></i></a>
								</#list>
							</div>
						</div>
					</div><!-- /.model-div -->
<script>
function toCheckEdit(roleType){
	if(roleType == '02'){
		//进入值周表
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkTable/page?blackAdmin=${blackAdmin!}&blackTable=${blackTable!}&roleType="+roleType);
	}else{
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkList/page?blackAdmin=${blackAdmin!}&blackTable=${blackTable!}&roleType="+roleType);
	}
	
}
</script>