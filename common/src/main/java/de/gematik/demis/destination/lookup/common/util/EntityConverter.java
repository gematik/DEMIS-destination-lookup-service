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

import de.gematik.demis.destination.lookup.common.dto.NotificationDTO;
import de.gematik.demis.destination.lookup.common.entity.Notification;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * Utility class for converting between NotificationDTO and Notification entity.
 *
 * <p>This class provides methods to convert a NotificationDTO object to a Notification entity,
 * including handling of date-time parsing and setting additional fields.
 */
public final class EntityConverter {
  private EntityConverter() {
    // Utility class
  }

  /**
   * Converts a NotificationDTO to a Notification entity with specified canDelete value.
   *
   * @param notificationDTO the NotificationDTO to convert
   * @return the corresponding Notification entity
   */
  public static Notification from(final NotificationDTO notificationDTO) {
    final var lastUpdated = toOffsetDateTime(notificationDTO.lastUpdated());

    final Notification entity = new Notification();
    entity.setNotificationId(UUID.fromString(notificationDTO.notificationId()));
    entity.setResponsibleDepartment(notificationDTO.responsibleDepartment());
    entity.setNotificationCategory(notificationDTO.notificationCategory());
    entity.setLastUpdated(lastUpdated);
    return entity;
  }

  /**
   * Parses a date-time string in ISO 8601 format and converts it to an Instant in the Europe/Berlin
   * time zone.
   *
   * @param lastUpdated the date-time string to parse
   * @return the corresponding Instant
   */
  public static OffsetDateTime toOffsetDateTime(String lastUpdated) {
    return OffsetDateTime.ofInstant(Instant.parse(lastUpdated), ZoneId.systemDefault());
  }
}
