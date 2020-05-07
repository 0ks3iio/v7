<div class="col-sm-${(data.col)!}">
	<div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">${(data.title)!}</h4>
		</div>
		<div class="box-body">
			<table class="table table-striped no-margin">
				<#if data.calendars?exists && data.calendars?size gt 0>
				<thead>
					<tr>
						<th>时间</th>
						<th>事件</th>
					</tr>
				</thead>
				</#if>
				<tbody>
				     <#if data.calendars?exists && data.calendars?size gt 0>
                     <#list data.calendars as calendar>
                        <tr>
                           <td>${calendar.showTime!}</td>                                
                           <td>${calendar.content!}</td>                 
                        </tr>
                    </#list>
	                <#else>
	                	${data.messageEmpty!"暂无内容"}
	                </#if>
				</tbody>
			</table>
			<div class="box-more"><a href="javascript:void(0);" onclick="openMore();">更多&gt;</a></div>
		</div>
	</div>
</div>
<script>
     function openCalendar(fullUrl){
      window.open(fullUrl,'','fullscreen,scrollbars,resizable=yes,toolbar=no');
     }
     function openMore(){
         var dayCalendar2 = "";
         if (!dayCalendar2) {
             dayCalendar2 = '${UuidUtils.generateUuid()}';
		 }
     	breadUpdate(null,null,"日程表",null,dayCalendar2);
     	$("#index-home").load("${request.contextPath}/desktop/index/userCalendar");
     }

</script>
