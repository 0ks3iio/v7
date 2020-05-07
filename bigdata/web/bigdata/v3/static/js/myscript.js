var count = 0;
$('[data-toggle="tooltip"]').tooltip();
//模块选择
// $('.static-sidebar li').each(function(index,ele){
// 	$(this).click(function(){
// 		$(this).addClass('active').siblings('li').removeClass('active');
// 		if ($(this).data('type') != 'single') {
// 			$('.dynamic-sidebar').addClass('active');
// 			$('.main-content').css('margin-left','220px');
// 			$('.fold-icon').find('i').removeClass('icon-packup-fill').addClass('icon-unfold-fill');
// 			$('.dynamic-sidebar').children('ul').eq(index - 2).addClass('active').siblings('ul').removeClass('active');
//
// 		} else {
// 			$('.dynamic-sidebar').removeClass('active');
// 			$('.main-content').css('margin-left','50px');
// 			$('.dynamic-sidebar').children('ul').removeClass('active');
// 			$('.fold-icon').find('i').removeClass('icon-unfold-fill').addClass('icon-packup-fill');
// 		}
// 	})
// });
//隐藏侧边栏
$('.fold-icon').on('click',foldSide);
function foldSide(){
	$('.dynamic-sidebar').toggleClass('active');
	$('.main-content').toggleClass('active');
	if ($('.fold-icon').find('i').hasClass('icon-packup-fill')) {
		$('.fold-icon').find('i').removeClass('icon-packup-fill').addClass('icon-unfold-fill')
	} else {
		$('.fold-icon').find('i').removeClass('icon-unfold-fill').addClass('icon-packup-fill')
	}
}
//侧边栏点击
$('.dynamic-sidebar a').each(function(index,ele){
	$(this).click(function(e){
		$('.dynamic-sidebar a').removeClass('active');
		$(this).addClass('active');
	});
});
//目录
$('body').on('click','.directory-tree li',function(e){
	$(this).addClass('active').siblings().removeClass('active').find('li').removeClass('active');
});

//添加下级目录
$('body').on('click','.js-add-directory',function(e){
	e.stopPropagation();
	count ++;
	if ($(this).closest('ul').hasClass('collapse')) {
		//添加三级
		if ($(this).closest('a').next().is('ul')) {
			var str = '<li>\
							<a href="#">\
								<span>三级目录</span>\
								<div class="pos-icon">\
									<i class="iconfont icon-editor-fill js-edit-directory"></i>\
									<i class="iconfont icon-delete-bell js-remove-directory"></i>\
								</div>\
							</a>\
						</li>';
			$(this).closest('a').next('ul').append(str);			
			
		} else {
			var str = '<ul class="collapse" id="a'+ count +'">\
							<li>\
								<a href="#">\
									<span>三级目录</span>\
									<div class="pos-icon">\
										<i class="iconfont icon-editor-fill js-edit-directory"></i>\
										<i class="iconfont icon-delete-bell js-remove-directory"></i>\
									</div>\
								</a>\
							</li>\
						</ul>';
			$(this).closest('a').attr('href','#a'+ count).prepend('<span class="multilevel"><i class="iconfont icon-caret-down"></i></span>').after(str);
		}
	} else {    
		//添加二级
		if ($(this).closest('a').next().is('ul')) {
			var str = '<li>\
							<a href="#" data-toggle="collapse">\
								<span>二级目录</span>\
								<div class="pos-icon">\
									<i class="iconfont icon-adddirectory-b js-add-directory"></i>\
									<i class="iconfont icon-editor-fill js-edit-directory"></i>\
									<i class="iconfont icon-delete-bell js-remove-directory"></i>\
								</div>\
							</a>\
						</li>';
			$(this).closest('a').next('ul').append(str);			
			
		} else {
			var str = '<ul class="collapse" id="a'+ count +'">\
							<li>\
								<a href="#" data-toggle="collapse">\
									<span>二级目录</span>\
									<div class="pos-icon">\
										<i class="iconfont icon-adddirectory-b js-add-directory"></i>\
										<i class="iconfont icon-editor-fill js-edit-directory"></i>\
										<i class="iconfont icon-delete-bell js-remove-directory"></i>\
									</div>\
								</a>\
							</li>\
						</ul>';
			$(this).closest('a').attr('href','#a'+ count).prepend('<span class="multilevel"><i class="iconfont icon-caret-down"></i></span>').after(str);
		}
	}
});




// 通知滚动
(function($){
	var $scrollBoxs = $('[data-action=scroll]');
	if($scrollBoxs.length > 0){
		$scrollBoxs.each(function(){
			var _this = $(this);
			if(_this.outerHeight() > _this.parent().outerHeight()){
				var timer = setInterval(function(){
					if(_this.hasClass('post-list-1of2')){
						_this.children().eq(0).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
						_this.children().eq(1).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
					}else{
						_this.children().eq(0).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
					}
				},5000)
			}
		})
	}
})(jQuery);

//$(function(){
//	//树结构
//	var setting = {
//      view: {
//          addHoverDom: addHoverDom,
//          removeHoverDom: removeHoverDom,
//          selectedMulti: false
//      },
//      check: {
//      	//chkboxType : { "Y" : "", "N" : "" },
//          enable: true
//      },
//      data: {
//          simpleData: {
//              enable: true
//          }
//      },
//      edit: {
//          enable: true
//      }
//  };
//  var zNodes =[
//      { id:1, pId:0, name:"父节点1", open:true},
//      { id:11, pId:1, name:"父节点11"},
//      { id:111, pId:11, name:"叶子节点111"},
//      { id:112, pId:11, name:"叶子节点112"},
//      { id:113, pId:11, name:"叶子节点113"},
//      { id:114, pId:11, name:"叶子节点114"},
//      { id:12, pId:1, name:"父节点12"},
//      { id:121, pId:12, name:"叶子节点121"},
//      { id:122, pId:12, name:"叶子节点122"},
//      { id:123, pId:12, name:"叶子节点123"},
//      { id:124, pId:12, name:"叶子节点124"},
//      { id:13, pId:1, name:"父节点13", isParent:true},
//      { id:2, pId:0, name:"父节点2"},
//      { id:21, pId:2, name:"父节点21", open:true},
//      { id:211, pId:21, name:"叶子节点211"},
//      { id:212, pId:21, name:"叶子节点212"},
//      { id:213, pId:21, name:"叶子节点213"},
//      { id:214, pId:21, name:"叶子节点214"},
//      { id:22, pId:2, name:"父节点22"},
//      { id:221, pId:22, name:"叶子节点221"},
//      { id:222, pId:22, name:"叶子节点222"},
//      { id:223, pId:22, name:"叶子节点223"},
//      { id:224, pId:22, name:"叶子节点224"},
//      { id:23, pId:2, name:"父节点23"},
//      { id:231, pId:23, name:"叶子节点231"},
//      { id:232, pId:23, name:"叶子节点232"},
//      { id:233, pId:23, name:"叶子节点233"},
//      { id:234, pId:23, name:"叶子节点234"},
//      { id:3, pId:0, name:"父节点3", isParent:true}
//  ];
//
//	$.fn.zTree.init($("#treeDemo-one"), setting, zNodes);
//	
//  var newCount = 1;
//  function addHoverDom(treeId, treeNode) {
//      var sObj = $("#" + treeNode.tId + "_span");
//      if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
//      var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
//          + "' title='add node' onfocus='this.blur();'></span>";
//      sObj.after(addStr);
//      var btn = $("#addBtn_"+treeNode.tId);
//      if (btn) btn.bind("click", function(){
//          var zTree = $.fn.zTree.getZTreeObj("treeDemo-one");
//          zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
//          return false;
//      });
//  };
//  function removeHoverDom(treeId, treeNode) {
//      $("#addBtn_"+treeNode.tId).unbind().remove();
//  };
//});
