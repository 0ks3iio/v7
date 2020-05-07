<div class="explain explain-no-icon">
    <p>说明：学科成绩折算名次设置指，首先将学生的学科成绩按年级进行排名，然后按照名次段赋予折算分，最后会以折算分计算综合素质分。</p>
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
				<th>初三下</th>
				<th>高一上</th>
				<th>高一下</th>
				<th>高二上</th>
				<th>高二下</th>
				<th>高三上</th>
				<th>操作</th>
			</tr>
		</thead>
		
		<tbody>
			<#if compreParamInfoList?exists && (compreParamInfoList?size>0)>
				<#list compreParamInfoList as item>
					<tr>
						<td>
							<div class="input-group mc_main_class" style="width:200px">
							  <input type="text" readonly="true" onfocus="this.blur()" class="form-control mc_class mc_class_prefix" maxlength="3" id="mcPrefix_${item_index}" vtype="int" name="compreParamInfoList[${item_index}].mcPrefix" value="${item.mcPrefix}">
							  <span class="input-group-addon"><i class="fa fa-minus"></i></span>
							  <input type="text" class="form-control mc_class" maxlength="3" name="compreParamInfoList[${item_index}].mcSuffix" id="mcSuffix_${item_index}" vtype="int" value="${item.mcSuffix}" onblur="changeItem(this);">
							</div>
						</td>
						
						<td>
							<input  type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].scoreList[0]" value="${item.scoreList[0]}">
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].scoreList[1]" value="${item.scoreList[1]}">
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].scoreList[2]" value="${item.scoreList[2]}">
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].scoreList[3]" value="${item.scoreList[3]}">
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].scoreList[4]" value="${item.scoreList[4]}">
						</td>
						<td>
							<input type="text" class="form-control number score_class" name="compreParamInfoList[${item_index}].scoreList[5]" value="${item.scoreList[5]}">
						</td>
						<td><a class="color-red js-del" href="javascript:void(0);" onclick="delRow(this,${item.mcPrefix},${item.mcSuffix});">删除</a></td>
					</tr>
				</#list>
			</#if>
			<tr>
				<td class="text-center" colspan="8"><a class="js-addObject" href="">+新增</a></td>
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

$('.js-addObject').on('click', function(e){
	e.preventDefault();
	var mcPrefix = $('.mc_class').last().val();
	if(typeof(mcPrefix)=="undefined"){
		mcPrefix = 0;
	}
	
	var form_group_item='<tr><td><div class="input-group mc_main_class" style="width:200px">'
				+'<input type="text" readonly="true" onfocus="this.blur()" class="form-control mc_class mc_class_prefix" maxlength="3" name="compreParamInfoList['+index+'].mcPrefix" id="mcSuffix_'+index+'" vtype="int" value="'+mcPrefix+'" id="prefix"  >'
				+'<span class="input-group-addon"><i class="fa fa-minus"></i></span>'
				+'<input type="text"  class="form-control mc_class" maxlength="3" name="compreParamInfoList['+index+'].mcSuffix" id="mcSuffix_'+index+'" vtype="int" onblur="changeItem(this);"></div></td><td>'
				+'<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].scoreList[0]" ></td><td>'
				+'<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].scoreList[1]" ></td><td>'
				+'<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].scoreList[2]" ></td><td>'
				+'<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].scoreList[3]" ></td><td>'
				+'<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].scoreList[4]" ></td><td>'
				+'<input type="text" class="form-control number score_class" name="compreParamInfoList['+index+'].scoreList[5]" ></td><td>'
				+'<a class="js-del color-red" href="javascript:void(0);" onclick="delRow(this)">删除</a></td></tr>'
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
		url : '${request.contextPath}/comprehensive/quality/score/other/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg("保存成功", {
					offset: 't',
					time: 2000
				});
	 			itemShowList(1);
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
	//名次段不能有空缺 不建议直接删除后台数据
	$(own).parent().parent().remove();
	//showConfirmMsg('确定删除么？','提示',function(ii){
	// 	if(mcSuffix && mcSuffix!=''){
			//后台删除
	//		$.ajax({
	//		    url:'${request.contextPath}/comprehensive/quality/score/other/delete',
	//		    data: {'mcPrefix':mcPrefix,'mcSuffix':mcSuffix},  
	//		    type:'post',  
	//		    success:function(data) {
	//		    	var jsonO = JSON.parse(data);
	//		    	if(jsonO.success){
	//					$(own).parent().parent().remove();
	//		 		}else{
	//		 			layer.msg(data.msg, {offset: 't',time: 2000});
	//	 				isSubmit=false;
	//				}
	//		    }
	//		});
	//	}else{
	//		$(own).parent().parent().remove();
	//	}
    //	layer.close(ii);
	//});
	
}

function changeItem(own) {
	var item = $(own).parent().parent().parent().next();
	item.find(".mc_class_prefix").val($(own).val());
}

</script>