<div class="layer layer-addTerm layer-change" style="display:block;height:320px" id="myDiv">
    <div class="layer-content">
        <div class="form-horizontal">
            <input type="hidden" id="state" value="${state!}">
            <div class="form-group margin-b-30">
                <label class="col-sm-2 control-label no-padding-right">科目：</label>
                <div class="col-sm-10 js-choose-condition">
                    <#if courseList?exists && courseList?size gt 0>
                        <#list courseList as course>
                            <label class="pos-rel margin-r-20">
                                <input name="course-checkbox" type="checkbox" class="wp">
                                <span class="lbl" shortName="${course.shortName!}"> ${course.subjectName!}</span>
                            </label>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="form-group layer-add">
                <label class="col-sm-2 control-label no-padding-right">选课组合：</label>
                <div class="col-sm-10">
                    <div class="publish-course publish-course-sm ">
			        <#if choiceSubjectDtoList?exists && (choiceSubjectDtoList?size>0)>
				        <#list choiceSubjectDtoList as item>
					        <input type="hidden" value="${item.ids}">
                            <#if item.state == "0">
						        <span class="subjectList" onclick="change(this)">${item.shortNames}</span>
                            <#else>
						        <span class="subjectList active" onclick="change(this)">${item.shortNames}</span>
                            </#if>
                        </#list>
                    </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="layui-layer-btn">
    <a class="layui-layer-btn0" id="arrange-commit">确定</a>
    <a class="layui-layer-btn1" id="arrange-close">取消</a>
</div>

<script>

$(function(){
	if($("#state").val() == "1") {
		$(".recommend-list span").each(function(){
			var sub = $(this).text();
			$("span.subjectList").each(function(){
				if($(this).text()==sub){
					$(this).addClass("active");
				}
			})
		})
	}else if($("#state").val() == "2"){
		$(".ban-list span").each(function(){
			var sub = $(this).text();
			$("span.subjectList").each(function(){
				if($(this).text()==sub){
					$(this).addClass("active");
				}
			})
		})
	}else if($("#state").val() == "3"){
		$(".mutex-list span").each(function(){
			var sub = $(this).text();
			$("span.subjectList").each(function(){
				if($(this).text()==sub){
					$(this).addClass("active");
				}
			})
		})
	}

	// var str = "";
	// $(".class-array .subject").each(function () {
    //     str += '<label class="pos-rel margin-r-20">\
    //                 <input name="course-checkbox" type="checkbox" class="wp">\
    //                 <span class="lbl" shortName="' + $(this).attr("shortName") + '"> ' + $(this).text() + '</span>\
    //             </label>';
    // });
	// $(".js-choose-condition").html(str);

    $('.js-choose-condition label').each(function(){
        $(this).click(function(){
            $('.layer-add').find('.publish-course-sm span').show();
            var arr = [];
            $('.js-choose-condition label').find('input:checked').each(function(){
                var t = $(this).siblings('.lbl').attr('shortName');
                arr.push(t);
            });
            $('.layer-add').find('.publish-course-sm span').each(function(){
                for (var i = 0;i < arr.length; i++) {
                    if ($(this).text().indexOf(arr[i]) == -1) {
                        $(this).hide();
                    }
                }
            });

            if ($('.js-choose-condition label').find('input:checked').length > 2){
                $('.js-choose-condition label').find('input').each(function(){
                    if ($(this).prop('checked') == false){
                        $(this).prop('disabled',true)
                    }
                });
            } else {
                $('.js-choose-condition label').find('input').each(function(){
                    if ($(this).prop('checked') == false){
                        $(this).removeAttr('disabled')
                    }
                });
            }
        });
    });

    var arr=[];
    if ($("#state").val() == "1") {
        $(".ban-list span").each(function () {
            arr.push($(this).text());
        });
        $(".layer-add span").each(function () {
            for (var i = 0; i < arr.length; i++) {
                if ($(this).text() == arr[i]) {
                    $(this).addClass("disabled");
                }
            }
        });
    } else {
        $(".recommend-list span").each(function () {
            arr.push($(this).text());
        });
        $(".layer-add span").each(function () {
            for (var i = 0; i < arr.length; i++) {
                if ($(this).text() == arr[i]) {
                    $(this).addClass("disabled");
                }
            }
        });
    }
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 // 确定按钮操作功能
 $("#arrange-commit").on("click", function(){	
 	if($("#state").val() == 1) {
	 	var content = $(".recommend-list").html();
	 	content = "";
	 	var items = $(".subjectList.active");
		items.each(function(i){
			var ids = $(this).prev().val();
	    	var shortNames = $(this).html();
	    	var str = "<input type='hidden' name='recommendList["+i+"].ids' value='"+ids+"'><span>"+shortNames+"</span>";
	    	content += str;
	 	});
	 	content += '<a href="javascript:void(0)" onclick="alertList(1)">修改</a>';
	 	$(".recommend-list").html(content);
	 	layer.closeAll();
	 }else if($("#state").val() == 2){
	 	var content = $(".ban-list").html();
	 	content = "";
	 	var items = $(".subjectList.active");
		items.each(function(i){
			var ids = $(this).prev().val();
	    	var shortNames = $(this).html();
	    	var str = "<input type='hidden' name='banCourseList["+i+"].ids' value='"+ids+"'><span>"+shortNames+"</span>";
	    	content += str;
	 	});
	 	content += '<a href="javascript:void(0)" onclick="alertList(2)">修改</a>';
	 	$(".ban-list").html(content);
	 	layer.closeAll();
	 }else if($("#state").val() == 3){//临时演示
	 	var content = $(".mutex-list").html();
	 	content = "";
	 	var items = $(".subjectList.active");
		items.each(function(i){
			var ids = $(this).prev().val();
	    	var shortNames = $(this).html();
	    	var str = "<input type='hidden' name='mutexCourseList["+i+"].ids' value='"+ids+"'><span>"+shortNames+"</span>";
	    	content += str;
	 	});
	 	content += '<a href="javascript:void(0)" onclick="alertList(3)">修改</a>';
	 	$(".mutex-list").html(content);
	 	layer.closeAll();
	 
	 }
	layer.closeAll();
});

function change(obj){
	obj = $(obj);
	if (obj.hasClass("disabled")) {
	    return;
    }
	if(obj.hasClass('active')){
		obj.removeClass('active');
	}else{
		obj.addClass('active');
	}
}

</script>

