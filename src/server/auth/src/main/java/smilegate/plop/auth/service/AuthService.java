package smilegate.plop.auth.service;


import org.modelmapper.convention.MatchingStrategies;
import smilegate.plop.auth.domain.UserEntity;
import smilegate.plop.auth.domain.UserRepository;
import smilegate.plop.auth.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import smilegate.plop.auth.exception.JwtTokenMissingException;
import smilegate.plop.auth.model.JwtUser;
import smilegate.plop.auth.model.ResponseJWT;
import smilegate.plop.auth.security.JwtTokenProvider;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class AuthService implements UserDetailsService {
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

    public UserDto createUser(UserDto userDto) {
        userDto.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));
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
                .build();

        userRepository.save(userEntity);

        return userDto;
    }

    @Transactional
    public UserDto logOut(String jwt) {
        //게이트웨이에서 이미 유효성을 확인함

        JwtUser user = jwtTokenProvider.getUserInfo(jwt);

        redisService.delValues(user.getEmail());

        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null)
            throw new UsernameNotFoundException(user.getEmail());

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
        }
        return null;
    }

    public UserDto withdrawal(String jwt) {
        JwtUser user = jwtTokenProvider.getUserInfo(jwt);

        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null)
            throw new UsernameNotFoundException(user.getEmail());

        //state :9 => 회원 탈퇴, 실제 삭제은 하지 않음, 탈퇴 후 ~ 기간 이후에 삭제하는 방식?
        userEntity.setState(9);
        UserEntity savedUser = userRepository.save(userEntity);

        UserDto userDto = savedUser.toUserDto();
        return userDto;
    }

    public UserDto getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);


        return userDto;
    }

    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = userEntity.toUserDto();
//        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
//        UserDto userDto = UserDto.builder()
//                .email(userEntity.getEmail())
//                .userId(userEntity.getUserId())
//                .encryptedPwd(userEntity.getEncryptedPwd())
//                .state(userEntity.getState())
//                .img(userEntity.getProfile().get("img").toString())
//                .nickname(userEntity.getProfile().get("nickname").toString())
//                .role(userEntity.getRole())
//                .build();
        return userDto;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true,true,true,true, new ArrayList<>());
    }


}