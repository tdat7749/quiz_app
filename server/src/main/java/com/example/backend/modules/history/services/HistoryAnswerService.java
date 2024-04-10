package com.example.backend.modules.history.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.dtos.CreateHistoryAnswerDTO;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.viewmodels.HistoryAnswerVm;
import com.example.backend.modules.user.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryAnswerService {
    boolean createBulkHistoryAnswer(List<CreateHistoryAnswerDTO> listDto, History history);

    // có thể viết thêm 1 hàm get historyanswer cho 1 history nhằm tăng performance.

    void deleteByHistoryId(int historyId);

}
