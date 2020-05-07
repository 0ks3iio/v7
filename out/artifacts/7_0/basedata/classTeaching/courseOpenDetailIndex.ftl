<form id="myFormOpenSave">
<div class="<#if state?default('1')=='1'>col-sm-6<#else>col-sm-7</#if>">
	<h3>必修课</h3>
	<table class="table table-bordered table-striped table-hover" id="subTable">
		<thead>
			<tr>
				<th>序<br>号</th>
				<th style="width:90px">课程</th>
				<th>所属学科</th>
				<#if state?default('1') == '2'>
					<th style="width:20px">课时</th>
					<th rowspan="2" style="width:40px">是否教<br>学班教学</th>
					<th rowspan="2" style="width:60px">是否考勤</th>
					<th>单双周</th>
					<th style="width:30px">学分</th>
				<#else>
					<th>是否教学班教学</th>
					<th style="width:40px">学分<br>(Enter保存)</th>
				</#if>
				<th >操作</th>
			</tr>
		</thead>
		<tbody>
				<#if requiredCourseList?exists && (requiredCourseList?size>0)>
					<#list requiredCourseList as item>
							<tr>
								<input type="hidden" value="${item.id}" name="requiredCourseList[${item_index}].id">
								<td>${item_index + 1}</td>
								<td>${item.subjectName}</td>
								<td>${item.belongToSubjectName!}</td>
								<#if state?default('1') == '2'>
									<td><input style="width:40px" class="other" maxlength="3"  name="requiredCourseList[${item_index}].courseHour"  value="${item.courseHour!}"></td>
									<td>
										<label class="pos-rel confirm">
											<input type="radio" class="wp" <#if item.isTeaCls?default(0) == 1>checked="true"</#if> <#if item.subjectType?default('1')=='3'>disabled</#if>  name="requiredCourseList[${item_index}].isTeaCls" value="1">
										    <span class="lbl"> 是</span>
										</label>
										<label class="pos-rel">
										   <input type="radio" class="wp" <#if item.isTeaCls?default(0) == 0>checked="true"</#if> name="requiredCourseList[${item_index}].isTeaCls" value="0">
										   <span class="lbl"> 否</span>
										</label>
									</td>
									<td>
										<label class="pos-rel">
											<input type="radio" class="wp" <#if item.punchCard?default(0) == 1>checked="true"</#if>  name="requiredCourseList[${item_index}].punchCard" value="1">
										  	<span class="lbl"> 是</span>
										</label>
										<label class="pos-rel">
										   <input type="radio" class="wp" <#if item.punchCard?default(0) == 0>checked="true"</#if> name="requiredCourseList[${item_index}].punchCard" value="0">
										   <span class="lbl"> 否</span>
										</label>
									</td>
									<td>
										<select class="form-control" name="requiredCourseList[${item_index}].weekType">
											<option value="3" <#if item.weekType?default(3)==3>selected="selected"</#if>>正常</option>
											<option value="1" <#if item.weekType?default(3)==1>selected="selected"</#if>>单周</option>
											<option value="2" <#if item.weekType?default(3)==2>selected="selected"</#if>>双周</option>
										</select>
									</td>
									<td>
										<div class="form-number form-number-sm credit-number" data-step="1" >
											<input type="text" name="requiredCourseList[${item_index}].credit" class="form-control credit" maxlength="2" value="${item.credit?default(0)}" onblur="checkCredit()" />
											<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
											<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
										</div>
									</td>
								<#else>	
									<td>
										<label class="pos-rel confirm">
											<input type="radio" class="wp" <#if item.isTeaCls?default(0) == 1>checked="true"</#if> <#if item.subjectType?default('1')=='3'>disabled</#if>  name="requiredCourseList[${item_index}].isTeaCls" value="1" onChange="changeTeaCls('${item.id!}',1,this);">
										    <span class="lbl"> 是</span>
										</label>
										<label class="pos-rel">
										   <input type="radio" class="wp" <#if item.isTeaCls?default(0) == 0>checked="true"</#if> name="requiredCourseList[${item_index}].isTeaCls" value="0" onChange="changeTeaCls('${item.id!}',0,this);">
										   <span class="lbl"> 否</span>
										</label>
									</td>
									<td>
										<#--<div class="form-number form-number-sm credit-number" data-step="1" >-->
											<input type="text" name="requiredCourseList[${item_index}].credit" id="credit${item_index}" class="form-control credit" maxlength="2" value="${item.credit?default(0)}" onkeydown="changeCredit('${item.id}','${item_index}',this)"/>
											<#--<div class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></div>
											<div class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></div>-->
										<#--</div>-->
									</td>
								</#if>
								<td>
									<a href="javascript:" class="table-btn js-del" onclick="deleteClassTeaching('${item.id!}')">删除</a>
								</td>
							</tr>
					</#list>
				</#if>
				
			</tbody>
		</table>
	</div>
	<div class="<#if state?default('1')=='1'>col-sm-6<#else>col-sm-5</#if>">
		<h3>选修课</h3>
		<table class="table table-bordered table-striped table-hover" id="xSubTable">
			<thead>
				<tr>
					<th>序号</th>
					<th style="width:120px">课程</th>
					<th>所属学科</th>
					<#if state?default('1') == '2'>
						<th style="width:40px">课时</th>
					</#if>
					<th style="width:90px">学分</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if optionaldCourseList?exists && (optionaldCourseList?size>0)>
					<#list optionaldCourseList as item>
						<tr>
							<input type="hidden" value="${item.id}" name="optionaldCourseList[${item_index}].id">
							<td>${item_index + 1}</td>
							<td>${item.subjectName}</td>
							<td>${item.belongToSubjectName!}</td>
							<#if state?default('1') == '2'>
								<td><input style="width:40px" <#if item.isDeleted == 1>disabled="disabled"</#if> class="other" maxlength="3" name="optionaldCourseList[${item_index}].courseHour" value="${item.courseHour!}"></td>
								<td>
									<div class="form-number form-number-sm xCredit-number" data-step="1" >
										<input type="text" name="optionaldCourseList[${item_index}].credit" class="form-control xCredit" maxlength="2" value="${item.credit?default(0)}" onblur="checkCredit()" />
										<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
										<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
									</div>
								</td>
							<#else>
							<td>
								<input type="text" name="optionaldCourseList[${item_index}].credit" id="xCredit${item_index}" class="form-control xCredit" maxlength="2" value="${item.credit?default(0)}" onkeydown="changeXcredit('${item.id}','${item_index}',this)"/>
							</td>
							</#if>
							<td>
								<a href="javascript:" class="table-btn js-del" onclick="deleteClassTeaching('${item.id!}')">删除</a>
							</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
</form>

<script>

$(".confirm").on("click",function(e){
	e.preventDefault();
	var $obj = $(this);
	if($obj.find("input").prop("checked")){
		return;
	}
	if($obj.find("input").prop("disabled")){
		return;
	}
	<#if state?default('1')=='1'>
	layer.confirm('确定要更改为以教学班形式教学吗？确定后，该年级下所有行政班此课程所对应的课程表数据都会被删除。', function(index){
		layer.closeAll();
		$obj.find("input").change();
	},function(index){
		return;
	})
	<#else>
		$obj.find("input").prop("checked",true);
	</#if>
})

function deleteClassTeaching(classTeachingId){
	layer.confirm('确定要删除该课程吗？删除后，该<#if state?default('1')=='1'>年级下所有</#if>行政班此课程所对应的课程表数据都会被删除。', function(index){
		layer.closeAll();
		$.ajax({
			url:'${request.contextPath}/basedata/<#if state?default('1')=='1'>grade<#else>class</#if>/courseopen/delete', 
			data:{"<#if state?default('1')=='1'>gradeTeachingId<#else>classTeachingId</#if>":classTeachingId},
		    dataType:'json',
		    type:'post',
		    success:function(data) {
		    	layer.closeAll();
		    	if(data.success){
		    		layer.msg("删除成功！", {
						offset: 't',
						time: 2000
					});
					<#if state?default('1')=='1'>
					change("1");
					<#else>
					change2("1");
					</#if>
		        }else{
		         	layerTipMsg(data.success,"失败",data.msg);
		        }
		        
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});	
	},function(index){
		return;
	})
}

function changeTeaCls(gradeTeachingId,isTeaCls,obj){
	$.ajax({
		url:"${request.contextPath}/basedata/courseopen/changeGradeIsTeaCls",
		data:{"gradeTeachingId":gradeTeachingId, "isTeaCls":isTeaCls},
		dataType: "json",
		success: function(data){
			if(data.success){
				$(obj).prop("checked",true);
				layer.msg(data.msg, {offset: 't',time: 2000});
	 		}else{
	 			layerTipMsg(data.success,"操作失败",data.msg);
			}
		}
	});
}

function checkCredit() {
	var isSubmit=false;
	var f=false;
	if(isSubmit){
		return;
	}
	$(".credit").each(function () {
		var credit=$(this).val();
		if(credit!=""){
			if (!/^\d+$/.test(credit)){
				f=true;
				layer.tips("格式不正确(最多2位整数)！",$(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
	});
}

function changeCredit(gradeTeachingId,num,obj) {
	var theEvent = window.event || gradeTeachingId;
	var code = theEvent.keyCode || theEvent.which;
	if (code==13) {  //回车键的键值为13
		// 验证
		var isSubmit=false;
		var f=false;
		if(isSubmit){
			return;
		}
		$(".credit").each(function () {
			var credit=$(this).val();
			if(credit!=""){
				if (!/^\d+$/.test(credit)){
					f=true;
					layer.tips("格式不正确(最多2位整数)！",$(this), {
						tipsMore: true,
						tips: 3
					});
					return false;
				}
			}
		});
		if(f){
			isSubmit=false;
			return;
		}

		var credit = $("#credit"+num).val();
		// 取消回车聚焦
		obj.blur();
		$.ajax({
			url:"${request.contextPath}/basedata/courseopen/changeGradeCredit",
			data:{"gradeTeachingId":gradeTeachingId,"credit":credit},
			dataType:"json",
			success:function (data) {
				$(obj).unbind("blur");
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
				}else{
					layerTipMsg(data.success,"操作失败",data.msg);
				}
			}
		})
	}
}

function changeXcredit(gradeTeachingId,num,obj) {
	var theEvent = window.event || gradeTeachingId;
	var code = theEvent.keyCode || theEvent.which;
	if (code==13) {  //回车键的键值为13
		// 验证
		var isSubmit=false;
		var f=false;
		if(isSubmit){
			return;
		}
		$(".xCredit").each(function () {
			var credit=$(this).val();
			if(credit!=""){
				if (!/^\d+$/.test(credit)){
					f=true;
					layer.tips("格式不正确(最多2位整数)！",$(this), {
						tipsMore: true,
						tips: 3
					});
					return false;
				}
			}
		});
		if(f){
			isSubmit=false;
			return;
		}

		var credit = $("#xCredit"+num).val();
		// 取消回车聚焦
		obj.blur();
		$.ajax({
			url:"${request.contextPath}/basedata/courseopen/changeGradeXcredit",
			data:{"gradeTeachingId":gradeTeachingId,"credit":credit},
			dataType:"json",
			success:function (data) {
				$(obj).unbind("blur");
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
				}else{
					layerTipMsg(data.success,"操作失败",data.msg);
				}
			}
		})
	}
}

var opoNum=0;
$(function () {
	//数字增加以及减少
	$("#subTable").on('click','.form-number > button',function(e){
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
			if (num >= 99) num = 99;
		} else{
			num -= step;
			if (num <= 0) num = 0;
		}
		opoNum++;
		$num.val(num);
	});
})


//var opoNum=0;
$(function () {
	//数字增加以及减少
	$("#xSubTable").on('click','.form-number > button',function(e){
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
			if (num >= 99) num = 99;
		} else{
			num -= step;
			if (num <= 0) num = 0;
		}
		opoNum++;
		$num.val(num);
	});
})


</script>