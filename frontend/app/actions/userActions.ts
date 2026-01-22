'use client'

import {axiosInstance} from "@/app/AxiosInterceptor/axiosInterceptor";
import {CommentType} from "@/app/components/Comment";

export async function login(email: string, password: string): Promise<string> {
  return axiosInstance.post(`/api/users/login`,
    {
      email: email,
      password: password,
  })
    .then(response => response.data)
    .then(data => data['auth-token'])
    .then((token) => setToken(token))
}

export async function registration(mail: string, name: string, pw: string): Promise<string> {
  return axiosInstance.post(`/api/users/registration`,
    {
      mail: mail,
      name: name,
      pw: pw,
    })
    .then(response => response.data)
    .then(data => data['auth-token'])
    .then((token) => setToken(token))
}

export async function setCache(lng: number, lat: number, title: string, description: string, image: string): Promise<string> {
  return axiosInstance.post(`/api/caches`,
    {
      lng: lng,
      lat: lat,
      title: title,
      desc: description,
      imageBase64: image,
    })
    .then(response => response.data)
}

export async function setComment(message: string, cacheId: number): Promise<CommentType> {

    //console.log(cacheId)

    return axiosInstance.post(`/api/comments`,
        {
            message: message,
            cacheId: cacheId
        })
        .then(response => response.data)
}

export async function loadImage(filename: string):Promise<Blob> {
  return axiosInstance.get(`/api/caches/images/${filename}`, {responseType: 'blob'})
    .then(response => {
      console.log(response.data);
      return response.data;
    })
}

function setToken(token: string) {
  document.cookie = `token=${token}; path=/`;
  return token;
}

export async function handleLogout(token: string): Promise<void> {
  try {

    return axiosInstance.post(`/api/users/logout`).then(() => {
        // document.cookie.replace(new RegExp('/*'), "");
        //document.cookie.replace(new RegExp('/*'), "");
        document.cookie = `token=${token}; expires=Thu, 07 Apr 1999 00:00:00 UTC; path=/`;
    });
  } catch (error) {
    console.error('Logout failed', error);
  }
}

export async function getCurrentUser() {
  try {
    const response = await axiosInstance.get('/api/users/current', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching current user:', error);
    throw error;
  }
}

export async function getCachesByUser(userId: string) {
  try {
    const response = await axiosInstance.get(`/api/users/${userId}/caches`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    const caches = response.data;

    for (const cache of caches) {
      if (cache.imageFilename) {
        const blob = await loadImage(cache.imageFilename);
        cache.image = URL.createObjectURL(blob);
      }
    }

    return caches;
  } catch (error) {
    console.error('Error fetching caches by user:', error);
    throw error;
  }
}