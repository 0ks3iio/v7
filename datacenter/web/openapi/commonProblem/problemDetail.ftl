<style type="text/css">
em{
font-style:italic;color:#333;
}
</style>
<ol class="breadcrumb">
    <li><a href="javascript:void(0);" id="jumpProblemIndex">常见问题 </a></li>
    <li><a href="javascript:void(0);" id="jumpProblemtype" data-i="${problem.id}">${problem.typeName!} </a></li>
    <li class="active">${problem.question!}</li>
</ol>
<div class="faq-content-header">
    <span class="faq-content-header-time">更新时间：${problem.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</span>
    <span class="faq-content-header-title">${problem.question!}</span>
</div>
<div class="faq-content-body">
    ${content!}
</div>
<script>
    $('#jumpProblemtype').click(function(){
        var id=$(this).data('i');
        var jumpElement= $('.'+id).parent().parent().find('li:eq(0)');
        //当前问题去掉active属性
        $('.'+id).parent().removeClass('active');
        //第一个问题加active属性
        jumpElement.addClass('active');
        var jumpId= jumpElement.find('a').data("i");
        rightContentLoad("${request.contextPath}/problem/problemDetail?id="+jumpId);
    });
    
    $('#jumpProblemIndex').click(function(){
        window.location.href="${request.contextPath}/problem/problemList";
    });
</script>