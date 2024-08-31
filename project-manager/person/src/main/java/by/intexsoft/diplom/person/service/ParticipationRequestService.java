package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.model.status.ParticipationRequestStatusModel;
import by.intexsoft.diplom.common.repository.request.ParticipationRequestRepository;
import by.intexsoft.diplom.common.repository.status.ParticipationRequestStatusRepository;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.exception.EmptyPageException;
import by.intexsoft.diplom.person.exception.RequestNotFoundException;
import by.intexsoft.diplom.person.exception.StatusNotFoundException;
import by.intexsoft.diplom.person.exception.UnavailablePageNumberException;
import by.intexsoft.diplom.person.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {

        private final ParticipationRequestRepository requestRepository;
        private final ParticipationRequestStatusRepository statusRepository;
        private final ObjectMapper objectMapper;

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

        public void setStatusToRequestAndSave(ParticipationRequestModel requestModel,
                                              String statusName) {
            requestModel.setStatus(findByStatus(
                    statusName
            ));
           saveRequest(requestModel);
        }

        public ParticipationRequestStatusModel findByStatus(String statusName) {
            return statusRepository.findByStatusName(statusName)
                    .orElseThrow(() -> new StatusNotFoundException("cannot find this status"));
        }

        public Page<ParticipationRequestDto> findByPartyAndStatus(Integer partyId,
                                                                  Pageable pageable) {
            Page<ParticipationRequestModel> page =  requestRepository.findAllInProcessByPartyAnd(partyId, pageable);
            validatePageNumber(page, pageable.getPageNumber());
            isPageEmpty(page);
            return page.map(request -> objectMapper.convertRequestToDto(request));
        }

        private void saveRequest(ParticipationRequestModel requestModel) {
            requestRepository.save(requestModel);
        }

        private void validatePageNumber(Page<?> page, int pageNumber) {
            if (page.getTotalPages() <= pageNumber) {
                throw new UnavailablePageNumberException("Page doesn't exist");
            }
        }

        private void isPageEmpty(Page page) {
            if (page.isEmpty()) {
                throw new EmptyPageException("No parties found");

            }
        }
}
