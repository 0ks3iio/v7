<style>
	.jxbTables table .my2{position: relative;cursor: pointer;}
	.jxbTables table .my2 i{display:none;position: absolute;top: -5px;right: -5px;border-radius:50%;background: #fff;}
	.jxbTables table .my2:hover i{display: block;}
</style>
<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<#if openType?default('')=='09'>
			<li class="active"><span><i>1</i>选考分层</span></li>
			<li class="active"><span><i>2</i>分选考班</span></li>
			<li class="active"><span><i>3</i>选考班结果</span></li>
			<li class="active"><span><i>4</i>分学考班</span></li>
			<li class="active"><span><i>5</i>学考班结果</span></li>
		<#else>
			<li class="active"><span><i>1</i>原行政班</span></li>
			<li class="active"><span><i>2</i>选考分层</span></li>
			<li class="active"><span><i>3</i>分选考班</span></li>
			<li class="active"><span><i>4</i>选考班结果</span></li>
			<li class="active"><span><i>5</i>分学考班</span></li>
			<li class="active"><span><i>6</i>学考班结果</span></li>
		</#if>
	</ul>
</div>
<div class="row">
	<div class="col-xs-12">
		<div class="box box-default jxbTables" id="jxbTables">
			<div class="box-body">
			<#if openType?default('')=='11' && onlyXzbDtoList?exists && onlyXzbDtoList?size gt 0>
				<h4 class="form-title">
					<b>行政班科目</b>
				</h4>
				<table class="table table-bordered layout-fixed table-editable">
					<thead>
						<tr>
							<th class="text-center" width="100px">科目</th>
							<th class="text-center" width="100px">总人数</th>
							<th class="text-center" width="100px">班级数</th>
							<th class="text-center">行政班结果</th>
						</tr>
					</thead>
					<tbody>
					<#list onlyXzbDtoList as dto>
							<tr>
								<td >${dto.courseName!}</td>
								<td >${dto.totalNum!}</td>
								<td >${dto.aClassNum!}</td>
								<td>
									<#if dto.asXzbClassList?exists && dto.asXzbClassList?size gt 0>
									<#list dto.asXzbClassList as clazz>
										<a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" data-class="${clazz.id!}" <#if clazz.studentList?size gt 0>onclick="showDetail('${clazz.id!}')"</#if>>
											${clazz.className!}
											<span class="color-999 font-12">（${clazz.studentList?size}人）</span>
											<#if clazz.studentList?size == 0><i class="fa fa-times-circle color-red js-del"></i></#if>
										</a>
									</#list>
									<#else>
									/
									</#if>
								</td>
							</tr>
					</#list>
					</tbody>
				</table>
			</#if>
			<#if xzbDtoList?exists && xzbDtoList?size gt 0>
				<h4 class="form-title">
					<b>行政班科目</b>
				</h4>
				<table class="table table-bordered layout-fixed table-editable">
					<thead>
						<tr>
							<th class="text-center">科目</th>
							<th class="text-center">总人数</th>
							<th class="text-center" >班级数</th>
							<#list bathList as bath>
								<th class="text-center" width="20%">学考${bath!}</th>
							</#list>
						</tr>
					</thead>
					<tbody>
					<#if xzbDtoList?exists && xzbDtoList?size gt 0>
					<#list xzbDtoList as dto>
						<tr>
							<td>${dto.courseName!}</td>
							<td>${dto.totalNum!}</td>
							<td>${dto.aClassNum!}</td>
							<#list bathList as i>
							<#assign cnum=0>
							<td>
								<#if dto.levelMap?exists && dto.levelMap?size gt 0>
									<#list dto.levelMap?keys as level>
										<#if dto.levelBatchMap[level][i+""]?exists && dto.levelBatchMap[level][i+""]?size gt 0>
											<#list dto.levelBatchMap[level][i+""] as clazz>
												<a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" data-class="${clazz.id!}" <#if clazz.studentList?size gt 0>onclick="showDetail('${clazz.id!}')"</#if>>
													${clazz.className!}
													<span class="color-999 font-12">（${clazz.studentList?size}人）</span>
													<#if clazz.studentList?size == 0><i class="fa fa-times-circle color-red js-del"></i></#if>
												</a>
												<#assign cnum=cnum+1>
											</#list>
										</#if>
									</#list>
								</#if>
								<#if cnum==0>/</#if>
							</td>
							</#list>
						</tr>
					</#list>
					</#if>
					</tbody>
				</table>
				</#if>
				<#if jxbDtoList?exists && jxbDtoList?size gt 0>
				<h4 class="form-title">
					<b>走班科目</b>
				</h4>
				<table class="table table-bordered layout-fixed table-editable">
					<thead>
						<tr>
							<th class="text-center">科目</th>
							<th class="text-center">总人数</th>
							<th class="text-center" >班级数</th>
							<#list bathList as bath>
								<th class="text-center" width="20%">学考${bath!}</th>
							</#list>
						</tr>
					</thead>
					<tbody>
					<#if jxbDtoList?exists && jxbDtoList?size gt 0>
					<#list jxbDtoList as dto>
						<tr>
							<td>${dto.courseName!}</td>
							<td>${dto.totalNum!}</td>
							<td>${dto.aClassNum!}</td>
							<#list bathList as i>
								<td>
								<#assign nums=0>
								<#if dto.levelMap?exists && dto.levelMap?size gt 0>
									<#list dto.levelMap?keys as level>
										<#if dto.levelBatchMap[level][i+""]?exists && dto.levelBatchMap[level][i+""]?size gt 0>
											<#list dto.levelBatchMap[level][i+""] as clazz>
												<a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" data-class="${clazz.id!}" <#if clazz.studentList?size gt 0>onclick="showDetail('${clazz.id!}')"</#if>>
													${clazz.className!}
													<span class="color-999 font-12">（${clazz.studentList?size}人）</span>
													<#if clazz.studentList?size == 0><i class="fa fa-times-circle color-red js-del"></i></#if>
												</a>
												<#assign nums=nums+1>
											</#list>
										</#if>
								</#list>
								</#if>
								<#if nums==0>
									/
								</#if>
								</td>
							</#list>
						</tr>
					</#list>
					</#if>
					</tbody>
				</table>
				</#if>
			</div>
		</div>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-white" href="javascript:" onclick="toBack();">上一步</a>
	<a class="btn btn-blue"  href="javascript:" onclick="finshDivide()">完成分班</a>
</div>
<script>
	$(function(){
		// 删除
		$("#jxbTables").on("click",".js-del",function(e){
			e.stopPropagation();
			var delId=$(this).parent().attr("data-class");
			deletejxb(delId);
		})
	})
	var isDelete=false;
	function deletejxb(delId){
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("确定删除该班级",options,function(){
			deleteByjxbId(delId);
		},function(){
			isDelete=false;
		});
	}
	
	
	function deleteByjxbId(id){
    	if(isDelete){
    		return;
    	}
    	var ii = layer.load();	
    	isDelete=true;
    	$.ajax({
    		 url: "${request.contextPath}/newgkelective/${divideId!}/divideClass/deleteTeachClass",
             data: {"classId": id},
             dataType: 'json',
             type: 'post',
             success: function (data) {
             	var jsonO=data;
				if(jsonO.success){
					layer.closeAll();
					layer.msg("删除成功！", {
						offset: 't',
						time: 2000
					});
					isDelete=false;
					var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultA/page';
					$("#showList").load(url)
	 			}else{
	 				isDelete=false;
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
	 			}	
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
    }

	function toBack(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/parametersetB';
		$("#showList").load(url);
	}
	var isFinsh=false;
	function finshDivide(){
		if(isFinsh){
			return;
		}
		isFinsh=true;
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/finshDivide',
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
		 			backToResult();
		 		}
		 		else{
		 			isFinsh=false;
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function backToResult(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultClassList';
		$("#showList").load(url);
	}
	function showDetail(classId) {
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/showJxbStuResult?teachClassId=' + classId+'&subjectType=B';
		$("#showList").load(url);
	}
</script>