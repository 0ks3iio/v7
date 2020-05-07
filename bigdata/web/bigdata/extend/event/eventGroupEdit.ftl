<form id="eventGroupSubmitForm">
	<input type="hidden" name="id" value="${group.id!}">
	<p class="color-999">概览组名称</p>
	<input type="text" class="form-control js-name" name="groupName" id="groupName" value="${group.groupName!}"  maxLength="20"/>
	<br>
	<p class="color-999">排序号</p>
	<input type="text" class="form-control js-name" name="orderId" id="orderId" value="${group.orderId!}"  maxLength="3" 
		onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
		onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
</form>