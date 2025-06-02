import axios from "axios";
import * as authService from "../APIs/authentication.js";

const request = axios.create({
  baseURL: "http://100.114.40.116:8081",
  timeout: 10000,
});

let isRefreshing = false;
let failedRequestsQueue = [];

const refreshToken = async () => {
  try {
    const response = await authService.refreshToken();
    const newAccessToken = response.accessToken;
    localStorage.setItem("accessToken", newAccessToken);
    return newAccessToken;
  } catch (e) {
    console.error("Error refreshing token:", e);
    return null;
  }
};

request.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const response = error.response.data;
    if (response === "AccessToken expired!") {
      if (isRefreshing) {
        return new Promise((resolve) => {
          failedRequestsQueue.push((newToken) => {
            originalRequest.headers["Authorization"] = `Bearer ${newToken}`;
            resolve(request(originalRequest));
          });
        });
      }
      isRefreshing = true;
      const newAccessToken = await refreshToken();
      isRefreshing = false;
      if (newAccessToken) {
        originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
        const res = await request(originalRequest);
        failedRequestsQueue.forEach((callback) => callback(newAccessToken));
        failedRequestsQueue = [];
        return res;
      }
    }else if(response === "AccessToken unauthorized!"){
      localStorage.clear();
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export const get = async (api, config = {}) => {
  try {
    const response = await request.get(api, config);
    return response.data;
  }catch (e) {
    console.error("Error get:", e);
  }
};

export const post = async (api, options = {}, config = {}) => {
  try {
    const response = await request.post(api, options, config);
    return response.data;
  }catch (e){
    console.error("Error post:", e);
  }
};

export const put = async (api, options = {}, config = {}) => {
  try {
    const response = await request.put(api, options, config);
    return response.data;
  }catch (e){
    console.error("Error put:", e);
  }
};

export const remove = async (api, config = {}) => {
  try {
    const response = await request.delete(api, config);
    return response.data;
  }catch (e){
    console.error("Error delete:", e);
  }
};
