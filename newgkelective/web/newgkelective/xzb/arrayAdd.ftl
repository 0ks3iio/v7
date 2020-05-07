<script>
var oldName = "${arrayName!}";
var pass = true;
//名称错误结果信息
var checkMsg;

function modiName(obj,val){
	var newName = $.trim(val);
	if(newName == oldName){
		pass = true;
		return;
	}
	if(newName==''){
		layer.tips('名称不能为空！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		pass = false;
		checkMsg = '名称不能为空！';
		return;	
	}
	if(getLength(newName)>80){
		layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		pass = false;
		checkMsg = '名称内容不能超过80个字节（一个汉字为两个字节）！';
		return;	
	}
	
	var id=$("#choosearrayId").val();
	if(id==""){
		//后台只做验证，不进行修改
		id="create";
	}
	$.ajax({
		url:'${request.contextPath}/newgkelective/xzb/saveName',
		data: {'arrayId':id,'arrayName':newName},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				pass = true;
	 		}else{
	 			layer.tips(jsonO.msg,$(obj), {
					tipsMore: true,
					tips: 3
				});
				pass = false;
				checkMsg = jsonO.msg;
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

</script>

<form id="arrayFormClass">
	<input type="hidden" name="id" id="choosearrayId" value="${newGkArray.id!}"/>
	<input type="hidden" name="gradeId" value="${newGkArray.gradeId!}"/>
	<div class="box box-default">
		<div class="box-header">
			<h3 class="box-title" style="width:50%;" >
				<i class="fa fa-edit" title="点击标题可编辑" data-toggle="tooltip" data-placement="bottom"></i>
				<input style="width:85%;" type="text" onblur="modiName(this,this.value)" name="arrayName" id="arrayName"  class="table-input" value="${arrayName!}">
			</h3>
		</div>
		<div class="box-header">
			<h3 class="box-caption">排课对象范围</h3>
			<a href="javascript:" class="btn btn-blue pull-right lessonTime-add" onclick="addGradeClass();">新增</a>
		</div>
		<div class="box-body">		
			<ul class="divideUI">
				<#if divideList?exists && divideList?size gt 0>
				<#list divideList as item>
				<li class="item-li divideLi${item.id}"  item-id="${item.id}">
				<input type="hidden" id="gradeName_${item.id}" value="${item.gradeName!}">
				<table width="100%" id="table${item.id}">
					<td valign="top" width="25" style="padding-top: 8px;">
						<label><input type="radio" class="wp" name="divideId" <#if newGkArray.divideId?default('')==item.id>checked="checked"</#if> value="${item.id}"><span class="lbl"></span></label>
					</td>
					<td>
						<div class="tt item-name" onclick="$(this).parent().parent().find('.wp').click();">
							${item.divideName!}
							<a class="ml10 color-blue dv-detail-btn" item-id="${item.id}" href="javascript:" onClick="deleteDivide(this)">删除</a>
							<a class="ml10 color-blue dv-detail-btn" item-id="${item.id}" href="javascript:" onClick="showJxb(this);">班级详情</a>
						</div>
					</td>
				</table>
				</li>
				</#list>
				<#else>
				<li class="item-li "  item-id="">
				暂无可用数据
				
				</li>
				</#if>
			</ul>
		</div>
	</div>		
	
	<div class="box box-default">
		<div class="box-header">
			<h3 class="box-caption">排课特征</h3>
			<a href="javascript:" class="btn btn-blue pull-right lessonTime-add" onclick="addLessonTable();">新增</a>
		</div>
		<div class="box-body">
			<ul id="lessonTimeUl" class="item-ul">
			暂无可用数据
			</ul>
		</div>
	</div>
</form>

<div class="layer layer-gradeClassAdd">
	<div class="layer-content" style="height:360px;overflow-y:auto;">
		<div class="filter">
			<div class="filter-item" style="width:100%;">
				<span class="filter-name">是否同步教学班：</span>
				<div class="filter-content">
					<label>
						<input type="checkbox" name="useJxb" value="1" class="wp wp-switch"  onclick="switchSemester();">
						<span class="lbl"></span>
					</label>
				</div>
			</div>
			<div class="filter-item jxbParam">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyearSearch" id="acadyearSearch" data-title="学年" class="form-control" style="width:188px;" onchange="serarchBySemester()">
						<#if acadyearList?exists && acadyearList?size gt 0>
					     	<#list acadyearList as item>
					     		<option value="${item!}" <#if acadyear?default('')== item >selected</#if> >${item!}学年</option>
					     	</#list>
					     <#else>
					     	<option value="">--未设置--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item jxbParam">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semesterSearch" id="semesterSearch" data-title="学期" class="form-control" style="width:188px;"  onchange="serarchBySemester()">
						${mcodeSetting.getMcodeSelect('DM-XQ',semester?default('0'),'0')}
					</select>
				</div>
			</div>
			
		</div>
	
		<table class="" width="100%">
			<tr>
				<td class="text-right" valign="top"></td>
				<td>
					<#if gradeList?exists && gradeList?size gt 0>
						<#list gradeList as grade>
						<label class="mb10 mr10 w155 ellipsis">
						
						<input type="radio" class="wp" name="addGradeId" value="${grade.id!}">
						<span class="lbl"> ${grade.gradeName!}</span>
						</label>
						</#list>
					</#if>
				</td>
			</tr>
		</table>
	</div>
</div>

<div class="navbar-fixed-bottom opt-bottom">
    <a class="btn btn-blue save" href="javascript:" id="arrangeLessonId" onclick="saveArray(1)">保存排课方案</a>
    <a class="btn btn-blue save" href="javascript:" id="arrangeLessonId" onclick="saveArray(0)">自动排课</a>
    <span class="color-blue" id="arraymess">
	</span>
</div>
<script src="${request.contextPath}/static/newgkelective/arrayEdit.js"></script>
<script>
var ownMakeName="";
<#if newGkArray.id?default('')==''>
	ownMakeName='${arrayName!}';
</#if>
//返回按钮
function backArrayIndex(){
	var url =  '${request.contextPath}/newgkelective/xzb/index/page?useMaster=1';
	$("#showList").load(url);
}
showBreadBack(backArrayIndex, false, '新增排课');

function addGradeClass(){
    if ($("input[name='addGradeId']").length == 1) {
        saveGradeClass($("input[name='addGradeId']").eq(0).val());
        return;
    }
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '选择年级',
		area: ['600px','500px'],
		btn: ['确定', '取消'],
		scrollbar:false,
		btn1:function(){
			var gradeId=$("input[name='addGradeId']:checked").val();
			var acadyear=$("#acadyearSearch").val();
			var semester=$("#semesterSearch").val();
			var useJxb=$("input[name='useJxb']:checked").val();
			
			if(!gradeId){
				layer.msg('请选择年级！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
			}else if(!acadyear){
				layer.msg('请选择学年！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
			}else if(!semester){
				layer.msg('请选择学期！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
			}else{
				saveGradeClass(gradeId,acadyear,semester,useJxb);
			}
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-gradeClassAdd')
	});
}
var isGradeClass=false;
function saveGradeClass(gradeId,acadyear,semester,useJxb){
	if(isGradeClass){
		return;
	}
	isGradeClass=true;
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/newgkelective/xzb/saveGradeClass',
		data: {'gradeId':gradeId,'acadyear':acadyear,'semester':semester,'useJxb':useJxb},
		type:'post',
		dataType:'JSON',
		success:function(data) {
			isGradeClass=false;
			layer.close(ii);
			if(!data.success){
				layerTipMsg(data.success,"操作失败",data.msg);
			}else{
				layer.closeAll();
				//ul加上一行
				refreshEdit("1");
			}
			
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
	
}


var isDeleteDivide=false;
function deleteDivide(obj){
	var deleteDivideId=$(obj).attr("item-id");
	if(isDeleteDivide){
		return;
	}
	isDeleteDivide=true;
	showConfirmMsg('确认删除吗？','提示',function(){
		
		$.ajax({
			url:'${request.contextPath}/newgkelective/xzb/deleteDivide',
			data: {'divideId':deleteDivideId},
			type:'post',
			dataType:'JSON',
			success:function(data) {
				layer.closeAll();
				if(!data.success){
					layerTipMsg(data.success,"操作失败",data.msg);
				}else{
					//ul加上一行
					//刷新
					refreshEdit("1");
					//$(".divideUI").find(".divideLi"+deleteDivideId).remove();
				}
				isDeleteDivide=false;
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		
		
		
	},function(){
		isDeleteDivide=false;
		layer.closeAll();
	});
	
	
}




var oldDivideId="";
var nowDivideId = '';

var lessonArrangeId='${newGkArray.lessonArrangeId?default('')}';


$(function(){
	//radio的事件
	initRedio();
	//initDvDetail();
	//initModiName();
	<#if newGkArray.divideId?default('')!=''>
		var divideId=$("input[name='divideId']:checked").val();
		if(divideId){
			findByDivideId(divideId);
		}
	</#if>
})	

function refreshEdit(useMaster){
	var arrId = '${newGkArray.id!}';
	if(arrId ==''){
	   var url = '${request.contextPath}/newgkelective/xzb/addArray/page?divideId='+nowDivideId+getArrayParams();
   }else{
	   var url =  '${request.contextPath}/newgkelective/xzb/editArray/page?arrayId='+arrId;
   }
   url += "&useMaster="+useMaster;
   $("#showList").load(url);
}



function initRedio(){
	//input[type=radio]
	$('.wp').off('change').on('change', function(){
		var option = $(this).attr('name');
		var optionValue = $(this).attr('value');
		if("divideId"!=option){
			$('[name=' + option + ']').parents("td").siblings().children(".tt").show().next().hide();
			if($(this).prop('checked') === true){
				$(this).parents("td").siblings().children(".tt").hide().next().show();
			}
		}
		if("divideId"==option){
			if(oldDivideId==""){
				oldDivideId=optionValue;
				findByDivideId(optionValue);
			}else{
				if(oldDivideId==optionValue){
					return;
				}else{
					oldDivideId=optionValue;
					findByDivideId(optionValue);
				}
			}	
		}else if("lessonArrangeId"==option){
			lessonArrangeId=optionValue;
		}
	})
}

function getArrayParams(){
	return "&lessArrayId="+lessonArrangeId;
}

function initDvDetail(){
<#--
	$('.dv-detail-btn').off('click').on('click', function(){
		var itemId = $(this).attr('item-id');
		var url = "${request.contextPath}/newgkelective/"+itemId+"/divideClass/resultClassList?fromArray=1&arrayId=${newGkArray.id!}"+getArrayParams();
		$("#showList").load(url);
	});
-->
}

function initLtDetail(){
	$('.detail-btn').off('click').on('click', function(){
		var itemId = $(this).attr('item-id');
		var liId = $(this).attr('li-id');
		var url = "${request.contextPath}/newgkelective/xzb/gradeArrange/edit?divideId="+nowDivideId+"&arrayId=${newGkArray.id!}&arrayItemId="+itemId+"&toLi="+liId+getArrayParams();;
		$("#showList").load(url);
	});
}

function initModiName(){
	$(".modify-name").each(function(){
		//$(this).children('div').length>0
		if($(this).find('.modify-name-layer').length > 0){
			return;
		}
		var tn = $(this).parents('td').find('.tt').text();
		var itemId = $(this).parents('.item-li').attr('item-id');
		var dc = ''
		if($(this).hasClass('divide-modi')){
			dc = 'divide-modi';
		}
		var modifyNameLayer = '<div class="modify-name-layer hidden" id="modiv'+itemId+'"style="width:350px;">\
						<h5>修改名称</h5>\
						<p><input type="text" class="form-control" placeholder="请输入名称" value="'+tn+'"></p>\
						<div class="text-right" item-id="'+itemId+'"><button class="btn btn-sm btn-white modi-cancel">取消</button><button class="btn btn-sm btn-blue modi-ok ml10 '+dc+'">确定</button></div>\
					</div>';
		$(this).append(modifyNameLayer);
	});

	$(".modify-name a").off('click').click(function(e){
		console.log(11);
		e.preventDefault();
		var tn = $(this).parents('td').find('.tt').text();
		$(this).parents('td').find(".form-control").val(tn);
		$(this).next().removeClass('hidden').show();
		if($(this).children().length === 1){
		}
	});	
	
	$('.modi-cancel').off('click').on('click', function(e){
		e.preventDefault();
		var itemId = $(this).parent().attr('item-id');
		$("#modiv"+itemId).addClass('hidden');
	});
	
	$('.modi-ok').off('click').on('click', function(e){
		e.preventDefault();
		var newName = $(this).parents('.modify-name-layer').find('.form-control').val();
		var oldName = $(this).parents('td').find('.tt').text();
		var itemId = itemId = $(this).parent().attr('item-id');
		var isDivide = $(this).hasClass('divide-modi');
		var rev = modifyName(newName,oldName,itemId,isDivide);
	});
}

var isClick=false;
function modifyName(newName,oldName,arrayItemId,isDivide){
	if(isClick){
		return false;
	}
	isClick=true;
	var obj = this.event.target;
	var nn = $.trim(newName);
	if(nn==''){
		isClick=false;
		layer.tips('名称不能为空！',$(obj).parent().prev(), {
				tipsMore: true,
				tips: 3
			});
		$(obj).focus();
		return false;	
	}
	if(nn==oldName){
		isClick=false;
		$("#modiv"+arrayItemId).addClass('hidden');
		return true;
	}
	
	var result = false;
	if(!isDivide){
		if(getLength(nn)>80){
			isClick=false;
			layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj).parent().prev(), {
					tipsMore: true,
					tips: 3
				});
			$(obj).focus();
			return false;	
		}
		$.ajax({
			url:'${request.contextPath}/newgkelective/'+arrayItemId+'/gradeArrange/changName',
			data: {'itemName':newName},
			type:'post',
			dataType:'JSON',
			success:function(data) {
		 		result = modiSuccess(data,arrayItemId,oldName,newName,obj);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	} else {
		if(getLength(nn)>50){
			isClick=false;
			layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！',$(obj).parent().prev(), {
					tipsMore: true,
					tips: 3
				});
			$(obj).focus();
			return false;	
		}
		$.ajax({
			url:'${request.contextPath}/newgkelective/${newGkArray.gradeId!}/goDivide/saveName',
			data: {'divideId':arrayItemId,'divideName':newName},
			type:'post',
			dataType:'JSON',
			success:function(data) {
		 		result = modiSuccess(data,arrayItemId,oldName,newName,obj);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	return result;
}

function modiSuccess(data,itemId,oldName,newName,obj){
	isClick=false;
	if(data.success){
		layer.closeAll();
	  	layer.msg(data.msg, {
				offset: 't',
				time: 2000
			});
		$('#li'+itemId).find('.item-name').each(function(){
				$(this).html(newName);
				$(this).text(newName);
			});
		$("#modiv"+itemId).addClass('hidden');
		return true;	
	}else{
		obj.value=oldName;
		layer.tips(data.msg,$(obj).parent().prev(), {
			tipsMore: true,
			tips: 3
		});
		return false;
	}
}

function initDel(){
	$('.del-btn').off('click').on('click', function(){
		var delj = this;
		showConfirmMsg('确认删除记录吗？','提示',function(){
			layer.closeAll();
			delObj(delj);
		},function(){
			layer.closeAll();
		});
	})
}

var isDel=false;
function delObj(delObj){
	if(isDel){
		return;
	}
	isDel=true;
	var itemId = $(delObj).attr('item-id');
	if($(delObj).hasClass('place-del')){
		deletePlItem(itemId);
	} else if($(delObj).hasClass('lesson-del')){
		deleteLsItem(itemId);
	}
	isDel=false;
}
var useMaster = "";
function findByDivideId(divideId){
	nowDivideId = divideId;
	var arrName=$("#arrayName").val();
	
	if(ownMakeName!=""){
		if(arrName.indexOf(ownMakeName)>-1){
			var gradeName=$("#gradeName_"+divideId).val();
			arrName=ownMakeName+"（"+gradeName+"）";
			$("#arrayName").val(arrName);
		}
		
		
	}
	
	
	$.ajax({
		url:"${request.contextPath}/newgkelective/goArrange/chosenItemByDivideId",
		data:{"divideId":divideId,"useMaster":useMaster},
		dataType: "json",
		success: function(data){
			var htmlTest1="暂无可用数据";
			var jsonArr=data.value;
			$("#placeUl").html(htmlTest1);
			$("#lessonTimeUl").html(htmlTest1);
			useMaster = "";
			if(jsonArr.length>0){
				for(var i=0;i<jsonArr.length;i++){
					var arrData=jsonArr[i];
					var arrType=arrData.type;
					var arrDataList=arrData.list;
					if(arrDataList.length>0){
						if("04"==arrType){
							$("#lessonTimeUl").html(toMakeLi(arrDataList,"lessonArrangeId"));
						}
					}
				}
				initRedio();
				initDel();
				initModiName();
				initLtDetail();
				if(lessonArrangeId!=''){
					$("input[name='lessonArrangeId']").parents("td").siblings().children(".tt").show().next().hide();
					if($("input[name='lessonArrangeId']").length>0){
						$("input[name='lessonArrangeId']").each(function(){
							var vv=$(this).val();
							if(vv==lessonArrangeId){
								//选中
								$(this).click();
								$(this).parents("td").siblings().children(".tt").hide().next().show();
								return;
							}
						})
					}
					//lessonArrangeId="";
				}
			}
		}
	});
}

function toMakeLi(arrDataList,optionName){
	var htmlTest="";
	for(var j=0;j<arrDataList.length;j++){
		var item=arrDataList[j];
		htmlTest=htmlTest+'<li id="li'+item.id+'" class="item-li" item-id="'+item.id+'"><table width="100%"><td valign="top" width="25" style="padding-top: 8px;">'
				+'<label><input type="radio" class="wp" name="'+optionName+'" value="'+item.id+'"><span class="lbl"></span></label>'
				+'</td><td><div class="tt item-name" onclick="$(this).parent().prev().find(\'.wp\').click();" style=" line-height: 38px;">'+item.name+'</div>'
				+'<div class="box box-primary" style="display: none;">'
				+'<div class="box-header-blue clearfix">'
				+'<div class="float-left item-name">'+item.name+'</div>'
				+'<div class="float-right">';
		if('lessonArrangeId'==optionName){
			htmlTest=htmlTest+'<a class="color-blue detail-btn" item-id="'+item.id+'" li-id="01" href="javascript:">年级特征</a>'
					+'<span class="color-lightblue"> | </span>'
					+'<a class="color-blue detail-btn" item-id="'+item.id+'" li-id="02" href="javascript:">课程特征</a>'
					+'<span class="color-lightblue"> | </span>'
					+'<a class="color-blue detail-btn" item-id="'+item.id+'" li-id="05" href="javascript:">班级特征</a>'
					+'<span class="color-lightblue"> | </span>'
					+'<a class="color-blue detail-btn" item-id="'+item.id+'" li-id="03" href="javascript:">教师特征</a>'
					+'<span class="color-lightblue"> | </span>'
					+'<a class="color-blue detail-btn" item-id="'+item.id+'" li-id="04" href="javascript:">课表设置</a>'
					+'<span class="color-lightblue"> | </span>'

					+'<div class="modify-name position-relative" style="display:inline;">'
					+	'<a class="color-blue position-relative" href="javascript:void(0);">修改名称</a>'
					+'</div>'
					
					+'<span class="color-lightblue"> | </span>'
					+'<a class="color-blue position-relative" href="javascript:copyItem(\''+item.id+'\')">复制排课特征</a>'
					+'<span class="color-lightblue"> | </span>'
					+'<a class="color-blue del-btn lesson-del" item-id="'+item.id+'" href="javascript:">删除</a></div></div>'
					+'<div class="box-body"><table class="table table-bordered table-striped table-hover"><thead><tr>';
			var	itemValue=arrDataList[j].itemValue;
			var heads = '<th class="text-center">周课时/科目教师数</th>';
			var bodys = '<th class="text-center">数量</th>';
			for(var k=0;k<itemValue.length;k++){
				heads+=('<th class="text-center">'+itemValue[k].name+'</th>');
				bodys+=('<td class="text-center">'+itemValue[k].num+'</td>');
			}
			htmlTest=htmlTest+heads+'</tr></thead><tbody><tr>'+bodys;
			htmlTest=htmlTest+'</tr></tbody></table>';		
		}
		htmlTest=htmlTest+'<div class="color-999" style="position:relative;">创建时间：'+item.creatTime+'<span style="position:absolute;right:0px;color:red;">'+ item.norange +'</span></div></div></div></td></table></li>';				
	}
	if(htmlTest==""){
		htmlTest="暂无可用数据";
	}
	return 	htmlTest;		
}

	//新增课表
	var isSubmit = false;
	function addLessonTable(){
		var divide_id = nowDivideId;
		if(!divide_id){
			layerTipMsg(false,"失败","请先选择排课对象");
			return;
		}
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var url =  '${request.contextPath}/newgkelective/xzb/gradeArrange/edit?arrayId=${newGkArray.id!}'+'&divideId='+divide_id+getArrayParams();
		$("#showList").load(url);
	}

	function hasSolution(a){
		var solutionNum = $(a).parent("h4").next("ul").children("li").length;
		if(solutionNum > 0)
			return true;
		else
			return false;
	}
	
	function setItemByDivide(tep, event){
		var divideId=$("input[name='divideId']:checked").val();
		if(!divideId){
			layerTipMsg(false,"失败","请先选择年级");
			return;
		}
		
		var ele =  event.target;
		var hasSolutionItem = hasSolution(ele);
		
		var arrayId=$("#choosearrayId").val();
		var url="";
		if("1"==tep){
			if(hasSolutionItem)
				url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/index/page?arrayId='+arrayId;
			else
  				 url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/eidt?arrayId='+arrayId;
		}
		else if("4"==tep){
			if(hasSolutionItem){
				//url='${request.contextPath}/newgkelective/'+divideId+'/gradeArrange/index?arrayId='+arrayId;
				url='${request.contextPath}/newgkelective/'+divideId+'/gradeArrange/index?arrayId='+arrayId;
			}else{
				//url = '${request.contextPath}/newgkelective/'+divideId+'/lessonTimeSetting/list/page?arrayId='+arrayId;
				url = '${request.contextPath}/newgkelective/'+divideId+'/gradeArrange/edit?arrayId='+arrayId;
			}
		}
		
		if(url!=""){
			$("#showList").load(url);
		}
	}
	
	function checkMyArrayForm(){
		var divideId=$("input[name='divideId']:checked").val();
		if(!divideId){
			layerTipMsg(false,"保存失败","请选择排课对象");
			return false;
		}
		
		var leArrangeId=$("input[name='lessonArrangeId']:checked").val();
		if(!leArrangeId){
			layerTipMsg(false,"保存失败","请选择排课特征方案");
			return false;
		}
		
		return true;
		
	}
	
	var isSubmit=false;
	function saveArray(justSave){
		if(isSubmit){
			$(".save").addClass("disabled");
			return;
		}
		
		if(!pass){
			layer.tips(checkMsg,$("#arrayName"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		
		if($("#arrangeLessonId").hasClass("disabled")){
			isSubmit=true;
			return;
		}
		isSubmit=true;
		$(".save").addClass("disabled");

		var check = checkMyArrayForm();
	    if(!check){
	        $(".save").removeClass("disabled");
	        isSubmit=false;
	        return;
	    }
	    $("#arraymess").html('<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">正在保存排课设置数据...');
	    isSubmit=true;
		var options = {
			url : "${request.contextPath}/newgkelective/xzb/saveArray",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
				isSubmit = false;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$(".save").removeClass("disabled");
				    $("#arraymess").html('');
		 			return;
		 		}else{
		 			layer.closeAll();
		 			if("success"==jsonO.msg){
		 				layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
						backArrayIndex();
		 			}else if(justSave == 0){
		 				arrangeLesson(jsonO.msg);
		 			}else{
						backArrayIndex();
		 			}
					
				  	
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#arrayFormClass").ajaxSubmit(options);
	}
	
	
	var isArrangeLesson=false;
	function arrangeLesson(arrayId){
		$("#choosearrayId").val(arrayId);
		if(isArrangeLesson){
			$("#arrangeLessonId").addClass("disabled");
			return;
		}
		//if($("#arrangeLessonId").hasClass("disabled")){
		//	isArrangeLesson=true;
		//	return;
		//}
		isArrangeLesson=true;
		//$("#arrangeLessonId").addClass("disabled");
		$("#arraymess").html('<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">开始排课...');
		arrangeLessonNoTeacher2(arrayId);
	}
	
	var isScricle=false;
	function arrangeLessonNoTeacher2(arrayId){
		$.ajax({
			url:"${request.contextPath}/newgkelective/"+arrayId+"/arrayLesson/autoArrayLessonNoTeacher",
			data:{"arrayId":arrayId},
			dataType: "json",
			success: function(data){
				if(data.stat=="success"){
					
					backArrayIndex();
					
	 			}else if(data.stat=="error"){
	 				backArrayIndex();//显示在首页 防止多次排课保存数据
	 				//isArrangeLesson=false;
	 				//$("#arraymess").html(data.message);
	 			}else{
	 				//循环访问结果
	 				if(isScricle){
	 					$("#arraymess").html(data.message+"...");
	 					arrangeLessonNoTeacher2(arrayId);
	 				}else{
	 					backArrayIndex();
	 				}
	 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}

function copyItem(arrayItemId){
	var itemName = $('#li'+arrayItemId).find('div.item-name').html();
	showConfirmMsg('确认复制<br>'+itemName+'？','提示',function(){
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/newgkelective/'+arrayItemId+'/gradeArrange/copyFeature',
			type:'post',
			dataType:'JSON',
			success:function(data) {
				layer.close(ii);
		 		if(data.success){
		 			layer.closeAll();
				  	layer.msg(data.msg, {
							offset: 't',
							time: 2000
						});
					useMaster = "1";
					setTimeout(refreshNoDivideInfo,500);
					//refreshNoDivideInfo();
		 		}else{
		 			alert('复制失败');
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){layer.close(ii);}
		});
	},function(){
		layer.closeAll();
	});
}
function refreshNoDivideInfo(){
	var divideId=$("input[name='divideId']:checked").val();
	if(divideId){
		findByDivideId(divideId);
	}
}
function switchSemester(){
	$(".jxbParam").toggle();
}
function showJxb(obj){
	var currDivideId =$(obj).attr("item-id");
	var url = "${request.contextPath}/newgkelective/xzb/"+currDivideId+"/divideResult/index";
	$("#showList").load(url);
}
$(function(){
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});	
		$(".jxbParam").hide();
});
</script>