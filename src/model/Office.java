package model;

import lombok.Data;

import java.util.HashSet;

@Data
public class Office {
    private String name;
    private HashSet<Doctor> doctors = new HashSet<Doctor>();
}
