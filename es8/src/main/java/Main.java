import BankManager.Causale;
import BankManager.ContoCorrente;
import BankManager.Movimento;
import JSONManager.JSONFileCreator;
import JSONManager.JSONFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final int N_ACCOUNTS = 10;
    private static final int N_MOVEMENTS = 100;

    private static final String[] names = {"Pippo", "Matteo", "Gabriele", "Lorenzo", "Gianmarco", "Augusto", "Ercole", "Dedalo",
            "Topolino", "Walt"};
    private static final String[] surnames = {"Baudo", "Marini", "Lumi", "Carrara", "Esposito", "Imperatore", "Greek", "Icaro",
            "Minni", "Disney"};

    public static void main(String[] args) {
        ArrayList<ContoCorrente> conti = getBankAccounts();

        // ================================================================
        JSONFileCreator jfileCreator = new JSONFileCreator(conti);
        jfileCreator.create();
        // ================================================================

        JSONFileReader reader = new JSONFileReader();
        try {
            reader.readAndCount();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file JSON");
            return;
        }


        String[] result = reader.getResult();
        for (String s : result) {
            System.out.println(s);
        }

    }

    private static ArrayList<ContoCorrente> getBankAccounts() {
        ArrayList<ContoCorrente> conti = new ArrayList<>(N_ACCOUNTS);
        Random random = new Random();

        for (int i = 0; i < N_ACCOUNTS; i++) {
            ContoCorrente cc = new ContoCorrente(names[random.nextInt(names.length)],
                    surnames[random.nextInt(surnames.length)]);

            for (int j = 0; j < random.nextInt(N_MOVEMENTS); j++) {
                Causale randomCausale = Causale.values()[random.nextInt(Causale.values().length)];
                cc.addMovimento(Movimento.newMovimento(randomCausale));
            }

            conti.add(cc);
        }

        return conti;
    }
}
