package net.zdsoft.studevelop.data.dto;

/**
 * Created by Administrator on 2018/4/20.
 */
public class StuDevelopSubjectHealthDto {
    private String id;

    private String nameOrValue;
    private String colRow;
    private int  number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameOrValue() {
        return nameOrValue;
    }

    public void setNameOrValue(String nameOrValue) {
        this.nameOrValue = nameOrValue;
    }

    public String getColRow() {
        return colRow;
    }

    public void setColRow(String colRow) {
        this.colRow = colRow;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
