package de.gematik.demis.destination.lookup.repository;

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

import de.gematik.demis.destination.lookup.common.entity.Notification;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.Nullable;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class TestWithPostgresContainer {

  @Container
  protected static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
          .withDatabaseName("destination_lookup")
          .withUsername("postgres")
          .withPassword("postgres")
          .withInitScript("db/db-init.sql");

  @DynamicPropertySource
  protected static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.liquibase.user", () -> "destination_lookup_writer_ddl");
    registry.add("spring.liquibase.password", () -> "destination_lookup_writer_ddl");
  }

  protected static void executeSqls(String... sqlCmds) throws IOException, InterruptedException {
    for (String cmd : sqlCmds) {
      postgres.execInContainer(
          "psql", "--user", postgres.getUsername(), "-d", postgres.getDatabaseName(), "-c", cmd);
    }
  }

  protected static Notification prepareNotificationData() throws IOException, InterruptedException {
    return prepareNotificationData(null);
  }

  protected static Notification prepareNotificationData(@Nullable OffsetDateTime offsetDateTime)
      throws IOException, InterruptedException {
    Notification notification =
        new Notification(
            UUID.randomUUID(),
            "GA Berlin",
            "CVDP",
            offsetDateTime == null ? OffsetDateTime.now() : offsetDateTime);
    executeSqls(
        String.format(
            "INSERT INTO destination_lookup (notification_id, responsible_department, notification_category, last_updated) "
                + "VALUES ('%s', '%s', '%s', '%s');",
            notification.getNotificationId(),
            notification.getResponsibleDepartment(),
            notification.getNotificationCategory(),
            notification.getLastUpdated()));
    return notification;
  }
}
