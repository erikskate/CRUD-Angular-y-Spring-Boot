import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Producto } from '../models/producto';
import { ProductoService } from '../service/producto.service';

@Component({
  selector: 'app-editar-producto',
  templateUrl: './editar-producto.component.html',
  styleUrls: ['./editar-producto.component.css']
})
export class EditarProductoComponent implements OnInit {

  producto:Producto = null;

  constructor(private productoService: ProductoService,
              private toastr: ToastrService,
              private router:Router,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params.id;
    this.productoService.detail(id).subscribe(
      data =>{
        this.producto = data;
      },
      err =>{
        this.toastr.error(err.error.mensaje,'ERROR!',{timeOut:3000, positionClass:'toast-top-center'});
        this.router.navigate(['/']);
      }
    );
  }

  onUpdate():void{
    const id = this.activatedRoute.snapshot.params.id;
    this.productoService.update(id,this.producto).subscribe(
      data =>{
        this.producto = data;
        this.toastr.success('Registro Editado con exito.','OK!',{timeOut:3000, positionClass:'toast-top-center'});
        this.router.navigate(['/']);
      },
      err =>{
        this.toastr.error(err.error.mensaje,'ERROR!',{timeOut:3000, positionClass:'toast-top-center'});
        this.router.navigate(['/']);
      }
    )
  }
}
