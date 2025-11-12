# DestinationLookupControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getNotificationType**](DestinationLookupControllerApi.md#getNotificationType) | **GET** /notification/{notificationId}/notificationCategory |  |
| [**getResponsibleDepartment**](DestinationLookupControllerApi.md#getResponsibleDepartment) | **GET** /notification/{notificationId}/department |  |


<a name="getNotificationType"></a>
# **getNotificationType**
> NotificationCategoryDTO getNotificationType(notificationId)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **notificationId** | **UUID**|  | [default to null] |

### Return type

[**NotificationCategoryDTO**](../Models/NotificationCategoryDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getResponsibleDepartment"></a>
# **getResponsibleDepartment**
> DepartmentDTO getResponsibleDepartment(notificationId)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **notificationId** | **UUID**|  | [default to null] |

### Return type

[**DepartmentDTO**](../Models/DepartmentDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

