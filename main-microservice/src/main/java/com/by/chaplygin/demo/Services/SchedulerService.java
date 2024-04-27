package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Model.EmailParams;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.PersonRepository;
import com.by.chaplygin.demo.Util.JdbcRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final PersonRepository personRepository;
    private final JdbcRequest jdbcRequest;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 50 23 * * *")
    public void sendEmailAboutPartyCreateInMyCity(Party party) throws SQLException {
        Set<Person> personsInThisCity = personRepository.findAllByCity(party.getCity());
        if(!personsInThisCity.isEmpty()){
            int counfOfPartiesInCity= jdbcRequest.getCountOfPartiesInCityAndDateJDBC(party.getCity());
            for (Person person:personsInThisCity){
                EmailParams emailParams = new EmailParams();
                emailParams.setEmail(person.getEmail());
                emailParams.setSubject("create");
                emailParams.setMessage("в вашем городе созданно " + counfOfPartiesInCity + "новых вечеринок, скорее проверяй");
                rabbitTemplate.convertAndSend("emailExchange", "emailRoutingKey", emailParams );
            }
        }
    }

}
