import axios from "axios";
import { API_BASE_URL } from "./elements"

export const instance = axios.create({
  baseURL: API_BASE_URL
});