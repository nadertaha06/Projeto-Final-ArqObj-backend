package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.produto.Categoria;
import com.ProjetoFinal.ecommerce.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria criar(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new IllegalArgumentException("Categoria já existe: " + categoria.getNome());
        }
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada: " + id));
    }

    public Categoria atualizar(Long id, Categoria dadosAtualizados) {
        Categoria categoria = buscarPorId(id);
        categoria.setNome(dadosAtualizados.getNome());
        categoria.setDescricao(dadosAtualizados.getDescricao());
        return categoriaRepository.save(categoria);
    }

    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException("Categoria não encontrada: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
