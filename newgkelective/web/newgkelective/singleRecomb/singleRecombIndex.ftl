<link rel="stylesheet" href="${request.contextPath}/static/components/jQuerycxColor/css/jquery.cxcolor.css">
<script src="${request.contextPath}/static/components/jQuerycxColor/js/jquery.cxcolor.js"></script>
<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<#if openType?default('')=='11'>
			<li class="active"><span><i>1</i>原行政班</span></li>
			<li class="active"><span><i>2</i>选考分层</span></li>
			<li><span><i>3</i>分选考班</span></li>
			<li><span><i>4</i>选考班结果</span></li>
			<li><span><i>5</i>分学考班</span></li>
			<li><span><i>6</i>学考班结果</span></li>
		<#else>
			<li class="active"><span><i>1</i>选考分层</span></li>
			<li><span><i>2</i>分选考班</span></li>
			<li><span><i>3</i>选考班结果</span></li>
			<li><span><i>4</i>分学考班</span></li>
			<li><span><i>5</i>学考班结果</span></li>
		</#if>
	</ul>
</div>
<div class="row">
	<div class="col-xs-12">
		<div class="box box-default">
			<div class="box-body">
				<#if canEdit>
				<div class="filter">
					<div class="filter-item">
						<div class="filter-content">
							<button class="btn btn-blue js-group" type="button" onclick="intelliOpenTeachClass()">智能分层</button>
							<button class="btn btn-default" type="button" onclick="clearRange()">重新分层</button>
						</div>
					</div>
				</div>
				</#if>
				<#if openType?default('')!='11' && xzbList?exists && xzbList?size gt 0>
				<h4 class="form-title">
					<b>行政班科目</b>
				</h4>
				<div class="row">
					<#list xzbList as dto>
					<div class="col-xs-6">
						<div class="single-subject">
							<div class="hd">
								<img src="${request.contextPath}/static/images/7choose3/${dto.pngName?default('chemistry')}.png">
								<span class="title">${dto.subjectName!}</span>
							</div>
							<div class="bd">
								<div class="clearfix">
									<div class="float-left mt5">
										<span class="mr20">全部：${dto.stuNum}人</span>
										<span>未安排：<strong class="color-red"> ${dto.stuNum-dto.arrangeNum}</strong>人</span>
									</div>
									<#if canEdit>
									<div class="float-right">
										<div class="btn-group btn-group-sm">
                                            <button class="btn btn-default" type="button" onclick="quickOpenTeachClass('${dto.subjectId!}')">快捷分层</button>
                                            <button class="btn btn-default" type="button" onclick="tofenc('${dto.subjectId!}')">手动分层</button>
                                        </div>
									</div>
									</#if>
								</div>
								<#if dto.rangeList?exists && dto.rangeList?size gt 0>
									<ul class="level clearfix">
									<#list dto.rangeList as rangeDto>
										<li>
											<span class="letter">${rangeDto.type!}</span>
											<span class="num">${rangeDto.arrangeNum}</span>
										</li>
									</#list>
									</ul>
								<#else>
									<#if canEdit>
										<div class="text-center color-999 mt30">请点击上方按钮快捷分层<img src="${request.contextPath}/static/images/public/arrow.png"></div>
									<#else>
										<div class="text-center color-999 mt30">暂无分层</div>
									</#if>
								</#if>
							</div>
						</div>
					</div>
					</#list>
				</div>
				</#if>
				<#if jxbList?exists && jxbList?size gt 0>
				<h4 class="form-title">
					<b>走班科目</b>
				</h4>
				<div class="row">
					<#list jxbList as dto>
						<div class="col-xs-6">
							<div class="single-subject">
								<div class="hd">
									<img src="${request.contextPath}/static/images/7choose3/${dto.pngName?default('chemistry')}.png">
									<span class="title">${dto.subjectName!}</span>
								</div>
								<div class="bd">
									<div class="clearfix">
										<div class="float-left mt5">
											<span class="mr20">全部：${dto.stuNum}人</span>
											<span>未安排：<strong class="color-red">${dto.stuNum-dto.arrangeNum}</strong>人</span>
										</div>
										<#if canEdit>
										<div class="float-right">
											<div class="btn-group btn-group-sm">
	                                            <button class="btn btn-default" type="button" onclick="quickOpenTeachClass('${dto.subjectId!}')">快捷分层</button>
	                                            <button class="btn btn-default" type="button" onclick="tofenc('${dto.subjectId!}')">手动分层</button>
	                                        </div>
										</div>
										</#if>
									</div>
									<#if dto.rangeList?exists && dto.rangeList?size gt 0>
									<ul class="level clearfix">
										<#list dto.rangeList as rangeDto>
											<li>
												<span class="letter">${rangeDto.type!}</span>
												<span class="num">${rangeDto.arrangeNum}</span>
											</li>
										</#list>
									</ul>
									<#else>
										<#if canEdit>
										<div class="text-center color-999 mt30">请点击上方按钮快捷分层<img src="${request.contextPath}/static/images/public/arrow.png"></div>
									<#else>
										<div class="text-center color-999 mt30">暂无分层</div>
									</#if>
									</#if>
								</div>
							</div>
						</div>
					</#list>
				</div>
				</#if>
			</div>
		</div>
	</div>
</div>
<#-- 底部按钮 -->
<div class="navbar-fixed-bottom opt-bottom">
	<#if openType?default('')=='11'>
		<a class="btn btn-default"  href="javascript:" onclick="backNextfun()">上一步</a>
	</#if>
	
	<a class="btn btn-blue"  href="javascript:" onclick="goNextfun()">下一步</a>
</div>


<script>
	$(function(){
		showBreadBack(gobackResult,false,"分班安排");
	})
	var isGobackResult=false;
	function gobackResult(){
		if(isGobackResult){
			return;
		}
		isGobackResult=true;
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		$("#showList").load(url);
	}
	
	function backNextfun(){
		var url =  '${request.contextPath}/newgkelective/${divideId}/divideClass/item';
		$("#showList").load(url);
	}
	
	var isNext=false;
	function goNextfun(){
		if(isNext){
			return;
		}
		isNext=true;
		<#if canEdit>
			var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/checkSingleA';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					var jsonO = data;
			 		if(jsonO.success){
			 			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/parametersetA';
						$("#showList").load(url);
			 		}
			 		else{
			 			isNext=false;
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}<#--请求出错 -->
			});
		<#else>
			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/parametersetA';
			$("#showList").load(url);
		</#if>

	}
	
	function tofenc(subjectId){
		var url =  '${request.contextPath}/newgkelective/${divideId}/divideClass/singleRecomb/rangeSet?from=reArrange&subjectId=' + subjectId;
		$("#showList").load(url);
	}

	function refreshThis() {
		<#if openType?default('')=='11'>
		$("#showList").load('${request.contextPath}/newgkelective/${divideId}/divideClass/xkSingle/page');
		<#else>
		$("#showList").load('${request.contextPath}/newgkelective/${divideId}/divideClass/item');
		</#if>
		
	}

	function quickOpenTeachClass(subjectId) {
		var title = "快捷分层（将清空原有数据）";
		var width = 390;
		var height = 450;
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/quickStratify?subjectId=' + subjectId;
		indexDiv = layerDivUrl(url, {title: title, width: width, height: height});
	}

	function intelliOpenTeachClass() {
		var title = "智能分层（将清空原有数据）";
		var width = 500;
		var height = <#if divideType?default("") == "11">400<#else>490</#if>;
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/intelliStratify';
		indexDiv = layerDivUrl(url, {title: title, width: width, height: height});
	}

	var isAllMove=false;
	function clearRange(){
		if(isAllMove){
			return;
		}
		isAllMove=true;
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定清除原有分层数据",options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/clearRange',
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			refreshThis();
			 		}
			 		else{
			 			isAllMove=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
		},function(){
			isAllMove=false;
		});
		isAllMove=false;
	}
</script>