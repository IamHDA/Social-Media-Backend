import * as request from '../utils/request';

export const getParticipants = (conversationId) => {
    return request.get(`/participant/getByConversation/${conversationId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const changeRole = (conversationId, participantId, role) => {
    return request.put(`/participant/changeRole/${conversationId}/${participantId}`, {role}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const changeNickname = (conversationId, participantId, nickname) => {
    return request.put(`/participant/changeNickname/${conversationId}/${participantId}`, {nickname}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const addParticipant = (conversationId, participantId) => {
    return request.post(`/participant/add/${conversationId}`, {participantId}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const kickParticipant = (conversationId, participantId) => {
    return request.remove(`/participant/delete/${conversationId}/${participantId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}