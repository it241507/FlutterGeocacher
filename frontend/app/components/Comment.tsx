export type CommentType = {
    id: number;
    timeStamp: string;
    message: string;
    user: {
        id: number;
        mail: string;
        name: string;
        pwhash: string;
    }
    cacheId: number;
};