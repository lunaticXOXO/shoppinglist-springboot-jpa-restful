/*
 * ShoppingListCtrl.java
 *
 * Created on Mar 22, 2021, 01.12
 */
package shoppinglist.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaRepo;
import shoppinglist.service.ShoppingListService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author irfin
 */
@RestController
public class ShoppingListCtrl {
    @Autowired
    private ShoppingListService service;
    private DaftarBelanjaRepo repo;
    /**
     * Mengembalikan daftar objek DaftarBelanja utk pengaksesan HTTP GET.
     *
     * @return
     */
    @GetMapping
    public Iterable<DaftarBelanja> getAll()
    {
        return service.getAllData();
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody ShoppingDataCreateDto json) {
        // Ubah data yg terkandung dlm JSON ke dalam objek yg bisa diterima oleh
        // Service.
        DaftarBelanja entity = new DaftarBelanja();
        entity.setJudul(json.getJudul());

        // Ubah java.util.Date ke LocalDateTime
        LocalDateTime tglLocalDateTime = json.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        entity.setTanggal(tglLocalDateTime);

        List<ShoppingDataCreateDto.DataBarang> listDataBarang = json.getListbarang();
        DaftarBelanjaDetil[] arrDetilBelanja = new DaftarBelanjaDetil[listDataBarang.size()];

        for (int i = 0; i < listDataBarang.size(); i++) {
            arrDetilBelanja[i] = new DaftarBelanjaDetil();
            arrDetilBelanja[i].setByk(listDataBarang.get(i).getByk());
            arrDetilBelanja[i].setMemo(listDataBarang.get(i).getMemo());
            arrDetilBelanja[i].setNamaBarang(listDataBarang.get(i).getNama());
            arrDetilBelanja[i].setSatuan(listDataBarang.get(i).getSatuan());
        }

        if (service.create(entity, arrDetilBelanja))
            return ResponseEntity.ok("Data tersimpan dengan ID: " + entity.getId());
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data gagal tersimpan");
    }

    //versi postman
    //Mencari daftar belanja berdasarkan judul
    public ResponseEntity<List<DaftarBelanja>> findJudul(@RequestParam String title){
        try{

            List<DaftarBelanja> db = new ArrayList<>();
            repo.findbyTitle(title).forEach(db :: add);
            if(db.isEmpty()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            return  new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Insert Daftar Belanja
    @PostMapping("shoppinglist")
    public ResponseEntity<DaftarBelanja> insertDaftarBelanja(@RequestBody DaftarBelanja db){
        try{
            DaftarBelanja db2 = repo.save(new DaftarBelanja(db.getJudul(),db.getTanggal(),db.getDaftarBarang()));
            return new ResponseEntity<>(db2,HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Daftar Belanja

    public ResponseEntity<DaftarBelanja> updateDaftarBelanja(@PathVariable("id") Long id,@RequestBody DaftarBelanja db){

        Optional<DaftarBelanja> dbData = repo.findById(id);
        if(dbData.isPresent()){
            DaftarBelanja db2 = dbData.get();
            db2.setJudul(db.getJudul());
            db.setTanggal(db.getTanggal());
            return new ResponseEntity<>(repo.save(db2),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete Daftar Belanja

    public ResponseEntity<DaftarBelanja> DeleteDaftarBelanja(@PathVariable("id") Long id,@RequestBody DaftarBelanja db){
        try{
            repo.deleteById(id);
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
