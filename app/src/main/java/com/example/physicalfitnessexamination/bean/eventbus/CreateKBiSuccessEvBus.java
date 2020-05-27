package com.example.physicalfitnessexamination.bean.eventbus;

/**
 * Created by chenzhiyuan On 2020/5/27
 */
public class CreateKBiSuccessEvBus {
    //考核id
    private String kbiId;

    public CreateKBiSuccessEvBus(String kbiId) {
        this.kbiId = kbiId;
    }

    public String getKbiId() {
        return kbiId;
    }

    public void setKbiId(String kbiId) {
        this.kbiId = kbiId;
    }
}
