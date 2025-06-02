import * as request from '../utils/request.js';

export const findFriends = async (userId, keyword, pageNumber, pageSize) => {
    return request.get('/friend/getList/' + userId, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
            keyword, pageNumber, pageSize
        }}
    )
}

export const checkFriendRequest = async (opponentId) => {
    return request.get('/friendRequest/check/' + opponentId, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const checkFriend = async (opponentId) => {
    return request.get('/friend/checkFriendship/' + opponentId, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const getFriendRequests = async (pageNumber, pageSize) => {
    return request.get('/friendRequest/getList', {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
            pageNumber, pageSize
        }
    })
}

export const acceptFriendRequest = async (senderId) => {
    return request.post('/friend/acceptRequest/' + senderId, {}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const declineFriendRequest = async (senderId, recipientId) => {
    return request.remove('/friendRequest/delete/' + senderId + '/' + recipientId, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`
            }
        }
    )
}

export const sendFriendRequest = async (opponentId) => {
    return request.post('/friendRequest/send/' + opponentId, {}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}

export const deleteFriend = async (opponentId) => {
    return request.remove('/friend/delete/' + opponentId, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    })
}
