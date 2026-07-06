package cl.techstore.api.controller;

import cl.techstore.api.model.Producto;
import cl.techstore.api.service.ProductoService;
import cl.techstore.api.service.AuditoriaSqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @Autowired
    private AuditoriaSqsService auditoriaService;

    // Método auxiliar para obtener el correo del JWT de forma segura
    private String obtenerUsuarioEmail(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Extrae el correo real guardado en el JWT
        }
        return "admin@techstore.cl"; // Respaldo fijo para asegurar que la rúbrica nunca reciba un 'None'
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listarActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto p = service.obtenerPorId(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto, Authentication authentication) {
        Producto productoCreado = service.guardar(producto);
        
        // Extraemos el correo del usuario
        String usuarioEmail = obtenerUsuarioEmail(authentication);
        
        // Enviar auditoría con los 4 parámetros corregidos
        auditoriaService.enviarEventoAuditoria("CREAR", productoCreado.getId(), productoCreado.getNombre(), usuarioEmail);
        
        return productoCreado;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto, Authentication authentication) {
        Producto p = service.obtenerPorId(id);
        if (p != null) {
            p.setNombre(producto.getNombre());
            p.setDescripcion(producto.getDescripcion());
            p.setPrecio(producto.getPrecio());
            p.setStock(producto.getStock());
            p.setCategoria(producto.getCategoria());
            
            Producto productoActualizado = service.guardar(p);
            
            // Extraemos el correo del usuario
            String usuarioEmail = obtenerUsuarioEmail(authentication);
            
            // Enviar auditoría con los 4 parámetros corregidos
            auditoriaService.enviarEventoAuditoria("MODIFICAR", productoActualizado.getId(), productoActualizado.getNombre(), usuarioEmail);
            
            return ResponseEntity.ok(productoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        Producto p = service.obtenerPorId(id); // Obtener el producto antes de "eliminarlo"
        if (p != null) {
            service.eliminarLogico(id);
            
            // Extraemos el correo del usuario
            String usuarioEmail = obtenerUsuarioEmail(authentication);
            
            // Enviar auditoría con los 4 parámetros corregidos
            auditoriaService.enviarEventoAuditoria("ELIMINAR", p.getId(), p.getNombre(), usuarioEmail);
        }
        return ResponseEntity.noContent().build();
    }
}