
<div class="col-sm-${(data.col)!}">
    <div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">我孩子的作业</h4>
		</div>
		<div class="box-body"> 
			<div class="homework">
				<div class="children">
				   <#if data.students?exists && data.students?size gt 0>
                    <#list data.students as student>
                        <a id="${student.studentId!}" ><img class="studentImage" src="${student.avatarUrl!}" alt="" onclick="change('${student.studentId!}','${student.studentName!}');"></a>
                    </#list>
                   </#if>
				</div>
				<span class="currentChild"></span>
			</div>
			<table class="table table-striped no-margin">
				<thead>
					<tr>
						<th>完成情况</th>
						<th>作业内容</th>
						<th>布置时间</th>
						<th>截止时间</th>
					</tr>
				</thead>
				<tbody id = "showHomeWork">
					 <#if data.homeWork?exists && data.homeWork?size gt 0>
					    <#list data.homeWork as homework>
					        <tr>
								<td>
								   <#if homework.isSubmit == "NO">
								      <span class="label label-yellow">未完成</span>
								   <#elseif homework.isSubmit == "YES">
								      <span class="label label-green">已完成</span>
								   </#if>
								</td>
								<td>
									<a href="javascript:void(0);" onclick="openHomeWork('${homework.homeWorkUrl!}');">${homework.caseName!}</a>
								</td>
								<td>${homework.showSendTime!}</td>
								<td>${homework.showEndTime!}</td>
						    </tr>
					    </#list>
					 <#else>
					 	 
					 </#if>
				</tbody>
			</table>
		</div>
   </div>
</div>   

<script>
    $('img').on('click',function(){
	  $(this).parent().addClass('active');
	  $(this).parent().siblings().removeClass('active');
    });
    //得到显示的学生
	$(document).ready(function(){
	  $('#${data.showStudentId!}').addClass('active'); 
	  $(".currentChild").text("${data.studentName!}");
	  $(".studentImage").each(function () {
		 var url = $(this).attr("src");
		 if ( url ) {
		     $(this).attr("src",url+'?'+new Date().getTime());
		 }
      });
	});

    function change(studentId,studentName){
      $(".currentChild").text(studentName);
      $("#showHomeWork").load("${request.contextPath}/desktop/app/changeShowHome?studentId="+studentId+"&url=${data.url!}"+"&num=${data.num?default('0')}");
    }
    
    function openHomeWork(homeWorkUrl){
     
      window.open(homeWorkUrl,'','fullscreen,scrollbars,resizable=yes,toolbar=no');
    }
   
   

</script>