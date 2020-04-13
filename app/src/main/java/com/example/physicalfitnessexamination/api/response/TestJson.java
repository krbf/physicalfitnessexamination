package com.example.physicalfitnessexamination.api.response;

/**
 * Created by chenzhiyuan On 2020/4/12
 */
public class TestJson {
    private boolean success;
    private String msg;
    private String Msg;
    private TestProjectRes obj;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    public TestProjectRes getObj() {
        return obj;
    }

    public void setObj(TestProjectRes obj) {
        this.obj = obj;
    }
}
