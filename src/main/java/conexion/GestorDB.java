package conexion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

public class GestorDB implements DataB {
    private Connection connection;

    public GestorDB() {
        getConexion();
    }

    public void getConexion() {
        String nombreUsuario = "Javi";
        String password = "boligrafo";
        String baseDatos = "Almacen";
        String Host = "127.0.0.1:3306";
        String url = "jdbc:mariadb://" + Host + "/" + baseDatos;

        try {
            // Cargamos en memoria el Driver para poder utilizarlo y ponemos el nombre del Driver de mi librería
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }

        try {
            // Conectamos con la base de datos, lo que utilizamos de la clase Driver es el DriverManager.getConnection
            connection = DriverManager.getConnection(url, nombreUsuario, password);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void llamarConexion() {
        System.out.println("Conexión realizada con éxito.");
    }

    public void tablaProductos() {
        try {
            String urlString = "https://dummyjson.com/products";
            URL direct = new URL(urlString);
            HttpURLConnection conex = (HttpURLConnection) direct.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conex.getInputStream()));

            String lectura = br.readLine();
            //System.out.println(lectura);

            JSONObject objeto = new JSONObject(lectura);
            JSONArray productos = objeto.getJSONArray("products");

            String query = "INSERT INTO " + DataB.NOM_TABLA + " (" + DataB.ID + ", " + DataB.NOMBRE + ", " + DataB.DESCRIPCION + ", " + DataB.CANTIDAD + ", " + DataB.PRECIO + ") VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = null;

            try {
                pst = connection.prepareStatement(String.format(query));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < productos.length(); i++) {
                JSONObject prod = productos.getJSONObject(i);
                int id = prod.getInt("id");
                String nombre = prod.getString("title");
                String descripcion = prod.getString("description");
                int cantidad = prod.getInt("stock");
                int precio = prod.getInt("price");

                try {
                    pst.setInt(1, id);
                    pst.setString(2, nombre);
                    pst.setString(3, descripcion);
                    pst.setInt(4, cantidad);
                    pst.setInt(5, precio);

                    pst.execute();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void agregarEmpleado() {
        Scanner scan = new Scanner(System.in);

        String query = "INSERT INTO " + DataB.NOM_TABLA_EMPLEADOS + " (" + DataB.NOMBRE_EMPLEADOS + ", " + DataB.APELLIDOS_EMPLEADOS + ", " + DataB.CORREO_EMPLEADOS + ") VALUES (?, ?, ?)";
        System.out.println("Introduzca los datos del empleado.");
        System.out.print("Nombre: ");
        String nombre = scan.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = scan.nextLine();
        System.out.print("Correo: ");
        String correo = scan.nextLine();

        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(String.format(query));

            pst.setString(1, nombre.toString());
            pst.setString(2, apellidos.toString());
            pst.setString(3, correo.toString());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void agregarPedido() {
        Scanner scan = new Scanner(System.in);
        String query = "INSERT INTO " + DataB.NOM_TABLA_PEDIDOS + " ( " + DataB.ID_PRODUCTO + ", " + DataB.DESCRIPCION_PEDIDOS + ", " + DataB.PRECIO_TOTAL + ") VALUES (?, ?, ?)";

        System.out.println("Introduzca los datos del pedido.");
        System.out.print("ID: ");
        int ID = scan.nextInt();
        System.out.print("Cantidad: ");
        int cantidadUnidades = scan.nextInt();
        int precioTotal = buscarPrecio(ID) * cantidadUnidades;
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(String.format(query));
            pst.setInt(1, ID);
            pst.setString(2, buscarDescripcion(ID));
            pst.setInt(3, precioTotal);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String buscarDescripcion(int id) {
        String query = "SELECT " + DataB.DESCRIPCION + " FROM " + DataB.NOM_TABLA + " WHERE " + DataB.ID + " = ?";
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setString(1, String.valueOf(id));
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getString(DataB.DESCRIPCION);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int buscarPrecio(int id) {
        String query = "SELECT " + DataB.PRECIO + " FROM " + DataB.NOM_TABLA + " WHERE " + DataB.ID + " = ?";
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(DataB.PRECIO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarProductos() {
        String query = "SELECT * FROM " + DataB.NOM_TABLA;
        System.out.println("--- PRODUCTOS ---");
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(5) + "€");
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarEmpleados() {
        String query = "SELECT * FROM " + DataB.NOM_TABLA_EMPLEADOS;
        System.out.println("--- EMPLEADOS ---");
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + " | " + rs.getString(2) + " " + rs.getString(3) + ".");
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarPedidos() {
        String query = "SELECT * FROM " + DataB.NOM_TABLA_PEDIDOS;
        System.out.println("--- PEDIDOS ---");
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getString(1) + " | ID Producto: " + rs.getString(2) + " | Precio total del pedido: " + rs.getString(4) + "€");
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarProductosBaratos() {
        String query = "SELECT * FROM " + DataB.NOM_TABLA + " WHERE " + DataB.PRECIO + " < " + 600;
        System.out.println("--- PRODUCTOS INFERIORES A 600 €---");
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(5) + "€");
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ProductosFavoritos() {
        String query = "SELECT * FROM " + DataB.NOM_TABLA + " WHERE " + DataB.PRECIO + " > " + 1000;
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int idProducto = rs.getInt(ID);
                insertarProductosFavoritos(idProducto);
                System.out.println("Productos añadidos con éxito.");
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertarProductosFavoritos(int idProducto) {
        String insertQuery = "INSERT INTO " + DataB.NOM_TABLA_FAV + " (" + DataB.ID_PRODUCTO_FAV + ") VALUES ( ?)";

        try {
            PreparedStatement pst = connection.prepareStatement(insertQuery);
            pst.setInt(1, idProducto);
            pst.execute();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

