package by.intexsoft.diplom.common_module.models.roles;

import by.intexsoft.diplom.common_module.models.Person;
import by.intexsoft.diplom.common_module.models.enums.PersonRolesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String role;

    @OneToMany(mappedBy = "role")
    private List<Person> person = new ArrayList<>();

    public PersonRole(String role){
        this.role=role;
    }




}
