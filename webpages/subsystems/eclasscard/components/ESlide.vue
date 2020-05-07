<!-- 轮播图组件 -->
<template>
    <div class="box box-slider">
        <div class="slider-wrap" :style="{height:height}">
            <div class="slider-btn">
            <a href="javascript:void(0);" class="slider-prev"><span class="icon icon-angle-left"></span></a>
            <a href="javascript:void(0);" class="slider-next"><span class="icon icon-angle-right"></span></a>
            </div>
            <div class="slider-counter">{{swipeCounter}}</div>
            <swipe class="slider" :showIndicators="false" :speed="1000" :auto="10000"  @change="handleChange">
                <swipe-item class="slider-item" v-for="(photo,index) in photoList" :key="index">
                    <div class="img-wrap img-wrap-16by9" :style="{backgroundImage:`url(${photo.noPhoto ? '' : host}${photo.url})`}"></div>
                    <h4 class="slider-title">{{photo.filename}}</h4>
                </swipe-item>
            </swipe>
        </div>
    </div>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
    },
    data () {
        return {
            photoIndex: 0,
        }
    },
    props: {
        height: {
            type: String,
            default: '390px'
        }
    },
    methods: {
        handleChange(index) {
            this.photoIndex = index
        }
    },
    computed: {
        // ...mapState({
        //     photoList: store => {return [...store.photos.photos]}
        // }),
        ...mapGetters([
            'photoList'
        ]),
        swipeCounter() {
            return `${this.photoIndex+1} /${this.photoList.length}`
        },
        host() {
            return this.$store.state.domain
        }
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .img-wrap{
        height: 100%;
    }
</style>
