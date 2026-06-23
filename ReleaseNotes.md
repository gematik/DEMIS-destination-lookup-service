<img align="right" width="250" height="47" src="media/Gematik_Logo_Flag.png"/> <br/> 
 
# Release notes
## Release 1.3.0
- Added prometheus pod annotations to helm charts
- arranged jvm options and resource limits
- optimized custom environment variables handling in helm chart
- updated docker base image to gematik1/osadl-alpine-openjdk25-jre:1.0.5
- updated to spring-boot 4

## Release 1.2.0 
- updated base-image and updated from java 21 to java 25

## Release 1.1.0 
- Removed istio helm chart
- Updated Libraries (incl. Spring-Parent)

## Release 1.0.0
- Initial implementation of reader, writer and purger services
- definition of schema
- setup DB liquibase scripts 
- tests for all services
- Setup of CI/CD pipelines
