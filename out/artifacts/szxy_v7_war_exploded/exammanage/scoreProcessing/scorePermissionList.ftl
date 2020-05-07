<form id="limitForm">
<input type="hidden" name="examId" value="${examId!}"/>
<input type="hidden" name="subjectId" value="${subjectId!}"/>
<div class="mylimitDiv">
<table class="table table-bordered table-striped table-hover" id="limitTable">
	<thead>
		<tr>
			<th class="text-center" width="30%;">录分人员</th>
			<th class="text-center" >添加行政班</th>
			<th class="text-center" width="20%;">操作</th>
		</tr>
	</thead>
	<tbody>
	<#if (limitList?exists && limitList?size>0)>
	<#list limitList as item>
		<tr>
			<td class="tdteacher">
				<input type="hidden" value="${item.id!}"  name="emLimitList[${item_index}].id">
				<select class="form-control teacher-class" vtype="selectOne" name="emLimitList[${item_index}].teacherId" id="teacherId_${item_index}" nullable="false">
	                <#if teacherList?exists && (teacherList?size>0)>
			           <#list teacherList as item2>
				          <option value="${item2.id!}" <#if item2.id == item.teacherId>selected</#if>>${item2.teacherName!}</option>
			           </#list>
			         <#else>
			           	<option value="">暂无数据</option>	
		            </#if>
                </select>
			</td>
			<td class="pos-rel tdclass">
				<select multiple vtype="selectMore" nullable="false" name="emLimitList[${item_index}].classIds" id="classIds_${item_index}" class="form-control chosen-select clazz-class" data-placeholder="未选择" >
					<#if clazzList?exists && (clazzList?size>0)>
			           <#list clazzList as item2>
				          <option value="${item2.id!}" 
			          			<#if item.classIds?exists>
				                   <#list item.classIds as key>					                                  
		                               <#if key == item2.id >selected</#if>
		                            </#list>
				                 </#if>
					        >${item2.classNameDynamic!}</option>
			           </#list>
			        <#else>
			        	
		            </#if>   
				</select>
			</td>
			<td class="text-center"><a class="table-btn color-red js-deleTeacher" href="javascript:;" onClick="doDelete(this,'${item.id!}')">删除</a></td>
		</tr>
	</#list>
	</#if>
		<tr>
		    <td class="text-center" colspan="3">
			    <a class="table-btn js-addLimit" href="javascript:;" onclick="addLimit()">+新增</a>
			</td>
		</tr>	
	</tbody>
</table>
</div>
</form>
<script>
var index=0;
<#if limitList?exists >
	index=${limitList?size};
</#if>
var teacherhtml='';
var classhtml='';
<#if teacherList?exists && (teacherList?size>0)>
  <#list teacherList as item2>
  		teacherhtml=teacherhtml+'<option value="${item2.id!}">${item2.teacherName!}</option>';
  </#list>
<#else>
	teacherhtml=teacherhtml+'<option value="">暂无数据</option>';
</#if>

<#if clazzList?exists && (clazzList?size>0)>
   <#list clazzList as item2>
      classhtml=classhtml+'<option value="${item2.id!}">${item2.classNameDynamic!}</option>';
   </#list>
</#if>   

$(function(){
	//初始化多选
	initChosenMore("#limitForm");
	initChosenOne("#limitForm");
});	

function addLimit(){
	var $form_group_item='<tr><td class="tdteacher"><input type="hidden" value=""  name="emLimitList['+index+'].id" >'
		+'<select class="form-control teacher-class" vtype="selectOne"  name="emLimitList['+index+'].teacherId" id="teacherId_'+index+'" nullable="false">'
		+teacherhtml+'</select></td>'
		+'<td class="pos-rel tdclass"><select multiple vtype="selectMore" nullable="false" name="emLimitList['+index+'].classIds" id="classIds_'+index+'" class="form-control chosen-select clazz-class" data-placeholder="未选择" >'	             
		+classhtml+'</select></td><td class="text-center">'	
		+'<a class="color-red del" href="javascript:"  onClick="doDelete(this)">删除</a></td></tr>';
	$('#limitTable').find('tr:last').before($form_group_item);
	
	$('#teacherId_'+index).chosen({
		
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:false, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
		//resize the chosen on window resize

	$('#classIds_'+index).chosen({
		allow_single_deselect:true,
		disable_search_threshold: 10
	}); 

	index++;
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		$(window).trigger('resize.chosen')
	})
}
var isSubmit=false;
function save(){
	if(isSubmit){
		return;
	}
	var check = checkValue('.mylimitDiv');
	if(!check){
		isSubmit=false;
		return;
	}
	check=mycheckTeacher();
	if(!check){
		isSubmit=false;
		return;
	}
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/scorePermission/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				showPermission('${subjectId!}');
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
	$("#limitForm").ajaxSubmit(options);
} 

function mycheckTeacher(){
	var returnObj=true;
	var i=0;
	var teacherStr="";
	$(".teacher-class").each(function(){
		var $tr=$(this).parent().parent();
		if($(this).val()!=""){
			if(teacherStr.indexOf($(this).val()) > 0 ){
				layer.tips("录分人员中有重复,选择了同个老师", $tr.find(".tdteacher"), {
					tipsMore: true,
					tips:3		
				});
				returnObj=false;
    			return false;
			}
			teacherStr=teacherStr+","+$(this).val();
			
			var clazzids=$tr.find('td .clazz-class').val();
			if(clazzids==null){
				layer.tips("行政班不为空", $tr.find(".tdclass"), {
					tipsMore: true,
					tips:3		
				});
				
				returnObj=false;
				return false;
			}
			i++;
		}else{
			//通过参数 nullable控制
			returnObj=false;
			return false;
		}
	});
	if(returnObj && i==0){
		layerTipMsg(false,"失败","没有添加数据");
		returnObj=false;
	}
	return returnObj;
}

function doDelete(own,limitId){
	if(limitId && limitId!=''){
		//后台删除
		$.ajax({
		    url:'${request.contextPath}/exammanage/scorePermission/delete',
		    data: {'id':limitId},  
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
</script>