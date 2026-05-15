package cl.techstore.api.service;

import cl.techstore.api.model.Producto;
import cl.techstore.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarActivos() {
        return repository.findByActivoTrue();
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminarLogico(Long id) {
        Producto p = obtenerPorId(id);
        if (p != null) {
            p.setActivo(false);
            repository.save(p);
        }
    }
}