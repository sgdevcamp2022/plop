package smilegate.plop.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import smilegate.plop.auth.domain.UserEntity;
import smilegate.plop.auth.domain.UserRepository;
import smilegate.plop.auth.dto.response.ErrorResponseDto;
import smilegate.plop.auth.dto.response.ResponseDto;
import smilegate.plop.auth.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import smilegate.plop.auth.exception.ErrorCode;
import smilegate.plop.auth.exception.WithdrawalUserException;
import smilegate.plop.auth.dto.request.RequestLogin;
import smilegate.plop.auth.dto.response.ResponseJWT;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import smilegate.plop.auth.service.AuthService;
import smilegate.plop.auth.service.RedisService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthService authService;
    private Environment env;

    private RedisService redisService;

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AuthService authService, Environment env
            ,RedisService redisService ,UserRepository userRepository, JwtTokenProvider jwtTokenProvider  ) {
        super.setAuthenticationManager(authenticationManager);
        this.authService = authService;
        this.env = env;
        this.redisService = redisService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(), creds.getPassword(),new ArrayList<>()
                    ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String userName = ( ((User)authResult.getPrincipal()).getUsername() );
        // 수정 - 닉네임 불러오도록 수정 필요
        UserDto userDto = authService.getUserDetailsByEmail(userName);
        log.error("token-userDto : ", userDto);
        Date accessExpire = new Date(System.currentTimeMillis() + jwtTokenProvider.getAccessExpiredTime());
        Date refreshExpire = new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiredTime());

        String accessToken = jwtTokenProvider.createAccessToken(userDto);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDto);


        redisService.setValues(userDto.getEmail(),refreshToken);

        UserEntity userEntity =  userRepository.findByEmail(userDto.getEmail());
//        if (userEntity == null)
//            throw new UsernameNotFoundException("user not found");

        // state : 9 => 회원 탈퇴 상태
        if (userEntity.getState() == 9)
            try {
                throw new WithdrawalUserException("user state is 9");
            } catch (WithdrawalUserException e) {
                ObjectMapper mapper = new ObjectMapper();

                response.setContentType("application/json");
                ErrorResponseDto errorResponseDto = ErrorCode.WITHDRAWAL_USER.toErrorResponseDto(e.getMessage());
                ResponseDto responseDto = new ResponseDto("FAIL","login failed", errorResponseDto);
                String result = mapper.writeValueAsString(responseDto);
                // 한글 응답을 보내기 위해 응답 인코딩 방식 지정
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(result);
                return;
            }
        userEntity.setLoginAt(LocalDateTime.now());
        userRepository.save(userEntity).toString();

        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        ResponseJWT responseJWT = new ResponseJWT(accessToken,refreshToken,accessExpire,refreshExpire);
        ResponseDto responseDto = new ResponseDto("SUCCESS","login successfully", responseJWT);
        String result = mapper.writeValueAsString(responseDto);

        response.getWriter().write(result);
    }

}
