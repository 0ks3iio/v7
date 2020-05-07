<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<#import "/fw/macro/treemacro.ftl" as treemacro>
<form id="subForm">
<input type="hidden" name="infoYear" value="${teaexamInfo.infoYear?default(0)}"/>
<input type="hidden" name="infoType" value="${teaexamInfo.infoType?default(0)}"/>
<input type="hidden" name="unitId" value="${unitId!}"/>
<input type="hidden" name="id" value="${teaexamInfo.id!}"/>
<input type="hidden" name="creationTime" value="${teaexamInfo.creationTime!}"/>
<#assign examEdit = true />
<#assign infoStr = '考试' />
<#if teaexamInfo.infoType?default(0) !=0>
<#assign infoStr = '培训' />
<#assign examEdit = false />
</#if>
<div class="box box-default">
<div class="box-body">
	<div class="form-horizontal" id="myForm">
		<div class="form-group">
			<h3 class="col-sm-2 control-label no-padding-right bold"><#if !teaexamInfo.id?exists>新增<#else>编辑</#if>教师${infoStr!}</h3>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">${infoStr!}名称</label>
			<div class="col-sm-4">
				<input type="text" class="form-control js-file-name width-1of1" maxlength="60" nullable="false" placeholder="请输入${infoStr!}名称" id="examName" name="examName" value="${teaexamInfo.examName!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">报名时间</label>
			<div class="col-sm-4">					
				<div class="input-group float-left" style="width: 40%;">
				<#if teaexamInfo.registerBegin?exists>
					<input id="registerBegin" autocomplete="off" name="registerBegin" class="form-control datetimepicker" type="text" nullable="false"  placeholder="报名开始时间" value="${teaexamInfo.registerBegin?string("yyyy-MM-dd")!}"/>
				<#else>
				    <input id="registerBegin" autocomplete="off" name="registerBegin" class="form-control datetimepicker" type="text" nullable="false"  placeholder="报名开始时间" value=""/>
				</#if>
					<span class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</span>
				</div>
				<span class="float-left mt7 mr10 ml10"> 至 </span>
				<div class="input-group float-left" style="width: 40%;">
				<#if teaexamInfo.registerEnd?exists>
					<input id="registerEnd" autocomplete="off" name="registerEnd" class="form-control datetimepicker" type="text" nullable="false"  placeholder="报名结束时间" value="${teaexamInfo.registerEnd?string("yyyy-MM-dd")!}"/>
				<#else>
				    <input id="registerEnd" autocomplete="off" name="registerEnd" class="form-control datetimepicker" type="text" nullable="false"  placeholder="报名结束时间" value=""/>
				</#if>
					<span class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</span>
				</div>
			</div>
		    <div class="col-sm-4 control-tips"></div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">${infoStr!}时间范围</label>
			<div class="col-sm-4">					
				<div class="input-group float-left" style="width: 40%;">
				<#if teaexamInfo.examStart?exists>
					<input id="examStart" autocomplete="off" name="examStart" class="form-control datetimepicker" type="text" nullable="false"  placeholder="考试开始时间" value="${teaexamInfo.examStart?string("yyyy-MM-dd")!}"/>
				<#else>
				    <input id="examStart" autocomplete="off" name="examStart" class="form-control datetimepicker" type="text" nullable="false"  placeholder="考试开始时间" value=""/>
				</#if>
					<span class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</span>
				</div>
				<span class="float-left mt7 mr10 ml10"> 至 </span>
				<div class="input-group float-left" style="width: 40%;">
				<#if teaexamInfo.examEnd?exists>
					<input id="examEnd" autocomplete="off" name="examEnd" class="form-control datetimepicker" type="text" nullable="false"  placeholder="考试结束时间" value="${teaexamInfo.examEnd?string("yyyy-MM-dd")!}"/>
				<#else>
				    <input id="examEnd" autocomplete="off" name="examEnd" class="form-control datetimepicker" type="text" nullable="false"  placeholder="考试结束时间" value=""/>
				</#if>
					<span class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</span>
				</div>
			</div>
		    <div class="col-sm-4 control-tips"></div>
		</div>
		<#if examEdit>
		<div class="form-group timePlaceDiv">
			<label for="" class="col-sm-2 control-label no-padding-right">考试科目</label>
			<div class="col-sm-6">
				<table id="addTimePlace" class="table table-bordered">
					<thead>
					     <tr>
					        <th>科目</th>
					        <th>学段</th>
					        <th>考试开始时间</th>
					        <th>考试结束时间</th>
					        <th>操作</th>
					     </tr>
				    </thead>
					<tbody id="tb2">
						 <#if subjectList?exists && subjectList?size gt 0>
						     <#list subjectList as item>
						         <tr>
						             <td><input type="text" class="form-control" style="width:120px;" id="teaexamSubjectList${item_index!}subjectName" name="teaexamSubjectList[${item_index!}].subjectName" nullable="false" value="${item.subjectName!}"></td>
						             <input type="hidden" name="teaexamSubjectList[${item_index!}].id" value="${item.id!}"/>
						             <input type="hidden" name="teaexamSubjectList[${item_index!}].examId" value="${item.examId!}"/>
						             <input type="hidden" name="teaexamSubjectList[${item_index!}].creationTime" value="${item.creationTime!}"/>
						             <td>
						                 <select name="teaexamSubjectList[${item_index!}].section" id="teaexamSubjectList${item_index!}section" nullable="false" maxlength="200" class="form-control" style="width:120px">
		                                    <option value="">--请选择--</option>'
		                                    <option value="0" <#if item.section==0>selected="selected"</#if>>学前</option>
		                                    <option value="1" <#if item.section==1>selected="selected"</#if>>小学</option>
		                                    <option value="2" <#if item.section==2>selected="selected"</#if>>初中</option>
		                                    <option value="3" <#if item.section==3>selected="selected"</#if>>高中</option>		                       
	                                     </select>
						             </td>
						             <td>
						                  <div class="input-group float-left" style="width: 180px;">
		                                     <input id="teaexamSubjectList${item_index!}startTime" autocomplete="off" name="teaexamSubjectList[${item_index!}].startTime" class="form-control datetimepicker3" type="text" nullable="false"  placeholder="开始时间" value="${item.startTime?string("yyyy-MM-dd HH:mm")!}" />
		                                     <span class="input-group-addon">
			                                    <i class="fa fa-calendar"></i>
		                                     </span>
	                                      </div>
						             </td>
						             <td>
						                  <div class="input-group float-left" style="width: 180px;">
		                                     <input id="teaexamSubjectList${item_index!}endTime" autocomplete="off" name="teaexamSubjectList[${item_index!}].endTime" class="form-control datetimepicker2" type="text" nullable="false"  placeholder="结束时间" value="${item.endTime?string("yyyy-MM-dd HH:mm")!}" />
		                                     <span class="input-group-addon">
			                                    <i class="fa fa-calendar"></i>
		                                     </span>
	                                      </div>
						             </td>
						             <td><a class="color-red" href="javascript:void(0);" onclick="delSubRow(this);">删除</a></td>
						         </tr>
						     </#list>
						 </#if>
					</tbody>
					<thead>
				         <tr>
				            <td colspan="6" class="text-center"><a class="js-add js-addTimeRow" id="js-addTimeRow" href="javascript:" onclick="addSubRow();">+新增</a></td>
				         </tr>
				    </thead>
				</table>
			</div>
		</div>
		</#if>								
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">${infoStr!}单位</label>
			<div class="col-sm-8">
				<div class="col-sm-6 no-padding-left">
					
						<p class="tree-name">
							所有单位
						</p>
						<@treemacro.unitForSubInsetTree height="450" checkEnable=true onCheck="chooseSch2" notRelate=true/>
																	
				</div>
				<div class="col-sm-6 no-padding-left">
					<div class="tree">
						<p class="tree-name">
							已选
						</p>
                        <table id="addTimePlace" class="table">
					        <tbody id='tb'>		
					            <#if schIdList?exists && schIdList?size gt 0>
						           <#list schIdList as item>
						               <tr id="${item!}" class="atr">
						                   <td>${schNameMap[item]!}</td>
						                   <input type="hidden" name="schIdList[${item_index!}]" value="${item!}"/>
						                   <td style="white-space: nowrap"><a class="color-red" href="javascript:void(0);" onclick="delTr('${item!}');">删除</a></td>
						               </tr>
						           </#list>
						        </#if>					    
					        </tbody>
				        </table>
					</div>
				</div>
			</div>
	   </div>
	   <#if !examEdit>
	   <div class="form-group">
		    <label class="col-sm-2 control-label no-padding-right">培训项目</label>
			<div class="col-sm-9">
				<div class="textarea-container">
	    			<textarea name="trainItems" id="trainItems" cols="30" rows="10" maxlength="500" class="form-control js-limit-word2">${teaexamInfo.trainItems!}</textarea>
	    			<span style="right:15px;">500</span>
	    		</div>
			</div>
		</div>
		</#if>
	   <div class="form-group">
		    <label class="col-sm-2 control-label no-padding-right"><#if examEdit>内容<#else>培训要求</#if></label>
			<div class="col-sm-9">
				<div class="textarea-container">
	    			<textarea name="description" id="description" cols="30" rows="10" maxlength="500" class="form-control js-limit-word">${teaexamInfo.description!}</textarea>
	    			<span style="right:15px;">500</span>
	    		</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-8 col-sm-offset-2">
			    <a class="btn btn-blue js-added" onClick="examSave('1');">保存</a>
				<a class="btn btn-blue js-added" onClick="examSave('2');">发布</a>
				<a class="btn btn-ringblue" onClick="searchList();">取消</a>
			</div>
		</div>
	</div>
</div>
</div>
</form>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/js/bootstrap-datetimepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script type="text/javascript">
$(function(){
	showBreadBack(searchList,true,"考试设置");
	
	// 时间
	$('.datetimepicker').datepicker({
		language: 'zh-CN',
    	format: 'yyyy-mm-dd',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
	$('.datetimepicker2').datetimepicker({
		language: 'zh-CN',
    	format: 'yyyy-mm-dd hh:ii',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
	$('.datetimepicker3').datetimepicker({
		language: 'zh-CN',
    	format: 'yyyy-mm-dd hh:ii',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
	var titleLength = getLength($('#description').val());
	$('#description').next().text(titleLength +'/'+500+'(字节)');
	$('.js-limit-word').on('keyup',function(){
		var max = 500;
		$(this).next().text(getLength(this.value) +'/'+max+'(字节)');
	});
	
	var titleLength = getLength($('#trainItems').val());
	$('#trainItems').next().text(titleLength +'/'+500+'(字节)');
	$('.js-limit-word2').on('keyup',function(){
		var max = 500;
		$(this).next().text(getLength(this.value) +'/'+max+'(字节)');
	});
	
	
	var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
	if(zTree11 == null){
	    setTimeout(function(){
	       zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
	       <#if schIdList?exists && schIdList?size gt 0>
		      <#list schIdList as item>
                 var treeNode2 = zTree11.getNodeByParam("id",'${item!}');
                 treeNode2.checked = true;
                 zTree11.updateNode(treeNode2);
		     </#list>
	       </#if>
	    }, 2000);
	}
})	


var schIndex = 0;
<#if schIdList?exists && schIdList?size gt 0>
    schIndex = ${schIdList?size}-1; 
</#if>
function chooseSch(event, treeId, treeNode){
   var html = '';
   if(treeNode.checked){
       if (treeNode.isParent) {
          var result = '';
          var childrenNodes = treeNode.children;
          if (childrenNodes) {
             for (var i = 0; i < childrenNodes.length; i++) {
                html = html + '<tr id='+childrenNodes[i].id+'><td>'+childrenNodes[i].name+'</td><input type="hidden" name="schIdList['+schIndex+']" value='+childrenNodes[i].id+'><td style="white-space: nowrap"><a class="color-red" href="javascript:void(0);" onclick="delTr(\''+childrenNodes[i].id+'\');">删除</a></td></tr>'
                schIndex++;
             }
          }
       }else{
          html = '<tr id='+treeNode.id+'><td>'+treeNode.name+'</td><input type="hidden" name="schIdList['+schIndex+']" value='+treeNode.id+'><td style="white-space: nowrap"><a class="color-red" href="javascript:void(0);" onclick="delTr(\''+treeNode.id+'\');">删除</a></td></tr>';
          schIndex++;
       }
       $('#tb').append(html);
   }else{
       if (treeNode.isParent) {
          var result = '';
          var childrenNodes = treeNode.children;
          if (childrenNodes) {
             for (var i = 0; i < childrenNodes.length; i++) {
                $('#'+childrenNodes[i].id).remove();
             }           
          }
       }else{
          $('#'+treeNode.id).remove();
       }
   }  
}

function chooseSch2(event, treeId, treeNode){
   $(".atr").remove();
   schIndex = 0;
   var html = '';
   var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
   var nodes=zTree11.getCheckedNodes(true);
   for (var i = 0; i < nodes.length; i++) {
       html = html + '<tr id='+nodes[i].id+' class="atr"><td>'+nodes[i].name+'</td><input type="hidden" name="schIdList['+schIndex+']" value='+nodes[i].id+'><td style="white-space: nowrap"><a class="color-red" href="javascript:void(0);" onclick="delTr(\''+nodes[i].id+'\');">删除</a></td></tr>'
       schIndex++;
   }
   $('#tb').append(html);
}

function delTr(trId){ 
    var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
    var treeNode2 = zTree11.getNodeByParam("id",trId);
    treeNode2.checked = false;
    zTree11.updateNode(treeNode2);
    $('#'+trId).remove();
}

var subIndex = 0;
<#if subjectList?exists && subjectList?size gt 0>
    subIndex = ${subjectList?size}; 
</#if>

function addSubRow(){
    var html = '<tr><td><input type="text" class="form-control" style="width:120px;" nullable="false" id="teaexamSubjectList'+subIndex+'subjectName" name="teaexamSubjectList['+subIndex+'].subjectName"></td><td>'
    +'<select name="teaexamSubjectList['+subIndex+'].section" nullable="false" id="teaexamSubjectList'+subIndex+'section" class="form-control" style="width:120px">'
		+'<option value="">--请选择--</option>'
		+'<option value="0">学前</option>'
		+'<option value="1">小学</option>'
		+'<option value="2">初中</option>'
		+'<option value="3">高中</option>'		                       
	+'</select>\
    </td><td>'
    +'<div class="input-group float-left" style="width: 180px;">'
		+'<input id="teaexamSubjectList'+subIndex+'startTime" autocomplete="off" name="teaexamSubjectList['+subIndex+'].startTime" class="form-control datetimepicker3" type="text" nullable="false"  placeholder="开始时间" value="" />'
		+'<span class="input-group-addon">'
			+'<i class="fa fa-calendar"></i>'
		+'</span>'
	+'</div>\
    </td><td>'
    +'<div class="input-group float-left" style="width: 180px;">'
		+'<input id="teaexamSubjectList'+subIndex+'endTime" autocomplete="off" name="teaexamSubjectList['+subIndex+'].endTime" class="form-control datetimepicker2" type="text" nullable="false"  placeholder="结束时间" value="" />'
		+'<span class="input-group-addon">'
			+'<i class="fa fa-calendar"></i>'
		+'</span>'
	+'</div>\
    </td><td><a class="color-red" href="javascript:void(0);" onclick="delSubRow(this)">删除</a></td></tr>';
    $('#tb2').append(html);
    $('.datetimepicker3').datetimepicker({
		language: 'zh-CN',
    	format: 'yyyy-mm-dd hh:ii',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
	$('.datetimepicker2').datetimepicker({
		language: 'zh-CN',
    	format: 'yyyy-mm-dd hh:ii',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
	subIndex++;
}

function delSubRow(obj){
    $(obj).parent().parent("tr").remove();
}

var isSubmit=false;
function examSave(state){	
    if(isSubmit){
    	isSubmit = true;
		return;
	}
	var check = checkValue('#subForm');
	if(!check){
		isSubmit=false;
		return;
	}
	var registerBegin = $('#registerBegin').val();
	var registerEnd = $('#registerEnd').val();
	if(registerBegin>registerEnd){
	    layerTipMsgWarn("提示","报名开始时间不能大于报名结束时间!");
		return;
	}
	var examStart = $('#examStart').val();
	var examEnd = $('#examEnd').val();
	if(examStart>examEnd){
		layerTipMsgWarn("提示","${infoStr!}开始时间不能大于${infoStr!}结束时间!");
		return;
	}
	
	if(registerEnd>=examStart){
	    layerTipMsgWarn("提示","报名结束时间不能大于或等于${infoStr!}开始时间!");
		return;
	}
	var msg = "";
	if(state == '1'){
	   msg = "保存";
	}else{
	   msg = "发布";
	}
	var options = {
		url : '${request.contextPath}/teaexam/examInfo/examInfoSave?state='+state,
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,msg+"失败",jsonO.msg);
		 		$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,msg+"成功",jsonO.msg);
				searchList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);	
}

function searchList(){
    url = "${request.contextPath}/teaexam/examInfo/index/page?year=${teaexamInfo.infoYear?default(0)}&type=${teaexamInfo.infoType?default(0)}";
    $(".model-div-show").load(url);
}
</script>