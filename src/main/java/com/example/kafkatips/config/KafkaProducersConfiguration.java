package com.example.kafkatips.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.example.kafkatips.dtos.CreditTransferDto;

@EnableKafka
@Configuration
public class KafkaProducersConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducersConfiguration.class);

	@Value("${kafka.producer.REQUEST_TIMEOUT_MS_CONFIG}")
	private String REQUEST_TIMEOUT_MS_CONFIG;
	@Value("${kafka.producer.ENABLE_IDEMPOTENCE_CONFIG}")
	private boolean ENABLE_IDEMPOTENCE_CONFIG;
	@Value("${kafka.producer.RETRIES_CONFIG}")
	private String RETRIES_CONFIG;
	@Value("${kafka.producer.RETRY_BACKOFF_MS_CONFIG}")
	private int RETRY_BACKOFF_MS_CONFIG;
	@Value("${kafka.producer.BATCH_SIZE_CONFIG}")
	private int BATCH_SIZE_CONFIG;
	@Value("${kafka.producer.LINGER_MS_CONFIG}")
	private int LINGER_MS_CONFIG;
	@Value("${kafka.producer.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION}")
	private int MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION;
	@Value("${kafka.producer.BUFFER_MEMORY_CONFIG}")
	private int BUFFER_MEMORY_CONFIG;
	@Value("${kafka.producer.MAX_BLOCK_MS_CONFIG}")
	private int MAX_BLOCK_MS_CONFIG;
	@Value("${kafka.producer.MAX_REQUEST_SIZE_CONFIG}")
	private int MAX_REQUEST_SIZE_CONFIG;
	@Value("${kafka.producer.ACKS_CONFIG}")
	private String ACKS_CONFIG;
	@Value("${kafka.producer.COMPRESSION_TYPE_CONFIG}")
	private String COMPRESSION_TYPE_CONFIG;

	private Map<String, Object> extractConfigMap(Map<String, Object> kafkaServerConfig) throws ClassNotFoundException {
		final Map<String, Object> configMap = new HashMap<>();
		// configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);

		configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				org.apache.kafka.common.serialization.StringSerializer.class);
		configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				org.springframework.kafka.support.serializer.JsonSerializer.class);
		configMap.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG);

		configMap.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE_CONFIG);
		int retryConfig = Integer.MAX_VALUE;
		try {
			retryConfig = Integer.parseInt(RETRIES_CONFIG);
		} catch (final Exception ex) {
			logger.info("max value set for RETRIES_CONFIG ");
		}
		configMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, REQUEST_TIMEOUT_MS_CONFIG);
		configMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, ENABLE_IDEMPOTENCE_CONFIG);
		configMap.put(ProducerConfig.RETRIES_CONFIG, retryConfig);
		configMap.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, RETRY_BACKOFF_MS_CONFIG);
		configMap.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE_CONFIG);
		configMap.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_MS_CONFIG);
		configMap.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION);
		configMap.put(ProducerConfig.BUFFER_MEMORY_CONFIG, BUFFER_MEMORY_CONFIG);
		configMap.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, MAX_BLOCK_MS_CONFIG);
		configMap.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, MAX_REQUEST_SIZE_CONFIG);

		kafkaServerConfig = (Map<String, Object>) kafkaServerConfig.get("kafkaServerConfig");
		configMap.putAll(kafkaServerConfig);
		return configMap;
	}

	@Bean
	public KafkaTemplate<String, CreditTransferDto> kafkaTemplate06(
			ProducerFactory<String, CreditTransferDto> producerFactoryItemDto) throws ClassNotFoundException {
		return new KafkaTemplate<>(producerFactoryItemDto);
	}

	@Bean
	public ProducerFactory<String, CreditTransferDto> producerFactory01(
			@Qualifier("kafkaServerConfig") Map<String, Object> kafkaServerConfig) throws ClassNotFoundException {
		final Map<String, Object> configMap = extractConfigMap(kafkaServerConfig);
		return new DefaultKafkaProducerFactory<>(configMap);
	}

}
