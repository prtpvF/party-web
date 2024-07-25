package by.intexsoft.diplom.common.model.role;

import by.intexsoft.diplom.common.model.PersonModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<PersonModel> personModels = new ArrayList<>();

    public PersonRole(String roleName){
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
