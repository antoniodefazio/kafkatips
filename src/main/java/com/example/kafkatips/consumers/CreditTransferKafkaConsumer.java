package com.example.kafkatips.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.example.kafkatips.dtos.CreditTransferDto;
import com.example.kafkatips.dtos.CreditTransferOperationType;
import com.example.kafkatips.dtos.ExternalServerStatus;
import com.example.kafkatips.entities.ExactlyOnce;
import com.example.kafkatips.exceptions.DuplicateMessageException;
import com.example.kafkatips.services.IdempotentAPICaller;

@Component
public class CreditTransferKafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CreditTransferKafkaConsumer.class);

	@Autowired
	private IdempotentAPICaller idempotentAPICaller;

	private ExternalServerStatus currentContainerStatusSimulation = ExternalServerStatus.DOWN;

	@KafkaListener(groupId = "${kafka.consumer.group01}", topics = "${kafka.topic01}", containerFactory = "kafkaListenerContainerFactory")
	public boolean receive(@Header(KafkaHeaders.RECEIVED_PARTITION) int recPartition,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String recTopic, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
			@Header(KafkaHeaders.GROUP_ID) String group, @Header(KafkaHeaders.OFFSET) long offset,
			CreditTransferDto creditTransferDto) {
		logger.info("Received message={}", creditTransferDto);
		final ExactlyOnce exactlyonce = new ExactlyOnce();
		exactlyonce.setGroup(group);
		exactlyonce.setOffset(offset);
		exactlyonce.setRecTopic(recTopic);
		exactlyonce.setRecPartition(recPartition);
		exactlyonce.generateId();

		try {
			logger.info("Trying to send {} ", creditTransferDto);
			idempotentAPICaller.produce(exactlyonce, creditTransferDto);
		} catch (final DuplicateMessageException duplicateMessageException) {
			logger.warn("Duplicate message received {} ", duplicateMessageException.toString());

		}
		// whatever happens after is reprocessed but exact one sent
		logger.info("(If no duplicate) Commit and Sent to API REST message={}", creditTransferDto);
		if (shutDownUnexpectedly(creditTransferDto)) {
			currentContainerStatusSimulation = ExternalServerStatus.UP;
			throw new RuntimeException("shutDownUnexpectedly");

		}

		return true;
	}

	private boolean shutDownUnexpectedly(CreditTransferDto creditTransferDto) {
		return creditTransferDto.getOperationType() == CreditTransferOperationType.BlackListIn
				&& currentContainerStatusSimulation == ExternalServerStatus.DOWN;
	}

}
