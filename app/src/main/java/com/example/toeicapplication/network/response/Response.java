package com.example.toeicapplication.network.response;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    public Response() {
    }

    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final class PostResponse<T> extends Response {
        private T data;

        public PostResponse() {
            super();
        }

        public PostResponse(PostResponse<T> obj) {
            super(obj.isStatus(), obj.getMessage());
            this.data = obj.getData();
        }

        public T getData() {
            return this.data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    public static final class PutResponse<T> extends Response {
        private T data;

        public PutResponse() {
            super();
        }

        public PutResponse(PostResponse<T> obj) {
            super(obj.isStatus(), obj.getMessage());
            this.data = obj.getData();
        }

        public T getData() {
            return this.data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
