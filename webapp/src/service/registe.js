import request from '../request/api'
//注册账户
export const signUpNewCompanyAccount = (data) => {
    return request('/api/userregist', {
        method: 'post',
        data
    })
}

