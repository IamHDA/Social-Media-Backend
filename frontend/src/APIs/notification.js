import * as request from '../utils/request.js';

export const getNotifications = async (pageSize, pageNumber) => {
    return request.get('/notification/getList', {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
            pageSize, pageNumber
        }
    })
}