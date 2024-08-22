package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.publicapi.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service help to find person's city and Country.
 * It needs for findPartyInPersonCity method.
 * Location data doesn't store anywhere!
 * @author Mihail Chaplygin
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LocationService {

        private static final String API_URL = "https://ipinfo.io/{ip}/json";
        private final RestTemplate restTemplate;

        public Location getLocationByIp(String ip) {
            return restTemplate.getForObject(API_URL, Location.class, ip);
        }
}