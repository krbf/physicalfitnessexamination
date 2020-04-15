package com.example.physicalfitnessexamination.bean;

import com.example.physicalfitnessexamination.api.response.AssessmentGroupRes;

/**
 * Created by chenzhiyuan On 2020/4/13
 */
public class KbiOrgGroupTypeBean extends KbiOrgGroupBean {
    /**
     * 组别类型
     */
    private AssessmentGroupRes groupType;

    public AssessmentGroupRes getGroupType() {
        return groupType;
    }

    public void setGroupType(AssessmentGroupRes groupType) {
        this.groupType = groupType;
    }
}
