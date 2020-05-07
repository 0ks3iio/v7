<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${stuDevelopHonorRecord.id!}">
<input type="hidden" name="unitId" id="unitId" value="${stuDevelopHonorRecord.unitId!}">
<input type="hidden" name="studentId" id="studentId" value="${stuDevelopHonorRecord.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${stuDevelopHonorRecord.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${stuDevelopHonorRecord.semester!}">
<input type="hidden" name="honorType" id="honorType" value="${stuDevelopHonorRecord.honorType!}">
	<table class="table table-bordered table-striped table-hover no-margin">
		<tr class="first">
        	<th colspan="4" style="text-align:center;"><#if (stuDevelopHonorRecord.honorType!)=="1">星级人物<#else>七彩阳光卡</#if></th>
        </tr>
          <tr>
          	 <th style="width:100px">
          	 	  <span style="color:red">*</span>获得日期：
          	 </th>	
          	 <td>
          	 	<div class="filter-item">
					<div class="filter-content">
						<div class="input-group">
							<input class="form-control date-picker" vtype="data" style="width: 120px;height:28px;" type="text" nullable="false" name="giveDate" id="giveDate" placeholder="获得日期" value="${(stuDevelopHonorRecord.giveDate?string('yyyy-MM-dd'))!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					 </div>
				</div>
          	 </td>
          </tr>
		  <tr>
		  	 <th>
		  	    <span style="color:red">*</span>荣誉名称：
		  	 </th>
		  	 <td>
		  	 	<#if (stuDevelopHonorRecord.honorType!)=="1">
		  	 		${mcodeSetting.getMcodeRadio('DM-XJRW',((stuDevelopHonorRecord.honorLevel)?default(''))?string,'honorLevel')}
		  	 	<#else>
		  	 		<label><input type="radio" id="honorLevel_01" name="honorLevel" value="01" title="聪慧博学" <#if (stuDevelopHonorRecord.honorLevel!) == "01">checked</#if> class="ace" /><span class='lbl'>聪慧博学</span></label>
		  	 		<label><input type="radio" id="honorLevel_02" name="honorLevel" value="02" title="领袖气质" <#if (stuDevelopHonorRecord.honorLevel!) == "02">checked</#if> class="ace" /><span class='lbl'>领袖气质</span></label>
		  	 		<label><input type="radio" id="honorLevel_03" name="honorLevel" value="03" title="健康体魄" <#if (stuDevelopHonorRecord.honorLevel!) == "03">checked</#if> class="ace" /><span class='lbl'>健康体魄</span></label>
		  	 		<label><input type="radio" id="honorLevel_04" name="honorLevel" value="04" title="阳光心态" <#if (stuDevelopHonorRecord.honorLevel!) == "04">checked</#if> class="ace" /><span class='lbl'>阳光心态</span></label>
		  	 		<label><input type="radio" id="honorLevel_05" name="honorLevel" value="05" title="品格健全" <#if (stuDevelopHonorRecord.honorLevel!) == "05">checked</#if> class="ace" /><span class='lbl'>品格健全</span></label>
		  	 		<label><input type="radio" id="honorLevel_06" name="honorLevel" value="06" title="创新实践" <#if (stuDevelopHonorRecord.honorLevel!) == "06">checked</#if> class="ace" /><span class='lbl'>创新实践</span></label>
		  	 		<label><input type="radio" id="honorLevel_07" name="honorLevel" value="07" title="艺术素养" <#if (stuDevelopHonorRecord.honorLevel!) == "07">checked</#if> class="ace" /><span class='lbl'>艺术素养</span></label>
		  	 	</#if>
		  	 </td> 
          </tr>
          <tr>
            <th>
          	 	备注：
          	 </th>
          	 <td>
          		<textarea style="width:435px;" id="remark" name="remark" maxLength="100" rows="2" cols="64" >${stuDevelopHonorRecord.remark!}</textarea>
          	 </td>          
          </tr>
	</table>		
</form>
</div>	
	<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">保存</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
    </div>

<script>
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#myDiv",".date-picker",viewContent);
		//初始化多选控件
		initChosenMore("#myDiv");
		$('.date-picker').next().on("click", function(){
			$(this).prev().focus();
		});
	});
	
	$("#arrange-close").on("click", function(){
    	doLayerOk("#arrange-commit", {
    	redirect:function(){},
    	window:function(){layer.closeAll()}
    	});     
 	});
 	
 	var isSubmit=false;
 	$("#arrange-commit").on("click", function(){
 		if(isSubmit){
 			return;
 		}
 		isSubmit=true;
 		if(!checkValue('#subForm')){
 			isSubmit=false;
 			return;
 		}
 		
 		var honorLevel = $('input:radio[name="honorLevel"]:checked').val();
			if(honorLevel==""){
			layerTipMsg(false,"荣誉名称不能为空!","");
			isSubmit=false;
			return;
		}
        var stuId = $("#studentId").val();
		var options = {
			url : "${request.contextPath}/studevelop/honorRecord/updateSave",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
		 			return;
		 		}else{
		 			layer.closeAll();
					if(stuId==""){
						honorList();
					}else{
				  		changeStuId();
					}
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
		
 	});
</script>
    