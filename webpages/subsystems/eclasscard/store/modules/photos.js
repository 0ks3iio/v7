import { getAlbum } from '../../utils/api'

const state = {
    photos: [],
    video: '',
    type: 1,
}

const getters = {
    photoList (state) {
        let noPhoto = [{filename: '暂无图片', url: require('../../assets/imgs/body-bg.png'), noPhoto: true}]
        if(state.photos.length) {
            return state.photos
        } else {
            return noPhoto
        }
    }
}

const actions = {
    getPhotos (context) {
        getAlbum().then((data) => {
            context.commit('setPhotos', data.data.attachs)
            context.commit('setVideo', data.data.videoUrl)
        }).catch(err => {
            
        })
    }
}

const mutations = {
    setPhotos (state, payload) {
        state.photos = payload
    },
    setVideo (state, payload) {
        state.video = payload
    },
    setType (state, payload) {
        state.type = payload
    }
}

export default ({
    state,
    getters,
    actions,
    mutations,
})