<a class="js-addModule" href="#"  title="添加模块">
   <i class="wpfont icon-module"></i>
</a>


<script>

    // 添加模块
	$('.js-addModule').on('click',function(){
		$('.layer-addModule').load("${request.contextPath}/desktop/index/addModule",function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: ['添加模块','font-size:16px'],
					area: ['940px','600px'],
					btn:['确定','取消'],
					yes:function(index,layero){
                      addFunctionArea("${request.contextPath}");
                    },
					content: $('.layer-addModule')
					})
		  });
	})
	
	function addFunctionArea(contextPath){
    var $active ;
    var functions = new Array();
    var n = 0;
    $(".module-style-list").find("li").each(function () {
        $this = $(this);
        var class1 = null;
        if(!$this.hasClass("selected")){
            $active = $this;
            return ;
        }
        class1 = $(this).attr("class");
        class1 = class1.replace("selected","");
        class1 = class1.replace("applied","");
        var obj = dealEValue("."+class1);
        functions[n] = obj;
        n++;
    });  
    var options;
   options = {
            url:contextPath+"/desktop/app/addFunctionAreaUser",
            data:JSON.stringify(functions),
            clearForm : false,
            resetForm : false,
            dataType:"json",
            type:'post',
            contentType: "application/json",
            success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,goHome);
                //    showSuccessMsg(data.msg);
                }else{
                    showErrorMsg(data.msg);
                }
            }
        }
    $.ajax(options);
}
   function dealEValue(container){
    var tags = ["input"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {      
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var value = $this.val();
            var name = $this.attr("name");
  //          name = name || $this.attr("id");
            obj[name] = value;
        });
    }
    return obj;
} 


</script>