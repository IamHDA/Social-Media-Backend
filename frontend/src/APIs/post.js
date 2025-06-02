import * as request from "../utils/request";

export const createNewPost = async (formData) => {
    return await request.post("/post/create", formData, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        }
    });
};

export const sharePost = async (postId, data) => {
    return await request.post(`/post/share/${postId}`, data, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        }
    })
}

export const getNewestPost = async (pageNumber) => {
    return await request.get("/post/newest", {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        params: {
            pageNumber
        }
    })
}

export const getPostByUserId = async (userId, pageNumber) => {
    return await request.get(`/post/user/${userId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
        params: {
            pageNumber
        }
    })
}