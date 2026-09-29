package com.mifel.test.dto.cypher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CypherDecodeReqDto {
	private String encodeText;
	private String symmetricKey;
	private String initializationVector;
}
