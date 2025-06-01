import * as request from '../utils/request.js';

export const getMessages = async (conversationId) => {
    return request.get(`/message/getMessages/${conversationId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const getLastMessageIdByConversationId = async (conversationId, senderId) => {
    return request.get(`/message/lastMessageId/${conversationId}/${senderId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const uploadMessageFile = (files) => {
    return request.post('/message/upload/file', files, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}