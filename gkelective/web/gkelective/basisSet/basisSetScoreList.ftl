<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
  <table class="table table-striped table-hover table-layout-fixed no-margin">
      <thead>
          <tr>
                <th style="width:15%">学号</th>
				<th style="width:7%">姓名</th>
				<th style="width:5%">性别</th>
				<th style="width:10%">行政班</th>
				<#if coursesList?? && (coursesList?size>0)>
                  <#list coursesList as course>
                      <th >${course.subjectName!}</th>
                  </#list>
                </#if> 
          </tr>
      </thead>
      <tbody>
      <#if gkStuScoreDtoList?? && (gkStuScoreDtoList?size>0)>
          <#list gkStuScoreDtoList as item>
          <tr >  
              <td>${item.student.studentCode!}</td>
              <td>${item.student.studentName!}</td>
              <td>${mcodeSetting.getMcode("DM-XB","${item.student.sex!}")}</td>
              <td>${item.student.className!}</td>
              <#if coursesList?? && (coursesList?size>0)>
                  <#list coursesList as course>
                  <td >
                  	${item.subjectScore[course.id]!}
                  </td>
                  </#list>
              </#if> 
          </tr>
          </#list>
      <#else>
          <tr >
          	<td colspan="88" align="center">
          		暂无数据
          	</td>
          <tr>
      </#if> 
      </tbody>
  </table>
  <#if classId?default("")=="" >
  	<@htmlcom.pageToolBar container="#showScoreListDivId" class="noprint"/>
  </#if>
