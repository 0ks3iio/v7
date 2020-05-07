<#import "/fw/macro/mobilecommon.ftl" as common />

<@common.moduleDiv titleName="申请积极分子">
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav mui-bar-grey">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <a class="mui-icon mui-icon-more mui-pull-right"></a>
    <h1 class="mui-title">申请积极分子</h1>
</header>
<div class="mui-content">
    <form class="mui-input-group mui-activist-seach">
        <div class="mui-input-row">
            <div class="mui-col-xs-10 mui-pull-left">
                <input type="text" class="mui-input-clear f15"data-input-clear="1" value="吉林省教育厅党组织一部">
            </div>
            <div class="mui-col-xs-2 mui-pull-right">
                <button class="mui-btn-link">取消</button>
            </div>
        </div>
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">吉林省教育厅</li>
            <li class="mui-table-view-cell"    >吉林省教育厅党组织</li>
            <li class="mui-table-view-cell">吉林省教育厅党组织组织</li>
        </ul>

    </form>
</div>

</@common.moduleDiv >