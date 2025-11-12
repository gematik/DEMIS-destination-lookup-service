package de.gematik.demis.destination.lookup.service.reader.api;

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

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.gematik.demis.destination.lookup.common.dto.DepartmentDTO;
import de.gematik.demis.destination.lookup.common.dto.NotificationCategoryDTO;
import de.gematik.demis.destination.lookup.common.exception.NotificationNotFoundException;
import de.gematik.demis.destination.lookup.service.reader.service.DestinationLookupService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(DestinationLookupController.class)
class DestinationLookupControllerIT {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private DestinationLookupService destinationLookupService;

  private static final String DEPARTMENT_ENDPOINT_FMT = "/notification/%s/department";
  private static final String CATEGORY_ENDPOINT_FMT = "/notification/%s/notificationCategory";
  private static final UUID VALID_NOTIFICATION_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000123");
  private static final UUID INVALID_NOTIFICATION_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000456");

  public static MockHttpServletRequestBuilder jsonRequest(String url) {
    return MockMvcRequestBuilders.get(url)
        .header("Content-Type", APPLICATION_JSON_VALUE)
        .header("Accept", APPLICATION_JSON_VALUE);
  }

  @BeforeEach
  void setup() {
    // Mock positive responses
    when(destinationLookupService.findResponsibleDepartment(VALID_NOTIFICATION_ID))
        .thenReturn(new DepartmentDTO("dummy-department"));
    when(destinationLookupService.findNotificationCategory(VALID_NOTIFICATION_ID))
        .thenReturn(new NotificationCategoryDTO("dummy-notification-category"));
    // Mock negative responses
    when(destinationLookupService.findResponsibleDepartment(INVALID_NOTIFICATION_ID))
        .thenThrow(new NotificationNotFoundException("Department not found"));
    when(destinationLookupService.findNotificationCategory(INVALID_NOTIFICATION_ID))
        .thenThrow(new NotificationNotFoundException("Notification category not found"));
  }

  @Test
  void getResponsibleDepartment_endpointReturnsDepartment() throws Exception {
    mockMvc
        .perform(jsonRequest(String.format(DEPARTMENT_ENDPOINT_FMT, VALID_NOTIFICATION_ID)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.department").value("dummy-department"));
  }

  @Test
  void getNotificationType_endpointReturnsNotificatioNCategory() throws Exception {
    mockMvc
        .perform(jsonRequest(String.format(CATEGORY_ENDPOINT_FMT, VALID_NOTIFICATION_ID)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notificationCategory").value("dummy-notification-category"));
  }

  @Test
  void getResponsibleDepartment_endpointReturnsNotFound() throws Exception {
    mockMvc
        .perform(jsonRequest(String.format(DEPARTMENT_ENDPOINT_FMT, INVALID_NOTIFICATION_ID)))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department not found"));
  }

  @Test
  void getNotificationType_endpointReturnsNotFound() throws Exception {
    mockMvc
        .perform(jsonRequest(String.format(CATEGORY_ENDPOINT_FMT, INVALID_NOTIFICATION_ID)))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Notification category not found"));
  }
}
