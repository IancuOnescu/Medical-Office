package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;

@Data @AllArgsConstructor
public class Patient extends Entity{
    private Doctor familyDoctor;

    @Override
    public int hashCode() {
        return Objects.hash(super.getLastName(), super.getFirstName(), super.getCnp());
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        final Patient app = (Patient) obj;
        return super.getCnp().equals(app.getCnp()) && this.getLastName().equals(app.getLastName()) && this.getFirstName().equals(app.getFirstName());
    }
}
