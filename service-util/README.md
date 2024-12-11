# com.cmile.common.service-util

## Overview
The com.cmile.common.service-util library provides utilities for working with various services and features. 
Each feature includes classes prefixed with Cfg for easy configuration. Below are instructions for setting 
up and using different utilities in your projects.

## Usage
To incorporate a feature, include its respective configuration class (e.g., CfgPostgres) in your main configuration class, 
then follow the setup instructions for that feature.

### BigQuery

To use the BigQuery utilities, you need to configure and instantiate the `BigQueryService` and `BigQueryProvisionService` classes.

- **BigQueryService**: Provides methods for querying and importing data into BigQuery.
  - [BigQueryService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryService.java)
  - [BigQueryServiceImpl.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryServiceImpl.java)

- **BigQueryProvisionService**: Provides methods for managing BigQuery datasets and tables.
  - [BigQueryProvisionService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryProvisionService.java)
  - [BigQueryProvisionServiceImpl.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryProvisionServiceImpl.java)

### MongoDB

To use the MongoDB utilities, you need to configure and instantiate the `DynamicMongoTemplate`, `MongoCacheManager`, and `AtlasMongoDbService` classes.

- **DynamicMongoTemplate**: Provides dynamic MongoDB template instances based on the current context.
  - [DynamicMongoTemplate.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/mongo/DynamicMongoTemplate.java)

- **MongoCacheManager**: Manages caching of MongoDB connection details.
  - [MongoCacheManager.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/mongo/MongoCacheManager.java)

- **AtlasMongoDbService**: Service for managing MongoDB collections and users in MongoDB Atlas.
  - [AtlasMongoDbService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/mongo/AtlasMongoDbService.java)
  
### API Invoker

To use the API invoker utilities, you need to configure and instantiate the relevant classes for making API calls.

- **ApiInvokerService**: Service for invoking external APIs.
  - [ApiInvokerService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/apiinvoker/ApiInvokerService.java)

### Metrics

To use the metrics utilities, you need to configure and instantiate the relevant classes for collecting and reporting metrics.

- **MetricsService**: Service for collecting and reporting application metrics.
  - [MetricsService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/metrics/MetricsService.java)

### PostgreSQL

To use the PostgreSQL utilities, you need to configure and instantiate the `PostgresCacheManager` and `LiquibaseService` classes.

- **PostgresCacheManager**: Manages caching of PostgreSQL connection details.
  - [PostgresCacheManager.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/sqlconnection/PostgresCacheManager.java)

- **LiquibaseService**: Service for managing database migrations using Liquibase.
  - [LiquibaseService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/sqlconnection/migration/LiquibaseService.java)

### GCP Secret Manager

To use the GCP Secret Manager utilities, you need to configure and instantiate the `SecretManagerService` class.

- **SecretManagerService**: Service for managing secrets in GCP Secret Manager.
  - [SecretManagerService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/secret/SecretManagerService.java)

### GCP Storage

To use the GCP Storage utilities, you need to configure and instantiate the `GcpCloudStorageService` class.

- **GcpCloudStorageService**: Service for managing files in GCP Cloud Storage.
  - [GcpCloudStorageService.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/storage/GcpCloudStorageService.java)

### Logging

To use the logging utilities, you need to configure and instantiate the `CfgLogging` class.

- **CfgLogging**: Configuration class for setting up logging.
  - [CfgLogging.java](lib-common/service-util/src/main/java/com/cmile/serviceutil/logging/CfgLogging.java)

## Contributing

We welcome contributions to this project. Please follow the guidelines below to contribute:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Write your code and tests.
4. Ensure all tests pass.
5. Submit a pull request.

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.