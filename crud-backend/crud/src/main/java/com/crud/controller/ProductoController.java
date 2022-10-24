package com.crud.controller;

import java.util.List;

import javax.xml.ws.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.dto.Mensaje;
import com.crud.dto.ProductoDto;
import com.crud.entity.Producto;
import com.crud.service.ProductoService;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> list(){
        List<Producto> list = productoService.list();

        return  new ResponseEntity<List<Producto>>(list,HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Producto> getById(@PathVariable("id") int id){
        if(!productoService.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
        }

        Producto producto = productoService.getOne(id).get();

        return new ResponseEntity<Producto>(producto,HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<Producto> getByNombre(@PathVariable("nombre") String nombre){
        if(!productoService.existsByNombre(nombre)){
            return new ResponseEntity(new Mensaje("NO EXISTE"), HttpStatus.NOT_FOUND);
        }
        Producto producto = productoService.getByNombre(nombre).get();
        return new ResponseEntity<Producto>(producto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductoDto productoDto){
        if(StringUtils.isBlank(productoDto.getNombre())){
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if(productoDto.getPrecio() < 0 ){
            return new ResponseEntity(new Mensaje("El precio debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }

        if(productoService.existsByNombre(productoDto.getNombre())){
            return new ResponseEntity(new Mensaje("Ese nombre ya existe."), HttpStatus.BAD_REQUEST);
        }

        Producto producto = new Producto(productoDto.getNombre(), productoDto.getPrecio());
        productoService.save(producto);

        return new ResponseEntity(new Mensaje("El producto fue creado"),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, 
                                    @RequestBody ProductoDto productoDto){

        if(productoService.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el producto"), HttpStatus.BAD_REQUEST);
        }   

        
        if(productoService.existsByNombre(productoDto.getNombre()) && productoService.getByNombre(productoDto.getNombre()).get().getId() != id){
            return new ResponseEntity(new Mensaje("Ese nombre ya existe."), HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isBlank(productoDto.getNombre())){
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if(productoDto.getPrecio() < 0 ){
            return new ResponseEntity(new Mensaje("El precio debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }

        Producto producto = productoService.getOne(id).get();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        productoService.save(producto);

        return new ResponseEntity(new Mensaje("Producto actualizado con exito"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
     
        if(productoService.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el producto"), HttpStatus.BAD_REQUEST);
        }   
        productoService.delete(id);
        return new ResponseEntity(new Mensaje("Producto eliminado"), HttpStatus.OK);
    }
}
