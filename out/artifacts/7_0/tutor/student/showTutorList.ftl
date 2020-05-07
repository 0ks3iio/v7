<h3>导师选择   <#if !noRound>&nbsp;&nbsp;${tutorRound.beginTime?string('yyyy-MM-dd HH:mm')} -- ${tutorRound.endTime?string('yyyy-MM-dd HH:mm')}</#if></h3>
<ul class="tutor-list js-select">
    <#if noRound>
    	暂无轮次进行中
    <#else>
	    <#if listTATD?exists&&listTATD?size gt 0>
	      <#list listTATD as tatd>
			 <li>
		         <a href="javascript:void(0);" class="tutor-item <#if tutorResult?exists && tutorResult.teacherId == tatd.teacher.id>active </#if>" value ="${tatd.teacher.id!}" >${tatd.teacher.teacherName!}<#if tatd.isFull == '1'>(已满员)<#else>（${tatd.isChooseNum!}/${tatd.param!}）</#if></a>
			     <input type="hidden"  id="status" value="${tatd.isFull!}"/>
			     <input type="hidden"  id="tutorRoundId" value="${tutorRoundId!}"/>
			 </li>
	      </#list>
	    <#else>
	      	尚未添加导师
	    </#if>
    </#if>
</ul>

<script>
  $(document).ready(function(){
	  $('.js-select li').each(function(){
	     if($(this).find('#status').val() == '1'){
	         $(this).find('a').addClass('disabled');
	     }
	  })
	});
  $('.js-select a').on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('disabled')){
			return false;
		}else{
			$(this).addClass('active').parent().siblings().find('a').removeClass('active');
			chooseTuror();
		}
	});
   
</script>