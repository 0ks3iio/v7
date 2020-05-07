<#if teacherList?exists && teacherList?size gt 0>
<table>
<#list teacherList as teacher>
	<tr data-teacher="${teacher.id!}">
        <td class="name" style="width:100px;">${teacher.teacherName!}</td>
		<td>${teacher.teacherCode!}</td>
        <td class="opt">
       		 <a class="js-replace" href="javascript:" data-tname="${teacher.teacherName!}" data-tId="${teacher.id!}"  data-type="1">代课</a>		
        </td>
        <td class="opt"><a class="js-replace" href="javascript:" data-tname="${teacher.teacherName!}" data-tId="${teacher.id!}"  data-type="2" >管课</a></td>
	</tr>
	<tr class="inner mytr" id="tr_${teacher.id!}" style="display:none">
		<td colspan="4" class="py-5">
			<form id="form_${teacher.id!}">
				<input type="hidden" name="acadyear"id="acadyear_${teacher.id!}"  value=""/>
				<input type="hidden" name="semester"id="semester_${teacher.id!}"  value=""/>
				<input type="hidden" name="newTeacherId" value="${teacher.id!}"/>
				<input type="hidden" name="courseScheduleIds" id="scheduleId_${teacher.id!}" value=""/>
				<input type="hidden" name="oldTeacherId" id="oldTeacherId_${teacher.id!}"  value=""/>
				<input type="hidden" name="type" id="type_${teacher.id!}" value="">
				<input type="hidden" name="applyType" id="applyType_${teacher.id!}" value="${teacher.id!}"/>
				<p id="typeName_${teacher.id!}">类型：</p>
				<p>备注：<input type="text"  maxlength="250" class="form-control" name="remark" id="remark_${teacher.id!}"></p>
				<p class="abtn">
					<a class="abtn-blue" onClick="saveTipsay('${teacher.id!}')">确定</a>
					<a class="abtn-white" onClick="cancelForm('${teacher.id!}')">取消</a>
				</p>
			</form>
		</td>
	</tr>
	</#list>
</table>

<script>
	var chooseTeacherId="";
	$(function(){
		//隔行颜色变化
		$('.tab-wrap .tab-item').each(function(){
	        $(this).find('table tr:not(".mytr"):even').addClass('even');
	    });
	    
	    //滑动
	    $('.tab-item table tr').hover(function(){
	    	if($(this).hasClass("mytr")){
	    		//移动到代课列表  
	    	}else{
	    		$(this).addClass('current').siblings('tr').removeClass('current');
				var teacherId=$(this).attr("data-teacher");
				var teacherName=$(this).find(".name").html();
				chooseTeacherId=teacherId;
				
				//延迟一秒
				setTimeout(function(){
					if(teacherId==chooseTeacherId){
						//获取教师课表
						$("#moveTeacherName").html(teacherName);
						loadTeacherByWeek(teacherId,false,"teacherTable1");
					}else{
						
					}
				},1000);
				
				
	    	}
			
		},function(){
			if($(this).hasClass("mytr")){
	    		//移动到代课列表
	    	}else{
				$(this).removeClass('current');
			}
		});
		
		$(".js-replace").click(function(){
			var tName=$(this).attr("data-tname");
			var tId=$(this).attr("data-tId");
			var tipType=$(this).attr("data-type");
			if(!addTipsay(tId,tName,tipType)){
				return;
			}
			//将之前的数据隐藏
			$(".mytr").hide();
			$("#tr_"+tId).show();
		});
	})
	
	
	function addTipsay(teacherId,tName,tipType){
		$(".mytr").find("input[name='remark']").val("");
		$(".mytr").find("input[name='courseScheduleIds']").val("");
		//选中的值
		var opotName="代课";
		if("2"==tipType){
			opotName="管课";
		}
		var scheduleIds=getChooseSchedule();
		
		if(!scheduleIds || scheduleIds==""){
			autoTips('请先选择需要'+opotName+'课程');
			return false;
		}else{
			scheduleIds=scheduleIds.substring(1);
			$("#scheduleId_"+teacherId).val(scheduleIds);
			$("#typeName_"+teacherId).html("类型："+opotName);
			$("#type_"+teacherId).val(tipType);
			var oldTeacherId=$("#chooseTeacherId").val();
			$("#oldTeacherId_"+teacherId).val(oldTeacherId);
		}
		return true;
	}
	
	function getChooseSchedule(){
		var radioValue=takeRadioValue();
		var scheduleIds="";
		if(radioValue=="1"){
			$("#allTable").find(".item").each(function(){
				if($(this).find(".courseScheduleId").length>0){
					var scheduleId=$(this).find(".courseScheduleId").val();
					if($(this).hasClass("item-sel")){
						scheduleIds=scheduleIds+","+scheduleId;
					}
				}
				
			});
		}else{
			var lll=$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").length;
			if(lll>0){
				$(".tableContainer").find("table").find(".ui-checkbox-current").not(".mycheckAll").each(function(){
					scheduleIds=scheduleIds+","+$(this).find(".chk").val();
				})
			}
		}
		return scheduleIds;
	}
	
	function cancelForm(teacherId){
		$("#tr_"+teacherId).hide();
	}
	var isSave=false;
	function saveTipsay(teacherId){
		//form提交
		if(isSave){
			return ;
		}
		var scheduleIds=getChooseSchedule();
		
		if(!scheduleIds || scheduleIds==""){
			autoTips('请先选择需要代课课程');
			isSave=false;
			return;
		}else{
			scheduleIds=scheduleIds.substring(1);
			$("#scheduleId_"+teacherId).val(scheduleIds);
		}
		$("#acadyear_"+teacherId).val($("#acadyear").val());
		$("#semester_"+teacherId).val($("#semester").val());
		$("#applyType_"+teacherId).val($("#applyTypeValue").val());
		var oldTeacherId=$("#oldTeacherId_"+teacherId).val();
		isSave=true;
		var options = {
			url : "${request.contextPath}/basedata/tipsay/saveTipsay1",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			isSave=false;
		 			autoTips(jsonO.msg)
		 		}else{
		 			isSave=false;
		 			autoTips('操作成功');
					//重新左边数据
					var radioValue=takeRadioValue();
					if(radioValue=="1"){
						loadTeacherByWeek(oldTeacherId,true,"allTable");
					}else{
						loadTeacherByTime();
					}
					//关闭
					cancelForm(teacherId);
					$("#teacherTable1").find(".item").html("");
					$("#moveTeacherName").html("");
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
<div class="tab-item rightTeacherList">
	<div class="t-center mt-80">
		<img src="${request.contextPath}/static/images/public/nodata.png" alt="">
		<p class="c-999">暂无数据</p>
	</div>
</div>
</#if>	
