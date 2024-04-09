package com.example.kafkatips.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CreditTransferDto implements Serializable {

	public static final class Builder {
		private CreditTransferOperationType operationType;
		private Date operationDate;
		private String resultCode;
		private String resultDescription;
		private String phoneNumberDonor;
		private String phoneNumberReceiver;
		private BigDecimal amount;
		private Integer frequency;
		private BigDecimal operationCost;

		private Builder() {
		}

		public CreditTransferDto build() {
			return new CreditTransferDto(this);
		}

		public Builder withAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public Builder withFrequency(Integer frequency) {
			this.frequency = frequency;
			return this;
		}

		public Builder withOperationCost(BigDecimal operationCost) {
			this.operationCost = operationCost;
			return this;
		}

		public Builder withOperationDate(Date operationDate) {
			this.operationDate = operationDate;
			return this;
		}

		public Builder withOperationType(CreditTransferOperationType operationType) {
			this.operationType = operationType;
			return this;
		}

		public Builder withPhoneNumberDonor(String phoneNumberDonor) {
			this.phoneNumberDonor = phoneNumberDonor;
			return this;
		}

		public Builder withPhoneNumberReceiver(String phoneNumberReceiver) {
			this.phoneNumberReceiver = phoneNumberReceiver;
			return this;
		}

		public Builder withResultCode(String resultCode) {
			this.resultCode = resultCode;
			return this;
		}

		public Builder withResultDescription(String resultDescription) {
			this.resultDescription = resultDescription;
			return this;
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static Builder builder() {
		return new Builder();
	}

	private CreditTransferOperationType operationType;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private Date operationDate;
	private String resultCode;
	private String resultDescription;
	private String phoneNumberDonor;
	private String phoneNumberReceiver;
	private BigDecimal amount;
	private Integer frequency;
	private BigDecimal operationCost;

	public CreditTransferDto() {
	}

	private CreditTransferDto(Builder builder) {
		this.operationType = builder.operationType;
		this.operationDate = builder.operationDate;
		this.resultCode = builder.resultCode;
		this.resultDescription = builder.resultDescription;
		this.phoneNumberDonor = builder.phoneNumberDonor;
		this.phoneNumberReceiver = builder.phoneNumberReceiver;
		this.amount = builder.amount;
		this.frequency = builder.frequency;
		this.operationCost = builder.operationCost;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public BigDecimal getOperationCost() {
		return operationCost;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public CreditTransferOperationType getOperationType() {
		return operationType;
	}

	public String getPhoneNumberDonor() {
		return phoneNumberDonor;
	}

	public String getPhoneNumberReceiver() {
		return phoneNumberReceiver;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public void setOperationCost(BigDecimal operationCost) {
		this.operationCost = operationCost;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public void setOperationType(CreditTransferOperationType operationType) {
		this.operationType = operationType;
	}

	public void setPhoneNumberDonor(String phoneNumberDonor) {
		this.phoneNumberDonor = phoneNumberDonor;
	}

	public void setPhoneNumberReceiver(String phoneNumberReceiver) {
		this.phoneNumberReceiver = phoneNumberReceiver;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}

	@Override
	public String toString() {
		return "CreditTransferDto [operationType=" + operationType + ", operationDate=" + operationDate
				+ ", resultCode=" + resultCode + ", resultDescription=" + resultDescription + ", phoneNumberDonor="
				+ phoneNumberDonor + ", phoneNumberReceiver=" + phoneNumberReceiver + ", amount=" + amount
				+ ", frequency=" + frequency + ", operationCost=" + operationCost + "]";
	}

}
