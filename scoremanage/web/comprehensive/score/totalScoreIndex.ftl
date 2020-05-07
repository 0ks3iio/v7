<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
			<ul class="nav nav-tabs nav-tabs-1" role="tablist">
				<li class="active" role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList('1')">学科成绩</a></li>
				<li role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList('2')">英语</a></li>
				<li role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList('3')">口试</a></li>
                <li role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList('4')">体育</a></li>
                <li role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList('5')">学考</a></li>
			</ul>
		</div>
		<div class="tab-content" id="itemShowDiv">
		</div>
	</div>
</div>
<script>
    $(function(){
	    itemShowList('1');
    });

    function itemShowList(from){
	    var url = '${request.contextPath}/comprehensive/score/filter/page?from=' + from;
        $("#itemShowDiv").load(url);
    }
</script>