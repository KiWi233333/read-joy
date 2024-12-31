
export interface FileItem {
  url: string
  fileName: string
  currentSize: number
  totalSize: number
  status: FileStatus
  localPath: string
  mimeType: string
  downloadTime: number
  fromUid?: string
}

export enum FileStatus {
  NOT_FOUND = 0,
  DOWNLOADING = 1,
  DOWNLOADED = 2,
  PAUSED = 3,
  ERROR = 4,
}

export const DownFileTextMap: Record<FileStatus, string> = {
  [FileStatus.DOWNLOADING]: "正在下载",
  [FileStatus.ERROR]: "下载失败",
  [FileStatus.PAUSED]: "暂停下载",
  [FileStatus.NOT_FOUND]: "文件不存在",
  [FileStatus.DOWNLOADED]: "下载完成",
};

// @unocss-include
export const DownFileStatusIconMap: Record<FileStatus, string> = {
  [FileStatus.DOWNLOADING]: "i-solar-download-minimalistic-broken",
  [FileStatus.ERROR]: "i-solar-danger-circle-outline",
  [FileStatus.PAUSED]: "i-solar-alt-arrow-right-bold",
  [FileStatus.NOT_FOUND]: "i-solar-file-corrupted-broken",
  [FileStatus.DOWNLOADED]: "i-solar-check-circle-outline",
};


/**
 * 下载文件 by streamSaver
 * https://segmentfault.com/a/1190000044342886
 * @param url 下载地址
 * @param fileName 下载后的文件名
 * @param headers 请求头
 * @param callback 下载进度回调函数
 * @returns 下载进度对象
 */
export async function downloadFile(url: string, fileName: string, headers: Record<string, string> = {}, callback?: (progress: number) => void) {
  const progress = ref(0);
  let writer: WritableStreamDefaultWriter<Uint8Array>;
  // 【步骤1】创建一个文件，该文件支持写入操作
  // @ts-expect-error
  const streamSaver = window?.streamSaver;
  if (!streamSaver) { // a标签的方法
    const a = document.createElement("a");
    a.href = url;
    a.download = fileName;
    a.click();
    return {
      progress,
    };
  }
  const fileStream = streamSaver.createWriteStream(fileName); // 这里传入的是下载后的文件名，这个名字可以自定义
  // 【步骤2】使用 fetch 方法访问文件的url，将内容一点点的放到 StreamSaver 创建的文件里
  fetch(url, {
    headers,
    // 禁止缓存
    cache: "no-cache",
  })
    .then((res) => {
      const stream = res.body;
      if (!stream)
        return;

      if (window.WritableStream && stream.pipeTo) {
        return stream.pipeTo(fileStream)
          .then(() => {
            if (typeof callback === "function")
              callback(1);
            progress.value = 1;
          });
      }
      // 【步骤3】监听文件内容是否读取完整，读取完就执行“保存并关闭文件”的操作。
      writer = fileStream.getWriter();
      const reader = stream.getReader();
      const pump: () => void = () => reader.read()
        .then(res => res.done
          ? writer.close()
          : writer.write(res.value).then(pump),
        );
      // 【步骤4】监听写入进度
      pump();
    });

  return {
    progress,
  };
}

