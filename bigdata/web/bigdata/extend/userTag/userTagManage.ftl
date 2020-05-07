<link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/js/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js" type="text/javascript" charset="utf-8"></script>
<div class="box box-default clearfix label-manage">
    <div class="col-md-2 no-padding-left">
        <div class="tree labels box-standard-part">
            <div class="tree-name no-margin box-part-title">
                <span>角色选择</span>
            </div>
            <ul class="role-choice box-part-content">
                <#list userProfileList as item>
                    <li <#if userProfile.id! == item.id>class="active" </#if> onclick="switchProfile('${item.id}')"><span>${item.name!}</span>
                    <#if userProfile.id! == item.id>
                    <div class="pos-right">
                    <img src="${request.contextPath}/static/bigdata/images/edits.png" class="" style=""
                         onclick="editUserProfile()">
                    </div>
                    </li>
                    </#if>
                </#list>
            </ul>
        </div>
    </div>
    <div class="col-md-5 no-padding-right">
        <div class="tree labels box-standard-part no-border-r no-radius-right">
            <div class="tree-name box-part-title">
                <span>标签</span>
                <div class="pos-btn-right clearfix">
	                <button class="btn btn-blue btn-30 js-add-fatherNode" onclick="addTagLayer('')">新增标签</button>
	                <button class="btn btn-blue btn-30"  onclick="setGroupPortrait('${userProfile.code!}')" >群体画像设置</button >
	            </div>
            </div>
            <div class="search-wrap box-part-content clearfix">
                <div class="clearfix">
                    <div class="filter-item block no-margin">
                            <div class="input-group width-1of2">
                                <input type="text" id="searchName" class="form-control" placeholder="搜索"/>
                                <a href="javascript::void(0);" class="input-group-addon" id="treeSearch"><i class="wpfont icon-search"></i></a>
                            </div>
                    </div>
                </div>
                <ul id="tree-two" class="ztree ztree-extand two-step-tree ztree-input"></ul>
            </div>
        </div>
    </div>
    <div class="col-md-5 no-padding-side">
        <div class="tree labels box-standard-part no-radius-left" id="addRuleDiv">
            <div class="tree-name box-part-title">
                <span>规则</span>
                <button class="btn btn-blue btn-30 pos-r hide" id="saveRuleBtn">保存</button>
            </div>
            <input type="hidden" id="tagId" value="">
            <input type="hidden" id="tagName" value="">
            <input type="hidden" id="isSecondTag" value="">
            <div class="condition add-rules box-part-content" hidden="hidden">
                <div class="form-group" style="display: inline;">
                    <label class="col-sm-9 control-label no-padding-right">是否作为关键查询条件&nbsp;&nbsp;</label>
                    <div class="col-sm-3 switch-button" style="padding-top: 0px">
                        <input id="isMainQc" name="switch-field-1" class="wp wp-switch" type="checkbox" onchange="changeCondition(this)">
                        <span class="lbl"></span>
                    </div>
                </div>
                <div class="form-group" style="display: inline;">
                    <label class="col-sm-9 control-label no-padding-right">是否作为非关键查询条件&nbsp;&nbsp;</label>
                    <div class="col-sm-3 switch-button" style="padding-top: 0px">
                        <input id="isSecondaryQc" name="switch-field-1" class="wp wp-switch" type="checkbox" onchange="changeCondition(this)">
                        <span class="lbl"></span>
                    </div>
                </div>
                <div class="form-group" id="isMultipleChoiceDiv" hidden="hidden" style="display: inline;">
                    <label class="col-sm-9 control-label no-padding-right">是否多选条件&nbsp;&nbsp;</label>
                    <div class="col-sm-3 switch-button" style="padding-top: 0px">
                        <input id="isMultipleChoice" name="switch-field-1" checked="checked" class="wp wp-switch" type="checkbox">
                        <span class="lbl"></span>
                    </div>
                </div>
                <div class="form-group" id="orderDiv" style="display: inline;">
                    <label class="col-sm-9 control-label no-padding-right">排序号&nbsp;&nbsp;</label>
                    <div class="col-sm-3 switch-button" style="padding-top: 0px">
                        <input id="orderId" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" style="width: 55px;">
                    </div>
                </div>
            </div>
            <div class="add-rules">
                <span>更新周期：</span>
                <select name="" id="updateCycle">
                    <option value="daily">每日</option>
                    <option value="weekly">每周</option>
                    <option value="monthly">每月</option>
                    <option value="quarterly">每季度</option>
                    <option value="semiannual">每半年</option>
                    <option value="yearly">每年</option>
                </select>
                <span style="margin-left: 20px;">排序号：</span>
                    <input id="secondOrderId" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" style="width: 55px;">
                <hr>
                <span>条件：</span>
                <#list ruleTypes as item>
                    <span class="add margin-bottom-15 addRootRule" groupId="1" onclick="addRule(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                </#list>
                <ul class="form-group all-rule no-margin">
                </ul>
                <ul hidden class="hide-rules">
                    <#list ruleTypes as type>
                    <#assign key = type.ruleType?string>
                        <div id="ruleType${key!}">
                            <li class="rule clearfix">
                                <div class="col-sm-3 no-padding-left">
                                    <select name="metaId" id="" ruletype="" class="form-control" onchange="getTableColumn(this,true)">
                                        <#list metas as item>
                                            <option value="${item.id}">${item.name!}</option>
                                        </#list>
                                    </select>
                                </div>
                                <div class="col-sm-3 no-padding-left">
                                    <select name="mdColumnId" id="" ruletype="" class="form-control" onchange="changeSelect(this)">
                                    </select>
                                </div>
                                <div class="col-sm-2 no-padding-left">
                                    <select name="symbolId" id="" class="form-control" onchange="changeSymbol(this)" placeholder="请选择">
                                        <#list tagRuleSymbolMap[key] as item>
                                            <option value="${item.id}">${item.symbolName}</option>
                                        </#list>
                                    </select>
                                </div>
                                <div class="col-sm-3 no-padding result-div">
                                    <input name="result" type="text" class="form-control" placeholder="">
                                </div>
                                <i class="fa fa-times-circle pointer"></i>
                            </li>
                            <li class="or">或者&nbsp;
                                <#list ruleTypes as item>
                                    <span class="add" onclick="addRuleOr(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                </#list>
                            </li>
                        </div>
                    </#list>
                    <div id="andHtml">
                        <li class="and no-border">
                            <span class="">并且</span>
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleAnd(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                        </li>
                    </div>
                    <div id="orHtml">
                            <span class="">或者</span>
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleOr(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                    </div>

                    <div id="orHtmlOut">
                        <li class="or">或者&nbsp;
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleOr(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                        </li>
                    </div>

                    <div id="andHtmlInner">
                        <span class="">并且</span>
                                    <#list ruleTypes as item>
                                        <span class="add" onclick="addRuleAnd(this, '${item.ruleType}')">+ ${item.ruleTypeName!}</span>
                                    </#list>
                    </div>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="layer layer-tag-edit">

</div>

<div class="layer layer-profile-edit">
	<form id="profileForm">
	<div class="form-horizontal form-made">
		<input type="hidden" name="id" value="${userProfile.id!}">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">关联元数据：</label>
            <div class="col-sm-7">
            <input type="text" class="form-control" disabled value="${metaName!}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">外键：</label>
            <div class="col-sm-7">
            <input type="text" class="form-control" id="foreignKey" disabled name="foreignKey" maxlength="25" value="${userProfile.foreignKey!}">
            </div>
        </div>
	    <div class="form-group">
	        <label class="col-sm-3 control-label no-padding-right">主要字段：</label>
	        <div class="col-sm-7">
	            <select multiple name="basicColumns" id="mainColumnSelect"
	                    class="form-control chosen-select"  style="width: 220px;"
	                    data-placeholder="选择字段">
	                <#list exitCols as col>    
	                <option  value="${col.columnName!}" selected="selected">${col.name!}</option>
	                </#list>
	                <#list otherCols as col>    
	                <option  value="${col.columnName!}">${col.name!}</option>
	                </#list>
	            </select>
	        </div>
	    </div>	
	</div>
	</form>
</div>	
<input type="hidden" id="profileCode" value="${userProfile.code!}">
<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js"></script>
<script type="text/javascript">
    function tips(msg, key) {
        layer.tips(msg, key, {
            tipsMore: true,
            tips: 3,
            time: 2000
        });
    }
	//初始化标签查询下拉框
    function initMainColumnSelect() {
        $('#mainColumnSelect').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            no_results_text: '没有找到字段'
        });

        $('.layer-profile-edit').off('resize.chosen')
            .on('resize.chosen', function () {
                $('.chosen-select').each(function () {
                    $(this).next().css({'width': $('#foreignKey').width() + 20});
                    $(this).next().find('.chosen-results').css('height','100px').addClass('scrollbar-made');
                    $(this).next().find('.chosen-results').css('z-index','9999999');
                })
            }).trigger('resize.chosen');
        $('.chosen-choices').css('min-height', '32px');
    }
    function editUserProfile() {
        layer.open({
        	type: 1,
        	title: '设置${userProfile.name!}参数',
        	area: ['520px', '400px'],
        	btn: ['确定', '取消'],
        	yes: function(index){
			    updateProfile(index);
			  },
			btn2: function(index){
				var href = '/bigdata/userProfile/userTagManage?profileId=${userProfile.id!}';
			    $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
			  },
        	content: $('.layer-profile-edit')
        });
        $('.chosen-container').width('100%');
    }
    var isSubmit = false;
    function updateProfile(index) {
    	if(isSubmit){
    		return;
    	}
    	var foreignKey = $('#foreignKey').val();
    	if (foreignKey == "") {
            layer.tips("不能为空", "#foreignKey", {
                tipsMore: true,
                tips: 3
            });
            return;
        }
        isSubmit = true;
        var options = {
            url: '${request.contextPath}/bigdata/userProfile/update',
            dataType: 'json',
            data:{},
            success: function (data) {
                if (!data.success) {
                	showLayerTips4Confirm('error',data.message);
                } else {
                    showLayerTips('success','保存成功!','t');
                    layer.close(index);
                }
                isSubmit = false;
            },
            clearForm: false,
            resetForm: false,
            type: 'post',
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	isSubmit = false;
            }//请求出错
        };
        $("#profileForm").ajaxSubmit(options);
    }
    function changeCondition(e) {
        var isMainQc = $('#isMainQc').is(':checked');
        if ($(e).attr('id') == 'isMainQc' && isMainQc) {
            $('#isSecondaryQc').attr('checked', false);
        }
        var isSecondaryQc = $('#isSecondaryQc').is(':checked');
        if ($(e).attr('id') == 'isSecondaryQc' && isSecondaryQc) {
            $('#isMainQc').attr('checked', false);
        }
        if (isMainQc || isSecondaryQc) {
            $('#isMultipleChoiceDiv').show();
        } else {
            $('#isMultipleChoiceDiv').hide()
        }
    }
    
	function addTagLayer(id){
		var url = '${request.contextPath}/bigdata/userTag/edit?id='+id;
		$('.layer-tag-edit').load(url);
		var title = '编辑';
		if(id==''){
			title = '新增';
		}
		layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: title,
	    	area: '400px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    addType();
			  },
	    	zIndex: 1030,
	    	content: $('.layer-tag-edit')
	    })
	}
	
	function getTableColumn(obj,isChange,mdColumnId,resultval) {
		var metaId = $(obj).val();
	    $.ajax({
	        url: '${request.contextPath}/bigdata/userTag/meta/table/column',
	        type: 'POST',
	        data : {'metaId' : metaId},
	        dataType: 'json',
	        success: function (val) {
	            if (val.success) {
	            	var result = val.data;
	            	var html = '';
	            	for(i=0;i<result.length;i++){
	            		html+='<option value="'+result[i].id+'" remark=\''+result[i].dataDictionary+'\' holderstr="请填写'+result[i].name+'" >'+result[i].name+'</option>'
	            	}
	            	var $mdColumn = $(obj).parent().parent().find('select[name="mdColumnId"]');
	            	$mdColumn.html(html);
	            	if(result.length==0){
	            		var holder = $($mdColumn).find('option:selected').attr('holderstr');
	            		if(!holder)holder='';
				        var html = '<input name="result" type="text" value="" class="form-control" placeholder="'+holder+'">';
			        	$($mdColumn).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(html);
	            		changeSymbol($($mdColumn).parent().parent().find('select[name="symbolId"]'));
	            	}else if(isChange){
		            	changeSelect($mdColumn);
	            	}else{
	            		$mdColumn.val(mdColumnId);
	            		var re = $($mdColumn).find('option:selected').attr('remark');
				        if(re && re!='null' && re!='undefined' && re!=''){
				        	var rejson = JSON.parse(re); 
				        	var htmlstr = '<select name="result" id="" class="form-control" placeholder="请选择">';
				        	for(var p in rejson){
				        		htmlstr+='<option value="'+rejson[p]+'" >'+rejson[p]+'</option>';
				        	}
				        	htmlstr+='</select>';
				        	$($mdColumn).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(htmlstr);
		            	}
		            	var $result = $($mdColumn).parent().parent().find('input[name="result"],select[name="result"]');
	                    $result.val(resultval);
	            	}
	            } 
	        }
	    });
	}
	
	function setGroupPortrait(profileCode) {
	    var href = '${request.contextPath}/bigdata/userTag/group/portrait?type=0&profileCode='+profileCode;
        routerProxy.go({
            path: href,
            level: 3,
            name: '群体画像'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
	}
	
    $(function(){
        // 隐藏按钮和规则页面
        $('#saveRuleBtn').removeClass('show').add('hide');
        $('.add-rules').hide();
        //删除规则
        $('.add-rules').on('click','.fa-times-circle',function(){
            var $dad=$(this).parent();
            var $nextOne=$dad.next();
            var $nextTwo=$dad.next().next();
            var $prev=$dad.prev();
            if($('.add-rules ul.all-rule li').length==3){
                $('ul.all-rule').empty();
                $('.add-rules>.add').addClass('js-add-start').removeClass('useless');
            }else if($nextTwo.hasClass('and')&&$prev.hasClass('and')||$nextTwo.hasClass('and')&&$dad.index()==0){
                $dad.remove();
                if ($nextOne.find('.add').length == 0) {
                    $nextOne.remove();
                } else {
                    $prev.remove();
                }
                if ($nextTwo.hasClass('and')) {
                    $nextOne.remove();
                    $prev.remove();
                    if ($nextTwo.prev().html() == null) {
                        $nextTwo.remove();
                    }
                } else {
                    $nextTwo.remove();
                }
            }else{
                $dad.remove();
                if ($nextOne.find('.add').length == 0) {
                    $nextOne.remove();
                } else {
                    $prev.remove();
                }
            };
            var groupId = $dad.find("input[name='groupId']").val();
            $('#orHtml').find('.add').attr('groupId', groupId);

            var groupId1 = $('.all-rule').find('.rule').last().find("input[name='groupId']").val();
            // 只有一个时
            $('#andHtmlInner').find('.add').attr('groupId', parseInt(groupId1) + 1);
            var $last=$('.all-rule').find('.and').last();
            $last.empty().html($('#andHtmlInner').html());
            if ($('.all-rule li').length == 0) {
                $('.addRootRule').show();
            }
            
        });
        $('.hide-rules').find('.rule').find('select[name="metaId"]').each(function(){
            getTableColumn(this,true);
		})
        $('#saveRuleBtn').click(function () {
            if ($('#isSecondTag').val() == '0') {
                var isMainQc = $('#isMainQc').is(':checked') ? '1' : '0';
                var isSecondaryQc = $('#isSecondaryQc').is(':checked') ? '1' : '0';
                var isMultipleChoice = $('#isMultipleChoice').is(':checked') ? '1' : '0';
                var orderId = $('#orderId').val();
                if (orderId == null || orderId.length < 1) {
                    tips("请维护排序号", $('#orderId'));
                    return;
                }
                $.ajax({
                    url: '${request.contextPath}/bigdata/userTag/update',
                    data : {
                        id:$('#tagId').val(),
                        tagName:$('#tagName').val(),
                        isMainQc:isMainQc,
                        isSecondaryQc:isSecondaryQc,
                        isMultipleChoice:isMultipleChoice,
                        profileCode : $('#profileCode').val(),
                        orderId : orderId
                    },
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            showLayerTips4Confirm('error', val.message, 't', null);
                        }
                        else {
                            showLayerTips('success', '保存成功', 't', null);
                        }
                    }
                });
                return;
            }
            var isTrue = true;
            var list =[];
            var all_li = $('.all-rule').find('.rule');
            $.each(all_li, function (index, value) {
                var metaId = $(value).find('select[name="metaId"]').val();
                if (metaId == null) {
                    $('#addRuleDiv').scrollTop($(value).find('select[name="metaId"]').offset().top);
                    tips("请先选择元数据", $(value).find('select[name="metaId"]'));
                    isTrue = false;
                    return;
                }
                var ruleName = $(value).find('select[name="mdColumnId"]').find("option:selected").text();
                var mdColumnId = $(value).find('select[name="mdColumnId"]').val();
                if (mdColumnId == null) {
                    $('#addRuleDiv').scrollTop($(value).find('select[name="mdColumnId"]').offset().top);
                    tips("请先维护规则属性", $(value).find('select[name="mdColumnId"]'));
                    isTrue = false;
                    return;
                }
                var symbolName = $(value).find('select[name="symbolId"]').find("option:selected").text();
                var symbolId = $(value).find('select[name="symbolId"]').val();
                if (symbolId == null) {
                    $('#addRuleDiv').scrollTop($(value).find('select[name="symbolId"]').offset().top);
                    tips("请先维护规则符号", $(value).find('select[name="symbolId"]'));
                    isTrue = false;
                    return;
                }
                var result =  $(value).find('input[name="result"],select[name="result"]').val();
                if (!$(value).find('input[name="result"],select[name="result"]').is(':hidden') && $.trim(result) == '') {
                    $('#addRuleDiv').scrollTop($(value).find('input[name="result"],select[name="result"]').offset().top);
                    tips(ruleName + "结果值不能为空!", $(value).find('input[name="result"],select[name="result"]'));
                    isTrue = false;
                    return;
                }
                var groupId =  $(value).find('input[name="groupId"]').val();
                var ruleType =  $(value).find('input[name="ruleType"]').val();
                var tagId = $('#tagId').val();
                var tagName = $('#tagName').val();
                var profileCode = $('#profileCode').val();
                var data = new Object();
                data.profileCode = profileCode;
                data.tagId = tagId;
                data.tagName = tagName;
                data.groupId = groupId;
                data.result = result;
                data.mdId = metaId;
                data.ruleType = ruleType;
                data.mdColumnId = mdColumnId;
                data.ruleSymbolName = symbolName;
                data.ruleSymbolId = symbolId;
                list.push(data);
            });
            if (!isTrue) {
                return;
            }

            $.ajax({
                url: '${request.contextPath}/bigdata/userTag/saveTagRule',
                type: 'POST',
                data : {tagRules : JSON.stringify(list), orderId:$('#secondOrderId').val(), updateCycle:$('#updateCycle').val(), tagId:$('#tagId').val(),profileCode: $('#profileCode').val()},
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    }
                    else {
                        showLayerTips('success', '保存成功', 200, null);
                    }
                }
            });
        });
        initMainColumnSelect();
    });

    function changeSelect(e) {
        var remark = $(e).find('option:selected').attr('remark');
        if(remark && remark!='null' && remark!='undefined' && remark!=''){
        	var obj = JSON.parse(remark); 
        	var html = '<select name="result" id="" class="form-control" placeholder="请选择">';
        	for(var p in obj){
        		html+='<option value="'+obj[p]+'" >'+obj[p]+'</option>';
        	}
        	html+='</select>';
        	$(e).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(html);
        }else{
	        var holder = $(e).find('option:selected').attr('holderstr');
	        if(!holder)holder='';
	        var html = '<input name="result" type="text" value="" class="form-control" placeholder="'+holder+'">';
        	$(e).parent().parent().find('input[name="result"],select[name="result"]').parent('.result-div').html(html);
        }
        changeSymbol($(e).parent().parent().find('select[name="symbolId"]'));
    }

    function changeSymbol(e) {
        var text = $(e).find('option:selected').text();
        if (text == '是空值' || text == '不是空值') {
            $(e).parent().parent().find('input[name="result"],select[name="result"]').val('').hide();
        } else {
            $(e).parent().parent().find('input[name="result"],select[name="result"]').show();
        }
    }

    function addRule(e, ruleType) {
        var groupId = $(e).attr('groupId');
        $('#ruleType' + ruleType).find('.rule').find('input[name="groupId"]').remove();
        $('#ruleType' + ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + groupId + "'><input type='hidden' name='ruleType' value='" + ruleType + "'>");
        $('#ruleType' + ruleType).find('.or').find('.add').attr('groupId', groupId);
        // placeholer
        changeSelect($('#ruleType' + ruleType).find('select[name="mdColumnId"]'));
        var str = $('#ruleType' + ruleType).html();
        //添加规则
        $('#andHtml').find('.add').attr('groupId', parseInt(groupId) + 1);
        $('.all-rule').append(str).append($('#andHtml').html());
        $('.add-rules').removeClass('js-add-start').addClass('useless');
        $('.addRootRule').hide();
    }

    function addRuleAnd(e, ruleType) {
        var groupId = $(e).attr('groupId');
        $('#ruleType' + ruleType).find('.rule').find('input[name="groupId"]').remove();
        $('#ruleType' + ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + groupId + "'><input type='hidden' name='ruleType' value='" + ruleType + "'>");
        $('#ruleType' + ruleType).find('.or').find('.add').attr('groupId', groupId);
        changeSelect($('#ruleType' + ruleType).find('select[name="mdColumnId"]'));
        var str = $('#ruleType' + ruleType).html();
        //并且,添加规则
        $('#andHtml').find('.add').attr('groupId', parseInt(groupId) + 1);
        $('.all-rule').append(str).append($('#andHtml').html());
        $('.add-rules').removeClass('js-add-start').addClass('useless');
        $(e).parent().empty().html("<span class=\"\">并且</span>");
    }

    function addRuleOr(e, ruleType) {
        $('#ruleType' + ruleType).find('.rule').find('input[name="groupId"]').remove();
        $('#ruleType' + ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + $(e).attr('groupId') + "'><input type='hidden' name='ruleType' value='" + ruleType + "'>");
        $('#ruleType' + ruleType).find('.or').find('.add').attr('groupId', $(e).attr('groupId'));
        changeSelect($('#ruleType' + ruleType).find('select[name="mdColumnId"]'));
        var str = $('#ruleType' + ruleType).html();
        //或者,添加规则
        $(e).parent('.or').after(str);
        $(e).parent().empty().html("或者&nbsp;");
    }
</script>
<script type="text/javascript">
    $(function(){
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: true,
                showIcon: false,
                showLine: false,
                fontCss: getFont,
                nameIsHTML: true
            },
            edit: {
                enable: true,
                editNameSelectAll: true,
                showRemoveBtn: true,
                showRenameBtn: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeDrag: beforeDrag,
                beforeEditName: beforeEditName,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename,
                onRemove: onRemove,
                onRename: onRename,
                onClick: zTreeOnClick
            }
        };
        var zNodes =[];
        var tagCount = 1;
        var typeCount = 1;
        initTree();
        function initTree() {
            var i = 0;
            <#list tagList as tag>
                if ('${tag.parentId!}' == '00000000000000000000000000000000') {
                    typeCount++;
                } else
                    tagCount++;
                zNodes[i++] = {id:'${tag.id!}', pId:'${tag.parentId}', name:'${tag.tagName}'};
            </#list>
        };
        function zTreeOnClick(event, treeId, treeNode) {
            $('#tagId').val($.trim(treeNode.id));
            $('#tagName').val($.trim(treeNode.name));
            $('#saveRuleBtn').addClass('show');
            $('#isSecondTag').val('1');
            if (treeNode.isParent || treeNode.getParentNode() == null) {
                $('.add-rules').hide();
                $('.condition').show();
                $('#isSecondTag').val('0');
                $('#isMultipleChoiceDiv').hide();
                $.ajax({
                    url: '${request.contextPath}/bigdata/userTag/getUserTagByTagId',
                    type: 'POST',
                    data : {tagId:treeNode.id},
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            showLayerTips4Confirm('error', val.message, 't', null);
                        }
                        else {
                            if (val.data.isMainQc == '1' ) {
                                $('#isMainQc').prop('checked', true);
                                $('#isMultipleChoiceDiv').show();
                            } else {
                                $('#isMainQc').prop('checked', false);
                            }

                            if (val.data.isSecondaryQc == '1' ) {
                                $('#isSecondaryQc').prop('checked', true);
                                $('#isMultipleChoiceDiv').show();
                            } else {
                                $('#isSecondaryQc').prop('checked', false);
                            }

                            if (val.data.isMultipleChoice == '1' ) {
                                $('#isMultipleChoice').prop('checked', true);
                            } else {
                                $('#isMultipleChoice').prop('checked', false);
                            }
                            $('#orderId').val(val.data.orderId);
                        }
                    }
                });
                return;
            }
            $('.add-rules').show();
            $('.condition').hide();
            $.ajax({
                url: '${request.contextPath}/bigdata/userTag/getTagRuleRelationByTagId',
                type: 'POST',
                data : {tagId:treeNode.id, profileCode:$('#profileCode').val()},
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    }
                    else {
                        $('.addRootRule').show();
                        $('.all-rule').empty();
                        var gId = 1;
                        $.each(val.data, function (i, v) {
                            // 遍历或者
                            $.each(v, function (j, value) {
                                gId = value.groupId;
                                $('#ruleType' + value.ruleType).find('.rule').find('input[name="groupId"]').remove();
                                $('#ruleType' + value.ruleType).find('.rule').append("<input type='hidden' name='groupId' value='" + value.groupId + "'><input type='hidden' name='ruleType' value='" + value.ruleType + "'>");
                                var str = $('#ruleType' + value.ruleType).html();
                                //或者,添加规则
                                $('.all-rule').append(str);
                                var $tagMetaSelect = $('.all-rule').find('select[name="metaId"]').last();
                                var $tagRuleSelect = $('.all-rule').find('select[name="mdColumnId"]').last();
                                var $symbolSelect = $('.all-rule').find('select[name="symbolId"]').last();
                                $tagMetaSelect.val(value.mdId);
                                getTableColumn($tagMetaSelect,false,value.mdColumnId,value.result);
                                //$tagRuleSelect.val(value.mdColumnId);
                                $symbolSelect.val(value.ruleSymbolId);
                                changeSymbol($symbolSelect);
                                //var $result = $('.all-rule').find('input[name="result"],select[name="result"]').last();
                                //$result.val(1);
                                if ((j+1) < v.length) {
                                    $('.all-rule').find('.or').last().remove();
                                    $('.all-rule').append("<li class=\"or\">或者&nbsp;</li>");
                                } else {
                                    $('.all-rule').find('.or').last().find('.add').attr('groupId', value.groupId);
                                }
                            });
                            if (i == Object.keys(val.data).length) {
                                //并且,添加规则
                                $('#andHtml').find('.add').attr('groupId', gId + 1);
                                $('.all-rule').append($('#andHtml').html());
                            } else {
                                // 并且
                                $('.all-rule').append("<li class=\"and no-border\"><span class=\"\">并且</span></li>");
                            }
                        });
                        if ($('.all-rule li').length > 0) {
                            $('.addRootRule').hide();
                        }
                    }
                }
            });

            $.ajax({
                url: '${request.contextPath}/bigdata/userTag/getUserTagByTagId',
                type: 'POST',
                data : {tagId:treeNode.id},
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    }
                    else {
                        $('#updateCycle').val(val.data.updateCycle);
                        $('#secondOrderId').val(val.data.orderId);
                    }
                }
            });
        };
        //背景颜色
        function getFont(treeId, node) {
            return node.font ? node.font : {};
        };
        var className = "dark";
        function beforeDrag(treeId, treeNodes) {
            return false;
        };
        function beforeEditName(treeId, treeNode) {
        	if (treeNode.isParent || treeNode.getParentNode() == null) {
        		addTagLayer(treeNode.id);
        		return false;
        	}else{
	            className = (className === "dark" ? "":"dark");
	            var zTree = $.fn.zTree.getZTreeObj("tree-two");
	            zTree.selectNode(treeNode);
	            zTree.editName(treeNode);
	            return false;
        	}
        };
        function beforeRemove(treeId, treeNode) {
            className = (className === "dark" ? "":"dark");
            showConfirmTips('prompt', '删除标签', "确认删除 标签 -- " + treeNode.name + " 吗？", function (index) {
                layer.close(index);
                deleteTag(treeNode);
            });
            return false;
        };
        function onRemove(e, treeId, treeNode) {
        };
        function beforeRename(treeId, treeNode, newName, isCancel) {
            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            if (treeNode.name == $.trim(newName)) {
                return true;
            }
            if (newName.length == 0) {
                setTimeout(function() {
                    zTree.cancelEditName();
                    alert("标签名称不能为空.");
                }, 0);
                return false;
            }
            var node = zTree.getNodeByParam("name", newName, treeNode.getParentNode());
            if (node != null) {
                alert("标签名称不能重复.");
                zTree.cancelEditName();
                return false;
            }
            className = (className === "dark" ? "":"dark");
            editTag(treeNode.id, newName);
        };
        function onRename(e, treeId, treeNode, isCancel) {

        };
        function addHoverDom(treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            if (treeNode.getParentNode() != null) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId+ "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
                var newName = "新增标签" + tagCount++;
                var zTree = $.fn.zTree.getZTreeObj("tree-two");
                var node = zTree.getNodeByParam("name", newName);
                while (node != null) {
                    newName = "新增标签" + tagCount++;
                    node = zTree.getNodeByParam("name", newName);
                }
                addTag(treeNode, newName, 2);
                return true;
            });
        };

        function add(e) {
            var newName = "新增类别" + typeCount++;
            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            var node = zTree.getNodeByParam("name", newName);
            while (node != null) {
                newName = "新增类别" + typeCount++;
                node = zTree.getNodeByParam("name", newName);
            }
            addType(newName);
        };

        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        };

        var num=3;
        $(document).ready(function(){
            $.fn.zTree.init($("#tree-two"), setting, zNodes);
        });

        $('#treeSearch').click(function () {
            var zTree = $.fn.zTree.getZTreeObj("tree-two");
            zTree.cancelSelectedNode();
            var nodes = zTree.getNodesByParamFuzzy("name", $.trim($('#searchName').val()), null);
            $.each(nodes, function (i, value) {
                zTree.selectNode(value, true, false);
            });
        });

    });

    function addTag(treeNode, tagName, tagType) {
        var id = 0;
        $.ajax({
            url: '${request.contextPath}/bigdata/userTag/save',
            type: 'POST',
            data : {parentId : treeNode.id, tagName : tagName, tagType : tagType, profileCode : $('#profileCode').val(),saveType:1},
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips4Confirm('error', val.message, 't', null);
                }
                else {
                    var zTree = $.fn.zTree.getZTreeObj("tree-two");
                    zTree.addNodes(treeNode, {id:val.data, pId:treeNode.id, name:tagName });
                }
            }
        });
        return id;
    }

    function addType() {
    	var tagName = $("#tag-edit-name").val();
    	var id = $("#tag-edit-id").val();
    	var targetColumn = $("#tag-edit-target-column").val();
    	if(!tagName||tagName==''){
    		tips("请维护标签名称", $("#tag-edit-name"));
    		return;
    	}
    	if(!targetColumn||targetColumn==''){
    		tips("请维护标签列名", $("#tag-edit-target-column"));
    		return;
    	}
        $.ajax({
            url: '${request.contextPath}/bigdata/userTag/save',
            type: 'POST',
            data : {parentId : '00000000000000000000000000000000',id : id, tagName : tagName,targetColumn : targetColumn, tagType : 1, profileCode : $('#profileCode').val(),saveType:1},
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips4Confirm('error', val.message, 't', null);
                }
                else {
                	layer.closeAll();
                    var zTree = $.fn.zTree.getZTreeObj("tree-two");
                    if(id!=''){
                    	var node = zTree.getNodeByParam("id", val.data);
                    	if(node!=null){
                    		node.name=tagName;
	                    	zTree.updateNode(node);
                    	}
                    }else{
	                    treeNode = zTree.addNodes(null, {
	                        id: val.data,
	                        pId: '00000000000000000000000000000000',
	                        isParent: true,
	                        name: tagName
	                    });
                    }
                }
            }
        });
    }

    function editTag(id, tagName) {
        $.ajax({
            url: '${request.contextPath}/bigdata/userTag/save',
            type: 'POST',
            data : {id : id, tagName : tagName,saveType:1},
            dataType: 'json',
            success: function (val) {
                var zTree = $.fn.zTree.getZTreeObj("tree-two");
                if (!val.success) {
                    showLayerTips4Confirm('error', val.message, 't', null);
                    zTree.cancelEditName();
                    return false;
                }
                else {
                    return true;
                }
            }
        });
    }

    function deleteTag(treeNode) {
        $.ajax({
            url: '${request.contextPath}/bigdata/userTag/delete',
            type: 'POST',
            data : {id : treeNode.id},
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips4Confirm('error', val.message, 't', null);
                } else {
                    var zTree = $.fn.zTree.getZTreeObj("tree-two");
                    zTree.removeNode(treeNode);
                    zTree.selectNode(treeNode);
                    if (treeNode.id == $('#tagId').val()) {
                        $('#saveRuleBtn').removeClass('show').addClass('hide');
                        $('.add-rules').hide();
                    }
                }
            }
        });
    }

    function switchProfile(id) {
        var href = '/bigdata/userProfile/userTagManage?profileId=' + id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '标签管理'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }
    
</script>