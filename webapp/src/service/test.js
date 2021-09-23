import request from '../request/api'


//登录
export const Test = (data) => {
    return request('/api/message/offline', {
        method:'get',
        params:{
            userId: data.userId,
            token:data.token,
        }
    })
}
