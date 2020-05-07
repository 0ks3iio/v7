<div class="font-16 mb20"><b>学科成绩</b></div>
<table class="table table-bordered table-striped table-hover">
    <thead>
		<tr>
		    <th width="100">类型</th>
			<th>所选考试名称</th>
			<th>科目</th>
			<th width="100">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if dtoMap?exists && dtoMap['1']?exists && dtoMap['1']?size gt 0 >
		<#list dtoMap['1'] as dto>
	    <tr>
			<#if dto_index=0><td rowspan="${dtoMap['1']?size}">学科成绩</td></#if>
			<td>${dto.examName!}</td>
			<td>${dto.subjectNames!}</td>
			<#if dto_index=0><td rowspan="${dtoMap['1']?size}"><a class="color-blue js-main" href="#">编辑</a></td></#if>
		</tr>
		</#list>
		<#else>
		<tr>
			<td>学科成绩</td>
			<td colspan="2" class="text-center">暂无相关数据，请先编辑！</td>
			<td><a class="color-blue js-main" href="#">编辑</a></td>
		</tr>
		</#if>
	</tbody>
</table>
<div class="font-16 mb20"><b>英语、口试、体育</b></div>
<table class="table table-bordered table-striped table-hover no-margin">
    <thead>
		<tr>
		    <th width="100">类型</th>
			<th>所选考试名称</th>
			<th>科目</th>
			<th width="100">操作</th>
		</tr>
	</thead>
	<tbody>
		<#if dtoMap?exists && dtoMap?size gt 0 >
		<#list dtoMap?keys as key>
		<#if key!='1'>	
	    	<#if dtoMap[key]?exists && dtoMap[key]?size gt 0>
	    	<#list dtoMap[key] as dto>
		    <tr>
				<#if dto_index=0><td rowspan="${dtoMap[key]?size}">${typeNameMap[key]!}</td></#if>
				<td>${dto.examName!}</td>
				<td>${dto.subjectNames!}</td>
				<#if dto_index=0><td rowspan="${dtoMap[key]?size}"><a class="color-blue js-submain" href="#" data-type="${key!}" data-name="${typeNameMap[key]!}">编辑</a></td></#if>
			</tr>
			</#list>
			<#else>
		    <tr>
				<td>${typeNameMap[key]!}</td>
				<td colspan="2" class="text-center">暂无相关数据，请先编辑！</td>
				<td><a class="color-blue js-submain" href="#" data-type="${key!}" data-name="${typeNameMap[key]!}">编辑</a></td>
			</tr>
			</#if>
		</#if>
		</#list>
		</#if>
	</tbody>
</table>

<!-- 学科成绩 -->
<!-- 英语、口试、体育-->
<div class="layer layer-edit">
	<div class="layer-content" id="editDivId">
		
	</div>
</div>

<script type="text/javascript">
	// 学科成绩
	$('.js-main').on('click', function(){
	    var url = "${request.contextPath}/comprehensive/select/xkcjEdit/page?infoId=${infoId}";
	    $("#editDivId").load(url,function() {
			 layer.open({
		         type: 1,
		         shade: 0.5,
		         title: '学科成绩',
		         area: '520px',
		         btn: ['确定','取消'],
		         yes: function(index){
				 	doSave();
			     },
		         content: $('.layer-edit')
		    });
		});
	});
	
	// 英语、口试、体育
	$('.js-submain').on('click', function(){
		var type = $(this).data("type");
		var url = "${request.contextPath}/comprehensive/select/otherEdit/check";
		$.post(url,{"infoId":"${infoId!}","type":type},function(msg){
			if(msg.success){
				var name = $(this).data("name");
				var url = "${request.contextPath}/comprehensive/select/otherEdit/page?infoId=${infoId}&type="+type;
				$("#editDivId").load(url,function() {
					layer.open({
						type: 1,
						shadow: 0.5,
						title: name,
						area: '520px',
						btn: ['确定', '取消'],
						scrollbar:true,    //是否允许浏览器出现滚动条。默认为true
						yes: function(index){
							doSave();
					    },
						content: $('.layer-edit')
					});
				})
			}else{
				layer.msg(msg.msg, {offset: 't',time: 2000});
			}
		},"JSON");
	});
</script>