package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import by.intexsoft.diplom.auth.kafka.KafkaProducer;
import by.intexsoft.diplom.auth.response.AuthResponse;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.enums.PersonStatusEnum;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.PersonRepository;
import by.intexsoft.diplom.common.repository.RoleRepository;
import by.intexsoft.diplom.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.security.SecureClassLoader;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final PersonRepository personRepository;
        private final RoleRepository roleRepository;
        private final EmailVerificationService emailVerificationService;
        private final PasswordEncoder passwordEncoder;
        private final ModelMapper modelMapper;


        /**
         * Method prepares person for registration, checks person's existing in db,
         * sends email with verification code and starts threads which will delete person in case
         * person won't finish registration process
         * @param registrationDto - dto with necessary data for registration
         */
        @Transactional
        public void register(RegistrationDto registrationDto) {
            checkPersonExists(registrationDto.getUsername(), registrationDto.getEmail());
            PersonModel personModel = convertDtoToPerson(registrationDto);
            preparePersonForRegistration(personModel, registrationDto.isOrganizer());
            personRepository.save(personModel);
            emailVerificationService.sendVerificationCodeToUser(registrationDto);
            log.info("New person has registered: {}", personModel);
        }

//        public AccessTokenResponse login(LoginDto logInDto) {
//            PersonModel personModel = personRepository.findByUsername(logInDto.getUsername())
//                    .filter(p -> passwordEncoder.matches(logInDto.getPassword(), p.getPassword()))
//                    .orElseThrow(() -> new PersonNotFoundException("Person with these credentials not found"));
//            return keycloakService.generateToken(logInDto);
//        }

//        public void logout(HttpServletRequest request, HttpServletResponse response,
//                           String authorization) {
//            Authentication authentication = SecurityContextHolder
//                    .getContext()
//                    .getAuthentication();
//
//            if (authentication != null) {
//                new SecurityContextLogoutHandler().logout(request, response,
//                        authentication);
//                jwtUtil.removeTokens(getUsernameFromToken(authorization));
//            }
//        }

//        public AuthResponse createJwtToken(String refreshToken) {
//            String username = getUsernameFromToken(refreshToken);
//            jwtUtil.isRefreshTokenActive(username);
//            jwtUtil.removeTokens(username);
//            String accessToken = jwtUtil.generateToken(username);
//            String refreshedToken = jwtUtil.generateRefreshToken(username);
//            return AuthResponse.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshedToken)
//                    .build();
//        }

        private void checkPersonExists(String username, String email) {
            personRepository.findByUsernameOrEmail(username, email).ifPresent(p -> {
                throw new PersonAlreadyExists("Person with this credentials already exists");
            });
        }

        private PersonModel convertDtoToPerson(RegistrationDto registrationDto) {
            return modelMapper.map(registrationDto, PersonModel.class);
        }

        private void preparePersonForRegistration(PersonModel personModel, boolean organizer) {
            personModel.setActive(true);
            personModel.setPassword(passwordEncoder.encode(personModel.getPassword()));
            personModel.setRating(0.0);
            personModel.setStatus(PersonStatusEnum.UNAVAILABLE.name());
            personModel.setRole(getPersonRole(organizer));
        }

        private PersonRoleModel getPersonRole(boolean organizer) {
            String roleName = organizer ? PersonRolesEnum.ORGANIZER.name() : PersonRolesEnum.USER.name();
            return roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        }

//        private String getUsernameFromToken(String authorization) {
//            return jwtUtil.validateTokenAndRetrieveClaim(authorization);
//        }
}