import * as request from '../utils/request.js'

export const getAll = async () => {
    return request.get('/conversation/getAll', {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const getConversationIdByRecipientId = async (recipientId) => {
    return request.get('/conversation/getPrivateConversationId/'+ recipientId, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const getNotReads = async () => {
    return request.get('/conversation/getNotRead', {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const getById = async (id) => {
    return request.get(`/conversation/${id}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const searchConversation = async (keyword) => {
    return request.get('/conversation/search', {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
            keyword
        }
    })
}

export const getConversationFiles = async (conversationId, types, pageNumber) => {
    return request.get(`/conversation/getFiles/${conversationId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
            types: types.join(','),
            pageNumber
        }
    })
}

export const create = async (formData) => {
    return request.post('/conversation/create', formData, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const updateLastMessageStatus = async (conversationId, userId) => {
    return request.put(`/conversation/lastMessage/updateStatus/${conversationId}/${userId}`, {}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const updateName = async (conversationId, name) => {
    return request.put(`/conversation/updateName/${conversationId}`, {name}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const changeAvatar = async (conversationId, avatar) => {
    return request.put(`/conversation/changeAvatar/${conversationId}`, avatar, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}