import request from '../request/api'

//登录
export const RecentChat = (data) => {
    console.log(data)
    return request('/api/recent/chat', {
        method: 'get',
        headers: {
            userId: data.userId,
            token: data.token
        }
    })
}

