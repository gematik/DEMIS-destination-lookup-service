package de.gematik.demis.destination.lookup.service.writer.db;

/*-
 * #%L
 * destination-lookup-writer
 * %%
 * Copyright (C) 2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 * #L%
 */

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import de.gematik.demis.destination.lookup.common.entity.Notification;
import de.gematik.demis.destination.lookup.repository.TestWithPostgresContainer;
import de.gematik.demis.destination.lookup.service.writer.repository.DestinationLookupWriterRepository;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
    properties = {
      "spring.datasource.username=destination_lookup_reader",
      "spring.datasource.password=destination_lookup_reader"
    })
class DestinationLookupReaderDbPermissionsTest extends TestWithPostgresContainer {
  @Autowired private DestinationLookupWriterRepository repository;

  @DisplayName("Reader user could not write to destination_lookup table")
  @Test
  void readerCouldNotWriteToDestinationLookupTable() {
    assertThatExceptionOfType(InvalidDataAccessResourceUsageException.class)
        .isThrownBy(
            () ->
                repository.save(
                    new Notification(
                        UUID.randomUUID(), "GA Berlin", "CVDP", OffsetDateTime.now())));
  }

  @DisplayName("Reader user could read from destination_lookup table")
  @Test
  void readerCouldReadFromDestinationLookupTable() throws IOException, InterruptedException {
    Notification notification = prepareNotificationData();
    assertThatNoException()
        .isThrownBy(() -> repository.findById(notification.getNotificationId()).orElseThrow());
  }

  @DisplayName("Reader user could not update destination_lookup table")
  @Test
  void readerCouldNotUpdateOnDestinationLookupTable() throws IOException, InterruptedException {
    Notification notification = prepareNotificationData();

    notification.setResponsibleDepartment("GA Munich");
    assertThatExceptionOfType(InvalidDataAccessResourceUsageException.class)
        .isThrownBy(() -> repository.save(notification));
  }

  @DisplayName("Reader user could not delete from destination_lookup table")
  @Test
  void readerCouldNotDeleteFromDestinationLookupTable() throws IOException, InterruptedException {
    Notification notification = prepareNotificationData();
    assertThatExceptionOfType(InvalidDataAccessResourceUsageException.class)
        .isThrownBy(() -> repository.delete(notification));
  }
}
