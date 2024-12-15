export const DATE_FORMAT = "YYYY-MM-DD";
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

