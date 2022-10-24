import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Producto } from '../models/producto';
import { ProductoService } from '../service/producto.service';

@Component({
  selector: 'app-detalle-producto',
  templateUrl: './detalle-producto.component.html',
  styleUrls: ['./detalle-producto.component.css']
})
export class DetalleProductoComponent implements OnInit {

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
        this.volver();
      }
    );
  }

  volver():void{
    this.router.navigate(['/']);
  }

}
