package de.gematik.demis.destination.lookup.common.exception;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/** Handles different types of Exceptions */
@ControllerAdvice
public class RestExceptionHandler {

  /**
   * Handles NotificationNotFoundException and returns a 404 Not Found response with the error
   * message.
   *
   * @param ex the thrown NotificationNotFoundException
   * @return ResponseEntity with error message and 404 status
   */
  @ExceptionHandler(NotificationNotFoundException.class)
  @ResponseBody
  public ResponseEntity<String> handleNotificationNotFound(NotificationNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  /**
   * Handles IllegalArgumentException and returns a 400 Bad Request with the error message.
   *
   * @param ex the thrown IllegalArgumentException
   * @return ResponseEntity with error message and 400 status
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseBody
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  /**
   * Handles NotificationInvalidException and returns a 422 Unprocessable Entity with the error
   * message.
   *
   * @param ex the thrown NotificationNotFoundException
   * @return ResponseEntity with error message and 404 status
   */
  @ExceptionHandler(NotificationInvalidException.class)
  @ResponseBody
  public ResponseEntity<String> handleNotificationInvalid(NotificationInvalidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
