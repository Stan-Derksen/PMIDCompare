import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Stan on 1-2-2016.
 */
public class Data {

    //BufferedReader om bestanden te lezen.
    private static BufferedReader infile1;

    //Boolean flag, wordt op true gezet als de HashmMap gevuld is.
    public static Boolean flag = false;
    public static Boolean flag2 = false;

    /**
     * Summary : Leest het bestand taxID_to_organism_names.txt en maakt een TreeMap<String organisme, Integer tax_id>.
     *
     * @return TreeMap<String organisme, Integer tax_id>.
     */
    public static TreeMap<String, Integer> fillMapID_Organism() {

        String line;
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();

        try {

            try {

                infile1 = new BufferedReader(new FileReader("taxID_to_organism_names.txt"));

            } catch (FileNotFoundException e) {

                System.out.println("Bestand niet gevonden.");

            }

            while ((line = infile1.readLine()) != null) {

                map.put(line.split("\t")[1], Integer.parseInt(line.split("\t")[0]));

            }

            infile1.close();

        } catch (IOException e) {

            System.out.println("Er is een IO fout opgetreden");

        }

        flag2 = true;
        System.out.println("Map 1 done");
        return map;

    }

    /**
     * Summary : Leest het bestand gene2pubmed en maakt 2 HashMaps: {Integer tax_id : HashSet<Integer PubMedID's>}, {Integer PubMedID : HashSet<Integer GeneID's>}.
     *
     * @return HashMap[] met 2 HashMaps (zie Summary)
     */
    public static HashMap[] fillTaxPubmed() {

        HashMap[] mapCollection = new HashMap[2];

        HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();

        HashMap<Integer, HashSet<Integer>> map2 = new HashMap<Integer, HashSet<Integer>>();

        String line;

        HashSet<Integer> set = new HashSet<Integer>();


        int tax_id = 0;


        try {

            try {

                infile1 = new BufferedReader(new FileReader("gene2pubmed"));

            } catch (FileNotFoundException e) {

                System.out.println("Bestand niet gevonden.");

            }

            while ((line = infile1.readLine()) != null) {

                HashSet<Integer> set2 = new HashSet<Integer>();

                if (map2.containsKey(Integer.parseInt(line.split("\t")[2]))) {

                    set2 = map2.get(Integer.parseInt(line.split("\t")[2]));
                    set2.add(Integer.parseInt(line.split("\t")[1]));
                    map2.put(Integer.parseInt(line.split("\t")[2]), set2);

                }

                else {

                    set2.add(Integer.parseInt(line.split("\t")[1]));
                    map2.put(Integer.parseInt(line.split("\t")[2]), set2);

                }

                if (Integer.parseInt(line.split("\t")[0]) == tax_id || tax_id == 0) {

                    set.add(Integer.parseInt(line.split("\t")[2]));

                } else {

                    map.put(tax_id, set);

                    set = new HashSet<Integer>();

                    set.add(Integer.parseInt(line.split("\t")[2]));

                }

                tax_id = Integer.parseInt(line.split("\t")[0]);

            }

            map.put(tax_id, set);

            infile1.close();

        } catch (IOException e) {

            System.out.println("Er is een IO fout opgetreden");

        }

        mapCollection[0] = map;
        mapCollection[1] = map2;

        flag = true;
        System.out.println("Map 2 & 3 done");

        return mapCollection;
    }
}
