package api.response;

public class FailReg {

    private String error;

    public FailReg() {    }

    public FailReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
