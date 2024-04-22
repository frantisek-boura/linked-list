package procesy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import abstrdoublelist.AbstrDoubleList;
import abstrdoublelist.AbstrDoubleListException;
import abstrlifo.AbstrLifo;
import abstrlifo.AbstrLifoException;
import abstrlifo.IAbstrLifo;
import enums.EPozice;
import enums.EReorg;

public class VyrobniProces implements IVyrobniProces {

    private AbstrDoubleList<Proces> data;

    public VyrobniProces() {
        data = new AbstrDoubleList<Proces>();
    }

    @Override
    public void generatorDat(int pocet) {
        if (pocet <= 0) {
            throw new IllegalArgumentException("Počet náhodně generovaných dat musí být větší než 0");
        }

        // vymazani aktualne pouzivanych dat
        data.zrus();

        // ciselne hranice jsem urcil podle vzoroveho souboru import.csv
        final int MIN_POCET_OSOB = 2;
        final int MAX_POCET_OSOB = 8;
        final int MIN_CAS_PROCESU = 2;
        final int MAX_CAS_PROCESU = 30;

        Random RND = new Random();
        RND.setSeed(new Date().getTime());

        Proces proces;
        String id;
        int pocetOsob;
        int casProcesu;

        int pocetManual = 0;
        int pocetRobot = 0;

        for (int i = 0; i < pocet; i++) {
            pocetOsob = RND.nextInt(MAX_POCET_OSOB - MIN_POCET_OSOB) + MIN_POCET_OSOB;
            casProcesu = RND.nextInt(MAX_CAS_PROCESU - MIN_CAS_PROCESU) + MIN_CAS_PROCESU;

            if (RND.nextInt(2) >= 1) {
                pocetManual++;

                id = "O1" + "0".repeat(2 - Integer.toString(pocetManual).length()) + pocetManual;
                proces = new ProcesManualni(id, pocetOsob, casProcesu);
                data.vlozPosledni(proces);
            } else {
                pocetRobot++;
            
                id = "R1" + "0".repeat(2 - Integer.toString(pocetRobot).length()) + pocetRobot;
                proces = new ProcesRoboticky(id, casProcesu);
                data.vlozPosledni(proces);
            }
        }
    }

    @Override
    public String exportDat() {
        StringBuilder fileContent = new StringBuilder();
        fileContent.append("IdProc;persons;time\n");

        Iterator<Proces> iterator = iterator();
        while (iterator.hasNext()) {
            Proces p = iterator.next();
            
            if (p instanceof ProcesManualni pm) {
                fileContent.append(pm.getId() + ";" + pm.getPocetOsob() + ";" + pm.getCasProcesu() + "\n");
            } else if (p instanceof ProcesRoboticky pr) {
                fileContent.append(pr.getId() + ";" + 0 + ";" + pr.getCasProcesu() + "\n");
            }
        }

        return fileContent.toString();
    }

    @Override
    public int importDat(String soubor) throws VyrobniProcesException, FileNotFoundException {
        if (soubor.isEmpty()) {
            throw new IllegalArgumentException("Vybraný soubor neexistuje");
        }

        if (!Files.exists(Paths.get(soubor))) {
            throw new FileNotFoundException("Vybraný soubor neexistuje");
        }

        this.data.zrus();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(soubor));
            ) {

            //skip hlavicky csv souboru
            reader.readLine(); 
            String radek;
            int pocet = 0;

            while ((radek = reader.readLine()) != null) {
                if (radek.length() == 0 || radek.isEmpty()) break;

                String[] radekData = radek.split(";");
                Proces proces;
                
                if (radekData[0].charAt(0) == 'R') {
                    proces = new ProcesRoboticky(radekData[0], Integer.parseInt(radekData[2]));
                } else {
                    proces = new ProcesManualni(radekData[0], Integer.parseInt(radekData[2]), Integer.parseInt(radekData[1]));
                }
                
                data.vlozPosledni(proces);
                pocet++;
            }
            return pocet;
        } catch (IOException | NumberFormatException e) {
            data.zrus();
            throw new VyrobniProcesException("Importovaná data ve špatném formátu");
        }

    }

    @Override
    public void vlozProces(Proces proces, EPozice pozice) throws VyrobniProcesException {
        if (pozice == EPozice.AKTUALNI) {
            throw new IllegalArgumentException("Nelze vložit na aktuální pozici");
        }

        if (pozice == null) {
            throw new NullPointerException("Špatně vybraná pozice");
        }

        try {
            switch (pozice) {
                case NASLEDNIK -> {
                    data.vlozNaslednika(proces);
                }
                case POSLEDNI -> {
                    data.vlozPosledni(proces);
                }
                case PREDCHUDCE -> {
                    data.vlozPredchudce(proces);
                }
                case PRVNI -> {
                    data.vlozPosledni(proces);
                }
                case AKTUALNI -> {
                    // sem se to nikdy nedostane diky podmince na zacatku metody, 
                    // ale IDE se nelibi ze switch nema pri vyhodnocovani enumu vsechny pripady k dispozici
                }
            }
        } catch (AbstrDoubleListException e) {
            throw new VyrobniProcesException("Nelze vložit následníka/předchůdce, protože nebyl nastaven aktuální prvek, nebo je seznam prázdný");
        }
        
    }

    @Override
    public Proces zpristupniProces(EPozice pozice) throws VyrobniProcesException {
        if (pozice == null) {
            throw new NullPointerException("Špatně vybraná pozice");
        }

        try {
            Proces proces = null;

            switch (pozice) {
                case NASLEDNIK -> {
                    proces = data.zpristupniNaslednika();
                }
                case POSLEDNI -> {
                    proces = data.zpristupniPosledni();
                }
                case PREDCHUDCE -> {
                    proces = data.zpristupniPredchudce();
                }
                case PRVNI -> {
                    proces = data.zpristupniPrvni();
                }
                case AKTUALNI -> {
                    proces = data.zpristupniAktualni();
                }
            }

            return proces;
        } catch (AbstrDoubleListException e) {
            throw new VyrobniProcesException("Nelze zpřístupnit následníka/předchůdce, protože nebyl nastaven aktuální prvek, nebo je seznam prázdný");
        }
    }

    @Override
    public Proces odeberProces(EPozice pozice) throws VyrobniProcesException {
        if (pozice == null) {
            throw new NullPointerException("Nebyla vybrána pozice");
        }

        try {
            Proces proces = null;

            switch (pozice) {
                case NASLEDNIK -> {
                    proces = data.odeberNaslednika();
                }
                case POSLEDNI -> {
                    proces = data.odeberPosledni();
                }
                case PREDCHUDCE -> {
                    proces = data.odeberPredchudce();
                }
                case PRVNI -> {
                    proces = data.odeberPrvni();
                }
                case AKTUALNI -> {
                    proces = data.odeberAktualni();
                }
            }

            return proces;
        } catch (AbstrDoubleListException e) {
            throw new VyrobniProcesException("Nelze odebrat následníka/předchůdce, protože nebyl nastaven aktuální prvek, nebo je seznam prázdný");
        }
    }

    @Override
    public Iterator<Proces> iterator() {
        return data.iterator();
    }

    @Override
    public IAbstrLifo<Proces> vytipujKandidatiReorg(int cas, EReorg reorgan) throws VyrobniProcesException {
        if (reorgan == null) {
            throw new NullPointerException("Špatně zadaný zásobník kandidátů");
        }

        if (cas <= 0) {
            throw new IllegalArgumentException("Časové kritérium musí být větší než 0");
        }

        AbstrLifo<Proces> zasobnik = new AbstrLifo<Proces>();
        Iterator<Proces> iterator = data.iterator();

        switch (reorgan) {
            case AGREGACE -> {
                Proces p1 = null;
                Proces p2 = null;
    
                while (iterator.hasNext()) {
                    p2 = p1;
                    p1 = iterator.next();

                    if (!(p1 instanceof ProcesManualni) || !(p2 instanceof ProcesManualni)) {
                        continue;
                    }

                    if (p1.getCasProcesu() <= cas && p2.getCasProcesu() <= cas) {
                        zasobnik.vloz(p2);
                        zasobnik.vloz(p1);

                        p1 = null;
                        p2 = null;
                    }
                }
            }
            case DEKOMPOZICE -> {
                while (iterator.hasNext()) {
                    Proces proces = iterator.next();

                    if (!(proces instanceof ProcesManualni)) {
                        continue;
                    }
    
                    if (proces.getCasProcesu() >= cas) {
                        zasobnik.vloz(proces);
                    }
                }
            }
        }

        return zasobnik;
    }

    @Override
    public void reorganizace(EReorg reorgan, IAbstrLifo<Proces> zasobnik) throws VyrobniProcesException {
        if (reorgan == null || zasobnik == null) {
            throw new NullPointerException();
        }

        switch (reorgan) {
            case AGREGACE -> {
                Proces p1;
                Proces p2;

                try {
                    p1 = zasobnik.odeber();
                    p2 = zasobnik.odeber();
                    while (!zasobnik.jePrazdny()) {
                        if (p1 instanceof ProcesManualni pm1 && p2 instanceof ProcesManualni pm2) {
                            int pocetOsob = pm1.getPocetOsob() + pm2.getPocetOsob();
                            int casProcesu = (pm1.getCasProcesu() + pm2.getCasProcesu()) / 4;
                            ProcesManualni novyProces = new ProcesManualni(p1.getId(), pocetOsob, casProcesu);

                            data.zpristupniPrvni();
                            while (data.zpristupniAktualni() != p1) {
                                data.zpristupniNaslednika();
                            }

                            data.odeberNaslednika();
                            data.odeberAktualni();
                            data.vlozPosledni(novyProces);
                        }
                    }
                } catch (AbstrLifoException | AbstrDoubleListException e) {
                    throw new VyrobniProcesException("Poskytnutý zásobník s kandidáty nemá sudý počet procesů nebo je prázdný");
                };

                


            }
            case DEKOMPOZICE -> {
                try {
                    data.zpristupniPosledni();
                    while (!(data.zpristupniAktualni() instanceof ProcesManualni)) {
                        data.zpristupniPredchudce();
                    }

                    int startId = Integer.parseInt(data.zpristupniAktualni().getId().substring(1)) + 1;

                    Proces proces;
                    while (!zasobnik.jePrazdny()) {
                        proces = zasobnik.odeber();

                        if (proces instanceof ProcesManualni pm) {
                            int pocetOsob1 = (pm.getPocetOsob() / 2) + (pm.getPocetOsob() % 2);
                            int pocetOsob2 = (pm.getPocetOsob() / 2);
                            int casProcesu1 = (pm.getCasProcesu() / 2) + (pm.getCasProcesu() % 2);
                            int casProcesu2 = (pm.getCasProcesu() / 2) + (pm.getCasProcesu() % 2);

                            ProcesManualni p1 = new ProcesManualni("O" + (startId++), pocetOsob1, casProcesu1);
                            ProcesManualni p2 = new ProcesManualni("O" + (startId++), pocetOsob2, casProcesu2);

                            data.zpristupniPrvni();
                            while (data.zpristupniAktualni() != proces) {
                                data.zpristupniNaslednika();
                            }

                            data.odeberAktualni();
                            data.vlozPosledni(p1);
                            data.vlozPosledni(p2);
                        }
                    }
                } catch (AbstrDoubleListException e) {
                    throw new VyrobniProcesException("Seznam procesů je prázdný");
                } catch (AbstrLifoException e) {
                    throw new VyrobniProcesException("Poskytnutý zásobník s kandidáty je prázdný");
                }

            }
        }
    }

    @Override
    public void zrus() {
        data.zrus();
    }
}
