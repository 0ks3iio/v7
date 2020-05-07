//$('.form-control').tags();
//标签输入
;
(function($, window, document, undefined){
	//默认参数
	var defaults = {
		
	};
	
	//定义构造函数
	function Tags(element, setting) {
		this.element = element;
		this.options = $.extend({}, defaults, setting);
		this.init();
	};

	//定义方法
	Tags.prototype = {
		//初始化
		init: function() {
			//console.log(this.options);
			var _ = this,
				_el = $(this.element),
				inputWidth = _el.outerWidth() + 'px',
				inputVal = _el.val(),
				tagSlist = inputVal.split(','),
				str = '';
			for (i in tagSlist) {
				//console.log(tagSlist[i]);
				str += '<li><span>' + tagSlist[i] + '</span><i>x</i></li>';
			}
			_el.hide().wrap('<div class="form-tags" style="width: ' + inputWidth + '"></div>').after('<ul class="form-tags-list"></ul><input type="text" maxlength="20" class="form-tags-input" />');
			_tags = _el.parent('.form-tags'),
			_list = _el.siblings('.form-tags-list'),
			_input = _el.siblings('.form-tags-input');
			_list.append(str);
			//选中
			_tags.on('click.tags', function(ev) {
				_input.focus();
			});
			//输入
			_input.on('keyup.tags', function(ev) {
				e = ev;
				_.keyup();
			});
			//生成
			_input.on('keydown.tags', function(ev) {
				e = ev;
				_.enter();
			});
			//删除
			_list.on('click.tags', 'li i', function(ev) {
				e = ev;
				_i = $(this);
				_.remove();
			});
		},
		//输入
		keyup: function() {
			var _ = this,
				_el = $(this.element),
				_input = _el.siblings('.form-tags-input');
			keyCode = e.keyCode || e.which;
			if (keyCode === 8 || keyCode === 46 || (keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 96 && keyCode <= 111 && keyCode != 108) || (keyCode >= 186 && keyCode <= 192) || (keyCode >= 219 && keyCode <= 222)) {
				_input.width(_input.width() + 20);
			}
		},
		//生成
		enter: function() {
			var _ = this,
				_el = $(this.element),
				_input = _el.siblings('.form-tags-input');
			keyCode = e.keyCode || e.which;
			if (keyCode === 32) {
				e.preventDefault();
				var val = _input.val(),
					tags = _el.val().split(','),
					hasTag = false;
				$.each(tags, function (index, value) {
					if ($.trim(value) === $.trim(val)) {
						hasTag = true;
					}
				});
				if (val !== '') {
					if (!hasTag) {
						_list.append('<li><span>' + val + '</span><i>x</i></li>');
						_el.val(_el.val() + ',' + val);
					} else{
						//调用layer.js
						layer.msg('此标签已存在！', {
							icon: 2,
							time: 1500,
							shade: 0.2
						});
					}
					_input.width(20).val('');
				}
			}
		},
		//删除
		remove: function() {
			var _ = this,
				_el = $(this.element),
				_list = _el.siblings('.form-tags-list'),
				val = '';
			e.preventDefault();
			e.stopPropagation();
			_i.parent('li').remove();
			_list.children('li').each(function(){
				val += $(this).children('span').text() + ',';
			});
			val = val.substring(0, val.lastIndexOf(','));
			_el.val(val);
		}
	};
	
	//定义插件
	$.fn.tags = function(options) {
        return this.each(function(index, element) {
            element.tags = new Tags(element, options);
        });
	};
	
})(jQuery, window, document);