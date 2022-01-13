package com.kenzie.app;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * This class will accomplish two things: 1. it will make a GET request to the
 * API, which will provide the questions for the game; 2. it will parse the
 * resulting HTTP response into a list of Clue Objects and store them in the
 * ClueListDTO.
 *
 * @author Ethan Tauriainen
 */
public class CustomHttpClient {

    /**
     * Makes a GET request to the API at the given URL.
     * Based on the reading, Calling an HTTP API.
     *
     * @param URLString the URL with which the GET request will be made.
     * @return the response body containing a JSON string, or, if it fails,
     * an exception will be thrown and handled upstream.
     *
     * @throws URISyntaxException thrown if the URL provided cannot be parsed.
     * @throws IOException general input/output exception, will catch JSON parsing issues, too.
     * @throws InterruptedException thrown if the connection halts (i.e. the thread is halted).
     * @throws ResponseCodeException thrown if any response code other than 200 is received.
     */
    public static String sendGET(String URLString) throws URISyntaxException, IOException,
            InterruptedException, ResponseCodeException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(URLString))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = httpResponse.statusCode();
        if (status == 200) {
            return httpResponse.body();
        } else {
            // Custom exception I made to handle non '200' response codes.
            throw new ResponseCodeException("Error: bad response code of " + status + " received.");
        }
    }

    /**
     * Method to parse the HTTP response into a list of Clue objects.
     * Since I made a ClueListDTO class there was no need to use the TypeReference syntax
     * in order to create the list. I simply mapped the information to the DTO and returned
     * the list created as a part of the ClueListDTO.
     *
     * @param httpResponseBody the JSON response from the API. Acquired above.
     * @return the list of ClueDTO objects created in the ClueListDTO. Should be 100.
     * @throws JsonProcessingException in case the HTTP response cannot be parsed as JSON.
     */
    public static List<ClueDTO> getCluesList(String httpResponseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClueListDTO ClueDTOList = objectMapper.readValue(httpResponseBody, ClueListDTO.class);
        return ClueDTOList.getClues();
    }
}

