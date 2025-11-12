package de.gematik.demis.destination.lookup.service.purger.service;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.gematik.demis.destination.lookup.common.entity.Notification;
import de.gematik.demis.destination.lookup.repository.TestWithPostgresContainer;
import de.gematik.demis.destination.lookup.service.purger.repository.DestinationLookupPurgerRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PurgerServiceSystemTest extends TestWithPostgresContainer {

  @Autowired private PurgerService purgerService;

  @Autowired private DestinationLookupPurgerRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  @Transactional
  @DisplayName("Should delete only old records marked for deletion")
  void shouldDeleteOnlyOldRecordsMarkedForDeletion() throws IOException, InterruptedException {
    // Given
    OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
    OffsetDateTime oldDate = now.minusDays(35); // Older than 30 days
    OffsetDateTime edgeCaseOneDayOlderDate = now.minusDays(31); // 31 Days old
    OffsetDateTime edgeCaseOneDayYoungerDate = now.minusDays(29); // 29 Days old
    OffsetDateTime edgeCaseFuture = now.plusDays(1); // 1 Day in the future
    OffsetDateTime recentDate = now.minusDays(20); // Within 30 days
    // Create test data
    Notification notificationRecent = prepareNotificationData(recentDate);
    Notification notificationEdgeCaseOneDayYounger =
        prepareNotificationData(edgeCaseOneDayYoungerDate);
    Notification edgeCaseFutureNotification = prepareNotificationData(edgeCaseFuture);
    prepareNotificationData(edgeCaseOneDayOlderDate);
    prepareNotificationData(oldDate);
    prepareNotificationData(oldDate.minusDays(5));

    repository.flush();

    // Verify initial count
    assertThat(repository.count()).isEqualTo(6);
    // When
    purgerService.performPurge();

    // Then
    List<Notification> remainingNotifications = repository.findAll();
    assertThat(remainingNotifications).hasSize(3);

    // Verify correct records were deleted
    List<String> remainingIds =
        remainingNotifications.stream()
            .map(Notification::getNotificationId)
            .map(String::valueOf)
            .toList();

    assertThat(remainingIds)
        .hasSize(3)
        .containsExactlyInAnyOrder(
            notificationRecent.getNotificationId().toString(),
            notificationEdgeCaseOneDayYounger.getNotificationId().toString(),
            edgeCaseFutureNotification.getNotificationId().toString());
  }

  @Test
  @Transactional
  @DisplayName("Should delete nothing when no records meet criteria")
  void shouldDeleteNothingWhenNoRecordsMeetCriteria() throws IOException, InterruptedException {
    // Given
    OffsetDateTime recentDate = OffsetDateTime.now().minusDays(10);
    OffsetDateTime oneDayBeforeDate = OffsetDateTime.now().minusDays(29);
    Notification notification1 = prepareNotificationData(recentDate);
    Notification notification2 = prepareNotificationData(oneDayBeforeDate);

    repository.flush();

    // When
    purgerService.performPurge();
    List<Notification> remainingNotifications = repository.findAll();
    assertThat(repository.count()).isEqualTo(2);

    // Then

    List<String> remainingIds =
        remainingNotifications.stream()
            .map(Notification::getNotificationId)
            .map(String::valueOf)
            .toList();

    assertThat(remainingIds)
        .containsExactlyInAnyOrder(
            notification1.getNotificationId().toString(),
            notification2.getNotificationId().toString());
  }

  @Test
  @DisplayName("Should handle empty database gracefully")
  void shouldHandleEmptyDatabaseGracefully() {
    // Given - empty database
    assertThat(repository.count()).isZero();

    // When
    assertThatNoException().isThrownBy(() -> purgerService.performPurge());

    // Then
    assertThat(repository.count()).isZero();
  }

  @Test
  @Transactional
  @DisplayName("Should respect exact retention boundary")
  void shouldRespectExactRetentionBoundary() throws IOException, InterruptedException {
    // Given
    OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
    OffsetDateTime exactBoundary = now.minusDays(30);
    OffsetDateTime justBefore = exactBoundary.minusDays(1);
    OffsetDateTime justAfter = exactBoundary.plusDays(1);

    prepareNotificationData(justBefore);
    Notification notificationJustAfter = prepareNotificationData(justAfter);

    // When
    purgerService.performPurge();
    repository.flush();

    // Then
    List<Notification> remaining = repository.findAll();
    assertThat(remaining).hasSize(1);

    List<String> remainingIds =
        remaining.stream().map(Notification::getNotificationId).map(String::valueOf).toList();

    assertThat(remainingIds)
        .containsExactlyInAnyOrder(String.valueOf(notificationJustAfter.getNotificationId()));
  }

  @Test
  @Transactional
  @DisplayName("Should be transactional and rollback on exception")
  void shouldBeTransactionalAndRollbackOnException() throws IOException, InterruptedException {
    // Given
    OffsetDateTime oldDate = OffsetDateTime.now(ZoneId.systemDefault()).minusDays(35);

    prepareNotificationData(oldDate);

    // Mock the repository to throw an exception
    DestinationLookupPurgerRepository mockRepository = spy(repository);
    when(mockRepository.deleteNotificationsByLastUpdated(any(OffsetDateTime.class)))
        .thenThrow(new RuntimeException("Database error"));

    PurgerService purgerServiceWithMockRepo = new PurgerService(mockRepository, 30);

    // When & Then
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(purgerServiceWithMockRepo::performPurge);

    // Verify data is still there (transaction rolled back)
    assertThat(mockRepository.count()).isEqualTo(1);
  }

  @Test
  @Transactional
  @DisplayName("Should work with different retention periods")
  void shouldWorkWithDifferentRetentionPeriods() throws IOException, InterruptedException {
    // Given - Service with 10 days retention instead of 30
    PurgerService shortRetentionService = new PurgerService(repository, 10);

    OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
    OffsetDateTime fifteenDaysAgo = now.minusDays(15);
    OffsetDateTime fiveDaysAgo = now.minusDays(5);

    prepareNotificationData(fifteenDaysAgo);
    Notification notificationFiveDaysAgo = prepareNotificationData(fiveDaysAgo);

    // When
    shortRetentionService.performPurge();

    // Then
    List<Notification> remaining = repository.findAll();
    assertThat(remaining).hasSize(1);
    assertThat(remaining.getFirst().getNotificationId())
        .asString()
        .isEqualTo(String.valueOf(notificationFiveDaysAgo.getNotificationId()));
  }
}
