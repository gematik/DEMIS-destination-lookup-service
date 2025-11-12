package de.gematik.demis.destination.lookup.service.writer.service;

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

import de.gematik.demis.destination.lookup.common.dto.NotificationDTO;
import de.gematik.demis.destination.lookup.common.entity.Notification;
import de.gematik.demis.destination.lookup.common.exception.NotificationInvalidException;
import de.gematik.demis.destination.lookup.common.util.EntityConverter;
import de.gematik.demis.destination.lookup.service.writer.repository.DestinationLookupWriterRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WriterService {
  private final DestinationLookupWriterRepository writerRepository;

  public WriterService(DestinationLookupWriterRepository writerRepository) {
    this.writerRepository = writerRepository;
  }

  @Transactional
  public String storeNotification(final NotificationDTO notificationDTO) {
    final var entity = writerRepository.findById(UUID.fromString(notificationDTO.notificationId()));
    if (entity.isEmpty()) {
      log.info("Storing notificationDTO with ID {}", notificationDTO.notificationId());
      final Notification convertedEntity = EntityConverter.from(notificationDTO);
      final Notification savedNotification = writerRepository.save(convertedEntity);
      log.info("Stored notificationDTO with ID {}", savedNotification.getNotificationId());
      return String.valueOf(savedNotification.getNotificationId());
    }

    return updateNotification(entity.get(), notificationDTO);
  }

  private String updateNotification(
      final Notification entity, final NotificationDTO notificationDTO) {
    log.info("Updating notification with ID {}", entity.getNotificationId());

    final Integer changedEntity =
        writerRepository.updateNotification(
            entity.getNotificationId(),
            notificationDTO.responsibleDepartment(),
            EntityConverter.toOffsetDateTime(notificationDTO.lastUpdated()));

    if (changedEntity == 0) {
      final String message =
          String.format(
              "No notification has been changed with ID %s and category %s",
              entity.getNotificationId(), entity.getNotificationCategory());
      log.warn(message);
      throw new NotificationInvalidException(message);
    }

    log.info("Updated notification {} metadata successfully", entity.getNotificationId());
    return String.valueOf(entity.getNotificationId());
  }
}
