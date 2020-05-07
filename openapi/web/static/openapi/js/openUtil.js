//如果session过期 重定向
function ajaxSessionVai(result){
  var obj=result;
  if(typeof(result)=="string"){
    try{
      obj=JSON.parse(result);
    }catch(e){
      return;
    }
  }
  if(typeof(obj)=='object' && obj.code==-2){
    location.href=obj.msg;
  }
}
//load
function loadDiv(tag,url,data){
  $(tag).load(url,function(result){
    var obj=result;
    if(typeof(result)=="string"){
      try{
        obj=JSON.parse(result);
      }catch(e){
        return false;
      }
    }
    if(typeof(obj)=='object' && obj.code==-2){
      location.href=obj.msg;
    }
  }); 
}
