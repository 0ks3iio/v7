<div class="form-horizontal">
    <#if dbType! =="mysql">
        <div class="form-group">
            <div class="col-sm-12">
                <pre id="createSql">${create_sql!}</pre>
            </div>
            <#--<div class="col-sm-12">-->
                <#--<button class="btn btn-lightblue" onclick="executeSql()">创建表结构</button>-->
            <#--</div>-->
        </div>
    <#else>
        <div class="form-group">
            <div class="col-sm-12">

            </div>
            <div class="col-sm-12">
                <button class="btn btn-lightblue" onclick="executeSql()">创建表结构</button>
            </div>
        </div>
    </#if>
</div>
<script>

    $(function () {
        $('#createSql').css('width', $('.nav-tabs-1').width());
        $('#createSql').css('height', $('.box-part-content').height()-30);
        <#if dbType! =="mysql">
            var createSql = ace.edit("createSql");
            createSql.setTheme("ace/theme/chrome");
            createSql.session.setMode("ace/mode/sql");
            createSql.setReadOnly(true);
        </#if>
    });

    function executeSql() {
        var metadataId = $('.directory-tree a.active').attr('id');
        let createSql ="";
        <#if dbType! =="mysql">
             createSql = ace.edit("createSql").session.getValue();
        </#if>
        $.ajax({
            url: '${request.contextPath}/bigdata/metadata/createTable',
            type: 'POST',
            data: {
                metadataId: metadataId,
                create_sql: createSql
            },
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips4Confirm('error',val.message);
                }
                else {
                    showLayerTips('success','执行成功','t');
                }
            }
        });
    }
</script>