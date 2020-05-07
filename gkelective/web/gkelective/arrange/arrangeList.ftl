<!-- ajax layout which only needs content area -->
<#if dtos?exists && (dtos?size>0)>
	<div class="page-head-btns"><a href="javascript:" class="btn btn-blue js-addNewSystem" id="btn-addArrange">新建选课项目</a></div>
		<#list dtos as item>
		<div class="box box-default">
				<div class="box-header">
					<a class="system-flow-delete" href="javascript:void(0);" onclick="doDelete('${item.gsaEnt.id!}')"><i class="fa fa-trash"></i></a>
					<h3 class="box-title box-title-lg">${item.gsaEnt.arrangeName!}</h3>
				</div>
				<div class="box-body">
					<div class="system-flow clearfix">
						<a href="javascript:" class="flow" onclick="doEnterSetp('${item.gsaEnt.id!}',1)">
							<span class="flow-img flow-img-1"></span>
							<div class="flow-content">
								<h4 class="flow-name">发布选课</h4>
								<p class="flow-txt">设置并发布选课</p>
							</div>
						</a>
						<a href="javascript:" class="flow <#if item.gsaEnt.isUsing == 0>disabled</#if>" <#if item.gsaEnt.isUsing == 1>onclick="doEnterSetp('${item.gsaEnt.id!}',2)"</#if> >
							<span class="flow-img flow-img-2"></span>
							<div class="flow-content">
								<h4 class="flow-name">参考成绩</h4>
								<p class="flow-txt">导入参考成绩</p>
							</div>
						</a>
						<a href="javascript:" class="flow <#if item.gsaEnt.isUsing == 0>disabled</#if>" <#if item.gsaEnt.isUsing == 1>onclick="doEnterSetp('${item.gsaEnt.id!}',3)"</#if>>
							<span class="flow-img flow-img-3"></span>
							<div class="flow-content">
								<h4 class="flow-name">选课结果</h4>
								<p class="flow-txt">学生选课结果</p>
							</div>
						</a>
						<a href="javascript:" class="flow <#if item.gsaEnt.isUsing == 0>disabled</#if>" <#if item.gsaEnt.isUsing == 1>onclick="doEnterSetp('${item.gsaEnt.id!}',4)"</#if>>
							<span class="flow-img flow-img-4"></span>
							<div class="flow-content">
								<h4 class="flow-name">开班安排</h4>
								<p class="flow-txt">手动与自动开班</p>
							</div>
						</a>
						<a href="javascript:" class="flow <#if item.gsaEnt.isUsing == 0>disabled</#if>" <#if item.gsaEnt.isUsing == 1>onclick="doEnterSetp('${item.gsaEnt.id!}',6)"</#if>>
							<span class="flow-img flow-img-5"></span>
							<div class="flow-content">
								<h4 class="flow-name">最终确定方案</h4>
								<p class="flow-txt">安排教室与老师</p>
							</div>
						</a>
					</div>
				</div>
		</div>	
		</#list>
	<#else>
		<div class="no-data-container">
			<div class="no-data no-data-hor">
				<span class="no-data-img">
					<img src="${request.contextPath}/gkelective/images/noSelectSystem.png" alt="">
				</span>
				<div class="no-data-body">
					<h3>暂无选课项目</h3>
					<p class="no-data-txt">如需进行选课，请新建选课项目</p>
					<a class="btn btn-lg btn-blue" onclick="addArrange()">新建选课项目</a>
				</div>
			</div>
		</div>
</#if>
<script>
	function doEnterSetp(id,step){
		var url = '';
		if(step == 1){
			url =  '${request.contextPath}/gkelective/'+id+'/goClass/index/page';
		}else if(step == 2){
			url =  '${request.contextPath}/gkelective/'+id+'/basisSet/index/page';
		}else if(step == 3){
			url =  '${request.contextPath}/gkelective/'+id+'/chosenSubject/index/page';
		}else if(step == 4){
			url =  '${request.contextPath}/gkelective/'+id+'/arrangeRounds/index/page';
		}else if(step == 5){
			url =  '${request.contextPath}/gkelective/'+id+'/arrangeResult/index/page';
		}else if(step == 6){
			url =  '${request.contextPath}/gkelective/'+id+'/arrangePlan/index/page';
		}
		$("#showList").load(url);
	}
	function addArrange(){
		var url = "${request.contextPath}/gkelective/arrange/edit/page";
		indexDiv = layerDivUrl(url,{title: "新建选课项目",width:350,height:200});
	}
	$("#btn-addArrange").on("click",function(){
		addArrange();
	});
	
	function doDelete(id){
		var url = "${request.contextPath}/gkelective/arrange/show/delete/page?deleteId="+id;
		indexDiv = layerDivUrl(url,{title: "删除",width:345,height:225});
	}
	
	function doDeleteById(id){
		var verifyCode = $("#verifyCode1").val();
		 if(isBlank(verifyCode)){
            layerTipMsg(false,"失败","请输入验证码！");
            $("#verifyCode1").focus();
            return ;
        }
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/gkelective/arrange/delete',
			data: {'id':id,"verifyCode":verifyCode},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
				  	showList();
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function isBlank(str){
        return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
    }
</script>