package de.gematik.demis.destination.lookup.service.reader.service;

/*-
 * #%L
 * destination-lookup-reader
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.gematik.demis.destination.lookup.common.dto.DepartmentDTO;
import de.gematik.demis.destination.lookup.common.dto.NotificationCategoryDTO;
import de.gematik.demis.destination.lookup.common.entity.Notification;
import de.gematik.demis.destination.lookup.common.exception.NotificationNotFoundException;
import de.gematik.demis.destination.lookup.service.reader.repository.DestinationLookupReaderRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DestinationLookupServiceTest {
  private final DestinationLookupReaderRepository repository =
      Mockito.mock(DestinationLookupReaderRepository.class);
  private final DestinationLookupService service = new DestinationLookupService(repository);

  @BeforeEach
  void beforeEach() {
    Mockito.reset(repository);
  }

  @Test
  void findValidResponsibleDepartment_returnsDepartment() {
    final var notificationId = UUID.randomUUID();
    Notification notification = new Notification();
    notification.setResponsibleDepartment("GA Hamburg");
    Mockito.when(repository.findByNotificationId(notificationId)).thenReturn(notification);

    DepartmentDTO dto = service.findResponsibleDepartment(notificationId);
    assertThat(dto.getDepartment())
        .as("DepartmentDTO should contain 'GA Hamburg'")
        .isEqualTo("GA Hamburg");
  }

  @Test
  void findResponsibleDepartment_throwsExceptionIfNotFound() {
    final var notificationId = UUID.randomUUID();
    Mockito.when(repository.findByNotificationId(notificationId)).thenReturn(null);
    assertThatThrownBy(() -> service.findResponsibleDepartment(notificationId))
        .isInstanceOf(NotificationNotFoundException.class)
        .hasMessage("No responsible department found for notificationId: " + notificationId);
  }

  @Test
  void findValidNotificationType_returnsCategory() {
    final var notificationId = UUID.randomUUID();
    Notification notification = new Notification();
    notification.setNotificationCategory("CVDP");
    Mockito.when(repository.findByNotificationId(notificationId)).thenReturn(notification);
    NotificationCategoryDTO dto = service.findNotificationCategory(notificationId);
    assertThat(dto.getNotificationCategory())
        .as("NotificationCategoryDTO should contain 'CVDP'")
        .isEqualTo("CVDP");
  }

  @Test
  void findNotificationCategory_returnsEmptyIfNotFound() {
    final var notificationId = UUID.randomUUID();
    Mockito.when(repository.findByNotificationId(notificationId)).thenReturn(null);
    assertThatThrownBy(() -> service.findNotificationCategory(notificationId))
        .isInstanceOf(NotificationNotFoundException.class);
  }
}
