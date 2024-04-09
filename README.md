https://www.linkedin.com/in/antoniodefazio-java-python-developer-sviluppatore-trainer-docente

Idempotence of Kafka consumers provided by at-least-once message delivery semantics


I worked for the most important Italian telecommunications company, the manager told me that we had to manage the telephone top-up system by transferring the remaining credit. These top-ups come from various apps and servers and must flow into the central system via a message broker. 

The events must be processed respecting the order of arrival and the charging is concretely finalized with a call to a REST API (external system) containing the data in the broker's record. 

I started with the in-depth questions of the task and pointed out the fact that the record is considered processed/consumed (fro example org.springframework.kafka.support.Acknowledgment.acknowledge() ) if and only the call to the API has been successfully sent ( http response status ok). I also pointed out to the manager that in a distributed and containerized environment like Kubernetes(we deployed on GKE) it is possible that a consumer deployed as a pod could be shut down unexpectedly due to various reasons such as pod evictions, node failures, or deployments. As a result, the same broker's record may be processed multiple times, potentially causing duplicate processing and, in our case, duplicate calls to the API.

Therefore, better outline the scenario (use case) with the following constraint:

- each broker's record generates the call to the API that actually carries out the recharge, this call obviously cannot be made twice for the same record

I choose to use Apache Kafka as a message broker, with TLS and authentication, to rely on a transactional database (MySQL) to implement the idempotency of the Kafka consumer via Exactly-Once Semantics.
 
To create exceptional situations (shutDownUnexpectedly and externalServerError) you need to ping the /dummyProducerCrash API, the second message generates the exceptions. Look at logs to understand the flow and how the problem is fixed. Check the database status also at http://localhost:8080/h2-console after the call.

I apologize in advance for the antipatterns in the project and for the repeated code but this is a simulation in which a specific problem is focused on and solved. Non-use of Lombok and Java records is desired.

See the next article to see the secured Kafka and Kafka Streams!!
