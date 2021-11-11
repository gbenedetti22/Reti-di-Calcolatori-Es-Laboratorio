package JSONManager;

import BankManager.ContoCorrente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class JSONFileCreator {
    private ArrayList<ContoCorrente> contiCorrenti;
    private final String BASE_PATH = "src/main/java/";

    public JSONFileCreator(ArrayList<ContoCorrente> contiCorrenti) {
        this.contiCorrenti = contiCorrenti;
    }

    public void create() {
        if (contiCorrenti.isEmpty())
            return;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringBuilder result = new StringBuilder();

        result.append(gson.toJson(contiCorrenti));

        try {
            writeToFile(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String s) throws IOException {
        FileOutputStream fos = new FileOutputStream(BASE_PATH + "conti.json");
        FileChannel channel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(s.length());
        buffer.put(s.getBytes());

        buffer.flip();
        channel.write(buffer);

        fos.close();
    }

}
