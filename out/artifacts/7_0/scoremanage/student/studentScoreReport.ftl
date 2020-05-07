<head>
<style>
    .table > tbody > tr > td{/*line-height: 1.4;*/ border: 2px solid #333;}
    .valign-top{vertical-align: top !important;}
</style>
</head>
<div class="box box-default">
					<div class="box-body" id="print">					   
					   <p class="text-center" style="font-size: 28px;">
					   <img src="${request.contextPath}/scoremanage/images/logo1.png"/>
					   ${unitName!}${acadyear!}学年${semester!}学期学生成绩单</p>
                       <table class="table table-print no-margin" style="height:800px;">
                          <tbody>
                             <tr>
                                 <td width="25%"><b>班级：${className!}</b></td>
                                 <td width="25%"><b>姓名：${studentName!}</b></td>
                                 <td width="25%"><b>学号：${studentCode!}</b></td>
                                 <td width="25%"><b>操行等第：${optionName!}</b></td>
                             </tr>
                             <tr>
                                 <td colspan="4"><b>
                                                                                           体检信息：
					             <#if dyStuHealthResultList?exists && dyStuHealthResultList?size gt 0>
					                <#list dyStuHealthResultList as item>
					                    ${item.itemName!}&nbsp;${item.itemResult!}${item.itemUnit!}；
					                </#list>
					             </#if>
                                 </b></td>
                             </tr>
                             <tr>
                                 <td><b>事假：天</b></td>
                                 <td><b>病假：天</b></td>
                                 <td><b>旷课：天</b></td>
                                 <td><b></b></td>
                             </tr>
                             <tr>
                                 <td colspan="2" style="height: 400px;" class="valign-top"><b>                                                                                      
                                 <#if scoreInfoList?exists && scoreInfoList?size gt 0>
                                                                                                        必修课：<br>
					                  <#list scoreInfoList as item>
					                      <#if item.inputType=='S'>
					                          ${item.subjectName!}：${item.score!}（考试），${item.toScore!}（总评）<br>
					                      <#else>
					                          <#if item.score=="A">
					                             ${item.subjectName!}：优秀（考试），${item.toScore!}（总评）<br>
					                          <#elseif item.score=="B">
					                             ${item.subjectName!}： 良好（考试），${item.toScore!}（总评）<br>
					                          <#elseif item.score=="C">
					                             ${item.subjectName!}： 中等（考试），${item.toScore!}（总评）<br>
					                          <#elseif item.score=="D">
					                             ${item.subjectName!}：合格（考试），${item.toScore!}（总评）<br>
					                          <#else>
					                              ${item.subjectName!}：不合格（考试），${item.toScore!}（总评）<br>
					                          </#if>
					                      </#if>
					                  </#list>
					             </#if>					                                 
                                 </b></td>
                                 <td colspan="2" style="height: 400px;" class="valign-top"><b>                                                                                        				                                  
					             <#if optionalScoreInfoList?exists && optionalScoreInfoList?size gt 0>
					                                               选修课：<br>
					                  <#list optionalScoreInfoList as item>
					                      <#if item.inputType=='S'>
					                          ${item.subjectName!}（${item.courseTypeName!}）：${item.toScore!}学分<br>
					                      <#else>
					                          <#if item.score=="A">
					                             ${item.subjectName!}：优秀（考试），${item.toScore!}学分<br>
					                          <#elseif item.score=="B">
					                             ${item.subjectName!}： 良好（考试），${item.toScore!}学分<br>
					                          <#elseif item.score=="C">
					                             ${item.subjectName!}： 中等（考试），${item.toScore!}学分<br>
					                          <#elseif item.score=="D">
					                             ${item.subjectName!}：合格（考试），${item.toScore!}学分<br>
					                          <#else>
					                              ${item.subjectName!}：不合格（考试），${item.toScore!}学分<br>
					                          </#if>
					                      </#if>
					                  </#list>
					             </#if>
                                 </b></td>
                             </tr>
                             <tr>
                                 <td class="text-center" style="height: 100px;"><b>期末评语<br></b></td>
                                 <td colspan="3"><b>${remark!}</b></td>
                             </tr>
                             <tr>
                                 <td colspan="3"><b>备注：</b></td>
                                 <td class="text-center"><b>杭州外国语学校教务处<br>${nowDate?string('yyyy-MM-dd')!}</b></td>
                             </tr>
                           </tbody>
                        </table>
					</div>
				</div>