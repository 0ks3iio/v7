<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.core.js"></script>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<div class="clearfix">
    <div class="col-sm-2" style="min-width: 276px;">
    	<div class="box box-default">
		    <div class="box-header clearfix" style="margin:10px 0;padding:0 5px 10px 0px;">
		    	<div class="filter">
		    		<#--h3 class="pull-left" style="margin:12px 0 0 10px;font-size: 16px;">机构</h3-->
					<div class="filter-content pull-left" style="margin-left:20px;">
							<select name="" id="gradeCode" class="form-control" onchange="clickGrade();">
							     <option value="31">高一年级</option>
							     <option value="32">高二年级</option>
							     <option value="33">高三年级</option>
							</select>
					</div>
				</div>
		    </div>
		    <div class="box-body"> 
		    	<#--@treemacro.unitForDirectInsetTree height="550" click="" /-->
		    	<div class="filter">
					<div class="filter-item">
						<div class="filter-content">
							<div class="input-group input-group-search">
						        <div class="pos-rel pull-left">
						        	<input type="text" class="typeahead scrollable form-control" id="unitName" autocomplete="off" data-provide="typeahead" placeholder="请输入单位名称" value="${searchUnitName!}">
						        </div>
							    
							    <div class="input-group-btn">
							    	<a class="btn btn-default btn-search">
								    	<i class="fa fa-search"></i>
								    </a>
							    </div>
						    </div><!-- /input-group -->
						</div>
					</div>
			    </div>
		    	<ul id="tree" class="ztree" style=" height:1086px; overflow:auto;">
				    <li id="tree_1" class="level0" tabindex="0" hidefocus="true" treenode=""><span id="tree_1_switch" title="" class="button level0 switch root_close" treenode_switch=""></span><a id="tree_1_a" class="level0" treenode_a="" onclick="" target="_blank" style="" title="test1"><span id="tree_1_ico" title="" treenode_ico="" class="button ico_close" style=""></span><span id="tree_1_span" class="node_name">浙江省教育局</span></a></li>
				</ul>
		    </div>
		</div>
	</div>
	
	<div class="col-sm-9">
		<div class="box box-default">
		    <div id="resultDataDiv">
		    	<div class="no-data-container">
					<div class="no-data">
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
						</span>
						<div class="no-data-body">
							<h3>请选择要查看的单位</h3>
						</div>
					</div>
				</div>
		    </div>
		</div>
	</div>
</div>
<script>
hidenBreadBack();

$('#tree').css({
	overflow: 'auto',
	height: $(window).innerHeight() - $('#tree').offset().top - 60
});
$('.no-data-container').css({
	height: $('#tree').height()+60
});

$('.btn-search').on('click',function(){
	searchUnit();
});

$('#unitName').keydown(function(event){
    if(event.keyCode==13){
    	searchUnit();
    }
});

function searchUnit(){
	var un = $.trim($('#unitName').val());
	if(un == '${searchUnitName!}'){
		return;
	}
	if(un.indexOf('\'')>-1||un.indexOf('%')>-1){
        layer.tips('机构名称不能包含单引号、百分号等特殊符号！',$('#unitName'), {
					tipsMore: true,
					tips: 3
				});
        return ;
    }
    var url='${request.contextPath}/newgkelective/choiceResult/query/index/page?searchUnitName='+encodeURIComponent(un);
    $('.model-div').load(url);
}

var nowUnitId = '';
//树结构
var zTree;
var demoIframe;

var setting = {
	view: {
		dblClickExpand: false,
		showLine: true,
		selectedMulti: false
	},
	data: {
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: ""
		}
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			nowUnitId = treeNode.id;
			if (treeNode.isParent) {
				zTree.expandNode(treeNode);
				$('.curSelectedNode').removeClass('curSelectedNode');
				loadUnitData(treeNode.id);
				return false;
			} else {
				loadUnitData(treeNode.id,2);
				return true;
			}
		}
	}
};

var zNodes =[
	<#list units as item>
	{"id":"${item.id!}", "pId":<#if item_index==0>0<#else>"${item.parentId!}"</#if>, "name":"${item.unitName!}"},
	</#list>   
    <#--{"id":11, "pId":1, "name":"杭州市教育局"},   
    {"id":12, "pId":1, "name":"丽水市教育局"},   
    {"id":111, "pId":11, "name":"杭州第一中学"},  
    {"id":111, "pId":12, "name":"丽水第一中学"},-->   
];


//$(document).ready(function(){
	var t = $("#tree");
	t = $.fn.zTree.init(t, setting, zNodes);
	demoIframe = $("#testIframe");
	var zTree = $.fn.zTree.getZTreeObj("tree");
	zTree.selectNode(zTree.getNodeByParam("id", 101));
//});

function loadUnitData(unitId){
	if(!unitId || unitId==''){
		layer.msg('请选择一个单位！', {
				offset: 't',
				time: 2000
			});
		return;
	}
	var gc = $('#gradeCode').val();
	var url ='${request.contextPath}/newgkelective/choiceResult/query/data/page?queryUnitId='+unitId+'&gradeCode='+gc;
	$('#resultDataDiv').load(url);
}

function clickGrade(){
	loadUnitData(nowUnitId);
}

function getDefaultData(size){
	var ar = [];
	if(size < 1){
		return ar;
	}
	for(var i=0;i<size;i++){
		ar.push('0');
	}
	return ar;
}
</script>