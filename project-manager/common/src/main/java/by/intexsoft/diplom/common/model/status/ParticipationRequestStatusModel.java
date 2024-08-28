package by.intexsoft.diplom.common.model.status;

import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participation_request_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestStatusModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @NotBlank
        private String statusName;

        @OneToMany(mappedBy = "status")
        private List<ParticipationRequestModel> participationRequests = new ArrayList<>();
}
