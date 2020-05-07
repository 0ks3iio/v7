<div class="layer-content clearfix">
    <pre id="sql" style="height: 170px;">${sql!}</pre>
</div>
<script>
    $(function () {
        var sql = ace.edit("sql");
        sql.setTheme("ace/theme/chrome");
        sql.session.setMode("ace/mode/sql");
        sql.setReadOnly(true);
    });
</script>