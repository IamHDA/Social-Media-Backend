import * as request from "../utils/request.js";

export const signUp = async (email, password, userName) => {
    return await request.post("authenticate/register", {
        email,
        password,
        userName,
    });
};

export const login = async (email, password) => {
    return await request.post('authenticate/login', {email, password}, {});
}

export const refreshToken = async () => {
    const refreshToken = await localStorage.getItem("refreshToken");
        return await request.post(
        "authenticate/refreshToken", {}, {
            headers: {
                Authorization: "Bearer " + refreshToken,
            },
        }
    );
};

export const logout = async () => {
    return await request.post("/logout", {}, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem("accessToken"),
            }
        }
    );
};
