<form>
<ul class="setting-list">
<li>
<ul>
	<#if itemList?exists && itemList?size gt 0>
	<#list itemList as item>
		<li>
			<label><input type="radio" class="wp" name="arrayItemId" value="${item.id}"><span class="lbl"> ${item.itemName!}</span></label>
			<div class="box box-primary hidden">
				<div class="box-header">
					<h3 class="box-title">${item.itemName!}</h3>
				</div>
				<div class="box-body">
					<div class="explain">
						<p>创建时间：${item.creationTime!}</p>
					</div>
					<div class="number-container">
						<ul class="number-list">
						<#assign numArr=item.newGkItemDto.num>
						<#if item.newGkItemDto.typeName?exists && item.newGkItemDto.typeName?size gt 0>
						<#assign i=0>
						<#list item.newGkItemDto.typeName as name>
							<li><em>${numArr[i]!}</em><span>${name!}</span></li>
							<#assign i=i+1>
						</#list>
						</#if>
						</ul>
					</div>
				</div>
			</div>
		</li>
	</#list>
	
	<#else>
		没有设置排课特征
	</#if>
</ul>
</li>
</ul>
</form>

<script>
	$(function(){

		$('input[type=radio]').on('click', function(){

			var option = $(this).attr('name');
			$('[name=' + option + ']').parent().next().addClass('hidden');

			if($(this).prop('checked') === true){
				var b = $(this).parent().next('.box');

				if(typeof b !== 'object') return;
				b.removeClass('hidden');
			}
		})
	})
</script>
