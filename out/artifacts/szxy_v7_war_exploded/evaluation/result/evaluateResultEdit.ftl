<form id="itemFormId">
<div class="main-content">
	
	<div class="main-content-inner">
		<div class="model-div">

			<a href="javascript:void(0);" onclick="goback()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
			<div class="box box-default">
				<div class="box-body">
					<#-- 
					<div class="explain">
						<h4>亲爱的同学：</h4>
						<p>每位同学应本着负责任的态度，独立、认真完成</p>
					</div> -->
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
						                        <#-- <option value="${item.id!}" <#if item.id==dto.subjectId?default("")>selected="selected"</#if>>${item.subjectName!}</option> -->
						                        <option value="${item.id!},${item.unitName?default("")},${item.courseTypeName?default("")}" <#if item.id==dto.subjectId?default("") && item.unitName==tId?default("") && item.courseTypeName==cId?default("")>selected="selected"</#if>>${item.subjectName!}</option>
					                		</#list>
					                </select>
								</div>
							</div>
                		</#if>
					</div>
					<#assign haveData ="">
					<div class="table-container">
						<div class="table-container-body">
							<table class="table table-bordered table-striped">
								
								<tbody>
									<#if zbmcodeList?exists && zbmcodeList?size gt 0>
									<#assign inNumber = 0/>
									<#assign trNumber = 1/>
									<#list zbmcodeList as macode>
									<#assign  itemList=itemMap[macode.thisId] />
										<#if itemList?exists && itemList?size gt 0>
											<#assign haveData="true">
											<#if macode.thisId!="12">
												<tr>
													<th width="10%">序号</th>
													<th width="50%">名称</th>
													<th width="15%">类型</th>
													<th >选项</th>
												</tr>
												<#list itemList as item>
												<tr>
													<td>${item_index+1}</td>
													<td style="word-break:break-all;">${item.itemName!}</td>
													<#if item.showType?default("")=="O"><td>单选</td><#else><td class="color-red">多选</td></#if><#--item.showType?default("")=="M"-->
													<td id="trId${trNumber}" style="word-break:break-all;"> 
														<#if item.optionList?exists && item.optionList?size gt 0>
															<input type="hidden" name="resultList[${inNumber!}].itemId" value="${item.id!}">
															<input type="hidden" name="resultList[${inNumber!}].itemName" value="${item.itemName!}">
															<input type="hidden" name="resultList[${inNumber!}].itemType" value="${item.itemType!}">
															<#list item.optionList as option>
																<p><label><input name="resultList[${inNumber!}].resultId" value="${option.id!}" <#if option.haveSelected?default(false)>checked="true"</#if> <#if item.showType?default("")=="O">type="radio"<#else>type="checkbox"</#if> class="wp checked${trNumber!}"><span class="lbl">${option.optionName!}</span></label></p>
															</#list>
															<#assign inNumber = inNumber+1/>
														</#if>
													</td>
												</tr>
												<#assign trNumber = trNumber+1/>
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
													<input type="hidden" name="resultList[${inNumber!}].itemId" value="${item.id!}">
													<input type="hidden" name="resultList[${inNumber!}].itemName" value="${item.itemName!}">
													<input type="hidden" name="resultList[${inNumber!}].itemType" value="${item.itemType!}">
													<td>${item_index+1}</td>
													<td width="50%" style="word-break:break-all;">${item.itemName!}</td>
													<td colspan="2">
														<textarea name="resultList[${inNumber!}].result" maxlength="1000" id="result${inNumber}" rows="5" nullable="false" class="form-control">${item.result!}</textarea>
													</td>
												</tr>
												<#assign inNumber = inNumber+1/>
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
					<#--<#if status?default("")=="1"></#if>-->
					<#if haveData=="true">
						<div align="center">
							<button class="btn btn-long btn-blue" onclick="saveItemList('1','${trNum!}');return false;">保存</button>
							<button class="btn btn-long btn-blue" onclick="saveItemList('2','${trNum!}');return false;">全部提交</button>
						</div>
					</#if>
					
					<div id="searchType">
						<input type="hidden" name="teachOrclassId"  value="${teachOrclassId!}">
						<input type="hidden" name="teacherId" id="teacherId" value="${teacherId!}">
						<input type="hidden" name="projectId" id="projectId" value="${dto.projectId!}">
						<input type="hidden" name="evaluateType" id="evaluateType" value="${dto.evaluateType!}">
						<input type="hidden" name="acadyear" id="acadyear" value="${dto.acadyear!}">
						<input type="hidden" name="semester" id="semester" value="${dto.semester!}">
						<input type="hidden" name="status" id="status" value="${status!}">
					</div>
				</div>
			</div>
		</div><!-- /.model-div -->
	</div>
</div><!-- /.main-content -->
</form>	
<script>
	function doSearch(){
		var subjectId=$("#subjectId").val();
		if(subjectId==undefined){
			subjectId="";
		}
		var url="${request.contextPath}/evaluate/stu/edit?"+searchUrlValue("#searchType")+"&subjectId="+subjectId;
		$("#itemShowDivId").load(url);
	}
	function goback(){
		var url="${request.contextPath}/evaluate/stu/list";
		$("#itemShowDivId").load(url);
	}
	function saveItemList(state,trNum){
		if(state=="2"){
			showConfirmMsg('提交后只能查看不可修改，确认提交?','提示',function(){
				layer.closeAll();
				saveItem(state,trNum,"提交");
			});
		}else{
			saveItem(state,trNum,"保存");
		}
	}
	var isSubmit=false;
	function saveItem(state,trNum,type){
		if(isSubmit){
			return;
		}
		var isChecked=false;
		for(var i=1;i<=trNum;i++){
			//$(".checked"+i)
			if($("input[class='wp checked"+i+"']:checked").length==0){
				layer.tips('不能为空!', $("#trId"+i), {
					tipsMore: true,
					tips: 4
				});
				isChecked=true;
			}
		}
		isSubmit = true;
		var check = checkValue('#itemFormId');
		if(!check || isChecked){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/evaluate/stu/saveItem",
			dataType : 'json',
			data:{'state':state},
			success : function(data){
	 			var jsonO = data;
	 			isSubmit = false;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,type+"失败",jsonO.msg);
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,type+"成功",jsonO.msg);
					if(state=="2"){
					  	goback();
					}
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#itemFormId").ajaxSubmit(options);
	}
</script>
