package smilegate.plop.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import smilegate.plop.user.domain.friend.FriendEntity;
import smilegate.plop.user.domain.friend.FriendRepository;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.domain.user.UserRepository;
import smilegate.plop.user.dto.UserDto;
import smilegate.plop.user.dto.request.RequestProfile;
import smilegate.plop.user.dto.response.ResponseProfile;
import smilegate.plop.user.dto.response.ResponseUser;
import smilegate.plop.user.exception.FileStreamException;
import smilegate.plop.user.exception.UserNotFoundException;
import smilegate.plop.user.model.JwtUser;
import smilegate.plop.user.security.JwtTokenProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    UserRepository userRepository;
    FriendRepository friendRepository;

    JwtTokenProvider jwtTokenProvider;

    AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    String S3Bucket;

    @Autowired
    public UserService(UserRepository userRepository, FriendRepository friendRepository,
               JwtTokenProvider jwtTokenProvider, AmazonS3Client amazonS3Client){
        this.userRepository = userRepository;
        this.friendRepository =friendRepository;
        this.jwtTokenProvider =jwtTokenProvider;
        this.amazonS3Client = amazonS3Client;
    }

    public ResponseProfile getProfile(String target) {
        UserEntity userEntity = userRepository.findByUserIdOrEmail(target,target);

        if (userEntity == null) {
            throw new UserNotFoundException(String.format("[%s] is Not Found", target));
        } else {
            ResponseProfile responseProfile = new ResponseProfile(
                    userEntity.getUserId(), userEntity.getEmail(),userEntity.getProfile());
            return responseProfile;
        }
    }

    public ResponseProfile putProfile(RequestProfile profile) {
        UserEntity userEntity = userRepository.findByUserIdOrEmail(profile.getTarget(),profile.getTarget());

        if (userEntity == null) {
            throw new UserNotFoundException(String.format("[%s] is Not Found",profile.getTarget()));
        }
        MultipartFile multipartFile = profile.getImg();
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        try {
            objMeta.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(S3Bucket, s3FileName, multipartFile.getInputStream(), objMeta);
            String uri = amazonS3Client.getUrl(S3Bucket, s3FileName).toString();

            userEntity.setProfile(Map.of(
                    "nickname", profile.getNickname(),
                    "img", uri
            ));
            UserEntity savedUser = userRepository.save(userEntity);
            ResponseProfile responseProfile = new ResponseProfile(
                    savedUser.getUserId(), savedUser.getEmail(), savedUser.getProfile()
            );
            return responseProfile;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStreamException("file streaming is failed");
        }
    }

    public List<ResponseProfile> searchUser(String target) {
        List<UserEntity> users = userRepository.findByUserIdContainingOrEmailContaining(target,target);
        log.error(users.toString());

        List<ResponseProfile> responseUserList = new ArrayList<>();
        if (users == null)
            throw new UserNotFoundException("there is no related user");
        else {
            for(UserEntity userEntity : users) {
                ResponseProfile user = new ResponseProfile(
                        userEntity.getUserId(), userEntity.getEmail(), userEntity.getProfile());
                responseUserList.add(user);
            }
            return responseUserList;
        }
    }
}
