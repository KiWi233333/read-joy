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
