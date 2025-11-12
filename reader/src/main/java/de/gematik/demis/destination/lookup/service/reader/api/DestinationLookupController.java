package de.gematik.demis.destination.lookup.service.reader.api;

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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import de.gematik.demis.destination.lookup.common.dto.DepartmentDTO;
import de.gematik.demis.destination.lookup.common.dto.NotificationCategoryDTO;
import de.gematik.demis.destination.lookup.service.reader.service.DestinationLookupService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class DestinationLookupController {

  private final DestinationLookupService destinationLookupService;

  public DestinationLookupController(DestinationLookupService destinationLookupService) {
    this.destinationLookupService = destinationLookupService;
  }

  @GetMapping(
      path = "/{notificationId}/department",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DepartmentDTO> getResponsibleDepartment(
      @NonNull @PathVariable final UUID notificationId) {
    DepartmentDTO departmentDto =
        destinationLookupService.findResponsibleDepartment(notificationId);
    return ResponseEntity.ok(departmentDto);
  }

  @GetMapping(
      path = "/{notificationId}/notificationCategory",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<NotificationCategoryDTO> getNotificationType(
      @NonNull @PathVariable final UUID notificationId) {
    NotificationCategoryDTO notificationCategoryDTO =
        destinationLookupService.findNotificationCategory(notificationId);
    return ResponseEntity.ok(notificationCategoryDTO);
  }
}
