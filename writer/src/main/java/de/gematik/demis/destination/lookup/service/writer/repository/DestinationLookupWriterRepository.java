package de.gematik.demis.destination.lookup.service.writer.repository;

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

import de.gematik.demis.destination.lookup.common.entity.Notification;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationLookupWriterRepository extends JpaRepository<Notification, UUID> {

  /**
   * Update responsibleDepartment and lastUpdated by notificationId
   *
   * @param id the NotificationId
   * @param responsibleDepartment the new value for responsibleDepartment
   * @param lastUpdated the new value for lastUpdated
   * @return number of rows affected - should be 0 or 1
   */
  @Modifying(clearAutomatically = true)
  @Query(
      "UPDATE destination_lookup dl SET dl.responsibleDepartment = :responsibleDepartment,"
          + " dl.lastUpdated = :lastUpdated WHERE dl.notificationId = :id")
  Integer updateNotification(
      @Param("id") UUID id,
      @Param("responsibleDepartment") String responsibleDepartment,
      @Param("lastUpdated") OffsetDateTime lastUpdated);
}
