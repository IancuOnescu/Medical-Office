package model;

import lombok.Data;

import java.util.HashSet;

@Data
public class Patient extends Entity{
    private Doctor familyDoctor;
}
