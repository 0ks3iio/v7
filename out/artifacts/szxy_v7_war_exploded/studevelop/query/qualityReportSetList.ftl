<div class="box-body">
	<div class="row">
	<#if stuDevelopQualityReportSet?exists>
	    <#if stuDevelopQualityReportSet.section == 1>
	    <div class="col-sm-6">
			<div class="report-tpl clearfix">
				<img src="${request.contextPath}/studevelop/images/report-tpl-03.png" alt="" class="report-tpl-img">
				<h4 class="report-tpl-name">小学报告单1</h4>
				<p class="report-tpl-info">4页装订效果，分为封面、封底及内页，封面主要有主题、班级姓名、班主任等信息；内页信息包含文化素质、品德素质、心理素质等；封底包含奖惩、老师寄语等信息。</p>
				<#if stuDevelopQualityReportSet.template! == '1'>
				    <button class="btn btn-green">使用中</button>
				<#else>
				    <button class="btn btn-blue" onclick="chooseTemplate('1')">选择模板</button>
				</#if>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="report-tpl clearfix">
				<img src="${request.contextPath}/studevelop/images/report-tpl-01.png" alt="" class="report-tpl-img">
				<h4 class="report-tpl-name">小学报告单2</h4>
				<p class="report-tpl-info">两折页效果，左页包含班级、姓名、老师寄语等学生基本信息；右页包含学生各个科目成绩和评语信息。</p>
				<#if stuDevelopQualityReportSet.template! == '2'>
				    <button class="btn btn-green">使用中</button>
				<#else>
				    <button class="btn btn-blue" onclick="chooseTemplate('2')">选择模板</button>
				</#if>
			</div>
		</div>
	    <#elseif stuDevelopQualityReportSet.section == 2>
	    <div class="col-sm-6">
			<div class="report-tpl clearfix">
				<img src="${request.contextPath}/studevelop/images/report-tpl-04.png" alt="" class="report-tpl-img">
				<h4 class="report-tpl-name">初中报告单1</h4>
				<p class="report-tpl-info">4页装订效果，分为封面、封底及内页，封面主要有主题、班级姓名、班主任等信息；内页信息包含文化素质、品德素质、心理素质等；封底包含奖惩、老师寄语等信息。</p>
				<#if stuDevelopQualityReportSet.template! == '1'>
				    <button class="btn btn-green">使用中</button>
				<#else>
				    <button class="btn btn-blue" onclick="chooseTemplate('1')">选择模板</button>
				</#if>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="report-tpl clearfix">
				<img src="${request.contextPath}/studevelop/images/report-tpl-02.png" alt="" class="report-tpl-img">
				<h4 class="report-tpl-name">初中报告单2</h4>
			    <p class="report-tpl-info">单页效果，左上为操行评语、右上为学科成绩及学生基本信息，下方为获奖情况及附告通知。</p>
				<#if stuDevelopQualityReportSet.template! == '2'>
				    <button class="btn btn-green">使用中</button>
				<#else>
				    <button class="btn btn-blue" onclick="chooseTemplate('2')">选择模板</button>
				</#if>
			</div>
		</div>
	    </#if>
	</#if>	
	</div>
</div>

<script>
var isSubmit=false;
function chooseTemplate(template){
   var acadyear = $('#queryAcadyear').val();
   var semester = $('#querySemester').val();
   var section = $('#section').val();  
   showConfirmMsg('确认更换模板？','提示',function(){
        var ii = layer.load();
        $.ajax({
		url:'${request.contextPath}/studevelop/stuQualityReportSet/templateSave',
		data: {'acadyear':acadyear,'semester':semester,'section':section,'template':template},  
		type:'post',  
		success:function(data) {
		    var jsonO = JSON.parse(data);
		    if(jsonO.success){
               layer.closeAll();
			   layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
			   searchList();
		 	}else{
               layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
               isSubmit=false;
		 	   return;
			}
			layer.close(ii);
	    }
       }); 
   });
}
</script>