export interface UploadResults {
    imageKey: string;
    image: Blob;
}

export interface DownloadResults {
    image: Blob;
    uid: string;
    comment: string;
}