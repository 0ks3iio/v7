<#import "/fw/macro/webmacro.ftl" as w>
<#if errorMessage?default("") != "">
<p class="alert alert-warning">${errorMessage!}</p>
<#else>
<#if infoMessage?default("") != "">
<p class="alert alert-info">${infoMessage!}</p>
</#if>
<div class="filter table-wrapper">
	<div class="row">
	<#if students?exists && students?size gt 0>
		<#list students as studentDto>
		<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
			<div class="filter-item block">
				<label for="" class="filter-name">学生姓名：</label>
				<div class="filter-content">
					<p class="student-name">${studentDto.student.studentName!}</p>
				</div>
			</div>
			<div class="filter-item block">
				<label for="" class="filter-name">身份证件号：</label>
				<div class="filter-content">
					<p class="student-identityCard">${studentDto.student.identityCard!}</p>
				</div>
			</div>
			<div class="filter-item block">
				<label for="" class="filter-name">现学校：</label>
				<div class="filter-content">
					<p class="school-name">${studentDto.schoolName!}</p>
				</div>
			</div>
			<div class="filter-item block">
				<label for="" class="filter-name">现班级：</label>
				<div class="filter-content">
					<p class="class-name">${studentDto.className!}</p>
				</div>
			</div>
			<button style="margin-bottom:20px;" class="btn btn-darkblue btn-lg js-leaveSchool width-180" dataVal="${studentDto.student.id}">转出</button>
		</div>
		</#list>
		<#else>
			<p class="alert alert-warning">未找到该学生!</p>
		</#if>
	</div>
</div>
<#if students?exists && students?size gt 0>
<@w.pagination  container="#a .searchResult" pagination=pagination page_index=3/>
</#if>


<!-- page specific plugin scripts -->
<!-- 弹出层开始 -->
<div class="studentOutDetail" style="display:none">
	<div class="layui-layer-title" style="cursor: move;" move="ok">转出操作</div>
		<div class="filter clearfix">
			<div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">
			<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
				<label for="" class="filter-name">学生姓名：</label>
				<div class="filter-content">
					<p id="layer-student-name"></p>
				</div>
			</div>
			<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
				<label for="" class="filter-name">身份证件号：</label>
				<div class="filter-content">
					<p id="layer-student-identityCard"></p>
				</div>
			</div>
			<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
				<label for="" class="filter-name">现学校：</label>
				<div class="filter-content">
					<p id="layer-school-name"></p>
				</div>
			</div>
			<div class="filter-item block col-xs-10 col-sm-10 col-md-10 col-lg-10">
				<label for="" class="filter-name">现班级：</label>
				<div class="filter-content">
					<p id="layer-class-name"></p>
				</div>
			</div>
			<div class="filter-item block reason-content col-xs-10 col-sm-10 col-md-10 col-lg-10">
				<label for="" class="filter-name ">转出原因：</label>
				<div class="filter-content  tips-reason" style="width:260px;">
					<label>
						<input name="form-field-radio" type="radio" class="ace  check-radio">
						<span class="lbl reason_rd"> 休学</span>
					</label>
					<label>
						<input name="form-field-radio" type="radio" class="ace check-radio">
						<span class="lbl reason_rd"> 退学</span>
					</label>
					<label>
						<input name="form-field-radio" type="radio" class="ace check-radio">
						<span class="lbl reason_rd"> 转学</span>
					</label>
					<label>
						<input name="form-field-radio" type="radio" class="ace check-radio">
						<span class="lbl reason_rd"> 出国</span>
					</label>
					<label>
						<input name="form-field-radio" type="radio" class="ace other check-radio" >
						<span class="lbl reason_rd" id="other_span_"> </span>
						<input type="text" id="otherReason_" class="other-reason form-control " maxLength="50" placeholder="其他">
					</label>
				</div>
				</div>
		</div>
	</div>
	<div class="clearfix form-actions center">
		<@w.btn btnId="layer-submit" btnClass="fa-check  " btnValue="确定" />
		<@w.btn btnId="layer-cancel" btnClass="fa-times  " btnValue="取消" />
	</div>
	</div>
</div><!-- 弹出层结束 -->
<script type="text/javascript">
	var isSubmit = false;
	$(document).ready(function(){
		$(".other-reason").removeAttr("value").attr("readonly","true").val("");
	});
	
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$(".check-radio").on("click",function(){
			if($(this).hasClass("other")){
				$("#otherReason_").removeAttr("readonly","true").attr("placeholder","其他");
			}else{
				$("#otherReason_").attr("readonly","true").attr("placeholder","其他").val("");
			}
		});
	
	});
	$(".js-leaveSchool").bind("click",function(){
		var eventBtn = $(this);
		var studentId = $(this).attr("dataVal");
		var studentIdentityCard  = $(this).parent('div').find('.student-identityCard').text();
		var studentName  = $(this).parent('div').find('.student-name').text();
		var schoolName = $(this).parent('div').find('.school-name').text();
		var className = $(this).parent('div').find('.class-name').text();
		if(studentName){
			$("#layer-student-name").html(studentName);
		}
		if(studentIdentityCard){
			$("#layer-student-identityCard").html(studentIdentityCard);
		}
		if(schoolName){
			$("#layer-school-name").html(schoolName);
		}
		if(className){
			$("#layer-class-name").html(className);
		}
		$('input[name="form-field-radio"]:checked').each(function(){
			$(this).get(0).checked=false;
		});
		$('.other-reason').val('');
		var layerIndex = layerDiv('500px', '450px', $('.studentOutDetail'), '', 0);
		var _closeBtn = "<span class=\"layui-layer-setwin close-div-self\"><a class=\"layui-layer-ico layui-layer-close layui-layer-close1\" href=\"javascript:;\"></a></span>";
			$(".studentOutDetail").parent("div").css("height","500px");
			$(".studentOutDetail").parent("div").next().remove();
			$(".studentOutDetail").parent("div").append(_closeBtn);
			//$(".studentOutDetail").next().remove();
		$(".close-div-self").unbind('click').bind("click",function(){
			layer.close(layerIndex);
			$(".close-div-self").remove();
		});
		$("#layer-cancel").unbind('click').bind("click",function(){
			layer.close(layerIndex);
			$(".close-div-self").remove();
		});
		$("#layer-submit").unbind('click').bind("click",function(){
			if(isSubmit){
				return;
			}
			var reason = "";
			if($('input[name="form-field-radio"]:checked').siblings().size()>0){
				reason = $($('input[name="form-field-radio"]:checked').siblings()[0]).text();
				reason = $.trim(reason);
			}
			if(reason==''){
				reason = $(".other-reason").val();
				if($.trim(reason) == ''){
					layer.tips('请选择转出原因!', '.other-reason');
					return;
				}
			}
			if(getLength(reason)>20){
				layer.tips('转出原因不能超过20个字符(10个汉字)!', '#otherReason_');
				return;
			}
			var o = new Object();
			o.studentId = studentId;
			o.schoolName = schoolName;
			o.className = className;
			o.reason = reason;
			$(".layer-submit").attr("disabled","disabled").addClass("disabled");
			isSubmit = true;
			$.ajax({
			    url:'${request.contextPath}/basedata/studentFlowOut/searchResult/leaveSchool',
			    data: $.param(o),  
			    type:'get',  
			    cache:false,  
			    contentType: "application/json",
			    success:function(data) {
			    	var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
						showMsgError(jsonO.msg,'转出失败!',function(index){
							layer.close(index);
							isSubmit = false;
							$(".layer-submit").attr("disabled","").removeClass("disabled");
						});
			 		}
			 		else{
			 			showMsgSuccess(jsonO.msg,'转出成功!',function(index){
							layer.close(index);
							isSubmit = false;
							$(".layer-submit").attr("disabled","").removeClass("disabled");
							layer.close(layerIndex);
							$(".studentOutDetail").next().remove();
							eventBtn.attr("disabled","disabled").addClass("disabled").html("已转出");
						})
	    			}
			     }
			});
		});
		
		
	});


</script>
</#if>