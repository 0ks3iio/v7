<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<div class="row">
    <div>
    <div class="col-sm-8">
        <div class="box box-default" id="test">
            <div class="box-header">
                <h5 class="box-title">年级人数统计</h5>
            </div>
            <div class="box-body">
                <#if jsonStringCharts??>
                    <div style="height:195px;">
                        <@chartstructure.histogram loadingDivId="loadingDivIdHis" divClass="" divStyle="width: 100%;height: 100%;" jsonStringData=jsonStringCharts[0][0] isDifferentColour=true barWidth=20 isShowLegend=true isShowToolbox=true color="['#b2d8fc','#bcdf69', '#deb968', '#e0796a', '#ee99c3','#a599ef',  '#ffa96e', '#cb92f3','#6791e7', '#89e7b2', '#efd87a']"/>
                    </div>
                <#else>

                </#if>
            </div>

        </div>
    </div>
    </div>
    <div class="col-sm-4">
        <div class="box box-default">
            <div class="box-header">
                <h4 class="box-title">教师统计图</h4>
            </div>
            <div class="box-body">

            </div>
        </div>
    </div>
</div>