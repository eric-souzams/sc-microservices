package com.sc.cambioservice.controller;

import com.sc.cambioservice.dto.CambioDto;
import com.sc.cambioservice.model.Cambio;
import com.sc.cambioservice.repository.CambioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Tag(name = "Cambio Endpoint")
@RestController
@RequestMapping(value = "/cambio-service")
public class CambioController {

    @Autowired
    private Environment environment;

    @Autowired
    private CambioRepository cambioRepository;

    @Operation(summary = "Get cambio from currency")
    @GetMapping(value = "/{amount}/{from}/{to}")
    public CambioDto getCambio(@PathVariable("amount") BigDecimal amount,
                               @PathVariable("from") String from,
                               @PathVariable("to") String to) {

        Optional<Cambio> result = cambioRepository.findByFromAndTo(from, to);
        if (result.isEmpty()) throw new RuntimeException("Currency Unsupported");

        CambioDto cambioDto = new CambioDto();
        BeanUtils.copyProperties(result.get(), cambioDto);

        BigDecimal conversionFactor = cambioDto.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        cambioDto.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));

        String port = environment.getProperty("local.server.port");
        cambioDto.setEnvironment(port);

        return cambioDto;
    }

}
