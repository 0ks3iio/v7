<div class="box box-default">
	<div class="box-body" id="dlFind">
		<form id="mysavediv">
		<input type="hidden" name="assessId" value="${assessId!}">
		<input type="hidden" name="subjectId" value="${subjectId!}">
		<table class="table table-bordered table-striped table-hover no-margin">
            <thead>
				<tr>
				    <th>班级</th>
					<th>分层</th>
				</tr>
			</thead>
			<tbody>	
			<#if teacherAsessSets?exists && teacherAsessSets?size gt 0>
				<#list teacherAsessSets as item>
				<tr>
					<td>
						<input type="hidden" name="teaSetlist[${item_index}].className" value="${item.className}">
						<input type="hidden" name="teaSetlist[${item_index}].id" value="${item.id!}">
						<input type="hidden" name="teaSetlist[${item_index}].unitId" value="${item.unitId!}">
						<input type="hidden" name="teaSetlist[${item_index}].assessId" value="${item.assessId!}">
						<input type="hidden" name="teaSetlist[${item_index}].subjectId" value="${item.subjectId!}">
						<input type="hidden" name="teaSetlist[${item_index}].classType" value="${item.classType!}">
						<input type="hidden" name="teaSetlist[${item_index}].classId" value="${item.classId!}">
						${item.className}
					</td>
					<td>
						<input type="radio" <#if item.slice?default("")=="0">checked="checked"</#if> name="teaSetlist[${item_index}].slice" value="0">A &nbsp;
						<input type="radio" <#if item.slice?default("")=="1">checked="checked"</#if> name="teaSetlist[${item_index}].slice" value="1">B &nbsp; 
					</td>
				</tr>
				</#list>
			</#if>	
			</tbody>
		</table>
		<table class="table table-bordered table-striped table-hover" style="margin-top:10px;" id="addAB">
            <thead>
				<tr>
				    <th>名称</th>
					<th>A层班</th>
					<th>B层班</th>
					<th>得分比例</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>	
			<#if teacherAsessRanks?exists && teacherAsessRanks?size gt 0>
				<#list teacherAsessRanks as item>
				<tr>
					<td class="scoreRankTd">${item.name!}</td>
					<td>
						<input type="hidden" class="scoreRank" name="teaRankList[${item_index}].id" value="${item.id!}">
						<input type="hidden" name="teaRankList[${item_index}].unitId" value="${item.unitId!}">
						<input type="hidden" name="teaRankList[${item_index}].assessId" value="${item.assessId!}">
						<input type="hidden" name="teaRankList[${item_index}].subjectId" value="${item.subjectId!}">
						<input type="hidden" name="teaRankList[${item_index}].name" value="${item.name}">
						前<input type="text" id="input_aslice${item_index!}" name="teaRankList[${item_index}].aslice" class="form-control inline-block number" nullable="false" vtype="int" min="1" max="9999" value="${item.aslice?string('0.#')}">名
					</td>
					<td>
						前<input type="text" id="input_bslice${item_index!}" name="teaRankList[${item_index}].bslice" class="form-control inline-block number" nullable="false" vtype="int" min="1" max="9999" value="${item.bslice?string('0.#')}">名
					</td>
					<td>
						<input type="text" id="input_scale${item_index!}" name="teaRankList[${item_index}].scale" class="form-control inline-block number" nullable="false" vtype="int" min="0" max="100" value="${item.scale?string('0.#')}">%
					</td>
					<td>
						<a class="color-blue" href="javascript:void(0);" onclick="delRow(this,'${item.id!}')">删除</a>
					</td>
				</tr>
				</#list>
			</#if>	
			<tr id="addTr">
				<td colspan="5" class="text-center">
					<a class="color-blue  js-newSet" href="javascript:;">+ 新增名次线</a>
				</td>
			</tr>
			</tbody>
		</table>
		</form>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
    <a class="btn btn-blue" href="javascript:;" onclick="save();">保存</a>
    <a class="btn btn-white" href="javascript:;" onclick="noSave();">取消</a>
</div>
<script type="text/javascript">
var character = new Array("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
var ABIndex = ${teacherAsessSets?size};
var trIndex = ${teacherAsessRanks?size};
var maxIndex =${teacherAsessRanks?size}; 
var isSubmit=false;
$(function(){
    // 添加行
    $('#addAB').on('click','.js-newSet',function(){
    	if(trIndex>25){
    		layerTipMsg(false,"提示","当前新增上限只能26个字母！");
			isSubmit=false;
			return;
    	}
        var $form_group_item='<tr><td class="scoreRankTd">'+character[trIndex]+'</td>'
								+'<td><input type="hidden" class="scoreRank" name="teaRankList['+maxIndex+'].id" value="">'
								+'<input type="hidden" name="teaRankList['+maxIndex+'].unitId" value="">'
								+'<input type="hidden" name="teaRankList['+maxIndex+'].assessId" value="">'
								+'<input type="hidden" name="teaRankList['+maxIndex+'].subjectId" value="">'
								+'<input type="hidden" name="teaRankList['+maxIndex+'].name" value="'+character[trIndex]+'">'
								+'前<input type="text" id="input_aslice'+maxIndex+'" name="teaRankList['+maxIndex+'].aslice" class="form-control inline-block number" nullable="false" vtype="int" min="1" max="9999" value="">名</td>'
								+'<td>前 <input type="text" id="input_bslice'+maxIndex+'" name="teaRankList['+maxIndex+'].bslice" class="form-control inline-block number" nullable="false" vtype="int" min="1" max="9999" value="">名</td>' 
								+'<td> <input type="text" id="input_scale'+maxIndex+'" name="teaRankList['+maxIndex+'].scale" class="form-control inline-block number" nullable="false" vtype="int" min="0" max="100" value="">%</td>' 
								+'<td><a class="color-blue" href="javascript:void(0);" onclick="delRow(this)">删除</a></td>'
            					+'</tr>';
        $('#addTr').before($form_group_item);
        var $inp = $('input:text');
        $inp.on('keydown', function (e) {
            var key = e.which;
            if (key == 13) {
                e.preventDefault();
                var nxtIdx = $inp.index(this) + 1;
                $(":input:text:eq(" + nxtIdx + ")").focus().select();
            }
        });                
        trIndex++;
        maxIndex++;
    });
})

function noSave(){
	$("#40201").click();
}

function save(){
	if(isSubmit){
		return;
	}
	var check = checkValue('#mysavediv');
	if(!check){
		isSubmit=false;
		return;
	}
	
	for(var i=0; i<ABIndex; i++) {
		var slice=$('input[name="teaSetlist['+i+'].slice"]:checked').val();
		var className=$('input[name="teaSetlist['+i+'].className"]').val();
		if(!slice){
			layerTipMsg(false,"提示",className+"分层设置不能为空！");
			isSubmit=false;
			return;
		}
	}
	
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/teacherasess/asessResult/asessResultChange/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				isSubmit=false;
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#mysavediv").ajaxSubmit(options);
} 

function delRow(own,conId){
	$(own).parent().parent().remove();
	initScoreRank();
}


function initScoreRank(){
	var tdArr = $(".scoreRankTd");
	if(!tdArr){
		trIndex = 0;
		return;
	}
	for(var i=0; i < tdArr.length; i++){
		tdArr[i].innerHTML = character[i];
	}
	trIndex = tdArr.length;
}
</script>