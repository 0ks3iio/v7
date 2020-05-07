<div class="explain explain-no-icon">
    <p>说明：英语成绩折算名称设置指，首先将学生的英语笔试成绩（不包含口语）按年级进行排名，然后按照名次段赋予折算分，最后会以折算分计算综合素质分。</p>
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
				<th>名次段（%）</th>
				<th>分值</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#if compreParamInfoList?exists && (compreParamInfoList?size>0)>
				<#list compreParamInfoList as item>
					<tr>
						<td>
							<div class="input-group" style="width:200px">
							  <input type="text" readonly="true" onfocus="this.blur()" class="form-control mc_class mc_class_prefix" maxlength="3" name="compreParamInfoList[${item_index}].mcPrefix"  id="mcPrefix_${item_index}" vtype="int" value="${item.mcPrefix}">
							  <span class="input-group-addon"><i class="fa fa-minus"></i></span>
							  <input type="text" class="form-control mc_class" maxlength="3" name="compreParamInfoList[${item_index}].mcSuffix"  vtype="int" onblur="changeItem(this);" id="mcSuffix_${item_index}" value="${item.mcSuffix}">
							</div>
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].score" value="${item.score}">
						</td>
						<td><a class="color-red js-del" href="javascript:void(0);" onclick="delRow(this,${item.mcPrefix},${item.mcSuffix});">删除</a></td>
					</tr>
				</#list>
			</#if>
			<tr>
				<td class="text-center" colspan="3"><a class="js-addEnglish" href="">+新增</a></td>
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

$('.js-addEnglish').on('click', function(e){
	e.preventDefault();
	var mcPrefix = $('.mc_class').last().val();
	if(typeof(mcPrefix)=="undefined"){
		mcPrefix = 0;
	}
	var form_group_item = '<tr><td><div class="input-group" style="width:200px">'
						+ '<input type="text" readonly="true" onfocus="this.blur()" class="form-control mc_class mc_class_prefix" maxlength="3" vtype="int" name="compreParamInfoList['+index+'].mcPrefix" id="mcPrefix_'+index+'" value="'+mcPrefix+'">'
						+ '<span class="input-group-addon"><i class="fa fa-minus"></i></span>'
						+ '<input type="text" class="form-control mc_class" maxlength="3" name="compreParamInfoList['+index+'].mcSuffix" onblur="changeItem(this);" vtype="int" id="mcSuffix_'+index+'">'
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
	
	var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
	var f=false;
	$(".mc_class").each(function(){
		var r = $(this).val().match(reg);
		if(r==null){
			f=true;
			layer.tips('格式不正确(最多3位整数)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
		var r = $(this).val();
		if(r>100){
			f=true;
			layer.tips('不能超过100!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});
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
		url : '${request.contextPath}/comprehensive/quality/score/english/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg("保存成功", {
					offset: 't',
					time: 2000
				});
	 			itemShowList(2);
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

function delRow(own,mcPrefix,mcSuffix){
	$(own).parent().parent().remove();
	//showConfirmMsg('确定删除么？','提示',function(ii){
	// 	if(mcSuffix && mcSuffix!=''){
			//后台删除
	//		$.ajax({
	//		    url:'${request.contextPath}/comprehensive/quality/score/english/delete',
	//		    data: {'mcPrefix':mcPrefix,'mcSuffix':mcSuffix},  
	//		    type:'post',  
	//		    success:function(data) {
	//		    	var jsonO = JSON.parse(data);
	//		    	if(jsonO.success){
	//					$(own).parent().parent().remove();
	//		 		}else{
	//	 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
	//	 				isSubmit=false;
	//				}
	//		    }
	//		});
	//	}else{
	//		$(own).parent().parent().remove();
	//	}
   //  	layer.close(ii);
	//});
}

function changeItem(own) {
	var item = $(own).parent().parent().parent().next();
	item.find(".mc_class_prefix").val($(own).val());
}
</script>	
