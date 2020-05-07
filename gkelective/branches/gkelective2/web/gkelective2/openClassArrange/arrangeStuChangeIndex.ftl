<div id="resultListDiv">
	<div id="dd" class="tab-pane active" role="tabpanel">
		<div class="filter chosenClassHeaderClass">
		
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<div class="input-group input-group-search">
				        <div class="pos-rel pull-left">
				        	<select vtype="selectOne" id="teachClassId" data-placeholder="选择教学班" onChange="searchStuList()">
								<option value=""></option>
								<#if classDtoList?? && (classDtoList?size>0)>
									<#list classDtoList as item>
										<option value="${item.classId}#${item.type}">${item.name!}</option>
									</#list>
								</#if>
							</select>
				        </div>
					    
				    </div>
				</div>
			</div>
		</div>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学生：</span>
				<div class="filter-content chosenClassHeaderClass">
					<select vtype="selectOne" id="stuId" data-placeholder="选择学生" onchange="doChangeStu()">
						<#if stuList?? && (stuList?size>0)>
							<option value=""></option>
							<#list stuList as item>
								<option value="${item.id}" >${item.studentCode?default('无学号')}-${item.studentName?default('')}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">原选课：</span>
				<div class="filter-content">
					<input type="text" class="form-control" readonly="true" id="stuChosenSubName" value="">
				</div>
			</div>
			<div class="filter-item chosenSubjectHeaderClass">
				<span class="filter-name">选课调整：</span>
				<div class="filter-content">
					<select multiple vtype="selectMore" id="searchSubject" data-placeholder=" " onchange="findByCondition()">
						<#if coursesList?? && (coursesList?size>0)>
							<#list coursesList as item>
								<option value="${item.id}" >${item.subjectName}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
		</div>
		<div class="detail-div" id="stuChangeShowListDivId">
		</div>
	</div>
</div>
<script type="text/javascript">

$(function(){
	//初始化多选控件
	var viewContent1={
		'width' : '250px',//输入框的宽度
		'multi_container_height' : '33px',//输入框的高度
		'results_height' : '150px',//下拉选择的高度
		'max_selected_options' : '3'//限制3个
	}
	initChosenMore(".chosenSubjectHeaderClass","",viewContent1);
	var viewContent2={
		'width' : '220px',//输入框的宽度
		'multi_container_height' : '33px',//输入框的高度
		'results_height' : '150px',//下拉选择的高度
	}
	initChosenOne(".chosenClassHeaderClass","",viewContent2);
});
var stuSubMap = {};
function findByCondition(){
	$("#stuChangeShowListDivId").html("");
	if($("#searchSubject").val() == null || $("#searchSubject").val().length < 3){
		return;
	}
	var studentId = $("#stuId").val();
	if(studentId == ""){
		return;
	}
	var searchSubject = $("#searchSubject").val();
	var isPass = false;
	var searchSubjectIds = "";
	for(var i=0;i<searchSubject.length;i++){
		if(!stuSubMap[searchSubject[i]]){
			isPass = true;
		}
		if(searchSubjectIds == ""){
			searchSubjectIds+=searchSubject[i];
		}else{
			searchSubjectIds+=","+searchSubject[i];
		}
	}
	//if(!isPass){
	//	showMsgErrorSmall("不能和原选课相同！");
	//	return;
	//}
	$("#stuChangeShowListDivId").load("${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/stuSubChange/showList?searchSubjectIds="+searchSubjectIds+"&stuId="+studentId);
	
}

function doChangeStu(){
	var stuId = $("#stuId").val();
	if(stuId == ''){
		$("#stuChangeShowListDivId").html("");
		$("#searchSubject option").each(function(){
			$(this).removeAttr("selected");
		});
		$("#searchSubject").trigger("chosen:updated");
		$("#stuChosenSubName").val("");
		return;
	}
	$("#stuChangeShowListDivId").html("");
	$("#searchSubject option").each(function(){
		$(this).removeAttr("selected");
	});
	$("#searchSubject").trigger("chosen:updated");
	$("#stuChosenSubName").val("");
	stuSubMap = {};
	$.ajax({
	    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/stuSubChange/findStudent?stuId='+stuId,
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	if(jsonO.success){
				var stuSubIds = jsonO.stuSubIds;
				if(stuSubIds != ""){
					stuSubIds = stuSubIds.split(",");
					for(var j=0;j<stuSubIds.length;j++){
						stuSubMap[stuSubIds[j]] = stuSubIds[j];
					}
					var stuChosenSubName = "";
					$("#searchSubject option").each(function(){
						for(var j=0;j<stuSubIds.length;j++){
							if($(this).val()==stuSubIds[j]){
								if(stuChosenSubName == ""){
									stuChosenSubName+=$(this).text();
								}else{
									stuChosenSubName+=","+$(this).text();
								}
							}
						}
					});
					$("#stuChosenSubName").val(stuChosenSubName);
				}else{
					$("#stuChosenSubName").val("无");
				}
	 		} else {
	 			showMsgErrorSmall("对应的学生不存在！");
	 		}
	     }
	});
}

function showMsgErrorSmall(msg){
	layer.msg(msg, {
		offset: 't',
		time: 2000
	});
}

function searchStuList(){
	var searchClassCon = $("#teachClassId").val();
	if(searchClassCon == ''){
		itemShowList();
		return;
	}
	
	$("#stuId").html("");
	$("#stuId option").remove();
	$("#stuId").append("<option value=''></option>");

	var ii = layer.load();
	$.ajax({
	    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/stuSubChange/findStuByTeachClassId',
	    data: {'searchClassCon':searchClassCon},
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	$("#stuId").append("<option value=''></option>");
	    	if(jsonO.length>0){
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].studentCode+"-"+jsonO[index].studentName;
	    			$("#stuId").append(htmlOption);
		    	});
		    }else{
		    	layer.msg("未找到学生", {
					offset: 't',
					time: 2000
				});
		    }
			$('#stuId').trigger("chosen:updated");
			layer.close(ii);
	    }
	});
}

</script>