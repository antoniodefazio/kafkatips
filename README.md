https://www.linkedin.com/in/antoniodefazio-java-python-developer-sviluppatore-trainer-docente

I worked for the most important Italian telecommunications company, the manager told me that we had to manage the telephone top-up system by transferring the remaining credit. These top-ups come from various apps and servers and must flow into the central system via a message broker. The events must be processed respecting the order of arrival and the charging is concretely finalized with a call to a REST API (external system). I start with the in-depth questions of the task and point out the fact that the record is considered processed/consumed (org.springframework.kafka.support.Acknowledgment.acknowledge() ) if and only the call to the API has been successfully sent ( http response status ok). I also point out to the manager that in a distributed and containerized environment like Kubernetes it is possible that a consumer deployed as a pod could be shut down unexpectedly due to various reasons such as pod evictions, node failures, or deployments. As a result, the same Kafka record may be processed multiple times, potentially causing duplicate processing.

Therefore, better outline the scenario (use case) with the following constraint:

- each record generates the call to the API that actually carries out the recharge, this call obviously cannot be made twice for the same record

I therefore choose to use Apache Kafka as a message broker, with TLS and authentication, to rely on a transactional database (MySQL) to implement the idempotency of the Kafka consumer via Exactly-Once Semantics.
 
To create exceptional situations (shutDownUnexpectedly and externalServerError) you need to ping the /dummyProducerCrash API, the second message generates the exceptions, and then note from the logs that if the consumer crashes before notifying the Kafka broker, the message is appropriately considered duplicated therefore the API is not called, furthermore if something goes wrong during the call to the external service the record is considered to be reprocessed while waiting for the external service to be restored. Check the database status also at http://localhost:8080/h2-console after the call.

I apologize in advance for the antipatterns in the project and for the repeated code but this is a simulation in which a specific problem is focused on and solved. Non-use of Lombok and Java records is desired.

See the next article to see the secured Kafka and Kafka Streams!!
