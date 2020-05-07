<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div class="box box-default openXzbBox">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<div class="filter-content">
					<button class="btn btn-blue" onclick="backToPenXzb()">返回</button>
				</div>
			</div>
		</div>
		<div class="table-switch-container no-margin">
			<div class="table-switch-box">
				<div class="table-switch-filter">
					<div class="filter">
						<input type="hidden" name="subjectIds"  id="subjectIds" value="${subjectIds!}">
						<div class="filter-item">
							<span class="filter-name"><span>科目：</span><span id="groupNames">${subjectNames!}</span></span>
						</div>
						<div class="filter-item">
							<div class="filter-content">
								<input type="text" class="form-control float-left" id="chooseRowNum" placeholder="请输入" maxlength="4" style="width: 70px;">
								<button class="btn btn-blue float-left ml5" onclick="chooseRows()">选行</button>
							</div>
						</div>
						<div class="filter-item">
							<div class="filter-content">
								<input type="text" class="form-control float-left" id="openClassNum" placeholder="请输入" maxlength="3" style="width: 70px;">
								<button class="btn btn-blue float-left ml5" onclick="openSomeClass()">开班</button>
							</div>
						</div>
					</div>
				</div>
				<div class="leftTable">
					<div class="table-switch-data default">
						<span>总数：<em>${manNum+womanNum}</em></span>
						<span>男：<em>${manNum}</em></span>
						<span>女：<em>${womanNum}</em></span>
						<#list showSubjectName as sub>
						<span>${sub[1]!}：<em>${allScore[sub[0]]?default(0)?string("0.##")}</em></span>
						</#list>
					</div>
					<table class="table table-bordered table-striped js-sort-table show-stulist-table left-table">
						<thead>
							<tr>
								<th>
									<label class="pos-rel" style="margin-right: 0px">
										<input type="checkbox" class="wp" checked name="allStudentId">
										<span class="lbl"></span>
									</label>
								</th>
								<th>序号</th>
								<th>姓名</th>
								<th>性别</th>
								<th>原行政班 <a class="float-right color-grey js-popover-all js-popover-filter js-popover-filter-class mr10" href="#" aria-describedby="filterClass"><i class="fa fa-filter"></i></a></th>
								<th>选课 <a class="float-right color-grey js-popover-all js-popover-filter js-popover-filter-subject mr10" href="#"><i class="fa fa-filter"></i></a></th>
								<#list showSubjectName as sub>
								<th>${sub[1]!}</th>
								</#list>
								<th>总分</th>
							</tr>
						</thead>
						<tbody>
							<#if leftStuDtoList?exists && leftStuDtoList?size gt 0>
							<#list leftStuDtoList as item>
								<tr>
									<td>
										<label class="pos-rel" style="margin-right: 0px">
											<input type="checkbox" class="wp" name="studentId"  checked value="${item.studentId!}">
											<span class="lbl"></span>
										</label>
									</td>
									<td class="xuhao">${item_index+1}</td>
									<td>${item.studentName!}</td>
									<td>${item.sex!}</td>
									<td><input type="hidden" class="classId" value="${item.classId!}">${item.className!}</td>
									<td><input type="hidden" class="subjectId" value="${item.chooseSubjects!}">${item.choResultStr!}</td>
									<#if item.subjectScore?exists && item.subjectScore?size gt 0>
										<#assign subjectScore=item.subjectScore>
										<#assign scores=0.0>
										<#list showSubjectName as sub>
											<#assign score1=subjectScore[sub[0]]?default(0)?string("0.##")>
											<td>${score1}</td>
											<#assign scores=scores+score1?number>
										</#list>
										<td>${scores}</td>
									<#else>
										<#list showSubjectName as sub>
										<td>0</td>
										</#list>
										<td>0</td>
									</#if>
									
								</tr>
							</#list>
							</#if>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="table-switch-control">
				<button class="btn btn-sm" onclick="leftToRightTable()"><i class="wpfont icon-arrow-right"></i></button>
				<button class="btn btn-sm" onclick="rightToLeftTable()"><i class="wpfont icon-arrow-left"></i></button>
			</div>
			<div class="table-switch-box">
				<div class="table-switch-filter">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">班级：</span>
							<div class="filter-content">
								<select class="form-control" name="groupClassId" id="groupClassId" onChange="loadRight()">
									
									<#if classList?exists && classList?size gt 0>
										<#list classList as zz>
											<option value="${zz[0]!}" <#if groupClassId?default('')==zz[0]>selected</#if>>${zz[1]!}</option>
										</#list>
									<#else>
										<option value="">暂无数据</option>
									</#if>
									
								</select>
							</div>
						</div>
						<div class="filter-item filter-item-right">
							<div class="filter-content">
								<button class="btn btn-blue" onclick="addNewClass()">创建班级</button>
								<button class="btn btn-default" onclick="deleteClass()">删除</button>
							</div>
						</div>
					</div>
				</div>
				<div class="rightTable">
					
				</div>
			</div>
		</div>
	</div>
</div>
<#----默认全部选中-->
<div id="filterClassId" style="display:none;">
	<div id="filterClassId1">
	 	<#list classFilterList as classFilter>
		<div>
			<label class="pos-rel">
				<input type="checkbox" class="wp" name="classFilter" checked value="${classFilter[0]!}" onChange="myfilter(this,1)">
				<span class="lbl"> ${classFilter[1]!}(${classFilter[2]!})</span>
			</label>
		</div>
		
		</#list>
	</div>
</div>
<div id="filterSubjectId"  style="display:none;">
	<div id="filterSubjectId1">
 	<#list subjectFilterList as subjectFilter>
		<div>
			<label class="pos-rel">
				<input type="checkbox" class="wp" name="subjectFilter" checked value="${subjectFilter[0]!}" onChange="myfilter(this,2)">
				<span class="lbl"> ${subjectFilter[1]!}(${subjectFilter[2]!})</span>
			</label>
		</div>
		
	</#list>
	</div>
</div>
<script>
	var isShowClass=true;
	var isShowSubject=true;
	//班级选中
	var map1={};
	//科目选中
	var map2={};
	
	var chooseStuIdsToAdd="";//用于新增存放学生
	$(function(){
		// 通过js添加table水平垂直滚动条
		$('.left-table').DataTable({
			// 设置垂直方向高度
	        scrollY: 450,
			// 禁用搜索
			searching: false,
	        // 禁止表格分页
	        paging: false,
	        // 禁止宽度自动
			autoWidth: false,
			info: false,
			order: [
			
				<#assign ss=0>
				<#list showSubjectName as sub>
					<#if sub_index gt 0></#if>[${sub_index+6},'desc'],
					<#assign ss=sub_index+6>
				</#list>
				[${ss+1},'desc']
			],
			// 禁用指定列排序
			columnDefs: [
				{ orderable: false, targets: 0 },
				{ orderable: false, targets: 1 }
		    ],
		    language: {
		    	 emptyTable:"暂无数据"
		    },
		    fnDrawCallback:function(){
		    	makeXuhao("leftTable");
		    }
	    });
	    
	    //左右全选功能
	    $('.openXzbBox').on('change','.show-stulist-table input:checkbox[name=allStudentId]',function(){
			if($(this).is(':checked')){
				$(this).parents(".table-switch-box").find('input:checkbox[name=studentId]').each(function(i){
					var $tr=$(this).parents("tr");
					if(!$($tr).is(":hidden")){
						$(this).prop('checked',true);
					}else{
						$(this).prop('checked',false);
					}
					
				});
			}else{
				$(this).parents(".table-switch-box").find('input:checkbox[name=studentId]').each(function(i){
					$(this).prop('checked',false);
				});
			}
		});
		$('.openXzbBox').on('change','.show-stulist-table input:checkbox[name=studentId]',function(){
			if($(this).is(':checked')){
				
			}else{
				$(this).parents(".table-switch-box").find("input:checkbox[name=allStudentId]").prop('checked',false);
			}
		});
	    
		
		//行政班过滤
		$('.js-popover-filter-class').popover({
			content:$("#filterClassId1"),
			html: true,
			placement: 'bottom',
			trigger: 'manual',
		    container: '.openXzbBox'
		})
		//选课过滤
		$('.js-popover-filter-subject').popover({
			content:$("#filterSubjectId1"),
			html: true,
			placement: 'bottom',
			trigger: 'manual',
		    container: '.openXzbBox'
		})
		//显示过滤列表
		$('.js-popover-filter-subject').click(function(){
			$('.js-popover-filter-class').popover('hide');
			$(this).popover('show');
		})
		$('.js-popover-filter-class').click(function(){
			$('.js-popover-filter-subject').popover('hide');
			$(this).popover('show');
		})
		
		loadRight();
	})
	
	//过滤的地方处理
	function myfilter(obj,type){
		var filter1=$(obj).val();
		if(type==1){
			filterItem(filter1,obj,type);
		}else{
			filterItem(filter1,obj,type);
		}
	}
	//obj:过滤器中点击的复选框
	function filterItem(filterValue,obj,type){
		var isShowAll=false;
		var objDiv=$(obj).parent().parent();
		var otherObj=$(objDiv).siblings();
		if(filterValue==""){
			//操作的是全部
			if($(obj).is(':checked')){
				//选中所有
				if(otherObj.length>0){
					$(objDiv).siblings().each(function(){
						$(this).find("input").prop('checked',true);
					})
				}
				isShowAll=true;
			}else{
				isShowAll=false;
				if(otherObj.length>0){
					$(objDiv).siblings().each(function(){
						$(this).find("input").prop('checked',false);
					})
				}
			}
		}else{
			if($(obj).is(':checked')){
				isShowAll=false;
			}else{
				isShowAll=false;
				//全选部分不选中
				$(objDiv).siblings().each(function(){
					if($(this).val()==""){
						$(this).find("input").prop('checked',false);
						return false;
					}
				})
			}
		}
		if(type==1){
			if(isShowAll){
				isShowClass=true;
			}else{
				isShowClass=false;
				map1={};
				$("#filterClassId1").find("input").each(function(){
					if($(this).is(':checked')){
						var iid=$(this).val();
						map1[iid]=iid;
					}
				})
			}
		}else{
			if(isShowAll){
				isShowSubject=true;
			}else{
				isShowSubject=false;
				map2={};
				$("#filterSubjectId1").find("input").each(function(){
					if($(this).is(':checked')){
						var iid=$(this).val();
						map2[iid]=iid;
					}
				})
			}
		}
		filterTable();
	}
	function filterTable(){
		//过滤
		var trList=$(".left-table tbody").find("tr");
		if(trList.length==0){
			return;
		}
		if(isShowSubject && isShowClass){
			for(var i=0;i<trList.length;i++){
				$(trList[i]).find("input[name='studentId']").prop('checked',true);
				$(trList[i]).show();
			}
		}else{
			for(var i=0;i<trList.length;i++){
				var ccd=$(trList[i]).find(".classId").val();
				var ssd=$(trList[i]).find(".subjectId").val();
				if((isShowClass || map1[ccd]) && (isShowSubject || map2[ssd])){
					$(trList[i]).find("input[name='studentId']").prop('checked',true);
					$(trList[i]).show();
				}else{
					$(trList[i]).find("input[name='studentId']").prop('checked',false);
					$(trList[i]).hide();
				}
			}
		}
		makeXuhao("leftTable");
	}
	
	function chooseRows(){
		var num=$("#chooseRowNum").val().trim();
		var pattern=/[^0-9]/;
		if(pattern.test(num) || num.slice(0,1)=="0"){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#chooseRowNum").val('');
			$("#chooseRowNum").focus();
			return false;
		}
		var rows=parseInt(num);
		if(num<=0){
			return false;
		}
		var ii=0;
		var trList=$(".left-table tbody").find("tr");
		for(var i=0;i<trList.length;i++){
			if(!$(trList[i]).is(":hidden")){
				if(ii<rows){
					$(trList[i]).find("input[name='studentId']").prop('checked',true);
				}else{
					$(trList[i]).find("input[name='studentId']").prop('checked',false);
				}
				ii++;
			}else{
				$(trList[i]).find("input[name='studentId']").prop('checked',false);
			}
		}
		
	}
	
	function backToPenXzb(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
		$("#showList").load(url);
	}
	
	function loadRight(){
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==''){
			$(".rightTable").html(
				`<div class="table-switch-data default" style="height:530px;">
					<div class="no-data-container text-center" style="margin-top:20%;">
						<div class="no-data">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata7.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt" style="font-size:14px;">暂无开设班级</p>
							</div>
						</div>
					</div>
				</div>`
			);
			return;
		}
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/loadRightList?groupClassId='+groupClassId;
		$(".rightTable").load(url);
	}
	var isSubmitAdd=false;
	function saveNewClass(){
		if(isSubmitAdd){
			return;
		}
		isSubmitAdd=true;
		var subjectIds=$("#subjectIds").val();
		var className=$.trim($("#newclassname").val());
		if(className==""){
			isSubmitAdd=false;
			layer.tips('不能为空', $("#newclassname"), {
				tipsMore: true,
				tips:3				
			});
			return;
		}
		
		var url='${request.contextPath}/newgkelective/${divideId!}/divideClass/saveOneClass';
		$.ajax({
			url:url,
			data:{"subjectIds":subjectIds,"className":className,"studentIds":studentIds},
			dataType: "JSON",
			success: function(data){
				isSubmitAdd=false;
				if(data.success){
		 			layer.closeAll();
		 			layer.msg("保存成功！", {
						offset: 't',
						time: 2000
					});
					refreshOpenHead(subjectIds,data.msg);
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function addNewClass(){
		chooseStuIdsToAdd="";
		var subjectIds=$("#subjectIds").val();
		if(subjectIds==null || subjectIds==""){
			layer.msg("请先选择组合！", {
				offset: 't',
				time: 2000
			});
		}else{
			var studentIds="";
			var trList=$(".left-table tbody").find("tr");
			if(trList.length>0){
				for(var i=0;i<trList.length;i++){
					if(!$(trList[i]).is(":hidden")){
						if($(trList[i]).find("input[name='studentId']").is(':checked')){
							var stuId=$(trList[i]).find("input[name='studentId']").val();
							studentIds=studentIds+","+stuId;
						}
					}
				}
				if(studentIds!=""){
					studentIds=studentIds.substring(1);
				}
			}
			chooseStuIdsToAdd=studentIds;
			<#--页面保存学生id-->
			var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/newClass/page?subjectIds='+subjectIds;
			indexDiv = layerDivUrl(url,{title: "新建班级",width:350,height:250});
		}
	}
	
	
	function clearNewClass(){
		idAdd=false;
		$('.js-popover-create').popover('hide');
	}
	
	function clearOpenpopover(){
		$('.js-popover-all').popover('hide');
	}
	
	function refreshOpenHead(subjectIds,divideClassId){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/scheduleIndex?subjectIds='+subjectIds+"&groupClassId="+divideClassId;
		$("#showList").load(url);
	}
	var moveSubmit=false;
	function leftToRightTable(){
		if(moveSubmit){
			return;
		}
		moveSubmit=true;
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==""){
			addNewClass();
			return;
		}
		if($(".rightTable").find("table").length == 0){
			moveSubmit=false;
			return;
		}
		var isDeletdFirst=false;
		var allTr=$(".rightTable").find("table").find("tbody").find("tr");
		if(allTr.length==0){
			
		}else{
			var firstRow=$(allTr[0]).find('input:checkbox[name=studentId]').length;
			if(firstRow==0){
				isDeletdFirst=true;
			}
		}
		var i=0;
		var stuId=[];
		<#--增加到右边的html-->
		var htmlText="";
		//增加
		$(".leftTable").find("table").find(".linChangeClass").removeClass("linChangeClass");
		
		if($(".leftTable").find('input:checkbox[name=studentId]:checked').length>0){
			if(isDeletdFirst){
				$(allTr[0]).remove();
			}
			$(".leftTable").find('input:checkbox[name=studentId]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				htmlText=htmlText+'<tr class="new">'+$(this).parents("tr").html()+'</tr>';
				$(this).parents("tr").addClass("linChangeClass");
			});
			$(".rightTable").find("table").find("tbody").append(htmlText);
			$(".leftTable").find("input:checkbox[name=studentIds]").prop('checked',false);
			$(".leftTable").find("table").find(".linChangeClass").remove();
		}else{
			moveSubmit=false;
			return;
		}
		makeXuhao("leftTable");
		makeXuhao("rightTable");
		saveRight();
	}
	function rightToLeftTable(){
		if(moveSubmit){
			return;
		}
		moveSubmit=true;
		if($(".leftTable").find("table").length == 0){
			moveSubmit=false;
			return;
		}
		var isDeletdFirst=false;
		var allTr=$(".leftTable").find("table").find("tbody").find("tr");
		if(allTr.length==0){
			
		}else{
			var firstRow=$(allTr[0]).find('input:checkbox[name=studentId]').length;
			if(firstRow==0){
				isDeletdFirst=true;
			}
		}
		var i=0;
		var stuId=[];
		var htmlText="";
		//增加
		$(".rightTable").find("table").find(".linChangeClass").removeClass("linChangeClass");
		
		if($(".rightTable").find('input:checkbox[name=studentId]:checked').length>0){
			if(isDeletdFirst){
				$(allTr[0]).remove();
			}
			$(".rightTable").find('input:checkbox[name=studentId]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				htmlText=htmlText+'<tr class="new">'+$(this).parents("tr").html()+'</tr>';
				$(this).parents("tr").addClass("linChangeClass");
			});
			$(".leftTable").find("table").find("tbody").append(htmlText);
			$(".rightTable").find("input:checkbox[name=studentIds]").prop('checked',false);
			$(".rightTable").find("table").find(".linChangeClass").remove();
		}else{
			moveSubmit=false;
			return;
		}
		makeXuhao("leftTable");
		makeXuhao("rightTable");
		saveRight();
	}
	
	function saveRight(){
		var groupClassId=$("#groupClassId").val();
		var stuId="";
		if($(".rightTable").find('input:checkbox[name=studentId]').length>=0){
			$(".rightTable").find('input:checkbox[name=studentId]').each(function(i){
				var studentId = $(this).val();
				if(stuId.indexOf(studentId)>-1){
					
				}else{
					stuId=stuId+","+studentId;
				}
			});
			stuId=stuId.substring(1);
		}
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/groupClassSaveStu',
			data:{"stuId":stuId,"groupClassId":groupClassId},
			dataType : 'json',
			type:'post',
			success:function(data) {
				moveSubmit=false;
				var jsonO = data;
		 		if(jsonO.success){
		 			subjectIds=$("#subjectIds").val();
					refreshOpenHead(subjectIds,groupClassId);
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function makeXuhao(objClass){
		var $tdody=$("."+objClass).find("tbody");
		if($tdody){
			var trList=$($tdody).find("tr");
			var ii=1;
			for(var i=0;i<trList.length;i++){
				if(!$(trList[i]).is(":hidden")){
					$(trList[i]).find(".xuhao").html(ii);
					ii++;
				}
			}
		}
	}
	
	function deleteClass(){
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==null || groupClassId==""){
			layer.msg("没有班级需要删除！", {
				offset: 't',
				time: 2000
			});
			return ;
		}
		var subjectIds=$("#subjectIds").val();
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm("是否确定删除班级",options,function(){
			$.ajax({
				url:"${request.contextPath}/newgkelective/${divideId!}/divideClass/deleteDivideClass",
				data:{"divideClassId":groupClassId},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
					  	refreshOpenHead(subjectIds,"");
			 		}
			 		else{
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){});
	}
	
	var isOpenSame=false;
	function openSomeClass(){
		if(isOpenSame){
			return;
		}
		isOpenSame=true;
		var openSubmit=false;
		var num=$("#openClassNum").val().trim();;
		if(num == ""){
			layer.msg("不能为空！", {
				offset: 't',
				time: 2000
			});
			$("#openClassNum").val('');
			$("#openClassNum").focus();
			isOpenSame=false;
			return false;
		}
		var pattern=/[^0-9]/;
		if(pattern.test(num) || num.slice(0,1)=="0"){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#openClassNum").val('');
			$("#openClassNum").focus();
			isOpenSame=false;
			return false;
		}
		var rows=parseInt(num);
		if(rows<=0){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#openClassNum").val('');
			$("#openClassNum").focus();
			isOpenSame=false;
			return false;
		}
		var stuIdStr="";
		if($(".leftTable").find('input:checkbox[name=studentId]:checked').length>0){
			$(".leftTable").find('input:checkbox[name=studentId]:checked').each(function(i){
				stuIdStr=stuIdStr+","+$(this).val();
			});
			stuIdStr=stuIdStr.substring(1);
		}
		subjectIds=$("#subjectIds").val();
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定开班",options,function(){
			if(openSubmit){
				return;
			}
			openSubmit=true;
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/autoOpenClass',
				data:{"subjectIds":subjectIds,"openNum":rows,"stuids":stuIdStr},
				dataType : 'json',
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = data;
			 		if(jsonO.success){
			 			if("没有学生需要分班"==jsonO.msg){
			 				layer.msg(jsonO.msg, {
								offset: 't',
								time: 2000
							});
			 			}else{
			 				refreshOpenHead(subjectIds,"");
			 			}
			 			
			 		}else{
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
					
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
			
		},function(){
			isOpenSame=false;
		});
		
	}
</script>