
/**
 * 取得一个输入框中的数字，返回float
 * obj为文本框对象
 * 若数字不规范则返回0
 * Created by Sun Ziyi 2002-08-30
 * @return
 */
function getFloat(obj){
  if (!obj){
    alert(obj+"对象不存在！");
    return (new float(0));
  }
  if (!obj.value){
    return (new float(0));
  }
  if (isNaN(obj.value)){
    return (new float(0));
  }
  var sObj = new String(obj.value);
  if (sObj.length<=0){
    return (new float(0));
  }

  return parseFloat(obj.value);
}

/**
 * 取得四舍五入后的小数(小数点后两位)
 * Created by Sun Ziyi 2002-08-30
 * @return
 */
function getRoundFloat(x){
  if (isNaN(x)){
    return 0;
  }
  var d = Math.round(x*10000)/100;
  return d;
}

  function checkNum(obj,objName)	//参数：需要检测的对象；显示在警告窗口中的输入项名。
  {
    var flag=0;
    var dotFlag=0;
    var dot=".";
    var num="E.0123456789,-()";
    for (var i=0;i<(obj.value.length);i++)
    {
      tmp=obj.value.substring(i,i+1);
      if (num.indexOf(tmp)<0)
        flag++;
      if (tmp.indexOf(dot)>=0)
                dotFlag++;
    }
    if(flag>0||dotFlag>1)
    {
      alert(objName + "必须输入数字！") ;
      obj.focus();
      obj.select()
      return (false);
    }
    return (true);
  }
  //检查是不是正整数
  function checkInt(obj,objName)	//参数：需要检测的对象；显示在警告窗口中的输入项名。
  {
    var flag=0;
    var num="0123456789";
    for (var i=0;i<(obj.value.length);i++)
    {
      tmp=obj.value.substring(i,i+1);
      if (num.indexOf(tmp)<0)
        flag++;
    }
    if(flag>0)
    {
      alert(objName + "必须输入正整数！") ;
      obj.focus();
      obj.select()
      return (false);
    }
    return (true);
  }
  function checkPhone(obj,objName)
  {
    var flag=0;
    var num=".0123456789/-()";
    for (var i=0;i<(obj.value.length);i++)
    {
      tmp=obj.value.substring(i,i+1);
      if (num.indexOf(tmp)<0)
        flag++;
    }
    if(flag>0)
    {
      alert(objName + "输入了不正确的电话号码！") ;
      obj.focus();
      obj.select()
      return (false);
    }
    return (true);
  }

  function checkEmail(obj,objName)
  {
    if(obj.value=="")return true;
    if (obj.value.indexOf("@")<0)
    {
      alert("输入了不正确的Email地址！") ;
      obj.focus();
      obj.select()
      return (false);
    }
    if (obj.value.indexOf(".")<0)
    {
      alert(objName + "输入了不正确的Email地址！") ;
      obj.focus();
      obj.select()
      return (false);
    }
    return (true);
  }

  function checkEmpty(obj,objName)
  {
    if((obj.value==null)||(obj.value.length)==0)
    {
      alert(objName + "不能为空");
      obj.focus();
      return (false);
    }
    return (true);
  }

  function checkDate(obj,objName)	//yyyy-mm-dd or yyyy/mm/dd
  {
    var flag=0
    var leap=0
    var num="0123456789-/"
    var strArray = obj.value.split("/");

    for (var i=0;i<(obj.value.length);i++)
    {
      tmp=obj.value.substring(i,i+1)
      if (num.indexOf(tmp)<0)
        flag++
    }

    if (strArray.length!=3)
    {
      flag++;
    }else
    {
      for (var i=0;i<3;i++)
      {
        if (strArray[i] == "")
        {
          flag++;
        }
      }

      var years = parseInt(strArray[0],10);
      var mons = parseInt(strArray[1],10);
      var days = parseInt(strArray[2],10);
      if (strArray[1].length!=2||strArray[2].length!=2) flag++;

      if (years>3000 || years<1900)
        flag++;
      if (mons>12 || mons<1)
        flag++;
      if (days < 1)
        flag++;

      if (mons==2)
      {
        if (years%4==0)
        {
          if(years%100==0)
          {
            if(years%400==0)
              leap = 1;
            else
              leap = 0;
          }else
          {
            leap = 1;
          }
        }else
        {
          leap = 0;
        }

        if (leap==0 && days>28)
          flag++;
        else if (leap==1 && days>29)
          flag++;

      }else if (mons==1||mons==3||mons==5||mons==7||mons==8||mons==10||mons==12)
      {
        if (days>31)
          flag++;
      }else
      {
        if (days > 30)
          flag++;
      }
    }

    if(flag>0)
    {
      alert(objName + "输入了不正确的日期！"+"\n正确的格式为\"2001/07/01 13:01:40\"")
      obj.focus()
      obj.select()
      return (false)
    }
    return (true)
  }
/**
  *检查键盘输入是否为数字
  */
function defInput(type)
{
  if(type!="" || type!=null)
  {

          var str=type.toLowerCase();

          if(str=='number')
          {
                  switch(window.event.keyCode)
                  {
                          case 13:
                          case 108:
                          case 37:
                          case 39:
                          case 8:
                          case 9:
                          case 46:
                          case 144:
                          break;
                          default:
                          if(((event.keyCode>=48) && (event.keyCode<=57)))
                          return true;
                          else
                          {
                                  event.returnValue=0;
                                  return false;
                          }
                    }
              }
    }
}
    /**
     *将字符串转成浮点数
     */
    function strToDouble(obj1){
      var temp="";
      var tmp;
      var num=".0123456789-";
      var obj=""+obj1;

      for (var i=0;i<(obj.length);i++)
      {
        tmp=obj.substring(i,i+1)
        if (num.indexOf(tmp)<0)
          temp=temp+tmp;
      }
      return parseFloat(temp);
    }



          function comparedate(objstartdate,objenddate)//2001-12-25 lwg
          {
            var startyear;
            var endyear;
            var startmonth;
            var endmonth;
            var startday;
            var endday;
            var temp;
            var strlen;
            temp=objstartdate.value;
            strlen=temp.length;
            startyear=temp.substring(0,4);
            temp=temp.substring(5,strlen);
            var temp1;
            temp1=temp.indexOf("/");
            startmonth=temp.substring(0,temp1);
            var temp2;
            temp2=temp.indexOf("/");
            startday=temp.substring(temp2+1,strlen);
            var firdate=new Date(startyear,startmonth,startday);
            temp=objenddate.value;
            strlen=temp.length;
            endyear=temp.substring(0,4);
            temp=temp.substring(5,strlen);
            temp1=temp.indexOf("/");
            endmonth=temp.substring(0,temp1);
            temp2=temp.indexOf("/");
            endday=temp.substring(temp2+1,strlen);
            var seconddate=new Date(endyear,endmonth,endday);
            if(seconddate<=firdate)
            {
              alert("聘期截止日期必须大于起始日期");
              objstartdate.focus();
              return false;
            }
            else
              return true;
          }

          function comparedate1(objstartdate,objenddate)//2001-12-25 lwg
          {
            if (objstartdate.value!="" && objenddate.value!="") {
            var startyear;
            var endyear;
            var startmonth;
            var endmonth;
            var startday;
            var endday;
            var temp;
            var strlen;
            temp=objstartdate.value;
            strlen=temp.length;
            startyear=temp.substring(0,4);
            temp=temp.substring(5,strlen);
            var temp1;
            temp1=temp.indexOf("/");
            startmonth=temp.substring(0,temp1);
            var temp2;
            temp2=temp.indexOf("/");
            startday=temp.substring(temp2+1,strlen);
            var firdate=new Date(startyear,startmonth,startday);
            temp=objenddate.value;
            strlen=temp.length;
            endyear=temp.substring(0,4);
            temp=temp.substring(5,strlen);
            temp1=temp.indexOf("/");
            endmonth=temp.substring(0,temp1);
            temp2=temp.indexOf("/");
            endday=temp.substring(temp2+1,strlen);
            var seconddate=new Date(endyear,endmonth,endday);
            if(seconddate<=firdate)
            {
              alert("聘期截止日期必须大于起始日期");
              objstartdate.focus();
              return false;
            }
            else
              return true;
          }
          }