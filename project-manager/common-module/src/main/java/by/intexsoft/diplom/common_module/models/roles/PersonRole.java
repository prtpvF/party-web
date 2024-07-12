package by.intexsoft.diplom.common_module.models.roles;

import by.intexsoft.diplom.common_module.models.Person;
import by.intexsoft.diplom.common_module.models.enums.PersonRolesEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person_role")
@Data
public class PersonRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private PersonRolesEnum role;

    @OneToMany(mappedBy = "role")
    private List<Person> person = new ArrayList<>();

    public PersonRole(PersonRolesEnum role){
        this.role = role;
    }


}
