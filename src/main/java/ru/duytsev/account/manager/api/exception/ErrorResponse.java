package ru.duytsev.account.manager.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

	private final int errorCode;
	private final String errorMessage;
}
