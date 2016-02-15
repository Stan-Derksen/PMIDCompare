import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Created by Stan on 1-2-2016.
 */

public class PMCApp extends JFrame implements ActionListener {
    private JPanel panel1;
    private JLabel lblTaxonomy;
    public JComboBox<String> cmbOrganisme1;
    public JComboBox<String> cmbOrganisme2;
    public JTextArea txtOutput;
    private JPanel pnlPiechart;
    private JLabel lblPMID;
    private JComboBox cmbPMID;
    private JPanel pnlTable;


    /**
     * Summary : Constructor van de GUI.
     */

    public PMCApp() {

        cmbOrganisme1.addActionListener(this);
        cmbOrganisme2.addActionListener(this);
        cmbPMID.addActionListener(this);

        add(panel1);

        setPreferredSize(new Dimension(2000, 1000));
        pack();
        setVisible(false);

    }

    /**
     * Summary : Creeert een JFrame dat als laadscherm gebruikt wordt tijdens het laden van data.
     *
     * @return JFrame loading screen.
     */
    public JFrame showLoadingScreen() {

        setEnabled(false);

        JFrame frame = new JFrame("Loading");
        JLabel label = new JLabel("Data wordt geladen, een moment geduld alstublieft ...");

        frame.add(label);

        frame.setPreferredSize(new Dimension(400, 200));

        frame.validate();
        frame.pack();
        frame.setVisible(true);

        return frame;

    }

    /**
     * Summary : Sluit een JFrame, bedoeld om een laadscherm te sluiten.
     *
     * @param frame, JFrame.
     */
    public void closeLoadingScreen(JFrame frame) {

        setEnabled(true);
        setVisible(true);

        frame.dispose();

    }

    /**
     * Summary : Creeert een tabel met de meegegeven data.
     *
     * @param data, tweedimensionaal object om de tabel te vullen.
     */
    private void createTable(Object[][] data) {

        String[] columnNames = {"Tax ID", "Gene ID", "Description", "Symbol"};

        JTable tblResults = new JTable(data, columnNames);

        pnlTable.setLayout(new BorderLayout());

        pnlTable.add(new JScrollPane(tblResults));

        tblResults.setFillsViewportHeight(true);

        pnlTable.validate();

    }

    /**
     * Summary : Vult de 2 organisme comboboxes met de ingelezen organismen.
     *
     * @param map, TreeMap {String organisme : Integer tax_id}.
     */
    public void fillOrganismComboboxes(TreeMap<String, Integer> map ) {

        for (String s : map.keySet()) {

            cmbOrganisme1.addItem(s);
            cmbOrganisme2.addItem(s);

        }
    }

    /**
     * Summary : Vult de PubMedID combobox met de overlap in PubMedID's tussed de twee organismen.
     *
     * @param setPMIDs, HashSet<Integer overeenkomstige PubMedID's>.
     */
    public void fillPMIDComboBoxes(HashSet<Integer> setPMIDs) {

        cmbPMID.removeAllItems();

        for (Integer pmid : setPMIDs) {

            cmbPMID.addItem(pmid);

        }
    }

    /**
     * Summary : Creeert een piechart met een lijst met int's.
     *           In de lijst zitten 3 waardes, aantal PubMedID's organisme 1, aantal PubMedID's organisme 2 en aantal PubMedID's die overlappen.
     *
     * @param results, int[] met 3 waardes.
     */
    private void drawPieChart(int[] results) {

        pnlPiechart.removeAll();
        pnlPiechart.setLayout(new java.awt.BorderLayout());

        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue(cmbOrganisme1.getSelectedItem().toString(), new Double(results[0]));
        dataset.setValue(cmbOrganisme2.getSelectedItem().toString(), new Double(results[1]));
        dataset.setValue("Overeenkomst", new Double(results[2]));

        JFreeChart chart = ChartFactory.createPieChart3D("Piechart", dataset, true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));


        ChartPanel chartPanel = new ChartPanel(chart);

        pnlPiechart.add(chartPanel);

        pnlPiechart.validate();

    }

    /**
     * Summary : Schrijft de resultaten van de vergelijking van de PubMedID's naar een textveld en roept drawPieChart aan.
     *
     */
    public void writeResults() {

        String org1;
        String org2;
        int[] aantalPMIDs;

        txtOutput.setText("De gevonden PubMed artikelen voor de geselecteerde organismen:\n");

        org1 = cmbOrganisme1.getSelectedItem().toString();
        org2 = cmbOrganisme2.getSelectedItem().toString();

        aantalPMIDs = AnalysePubMed.compare(this);

        txtOutput.append(org1 + " is in " + aantalPMIDs[0] + " PubMed artikelen gevonden.\n");
        txtOutput.append(org2 + " is in " + aantalPMIDs[1] + " PubMed artikelen gevonden.\n");
        txtOutput.append(org1 + " en " + org2 + " worden in " + aantalPMIDs[2] + " PubMed artikelen samen genoemd.\n");

        drawPieChart(aantalPMIDs);

    }

    /**
     * Summary : Maakt de tweedimensionale lijst en roept createTable aan.
     *
     */
    public void fillTableResults() {

        JFrame frame = showLoadingScreen();

        ArrayList<Gene> listGenes = Gene.sort(new ArrayList<Gene>(AnalysePubMed.findGenesInPMID ((Integer) cmbPMID.getSelectedItem())));

        Object[][] data = new Object[listGenes.size()][4];

        int count = 0;

        for (Gene gene : listGenes) {

            data[count][0] = gene.getTax_id();
            data[count][1] = gene.getGeneID();
            data[count][2] = gene.getDescription();
            data[count][3] = gene.getGeneSymbol();

            count++;

        }

        createTable(data);

        closeLoadingScreen(frame);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(cmbOrganisme1) || e.getSource().equals(cmbOrganisme2)) {

            if (Data.flag && Data.flag2 && cmbOrganisme2.getSelectedItem() != null && cmbOrganisme1.getSelectedItem() != null) {

                pnlTable.removeAll();
                writeResults();

            }
        }

        else if (e.getSource().equals(cmbPMID) && cmbPMID.getSelectedItem() != null) {

            pnlTable.removeAll();
            fillTableResults();

        }
    }
}
