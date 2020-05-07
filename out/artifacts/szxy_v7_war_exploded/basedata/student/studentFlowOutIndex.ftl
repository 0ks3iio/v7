<title>学生转出</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>

<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
 
<!-- ajax layout which only needs content area -->

<div class="row">
	<div class="col-xs-12">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs pull-left">
				 	<li class="active">
				 		<a data-toggle="tab" href="#a">转出管理</a>
				 	</li>
				 	<li>
				 		<a data-toggle="tab" href="#b">操作记录</a>
				 	</li>
				 </ul>
			</div>
			<div class="tab-content">
				<div id="a" class="tab-pane fade in active">
					<!-- 条件筛选开始 -->
					<div class="filter">
						<div class="filter-item">
							<label for="" class="filter-name">学生姓名：</label>
							<div class="filter-content">
								<input type="text" id="studentName" class="form-control">
							</div>
						</div>
						<div class="filter-item">
							<label for="" class="filter-name">身份证件号：</label>
							<div class="filter-content">
								<input type="text" id="identityCard" class="form-control input-large">
							</div>
						</div>
						<button class="btn btn-darkblue pull-left btn-search">查找</button>
						<div class="filter-item pull-right">
							<a class="btn btn-darkblue flowout-import" href="javascript:;">批量操作</a>
						</div>
					</div><!-- 条件筛选结束 -->
					<div class="widget-box">
						<div class="widget-body">
							<div class="widget-main searchResult">
								<p class="alert alert-info">请输入准确的学生信息，点击查询后查看结果</p>
							</div>
						</div>
					</div>
				</div>
				<div id="b" class="tab-pane fade flowout-logs">
					<!-- 条件筛选开始 -->
					<div class="filter ">
						<div class="filter-item">
							<label for="" class="filter-name">学生姓名：</label>
							<div class="filter-content">
								<input nullable="false" type="text" id="studentName2" class="form-control">
							</div>
						</div>
						<div class="filter-item">
							<label for="" class="filter-name">转出时间：</label>
							<div class="filter-content">
								<div class="input-daterange input-group">
									<@w.dateDivWithNoColumn nullable="false" id="startDate" displayName="" value="" inputClass="input-sm form-control"/>
									<span class="input-group-addon">
										<i class="fa fa-exchange"></i>
									</span>
									<@w.dateDivWithNoColumn nullable="false" id="endDate" displayName="" value="" inputClass="input-sm form-control"/>
								</div>
							</div>
						</div>
						<button class="btn btn-darkblue pull-left">查找</button>
						<div class="filter-item pull-right">
							<a class="btn btn-darkblue export-flowout" href="javascript:;">导出操作记录</a>
						</div>
					</div><!-- 条件筛选结束 -->
					<div class="table-wrapper">
						<div class="widget-main searchResult searchListResult">
							<p class="alert alert-info">请输入准确的学生信息，点击查询后查看结果</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">

	var indexDiv = 0;
	$(window).bind("resize",function(){
		resizeLayer(indexDiv, 1000, 350);
	});
	
	// 需要用到的js脚本，延迟加载
	var scripts = [null,
	"${request.contextPath}/static/ace/components/moment/moment.js",
	"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
	"${request.contextPath}/static/ace/components/chosen/chosen.jquery.js",null
	];
	
	
	
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		initCalendar("#b");
		$(".flowout-import").on("click",function(){
			var url = "${request.contextPath}/basedata/studentFlowOut/import/page/";
			var indexDiv = layerDivUrl(url,{title:'批量导入',max:true});
			//resizeLayer(indexDiv, 1000, 350);
		});
		$("#a .btn-search").on("click", function(){
			//var idNum = $("#identityCard").val();
			//var regex = /^[0-9]{17}[0-9x]{1}$/gi;
			//if($.trim(idNum)!='' && !regex.test(idNum)){
			//	layer.tips('请输入正确的身份证号','#identityCard',{tips:3,tipsMore:true});
			//	return;
			//}
			var studentName = $("#a #studentName").val();
			var idCard = $("#a #identityCard").val();
			var isOk = true;
			if(studentName == ''){
				layer.tips('请输入姓名','#a #studentName',{tips:3,tipsMore:true});
				isOk = false;
			}
			if(idCard == ''){
				layer.tips('请输入身份证件号','#identityCard',{tips:3,tipsMore:true});
				isOk = false;
			}
			if(!isOk){return ;}
			var url = "${request.contextPath}/basedata/studentFlowOut/searchResult/1/page?studentName=" + $("#a #studentName").val() + "&identityCard=" + $("#a #identityCard").val();
			
			$("#a .searchResult").load(encodeURI(url,"UTF-8"));
		});
		$("#b .pull-left").on("click", function(){
			if(!_checkStudentName()){
				return ;
			}
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
				layer.tips('开始日期不能大于结束日期','#startDate',{tips:3,tipsMore:true});
				return ;
			}
			
			var url = "${request.contextPath}/basedata/studentFlowOut/searchResult/2/page?studentName=" + $("#b #studentName2").val() + "&startDate="+$("#b #startDate").val()+"&endDate="+$("#b #endDate").val();
			$("#b .searchResult").load(encodeURI(url,"UTF-8"));
		});
		
		//绑定导出
		$("#b .export-flowout").on("click",function(){
			if(!_checkStudentName()){
				return ;
			}
			showConfirm("确认导出？",function(){
				var url = '${request.contextPath}/basedata/studentFlowOut/export/logs?studentName='+ $("#b #studentName2").val() + "&startDate="+$("#b #startDate").val()+"&endDate="+$("#b #endDate").val();
				location.href = encodeURI(url,"UTF-8");
				layer.closeAll();
			},function(){
				layer.closeAll();
			});
		});
		
		function _checkStudentName(){
			var studentName = $("#studentName2").val();
			if(studentName == ''){
				layer.tips('请输入学生姓名','#studentName2',{tips:3,tipsMore:true});
				return false;
			}
			return true;
		}
	});
</script>
