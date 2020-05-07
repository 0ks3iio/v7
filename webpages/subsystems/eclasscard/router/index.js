import index from '../pages/index'
import signIn from '../pages/signIn'
import classSpace from '../pages/classSpace'
import schoolSpace from '../pages/schoolSpace'
import activity from '../pages/activity'
import stuCourseTable from '../pages/stuCourseTable'
import stuSpace from '../pages/stuSpace'
import sticker from '../pages/sticker'

export default {
  routes: [
    {path: '/', name: 'index', component: index, meta: {}},
    {path: '/signin', name: 'signIn', component: signIn, meta: {}},
    {path: '/classSpace', name: 'classSpace', component: classSpace, meta: {}},
    {path: '/schoolSpace', name: 'schoolSpace', component: schoolSpace, meta: {}},
    {path: '/activity', name: 'activity', component: activity, meta: {}},
    {path: '/stuCourseTable', name: 'stuCourseTable', component: stuCourseTable, meta: {}},
    {path: '/stuSpace', name: 'stuSpace', component: stuSpace, meta: {}},
    {path: '/sticker', name: 'sticker', component: sticker, meta: {}},
  ]
}

