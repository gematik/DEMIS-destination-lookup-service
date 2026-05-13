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

import org.springframework.lang.NonNull;

/** Data Transfer Object for the responsible department. */
public class DepartmentDTO {
  /** The responsible department (Health Office). */
  private @NonNull String department;

  /** Constructs a DepartmentDTO with the given department. */
  public DepartmentDTO(@NonNull final String department) {
    this.department = department;
  }

  /**
   * @return the responsible department (Health Office)
   */
  public @NonNull String getDepartment() {
    return department;
  }

  /** Sets the responsible department (Health Office). */
  public void setDepartment(@NonNull final String department) {
    this.department = department;
  }
}
