package com.example.kafkatips.exceptions;

import com.example.kafkatips.dtos.CreditTransferDto;

public class DuplicateMessageException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final CreditTransferDto creditTransferDto;

	public DuplicateMessageException(final String eventId, Exception ex, CreditTransferDto creditTransferDto) {
		super("Duplicate meessage Id: " + eventId, ex);
		this.creditTransferDto = creditTransferDto;
	}

	@Override
	public String toString() {
		return "DuplicateMessageException [creditTransferDto=" + creditTransferDto + ", super.toString()="
				+ super.toString() + "]";
	}

}
