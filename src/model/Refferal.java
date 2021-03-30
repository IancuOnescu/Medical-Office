package model;

import lombok.Data;

import java.util.Date;

@Data
public class Refferal extends Document {
    private Office office;
    private Date appointmentDate;
    private Doctor doctor;
}
