<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<div class="page-sidebar">
	<div class="page-sidebar-body">
		<div class="page-sidebar-body-title">教学班分析（单科方向）</div>
		<ul class="chosen-tree chosen-tree-tier1">
			<li class="active">
				<div class="chosen-tree-item" data-index="1" onclick="toTeachShowTab('1')">
					<span class="arrow"></span>
					<span class="name">学生考试分排名分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toTeachShowTab('2')">
					<span class="arrow"></span>
					<span class="name">各班分数段占比分析</span>
				</div>
			</li>
			 <li>
				<div class="chosen-tree-item" data-index="20" onclick="toTeachShowTab('20')">
					<span class="arrow"></span>
					<span class="name">各班名次段占比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="3" onclick="toTeachShowTab('3')">
					<span class="arrow"></span>
					<span class="name">各班赋分等级占比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="4" onclick="toTeachShowTab('4')">
					<span class="arrow"></span>
					<span class="name">各班平均分对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="5" onclick="toTeachShowTab('10')">
					<span class="arrow"></span>
					<span class="name">各班单上线对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="6" onclick="toTeachShowTab('12')">
					<span class="arrow"></span>
					<span class="name">各班双上线对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="7" onclick="toTeachShowTab('5')">
					<span class="arrow"></span>
					<span class="name">历次考试对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="8" onclick="toTeachShowTab('13')">
					<span class="arrow"></span>
					<span class="name">学生进步度排名分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="9" onclick="toTeachShowTab('14')">
					<span class="arrow"></span>
					<span class="name">各班进步度对比分析</span>
				</div>
			</li>
		</ul>
		<div class="page-sidebar-body-title">行政班分析（总分方向）</div>
		<ul class="chosen-tree chosen-tree-tier1">
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('6')">
					<span class="arrow"></span>
					<span class="name">学生考试分排名分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('7')">
					<span class="arrow"></span>
					<span class="name">各班分数段占比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="21" onclick="toShowTab('21')">
					<span class="arrow"></span>
					<span class="name">各班名次段占比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('8')">
					<span class="arrow"></span>
					<span class="name">各班平均分对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('11')">
					<span class="arrow"></span>
					<span class="name">各班单上线对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('9')">
					<span class="arrow"></span>
					<span class="name" >历次考试对比分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('15')">
					<span class="arrow"></span>
					<span class="name">学生进步度排名分析</span>
				</div>
			</li>
			<li>
				<div class="chosen-tree-item" data-index="2" onclick="toShowTab('16')">
					<span class="arrow"></span>
					<span class="name">各班进步度对比分析</span>
				</div>
			</li>
		</ul>
	</div>
</div>
<div class="page-content-inner" id="showTabDiv"></div>

<script>
	$(function(){
		//$("#jxbAId").trigger("click");
		toTeachShowTab('1');
		//$("#jxbStuId").addClass("active");
	});
	function toShowTab(type){
		$("#showTabDiv").load("${request.contextPath}/examanalysis/examNewClass/tab/page?type="+type);
	}
	function toTeachShowTab(type){
		$("#showTabDiv").load("${request.contextPath}/examanalysis/examNewClass/teachTab/page?type="+type);
	}
	/**
	$(".accordionContainer a.list-group-item").click(function(){
    	$(".accordionContainer a.list-group-item").removeClass("active");
    	$(this).addClass("active");
    })
    **/
    
	//收缩树
	$('.page-sidebar').on('click', '.chosen-tree-item .arrow', function(e){
		e.stopPropagation();
		var $scroll = $('.page-sidebar-body'),
			$tree = $scroll.children('.chosen-tree'),
			$li = $(this).parent('.chosen-tree-item').parent('li');
		if ($li.hasClass('sub-tree')) {
			$li.toggleClass('open');
		}
		$scroll.scrollLeft(500);
		var sLeft = $scroll.scrollLeft(),
			sWidth = $scroll.width(),
			tWidth = sLeft + sWidth;
		$tree.width(tWidth);
	});
	//选中树
	$('.page-sidebar').on('click', '.chosen-tree-item', function(){
		var $li = $(this).parent('li');
		$('.chosen-tree li').removeClass('active');
		$li.addClass('active');
	});
    
</script>