package com.example.kafkatips.restapi;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafkatips.dtos.CreditTransferDto;
import com.example.kafkatips.dtos.CreditTransferOperationType;

@RestController
public class DummyKafkaController {
	private static final Logger logger = LoggerFactory.getLogger(DummyKafkaController.class);

	@Value("${kafka.topic01}")
	private String topic01;

	@Autowired
	private KafkaTemplate<String, CreditTransferDto> kafkaTemplate1;

	@GetMapping(value = "/dummyProducer")
	public ResponseEntity<String> dummyProducer01() throws URISyntaxException {
		final CreditTransferDto creditTransferDto1 = CreditTransferDto.builder()
				.withPhoneNumberDonor("PhoneNumberDonorA").withPhoneNumberReceiver("PhoneNumberReceiverB")
				.withAmount(BigDecimal.valueOf(50)).withOperationType(CreditTransferOperationType.OneShotTransfer)
				.build();
		final CreditTransferDto creditTransferDto2 = CreditTransferDto.builder()
				.withPhoneNumberDonor("PhoneNumberDonorB").withPhoneNumberReceiver("PhoneNumberReceiverC")
				.withAmount(BigDecimal.valueOf(10)).withOperationType(CreditTransferOperationType.OneShotTransfer)
				.build();
		final CreditTransferDto creditTransferDto3 = CreditTransferDto.builder()
				.withPhoneNumberDonor("PhoneNumberDonorC").withPhoneNumberReceiver("PhoneNumberReceiverD")
				.withAmount(BigDecimal.valueOf(5)).withOperationType(CreditTransferOperationType.OneShotTransfer)
				.build();
		logger.info("Producing message={}", creditTransferDto1);
		kafkaTemplate1.send(topic01, creditTransferDto1);
		logger.info("Producing message={}", creditTransferDto2);
		kafkaTemplate1.send(topic01, creditTransferDto2);
		logger.info("Producing message={}", creditTransferDto3);
		kafkaTemplate1.send(topic01, creditTransferDto3);

		return ResponseEntity.created(new URI("/dummyProducer")).body("OK");

	}

	@GetMapping(value = "/dummyProducerCrash")
	public ResponseEntity<String> dummyProducer03() throws URISyntaxException {

		final CreditTransferDto creditTransferDto1 = CreditTransferDto.builder()
				.withPhoneNumberDonor("PhoneNumberDonorA").withPhoneNumberReceiver("PhoneNumberReceiverB")
				.withAmount(BigDecimal.valueOf(50)).withOperationType(CreditTransferOperationType.OneShotTransfer)
				.build();
		final CreditTransferDto creditTransferDto2 = CreditTransferDto.builder()
				.withPhoneNumberDonor("PhoneNumberDonorB").withPhoneNumberReceiver("PhoneNumberReceiverC")
				.withAmount(BigDecimal.valueOf(10)).withOperationType(CreditTransferOperationType.BlackListIn).build();
		final CreditTransferDto creditTransferDto3 = CreditTransferDto.builder()
				.withPhoneNumberDonor("PhoneNumberDonorC").withPhoneNumberReceiver("PhoneNumberReceiverD")
				.withAmount(BigDecimal.valueOf(5)).withOperationType(CreditTransferOperationType.OneShotTransfer)
				.build();
		logger.info("Producing message={}", creditTransferDto1);
		kafkaTemplate1.send(topic01, creditTransferDto1);
		logger.info("Producing message={}", creditTransferDto2);
		kafkaTemplate1.send(topic01, creditTransferDto2);
		logger.info("Producing message={}", creditTransferDto3);
		kafkaTemplate1.send(topic01, creditTransferDto3);
		return ResponseEntity.created(new URI("/dummyProducerCrash")).body("OK");

	}

}
