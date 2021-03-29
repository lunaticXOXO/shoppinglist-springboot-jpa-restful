package shoppinglist;

import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaRepo;
import shoppinglist.service.ShoppingListService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class DemoShoppingListSpringBootApplication implements CommandLineRunner {
    @Autowired
    private DaftarBelanjaRepo repo;
    private ShoppingListService svc;

    public static void main(String[] args) {
        SpringApplication.run(DemoShoppingListSpringBootApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Membaca Semua Record DaftarBelanja:");
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            System.out.println("[" + db.getId() + "] " + db.getJudul());

            List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : listBarang) {
                System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        }

        Scanner keyb = new Scanner(System.in);

        // Baca berdasarkan ID
        System.out.print("Masukkan ID dari objek DaftarBelanja yg ingin ditampilkan: ");
        long id = Long.parseLong(keyb.nextLine());
        System.out.println("Hasil pencarian: ");

        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("\tJudul: " + db.getJudul());
        } else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }
    }

    //versi lokal
    public void CariDaftarBarangBerdasarkanJudul() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Membaca Record DaftarBelanja Berdasarkan Judul:");
        System.out.print("Masukkan Judul : ");
        String Judul = scan.nextLine();
        System.out.println("Hasil pencarian: ");
        DaftarBelanja tempDb = null;
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            if (db.getJudul().equals(Judul)) {
                tempDb = db;
            }
        }
        if (tempDb != null) {
            System.out.println("Judul : " + tempDb.getJudul());
            List<DaftarBelanjaDetil> listDbd = tempDb.getDaftarBarang();
            System.out.println("   DetailDaftarBelanja :");
            for (DaftarBelanjaDetil dbd : listDbd) {
                System.out.println("\t Nama Barang : " + dbd.getNamaBarang() + "\n\t Jumlah : " +
                        dbd.getByk() + "\n\t Satuan : " + dbd.getSatuan());
            }

        } else {
            System.out.println("Data tidak ditemukan");
        }
    }

    public void TambahDaftarBelanja() {
        LocalDateTime tglBuat = LocalDateTime.now().withNano(0);
        Scanner scan = new Scanner(System.in);
        System.out.println("Tambah Record DaftarBelanja");
        System.out.print("Masukkan Judul : ");
        String Judul = scan.nextLine();
        DaftarBelanja db = new DaftarBelanja();
        db.setJudul(Judul);
        db.setTanggal(tglBuat);
        repo.save(db);
        System.out.println("Berhasil Dtambahkan");
    }

    public void PerbaruiDaftarBelanja() {
        LocalDateTime tglBuat = LocalDateTime.now().withNano(0);
        Scanner scan = new Scanner(System.in);
        System.out.println("Update Recprd Daftar Belanja:");
        System.out.print("Masukkan ID : ");
        String tempId = scan.nextLine();
        long id = Long.parseLong(tempId);
        System.out.println("Hasil pencarian: ");
        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("Judul : " + db.getJudul());
            List<DaftarBelanjaDetil> listDbd = db.getDaftarBarang();
            System.out.println("   DetailDaftarBelanja :");
            for (DaftarBelanjaDetil dbd : listDbd) {
                System.out.println("\t Nama Barang : " + dbd.getNamaBarang() + "\n\t Jumlah : " +
                        dbd.getByk() + "\n\t Satuan : " + dbd.getSatuan());
            }
            System.out.println("Masukan Data Update");
            System.out.print("Masukan Judul : ");
            String Judul = scan.nextLine();
            db.setJudul(Judul);
            db.setTanggal(tglBuat);
            repo.save(db);
            System.out.println("Berhasil Diupdate");

        } else {
            System.out.println("\tData tidak ditemukan.");

        }
    }

    public void HapusDaftarBelanja() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hapus DaftarBelanja:");
        System.out.print("Masukkan ID : ");
        String tempId = scan.nextLine();
        long id = Long.parseLong(tempId);
        Optional<DaftarBelanja> optDB = repo.findById(id);
        System.out.println("Data yang ingin dihapus : ");
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("Judul : " + db.getJudul());
            List<DaftarBelanjaDetil> listDbd = db.getDaftarBarang();
            System.out.println("DetailDaftarBelanja  : ");
            for (DaftarBelanjaDetil dbd : listDbd) {
                System.out.println("\t Nama Barang : " + dbd.getNamaBarang() + "\n\t Jumlah : " +
                        dbd.getByk() + "\n\t Satuan : " + dbd.getSatuan());
            }
            System.out.println("Yakin ingin menghapus?(y/n)");
            String del = scan.nextLine();
            if (del.equals("y")) {
                repo.deleteById(id);
                System.out.println("Penghapusan Berhasil");
            }
        }
            else {
                System.out.println("\tData tidak ditemukan.");
             }
        }
    }
