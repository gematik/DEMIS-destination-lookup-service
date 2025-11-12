package de.gematik.demis.destination.lookup.service.purger.repository;

/*-
 * #%L
 * destination-lookup-purger
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
public interface DestinationLookupPurgerRepository extends JpaRepository<Notification, UUID> {
  /**
   * Performs the deletion of all notifications that have a lastUpdated timestamp older than the
   * specified instant.
   *
   * @param retentionPeriod the cutoff retention Period; notifications with lastUpdated before this
   *     will be deleted
   * @return the number of notifications deleted
   */
  @Modifying
  @Query("DELETE FROM destination_lookup dl WHERE dl.lastUpdated < :retentionPeriod")
  Integer deleteNotificationsByLastUpdated(
      @Param("retentionPeriod") OffsetDateTime retentionPeriod);
}
