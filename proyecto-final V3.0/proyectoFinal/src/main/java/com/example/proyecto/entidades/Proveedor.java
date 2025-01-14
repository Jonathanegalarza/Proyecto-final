
package com.example.proyecto.entidades;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Proveedor extends Persona{
    
private String matricula;//
private String descripcion;//
private int puntuacionPromedio;//esto es para las estrellitas

@OneToOne
private Rubro rubro;
//private EstadoTrabajo estadoTrabajo;//visualiza en el perfil del proveedor 
private boolean estadoActual;//Depende del estado actual


private Double precioHora;
private Date fechaAlta;
private int contdTrabajoRealizado;//esto permite scar un promedio de los trabajos realizados


 // se elimino lista de roles
/*
se puso el rol en la entidad persona
@Enumerated(EnumType.STRING)
    private Rol rol;
 */
 @OneToOne
 private Imagen imagen;
 
}
