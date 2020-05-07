<div class="layer-content clearfix">
    <pre id="logDescription" style="height: 470px;">
        ${logDescription!}
    </pre>
</div>
<script>
    $(function () {
        var logDescription = ace.edit("logDescription");
        logDescription.setTheme("ace/theme/chrome");
        logDescription.session.setMode("ace/mode/html");
    });
</script>