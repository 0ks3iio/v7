<div class="filter">
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="" id="gradeId" class="form-control" onChange="changeGrade()">
				<option value="">---请选择---</option>
				<#if grades?exists&&grades?size gt 0>
                  	<#list grades as item>
					<option value="${item.id!}">${item.gradeName!}</option>
              	    </#list>
                </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select name="" id="classId" class="form-control" onChange="showStuList('1')">
				<option value="">---请选择---</option>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">是否有导师：</span>
		<div class="filter-content">
			<select name="" id="haveTutor" class="form-control" onChange="showStuList('1')">
				<option value="">全部</option>
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
		</div>
	</div>
	<div class="filter-item">

		<div class="filter-content">
			<div class="input-group input-group-search">
		        <select name="" id="queryType" class="form-control" onChange="showStuList('1')">
		        	<option value="1">姓名</option>
		        	<option value="2">学号</option>
		        </select>
		        <div class="pos-rel pull-left">
		        	<input type="text" id="qeuryParam" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead">
		        </div>
			    
			    <div class="input-group-btn">
			    	<a type="button" href="javascript:void(0);" onclick="showStuList('1')" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
</div>

<div class="table-container">
	<div class="table-container-header text-right">
		<a href="javascript:void(0);" onclick="doTeaExport()" class="btn btn-blue">导出Excel</a>
		<!--  <a href="javascript:void(0);" onclick="doRelieveStu()" class="btn btn-blue" >解除学生</a> -->
	</div>
	<div id="showStuList" class="table-container-body">
	</div>
</div>
<script>
$(function(){
	showStuList('1');
});
function showStuList(type){
	var str = "";
	var classId = $("#classId").val();
	var haveTutor = $("#haveTutor").val();
	str+= '?type='+haveTutor;
	str+= '&classId='+classId;
	var queryType = $("#queryType").val();
	str+='&queryType='+queryType
	var qeuryParam = $("#qeuryParam").val();
	if(queryType=='1'){
		str+='&studentName='+encodeURIComponent(qeuryParam.trim());
	}else{
		str+= '&studentCode='+encodeURIComponent(qeuryParam.trim());
	}
	if(type=='2'){
		document.location.href = "${request.contextPath}/tutor/result/student/export"+str;
	}else{
		var url =  '${request.contextPath}/tutor/result/student/list'+str;
		$("#showStuList").load(url);
	
	}
	

}
function doTeaExport(){
	showStuList("2");
}
function changeGrade(){
	var gradeId = $("#gradeId").val();
	$.ajax({
        url:"${request.contextPath}/tutor/result/get/class/page",
        data:{'gradeId':gradeId},
        dataType:'json',
        async: true,
        type:'POST',
        success: function(data) {
			var array = data;
			var htmlStr = '<option value="">---请选择---</option>';
			if(array.length > 0){
    			$.each(array, function(index, json){
    				htmlStr += '<option value="'+json.id+'">'+json.name+'</option>';
    			});
    		}
			$("#classId").html(htmlStr);
			$("#classId").val('');
			showStuList('1');
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
		}
    })

}

//解除学生导师关系
function doRelieveStu(){
	$.ajax({
        url:"${request.contextPath}/tutor/result/student/relieve",
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            if(data.success){
            	showSuccessMsg(data.msg);
            }else{
                showErrorMsg(data.msg);
            }
        }
  })
}
</script>