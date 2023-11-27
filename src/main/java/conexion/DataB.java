package conexion;

public interface DataB {
    String NOM_DB = "Almacen";

    // ----------------------------------------------------------------
    String NOM_TABLA = "Productos";
    String ID = "ID";
    String NOMBRE = "Nombre";
    String DESCRIPCION = "Descripcion";
    String CANTIDAD = "Cantidad";
    String PRECIO = "Precio";

    // ----------------------------------------------------------------

    String NOM_TABLA_EMPLEADOS = "Empleados";
    String ID_EMPLEADOS = "ID";
    String NOMBRE_EMPLEADOS = "Nombre";
    String APELLIDOS_EMPLEADOS = "Apellidos";
    String CORREO_EMPLEADOS = "Correo";

    // ----------------------------------------------------------------

    String NOM_TABLA_PEDIDOS = "Pedidos";
    String ID_PEDIDOS = "ID";
    String DESCRIPCION_PEDIDOS = "Descripcion";
    String ID_PRODUCTO = "idProducto";
    String PRECIO_TOTAL = "PrecioTotal";

    // ----------------------------------------------------------------

    String NOM_TABLA_FAV = "ProductoFavorito";
    String ID_FAVORITOS = "ID";
    String ID_PRODUCTO_FAV = "idProducto";
}
