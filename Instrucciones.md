Para actualizar o corregir los datos de un usuario existente en el sistema, siga estos pasos interactivos desde la consola:

1. En el menú principal, acceda a la sección **Gestionar Usuarios**.
2. Seleccione la opción para **Editar usuario**.
3. El sistema solicitará ingresar el **ID del usuario** que desea modificar. *(Se recomienda utilizar la opción "Listar usuarios" previamente para ubicar el ID correcto).*
4. Si el ID ingresado es válido y no se encuentra eliminado, el sistema le permitirá actualizar los campos correspondientes (Nombre, Apellido, Mail, Celular, Contraseña o Rol).
   **Validación de seguridad:** Si decide modificar el correo electrónico (Mail), el sistema verificará automáticamente que el nuevo correo sea único y no pertenezca a otro usuario registrado.
5. Tras confirmar la operación, los cambios se guardarán de forma persistente en la base de datos SQLite.
