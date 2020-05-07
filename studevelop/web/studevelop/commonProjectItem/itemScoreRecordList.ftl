<#import "/studevelop/commonProjectItem/itemResultMacro.ftl" as resultMacro />
<form id="subForm">
<div class="box box-default">
		<div class="box-header">
			<h3 class="box-title">${studentName!}</h3>
		</div>
		<div class="box-body">
			<div class="table-container">
				<div class="explain">
					<p>说明：为了素质报告单更好的展示，建议平时+期末+态度习惯维护不要超过10个汉字</p>
				</div>
				<div class="table-container-header text-right">
					<a class="btn btn-blue" onclick="saveScore();">保存</a>
					<#--<a class="btn btn-blue" onclick="doImport();">导入</a>-->
				</div>
				<div class="table-container-body" id="myDiv">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th class="text-center" width="10%" style="word-break:break-all;">学科</th>
								<th class="text-center" width="10%" style="word-break:break-all;">学科类别</th>
								<#if templateItemList?exists && templateItemList?size gt 0>
								    <#list templateItemList as item>
								         <td width="100px;" style="word-break:break-all;background-color:#ececec;font-weight: bold;">${item.itemName!}
								         </td>
								    </#list>
								</#if>
							</tr>
						</thead>
						<tbody>
						    <#assign c = 0 >
							<#if subjectList?exists && subjectList?size gt 0>
								<#list subjectList as sub>
										<#assign ind = 0 />
										<#if sub.cateGoryList?exists && sub.cateGoryList?size gt 0 >
                                            <#assign cateGoryList = sub.cateGoryList />
											<#assign cateGorySize = cateGoryList?size />
											<tr>
												<td style="word-break: break-all;word-wrap: break-word;" rowspan="${cateGoryList?size}" >${sub.name!}</td>
												<td style="word-break: break-all;word-wrap: break-word;">${cateGoryList[ind].categoryName!}</td>

												<#if templateItemList?exists && templateItemList?size gt 0>
													<#list templateItemList as item>
                                                        <#if ind == 0 && item.objectType == '12'>
                                                            <#assign firstR = true />
															<#assign key = item.id +"_"+sub.id />
                                                        <#else >
                                                            <#assign firstR = false />
															<#assign key = item.id +"_"+sub.id+"_"+cateGoryList[ind].id />
                                                        </#if>
														<@resultMacro.tdInput ind=c  project=item firstRow=firstR size="${cateGorySize}" result=templateResultMap[key] />
														<#assign c = c+1 />
													</#list>
												</#if>
											</tr>
											<#assign ind = ind +1 />
											<#if cateGorySize gt 1  >
												<#list ind..(cateGorySize-1) as cateGory >
												<tr>
													<td style="word-break: break-all;word-wrap: break-word;">${cateGoryList[ind].categoryName!}</td>
													<#if templateItemList?exists && templateItemList?size gt 0>
														<#list templateItemList as item>
															<#-- 针对 学科类别才会显示 td -->
															<#if item.objectType == '11'>
																<@resultMacro.tdInput ind=c  project=item result=templateResultMap[item.id +"_"+sub.id+"_"+cateGoryList[ind].id] />
																<#assign c = c+1 />
															</#if>
														</#list>
													</#if>
												</tr>
													<#assign ind = ind +1 />
												</#list>
											</#if>
										<#else>
											<tr>
                                                <td style="word-break: break-all;word-wrap: break-word;" >${sub.name!}</td>
												<td></td>
												<#if templateItemList?exists && templateItemList?size gt 0>
													<#list templateItemList as item>
														<@resultMacro.tdInput ind=c  project=item result=templateResultMap[item.id +"_"+sub.id] />
														<#assign c = c+1 />
													</#list>
												</#if>
                                            </tr>
										</#if>

								</#list>
							</#if>
						</tbody>
					</table>
					<div class="text-right" style="margin-top:10px;">
			             <a class="btn btn-blue" onclick="saveScore('');">保存</a>
	                </div>
				</div>
			</div>
		</div>
	</div>
</form>
<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<script>
var isSubmit=false;
function saveScore(){
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var classId=$("#classId").val();
    var isAdmin=$("#isAdmin").val();
    var code = $("#code").find("li[class = 'active']").find("a").attr("val");
	var options = {
		url : "${request.contextPath}/stuDevelop/proItemResult/save?acadyear=${acadyear!}&semester=${semester!}&studentId=${studentId!}&isAdmin="+isAdmin+"&classId="+classId+"&code="+code,
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		return;
		 	}else{
		 		layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
                doSearch("1");
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
	};
	$("#subForm").ajaxSubmit(options);
}
</script>