package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.repository.request.ParticipationRequestRepository;
import by.intexsoft.diplom.person.exception.RequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {

        private final ParticipationRequestRepository requestRepository;

        /**
         * method find a participation request by id in DB
         * or throw an exception if nothing found
         * @param requestId - identifier of participate request
         * @return founded participate request
         */
        public ParticipationRequestModel findRequestById(int requestId) {
            return requestRepository.findById(requestId)
                    .orElseThrow(() -> new RequestNotFoundException
                            ("participate request with this id doesnt exist"));
        }
}
