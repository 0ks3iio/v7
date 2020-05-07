<!--二维码-->
<a class="js-dropbox-code js-dropbox-toggle nav-bar-qrCode" href="#">
    <i class="wpfont icon-qr-code"></i>
</a>
<div class="dropbox dropbox-code">
    <div class="dropbox-container text-center">
        <p>手机扫码下载移动OA</p>
        <img src="../images/qrcode.png" alt="">
    </div>
</div>
<script>
    $(function () {
        $('.nav-bar-qrCode').on('click',function(){
            $(this).next().toggle().end()
                    .closest('li').addClass('open')
                    .siblings().removeClass('open').find('.dropbox').hide();
        }).hover(function(){
            $('.dropbox-code').show()
        },function(){
            $('.dropbox-code').hide()
        })
    });
</script>