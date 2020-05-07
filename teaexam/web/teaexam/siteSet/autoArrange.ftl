<#include "arrangeCommon.ftl">
<div class="no-data-container" <#if status?default('0') != '9'>style="display:none;"</#if>>
	<div class="no-data">
		<span class="no-data-img">
			<i class="fa fa-exclamation-circle font-70 color-999"></i>
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">当前考试未将考生分配进考场，请分配</p>
			<a href="javascript:;" class="btn btn-blue" onclick="autoArrange();">自动分配</a>
		</div>
	</div>
</div>

<div id="reArrange" class="no-data-container" <#if status?default('0') != '-1'>style="display:none;"</#if>>
	<div class="no-data">
		<span class="no-data-img">
			<i class="fa fa-exclamation-circle font-70 color-red"></i>
		</span>
		<div class="no-data-body">
			<p class="no-data-txt color-red">自动分配失败，请重试</p>
			<a href="javascript:;" class="btn btn-blue" onclick="autoArrange();">自动分配</a>
		</div>
	</div>
</div>

<div id="ingArrange" class="text-center color-blue my100" <#if status?default('0') != '1'>style="display:none;"</#if>>
	<i class="fa fa-spinner fa-spin"></i> 正在自动分配中...
</div>
