import axios from "axios";
import {baseUrl} from "@/app/env";
import {Configuration} from "@/api";


export const axiosInstance = axios.create({
  baseURL: baseUrl,
})

axiosInstance.interceptors.request.use(config => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = "Bearer " + token;
  }
  return config;
})

function getToken(): string | null {
  return document.cookie.replace("token=", "");
}