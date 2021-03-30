package model;

import lombok.Data;

@Data
public class Prescription extends Document{
    private String[] meds;
    private Patient patient;
}
