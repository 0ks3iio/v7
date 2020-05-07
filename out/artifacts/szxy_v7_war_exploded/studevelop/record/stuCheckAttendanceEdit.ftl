<title>考勤总计详情</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${stuCheckAttendance.id!}">
<input type="hidden" name="studentId" id="studentId" value="${stuCheckAttendance.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${stuCheckAttendance.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${stuCheckAttendance.semester!}">
	<div class="layer-body">

				<div class="filter clearfix">
					<table class="table table-bordered table-striped table-hover no-margin">
		 <tr>
	     	<th width="20%">&nbsp;</th>
	     	<td>
	     		本学期实际授课&nbsp;<input name="studyDate" id="studyDate" type="text"  style="width:40px;" size="4" maxLength="5"  min="0" max="200" nullable="false" vtype="number" <#if (stuCheckAttendance.id)?exists>value="${(stuCheckAttendance.studyDate)?string("0.#")!}"<#else>value=""</#if> />&nbsp;天，
	     		事假&nbsp;<input name="businessVacation" id="businessVacation" type="text"  style="width:40px;" size="4" maxLength="5"  min="0" max="999" nullable="false" vtype="number" <#if (stuCheckAttendance.id)?exists>value="${(stuCheckAttendance.businessVacation)?string("0.#")?default("0")}"<#else>value="0"</#if>  />&nbsp;天，
	     		病假&nbsp;<input name="illnessVacation" id="illnessVacation" type="text"  style="width:40px;" size="4" maxLength="5"  min="0" max="999" nullable="false" vtype="number" <#if (stuCheckAttendance.id)?exists>value="${(stuCheckAttendance.illnessVacation)?string("0.#")?default("0")}"<#else>value="0"</#if>  />&nbsp;天，
	     		旷课&nbsp;<input name="wasteVacation" id="wasteVacation" type="text"  style="width:40px;" size="4" maxLength="4"   min="0" max="1000" nullable="false" vtype="int" <#if (stuCheckAttendance.id)?exists>value="${stuCheckAttendance.wasteVacation!}"<#else>value="0"</#if>  />&nbsp;节，
	        	迟到&nbsp;<input name="late" id="late" type="text" style="width:40px;" size="5" maxLength="4"  nullable="false" vtype="int" <#if (stuCheckAttendance.id)?exists>value="${stuCheckAttendance.late!}"<#else>value="0"</#if>  />&nbsp;节，
				早退&nbsp;<input name="leaveEarly" id="leaveEarly" type="text" style="width:40px;" size="4" maxLength="4" nullable="false" vtype="int" <#if (stuCheckAttendance.id)?exists>value="${stuCheckAttendance.leaveEarly!}"<#else>value="0"</#if>  />&nbsp;节。
	        </td>
	      </tr>
		  <tr>
			  <th>&nbsp;</th>
			  <td>
			  	  <input name="registerBegin" id="registerBegin" type="hidden" value="${stuCheckAttendance.registerBegin!}" />
			  	  <input name="studyBegin" id="studyBegin" type="hidden" value="${stuCheckAttendance.studyBegin!}" />
				  下学期定于&nbsp;<input name="regMon" id="regMon" type="text" class="input-txt100 input-readonly" readonly style="width:30px;" size="3" maxLength="2" value="" />&nbsp;月
				  <input name="regDay" id="regDay" type="text" class="input-txt100 input-readonly" readonly style="width:30px;" size="3" maxLength="2" value="" />&nbsp;日注册报到，
				  <input name="studyMon" id="studyMon" type="text" class="input-txt100 input-readonly" readonly style="width:30px;" size="3" maxLength="2" value="" />&nbsp;月
				  <input name="studyDay" id="studyDay" type="text" class="input-txt100 input-readonly" readonly style="width:30px;" size="3" maxLength="2" value="" />&nbsp;日正式上课。
			  </td>
	      </tr>
	      <tr>
		      <th>备注：</th>
			  <td width="" height="30"><textarea class="area300"  style="width:435px;" id="remark" name="remark" maxLength="200" msgName="备注" rows="2" cols="64">${stuCheckAttendance.remark!}</textarea></td>
	      </tr>
					</table>
               </div>
	</div>
    <div class="layer-footer">
        <a class="btn btn-lightblue" id="arrange-commit">保存</a>
        <a class="btn btn-grey" id="arrange-close">取消</a>
    </div>
		</form>
	</div>

<script>
	$(function(){
			var reg = "${stuCheckAttendance.registerBegin!}";
			var study = "${stuCheckAttendance.studyBegin!}";
			if(reg.indexOf('-') != -1){
				var regs = reg.split('-');
				if(regs.length == 2){
					$("#regMon").val(regs[0]);
					$("#regDay").val(regs[1]);
				}
			}
			if(study.indexOf('-') != -1){
				var studys = study.split('-');
				if(studys.length == 2){
					$("#studyMon").val(regs[0]);
					$("#studyDay").val(regs[1]);
				}
			}
	
		var isSubmit = false;
		
		$("#arrange-close").on("click", function(){
    		changeStuId();    
 		});
 		
 		$("#arrange-commit").on("click", function(){
 			if(isSubmit){
				return;
			} 	
			debugger;
			isSubmit = true;
			var check = checkValue('#subForm');
			if(!check){
				isSubmit = false;
				return;
			} 
			
			var options = {
			url : "${request.contextPath}/studevelop/checkAttendance/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit = false;
		 			return;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					isSubmit = true;
                    checkList();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
			
 		});
	});
</script>