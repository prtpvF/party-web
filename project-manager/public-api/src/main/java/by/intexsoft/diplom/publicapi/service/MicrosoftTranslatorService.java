package by.intexsoft.diplom.publicapi.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MicrosoftTranslatorService {

        public String translate(String text) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://microsoft-translator-text.p.rapidapi.com/languages?api-version=3.0"))
                    .header("x-rapidapi-key", "f2ff8c4e21msh3816ed914cc8570p1cf283jsnd189173d1511")
                    .header("x-rapidapi-host", "microsoft-translator-text.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
}
