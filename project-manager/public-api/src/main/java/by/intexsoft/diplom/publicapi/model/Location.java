package by.intexsoft.diplom.publicapi.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model of response ip service with all location data
 * @author Mihail Chaplygin
 * @version 1.0
 */
@Getter
@Setter
public class Location {

        private String ip;
        private String city;
        private String region;
        private String country;
        private String loc;
        private String postal;
        private String timezone;
}
