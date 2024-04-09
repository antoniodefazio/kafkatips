package com.example.kafkatips.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.kafkatips.dtos.CreditTransferDto;

@EnableKafka
@Configuration
public class KafkaListenersConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(KafkaListenersConfiguration.class);

	@Value("${kafka.consumer.REQUEST_TIMEOUT_MS_CONFIG}")
	private int REQUEST_TIMEOUT_MS_CONFIG;
	@Value("${kafka.consumer.MAX_POLL_RECORDS_CONFIG}")
	private int MAX_POLL_RECORDS_CONFIG;
	@Value("${kafka.consumer.MAX_POLL_INTERVAL_MS_CONFIG}")
	private int MAX_POLL_INTERVAL_MS_CONFIG;
	@Value("${kafka.consumer.ENABLE_AUTO_COMMIT_CONFIG}")
	private boolean ENABLE_AUTO_COMMIT_CONFIG;
	@Value("${kafka.consumer.AUTO_COMMIT_INTERVAL_MS_CONFIG}")
	private int AUTO_COMMIT_INTERVAL_MS_CONFIG;
	@Value("${kafka.consumer.AUTO_OFFSET_RESET_CONFIG}")
	private String AUTO_OFFSET_RESET_CONFIG;
	@Value("${kafka.servers}")
	private String servers;

	@Bean
	public ConsumerFactory<String, CreditTransferDto> consumerFactory() throws ClassNotFoundException {
		final Map<String, Object> configMap = extractConsumerConfigMap();
		return new DefaultKafkaConsumerFactory<>(configMap, new StringDeserializer(),
				new JsonDeserializer<>(CreditTransferDto.class));

	}

	private Map<String, Object> extractConsumerConfigMap() throws ClassNotFoundException {
		final Map<String, Object> configMap = new HashMap<>();

		configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				org.apache.kafka.common.serialization.StringDeserializer.class);
		configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				org.apache.kafka.common.serialization.StringDeserializer.class);
		configMap.putAll(kafkaServerConfig());

		return configMap;
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {

		return new KafkaAdmin(kafkaServerConfig());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, CreditTransferDto> kafkaListenerContainerFactory(
			ConsumerFactory<String, CreditTransferDto> consumerFactory) {
		final ConcurrentKafkaListenerContainerFactory<String, CreditTransferDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}

	@Bean(name = "kafkaServerConfig")
	public Map<String, Object> kafkaServerConfig() {
		final Map<String, Object> configs = new HashMap<>();

		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);

		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

		configs.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, REQUEST_TIMEOUT_MS_CONFIG);
		configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS_CONFIG);

		configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS_CONFIG);
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT_CONFIG);
		configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, AUTO_COMMIT_INTERVAL_MS_CONFIG);
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_CONFIG);

		return configs;
	}

}
