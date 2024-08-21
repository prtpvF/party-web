package by.intexsoft.diplom.common.model.role;

import by.intexsoft.diplom.common.model.person.PersonModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roleName;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference
    private List<PersonModel> personModels = new ArrayList<>();

    public PersonRoleModel(String roleName){
        this.roleName=roleName;
    }

    @Override
    public String toString() {
        return "PersonRole{" +
                "id=" + id +
                ", role='" + roleName + '\'' +
                '}';
    }
}
