package com.example.backend.modules.history.services;

import com.example.backend.modules.history.dtos.CreateHistoryAnswerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryAnswerService {
    boolean createBulkHistoryAnswer(List<CreateHistoryAnswerDTO> listDto);

    // có thể viết thêm 1 hàm get historyanswer cho 1 history nhằm tăng performance.
}
