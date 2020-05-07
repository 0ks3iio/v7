<div class="box box-default rule-setup">
            <div class="form-horizontal layer-dataSet">
                <div class="form-group">
                    <h3 class="col-sm-2 control-label no-padding-right bold">基本信息　</h3>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称　</label>
                    <div class="col-sm-6">
                        <input type="text" maxlength="50" nullable="false" id="name" value="${dataSet.name!}" class="form-control js-file-name width-1of1" placeholder="请输入名称">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>元数据</label>
                    <div class="col-sm-6">
	                    <select id="mdId" name="mdId" nullable="false" class="form-control" nullable="false" <#if dataSet.mdId?exists> disabled="true" </#if>  onchange="changeTableColumn()">
		                	<#if metadatas?exists && metadatas?size gt 0>
								<#list metadatas as db>
				                    <option <#if dataSet.mdId! == db.id!>selected="selected"</#if> value="${db.id!}" dbType="${db.type!}">${db.name!}</omption>
				               </#list>
							</#if>     
	                    </select>
                    </div>
                </div>
                <div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>描述：</label>
					<div class="col-sm-6">
						<div class="textarea-container">
			    			<textarea  name="remark"  nullable="false" maxLength="30" id="remark"  cols="30" rows="5" class="form-control js-limit-word" value = "${dataSet.remark!}">${dataSet.remark!}</textarea>
			    		</div>
					</div>
			   </div>
                <div class="form-group">
                  <h4 class="col-sm-2 control-label no-padding-right bold no-margin">数据集规则　</h4>
                    <div class="col-sm-6">
                        <div id="collapseExample" class="collapse in" aria-expanded="true">
	                    	<div class="all-events">
	                    		<div class="events first-event">
	                    		  <#if dataSetRuleDtos?exists && dataSetRuleDtos?size gt 0>
	                    		      <#list dataSetRuleDtos as item>
	                    		            <#assign index = item_index + 1>
			                    			<div class="full-event js-remove-target">
					                      		<div class="event-head padding-l-30 pos-rel clearfix">
					                      			<div class="add-btn pos-left-20 pointer js-add-event">+</div>
						                       		<div class="filter-item">
						                                <div class="event-num pointer">规则${index}</div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="filter-content">
						                                    <select id = "paramType" name="paramType" class="form-control width-auto" disabled="true">
						                                        <option <#if item.paramType == '1'> selected="selected"  </#if> value="1">字段</option>
						                                        <option <#if item.paramType == '2'> selected="selected"  </#if> value="2">sql语句</option>
						                                    </select>
						                                </div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="filter-name line-h-36">的类型</div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="event-remove pointer"><i class="fa fa-times-circle  js-remove"></i></div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="filter-name line-h-36 pointer filter-condition js-add-condition <#if item.paramType == '2'>  hide </#if> " 
						                                  >+筛选条件</div>
						                                
						                            </div>
			                      		        </div>
		                            			<div class="event-body">
			                            			<div class="condition-body">
			                            				<#if item.paramType == '1'>
				                            				<div class="more-condition"  <#if item.ruleXXs?exists && item.ruleXXs?size gt 0 > style="display: block;" </#if>>
				                            					<div class="and js-and-or">${item.relationName!}</div>
				                            				</div>
			                            				    <#if item.ruleXXs?exists && item.ruleXXs?size gt 0>
				                            				   <#list item.ruleXXs as ru>
				                            				      <div class="condition-detail clearfix js-condition-remove-target">
				                            				            <div class="filter-item">
				                            				                <div class="filter-content">
				                            				                    <select name="columnName" class="form-control">
				                            				                       <#if columnNames?exists && columnNames?size gt 0>
				                            				                            <#list columnNames as c>
				                            				                               <option <#if ru.columnName == c.columnName > selected="selected"  </#if>  value='${c.columnName!}'>${c.name!}</option>
				                            				                            </#list>
				                            				                       </#if>
				                            				                    </select>
				                            				                 </div>
				                            				             </div>
				                            				             <div class="filter-item">
					                            				             <div class="filter-content">
														                        <select name="rule" class="form-control width-auto">
														                            <option <#if ru.rule == '='> selected="selected"  </#if> value="=">等于</option>
														                            <option <#if ru.rule == '>'> selected="selected"  </#if> value=">">大于</option>
														                            <option <#if ru.rule == '<'> selected="selected"  </#if> value="<">小于</option>
														                        </select>
														                    </div>
														                  </div>
														                  <div class="filter-item">
														                    <div class="filter-content">
														                        <input maxlength="12" type="text" value="${ru.ruleValue!}" name="ruleValue"  id="form-field-4" class="form-control">
														                    </div>
														                  </div>
														                  <div class="filter-item">
														                     <div class="event-remove pointer"><i class="fa fa-times-circle js-condition-remove"></i></div>
														                  </div>
														              </div>
				                            				   </#list>
				                            				</#if>
			                            				<#else>
			                            				       <div class="condition-detail clearfix js-condition-remove-target">
																	<div class="filter-item">
															           <div class="filter-content">
															               <textarea  name="sqlValue"  id="sqlValue"  cols="30"
															               value = "${item.value!}">${item.value!}
															               </textarea>
															           </div>
															        </div>
															   </div>
			                            				</#if>
		                            			   </div>
				                     		   </div>
				                     		</div>
		                     		      </#list>
		                     		   <#else>
		                     		       <div class="full-event js-remove-target">
					                      		<div class="event-head padding-l-30 pos-rel clearfix">
					                      			<div class="add-btn pos-left-20 pointer js-add-event">+</div>
						                       		<div class="filter-item">
						                                <div class="event-num pointer">规则1</div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="filter-content">
						                                    <select id = "paramType" name="paramType" class="form-control width-auto">
						                                        <option value="1">字段</option>
						                                        <option value="2">sql语句</option>
						                                    </select>
						                                </div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="filter-name line-h-36">的类型</div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="event-remove pointer"><i class="fa fa-times-circle hide js-remove"></i></div>
						                            </div>
						                            <div class="filter-item">
						                                <div class="filter-name line-h-36 pointer filter-condition js-add-condition">+筛选条件</div>
						                                
						                            </div>
			                      		        </div>
		                            			<div class="event-body">
			                            			<div class="condition-body">
			                            				<div class="more-condition">
			                            					<div class="and js-and-or">且</div>
			                            				</div>
			                            				
			                            			</div>
		                            			</div>
				                     		 </div>
	                    		       </#if>
		                    	  </div>
		                       </div>
	                    </div>
	               </div> 
                </div>
                <div class="form-group">
                   <div class="col-sm-6 col-sm-offset-2">
                       <button class="btn btn-blue" type="button" id="saveBtn">保存</button>
                   </div>
               </div>
            </div>
        </div>
</div>
<input type="hidden" id="dataSetId" value="${dataSet.id!}">
<script type="text/javascript">
     var showColumns;
     var mdid;
     $(function(){
    	 changeTableColumn();
     })

     function tips(msg, key) {
        layer.tips(msg, key, {
            tipsMore: true,
            tips: 3,
            time: 2000
        });
    }
     
     function changeTableColumn(){
	     $.ajax({
	         url: '${request.contextPath}/bigdata/dataset/getMetadataTableColumn',
	         type: 'POST',
	         data : {mdId:$('#mdId').val()},
	         dataType: 'json',
	         success: function (val) {
	             if (!val.success) {
	                 layer.msg(val.message, {icon: 2});
	             }
	             else {
	            	 showColumns = val.data;
	             }
	           }
	         });
     }
     
     var isSubmit=false;
     $('#saveBtn').click(function () {
    	 if(isSubmit){
 			return;
 		}
 		isSubmit = true;
 		var check = checkValue('.layer-dataSet');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
    	var metadataId = $('#mdId').val();
    	var name =  $('#name').val();
    	var remark = $('#remark').val();
    	var isTrue = true;
    	var dataSetRules = new Array();
    	var all_li = $('.all-events').find('.full-event');
        $.each(all_li, function (index, value) {
            var paramType = $(value).find('select[name="paramType"]').find("option:selected").val();
            var paramValue = '';
            var paramJson  = new Array();
           	var rulexx_li = $(value).find('.condition-detail');
            var relation = $(value).find('.js-and-or').html();
            $.each(rulexx_li, function (index, value) {
                	 if(paramType == 1){
		    	            var columnName = $(value).find('select[name="columnName"]').find("option:selected").val();
		    	            var rule =  $(value).find('select[name="rule"]').find("option:selected").val();
		    	            var ruleValue = $(value).find('input[name="ruleValue"]').val();
		    		        if (columnName == null) {
		    		              tips("请先勾选字段", $(value).find('select[name="columnName"]'));
		    		              window.scrollTo(0, $(value).find('select[name="columnName"]').offset().top);
		    		              isTrue = false;
		    		              return;
		    	            }
		    		        if (!$(value).find('input[name="ruleValue"]').is(':hidden') && $.trim(ruleValue) == '') {
		    		              tips(columnName + "结果值不能为空!", $(value).find('input[name="ruleValue"]'));
		    		              window.scrollTo(0, $(value).find('input[name="ruleValue"]').offset().top);
		    		              isTrue = false;
		    		              return;
		    		        }
		    		        var one = '';
		    		        if(index != 0){
		    		        	if(relation == '且') {
		    		        		one = one + " and ";
		    		        	}else{
		    		        		one = one + " or ";
		    		        	}
		    		        }
		    		        one = one + columnName + rule + ruleValue
		    		        var rulexx = new Object();
		    		        rulexx.columnName = columnName;
		    		        rulexx.rule = rule;
		    		        rulexx.ruleValue = ruleValue;
		    		        paramJson.push(rulexx);
		    		        paramValue = paramValue + one ;
                	 }else{
		            	paramValue = $.trim($(value).find('#sqlValue').val());
		            	if (!$(value).find('textarea[name="sqlValue"]').is(':hidden') && $.trim(paramValue) == '') {
				              tips("sql语句不能为空!", $(value).find('textarea[name="sqlValue"]'));
				              window.scrollTo(0, $(value).find('textarea[name="sqlValue"]').offset().top);
				              isTrue = false;
				              return;
				        }
                    }
            })
            var dataSetRule = new Object();
            dataSetRule.paramType = paramType;
            dataSetRule.value =  paramValue;
            dataSetRule.ruleXXs = paramJson;
            dataSetRules.push(dataSetRule);
        });
        if (!isTrue) {
        	isSubmit = false;
            return;
        }
        $.ajax({
            url: '${request.contextPath}/bigdata/dataset/save',
            type: 'POST',
            data : {
                id : $('#dataSetId').val(),
                name : name,
                remark : remark,
                metadataId : metadataId,
                dataSetRuleDtos : JSON.stringify(dataSetRules),
            },
            dataType: 'json',
            success: function (val) {
            	isSubmit = false;
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    layer.msg('保存成功', {icon: 1, time: 1000}, function () {
                        var href = '${request.contextPath}/bigdata/dataset/index';
                        routerProxy.go({
                            path: href,
                            level: 1,
                            name: '数据集管理'
                        }, function () {
                            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
                        });
                    });
                }
            }
        });

    });
    
  //添加事件
	$('.all-events').on('click','.first-event .js-add-event',function(){
		var count = $('.first-event').find('.full-event').length + 1;
		var str = '<div class="full-event js-remove-target">\
                		<div class="event-head padding-l-30 pos-rel clearfix">\
                			<div class="add-btn pos-left-20 pointer js-add-event">+</div>\
                    		<div class="filter-item">\
                                <div class="event-num pointer">规则'+ count +'</div>\
                            </div>\
                            <div class="filter-item">\
                                <div class="filter-content">\
                                    <select id = "paramType" name="paramType" class="form-control width-auto">\
                                        <option value="1">字段</option>\
                                        <option value="2">sql语句</option>\
                                    </select>\
                                </div>\
                            </div>\
                            <div class="filter-item">\
                                <div class="filter-name line-h-36">的类型</div>\
                            </div>\
                            <div class="filter-item">\
                                <div class="event-remove pointer"><i class="fa fa-times-circle js-remove"></i></div>\
                            </div>\
                            <div class="filter-item">\
                                <div class="filter-name line-h-36 pointer filter-condition js-add-condition">+筛选条件</div>\
                            </div>\
                		</div>\
                		<div class="event-body">\
	            			<div class="condition-body">\
	            				<div class="more-condition">\
	            					<div class="and js-and-or">且</div>\
	            				</div>\
	            			</div>\
	        			</div>\
            		</div>';
        $(this).parents('.full-event').find('.js-remove').removeClass('hide');  	
        $(this).parents('.full-event').after(str);  	
	});
  
	//添加筛选
	$('.all-events').on('click','.js-add-condition',function(){
		var paramType = $(this).parents('.event-head').find('#paramType').val();
		var str = '';
		if(paramType == 1 ){
			str = str + '<div class="condition-detail clearfix js-condition-remove-target"><div class="filter-item"><div class="filter-content"><select name="columnName" class="form-control">';
			 for(var i in showColumns){
             	var o= showColumns[i];
             	str = str +  '<option value='+o.columnName+'>'+o.name+'</option>';
             }
			str = str + '</select></div></div>';
			str = str + '<div class="filter-item">\
                    <div class="filter-content">\
                        <select name="rule" class="form-control width-auto">\
                            <option value="=">等于</option>\
                            <option value=">">大于</option>\
                            <option value="<">小于</option>\
                        </select>\
                    </div>\
                </div>\
                <div class="filter-item">\
                    <div class="filter-content">\
                        <input maxlength="12" type="text" name="ruleValue"  id="form-field-4" class="form-control">\
                    </div>\
                </div>\
                <div class="filter-item">\
                    <div class="event-remove pointer"><i class="fa fa-times-circle js-condition-remove"></i></div>\
                </div>\
			</div>';
		}else{
			str ='<div class="condition-detail clearfix js-condition-remove-target">\
					<div class="filter-item">\
			           <div class="filter-content">\
			               <textarea  name="sqlValue"  id="sqlValue" cols="30">\
			               </textarea>\
			           </div>\
			       </div>\
			    </div>';
			    $(this).addClass('hide');
		}
    	$(this).parents('.event-head').next('.event-body').find('.condition-body').append(str);
    	$(this).parents('.event-head').next('.event-body').addClass('active');
    	$(this).parents('.event-head').addClass('active');
    	if($(this).parents('.event-head').next('.event-body').find('.condition-detail').length > 1){
    		$(this).parents('.event-head').next('.event-body').find('.more-condition').show()
    	}
    	
    	//判断是否添加了条件
    	$(this).parents('.event-head').find('#paramType').attr("disabled",true);
    	
	});
	//且，或
    $('.all-events').on('click', '.js-and-or', function () {
        if ($(this).text() == '且') {
            $(this).text('或');
        } else {
            $(this).text('且');
        }
    });
	
	//删除事件
	$('body').on('click','.js-remove',function(){
		var $f = $(this).parents('.first-event');
		if($(this).closest('.events').find('.full-event').length == 2){
			$(this).closest('.full-event').siblings('.full-event').find('.js-remove').addClass('hide');
		}
		
		$(this).closest('.js-remove-target').remove();
		$f.find('.event-num').each(function(index,ele){
			$(this).text('规则' + (index + 1))
		});
		
		
	});
	//删除筛选
	$('body').on('click','.js-condition-remove',function(){
		if($(this).closest('.condition-body').find('.condition-detail').length < 3){
			$(this).closest('.condition-body').find('.more-condition').hide();
			if($(this).closest('.condition-body').find('.condition-detail').length == 1){
				$(this).closest('.event-body').removeClass('active');
				$(this).closest('.event-body').prev('.event-head').removeClass('active')
				$(this).parents('.full-event').find('#paramType').attr("disabled",false);
			}
		}
		$(this).closest('.js-condition-remove-target').remove();
	});
	
	
	$('body').on('click','.js-save',function(){
		layer.open({
    		type: 1,
            shade: .6,
            title: '设置',
            btn: ['确定','取消'],
            area: ['800px','500px'],
            content: $('.layer-set')
    	});
    	$('.layui-layer-btn').css('border','none');
	});
	
	$('body').on('click','.js-add-active>div',function(){
		$(this).addClass('active').siblings().removeClass('active')
	}).on('mouseenter','.js-add-active>div',function(){
		$(this).addClass('hover')
	}).on('mouseleave','.js-add-active>div',function(){
		$(this).removeClass('hover')
	});

</script>