<form  id="healthInputForm" class="healthInput">
	<div class="health-checkup ">
		<h2>${dshtDto.schoolYearTeam!}体检项目单</h2>
		<div class="filter" style="margin-left: 21%;">
			<div class="filter-item" style="width:150px;">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<p id="stuName">${dshtDto.studentName!}</p>
				</div>
			</div>
			<div class="filter-item" style="width:130px;">
				<span class="filter-name">性别：</span>
				<div class="filter-content">
					<p id="stuSex">${dshtDto.sex!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<p id="stuClass">${dshtDto.className!}</p>
				</div>
			</div>
		</div>
		<table class="table" style="table-layout: fixed; ">
			<thead>
				<tr>
					<th width="30%">体检项</th>
					<th width="20%">单位</th>
					<th width="50%">检查结果</th>
				</tr>
			</thead>
			<tbody>
			 <#if dshtDto.listDSHPI?exists&&dshtDto.listDSHPI?size gt 0>
			    <#list dshtDto.listDSHPI as dshpi>
			      <tr>
					<td>${dshpi.itemName!}</td>
					<td><#if dshpi.itemUnit?default("")=="">/<#else>${dshpi.itemUnit!}</#if></td>
					<#if dshtDto.itemIdMap?exists&&dshtDto.itemIdMap?size gt 0>
					<td style="DISPLAY:none">${dshtDto.itemIdMap[dshpi.itemId]!}</td>
					<td><input type="text" style="width:340px;" class="form-control number itemName"  name="${dshpi.itemName!}" nullable="false" maxlength="100" id="${dshpi.itemId!}" value="${dshtDto.itemIdMap[dshpi.itemId]!}"></td>
					<#else>
					<td><input type="text" style="width:340px;" class="form-control number itemName"  name="${dshpi.itemName!}" nullable="false" maxlength="100" id="${dshpi.itemId!}" "></td>
					</#if>
				  </tr>
			    </#list>
			 <#else>
			 
			 </#if>
			</tbody>
		</table>
	</div>
</form>
<div class="text-right">
	<button class="btn btn-white" onclick="doInputExport()">导出</button>
	<button class="btn btn-white" onclick="doPrint()">打印</button>
	<button class="btn btn-blue js-save">保存</button>
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
   //导出
    function doInputExport(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		if (LODOP==undefined || LODOP==null) {
			return;
		 }
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".healthInput")));
		LODOP.SAVE_TO_FILE(getConditions()+"体检结果表"+".xls");	
	}
	
	function getConditions(){
		var acadyear = $("#searchAcadyear").val();
		var semester = $("#searchSemester").val();
		var stuName = $("#stuName").text();
		var stuSex = $("#stuSex").text();
		var stuClass = $("#stuClass").text();
		return acadyear+"学年第"+semester+"学期"+stuClass+"学生"+stuName;
	}
   
   <#--  
	function doInputExport(){
	    var acadyear = $("#searchAcadyear").val();
		var semester =  $("#searchSemester").val();
		var studentId = $("#studentId").val();
	    var obj = new Object();
	    $('.itemName').each(function () {
            var value = $(this).val();
            var name = $(this).attr("name");
            obj[name] = value;
	    });
        var inputResult = JSON.stringify(obj);
	    alert(inputResult);
		document.location.href = "${request.contextPath}/stuwork/health/input/export?inputResult="+inputResult+"&studentId="+studentId+"&acadyear="+acadyear+"&semester="+semester;
	}
     -->
    function getResult(){
       var obj = new Object();
       $('.itemName').each(function () {
            var value = $(this).val();
            var name = $(this).attr("name");
            obj[name] = value;
	    });
       return JSON.stringify(obj);
    }
    
   
    
    //保存
    $('.js-save').click(function(){
       var acadyear = $("#searchAcadyear").val();
	   var semester =  $("#searchSemester").val();
	   var studentId = $("#studentId").val();
	    var check = checkValue('#healthInputForm');
		if(!check){
		 	return;
	   } 
       $.ajax({
                url:"${request.contextPath}/stuwork/health/input/add?studentId="+studentId+"&acadyear="+acadyear+"&semester="+semester,
	            data:getResult(),
	            clearForm : false,
	            resetForm : false,
	            dataType:'json',
	            contentType: "application/json",
	            type:'post',
	            success:function (data) {
	                if(data.success){
	                 showSuccessMsgWithCall(data.msg,showList(studentId));
	          //       showSuccessMsg(data.msg);
	                }else{
	                    showErrorMsg(data.msg);
	                }
	            }
	        });
    })
    

   //打印
   function doPrint(){
    var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
    if (LODOP==undefined || LODOP==null) {
			return;
		}
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_HTM("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".healthInput")));
	LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
	LODOP.PREVIEW();//打印预览
}

</script>