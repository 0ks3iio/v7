<div class="index form-set">
    <div class="form-set-head clearfix" style="_height:90px; min-height:90px;">
    <form id="formsForm">
	    <input type="hidden" id="form-edit-id" name="id" value="${form.id!}">
	    <input type="hidden" id="form-meta-id" name="mdId" value="${form.mdId!}">
	    <input type="hidden" id="form-meta-orderId" name="orderId" value="${form.orderId!}">
	    <input type="hidden" id="form-meta-name"  name="name" value="${form.name!}">
	    <input type="hidden" id="form-meta-remark" name="remark" value="${form.remark!}">
    </form>
        <p class="ellipsis">
            <span class="name-target">表单名称：${form.name?default('暂无')}</span>
            <span class="group-target">元数据：${form.metaName?default('暂无')}</span>
            <span class="order-target">排序号：${form.orderId?default('')}</span> 
        </p>

        <p class="ellipsis"><span class="remark-target" title="${form.remark?default('暂无')}">备注：${form.remark?default('暂无')}</span></p>
        <div class="edit-box">
            <i class="iconfont icon-editor-fill"></i>
        </div>
        <!--弹窗-->
        <div class="layer layer-infor">
        	<div class="form-line">
                <span><font style="color:red;">*</font>表单名称：</span>
                <div class="">
                    <input type="text" class="form-control js-form-name" maxlength="25" <#if isEdit>value="${form.name!}"</#if>>
                </div>
            </div>
            <div class="form-line">
                <span><font style="color:red;">*</font>元数据：</span>
                <div class="">
                	<#if isEdit>
                    <select name="" class="form-control js-group" disabled>
                        <option value="${meta.id}">${meta.name}</option>
                    </select>
                    <#else>
                    <select name="" class="form-control js-group">
                    <#if metas?exists && metas?size gt 0>
	                    <#list metas as m>
	                        <option value="${m.id!}">${m.name!}</option>
	                    </#list>
                    </#if>
                    </select>
                    </#if>
                </div>
            </div>
            <div class="form-line">
                <span><font style="color:red;">*</font>排序号：</span>
                <div class="">
                    <input type="text" <#if isEdit>value="${form.orderId!}"</#if> class="form-control" id="formOrderId" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="3" >
                </div>
            </div>
            <div class="form-line">
                <span><span class="color-999">（可选）</span>备注：</span>
                <div class="">
                    <textarea maxlength="100" rows="" cols="" class="form-control js-remark" ><#if isEdit>${form.remark!}</#if></textarea>
                </div>
            </div>
        </div>	
    </div>
    
    <!-- 内容 -->
    <div id="no-meta-show" class="form-set-content-wrap ">
	    <div class="no-data-common">
	        <div class="text-center">
	            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
	            <p class="color-999">
	               点击右上角选择要设置表单的元数据吧
	            </p>
	        </div>
	    </div>
    </div>
    <div id="have-meta-show" class="form-set-content-wrap hidden">
        <!-- 类型选择 -->
        <div class="form-choose-list">
            <ul>
                <li class="js-add-form"><i class="iconfont icon-form"></i><div>表单</div></li>
                <li class="js-add-classify"><i class="iconfont icon-classification"></i><div>分类</div></li>
                <li class="js-add-line"><i class="iconfont icon-line"></i><div>辅助线</div></li>
            </ul>
        </div>
        <!-- 右键box -->
        <div class="move-box hide" data-type = "">
            
        </div>
        
        <div class="form-set-content scrollBar4">
            <!-- 内容设置 -->
            <div class="form-content-set clearfix">
                <!-- icon设置 -->
                <div class="form-icon-list">
                    <ul>
                        <li class="active"><i class="wpfont icon-cog js-cog font-16"></i></li>
                        <li><i class="iconfont icon-arrow font-14 js-up"></i></li>
                        <li class="rotateZ-180"><i class="iconfont icon-arrow font-14 js-down"></i></li>
                        <li><i class="iconfont icon-delete-bell js-delete"></i></li>
                    </ul>
                </div>
                
                <div class="form-set-list">
                    <!-- 标题设置 -->
                    <div class="form-type-made" data-type = "theme">
                        <div class="form-title"><b>主题设置</b><span class="pos-r-side"><i class="wpfont icon-close color-a5a5a5 font-16 js-close"></i></span></div>
                        <div class="form-text">主题名称</div>
                        <div class=""><input type="text" class="form-control js-text-name" maxlength="30" placeholder="给表单设置一个主题吧"/></div>
                        <div class="form-text">显示位置</div>
                        <div class="halve-box-wrap js-text-align">
                            <label for="r1-1" class="choice">
                                <input type="radio" name="r1" id="r1-1" />
                                <span class="choice-name">居左</span>
                            </label>
                            <label for="r1-2" class="choice">
                                <input type="radio" name="r1" id="r1-2" checked="checked"/>
                                <span class="choice-name">居中</span>
                            </label>
                            <label for="r1-3" class="choice">
                                <input type="radio" name="r1" id="r1-3" />
                                <span class="choice-name">居右</span>
                            </label>
                        </div>
                        <div class="form-text">样式选择</div>
                        <div class="">
                            <select name="" class="form-control js-type">
                                <option value="">样式一</option>
                                <option value="">样式二</option>
                            </select>
                        </div>
                        <div class="form-text">边距设置</div>
                        <div class="halve-box-wrap">
                            <div class="halve-box-wrap mr-10">
                                <span class="halve-name">上边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mt" value="10"/>
                            </div>
                            <div class="halve-box-wrap">
                                <span class="halve-name">下边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mb" value="0"/>
                            </div>
                        </div>
                    </div>
                    
                    <!-- 分类设置 -->
                    <div class="form-type-made hide" data-type = "classify">
                        <div class="form-title"><b>分类设置</b><span class="pos-r-side"><i class="wpfont icon-close color-a5a5a5 font-16 js-close"></i></span></div>
                        <div class="form-text">分类名称</div>
                        <div class=""><input type="text" class="form-control js-text-name" maxlength="30" placeholder="给分类设置一个名字吧" /></div>
                        <div class="form-text">显示位置</div>
                        <div class="halve-box-wrap js-text-align">
                            <label for="r2-1" class="choice">
                                <input type="radio" name="r2" id="r2-1" checked="checked"/>
                                <span class="choice-name">居左</span>
                            </label>
                            <label for="r2-2" class="choice">
                                <input type="radio" name="r2" id="r2-2" />
                                <span class="choice-name">居中</span>
                            </label>
                            <label for="r2-3" class="choice">
                                <input type="radio" name="r2" id="r2-3" />
                                <span class="choice-name">居右</span>
                            </label>
                        </div>
                        <div class="form-text">边距设置</div>
                        <div class="halve-box-wrap">
                            <div class="halve-box-wrap mr-10">
                                <span class="halve-name">上边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mt" value="10"/>
                            </div>
                            <div class="halve-box-wrap">
                                <span class="halve-name">下边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mb" value="0"/>
                            </div>
                        </div>
                    </div>
                    
                    <!-- 表单设置 -->
                    <div class="form-type-made hide" data-type = "text">
                        <div class="form-title"><b>表单设置</b><span class="pos-r-side"><i class="wpfont icon-close color-a5a5a5 font-16 js-close"></i></span></div>
                        <div class="form-text">布局选择</div>
                        <div class="three-part-wrap js-input-count">
                            <div class="three-part active">
                                <div class="three-part-area"></div>
                            </div>
                            <div class="three-part flex ">
                                <div class="three-part-area"></div>
                                <div class="three-part-area"></div>
                            </div>
                            <div class="three-part flex three">
                                <div class="three-part-area"></div>
                                <div class="three-part-area"></div>
                                <div class="three-part-area"></div>
                            </div>
                        </div>
                        <div class="form-name-set">
                            <div class="form-text">第一个字段</div>
                            <select name="" class="form-control js-add-name" data-which = "0">
                                <option value=""></option>
                            </select>
                        </div>
                        <div class="form-name-set hide">
                            <div class="form-text">第二个字段</div>
                            <select name="" class="form-control js-add-name" data-which = "1">
                                <option value=""></option>
                            </select>
                        </div>
                        <div class="form-name-set hide">
                            <div class="form-text">第三个字段</div>
                            <select name="" class="form-control js-add-name" data-which = "2">
                                <option value=""></option>
                            </select>
                        </div>
                        <div class="form-text">边距设置</div>
                        <div class="halve-box-wrap">
                            <div class="halve-box-wrap mr-10">
                                <span class="halve-name">上边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mt" value="10"/>
                            </div>
                            <div class="halve-box-wrap">
                                <span class="halve-name">下边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mb" value="0"/>
                            </div>
                        </div>
                    </div>
                    
                    <!-- 辅助线设置 -->
                    <div class="form-type-made hide" data-type = "line">
                        <div class="form-title"><b>辅助线设置</b><span class="pos-r-side"><i class="wpfont icon-close color-a5a5a5 font-16 js-close"></i></span></div>
                        <div class="form-text">边距设置</div>
                        <div class="halve-box-wrap">
                            <div class="halve-box-wrap mr-10">
                                <span class="halve-name">上边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mt" value="10"/>
                            </div>
                            <div class="halve-box-wrap">
                                <span class="halve-name">下边距 &nbsp;&nbsp;</span>
                                <input type="number" min="0" name="" class="form-control js-mb" value="0"/>
                            </div>
                        </div>
                    </div>
                </div>
                
            </div>
            
            <!-- 内容 -->
            <div class="form-item-wrap">
    			<#if isEdit>
    			${html!}
				<#else>
                <div class="form-items">
                    <div class="form-item item-theme selected" data-type = "theme"  style="text-align: center;" data-class = "">
                        <span>我是主题</span>
                    </div>
	                 <!-- 模块放置区域 -->
	                <div class="form-item-place hide">
	                    <div>模块放置区域</div>
	                </div>
                </div>
                
                <!-- 底部操作 -->
                <div class="form-bottom">
                    <button type="button" onclick="formSetSave()" class="btn btn-blue mr-10">保存</button>
                </div>
                <script type="text/javascript">
                </script>
				</#if>
            </div>
        
        </div>
    </div>
</div>
<script type="text/javascript">
var isShow =false;
	$(function(){
		var colunResults;
        // 头部编辑
        $('.edit-box').on('click',function(){
            layer.open({
            	type: 1,
            	title: '表单设置',
            	area: ['520px', 'auto'],
            	btn: ['确定', '取消'],
            	yes: function(index){
				    addMetaPage();
				  },
            	content: $('.layer-infor')
            });
        });
        <#if isEdit>
		getMetaColumn("${form.mdId!}");
		<#else>
			layer.open({
            	type: 1,
            	title: '表单设置',
            	area: ['520px', 'auto'],
            	btn: ['确定', '取消'],
            	yes: function(index){
				    addMetaPage();
				  },
            	btn2: function(index){
				     router.go({
						path: '/bigdata/metadata/forms'
					});
				  },
            	content: $('.layer-infor')
            });
		</#if>
        
        function addMetaPage(){
        	var name = $('.js-form-name').val().trim() == ''?'':$('.js-form-name').val();
            var group = $('.js-group option:selected').text().trim() == ''?'':$('.js-group option:selected').text();
            var remark = $('.js-remark').val().trim() == ''?'':$('.js-remark').val();
            if (name == "") {
                layer.tips("不能为空", ".js-form-name", {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
            if (group == "") {
                layer.tips("不能为空", ".js-group", {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
            if ($('#formOrderId').val() == "") {
                layer.tips("不能为空", "#formOrderId", {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
            
            $('.name-target').text("表单名称："+name);
            $('.group-target').text("元数据："+group);
            $('.remark-target').text("备注："+remark);
            $('.order-target').text("排序号："+$('#formOrderId').val());
            var metaId = $('.js-group option:selected').val();
            $('#form-meta-id').val(metaId);
            $('#form-meta-name').val(name);
            $('#form-meta-remark').val(remark);
            $('#form-meta-orderId').val($('#formOrderId').val());
            if(metaId&&metaId!='')getMetaColumn(metaId);
            layer.closeAll();
        }
        
        //防止内容被选中
        $(document).bind("selectstart", function(e) {return false;});
        
        // 添加内容类型
        $('.form-choose-list li').on('mousedown',function(e){
            $('.move-box').removeClass('hide').attr('data-type',$(this).index()).append($(this).children().clone()).css({
                left: e.pageX + 4,
                top: e.pageY + 4
            });
            $('.form-item-place').removeClass('hide');
        });
        $("#have-meta-show").on('mousemove', function(e) {
            if ($('.move-box').hasClass('hide') == false){
                $('.move-box').css({
                    left: e.pageX + 4,
                    top: e.pageY + 4
                })
            }
        }).on('mouseup',function(e){
            if (isShow && $('.move-box').hasClass('hide') == false){
                var str;
                if ($('.move-box').attr('data-type') == 0){
                    str = '<div class="form-item item-text halve-table-wrap selected" data-type = "text">\
                                <div class="">\
                                    <div class="halve-box-wrap">\
                                        <span class="halve-name">字段名：</span>\
                                        <div class="lh-32" data-type="">字段值</div>\
                                    </div>\
                                </div>\
                                <div class="hide">\
                                    <div class="halve-box-wrap">\
                                        <span class="halve-name">字段名：</span>\
                                        <div class="lh-32" data-type="">字段值</div>\
                                    </div>\
                                </div>\
                                <div class="hide">\
                                    <div class="halve-box-wrap">\
                                        <span class="halve-name">字段名：</span>\
                                        <div class="lh-32" data-type="">字段值</div>\
                                    </div>\
                                </div>\
                            </div>';
                    
                } else if($('.move-box').attr('data-type') == 1){
                    str = '<div class="form-item item-classify selected" data-type = "classify">\
                                <span>分类</span>\
                            </div>';
                    
                } else if($('.move-box').attr('data-type') == 2){
                    str = '<div class="form-item item-line selected" data-type = "line">\
                                <div></div>\
                            </div>';
                    
                }
                $('.form-item.selected').removeClass('selected');
                $('.form-item-place').before(str);      
                setShow();
                $('.move-box').empty().addClass('hide');
                $('.form-item-place').appendTo($('.form-items')).addClass('hide');
                $('.js-input-count .three-part').eq(0).click();
                $('.form-set-list .form-type-made[data-type = "text"]').find('.js-mt').val(10);;
                $('.form-set-list .form-type-made[data-type = "text"]').find('.js-mb').val(0);;
            }
        });
        
        // form-item 选中状态
        $('.form-items').on('click','.form-item',function(e){
            if ($(this).hasClass('active') == false){
                $(this).addClass('selected').siblings('.form-item').removeClass('selected');
                setShow();
            	$('.js-delete').parent('li').show();
                if($(this).attr('data-type') == 'theme'){
                	backShowTheme($(this));
                	$('.js-delete').parent('li').hide();
                }else if($(this).attr('data-type') == 'classify'){
                	backShowClassify($(this));
                }else if($(this).attr('data-type') == 'text'){
                	backShowText($(this));
                }else if($(this).attr('data-type') == 'line'){
                	backShowLine($(this));
                }
            }
        }).on('mousemove','.form-item',function(e){
        });
        //回显主题
        function backShowTheme(srcObj){
        	var obj = $('.form-set-list .form-type-made[data-type = "theme"]');
        	obj.find('.js-text-name').val(srcObj.find('span').text());
        	obj.find('.js-type').find('option').eq(srcObj.attr('data-class')).prop('selected',true);
        	var pos = srcObj.css('text-align');
        	if(pos == 'center'){
        		obj.find('.js-text-align label').eq(1).find('input').prop("checked", true);
        	}else if(pos == 'right'){
        		obj.find('.js-text-align label').eq(2).find('input').prop("checked", true);
        	}else{
        		obj.find('.js-text-align label').eq(0).find('input').prop("checked", true);
        	}
        	var top = srcObj.css('margin-top').replace('px', '');
			var bottom = srcObj.css('margin-bottom').replace('px', '');
			obj.find('.js-mt').val(top);
			obj.find('.js-mb').val(bottom);
        }
        //回显分类
        function backShowClassify(srcObj){
        	var obj = $('.form-set-list .form-type-made[data-type = "classify"]');
        	obj.find('.js-text-name').val(srcObj.find('span').text());
        	var pos = srcObj.css('text-align');
        	if(pos == 'center'){
        		obj.find('.js-text-align label').eq(1).find('input').prop("checked", true);
        	}else if(pos == 'right'){
        		obj.find('.js-text-align label').eq(2).find('input').prop("checked", true);
        	}else{
        		obj.find('.js-text-align label').eq(0).find('input').prop("checked", true);
        	}
        	var top = srcObj.css('margin-top').replace('px', '');
			var bottom = srcObj.css('margin-bottom').replace('px', '');
			obj.find('.js-mt').val(top);
			obj.find('.js-mb').val(bottom);
        }
        
        //回显表单
        function backShowText(srcObj){
        	var obj = $('.form-set-list .form-type-made[data-type = "text"]');
        	obj.find('.js-input-count').children().eq(2-srcObj.children('.hide').length).addClass('active').siblings().removeClass('active');
        	var curren = obj.find('.form-name-set').eq(2-srcObj.children('.hide').length).removeClass('hide');
        	curren.prevAll('.form-name-set').removeClass('hide');
        	curren.nextAll('.form-name-set').addClass('hide');
        	srcObj.children().each(function(i,e){
        		if ($(this).hasClass('hide') == false){
        			var val = $(this).find('.lh-32').attr('data-type');
        			obj.find('.form-name-set').eq(i).find('option[value="'+val+'"]').prop("selected", true);
        		}
			});
			var top = srcObj.css('margin-top').replace('px', '');
			var bottom = srcObj.css('margin-bottom').replace('px', '');
			obj.find('.js-mt').val(top);
			obj.find('.js-mb').val(bottom);
        }
        
        //回显线
        function backShowLine(srcObj){
        	var obj = $('.form-set-list .form-type-made[data-type = "line"]');
        	var top = srcObj.css('margin-top').replace('px', '');
			var bottom = srcObj.css('margin-bottom').replace('px', '');
			obj.find('.js-mt').val(top);
			obj.find('.js-mb').val(bottom);
        }
        //判断哪个设置项
        function setShow(){
            if ($('.js-cog').closest('li').hasClass('active') == false){
                $('.js-cog').closest('li').addClass('active');
                $('.form-set-list').removeClass('hide');
                $('.form-set-list .form-type-made[data-type = "' + $('.form-item.selected').data("type") + '"]').removeClass('hide').siblings().addClass('hide');
                
            } else {
                $('.form-set-list .form-type-made[data-type = "' + $('.form-item.selected').data("type") + '"]').removeClass('hide').siblings().addClass('hide');
            }
            $('.form-content-set').css('top',($('.form-item.selected').offset().top - 211 + $('.form-set-content').scrollTop()) + 'px')
        }
        //判断设置项定位
        function setPosition(){
            $('.form-content-set').css('top',($('.form-item.selected').get(0).getBoundingClientRect().top - 211 + $('.form-set-content').scrollTop()) + 'px')
        }
        
        
        // 内容设置
        $('.form-set').on('click','.js-close',function(){
            $(this).closest('.form-set-list').addClass('hide');
            $('.form-icon-list li').first().removeClass('active');
        });
        // 文本内容 
        $('.form-set').on('input propertychange','.js-text-name',function(){
            $('.form-item.selected span').text($(this).val());
        });
        // 文本位置
        $('.form-set').on('change','.js-text-align label',function(){
            if ($(this).index() == 0){
                $('.form-item.selected').css('text-align','left')
                
            } else if ($(this).index() == 1) {
                $('.form-item.selected').css('text-align','center')
            } else {
                $('.form-item.selected').css('text-align','right')
            }
        });
        //样式
        $('.form-set').on('change','.js-type',function(){
            $('.form-item.selected').attr('data-class',$(this).find('option:selected').index());
        });
        // 边距
        $('.form-set').on('input propertychange','.js-mt',function(){
        	if($(this).val()<0){
	        	$(this).val(0);
        	}
        	if($(this).val()>99){
	        	$(this).val(99);
        	}
            $('.form-item.selected').css('margin-top',$(this).val() + 'px');
        });
        $('.form-set').on('input propertychange','.js-mb',function(){
        	if($(this).val()<0){
	        	$(this).val(0);
        	}
        	if($(this).val()>99){
	        	$(this).val(99);
        	}
            $('.form-item.selected').css('margin-bottom',$(this).val() + 'px');
        });
        // 选择字段
        $('.form-set').on('change','.js-add-name',function(){
            $('.form-item.selected .halve-box-wrap').eq($(this).data('which')).find('.halve-name').text($(this).find('option:selected').text() + '：');
            $('.form-item.selected .halve-box-wrap').eq($(this).data('which')).find('.lh-32').attr('data-type',$(this).find('option:selected').val()).text('{#'+$(this).find('option:selected').val() + '}');
        });
        // 表单数量分布
        $('.form-set').on('click','.js-input-count .three-part',function(){
                $(this).addClass('active').siblings().removeClass('active');
                var formNameSet = $('.form-name-set').eq($(this).index());
                var formItem =  $('.form-item.selected').children().eq($(this).index());
                formNameSet.find('option').eq($(this).index()).prop('selected',true);
                formNameSet.removeClass('hide').nextAll('.form-name-set').addClass('hide');
                formNameSet.prevAll('.form-name-set').each(function(i,e){
                	if($(this).hasClass('hide')){
		                $(this).removeClass('hide');
		                $(this).find('select').change();
                	}
                });
                formNameSet.find('select').change();
                formItem.removeClass('hide').nextAll().addClass('hide');
                formItem.prevAll().removeClass('hide');
        });
        
        
        // icon设置
        $('.js-cog').on('click',function(){
            $(this).closest('li').toggleClass('active');
            $('.form-set-list').toggleClass('hide');
        });
        $('.js-up').on('click',function(){
            if ($('.form-item.selected').length > 0){
                if ($('.form-item.selected').prev().is('.form-item')){
                    $('.form-item.selected').insertBefore($('.form-item.selected').prev());
                }
            }
        });
        $('.js-down').on('click',function(){
            if ($('.form-item.selected').length > 0){
                if ($('.form-item.selected').next().is('.form-item')){
                    $('.form-item.selected').insertAfter($('.form-item.selected').next());
                }
            }
        });
        $('.js-delete').on('click',function(){
        	if($('.form-item.selected').attr('data-type') == 'theme'){
        		return;
        	}
            if ($('.form-item.selected').length > 0){
                if ($('.form-item.selected').prev().is('.form-item')){
	            	var pre = $('.form-item.selected').prev();
	                $('.form-item.selected').remove();
                	pre.click();
                }else if($('.form-item.selected').next().is('.form-item')){
	            	var next = $('.form-item.selected').next();
	                $('.form-item.selected').remove();
                	$('.form-item.selected').remove();
                	next.click();
                }
            }
        });
	
	function getMetaColumn(metaId) {
		colunResults = null;
	    $.ajax({
	        url: '${request.contextPath}/bigdata/metadata/'+metaId+'/getField',
	        type: 'GET',
	        data : {},
	        dataType: 'json',
	        success: function (val) {
	            if (val.success) {
	            	colunResults= val.data;
	            	if(colunResults.length>0){
	            		$('#no-meta-show').addClass('hidden');
	            		$('#have-meta-show').removeClass('hidden');
	            		isShow = true;
	            		var htmlStr = '';
	            		$.each(colunResults, function (index, value) {
							htmlStr+='<option value="'+value.columnName+'">'+value.name+'</option>';
						});
	            		$('.form-type-made[data-type="text"] .form-name-set .js-add-name').each(function(index){
							$(this).html(htmlStr);
						});
	            		<#if isEdit>
	            		$('.form-item.selected').click();
	            		<#else>
	            		reSetForm();
						</#if>
	            	}else{
	            		$('#have-meta-show').addClass('hidden');
	            		$('#no-meta-show').removeClass('hidden');
	            		isShow = false;
	            	}
	            }else{
	        		$('#have-meta-show').addClass('hidden');
	        		$('#no-meta-show').removeClass('hidden');
	        		isShow = false;
	            } 
	        }
	    });
	}
	function reSetForm() {
		var html = '<div class="form-item item-theme selected" data-type = "theme" style="text-align: center;" data-class = ""><span>主题</span></div><div class="form-item-place hide"><div>模块放置区域</div></div>';
	    $('.form-items').html(html);
	    setShow();                                  
	}
      
})
var isSubmit = false;
function formSetSave() {
	if(isSubmit){
		return;
	}
	var content = $(".form-item-wrap").html();
	
	isSubmit = true;
	var options = {
        url: "${request.contextPath}/bigdata/metadata/forms/save",
        dataType: 'json',
        data:{content:content},
        success: function (data) {
            if (!data.success) {
            	showLayerTips4Confirm('error',data.message);
                isSubmit = false;
            } else {
                showLayerTips('success','保存成功!','t');
                router.go({
					path: '/bigdata/metadata/forms'
				});
            }
        },
        clearForm: false,
        resetForm: false,
        type: 'post',
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }//请求出错
    };
    $("#formsForm").ajaxSubmit(options);                                  
}
function backFormList() {

}
</script>	