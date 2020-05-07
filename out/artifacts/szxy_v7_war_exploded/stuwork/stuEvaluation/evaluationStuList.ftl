		<div class="box box-default">
			<div class="box-body">
				<div class="filter-container" id="searchType">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">学年：</span>
			                <div class="filter-content">
			                    <select name="acadyear" id="acadyear" class="form-control" onchange="doSearch('')" style="width:120px">
			                    	<#if acadyearList?exists && acadyearList?size gt 0>
			                    		<#list acadyearList as item>
					                        <option value="${item!}" <#if item==dto.acadyear?default("")>selected="selected"</#if>>${item!}</option>
			                    		</#list>
			                    	</#if>
			                    </select>
			                </div>
						</div>
						<div class="filter-item">
							<span class="filter-name">学期：</span>
			                <div class="filter-content">
			                    <select name="semester" id="semester" class="form-control" onchange="doSearch('')" style="width:120px">
			                        <option value="1" <#if "1"==dto.semester?default("")>selected="selected"</#if>>第一学期</option>
			                        <option value="2" <#if "2"==dto.semester?default("")>selected="selected"</#if>>第二学期</option>
		                    </select>
			                </div>
						</div>
						<div class="filter-item">
							<span class="filter-name">班级：</span>
			                <div class="filter-content">
			                    <select name="classId" id="classId" class="form-control" onchange="doSearch('')" style="width:120px">
			                       <#if classList?exists && classList?size gt 0>
		                    		<#list classList as item>
				                        <option value="${item.id!}" <#if item.id==dto.classId?default("")>selected="selected"</#if>>${item.classNameDynamic!}</option>
		                    		</#list>
		                    		</#if>
		                   		</select>
			                </div>
						</div>
						<div class="filter-item filter-item-right">
							<button class="btn btn-blue" onclick="doImport();return false;">导入</button>
							<button class="btn btn-blue" onclick="doPrint()">打印</button>
							<#--<button class="btn btn-lightgreen" onclick="doExport();">导出</button>-->
						</div>
					</div>
				</div>
				
			</div>
		</div>
		<div class="row">
			<input type="hidden" name="studentId" id="studentId" value="${dto.studentId!}">
			<input type="hidden" name="id" id="evaId" value="${eva.id!}">
			<div class="col-sm-3">
				<div class="list-group" style="max-height:500px;overflow:auto;">
					<#if evaList?exists && evaList?size gt 0>
						<#list evaList as item>
							<a href="javascript:doSearch('${item.studentId!}')" class="list-group-item <#if dto.studentId?default("")==item.studentId>active</#if>">
								<#if item.id?default("")==""><span class="badge badge-red">未录入</span>
								<#else><span class="badge badge-lightgreen">已录入</span>
								</#if>
								${item.studentName!}
							</a>
						</#list>
					</#if>
				</div>
			</div>
			<form id="submitForm">
			<div class="col-sm-9">
				<div class="box box-default">
					<div class="box-body">
						<div class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-title no-padding-right">等第评语录入</label>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right">姓名：</label>
								<div class="col-sm-6">
									<p class="form-group-txt">${studentName!}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right">操行等第：</label>
								<div class="col-sm-6">
									<select name="gradeId" id="gradeId" class="form-control" nullable="false"  style="width:120px">
										<option value="">--请选择--</option>
				                    	<#if boptionList?exists && boptionList?size gt 0>
				                    		<#list boptionList as item>
						                        <option value="${item.id!}" <#if item.id==eva.gradeId?default("")>selected="selected"</#if>>${item.optionName!}</option>
				                    		</#list>
				                    	</#if>
				                    </select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right">期末评语：</label>
								<div class="col-sm-6">
									<textarea name="remark" id="remark" maxlength="1000"" rows="6" class="form-control">${eva.remark!}</textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right">社团工作：</label>
								<div class="col-sm-6">
									<textarea name="association" id="association" rows="6" maxlength="1000" class="form-control">${eva.association!}</textarea>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-6 col-sm-offset-2">
									<button class="btn btn-long btn-blue" onclick="doSaveEva();return false;">保存</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>
<#--隐藏的打印内容-->
<#if rowCount?exists>
<div id="printContent" style="display:none;">
		<h1 class="text-center">${unitName?replace("数字校园", "")+ "(" + dto.acadyear+semesterName + ")"!}<br>${subjectName!}成绩登记表</h1>

	<div style="col-xs-12">
		<span class="col-xs-4">班级：${className!}</span>
		<span class="col-xs-4">科目：操行等第</span>
		<span class="col-xs-3">班主任签名：</span>
	</div>
	<table class="table table-bordered table-condensed no-margin"  style="border:1px solid #000">
	    <thead>
	       <tr>
		   		<th class="text-center" width="50px;" style="border:1px solid #000">序号</th>
		        <th class="text-center" width="200px;" style="border:1px solid #000">姓名</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">成绩</th>
		        
				<th width="20px;" style="border:1px solid #000"></th>
		        
		        <th class="text-center" width="50px;" style="border:1px solid #000">序号</th>
		        <th class="text-center" width="200px;" style="border:1px solid #000">姓名</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">成绩</th>
	        </tr>
	    </thead>
	    <tbody>
	    <#if rowCount?default(0) gt 0>
	    <#list 1..rowCount as item>
	    	<tr>
		        <td style="border:1px solid #000" class="text-center">${item_index+1}</td>
		        <td  style="border:1px solid #000" class="text-left">${evaList[item_index].studentName!}</td>
		        <td style="border:1px solid #000" class="text-center">${evaList[item_index].grade!}</td>
		        
		         <#if item_index == 0>
		      		<td style="border:1px solid #000" rowspan="${rowCount!}"></td>
		      	</#if>
		        
		        <#if !(evaCount lt (item_index+rowCount+1))>
			        <td style="border:1px solid #000" class="text-center">${item_index+rowCount+1}</td>
			        <td  style="border:1px solid #000" class="text-left">${evaList[item_index+rowCount].studentName!}</td>
			        <td style="border:1px solid #000" class="text-center">${evaList[item_index+rowCount].grade!}</td>
		        </#if>
	        </tr>
	    </#list>
	    </#if>
	    </tbody>
	</table>
	<div style="col-xs-12">
    	<span class="col-xs-4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-3 ">打印时间：<span id="time"></span></span>
	</div>
</div>
</#if>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />

<script>

	function doImport(){
		$("#itemShowDivId").load("${request.contextPath}/stuwork/evaluation/stu/doImport?"+searchUrlValue("#searchType"));
	}
	function doSearch(studentId){
		var url="${request.contextPath}/stuwork/evaluation/stu/list/page?"+searchUrlValue("#searchType")+"&studentId="+studentId;
		$("#itemShowDivId").load(url);
	}
	var isSubmit=false;
	function doSaveEva(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classId").val();
		var studentId=$("#studentId").val();
		var id=$("#evaId").val();
		var gradeId=$("#gradeId").val();
		var remark=$("#remark").val();
		var association=$("#association").val();
		$.ajax({
			url:'${request.contextPath}/stuwork/evaluation/stu/doSaveEva',
			data:{'acadyear':acadyear,'semester':semester,'classId':classId,'studentId':studentId,'id':id,'gradeId':gradeId,'remark':remark,'association':association},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	doSearch($("#studentId").val());
    			}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	//打印
   function doPrint(){
   		<#if !rowCount?exists>
   			return;
   		</#if>
	    var now = new Date();
		$("#time").html(now.getFullYear()+'/'+(now.getMonth()+1)+'/'+now.getDate());
	
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		if (LODOP==undefined || LODOP==null) {
			return;
		}
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
			LODOP.ADD_PRINT_HTM("15mm","10mm","RightMargin:10mm","BottomMargin:3mm",getPrintContent($("#printContent")));
		//LODOP.ADD_PRINT_HTM("0mm","0mm","RightMargin:0mm","BottomMargin:0mm",getPrintContent($("#printContent")));
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//纵向打印
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
		LODOP.PREVIEW();//打印预览
	}
</script>
