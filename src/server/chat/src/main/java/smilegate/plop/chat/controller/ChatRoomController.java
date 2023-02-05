package smilegate.plop.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.chat.dto.APIMessage;
import smilegate.plop.chat.dto.request.ReqDmDto;
import smilegate.plop.chat.dto.request.ReqGroupDto;
import smilegate.plop.chat.dto.request.ReqInviteDto;
import smilegate.plop.chat.dto.response.RespRoomDto;
import smilegate.plop.chat.service.ChatRoomService;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/chatting/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomMongoService;

    @PostMapping("/v1/dm-creation")
    public ResponseEntity<RespRoomDto> dmCreation(@RequestHeader("Authorization") String jwt, @RequestBody ReqDmDto reqDmDto){
        String userId = "test";
        reqDmDto.setCreator(userId);
        return new ResponseEntity<>(chatRoomMongoService.createDmRoom(reqDmDto),HttpStatus.CREATED);
    }

    @PostMapping("/v1/group-creation")
    public ResponseEntity<RespRoomDto> groupCreation(@RequestHeader("Authorization") String jwt, @RequestBody ReqGroupDto reqGroupDto){
        String userId = "test";
        reqGroupDto.setCreator(userId);
        return new ResponseEntity<>(chatRoomMongoService.createGroup(reqGroupDto),HttpStatus.CREATED);
    }

    @PostMapping("/v1/invitation")
    public ResponseEntity<APIMessage> groupInvitation(@RequestBody ReqInviteDto reqInviteDto){
        if(chatRoomMongoService.inviteMembers(reqInviteDto)){
            return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.success,reqInviteDto),HttpStatus.OK);
        }
        return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.failed,reqInviteDto),HttpStatus.OK);
    }

    // 자신 채팅방 리스트 조회 ~ing
    @GetMapping("/v1/my-rooms")
    public ResponseEntity<APIMessage> myChatRooms(@RequestHeader("Authorization") String jwt){
        //jwt를 auth 서버를 통해 사용자 id 가져온다.
        String userId="member id 1";
        return new ResponseEntity<>(chatRoomMongoService.findMyRoomsByUserId(userId),HttpStatus.OK);
    }

    // 채팅방 나가기
    @DeleteMapping("/v1/out/{roomid}")
    public ResponseEntity<APIMessage> outOfTheRoom(@RequestHeader("Authorization") String jwt, @PathVariable(value = "roomid") String roomId){
        String userId="member id 1";
        if(chatRoomMongoService.outOfTheRoom(roomId, userId)){
            return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.success, new HashMap<String,String>() {{
                put("room_id",roomId);
            }}), HttpStatus.OK);
        }
        return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.failed, new HashMap<String,String>() {{
            put("room_id",roomId);
        }}), HttpStatus.OK);
    }

    @GetMapping("/v1/info/{roomid}")
    public ResponseEntity<RespRoomDto> chatRoomInfo(@PathVariable(value = "roomid") String roomId){
        return new ResponseEntity<>(chatRoomMongoService.getChatRoomInfo(roomId),HttpStatus.OK);
    }
}
