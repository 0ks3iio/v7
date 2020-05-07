<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<div class="successed-analysis-container" id="successDiv" style="display:none;">
			<div class="successed-analysis-data">
				<span class="successed-analysis-img">
					<img src="../../static/images/icons/icon-successed-big.png" alt="">
				</span>
				<div class="successed-analysis-body">
					<p class="successed-analysis-tit">计算成功！</p>
					<p class="successed-analysis-text">请前往综合素质模块查看</p>
				</div>
			</div>
		</div>
		<div class="filter filter-f16 data-checking" id="statHead">
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control" notnull="false">
						<#if (gradeList?exists && gradeList?size>0)>
							<#list gradeList as grade>
					     	<option  value = "${grade.id!}" >${grade.gradeName!}</option>
					     	</#list>
					     <#else>
					     	<option  value = "" >--无数据--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" notnull="false">
						<#if (acadyearList?exists && acadyearList?size>0)>
							<#list acadyearList as item>
					     	<option  value = "${item!}" <#if semester.acadyear?default('')==item>selected="selected"</#if>>${item!}</option>
					     	</#list>
					     <#else>
					     	<option  value = "" >--无数据--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" notnull="false">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<a href="javascript:" class="btn btn-blue" id="statQualityBtn" onclick="statQuality()">计算</a>
			</div>
		</div>
		<ul class="data-checking-list data-list-bordered" id="messul">
		</ul>
	</div>
</div>
<script>
	var isSubmit=false;
	var sunhuan=true;
	var statName="";
	function removeResult1(){
		var gradeId=$("#gradeId").val();
    	var acadyear=$("#acadyear").val();
    	var semester=$("#semester").val();
    	removeResult(gradeId,acadyear,semester);
	}
	function statQuality(){
		var check = checkValue('#statHead');
   		if(!check){
	        $("#statQuality").removeClass("disabled");
	        isSubmit=false;
	        return;
    	}
    	isSubmit=true;
    	if($("#statQuality").hasClass("disabled")){
    		return;
    	}
		var gradeName=$("#gradeId option:selected").text();
    	var acadyearName=$("#acadyear option:selected").text();
    	var semesterName=$("#semester option:selected").text();
    	var mess=gradeName+"在"+acadyearName+"学年"+semesterName+"的综合素质准备计算...";
    	statName=gradeName+"在"+acadyearName+"学年"+semesterName+"的综合素质";
    	$("#messul").text("");
    	makeMess("",mess);
    	statScore2();
	}
	
	function removeResult(gradeId,acadyear,semester){
		var b=false;//防止多次点击确定
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm("是否确定重新统计，一旦点击确认，将删除已有统计结果",options,function(){
			window.clearTimeout("statScore2()");
			if(b){
				return;
			}
			b=true;
			$.ajax({
				url:'${request.contextPath}/comprehensive/qualityScoreStat/removeStat',
					data:{"gradeId":gradeId,"acadyear":acadyear,"semester":semester},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			//清除之前
			 			$("#messul").append("");
			 			makeMess("success","删除结果成功！请重新点击计算。");
			 			$("#statQualityBtn").removeClass("disabled");
		 				isSubmit=false;
	       				return;
			 		}
			 		else{
			 			layer.closeAll();
			 			$("#messul").append("");
			 			makeMess("error",jsonO.msg);
			 			makeMess("success","建议重新刷新后，再次操作。");
			 			$("#statQualityBtn").removeClass("disabled");
		 				isSubmit=false;
		 				return;
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){});
	}
	function statScore2(){
		var gradeId=$("#gradeId").val();
    	var acadyear=$("#acadyear").val();
    	var semester=$("#semester").val();
    	statScore3(gradeId,acadyear,semester);
	}

	function statScore3(gradeId,acadyear,semester){
		var oldgradeId=$("#gradeId").val();
    	var oldacadyear=$("#acadyear").val();
    	var oldsemester=$("#semester").val();
    	if(oldgradeId!=gradeId || oldacadyear!=acadyear || oldsemester!=semester){
    		return;
    	}
		$.ajax({
		  	url : "${request.contextPath}/comprehensive/qualityScoreStat/scoreStat",
		  	data:{"gradeId":gradeId,"acadyear":acadyear,"semester":semester},
			dataType : 'json',
			success : function(data){
				var obj=data;
				if(obj.type=="result"){
					makeMess("success",obj.mess);
					removeResult(gradeId,acadyear,semester);
				}else if(obj.type=="success"){
					makeMess("success",obj.mess);
	 				showSuccess();
	 			}else if(obj.type=="error"){
	 				makeMess("error",obj.mess);
	 				$("#statQualityBtn").removeClass("disabled");
		 			isSubmit=false;
	 			}else{
	 				makeMess("",obj.mess);
	 				//循环访问结果
	 				window.setTimeout("statScore2()",800);
	 			}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	 
	
function makeMess(type,mess){
	var date = new Date();
	var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var time1=+date.getFullYear() + seperator1 + month + seperator1 + strDate;
    var hours=date.getHours();
    if(hours>=1 && hours<=9){
    	hours="0"+hours;
    }
    var minutes=date.getMinutes();
    if(minutes>=0 && minutes<=9){
    	minutes="0"+minutes;
    }
    var seconds=date.getSeconds();
    if(seconds>=0 && seconds<=9){
    	seconds="0"+seconds;
    }
    var time2=hours + seperator2 + minutes + seperator2 + seconds;
    var $html='<li><span class="col-xs-7" title="'+mess+'"><i class="data-loading-s-blue"></i>';
    if(type=="error"){
    	 $html='<li><span class="col-xs-7 color-red" title="'+mess+'">';
    }
    if(mess.length>35){
    	mess=mess.substring(0,35);
    	mess=mess+"...";
    }
	var $html=$html+mess+'</span>'
	+'<span class="col-xs-4"><em>'+time1+'</em><em> '+time2+'</em></span>';
	$("#messul").append($html);
}
function showSuccess(){
	layer.open({
		type: 1,
		shade: 0.5,
		title: '计算成功',
		time: 5000,
		btn: ['确定'],
		btn1: function(index, layero){
   		 	$("#messul").text("");
   		 	makeMess("",statName+"统计成功！");
   		 	layer.close(index);
 		 },
		area: '460px',
		content: $('#successDiv')
	});
}

</script>