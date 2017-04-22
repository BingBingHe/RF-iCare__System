<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.*" %><%--
  Created by IntelliJ IDEA.
  User: Murphy
  Date: 2017/3/31
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>RF-iCare(尔福康)输液系统</title>
  <META http-equiv=Content-Type content="text/html; charset=UTF-8">
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%--<meta http-equiv="refresh" content="5">--%>

</head>


<script language="javascript">

  var XMLHttpReq;
  //创建XMLHttpRequest对象

  function createXMLHttpRequest() {
    if(window.XMLHttpRequest) { //Mozilla 浏览器
      XMLHttpReq = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) { // IE浏览器
      try {
        XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
      } catch (e) {
        try {
          XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {}
      }
    }
  }
  //发送请求函数
  function sendRequest() {

    var bed_num = <%=request.getParameter("bed_num")%>;
//    window.alert(bed_num);

    if (bed_num == null){
      setTimeout("sendRequest()", 1000);
    }else {
      createXMLHttpRequest();
      var url = "ajax.jsp" + "?bed_num=" + bed_num;
      XMLHttpReq.open("GET", url, true);
      XMLHttpReq.setRequestHeader("Content-type","application/x-www-form-urlencoded");
      XMLHttpReq.onreadystatechange = processResponse;//指定响应函数
      XMLHttpReq.send(null);  // 发送请求
    }
  }
  // 处理返回信息函数
  function processResponse() {
    if (XMLHttpReq.readyState == 4) { // 判断对象状态
      if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息
        DisplayHot();
        setTimeout("sendRequest()", 1000);
      } else { //页面不正常
        window.alert("您所请求的页面有异常。" + XMLHttpReq.status);
      }
    }
  }
  function DisplayHot() {
    var name = XMLHttpReq.responseXML.getElementsByTagName("name")[0].firstChild.nodeValue;
//    window.alert(name);

    document.getElementById("name").innerHTML = "姓&nbsp&nbsp名：" +  name;

  }


</script>



<body onload="sendRequest()">
<div style="width:100%; height: 200px;">
  <div  align="center" style="width: 30%; height: 100%; float:left">
    <img src="images/logo.png" style="max-width:100%; max-height: 100%">
  </div>

  <div align="center"  style="width: 70%; height: 50%; float:right">
    <font size="5">RF-iCare(尔福康)输液系统</font>
  </div>

  <div align="center"  style="width: 70%; height: 50%; float:right">
    <p>请输入床号：</p>
    <form name="form1" method="post">
      <input type="text" name="bed_num" id="bed_num_id">
      <input type="submit" value="确定">
    </form>
  </div>

  <div style="clear:both"></div>
</div>

<div style="width:100%; height:200px ">
  <table border="0" align="center" width="100%" height="100%">
    <tr>
      <td width="50%" id="liquid_pic"><div align="center"><img src="images/img/500_8.png" style="max-width:100%; max-height: 100%"></div></td>
      <td width="50%">
        <table border="0" width="100%" style="font-family:'宋体'; font-size:18px;">

          <tr><td id="bed_num">床&nbsp&nbsp号：</td></tr>
          <tr><td id="name">姓&nbsp&nbsp名：</td></tr>
          <tr><td id="remaining">余&nbsp&nbsp量：</td></tr>
          <tr><td id="info">药&nbsp&nbsp品：</td></tr>
          <tr><td id="timestamp">日&nbsp&nbsp期：</td></tr>
        </table>
      </td>
    </tr>
  </table>
</div>





</body>


</html>

