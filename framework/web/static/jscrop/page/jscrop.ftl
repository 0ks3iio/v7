<script src="${request.contextPath}/static/jscrop/js/jquery.Jcrop.js"></script>
<script src="${request.contextPath}/static/jscrop/js/jquery.color.js"></script>
<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/jscrop/css/jquery.Jcrop.css" type="text/css" />
<link rel="stylesheet" href="${request.contextPath}/static/jscrop/css/mypage.css" type="text/css" />
<form id="jscropForm" enctype="multipart/form-data" class="form-horizontal margin-10" role="form">
    <input type="hidden" name="originZoomWidth" value="400" />
    <input type="hidden" name="originZoomHeight" value="300" />
    <input type="hidden" name="bigDescHeight" value="80" />
    <input type="hidden" name="bigDescWidth" value="80" />
    <input type="hidden" name="smallerDescHeight" value="40" />
    <input type="hidden" name="smallerDescWidth" value="40" />
    <div class="portrait-setting-container">

        <div class="jscrop-container"style="background-image: url(${request.contextPath}/static/jscrop/images/bg.png);">
                <span id="loadingPortrait" style="display: none;margin-top: 134px;z-index: 2;">
                    <img src="${request.contextPath}/static/jscrop/images/loadingPortrait.gif" />
                </span>
            <div style="display: none;" id="jscrop_container_in">
                <img src="${request.contextPath}/static/jscrop/images/auto.png" id="target" alt="">
            </div>
        </div>

        <div id="preview-pane" >
            <div class="crop-file" style="margin-top: 30px; overflow: hidden; width: 72px; margin-left: 14px;">
                <input type="file" name="image22" style="width: 72px;" accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" />
            </div>
            <div class="preview-container">
                <img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big" class="jcrop-preview" id="preview" alt="">
            </div>
            <div class="preview-container-smaller">
                <img src="${request.contextPath}/zdsoft/crop/doPortrait?type=small" class="jcrop-preview-smaller" id="preview-smaller" alt="">
            </div>

        </div>
        <div>
            <div>
                <input type="hidden" name="x" id="x" />
                <input type="hidden" name="y" id="y" />
                <input type="hidden" name="x2" id="x2" />
                <input type="hidden" name="y2" id="y2" />
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label no-padding"></label>
        <div class="col-sm-6">
            <br>
            <input type="button" class="btn btn-blue save"  value="保存" />
        </div>
        <div class="col-sm-4 control-tips">
        </div>
    </div>
</form>
<script>

    $(document).ready(function(){
        var jcrop_api,
                boundx, boundy, $preview = $('#preview-pane'),
                $pcnt = $('#preview-pane .preview-container'),
                $pimg = $('#preview-pane .preview-container img'),
                xsize = $pcnt.width(),
                ysize = $pcnt.height(),
                $pcnts = $(".preview-container-smaller"),
                $pimgs = $(".preview-container-smaller img"),
                sxsize = $pcnts.width(),
                sysize = $pcnts.height();
        function initParams(){
            $preview = $('#preview-pane');
            $pcnt = $('#preview-pane .preview-container');
            $pcnts = $(".preview-container-smaller");
            $pimg = $('#preview-pane .preview-container img');
            $pimgs = $(".preview-container-smaller img");
            xsize = $pcnt.width();
            ysize = $pcnt.height();
            sxsize = $pcnts.width();
            sysize = $pcnts.height();
        }
        $('#target').Jcrop({
                    onChange: updatePreview,
                    onSelect: updatePreview,
                    aspectRatio: 1,

                    //boxWidth:options.boxWidth,
                    //boxHeight:options.boxHeight
                }
                ,function(){
                    var bounds = this.getBounds();
                    boundx = bounds[0];
                    boundy = bounds[1];
                    jcrop_api = this;
                    $(".jcrop-holder").css("margin","auto");
                    $(".jcrop-holder").css("margin-top",Math.abs(150-$("#target").height()/2)+"px");
                }
        );
        bindClickMethod();
        function updatePreview(c){
            if (parseInt(c.w) > 0) {
                var rx = xsize / c.w;
                var ry = ysize / c.h;
                var srx = sxsize /c.w;
                var sry = sysize /c.h;
                $pimg.css({
                    width: Math.round(rx * boundx) + 'px',
                    height: Math.round(ry * boundy) + 'px',
                    marginLeft: '-' + Math.round(rx * c.x) + 'px',
                    marginTop: '-' + Math.round(ry * c.y) + 'px'}
                );
                $pimgs.css({
                    width: Math.round(srx * boundx) + 'px',
                    height: Math.round(sry * boundy) + 'px',
                    marginLeft: '-' + Math.round(srx * c.x) + 'px',
                    marginTop: '-' + Math.round(sry * c.y) + 'px'
                });
                $("#x").val(c.x);
                $("#y").val(c.y);
                $("#x2").val(c.x2);
                $("#y2").val(c.y2);
            }
        }

        function upload(){
            $("#jscrop_container_in").hide();
            $("#loadingPortrait").css("display","");
            var options = {
                url:'${request.contextPath}/zdsoft/crop/doUpload',
                type:'post',
                dataType:'json',
                success:previewReply
            }
            $("#jscropForm").ajaxSubmit(options);
        }

        function previewReply(data){
            if(data.success){
                $("#loadingPortrait").css({display:"block"})
                var zoomParams = "?originZoomWidth=400&originZoomHeight=300&"+new Date().getTime();
                $(".jscrop-container").find("img").each(function(){
                    $("#target").hide();
                    $("#target").attr("src","${request.contextPath}/zdsoft/crop/doPreview"+zoomParams);
                    $("#target").css("width","");
                    $("#target").css("height","");
                });
                $("#preview").attr("src","${request.contextPath}/zdsoft/crop/doPreview"+zoomParams);
                $("#preview-smaller").attr("src","${request.contextPath}/zdsoft/crop/doPreview"+zoomParams);
                $("#preview").hide();
                $("#download").bind("click",function(){
                    save();
                });
                $("#realWidth").val(data.width);
                $("#realHeight").val(data.height);

                try{jcrop_api.destroy();}catch (e){}
                initParams();
                $('#target').Jcrop({
                            onChange: updatePreview,
                            onSelect: updatePreview,
                            bgColor:"BLUE",
                            allowSelect:false,
                            setSelect:   [ 150, 150, 50, 50 ],
                            aspectRatio: 1,
                        }
                        ,function(){
                            var bounds = this.getBounds();
                            boundx = bounds[0];
                            boundy = bounds[1];
                            jcrop_api = this;
                            //$(".jcrop-holder").css("margin","auto");
                            $(".jcrop-holder").css("margin-top",Math.abs(150-$("#target").height()/2)+"px");
                            $("#preview").show();
                            $("#loadingPortrait").hide();
                            $("#jscrop_container_in").show();
                        }
                );

            }
        }

        function bindClickMethod() {
            $(".crop-file").find("input").on("change",function(){
                var file = $(this).val();
                if(file && file != ''){
                    upload();
                }
            });
        }
    });
    var JsCropUtils = {
        JsCropSave:function(callback){
            var file = $("input[name='image22']").val();

            var options = {
                url:'${request.contextPath}/zdsoft/crop/doSave',
                clearForm : false,
                resetForm : false,
                dataType:'json',
                type:'post',
                success:function (data) {
                    callback(data);
                }
            }
            $("#jscropForm").ajaxSubmit(options);
        }
    }
</script>
