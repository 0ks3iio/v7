/* 
* @Author: anchen
* @Date:   2018-03-12 17:36:03
* @Last Modified by:   anchen
* @Last Modified time: 2018-03-12 17:36:37
*/
var star = (function(){
  //继承,subClass参数为子类，superClass为父类
  var extend = function(subClass,superClass){
      var F = function(){};//创建一个空的函数
      F.prototype = superClass.prototype;
      subClass.prototype = new F();
      subClass.prototype.constructor = subClass;
  };
  //点亮
  var Light = function(el,options){
     this.$el = $(el);
     this.opts = options;
     this.$item = this.$el.find('.rating-item');
     this.add = 1;
     this.selectEvent = 'mousemove';
  };
  Light.prototype.init = function(){ //在构造函数Light中添加init方法
     this.lightOn(this.opts.num);
     //判断是否只读，如果不是只读，绑定鼠标事件
     if(!this.opts.readOnly){
        this.bindEvent(); //绑定鼠标事件
     }
  };
  Light.prototype.lightOn = function(num){//在构造函数Light中添加默认点亮的个数
    num = parseInt(num);
    this.$item.each(function(i) {
        if(i < num){
          $(this).css('background-position','-47px 0');  
        }else{
          $(this).css('background-position','0 0');  
        }
    });
  };
  Light.prototype.bindEvent = function(){//在构造函数Light中添加鼠标事件
    var self = this,
        itemLength = self.$item.length;
    self.$el.on(self.selectEvent,'.rating-item',function(e){
       var $this = $(this),
           num = 0;
       self.select(e,$this);
       num = $(this).index() + self.add;
       self.lightOn(num);
       (typeof self.opts.select === 'function') && self.opts.select.call(this,self.num,itemLength); 
       self.$el.trigger('select',[self.opts.num,itemLength]);
    })
    .on('click','.rating-item',function(){
        self.opts.num = $(this).index() + self.add;
        (typeof self.opts.chosen === 'function') && self.opts.chosen.call(this,self.opts.num,itemLength); 
        self.$el.trigger('chosen',[self.opts.num,itemLength]);
    })
    .on('mouesout','.rating-item',function(){
        self.lightOn(self.opts.num);
    });
  };
  Light.prototype.select = function(){
        throw new Error('子类必须重写此方法');
  };
  Light.prototype.unbindEvent = function(){
        this.$el.off();
  };

  //点亮整颗
  var LightEntire = function(el,options){
      Light.call(this,el,options);//调用父类构造函数的内容
      this.selectEvent = 'mouseover';
  };
  extend(LightEntire,Light); //LightEntire继承Light
  LightEntire.prototype.lightOn = function(num){
     Light.prototype.lightOn.call(this,num);
  };
  LightEntire.prototype.select = function(){
     this.selectEvent = 'mouseover';
  };

  //点亮半颗
  var LightHalf = function(el,options){
      Light.call(this,el,options);//调用父类构造函数的内容
      this.selectEvent = 'mousemove';
  };
  extend(LightHalf,Light); //LightHalf继承Light
  LightHalf.prototype.lightOn = function(num){
     var count = parseInt(num),  //将num转换为整数
         isHalf = count!==num; //判断num是否为整数，并把布乃值赋予给isHalf
     Light.prototype.lightOn.call(this,count);
     if(isHalf){ //如果num不为整数，则背景半颗星
         this.$item.eq(count).css('background-position','-93px 0');
     }
  };
  LightHalf.prototype.select = function(e,$this){
    //如果鼠标移动的位置 - 元素的左偏移量 < 该元素半个宽。
     if(e.pageX - $this.offset().left < $this.width()/2){
        this.add = 0.5; //加半颗星
     }else{
        this.add = 1; //加一颗星
     }
  };
  
  //默认参数
  var defaults = {
      mode:'LightEntire',
      num:0,
      readOnly:false,
      select:function(){},
      chosen:function(){}
  }; 

  //方法模式
  var mode = {
     'LightEntire':LightEntire,
     'LightHalf':LightHalf
  };
  
  //初始化方法
  var init = function(el,option){
     var $el = $(el),
         dep = $el.data('deposit'), //取回数据deposit并赋予给dep
         options = $.extend({},defaults,typeof option === 'object' && option);
         if(!mode[options.mode]){ //模式选错时默认点颗整颗星
           options.mode = 'LightEntire';
         }
         if(!dep){ //如果数据为空，则向元素附加数据
            $el.data('deposit',(rating = new mode[options.mode](el,options)));
            rating.init();
         }
         if(typeof option === 'string')rating[option]();
  };

  //封装jq插件
  $.fn.extend({
      star:function(option){
          return this.each(function(){
              init(this,option);
          })
      }
  });

  //返回一个对象,该对象包含init方法
    return {
        init: init
    }

})();
