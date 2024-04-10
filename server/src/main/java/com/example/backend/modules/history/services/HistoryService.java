package com.example.backend.modules.history.services;


import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.dtos.CreateHistoryDTO;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.viewmodels.HistoryAnswerVm;
import com.example.backend.modules.history.viewmodels.HistoryRankVm;
import com.example.backend.modules.history.viewmodels.HistoryRoomVm;
import com.example.backend.modules.history.viewmodels.HistorySingleVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface HistoryService {

    Optional<History> findByIdAndUser(int id,User user);

    ResponseSuccess<Boolean> createHistory(CreateHistoryDTO dto,User user);

    ResponseSuccess<ResponsePaging<List<HistoryRankVm>>> getHistoryRankSingle(int quizId, int pageIndex);

    ResponseSuccess<ResponsePaging<List<HistoryRankVm>>> getHistoryRankRoom(int roomid, int pageIndex);

    ResponseSuccess<List<HistoryAnswerVm>> getListHistoryAnswer(User user,int roomId);

    // cần thêm 1 hàm get history cho host room xem lại history của các room, trả về list user và kết quả tham gia room.

}
