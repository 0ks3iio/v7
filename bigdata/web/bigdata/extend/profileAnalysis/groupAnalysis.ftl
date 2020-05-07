<div class="box box-default" id="groupAnalysisDiv">
    <div class="box-body">
        <div class="picker-table pos-rel margin-bottom-40">
                <ul class="nav nav-tabs nav-tabs-1 profileSelect">
                    <#list userProfiles as item>
                    <li <#if item.code == profileCode>class="active"</#if>>
                        <a data-toggle="tab" code="${item.code!}" href="javascript:void(0)" class="no-margin">${item.name!}</a>
                    </li>
                    </#list>
                </ul>
                <div class="tab-content no-padding">
                    <div class="tab-pane active">
                        <table class="tables tables-border">
                            <tbody>
                                <#list mainUserTags as key>
                                <tr>
                                    <th style="width: 160px;">${key.tagName!}：</th>
                                    <td>
                                        <div class="mainSelect" multiSelect="${key.isMultipleChoice!}" id="${key.id!}">
                                            <#list mainUserTagMap[key.id] as item>
                                                <a class="tagSelect" tagId="${item.id!}" href="javascript:void(0);">${item.tagName!}</a>
                                            </#list>
                                        </div>
                                    </td>
                                    <td width="150" class="text-right">
                                        <div class="" style="text-align: center">
                                            <a class="picker-more color-blue" href="javascript:void(0);"><span tagId="${key.id!}" style="display: none;" onclick="clearSelect(this)">清空</span>&nbsp;<#--|&nbsp;<span>展开</span>--></a>
                                        </div>
                                    </td>
                                </tr>
                                </#list>
                                <#if secondaryUserTags?size gt 0>
                                    <tr class="position-relative select-wrap">
                                        <th style="width: 160px;">其他条件：</th>
                                        <td>
                                            <div class="outter" style="height: auto;">
                                                <#list secondaryUserTags as item>
                                                <a href="javascript:void(0);" data-index="${item.id!}" class="${item.id!}"><span>${item.tagName}</span>：<span><i class="fa fa-angle-down"></i></span></a>
                                                </#list>
                                            </div>
                                        </td>
                                        <td width="100" class="text-right" style="vertical-align: top;">
                                            <div class="outter">

                                            </div>
                                        </td>
                                        <#list secondaryUserTags as key>
                                        <div class="selects mainSelect" id="${key.id!}">
                                            <div class="" multiSelect="${key.isMultipleChoice!}">
                                                <#list secondaryUserTagMap[key.id] as item>
                                                    <a href="javascript:void(0);" tagId="${item.id!}">${item.tagName!}</a>
                                                </#list>
                                            </div>
                                            <div class="text-center">
                                                <button class="btn btn-blue secondEnsure" tagId="${key.id!}">确定</button>
                                                <button class="btn btn-default js-gone">取消</button>
                                            </div>
                                        </div>
                                        </#list>
                                    </tr>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
        </div>
        <div class="gradeset-content clearfix pb-10">
            <div class="btn-group pull-right js-chioce">
                <button class="btn btn-default" id="listBtn" onclick="queryResult('list')">
                    <i class="fa fa-list-ul fa-fw" aria-hidden="true"></i>&nbsp;列表
                </button>
                <button class="btn btn-default" id="profileBtn" onclick="queryResult('profile')">
                    <i class="fa fa-user" aria-hidden="true"></i>&nbsp;画像
                </button>
            </div>
            <div class="pull-right downloadList" role="group" aria-label="...">
                <div class="btn-group" role="group">
                    <button type="button" class="btn btn-default" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" onclick="exportToExcel('${profileCode!}')">
                        <i class="fa fa-download fa-fw" aria-hidden="true"></i>&nbsp;下载
                    </button>
                </div>&nbsp;
            </div>
        </div>
        <div class="table-container no-margin" id="groupAnalysisList">

        </div>
    </div>
</div>
<input type="hidden" id="profileCode" value="${profileCode!}">
<input type="hidden" id="type" value="${type!'profile'}">
<script type="text/javascript">
    $(function(){
        queryResult($('#type').val());
        $('.profileSelect').on('click', 'a', function () {
            var profileCode = $(this).attr('code');
            var type = 'profile';
            if ($('#listBtn').hasClass('active')) {
                type = 'list';
            }
            var href = '/bigdata/groupAnalysis/index?profileCode=' + profileCode + '&type=' + type;
            <#if biStyle>
            	$("#moduleDetailDiv").load('${request.contextPath}' + href+"&style=bi");
            <#else>
	            router.go({
	                path: href,
	                level: 2,
	                name: '群体画像分析'
	            }, function () {
	                $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
	            });
            </#if>
        });

        $('.mainSelect').on('click', 'a', function () {
            var multiSelect = $(this).parent().attr('multiSelect');
            if (multiSelect == 1) {
                $(this).toggleClass('selected');
            } else {
                $(this).toggleClass('selected').siblings().removeClass('selected');
            }
            var clearTagId = $(this).parent().attr('id');
            $("span[tagid='" + clearTagId +"']").show();
            if ($(this).hasClass('tagSelect')) {
                queryResult();
            }
        });

        //更多条件
        var index;
        $('.select-wrap ').on('click','.outter>a i',function(){
            index=$(this).parent().parent().data('index');
            var count = $(this).parent().parent().find('span:last-child').find('a').length;
            if (count == 0) {
                $('#' + index).find('a').attr('class', '');
            }
            $('#' + index).show().siblings('.selects').hide();
        });

        $('.select-wrap ').on('click','.outter>a span:first-child',function(){
            index=$(this).parent().data('index');
            var count = $(this).parent().find('span:last-child').find('a').length;
            if (count == 0) {
                $('#' + index).find('a').attr('class', '');
            }
            $('#' + index).show().siblings('.selects').hide();
        });
        //确定，取消
        $('.secondEnsure').on('click',function(){
            var $selects=$(this).parent().prev().find('a.selected').clone();
            var tagId = $(this).attr('tagId');
            $selects.each(function(index,ele){
                $(ele).append(' <i class="fa fa-times" aria-hidden="true"></i>')
            });
            $selects.attr('class', $selects.attr('class') + ' tagSelect');
            $('.' + tagId).find('span:last-child').empty().append($selects);
            $(this).parents('.selects').hide();
            if ($(this).parent().parent().is(":hidden")) {
                queryResult();
            }
        });
        $('body').on('click','.selects button:last-child',function(){
            $(this).parents('.selects').hide();
        });
        //删除
        $('.select-wrap').on('click','.fa-times',function(){
            var $html=$(this).parent().parent();
            var tagId = $(this).parent().attr('tagid');
            var index = $html.parent().data('index');
            $('#' + index).find('a').each(function(index,ele){
                if (tagId == $(ele).attr('tagid')) {
                    $(ele).attr('class', '');
                }
            });
            $(this).parent('a').remove();
            var $text=$html.text();
            if($text==''){
                $html.html('<i class="fa fa-angle-down"></i>');
            }
            queryResult();
        })
    });
    
    function clearSelect(e) {
        var tagId = $(e).attr('tagId');
        $('#' + tagId).find('a').attr('class', 'tagSelect');
        $("span[tagid='" + tagId +"']").hide();
        queryResult();
    }

    function queryResult(type) {
        var profileCode = $('#profileCode').val();
        var tagArray = [];
        $('.tagSelect.selected').each(function (i, v) {
            tagArray.push($(v).attr('tagId'));
        });

        var url = '${request.contextPath}/bigdata/groupAnalysis/list';
        if (type != 'list' && (type == 'profile' || $('#profileBtn').hasClass('active'))) {
            //url = '${request.contextPath}/bigdata/groupAnalysis/groupProfile';
            url = '${request.contextPath}/bigdata/userTag/group/portrait';
            $('#listBtn').removeClass('active');
            $('#profileBtn').addClass('active');
            $('.downloadList').hide();
        } else {
            $('#profileBtn').removeClass('active');
            $('#listBtn').addClass('active');
            $('.downloadList').show();
        }

        $.ajax({
            url: url,
            type: 'POST',
            data : {profileCode : profileCode, type:1,tagArray:JSON.stringify(tagArray)},
            dataType: 'html',
            success: function (val) {
                $('#groupAnalysisList').empty().append(val);
            }
        });
    }
    
    function exportToExcel(profileCode) {
        var tagArray = [];
        $('.tagSelect.selected').each(function (i, v) {
            tagArray.push($(v).attr('tagId'));
        });
        var url = "${request.contextPath}/bigdata/groupAnalysis/export?profileCode=" + profileCode + "&tagArray=" + tagArray.join(",");
        window.location.href=url;
    }
    
    function viewPersonAnalysis(id, profileCode) {
        var href = '/bigdata/personAnalysis/index?userId=' + id + '&profileCode=' + profileCode;
         <#if biStyle>
        	$("#moduleDetailDiv").load('${request.contextPath}' + href+"&style=bi");
        <#else>
        router.go({
            path: href,
            level: 2,
            name: '个体画像分析'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
        </#if>
    }

</script>