package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
abstract public class Entity {
    private String firstName;
    private String lastName;
    private String cnp;
    private Integer[] phoneNumber;

    public Entity(){}
}
