package firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

public class FirebaseController {

    private FirebaseDatabase firebaseDatabase;

    public FirebaseController() {
        initFirebase();
    }

    private void initFirebase() {
        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://prueba-esp-a7c2a.firebaseio.com")
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\elpow\\OneDrive\\Escritorio\\firebase\\pruebas-8b4d7-firebase-adminsdk-s8bpl-50f58698d7.json")))
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("Conexión a Firebase realizada exitosamente...");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void saveItem(Item item, String childPath) {
        if (item != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference(childPath);

            CountDownLatch countDownLatch = new CountDownLatch(1);

            // Guardar el registro en Firebase
            databaseReference.push().setValue(item, (de, dr) -> {
                if (de == null) {
                    System.out.println("Registro guardado correctamente.");
                } else {
                    System.err.println("Error al guardar el registro: " + de.getMessage());
                }
                countDownLatch.countDown();
            });

            try {
                countDownLatch.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void readItem(String childPath) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(childPath);

        // Leer registros desde Firebase
        // Por ejemplo, puedes usar addListenerForSingleValueEvent para obtener los datos.
        // Aquí solo muestro un mensaje de ejemplo:
        System.out.println("Leyendo registros desde Firebase...");
    }

    public void updateItem(String childPath, String itemId, Item updatedItem) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(childPath);

        // Actualizar un registro específico en Firebase
        // Puedes usar updateChildren o setValue según tus necesidades.
        // Aquí solo muestro un mensaje de ejemplo:
        System.out.println("Actualizando registro en Firebase...");
    }

    public static void main(String[] args) {
        Item item = new Item();
        item.setId(1L);
        item.setName("celu_2");
        item.setPrice(100.156);

        FirebaseController controller = new FirebaseController();
        controller.saveItem(item, "CarpetaPrueba");

        // Ejemplo de lectura:
        controller.readItem("CarpetaPrueba");

        // Ejemplo de actualización:
        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("celu_2_actualizado");
        updatedItem.setPrice(150.0);
        controller.updateItem("CarpetaPrueba", "clave_del_registro_a_actualizar", updatedItem);
    }
}
