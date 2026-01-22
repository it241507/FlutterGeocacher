import {CommentType} from "@/app/components/Comment";

export type CacheType = {
  id: number;
  timeStamp: string;
  title: string;
  desc: string;
  lat: number;
  lng: number;
  imageFilename: string | null;
  image: string | null;
  userID: string;
  userName: string;
  comments: CommentType[];
};