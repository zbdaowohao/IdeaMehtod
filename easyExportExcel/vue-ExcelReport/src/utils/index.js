/**
 * 工具类
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null;
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}';
  let date;
  if (typeof time == 'object') {
    date = time;
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000;
    date = new Date(time);
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  };
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key];
    if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1];
    if (result.length > 0 && value < 10) {
      value = '0' + value;
    }
    return value || 0;
  });
  if(time_str=='000'){
	  return time;
  }
  return time_str;
}

export function formatTime(time, option) {
  time = +time * 1000;
  const d = new Date(time);
  const now = Date.now();

  const diff = (now - d) / 1000;

  if (diff < 30) {
    return '刚刚';
  } else if (diff < 3600) { // less 1 hour
    return Math.ceil(diff / 60) + '分钟前';
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前';
  } else if (diff < 3600 * 24 * 2) {
    return '1天前';
  }
  if (option) {
    return parseTime(time, option);
  } else {
    return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' +
      d.getMinutes() + '分';
  }
}

// 格式化时间
export function getQueryObject(url) {
  url = url == null ? window.location.href : url;
  const search = url.substring(url.lastIndexOf('?') + 1);
  const obj = {};
  const reg = /([^?&=]+)=([^?&=]*)/g;
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1);
    let val = decodeURIComponent($2);
    val = String(val);
    obj[name] = val;
    return rs;
  });
  return obj;
}

/**
 *get getByteLen
 * @param {Sting} val input value
 * @returns {number} output value
 */
export function getByteLen(val) {
  let len = 0;
  for (let i = 0; i < val.length; i++) {
    if (val[i].match(/[^\x00-\xff]/ig) != null) {
      len += 1;
    } else {
      len += 0.5;
    }
  }
  return Math.floor(len);
}

export function cleanArray(actual) {
  const newArray = [];
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i]);
    }
  }
  return newArray;
}

export function param(json) {
  if (!json) return '';
  return cleanArray(Object.keys(json).map(key => {
    if (json[key] === undefined || json[key] == null) return '';
    return encodeURIComponent(key) + '=' +
      encodeURIComponent(json[key]);
  })).join('&');
}

export function param2Obj(url) {
  const search = url.split('?')[1];
  if (!search) {
    return {};
  }
  return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
}

export function html2Text(val) {
  const div = document.createElement('div');
  div.innerHTML = val;
  return div.textContent || div.innerText;
}

export function objectMerge(target, source) {
  /* Merges two  objects,
   giving the last one precedence */

  if (typeof target !== 'object') {
    target = {};
  }
  if (Array.isArray(source)) {
    return source.slice();
  }
  for (const property in source) {
    if (source.hasOwnProperty(property)) {
      const sourceProperty = source[property];
      if (typeof sourceProperty === 'object') {
        target[property] = objectMerge(target[property], sourceProperty);
        continue;
      }
      target[property] = sourceProperty;
    }
  }
  return target;
}

export function scrollTo(element, to, duration) {
  if (duration <= 0) return;
  const difference = to - element.scrollTop;
  const perTick = difference / duration * 10;
  setTimeout(() => {
    console.log(new Date());
    element.scrollTop = element.scrollTop + perTick;
    if (element.scrollTop === to) return;
    scrollTo(element, to, duration - 10);
  }, 10);
}

export function toggleClass(element, className) {
  if (!element || !className) {
    return;
  }
  let classString = element.className;
  const nameIndex = classString.indexOf(className);
  if (nameIndex === -1) {
    classString += '' + className;
  } else {
    classString = classString.substr(0, nameIndex) +
      classString.substr(nameIndex + className.length);
  }
  element.className = classString;
}

export const pickerOptions = [
  {
    text: '今天',
    onClick(picker) {
      const end = new Date();
      const start = new Date(new Date().toDateString());
      end.setTime(start.getTime());
      picker.$emit('pick', [start, end]);
    },
  }, {
    text: '最近一周',
    onClick(picker) {
      const end = new Date(new Date().toDateString());
      const start = new Date();
      start.setTime(end.getTime() - 3600 * 1000 * 24 * 7);
      picker.$emit('pick', [start, end]);
    },
  }, {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date(new Date().toDateString());
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      picker.$emit('pick', [start, end]);
    },
  }, {
    text: '最近三个月',
    onClick(picker) {
      const end = new Date(new Date().toDateString());
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      picker.$emit('pick', [start, end]);
    },
  }];

export function getTime(type) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90;
  } else {
    return new Date(new Date().toDateString());
  }
}

// export function debounce(func, wait, immediate) {
//   let timeout, args, context, timestamp, result;

//   const later = function () {
//     // 据上一次触发时间间隔
//     const last = +new Date() - timestamp;

//     // 上次被包装函数被调用时间间隔last小于设定时间间隔wait
//     if (last < wait && last > 0) {
//       timeout = setTimeout(later, wait - last);
//     } else {
//       timeout = null;
//       // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
//       if (!immediate) {
//         result = func.apply(context, args);
//         if (!timeout) context = args = null;
//       }
//     }
//   };

//   return function (...args) {
//     context = this;
//     timestamp = +new Date();
//     const callNow = immediate && !timeout;
//     // 如果延时不存在，重新设定延时
//     if (!timeout) timeout = setTimeout(later, wait);
//     if (callNow) {
//       result = func.apply(context, args);
//       context = args = null;
//     }

//     return result;
//   };
// }

export function debounce(fn, wait) {
  let timer = null;
  return function () {
    let context = this;
    let args = arguments;
    if (timer) {
      clearTimeout(timer);
      timer = null;
    }
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, wait);
  }
}

/**
 * 判断是否为空对象
 * @param {Object} obj 要判断的对象
 */
export function isEmptyObject(obj) {
  for (var item in obj) {
    if (obj.hasOwnProperty(item)) {
      return false
    }
  }
  return true
}

/**
 * 获取屏幕可视高度宽度
 * @param {String} type width(宽度) height(高度)
 * @return {String|Oject} 有 type 参数则返回参数对应的 String 否则返回一个含有全部内容的 Object
 */
export function client(type) {
  var obj = {}
  if (window.innerWidth != null) { // ie9 +  最新浏览器
    obj.width = window.innerWidth
    obj.height = window.innerHeight
  } else if (document.compatMode === 'CSS1Compat') { // 标准浏览器
    obj.width = document.documentElement.clientWidth
    obj.height = document.documentElement.clientHeight
  }
  // 怪异浏览器
  obj.width = document.body.clientWidth
  obj.height = document.body.clientHeight
  if (type === 'width') {
    return obj.width
  } else if (type === 'height') {
    return obj.height
  } else {
    return obj
  }
}
/**
 * 内容区域的高度
 */
export function mainHeigh() {
  //headerNav的高度是60px；breadNav的高度是50px  ，
  // 还有俩个margin为18 footer的高度是38px
  //内容区域的上下padding为30px
  // 先手动减去，后期优化为动态减去
  // let h = client('height') - 60 - 50 - 18 - 38 - 30 - 30;
  let h = client('height')  - 52 - 40 - 38 - 30 -15;
  return h;
}
/**
 * 表格上方的Cform的高度
 */
export function cFormHeigh() {
    let cform =  document.getElementsByClassName('cform');
    let height = 60;
    for(let i = 0 ;i<cform.length;i++){
      height = cform[0].clientHeight;
    }

  return height;
}
/**
 * 获取表格分页page的高度
 */
export function pageHeigh() {
  let page =  document.getElementsByClassName('table-page');
  let height = 60;
  for(let i = 0 ;i<page.length;i++){
    height = page[0].clientHeight;
  }
  return height;
}

/**
 * 主内容区域的表格高度，
 * 如果用了tableLayOut才能用此方法 ，
 * 如果没用tableLayOut不能用此方法，需要手动减去
 */
export function tableLayOutHeigh() {
    let h = mainHeigh() - cFormHeigh() - pageHeigh();
    return h;
}
/**
 * 树的自适应高度
 */
export function treeSiteHeigh() {
    let h = mainHeigh() - 30;
    return h;
}

/**
 * 解析组织机构树
 * @param orgTreeList
 * @returns {Array}
 */
export function parseOrgTree(orgTreeList) {
  let fmOrgTreeList = []
  orgTreeList.forEach(orgTree => {
    let {
      attributes,
      checked,
      children,
      iconCls,
      id,
      merchantTag,
      state,
      text
    } = orgTree;
    if (children && children instanceof Array && children.length > 0) {
      children = parseOrgTree(children)
    }
    let fmOrgTree = {
      id: id,
      title: text,
      children: children,
      expand: (state == 'close' ? true : false)
    }
    fmOrgTreeList.push(fmOrgTree)
  })
  return fmOrgTreeList;
}


/**
 * 解析组织机构树为穿梭框赋值
 * @param orgTreeList
 * @returns {Array}
 */
export function parseOrgToTransfer(orgTreeList) {
  let fmOrgList = []
  orgTreeList.forEach(orgTree => {
    let {
      id,
      organizationname,
      organizationcode,
    } = orgTree;
    let fmOrg = {
      key: id,
      label: organizationname,
      description: organizationcode,
      disabled: false
    }
    fmOrgList.push(fmOrg)
  })
  return fmOrgList;
}

/**
 * 为商品穿梭框赋值
 * @param orgTreeList
 * @returns {Array}
 */
export function parseProductToTransfer(productList) {
  let tfProductList = []
  productList.forEach(product => {
    let {
      productId,
      productName,
      stopDay,
      startDay,
      productPutrangeId
    } = product;
    if(productPutrangeId && null != productPutrangeId){
      let tfProduct = {
        key: productId,
        label: productName,
        description:'(  有效期:'+startDay+'--'+stopDay+')',
        disabled: false
      }
      tfProductList.push(tfProduct)
    }else{
      let tfProduct = {
        key: productId,
        label: productName,
        description:'',
        disabled: false
      }
      tfProductList.push(tfProduct)
    }
  })
  return tfProductList;
}
export function parseDeliveryProduct(productList) {
  let tfProductList = []
  productList.forEach(product => {
    let {
      productId,
      productName,
      stopDay,
      startDay,
      productPutrangeId
    } = product;
    if(productPutrangeId && null != productPutrangeId) {
      let tfProduct = {
        id: productId,
        start: startDay,
        stop: stopDay
      }
      tfProductList.push(tfProduct)
    }
  })
  return tfProductList;
}
/**
 * 为站厅穿梭框赋值
 * @param orgTreeList
 * @returns {Array}
 */
export function parseRommsToTransfer(roomList) {
  let tfProductList = []
  roomList.forEach(room => {
    let {
      merchant_room_id,
      room_name,
      vstartDay,
      vstopDay
    } = room;
    if(null != vstartDay || null != vstopDay){
      let tfroom = {
        key: merchant_room_id,
        label: room_name,
        description:'(  有效期:'+vstartDay+'--'+vstopDay+')',
        disabled: false
      }
      tfProductList.push(tfroom)
    }else {
      let tfroom = {
        key: merchant_room_id,
        label: room_name,
        description:'',
        disabled: false
      }
      tfProductList.push(tfroom)
    }
})
  return tfProductList;
}
export function parseDeliveryItemList(roomList) {
  let tfProductList = []
  roomList.forEach(room => {
    let {
      merchant_room_id,
      vstartDay,
      vstopDay
    } = room;
    if(null != vstartDay && null != vstopDay){
      let tfroom = {
        id:merchant_room_id,
        start:vstartDay,
        stop:vstopDay,
      }
      tfProductList.push(tfroom)
    }
  })
  return tfProductList;
}
export function parseCompany(list){
  let companyList = []
  list.forEach(item=> {
    let{
      companyId,
      companyName
    } =item;
    let companySelect = {
      key:companyId,
      label:companyName,
      value:companyId
    }
    companyList.push(companySelect)
  })
  return companyList;
}
export function parseOrgData(list){
  let companyList = []
  list.forEach(item=> {
    let{
      id,
      organizationname,
      organizationcode,
      parentid
    } =item;
  let companySelect = {
    key:id,
    label:organizationname,
    code:organizationcode,
    value:id+","+organizationcode
  }
  companyList.push(companySelect)
})
  return companyList;
}

export function parseroomData(list) {
  let companyList = []
  list.forEach(item => {
    let {
      merchant_room_id,
      room_name,
      organization_id
    } = item;
    let companySelect = {
      key: merchant_room_id,
      label: room_name,
      id: organization_id,
      value: merchant_room_id
    }
    companyList.push(companySelect)
  })
  return companyList;
}
export function parsepgRommsToTransfer(roomList) {
  let tfProductList = []
  roomList.forEach(room => {
  tfProductList.push(merchant_room_id)
});
  return tfProductList;
}
/**
 * 判断数据类型
 * @param {Array} params 原数据
 */
export function typeOf(params) {
  const toString = Object.prototype.toString;
  const map = {
    '[object Boolean]': 'boolean',
    '[object Number]': 'number',
    '[object String]': 'string',
    '[object Function]': 'function',
    '[object Array]': 'array',
    '[object Date]': 'date',
    '[object RegExp]': 'regExp',
    '[object Undefined]': 'undefined',
    '[object Null]': 'null',
    '[object Object]': 'object'
  };
  return map[toString.call(params)];
}


/**
 *获取当前日期
 *
 */
export function getNowData(params) {
  let date = new Date();
  let seperator1 = "-";
  let seperator2 = ":";
  let month = date.getMonth() + 1;
  let strDate = date.getDate();
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = "0" + strDate;
  }
  let currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
  return currentdate;

}


/**
 *精确计算乘法
 *
 */
export function accMul(num1,num2) {
  var m=0,s1=num1.toString(),s2=num2.toString();
  try{m+=s1.split(".")[1].length}catch(e){}
  try{m+=s2.split(".")[1].length}catch(e){}
  return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}


/**
 *表格滚动条
 *
 */
export function tableScroll(){
  let menuItemSelect =  document.getElementsByClassName('ivu-table-body');
  for(let i = 0 ;i<menuItemSelect.length;i++){
    menuItemSelect[i].classList.add('nui-scroll');
  }
}

/**
 * 数组去重
 * @type string,number,boolean,null,undefined and array,object,function
*/
export function unique(arr) {
  var res = [arr[0]];
  for (var i = 0; i < arr.length; i++) {
      var repeat = false;
      for (let j = 0; j < res.length; j++) {
          if (isObjectValueEqual(arr[i], res[j])) {
              repeat = true;
              break;
          }
      }
      if (!repeat) {
          res.push(arr[i]);
      }
  }
  return res;
}
//判断两对象是否相等
function isObjectValueEqual(a, b) {
  var aProps = Object.getOwnPropertyNames(a);
  var bProps = Object.getOwnPropertyNames(b);
  if (aProps.length != bProps.length) {
      return false;
  }
  for (var i = 0; i < aProps.length; i++) {
      var propName = aProps[i];
      if (a[propName] !== b[propName]) {
          return false;
      }
  }
  return true;
}

// //hashchange  路由跳转兼容ie
// export function hashchange() {
//   if (
//     '-ms-scroll-limit' in document.documentElement.style &&
//     '-ms-ime-align' in document.documentElement.style
//   ) { // detect it's IE11
//     window.addEventListener("hashchange", function(event) {
//       var currentPath = window.location.hash.slice(1);
//       if (store.state.route.path !== currentPath) {
//         router.push(currentPath)
//       }
//     }, false)
//   }
//   return true;
// }

