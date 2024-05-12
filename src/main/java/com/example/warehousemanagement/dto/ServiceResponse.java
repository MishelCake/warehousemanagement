package com.example.warehousemanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
public class ServiceResponse {
	
	@NotNull
	@JsonProperty("status")
	private Integer status;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("message")
	private String message;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("data")
	private Object data;

}
