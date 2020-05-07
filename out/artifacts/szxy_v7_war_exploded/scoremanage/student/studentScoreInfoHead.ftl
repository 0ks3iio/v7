<div id="bb" class="tab-pane active" role="tabpanel">
	<div class="box box-default">
	    <div class="box-header header_filter">
	        <div class="filter filter-f16">
	        	<div class="filter-item">
					<label for="" class="filter-name">学年：</label>
					<div class="filter-content">
						<select class="form-control" id="acadyearSearch" onChange="changeExam()">
						<#if (acadyearList?size>0)>
							<#list acadyearList as item>
							<option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}学年</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">学期：</label>
					<div class="filter-content">
						<select class="form-control" id="semesterSearch" onChange="changeExam()">
						 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
						</select>
					</div>
				</div>
				<#if tabIndex?default('1')=='1' || tabIndex?default('1')=='3'>	
				<div class="filter-item">
					<label for="" class="filter-name">考试名称：</label>
					<div class="filter-content">
						<select vtype="selectOne" class="form-control" id="examId" onChange="searchList()">
							<option value="">-----请选择-----</option>
						</select>
					</div>
				</div>
				</#if>	
				<#if tabIndex?default('1')=='3'>
				<div class="filter-item filter-item-right">
					<button class="btn btn-blue" onclick="doPrint()">打印</button>
				</div>
				</#if>
			</div>
	    </div>
	    <div class="box-body" id="showList">
	    
	    </div>
	</div>
</div>
<script>
	$(function(){
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	function changeExam(){
		var acadyear=$("#acadyearSearch").val();
		var semester=$("#semesterSearch").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/student/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				examClass.chosen("destroy");
				if(data.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examNameOther+"</option>");
					}
				}
				$(examClass).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				searchList();
			}
		});
	}
	
	function searchList(){
		var acadyear=$("#acadyearSearch").val();
		var semester=$("#semesterSearch").val();
		var param = "&acadyearSearch="+acadyear+"&semesterSearch="+semester+"&tabIndex=${tabIndex?default('1')}";
		if(${tabIndex?default('1')}=='1' || ${tabIndex?default('1')}=='3'){
			var examId=$("#examId").val();
			param += "&examId="+examId;
		}
		var url = "${request.contextPath}/scoremanage/student/list/page?"+param;
		$("#showList").load(url);
	}
	//打印
   function doPrint(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		if (LODOP==undefined || LODOP==null) {
			return;
		}
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_HTM("10mm","5mm","RightMargin:0mm","BottomMargin:0mm",getPrintContent($("#showList")));
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//纵向打印
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
		LODOP.PREVIEW();//打印预览
	}
</script>