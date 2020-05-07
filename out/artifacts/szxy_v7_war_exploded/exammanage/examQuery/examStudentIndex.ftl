<div class="main-content-inner">
	<ol class="breadcrumb hidden "></ol>
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<div class="box box-default">
					<div class="box-body clearfix">
		            	<div class="seachPersonal-query seachPersonal-warp" id='divType'>
		                	<strong>个人成绩查询</strong>
		                	<div class="filter">
								<div class="filter-item">
									<div class="filter-content">
										<div class="input-group input-group-search">
											<select name="queryType" id="queryType" class="form-control" onChange='stuTypeChange()'>
												<option value="0">姓名</option>
												<option value="1">学号</option>
											</select>
											<div class="pos-rel pull-left">
												<input type="text" id="queryContent" name="queryContent" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入姓名" onkeydown="dispResStudent()">
											</div>
											<div class="input-group-btn">
												<button type="button" class="btn btn-default" onclick="findResult()">
													<i class="fa fa-search"></i>
												</button>
											</div>
										</div><!-- /input-group -->
									</div>
								</div>
							</div>
		                </div>
		                <div class="col-sm-12" id="examStudentList">
		                	
						</div>
					</div>
				</div>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</div>
<script>
	function dispResStudent(){
	var x;
    if(window.event) // IE8 以及更早版本
    {	x=event.keyCode;
    }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
    {
        x=event.which;
    }
    if(13==x){
        findResult();
    }
	}
	
	function findResult() {
		var queryType = $("#queryType").val();
		var queryContent = $("#queryContent").val();
		var str = "?queryType="+queryType+"&queryContent="+queryContent;
		var ss="请先输入要查询的学生姓名或学号";
		if(queryContent==""){
			layerTipMsg(false,"提示",ss+"！");
			return;
		}
		$("#divType").attr("class","seachPersonal-query");
		var url =  '${request.contextPath}/examquery/examStudent/Tab/page'+str;
		url=encodeURI(encodeURI(url));
		$("#examStudentList").load(url);
	}
	
	function stuTypeChange() {
		var queryType = $("#queryType").val();
		var queryTypeName = $("#queryType :selected").text();
		$("#queryContent").attr("placeholder","请输入"+queryTypeName);
	}
</script>