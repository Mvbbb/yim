import request from '../request/api'

//登录
export const Login = (data) => {
    return request('/api/login', {
        method: 'post',
        data
    })
}
//登出
export const Logout = () => {
    return request('/api/logout', {
        method: 'get',
    })
}
