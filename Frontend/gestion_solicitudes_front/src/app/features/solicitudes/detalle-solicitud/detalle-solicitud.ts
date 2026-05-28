import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { AuthService } from '../../../core/services/auth.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Solicitud, Estado, TipoSolicitud } from '../../../core/models/solicitud.model';
import { UsuarioResumen } from '../../../core/models/usuario.model';
import { HistorialService } from '../../../core/services/historial.service';
import { Historial } from '../../../core/models/historial';
import { IaService } from '../../../core/services/ia.service';

@Component({
  selector: 'app-detalle-solicitud',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './detalle-solicitud.html',
  styleUrl: './detalle-solicitud.css',
})
export class DetalleSolicitud implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private solicitudService = inject(SolicitudService);
  private authService = inject(AuthService);
  private usuarioService = inject(UsuarioService);
  private fb = inject(FormBuilder);
  private historialService = inject(HistorialService);
  private iaService = inject(IaService);

  loadingIA = signal(false);
  errorIA = signal('');
  historial = signal<Historial[]>([]);
  solicitud = signal<Solicitud | null>(null);
  loading = signal(true);
  error = signal('');

  rol = computed(() => this.authService.getRole());

  puedeClasificar = computed(
    () =>
      (this.rol() === 'ROL_ADMINISTRATIVO' || this.rol() === 'ROL_DOCENTE') &&
      this.solicitud()?.estado === 'REGISTRADA',
  );

  puedeAsignar = computed(
    () => this.rol() === 'ROL_ADMINISTRATIVO' && this.solicitud()?.estado === 'CLASIFICADA',
  );

  puedeTomar = computed(
    () => this.rol() === 'ROL_DOCENTE' && this.solicitud()?.estado === 'CLASIFICADA',
  );

  puedeAtender = computed(
    () =>
      (this.rol() === 'ROL_ADMINISTRATIVO' || this.rol() === 'ROL_DOCENTE') &&
      this.solicitud()?.estado === 'EN_ATENCION',
  );

  puedeCerrar = computed(
    () => this.rol() === 'ROL_ADMINISTRATIVO' && this.solicitud()?.estado === 'ATENDIDA',
  );

  // ── Modal: Clasificar ──
  modalClasificar = signal(false);
  loadingClasificar = signal(false);
  errorClasificar = signal('');

  clasificarForm: FormGroup = this.fb.group({
    prioridad: ['', Validators.required],
    justificacion: ['', Validators.required],
  });

  modalAsignar = signal(false);
  responsables = signal<UsuarioResumen[]>([]);
  loadingResponsables = signal(false);
  errorAsignar = signal('');
  responsableSeleccionado = signal<number | null>(null);
  loadingAsignar = signal(false);

  modalAtender = signal(false);
  loadingAtender = signal(false);
  errorAtender = signal('');

  atenderForm: FormGroup = this.fb.group({
    anotacion: ['', Validators.required],
  });

  modalCerrar = signal(false);
  loadingCerrar = signal(false);
  errorCerrar = signal('');

  cerrarForm: FormGroup = this.fb.group({
    anotacion: ['', Validators.required],
  });

  userName = computed(() => {
    const token = this.authService.token();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.nombre ?? payload.sub ?? 'Usuario';
    } catch {
      return 'Usuario';
    }
  });
  sugerirConIA() {
    const descripcion = this.solicitud()!.descripcion;
    console.log(descripcion)
    this.loadingIA.set(true);
    this.errorIA.set('');

    this.iaService.clasificar(descripcion).subscribe({
      next: (sugerencia) => {
        this.clasificarForm.patchValue({
          prioridad: sugerencia.prioridad,
          justificacion: sugerencia.explicacion,
        });
        this.loadingIA.set(false);
      },
      error: () => {
        this.errorIA.set('No se pudo obtener sugerencia de la IA.');
        this.loadingIA.set(false);
      },
    });
  }

  userInitial = computed(() => this.userName().charAt(0).toUpperCase());

  userRole = computed(() => {
    const role = this.authService.getRole();
    const labels: Record<string, string> = {
      ROL_ESTUDIANTE: 'Estudiante',
      ROL_ADMINISTRATIVO: 'Administrativo',
      ROL_DOCENTE: 'Docente',
    };
    return role ? (labels[role] ?? role) : '';
  });

  // ══════════════════════════════
  // CICLO DE VIDA
  // ══════════════════════════════
  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarSolicitud(id);
  }

  cargarSolicitud(id: number) {
    this.loading.set(true);
    this.error.set('');

    this.solicitudService.obtener(id).subscribe({
      next: (data) => {
        this.solicitud.set(data);
        this.loading.set(false);
        this.cargarHistorial(data.id);
      },
      error: () => {
        this.error.set('No se pudo cargar la solicitud.');
        this.loading.set(false);
      },
    });
  }

  cargarHistorial(id: number) {
    this.historialService.obtener(id).subscribe({
      next: (data) => this.historial.set(data),
      error: () => {},
    });
  }

  volver() {
    this.router.navigate(['/solicitudes']);
  }

  // ══════════════════════════════
  // CLASIFICAR
  // ══════════════════════════════
  abrirModalClasificar() {
    this.clasificarForm.reset();
    this.errorClasificar.set('');
    this.modalClasificar.set(true);
  }

  cerrarModalClasificar() {
    this.modalClasificar.set(false);
  }

  clasificar() {
    if (this.clasificarForm.invalid) {
      this.clasificarForm.markAllAsTouched();
      return;
    }

    this.loadingClasificar.set(true);
    this.errorClasificar.set('');

    const id = this.solicitud()!.id;

    this.solicitudService.clasificar(id, this.clasificarForm.value).subscribe({
      next: (actualizada) => {
        this.solicitud.set(actualizada);
        this.loadingClasificar.set(false);
        this.modalClasificar.set(false);
        this.cargarHistorial(actualizada.id);
      },
      error: () => {
        this.errorClasificar.set('No se pudo clasificar la solicitud.');
        this.loadingClasificar.set(false);
      },
    });
  }

  // ══════════════════════════════
  // ASIGNAR RESPONSABLE
  // ══════════════════════════════
  abrirModalAsignar() {
    this.errorAsignar.set('');
    this.responsableSeleccionado.set(null);
    this.loadingResponsables.set(true);
    this.modalAsignar.set(true);

    this.usuarioService.obtenerResponsables().subscribe({
      next: (data) => {
        this.responsables.set(data);
        this.loadingResponsables.set(false);
      },
      error: () => {
        this.errorAsignar.set('No se pudo cargar la lista de responsables.');
        this.loadingResponsables.set(false);
      },
    });
  }

  cerrarModalAsignar() {
    this.modalAsignar.set(false);
  }

  seleccionarResponsable(event: Event) {
    const id = Number((event.target as HTMLSelectElement).value);
    this.responsableSeleccionado.set(id);
  }

  asignarResponsable() {
    const responsableId = this.responsableSeleccionado();
    if (!responsableId) return;

    this.loadingAsignar.set(true);
    this.errorAsignar.set('');

    const id = this.solicitud()!.id;

    this.solicitudService.asignarResponsable(id, responsableId).subscribe({
      next: (actualizada) => {
        this.solicitud.set(actualizada);
        this.loadingAsignar.set(false);
        this.modalAsignar.set(false);
        this.cargarHistorial(actualizada.id);
      },
      error: () => {
        this.errorAsignar.set('No se pudo asignar el responsable.');
        this.loadingAsignar.set(false);
      },
    });
  }

  // ══════════════════════════════
  // TOMAR SOLICITUD (docente)
  // ══════════════════════════════
  tomarSolicitud() {
    const token = this.authService.token();
    if (!token) return;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const userId = payload.id;

      this.solicitudService.asignarResponsable(this.solicitud()!.id, userId).subscribe({
        next: (actualizada) => {
          this.solicitud.set(actualizada);
          this.cargarHistorial(actualizada.id);
        },
        error: () => this.error.set('No se pudo tomar la solicitud.'),
      });
    } catch {
      this.error.set('Error al obtener datos del usuario.');
    }
  }

  // ══════════════════════════════
  // ATENDER
  // ══════════════════════════════
  abrirModalAtender() {
    this.atenderForm.reset();
    this.errorAtender.set('');
    this.modalAtender.set(true);
  }

  cerrarModalAtender() {
    this.modalAtender.set(false);
  }

  atender() {
    if (this.atenderForm.invalid) {
      this.atenderForm.markAllAsTouched();
      return;
    }

    this.loadingAtender.set(true);
    this.errorAtender.set('');

    const id = this.solicitud()!.id;
    const anotacion = this.atenderForm.value.anotacion;

    this.solicitudService.atender(id, anotacion).subscribe({
      next: (actualizada) => {
        this.solicitud.set(actualizada);
        this.loadingAtender.set(false);
        this.modalAtender.set(false);
        this.cargarHistorial(actualizada.id);
      },
      error: () => {
        this.errorAtender.set('No se pudo marcar como atendida.');
        this.loadingAtender.set(false);
      },
    });
  }

  // ══════════════════════════════
  // CERRAR
  // ══════════════════════════════
  abrirModalCerrar() {
    this.cerrarForm.reset();
    this.errorCerrar.set('');
    this.modalCerrar.set(true);
  }

  cerrarModalCerrar() {
    this.modalCerrar.set(false);
  }

  cerrar() {
    if (this.cerrarForm.invalid) {
      this.cerrarForm.markAllAsTouched();
      return;
    }

    this.loadingCerrar.set(true);
    this.errorCerrar.set('');

    const id = this.solicitud()!.id;
    const anotacion = this.cerrarForm.value.anotacion;

    this.solicitudService.cerrar(id, anotacion).subscribe({
      next: (actualizada) => {
        this.solicitud.set(actualizada);
        this.loadingCerrar.set(false);
        this.modalCerrar.set(false);
        this.cargarHistorial(actualizada.id);
      },
      error: () => {
        this.errorCerrar.set('No se pudo cerrar la solicitud.');
        this.loadingCerrar.set(false);
      },
    });
  }

  // ══════════════════════════════
  // FORMATTERS
  // ══════════════════════════════
  formatEstado(estado: Estado): string {
    const map: Record<string, string> = {
      REGISTRADA: 'Registrada',
      CLASIFICADA: 'Clasificada',
      EN_ATENCION: 'En atención',
      ATENDIDA: 'Atendida',
      CERRADA: 'Cerrada',
    };
    return map[estado] ?? estado;
  }

  formatTipo(tipo: TipoSolicitud): string {
    const map: Record<string, string> = {
      REGISTRO_ASIGNATURAS: 'Registro',
      HOMOLOGACION: 'Homologación',
      CANCELACION_ASIGNATURAS: 'Cancelación',
      SOLICITUD_CUPOS: 'Cupos',
      CONSULTA_ACADEMICA: 'Consulta',
    };
    return map[tipo] ?? tipo;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
