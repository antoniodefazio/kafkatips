package com.example.kafkatips.services;

import com.example.kafkatips.dtos.CreditTransferDto;
import com.example.kafkatips.entities.ExactlyOnce;

public interface IdempotentAPICaller {

	boolean produce(ExactlyOnce exactlyonce, CreditTransferDto proNF001OUTFullMessage);

}
