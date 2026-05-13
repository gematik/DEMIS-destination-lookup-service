package de.gematik.demis.destination.lookup.common.dto;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Data Transfer Object for Notification entity.
 *
 * @param notificationId the notification ID
 * @param responsibleDepartment the responsible department (Health Office) that handles the
 *     notification
 * @param notificationCategory the type of the notification (the pathogen/disease code)
 * @param lastUpdated the timestamp of the last update in ISO 8601 format as String
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record NotificationDTO(
    String notificationId,
    String responsibleDepartment,
    String notificationCategory,
    String lastUpdated) {}
