import * as request from '../utils/request';

export const getPostComment = async (postID) => {
    return await request.get("/postComment/post/" + postID, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const getResponse = async (commentId) => {
    return await request.get("/postComment/getResponse/" + commentId, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const createPostComment = async (postId, formData) => {
    return await request.post("/postComment/create/post/" + postId, formData, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const createReplyComment = async (commentId, formData) => {
    return await request.post("/postComment/createResponse/" + commentId, formData, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}