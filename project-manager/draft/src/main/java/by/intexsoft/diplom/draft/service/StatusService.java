package by.intexsoft.diplom.draft.service;

import by.intexsoft.diplom.common.model.status.PartyDraftStatusModel;
import by.intexsoft.diplom.draft.exception.DraftStatusNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusService {

        private final PartyDraftStatusRepository draftStatusRepository;

        public PartyDraftStatusModel findById(Integer statusId) {
            return draftStatusRepository.findById(statusId)
                    .orElseThrow(() -> new DraftStatusNotFoundException(
                            "Cannot find draft status"));
        }
}
