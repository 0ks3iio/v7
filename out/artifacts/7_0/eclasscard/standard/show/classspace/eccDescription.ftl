				<div class="class-introduction">
					<#if pictureUrl != ''>
					<div class="class-introduction-imgs">
						<img src="${request.contextPath}${pictureUrl!}" alt="">
					</div>
					</#if>
					<div class="class-introduction-main">
						<div class="class-introduction-item">
							<h3>班级概况</h3>
							<p>${introduction!}</p>
						</div>
						<#if honorList?exists&&honorList?size gt 0>
						<div class="class-introduction-item">
							<h3>班级荣誉</h3>
							<#list honorList as honor>
							<p>${honor.title!}</p>
							<#if honor_index+1 == 3>
							<#break>
							</#if>
							</#list>
						</div>
						</#if>
						<#if classDesc.content?exists>
						<div class="class-introduction-item">
							<h3>班级简介</h3>
							<p>${classDesc.content!}</p>
						</div>
						</#if>
					</div>
				</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
	});	
</script>	