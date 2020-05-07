  <meta name="description" content="" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
  
  <!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
  <link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />
  
  <link rel="stylesheet" href="${resourceUrl}/css/components.css">
  <link rel="stylesheet" href="${resourceUrl}/css/basic-data.css">
  
  <style>
	.select-port tbody tr td{cursor: pointer;}
	.select-port tbody tr td.active,.select-port tbody tr td.active:hover{background: #c5e9f9;}
  </style>
  <script type="text/javascript">
    var browser = {};//定义浏览器json数据对象
    var ua = navigator.userAgent.toLowerCase();
    var s;
    (s = ua.match(/msie ([\d.]+)/)) ? browser.ie = s[1] :
    (s = ua.match(/firefox\/([\d.]+)/)) ? browser.firefox =   s[1] :
    (s = ua.match(/chrome\/([\d.]+)/)) ? browser.chrome =   s[1] :
    (s = ua.match(/opera.([\d.]+)/)) ? browser.opera = s[1]   :
    (s = ua.match(/version\/([\d.]+).*safari/)) ?   browser.safari = s[1] : 0;
    if (browser.ie){
      document.write('<link rel="stylesheet" href="${resourceUrl}/css/pages-ie.css">');
      if(browser.ie=='9.0'){
        
      }else if(browser.ie=='8.0'){
        document.write("<script src='${resourceUrl}/components/html5shiv/dist/html5shiv.min.js'>"+"<"+"/script>");
        document.write("<script src='${resourceUrl}/components/respond/dest/respond.min.js'>"+"<"+"/script>");
      }
   }
  </script>
