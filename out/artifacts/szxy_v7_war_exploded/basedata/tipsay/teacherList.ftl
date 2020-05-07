<#if teacherList?exists && teacherList?size gt 0>
<#list teacherList as teacher>
<li class="move_teacher" data-teacher="${teacher.id!}" data-tname="${teacher.teacherName!}">
	<table class="table text-center">
		<tbody>
			<tr>
				<td width="33%">${teacher.teacherName!}</td>
				<td width="33%">${teacher.teacherCode!}</td>
				<!--onclick="addTipsay('${teacher.id!}','${teacher.teacherName!}')"-->
				<td width="33%">
				<a class="js-replace" href="javascript:" data-tname="${teacher.teacherName!}" data-tId="${teacher.id!}"  data-type="1">代课</a>
				<a class="js-replace" href="javascript:" data-tname="${teacher.teacherName!}" data-tId="${teacher.id!}"  data-type="2" >管课</a>
				</td>
			</tr>
		</tbody>
	</table>
	<form class="form-horizontal box-graybg" id="form_${teacher.id!}">
		<input type="hidden" name="newTeacherId" value="${teacher.id!}"/>
		<input type="hidden" name="courseScheduleId" id="scheduleId_${teacher.id!}" value=""/>
		
		<input type="hidden" name="type" id="type_${teacher.id!}" value="">
		<div class="form-group form-group-sm">
			<label class="col-sm-3 control-label no-padding font-14">时间:</label>
			<div class="col-sm-9 time_class choose_class">2018-10-15 星期四</div>
		</div>
		<div class="form-group form-group-sm">
			<label class="col-sm-3 control-label no-padding font-14">班级:</label>
			<div class="col-sm-9 clazz_class choose_class">高二(2)班</div>
		</div>
		<div class="form-group form-group-sm">
			<label class="col-sm-3 control-label no-padding font-14">内容:</label>
			<div class="col-sm-9 course_class choose_class">第2节化学由王五教师代课</div>
		</div>
		<div class="form-group form-group-sm">
			<label class="col-sm-3 control-label no-padding-right font-14">备注:</label>
			<!--中文占2个字节 这边就500减半-->
			<div class="col-sm-9"><input type="text"  maxlength="250" class="form-control" name="remark" id="remark_${teacher.id!}"></div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"></label>
			<div class="col-sm-9">
				<a class="btn btn-blue btn-sm" onClick="saveTipsay('${teacher.id!}')">确定</a>
				<a class="btn btn-white btn-sm" onClick="cancelForm('${teacher.id!}')">取消</a>
			</div>
		</div>
	</form>
</li>
</#list>
<script>
	//全局变量
	var chooseTeacherId="";
	var chooseweek="";
		$(function(){
			$(".move_teacher").on('mouseover',function(){
				var teacherId=$(this).attr("data-teacher");
				var tname=$(this).attr("data-tname");
				var acadyear=$('#acadyear').val();
				var semester=$('#semester').val();
				var week= $("#week").val();
				
				chooseTeacherId=teacherId;
				chooseweek=week;
				
				//延迟一秒
				setTimeout(function(){
					if(teacherId==chooseTeacherId && chooseweek==week){
						//获取教师课表
						searchSchedules(acadyear,semester,week,teacherId,"rightSchedule",false);
						$("#rightTeacherName").html(tname);
					}else{
						
					}
				},1000);
			})
		})
		$(".js-replace").click(function(){
			var tName=$(this).attr("data-tname");
			var tId=$(this).attr("data-tId");
			var tipType=$(this).attr("data-type");
			if(!addTipsay(tId,tName,tipType)){
				return;
			}
			$(this).parents("table").addClass("active").siblings("form").show().parent("li").siblings().children("table").removeClass("active").siblings("form").hide();
		
		});
		function addTipsay(teacherId,tName,tipType){
			//$(".replace-list").find(".box-graybg").hide();
			$(".replace-list").find(".choose_class").html("");
			$(".replace-list").find("input[name='remark']").val("");
			$(".replace-list").find("input[name='scheduleId']").val("");
			//选中的值
			var scheduleId="";
			var timeStr="";
			var clazzName="";
			var subjectName="";
			var opotName="代课";
			if("2"==tipType){
				opotName="管课";
			}
			$("#centerSchedule").find(".item").each(function(){
				if($(this).hasClass("active")){
					//存在
					timeStr=$(this).attr("data-name");
					scheduleId=$(this).find(".courseScheduleId").val();
					clazzName=$(this).find(".clazzId_item").html();
					subjectName=$(this).find(".subjectId_item").html();
					return;
				}
			})
			if(!scheduleId || scheduleId==""){
				layer.msg("请先选择需要代课课程", {
							offset: 't',
							time: 2000
						});
				return false;
			}else{
				//$("#form_"+teacherId).show();
				$("#form_"+teacherId).find(".time_class").html(timeStr);
				$("#form_"+teacherId).find(".clazz_class").html(clazzName);
				var subjectStr=subjectName+"由"+tName+"教师"+opotName;
				$("#form_"+teacherId).find(".course_class").html(subjectStr);
				$("#remark_"+teacherId).val("");
				$("#scheduleId_"+teacherId).val(scheduleId);
				$("#type_"+teacherId).val(tipType);
			}
			return true;
		}
		
		function cancelForm(teacherId){
			$("#form_"+teacherId).hide();
			$("#form_"+teacherId).find(".choose_class").html("");
			$("#remark_"+teacherId).val("");
			$("#scheduleId_"+teacherId).val("");
		}
		var isSave=false;
		function saveTipsay(teacherId){
			//form提交
			if(isSave){
				return ;
			}
			isSave=true;
			var options = {
				url : "${request.contextPath}/basedata/tipsay/saveTipsay",
				dataType : 'json',
				success : function(data){
		 			var jsonO = data;
			 		if(!jsonO.success){
			 			isSave=false;
			 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 		}else{
			 			isSave=false;
			 			layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
						//重新加载左边
						var acadyear=$('#acadyear').val();
						var semester=$('#semester').val();
						var week= $("#week").val();
						var selectTeacherId=$("#selectTeacherId").val();
						//alert(selectTeacherId);
						//加载中间
						searchSchedules(acadyear,semester,week,selectTeacherId,"centerSchedule",true);
						//关闭
						cancelForm(teacherId);
						$("#rightSchedule").find(".item").html("");
						$("#rightTeacherName").html("");
					}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#form_"+teacherId).ajaxSubmit(options);
		}
</script>
<#else>
	<script>
		$(function(){
			$("#teacherTab").find(".inputMessName").html("${inputMess?default('请输入教师姓名查询')}");
			$("#teacherTab").find(".replace-list").html("");
			$("#teacherTab").find(".no-data-container").show();
			$("#teacherTab").find(".replace-list").hide();
		})
		
	</script>
</#if>