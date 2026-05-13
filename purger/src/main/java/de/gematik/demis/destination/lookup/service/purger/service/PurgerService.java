package de.gematik.demis.destination.lookup.service.purger.service;

/*-
 * #%L
 * destination-lookup-purger
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

import de.gematik.demis.destination.lookup.service.purger.repository.DestinationLookupPurgerRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PurgerService {
  private final DestinationLookupPurgerRepository purgerRepository;
  private final int retentionDays;

  public PurgerService(
      DestinationLookupPurgerRepository purgerRepository,
      @Value("${purger.retention.days}") int retentionDays) {
    this.purgerRepository = purgerRepository;
    this.retentionDays = retentionDays;
  }

  @Transactional
  public void performPurge() {
    final long startMillis = System.currentTimeMillis();
    log.info("Performing purge");
    final OffsetDateTime retentionPeriod =
        OffsetDateTime.now(ZoneId.systemDefault()).minusDays(retentionDays);
    final int deletedCount = purgerRepository.deleteNotificationsByLastUpdated(retentionPeriod);
    final long endMillis = System.currentTimeMillis();
    log.info(
        "Purge completed in {} ms. Deleted {} records marked for deletion older than {}.",
        (endMillis - startMillis),
        deletedCount,
        retentionPeriod);
  }
}
