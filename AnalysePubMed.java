import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by Stan on 1-2-2016.
 */
public class AnalysePubMed {

    //HashMaps om gegevens uit gelezen bestanden op te slaan
    public static TreeMap<String, Integer> mapTaxID_Organism;
    public static HashMap[] mapsTaxIDPMIDs_PMIDGeneIDs;
    public static HashMap<Integer, Gene> mapGenes;

    //BufferedReader om bestanden te lezen.
    private static BufferedReader infile1;


    public static void main(String[] args) {

        //Initialisatie van de GUI
        PMCApp gui = new PMCApp();

        //Initialisatie van het laadscherm terwijl data geladen wordt.
        JFrame frame = gui.showLoadingScreen();

        //Vullen van de HashMaps {Integer tax ID : HashSet<Integer> PubMed ID's} (positie 0) en {Integer PubMed ID : HashSet<Integer> GeneID's} (positie 1)
        mapsTaxIDPMIDs_PMIDGeneIDs = Data.fillTaxPubmed();

        //Vullen van de HashMap {String organisme : Integer tax ID}
        mapTaxID_Organism = Data.fillMapID_Organism();

        //Vullen van de comboboxes met de namen van de organismen uit de eerder gemaakte HashMap
        gui.fillOrganismComboboxes(mapTaxID_Organism);

        //Verwijderen van laadscherm nadat data geladen is.
        gui.closeLoadingScreen(frame);

    }

    /**
     * Haalt op basis van de organismen in de comboboxes de PubMedID's gerelateerd aan de organismen op en rekent de overlap uit.
     *
     * @param gui, instantie van PMCApp.
     *
     * @return int[] met 3 waardes, aantal PubMedID's organisme 1 & 2 en aantal PubMedID's dat overlapt.
     */
    public static int[] compare(PMCApp gui) {

        int[] results = new int[3];

        HashSet<Integer> pubMedIDsOrg1;
        HashSet<Integer> pubMedIDsOrg2;

        HashSet<Integer> intersectionPMIDs;

        pubMedIDsOrg1 = (HashSet) mapsTaxIDPMIDs_PMIDGeneIDs[0].get(mapTaxID_Organism.get(gui.cmbOrganisme1.getSelectedItem()));
        pubMedIDsOrg2 = (HashSet) mapsTaxIDPMIDs_PMIDGeneIDs[0].get(mapTaxID_Organism.get(gui.cmbOrganisme2.getSelectedItem()));


        if (pubMedIDsOrg1 != null && pubMedIDsOrg2 != null) {

            intersectionPMIDs = (HashSet) pubMedIDsOrg1.clone();
            intersectionPMIDs.retainAll(pubMedIDsOrg2);

            results[2] = intersectionPMIDs.size();

            gui.fillPMIDComboBoxes(intersectionPMIDs);
        }

        else {

            results[2] = 0;

        }

        if (pubMedIDsOrg1 != null) {

            results[0] = pubMedIDsOrg1.size();

        }

        else {

            results[0] = 0;

        }

        if (pubMedIDsOrg2 != null) {

            results[1] = pubMedIDsOrg2.size();

        }

        else {

            results[1] = 0;

        }

        return results;

    }

    /**
     * Summary : Haalt op basis van meegegeven PubMedID de GeneID's die hierbij horen op, opent de gene_info file, en maakt Gene objecten van de genen die in het PubMedID gneoemd worden.
     *
     * @param pmid Integer, een PubMedID.
     *
     * @return HashSet<gene> met genen die in de meegegeven PubMedID voorkomen.
     */
    public static HashSet<Gene> findGenesInPMID(Integer pmid) {

        HashSet set2 = (HashSet) mapsTaxIDPMIDs_PMIDGeneIDs[1].get(pmid);

        HashSet<Gene> set = new HashSet<Gene>();
        String line;

        try {

            infile1 = new BufferedReader(new FileReader("gene_info"));

            infile1.readLine();


            while ((line = infile1.readLine()) != null) {

                if (set2.contains(Integer.parseInt(line.split("\t")[1]))) {

                    set.add(new Gene(Integer.parseInt(line.split("\t")[1]),
                            line.split("\t")[2], Integer.parseInt(line.split("\t")[0]), line.split("\t")[7]));

                }
            }
        }

        catch (IOException e) {

            System.out.println("IOException");

        }

        return set;
    }
}
