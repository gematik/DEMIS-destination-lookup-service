package de.gematik.demis.destination.lookup.service.reader.api;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import de.gematik.demis.destination.lookup.common.dto.DepartmentDTO;
import de.gematik.demis.destination.lookup.common.dto.NotificationCategoryDTO;
import de.gematik.demis.destination.lookup.service.reader.service.DestinationLookupService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DestinationLookupControllerTest {

  @Mock private DestinationLookupService destinationLookupServiceMock;

  @InjectMocks private DestinationLookupController controller;

  /** Verifies that the responsible department endpoint returns the expected DepartmentDTO. */
  @Test
  void getResponsibleDepartment_returnsDepartmentDto() {

    final var notificationId = UUID.randomUUID();
    when(destinationLookupServiceMock.findResponsibleDepartment(notificationId))
        .thenReturn(new DepartmentDTO("dummy-department"));
    ResponseEntity<DepartmentDTO> response = controller.getResponsibleDepartment(notificationId);
    assertThat(response.getStatusCode().value()).as("HTTP status should be 200").isEqualTo(200);
    assertNotNull(response.getBody());
    assertThat(response.getBody().getDepartment())
        .as("Department should be 'dummy-department'")
        .isEqualTo("dummy-department");
  }

  /**
   * Verifies that the notification category endpoint returns the expected NotificationCategoryDto.
   */
  @Test
  void getNotificationType_returnsNotificationCategoryDto() {
    final var notificationId = UUID.randomUUID();
    when(destinationLookupServiceMock.findNotificationCategory(notificationId))
        .thenReturn(new NotificationCategoryDTO("dummy-notification-category"));
    ResponseEntity<NotificationCategoryDTO> response =
        controller.getNotificationType(notificationId);
    assertThat(response.getStatusCode().value()).as("HTTP status should be 200").isEqualTo(200);
    assertNotNull(response.getBody());
    assertThat(response.getBody().getNotificationCategory())
        .as("Type should be 'dummy-notification-category'")
        .isEqualTo("dummy-notification-category");
  }
}
