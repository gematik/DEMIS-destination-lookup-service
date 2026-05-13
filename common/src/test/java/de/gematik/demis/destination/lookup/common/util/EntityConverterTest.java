package de.gematik.demis.destination.lookup.common.util;

/*-
 * #%L
 * destination-lookup-common
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.gematik.demis.destination.lookup.common.dto.NotificationDTO;
import de.gematik.demis.destination.lookup.common.entity.Notification;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("EntityConverter Tests")
class EntityConverterTest {

  @Nested
  @DisplayName("from(NotificationDTO) method tests")
  class FromNotificationDTOTests {

    @Test
    @DisplayName("Should convert NotificationDTO to Notification with isValid=false")
    void shouldConvertNotificationDTOWithDefaultIsValid() {
      // Given
      String notificationId = String.valueOf(UUID.randomUUID());
      String responsibleDepartment = "Health Department";
      String notificationCategory = "OUTBREAK";
      String lastUpdated = "2025-01-15T10:30:00Z";

      NotificationDTO dto =
          new NotificationDTO(
              notificationId, responsibleDepartment, notificationCategory, lastUpdated);

      // When
      Notification result = EntityConverter.from(dto);

      // Then
      assertThat(result)
          .as("Notification object should be created through from-method")
          .isNotNull();
      assertThat(result.getNotificationId())
          .as("Notification ID should match input")
          .asString()
          .isEqualTo(notificationId);
      assertThat(result.getResponsibleDepartment())
          .as("Responsible department should match input")
          .isEqualTo(responsibleDepartment);
      assertThat(result.getNotificationCategory())
          .as("Notification category should match input")
          .isEqualTo(notificationCategory);
      assertThat(result.getLastUpdated())
          .as("Last updated timestamp should be correctly parsed")
          .isEqualTo(OffsetDateTime.parse(lastUpdated));
    }

    @Test
    @DisplayName("Should handle null values in NotificationDTO")
    void shouldHandleNullValuesInDTO() {
      // Given
      NotificationDTO dto = new NotificationDTO(null, null, null, "2025-01-15T10:30:00Z");

      // When
      assertThatThrownBy(() -> EntityConverter.from(dto))
          .as("Should throw NullPointerException for null values in DTO")
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("from(NotificationDTO, boolean) method tests")
  class FromNotificationDTOWithIsValidTests {

    @Test
    @DisplayName("Should convert NotificationDTO with canDelete=true")
    void shouldConvertNotificationDTOWithisValidTrue() {
      // Given
      String notificationId = String.valueOf(UUID.randomUUID());
      String responsibleDepartment = "Emergency Services";
      String notificationCategory = "EMERGENCY";
      String lastUpdated = "2025-01-15T14:45:30.123Z";

      NotificationDTO dto =
          new NotificationDTO(
              notificationId, responsibleDepartment, notificationCategory, lastUpdated);

      // When
      Notification result = EntityConverter.from(dto);

      // Then
      assertThat(result)
          .as("Notification object should be created through from-method with isValid parameter")
          .isNotNull();
      assertThat(result.getNotificationId())
          .as("Notification ID should match input")
          .asString()
          .isEqualTo(notificationId);
      assertThat(result.getResponsibleDepartment())
          .as("Responsible department should match input")
          .isEqualTo(responsibleDepartment);
      assertThat(result.getNotificationCategory())
          .as("Notification category should match input")
          .isEqualTo(notificationCategory);
      assertThat(result.getLastUpdated())
          .as("Last updated timestamp should be correctly parsed")
          .isEqualTo(OffsetDateTime.parse(lastUpdated));
    }

    @Test
    @DisplayName("Should convert NotificationDTO")
    void shouldConvertNotificationDTOWithIsValidFalse() {
      // Given
      String lastUpdated = "2025-01-15T08:15:00Z";
      NotificationDTO dto =
          new NotificationDTO(
              String.valueOf(UUID.randomUUID()), "Department", "ROUTINE", lastUpdated);

      // When
      Notification result = EntityConverter.from(dto);

      // Then
      assertThat(result)
          .as("Notification object should be created through from-method")
          .isNotNull();
    }

    @Test
    @DisplayName("Should handle empty strings in NotificationDTO")
    void expectThrownByInvalidNotificationId() {
      // Given
      try {
        EntityConverter.from(new NotificationDTO(null, "", "", "2025-01-15T10:30:00Z"));
      } catch (Exception e) {
        assertThat(e).isInstanceOf(NullPointerException.class);
      }
    }
  }

  @Test
  @DisplayName("Should handle empty strings in NotificationDTO")
  void shouldHandleEmptyStringsInDTO() {
    // Given
    NotificationDTO dto =
        new NotificationDTO(String.valueOf(UUID.randomUUID()), "", "", "2025-01-15T10:30:00Z");

    // When
    Notification result = EntityConverter.from(dto);

    // Then
    assertThat(result.getResponsibleDepartment())
        .as("Empty responsible department should be preserved")
        .isEmpty();
    assertThat(result.getNotificationCategory())
        .as("Empty notification category should be preserved")
        .isEmpty();
  }
}

@Nested
@DisplayName("toInstant(String) method tests")
class ToInstantTests {

  @ParameterizedTest
  @DisplayName("Should parse valid ISO 8601 date-time strings")
  @ValueSource(
      strings = {
        "2025-01-15T10:30:00Z",
        "2025-01-15T10:30:00.123Z",
        "2025-01-15T10:30:00.123456Z",
        "2025-01-15T10:30:00.123456789Z",
        "2025-12-31T23:59:59Z",
        "1970-01-01T00:00:00Z"
      })
  void shouldParseValidISO8601DateTimeStrings(String dateTimeString) {
    // When
    OffsetDateTime result = EntityConverter.toOffsetDateTime(dateTimeString);

    // Then
    assertThat(result)
        .as("Parsed instant should match Java's built-in parser for: " + dateTimeString)
        .isEqualTo(OffsetDateTime.parse(dateTimeString));
  }

  @Test
  @DisplayName("Should throw exception for invalid date-time string")
  void shouldThrowExceptionForInvalidDateTime() {
    // Given
    String invalidDateTime = "invalid-date-time";

    // When & Then
    assertThatThrownBy(() -> EntityConverter.toOffsetDateTime(invalidDateTime))
        .as("Should throw DateTimeParseException for invalid date-time string")
        .isInstanceOf(DateTimeParseException.class);
  }

  @Test
  @DisplayName("Should throw exception for null date-time string")
  void shouldThrowExceptionForNullDateTime() {
    // When & Then
    assertThatThrownBy(() -> EntityConverter.toOffsetDateTime(null))
        .as("Should throw NullPointerException for null date-time string")
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw exception for empty date-time string")
  void shouldThrowExceptionForEmptyDateTime() {
    // When & Then
    assertThatThrownBy(() -> EntityConverter.toOffsetDateTime(""))
        .as("Should throw DateTimeParseException for empty date-time string")
        .isInstanceOf(DateTimeParseException.class);
  }

  @ParameterizedTest
  @DisplayName("Should throw exception for malformed date-time strings")
  @ValueSource(
      strings = {
        "2025-01-15",
        "10:30:00",
        "2025-01-15 10:30:00",
        "2025-13-01T10:30:00Z",
        "2025-01-32T10:30:00Z",
        "2025-01-15T25:30:00Z",
        "2025-01-15T10:60:00Z",
        "2025-01-15T10:30:60Z"
      })
  void shouldThrowExceptionForMalformedDateTimeStrings(String malformedDateTime) {
    // When & Then
    assertThatThrownBy(() -> EntityConverter.toOffsetDateTime(malformedDateTime))
        .as(
            "Should throw DateTimeParseException for malformed date-time string: "
                + malformedDateTime)
        .isInstanceOf(DateTimeParseException.class);
  }
}

@Nested
@DisplayName("Integration Tests")
class IntegrationTests {

  @Test
  @DisplayName("Should handle complete conversion workflow")
  void shouldHandleCompleteConversionWorkflow() {
    // Given
    String dateTimeString = "2025-01-15T12:00:00.456Z";
    NotificationDTO dto =
        new NotificationDTO(
            String.valueOf(UUID.randomUUID()), "Test Department", "TEST_CATEGORY", dateTimeString);

    // When
    Notification resultDefault = EntityConverter.from(dto);
    Notification resultWithIsValid = EntityConverter.from(dto);
    OffsetDateTime offsetDateTime = EntityConverter.toOffsetDateTime(dateTimeString);

    // Then
    assertThat(resultDefault)
        .as("Default conversion should create notification object")
        .isNotNull();
    assertThat(resultWithIsValid)
        .as("Conversion with isValid=true should create notification object")
        .isNotNull();
    assertThat(offsetDateTime).as("Direct instant parsing should work").isNotNull();

    // Verify both conversions use the same instant
    assertThat(resultDefault.getLastUpdated())
        .as("Default conversion should use same instant as direct parsing")
        .isEqualTo(offsetDateTime);
    assertThat(resultWithIsValid.getLastUpdated())
        .as("Conversion with isValid should use same instant as direct parsing")
        .isEqualTo(offsetDateTime);

    // Verify other fields are identical
    assertThat(resultDefault.getNotificationId())
        .as("Notification IDs should be identical between conversion methods")
        .isEqualTo(resultWithIsValid.getNotificationId());
    assertThat(resultDefault.getResponsibleDepartment())
        .as("Responsible departments should be identical between conversion methods")
        .isEqualTo(resultWithIsValid.getResponsibleDepartment());
    assertThat(resultDefault.getNotificationCategory())
        .as("Notification categories should be identical between conversion methods")
        .isEqualTo(resultWithIsValid.getNotificationCategory());
  }
}
