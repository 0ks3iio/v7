<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
	<div class="page-sidebar">
		<div class="page-sidebar-body">
			<div class="page-sidebar-body-title">年级成绩分析</div>
			<ul class="chosen-tree chosen-tree-tier1">
				<li class="active">
					<div class="chosen-tree-item" data-index="1" onclick="toShowTab('1')">
						<span class="arrow"></span>
						<span class="name">考试分排名分析</span>
					</div>
				</li>
				<li>
					<div class="chosen-tree-item" data-index="2" onclick="toShowTab('2')">
						<span class="arrow"></span>
						<span class="name">综合能力分排名分析</span>
					</div>
				</li>
				<li>
					<div class="chosen-tree-item" data-index="3" onclick="toShowTab('3')">
						<span class="arrow"></span>
						<span class="name">进步度排名分析</span>
					</div>
				</li>
				<li>
					<div class="chosen-tree-item" data-index="4" onclick="toShowTab('4')">
						<span class="arrow"></span>
						<span class="name">学科情况总体概况</span>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div class="page-content-inner"  id="showTabDiv"></div>
<script>
	$(function(){
		$("#gradeAId").trigger("click");
		toShowTab('1');
		$("#rankId").addClass("active");
	});
	function toShowTab(type){
		$("#showTabDiv").load("${request.contextPath}/examanalysis/examNewGrade/tab/page?type="+type);
	}
	/**$(".accordionContainer a.list-group-item").click(function(){
    	$(".accordionContainer a.list-group-item").removeClass("active");
    	$(this).addClass("active");
    })**/
    
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