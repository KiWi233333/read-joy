import type { AdminResourceVO } from "../api/admin/book";
import type { ResourceVO } from "../api/book";
import { BaseUrlFile } from "./useBaseUrl";
import { downloadFile } from "./useFile";

export const DATE_FORMAT = "YYYY-MM-DD";
export const DATE_TIME_FORMAT = "YYYY-MM-DD HH:mm:ss";
export const DATE_SELECTOR_OPTIONS: DatePreloadItem[] = [{
  text: "一周之内",
  value: () => {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
    return [start, end];
  },
}, {
  text: "一个月内",
  value: () => {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
    return [start, end];
  },
}, {
  text: "三个月内",
  value: () => {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
    return [start, end];
  },
}, {
  text: "六个月内",
  value: () => {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 180);
    return [start, end];
  },
}, {
  text: "一年内",
  value: () => {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 365);
    return [start, end];
  },
}];
Object.freeze(DATE_SELECTOR_OPTIONS);

export interface DatePreloadItem {
  readonly text: string;
  readonly value: () => [Date, Date];
}


export const IMG_MAX_SIZE = 5 * 1024 * 1024;// 5MB
export const FILE_MAX_SIZE = 50 * 1024 * 1024;// 50MB
export const FILE_TYPE_ICON_MAP = {
  "text/plain": "/images/icon/file/TXT.png",

  "application/vnd.ms-excel": "/images/icon/file/XLS.png",
  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": "/images/icon/file/XLSX.png",

  "application/vnd.ms-powerpoint": "/images/icon/file/PPT.png",
  "application/vnd.openxmlformats-officedocument.presentationml.presentation": "/images/icon/file/PPTX.png",

  "application/msword": "/images/icon/file/DOC.png",
  "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "/images/icon/file/DOCX.png",

  "application/pdf": "/images/icon/file/PDF.png",
  "application/x-pdf": "/images/icon/file/PDF.png",
  "application/x-bzpdf": "/images/icon/file/PDF.png",
  "application/x-gzpdf": "/images/icon/file/PDF.png",

  "image/jpeg": "/images/icon/file/IMAGE.png",
  "image/png": "/images/icon/file/IMAGE.png",
  "image/gif": "/images/icon/file/IMAGE.png",
  "image/svg+xml": "/images/icon/file/IMAGE.png",
  "application/zip": "/images/icon/file/ZIP.png",

  "application/octet-stream": "/images/icon/file/OTHER.png",
} as Record<string, string>;
export const FILE_UPLOAD_ACCEPT = Object.keys(FILE_TYPE_ICON_MAP).join(",");
/**
 * 默认文件图标
 */
export const FILE_TYPE_ICON_DEFAULT = "/images/icon/file/DEFAULT.png";

export function downloadResource(resource: ResourceVO | AdminResourceVO, token: string, callback?: (status: "403" | "200", message: string) => void) {
  if (!token) {
    ElMessage.error("没有权限，请先登录！");
    callback && callback("403", "没有权限，请先登录！");
    return;
  }
  ElMessage.warning("正在下载中，请稍后查看");
  downloadFile(BaseUrlFile + resource.url, resource.title, {
    Authorization: token,
  }, () => {
    ElMessage.closeAll("warning");
    ElMessage.success("下载完成！");
  });
}

/**
 * 格式化文件大小
 * @param size 字节大小
 * @returns 格式化后的文件大小字符串
 * @example
 * formatFileSize(1024) // "1KB"
 * formatFileSize(1024 * 1024) // "1MB"
 * formatFileSize(1024 * 1024 * 1024) // "1GB"
 */
export function formatFileSize(size: number): string {
  if (size < 1024)
    return `${size} B`;
  else if (size < 1024 * 1024)
    return `${(size / 1024).toFixed(2)} KB`;
  else if (size < 1024 * 1024 * 1024)
    return `${(size / (1024 * 1024)).toFixed(2)} MB`;
  else
    return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
}

// 格式化时间
export function formatDate(date: Date) {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const day = date.getDate().toString().padStart(2, "0");
  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");
  // const seconds = date.getSeconds().toString().padStart(2, '0');
  const isSameDay = date.toDateString() === new Date().toDateString();
  if (isSameDay)
    return `${hours}:${minutes}`;
  else
    return `${year}年${month}月${day}日 ${hours}:${minutes}`;
}


/**
 * 复制文字
 * @param text 复制的文本
 */
export function useCopyText(text: string, showToast?: boolean) {
  // 动态创建 textarea 标签
  const textarea: HTMLTextAreaElement = document.createElement("textarea");
  // 将该 textarea 设为 readonly 防止 iOS 下自动唤起键盘，同时将 textarea 移出可视区域
  textarea.readOnly = true;
  textarea.style.position = "absolute";
  textarea.style.opacity = "0";
  textarea.style.left = "-500px";
  // 将要 copy 的值赋给 textarea 标签的 value 属性
  textarea.value = text;
  // 将 textarea 插入到 body 中
  document.body.appendChild(textarea);
  // 选中值并复制
  textarea.select();
  const result = document.execCommand("Copy");
  document.body.removeChild(textarea);
  if (showToast)
    ElMessage.success("复制成功！");
  return result;
}


/**
 * 比对对象生成(表层级)
 * @param oldObj
 * @param newObj
 * @returns 差异de
 */
export function compareObjects<T extends object>(oldObj: T, newObj: T) {
  const updatedObj = {};
  // 遍历newObj的属性
  for (const key in newObj) {
    // 检查newObj是否有该属性，并且其值与oldObj中的值不相等
    if (Object.prototype.hasOwnProperty.call(newObj, key) && newObj[key] !== oldObj[key])
      // @ts-expect-error
      updatedObj[key] = newObj[key]; // 将更改的参数添加到updatedObj中
  }
  return updatedObj as T;
}

/**
 * 格式化手机号码
 * @param phone
 * @returns 格式化后的手机号码
 * @example
 * markPhone("13812345678") // "138****5678"
 */
export function markPhone(phone: string) {
  return phone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
}

/**
 * 深度比较对象
 * @param oldObj
 * @param newObj
 * @returns 差异de
 */
export function deepCompareObj<T extends object>(oldObj: T, newObj: T): Partial<T> {
  const updatedObj: Partial<T> = {};

  for (const key in newObj) {
    if (Object.prototype.hasOwnProperty.call(newObj, key)) {
      const oldValue = oldObj[key];
      const newValue = newObj[key];

      if (typeof oldValue === "object" && typeof newValue === "object") {
        // @ts-expect-error
        const nestedUpdates = compareObjects(oldValue, newValue);
        if (Object.keys(nestedUpdates).length > 0)
          // @ts-expect-error
          updatedObj[key] = nestedUpdates;
      }
      else if (oldValue !== newValue) {
        updatedObj[key] = newValue;
      }
    }
  }

  return updatedObj;
}


/**
 * 生成随机 ISBN 号
 * @returns 随机 ISBN 号
 */
export function randomISBN() {
  // ISBN 前缀
  const prefix = "978";
  // 地区码，这里随机选择 1 到 99 之间的数字
  const region = Math.floor(Math.random() * 99) + 1;
  // 出版社码，随机生成 1 到 999 之间的数字
  const publisher = Math.floor(Math.random() * 999) + 1;
  // 标题码，随机生成 1 到 9999 之间的数字
  const title = Math.floor(Math.random() * 9999) + 1;
  // 校验码计算
  const checkDigit = calculateCheckDigit(prefix + region + publisher + title);

  // 组合成完整的 ISBN 并添加连接符
  return `${prefix}-${region}-${publisher}-${title}-${checkDigit}`;
}

// 校验码计算函数
function calculateCheckDigit(isbn: string) {
  let sum = 0;
  for (let i = 0; i < 12; i++) {
    sum += (10 - i) * Number.parseInt(isbn.charAt(i), 10);
  }
  const checkDigit = (10 - (sum % 10)) % 10;
  return checkDigit ? checkDigit.toString() : "X";
}
