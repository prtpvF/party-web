package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.kafka.KafkaMessageModel;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.dto.VerifyCodeDto;
import by.intexsoft.diplom.auth.exception.CodesAreNotEqualException;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import by.intexsoft.diplom.auth.kafka.KafkaProducer;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PersonStatusEnum;
import by.intexsoft.diplom.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

        private final RedisTemplate<String, String> redisTemplate;
        private final PersonRepository personRepository;
        private final KafkaProducer producer;

        @Value("${verification.lifetime}")
        private long lifeTime;

        @Value("${spring.kafka.topic-verify.name}")
        private String verifyTopic;

        public HttpStatus doCodesMatch(VerifyCodeDto verifyCodeDto){
            String realCode = getCodeFromRedis(verifyCodeDto.getUsername());
            String personCode = verifyCodeDto.getCode();

            if(!realCode.equals(personCode)){
                throw new CodesAreNotEqualException("passed code is not correct");
            }
            changePersonStatus(verifyCodeDto.getUsername());
            deleteFromRedis(verifyCodeDto.getUsername());
            return HttpStatus.OK;
        }

        public void sendVerificationCodeToUser(RegistrationDto registrationDto) {
            Integer verificationCode = generateAndSaveVerificationCode(registrationDto.getUsername());
            producer.sendVerificationCode(KafkaMessageModel
                    .builder()
                    .topic(verifyTopic)
                    .toEmail(registrationDto.getEmail())
                    .verificationCode(verificationCode.toString())
                    .build());
        }

        private Integer generateAndSaveVerificationCode(String username) {
            SecureRandom secureRandom = new SecureRandom();
            Integer verificationCode =  secureRandom.nextInt(999999);
            saveIntoRedisWithTTL(username, verificationCode.toString());
            return verificationCode;
        }

        private void changePersonStatus(String username) {
            PersonModel personModel = personRepository.findByUsername(username)
                    .orElseThrow(() -> new PersonNotFoundException("cannot find person with this username"));
            personModel.setStatus(PersonStatusEnum.AVAILABLE.name());
            personRepository.save(personModel);
        }

        private String getCodeFromRedis(String username) {
            return redisTemplate.opsForValue().get(username + ":" + "verification");
        }

        private void saveIntoRedisWithTTL(String username, String verificationCode) {
            redisTemplate.opsForValue().set(username+":"+"verification",
                    verificationCode,
                    lifeTime,
                    TimeUnit.MILLISECONDS);
        }

        private void deleteFromRedis(String username) {
            redisTemplate.delete(username+":"+"verification");
        }
}