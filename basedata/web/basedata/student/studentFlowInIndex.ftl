<title>学生转入</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<#import "/fw/macro/htmlcomponent.ftl" as htmlmacro>

<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
 
<!-- ajax layout which only needs content area -->

<div class="row">
	<div class="col-xs-12">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs pull-left" id="tabId">
				 	<li class="active">
				 		<a data-toggle="tab" href="#a">转入管理</a>
				 	</li>
				 	<li>
				 		<a data-toggle="tab" href="#b">操作记录</a>
				 	</li>
				 </ul>
			</div>
			<div class="tab-content">
			
				<div id="a" class="tab-pane fade in active searchFlowinDetail">
					<!-- 条件筛选开始 -->
					<div class="filter">
						<div class="filter-item">
							<label for="" class="filter-name">学生姓名：</label>
							<div class="filter-content">
								<input type="text" id="studentName"  nullable="false" name="studentname" class="form-control flowin">
							</div>
						</div>
						<div class="filter-item">
							<label for="" class="filter-name">身份证件号：</label>
							<div class="filter-content">
								<input type="text" id="identityCard"  nullable="<#if !isUseIdentityCard?default(false)>true<#else>false</#if>" name="identityCard" class="form-control input-large flowin">
							</div>
						</div>
						<#if isUseVerifyCode!true>
						<#--
						-->
						<div class="filter-item">
							<label for="" class="filter-name">验证码：</label>
							<div class="filter-content">
								<input type="text" id="pin" nullable="false" name="pin" class="form-control input-large flowin">
							</div>
						</div>
						</#if>
						<button class="btn btn-darkblue pull-left btn-search">查找</button>
						<div class="filter-item pull-right">
							<a class="btn btn-darkblue flowin-import" href="javascript:void(0);" id="">批量操作</a>
						</div>
					</div><!-- 条件筛选结束 -->
					<div class="widget-box ">
						<div class="widget-body">
							<div class="widget-main searchResult">
								<p class="alert alert-info">请输入准确的学生信息，点击查询后查看结果</p>
							</div>
						</div>
					</div>
				</div>
				<!-- 历史记录查询 start-->
				<div id="b" class="tab-pane fade in searchFlowinList" style="">
						<!-- 条件筛选开始 -->
						<div class="filter">
							<div class="filter-item">
								<label for="" class="filter-name">学生姓名：</label>
								<div class="filter-content">
									<input type="text" nullable="false" id="studentName2" name="studentname" class="form-control history">
								</div>
							</div>
							<div class="filter-item">
								<label for="" class="filter-name">转入时间：</label>
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
							<button class="btn btn-darkblue pull-left btn-search">查找</button>
							<div class="filter-item pull-right">
								<a class="btn btn-darkblue export-flowin" href="javascript:void(0);">导出操作记录</a>
							</div>
						</div>
						<div class="widget-box">
							<div class="widget-body">
								<div class="widget-main searchListResult">
									<p class="alert alert-info">请输入准确的学生信息，点击查询后查看结果</p>
								</div>
							</div>
						</div>
			  </div><!--历史记录查询输入over-->
					
			</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">

	var indexDiv = 0;
	$(window).bind("resize",function(){
		resizeLayer(indexDiv); 
	});

	// 需要用到的js脚本，延迟加载
	var scripts = [null,
	"${request.contextPath}/static/ace/components/moment/moment.js",
	"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
	"${request.contextPath}/static/ace/components/chosen/chosen.jquery.js",null
	];

	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		initCalendar("#b");
		//绑定搜索
		$(".btn-search").on("click", function(){
			var type = "1";
			var searchClass = "searchResult";
			var formDiv = "flowin";
			var cls = ".searchFlowinDetail";
			if($("#a").attr("class").indexOf("active")>0){
				type = "1";
			}
			else{
				type = "2";
				searchClass = "searchListResult";
				formDiv = "history";
				cls = ".searchFlowinList"
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
			}
			var check = checkVal1(type,cls);
			if(!check){
		 		return;
			}
			
			$("."+searchClass).load("${request.contextPath}/basedata/studentFlowIn/searResult/"+type+"/page?"+$("."+formDiv).serialize()+"&startDate="+$("#b #startDate").val()+"&endDate="+$("#b #endDate").val());
		});
		
		//绑定导出
		$(".export-flowin").on("click",function(){
			var studentName = $("#studentName2").val();
			if(studentName == ''){
				layer.tips('请输入学生姓名','#studentName2',{tips:3,tipsMore:true});
				return ;
			}
			showConfirm("确认导出？",function(){
				
				location.href = "${request.contextPath}/basedata/studentFlowIn/export/logs?studentname="+$("#studentName2").val()+"&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val();
				layer.closeAll();
			},function(){
				layer.closeAll();
			});
		});
		
		$(".flowin-import").on("click",function(){
			var url = "${request.contextPath}/basedata/studentFlowIn/import/page/";
			var indexDiv = layerDivUrl(url,{title:'批量导入',max:true});
			//resizeLayer(indexDiv, 1000, 350);
		});
	});
	
	function checkVal1(type,cls){
		var isOk = true;
		//校验身份证号码和验证码长度
		if(type=="1"){
			isOk = checkValue(cls);
			<#if isUseVerifyCode!true>
			var pinCode = $("#pin").val();
			if($.trim(pinCode)!='' && pinCode.length!=6){
				isOk = false;
				layer.tips('请输入正确的验证码','#pin',{tips:3,tipsMore:true});
			}
			</#if>	
		}
		else if(type == "2"){
			var studentName = $("#studentName2").val();
			if(studentName == ''){
				layer.tips('请输入学生姓名','#studentName2',{tips:3,tipsMore:true});
				return ;
			}
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
				layer.tips('开始日期不能大于结束日期','#startDate',{tips:3,tipsMore:true});
				isOk = false;
			}
		}
		return isOk;
	}
	
</script>
