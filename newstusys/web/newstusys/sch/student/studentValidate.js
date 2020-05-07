function addFieldError(id,msg){
	layer.tips(msg, "#"+id, {
	    tipsMore: true,
	    tips:3
    });
	$("#"+id).focus()
}

/**
 *  11、去除前后的空格
 */
function trim(str) {
	if(str !='' && str != null){
		str= str.replace(/(^\s+)|(\s+$)/g, "");
	}
	return str;
}

/**
* 19、判断输入框中只能输入字母数字
*  
*/
function verifyLetterAndNum(elem,field){
 	var flag=false;
    var str=/[a-z]|[A-Z]|[0-9]/;
    var str1=trim(elem.value).length;
 	for(var i=0;i<str1;i++){
    	if(!str.test(trim(elem.value).charAt(i))){
	    	flag=true;
			break;
		}
	}
	if(flag==true){
	    addFieldError(elem.id,field+"只能为数字和字母！");
	   return false;
    }else {
    	return true;
    }  
 }

/**
 * 37、校验邮政编码是否有效
 *	elem：邮政编码输入框
 */
function checkPostalCode(elem){	
    if(isBlank(elem.value)) return true;
    var pattern = /^[0-9]{6}$/;
    if(pattern.test(elem.value)) {
        return true;
    }else {
   	 	addFieldError(elem.id,'邮政编码格式不对，请输入正确的邮政编码！');
        return false;
    }
}

/*
 *21、手机号码验证
 */
function checkMobilePhone(elem){
	var val=elem.value;
	if(trim(val)!=''){		
		var pattern=/^[0-9]{1,20}$/;
		//alert(val);
		if(!pattern.test(val)){
			addFieldError(elem.id,"请输入正确的号码！并且不能超过20个数字");
			return false;
		}
	}
	return true;
}

/*
 *27、固定电话号码验证
 */
function checkPhone(elem,field){
	var val=elem.value;
	if(trim(val).length==0){
		return true;
	}
	if(val.indexOf('-')==-1){
		var pattern=/^[0-9]{1,12}$/;
		if(!pattern.test(val)){
			addFieldError(elem.id,'请输入正确的'+field+",并且不能超过12个数字");
			return false;
		}
		return true;		
	}
	else{
		pattern=/^[0-9]{3,4}-[0-9]{6,8}$/;
		if(!pattern.test(val)){
			addFieldError(elem.id,'请输入正确的'+field);
			return false;
		}
		return true;		
	}		
}


/**
 * 17、校验身份证号是否有效
 */
function checkIdentityCard(elem,showPrompt){
	if(isBlank(elem.value)) return true;

	var reOld = /^[0-9]{15}$/gi;
	var reNew = /^[0-9]{17}[0-9x]{1}$/gi;

	var err = 0;

	if(reOld.test(elem.value)) {
		err = 0;
	}
	else if(reNew.test(elem.value)){
		err = 0;
	}
	else {
		err = 1;
		if(showPrompt == undefined || showPrompt)
			addFieldError(elem.id,'请输入正确的身份证号！');
		return false;
	}

	var year;
	var month;
	var day;
	var date;
	if (err == 0){
		if (elem.value.length == 15){
			year = "19" + elem.value.substring(6, 8);
			month = elem.value.substring(8, 10);
			day = elem.value.substring(10, 12)
			date = year + "-" + month + "-" + day;
		}
		else if (elem.value.length == 18){
			year = elem.value.substring(6, 10);
			month = elem.value.substring(10, 12);
			day = elem.value.substring(12, 14)
			date = year + "-" + month + "-" + day;
		}
		var checkdate = checkDate2(date, "身份证号中出生日期");
		if (checkdate == ""){
			return true;
		}
		else{
			if(showPrompt == undefined || showPrompt)
				addFieldError(elem.id, checkdate);
			return false;
		}
	}
}

/**
 * 慧平台规则：
 *0). 居民身份证
 *1). 香港身份证号码，号码格式为：A123456(1)
 *验证：^[0-9A-Z]{1,2}[0-9]{6}\([0-9A-Z]\)$
 *2). 澳门身份证号码，号码格式为：1123456(1)
 *验证：^[157][0-9]{6}\([0-9]\)$
 *3). 台湾居民来往大陆通行证，号码格式为：12345678
 *验证：^[0-9]{8}$
 *4). 台湾身份证号码，号码格式为：R123456789
 *验证：^[A-Z][0-9]{9}$
 *5)其它证件类型不作验证，长度需要大于6位
 * @param elem
 * @param idenType
 * @returns {boolean}
 */
function checkTypeIdentityCard(elem,idenTypeElem){
	if(idenTypeElem==null){
		return true;
	}

	if(isBlank(idenTypeElem.value)){
		addFieldError(idenTypeElem.id,"证件类型不能为空");
		return false;
	}

	if(idenTypeElem.value=='Z'){//可以为空不做验证
		return true;
	}

	if(isBlank(elem.value)){
		addFieldError(elem.id,"证件号码不能为空");
		return false;
	}
	if(idenTypeElem.value =='1'){
		if(!checkIdentityCard(elem)) return false;
	}

	if(idenTypeElem.value =='6'){
		var pattern=/^[0-9A-Z]{1,2}[0-9]{6}\([0-9A-Z]\)$/;
		if(!pattern.test(elem.value)){
			addFieldError(elem.id,"香港身份证号码，号码格式为：A123456(1)");
			return false;
		}
	}
	if(idenTypeElem.value =='7'){
		var pattern=/^[157][0-9]{6}\([0-9]\)$/;
		if(!pattern.test(elem.value)){
			addFieldError(elem.id,"澳门身份证号码，号码格式为：1123456(1)");
			return false;
		}
	}
	if(idenTypeElem.value =='8'){
		var pattern=/^[0-9]{8}$/;
		if(!pattern.test(elem.value)){
			addFieldError(elem.id,"台湾居民来往大陆通行证，号码格式为：12345678");
			return false;
		}
	}
	return true;
}