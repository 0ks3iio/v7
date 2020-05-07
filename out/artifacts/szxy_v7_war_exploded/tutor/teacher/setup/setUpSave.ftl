<div class="layer-content">
		<div class="form-horizontal">
		    <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">轮次名称：</label>
				<div class="col-sm-9">
				    <input type="text" class="form-control" name="roundName" id ="roundName" value="${tutorRound.roundName?default('')}" <#if isSee?default(false)> disabled="true" </#if>>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">开始时间：</label>
				<div class="col-sm-9">
					<div class="input-group">
						<input type="text" class="form-control datetimepicker" name="beginTime" id="beginT" value="<#if tutorRound.beginTime?exists>${tutorRound.beginTime?string('yyyy-MM-dd HH:mm')} </#if>" <#if isSee?default(false)> disabled="true" </#if>>
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">截止时间：</label>
				<div class="col-sm-9">
					<div class="input-group">
						<input type="text" class="form-control datetimepicker" name="endTime"  id="endT" value="<#if tutorRound.endTime?exists>${tutorRound.endTime?string('yyyy-MM-dd HH:mm')} </#if> " <#if isSee?default(false)> disabled="true" </#if>>
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">年级：</label>
				<div class="col-sm-9">
				    <#if lGrades?exists && lGrades?size gt 0>
				       <#list lGrades as grade>
				          <#if trgMap1?exists && trgMap1?size gt 0>
				             <label><input  type="radio" name="radio" class="wp"   onclick="clickCheckbox('${grade.id!}',this)" <#if isSee?default(false)> disabled="true" </#if> <#if trgMap1[grade.id]??>checked = "true" value='${grade.id!}' <#else> value='' </#if>><span class="lbl">${grade.gradeName!}</span></label>
				          <#elseif grade_index == lGrades?size-1>  
				             <label><input  type="radio" name="radio" class="wp" value=''  onclick="clickCheckbox('${grade.id!}',this)" <#if isSee?default(false)> disabled="true" </#if> ><span class="lbl showErrorGrade">${grade.gradeName!}</span></label>  
				          <#else>  
				             <label><input  type="radio" name="radio" class="wp" value=''  onclick="clickCheckbox('${grade.id!}',this)" <#if isSee?default(false)> disabled="true" </#if> ><span class="lbl">${grade.gradeName!}</span></label>
				          </#if>
				       </#list>
				    </#if>
				</div>
			</div>
		</div>
	</div>
	
  <script>
  
    $(document).ready(function(){
      $("#roundName").blur(function(){
           var rounName = $("#roundName").val();
           if(rounName==''){
              layerError("#roundName","轮次名称不能为空");
              return isOk=false;
           }
      });
    })
    function datetimepicker(){
        var radio=$('input:radio[name="radio"]:checked').val();
	    var date = getNowFormatDate();
	    var beginT = $("#beginT").val();
	    var rounName = $("#roundName").val();
	    if(rounName==''){
              layerError("#roundName","轮次名称不能为空");
              return isOk=false;
        }
        if(rounName && rounName !='' && getLength(rounName) >=101){
             layerError("#roundName","轮次名称长度过长,不能超过100！");
             return isOk=false;
        }
	    if(radio==null){
	         layerError(".showErrorGrade","年级不能为空");
	         return isOk=false;
	    };
	    if ( beginT <= date) {
	        layerError("#beginT","开始时间不能小于当前时间");
	        return isOk=false;
	    }
	    var endT = $("#endT").val();
	    if (endT <= date ) {
	        layerError("#endT","截止时间不能小于当前时间");
	        return isOk=false;
	    }
	    if(beginT!=null&&beginT!="" && endT!=null&&endT!="" && beginT >= endT){
	        layerError("#endT","开始时间不能大于截止时间");   
	        return isOk=false;
	    }
	    return isOk=true;
    }
    function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = "-";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if (hours >= 0 && hours <= 9) {
        hours = "0" + hours;
    }
    if(minutes >= 0 && minutes <= 9) {       
       minutes = "0"+ minutes;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + hours +":"+ minutes;
            
    return currentdate;
}
    
    // 时间
	$('.datetimepicker').datetimepicker({
		language: 'zh-CN',
    	format: 'yyyy-mm-dd hh:ii',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
	function clickCheckbox(gradeId,obj){
	       $(obj).val(gradeId)
	       $(obj).parent().siblings().find('input').val("");
	}
    
  </script>