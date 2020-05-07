<div class="form-horizontal" role="form">
	<div class="form-group">
		<label class="col-sm-2 control-title no-padding-right"><span class="form-title">场地资源</span></label>
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding">年级：</label>
		<div class="col-sm-10">${grade.gradeName!}</div>
	</div>
	<form id="base_form">
	<input type="hidden" name="gradeId" value="${grade.id!}">
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">可用场地数：</label>
		<#-- <input type="number" <#if !isNew>disabled</#if> name="placeNum" maxlength="4" nullable="false" value="${placeNum!0}" class="form-control" placeholder="请输入场地数">  -->
		<div class="col-sm-4">
			<div class="form-number  form-number-sm period-number" data-step="1">
	    		<input <#if !isNew>disabled</#if> type="text" name="placeNum" id="" class="form-control courseWorkDay"  value="${placeNum!0}"/>
	    		<#if isNew>
	    		<button style="line-height:15px;" class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
		    	<button style="line-height:15px;" class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
	    		</#if>
	    	</div>
		</div>
		<div class="col-sm-4 control-tips"></div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-title no-padding-right"><span class="form-title">教师资源</span></label>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">教师数量：</label>
		<div class="col-sm-5">
			<div class="table-container-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>科目</th>
							<#if xgkCourseList?? && xgkCourseList?size gt 0>
							<#list xgkCourseList as c>
							<th >${c.subjectName!}<input type="hidden" name="baseList[${c_index}].keyId" value="${c.id!}"></th>
							</#list>
							</#if>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>数量</td>
							<#if baseList?exists && baseList?size gt 0>
							<#list baseList as dto>
							<td>
								<div class="form-number  form-number-sm period-number" data-step="1">
									<input type="text"  <#if !isNew>disabled</#if> maxlength="4" nullable="false" class="form-control" name="baseList[${dto_index}].num" value="${dto.teacherNum!}">
									<#if isNew>
						    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
							    	<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
									</#if>
						    	</div>
							</td>
							</#list>
							</#if>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</form>
	<div class="form-group">
		<label class="col-sm-2">
		
		</label>
		
		<div class="col-sm-10">
			<#if isNew>
			    <a href="javascript:void(0)" class="btn btn-blue" onclick="doBaseSave();">上报</a>
			<#else>
			    <a href="javascript:void(0)" class="btn btn-blue" onclick="newFresh();">重新上报</a>
			</#if>
		</div>
	</div>
</div>

<script>
function newFresh(){
	var url = "${request.contextPath}/newgkelective/edu/baseItem/upload/page?gradeId=${grade.id!}&isNew=true&time="+new Date().getTime();
	
	$("#aa").load(url);
}

var isSubmit=false;
function doBaseSave(){
	
	if(!checkNum())
		return;
	layer.confirm('确定上报吗？', function(index){
		if(isSubmit || !checkNum())
			return;
		isSubmit = true;
	
		var params = $("#base_form").serialize();
		var url = "${request.contextPath}/newgkelective/edu/baseItem/upload/save";
		var ii = layer.load();
		$.ajax({
			url:url,
			type:"POST",
			data:params,
			dataType:"JSON",
			success:function(data){
				isSubmit = false;
				layer.close(ii);
				if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
		 			//layer.closeAll();
		 			layer.msg("上报成功", {
						offset: 't',
						time: 2000
					});
					
					var url = "${request.contextPath}/newgkelective/edu/baseItem/upload/page?gradeId=${grade.id!}&useMaster=1";
					$("#aa").load(url);
				}
			}
		});
	});
	
	
}

function checkNum(){
	var f=true;
	$("#base_form input:visible").each(function(i,obj){
		var num = $(obj).val();
		if (!num || !/^\d+$/.test(num)) {
			layer.tips("请输入正整数！",$(obj), {
				tipsMore: true,
				tips: 3
			});
			f=false;
			return false;
		}
		
	});
	return f;
}

//数字增加以及减少
$("#aa").off("click").on('click','.form-number > button',function(e){
	e.preventDefault();
	
	var $num = $(this).siblings('.form-control');
	var val = $num.val();
	if (!val ) val = 0;
	var num = parseInt(val);
	var step = $num.parent('.form-number').attr('data-step');
	if (step === undefined) {
		step = 1;
	} else{
		step = parseInt(step);
	}
	if ($(this).hasClass('form-number-add')) {
		num += step;
	} else{
		num -= step;
		if (num <= 0) num = 0;
	}
	
	$num.val(num);
	
});
</script>