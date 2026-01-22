'use client'
import {baseUrl} from "@/app/env";
import {axiosInstance} from "@/app/AxiosInterceptor/axiosInterceptor";

export async function getHelloWorld(): Promise<string> {
  return await axiosInstance.get(`/api/helloworld/hipublic`)
    .then(response => response.data)
}

export async function getPrivateHelloWorld(): Promise<string> {
  const token = localStorage.getItem('token');
  return await axiosInstance.get(`/api/helloworld/hiprivate`)
    .then(response => response.data)
}