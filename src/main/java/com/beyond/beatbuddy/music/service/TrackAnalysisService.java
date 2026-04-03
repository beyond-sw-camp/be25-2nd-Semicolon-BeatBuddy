package com.beyond.beatbuddy.music.service;

import com.beyond.beatbuddy.global.exception.BusinessException;
import com.beyond.beatbuddy.music.dto.response.TrackAnalysisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TrackAnalysisService {

	@Value("${rapidapi.key}")
	private String trackAnalysisKey;

	@Value("${rapidapi.track-analysis-host}")
	private String trackAnalysisHost;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public TrackAnalysisResponse getFeatures(String spotifyId) {
		String fullUrl = "https://track-analysis.p.rapidapi.com/pktx/spotify/" + spotifyId;

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(fullUrl))
				.header("x-rapidapi-key", trackAnalysisKey)
				.header("x-rapidapi-host", trackAnalysisHost)
				.GET()
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "RapidAPI 호출 실패: " + response.statusCode());
			}

			return objectMapper.readValue(response.body(), TrackAnalysisResponse.class);

		} catch (IOException | InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "RapidAPI 호출 실패: " + e.getMessage());
		}
	}
}