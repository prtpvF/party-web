package by.intexsoft.diplom.common.model.role;

import by.intexsoft.diplom.common.model.person.PersonModel;
<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
=======
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
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
