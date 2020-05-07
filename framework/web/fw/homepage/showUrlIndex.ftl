<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="用户登陆">
<#list sss as s>
${s!}<br>
</#list>
<script>
function showContent(url){
window.open("http://192.168.0.15:88/ap/homepage/index/page#" + url);
}
</script>
</@webmacro.commonWeb>