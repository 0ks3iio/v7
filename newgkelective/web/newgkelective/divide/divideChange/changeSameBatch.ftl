<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">

<div id="resultListDiv">
	<div class="filter">
		<div class="filter-item filter-item-left">
			<a href="javascript:doSaveChange();" class="btn btn-blue">保存结果</a>
			<a href="javascript:gobackClassStudent();" class="btn btn-default">取消</a>
		</div>
	</div>

	<div class="table-switch-container" id="allTable">
		<div class="table-switch-box" id="leftTable">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item">
						<span class="filter-name">班级：</span>
						<input type="hidden" id="leftClassSelectId" value="${chooseClass.id!}">
						<span class="filter-name">${chooseClass.className!}</span>
					</div>
					<div class="filter-item">
						<span class="filter-name">时间点：</span>
						<span class="filter-name"><#if chooseClass.subjectType?default('')=='A'>选考<#else>学考</#if>${chooseClass.batch!}</span>
					</div>
					<div class="filter-item">
						<div class="filter-content">
							<input type="text" class="form-control float-left" id="chooseRowNum" placeholder="请输入" maxlength="4" style="width: 70px;">
							<button class="btn btn-blue float-left ml5" onclick="chooseRows()">选行</button>
						</div>
					</div>
				</div>
			</div>
			<div style="width:100%;" class="tableDivClass" id="leftTableId"></div>
		</div>
		<div class="table-switch-control">
			<a id='toRight' onclick='moveStudents("R")' class="btn btn-sm"><i class="wpfont icon-arrow-right"></i></a>
			<a id='toLeft' onclick='moveStudents("L")' class="btn btn-sm"><i class="wpfont icon-arrow-left"></i></a>
		</div>
		<div class="table-switch-box" id="rightTable">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item chosenClassHeaderClass">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<select vtype="selectOne" name="" id="rightClassSelectId" class="form-control input-sm" data-placeholder="未找到班级" onchange="rightChangeClass()">
								<option value=""></option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<div style="width:100%;" class="tableDivClass" id="rightTableId"></div>
		</div>
	</div>
</div>
<script>
	var leftClassRightClassMap = {};
	$(function(){
		//初始化单选控件
		var viewContent={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '220px',//输入框的宽度
			'results_height' : '200px'//下拉选择的高度
		}
		initChosenOne(".chosenClassHeaderClass","",viewContent);
		leftChangeClass();
	});
	
	function refLeftDiv(){
		var classSelectId = $("#leftClassSelectId").val();
		var url='${request.contextPath}/newgkelective/${divideId}/arrayResult/changeClass/list?type=1&right=0&divideClassId='+encodeURIComponent(classSelectId);
		$("#leftTableId").load(url);
	}
	function leftChangeClass(){
		refLeftDiv();
		var classSelectId = $("#leftClassSelectId").val();
		$("#rightTableId").html("");
		$("#rightClassSelectId option").remove();
		$("#rightClassSelectId").append("<option value=''></option>");
		if(leftClassRightClassMap[classSelectId]){
			var jsonO = leftClassRightClassMap[classSelectId];
			if(jsonO.length>0){
				$("#rightClassSelectId").attr("data-placeholder","选择班级");
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].classId+"'>"+jsonO[index].className;
	    			$("#rightClassSelectId").append(htmlOption);
		    	});
		    }else{
		    	$("#rightClassSelectId").attr("data-placeholder","未找到班级");
		    }
			$('#rightClassSelectId').trigger("chosen:updated");
	    	return;
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/newgkelective/${divideId!}/arrayResult/changeClass/findClass?type=1&divideClassId='+encodeURIComponent(classSelectId),
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	leftClassRightClassMap[classSelectId]=jsonO;
		    	if(jsonO.length>0){
		    		$("#rightClassSelectId").attr("data-placeholder","选择班级");
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].classId+"'>"+jsonO[index].className;
		    			$("#rightClassSelectId").append(htmlOption);
			    	});
			    }else{
			    	$("#rightClassSelectId").attr("data-placeholder","未找到班级");
			    }
				$('#rightClassSelectId').trigger("chosen:updated");
				layer.close(ii);
		    }
		});
	}
	
	var leftContent = $("#leftTable").html();
	var rightContent = $("#rightTable").html();
	function moveStudents(flag){
		leftContent = $("#leftTable").html();
		rightContent = $("#rightTable").html();
		if($("#rightClassSelectId").val()==""){
			return;
		}
		var src = '';
		var dest = '';
		if(flag == 'R'){
			src = "#leftTable";
			dest = "#rightTable";
		}else{
			dest = "#leftTable";
			src = "#rightTable";
		}
		var i=0;
		var stuId=[];
		//增加到右边的html
		var htmlText="";
		if($(src).find('tbody input:checkbox[name="course-checkbox"]:checked').length>0){
			$(src).find('tbody input:checkbox[name="course-checkbox"]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				htmlText=htmlText+'<tr class="new">'+$(this).parents("tr").html()+'</tr>';
				$(this).parents("tr").addClass("linChangeClass");
			});
			$(dest).find("table").find("tbody").append(htmlText);
			$(src).find("table").find(".linChangeClass").remove();
		}
		
		updateNum();
	}
	
	function updateNum(){
		updateTableInf("leftTable"); 
		updateTableInf("rightTable"); 
		updateTableData0();
		updateTableData1();
	}
	
	
	function updateTableInf(obj){
		var all = $("#"+obj).find('tbody tr');
		if(all.length <1){
			$("#"+obj).find(".labelInf span em").text(0);
			return;
		}
		
		var avg1=0.0;
		var ysyAvg=0.0;
		var allAvg=0.0;
		var manCount =0;
		var woManCount=0;
		var i = 0;
		all.each(function(){
			if($(this).is(":hidden")){
				
			}else{
				i = i+1;
				$(this).find('td:eq(1)').text(i);
			}
			
			if($(this).find('td:eq(3)').text() == '男'){
				manCount = manCount +1;
			}
			if($(this).find('td:eq(3)').text() == '女'){
				woManCount =woManCount+1;
			}
			avg1 = avg1 + parseFloat($(this).find('td:eq(6)').text());
			ysyAvg = ysyAvg + parseFloat($(this).find('td:eq(7)').text());
			allAvg = allAvg + parseFloat($(this).find('td:eq(8)').text());
		});
		
		var labelInfs = $("#"+obj).find(".labelInf span em");
		labelInfs.eq(0).text(all.length);
		labelInfs.eq(1).text(manCount);
		labelInfs.eq(2).text(woManCount);
		labelInfs.eq(3).text((avg1/all.length).toFixed(2));
		labelInfs.eq(4).text((ysyAvg/all.length).toFixed(2));
		labelInfs.eq(5).text((allAvg/all.length).toFixed(2));
	}
	
	function rightChangeClass(){
		var classSelectId = $("#rightClassSelectId").val();
		var url='${request.contextPath}/newgkelective/${divideId}/arrayResult/changeClass/list?type=1&right=1&divideClassId='+encodeURIComponent(classSelectId);
		$("#rightTableId").load(url);
	}
	
	function selAll(){
		var checked = $(event.target).prop("checked");
		var all = $(event.target).parents("table").find("tbody input:checkbox[name='course-checkbox']");
		
		if(checked){
			all.prop('checked',true);
		}else{
			all.prop('checked',false);
		}
	}
	var isSubmit=false;
	function doSaveChange(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		if(($("#leftTableId").html() == '' || $("#rightTableId").html() == '') || ($("#leftTableId").find(".new").length == 0 && $("#rightTableId").find(".new").length == 0)){
			layer.msg("没有需要处理的数据", {
				offset: 't',
				time: 2000
			});
			isSubmit = false;
			return;
		}
		var leftClassSelectId = $("#leftClassSelectId").val();
		var rightClassSelectId = $("#rightClassSelectId").val();
		var leftAddStu = "";
		$("#leftTableId").find(".new").each(function(i){
			if(leftAddStu != ""){
				leftAddStu+=","+$(this).find("input").val();
			}else{
				leftAddStu+=$(this).find("input").val()
			}
		});
		var rightAddStu = "";
		$("#rightTableId").find(".new").each(function(i){
			if(rightAddStu != ""){
				rightAddStu+=","+$(this).find("input").val();
			}else{
				rightAddStu+=$(this).find("input").val()
			}
		});
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId!}/arrayResult/changeClass/save",
			data:{'leftClassSelect':leftClassSelectId,
            'rightClassSelect':rightClassSelectId,'leftAddStu':leftAddStu,'rightAddStu':rightAddStu},
			dataType: "JSON",
			success: function(data){
				if(data.success){
					layer.msg("保存成功！", {
						offset: 't',
						time: 2000
					});
					$("#leftTableId").find(".new").each(function(i){
						$(this).removeClass("new");
					});
					$("#rightTableId").find(".new").each(function(i){
						$(this).removeClass("new");
					});
					updateNum();
				}else{
					layerTipMsg(data.success,"失败",data.msg);
					$("#leftTable").html(leftContent);
					$("#rightTable").html(rightContent);
					
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
			isSubmit = false;
	}
	
	function gobackClassStudent(){
		showDetail('${chooseClass.id!}','${type!}');
	}
	
	function chooseRows(){
		var num=$("#chooseRowNum").val().trim();
		var pattern=/[^0-9]/;
		if(pattern.test(num) || num.slice(0,1)=="0"){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#chooseRowNum").val('');
			$("#chooseRowNum").focus();
			return false;
		}
		var rows=parseInt(num);
		if(num<=0){
			return false;
		}
		var ii=0;
		var trList=$(".mytable_0 tbody").find("tr");
		for(var i=0;i<trList.length;i++){
			if(!$(trList[i]).is(":hidden")){
				if(ii<rows){
					$(trList[i]).find("input[name='course-checkbox']").prop('checked',true);
				}else{
					$(trList[i]).find("input[name='course-checkbox']").prop('checked',false);
				}
				ii++;
			}else{
				$(trList[i]).find("input[name='course-checkbox']").prop('checked',false);
			}
		}
		
	}
</script>