package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
abstract public class Document {
    private Date date;
    private Doctor doctor;

    public Document(){}

    public abstract String getArgs();
}
