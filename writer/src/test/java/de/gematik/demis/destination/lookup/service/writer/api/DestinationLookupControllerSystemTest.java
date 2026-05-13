package de.gematik.demis.destination.lookup.service.writer.api;

/*-
 * #%L
 * destination-lookup-reader
 * %%
 * Copyright (C) 2025 - 2026 gematik GmbH
 * %%
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the
 * European Commission – subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 *
 * You find a copy of the Licence in the "Licence" file or at
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * In case of changes by gematik find details in the "Readme" file.
 *
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 *
 * *******
 *
 * For additional notes and disclaimer from gematik and in case of changes by gematik,
 * find details in the "Readme" file.
 * #L%
 */

import static org.assertj.core.api.Assertions.assertThat;

import de.gematik.demis.destination.lookup.common.dto.NotificationDTO;
import de.gematik.demis.destination.lookup.repository.TestWithPostgresContainer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DestinationLookupControllerSystemTest extends TestWithPostgresContainer {
  private final RestTemplate restTemplate = new RestTemplate();

  @LocalServerPort private int port;

  @DisplayName("Creates a new Notification and the return status code is 200")
  @Test
  void storeNewNotification_isSuccessful() {

    NotificationDTO notificationDTO =
        new NotificationDTO(
            "5e9dbe70-ea54-4d23-bc49-0ffc792db65b", "GA Berlin", "CVDP", Instant.now().toString());

    HttpEntity<String> entity =
        new HttpEntity<>(notificationDtoToJson(notificationDTO), createJsonHeaders());

    var response =
        restTemplate.exchange(
            String.format("http://localhost:%d/notification", port),
            HttpMethod.POST,
            entity,
            String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(notificationDTO.notificationId());
  }

  @DisplayName("Updates an existing Notification and the return status code is 200")
  @Test
  void updateMetadata_isSuccessful() {
    // GIVEN
    final String notificationId = "84d6cb09-924a-42c2-a786-0ec0d9271d4a";
    final NotificationDTO notificationDTO =
        new NotificationDTO(
            notificationId,
            "GA Leipzig",
            "CVDP",
            OffsetDateTime.now(ZoneId.systemDefault()).toString());
    // WHEN
    HttpEntity<String> entity =
        new HttpEntity<>(notificationDtoToJson(notificationDTO), createJsonHeaders());
    var updateResponse =
        restTemplate.exchange(
            String.format("http://localhost:%d/notification", port),
            HttpMethod.POST,
            entity,
            String.class);
    // THEN
    assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updateResponse.getBody()).isEqualTo(notificationDTO.notificationId());
  }

  @DisplayName(
      "Attempts to update an existing Notification with an invalid ID and the return status code is 404")
  @Test
  void updateMetadata_returnsBadRequestWithBadId() {
    final String notificationId = "not-a-valid-id";
    final NotificationDTO notificationDTO =
        new NotificationDTO(
            notificationId,
            "GA Berlin",
            "CVDP",
            OffsetDateTime.now(ZoneId.systemDefault()).toString());

    HttpEntity<String> entity =
        new HttpEntity<>(notificationDtoToJson(notificationDTO), createJsonHeaders());
    // WHEN
    try {
      restTemplate.exchange(
          String.format("http://localhost:%d/notification", port),
          HttpMethod.POST,
          entity,
          String.class);
    } catch (HttpClientErrorException e) {
      // THEN
      assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
  }

  @DisplayName(
      "Attempts to update an existing Notification with a different category and the return status code is 404")
  @Test
  void updateMetadata_returnsBadRequestWithDifferentCodeCategory() {
    final String notificationId = "84d6cb09-924a-42c2-a786-0ec0d9271d4a";
    final NotificationDTO notificationDTO =
        new NotificationDTO(notificationId, "GA Berlin", "HIVP", Instant.now().toString());

    HttpEntity<String> entity =
        new HttpEntity<>(notificationDtoToJson(notificationDTO), createJsonHeaders());
    // WHEN
    try {
      restTemplate.exchange(
          String.format("http://localhost:%d/notification", port),
          HttpMethod.POST,
          entity,
          String.class);
    } catch (HttpClientErrorException e) {
      // THEN
      assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
  }

  private HttpHeaders createJsonHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
    return headers;
  }

  private String notificationDtoToJson(NotificationDTO notificationDTO) {
    try {
      return new ObjectMapper().writeValueAsString(notificationDTO);
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert NotificationDTO to JSON", e);
    }
  }
}
