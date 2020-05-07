<!-- 复合图表 -->
<!-- 单个图表模板 -->
<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<div class="col-sm-${data.col?default('12')}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${data.title!}</h4>
        </div>
        <div class="box-body">
            <div class="col-sm-${data.col?default('12')}">
            </div>
            <div style="height: ${data.height!195}px;">
                <@chartstructure.map city="${data.city!}" jsonStringData=data.jsonData loadingDivId="div${echartsDivId!}"  divClass="" divStyle="width: 100%;height: 100%;"/>
            </div>
        </div>
    </div>
</div>
