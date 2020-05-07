<!-- 首页 -->
<template>
  <div class="main">
    <div class="main-container">
      <div class="grid">
        <e-msg />
        <div class="grid-cell grid-4of10">
          <e-slide />
          <e-notify />
        </div>
        <div class="grid-cell grid-3of10">
          <e-courses />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import eMsg from '../components/EMsg'
import eCourses from '../components/ECourses'
import eSlide from '../components/ESlide'
import eNotify from '../components/ENotify'
export default {
  name: 'index',
  components: {
    eMsg,
    eSlide,
    eNotify,
    eCourses,
  },
  data () {
    return {
    
      aaa: {}
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.main{
}
</style>
