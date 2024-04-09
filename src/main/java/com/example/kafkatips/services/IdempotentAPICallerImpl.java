package com.example.kafkatips.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.example.kafkatips.dtos.CreditTransferDto;
import com.example.kafkatips.dtos.CreditTransferOperationType;
import com.example.kafkatips.dtos.ExternalServerStatus;
import com.example.kafkatips.entities.ExactlyOnce;
import com.example.kafkatips.exceptions.DuplicateMessageException;
import com.example.kafkatips.repos.ExactlyOnceRepository;

@Service
public class IdempotentAPICallerImpl implements IdempotentAPICaller {

	private static final Logger logger = LoggerFactory.getLogger(IdempotentAPICallerImpl.class);

	@Autowired
	private ExactlyOnceRepository exactlyOnceRepository;

	private ExternalServerStatus externalServerStatusSimulation = ExternalServerStatus.DOWN;

	private boolean externalServerError(CreditTransferDto creditTransferDto) {
		return creditTransferDto.getOperationType() == CreditTransferOperationType.BlackListIn
				&& externalServerStatusSimulation == ExternalServerStatus.DOWN;
	}

	@Override
	@Transactional
	public boolean produce(ExactlyOnce exactlyonce, CreditTransferDto creditTransferDto) {
		try {
			exactlyOnceRepository.saveAndFlush(exactlyonce); // SUPER IMPORTANT: flush is not commit
			logger.info("API Call Attempt");
			if (externalServerError(creditTransferDto)) {
				externalServerStatusSimulation = ExternalServerStatus.UP;
				logger.warn("API Call error");
				throw new HttpServerErrorException(HttpStatusCode.valueOf(500));
			} else {
				logger.info("Successfully API Called");
			}
		} catch (final DataIntegrityViolationException e) {
			logger.warn("Message already processed: {}", exactlyonce.getId());
			throw new DuplicateMessageException(exactlyonce.getId(), e, creditTransferDto);
		}

		return true;
	}

}
