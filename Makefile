.PHONY: build run clean package
build:
	mvn -q -e -DskipTests -pl fleetwave-web -am package
run:
	SPRING_PROFILES_ACTIVE=dev mvn -q -pl fleetwave-web spring-boot:run
package:
	mvn -q -DskipTests -pl fleetwave-web -am package
clean:
	mvn -q clean
