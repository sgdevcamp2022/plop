package smilegate.plop.auth.service;


import org.modelmapper.convention.MatchingStrategies;
import smilegate.plop.auth.domain.UserEntity;
import smilegate.plop.auth.domain.UserRepository;
import smilegate.plop.auth.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import smilegate.plop.auth.dto.request.RequestLogin;
import smilegate.plop.auth.dto.request.RequestNewPassword;
import smilegate.plop.auth.dto.response.ResponseUser;
import smilegate.plop.auth.exception.ErrorCode;
import smilegate.plop.auth.exception.WithdrawalUserException;
import smilegate.plop.auth.model.JwtUser;
import smilegate.plop.auth.dto.response.ResponseJWT;
import smilegate.plop.auth.security.JwtTokenProvider;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class AuthService {
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    JwtTokenProvider jwtTokenProvider;

    RedisService redisService;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    public UserDto signUp(UserDto userDto) {
        userDto.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            return null;
        }
        UserEntity userEntity = UserEntity.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .encryptedPwd(userDto.getEncryptedPwd())
                .profile(Map.of(
                        "nickname" , userDto.getNickname(),
                        "img" , ""
                ))
                .state(0)
                .role("ROLE_ADMIN")
                .device(Map.of(
                        "IOS","",
                        "Android","",
                        "Window","",
                        "Mac",""
                ))
                .accessAt(LocalDateTime.now())
                // jpa auditing으로 인한 자동 생성
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
                .loginAt(LocalDateTime.of(1900, 01, 01, 00, 00, 00, 000000000))
                .fcmToken("")
                .build();

        userRepository.save(userEntity);

        return userDto;
    }

    public ResponseJWT logIn(RequestLogin requestLogin) {
        UserEntity userEntity = null;
        // 이메일인 경우
        if (requestLogin.getIdOrEmail().contains("@")) {
            userEntity = userRepository.findByEmail(requestLogin.getIdOrEmail());
        }
        // 아이디인 경우
        else {
            userEntity = userRepository.findByUserId(requestLogin.getIdOrEmail());
        }
        if (userEntity == null)
            throw new EntityNotFoundException();

        if (passwordEncoder.matches(requestLogin.getPassword(),userEntity.getEncryptedPwd())) {
            UserDto userDto = userEntity.toUserDto();
            String accessToken = jwtTokenProvider.createAccessToken(userDto);
            String refreshToken = jwtTokenProvider.createRefreshToken(userDto);
            Date accessExpire = new Date(System.currentTimeMillis() + jwtTokenProvider.getAccessExpiredTime());
            Date refreshExpire = new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiredTime());
            ResponseJWT responseJWT = new ResponseJWT(accessToken,refreshToken,accessExpire,refreshExpire);

            redisService.setValues(userDto.getEmail(),refreshToken);
            if (userEntity.getState() == 9)
            try {
                throw new WithdrawalUserException("user state is 9");
            } catch (WithdrawalUserException e) {
                e.printStackTrace();
                return null;
            }
            userEntity.setLoginAt(LocalDateTime.now());
            userRepository.save(userEntity);

            return responseJWT;
        }
        return null;
    }

    @Transactional
    public UserDto logOut(String jwt) {
        //게이트웨이에서 이미 유효성을 확인함

        JwtUser user = jwtTokenProvider.getUserInfo(jwt);

        redisService.delValues(user.getEmail());

        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null)
            throw new EntityNotFoundException();

        userEntity.setAccessAt(LocalDateTime.now());
//        userRepository.save(userEntity);
        userRepository.saveAndFlush(userEntity);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userEntity, UserDto.class);
        log.error(userDto.toString());
        return userDto;

    }

    public ResponseJWT reissue(String email, String jwt){
        //게이트웨이에서 이미 유효성을 확인함
        //access token이 만료
        //1) refresh token O access token 재발행 + refresh token 재발행
        //2) refresh token X 수동 로그인
        String savedRefreshToken = redisService.getValues(email);
        log.error(savedRefreshToken);
        if(savedRefreshToken.equals(jwt)) {
            UserEntity userEntity= userRepository.findByEmail(email);
            UserDto userDto = userEntity.toUserDto();
            String accessToken = jwtTokenProvider.createAccessToken(userDto);
            String refreshToken = jwtTokenProvider.createRefreshToken(userDto);
            Date accessExpire = new Date(System.currentTimeMillis() + jwtTokenProvider.getAccessExpiredTime());
            Date refreshExpire = new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiredTime());
            ResponseJWT responseJWT = new ResponseJWT(accessToken,refreshToken,accessExpire,refreshExpire);

            redisService.setValues(email, refreshToken);

            return responseJWT;
        } else
            return null;
    }

    public UserDto withdrawal(String jwt) {
        JwtUser user = jwtTokenProvider.getUserInfo(jwt);

        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null)
            throw new EntityNotFoundException(user.getEmail());

        //state :9 => 회원 탈퇴, 실제 삭제은 하지 않음, 탈퇴 후 ~ 기간 이후에 삭제하는 방식?
        userEntity.setState(9);
        UserEntity savedUser = userRepository.save(userEntity);

        UserDto userDto = savedUser.toUserDto();
        return userDto;
    }
    public ResponseUser changePassword(RequestNewPassword newPassword) {
        UserEntity userEntity = userRepository.findByEmail(newPassword.getEmail());
        if (userEntity == null)
            throw new EntityNotFoundException(userEntity.getEmail());

        // 비밀번호가 일치하지 않을 때만
        if (!passwordEncoder.matches(newPassword.getNewPassword(),userEntity.getEncryptedPwd())) {
            String newEncryptedPwd = passwordEncoder.encode(newPassword.getNewPassword());
            userEntity.setEncryptedPwd(newEncryptedPwd);
            UserEntity savedUser = userRepository.save(userEntity);

            ResponseUser responseUser = new ResponseUser(
                    savedUser.getEmail(),
                    savedUser.getProfile().get("nickname").toString(),
                    savedUser.getUserId());
            return responseUser;
        }


        return null;

    }

    public UserDto getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new EntityNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);


        return userDto;
    }

    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new EntityNotFoundException(email);

        UserDto userDto = userEntity.toUserDto();
        return userDto;
    }

//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity userEntity = userRepository.findByEmail(username);
//
//        if (userEntity == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
//                true,true,true,true, new ArrayList<>());
//    }


}
