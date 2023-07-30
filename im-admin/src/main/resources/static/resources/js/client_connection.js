//向服务器发送的消息对象
var mess = {
  code: null, //1: 请求获取ue4服务地址,2: 发送鼠标和键盘的操作次数，3：发送断开连接请求
  wsSessionId: null,  //ws服务的会话id
  mouseClickCount: 0, //统计鼠标点击次数
  keyboardPressCount: 0, //键盘按下次数
}

var socket = null;
var wsSessionId = getQueryParam("wsSessionId");

main();

/**
 * 事件监听
 */
function main() {
  // if(wsSessionId==null || wsSessionId==undefined){
  //    alert("参数错误，缺少wsSessionId");
  //     return ;
  // }
  //socket = new WebSocket("ws://localhost:8088/manyclient_connection?wsSessionId="+wsSessionId);
  //事件监听
  window.onclick = function () {
    mess.mouseClickCount++;
    console.log("子页面发生点击事件**************");
    sendMouseAndKeyboardData();
  }
  window.onkeypress = function () {
    mess.keyboardPressCount++;
    console.log("子页发生键盘事件****************");
    sendMouseAndKeyboardData();
  }
}

function sendMsg() {
  window.huashi_ws.send("hi");
}

function sendMsg1() {
  window.huashi_ws.send("{\"hi\":1}");
}

//发送键盘和鼠标点击次数
function sendMouseAndKeyboardData() {
  if (wsSessionId == null) {
    console.log("没有找到wsSessionId");
    return;
  }
  mess.code = 2;
  mess.wsSessionId = wsSessionId;
  console.log(JSON.stringify(mess));
  if (socket != null && mess.wsSessionId != null) {
    socket.send(JSON.stringify(mess));
  }
}

/**
 * 获取url参数
 * @param key
 * @returns {string|null}
 */
function getQueryParam(key) {
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    if (pair.length > 1) {
      if (pair[0] == key) {
        return pair[1];
      }
    }
  }
  return null;
}

//测试发送消息
function sendMsg(num) {
  // if(socket==null){
  //     alert("socket未连接");
  //     return;
  // }
  switch (num) {
    case 1:
      window.huashi_ws.send("hi");
      break;
    case 2:
      window.huashi_ws.send("{'test':'hi'}")
      break;
  }
}