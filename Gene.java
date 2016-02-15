import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Stan on 1-2-2016.
 */
public class Gene implements Comparable {

    public int geneID;
    public String geneSymbol;
    public int tax_id;
    public String description;

    public Gene(int geneID, String geneSymbol, int tax_id, String description) {

        this.geneID = geneID;
        this.geneSymbol = geneSymbol;
        this.tax_id = tax_id;
        this.description = description;

    }

    //Getters voor de properties van een Gene object.
    public int getGeneID() {
        return geneID;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public int getTax_id() {
        return tax_id;
    }

    public String getDescription() {
        return description;
    }


    //CompareTo voor vergelijking van Gene objecten op basis van GeneID.
    @Override
    public int compareTo(Object o) {

        Gene g = (Gene) o;

        if (g.getGeneID() > this.getGeneID()) {

            return -1;

        }

        else if (g.getGeneID() < this.getGeneID()) {

            return +1;

        }

        else {

            return 0;

        }
    }


    /**
     * Summary : Sorteert een lijst met genen op basis van GeneID.
     *
     * @param genes, ArrayList<Gene>.
     *
     * @return (gesorteerde) ArrayList<Gene>.
     */
    public static ArrayList<Gene> sort(ArrayList<Gene> genes) {

        int count = 0;

        while (count < genes.size() - 1) {

            if (genes.get(count).compareTo(genes.get(count + 1)) < 0) {

                count++;

            }

            else if (genes.get(count).compareTo(genes.get(count + 1)) > 0) {

                Collections.swap(genes, count, count + 1);

            }

            else {

                count++;

            }
        }

        if (!isSorted(genes)) {

            sort(genes);

        }

        return genes;
    }

    /**
     * Summary : Checkt of een ArrayList<Gene> op GeneID gesorteerd is.
     *
     * @param genes, ArrayList<Gene>.
     *
     * @return Boolean, true als de lijst gesorteerd is, false als dat niet zo is.
     */
    public static Boolean isSorted(ArrayList<Gene> genes) {

        int count = 0;

        while (count < genes.size() - 1) {

            if (genes.get(count).compareTo(genes.get(count + 1)) > 0) {

                return false;

            }

            count++;

        }

        return true;

    }
}
