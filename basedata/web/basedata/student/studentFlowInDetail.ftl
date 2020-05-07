<#import "/fw/macro/webmacro.ftl" as w>
<#if errorMsg?default('')!=''>
	<div class="widget-box" style="margin-top:80px;margin-bottom:80px;">
		<div class="widget-body">
			<div class="widget-main">
				<p class="alert alert-warning center">${errorMsg!}</p>
			</div>
		</div>
	</div>
	<div class="clearfix form-actions center">
		<@w.btn btnId="studentFlowIn-errorYes" btnClass="fa-check" btnValue="确定" />
	</div>
	<script>
		$('.page-content-area').ace_ajax('loadScripts', [], function() {
			$("#studentFlowIn-close").on("click",function(){
				layer.closeAll();
			});
			$("#studentFlowIn-errorYes").on("click",function(){
				$(".js-inSchool").unbind().addClass("disabled");
				layer.closeAll();
			});
		});
	</script>
<#else>
<!-- 弹出层开始 -->
<!--
<div class="layer-header">
	<h4 class="layer-title">转入操作</h4>
</div>
-->
<div class="row studentFlowDetail">
	<div class="clearfix">
		<div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">
		<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
			<label for="studentName" class="filter-name ">学生姓名：</label>
			<div class="filter-content ">
				<p>${studentDto.student.studentName!}</p>
				<input type="hidden" id="stu" name="studentFlow.studentId" value="${studentDto.student.id!}" />
				<input type="hidden" id="pin"  value="${pin!}" />
			</div>
		</div>
		<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
			<label for="" class="filter-name">身份证件号：</label>
			<div class="filter-content ">
				<p>${studentDto.student.identityCard!}</p>
			</div>
		</div>
		<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
			<label for="" class="filter-name">转入学校：</label>
			<#if isSch>
				<div class="filter-content ">
					<p class="school">${schoolName!}</p>
					<input type="hidden" value="${schoolId!}" id="sch" name="studentFlow.schoolId"/>
					<input type="hidden" value="${schoolName!}" id="schName" name="studentFlow.schoolName"/>
				</div>
			<#else>
				<div class="filter-content">
					<#if eduList?exists && eduList?size gt 0>
					<select class="form-control" id="edu" onChange="changeEdu();">
						<#list eduList as edu>
						<option value="${edu.id!}">${edu.unitName}</option>
						</#list>
					</select>
					<#else>
						<p class="form-control">${unitName!}</p>
					</#if>
					<input type="hidden" value="" name="studentFlow.schoolName" id="schName"/>
					<select class="form-control school" id="sch" onChange="changeSch();" name="studentFlow.schoolId">
						<option value="" selected >请选择学校</option>
						<#if schoolList?exists &&  schoolList?size gt 0>
						<#list schoolList as sch>
						<option value="${sch.id!}">${sch.unitName}</option>
						</#list>
						</#if>
					</select>
						
				</div>
			</#if>
		</div>
		<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
			<label for="" class="filter-name">转入班级：</label>
			<div class="filter-content">
				<input type="hidden" value="" name="studentFlow.schoolName" id="clsName"/>
				<select class="form-control clazz" id="cls" name="studentFlow.classId" onChange="clzzChange();">
					<option value="">请选择班级</option>
					<#if classList?exists && classList?size gt 0>
						<#list classList as clazz>
						<option value="${clazz.id!}">${clazz.className!}</option>
						</#list>
					</#if>
				</select>
			</div>
		</div>
		<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
			<label for="" class="filter-name">转入原因：</label>
			<div class="filter-content">
				<label>
					<input name="form-field-radio" type="radio" class="ace">
					<span class="lbl reason_rd"> 升学</span>
				</label>
				<label>
					<input name="form-field-radio" type="radio" class="ace">
					<span class="lbl reason_rd"> 复学</span>
				</label>
				<label>
					<input name="form-field-radio" type="radio" class="ace">
					<span class="lbl reason_rd"> 转学</span>
				</label>
				<label>
					<input name="form-field-radio" type="radio" class="ace">
					<span class="lbl reason_rd"> 出国</span>
				</label>
				<label>
					<input name="form-field-radio" type="radio" class="ace">
					<span class="lbl reason_rd" id="other_span"> </span>
					<input type="text" class="form-control" id="reason" maxLength="50" placeholder="其他">
				</label>
			</div>
		</div>
		</div>
	</div>
	<div class="clearfix form-actions center">
		<@w.btn btnId="studentFlowIn-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="studentFlowIn-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>
<script>
	function changeSch(){
		var schId = $("#sch").val();
		var schName = $("#sch").text();
		var url = "${request.contextPath}/basedata/studentFlowIn/clazz/options/99?unitId="+schId;
		if(schId != ""){
			getOptions(url,schId,"cls");
			$("#sch").find("option").each(function(){
				if($(this).attr("value")==schId){
					$("#schName").val($(this).text());
					return ;
				}
			});
		}
		else{
			$("#cls").html("<option value=\"\" selected >请选择班级</option>");
			$("#schName").val("");
		}
	}
	function changeEdu(){
		var eduId = $("#edu").val();
		var url = "${request.contextPath}/basedata/studentFlowIn/unit/options/2?unitId="+eduId;
		if(eduId != ""){
			getOptions(url,eduId,"sch");
			$("#cls").html("<option value=\"\" selected >请选择班级</option>");
		}
		else{
			$("#sch").html("<option value=\"\" selected >请选择学校</option>");
			$("#cls").html("<option value=\"\" selected >请选择班级</option>");
		}
	}
	
	function clzzChange(){
		var clsId = $("#cls").val();
		var clzzSelect = document.getElementById("cls");
		if(clsId != ""){
			$("#cls").find("option").each(function(){
				if($(this).attr("value")==clsId){
					$("#clsName").val($(this).text());
					return ;
				}
			});
		}
		else{
			$("#clsName").val("");
		}
	}
	
	function getOptions(url,unitid,type){
		$.ajax({
			url:url,
			type:'post',  
			cache:false,  
			contentType: "application/json",
			success:function(data) {
				if(type == "sch"){
					$("#sch").html(data);
				}
				else if(type == "cls"){
					$("#cls").html(data);
				}		 		
			 }
		});	
	}
	
	$(document).ready(function(){
		$("#reason").removeAttr("value").attr("readonly","true").val("");
	});
	
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		
		$(".reason_rd").each(function(){
			$(this).parent("label").on("click",function(){
				var spId = $(this).children("span").attr("id");
				if(spId == "" || typeof(spId) == "undefined"){
					$("#reason").parent("label").unbind().children("span").on("click",function(){
						$(this).next("input").removeAttr("readonly").attr("placeholder","其他");
					});
					$("#reason").unbind();
					$("#reason").attr("readonly","true").attr("placeholder","其他");
				}else{
					$("#reason").removeAttr("readonly").attr("placeholder","其他");
				}
			});
		});
		
		$("#studentFlowIn-close").on("click",function(){
			layer.closeAll();
		});
		
		
		$("#studentFlowIn-commit").on("click",function(){
			// 获取此控件下所有的可输入内容，并组织成json格式
			var studentId = $("#stu").val();
			var schoolId = $("#sch").val();
			var schoolName = $("#schName").val();
			var classId = $("#cls").val();
			var pin = $("#pin").val();
			var className = $("#clsName").val();
			var reason = "";
			if($('input[name="form-field-radio"]:checked').siblings().size()>0){
				reason = $.trim($($('input[name="form-field-radio"]:checked').siblings()[0]).text());
				if(reason == ''){
					reason = $.trim($("#reason").val());
				}
			}else{
				//layer.tips('请选择转入原因','#reason',{tipsMore: true});
				//swal({title: '',
	    		//	text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    		//});
			}
			var obj = {"studentId":studentId,"schoolId":schoolId,
						"schoolName":schoolName,"classId":classId,
						"className":className,"reason":reason,"pin":pin
					  };
					  
			$(this).addClass("disabled");
			var check1 = checkVal(reason);
			if(!check1){
			 	$(this).removeClass("disabled");
			 	return;
			}
			// 提交数据
		 	$.ajax({
			    url:'${request.contextPath}/basedata/studentFlowIn/detail/confirm',
			    data: JSON.stringify(obj),  
			    type:'post',  
			    cache:false,  
			    contentType: "application/json",
			    success:function(data) {
			    	var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
			 			//swal({title: "操作失败!",
		    			//text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
		    			//	$("#studentFlowIn-commit").removeClass("disabled");
		    			//});
		    			showMsgError(jsonO.msg,"操作失败!",function(index){
				  			layer.close(index);
			    			$("#studentFlowIn-commit").removeClass("disabled");
						});
			 		}
			 		else{
			 			// 需要区分移动端和非移动端返回处理不一样
		    			showMsgSuccess(jsonO.msg,"操作成功!",function(index){
				  			layer.close(index);
				  			$("#studentFlowIn-close").click();
			    			$(".js-inSchool_"+studentId).unbind().attr("disabled","disabled").addClass("disabled").text("转入成功");
			    			$("#studentFlowIn-commit").unbind();
						});
	    			}
			     }
			});
	 	});
	});
	
	
	function checkVal(reason){
		var clazzId = $("#cls").val();
		var schoolId = "";
		<#if !isSch>
			schoolId = $("#sch").val();
		</#if>
		var check = true;
		if(reason==''){
			layer.tips('原因不能为空', '#reason',{tipsMore: true});
			check = false;
		}
		if(getLength(reason)>20){
				layer.tips('原因不能超过20个字符(10个汉字)!', '#reason');
				check = false;
		}
		
		if(clazzId == ""){
			layer.tips('班级不能为空', '.clazz',{tipsMore: true});
			check = false;
		}
		<#if !isSch>
		if(schoolId == ""){
			layer.tips('学校不能为空', '.school',{tipsMore: true});
			check = false;
		}
		</#if>
		
		return check;
	}
</script>
</#if>
