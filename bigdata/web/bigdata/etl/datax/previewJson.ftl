<pre id="json" style="height: 470px;">
    <#if jsons?exists && jsons?size gt 0>[
        <#list jsons as json>
            ${json!}<#if json_has_next>,</#if>
        </#list>]
    <#else>
        ${json!}
    </#if>
</pre>
<script>
    $(function () {
        var json = ace.edit("json");
        json.setTheme("ace/theme/chrome");
        json.session.setMode("ace/mode/json");
    });
</script>