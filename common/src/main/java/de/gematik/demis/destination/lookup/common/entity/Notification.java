package de.gematik.demis.destination.lookup.common.entity;

/*-
 * #%L
 * destination-lookup-common
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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/** Entity representing a notification lookup entry in the database. */
@Entity(name = "destination_lookup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destination_lookup")
public class Notification {
  /** The notification ID (primary key). */
  @Id
  @Column(name = "notification_id")
  private @NonNull UUID notificationId;

  /** The responsible department (Health Office). */
  @Column(name = "responsible_department")
  private @NonNull String responsibleDepartment;

  /** The notification category (type). */
  @Column(name = "notification_category")
  private @NonNull String notificationCategory;

  /** The timestamp of the last update. */
  @Column(name = "last_updated")
  private @NonNull OffsetDateTime lastUpdated;
}
