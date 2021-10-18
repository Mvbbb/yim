import axios from 'axios'
// import {Decrypt} from "../util/secret";



// const CODE_MESSAGE =  {
//   200: '服务器成功返回请求的数据。',
//   201: '新建或修改数据成功。',
//   202: '一个请求已经进入后台排队（异步任务）。',
//   204: '删除数据成功。',
//   400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
//   401: '用户没有权限（令牌、用户名、密码错误）。',
//   403: '用户得到授权，但是访问是被禁止的。',
//   404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
//   406: '请求的格式不可得。',
//   410: '请求的资源被永久删除，且不会再得到的。',
//   422: '当创建一个对象时，发生一个验证错误。',
//   500: '服务器发生错误，请检查服务器。',
//   502: '网关错误。',
//   503: '服务不可用，服务器暂时过载或维护。',
//   504: '网关超时。'
// }
//
// const HandleAuthFailed = () => {
//   let allRecruit = store.state.name
//   return router.push({name: 'LoginChart'})
// }

export default (url, options={}) => {

  const defaultOptions = {
    method: 'get',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json; charset=utf-8',
    }
  }
  if(localStorage.getItem('Authorization')!= null){
    defaultOptions.headers["Authorization"] = localStorage.getItem('Authorization')
  }

  const newOptions = {...defaultOptions, ...options, url}


  return new Promise((resolve,reject) => {
    axios(newOptions).then((response) => {
      // console.log(response.data)
      // if (response.data.code === HttpResponseCode.invalid) {
      //   console.log(JSON.stringify(newOptions))
      //   console.log(JSON.stringify(response))
      //   HandleAuthFailed()
      // }
      // if (response.data.code === HttpResponseCode.success) {
      //   return resolve(response.data)
      // }
      // if (!response.data.code) {
      //   return reject({
      //     code: 2001,
      //     message: '服务端返回的数据格式不规范,无法解析',
      //   })
      // }
      // if (response.data)
      return resolve(response)
      }).catch((error) => {
      return reject(error)

    })
  })
}
