package com.example.backend.modules.room.controllers;

import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.room.dtos.CreateRoomDTO;
import com.example.backend.modules.room.dtos.EditRoomDTO;
import com.example.backend.modules.room.services.GameModeService;
import com.example.backend.modules.room.services.RoomService;
import com.example.backend.modules.room.viewmodels.GameModeVm;
import com.example.backend.modules.room.viewmodels.JoinRoomVm;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/rooms") //http://localhost:8080/api/rooms
public class RoomController {
    private final RoomService roomService;
    private final GameModeService gameModeService;

    public RoomController(
            RoomService roomService,
            GameModeService gameModeService
    ){
        this.roomService = roomService;
        this.gameModeService = gameModeService;
    }

    @GetMapping("/{roomPin}/join")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<JoinRoomVm>> joinRoom(
            @PathVariable("roomPin") String roomPin,
            @AuthenticationPrincipal User user
    ) {
        var result = roomService.joinRoom(user,roomPin);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/mode")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<GameModeVm>>> getListGameMode(
    ) {
        var result = gameModeService.getListGameMode();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{roomPin}/participant")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<RoomVm>> getRoomForParticipants(
            @PathVariable("roomPin") String roomPin,
            @AuthenticationPrincipal User user
    ) {
        var result = roomService.getRoomForParticipants(user,roomPin);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<RoomVm>> createRoom(
            @RequestBody @Valid CreateRoomDTO dto,
            @AuthenticationPrincipal User user
            ) {
        var result = roomService.createRoom(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{roomId}/end")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> endRoom(
            @PathVariable("roomId") int roomId,
            @AuthenticationPrincipal User user
    ) {
        var result = roomService.endRoom(user,roomId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> editRoom(
            @RequestBody @Valid EditRoomDTO dto,
            @AuthenticationPrincipal User user
    ) {
        var result = roomService.editRoom(user,dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> deleleRoom(
            @PathVariable("roomId") int roomId,
            @AuthenticationPrincipal User user
    ) {
        var result = roomService.deleteRoom(user,roomId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<RoomVm>>>> getMyListRooms(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "pageIndex", required = true, defaultValue = "0") Integer pageIndex,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BY_CREATED_AT) String sortBy,
            @AuthenticationPrincipal User user
    ) {
        var result = roomService.getMyListRooms(keyword,sortBy,pageIndex,user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<RoomVm>> getRoomDetail(
            @PathVariable("roomId") int roomId
    ) {
        var result = roomService.getRoomDetail(roomId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/joined")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<RoomVm>>>> getJoinedRoom(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "pageIndex", required = true, defaultValue = "0") Integer pageIndex
    ){
        var result = roomService.getJoinedRoom(user,pageIndex);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}/kick/{userId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> kickUser(
            @PathVariable("roomId") int roomId,
            @PathVariable("userId") int userId,
            @AuthenticationPrincipal User user
    ){
        var result = roomService.kickUser(user,roomId,userId);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/{roomId}/leave")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> leaveRoom(
            @PathVariable("roomId") int roomId,
            @AuthenticationPrincipal User user
    ){
        var result = roomService.leaveRoom(user,roomId);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
