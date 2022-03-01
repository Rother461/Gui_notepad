
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class NoteFrame extends AbstractFrame implements Runnable, ActionListener {
    private static String imie;
    private static String nazwisko;
    private JMenuBar menuBar;
    private JMenu mPlik, mEdycja, mPomoc;
    private JMenuItem iNowy, iOtworz, iZapisz, iZamknij, iGodzinaData, iInormacje, iImieNazwisko, iTwojeDane,iZmienKolor;
    private JTextArea notatnik;
    private JComboBox nCzcionka;
    private JTextField rCzcionka;
    private JButton bZatwierdz;
    private File plik;
    private Color kolor;
    private CloseAction ca = new CloseAction();
    private ZmienWielkosc zw = new ZmienWielkosc();
    private NoteFrame parentFrame;

    public static void main(String[] args)   {
        EventQueue.invokeLater(new NoteFrame("Notatnik"));
    }

    public NoteFrame(String title){
        super(title);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        setSize(new Dimension(dim.width / 2, dim.height / 2));
        addWindowListener(new WindowClosingAdapter());

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        mPlik = new JMenu("Plik");
        mEdycja = new JMenu("Edycja");
        mPomoc = new JMenu("Pomoc");


        iNowy = new JMenuItem("Nowy");
        iNowy.addActionListener(this);
        iOtworz = new JMenuItem("Otwórz");
        iOtworz.addActionListener(this);
        iZapisz = new JMenuItem("Zapisz");
        iZapisz.addActionListener(this);
        iZamknij = new JMenuItem(ca);
        iZamknij.addActionListener(this);

        iGodzinaData = new JMenuItem("Godzina/Data");
        iGodzinaData.addActionListener(this);

        iImieNazwisko = new JMenuItem("Imię i Nazwisko");
        iImieNazwisko.addActionListener(this);

        iInormacje = new JMenuItem("Informacje");
        iInormacje.addActionListener(this);

        iTwojeDane = new JMenuItem("Dodaj podpis");
        iTwojeDane.addActionListener(this);

        iZmienKolor = new JMenuItem("Zmień kolor tekstu");

        iZmienKolor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kolor=JColorChooser.showDialog(parentFrame, "Wybierz kolor", Color.BLACK);
                notatnik.setForeground(kolor);
            }
        });

        bZatwierdz = new JButton(zw);


        nCzcionka = new JComboBox();
        rCzcionka = new JTextField(4);
        nCzcionka.setMaximumSize(new Dimension(200, 30));
        rCzcionka.setMaximumSize(new Dimension(40, 30));


        mPlik.add(iNowy);
        mPlik.add(iOtworz);
        mPlik.add(iZapisz);
        mPlik.add(iTwojeDane);
        mPlik.add(iZamknij);


        mEdycja.add(iGodzinaData);
        mEdycja.add(iZmienKolor);
        mEdycja.add(iImieNazwisko);


        mPomoc.add(iInormacje);

        menuBar.add(mPlik);
        menuBar.add(mEdycja);

        menuBar.add(mPomoc);
        menuBar.add(nCzcionka);
        menuBar.add(rCzcionka);
        menuBar.add(bZatwierdz);
        nCzcionka.addItem("Times roam");
        nCzcionka.addItem("Arial");
        nCzcionka.addItem("Georgia");
        nCzcionka.addItem("Helvetica");
        nCzcionka.addActionListener(this);

        notatnik = new JTextArea();
        JScrollPane scrol = new JScrollPane(notatnik);
        add(scrol);
        setJMenuBar(menuBar);
        notatnik.setFont(new Font(null, 1, 25));
    }

    @Override
    public void run() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object a = e.getSource();
        if (a == iZapisz) {
            zapisz();
        } else if (a == nCzcionka) {


            String czcionka = nCzcionka.getSelectedItem().toString();
            switch (czcionka) {
                case "Georgia" -> {
                    notatnik.setFont(new Font("georgia", 1, notatnik.getFont().getSize()));
                    notatnik.setLayout(null);
                    notatnik.setVisible(true);
                }
                case "Arial" -> {
                    notatnik.setFont(new Font("arial", 1, notatnik.getFont().getSize()));
                    notatnik.setLayout(null);
                    notatnik.setVisible(true);
                }
                case "Times roam" -> {
                    notatnik.setFont(new Font("TimesRoman", 1, notatnik.getFont().getSize()));
                    notatnik.setLayout(null);
                    notatnik.setVisible(true);
                }
                case "Helvetica" -> {
                    notatnik.setFont(new Font("Helvetica", 1, notatnik.getFont().getSize()));
                    notatnik.setLayout(null);
                    notatnik.setVisible(true);
                }
            }


        }
        else if(a==iInormacje)
        {
            JOptionPane.showMessageDialog(this,"Notatnik własności Hlawacza i Czajkowicza",
                    "Autorzy",JOptionPane.INFORMATION_MESSAGE);
        }
        else if (a==iOtworz)
        {
            otworz();
        }
        else if (a==iNowy)
        {
            if(notatnik.getText().equals(""))
            {
                notatnik.setText("");
            }
            else if(!notatnik.getText().isEmpty()) {
                int odp= JOptionPane.showConfirmDialog(null, "Czy chcesz zapisać?", "Pytanie", JOptionPane.YES_NO_OPTION);
                if(odp == JOptionPane.YES_OPTION)
                {
                    zapisz();
                    notatnik.setText("");
                }
                if(odp == JOptionPane.NO_OPTION)
                {
                    notatnik.setText("");
                }
                if(odp == JOptionPane.CANCEL_OPTION)
                {
                    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }

            }
        }
        else if (a==iGodzinaData)
        {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            notatnik.setText(notatnik.getText()+"\n"+formatter.format(date));
        }
        else if(a==iImieNazwisko)
        {
    if(imie == null || nazwisko == null)
        throw new NullPointerException("Nie podano wartosci!");
            try {

                imie_nazwisko(imie,nazwisko);
            } catch (liczba myException) {

            }
        }
        else if(a==iTwojeDane)
        {
            imie = JOptionPane.showInputDialog("Wpisz imię");
            nazwisko = JOptionPane.showInputDialog("Wpisz nazwisko");
        }
    }

    class ZmienWielkosc extends AbstractAction implements GetMessage {
        public ZmienWielkosc(){
            putValue(Action.NAME, "Zmien");
        }
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                String val;
                val = rCzcionka.getText();
                int n = Integer.parseInt(val);
                Font font1 = new Font(null, 1, n);
                notatnik.setFont(font1);
            }
            catch(Exception ex) {
                showBadMessage();
            }
        }

        @Override
        public void showBadMessage() {
            JOptionPane.showMessageDialog(getFrame(), "Wprowadzono niepoprawną wartość");
        }
    }

    class WindowClosingAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int odp= JOptionPane.showConfirmDialog(null, "Czy na pewno wyjsc", "Pytanie", JOptionPane.YES_NO_OPTION);
            if(odp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            if(odp == JOptionPane.NO_OPTION) {
                setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }

            if(odp == JOptionPane.CANCEL_OPTION) {
                setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }
        }
    }

    class CloseAction extends AbstractAction {
        public CloseAction() {
            putValue(Action.NAME, "Zamknij");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Z"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NoteFrame kf = getFrame();
            kf.dispatchEvent(new WindowEvent(kf, WindowEvent.WINDOW_CLOSING));
        }
    }


    private NoteFrame getFrame() {
        return this;
    }

    private void zapisz() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            plik = fc.getSelectedFile();
            try {
                PrintWriter pw = new PrintWriter(plik);
                Scanner skaner = new Scanner(notatnik.getText());
                while (skaner.hasNext()) {
                    pw.println(skaner.nextLine() + "\n");
                }
                pw.close();
                skaner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void otworz() {
        notatnik.setText("");
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            plik = fc.getSelectedFile();
            try {
                Scanner scanner = new Scanner(plik);
                while (scanner.hasNext())
                    notatnik.append(scanner.nextLine() + "\n");
                scanner.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
    }

    private void imie_nazwisko(String imie, String nazwisko) throws liczba
    {
        for (char c : imie.toCharArray()) {
            if (Character.isDigit(c))
                throw new liczba();
        }
        for (char c : nazwisko.toCharArray()) {
            if (Character.isDigit(c))
                throw new liczba();
        }
        notatnik.setText(notatnik.getText()+"\n"+"Autor notatki:"+imie+" "+nazwisko);
    }

}
class liczba extends Exception{
    public liczba() {
        JOptionPane.showMessageDialog(null, "Wprowadzono niepoprawną wartość");
    }

}