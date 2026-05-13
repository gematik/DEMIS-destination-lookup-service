package de.gematik.demis.destination.lookup.common.entity;

/*-
 * #%L
 * destination-lookup-common
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

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class NotificationTest {
  @Test
  void testGetterAndSetter() {
    final var notificationId = UUID.randomUUID();
    Notification notification = new Notification();
    notification.setNotificationId(notificationId);
    notification.setResponsibleDepartment("GA Berlin");
    notification.setNotificationCategory("CVDP");
    OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
    notification.setLastUpdated(now);

    assertThat(notification.getNotificationId())
        .as("NotificationId should be '" + notificationId + "'")
        .isEqualTo(notificationId);
    assertThat(notification.getResponsibleDepartment())
        .as("ResponsibleDepartment should be 'GA Berlin'")
        .isEqualTo("GA Berlin");
    assertThat(notification.getNotificationCategory())
        .as("NotificationCategory should be 'CVDP'")
        .isEqualTo("CVDP");
    assertThat(notification.getLastUpdated())
        .as("LastUpdated should match the expected timestamp")
        .isEqualTo(now);
  }
}
