<a href="javascript:goBack();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">总评成绩</h4>
	</div>
	<div class="box-body">
		<div class="filter" id="titleDiv">
			<div class="filter-item">
				<span class="filter-name">名称：</span>
				<div class="filter-content">
					<p>${searchAcadyear!}学年<#if (searchSemester!) == '1'>第一学期<#else>第二学期</#if>${gradeName!}年级总评成绩</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" style="width:100px" onChange="changeClass();">
						<option value="all">全部</option>
						<#if classList?exists && (classList?size > 0)>
							<#list classList as clazz>
								<option value="${clazz.id!}">${clazz.classNameDynamic!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button class="btn btn-white" onClick="scoresImport();">导入Excel</button>
				<button class="btn btn-blue js-setting">各科成绩设置</button>
				<button class="btn btn-blue" onClick="scoreComputation();">计算</button>
				<button class="btn btn-blue" onClick="scoreSave();">页面成绩保存</button>
				<button class="btn btn-blue" onClick="scoreRank();">计算总成绩和排名</button>
			</div>
			<div class="filter-item filter-item-right">
				<span class="tip color-red" type="width:200px">请先维护[各科成绩设置]（每项设置完毕请都点击确定保存信息），然后点击[计算]（系统通过各科折分管理中计算的实分，按照[各科成绩设置]中设置的比例计算出各科总评分），若手动修改成绩后请点击[页面成绩保存]保存成绩信息，点击[计算总成绩和排名]可以计算出成绩排名！</span>
			</div>
		</div>
		<div id="totalMarkScoreDiv">
			
		</div>
	</div>
</div>
<div class="layer layer-setting">
	<div class="layer-content">
		<ul class="nav nav-tabs" role="tablist" id="ulSub">
			<#if compreRelationships?exists && (compreRelationships?size>0)>
				<#list compreRelationships as item>
					<li role="presentation" <#if item_index == 0>class="active"</#if> value='${item.relationshipValue!}' id="${item.relationshipValue!}"><a href="javascript:void(0);" onClick="getSubId('${item.relationshipValue!}');" role="tab" data-toggle="tab">${item.relationshipName!}</a></li>
				</#list>
			</#if>
		</ul>
		<div class="tab-content" id="totalMarkScoreSetSubListDiv">
			
		</div>
	</div>
</div>
<script>
	$(function(){
		changeClass();
		$('.js-setting').on('click', function(e){
			e.preventDefault();
			getSubId($('#ulSub li:first').attr("value"));
			layer.open({
			    type: 1,
			    shade: 0.5,
			    title: '各科成绩设置',
			    btn: ['确定', '取消'],
			    yes: function(index){
					var subjectId = $("#ulSub li[class='active']").attr("id");
					if (subjectId == "00000000000000000000000000000000") {
						saveSubIds();
					} else {
						saveExamIdAndVal(subjectId);
					}
			    },
			    area: '750px',
			    content: $('.layer-setting')
			})
		})
	});
	
	var isSubmit=false; 
	
	function saveExamIdAndVal(subjectId) {
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var examIdAndVal= {};//存储每一行数据
        var tableData={};
		var totalVal = 0;
		var pattern = /^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
		var isTrue = false;
		$("#settingTable tbody tr").each(function(trindex,tritem){//遍历每一行
         	tableData[trindex]=new Array();
      		$(tritem).find("input").each(function(tdindex,tditem){
          		if ($(tditem).val() != "") {
          			if (tdindex == 1 && !pattern.test($(tditem).val())) {
          				isTrue = true;
          			}
          			tableData[trindex][tdindex]=$(tditem).val();//遍历每一个数据，并存入
          		} else {
          			tableData[trindex][tdindex]="0";
          		}
          		if (tdindex == 1 && $(tditem).val() != "") {
          			totalVal += parseFloat($(tditem).val());
          		}
      		});
          	examIdAndVal[trindex]=tableData[trindex];//将每一行的数据存入
  		});
  		
  		if (isTrue) {
  			layerTipMsg(false,"提示","请输入正确格式的数字！");
  			isSubmit=false;
			return;
  		}
  		var examSetup = JSON.stringify(examIdAndVal);
  		if (totalVal != 100) {
  			layerTipMsg(false,"提示","所有分数加起来需为100！");
  			isSubmit=false;
			return;
  		}
		$.ajax({  
			url:'${request.contextPath}/comprehensive/subjects/totalMarkScore/setExamIdAndVal/page',
			data: {'subjectId':subjectId,'comInfoId':'${comInfoId!}','examSetup':examSetup},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
		 			isSubmit=false;
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
		 			isSubmit=false;
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		}); 
	}
	
	function saveSubIds() {
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var subIds="";
		var objElem=$('input:checkbox[name=couList]');
		if(objElem.length>0){
			$('input:checkbox[name=couList]').each(function(i){
				if($(this).is(':checked')){
					subIds=subIds+","+$(this).val();
				}
			});
		}else{
			layerTipMsg(false,"提示","请先选择科目！");
			isSubmit=false;
			return;
		}
		if(subIds==""){
			layerTipMsg(false,"提示","请先选择科目！");
			isSubmit=false;
			return;
		}
		subIds=subIds.substring(1);
		$.ajax({
			url:'${request.contextPath}/comprehensive/subjects/totalMarkScore/setSubNameSave/page',
			data: {'subIds':subIds,'comInfoId':'${comInfoId!}'},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
		 			isSubmit=false;
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
		 			isSubmit=false;
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function getSubId(subjectId) {
		$("#ulSub li").each(function(){
			$(this).removeAttr("class");
		});
		$('#'+subjectId).attr("class","active");
		if (subjectId == "00000000000000000000000000000000") {
			var comInfoId = "${comInfoId!}";
			var str = "?comInfoId="+comInfoId;
	    	var url =  '${request.contextPath}/comprehensive/subjects/totalMarkScore/setSubNameList/page'+str;
			$("#totalMarkScoreSetSubListDiv").load(url);
		} else {
			var searchAcadyear = '${searchAcadyear!}';
			var searchSemester = '${searchSemester!}'; 
			var gradeId = '${gradeId!}';
			var comInfoId = "${comInfoId!}";
			var str = "?subjectId="+subjectId+"&searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&gradeId="+gradeId+"&comInfoId="+comInfoId;
	   	 	var url =  '${request.contextPath}/comprehensive/subjects/totalMarkScore/setSubjectList/page'+str;
			$("#totalMarkScoreSetSubListDiv").load(url);
		}
	}
	
	function scoreComputation() {
	showConfirmMsg('该操作将会删除原有的数据重新计算，是否确定？','提示',function(){
		var ii = layer.load();
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var classId = $("#classId").val();
		$.ajax({
			url:'${request.contextPath}/comprehensive/subjects/totalMarkScore/scoreComputation/page',
			data: {'gradeId':'${gradeId!}','comInfoId':'${comInfoId!}','searchAcadyear':'${searchAcadyear!}','searchSemester':'${searchSemester!}'},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
		 			isSubmit=false;
		 			changeClass();
		 		}else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	});
	}
	
	function scoreSave() {
	showConfirmMsg('该操作将会删除总成绩和排名并修改科目成绩，是否确定？','提示',function(){
		var ii = layer.load();
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var subScores= {};//存储每一行数据
        var tableData={};
        var pattern = /^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
		var isNumber = false;
		var row = 0;
		var low = 0;
		$("#example tbody tr").each(function(trindex,tritem){//遍历每一行
         	tableData[trindex]=new Array();
      		$(tritem).find("input").each(function(tdindex,tditem){
          		if (tdindex !=0 && tdindex != 1 && $(tditem).val() != "" && !pattern.test($(tditem).val())) {
          			isNumber = true;
          			row = trindex+1;
          			low = tdindex-1;
          		}
          		tableData[trindex][tdindex]=$(tditem).val();//遍历每一个数据，并存入
      		});
          	subScores[trindex]=tableData[trindex];//将每一行的数据存入
  		});
		
		if (isNumber) {
			layer.closeAll();
  			layerTipMsg(false,"提示","第"+row+"行第"+low+"个科目成绩有误，请输入正确格式的数字，最多3位整数两位小数！");
  			isSubmit=false;
			return;
  		}
		
		var examMarkScores = JSON.stringify(subScores);
		if (examMarkScores == "{}") {
			layer.closeAll();
			layerTipMsg(false,"提示","没有数据可维护！");
			isSubmit=false;
			return;
		}
		var classId = $("#classId").val();
		$.ajax({  
			url:'${request.contextPath}/comprehensive/subjects/totalMarkScore/scoreSave/page',
			data: {'comInfoId':'${comInfoId!}','searchAcadyear':'${searchAcadyear!}','searchSemester':'${searchSemester!}','examMarkScores':examMarkScores},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
		 			isSubmit=false;
		 			changeClass();
		 		}else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	});
	}
	
	function scoreRank() {
	var ii = layer.load();
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$.ajax({  
			url:'${request.contextPath}/comprehensive/subjects/totalMarkScore/scoreRank/page',
			data: {'gradeId':'${gradeId!}','comInfoId':'${comInfoId!}','searchAcadyear':'${searchAcadyear!}','searchSemester':'${searchSemester!}'},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
		 			isSubmit=false;
		 			changeClass();
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function changeClass() {
		var classId = $("#classId").val();
		var searchAcadyear = "${searchAcadyear!}";
		var searchSemester = "${searchSemester!}";
		var comInfoId = "${comInfoId!}";
		var gradeId = "${gradeId!}";
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&classId="+classId+"&comInfoId="+comInfoId+"&gradeId="+gradeId;
	    var url =  '${request.contextPath}/comprehensive/subjects/totalMarkScore/List/page'+str;
		$("#totalMarkScoreDiv").load(url);
	}
	
	function goBack() {
		var searchAcadyear = '${searchAcadyear!}';
		var searchSemester = '${searchSemester!}';
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester;
		var url =  '${request.contextPath}/comprehensive/subjects/grade/gradeIndex/page'+str;
		$("#showIndex").load(url);
	}
	
	function scoresImport() {
		$("#titleDiv").css('display','none');
		var searchAcadyear = "${searchAcadyear!}";
		var searchSemester = "${searchSemester!}";
		var comInfoId = "${comInfoId!}";
		var gradeId = "${gradeId!}";
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&comInfoId="+comInfoId+"&gradeId="+gradeId;
		var url="${request.contextPath}/comprehensive/scoresImport/main"+str;
		$("#totalMarkScoreDiv").load(url);
	}
</script>