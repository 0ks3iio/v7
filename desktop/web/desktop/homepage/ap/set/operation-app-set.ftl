<div class="commonApp">
		<h4 class="commonApp-title">应用中心</h4>
		<div class="commonApp-body no-padding clearfix">
			<div class="myApp-wrap">
				<table class="table table-striped no-margin">
					<tbody>
					  <input type="hidden" name="isApp" value="${isApp!}" />
					   <#if serverDtos?exists && serverDtos?size gt 0>
                            <#list serverDtos as serverDto> 
									<tr>
										<td width="8%">
											<img width="46" height="46" src="${serverDto.icon!}" alt="">
										</td>
										<td width="42%">
											<h4>${serverDto.name!}</h4>
											<p class="color-grey" title = ${serverDto.descriptionAll!}>${serverDto.description!}</p>
										</td>
										<td width="22%">适用对象：${serverDto.showType!}</td>
									    <td width="20%"></td>
										<td width="10%" id = "showb"><button class="btn btn-blue "  value = "${serverDto.status!}" ></button></td>
										<input type="hidden" name="serverId" value="${serverDto.id!}" />
                                        <input type="hidden" name="status" id="status" />
									</tr>
					      </#list>
			           <#else>
			
			           </#if>
					</tbody>
				</table>
			</div>
			
		</div>
	</div>
</div>

<script>
  $('#showb .btn').on('click',function(){
	if($(this).hasClass('btn-blue')){
	    $(this).removeClass('btn-blue');
		$(this).addClass('btn-white');
		$(this).text("取消");
		$(this).parents("tr").find("#status").val("1");
	}else{
	    $(this).removeClass('btn-white');
		$(this).addClass('btn-blue');
		$(this).text("添加");
		$(this).parents("tr").find("#status").val("0");
	}
});

  //判断应用是否已经添加
	$(document).ready(function(){
	  $('#showb .btn').each(function(){
	     if($(this).val() == '1'){
	         $(this).removeClass('btn-blue');
		     $(this).addClass('btn-white');
		     $(this).text("取消");
		     $(this).parents("tr").find("#status").val("1");
	     }else{
	         $(this).text("添加");
	         $(this).parents("tr").find("#status").val("0");
	         
	     }
	  })
	});
  
  

</script>
