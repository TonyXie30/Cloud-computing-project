import axios from 'axios';
import { storage } from "@/utils/storage";

// Add a request interceptor to axios
axios.interceptors.request.use(function (config) {
  // Get the token from local storage
  const token = storage.get('user')?.token;

  // If the token exists, add it to the headers
  if (token) {
    config.headers.Authorization = 'Bearer ' + token;
  }

  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});

// Export the configured axios instance
export default axios;
