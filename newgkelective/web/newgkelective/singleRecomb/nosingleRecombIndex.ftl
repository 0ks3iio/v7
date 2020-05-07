<link rel="stylesheet" href="${request.contextPath}/static/components/jQuerycxColor/css/jquery.cxcolor.css">
<script src="${request.contextPath}/static/components/jQuerycxColor/js/jquery.cxcolor.js"></script>
<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<li class="active"><span><i>1</i>原行政班</span></li>
		<li><span><i>2</i>选考分层</span></li>
		<li><span><i>3</i>分选考班</span></li>
		<li><span><i>4</i>选考班结果</span></li>
		<li><span><i>5</i>分学考班</span></li>
		<li><span><i>6</i>学考班结果</span></li>
	</ul>
</div>
<div class="box box-default">
	<div class="box-body" id="divideContent">
		<div class="explain">
			<p>小提示：尽量通过合班方式，不要让同一个行政班同时开设物理，历史，否则容易造成排课时候场地不够。</p>
		</div>
		<table class="table table-striped table-bordered table-hover xzbTable" >
			<thead>
				<tr>
					<th>序号</th>
					<th>行政班</th>
					<th>总人数</th>
					<th>男</th>
					<th>女</th>
					<th>选物理人数</th>
					<th>选历史人数</th>
					<th>物理选考班</th>
					<th>历史选考班</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && (dtoList?size > 0)>
					<#list dtoList as dto>
						<tr>
							<td>${dto_index+1}</td>
							<td>${dto.className!}</td>
							<td>${dto.allStuNum?default(0)}</td>
							<td>${dto.boyNum?default(0)}</td>
							<td>${dto.girlNum?default(0)}</td>
							<td class="bg-success">${dto.courseNum1?default(0)}</td>
							<td class="bg-danger">${dto.courseNum2?default(0)}</td>
							<td>
								<#if dto.relaJxb1?exists>
										<#if dto.relaJxb1[3]?default('')!=''>
											<a class="merge-btn" href="javascript:void(0)" data-value="${dto.relaJxb1[0]!}">
												<span class="hd">${dto.relaJxb1[1]!}(${dto.relaJxb1[2]?default(0)})</span>
												<span class="bd">${dto.relaJxb1[3]}</span>
												<i class="fa fa-times-circle color-red js-del"></i>
											</a>
										<#else>
											<a class="btn btn-sm btn-white color-blue" href="javascript:void(0)">
												${dto.relaJxb1[1]!}<span class="color-999">(${dto.relaJxb1[2]?default(0)})</span>
											</a>
										</#if>
								</#if>
							</td>
							<td>
								<#if dto.relaJxb2?exists>
									
									<#if dto.relaJxb2[3]?default('')!=''>
										<a class="merge-btn" href="javascript:void(0)" data-value="${dto.relaJxb2[0]!}">
											<span class="hd">${dto.relaJxb2[1]!}(${dto.relaJxb2[2]?default(0)})</span>
											<span class="bd">${dto.relaJxb2[3]}</span>
											<i class="fa fa-times-circle color-red js-del"></i>
										</a>
									<#else>
										<a class="btn btn-sm btn-white color-blue" href="javascript:void(0)">
											${dto.relaJxb2[1]!}<span class="color-999">(${dto.relaJxb2[2]?default(0)})</span>
										</a>
									</#if>
								</#if>
							</td>
							<td>
								<a class="table-btn color-blue" href="javascript:void(0)" onclick="showXzbStudentList('${dto.classId!}')">查看学生</a>
								<#if canEdit && (dto.relaJxb2?exists || dto.relaJxb1?exists)>
								<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="comClasses('${dto.classId!}')">合教学班</a>
								</#if>
							</td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>

<div class="layer layer-group">
	<div class="layer-content">
		<div class="form-horizontal" id="comjxb">
			<div class="form-group" style="margin-left:10px;">
				<label class="control-label no-padding-right">教学班</label>
			</div>
			<div class="form-group" style="margin-left:10px;">
				<div class="publish-course" id="course_1">
				</div>
			</div>
			<div class="form-group" style="margin-left:10px;">
				<label class="control-label no-padding-right">合并到</label>
			</div>
			<div class="form-group" style="margin-left:10px;">
				<div class="publish-course" id="course_2">
				</div>
			</div>
		</div>
	</div>
</div>

<#-- 底部按钮 -->
<div class="navbar-fixed-bottom opt-bottom">
	<#if !canEdit || isError?default(false)>
		<a class="btn btn-default"   href="javascript:" onclick="reStartArrange()">重新安排</a>
	</#if>
	<#if isError?default(false)>
		<span class="color-blue" id="showMessId">
		数据出现错误
	 	</span>
	<#else>
		<a class="btn btn-blue"   href="javascript:" onclick="nextfun()">下一步</a>
	</#if>
	
</div>


<script>
	var isDelete=false;
	$(function(){
		showBreadBack(gobackResult,false,"分班安排");
		// 删除
		$(".xzbTable").on("click",".js-del",function(e){
			e.stopPropagation();
			var cId=$(this).parent().attr("data-value");
			if(isDelete){
				return;
			}
			isDelete=true;
			delById(cId);
		});
	})
	function reFreshList(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
		$("#showList").load(url);
	}
	
	function delById(cid){
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定删除该合班",options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/delhbJxb',
				data:{'jxbId':cid},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			reFreshList();
			 		}
			 		else{
			 			isDelete=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
		},function(){
			isDelete=false;
		});
		isDelete=false;
	}
	var isGobackResult=false;
	function gobackResult(){
		if(isGobackResult){
			return;
		}
		isGobackResult=true;
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		$("#showList").load(url);
	}
	
	var isStart=false;
	function reStartArrange(){
		if(isStart){
			return;
		}
		isStart=true;
		
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定重新安排",options,function(){
			var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/clearSingleRecomd';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					if(data.success){
						layer.msg("操作成功！", {
							offset: 't',
							time: 2000
						});
						var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
						$("#showList").load(url);
						isStart=false;
					}else{
						layer.closeAll();
						isStart=false;
						layerTipMsg(data.success,"失败",data.msg);
						
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}<#--请求出错 -->
			});
				
		},function(){
			isStart=false;
		});
		isStart=false;
		
	}
	
	var isCheck=false;
	function nextfun(){
		if(isCheck){
			return;
		}
		isCheck=true;
		<#if canEdit>
			var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/checkSingleRecomd';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					if(data.success){
						//if(data.msg!=""){
						//	layer.msg(data.msg, {
						//		offset: 't',
						//		time: 2000
						//	});
						//}
						var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/xkSingle/page';
						$("#showList").load(url);
						isCheck=false;
					}else{
						isCheck=false;
						layerTipMsg(data.success,"失败",data.msg);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}<#--请求出错 -->
			});
		<#else>
			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/xkSingle/page';
			$("#showList").load(url);
		</#if>
	}
	<#if canEdit>
	var isCom=false;
	function comClasses(clazzId){
		if(isCom){
			return;
		}
		isCom=true;
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/findClassList';
		$.ajax({
			url:url,
			data:{"xzbId":clazzId},
			dataType: "JSON",
			success: function(data){
				makeClassData(data);
				layer.open({
					type: 1,
					shadow: 0.5,
					title: '合教学班',
					area: ['550px','500px'],
					btn: ['确定', '取消'],
					btn1:function(){
						saveComJxb();
					},
					btn2:function(){
						isCom=false;
						layer.closeAll();
					},
					content: $('.layer-group')
				});
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}<#--请求出错 -->
		});
		
	}
	
	function makeClassData(data){
		$("#course_1").html("");
		$("#course_2").html("");
		var oneList=data.oneList;
		if(oneList && oneList.length>0){
			$("#course_1").html(makeSpans(oneList,true));
		}else{
			return;
		}
		var twoList=data.twoList;
		if(twoList && twoList.length>0){
			$("#course_2").html(makeSpans(twoList,false));
		}
		initspan();
		changSpan();
	}
	function initspan(){
		$("#course_1").on("click","span",function(){
			if($(this).hasClass("active")){
				
			}else{
				$("#course_1").find("span").removeClass("active");
				$(this).addClass("active");
				changSpan();
			}
		})
		$("#course_2").on("click","span",function(){
			if($(this).hasClass("active")){
				
			}else{
				$("#course_2").find("span").removeClass("active");
				$(this).addClass("active");
			}
		})
	}
	function changSpan(){
		var s=$("#course_1").find(".active");
		if(s.length>0){
			$("#course_2").find("span").removeClass("active");
			$("#course_2").find("span").hide();
			var subid=$(s[0]).attr("data-subid");
			$("#course_2").find("span[data-subid='"+subid+"']").show();
		}else{
			$("#course_2").find("span").removeClass("active");
			$("#course_2").find("span").hide();
		}
	}
	function makeSpans(itemlist,flag){
		var html1="";
		for(var i=0;i<itemlist.length;i++){
			var one=itemlist[i];
			if(i==0 && flag){
				html1=html1+'<span class="active" style="width:auto;padding-left:2px;padding-right:2px;" data-subid="'+one.subjectId+'" data-value="'+one.classId+'">'+one.className+'('+one.stunum+')</span>';
			}else{
				html1=html1+'<span style="width:auto;padding-left:2px;padding-right:2px;" data-subid="'+one.subjectId+'" data-value="'+one.classId+'">'+one.className+'('+one.stunum+')</span>';
			}
		}
		return html1;
	}
	var isSave=false;
	function saveComJxb(){
		if(isSave){
			return;
		}
		isSave=true;
		var fromIdObj=$("#course_1").find(".active");
		var fromId="";
		var toId="";
		if(fromIdObj.length>0){
			fromId=$(fromIdObj).attr("data-value");
		}else{
			layer.tips('请选择一个教学班', $("#course_1"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return;
		}
		var toIdObj=$("#course_2").find(".active");
		if(toIdObj.length>0){
			toId=$(toIdObj).attr("data-value");
		}else{
			layer.tips('请选择需要合并到班级', $("#course_2"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return;
		}
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/saveComjxb';
		$.ajax({
			url:url,
			data:{"fromId":fromId,"toId":toId},
			dataType: "JSON",
			success: function(data){
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
		 			layer.msg("合并成功！", {
						offset: 't',
						time: 2000
					});
				  	isSave=false;
				  	isCom=false;
				  	reFreshList();
		 		}
		 		else{
		 			isSave=false;
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}<#--请求出错 -->
		});
	}
	</#if>
	function showXzbStudentList(clazzId){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/oldXzbStudent/page?clazzId='+clazzId;
		$("#showList").load(url);
	}
	//底部操作按钮样式
	if ($(".opt-bottom").length > 0) {
		$(".page-content").css("padding-bottom","77px")
	}
</script>