import * as request from '../utils/request.js';

export const getPostEmotions = async (postId) => {
    return await request.get('/reaction/emotions/post/' + postId, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const sendReaction = async (reactionRequest) => {
    return await request.post('/reaction/send', reactionRequest, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const changeReaction = async (reactionId, emotion) => {
    return await request.put('/reaction/change/' + reactionId, {}, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        },
        params: {
            emotion
        }
    })
}

export const deleteReaction = async (reactionId) => {
    return await request.remove('/reaction/delete/' + reactionId, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}