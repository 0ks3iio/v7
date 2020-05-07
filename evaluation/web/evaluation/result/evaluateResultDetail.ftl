			<a href="javascript:" onclick="goback()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
			<div class="box box-default">
				<div class="box-body">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">姓名：</span>
							<div class="filter-content">
								<p>${studentName!}</p>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">学号：</span>
							<div class="filter-content">
								<p>${studentCode!}</p>
							</div>
						</div>
						<#if className?default("")!="">
						<div class="filter-item">
							<span class="filter-name">班级：</span>
							<div class="filter-content">
								<p>${className!}</p>
							</div>
						</div>
						<#elseif teachOrclassName?exists && teachOrclassName?default("")!="">
						<div class="filter-item">
							<span class="filter-name">班级：</span>
							<div class="filter-content">
								<p>${teachOrclassName!}</p>
							</div>
						</div>
						</#if>
						<div class="filter-item">
							<span class="filter-name">${showName!}：</span>
							<div class="filter-content">
								<p>${teacherName!}</p>
							</div>
						</div>
	                	<#if courseList?exists && courseList?size gt 0>
							<div class="filter-item">
								<span class="filter-name">课程：</span>
								<div class="filter-content">
									 <select name="subjectId" id="subjectId" class="form-control" onchange="doSearch()" style="width:120px">
					                		<#list courseList as item>
						                        <option value="${item.id!},${item.unitName?default("")},${item.courseTypeName?default("")}" <#if item.id==dto.subjectId?default("") && item.unitName==tId?default("") && item.courseTypeName==cId?default("")>selected="selected"</#if>>${item.subjectName!}</option>
					                		</#list>
					                </select>
								</div>
							</div>
                		</#if>
					</div>

					<div class="table-container">
						<div class="table-container-body">
							<table class="table table-bordered table-striped">
								
								<tbody>
									<#if zbmcodeList?exists && zbmcodeList?size gt 0>
									<#assign haveData ="">
									<#list zbmcodeList as macode>
									<#assign  itemList=itemMap[macode.thisId] />
										<#if itemList?exists && itemList?size gt 0>
											<#assign haveData="true">
											<#if macode.thisId!="12">
												<tr>
													<th width="10%">序号</th>
													<th width="50%">名称</th>
													<th width="15%">类型</th>
													<th>选项</th>
												</tr>
												<#list itemList as item>
												<tr>
													<td>${item_index+1}</td>
													<td style="word-break:break-all;">${item.itemName!}</td>
													<#if item.showType?default("")=="O"><td>单选</td><#else><td class="color-red">多选</td></#if><#--item.showType?default("")=="M"-->
													<td style="word-break:break-all;"> 
														<#if item.optionList?exists && item.optionList?size gt 0>
															<#list item.optionList as option>
																<#if option.haveSelected?default(false)>
																	<p class="color-green">${option.optionName!}<i class="fa fa-check"></i></p>
																<#else>
																	<p>${option.optionName!}</p>
																</#if>
															</#list>
														</#if>
													</td>
												</tr>
												</#list>
												<tr>
													<td colspan="4">&nbsp;</td>
												</tr>
											<#else>
												<tr>
													<th width="10%">序号</th>
													<th width="50%">名称</th>
													<th colspan="2">问答</th>
												</tr>
												<#list itemList as item>
												<tr>
													<td>${item_index+1}</td>
													<td width="50%" style="word-break:break-all;">${item.itemName!}</td>
													<td colspan="2" style="word-break:break-all;">
														${item.result!}
													</td>
												</tr>
												</#list>
											</#if>
										</#if>
									</#list>
									<#if haveData=="">
										<tr><td align="center">当前无评教</td></tr>
									</#if>
									</#if>
								</tbody>
							</table>
						</div>
					</div>
					<div id="searchType">
						<input type="hidden" name="projectId" id="projectId" value="${dto.projectId!}">
						<input type="hidden" name="evaluateType" id="evaluateType" value="${dto.evaluateType!}">
						<input type="hidden" name="acadyear" id="acadyear" value="${dto.acadyear!}">
						<input type="hidden" name="semester" id="semester" value="${dto.semester!}">
						<input type="hidden" name="status" id="status" value="${status!}">
						<input type="hidden" name="studentId" id="studentId" value="${studentId!}">
						<input type="hidden" name="teacherId" id="teacherId" value="${teacherId!}">
						<input type="hidden" name="teachOrclassId" id="teachOrclassId" value="${teachOrclassId!}">
					</div>
						<input type="hidden"  id="isStu" value="<#if isStu>true<#else>false</#if>">
				</div>
			</div>
					
<script>
	function doSearch(){
		var subjectId=$("#subjectId").val();
		if(subjectId==undefined){
			subjectId="";
		}
		var url="${request.contextPath}/evaluate/stu/edit?gradeId=${gradeId!}&classId=${classId!}&selectObj=${selectObj!}&selectType=${selectType!}&"+searchUrlValue("#searchType")+"&subjectId="+subjectId;
		if(isStu=="true"){
			$("#itemShowDivId").load(url);
		}else{
			$(".model-div").load(url);
		}
	}
	function goback(){
		var url="";
		var isStu=$("#isStu").val();
		if(isStu=="true"){
			url="${request.contextPath}/evaluate/stu/index/page";
			$(".model-div").load(url);
		}else{
			var projectId = $('#projectId').val();
			var acadyear = $('#acadyear').val();
			var semester = $('#semester').val();
			var url =  '${request.contextPath}/evaluate/project/showStuSubList/page?gradeId=${gradeId!}&classId=${classId!}&selectObj=${selectObj!}&selectType=${selectType!}&projectId='+projectId+'&acadyear='+acadyear+'&semester='+semester;
			$(".model-div").load(url);
		}
	}
</script>
