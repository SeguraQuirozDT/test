package com.mifel.test.controller;

import java.math.BigInteger;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifel.test.dto.cypher.CypherDecodeReqDto;
import com.mifel.test.dto.cypher.CypherDecodeResDto;
import com.mifel.test.dto.cypher.CypherEncodeReqDto;
import com.mifel.test.dto.cypher.CypherEncodeResDto;
import com.mifel.test.utils.CypherStr;

@RequestMapping(path = "/api/cypher")
@RestController
public class CypherController {
	
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@PostMapping(path = "/encode", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CypherEncodeResDto> encode(@RequestBody  CypherEncodeReqDto objCypherEncodeReqDto){
		try {
			SecretKey symmetricKey = CypherStr.createAESKey();
	        byte[] initializationVector = CypherStr.createInitializationVector();
	        byte[] cipherText;
			
			cipherText = CypherStr.do_AESEncryption(objCypherEncodeReqDto.getPlanText(), symmetricKey, initializationVector);	
	        
			System.out.println("Text encode : " + DatatypeConverter.printHexBinary(cipherText));
			System.out.println("Symmetric Key : " + Base64.getEncoder().encodeToString(symmetricKey.getEncoded()));
			System.out.println("Initialization Vector : " + DatatypeConverter.printHexBinary(initializationVector));
			
			return ResponseEntity.ok(new CypherEncodeResDto(DatatypeConverter.printHexBinary(cipherText), Base64.getEncoder().encodeToString(symmetricKey.getEncoded()), DatatypeConverter.printHexBinary(initializationVector)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}
		
	}
	
	
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@PostMapping(path = "/decode", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CypherDecodeResDto> dencode(@RequestBody  CypherDecodeReqDto objCypherDecodeReqDto) throws Exception {
		try {
		byte[] decodedKey = Base64.getDecoder().decode(objCypherDecodeReqDto.getSymmetricKey());
		SecretKey symmetricKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		//symmetricKey.
        // converting string to integer value 
		//int valInitializationVector = Integer.parseInt(objCypherDecodeReqDto.getInitializationVector(), 16);
		//BigInteger big = BigInteger.valueOf(valInitializationVector);
		//byte[] initializationVector = (big.toByteArray());
		
		
		
		String decryptedText = CypherStr.do_AESDecryption(DatatypeConverter.parseHexBinary(objCypherDecodeReqDto.getEncodeText()), symmetricKey, DatatypeConverter.parseHexBinary(objCypherDecodeReqDto.getInitializationVector()));
        System.out.println("Your original message is: " + decryptedText);

		
		return ResponseEntity.ok(new CypherDecodeResDto(decryptedText));
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}	
	}
	
}
