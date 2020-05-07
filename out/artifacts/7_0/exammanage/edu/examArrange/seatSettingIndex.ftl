<form id="settingForm">
<input type="hidden" name="examId" value="${dto.examId!}">
<div id="myDiv">
		<div class="">
			<div class="form-group">
				<label class="control-title no-padding-right">座位相邻设置 <small class="color-grey control-label">(不相邻表示前后左右均不相邻)</small></label>
			</div>
			<div class="">
				<label><input type="radio" name="type" class="wp" value="0" <#if dto.type?default("0") == "0">checked = "true"</#if>><span class="lbl"> 随机安排</span></label>
				<label><input type="radio" name="type" class="wp" value="1" <#if dto.type?default("1") == "1">checked = "true"</#if>><span class="lbl"> 同校不相邻</span></label>
				<label><input type="radio" name="type" class="wp" value="2" <#if dto.type?default("2") == "2">checked = "true"</#if>><span class="lbl"> 同校可相邻但同班不相邻</span></label>
			</div>
		</div>
		<br><br>
		<h4 class=""><b>考场座位数设置</b></h4>
		<h5>考场座位数：<span id="seatCountNum">${dto.sumSeatNum}</span></h5>
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序列号</th>
					<th>座位数</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#assign col = 0>
				<#if dto.settings?exists && (dto.settings?size > 0)>
					<#list dto.settings as setting>
					<#if setting.seatNum gt col>
						<#assign col = setting.seatNum>
					</#if>
					<tr id="tr_${setting.columnNo}">
						<td id="sp_${setting.columnNo}">${setting.columnNo}</td>
						<td>
							<input id="input_${setting.columnNo}" type="text" onBlur="updateSeatNum(this)" class="form-control" nullable="false" vtype="int" max="50" min="1" name="seatNums" value="${setting.seatNum}">
						</td>
						<td>
							<a class="table-btn" href="javascript:;" onclick="delCol(${setting.columnNo});">删除</a>
						</td>
					</tr>
					</#list>
				</#if>
				<tr id="addTr">
				    <td class="text-center" colspan="3"><a class="table-btn" href="javascript:;" onclick="addCol();">+新增</a></td>
				</tr>
			</tbody>
		</table>
		<h5>座位示意图（座位排序默认采用龙摆尾形式）</h5>
		<#if dto.settings?exists && (dto.settings?size > 0)>
		<table id="temp_table"  class="table table-striped table-hover">
		<#assign row = dto.settings?size>
			<thead>
				<tr>
					<#list 1..row as i><th>${i}</th></#list>
				</tr>
			</thead>
			<tbody>
			<#list 1..col as j>
				<tr>
					<#list 1..row as i>
					<td>
					<#assign indexNum=0>
					<#assign seat = 0>
					<#list dto.settings as setting>
						<#if i gt setting.columnNo>
							<#assign seat = seat + setting.seatNum>
						</#if>
						
						<#if setting_index+1 == i>
							<#assign indexNum = setting.seatNum>
						</#if>
					</#list>
					<#if j gt indexNum>
					<#else>
						<#if i%2 == 0>
						${seat + j}
						<#else>
						${seat + indexNum -j+1}
						</#if>
					</#if>
					</td>
					</#list>
				</tr>
			</#list>
			</tbody>
		</table>
		</#if>
		<div class="text-right">
			<a class="btn btn-blue" href="javascript:;" onclick="saveFrom();">保存</a>
		</div>	
</div>
</form>
<script>
var maxCol = parseInt('${row!}');//记录页面最大的列数,只加不减
var col = parseInt('${row}');//实际的列数
function addCol(){
	maxCol = maxCol + 1;
	if(col > 9){
		alert('已经达到最大列！');
		layerTipMsg(false,"","已经达到最大列！");
		return;
	}
	col = col + 1;
	var TrHtml = '<tr id="tr_'+maxCol+'"><td id="sp_'+maxCol+'">'+col+'</td><td>'
			+'<input id="input_'+maxCol+'" type="text" onBlur="updateSeatNum(this)" class="form-control" nullable="false" vtype="int" max="50" min="1" name="seatNums" value="1"></td>'
			+'<td><a class="table-btn" href="javascript:;" onclick="delCol('+maxCol+');">删除</a></td></tr>';
	$("#addTr").before($(TrHtml));  
	initTable();
}

function delCol(trIndex){
	if(col < 2){
		layerTipMsg(false,"","列数至少为1列！");
		return;
	}
	col = col - 1;
	var index = trIndex
	for(;index<maxCol+1;index++){
		if($("#sp_"+index)){
			var oldCol = parseInt($("#sp_"+index).html());
			var newCol = oldCol - 1;
			$("#sp_"+index).html(newCol);
		}
	}
	$("#tr_"+trIndex).remove();
	initTable();
}

function initTable(){
	var obj_cols = $("[name='seatNums']");
	var colNum = obj_cols.length;//表格有几列
	var maxRowNum = 0;//表格有几行
	var seatCountNum = 0;
	for(var i =0;i<colNum;i++){
		var seatNum = obj_cols[i].value;
		seatCountNum = seatCountNum + parseInt(seatNum);
		if(parseInt(seatNum) > maxRowNum){
			maxRowNum = parseInt(seatNum);
		}
	}
	var tableHtml = '<thead><tr>';
	for(var i =0;i<colNum;i++){
		tableHtml = tableHtml + '<th>'+(i+1)+'</th>';
	}
	tableHtml = tableHtml +'</tr></thead><tbody>';
	var seatNo = 0;
	for(var row = 0;row<maxRowNum;row++){
		tableHtml = tableHtml + '<tr>';
		for(var i =0;i<colNum;i++){
			var seatNum = obj_cols[i].value;
			if((row + 1) > seatNum){
				tableHtml = tableHtml + '<td></td>';
			}else{
				for(var c =0;c<colNum;c++){
					var vv = obj_cols[c].value;
					if(i > c){
						seatNo = seatNo + parseInt(vv);
					}
				}
				if(i%2 == 0){
					seatNo = seatNo + parseInt(seatNum) - row;
				}else{
					seatNo = seatNo + row + 1;
				}
				tableHtml = tableHtml + '<td>'+seatNo+'</td>';
			}
			seatNo = 0;
		}
		tableHtml = tableHtml + '</tr>';
	}
	tableHtml = tableHtml + '</tbody>'
	$('#temp_table').html(tableHtml);
	$('#seatCountNum').html(seatCountNum);
}
function updateSeatNum(e){
	var check = checkValue('#myDiv');
    if(!check){
        return;
    }
    initTable();
}


var isSubmit=false;
function saveFrom(){
	if(isSubmit){
		return;
	}
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        return;
    }
    isSubmit = true;
	var options = {
		url : "${request.contextPath}/exammanage/edu/examArrange/seatSetting/save",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		isSubmit=false;
		 		return;
		 	}else{
		 		layer.closeAll();
				//layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				layer.closeAll();
					layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				setTimeout(itemShowList1('3'),500);
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#settingForm").ajaxSubmit(options);
}

</script>
									