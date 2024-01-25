package com.example.backend.modules.history.services;


import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.dtos.CreateHistoryDTO;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.viewmodels.HistoryRoomVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface HistoryService {

    Optional<History> findByIdAndUser(int id,User user);

    ResponseSuccess<Boolean> createHistory(CreateHistoryDTO dto,User user);

    ResponseSuccess<ResponsePaging<List<HistoryRoomVm>>> getHistoryRoom(int pageIndex, User user);

    ResponseSuccess<ResponsePaging<List<HistoryRoomVm>>> getHistorySingle(int pageIndex, User user);

}
