package de.gematik.demis.destination.lookup.service.reader.exception;

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

import de.gematik.demis.destination.lookup.common.exception.NotificationNotFoundException;
import de.gematik.demis.destination.lookup.common.exception.RestExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/** Unit test for RestExceptionHandler. */
class RestExceptionHandlerTest {

  /**
   * Verifies that handleNotificationNotFound returns a 404 status and the correct error message.
   */
  @Test
  void handleNotificationNotFound_returns404AndMessage() {
    RestExceptionHandler controller = new RestExceptionHandler();
    NotificationNotFoundException ex = new NotificationNotFoundException("Notification not found");
    ResponseEntity<String> response = controller.handleNotificationNotFound(ex);
    assertThat(response.getStatusCode().value()).as("HTTP status should be 404").isEqualTo(404);
    assertThat(response.getBody())
        .as("Error message should match")
        .isEqualTo("Notification not found");
  }
}
