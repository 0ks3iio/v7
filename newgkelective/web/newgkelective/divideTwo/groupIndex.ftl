<#--<a href="javascript:" class="page-back-btn gotoDivideIndex"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title"><#if newDivide.openType=='03'>文理科分层教学模式 — 语数外独立分班
				<#else>文理科分层教学模式 — 语数外跟随文理组合分班</#if></h3>
	</div>
	<input type="hidden" id="galleryful" value="${newDivide.galleryful?default(0)}"/>
	<#assign index=0>
	<div class="box-body">
		<form id="openTwoForm">
		<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as dto>
				<h3>${dto.allgroupName!}组合</h3>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>组合名称</th>
							<th>分层参考成绩</th>
							<th>总人数</th>
							<th>未安排人数</th>
							<th>班级数设置<small>（每班可容纳${newDivide.galleryful?default(0)}人）</small></th>
						</tr>
					</thead>
					<tbody>
						<#assign exDtoList=dto.exList>
						<#if exDtoList?exists && exDtoList?size gt 0>
							<#list exDtoList as exdto>
								<input type="hidden" name="exList[${index}].id" value="${exdto.id!}">
								<input type="hidden" name="exList[${index}].groupType" value="${dto.groupType!}">
								<input type="hidden" name="exList[${index}].subjectType" value="${exdto.subjectType!}">
								<tr class="js-line">
									<td>${exdto.groupName!}</td>
									<td class="radioTd">
										<p class="js-layer-score js-layer-score-single">
											<label><input type="radio"  name="exList[${index}].hierarchyScore" <#if exdto.hierarchyScore?default('')=='1'>checked<#else><#if !isCanEdit>disabled</#if> </#if> class="wp" value="1"><span class="lbl"> 单科成绩</span></label>
										</p><br>
										<p class="js-layer-score js-layer-score-total">
											<label><input type="radio" name="exList[${index}].hierarchyScore" <#if exdto.hierarchyScore?default('')=='2'>checked<#else><#if !isCanEdit>disabled</#if></#if> class="wp" value="2"><span class="lbl"> 总成绩</span></label>
										</p>
										<span class="ff"></span>
									</td>
									<td><span class="js-total" data-max-total="${exdto.studentNum?default(0)}" >${exdto.studentNum?default(0)}</span></td>
									<td><span class="color-red js-surplus" data-surplus="${exdto.studentNum?default(0)}">${exdto.studentNum?default(0)}</span></td>
									<td height="135" class="sumTd">
										<div class="layer-setting layer-setting-three">
											<input type="hidden" class="classSumNum" name="exList[${index}].classSumNum" value=""/>
											<#assign classSumNumMap=exdto.classSumNumMap>
											<#if classSumNumMap?exists && classSumNumMap?size gt 0>
												<#if classSumNumMap['A']?exists>
													<p><strong>A：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumA" type="text" value="${classSumNumMap['A'][0]!}" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumA" type="text" value="${classSumNumMap['A'][1]!}" maxLength="5">名学生</p><br>
												<#else>
													<p><strong>A：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumA" type="text" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumA" type="text" maxLength="5">名学生</p><br>
												</#if>
												<#if classSumNumMap['B']?exists>
													<p><strong>B：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumB" type="text" value="${classSumNumMap['B'][0]!}" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumB" type="text" value="${classSumNumMap['B'][1]!}" maxLength="5">名学生</p><br>
												<#else>
													<p><strong>B：</strong><input <#if !isCanEdit>readOnly</#if> class="fform-control inline-block number js-grade classNumB" type="text" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumB" type="text" maxLength="5">名学生</p><br>
												</#if>
												<#if classSumNumMap['C']?exists>
													<p><strong>C：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumC" type="text" value="${classSumNumMap['C'][0]!}" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumC" type="text" value="${classSumNumMap['C'][1]!}" maxLength="5">名学生</p><br>
												<#else>
													<p><strong>C：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumC" type="text" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumC" type="text" maxLength="5">名学生</p><br>
												</#if>
											<#else>
												<p><strong>A：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumA" type="text" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumA" type="text" maxLength="5" >名学生</p><br>
												<p><strong>B：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumB" type="text" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumB" type="text" maxLength="5">名学生</p><br>
												<p><strong>C：</strong><input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-grade classNumC" type="text" maxLength="3">个班，<input <#if !isCanEdit>readOnly</#if> class="form-control inline-block number js-student stuNumC" type="text" maxLength="5">名学生</p>
											</#if>
											<span class="ff"></span>
										</div>
									</td>
								</tr>
								<#assign index=index+1>
							</#list>
						</#if>
					</tbody>
				</table>
			</#list>
		</#if>
		</form>
		<#--
		<div class="text-center">
		<a class="btn btn-long btn-blue <#if !isCanEdit>disabled</#if>" id="toOpenClass"  onclick="makeClass()">开始分班</a>
		 <span class="color-blue" id="showMessId">
		 	<#if !isCanEdit><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">
				正在分班中，请稍等．．．
			</#if>
		 </span>
		</div>
		-->
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-long btn-blue <#if !isCanEdit>disabled</#if>" id="toOpenClass"  onclick="makeClass()">开始分班</a>
	 <span class="color-blue" id="showMessId">
	 	<#if !isCanEdit><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">
			正在分班中，请稍等．．．
		</#if>
	 </span>
</div>
<script>
	$(function(){
		showBreadBack(gobackResult,false,"分班结果");
		initLeftStuNum();
		//返回
		<#--
			$(".gotoDivideIndex").on("click",function(){
				gobackResult();
			});
		-->
		
		//输入事件
		$('.js-line').each(function(){
			var $total = $(this).find('.js-total');	//总人数
			var $grade = $(this).find('.js-grade');	//班级
			var $student = $(this).find('.js-student');	//学生
			var $surplus = $(this).find('.js-surplus');	//未安排人数
			
			var total = parseInt($total.attr('data-max-total'));	//总人数
			var student=parseInt($("#galleryful").val());//单个班级容纳人数
			var grade = Math.ceil(total / student);	//最大可分班级
			var num = /^(\d+)$/;
			$total.attr('data-max-grade',grade);
			$grade.attr('data-max',grade);
			$student.attr('data-max',total);
			
			//输入班级数据
			$grade.on('keyup',function(){
				if ($(this).val() == '') {
					$(this).attr('data-val',0)
				} else{
					$(this).attr('data-val',$(this).val());
				};
				var $line = $(this).parents('.js-line');
				//var val = $(this).val();
				var val = $(this).attr('data-val');
				var grade_max = parseInt($(this).attr('data-max'));
				if(num.test(val)){ 
					//算出当前栏学生数
					var $myStudent = $(this).siblings('.js-student')
					var surplus_num = parseInt($line.find('.js-surplus').attr('data-surplus'));	//剩余学生数
					var student_num = val * student; //当前输入数值算出的学生数
					var my_num = $myStudent.val();
					if (my_num == '') {
						my_num = 0;
					}
					my_num = parseInt(my_num);
					var add_num = student_num - my_num;	//需要增加的学生数
					if (add_num < 0){
						$myStudent.val(student_num);
					} else{
						if (add_num > surplus_num){
							$myStudent.val(surplus_num + my_num);
						} else{
							$myStudent.val(student_num);
						};
					};
					//算出未安排人数
					var used_student = 0;
					$line.find('.js-student').each(function(){
						if ($(this).val() != '') {
							used_student += parseInt($(this).val());
						} 
					});
					var sur_student = total - used_student;
					$line.find('.js-surplus').text(sur_student).attr('data-surplus',sur_student);
					//处理学生最大可输入值
					$line.find('.js-student').each(function(){
						if ($(this).val() != '') {
							my_student = parseInt($(this).val());
						} else{
							my_student = 0;
						}
						my_student += sur_student;
						$(this).attr('data-max',my_student);
					});
				} else{
					//alert('格式错误！只能输入数字');
					layer.alert('格式错误：只能输入数字！');
				}
			});
			
			//输入学生数据
			$student.on('keyup',function(){
				if ($(this).val() == '') {
					//$(this).val('0');
					$(this).attr('data-val',0)
				} else{
					$(this).attr('data-val',$(this).val());
				};
				var $line = $(this).parents('.js-line');
				//var val = $(this).val();
				var val = $(this).attr('data-val');
				var student_max = parseInt($(this).attr('data-max'));
				if(num.test(val)){ 
					if (val > student_max) {
						//alert('最大可输入学生数为：' + student_max);
						layer.alert('最大可输入学生数为：' + student_max);
						$(this).val(student_max);
						$line.find('.js-surplus').text('0').attr('data-surplus',0);
					} else{
						//算出未安排人数
						var used_student = 0;
						$line.find('.js-student').each(function(){
							if ($(this).val() != '') {
								used_student += parseInt($(this).val());
							} 
						});
						var sur_student = total - used_student;
						$line.find('.js-surplus').text(sur_student).attr('data-surplus',sur_student);
						//处理学生最大可输入值
						$line.find('.js-student').each(function(){
							if ($(this).val() != '') {
								my_student = parseInt($(this).val());
							} else{
								my_student = 0;
							}
							my_student += sur_student;
							$(this).attr('data-max',my_student);
						});
					}
				} else{
					layer.alert('格式错误：只能输入数字！');
				}
			});
			
			
			
			
			
		});
				
	
	})
	
	//计算剩余人数
	function initLeftStuNum(){
		$(".js-line").each(function(){
			var allNum=$(this).find(".js-total").attr("data-max-total");//总人数
			var arrangeStuNum=0;
			$(this).find(".js-student").each(function(){
				var num=$(this).val();
				if(num!=""){
	    			arrangeStuNum=arrangeStuNum+parseInt(num);
	    		}
			})
			var leftNum=0;
	    	if(parseInt(allNum)>arrangeStuNum){
	    		leftNum=parseInt(allNum)-arrangeStuNum;
	    	}
	    	$(this).find(".js-surplus").text(leftNum+'').attr('data-surplus',leftNum);
	    });
	}
	
	
	//分层参考成绩必选一个验证
	function checkRadioTd(){
		var ff=false;
		$(".js-line").each(function(){
			var ss=$(this).find("input[type=radio]:checked").val();
			if(ss){
				//选择
			}else{
				//未选择
				layer.tips("请选择分层参考成绩", $(this).find(".ff"), {
					tipsMore: true,
					tips:3		
				});
				if(!ff){
					ff=true;
				}
				
			}
		})
		if(ff){
			return false;
		}
		return true;
	}
	//验证输入框是否符合都为正整数，不为空
	function checkNotNullAndInt(jsClass){
		var ff=false;
		$("."+jsClass).each(function(){
			var value=$(this).val();
			if ((!value || $.trim(value) == "")) {
				if(!ff){
					ff=true;
				}
				layer.tips("不能为空", $(this), {
					tipsMore: true,
					tips:3		
				});
			}else{
				if(!checkMyInt($.trim(value))){
					if(!ff){
						ff=true;
					}
					layer.tips("请输入整数", $(this), {
						tipsMore: true,
						tips:3		
					});
				}
			}
		})
		if(ff){
			return false;
		}
		return true;
	}
	
	function checkData(){
		//分班数
		if(!checkNotNullAndInt("js-grade")){
			return false;
		}
		//学生数
		if(!checkNotNullAndInt("js-student")){
			return false;
		}
		return true;
	}
	
	function checkMyInt(value){
		if (!/^\d+$/.test(value)) {
			return false;				
	    }
	    return true;
	}
	
	function checkAllStudentNum(){
		var ff=false;
	    $(".js-line").each(function(){
	    	var $total =$(this).find(".js-total").attr("data-max-total");//总人数
	    	
	    	var classNumA=$(this).find(".classNumA").val();
	    	var classNumB=$(this).find(".classNumB").val();
	    	var classNumC=$(this).find(".classNumC").val();
	    	
	    	var stuNumA=$(this).find(".stuNumA").val();
	    	var stuNumB=$(this).find(".stuNumB").val();
	    	var stuNumC=$(this).find(".stuNumC").val();
	    	
	    	//所有相加要等于总人数--防止直接取最后剩余人数 数据有误
	    	var allNum=parseInt($.trim(stuNumA))+parseInt($.trim(stuNumB))+parseInt($.trim(stuNumC));
	    	var allStuNum=parseInt($total);
	    	var leftNum=0;//剩余人数
	    	if(allStuNum!=allNum){
	    		if(!ff){
					ff=true;
				}
	    		layer.tips("班级设置的学生数总和要等于总人数", $(this).find(".sumTd").find(".ff"), {
					tipsMore: true,
					tips:3		
				});
				
				if(allStuNum>allNum){
					leftNum=allStuNum-allNum;
				}
	    	}else{
	    		var classSumNum="A:"+$.trim(classNumA)+":"+$.trim(stuNumA)+";B:"+$.trim(classNumB)+":"+$.trim(stuNumB)+";C:"+$.trim(classNumC)+":"+$.trim(stuNumC);
	    		$(this).find(".classSumNum").val(classSumNum);
	    	}
			$(this).find(".js-surplus").text(leftNum+'').attr('data-surplus',leftNum);
	    });
	    if(ff){
			return false;
		}
	    return true;
	}
	
	//验证提交的数据
	function checkMyFrom(){
		//分层参考成绩必选一个
		if(!checkRadioTd()){
			return false;
		}
		//验证输入框是否符合都为正整数，不为空
		if(!checkData()){
			return false;
		}
		//计算总人数是否符合
		if(!checkAllStudentNum()){
			return false;
		}
		return true;
	}
	

	var isSubmit=false;
	function makeClass(){
		if(isSubmit || $("#toOpenClass").hasClass("disabled")){
			isSubmit=true;
			$("#toOpenClass").addClass("disabled");
			return;
		}
		$("#toOpenClass").addClass("disabled");
		isSubmit=true;
		saveItem();
	}
	
	function saveItem(){
		//验证数据
		if(!checkMyFrom()){
			$("#toOpenClass").removeClass("disabled");
			isSubmit=false;
			return;
		}
		var options = {
			url : "${request.contextPath}/newgkelective/${newDivide.id!}/divideTwo/saveTwo",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit=false;
		 			$("#toOpenClass").removeClass("disabled");
		 			return;
		 		}else{
		 			layer.closeAll();
		 			if(jsonO.msg=="分班中"){
		 				//刷新这个页面
		 				layer.msg("已经在分班中", {
							offset: 't',
							time: 2000
						});
						gobackResultList('${newDivide.id!}');
		 			}else{
		 				layer.msg("设置数据保存成功", {
							offset: 't',
							time: 2000
						});
						//进入分班
		 				autoAllClass("${newDivide.id!}");
		 			}
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#openTwoForm").ajaxSubmit(options);
	}
	
	//保存后直接点击分班
	function autoAllClass(divideId){
		var text='<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">'
					+'正在分班中，请稍等．．．';
		$("#showMessId").html(text);
		//分班代码
		autoAllClass2(divideId,"1");
	}
	//之后可以用一段循环
	function autoAllClass2(divideId,indexStr){
		$.ajax({
			url:"${request.contextPath}/newgkelective/"+divideId+"/divideClass/autoDivideClass",
			data:{"divideId":divideId},
			dataType: "json",
			success: function(data){
				if(data.stat=="success"){
					//进入结果
					gobackResultList(divideId);
	 			}else if(data.stat=="error"){
	 				if(indexStr=="1"){
	 					//上次失败进入分班
	 					autoAllClass2(divideId,"0");
	 				}else{
	 					isSubmit=false;
		 				$("#toOpenClass").removeClass("disabled");
	 					$("#showMessId").html(data.message);
	 				}
	 				
	 			}else{
	 				//不循环访问结果--直接进入首页autoAllClass2()
	 				gobackResult(divideId);
	 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	function gobackResult(){
		var url =  '${request.contextPath}/newgkelective/${newDivide.gradeId!}/goDivide/index/page';
		<#if fromArray?default('') == '1'>
			<#if arrayId?default('')==''>
			   url = '${request.contextPath}/newgkelective/${newDivide.gradeId!}/goArrange/addArray/page?divideId=${newDivide.id!}&lessArrayId=${lessArrayId!}&plArrayId=${plArrayId!}';
		   <#else>
			   url = '${request.contextPath}/newgkelective/${newDivide.gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
		   </#if>
		</#if>		
		$("#showList").load(url);
	}
	
	function gobackResultList(divideId){
		var url =  '${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList';
		$("#showList").load(url);
	}
	
</script>