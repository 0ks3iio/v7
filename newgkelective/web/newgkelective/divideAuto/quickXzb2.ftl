<div class="layer-content">
	<form id="myForm2">
		<input type="hidden" name="subjectIds" value="${subjectIds!}">
		<input type="hidden" name="subjectType" value="${subjectType!}">
		<p><span class="mr30"><#if subjectType?default('')=='2'>2科</#if>组合：${group!}</span><span class="mr30">总人数：${allSize}</span><span class="mr30">未安排人数：${arrangeSize}</span></p>
		<div style="height:500px;overflow-y:auto;">
			<table class="table table-striped table-bordered table-hover no-margin">
				<thead>
					<tr>
						<th>班级名称</th>
						<th>可组班的3科组合</th>
						<th>未安排人数</th>
						<th>组班人数</th>
						<th>班级人数</th>
						<th>3科组合学生分发依据</th>
					</tr>
				</thead>
				<tbody>
				<#if groupDtoList?exists && groupDtoList?size gt 0>
					<#list groupDtoList as dto>
						<#if dto_index==0>
							<tr>
								<td rowspan="${groupDtoList?size}"><input type="text" name="className" id="className" value="${groupName!}" style="width:80px;"></td>
								<td>${dto.conditionName!}</td>
								<td>
								<input type="hidden" class="leftNumber" value="${dto.leftNumber}">
								${dto.leftNumber}/${dto.allNumber}
								</td>
								<td>
								<input type="hidden" name="dtoList[${dto_index}].subjectIds" value="${dto.subjectIds!}">
								<input type="text" class="form-control chooseNum" name="dtoList[${dto_index}].chooseNum" value="${dto.leftNumber}" maxLength="4" onblur="calStuNums()">
								</td>
								<td rowspan="${groupDtoList?size}" id="countNum">0</td>
								<td rowspan="${groupDtoList?size}">
									<label>
										<input type="radio" class="wp" name="openBasis" value="2"  checked="">
										<span class="lbl"> 按该选科与语数英成绩之和排名</span>
									</label>
									<label>
										<input type="radio" class="wp" name="openBasis"  value="1">
										<span class="lbl"> 按该选科成绩排名</span>
									</label>
									<label>
										<input type="radio" class="wp" name="openBasis" value="0">
										<span class="lbl"> 无依据</span>
									</label>
								</td>
							</tr>
						<#else>
							<tr>
								<td>${dto.conditionName!}</td>
								<td><input type="hidden" class="leftNumber" value="${dto.leftNumber}">${dto.leftNumber}/${dto.allNumber}</td>
								<td>
									<input type="hidden" name="dtoList[${dto_index}].subjectIds" value="${dto.subjectIds!}">
									<input type="text" class="form-control chooseNum" name="dtoList[${dto_index}].chooseNum" value="${dto.leftNumber}" maxLength="4" onblur="calStuNums()">
								</td>
							</tr>
						</#if>
						
					</#list>
				<#else>
				<tr>
					<td colspan="6" class="text-center">
						暂无需要安排学生数据
					</td>
				</tr>
				</#if>	
					
				</tbody>
			</table>
		</div>
	</form>
</div>
<div class="layer-footer" style="vertical-align: middle;border-top: 1px solid #eee;">
	<#if groupDtoList?exists && groupDtoList?size gt 0>
	<button class="btn btn-lightblue" id="quick2-commit">确定</button>
	</#if>
	<button class="btn btn-grey" id="quick2-close">取消</button>
</div>
<script>
	<#if groupDtoList?exists && groupDtoList?size gt 0>
	var isSubmit=false;
	$(function(){
		calStuNums();
		$("#quick2-close").on("click", function(){
			doLayerOk("#quick2-commit", {
				redirect:function(){},
				window:function(){layer.closeAll()}
			});
		 });
		$("#quick2-commit").off('click').on("click",function(){
			if(isSubmit){
				return false;
			}
			isSubmit=true;
			$(this).addClass("disabled");
			if(!checkSubmit()){
				$(this).removeClass("disabled");
		 		isSubmit=false;
		 		return false;
			}
			// 提交数据
			var options = {
				url : '${request.contextPath}/newgkelective/${divideId!}/divideClass/saveQuickOpen',
				dataType : 'json',
				success : function(data){
			 		if(data.success){
			 			layer.closeAll();
			 			layer.msg("保存成功！", {
							offset: 't',
							time: 2000
						});
						refArrange();
			 		}
			 		else{
			 			layerTipMsg(data.success,"失败",data.msg);
			 			$("#quick2-commit").removeClass("disabled");
			 			isSubmit=false;
					}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){} 
			};
			$("#myForm2").ajaxSubmit(options);
		})

	})
	
	function checkSubmit(){
		var className=$.trim($("#className").val());
		if(className==""){
			layer.tips('不能为空', $("#className"), {
				tipsMore: true,
				tips:3				
			});
			return false;
		}
		return calStuNums();
	}
	function calStuNums(){
		var f=true;
		var ii=0;
		$("#myForm2").find("tbody").find("tr").each(function(){
			var chooseNum=$.trim($(this).find(".chooseNum").val());
			if(chooseNum==""){
				layer.tips('请输入整数', $(this).find(".chooseNum"), {
					tipsMore: true,
					tips:3				
				});
				f=false;
				return false;
			}
			if (!/^\d+$/.test(chooseNum)) {
				layer.tips('请输入整数', $(this).find(".chooseNum"), {
					tipsMore: true,
					tips:3				
				});
				f=false;
				return false;
			}
			var chooseNumInt=parseInt(chooseNum);
			var aln=parseInt($(this).find(".leftNumber").val());
			if(aln<chooseNumInt){
				layer.tips('不能大于未安排人数', $(this).find(".chooseNum"), {
					tipsMore: true,
					tips:3				
				});
				f=false;
				return false;
			}
			ii=ii+chooseNumInt;
		})
		if(f){
			$("#countNum").html(ii);
		}
		
		return f;
	}
	<#else>
		$(function(){
			$("#quick2-close").on("click", function(){
				layer.closeAll();
			 });
		 })
	</#if>
	
	
</script>