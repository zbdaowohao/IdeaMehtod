import axios from 'axios'
import Cookies from 'js-cookie'
import iView from 'iview'
import store from '../store/index'

const service = axios.create({
  baseURL: process.env.BASE_API,
  timeout: 1000 * 60 * 1    //分钟
})
const iview = iView;
const Store = store;
service.interceptors.request.use((config) => {
  //带token
  if (Cookies.get('auth-token')) {
    config.headers['AUTH-TOKEN'] = Cookies.get('auth-token');
  }
  return config;
}, (error) => {
  console.log(error);
  iview.Notice.error({title: '请求超时！'});
  return Promise.reject("请求超时！");
})

service.interceptors.response.use((response) => {
  let token = response.headers['auth-token'] || '';
  Cookies.set('auth-token', token);
  if ("-1" === token) {
    Store.dispatch("Logout");
  }
  if (response.status && response.status == 200) {
    if (response.data.returnState == '0') {
      iview.Message.error(response.data.errorMessage || '业务异常！', 6);
    } else if (response.data.returnState == '-1') {
      iview.Message.error(response.data.errorMessage || '系统异常！', 6);
    }
  }
  return response.data;
}, (err) => {
  console.log(err);
  iview.Notice.error({title: "请求失败！"});
  return Promise.reject("请求失败！");
})

export default service
