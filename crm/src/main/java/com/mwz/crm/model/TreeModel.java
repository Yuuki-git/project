package com.mwz.crm.model;

/**
 * @author: yuuki
 * @Date: 2021-06-08 - 06 - 16:42
 * @Description: com.mwz.crm.model
 * @version: 1.0
 */
public class TreeModel {
    private Integer id;
    private Integer pId;
    private String name;
    private boolean checked= false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
