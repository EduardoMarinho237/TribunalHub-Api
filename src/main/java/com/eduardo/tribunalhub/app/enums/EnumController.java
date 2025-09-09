package com.eduardo.tribunalhub.app.enums;

import com.eduardo.tribunalhub.app.usuario.enums.Cargo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class EnumController {
    
    @GetMapping("/listar-cargos")
    public ResponseEntity<List<Map<String, String>>> getCargo() {
        List<Map<String, String>> cargos = Arrays.stream(Cargo.values())
            .map(cargo -> {
                Map<String, String> map = new HashMap<>();
                map.put("codigo", cargo.name());
                map.put("descricao", cargo.getDescricao());
                return map;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(cargos);
    }
}