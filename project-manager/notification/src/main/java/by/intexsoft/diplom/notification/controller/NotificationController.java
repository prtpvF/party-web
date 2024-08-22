package by.intexsoft.diplom.notification.controller;

import by.intexsoft.diplom.notification.service.EmailSenderServices;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

        private final EmailSenderServices emailSenderServices;

}