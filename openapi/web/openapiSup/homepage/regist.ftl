<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>开发者注册</title>
<link rel="stylesheet" href="${resourceUrl}/openapi/css/public.css">
<link rel="stylesheet" href="${resourceUrl}/openapi/css/style.css">
</head>

<body>
<div id="header">开发者注册</div>
<form name="developer" id="developer" method="post">  
    name: <input type="text" name="username"><br/>    
    pass: <input type="password" name="password"><br/>    
    developer: <input type="text" name="unitName"><br/>    
    realName: <input type="text" name="realName"><br/>
    ips: <input type="text" name="ips"><br/>  
    mobilePhone: <input type="text" name="mobilePhone"><br/>  
    email: <input type="text" name="email"><br/>  
    description: <input type="text" name="description"><br/>  
    ticketKey: <input type="text" name="ticketKey"><br/>      
    <input type="button" id="submit" value="提交"/>    
</form>
<div id="container" class="fn-clear">

</div>
<script type="text/javascript" src="${resourceUrl}/openapi/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/openapi/js/myscript.js"></script>
<script type="text/javascript" src="${resourceUrl}/openapi/js/json2.js"></script>
<script type="text/javascript" src="${resourceUrl}/openapi/js/jquery.Layer.js"></script>
<script type="text/javascript">

  $('input[name="username"]').blur(function(){
    var value=$(this).val();
    if($.trim(value)==""){
      alert("not null");
      return;
    }
    ajaxValide('userName/'+value);
  });
  $('input[name="unitName"]').blur(function(){
    var value=$(this).val();
    if($.trim(value)==""){
      alert("not null")
    }
    ajaxValide('unitName/'+value);
  });
  function ajaxValide(value){
    var url='/openapi/developer/regist/valid/'+value;
    $.ajax({
      url:url,
      type:'GET',
      success:function(data){
        var jsonO = JSON.parse(data);
        alert(jsonO.msg);
      }
    })
  };
  $('#submit').click(function(){
    $.ajax({
      url:'/openapi/developer/regist/save',
      type:'POST',
      data:$('#developer').serialize(),
      success:function(msg){
        var jsonO=JSON.parse(msg);
        if(jsonO.success){
          alert(jsonO.msg);
        }else{
          alert(jsonO.msg);
        }
      }
    });
  });
</script>
</body>
</html>
