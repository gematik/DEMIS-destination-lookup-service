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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import de.gematik.demis.destination.lookup.common.dto.NotificationDTO;
import de.gematik.demis.destination.lookup.service.writer.service.WriterService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DestinationLookupController {
  private final WriterService writerService;

  public DestinationLookupController(WriterService writerService) {
    this.writerService = writerService;
  }

  /**
   * Stores a notification in the database, if it does not already exist. In case the notificationId
   * is not a valid UUID, a 400 Bad Request response will be returned. In case the notification
   * already exists, the responsible department and last updated timestamp will be updated. In case
   * the notification already exists and the notificationCategory is not valid, a 400 Bad Request
   * response will be returned.
   *
   * @param notificationDTO the notification data to be stored
   * @return ResponseEntity with status 201 Created and the notification ID in the body in case of
   *     success, otherwise an appropriate error status
   */
  @PostMapping(
      path = "/notification",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> storeNotification(
      @NotNull @RequestBody final NotificationDTO notificationDTO) {
    final String notificationId = writerService.storeNotification(notificationDTO);
    return ResponseEntity.status(HttpStatus.OK).body(notificationId);
  }
}
