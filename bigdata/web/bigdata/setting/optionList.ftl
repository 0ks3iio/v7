<div class="col-md-2 no-padding-side clearfix">
    <div class="tree labels no-radius-right float-left" style="width: 100%">
        <p class="tree-name border-bottom-cfd2d4 no-margin">
            <b><#if type! =="frame">列表<#else>列表</#if></b>
        </p>
        <div class="tree-scroll-height count-name js-blur">
            <#if optionList?exists&&optionList?size gt 0>
            <#list optionList as option>
            <div class="count-line" id="${option.code!}" frameId="${option.id!}">
                <div class="" id="${option.id!}" onclick="viewFrameParam('${option.code!}')">
                    <span>${option.name!}</span>
                    <#if type! =="frame">
	                    <#if option.status?default(0) == 0>
	                        <img src="${request.contextPath}/static/images/big-data/over-icon.png" class="pos-right"/>
	                    <#else>
	                        <img src="${request.contextPath}/static/images/big-data/success-icon.png" class="pos-right"/>
	                    </#if>
                    </#if>
                </div>
            </div>
            </#list>
            <#else>
                <div class="wrap-1of1 centered no-data-state">
                    <div class="text-center">
                        <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
                        <p>暂无数据</p>
                    </div>
                </div>
            </#if>
        </div>
    </div>
</div>
<div class="col-md-10 no-padding-side" >
    <div class="tree labels no-radius no-radius-right no-border-left">
        <div class="tree-name border-bottom-cfd2d4 no-margin">
            <b>参数列表</b>
        </div>
        <div id="detailDiv">

        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $('.js-scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top -41,
                overflow: 'auto'
            })
        });

        $('.tree').each(function(){
            $(this).css({
                height: $(window).height() - $(this).offset().top - 40
            })
        });

        $('.count-line').first().children().first().trigger('click');

    });

    function viewFrameParam(code) {
        var url = '${request.contextPath}/bigdata/setting/option/view?code=' + code;
        $('.count-line').removeClass('bg-DEEBFC');
        $('#' + code).addClass('bg-DEEBFC');
        $("#detailDiv").load(url);
    }
    
    function changeStatus(e) {
        var id = $('.bg-DEEBFC').attr('frameId');
        $.ajax({
            url: '${request.contextPath}/bigdata/setting/option/updateParamStatus',
            type: 'POST',
            data: {frameParamId:id, status:$('#status').is(':checked') ? 1 : 0},
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layerTipMsg(response.success, response.message, "");
                } else {
                    if ($('#status').is(':checked')) {
                        $('#' + id).children().last().attr('src', '${request.contextPath}/static/images/big-data/success-icon.png');
                    } else {
                        $('#' + id).children().last().attr('src', '${request.contextPath}/static/images/big-data/over-icon.png');
                    }
                }
            }
        });
        
    }

</script>