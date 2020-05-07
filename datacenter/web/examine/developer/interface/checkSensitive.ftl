<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">${title?default('字段')}：</label>
			<div class="col-sm-6" >
			  <#if entitys?exists && entitys?size gt 0>
	             <#list entitys as dto>
	               <label class="inline">
	                 <input  class="wp xgwp" value="${dto.columnName!}" type="checkbox"  <#if dto.isAutho==1> checked </#if>>
	                 <span class="lbl">${dto.displayName!} </span>
	               </label>
	             </#list>
              </#if>
			</div>
		</div>
	</div>
	<script>
	//更新敏感字段
	function saveSensitive(){
		var arr=new Array();
	    $('.form-group').find('.xgwp').each(function(){
	      var $this=$(this)
	      if($this.prop('checked')){
	        arr.push($this.val());
	      }
	    });
	    if(arr.length==0){
	      arr.push('');
	    }
	    var url = '${request.contextPath}/system/developer/modifyEntity';
	    var params = {"columnNames":arr,'type':'${type!}','ticketKey':$('#ticketKey').val(), 'isSensitive':${isSensitive!}};
    	$.ajax({
    		   type: "POST",
    		   url: url,
    		   data: params,
    		   success: function(data){
    			 if(data.success){
    				 showSuccessMsgWithCall(data.msg,lookInterface('bb','1'));
    			 }else{
    				 showErrorMsg(data.msg);
    			 }
    		   },
    		   dataType: "JSON"
    		});
	}
   </script>
</div>