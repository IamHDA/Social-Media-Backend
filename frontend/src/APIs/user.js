import * as request from "../utils/request.js";

export const getCurrentUser = async () => {
    return await request.get("/user/currentUser", {
        headers: {
          Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    });
};

export const getProfile = async (userId) => {
    return await request.get(`/user/profile/${userId}`, {
        headers: {
          Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    });
}

export const getAllImages = async (userId, pageNumber, pageSize, sort) => {
    return await request.get(`/user/image/${userId}`, {
        headers: {
          Authorization: `Bearer ` + localStorage.getItem("accessToken")
        },
        params: {
            pageNumber, pageSize, sort
        }
    });
}

export const updateAvatar = async (avatar) => {
    return await request.put("/user/profile/update/avatar", avatar, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const updateBackgroundImage = async (backgroundImage) => {
    return await request.put("/user/profile/update/backgroundImage", backgroundImage, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const editBio = async (bio) => {
    return await request.put("/user/profile/update/bio", {bio}, {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        }
    })
}

export const searchUser = async (keyword) => {
    return await request.get("/user/search", {
        headers: {
            Authorization: `Bearer ` + localStorage.getItem("accessToken")
        },
        params: {
            keyword
        }
    })
}