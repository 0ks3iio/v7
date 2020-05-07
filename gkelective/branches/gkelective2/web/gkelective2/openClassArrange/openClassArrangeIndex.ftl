<script src="${request.contextPath}/gkelective2/js/myscriptCommon.js"></script> 
<script src="${request.contextPath}/gkelective2/openClassArrange/openClassArrange.js"></script>
<a href="#" class="page-back-btn gotoLcIndex"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}第${rounds.orderId}轮次</h4>
	</div>
	<div class="box-header">
		<ol class="steps">
			<#if rounds.openClassType?default('') == '1'>
			<li class="first completed perarrange-step click-btn">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>手动排班</h4>
					<p>手动开设组合班</p>
				</a>
			</li>
			<li class="group-result-step">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>手动开班结果</h4>
					<p>查看手动开班结果</p>
				</a>
			</li>
			<li class="un-arrange-step">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>未安排的学生</h4>
					<p>查看未被安排的学生</p>
				</a>
			</li>
			</#if>
			<li class="single-step <#if rounds.openClassType?default('') == '0'>first completed click-btn active</#if>">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>走班开班</h4>
					<p>单科走班开班</p>
				</a>
			</li>
			<li class="last all-result-step">
				<a href="javascript:" style="text-decoration:none">
					<span class="step"></span>
					<h4>全部开班结果</h4>
					<p>查看全部开班结果</p>
				</a>
			</li>
		</ol>
	</div>

	<div id="groupList">
	</div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(function(){
		contextPath = '${request.contextPath}';
		arrangeId='${arrangeId!}'
		roundsId = '${roundsId!}';
		<#if tipMsg?default('') != ''>
		layerTipMsgWarn("提示",'${tipMsg!}');
		</#if>
		<#if rounds.openClassType?default('') == '0' && rounds.step == 1>
			toSingle();
		<#elseif rounds.step == 1>
			toPerArrange();
		<#elseif rounds.step == 2>
			toGroupResult();
		<#elseif rounds.step == 3>
			toUnArrange();
		<#elseif rounds.step == 4>
			toSingle();
		<#elseif !(rounds.step lt 5)>
			toAllResult();
		</#if>
		<#if rounds.openClassType?default('') == '1'>
		$('.perarrange-step').on('click',function(){
			toPerArrange();
		});
		$('.group-result-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			toGroupResult();
		});
		$('.un-arrange-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			toUnArrange();
		});
		</#if>
		$('.single-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			toSingle();
		});
		$('.all-result-step').on('click',function(){
			if(!$(this).hasClass('completed')){
				return;
			}
			toAllResult();
		});
		$('.gotoLcIndex').on('click',function(){
			var url =  contextPath+'/gkelective/${rounds.subjectArrangeId!}/arrangeRounds/index/page';
			$("#showList").load(url);
		});
		
	});
</script>