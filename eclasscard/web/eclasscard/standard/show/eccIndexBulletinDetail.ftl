<div class="article">
	<h2 class="article-title">${bulletin.title!}</h2>
	<p class="article-time">发布时间：${bulletin.createTime?string('yyyy-MM-dd HH:mm:ss')}</p>
	<div class="article-content">
	${bulletin.content!}
	</div>
</div>