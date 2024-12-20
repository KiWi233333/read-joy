
export enum MessageType {
  TEXT = 0,
  AI_CHAT = 1,
}

export enum WsStatusEnum {
  OPEN = 1,
  SAFE_CLOSE = 2,
  CLOSE = 3,
}
export interface ChatMessageVO {
  /**
   * 发送者信息
   */
  fromUser: {
    userId: string
    avatar?: string
    nickName: string
  }
  /**
   * 消息详情
   */
  message: {
    id: number
    sendTime: string
    content: string
    type: MessageType
  }
}
