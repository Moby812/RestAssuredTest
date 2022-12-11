package api.response;

import api.request.UserTime;


public class UserTimeRes extends UserTime {
    private String updatedAt;

    public UserTimeRes() {    }

    public UserTimeRes(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
