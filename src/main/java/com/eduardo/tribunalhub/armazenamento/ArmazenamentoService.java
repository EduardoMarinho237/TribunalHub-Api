package com.eduardo.tribunalhub.armazenamento;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ArmazenamentoService {
    
    private final Path rootLocation = Paths.get("uploads");
    
    public ArmazenamentoService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads", e);
        }
    }
    
    public String salvarArquivo(MultipartFile arquivo) {
        try {
            if (arquivo.isEmpty()) {
                throw new RuntimeException("Arquivo vazio");
            }
            
            // Gera um nome único para o arquivo
            String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
            
            // Salva o arquivo no sistema de arquivos
            Path destino = this.rootLocation.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            
            // Retorna o caminho relativo para ser armazenado no banco
            return "/uploads/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar arquivo", e);
        }
    }
    
    public void excluirArquivo(String urlArquivo) {
        try {
            Path arquivo = rootLocation.resolve(urlArquivo.replace("/uploads/", ""));
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao excluir arquivo: " + urlArquivo, e);
        }
    }
}