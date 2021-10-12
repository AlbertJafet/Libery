# Libery
Este proyecto consta de una base de datos y Storage proporcionados por Firebase, la cual nos da estos servicios de forma gratuita

SPLASH ACTIVITY: Su función es importante ya que se encarga de verificar si un usuario dejo su cuenta abierta, en ese caso ya no tendrá que iniciar sesión de nuevo, ya que reconoció el inicio de sesión antes de cerrar la aplicación

MAIN ACTIVITY: Es la pantalla principal cuenta con dos botones uno para iniciar sesión y el otro para el registro del alumno.

LOGIN ACTIVITY:  Realiza la función al momento de revisar que tipo de usuario esta accediendo ya sea administrador o usuario al mismo tiempo, esto lo puede realizar por correo y contraseña.

REGISTRO ACTIVITY: Establece la comunicación con la base de datos en tiempo real, esta nos permite crea un nuevo usuario basándonos en diferentes parámetros como nombre, correo y contraseña.  Al crear un nuevo usuario por defecto quedo como un "usuario", el cual solo puede visualizar la información, por el contrario, un “admin” tiene la capacidad que crear nuevas categorías, subir nuevos documentos, también puede eliminar todo tipo de información, el registro también verifica que todos los campos sean válidos.

INICIO ADMIN ACTIVITY: Es la ventana del administrador la cual cuenta con la función de crear nuevas categorías en base a la cantidad de materias con la que cuenta la preparatoria, también puede subir diferentes tipos de archivos en base a las categorías creadas anteriormente, para ello se establece la conexión con la base de datos y el almacenamiento en la nube.

CATEGORY ACTIVITY: Esta pestaña se centra en la creación de nuevas categorías, por ende, solo se necesita de nombre de la nueva categoría para crearla, se crean los parámetros necesarios como id, uid, categoría, ya que esto nos revisa para referenciarlo.

PDF ADD ACTIVITY: Para este screen necesitamos la DB y Storage, ya que en estos se almacenarán los archivos (.pdf, .docx), en base a la interfaz tenemos una condicional la cual verificara que todos los campos están llenos, después procedemos a crear un HashMap tiene la función de enviar y crear los datos que estarán en la tabla “LIBROS”, también cuenta con una barra donde están las categorías es necesario seleccionar esta opción ya que cada archivo se colocara en su categoría correspondiente.



