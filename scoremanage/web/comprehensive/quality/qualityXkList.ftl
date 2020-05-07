<div class="explain explain-no-icon">
    <p>说明：学考等第设置是指将学考的等第转换成折算分，然后以折算分记录综合素质。</p>
</div>
<div class="filter">
	<div class="filter-item filter-item-right">
		<a href="javascript:void(0);" class="btn btn-blue" onclick="save();">保存</a>
	</div>
</div>
<form id="mysavediv">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>等第名称</th>
				<th>折算分</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#if compreParamInfoList?exists && (compreParamInfoList?size>0)>
				<#list compreParamInfoList as item>
					<tr>
						<td>
							<div class="input-group" style="width:200px">
							  <input type="text" class="form-control gradeScore_class" maxlength="1" name="compreParamInfoList[${item_index}].gradeScore"  value="${item.gradeScore!}" style="text-transform: uppercase;">
							</div>
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].score" value="${item.score?default(0)}" >
						</td>
						<td><a class="color-red js-del" href="javascript:void(0);" onclick="delRow(this);">删除</a></td>
					</tr>
				</#list>
			</#if>
			<tr>
				<td class="text-center" colspan="3"><a class="js-addXk" href="">+新增</a></td>
			</tr>
		</tbody>
	</table>
</form>

<script>

$(function(){

	// 回车获取焦点
    //$('input:text:first').focus();
    var $inp = $('input:text');
    $inp.on('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();
        }
    });
})

var index=0;
<#if compreParamInfoList?exists && (compreParamInfoList?size>0)>
	index=${compreParamInfoList?size};
</#if>

$('.js-addXk').on('click', function(e){
	e.preventDefault();
	var mcPrefix = $('.mc_class').last().val();
	if(typeof(mcPrefix)=="undefined"){
		mcPrefix = 0;
	}
	var form_group_item = '<tr><td><div class="input-group" style="width:200px">'
						+ '<input type="text" class="form-control gradeScore_class" maxlength="1" name="compreParamInfoList['+index+'].gradeScore" style="text-transform: uppercase;">'
						+ '</div></td><td>'
						+ '<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].score">'
						+ '</td><td><a class="js-del color-red" href="javascript:void(0);" onclick="delRow(this)">删除</a></td></tr>';
	$(this).closest('tr').before(form_group_item);
	index++;
})

var isSubmit=false;
function save(){
	if(isSubmit){
		return;
	}
	var check = checkValue('#mysavediv');
	if(!check){
		isSubmit=false;
		return;
	}
	
	var reg1=/^[A-Z]$/;
	var f=false;
	$(".gradeScore_class").each(function(){
		var r = $(this).val().match(reg1);
		if(r==null){
			f=true;
			layer.tips('格式不正确(请输入大写英文字母)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});
	var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
	$(".score_class").each(function(){
		var r = $(this).val().match(reg);
		if(r==null){
			f=true;
			layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
		r = $(this).val();
		if(r>100){
			f=true;
			layer.tips('不能超过100!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});	
	
	if(f){
		isSubmit=false;
		return;
	}
	
	
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/comprehensive/quality/score/xk/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg("保存成功", {
					offset: 't',
					time: 2000
				});
	 			itemShowList(4);
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

function delRow(own){
	$(own).parent().parent().remove();
}

function changeItem(own) {
	var item = $(own).parent().parent().parent().next();
	item.find(".mc_class_prefix").val($(own).val());
}
</script>	
