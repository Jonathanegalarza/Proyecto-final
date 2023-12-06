package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Proveedor;
import com.example.proyecto.entidades.Rubro;
import com.example.proyecto.entidades.Trabajo;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.ProveedorServicio;
import com.example.proyecto.servicios.RubroServicio;
import com.example.proyecto.servicios.TrabajoServicio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author AMD
 */
@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    ProveedorServicio proveedorServicio;
    @Autowired
    RubroServicio rubroServicio;
    @Autowired
    TrabajoServicio trabajoServicio;
   

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, String apellido, String dni, String telefono,
            String direccion, String email, String password, String matricula,
            Double precioHora, String descripcion,
            ModelMap modelo, MultipartFile archivo, String password2, String idRubro) {

        try {
            proveedorServicio.crearProveedor(archivo, nombre, apellido, dni, telefono, email,
                    password, password2, matricula, descripcion, precioHora, direccion, idRubro);

            modelo.put("exito", "El Proveedor fue registrado correctamente!");
        } catch (MiException ex) {
            
            List<Rubro> rubros = rubroServicio.ListaRubros();
            modelo.addAttribute("rubros", rubros);

            modelo.put("error", ex.getMessage());
            return "registroDoble.html";
        }

        return "index.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROVEEDOR')")
    @GetMapping("/listar/{rubro}")
    public String listar(@PathVariable String rubro, ModelMap modelo) {

        List<Proveedor> proveedores = new ArrayList<>();
        proveedores = proveedorServicio.listarPorRubro(rubro);

        modelo.addAttribute("proveedores", proveedores);

        return "proveedor_list.html";
    }

    @GetMapping("/")
    public String verPagInicio(Model modelo, @Param("palabraClave") String palabraClave) {
        List<Proveedor> listProveedors = proveedorServicio.listAll(palabraClave);
        modelo.addAttribute("listProveedors", listProveedors);
        modelo.addAttribute("palabraClave", palabraClave);

        return "index.html";

    }
    
    @GetMapping("/listarTrabajo")
    public String listarTrabajo(ModelMap modelo) {
        
        String dniProveedor = "t"; 
        
        List<Trabajo> listaTrabajos = trabajoServicio.listarTrabajoCliente(dniProveedor);
        
        modelo.addAttribute("listaTrabajos",listaTrabajos);
        
        return "listar_trabajo_proovedor.html";
    }
    
     @GetMapping("/perfil/{dni}")
    public String listarTrabajo(@PathVariable String dni, ModelMap modelo) {
         System.out.println(dni);
         System.out.println("--------------");
        Proveedor proveedor =proveedorServicio.getOne(dni);
                       
        modelo.addAttribute("proveedor",proveedor);
        
        return "Perfil.html";
    }
    
     @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo,HttpSession session){
        Proveedor proveedor = (Proveedor) session.getAttribute("personasession");
         modelo.put("proveedor", proveedor);
        return "proveedor_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(@RequestParam MultipartFile archivo, String nombre, String apellido, String dni, String telefono, String email, String password,
    String password2, String matricula, String descripcion,
    Double precioHora, String idRubro, String domicilio, ModelMap modelo) {

        try {
            proveedorServicio.modificar(archivo, nombre, apellido, dni, telefono, email, password, password2, matricula, descripcion, precioHora, idRubro, domicilio);

            modelo.put("exito", "Cliente actualizado correctamente!");

            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
         

            return "proveedor_modificar.html";
        }

    
}
    
}
