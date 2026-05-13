package de.gematik.demis.destination.lookup.service.reader.repository;

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

import de.gematik.demis.destination.lookup.common.entity.Notification;
import de.gematik.demis.destination.lookup.repository.TestWithPostgresContainer;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Integration test for DestinationLookupRepository using Testcontainers database. */
@ActiveProfiles("test")
@SpringBootTest()
class DestinationLookupReaderRepositoryIT extends TestWithPostgresContainer {
  @Autowired private DestinationLookupReaderRepository readerRepository;

  /** Verifies that findByNotificationId returns the correct entity when present. */
  @Test
  @DisplayName("findByNotificationId returns correct entity")
  void findByNotificationId_returnsEntity() throws IOException, InterruptedException {
    Notification notification = prepareNotificationData();

    Notification found = readerRepository.findByNotificationId(notification.getNotificationId());
    assertThat(found).as("Notification should be found").isNotNull();
    assertThat(found.getResponsibleDepartment())
        .as("ResponsibleDepartment should match")
        .isEqualTo(notification.getResponsibleDepartment());
    assertThat(found.getNotificationCategory())
        .as("NotificationCategory should match")
        .isEqualTo(notification.getNotificationCategory());
  }

  /** Verifies that findByNotificationId returns null if the entity is not found. */
  @Test
  @DisplayName("findByNotificationId returns null if not found")
  void findByNotificationId_returnsNullIfNotFound() {

    Notification found =
        readerRepository.findByNotificationId(
            UUID.fromString("abcdef54-310a-4385-8365-ffc37bec8b4c"));
    assertThat(found).as("Notification should be null if not found").isNull();
  }
}
