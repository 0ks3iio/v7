<script src="${request.contextPath}/gkelective/js/myscriptCommon.js"></script> 
<a href="#" class="page-back-btn gotoLcIndex"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}---安排老师场地</h4>
	</div>
	<div class="box-header">
		<ol class="steps">
			<li class="first completed plan-arrange click-btn <#if plan.step == 0>active </#if> ">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>老师、教室安排</h4>
					<p>安排老师与教室</p>
				</a>
			</li>
			<li class="teacher-result-step">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>老师安排结果</h4>
					<p>对结果进行微调</p>
				</a>
			</li>
			<li class="place-result-step">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>教室安排结果</h4>
					<p>根据同${PCKC!}班级数安排</p>
				</a>
			</li>
			<li class="last all-result-step <#if plan.step != 0> active </#if> ">
				<a href="javascript:" style="text-decoration:none" >
					<span class="step"></span>
					<h4>安排结果</h4>
					<p>整体查看全部开班结果</p>
				</a>
			</li>
		</ol>
	</div>

	<div id="planList">
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var contextPath = '${request.contextPath}';
	var arrangeId='${arrangeId!}'
	var planId = '${planId!}';
	$(function(){
		<#if plan.step == 0>
			toMakePlan();
		<#else>
			dealClickType(['.teacher-result-step','.place-result-step','.all-result-step'],true);
			toAllResult();
		</#if>
		
		$('.plan-arrange').on('click',function(){
			$('.plan-arrange').addClass('active').siblings('li').removeClass('active');
			toMakePlan();
		});
		$('.teacher-result-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			$('.teacher-result-step').addClass('active').siblings('li').removeClass('active');
			toTeacherPlace('1');
		});
		$('.place-result-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			$('.place-result-step').addClass('active').siblings('li').removeClass('active');
	
			toTeacherPlace('2');
		});
		$('.all-result-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			$('.all-result-step').addClass('active').siblings('li').removeClass('active');
			toAllResult();
		});
		$('.gotoLcIndex').on('click',function(){
			var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/index/page';
			$("#showList").load(url);
		});
		
	});
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
	function toMakePlan(){
		var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/makePlan/page?planId=${planId!}';
		$("#planList").load(url);
	}
	function toTeacherPlace(viewtype){
		if(viewtype=="1"){
			$('.teacher-result-step').addClass('active').siblings('li').removeClass('active');
		}else{
			$('.place-result-step').addClass('active').siblings('li').removeClass('active');
		}
		
		var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/toTeacherPlace/page?planId=${planId!}&viewtype='+viewtype;
		$("#planList").load(url);
	}
	function toAllResult(){
		$('.all-result-step').addClass('active').siblings('li').removeClass('active');
		var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/findAllResultIndex/page?planId=${planId!}';
		$("#planList").load(url);
	}
	
	
</script>