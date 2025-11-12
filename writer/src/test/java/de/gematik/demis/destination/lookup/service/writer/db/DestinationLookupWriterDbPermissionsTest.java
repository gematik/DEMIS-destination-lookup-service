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
      "spring.datasource.username=destination_lookup_writer",
      "spring.datasource.password=destination_lookup_writer"
    })
class DestinationLookupWriterDbPermissionsTest extends TestWithPostgresContainer {
  @Autowired private DestinationLookupWriterRepository repository;

  @DisplayName("Writer user can write to destination_lookup table")
  @Test
  void writerCouldWriteToDestinationLookupTable() {
    assertThatNoException()
        .isThrownBy(
            () ->
                repository.save(
                    new Notification(
                        UUID.randomUUID(), "GA Berlin", "CVDP", OffsetDateTime.now())));
  }

  @DisplayName("Writer user can read from destination_lookup table")
  @Test
  void writerCouldReadFromDestinationLookupTable() {
    UUID uuid = UUID.randomUUID();
    assertThatNoException()
        .isThrownBy(
            () ->
                repository.save(new Notification(uuid, "GA Berlin", "CVDP", OffsetDateTime.now())));
    assertThatNoException().isThrownBy(() -> repository.findById(uuid).orElseThrow());
  }

  @DisplayName("Writer user can update destination_lookup table")
  @Test
  void writerCouldUpdateOnDestinationLookupTable() {
    UUID uuid = UUID.randomUUID();
    Notification notification = new Notification(uuid, "GA Berlin", "CVDP", OffsetDateTime.now());
    assertThatNoException().isThrownBy(() -> repository.save(notification));
    notification.setResponsibleDepartment("GA Munich");
    assertThatNoException().isThrownBy(() -> repository.save(notification));
  }

  @DisplayName("Writer user cannot delete from destination_lookup table")
  @Test
  void writerCouldNotDeleteFromDestinationLookupTable() {
    UUID uuid = UUID.randomUUID();
    Notification notification = new Notification(uuid, "GA Berlin", "CVDP", OffsetDateTime.now());
    repository.save(notification);
    assertThatExceptionOfType(InvalidDataAccessResourceUsageException.class)
        .isThrownBy(() -> repository.delete(notification));
  }
}
