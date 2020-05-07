<#if canEdit>
<p class="text-right">
	<a href="javascript:" class="btn btn-blue js-newSet" onclick="newSet();">新增设置</a>
	<a href="javascript:" class="btn btn-green"  onclick="copySet();">复用当前设置</a>
</p>
</#if>
<form id="mysavediv">
	<!-- 表格开始 -->
	<div class="table-wrapper">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>等级比例</th>
					<th>分数</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if scoreConversionList?exists && (scoreConversionList?size>0)>
					<#list scoreConversionList as item>
						<tr>
							<td>
								<input type="hidden"  name="scoreConversionList[${item_index}].examId"  value="${examId!}">
								<input type="text" class="table-input balance_class" name="scoreConversionList[${item_index}].balance" id="balance_${item_index}" vtype = "number" nullable="false"  placeholder="请输入等级比例" value="${item.balance?number}" maxLength="7"></td>
							<td>
								<input type="text" class="table-input score_class" name="scoreConversionList[${item_index}].score" id="score_${item_index}" vtype = "number" nullable="false" placeholder="请输入分数" value="${item.score?number}" maxLength="7">
							</td>
							<td>
								<#if canEdit>
									 <a class="color-red" href="javascript:void(0);" onclick="delRow(this,'${item.id!}');">删除</a>
								</#if>
							</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div><!-- 表格结束 -->
</form>
<div class="row">
	<div class="col-xs-12 text-right">
		<#if canEdit>
			<a href="javascript:void(0);" class="btn btn-blue" onclick="save();">保存</a>
		</#if>
	</div>
</div>
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
<#if scoreConversionList?exists && (scoreConversionList?size>0)>
	index=${scoreConversionList?size};
</#if>

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
	$(".balance_class").each(function(){
		var r = $(this).val().match(reg);
		if(r==null){
			f=true;
			layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
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
	});	
	if(f){
		isSubmit=false;
		return;
	}
	
	
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/scoremanage/hierarchy/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layerTipMsg(data.success,"成功",data.msg);
				searchList();
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
function newSet(){
	 var $form_group_item='<tr><td><input type="hidden"  name="scoreConversionList['+index+'].examId"  value="${examId!}">'
			+'<input type="text" class="table-input balance_class" name="scoreConversionList['+index+'].balance" id="balance_'+index+'" vtype = "number" nullable="false"  placeholder="请输入等级比例" maxLength="7">'								
		+'</td><td><input type="text" class="table-input score_class" name="scoreConversionList['+index+'].score" id="score_'+ index +'" vtype = "number" nullable="false" placeholder="请输入分数" maxLength="7">'			
	    +'</td><td><a class="color-red" href="javascript:void(0);" onclick="delRow(this)">删除</a>'
	    +'</td></tr>';
	$("#mysavediv").find('tbody').append($form_group_item);
	index=index+1;
}
function delRow(own,conId){

	if(conId && conId!=''){
		//后台删除
		$.ajax({
		    url:'${request.contextPath}/scoremanage/hierarchy/delete',
		    data: {'id':conId},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.success){
					$(own).parent().parent().remove();
		 		}else{
	 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 				isSubmit=false;
				}
		    }
		});
	}else{
		$(own).parent().parent().remove();
	}
	
}
function copySet(){
	var url = "${request.contextPath}/scoremanage/hierarchy/copySet/page?examId=${examId!}";
	indexDiv = layerDivUrl(url,{title: "复用当前设置",width:400,height:439});
}
 
</script>