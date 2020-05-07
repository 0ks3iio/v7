<title>考号管理</title>
<!-- ajax layout which only needs content area -->
<div class="jqGrid_wrapper">
    <table id="examNumList"></table>
    <div id="examNumPager"></div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@examNumList />
	});
</script>

<#macro examNumList>
var rowList = [10, 20,50, 100, 200];
var rowNum = 50;
var lastsel;
var url = "${request.contextPath}/scoremanage/examNum/list?examId=${examId!}&classType=${classType!}&classIdSearch=${classIdSearch!}";
$("#examNumList").jqGrid({
	url: url,
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:false,
	rowList:rowList,
	rowNum:rowNum,	
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "考号信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	colModel:[
	//修改了jqgrid的源码，使其支持表头th元素的样式控制(thclasses)
	{name:"student.id",width:90,sortable:false,hidden:true},
	{name:"student.studentName",label:"姓名",width:90,sortable:false},
	{name:"student.identityCard",label:"身份证号",width:120,sortable:false},
	{name:"className",label:"班级",width:90,sortable:false},
	{name:"student.studentCode",label:"学号",width:120,sortable:false},
	{name:"examNum",label:"考号",width:120,sortable:false,editable: true,
		editoptions:{maxlength:50}
	},
	{name:'操作',index:'', width:80, fixed:true, sortable:false, resize:false,
		formatter:'actions', 
		formatoptions:{ 
			keys:true,
			delbutton: false,//disable delete button
			
			delOptions:{},
		}
	}
],
gridComplete: function () {
  updatePagerIcons(this);
}, 
editurl : "${request.contextPath}/scoremanage/examNum/rowEditing?examId=${examId!}",

jsonReader: {
	id: "student.id"
},
pager:"#examNumPager"}
);
 		

</#macro>
