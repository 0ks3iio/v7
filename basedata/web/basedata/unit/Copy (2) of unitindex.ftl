
<title>jqGrid - Ace Admin</title>

<link rel="stylesheet" href="${request.contextPath}/static/ace/css/jquery-ui.css" />
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/datepicker.css" />
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<script src="${request.contextPath}/static/ace/js/date-time/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-en.js"></script>

<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		jqGrid
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			Dynamic tables and grids using jqGrid plugin
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			You can have a custom jqGrid download here:
			<a href="http://www.trirand.com/blog/?page_id=6" target="_blank">
				http://www.trirand.com/blog/?page_id=6
				<i class="fa fa-external-link bigger-110"></i>
			</a>
		</div>

		<table id="grid-table"></table>
		<div id="grid-pager"></div>
		
		<table id="grid-table2"></table>
		<div id="grid-pager2"></div>

		<script type="text/javascript">
			var $path_base = "../..";//in Ace demo this will be used for editurl parameter
		</script>
		
		<div class="jqGrid_wrapper">
            <table id="table_list_1"></table>
            <div id="pager_list_1"></div>
        </div>
                        
		 <div class="jqGrid_wrapper">
            <table id="table_list_2"></table>
            <div id="pager_list_2"></div>
        </div>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<!-- page specific plugin scripts -->
<script type="text/javascript">

	 $(document).ready(function(){
	 var mydata=[
		{id:"27",invdate:"2007-10-04",name:"test",note:"note",amount:"200.00",tax:"10.00",total:"210.00"},
		{id:"28",invdate:"2007-10-03",name:"test2",note:"note2",amount:"300.00",tax:"20.00",total:"320.00"},
		{id:"29",invdate:"2007-09-01",name:"test3",note:"note3",amount:"400.00",tax:"30.00",total:"430.00"}
	];

	$("#table_list_1").jqGrid(
	{data:mydata,datatype:"local",height:250,autowidth:true,shrinkToFit:true,rowNum:14,rowList:[10,20,30],
	colNames:["序号","日期","客户","金额","运费","总额","备注"],
	colModel:[
	{name:"id",index:"id",width:60,sorttype:"int"},
	{name:"invdate",index:"invdate",width:90,sorttype:"date",formatter:"date"},
	{name:"name",index:"name",width:100},
	{name:"amount",index:"amount",width:80,align:"right",sorttype:"float",formatter:"number"},
	{name:"tax",index:"tax",width:80,align:"right",sorttype:"float"},
	{name:"total",index:"total",width:80,align:"right",sorttype:"float"},
	{name:"note",index:"note",width:150,sortable:false}
	],
	pager:"#pager_list_1",viewrecords:true,caption:"jqGrid 示例1",hidegrid:false}
	);

	$("#table_list_2").jqGrid(
	{data:mydata,datatype:"local",height:450,autowidth:true,shrinkToFit:true,rowNum:20,rowList:[10,20,30],
	colNames:["序号","日期","客户","金额","运费","总额","备注"],
	colModel:[
	{name:"id",index:"id",editable:true,width:60,sorttype:"int",search:true},
	{name:"invdate",index:"invdate",editable:true,width:90,sorttype:"date",formatter:"date"},
	{name:"name",index:"name",editable:true,width:100},
	{name:"amount",index:"amount",editable:true,width:80,align:"right",sorttype:"float",formatter:"number"},
	{name:"tax",index:"tax",editable:true,width:80,align:"right",sorttype:"float"},
	{name:"total",index:"total",editable:true,width:80,align:"right",sorttype:"float"},
	{name:"note",index:"note",editable:true,width:100,sortable:false}
	],
	pager:"#pager_list_2",viewrecords:true,caption:"jqGrid 示例2",
	add:true,edit:true,addtext:"Add",edittext:"Edit",hidegrid:false}
	);

	$("#table_list_2").jqGrid("navGrid","#pager_list_2",{
	edit:true,add:true,del:true,search:true},{height:200,reloadAfterSubmit:true});
	
	$(window).bind("resize",function(){
		var width=$(".jqGrid_wrapper").width();
		$("#table_list_1").setGridWidth(width);
		$("#table_list_2").setGridWidth(width)}
		)
	}
	
	);


	var scripts = [null,"${request.contextPath}/static/ace/js/date-time/bootstrap-datepicker.js","${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js","${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-en.js", null]
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	  //inline scripts related to this page
		 var grid_data = 
	[ 
		{id:"1",name:"Desktop Computer",note:"note",stock:"Yes",ship:"FedEx", sdate:"2007-12-03"},
		{id:"4",name:"Speakers",note:"note",stock:"No",ship:"ARAMEX",sdate:"2007-12-03"},
	];
	
	
	jQuery(function($) {
		var grid_selector = "#grid-table";
		var pager_selector = "#grid-pager";
		
		$(window).on('resize.jqGrid', function () {
			$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
	    })
		var parent_column = $(grid_selector).closest('[class*="col-"]');
		$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
			if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
				//setTimeout is for webkit only to give time for DOM changes and then redraw!!!
				setTimeout(function() {
					$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
				}, 0);
			}
	    })
		
		jQuery(grid_selector).jqGrid({
			data: grid_data,datatype: "local",height: "auto",
			colNames:[' ', 'ID','Last Sales','Name', 'Stock', 'Ship via','Notes'],
			colModel:[
				{name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false},
				{name:'id',index:'id', width:60, sorttype:"int", editable: true},
				{name:'sdate',index:'sdate',width:90, editable:true, sorttype:"date",unformat: pickDate},
				{name:'name',index:'name', width:150,editable: true,editoptions:{size:"20",maxlength:"30"}},
				{name:'stock',index:'stock', width:70, editable: true,edittype:"checkbox",editoptions: {value:"Yes:No"}},
				{name:'ship',index:'ship', width:90, editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"}},
				{name:'note',index:'note', width:150, sortable:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"}} 
			], 
	
			viewrecords : true,	rowNum:10,rowList:[10,20,30],
			pager : pager_selector,
			altRows: true,multiselect: true,multiboxonly: false,	
			loadComplete : function() {
				var table = this;
				setTimeout(function(){
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			},
			caption: "jqGrid with inline editing"
		});
		
				
		$(window).triggerHandler('resize.jqGrid');
		function pickDate( cellvalue, options, cell ) {
			setTimeout(function(){
				$(cell) .find('input[type=text]')
						.datepicker({format:'yyyy-mm-dd' , autoclose:true}); 
			}, 0);
		}
	
	
		//navButtons
		jQuery(grid_selector).jqGrid('navGrid',pager_selector,
			{ 	//navbar options
				edit: false,
				add: false,
				del: false,
				search: true,
				searchicon : 'ace-icon fa fa-search orange',
				refresh: true,
				refreshicon : 'ace-icon fa fa-refresh green',
				view: false
			},
			{},
			{},
			{},
			{
				recreateForm: true,
				afterShowSearch: function(e){
					var form = $(e[0]);
					form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
					style_search_form(form);
				},
				afterRedraw: function(){
					style_search_filters($(this));
				}
				,
				multipleSearch: true,
				/**
				multipleGroup:true,
				showQuery: true
				*/
			},
			{}
		)
		
		function style_search_filters(form) {
			form.find('.delete-rule').val('X');
			form.find('.add-rule').addClass('btn btn-xs btn-primary');
			form.find('.add-group').addClass('btn btn-xs btn-success');
			form.find('.delete-group').addClass('btn btn-xs btn-danger');
		}
		function style_search_form(form) {
			var dialog = form.closest('.ui-jqdialog');
			var buttons = dialog.find('.EditTable')
			buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'ace-icon fa fa-retweet');
			buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'ace-icon fa fa-comment-o');
			buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'ace-icon fa fa-search');
		}
	
		//unlike navButtons icons, action icons in rows seem to be hard-coded
		//you can change them like this in here if you want
		//replace icons with FontAwesome icons like above
		function updatePagerIcons(table) {
			var replacement = 
			{
				'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
				'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
				'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
				'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
		}
	
		function enableTooltips(table) {
			$('.navtable .ui-pg-button').tooltip({container:'body'});
			$(table).find('.ui-pg-div').tooltip({container:'body'});
		}
	
		//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
	
		$(document).one('ajaxloadstart.page', function(e) {
			$(grid_selector).jqGrid('GridUnload');
			$('.ui-jqdialog').remove();
		});
	});
	});
</script>
