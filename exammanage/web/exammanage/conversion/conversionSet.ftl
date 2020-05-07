<div class="box box-default">
	<div class="box-body" id="dlFind">
		<p class="color-999">
			<i class="fa fa-exclamation-circle color-yellow"></i>
			 操作完成之后必须点击“保存”按钮，取消操作请点击“取消”按钮
		</p>
		<form id="mysavediv">
		<table class="table table-bordered table-striped table-hover no-margin">
            <thead>
				<tr>
				    <th>等级</th>
					<th>人数比例</th>
					<th>赋分（输入时左边输入框的分值大于右边）</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>	
			<#if conlist?exists && conlist?size gt 0>
				<#list conlist as item>
				<tr>
					<td class="scoreRankTd">${item.scoreRank}</td>
					<td>
						<input type="hidden" class="scoreRank" name="emConlist[${item_index}].scoreRank" value="${item.scoreRank}">
						<input type="hidden" name="emConlist[${item_index}].id" value="${item.id}">
						<input type="text" id="input_balance${item_index!}" name="emConlist[${item_index}].balance" class="form-control inline-block number" nullable="false" vtype="int" min="0" max="100" value="${item.balance?string('0.#')}"> %
					</td>
					<td>
						<input type="text" id="input_endScore${item_index!}" name="emConlist[${item_index}].endScore" class="form-control inline-block number" nullable="false" vtype="int" min="0" max="999" value="${item.endScore?string('0.#')}"> - 
						<input type="text" id="input_startScore${item_index!}" name="emConlist[${item_index}].startScore" class="form-control inline-block number" nullable="false" vtype="int" min="0" max="999" value="${item.startScore?string('0.#')}">  
					</td>
					<td>
						<a class="color-blue" href="javascript:void(0);" onclick="delRow(this,'${item.id!}')">删除</a>
					</td>
				</tr>
				</#list>
			</#if>	
			<tr id="addTr">
				<td colspan="4" class="text-center">
					<a class="color-blue  js-newSet" href="javascript:;">+ 新增赋分等级</a>
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
var trIndex = ${conlist?size};
var maxIndex =${conlist?size}; 
var isSubmit=false;
$(function(){
    // 添加行
    $('.box-default').on('click','.js-newSet',function(){
        var $form_group_item='<tr><td class="scoreRankTd">'+(trIndex+1)+'</td>'
								+'<td><input type="text" id="input_balance'+maxIndex+'" name="emConlist['+maxIndex+'].balance" nullable="false" vtype="int" min="0" max="100" class="form-control inline-block number" value=""> %'
								+'<input type="hidden" name="emConlist['+maxIndex+'].id" value=""><input type="hidden" class="scoreRank" name="emConlist['+maxIndex+'].scoreRank" value="'+(trIndex+1)+'"></td>'
								+'<td> <input type="text" id="input_endScore'+maxIndex+'" name="emConlist['+maxIndex+'].endScore" nullable="false" vtype="int" min="0" max="999" class="form-control inline-block number" value=""> - ' 
								+' <input type="text" id="input_startScore'+maxIndex+'" name="emConlist['+maxIndex+'].startScore" nullable="false" vtype="int" min="0" max="999" class="form-control inline-block number" value=""></td>'
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
	$("#36009").click();
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
	
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/conversionSet/save',
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
	var inputArr = $(".scoreRank");
	if(!tdArr && !inputArr){
		trIndex = 0;
		return;
	}
	for(var i=0; i < tdArr.length; i++){
		tdArr[i].innerHTML = (i+1);
	}
	for(var i=0; i < inputArr.length; i++){
		inputArr[i].value = (i+1);
	}
	trIndex = tdArr.length;
}
</script>