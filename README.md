# lib-common

The `lib-common` project provides a comprehensive set of utilities and a test framework for interacting with multiple external services. These services include PostgreSQL, MongoDB, GCP Secret Manager, BigQuery, GCP Storage, and logging. The project is structured to facilitate easy integration and testing of these services in various applications.

## Project Structure

### service-util

The `service-util` directory contains the main utilities and service implementations. Below is a detailed description of the key components:

#### BigQuery

- **BigQueryService**: Interface defining methods for querying and importing data into BigQuery.
  - [BigQueryService.java](service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryService.java)

- **BigQueryServiceImpl**: Implementation of `BigQueryService` providing methods to execute queries and import data.
  - [BigQueryServiceImpl.java](service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryServiceImpl.java)

- **BigQueryProvisionService**: Interface defining methods for managing BigQuery datasets and tables.
  - [BigQueryProvisionService.java](service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryProvisionService.java)

- **BigQueryProvisionServiceImpl**: Implementation of `BigQueryProvisionService` providing methods to create and delete datasets and tables.
  - [BigQueryProvisionServiceImpl.java](service-util/src/main/java/com/cmile/serviceutil/bigquery/BigQueryProvisionServiceImpl.java)

- **CfgBigQuery**: Configuration class for setting up BigQuery services.
  - [CfgBigQuery.java](service-util/src/main/java/com/cmile/serviceutil/bigquery/CfgBigQuery.java)

#### MongoDB

- **DynamicMongoTemplate**: Provides dynamic MongoDB template instances based on the current context.
  - [DynamicMongoTemplate.java](service-util/src/main/java/com/cmile/serviceutil/mongo/DynamicMongoTemplate.java)

- **MongoCacheManager**: Manages caching of MongoDB connection details.
  - [MongoCacheManager.java](service-util/src/main/java/com/cmile/serviceutil/mongo/MongoCacheManager.java)

- **AtlasMongoDbService**: Service for managing MongoDB collections and users in MongoDB Atlas.
  - [AtlasMongoDbService.java](service-util/src/main/java/com/cmile/serviceutil/mongo/AtlasMongoDbService.java)

#### PostgreSQL

- **PostgresCacheManager**: Manages caching of PostgreSQL connection details.
  - [PostgresCacheManager.java](service-util/src/main/java/com/cmile/serviceutil/sqlconnection/PostgresCacheManager.java)

- **LiquibaseService**: Service for managing database migrations using Liquibase.
  - [LiquibaseService.java](service-util/src/main/java/com/cmile/serviceutil/sqlconnection/migration/LiquibaseService.java)

#### GCP Secret Manager

- **SecretManagerService**: Service for managing secrets in GCP Secret Manager.
  - [SecretManagerService.java](service-util/src/main/java/com/cmile/serviceutil/secret/SecretManagerService.java)

- **SecretTypeEnum**: Enum defining different types of secrets.
  - [SecretTypeEnum.java](service-util/src/main/java/com/cmile/serviceutil/secret/SecretTypeEnum.java)

- **SpaceSecretEntity**: Entity representing secrets for a specific space.
  - [SpaceSecretEntity.java](service-util/src/main/java/com/cmile/serviceutil/secret/entity/SpaceSecretEntity.java)

- **ServiceSecretEntity**: Entity representing secrets for a specific service.
  - [ServiceSecretEntity.java](service-util/src/main/java/com/cmile/serviceutil/secret/entity/ServiceSecretEntity.java)

#### GCP Storage

- **GcpCloudStorageService**: Service for managing files in GCP Cloud Storage.
  - [GcpCloudStorageService.java](service-util/src/main/java/com/cmile/serviceutil/storage/GcpCloudStorageService.java)

#### Logging

- **CfgLogging**: Configuration class for setting up logging.
  - [CfgLogging.java](service-util/src/main/java/com/cmile/serviceutil/logging/CfgLogging.java)

### test-common-util

The `test-common-util` directory contains utilities and configurations for testing the services provided in `service-util`.

- **CfgPostgresTest**: Configuration class for setting up PostgreSQL tests.
  - [CfgPostgresTest.java](test-common-util/src/main/java/com/cmile/testutil/CfgPostgresTest.java)

- **CfgMongoTest**: Configuration class for setting up MongoDB tests.
  - [CfgMongoTest.java](test-common-util/src/main/java/com/cmile/testutil/CfgMongoTest.java)

- **CfgSecretTest**: Configuration class for setting up GCP Secret Manager tests.
  - [CfgSecretTest.java](test-common-util/src/main/java/com/cmile/testutil/CfgSecretTest.java)

- **CfgPubSubTest**: Configuration class for setting up Pub/Sub tests.
  - [CfgPubSubTest.java](test-common-util/src/main/java/com/cmile/testutil/CfgPubSubTest.java)

- **GcpCloudStorageServiceTest**: Test class for `GcpCloudStorageService`.
  - [GcpCloudStorageServiceTest.java](test-common-util/src/test/java/com/cmile/serviceutil/storage/GcpCloudStorageServiceTest.java)

## Getting Started

To get started with this project, clone the repository and build the project using Maven:

```sh
git clone <repository-url>
cd lib-common
mvn clean install
