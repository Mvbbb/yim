import request from '../request/api'
//注册账户
export const signUpAccount = (data) => {
  return request('/api/register', {
    method:'post',
    data
  })
}

