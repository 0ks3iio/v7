var contextPath='';
var roundsId = '';

/**
 * 导航的选中样式变化
 * @param clsArr 要变化的步骤class样式，需带.
 * @param isAdd 添加/去除completed样式，true添加，false去除
 */
function dealActiveClsType(clsArr, isAdd){
	/**
	if(!clsArr || clsArr.length < 1){
		return;
	}
	for(var i=0;i<clsArr.length;i++){
		var obj = $(clsArr[i]);
		if(isAdd){
			if(!$(obj).hasClass('completed')){
				$(obj).addClass('completed');
			}
		} else {
			$(obj).removeClass('completed');
		}
	}
	*/
}

/**
 * 导航的是否可点击样式变化，有点击样式的步骤可以通过点击导航直接跳转至该步骤
 * @param clsArr 要变化的步骤class样式，需带.
 * @param isAdd 添加/去除active样式，true添加，false去除
 * 
 */
function dealClickType(clsArr, isAdd){
	if(!clsArr || clsArr.length < 1){
		return;
	}
	for(var i=0;i<clsArr.length;i++){
		var obj = $(clsArr[i]);
		if(isAdd){
			if(!$(obj).hasClass('completed')){
				$(obj).addClass('completed');
			}
		} else {
			$(obj).removeClass('completed');
		}
	}
}
/**
 * 手动排班
 */
function toPerArrange(){
	$('.perarrange-step').addClass('active').siblings('li').removeClass('active');
	var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/list/page';
	$("#groupList").load(url);
}
/**
 * 手动排班结果
 */
function toGroupResult(){
	$('.group-result-step').addClass('active').siblings('li').removeClass('active');
	dealClickType(['.perarrange-step','.group-result-step'],true);
	var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/group/resultList/page';
	$("#groupList").load(url);
}
/**
 * 未安排学生
 */
function toUnArrange(){
	$('.un-arrange-step').addClass('active').siblings('li').removeClass('active');
	dealClickType(['.perarrange-step','.group-result-step','.un-arrange-step'],true);
	var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/group/unArrange/page';
	$("#groupList").load(url);
}
/**
 * 单科自动排班
 */
function toSingle(){
	$('.single-step').addClass('active').siblings('li').removeClass('active');
	dealClickType(['.perarrange-step','.group-result-step','.un-arrange-step','.single-step'],true);
	var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/group/singleList/page';
	$("#groupList").load(url);
}
/**
 * 全部开班结果
 */
function toAllResult(){
	$('.all-result-step').addClass('active').siblings('li').removeClass('active');
	dealClickType(['.perarrange-step','.group-result-step','.un-arrange-step','.single-step','.all-result-step'],true);
	var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/group/allList/page';
	$("#groupList").load(url);
}

/**
 * 表单数据是否有修改
 * @returns {Boolean}
 */
function checkValChanged(){
	var arrs = $('#editForm input .form-control');
	if(arrs){
		var flag = false;
		arrs.each(function(){
			var inType = $(this).attr('type');
			var value = '';
			if(inType  == "radio"){
				value = $("input[name='" + $(this).attr('name') + "']:checked").val();
				if(value != 1){
					flag = true;
					return true;
				}
			} else if(inType == 'text'){
				value = $.trim($(this).val());
				var oldVal = $.trim($(this).attr('oldValue'));
				if(value != oldVal){
					flag = true;
					return true;
				}
			}
		});
		return flag;
	}
	return false;
}

/**
 * 删除班级，成功后列表刷新调用js方法reloadListData
 * @param clsId
 */
function doDelCls(clsId,clsName){
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("确定要删除<span style='color:red'>"+clsName+"</span>班级吗？",options,function(){
		var ii = layer.load();
		$.ajax({
			url:contextPath+'/gkelective/'+roundsId+'/openClassArrange/class/delete',
			data:{'clsId':clsId},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
				  	reloadListData();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});	
	},function(){});
}

function checkNum(allNum,clsMax,index, num){
	if(!/^\d+$/.test(num)){
		layerTipMsg(false,"信息","请输入整数");
		return -1;
	}
	num = parseInt(num);
	if(num>allNum){
		return 1;
	} else if(num == 0){
		layerTipMsg(false,"信息","每班人数最少为1，不能为0！");
		return -1;
	}
	var clsNum = parseInt(allNum/num);
	if(allNum%num != 0){
		clsNum++;
	}
	if(clsMax>0 && clsNum>clsMax){
		layerTipMsg(false,"信息","开班数不能大于同时段最大开班数("+clsMax+")，请修改每班人数！");
		return -1;
	}
	return clsNum;
}

function calClsNum(allNum,clsMax,index, val){
	allNum = parseInt(allNum);
	clsMax = parseInt(clsMax);
	var clsNum = checkNum(allNum,clsMax,index,val);
	if(clsNum >= 0){
		$('#'+index+'_num_claNum_td').html(clsNum);
		$('#'+index+'_num_claNum').val(clsNum);
	}
}