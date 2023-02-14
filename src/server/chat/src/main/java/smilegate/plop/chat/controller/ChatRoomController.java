package smilegate.plop.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.chat.config.kafka.Producers;
import smilegate.plop.chat.dto.APIMessage;
import smilegate.plop.chat.dto.request.ReqDmDto;
import smilegate.plop.chat.dto.request.ReqGroupDto;
import smilegate.plop.chat.dto.request.ReqInviteDto;
import smilegate.plop.chat.dto.response.RespMyChatRoom;
import smilegate.plop.chat.dto.response.RespRoomDto;
import smilegate.plop.chat.exception.CustomAPIException;
import smilegate.plop.chat.exception.ErrorCode;
import smilegate.plop.chat.exception.ErrorResponseDto;
import smilegate.plop.chat.model.jwt.JwtTokenProvider;
import smilegate.plop.chat.service.ChatRoomService;

import java.util.HashMap;
import java.util.List;

@Tag(name="room", description = "채팅방 API")
@Slf4j
@RestController
@RequestMapping("/chatting/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomMongoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Producers producers;

    private String getTokenToUserId(String jwt){
        return jwtTokenProvider.getUserInfo(jwtTokenProvider.removeBearer(jwt)).getUserId();
    }

    @Operation(summary = "1:1 채팅방 생성", description = "채팅을 보내지 않아도 생성됨, 상대방과 채팅방 개설한 적이 있다면 이전에 생성된 채팅방 반환", responses = {
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = RespRoomDto.class))),
            @ApiResponse(responseCode = "404", description = "상대방id가 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
    @PostMapping("/v1/dm-creation")
    public ResponseEntity<RespRoomDto> dmCreation(@RequestHeader("Authorization") String jwt, @RequestBody ReqDmDto reqDmDto){
        if(reqDmDto.getMessage_to() == null || reqDmDto.getMessage_to().equals("")) {
            log.error("dmCreation, Message={}, ErrorCode: {},","채팅방이 없거나 멤버가 없음",ErrorCode.DM_MEMBER_ERROR);
            throw new CustomAPIException(ErrorCode.DM_MEMBER_ERROR, "상대방id가 없음");
        }

        String userId = getTokenToUserId(jwt);
        reqDmDto.setCreator(userId);
        return new ResponseEntity<>(chatRoomMongoService.createDmRoom(reqDmDto),HttpStatus.CREATED);
    }

    @Operation(summary = "그룹 채팅방 생성", description = "그룹 채팅방 생성시 웹소켓으로 생성 정보 멤버들에게 전달", responses = {
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = RespRoomDto.class))),
            @ApiResponse(responseCode = "400", description = "초대한 상대방id가 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
    @PostMapping("/v1/group-creation")
    public ResponseEntity<RespRoomDto> groupCreation(@RequestHeader("Authorization") String jwt, @RequestBody ReqGroupDto reqGroupDto){
        String userId = getTokenToUserId(jwt);
        reqGroupDto.setCreator(userId);

        RespRoomDto respRoomDto = chatRoomMongoService.createGroup(reqGroupDto);
        producers.sendRoomMessage(respRoomDto);
        return new ResponseEntity<>(respRoomDto,HttpStatus.CREATED);
    }

    @Operation(summary = "그룹 채팅방에 멤버 초대", responses = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ReqInviteDto.class))),
            @ApiResponse(responseCode = "400", description = "채팅방이 없거나 멤버가 없음", content = @Content(schema = @Schema(implementation = ReqInviteDto.class))) })
    @PostMapping("/v1/invitation")
    public ResponseEntity<APIMessage> groupInvitation(@RequestBody ReqInviteDto reqInviteDto){
        if(chatRoomMongoService.inviteMembers(reqInviteDto)){
            log.error("groupInvitation, Message={}","채팅방이 없거나 멤버가 없음");
            return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.success,reqInviteDto),HttpStatus.OK);
        }
        return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.failed,reqInviteDto),HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "채팅방 리스트 조회", description = "나의 채팅방 리스트 조회", responses = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = APIMessage.class)))})
    @GetMapping("/v1/my-rooms")
    public ResponseEntity<APIMessage> myChatRooms(@RequestHeader("Authorization") String jwt){
        String userId = getTokenToUserId(jwt);
        List<RespMyChatRoom> respMyChatRooms = chatRoomMongoService.findMyRoomsByUserId(userId);
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(APIMessage.ResultEnum.success);
        apiMessage.setData(respMyChatRooms);
        return new ResponseEntity<>(apiMessage,HttpStatus.OK);
    }


    @Operation(summary = "채팅방 나가기", description = "그룹 채팅방에서 멤버 삭제", responses = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = APIMessage.class)))})
    @DeleteMapping("/v1/out/{roomid}")
    public ResponseEntity<APIMessage> outOfTheRoom(@RequestHeader("Authorization") String jwt,
                                                   @Parameter(description = "채팅방 id") @PathVariable(value = "roomid") String roomId){
        String userId = getTokenToUserId(jwt);
        if(chatRoomMongoService.outOfTheRoom(roomId, userId)){
            return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.success, new HashMap<String,String>() {{
                put("room_id",roomId);
            }}), HttpStatus.OK);
        }
        return new ResponseEntity<>(new APIMessage(APIMessage.ResultEnum.failed, new HashMap<String,String>() {{
            log.error("outOfTheRoom, 채팅방 나가기 실패 roomid: {}, userid: {}", roomId, userId);
            put("room_id",roomId);
        }}), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "채팅방 정보", description = "하나의 채팅방 정보를 조회", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = RespRoomDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않은 채팅방",content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/v1/info/{roomid}")
    public ResponseEntity<RespRoomDto> chatRoomInfo(@PathVariable(value = "roomid") String roomId){
        return new ResponseEntity<>(chatRoomMongoService.getChatRoomInfo(roomId), HttpStatus.OK);
    }
}
