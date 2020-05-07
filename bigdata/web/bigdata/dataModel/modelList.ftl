<link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/js/zTree/css/zTreeStyle.css">
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js"></script>
<style>
    .stat-img {
        background: #EBEBEB url('${request.contextPath}/static/bigdata/images/model-icon.png') no-repeat;
        background-position: center center;
    }

    .stat-img-kylin {
        background: #EBEBEB url('${request.contextPath}/static/bigdata/images/kylin-icon.png') no-repeat;
        background-position: center center;
    }

    .stat-img-impala {
        background: #EBEBEB url('${request.contextPath}/static/bigdata/images/impala-icon.png') no-repeat;
        background-position: center center;
    }

    .catalog-r-60.active .stat-img, .catalog-r-60:hover .stat-img {
        background: #307EEA url('${request.contextPath}/static/bigdata/images/model-icon-hover.png') no-repeat;
        background-position: center center;
    }

    .catalog-r-60.active .stat-img-kylin, .catalog-r-60:hover .stat-img-kylin {
        background: #307EEA url('${request.contextPath}/static/bigdata/images/kylin-icon-hover.png') no-repeat;
        background-position: center center;
    }

    .catalog-r-60.active .stat-img-impala, .catalog-r-60:hover .stat-img-impala {
        background: #307EEA url('${request.contextPath}/static/bigdata/images/impala-icon-hover.png') no-repeat;
        background-position: center center;
    }

    .stat-img-one {
        background: url('${request.contextPath}/static/bigdata/images/indicators-icon.png') no-repeat;
        background-position: center center;
    }

    .stat-img-two {
        background: url('${request.contextPath}/static/bigdata/images/dimensionality-icon.png') no-repeat;
        background-position: center center;
    }
</style>
<input type="hidden" id="currentUnitId" value="${currentUnitId!}">
<div class="box box-default height-1of1 no-margin clearfix">
            <div class="col-md-3 height-1of1 no-padding-side">
                <div class="tree labels no-radius-right height-1of1">
                    <div class="tree-name border-bottom-cfd2d4 no-margin">
                        <b>数据模型</b>
                        <div class="pos-right-c">
                            <img src="${request.contextPath}/static/bigdata/images/time-scope.png" data-toggle="tooltip" data-placement="bottom" title="时间维度设置" class="js-timeDimension"/>
                            &nbsp;<img src="${request.contextPath}/static/bigdata/images/powers.png" data-toggle="tooltip" data-placement="bottom" title="批量授权" class="js-subscription"/>
                            &nbsp;&nbsp;&nbsp;<img src="${request.contextPath}/static/bigdata/images/model-add.png" id="addModelBtn"/>
                        </div>
                    </div>
                    <div class="padding-20 height-calc-48 scrollbar-made js-click" id="modelDiv">
                        <#list models as item>
                            <div class="catalog catalog-r-60 dataModel" canEdit="${item.canEdit?string('true', 'false')}" code="${item.code!}" unitId="${item.unitId!}" source="${item.source!}" id="${item.id!}">
                                <span title="${item.name!}">${item.name!}</span>
                                <div class="pos-right clearfix">
                                    <#if item.dateDimSwitch?default(0) == 1><img src="${request.contextPath}/static/bigdata/images/time-scope.png"  class="" data-toggle="tooltip" data-placement="bottom" title="时间维度" onclick="editDateDim('${item.id!}')"></#if>
                                    <#if item.userDatasetSwitch?default(0) == 1><img src="${request.contextPath}/static/bigdata/images/power.png"  class="" data-toggle="tooltip" data-placement="bottom" title="数据集" onclick="editUserDataset('${item.id!}')"></#if>
                                    <img src="${request.contextPath}/static/bigdata/images/edits.png" class=""
                                         onclick="editDataModel('${item.id!}')">
                                    <img src="${request.contextPath}/static/bigdata/images/removes.png" class=""
                                         onclick="deleteDataModel('${item.id!}')"/>
                                </div>
                                <div class="left-pos-60 <#if item.type=='kylin' >stat-img-kylin<#elseif item.type=='impala'>stat-img-impala<#else>stat-img</#if>">
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
            <div class="col-md-3 height-1of1 no-padding-side">
                <div class="tree labels no-radius height-1of1 no-border-left">
                    <p class="tree-name border-bottom-cfd2d4 no-margin">
                        <b>指标</b>
                        <img src="${request.contextPath}/static/bigdata/images/model-add.png"
                             class="pos-right-c addModelParamBtn" alt=""/>
                    </p>
                    <div class="height-calc-48 scrollbar-made js-target">
                        <div class="padding-20 js-active" id="indexDiv">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3 height-1of1 no-padding-side">
                <div class="tree labels height-1of1 no-radius no-border-left js-active-one">
                    <p class="tree-name border-bottom-cfd2d4 no-margin">
                        <b>维度</b>
                        <img src="${request.contextPath}/static/bigdata/images/model-add.png"
                             class="pos-right-c addModelParamBtn" alt=""/>
                    </p>
                    <div class="height-calc-48 scrollbar-made js-target">
                        <div class="padding-20 js-active" id="dimensionDiv">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3 height-1of1 no-padding-side">
                <div class="tree labels height-1of1 no-radius-left no-border-left js-active-one">
                    <p class="tree-name border-bottom-cfd2d4 no-margin">
                        <b>授权</b>
                        <button class="pull-right btn btn-lightblue add-data" style="position: absolute;right: 10px;" id="modelSaveBtn" onclick="saveModelAuth()">保存</button>
                    </p>
                    <div class="padding-20 js-scroll-height" id="authDiv">

                    </div>
                </div>
            </div>
        </div>
<div class="layer layer-model-add">
    <div id="modelAddDiv">

    </div>
</div>
<div class="layer layer-modelParam-add">
    <div id="modelParamAddDiv">

    </div>
</div>
<div class="layer layer-datedim-setting">
    <div id="datedimDiv" class="mt-30">

    </div>
</div>
<div class="layer layer-timeDimension layer-choose-goal wrap-1of1 no-padding over-auto">

</div>
<script type="text/javascript">

    var editImg = '${request.contextPath}/static/bigdata/images/edits.png';
    var removeImg = '${request.contextPath}/static/bigdata/images/removes.png';
    var showIcon = '${request.contextPath}/static/bigdata/images/show-icon.png';
    var hideIcon = '${request.contextPath}/static/bigdata/images/hide-icon.png';
    $(function(){
        $('.page-content').css('padding-bottom','0px');
        $('.container-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top -20,
                overflow: 'hidden'
            })
        });
        $('.js-scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top-5,
                overflow: 'auto'
            })
        });

        $('[data-toggle="tooltip"]').tooltip();

        $('.js-subscription').on('click', function () {
            var href = '/bigdata/subcribe/batchOperate?type=model';
            routerProxy.go({
                path: href,
                level: 2,
                name: '批量授权'
            }, function () {
                $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
            });
        });

        $('.js-click').on('click','.catalog-r-60', function(){
            $(this).addClass('active').siblings().removeClass('active');
        });
        $('body').on('click','.catalog-l-40',function(){
            $(this).addClass('active').siblings().removeClass('active')
        });

        $(".js-click").on("click", ".dataModel", function () {
            var code = $(this).attr('code');
            initIndex(code);
            initDimension(code);
            initAuth(code);

        });

        if ('${selectId?default('')}' != '') {
            $('#${selectId!}').trigger("click");
        } else {
            $('.dataModel').first().trigger("click");
        }

        $('#addModelBtn').click(function () {
            editDataModel('');
        });

        $('.addModelParamBtn').click(function () {
            var type = $(this).prev().text();
            editDataModelParam('', type);
        });

        //指标库
        $('.tree').on('click','.js-timeDimension',function(){

            $.ajax({
                url: '${request.contextPath}/bigdata/model/timeDimensionList',
                type: 'POST',
                dataType: 'html',
                success: function (response) {
                    $('.layer-timeDimension').empty().append(response);
                }
            });

            layer.open({
                type: 1,
                shade: .6,
                title: '时间维度设置',
                btn: false,
                area: ['300px','400px'],
                content: $('.layer-timeDimension')
            });
        });
    });

    function initIndex(code) {
        $.ajax({
            url: '${request.contextPath}/bigdata/model/getModelParam',
            data: {code: code, type: 'index'},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('#indexDiv').empty();
                    $.each(data, function (i, v) {
                        var element = "<div class=\"catalog catalog-l-40\" id='" + v.id + "'>\n" +
                                "                            <span>" + v.name + "</span>\n" +
                                "                            <div class=\"pos-right clearfix\">"

                        if (v.isFilter == 1) {
                            element = element + "<img src=\"" + hideIcon + "\" onclick='changeShowStatu(this)' class=\"js-toggle\">";
                        } else {
                            element = element + "<img src=\"" + showIcon + "\" onclick='changeShowStatu(this)' class=\"js-toggle\">";
                        }

                        element = element +
                                "&nbsp;<img src=\"" + editImg + "\" class=\"js-edit\" onclick=\"editDataModelParam('" + v.id + "', '指标')\">" +
                                "&nbsp;<img src=\"" + removeImg + "\" class=\"\" onclick=\"deleteDataModelParam('" + v.id + "', '指标')\"/>" +
                                "</div>" +
                                "                        </div>";
                        $('#indexDiv').append(element);
                    })
                }
            }
        });
    }

    function initDimension(code) {
        $.ajax({
            url: '${request.contextPath}/bigdata/model/getModelParam',
            data: {code: code, type: 'dimension'},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('#dimensionDiv').empty();
                    $.each(data, function (i, v) {
                        var element = "<div class=\"catalog catalog-l-40\" id='" + v.id + "'>\n" +
                                "                            <span>" + v.name + "</span>\n" +
                                "                            <div class=\"pos-right clearfix\">";

                        if (v.isFilter == 1) {
                            element = element + "<img src=\"" + hideIcon + "\" onclick='changeShowStatu(this)' class=\"s-toggle\">";
                        } else {
                            element = element + "<img src=\"" + showIcon + "\" onclick='changeShowStatu(this)' class=\"s-toggle\">";
                        }
                        element = element +
                                "&nbsp;<img src=\"" + editImg + "\" class=\"js-edit\" onclick=\"editDataModelParam('" + v.id + "', '维度')\">" +
                                "&nbsp;<img src=\"" + removeImg + "\" class=\"\" onclick=\"deleteDataModelParam('" + v.id + "', '维度')\"/>" +
                                "</div>\n" +
                                "                        </div>";
                        $('#dimensionDiv').append(element);
                    })
                }
            }
        });
    }

    function initAuth(code) {
        $.ajax({
            url: '${request.contextPath}/bigdata/model/modelAuth',
            data: {code : code},
            type: 'POST',
            dataType: 'html',
            success: function (response) {
                $('#authDiv').empty().html(response);
            }
        });
    }

    function editUserDataset(id) {
        var href = '/bigdata/model/user/list?modelId='+id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '用户集管理'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function editDataModel(id) {
        if (id != '') {
            if ($('#' + id).attr('canEdit') == 'false') {
                if ($('#currentUnitId').val() != $('#' + id).attr('unitId')) {
                    layer.msg('该数据模型是由上级单位创建的，您不能新增或编辑！', {icon: 4, time:1500});
                    return;
                }
            }
        }
        $.ajax({
            url: '${request.contextPath}/bigdata/model/editModel',
            type: 'POST',
            data: {id: id},
            dataType: 'html',
            success: function (response) {
                $('#modelAddDiv').empty().append(response);
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增数据模型' : '修改数据模型',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;



                if ($('#modelName').val() == "") {
                    layer.tips("不能为空", "#modelName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#code').val() == "") {
                    layer.tips("不能为空", "#code", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#dbTypeSelect').val() == 'kylin') {
                    if ($('#project').val() == "") {
                        layer.tips("不能为空", "#project", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#dateDimSwitch').val() == '1') {
                    if ($('#dateDimTable').val() == "") {
                        layer.tips("不能为空", "#dateDimTable", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                    if ($('#dateColumn').val() == "") {
                        layer.tips("不能为空", "#dateColumn", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                }

                var options = {
                    url: "${request.contextPath}/bigdata/model/saveModel",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error', data.message, 't', null);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功','t');
                            layer.close(index);
                            $('#modelAddDiv').empty();
                            $('.page-content').load("${request.contextPath}/bigdata/model/design?selectId=" + data.data.id);
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#submitForm").ajaxSubmit(options);
            },
            area: ['800px', '700px'],
            content: $('.layer-model-add')
        });
    }
    
    function deleteDataModel(id) {
        if ($('#' + id).attr('canEdit') == 'false') {
            if ($('#currentUnitId').val() != $('#' + id).attr('unitId')) {
                layer.msg('该数据模型是由上级单位创建的，您不能删除！', {icon: 4, time:1500});
                return;
            }
        }

        var title = '确定删除【' + $('#' + id).find('span').text() + "】？";
        layer.confirm(title, {btn: ['确定', '取消'], title: '删除模型', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/model/deleteDataModel',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        layer.msg(val.message, {icon: 2});
                    }
                    else {
                        showLayerTips('success','删除成功','t');
                        $('#' + id).remove();
                        $('.dataModel').first().trigger("click");
                    }
                }
            });
        });
    }

    function editDataModelParam(id, type) {
        if ($('#currentUnitId').val() != $('.dataModel.active').attr('unitId')) {
            layer.msg('该数据模型是由上级单位创建的，您不能新增和编辑！', {icon: 4, time:1500});
            return;
        }
        var code = $('.dataModel.active').attr('code');
        if (code == null) {
            layer.msg('请先创建数据模型!', {icon: 2});
            return;
        }

        var modelId = $('.dataModel.active').attr('id');

        var title = "新增";
        if (id != '') {
            title = "修改";
        }
        title = title + type;
        if (type == '指标') {
            type = 'index';
        } else {
            type = 'dimension';
        }
        $.ajax({
            url: '${request.contextPath}/bigdata/model/editModelParam',
            type: 'POST',
            data: {id: id, code: code, type: type, modelId:modelId},
            dataType: 'html',
            success: function (response) {
                $('#modelParamAddDiv').empty().append(response);
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: title,
            btn: ['保存', '取消'],
            yes: function (index, layero) {

                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#modelParamName').val() == "") {
                    layer.tips("不能为空", "#modelParamName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#useTable').val() == "") {
                    layer.tips("不能为空", "#useTable", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#dimForeignId').val() != '') {
                    if ($('#factForeignId') == '') {
                        layer.tips("关联字段不能为空!", "#factForeignId", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if($('#measureSelect').val() == null){
                    if ($('#fieldSelect').val() == null &&$('#builtInSource').val()==0) {
                        console.log("fieldSelect进来了")
                        layer.tips("不能为空", "#fieldSelect", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                    else if ($('#useField').val()==''&& $('#fieldSelect').val() == null) {
                        console.log("useField进来了")
                        layer.tips("不能为空", "#useField", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                }

                if ($('#timeSwitch').is(':checked')) {
                    if ($('#dateDimTable').val() == '') {
                        layer.tips("时间维度表不能为空!", "#dateDimTable", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                    if ($('#dateColumn').val() == '') {
                        layer.tips("时间维度字段不能为空!", "#dateColumn", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }

                }
                var options = {
                    url: "${request.contextPath}/bigdata/model/saveModelParam",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error', data.message, 't', null);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功','t');
                            layer.close(index);
                            initIndex(code);
                            initDimension(code);
                            $('#modelParamAddDiv').empty();
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };

                $("#modelParamForm").ajaxSubmit(options);
            },
            cancel: function() {
                $('#modelParamAddDiv').empty();
            },
            area: ['650px', '650px'],
            content: $('.layer-modelParam-add')
        });
    }
    
     function editDateDim(id) {
    	    $.ajax({
	            url: '${request.contextPath}/bigdata/datedimension/detail',
	            type: 'POST',
	            data: {modeld:id},
	            dataType: 'html',
	            beforeSend: function(){
			      	$('#datedimDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('#datedimDiv').html(response);
	            }
            });
           var dateDimSubmit = false;
            layer.open({
            type: 1,
            shade: .6,
            title: '时间维度设置',
            btn: ['生成数据','取消'],
            yes:function(index, layero){
               if(dateDimSubmit){
               		layer.msg("数据生成中,请不要重复点击", {offset: 't',time: 2000});
					reurn;
				}
			   dateDimSubmit = true;
   		       if($('#tableName').val()== '') {
                    	layer.tips("时间维度表名不能为空!", "#tableName", {
                        tipsMore: true,
                        tips: 3
                    });
                    dateDimSubmit = false;
                    return;
                }
                if($('#startDate').val() == '') {
                    	layer.tips("起始日期不能为空!", "#startDate", {
                        tipsMore: true,
                        tips: 3
                    });
                    dateDimSubmit = false;
                    return;
                }
                if($('#endDate').val() == '') {
                    	layer.tips("结束日期不能为空!", "#endDate", {
                        tipsMore: true,
                        tips: 3
                    });
                    dateDimSubmit = false;
                    return;
                }

				var  options = {
					url : "${request.contextPath}/bigdata/datedimension/init",
					dataType : 'json',
					success : function(data){
					 	if(!data.success){
                            showLayerTips4Confirm('error', data.message, 't', null);
					 		dateDimSubmit = false;
					 	}else{
					 		$('#totalSize').html(data.msg+"条");
					 		layer.msg("初始化时间维度成功!一共生成"+data.msg+"条记录", {offset: 't',time: 2000});
					 		layer.close(index);
			    		}
					},
					clearForm : false,
					resetForm : false,
					type : 'post',
					error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
				};
				$("#dateDimSubmitForm").ajaxSubmit(options);
            },
            end:function(){
               	$('#datedimDiv').empty();
             },
            area: ['550px', '400px'],
            content: $('.layer-datedim-setting')
        });
	}

    function deleteDataModelParam(id, type) {
        if ($('#currentUnitId').val() != $('.dataModel.active').attr('unitId')) {
            layer.msg('该数据模型是由上级单位创建的，您不能删除！', {icon: 4, time:1500});
            return;
        }
        var title = '确定删除' + type + '【' + $('#' + id).find('span').text() + "】？";
        layer.confirm(title, {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/model/deleteDataModelParam',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message);
                    }
                    else {
                        showLayerTips('success','删除成功','t');
                        $('#' + id).remove();
                        var code = $('.dataModel.active').attr('code');
                        initIndex(code);
                        initDimension(code);
                    }
                }
            });
        });
    }
    
    function changeShowStatu(e) {
        if ($('#currentUnitId').val() != $('.dataModel.active').attr('unitId')) {
            layer.msg('该数据模型是由上级单位创建的，您不能编辑！', {icon: 4, time:1500});
            return;
        }
        var status = 1;
        var id = $(e).parent().parent().attr('id');
        if($(e).attr('src').indexOf('show-icon') == -1){
            status = 0;
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/model/showOrHideDataModelParam',
            type: 'POST',
            data: {
                id: id,
                status:status
            },
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    if($(e).attr('src').indexOf('show-icon') == -1){
                        $(e).attr('src',showIcon);
                        $(e).closest('.catalog').children('span').removeClass('active')
                        status = 0;
                    }else{
                        $(e).attr('src',hideIcon);
                        $(e).closest('.catalog').children('span').addClass('active')
                    }
                }
            }
        });

    }
</script>