<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div id="cc" class="tab-pane active" role="tabpanel">
<div style="display:none">
<div class="print">
	<table class="table table-striped table-hover no-margin" style="width:1000px" >
	                   		<tr>
		                        <td colspan="3">
		                  			<h3>单科统计结果（共${courseName2Count?size}种）</h3>
		                  		</td>
	                  		</tr>
	                      <tr>
	                          <td>序号</td>
	                          <td>学科</td>
	                          <td>选课人数</td>
	                      </tr>
                    	<#if courseName2Count?exists>
                            <#list courseName2Count?keys as key>
	                        <tr>
                            	<td>${key_index+1}</td>
                            	<td>${key}</td>
                                <td>
                                	<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="doQuery('${courseName2Id[key]}','0');">${courseName2Count[key]}</a>
                                </td>
	                        </tr>
                            </#list>
                        </#if>
	                   		<tr>
		                        <td colspan="3">
		                  			<h3>两科统计结果（共${gk2Condition?size}种）</h3>
		                  		</td>
	                  		</tr>
	                      <tr>
	                          <td>序号</td>
	                          <td>学科</td>
	                          <td>选课人数</td>
	                      </tr>
	                      <#if gk2Condition?? && (gk2Condition?size>0)>
	                      <#list gk2Condition as gkc>
	                          <tr>
	                              <td>${gkc_index+1}</td>
	                              <td>
	                                  <span>${gkc.subNames[0]!}</span>、
	                                  <span>${gkc.subNames[1]!}</span>
	                              </td>
	                              <td> 
	                              	<#assign subIds=courseName2Id[gkc.subNames[0]] +','+ courseName2Id[gkc.subNames[1]]>
	                              	<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="doQuery('${subIds!}','1');">${gkc.sumNum!}</a>
	                              	<#assign subIds = ''>
	                              </td>
	                          </tr>
	                      </#list>
	                      </#if>
	                   		<tr>
		                        <td colspan="3">
		                  			<h3>三科统计结果（共${gk3Condition?size}种）</h3>
		                  		</td>
	                  		</tr>
	                      <tr>
	                           <td>序号</td>
	                          <td>学科</td>
	                          <td>选课人数</td>
	                      </tr>
	                  <#if gk3Condition?? && (gk3Condition?size>0)>
                          <#list gk3Condition as gkc>
                              <tr>
                                  <td>${gkc_index+1}</td>
                                  <td>
                                      <span>${gkc.subNames[0]!}</span>、
                                      <span>${gkc.subNames[1]!}</span>、
                                      <span>${gkc.subNames[2]!}</span>
                                  </td>
                                  <td> 
                                  	<#assign subIds=courseName2Id[gkc.subNames[0]] +','+ courseName2Id[gkc.subNames[1]] +','+ courseName2Id[gkc.subNames[2]]>
                                  	<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="doQuery('${subIds!}','1');">${gkc.sumNum!}</a>
                                  	<#assign subIds = ''>
                                  </td>
                              </tr>
                          </#list>
                          </#if>
	              </table>
	</div>
</div>
      <div class="row ">
          <div class="col-md-6">
              <!-- S 单科统计结果 -->
              <div>
	              <h3>单科统计结果（共${courseName2Count?size}种）</h3>
	              <table class="table table-striped table-hover no-margin">
	                  <thead>
	                      <tr>
	                          <th>序号</th>
	                          <th>学科</th>
	                          <th>选课人数</th>
	                      </tr>
	                  </thead>
	                  <tbody>
                    	<#if courseName2Count?exists>
                            <#list courseName2Count?keys as key>
	                        <tr>
                            	<td>${key_index+1}</td>
                            	<td>${key}</td>
                                <td>
                                	<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="doQuery('${courseName2Id[key]}','0');">${courseName2Count[key]}</a>
                                </td>
	                        </tr>
                            </#list>
                        </#if>
	                  </tbody>
	              </table>
              </div><!-- E 单科统计结果 -->
              <div>
              		<h3>两科统计结果（共${gk2Condition?size}种）</h3>
	              <table class="table table-striped table-hover no-margin">
	                  <thead>
	                      <tr>
	                          <th>序号</th>
	                          <th>科目</th>
	                          <th>选课人数</th>
	                      </tr>
	                  </thead>
	                    <tbody>
	                      <#if gk2Condition?? && (gk2Condition?size>0)>
	                      <#list gk2Condition as gkc>
	                          <tr>
	                              <td>${gkc_index+1}</td>
	                              <td>
	                                  <span>${gkc.subNames[0]!}</span>、
	                                  <span>${gkc.subNames[1]!}</span>
	                              </td>
	                              <td> 
	                              	<#assign subIds=courseName2Id[gkc.subNames[0]] +','+ courseName2Id[gkc.subNames[1]]>
	                              	<a class="table-btn show-details-btn" href="javascript:void(0);" onclick="doQuery('${subIds!}','1');">${gkc.sumNum!}</a>
	                              	<#assign subIds = ''>
	                              </td>
	                          </tr>
	                      </#list>
	                      </#if>
	                    </tbody>
	              </table>
              </div>
          </div>
          <div class="col-md-6">
          	<div>
          		<h3>三科统计结果（共${gk3Condition?size}种）</h3>
          		<table class="table table-striped table-hover no-margin">
                      <thead>
                          <tr>
                              <th>序号</th>
                              <th>科目</th>
                              <th>选课人数</th>
                              <th>操作</th>
                          </tr>
                      </thead>
                        <tbody>
                          <#if gk3Condition?? && (gk3Condition?size>0)>
                          <#list gk3Condition as gkc>
                              <tr>
                                  <td>${gkc_index+1}</td>
                                  <td>
                                      <span>${gkc.subNames[0]!}</span>、
                                      <span>${gkc.subNames[1]!}</span>、
                                      <span>${gkc.subNames[2]!}</span>
                                  </td>
                                  <td> 
                                  	<#assign subIds=courseName2Id[gkc.subNames[0]] +','+ courseName2Id[gkc.subNames[1]] +','+ courseName2Id[gkc.subNames[2]]>
                                  	<a class="table-btn show-details-btn" href="javascript:void(0);" id="avlaclas-${gkc_index+1}" onclick="doQuery('${subIds!}','1');">${gkc.sumNum!}</a>
                                  	<#assign subIds = ''>
                                  </td>
                                  <td>
                                  	<#assign subIds=courseName2Id[gkc.subNames[0]] +','+ courseName2Id[gkc.subNames[1]] +','+ courseName2Id[gkc.subNames[2]]>
                                  	<a class="btn <#if ! gkc.limitSubject>btn-blue</#if> btn-sm" id="aclas-${gkc_index+1}" href="javascript:void(0);" <#if ! gkc.limitSubject> onclick="doLimitSubject('${subIds!}',${gkc_index+1});" </#if>>限选</a>
                                  	<#assign subIds = ''>
                                  </td>
                              </tr>
                          </#list>
                          </#if>
                        </tbody>
                  </table>
          	</div>
          </div>
    </div>
</div>
<script  type="text/javascript">
  function doQuery(courseId,type){
  	detailsPagevalue = courseId+"-"+type+"-";
	var url =  '${request.contextPath}/gkelective/${arrangeId}/doQueryStudent/list/page?detailsPagevalue='+detailsPagevalue+$("#searchClassId").val();
    $(".itemShowDivId").load(url);
  }
  
  function doLimitSubject(courseIds,index){
  	showConfirmMsg('限选操作后会把已选人员组合清退，科目组合将纳入限选组合，学生将不能选择，请确认！','提示',function(ii){
			layer.close(ii);
			$.ajax({
				url:'${request.contextPath}/gkelective/${arrangeId}/limitSubject/saveAll',
				data: {'subjectIds':courseIds},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
			 		if(jsonO.success){
			 			//$('#aclas-'+index).removeAttr('onclick');
			 			//$('#aclas-'+index).removeClass("btn-blue");
			 			//$('#avlaclas-'+index).text("0");
			 			itemShowList(3);
			 		}else{
			 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
			
		});
		
	
	
		
  }
</script>