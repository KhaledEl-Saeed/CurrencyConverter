package com.example.CurrencyConverter.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;



@Data
@Schema(description = "Response containing status and a map of currency codes with their names")
public class CurrencyResponse {

    @Schema(description = "Status of the currency fetch operation", example = "success")
    private String status;


    @Schema(
            description = "Map of currency codes and their full names",
            example = "{\"USD\": \"United States Dollar\", \"EUR\": \"Euro\", \"EGP\": \"Egyptian Pound\"}"
    )
    private Map<String, String> currencies;


}
