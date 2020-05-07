<template>
  <div id="app">
    <e-header v-if="this.$route.name !== 'sticker'" />
    <router-view/>
    <e-footer v-if="this.$route.name !== 'sticker'" />
  </div>
</template>

<script>
import eHeader from './components/EHeader'
import eFooter from './components/EFooter'
import { getWebSocket, getSticker, clock, clockIn } from './utils/api'
import { mapState } from 'vuex'

export default {
  name: 'App',
  data () {
    return {
      websocket: null,
      lockReconnect: false,
      timeoutObj: null,
      serverTimeoutObj: null,
    }
  },
  methods: {
    createWebSocket() {
      getWebSocket().then(data => {
        let sockJSUrl = data.data.sockJSUrl
        let webSocketUrl = data.data.webSocketUrl
        try {
          if ('WebSocket' in window) {
            this.websocket = new WebSocket(webSocketUrl);
          } else if ('MozWebSocket' in window) {
            this.websocket = new MozWebSocket(webSocketUrl);
          } else {
            throw ('Do not have WebSocket');
          }
          this.initEventHandle();
        } catch (e) {
          this.reconnect();
        }
      })
    },
    initEventHandle() {
      //心跳检测
      this.heartCheck();
      this.websocket.onclose = () => {
        this.reconnect();
      };
      this.websocket.onerror = () => {
        this.reconnect();
      };
      this.websocket.onopen = () => {
          //心跳检测重置
        this.heartCheck();
      };
      this.websocket.onmessage = (event) => {
        //如果获取到消息，心跳检测重置
        //拿到任何消息都说明当前连接是正常的
        let message = JSON.parse(event.data);
        switch (message.id) {
          case '0001':    //跳到考勤页
            setTimeout(() => {
              this.$store.dispatch('getClassClockIn')
              this.$router.push('/signin')
            },2000)
            break;
          case '0002':    //跳回首页
            setTimeout(() => {
              this.$router.push('/')
            },2000)
            break;
          case '0003':    //班级荣誉
            setTimeout(() => {
              this.$store.dispatch('getClassHonor')
            },2000)
            break;
          case '0004':    //个人荣誉
            setTimeout(() => {
              this.$store.dispatch('getStuHonor')
            },2000)
            break;
          case '0005':    //相册
            setTimeout(() => {
              this.$store.dispatch('getPhotos')
            },2000)
            break;
          case '0007':    //顶栏公告
            setTimeout(() => {
              this.$store.dispatch('getTopNotice')
            },2000)
            break;
// 可能存在消息同时发送接收错误
          case '0008':    //全屏公告
            setTimeout(() => {
              this.$store.dispatch('getSticker', message.text)
              this.$router.push('/sticker')
            },2000)
            break;
          case '0009':    //考场门贴
            setTimeout(() => {
              this.$router.push('/sticker')
            },2000)
            break;
          default:

            break;
        }
        this.heartCheck();
      };
    },
    heartCheck() {
      let timeout = 20000
      clearTimeout(this.timeoutObj);
      clearTimeout(this.serverTimeoutObj);
      this.timeoutObj = setTimeout(() => {
        //这里发送一个心跳，后端收到后，返回一个心跳消息，
        //onmessage拿到返回的心跳就说明连接正常
        if(this.websocket.readyState == WebSocket.OPEN){
          let heartBeat = {HeartBeat:"HeartBeat"};
          this.websocket.send(JSON.stringify(heartBeat));
        }
        this.serverTimeoutObj = setTimeout(() => {//如果超过一定时间还没重置，说明后端主动断开了
          if(this.websocket.readyState == WebSocket.OPEN){
            this.websocket.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
          }
        }, timeout)
      }, timeout)
    },
    reconnect() {
      if(this.lockReconnect) return;
      this.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      setTimeout(() => {
          this.createWebSocket();
          this.lockReconnect = false;
      }, 10000);
    },
    initClickIn() {
      window.eccClockIn = (cardNumber) => {
        if(!cardNumber||cardNumber==''||cardNumber=='0'){
            // showMsgTip("无效卡");
            return;
        }
        clock({"cardNumber":cardNumber,"objectId":this.objectId}).then( data => {               //????????????????
            var result = JSON.parse(data);
            if(result.haveStu){
              if(this.$route.name === 'signin') {         //考勤打卡
                clockIn({"cardNumber":cardNumber,"objectId":this.objectId}).then(data => {

                }).catch(err => {

                })
              } else {
                this.commit('setCardNumber', cardNumber)
                if(this.$route.name === "stuCourseTable") {
                  this.$store.dispatch('getStuLoginUser')
                }
              }
              // showStuClockMsg(result);
              this.$store.commit('setStudent', true)                      //待修改
              if(result.status!=2) {
                  if(result.ownerType=='2'){
                      // showTeaSuccess(result);
                  }else{
                      // showStuSuccess(result);
                  }
              }
            }else{
                showMsgTip(result.msg);
            }
        }).catch( err => {

        })
      }
    }
  },
  computed: {
    ...mapState({
      objectId: store => {return store.courses.objectId}
    })
  },
  created() {
    this.$store.dispatch('getTime')
    this.$store.dispatch('init')
    this.$store.dispatch('getClassHonor')
    this.$store.dispatch('getStuHonor')
    this.$store.dispatch('getPhotos')
    this.$store.dispatch('getNotify')
    this.$store.dispatch('getCoursesForm')
    this.$store.dispatch('getTopNotice')
    this.$store.dispatch('getStudents')
  },
  mounted() {
    this.createWebSocket()
    this.initClickIn()
  },
  components: {
    eHeader,
    eFooter,
  }
}
</script>

<style>
#app {
  height: 100%;
}
</style>
