package by.intexsoft.diplom.common.model.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageTypeModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;

        @OneToMany(mappedBy = "type", cascade = CascadeType.REFRESH)
        private List<MessageModel> messages = new ArrayList();
}
