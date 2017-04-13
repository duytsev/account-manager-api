package ru.duytsev.account.manager.api.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(Long id) {
		super(String.format("Resource with id %s was not found", id));
	}
}
